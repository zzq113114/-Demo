$package("phis.application.cnd.script")
$import("phis.script.SimpleList")
/**
 * cnd使用说明
 */
phis.application.cnd.script.CndListDemo_2 = function(cfg) {
	
	phis.application.cnd.script.CndListDemo_2.superclass.constructor.apply(
			this, [cfg])
}
/*
 * 说明 this.initCnd 为初始条件
 */
Ext.extend(phis.application.cnd.script.CndListDemo_2, phis.script.SimpleList, {
	/**
	 * 扩展配置信息,增减建档日期选择
	 * 
	 * @param {}
	 *            cfg
	 */
	expansion : function(cfg) {
		var label = new Ext.form.Label({
					text : "建档日期："
				});
		var date = new Ext.form.DateField({
					format : "Y-m-d",
					value : new Date()
				});
		this.jdrq = date;
		cfg.tbar.push(['-', label, '-', date]);
	},
	/**
	 * 观察后台sql语句，可以看到条件已经增加
	 * 
	 */
	doBRXZ : function() {
		// 具体性质根据实际情况设置，此处只是举例说明
		this.requestData.cnd = ['eq', ['$', 'BRXZ'], ['i', 6110]];
		this.loadData();
	},
	/**
	 * 设置requestData的cnd条件时同时设置到initCnd,
	 */
	doCSNY : function() {
		this.initCnd = this.requestData.cnd = ['gt', ['$', 'CSNY'],
				['todate', ['s', '2015-01-01'], ['s', 'yyyy-mm-dd']]];
		this.loadData();
	},
	doReset : function() {
		this.initCnd = null;
		this.requestData.cnd = null;
		this.loadData();
	},
	
	/*//重写doCndQuery,额外增加JDSJ的动态过滤条件
	 doCndQuery : function() {
		// 组装initCnd
		this.initCnd = [
				'and',
				['eq', ['$', 'BRXZ'], ['i', 6110]],
				[
						'gt',
						['$', 'JDSJ'],
						['todate', ['s', this.jdrq.getRawValue()],
								['s', 'yyyy-mm-dd']]]]
		// 调用父类的doCndQuery方法
		phis.application.cnd.script.CndListDemo_2.superclass.doCndQuery
				.call(this);
	},
	*/
	/**
	 * *************以下为配置说明信息，与Cnd查询功能实现无关***********************
	 */
	initPanel : function() {
		var grid = phis.application.cnd.script.CndListDemo_2.superclass.initPanel
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
								width : 300,
								items : [this.getDescPanel()]
							}]
				})
		return panel;
	},
	getDescPanel : function() {
		var panel = new Ext.Panel({
			title : "用法说明",
			html : "<p style='font-size:14px;'>"
					+ "<font color=red style='font-size:16px;'>病人性质过滤：</font>"
					+ "显示指定病人性质的信息，通过设置this.requestData.cnd=['eq', ['$', 'BRXZ'], ['i', 6110]]实现，但是点击【放大镜】进行过滤查询时，不包含该条件 "
					+ "<br />"
					+ "<font color=red style='font-size:16px;'>出身年月过滤：</font>"
					+ "通过设置this.initCnd = this.requestData.cnd = ['gt', ['$', 'CSNY'],['todate', ['s', '2015-01-01'], ['s', 'yyyy-mm-dd']]]实现，在点击【放大镜】进行过滤查询时，任然含该条件 "
					+ "<br />"
					+ "<font color=red style='font-size:16px;'>PS:</font>点击【放大镜】查询调用了方法doCndQuery进行查询，根据cndField的值和initCnd组合查询，若要额外增加查询条件，如本例中增加建档日期的查询条件，可重写doCndQuery实现"
					+ "<br />" + "</p>"

		})
		return panel;
	}

})