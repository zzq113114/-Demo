$package("phis.application.eg.script")
$import("phis.script.SimpleModule")

/**
 * Border布局是日常开发中使用频率最高的一种布局方式，基本上的界面都可以通过Border布局构建，且自适应性好。
 * 
 * @class phis.application.eg.script.BorderLayoutModule
 * @extends phis.script.SimpleModule
 */
phis.application.eg.script.BorderLayoutModule = function(cfg) {

	phis.application.eg.script.BorderLayoutModule.superclass.constructor.apply(
			this, [cfg])
}

Ext.extend(phis.application.eg.script.BorderLayoutModule,
		phis.script.SimpleModule, {
			/**
			 * 实现initPanel，构建界面
			 */
			initPanel : function() {
				if (this.panel)
					return this.panel;

				var panel = new Ext.Panel({
							title : 'Border布局展示',
							layout : "border",
							items : [this.getNorth(), this.getSouth(),
									this.getCenter(), this.getEast(),
									this.getWest()]
						});
				this.panel = panel;
				return panel;
			},
			getNorth : function() {
				return new Ext.Panel({
							layout : "fit",
							region : "north",
							height : 100, // region为north或south时，需要指定height高度，宽度指定无效，自适应容器大小
							html : "我是north部分"
						});
			},
			getSouth : function() {
				return new Ext.Panel({
							layout : "fit",
							region : "south",
							height : 100,
							html : "我是south部分"
						});
			},
			getCenter : function() {
				return new Ext.Panel({
							layout : "fit",
							region : "center", // 必须指定center部分，否则会提示错误，center指定width和height无效，自动占满容器剩余部分
							html : "我是center部分"
						});
			},
			getEast : function() {
				return new Ext.Panel({
							layout : "fit",
							region : "east",// region为east或west时，需要指定width宽度，高度指定无效，自适应容器大小
							width : 100,
							html : "我是east部分"
						});
			},
			getWest : function() {
				return new Ext.Panel({
							layout : "fit",
							region : "west",
							width : 100,
							html : "我是west部分"
						});
			}

		})