$package("phis.application.eg.script")
$import("phis.script.SimpleModule")
/*
 * $import("util.dictionary.TreeDicFactory",
 * "phis.script.layout.BorderLayoutEx")
 */

/**
 * 不同面板之间参数传递及值的获取
 * 
 * @class phis.application.eg.script.ComplexModule
 * @extends phis.script.SimpleModule
 */
phis.application.eg.script.ComplexModule = function(cfg) {

	phis.application.eg.script.ComplexModule.superclass.constructor.apply(this,
			[cfg])
}

Ext.extend(phis.application.eg.script.ComplexModule, phis.script.SimpleModule,
		{
			/**
			 * 实现initPanel，构建界面
			 */
			initPanel : function() {
				if (this.panel)
					return this.panel;

				var panel = new Ext.Panel({
							layout : "hbox",
							frame : true,
							items : [{
										flex : 1, // 占比
										layout : "fit",
										items : [this.getForm_1()]
									}, {
										flex : 1, // 占比
										layout : "fit",
										items : [this.getForm_2()]
									}]
						});
				this.panel = panel;
				return panel;
			},
			getForm_1 : function() {
				var m = this.createModule("refForm_1", this.refForm_1);
				m.opener = this; // 此处把当前对象赋值到form1的opener变量上
				this.form_1 = m; // 对象设置到this中，方便其它方法中获取此form对象
				return m.initPanel();
			},
			getForm_2 : function() {
				var m = this.createModule("refForm_2", this.refForm_2);
				m.opener = this; // 此处把当前对象赋值到form2的opener变量上
				this.form_2 = m; // 对象设置到this中，方便其它方法中获取此form对象
				return m.initPanel();
			}

		})