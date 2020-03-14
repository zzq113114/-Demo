$package("phis.application.evt.script")
$import("phis.script.SimpleModule")
phis.application.evt.script.EventDesc = function(cfg) {
	phis.application.evt.script.EventDesc.superclass.constructor.apply(this,
			[cfg])
}
/**
 * cnd使用说明
 * 
 * @class phis.application.evt.script.EventDesc
 * @extends phis.script.SimpleModule
 */
Ext.extend(phis.application.evt.script.EventDesc, phis.script.SimpleModule, {
	initPanel : function() {
		if (this.panel) {
			return this.panel;
		}
		var panel = new Ext.Panel({
			title : this.name,
			autoScroll : true,
			html : '<IFRAME id="cndDesc"  name="cndDesc" width="100%" height="100%" src="html/event.html" frameborder=0  ></IFRAME>'
		})
		this.panel = panel;
		return panel;
	}
})