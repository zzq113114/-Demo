$package("phis.script")
$import("app.desktop.Module", "util.helper.Helper")

phis.script.MySimplePrint = function(cfg) {
	this.printurl = util.helper.Helper.getUrl();
	this.reportName = null;// 报表名字
	this.urlCondition = '';// url后面的参数
	phis.script.MySimplePrint.superclass.constructor.apply(this, [cfg])
}
Ext.extend(phis.script.MySimplePrint, app.desktop.Module, {
	initPanel : function() {
		var printForm = new Ext.FormPanel({
					frame : true,
					labelWidth : 75,
					defaults : {
						width : '95%'
					},
					shadow : true,
					items : []
				})
		return printForm
	},
	printPreview : function(btn) {
		var page = this.printPage.getValue()
		var printWin;
		var type = 0
		var justPrint = false;
		if (btn && btn.iconCls) {
			if (btn.iconCls == "print" || btn.iconCls == "printView") {
				type = 1
				if (btn.iconCls == "print") {
					justPrint = true;
				}
			} else if (btn.iconCls == "doc") {
				type = 2
			} else if (btn.iconCls == "excel") {
				type = 3
			} else if (btn.iconCls == "pdf") {
				type = 4
			}
		}
		if (!this.reportName || this.reportName == null) {
			return;
		}
		var url = "resources/phis.prints.jrxml." + this.reportName
				+ ".print?silentPrint=1&temp=" + new Date().getTime()
				+ this.urlCondition;
		if (type == 1) {
			v var LODOP = getLodop();
			LODOP.PRINT_INIT("打印控件");
			LODOP.SET_PRINT_PAGESIZE("0", "", "", "");
			var rehtm = util.rmi.loadXML({
						url : url,
						httpMethod : "get"
					})
			rehtm = rehtm.replace(/table style=\"/g,
					"table style=\"page-break-after:always;");
			// 去掉打印body的边框
			rehtm = rehtm.replace("<body", "<body style='margin: 0'")
			LODOP.ADD_PRINT_HTM("0", "0", "100%", "100%", rehtm);
			LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", "Full-Width");
			if(!justPrint){
			// 预览
			LODOP.PREVIEW();
			}else{
			//直接打印
			LODOP.PRINT();
			}
		} else {
			printWin = window
					.open(
							url,
							"",
							"height="
									+ (screen.height - 100)
									+ ", width="
									+ (screen.width - 10)
									+ ", top=0, left=0, toolbar=no, menubar=yes, scrollbars=yes, resizable=yes,location=no, status=no")
		}
		printWin.onafterprint = function() {
			printWin.close();
		};
	},
	getWin : function() {
		var win = this.win
		if (!win) {
			win = new Ext.Window({
						title : "打印设置",
						width : 360,
						closeAction : "hide",
						items : this.initPanel(),
						modal : true,// add by liyl 2012-05-30
						constrainHeader : true,// add by liyl
						// 2012-06-17
						tbar : [{
									text : "直接打印",
									iconCls : "print",
									handler : this.printPreview,
									scope : this
								}, {
									text : "打印预览",
									iconCls : "printView",
									handler : this.printPreview,
									scope : this
								}, {
									text : "WORD",
									iconCls : "doc",
									handler : this.printPreview,
									scope : this
								}, {
									text : "PDF",
									iconCls : "pdf",
									handler : this.printPreview,
									scope : this
								}, {
									text : "EXCEL",
									iconCls : "excel",
									handler : this.printPreview,
									scope : this
								}, {
									text : "关闭",
									iconCls : "common_cancel",
									handler : function() {
										win.hide();
									},
									scope : this
								}]
					})
			var renderToEl = this.getRenderToEl()
			if (renderToEl) {
				win.render(renderToEl)
			}
			win.on("add", function() {
						this.win.doLayout()
					}, this)
			this.win = win
		}
		return win
	}
})