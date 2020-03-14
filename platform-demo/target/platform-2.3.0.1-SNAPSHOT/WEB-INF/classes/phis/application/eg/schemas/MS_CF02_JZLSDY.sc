<?xml version="1.0" encoding="UTF-8"?>

<entry entityName="MS_CF02_JZLSDY" tableName="MS_CF02" alias="药品信息" sort="YPZH,SBXH">
    <!-- 药品基本信息 -->
    <item id="SBXH" alias="处方明细序号" type="long" length="18" not-null="1"
          generator="assigned" pkey="true" hidden="true">
        <key>
            <rule name="increaseId" type="increase" length="18" startPos="1000" />
        </key>
    </item>
    <item id="YPXH" alias="药品序号" type="long" length="18" hidden="true" />
    <item id="CFSB" alias="处方识别" type="long" length="18" hidden="true" />
    <item ref="b.YPMC" alias="药品名称" mode="remote" type="string" width="160"
          anchor="100%" length="80" colspan="2" not-null="true" />
    <item id="YFGG" alias="规格" type="string" length="20" fixed="true"
          width="100" />
    <item id="CFTS" alias="处方帖数" type="int" display="0" />
    <item id="YCJL" alias="剂量" width="70" type="double" precision="4"
          max="9999999.999" not-null="true" />
    <item id="JLDW" alias="" width="60" type="string" not-null="true" />
    <item id="YPYF" alias="频次" type="string" not-null="true" length="18"
          width="60">
        <dic id="phis.dictionary.useRate" searchField="text" fields="key,text,MRCS"
             autoLoad="true" />
    </item>
    <item id="YPSL" alias="总量" width="50" type="int" defaultValue="1"
          max="99999999" />
    <item id="YFDW" alias="单位" type="string" length="4" fixed="true"
          width="40" />
    <item id="YYTS" alias="天数" not-null="true" type="int" width="45"
          max="99999999" />
    <item id="GYTJ" alias="药品用法" not-null="true" type="int" length="4"
          width="80">
        <dic id="phis.dictionary.drugMode" autoLoad="true" searchField="PYDM"
             fields="key,text,PYDM,FYXH" />
    </item>
    <item id="CDMC" alias="药品产地" width="80" type="string" fixed="true"></item>
    <item id="YPCD" alias="药品产地" width="80" fixed="true" type="string" display="0">
        <dic id="phis.dictionary.medicinePlace" />
    </item>
    <item id="YPDJ" width="70" type="double" alias="单价" precision="4"
          fixed="true" />
    <item id="BZXX" alias="备注" width="80" type="string" />

    <relations>
        <relation type="parent" entryName="phis.application.cic.schemas.YK_TYPK_MS">
            <join parent="YPXH" child="YPXH"></join>
        </relation>
    </relations>
</entry>
