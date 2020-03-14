$package("phis.application.cnd.script")
$import("phis.script.SimpleModule")
phis.application.cnd.script.CndDesc = function(cfg) {
	phis.application.cnd.script.CndDesc.superclass.constructor.apply(this,
			[cfg])
}
/**
 * cnd使用说明
 * 
 * @class phis.application.cnd.script.CndDesc
 * @extends phis.script.SimpleModule
 */
Ext.extend(phis.application.cnd.script.CndDesc, phis.script.SimpleModule, {
	initPanel : function() {
		if (this.panel) {
			return this.panel;
		}
		var panel = new Ext.Panel({
			title : this.name,
			autoScroll : true,
			html : '<IFRAME id="cndDesc"  name="cndDesc" width="100%" src="html/cnd.html" frameborder=0 onload="this.height=cndDesc.document.body.scrollHeight" ></IFRAME>'
		})
		this.panel = panel;
		return panel;
	}
})