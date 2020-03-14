<?xml version="1.0" encoding="UTF-8"?>

<entry entityName="MS_BRDA" tableName="MS_BRDA"  alias="病人档案表" sort="a.XGSJ desc">
	<item id="key" alias="主键" type="string" length="18" generator="assigned" pkey="true" display="0">
		<key>
			<rule name="increaseId" defaultFill="0" type="increase" length="10" startPos="1"/>
		</key>
	</item>
	<item id="string" alias="字符(长度5)" type="string" length="5"  />
	<item id="null" alias="必填项" type="string" length="32" width="120" not-null="1"/>
	<item id="date" alias="日期类型" type="date"/>
	<item id="time" alias="时间" type="datetime"/>
	<item id="int" alias="整数(1-99)" type="int" length="20" minValue="1" maxValue="99" width="180" />
	<item id="double" alias="小数" type="double" length="20" width="180" />
	<item id="combo" alias="下拉项" length="18"  type="string">
		<dic id="phis.dictionary.patientProperties"/>
	</item>
	<item id="combo_tree" alias="下拉树" type="string" width="180" >
		<dic id="platform.reg.dictionary.cardtype" render="Tree" columnWidth="200"/>
	</item>
	<item id="radio" alias="单选" length="4"  type="string" colspan="2">
		<dic id="phis.dictionary.gender" render="Radio"/>
	</item>
	<item id="combo_treechec" alias="下拉树选择" type="string" colspan="2" anchor="50%">
		<dic id="platform.reg.dictionary.cardtype" render="TreeCheck" columnWidth="200"/>
	</item>
	<item id="combo_check" alias="多选-跨行" type="string"   colspan="2" >
		<dic id="platform.reg.dictionary.cardtype" render="Checkbox" columns="5" columnWidth="200"/>
	</item>
	<item id="textarea" alias="文本框" xtype="textarea" length="200" colspan="2"/>
	<item id="htmleditor" alias="富文本框" xtype="htmleditor" length="200"  colspan="2"/>
</entry>