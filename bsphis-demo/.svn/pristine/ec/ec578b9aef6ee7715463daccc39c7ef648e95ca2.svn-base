$package("phis.application.cnd.script")
$import("phis.script.SimpleList", "phis.script.ux.html2image")
/**
 * cnd使用说明
 */
phis.application.cnd.script.CndListDemo_1 = function(cfg) {
	/*
	 * 一、查询所有男性病人
	 */
	this.initCnd = ['eq', ['$', 'BRXB'], ['i', 1]]
	/*
	 * 二、查询当前机构下的所有男性病人
	 */
	// this.initCnd = ['and', ['eq', ['$', 'BRXB'], ['i', 1]],
	// ['eq', ['$', 'JDJG'], ['$', '%user.manageUnit.id']]]
	phis.application.cnd.script.CndListDemo_1.superclass.constructor.apply(
			this, [cfg])
}

Ext.extend(phis.application.cnd.script.CndListDemo_1, phis.script.SimpleList, {
	/**
	 * 测试js截屏
	 */
	doScreen : function() {
		this.oplog("demo", "业务日志快照", true);
	},
	doLogonOut : function() {
		var re = util.rmi.miniJsonRequestSync({
					url : "logon/logonOff"
				});
	},
	doAsync : function() {
		this.mainApp.desktop.mainTab.el.mask("加载中...", "x-mask-loading")
		phis.script.rmi.jsonRequest({
					serviceId : "phis.publicService",
					serviceAction : "getServerDate"
				}, function(code, msg, json) {
					this.mainApp.desktop.mainTab.el.unmask();
					if (code > 200) {

					}
				}, this);
	},
	doSync : function() {
		var res = phis.script.rmi.miniJsonRequestSync({
					serviceId : "publicService",
					serviceAction : "getServerDate"
				});
		var code = res.code;
		var msg = res.msg;
		if (code >= 300) {
			this.processReturnMsg(code, msg);
			return;
		}
	},
	initPanel : function() {
		var grid = phis.application.cnd.script.CndListDemo_1.superclass.initPanel
				.call(this);
		var panel = new Ext.Panel({
					frame : false,
					layout : 'border',
					// title : this.name,
					items : [{
								layout : "fit",
								border : false,
								region : 'center',
								items : [grid]
							}, {
								layout : "fit",
								border : false,
								region : 'east',
								width : 600,
								items : [this.getDescPanel()]
							}]
				})
		return panel;
	},
	getDescPanel : function() {
		var panel = new Ext.Panel({
			title : "用法说明",
			html : "<p style='font-size:14px;'>"
					+ "<font color=red style='font-size:16px;'>initCnd:</font>"
					+ "初始条件设置,默认设置条件为['eq', ['$', 'BRXB'], ['i', 1]]，即查询所有性别为男的病人信息."
					+ "<br />"
					+ "通过initCnd设置的条件，通过框架自带的查询方法查询时，仍然会加上initCnd条件."
					+ "</p><img id='myscreen'>123</img>"

		})
		return panel;
	}
})