$package("phis.application.eg.script")
$import("phis.script.TableForm")
/*
 * $import("util.dictionary.TreeDicFactory",
 * "phis.script.layout.BorderLayoutEx")
 */

/**
 * 不同面板之间参数传递及值的获取
 * 
 * @class phis.application.eg.script.ComplexForm_2
 * @extends phis.script.TableForm
 */
phis.application.eg.script.ComplexForm_2 = function(cfg) {

	phis.application.eg.script.ComplexForm_2.superclass.constructor.apply(this,
			[cfg])
}

Ext.extend(phis.application.eg.script.ComplexForm_2, phis.script.TableForm, {
			/**
			 * form_2中给form_1的字段赋值
			 */
			doSetValue_1 : function() {
				// this.opener 获取上层容器对象
				var f1_brxm = this.opener.form_1.form.getForm()
						.findField("BRXM"); // 设置form_1中的病人姓名为张三
				f1_brxm.setValue("张三");
			}
		})