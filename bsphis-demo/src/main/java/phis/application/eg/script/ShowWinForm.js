$package("phis.application.eg.script")
$import("phis.script.TableForm")

/**
 * 如何打开一个弹出窗口，并与窗口交互
 * 
 * @class phis.application.eg.script.ShowWinForm
 * @extends phis.script.TableForm
 */
phis.application.eg.script.ShowWinForm = function(cfg) {

	phis.application.eg.script.ShowWinForm.superclass.constructor.apply(this,
			[cfg])

	this.on("winShow", this.onWinShow, this)
}

Ext.extend(phis.application.eg.script.ShowWinForm, phis.script.TableForm, {
			/*
			 * 每次打开窗口时执行的函数，事件注册方式
			 */
			onWinShow : function() {
				this.initDataId = this.brid; // brid为父类传递的参数
				this.loadData();
			},
			// 实现关闭按钮功能
			doClose : function() {
				this.fireEvent("winclose", new Date().format('Y-m-d H:i:s'));// 返回当前时间为示例，模拟结果数据返回
				this.win.hide();
			}
		})