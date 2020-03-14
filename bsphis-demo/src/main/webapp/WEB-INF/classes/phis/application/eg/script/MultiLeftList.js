$package("phis.application.eg.script")
$import("phis.script.SimpleList")

/**
 * 
 * @class phis.application.eg.script.MultiLeftList
 * @extends phis.script.SimpleList
 */
phis.application.eg.script.MultiLeftList = function(cfg) {

	phis.application.eg.script.MultiLeftList.superclass.constructor.apply(this,
			[cfg])
}

Ext.extend(phis.application.eg.script.MultiLeftList, phis.script.SimpleList, {
			// 双击默认调用方法
			onDblClick : function(grid, index) {
				var r = this.getSelectedRecord();
				this.fireEvent("loadDetails", r.get("BRID"));
			}
		})