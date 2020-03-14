$package("phis.script")
$import("util.dictionary.TreeCheckDicFactory")
phis.script.PrintWin = function(cfg) {
	phis.script.PrintWin.superclass.constructor.apply(this, [cfg])
}
Ext.extend(phis.script.PrintWin, app.desktop.Module, {
	print : function() {
		var w = this.printPreview({
			iconCls : "print"
		})
		if(Ext.isIE){
			w.print()
		}else{
			w.onload=function(){
				w.print()
			}
		}		
	},
	getSelectedPage : function() {
		var nodes = this.win.items.itemAt(0).getChecked()
		var pages = []
		for (var i = 0; i < nodes.length; i++) {
			var n = nodes[i]
			pages.push(n.attributes.print)
		}
		return pages
	},
	printPreview : function(btn) {
		var type = 0
		if (btn && btn.iconCls) {
			if (btn.iconCls == "print") {
				type = 1
			} else if (btn.iconCls == "doc") {
				type = 2
			} else if (btn.iconCls == "excel") {
				type = 3
			}
		}
		var pages = this.getSelectedPage()
		var ids_str = ""
		for(var key in this.ids){
			ids_str += ("&"+key+"="+this.ids[key])
		}
		var url = "print?type=" + type + ids_str + "&pages=" + pages
		var printWin = window
				.open(
						url,
						"",
						"height="
								+ (screen.height - 100)
								+ ", width="
								+ (screen.width - 10)
								+ ", top=0, left=0, toolbar=no, menubar=yes, scrollbars=yes, resizable=yes,location=no, status=no")
		return printWin
	},
	getWin : function() {
		var win = this.win
		if (!win) {
			var ehrNavTree = util.dictionary.TreeCheckDicFactory.createTree({
				id : "ehrViewPrint",
				onlyLeafCheckable : true
			})
			var win = new Ext.Window({
				title : "ѡ����Ҫ��ӡ�ĵ���",
				layout : "fit",
				width : 360,
				height : 360,
				closeAction : "hide",
				items : ehrNavTree,
				tbar : [
				{
						text : "��ӡ",
						iconCls : "print",
						handler : this.print,
						scope : this
				}, 
					{
					text : "��ӡԤ��",
					iconCls : "print",
					handler : this.printPreview,
					scope : this
				}, {
					text : "WORD",
					iconCls : "doc",
					handler : this.printPreview,
					scope : this
				}, {
					text : "PDF",
					iconCls : "pdf",
					handler : this.printPreview,
					scope : this
				}, {
					text : "EXCEL",
					iconCls : "excel",
					handler : this.printPreview,
					scope : this
				}]
			})
			var renderToEl = this.getRenderToEl()
			if (renderToEl) {
				win.render(renderToEl)
			}
			win.on("add", function() {
						this.win.doLayout()
					}, this)
			this.win = win
		}
		return win
	}
})