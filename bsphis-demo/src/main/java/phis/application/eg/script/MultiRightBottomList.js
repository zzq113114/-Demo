$package("phis.application.eg.script")
$import("phis.script.SimpleList")

/**
 * 
 * @class phis.application.eg.script.MultiRightBottomList
 * @extends phis.script.SimpleList
 */
phis.application.eg.script.MultiRightBottomList = function(cfg) {

	phis.application.eg.script.MultiRightBottomList.superclass.constructor
			.apply(this, [cfg])
}

Ext.extend(phis.application.eg.script.MultiRightBottomList,
		phis.script.SimpleList, {

		})