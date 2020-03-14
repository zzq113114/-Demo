$package("phis.application.eg.script")
/*
 * 引入扩展包ColumnHeaderGroup
 */
$import("phis.script.SimpleList", "org.ext.ux.ColumnHeaderGroup")

/**
 * 多表头列表 需要显示二级标题的场景
 * 
 * @class phis.application.eg.script.HeaderGroupList
 * @extends phis.script.SimpleList
 */
phis.application.eg.script.HeaderGroupList = function(cfg) {

	phis.application.eg.script.HeaderGroupList.superclass.constructor.apply(
			this, [cfg])
}

Ext.extend(phis.application.eg.script.HeaderGroupList, phis.script.SimpleList,
		{

			/**
			 * 父类的list配置信息扩展函数，个性化的配置可以通过此方法设置
			 * 
			 * @param cfg
			 *            配置信息
			 */
			expansion : function(cfg) {
				var groupHeader = new Ext.ux.grid.ColumnHeaderGroup({
							rows : [[{
										header : " ",// 序号列
										colspan : 1,
										align : 'center'
									}, {
										header : "个人基本信息",
										colspan : 5, // 需要合并显示表头的列数
										align : 'center'
									}, {
										header : "建档信息",
										colspan : 3,
										align : 'center'
									}]]
						});
				cfg.plugins = groupHeader;
			}
		})