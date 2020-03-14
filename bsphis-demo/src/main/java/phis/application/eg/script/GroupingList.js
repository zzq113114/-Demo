$package("phis.application.eg.script")
$import("phis.script.SimpleList")

/**
 * 分组列表使用说明
 * 列表数据需要分类展示是使用
 * 
 * @class phis.application.eg.script.GroupingList
 * @extends phis.script.SimpleList
 */
phis.application.eg.script.GroupingList = function(cfg) {
	
	/*
	 * 1：设置需要分组的字段
	 * 2：设置分组信息内容
	 */
	cfg.group = 'MZHM'; // 使用固定表头标志，设置为true
	
	cfg.groupTextTpl = '{text} ({[values.rs.length]} 条记录)'; // 默认值

	phis.application.eg.script.GroupingList.superclass.constructor.apply(this,
			[cfg])
}

Ext.extend(phis.application.eg.script.GroupingList, phis.script.SimpleList, {

})