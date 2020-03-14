$package("phis.application.eg.script")
$import("phis.script.SimpleList")

/**
 *
 * @class phis.application.eg.script.MultiRightBottomList
 * @extends phis.script.SimpleList
 */
phis.application.eg.script.HBoxLayoutModule = function(cfg) {

    phis.application.eg.script.HBoxLayoutModule.superclass.constructor
        .apply(this, [cfg])
}

Ext.extend(phis.application.eg.script.HBoxLayoutModule,
    phis.script.SimpleList, {

    })
