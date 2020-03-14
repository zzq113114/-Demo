<?xml version="1.0" encoding="UTF-8"?>
<application id="phis.application.eg.EG" name="常用示例(EG)">
	<catagory id="EG_1" name="表单示例">
		<module id="EG_1_1" name="普通表单"
			script="phis.script.SimpleForm">
			<properties>
				<p name="entryName">phis.application.eg.schemas.MS_BRDA</p>
			</properties>
			<action id="create" name="新建" />
			<action id="update" name="修改" />
		</module>
		<module id="EG_1_2" name="表格式表单"
			script="phis.script.TableForm">
			<properties>
				<p name="entryName">phis.application.eg.schemas.MS_BRDA</p>
				<p name="colCount">3</p><!-- 每行有几列 -->
			</properties>
			<action id="create" name="新建" />
			<action id="update" name="修改" />
		</module>
		<module id="EG_1_3" name="常用录入项"
			script="phis.script.TableForm">
			<properties>
				<p name="entryName">phis.application.eg.schemas.MS_BRDA_INPUT</p>
				<p name="colCount">2</p><!-- 每行有几列 -->
			</properties>
			<action id="create" name="新建" />
			<action id="save" name="保存" />
		</module>
	</catagory>
	<catagory id="EG_2" name="列表示例">
		<module id="EG_2_1" name="普通列表"
			script="phis.script.SimpleList">
			<properties>
				<p name="entryName">phis.application.eg.schemas.MS_BRDA</p>
			</properties>
            <action id="create" name="新建" />
            <action id="update" name="修改" />
		</module>

        <module id="EG_2_6" name="用例1列表"
                script="phis.application.eg.script.Cf01List">
            <properties>
                <p name="entryName">phis.application.eg.schemas.MS_CF01_JZLSDY</p>
            </properties>
            <action id="create" name="新建" />
            <action id="update" name="修改" />
            <action id="cs" name="测试" />
        </module>

		<!--扩展：分组列表 分组统计 多表头 固定列 等等 -->
		<module id="EG_2_2" name="分组列表"
			script="phis.application.eg.script.GroupingList">
			<properties>
				<p name="entryName">phis.application.eg.schemas.MS_BRDA_GROUP</p>
				<p name="group">MZHM</p><!--指定需要分组的字段-->
			</properties>
		</module>
		<module id="EG_2_3" name="统计列表"
			script="phis.script.SimpleList">
			<properties>
				<p name="entryName">phis.application.eg.schemas.MS_BRDA</p>
				<p name="group">MZHM</p><!--指定需要分组的字段-->
			</properties>
		</module>
		<module id="EG_2_4" name="固定列列表"
			script="phis.application.eg.script.LockingList">
			<properties>
				<p name="entryName">phis.application.eg.schemas.MS_BRDA_LOCK</p>
			</properties>
		</module>
		<module id="EG_2_5" name="多表头列表"
			script="phis.application.eg.script.HeaderGroupList">
			<properties>
				<p name="entryName">phis.application.eg.schemas.MS_BRDA</p>
			</properties>
		</module>
	</catagory>
	<catagory id="EG_3" name="复杂界面">
		<!--多组件组合的界面 -->
		<module id="EG_3_1" name="border布局"
			script="phis.application.eg.script.BorderLayoutModule">
		</module>
		<module id="EG_3_1_1" name="Border典型应用"
			script="phis.application.eg.script.BorderLayoutModule_1">
			<properties>
				<p name="refForm">phis.application.eg.EG/EG_1/EG_1_2</p>
				<p name="refList">phis.application.eg.EG/EG_2/EG_2_1</p>
			</properties>
		</module>
		<module id="EG_3_2" name="fit布局"
			script="phis.script.SimpleList">
			<properties>
				<p name="entryName">phis.application.eg.schemas.MS_BRDA</p>
			</properties>
		</module>
		<module id="EG_3_3" name="Accordion布局"
			script="phis.application.eg.script.AccordionLayoutModule">
		</module>
		<module id="EG_3_4" name="Card布局"
			script="phis.application.eg.script.CardLayoutModule">
		</module>
		<module id="EG_3_5" name="VBox布局"
			script="phis.application.eg.script.VBoxLayoutModule">
		</module>
		<module id="EG_3_6" name="HBox布局"
			script="phis.application.eg.script.HBoxLayoutModule">
		</module>
	</catagory>
	<catagory id="EG_4" name="自定义界面">
		<!--个性化的页面 -->
	</catagory>
	<catagory id="EG_5" name="多组件交互">
		<!--多组件交互-->
		<module id="EG_5_1" name="多组件相互调用"
			script="phis.application.eg.script.ComplexModule">
			<properties>
				<p name="refForm_1">phis.application.eg.EG/EG_5/EG_5_1_1</p>
				<p name="refForm_2">phis.application.eg.EG/EG_5/EG_5_1_2</p>
			</properties>
		</module>
		<module id="EG_5_1_1" name="表单1"
			script="phis.application.eg.script.ComplexForm_1" type="1">
			<properties>
				<p name="entryName">phis.application.eg.schemas.MS_BRDA</p>
				<p name="colCount">3</p><!-- 每行有几列 -->
			</properties>
			<action id="getValue_2" name="获取表单二内容" iconCls="save"/>
		</module>
		<module id="EG_5_1_2" name="表单2"
			script="phis.application.eg.script.ComplexForm_2" type="1">
			<properties>
				<p name="entryName">phis.application.eg.schemas.MS_BRDA</p>
				<p name="colCount">3</p><!-- 每行有几列 -->
			</properties>
			<action id="setValue_1" name="设置表单一姓名" iconCls="save"/>
		</module>
		<module id="EG_5_2" name="弹出窗交互"
			script="phis.application.eg.script.ShowWinList">
			<properties>
				<p name="entryName">phis.application.eg.schemas.MS_BRDA</p>
				<p name="refForm">phis.application.eg.EG/EG_5/EG_5_2_1</p>
			</properties>
			<action id="showDetails" name="详细信息" iconCls="create"/>
		</module>
		<module id="EG_5_2_1" name="详细信息" type="1"
			script="phis.application.eg.script.ShowWinForm">
			<properties>
				<p name="entryName">phis.application.eg.schemas.MS_BRDA</p>
				<p name="colCount">3</p><!-- 每行有几列 -->
			</properties>
			<action id="close" name="关闭" />
		</module>
		<module id="EG_5_3" name="多组件联动"
			script="phis.application.eg.script.MultiCompModule">
			<properties>
				<p name="refLeftList">phis.application.eg.EG/EG_5/EG_5_3_1</p>
				<p name="refRightTopForm">phis.application.eg.EG/EG_5/EG_5_3_2</p>
				<p name="refRightBottomList">phis.application.eg.EG/EG_5/EG_5_3_3</p>
			</properties>
		</module>
		<module id="EG_5_3_1" name="左边列表" type="1"
			script="phis.application.eg.script.MultiLeftList">
			<properties>
				<p name="entryName">phis.application.eg.schemas.MS_BRDA</p>
			</properties>
		</module>
		<module id="EG_5_3_2" name="右上表单" type="1"
			script="phis.application.eg.script.MultiRightTopForm">
			<properties>
				<p name="entryName">phis.application.eg.schemas.MS_BRDA</p>
			</properties>
		</module>
		<module id="EG_5_3_3" name="右下列表" type="1"
			script="phis.application.eg.script.MultiRightBottomList">
			<properties>
				<p name="entryName">phis.application.eg.schemas.MS_CF01_CIC</p>
			</properties>
		</module>
	</catagory>
	<catagory id="EG_6" name="前后台交互">
		<!--前后台交互-->
	</catagory>
</application>
