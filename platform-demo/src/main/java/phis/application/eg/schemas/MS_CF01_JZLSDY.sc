<?xml version="1.0" encoding="UTF-8"?>

<entry entityName="MS_CF01_JZLSDY" tableName="MS_CF01" alias="就诊历史处方打印01">
    <item id="CFSB" alias="处方识别" display="0" type="long" length="18" not-null="1" isGenerate="false" generator="assigned" pkey="true">
    </item>
    <item id="JGID" alias="机构ID" display="0" type="string" length="20" />
    <item id="CFHM" alias="处方号码" fixed="true" generator="assigned" type="string" length="10">
    </item>
    <item id="FPHM" alias="发票号码" type="string" length="20" display="0"/>
    <item id="KFRQ" alias="开方日期" xtype="datetimefield" width="120" type="date" not-null="1" />
    <item id="CFLX" alias="处方类型" type="int" length="1" not-null="1" defaultValue="1" >
        <dic id="phis.dictionary.prescriptionType" editable="false"/>
    </item>
    <item id="YSDM" alias="开方医生" length="10" type="string" defaultValue="%user.userId" fixed="true">
        <dic id="phis.dictionary.doctor_cfqx" filter="['eq',['$','item.properties.ORGANIZCODE'],['$','%user.manageUnit.ref']]"/>
    </item>

</entry>
