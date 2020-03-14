$package("phis.script")

$import("phis.script.SimpleModule", "phis.script.rmi.jsonRequest",
		"phis.script.rmi.miniJsonRequestSync",
		"phis.script.rmi.miniJsonRequestAsync")

phis.script.SimpleReport = function(cfg) {
	this.pageId = cfg.pageId; // 报表id

	phis.script.SimpleReport.superclass.constructor.apply(this, [cfg]);
}
Ext.extend(phis.script.SimpleReport, phis.script.SimpleModule, {
	initPanel : function() {
		if (this.panel)
			return this.panel;
		var panel = new Ext.Panel({
					html : '<iframe id="frame" name="frame" frameborder="0" src="ShowReport.wx?PAGEID='
							+ this.pageId + '" width=100% height=100%/>',
					iframe : false,
					border : false,
					bodyBorder : false
				});
		this.panel = panel;
		return panel;
	}

});
