$package("phis.application.eg.script")
$import("phis.script.SimpleList")

/**
 * 如何打开一个弹出窗口，并与窗口交互
 * 
 * @class phis.application.eg.script.ShowWinList
 * @extends phis.script.SimpleList
 */
phis.application.eg.script.ShowWinList = function(cfg) {

	phis.application.eg.script.ShowWinList.superclass.constructor.apply(this,
			[cfg])
}

Ext.extend(phis.application.eg.script.ShowWinList, phis.script.SimpleList, {
			doShowDetails : function() {
				var m = this.createModule("detailsForm", this.refForm);
				if (!this.detailsFormWin) {
					m.on("winclose", this.onWinClose, this); // 监听事件，注意事件不能重复监听
					var win = m.getWin();// getWin对象在父类已定义
					win.add(m.initPanel());
					this.detailsFormWin = win; // 缓存到当前对象
				}
				var r = this.getSelectedRecord(); // 列表获取选中列
				if (!r) {
					MyMessageTip.msg("提示", "请先选中需要操作的记录！", true);
					return;
				}
				m.brid = r.get("BRID"); // 传值到form对象的属性
				this.detailsFormWin.show();
			},
			onWinClose : function(param) {
				Ext.Msg.alert("提示", "窗口关闭时返回的内容:" + param)
			}
		})