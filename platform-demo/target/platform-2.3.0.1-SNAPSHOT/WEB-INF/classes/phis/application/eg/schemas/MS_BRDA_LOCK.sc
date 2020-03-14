<?xml version="1.0" encoding="UTF-8"?>

<entry entityName="MS_BRDA" tableName="MS_BRDA"  alias="病人档案表" sort="a.XGSJ desc">
	<item id="BRID" alias="病人ID号" type="string" length="18" generator="assigned" pkey="true" display="0">
		<key>
			<rule name="increaseId" defaultFill="0" type="increase" length="10" startPos="1"/>
		</key>
	</item>
	<item id="EMPIID" alias="EMPIID" type="string" length="32" display="0" />
	<item id="MZHM" alias="门诊号码" type="string" length="32" queryable="true" width="120" not-null="1" locked="true"/>
	<item id="BRXM" alias="病人姓名" type="string" length="40" queryable="true" selected="true" locked="true"/>
	<item id="BRXZ" alias="病人性质" length="18"  type="string">
		<dic id="phis.dictionary.patientProperties"/>
	</item>
	<item id="BRXB" alias="病人性别" length="4"  type="string">
		<dic id="phis.dictionary.gender"/>
	</item>
	<item id="CSNY" alias="出生年月" type="date"/>
	<item id="SFZH" alias="身份证号" type="string" length="20" width="180" display="0"/>
	<item ref="b.idCard" display="1" queryable="true" />
	<item id="HYZK" alias="婚姻状况" length="4" display="0"  type="string"/>
	
	<item id="JDJG" alias="建档机构" length="8" not-null="1" width="180"  type="string">
		<dic id="phis.@manageUnit"/>
	</item>
	<item id="JDSJ" alias="建档时间" type="timestamp" width="150"/>
	<item id="JDR" alias="建档人" type="string" length="10"  >
		<dic id="phis.dictionary.doctor" />
	</item>
	<item id="MZDM" alias="民族代码" length="4" type="string"/>
	<item id="XXDM" alias="血型代码" length="4" type="string"/>
	<item id="GMYW" alias="过敏药物" length="6" type="string"/>
	<item id="DWXH" alias="单位序号" length="18" type="string"/>
	<item id="DWMC" alias="单位名称" type="string" length="40"/>
	<item id="DWDH" alias="单位电话" type="string" length="16"/>
	<item id="DWYB" alias="单位邮编" type="string" length="6"/>
	<item id="HKDZ" alias="户口地址" type="string" length="40"/>
	<item id="JTDH" alias="家庭电话" type="string" length="16"/>
	<item id="HKYB" alias="户口邮编" type="string" length="6"/>
	<item id="JZCS" alias="就诊次数" length="4" type="string"/>
	<item id="JZRQ" alias="就诊日期" type="timestamp"/>
	<item id="CZRQ" alias="初诊日期" type="timestamp"/>
	<item id="JZKH" alias="就诊卡号" type="string" length="40"/>
	<item id="SFDM" alias="省份代码" length="4" type="string"/>
	<item id="JGDM" alias="籍贯代码" length="4" type="string"/>
	<item id="GJDM" alias="国籍代码" length="4" type="string"/>
	<item id="LXRM" alias="联系人名" type="string" length="10"/>	
	<item id="LXGX" alias="联系关系" type="string" length="4">
		<dic id="phis.dictionary.GB_T4761" />
	</item>
	<item id="LXDZ" alias="联系地址" type="string" length="200"/>
	<item id="LXDH" alias="联系电话" type="string" length="16"/>
	<item id="DBRM" alias="担保人名" type="string" length="10"/>
	<item id="DBGX" alias="担保关系" length="4" type="string"/>
	<item id="SBHM" alias="社保号码" type="string" length="20"/>
	<item id="YBKH" alias="医保卡号" type="string" length="20"/>
	<item id="ZZTX" alias="在职退休" length="4" type="string"/>
	<item id="JDJG" alias="建档机构" length="8" not-null="1" type="string"/>
	<item id="JDSJ" alias="建档时间" type="timestamp"/>
	<item id="JDR" alias="建档人" type="string" length="10"/>
</entry>