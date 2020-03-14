package test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ctd.print.IHandler;
import ctd.print.PrintException;
import ctd.util.context.Context;

public class TestPrintBean implements IHandler {

	@Override
	public void getParameters(Map<String, Object> request, Map<String, Object> response, Context ctx)
			throws PrintException {
		response.put("parameter1", "my title");

	}

	@Override
	public void getFields(Map<String, Object> request, List<Map<String, Object>> records, Context ctx)
			throws PrintException {
		for(int i=0;i<10;i++){
			Map<String, Object> d = new HashMap<String, Object>();
			d.put("field1", "aaa");
			records.add(d);
		}
	}

}
