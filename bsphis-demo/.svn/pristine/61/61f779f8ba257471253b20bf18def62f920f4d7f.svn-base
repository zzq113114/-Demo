$package("phis.application.eg.script")
$import("phis.script.TableForm")
/*
 * $import("util.dictionary.TreeDicFactory",
 * "phis.script.layout.BorderLayoutEx")
 */

/**
 * 不同面板之间参数传递及值的获取
 * 
 * @class phis.application.eg.script.ComplexForm_1
 * @extends phis.script.TableForm
 */
phis.application.eg.script.ComplexForm_1 = function(cfg) {

	phis.application.eg.script.ComplexForm_1.superclass.constructor.apply(this,
			[cfg])
}

Ext.extend(phis.application.eg.script.ComplexForm_1, phis.script.TableForm, {
			/*
			 * 获取表单二的内容
			 * 根据传入的父类对象获取form_2对象
			 */ 
			doGetValue_2 : function() {
				// this.opener 获取上层容器对象
				var f2_mzhm = this.opener.form_2.form.getForm()
						.findField("MZHM"); // 获取form_2的门诊号码字段
				Ext.Msg.alert("提示", "表单二中的门诊号码:" + f2_mzhm.getValue())
			}

		})