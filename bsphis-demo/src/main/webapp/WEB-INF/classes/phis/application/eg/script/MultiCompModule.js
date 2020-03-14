$package("phis.application.eg.script")
$import("phis.script.SimpleModule")
/*
 * $import("util.dictionary.TreeDicFactory",
 * "phis.script.layout.BorderLayoutEx")
 */

/**
 * 不同面板之间参数传递及值的获取
 * 
 * @class phis.application.eg.script.MultiCompModule
 * @extends phis.script.SimpleModule
 */
phis.application.eg.script.MultiCompModule = function(cfg) {

	phis.application.eg.script.MultiCompModule.superclass.constructor.apply(
			this, [cfg])
}

Ext.extend(phis.application.eg.script.MultiCompModule,
		phis.script.SimpleModule, {
			/**
			 * 复杂界面,使用hbox和vbox组合构建
			 */
			initPanel : function() {
				if (this.panel)
					return this.panel;

				var panel = new Ext.Panel({
							layout : {
								type : 'hbox',
								align : 'stretch'
							},
							defaults : {
								layout : 'fit' // 设置items中组件默认值
							},
							items : [{
										title : "<font color=red>双击左边列表数据</font>",
										flex : 1, // 占比
										items : [this.getLeftList()]
									}, {
										flex : 2, // 占比
										items : [{
											layout : {
												type : 'vbox',
												align : 'stretch'
											},
											defaults : {
												layout : 'fit' // 设置items中组件默认值
											},
											items : [{
														hegith : 140,
														items : this
																.getRightForm()
													}, {
														flex : 1,
														items : this
																.getRightList()
													}]
										}]
									}]
						});
				this.panel = panel;
				return panel;
			},
			getLeftList : function() {
				var m = this.createModule("refLeftList", this.refLeftList);
				m.on("loadDetails", this.loadDetails, this);
				return m.initPanel();
			},
			getRightForm : function() {
				var m = this.createModule("refRightTopForm",
						this.refRightTopForm);
				this.rightForm = m;
				return m.initPanel();
			},
			getRightList : function() {
				var m = this.createModule("refRightBottomList",
						this.refRightBottomList);
				this.rightList = m;
				return m.initPanel();
			},
			loadDetails : function(brid) {
				// 右上form加载数据
				this.rightForm.initDataId = brid; // 设置主键,form表单根据主键加载数据
				this.rightForm.loadData()

				// 右下list加载数据
				this.rightList.requestData.cnd = ['eq', ['$', 'BRID'],
						['l', brid]] // 设置查询条件
				this.rightList.loadData();
			}

		})