<?xml version="1.0" encoding="UTF-8"?>

<entry entityName="MS_BRDA" tableName="MS_BRDA"  alias="病人档案表" sort="a.XGSJ desc">
	<item id="BRID" alias="病人ID号" type="string" length="18" generator="assigned" pkey="true" display="0">
		<key>
			<rule name="increaseId" defaultFill="0" type="increase" length="10" startPos="1"/>
		</key>
	</item>
	<item id="EMPIID" alias="EMPIID" type="string" length="32" display="0" />
	<item id="MZHM" alias="门诊号码" type="string" length="32" queryable="true" width="120" not-null="1"/>
	<item id="BRXM" alias="病人姓名" type="string" length="40" queryable="true" selected="true" />
	<item ref="b.workPlace" />
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
	<relations>
		<relation type="children" entryName="phis.application.eg.schemas.MPI_DemographicInfo">
			<join parent="EMPIID" child="empiId" />
		</relation>
	</relations>
</entry>