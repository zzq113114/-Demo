package phis.source.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.xml.sax.SAXException;

import ctd.util.xml.XMLHelper;
import freemarker.ext.dom.NodeModel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class PDMToSchema {
	private static Log LOGGER = LogFactory.getLog(PDMToSchema.class);
	private static String pdmFilename;
	private static String schemaHome;
	private static String mappingHome;
	private static Map<String, String> types = new HashMap<String, String>();

	static {
		types.put("varchar", "string");
		types.put("char", "string");
		types.put("text", "string");
		types.put("int", "int");
		types.put("double", "double");
		types.put("datetime", "timestamp");
		types.put("date", "date");
		types.put("decimal", "bigDecimal");
	}

	public static void create(String pdmFileName) throws DocumentException,
			IOException {
		File file = new File(pdmFileName);
		if (!file.exists()) {
			LOGGER.error("pdm[" + pdmFileName + "]  not exists.");
			return;
		}
		pdmFilename = pdmFileName;
		schemaHome = file.getParentFile().getAbsolutePath() + File.separator
				+ "output" + File.separator + "schema" + File.separator;
		mappingHome = file.getParentFile().getAbsolutePath() + File.separator
				+ "output" + File.separator + "mappings" + File.separator;
		// System.out.println("schemaHome:"+schemaHome);
		// System.out.println("mappingHome:"+mappingHome);
		new File(schemaHome).mkdirs();
		new File(mappingHome).mkdirs();
		start();
	}

	@SuppressWarnings("unchecked")
	private static void start() throws DocumentException, IOException {
		Document pdmDoc = XMLHelper.getDocument(pdmFilename);
		if (pdmDoc == null) {
			LOGGER.error("load xml from pdm falied:" + pdmFilename);
			return;
		}
		List<Element> tables = pdmDoc.selectNodes("//c:Tables/o:Table");
		for (Iterator<Element> it = tables.iterator(); it.hasNext();) {
			Element t = it.next();
			cvtTable(t);
		}
	}

	@SuppressWarnings("unchecked")
	private static void cvtTable(Element td) throws IOException {
		String entryName = td.elementText("Code");
		Document sc = XMLHelper.createDocument();
		Element root = sc.addElement("entry");
		root.addAttribute("entityName", entryName);
		root.addAttribute("alias", td.elementText("Name"));
		List<Element> cols = td.selectNodes("c:Columns/o:Column");
		for (Iterator<Element> it = cols.iterator(); it.hasNext();) {
			Element t = it.next();
			root.add(cvtCol(t));
		}
		Element pkey = (Element) td
				.selectSingleNode("c:Columns/o:Column[@Id=../../c:Keys/o:Key/c:Key.Columns/o:Column/@Ref]");
		if (pkey != null) {
			String id = pkey.elementText("Code");
			String generator = pkey.elementText("Identity") == null ? "assigned"
					: "auto";
			Element item = (Element) root.selectSingleNode("item[@id='" + id
					+ "']");
			if (item != null) {
				item.addAttribute("generator", generator);
				item.addAttribute("pkey", "true");
			}
		}
		// writeSchemaFile(sc,entryName);
		writeMappingFile(writeSchemaFile(sc, entryName), entryName);
		// System.out.println("write mapping file:"+entryName);
	}

	private static Element cvtCol(Element col) {
		Element c = DocumentHelper.createElement("item");
		String code = col.elementText("Code");
		c.addAttribute("id", code);
		c.addAttribute("alias", col.elementText("Name"));
		String type = col.elementText("DataType");
		String length = col.elementText("Length");
		String Precision = col.elementText("Precision");
		if (type != null) {
			type = type.replaceAll("\\(.+\\)", "");
			if (code.equals("JGID")) {
				c.addAttribute("type", "string");
				length = "20";
			} else if (type.equals("NUMBER")) {
				if (Precision != null && Precision.trim().length() > 0) {
					c.addAttribute("type", "double");
				} else {
					if (length != null && Integer.parseInt(length) > 10) {
						c.addAttribute("type", "long");
					} else {
						c.addAttribute("type", "int");
					}
				}
			} else {
				c.addAttribute("type", types.get(type.toLowerCase()));
			}
		}
		c.addAttribute("length", length);
		c.addAttribute("precision", Precision);
		c.addAttribute("not-null", col.elementText("Mandatory"));
		return c;
	}

	private static File writeSchemaFile(Document doc, String entryName)
			throws IOException {
		File file = new File(schemaHome + entryName + ".xml");
		XMLHelper.putDocument(file, doc);
		// System.out.println("write schema file:"+entryName);
		return file;
	}

	private static void writeMappingFile(File scf, String entryName) {
		// WebApplicationContext wac = AppContextHolder.get();
		Configuration cfg;
		try {
			HashMap<String, Object> data = new HashMap<String, Object>();
			cfg = new Configuration();
			// 放置test.fsl文件的路径
			cfg.setDirectoryForTemplateLoading(new File(
					"D:/workspace/BS-PHIS-2.4.20/src/main/webapp/WEB-INF/classes/component/ftl"));
			Template t = cfg.getTemplate("mappingMeaker.ftl");
			File file = new File(mappingHome + entryName + ".hbm.xml");
			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file)));
			try {
				NodeModel m = freemarker.ext.dom.NodeModel.parse(scf);
				data.put("doc", m);
				t.process(data, out);
			} catch (TemplateException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		schemaHome = "D:/output/schema";
		mappingHome = "D:/output/mapping";
		create("D:/SSMZ.pdm");
	}
}
