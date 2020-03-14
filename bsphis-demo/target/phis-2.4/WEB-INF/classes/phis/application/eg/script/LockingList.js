$package("phis.application.eg.script")
$import("phis.script.SimpleList")

/**
 * 固定表头使用说明
 * 一般用于列表中显示的字段内容很多，有横向滚动条时，将重要的信息固定在列表上，
 * 不随滚动条滚动，方便用户查看信息
 * 
 * @class phis.application.eg.script.LockingList
 * @extends phis.script.SimpleList
 */
phis.application.eg.script.LockingList = function(cfg) {
	
	/*
	 * 1：设置固定表头列表参数lockingGrid = true
	 * 2：将entryName中需要固定的item上添加locked属性并设置为true
	 */
	cfg.lockingGrid = true; // 使用固定表头标志，设置为true

	phis.application.eg.script.LockingList.superclass.constructor.apply(this,
			[cfg])
}

Ext.extend(phis.application.eg.script.LockingList, phis.script.SimpleList, {

})