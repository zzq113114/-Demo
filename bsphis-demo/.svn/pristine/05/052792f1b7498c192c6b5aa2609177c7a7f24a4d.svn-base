$package("phis.application.eg.script")
$import("phis.script.SimpleModule")
/*
 * $import("util.dictionary.TreeDicFactory",
 * "phis.script.layout.BorderLayoutEx")
 */

/**
 * 日常开发使用示例
 * 
 * @class phis.application.eg.script.BorderLayoutModule_1
 * @extends phis.script.SimpleModule
 */
phis.application.eg.script.BorderLayoutModule_1 = function(cfg) {

	phis.application.eg.script.BorderLayoutModule_1.superclass.constructor
			.apply(this, [cfg])
}

Ext.extend(phis.application.eg.script.BorderLayoutModule_1,
		phis.script.SimpleModule, {
			/**
			 * 实现initPanel，构建界面
			 */
			initPanel : function() {
				if (this.panel)
					return this.panel;

				var panel = new Ext.Panel({
							layout : "border", // borderex
							items : [{
										region : "north",
										height : 120,
										items : [this.getForm()]
									}, {
										layout : 'fit', // fit布局，自动占满整个区域
										region : "center",
										items : [this.getList()]
									}/*
										 * , { region : "west", width : 240,
										 * axis2top : true, frame : true, items :
										 * [this.getTree()] }
										 */]
						});
				this.panel = panel;
				return panel;
			},
			/*// 以下为获取机构字典树的方式
			getTree : function() { 
				var tf = util.dictionary.TreeDicFactory.createDic({
							id : "phis.@manageUnit",
							parentKey : this.mainApp.deptId,
							rootVisible : true
						});
				return tf.tree;
			},*/
			getForm : function() {
				var m = this.createModule("refForm", this.refForm);
				this.form = m; // 对象设置到this中，方便其它方法中操作form对象
				return m.initPanel();
			},
			getList : function() {
				var m = this.createModule("refList", this.refList);
				this.list = m; // 对象设置到this中，方便其它方法中操作list对象
				return m.initPanel();
			}

		})