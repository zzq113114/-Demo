$package("phis.application.eg.script")
$import("phis.script.SimpleList")

/**
 *
 * @class phis.application.eg.script.MultiRightBottomList
 * @extends phis.script.SimpleList
 */
phis.application.eg.script.CardLayoutModule = function(cfg) {

    phis.application.eg.script.CardLayoutModule.superclass.constructor
        .apply(this, [cfg])
}

Ext.extend(phis.application.eg.script.CardLayoutModule,
    phis.script.SimpleList, {

    })
