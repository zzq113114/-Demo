<?xml version="1.0" encoding="UTF-8"?>

<role id="phis.50" name="全科门诊医生角色" parent="base" pageCount="2"
	version="1388456433586">
	<accredit>
		<apps>
			<app id="chis.application.index.INDEX">
				<others/>
			</app>
			<app id="phis.application.menu.COMM">
				<catagory id="PUB">
					<others />
				</catagory>
			</app>
			<app id="chis.application.common.COMMON">
				<catagory id="COMMON">
					<others />
				</catagory>
			</app>
			<app id="phis.application.top.TOP">
				<catagory id="TOPFUNC">
					<module id="DepartmentSwitch_out">
						<others />
					</module>
					<module id="PharmacySwitch">
						<others />
					</module>
					<module id="StoreHouseSwitch">
						<others />
					</module>
					<module id="MedicalSwitch">
						<others />
					</module>
					<module id="TreasurySwitch">
						<others />
					</module>
				</catagory>
			</app>
			<app id="phis.application.menu.QKZL">
				<catagory id="STM" acType="blacklist">
					<module id="STM12">
						<others />
					</module>
				</catagory>
				<catagory id="CIC">
					<module id="PatientList">
						<others />
					</module>
					<module id="CIC0110" >
						<others />
					</module>
                    <module id="CIC0113" >
                        <others />
                    </module>
					<module id="CIC60">
						<others />
					</module>
					<module id="CIC6001">
						<others />
					</module>
					<module id="CIC6002">
						<others />
					</module>
				<!--<module id="CIC0099">
						<others />
					</module>
					-->
					<module id="CCL40">
						<others />
					</module>
                    <module id="CCL4001">
                        <others />
                    </module>
                    <module id="CCL4002">
                        <others />
                    </module>
                    <module id="CCL400101">
                        <others />
                    </module>
                    <module id="CCL400102">
                        <others />
                    </module>
                    <module id="CCL40010101">
                        <others />
                    </module>
                    <module id="CCL40010102">
                        <others />
                    </module>
                    <module id="CCL40010103">
                        <others />
                    </module>
                    <module id="CCL40010104">
                        <others />
                    </module>
                    <module id="CCL40010201">
                        <others />
                    </module>
                    <module id="CCL40010202">
                        <others />
                    </module>
                    <module id="CCL400103">
                        <others />
                    </module>
                    <module id="CCL40010301">
                        <others />
                    </module>
                    <module id="CCL40010302">
                        <others />
                    </module>
                      <module id="CIC44">
                        <others/>
                      </module>
                      <module id="CIC46">
                        <others/>
                      </module>
                      <module id="CIC45">
                        <others/>
                      </module>
                    <module id="CIC471">
                      <others/>
                    </module>
					<module id="CIC472">
						<others/>
					</module>
					<module id="CIC90">
						<others />
					</module>

					<module id="CIC90">
						<others />
					</module>
					<!--<module id="CIC00">
						<others />
					</module>-->
					<module id="CIC0001">
						<others />
					</module>
					<module id="CIC01">
						<others />
					</module>
					<module id="CIC01010101">
						<others />
					</module>
					<module id="CIC0101">
						<others />
					</module>
					<module id="CIC010101">
						<others />
					</module>
					<module id="CIC0102">
						<others />
					</module>
					<module id="CIC0103">
						<others />
					</module>
					<module id="CIC0104">
						<others />
					</module>
					<module id="CIC0108">
						<others />
					</module>
					<module id="CIC0109">
						<others />
					</module>
					<module id="CIC0111">
						<others />
					</module>
					<module id="CIC010401">
						<others />
					</module>
					<module id="CIC010402">
						<others />
					</module>
					<module id="CIC0105">
						<others />
					</module>
					<module id="CIC0106">
						<others />
					</module>
					<module id="CIC010601">
						<others />
					</module>
					<module id="CIC010602">
						<others />
					</module>
					<module id="CIC0107">
						<others />
					</module>
					<module id="CIC04">
						<others />
					</module>
					<module id="CIC0401">
						<others />
					</module>
					<module id="CIC0402">
						<others />
					</module>
					<module id="CIC040201">
						<others />
					</module>
					<module id="CIC040202">
						<others />
					</module>
					<module id="CIC04020101">
						<others />
					</module>
					<module id="CIC04020102">
						<others />
					</module>
					<module id="CIC04020103">
						<others />
					</module>
					<module id="CIC04020201">
						<others />
					</module>
					<module id="CIC04020202">
						<others />
					</module>
					<module id="CIC04020203">
						<others />
					</module>
					<module id="CIC0402020201">
						<others />
					</module>
					<module id="CIC0402020101">
						<others />
					</module>
					<module id="CIC0402020102">
						<others />
					</module>
					<module id="CIC0112" >
						<others />
					</module>
					<module id="CIC05">
						<others />
					</module>
					<module id="CIC06">
						<others />
					</module>
					<module id="CIC109">
						<others />
					</module>
					<module id="CIC10901">
						<others />
					</module>
					<module id="CIC10902">
						<others />
					</module>
					<module id="CIC12">
						<others />
					</module>
					<module id="CIC1201">
						<others />
					</module>
					<module id="CIC1202">
						<others />
					</module>
					<module id="CIC1203">
						<others />
					</module>
					<module id="CIC23">
						<others />
					</module>
					<module id="CIC2301">
						<others />
					</module>
					<module id="CIC2302">
						<others />
					</module>
					<module id="CIC2303">
						<others />
					</module>
					<module id="CIC2304">
						<others />
					</module>
					<module id="CIC2305">
						<others />
					</module>
					<module id="CIC28">
						<others />
					</module>
					<module id="CIC2801">
						<others />
					</module>
					<module id="CIC2802">
						<others />
					</module>
					<module id="CIC280201">
						<others />
					</module>
					<module id="CIC280202">
						<others />
					</module>
					<module id="CIC280204">
						<others />
					</module>
					<module id="CIC28020401">
						<others />
					</module>
					<module id="CIC28020402">
						<others />
					</module>
					<module id="CIC24">
						<others />
					</module>
					<module id="CIC26">
						<others />
					</module>
					<module id="CIC25">
						<others />
					</module>
					<module id="CIC27">
						<others />
					</module>
					<module id="CIC2701">
						<others />
					</module>
					<module id="CIC2703">
						<others />
					</module>
					<module id="CIC2704">
						<others />
					</module>
					<module id="CIC270401">
						<others />
					</module>
					<module id="CIC270402">
						<others />
					</module>
					<module id="CIC2702">
						<others />
					</module>
					<module id="CIC270201">
						<others />
					</module>
					<module id="CIC270202">
						<others />
					</module>
					<module id="CIC270203">
						<others />
					</module>
					<module id="CIC270204">
						<others />
					</module>
					<module id="CIC270205">
						<others />
					</module>
					<module id="CIC270206">
						<others />
					</module>
					<module id="CIC270207">
						<others />
					</module>
					<module id="CIC270302">
						<others />
					</module>
					<module id="CIC270303">
						<others />
					</module>
					<module id="CIC0501">
						<others />
					</module>
					<module id="CIC0502">
						<others />
					</module>
					<module id="CIC0503">
						<others />
					</module>
					<module id="CIC0504">
						<others />
					</module>
					<module id="CIC050301">
						<others />
					</module>
					<module id="CIC050302">
						<others />
					</module>
					<module id="CIC050303">
						<others />
					</module>
					<module id="CIC050304">
						<others />
					</module>
					<module id="CIC05030401">
						<others />
					</module>
					<module id="CIC05030402">
						<others />
					</module>
					<module id="CIC05030403">
						<others />
					</module>
					<module id="CIC0601">
						<others />
					</module>
					<module id="CIC060101">
						<others />
					</module>
					<module id="CIC06030201">
						<others />
					</module>
					<module id="CIC0602">
						<others />
					</module>
					<module id="CIC060201">
						<others />
					</module>
					<module id="CIC060202">
						<others />
					</module>
					<module id="CIC060203">
						<others />
					</module>
                    <module id="CIC0112">
                        <others />
                    </module>

					<module id="CIC13">
						<others />
					</module>
					<module id="CIC14">
						<others />
					</module>
					<module id="CIC15">
						<others />
					</module>
					<module id="CIC16">
						<others />
					</module>
					<module id="IVC01010402">
						<others />
					</module>
					<module id="CIC29">
						<others />
					</module>
					<module id="CIC30">
						<others />
					</module>
					<module id="CIC3001">
						<others />
					</module>
					<module id="CIC3002">
						<others />
					</module>
					<module id="CIC3003">
						<others />
					</module>
					<module id="CIC3004">
						<others />
					</module>
					<module id="CIC310102">
						<others />
					</module>
					<module id="CIC31010201">
						<others />
					</module>
					<module id="CIC31010202">
						<others />
					</module>
					<module id="CIC3201">
						<others />
					</module>
					<module id="CIC33">
						<others />
					</module>
					<module id="CIC34">
						<others />
					</module>
					<module id="CIC3401">
						<others />
					</module>
					<module id="CIC35">
						<others />
					</module>
					<module id="CIC3502">
						<others />
					</module>
					<module id="CIC36">
						<others />
					</module>
					<module id="CIC3601">
						<others />
					</module>
					<module id="CIC37">
						<others />
					</module>
					<module id="CIC3701">
						<others />
					</module>
					<module id="CIC3702">
						<others />
					</module>
					<module id="CIC38">
						<others />
					</module>
					<module id="CIC3801">
						<others />
					</module>
					<module id="CIC380101">
						<others />
					</module>
					<module id="CIC380102">
						<others />
					</module>
					<module id="CIC39">
						<others />
					</module>
					<module id="CIC3901">
						<others />
					</module>
					<module id="CIC3902">
						<others />
					</module>
					<module id="CIC40">
						<others />
					</module>
					<module id="CIC4001">
						<others />
					</module>
					<module id="CIC41">
						<others />
					</module>
					<module id="CIC42">
						<others />
					</module>
					<module id="WAR36">
						<others />
					</module>
					<module id="WAR41">
						<others />
					</module>
					<module id="WAR4101">
						<others />
					</module>
					<module id="WAR4102">
						<others />
					</module>
					<module id="WAR42">
						<others />
					</module>
					<module id="WAR43">
						<others />
					</module>
					<module id="WAR44">
						<others />
					</module>
					<module id="WAR81">
						<others />
					</module>
					<module id="WAR90">
						<others />
					</module>
					<module id="WAR91">
						<others />
					</module>
					<module id="WAR9302">
						<others />
					</module>
					<module id="WAR930201">
						<others />
					</module>
					<module id="WAR930202">
						<others />
					</module>
					<module id="WAR930203">
						<others />
					</module>
					<module id="WAR930204">
						<others />
					</module>
					<module id="WAR930205">
						<others />
					</module>
					<module id="WAR930206">
						<others />
					</module>
					<module id="WAR9503">
						<others />
					</module>
					<module id="WAR9504">
						<others />
					</module>
					<module id="WAR9505">
						<others />
					</module>
					<module id="WAR950501">
						<others />
					</module>
					<module id="WAR950502">
						<others />
					</module>
					<module id="CIC0203">
						<others />
					</module>
					<module id="WAR340102">
						<others />
					</module>
					<module id="WAR34020101">
						<others />
					</module>
					<module id="WAR34010201">
						<others />
					</module>
					<module id="WAR34010202">
						<others />
					</module>
					
					<module id="CICFJZL">
						<others />
					</module>
					
					<module id="CICYCHZ">
						<others />
					</module>
					<module id="CICZZLB">
						<others />
					</module>
					<module id="CICYCHZ02">
						<others />
					</module>
					<module id="CICZZ02">
						<others />
					</module>
					<module id="CICJCKD">
						<others />
					</module>
					<module id="CICJCBG">
						<others />
					</module>

				</catagory>
				<catagory id="REG">
					<!--<module id="JZDWTJ">
						<others />
					</module>-->
					<module id="REG01" />
					<module id="REG0101">
						<action id="appointment" />
						<action id="create" />
						<action id="new" />
						<action id="callIn" />
						<action id="save" />
						<action id="retire" />
						<action id="turnDept" />
						<action id="ybdk" />
						<action id="sfz" />
						<action id="jzk" />
						<action id="retireJzk" />
						<action id="printSet" />
					</module>
					<module id="REG0102" />
					<module id="REG0103" />
					<module id="REG0104" />
					<module id="REG0105" />
					<module id="REG010501" />
					<module id="REG010502">
						<action id="commit" />
						<action id="cancel" />
					</module>
					<module id="REG0106" />
					<module id="REG010601">
						<action id="commit" />
						<action id="cancel" />
					</module>
					<module id="REG0107" />
					<module id="REG010701">
						<action id="commit" />
						<action id="cancel" />
					</module>
					<module id="REG27" />
					<module id="REG28" />
					<!--<module id="REG30" />-->
					<module id="REG31" />
				</catagory>
				<!--
				<catagory id="YB">
                    <module id="YB0101">
                        <others />
                    </module>
                    <module id="YB0102">
                        <others />
                    </module>
                    <module id="YB0103">
                        <others />
                    </module>
                    <module id="YB0104">
                        <others />
                    </module>
                    <module id="YB0105">
                        <others />
                    </module>
                    <module id="YB0106">
                        <others />
                    </module>
                    <module id="YB0107">
                        <others />
                    </module>
                     <module id="YB0108">
                         <others />
                     </module>
                     <module id="YB0109">
                         <others />
                     </module>
                     <module id="YB0110">
                         <others />
                     </module>
                     <module id="YB0111">
                         <others />
                     </module>
                      <module id="YB0112">
                          <others />
                      </module>
                    <module id="YB0113">
                        <others />
                    </module>
                    <module id="YB0114">
                        <others />
                    </module>
				</catagory>
				-->
				<catagory id="IVC">
					<module id="IVC13">
						<others />
					</module>
					<module id="IVC1301">
						<others />
					</module>
					<module id="IVC130101">
						<others />
					</module>
					<module id="IVC13010101">
						<others />
					</module>
					<module id="IVC130102">
						<others />
					</module>
					<module id="IVC130103">
						<others />
					</module>
					<module id="IVC130104">
						<others />
					</module>
					<module id="IVC130105">
						<others />
					</module>
					<module id="IVC130106">
						<others />
					</module>
					<module id="IVC0107" />
					<module id="IVC010701" />
					<module id="IVC010702" />
					<module id="IVC01070201" />
					<module id="IVC01" />
					<module id="IVC0102" />
					<module id="IVC0101">
						<action id="newPerson" />
						<action id="cflr" />
						<action id="czlr" />
						<action id="js" />
						<action id="qx" />
						<action id="fz" />
						<action id="xg" />
						<action id="ZDCR" />
					</module>
					<module id="IVC0108" >
					<others />
					</module>
					<module id="IVC010801" >
					<others />
					</module>
					<module id="IVC010802" >
					<others />
					</module>
					<module id="IVC010101" />
					<module id="IVC01010101">
						<action id="commit" />
					</module>
					<module id="IVC010102" />
					<module id="IVC010103" />
					<module id="IVC01010301" />
					<module id="IVC01010302" >
						<action id="commit" />
					</module>
					<module id="IVC01010303" >
						<action id="commit" />
					</module>
					<module id="IVC010104" />
					<module id="IVC01010401">
						<action id="clinicAll" />
					</module>
					<module id="IVC010105">
						<action id="commit" />
						<action id="cancel" />
					</module>
					<module id="IVC010106" />
					<module id="IVC010107">
						<action id="commit" />
						<action id="cancel" />
					</module>
					
					<module id="IVC01010801">
					</module>
					<module id="IVC03">
						<action id="fpzf" />
						<action id="qxzf" />
					</module>
					<module id="IVC0301">
						<action id="fpzf" />
						<action id="qxzf" />
						<action id="cancel" />
					</module>
					<!--
					<module id="IVC12" />
					<module id="IVC1201" />
					<module id="IVC1202" />
					<module id="IVC120201" />
					<module id="IVC120202" />
					<module id="IVC12020202" />
					<module id="IVC1203" />
					<module id="IVC120301" />
					<module id="IVC120302" />
					-->
					<module id="IVC05" />
					<module id="IVC0501">
						<action id="sure" />
						<action id="cancel" />
					</module>
					<module id="IVC0502" />
					<module id="IVC050201" />
					<module id="IVC050202" />
<!--					<module id="IVC06">-->
<!--						&lt;!&ndash;						<action id="ChargesSummary" />&ndash;&gt;-->
<!--						<action id="ChargesSummarySearch" />-->
<!--						<action id="ItemizeSummary2" />-->
<!--						<action id="OutstandingChargesSummary" />-->
<!--						<action id="FBQSummary" />-->
<!--					</module>-->
					<module id="IVC_sfyrbhz" />
					<module id="IVC020301" />
					<module id="IVC86" >
						<others />
					</module>
					<module id="IVC8601" >
						<others />
					</module>
					<module id="IVC8602" >
						<others />
					</module>
                   <module id="YB0101">
                        <others />
                    </module>
                    <module id="YB0102">
                        <others />
                    </module>
                    <module id="YB0103">
                        <others />
                    </module>
                    <module id="YB0104">
                        <others />
                    </module>
                    <module id="YB0105">
                        <others />
                    </module>
                    <module id="YB0106">
                        <others />
                    </module>
                    <module id="YB0107">
                        <others />
                    </module>
                     <module id="YB0108">
                         <others />
                     </module>
                     <module id="YB0109">
                         <others />
                     </module>
                     <module id="YB0110">
                         <others />
                     </module>
                     <module id="YB0111">
                         <others />
                     </module>
                      <module id="YB0112">
                          <others />
                      </module>
                    <module id="YB0113">
                        <others />
                    </module>
                    <module id="YB0114">
                        <others />
                    </module>
					<module id="YB0115">
						<others />
					</module>
				</catagory>
				<catagory id="MED">
					<module id="MED01">
						<action id="add" />
						<action id="modify" />
						<action id="delete" />
						<action id="execute" />
						<action id="refresh" />
						<action id="goback" />
					</module>
					<module id="MED0103" />
					<module id="MED0101">
						<action id="mzTab" />
						<action id="zyTab" />
						<action id="jcTab" />
					</module>
					<module id="MED010102" />
					<module id="MED010104" />
					<module id="MED01010201">
						<action id="add" />
						<action id="delete" />
						<action id="save" />
						<action id="clear" />
						<action id="close" />
					</module>
					<module id="MED01010202" />
					<module id="MED01010203" />
					<module id="MED01010204" />
					<module id="MED010101" />
					<module id="MED010103" />
					<module id="MED01010101">
						<action id="add" />
						<action id="delete" />
						<action id="save" />
						<action id="clear" />
						<action id="close" />
					</module>
					<module id="MED01010102" />
					<module id="MED01010103" />
					<module id="MED01010104" />
					<module id="MED010105" />
					<module id="MED010106" />
					<module id="MED01010501">
						<action id="add" />
						<action id="delete" />
						<action id="save" />
						<action id="clear" />
						<action id="close" />
					</module>
					<module id="MED01010502" />
					<module id="MED01010503" />
					<module id="MED01010504" />
					<module id="MED02">
						<action id="refresh" />
						<action id="cancel" />
						<action id="print" />
					</module>
					<module id="MED0201" >
						<action id="mzTabcancel" />
						<action id="zyTabcancel" />
						<action id="jcTabcancel" />
					</module>	
					<module id="MED020101" />
					<module id="MED020102" />
					<module id="MED020103" />
					<module id="MED02010101" />
					<module id="MED02010102" />
					<module id="MED02010201" />
					<module id="MED02010202" />
					<module id="MED02010301" />
					<module id="MED02010302" />
					<!--<module id="MED0202" />
						<module id="MED0203" />-->
				</catagory>
				<catagory id="FZXZ">
					<module id="FZXZList">
						<others />
					</module>
					<module id="SZBRList">
                       <others />
                    </module>
                    <module id="SZTJList">
                        <others />
                    </module>
                    <module id="FZXZRefuseForm">
                        <others />
                    </module>
				</catagory>
				<catagory id="YB">
					<module id="YB03">
						<others />
					</module>
				</catagory>
			</app>
			<app id="phis.application.menu.YYGL">
				<!--<catagory id="STO">-->
					<!--<module id="STO03">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO0301">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO0302">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO030101">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO03010101">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO03010102">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO03010103">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO03010201">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO030102">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO03010202">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO04">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO0401">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO0402">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO040101">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO04010101">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO04010102">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO040102">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO04010201">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO04010202">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO05">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO0501">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO050101">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO0502">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO050201">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO050301">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO06">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO06010101">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO07">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO07010101">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO07010201">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO07010202">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO09">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO09010101">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO09010201">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO09010202">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO090102">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO100101">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO100102">-->
						<!--<others />-->
					<!--</module>-->
					<!--&lt;!&ndash;ADD&ndash;&gt;-->
					<!--<module id="STO10010301">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO1001030101">-->
						<!--<others />-->
					<!--</module>-->
					<!--&lt;!&ndash;END&ndash;&gt;-->
					<!--<module id="STO100103">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO0301010201">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO0503">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO0602">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO060101">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO06010102">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO0702">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO070101">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO07010102">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO070102">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO0601">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO0701">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO0901">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO0902">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO090101">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO09010102">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO12">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO1201">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO10">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO1001">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO10010201">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO11">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO1101">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO1102">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO1103">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO110101">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO110201">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO22">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO2201">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO220101">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO220102">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO31">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO3101">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO310101">-->
						<!--<others />-->
					<!--</module>-->
					<!--<module id="STO310102">-->
						<!--<others />-->
					<!--</module>-->
				<!--</catagory>-->
				<catagory id="PHA">
					<module id="PHA31">
						<others />
					</module>
					<module id="PHA3101">
						<others />
					</module>
					<module id="PHA3102">
						<others />
					</module>
					<module id="PHA01">
						<others />
					</module>
					<module id="PHA0101">
						<others />
					</module>
					<module id="PHA0102">
						<others />
					</module>
					<module id="PHA010201">
						<others />
					</module>
					<module id="PHA010202">
						<others />
					</module>
					<module id="PHA0103">
						<others />
					</module>
					<module id="PHA010301">
						<others />
					</module>
					<module id="PHA0201">
						<others />
					</module>
					<module id="PHA0202">
						<others />
					</module>
					<module id="PHA020201">
						<others />
					</module>
					<module id="PHA020202">
						<others />
					</module>
					<module id="PHA0301">
						<others />
					</module>
					<module id="PHA0302">
						<others />
					</module>
					<module id="PHA030201">
						<others />
					</module>
					<module id="PHA030202">
						<others />
					</module>
					<module id="PHA0601">
						<others />
					</module>
					<module id="PHA12">
						<others />
					</module>
					<module id="PHA12010101">
						<others />
					</module>
					<module id="PHA13">
						<others />
					</module>
					<module id="PHA13010101">
						<others />
					</module>
					<module id="PHA14">
						<others />
					</module>
					<module id="PHA14010101">
						<others />
					</module>
					<module id="PHA14010103">
						<others />
					</module>
					<module id="PHA15">
						<others />
					</module>
					<module id="PHA15010101">
						<others />
					</module>
					<module id="PHA15010103">
						<others />
					</module>
					<module id="PHA15020101">
						<others />
					</module>
					<module id="PHA16">
						<others />
					</module>
					<module id="PHA16010101">
						<others />
					</module>
					<module id="PHA17">
						<others />
					</module>
					<module id="PHA17010101">
						<others />
					</module>
					<module id="PHA17010102">
						<others />
					</module>
					<module id="PHA18">
						<others />
					</module>
					<module id="PHA18010101">
						<others />
					</module>
					<module id="PHA1901">
						<others />
					</module>
					<module id="PHA1902">
						<others />
					</module>
					<module id="PHA190201">
						<others />
					</module>
					<module id="PHA190202">
						<others />
					</module>
					<module id="PHA19020201">
						<others />
					</module>
					<module id="PHA19020202">
						<others />
					</module>
					<module id="PHA19020203">
						<others />
					</module>
					<module id="PHA190202020101">
						<others />
					</module>
					<module id="PHA190202020102">
						<others />
					</module>
					<module id="PHA190301">
						<others />
					</module>
					<module id="PHA190302">
						<others />
					</module>
					<module id="PHA190303">
						<others />
					</module>
					<module id="PHA190304">
						<others />
					</module>
					<module id="PHA2101">
						<others />
					</module>
					<module id="PHA2102">
						<others />
					</module>
					<module id="PHA210201">
						<others />
					</module>
					<module id="PHA23">
						<others />
					</module>
					<module id="PHA24">
						<others />
					</module>
					<module id="PHA25">
						<others />
					</module>
					<module id="PHA26">
						<others />
					</module>
					<module id="PHA27">
						<others />
					</module>
					<module id="PHA02">
						<others />
					</module>
					<module id="PHA04">
						<action id="monthly" />
					</module>
					<module id="PHA0401">
						<action id="monthly" />
						<action id="cancel" />
					</module>
					<module id="PHA05">
						<action id="query" />
						<action id="reset" />
						<action id="detail" />
					</module>
					<module id="PHA0501">
						<action id="cancel" />
					</module>
					<module id="PHA06">
						<others />
					</module>
					<module id="PHA0702">
						<others />
					</module>
					<module id="PHA03">
						<others />
					</module>
					<module id="PHA30">
						<others />
					</module>
					<module id="PHA3001">
						<others />
					</module>
					<module id="PHA3002">
						<others />
					</module>
					<module id="PHA300201">
						<others />
					</module>
					<module id="PHA300202">
						<others />
					</module>
					<module id="PHA070201">
						<others />
					</module>
					<module id="PHA1201">
						<others />
					</module>
					<module id="PHA1202">
						<others />
					</module>
					<module id="PHA120101">
						<others />
					</module>
					<module id="PHA12010102">
						<others />
					</module>
					<module id="PHA1301">
						<others />
					</module>
					<module id="PHA1302">
						<others />
					</module>
					<module id="PHA130101">
						<others />
					</module>
					<module id="PHA13010102">
						<others />
					</module>
					<module id="PHA1401">
						<others />
					</module>
					<module id="PHA1402">
						<others />
					</module>
					<module id="PHA140101">
						<others />
					</module>
					<module id="PHA14010102">
						<others />
					</module>
					<module id="PHA1501">
						<others />
					</module>
					<module id="PHA1502">
						<others />
					</module>
					<module id="PHA150101">
						<others />
					</module>
					<module id="PHA15010102">
						<others />
					</module>
					<module id="PHA150201">
						<others />
					</module>
					<module id="PHA1601">
						<others />
					</module>
					<module id="PHA1602">
						<others />
					</module>
					<module id="PHA160101">
						<others />
					</module>
					<module id="PHA1701">
						<others />
					</module>
					<module id="PHA16010102">
						<others />
					</module>
					<module id="PHA1702">
						<others />
					</module>
					<module id="PHA170101">
						<others />
					</module>
					<module id="PHA1801">
						<others />
					</module>
					<module id="PHA1802">
						<others />
					</module>
					<module id="PHA180101">
						<others />
					</module>
					<module id="PHA18010102">
						<others />
					</module>
					<module id="PHA19">
						<others />
					</module>
					<module id="PHA1902020201">
						<others />
					</module>
					<module id="PHA1902020202">
						<others />
					</module>
					<module id="PHA1903">
						<others />
					</module>
					<module id="PHA20">
						<others />
					</module>
					<module id="PHA2001">
						<others />
					</module>
					<module id="PHA66">
						<others />
					</module>
					<module id="PHA6601">
						<others />
					</module>
					<module id="PHA660101">
						<others />
					</module>
					<module id="PHA660102">
						<others />
					</module>
				</catagory>
				<catagory id="SUP_ONE" acType="whitelist">
					<module id="SUP34">
						<others />
					</module>
					<module id="SUP3401">
						<others />
					</module>
					<module id="SUP3402">
						<others />
					</module>
					<module id="SUP340101">
						<others />
					</module>
					<module id="SUP34010101">
						<others />
					</module>
					<module id="SUP34010102">
						<others />
					</module>
					<module id="SUP34010103">
						<others />
					</module>
					<module id="SUP01">
						<others />
					</module>
					<module id="SUP0101">
						<others />
					</module>
					<module id="SUP0102">
						<others />
					</module>
					<module id="SUP010101">
						<others />
					</module>
					<module id="SUP01010101">
						<others />
					</module>
					<module id="SUP01010102">
						<others />
					</module>
					<module id="SUP0101010201">
						<others />
					</module>
					<module id="SUP03">
						<others />
					</module>
					<module id="SUP0301">
						<others />
					</module>
					<module id="SUP0302">
						<others />
					</module>
					<module id="SUP030101">
						<others />
					</module>
					<module id="SUP03010101">
						<others />
					</module>
					<module id="SUP03010102">
						<others />
					</module>
					<module id="SUP030104">
						<others />
					</module>
					<module id="SUP0301010201">
						<others />
					</module>
					<module id="SUP05">
						<others />
					</module>
					<module id="SUP0501">
						<others />
					</module>
					<module id="SUP0502">
						<others />
					</module>
					<module id="SUP050101">
						<others />
					</module>
					<module id="SUP05010101">
						<others />
					</module>
					<module id="SUP05010102">
						<others />
					</module>
					<module id="SUP05010103">
						<others />
					</module>
					<module id="SUP05010104">
						<others />
					</module>
					<module id="SUP0501010301">
						<others />
					</module>
					<module id="SUP0501010302">
						<others />
					</module>
					<module id="SUP06">
						<others />
					</module>
					<module id="SUP0601">
						<others />
					</module>
					<module id="SUP0602">
						<others />
					</module>
					<module id="SUP060101">
						<others />
					</module>
					<module id="SUP06010101">
						<others />
					</module>
					<module id="SUP06010102">
						<others />
					</module>
					<module id="SUP06010103">
						<others />
					</module>
					<module id="SUP10">
						<others />
					</module>
					<module id="SUP1001">
						<others />
					</module>
					<module id="SUP1002">
						<others />
					</module>
					<module id="SUP100101">
						<others />
					</module>
					<module id="SUP10010101">
						<others />
					</module>
					<module id="SUP10010102">
						<others />
					</module>
					<module id="SUP100102">
						<others />
					</module>
					<module id="SUP10010201">
						<others />
					</module>
					<module id="SUP10010202">
						<others />
					</module>
					<module id="SUP10010203">
						<others />
					</module>
					<module id="SUP100103">
						<others />
					</module>
					<module id="SUP10010301">
						<others />
					</module>
					<module id="SUP10010302">
						<others />
					</module>
					<module id="SUP10010303">
						<others />
					</module>
					<module id="SUP11">
						<others />
					</module>
					<module id="SUP1101">
						<others />
					</module>
					<module id="SUP1102">
						<others />
					</module>
					<module id="SUP110101">
						<others />
					</module>
					<module id="SUP11010101">
						<others />
					</module>
					<module id="SUP11010102">
						<others />
					</module>
					<module id="SUP11010103">
						<others />
					</module>
					<module id="SUP110102">
						<others />
					</module>
					<module id="SUP11010201">
						<others />
					</module>
					<module id="SUP11010202">
						<others />
					</module>
					<module id="SUP11010203">
						<others />
					</module>
					<module id="SUP110103">
						<others />
					</module>
					<module id="SUP11010301">
						<others />
					</module>
					<module id="SUP11010302">
						<others />
					</module>
					<module id="SUP11010303">
						<others />
					</module>
					<module id="SUP13">
						<others />
					</module>
					<module id="SUP1301">
						<others />
					</module>
					<module id="SUP1302">
						<others />
					</module>
					<module id="SUP130101">
						<others />
					</module>
					<module id="SUP13010101">
						<others />
					</module>
					<module id="SUP13010102">
						<others />
					</module>
					<module id="SUP13010103">
						<others />
					</module>
					<module id="SUP14">
						<others />
					</module>
					<module id="SUP1401">
						<others />
					</module>
					<module id="SUP16">
						<others />
					</module>
					<module id="SUP1601">
						<others />
					</module>
					<module id="SUP160101">
						<others />
					</module>
					<module id="SUP160102">
						<others />
					</module>
					<module id="SUP160103">
						<others />
					</module>
					<module id="SUP60">
						<others />
					</module>


					<module id="SUP45">
						<others />
					</module>
					<module id="SUP46">
						<others />
					</module>
					<module id="SUP4601">
						<others />
					</module>
					<module id="SUP460101">
						<others />
					</module>
					<module id="SUP4602">
						<others />
					</module>
					<module id="SUP460201">
						<others />
					</module>
					<module id="SUP47">
						<others />
					</module>
					<module id="SUP4701">
						<others />
					</module>
					<module id="SUP4702">
						<others />
					</module>
					<module id="SUP4703">
						<others />
					</module>
					<module id="SUP470301">
						<others />
					</module>
					<module id="SUP470302">
						<others />
					</module>
					<module id="SUP470303">
						<others />
					</module>
					<module id="SUP50">
						<others />
					</module>
					<module id="SUP5001">
						<others />
					</module>
					<module id="SUP5002">
						<others />
					</module>
					<module id="SUP5004">
						<others />
					</module>
					<module id="SUP5003">
						<others />
					</module>
					<module id="SUP51">
						<others />
					</module>
					<module id="SUP5101">
						<others />
					</module>
					<module id="SUP510101">
						<others />
					</module>
					<module id="SUP510103">
						<others />
					</module>
					<module id="SUP51010301">
						<others />
					</module>
					<module id="SUP5101030101">
						<others />
					</module>
					<module id="SUP5101030102">
						<others />
					</module>
					<module id="SUP51010302">
						<others />
					</module>
					<module id="SUP510102">
						<others />
					</module>
					<module id="SUP510104">
						<others />
					</module>
			        
				</catagory>
				<catagory id="SUP_TOW">
					<module id="SUP02">
						<others />
					</module>
					<module id="SUP0201">
						<others />
					</module>
					<module id="SUP0202">
						<others />
					</module>
					<module id="SUP020101">
						<others />
					</module>
					<module id="SUP02010101">
						<others />
					</module>
					<module id="SUP02010102">
						<others />
					</module>
					<module id="SUP030104">
						<others />
					</module>

					<module id="SUP04">
						<others />
					</module>
					<module id="SUP0401">
						<others />
					</module>
					<module id="SUP030104">
						<others />
					</module>
					<module id="SUP0402">
						<others />
					</module>
					<module id="SUP040101">
						<others />
					</module>
					<module id="SUP04010101">
						<others />
					</module>
					<module id="SUP04010102">
						<others />
					</module>
					<module id="SUP07">
						<others />
					</module>
					<module id="SUP0701">
						<others />
					</module>
					<module id="SUP0702">
						<others />
					</module>
					<module id="SUP070101">
						<others />
					</module>
					<module id="SUP07010101">
						<others />
					</module>
					<module id="SUP07010102">
						<others />
					</module>
					<module id="SUP08">
						<others />
					</module>
					<module id="SUP0801">
						<others />
					</module>
					<module id="SUP0802">
						<others />
					</module>
					<module id="SUP080101">
						<others />
					</module>
					<module id="SUP08010101">
						<others />
					</module>
					<module id="SUP08010102">
						<others />
					</module>
					<module id="SUP09">
						<others />
					</module>
					<module id="SUP0901">
						<others />
					</module>
					<module id="SUP12">
						<others />
					</module>
					<module id="SUP1201">
						<others />
					</module>
					<module id="SUP1202">
						<others />
					</module>
					<module id="SUP120101">
						<others />
					</module>
					<module id="SUP12010101">
						<others />
					</module>
					<module id="SUP12010102">
						<others />
					</module>
					<module id="SUP15">
						<others />
					</module>
					<module id="SUP1501">
						<others />
					</module>
					<module id="SUP1502">
						<others />
					</module>
					<module id="SUP150101">
						<others />
					</module>
					<module id="SUP15010101">
						<others />
					</module>
					<module id="SUP15010102">
						<others />
					</module>
					<module id="SUP15010103">
						<others />
					</module>
					<module id="SUP15">
						<others />
					</module>
					<module id="SUP17">
						<others />
					</module>
					<module id="SUP1701">
						<others />
					</module>
					<module id="SUP1702">
						<others />
					</module>
					<module id="SUP1703">
						<others />
					</module>
					<module id="SUP170101">
						<others />
					</module>
					<module id="SUP17010102">
						<others />
					</module>
					<module id="SUP17010101">
						<others />
					</module>
					<module id="SUP30">
						<others />
					</module>
					<module id="SUP3001">
						<others />
					</module>
					<module id="SUP42">
						<others />
					</module>
					<module id="SUP4201">
						<others />
					</module>
					<module id="SUP4202">
						<others />
					</module>
					<module id="SUP420101">
						<others />
					</module>
					<module id="SUP42010101">
						<others />
					</module>
					<module id="SUP42010102">
						<others />
					</module>
					<module id="SUP60">
						<others />
					</module>
					
					<module id="SUP52">
						<others />
					</module>
					<module id="SUP5201">
						<others />
					</module>
					<module id="SUP5202">
						<others />
					</module>
				</catagory>
				<catagory id="PCM" acType="blacklist">
					<others />
				</catagory>
			</app>
			<app id="phis.application.menu.TJFX">
				<catagory id="REG">
					<module id="JZDWTJ">
						<others />
					</module>
					<module id="REG07">
						<others />
					</module>
					<module id="REG0701">
						<others />
					</module>
					<module id="REG29">
						<others />
					</module>
					<module id="REG2901">
						<others />
					</module>
					<module id="REG2902">
						<others />
					</module>
				</catagory>
				<catagory id="IVC">
					<module id="IVC02">
						<others />
					</module>
					<module id="IVC0202">
						<others />
					</module>
					<module id="IVC0203">
						<others />
					</module>
					<module id="IVC020301">
						<others />
					</module>
					<module id="IVC0204">
						<others />
					</module>
					<module id="IVC0205">
						<others />
					</module>
					<module id="IVC0206">
						<others />
					</module>
					<module id="IVC020601">
						<others />
					</module>
					<module id="IVC0207">
						<others />
					</module>
					<module id="IVC07">
						<others />
					</module>
					<module id="IVC08">
						<others />
					</module>
					<module id="IVC15">
						<others />
					</module>
					<module id="IVC1501">
						<others />
					</module>
					<module id="IVC1502">
						<others />
					</module>
					<module id="IVC16">
						<others />
					</module>
				</catagory>
				<catagory id="CIC">
					<others />
				</catagory>
				<catagory id="PHSA_GW">
					<module id="PHSA_MBJXTJ">
						<others/>
					</module>
					<module id="PHSA_JDTJ">
						<others/>
					</module>
					<module id="PHSA_BLTJ_1" acType="blacklist">
						<action id="edit"/>
					</module>
					<module id="PHSA_BLTJ_2" acType="blacklist">
						<action id="edit"/>
					</module>
				</catagory>
				<catagory id="PHA">
					<module id="PHA22">
						<others />
					</module>
					<module id="PHA21">
						<others />
					</module>
					<module id="PHA2101">
						<others />
					</module>
					<module id="PHA2102">
						<others />
					</module>
					<module id="PHA210201">
						<others />
					</module>
				</catagory>
			</app>
			<app id="phis.application.sys.SYS">
				<catagory id="IVC">
					<module id="IVC04">
						<others />
					</module>
					<module id="IVC0401">
						<others />
					</module>
					<module id="IVC0402">
						<others />
					</module>
					<module id="IVC0403">
						<others />
					</module>
				</catagory>
				<catagory id="CLC_CFG" >
					<others />
				</catagory>
				<catagory id="REG_CFG">
					<others />
				</catagory>
				<catagory id="YJ_CFG">
					<others />
				</catagory>
				<catagory id="STO">
					<others />
				</catagory>
				<catagory id="PHA" acType="blacklist">
					<catagory id="PCM02">
						<others />
					</catagory>
				</catagory>
				<catagory id="SUP_ONE">
					<others />
				</catagory>
				<catagory id="SUP_TWO">
					<others />
				</catagory>
			</app>
			<app id="chis.application.healthmanage.HEALTHMANAGE">
				<catagory id="WL">
					<module id="A01">
						<others />
					</module>
					<module id="A01_1">
						<others />
					</module>
				</catagory>
				<catagory id="HR">
					<module id="B36">
                        <others/>
                    </module>
                    <module id="B12">
                        <others />
                    </module>
                    <module id="B341f">
                        <others/>
                    </module>
                    <module id="B341M">
                        <others/>
                    </module>
                    <module id="D20_2_1">
                        <others />
                    </module>
                    <module id="B17-2-1">
                        <others />
                    </module>
                    <module id="B18-2-1">
                        <others />
                    </module>
                    <module id="B01">
                        <others />
                    </module>
                    <module id="B01_">
                        <others />
                    </module>
                    <module id="B011">
                        <others />
                    </module>
                    <module id="B011_1">
                        <others />
                    </module>
                    <module id="B011_2">
                        <others />
                    </module>
                    <module id="B011_2_1">
                        <others />
                    </module>
                    <module id="B011_3">
                        <others />
                    </module>
                    <module id="B011_3_1">
                        <others />
                    </module>
                    <module id="B011_5">
                        <others />
                    </module>
                    <module id="B011_51">
                        <others />
                    </module>
                    <module id="B011_5_1_1">
                        <others />
                    </module>
                    <module id="B011_6">
                        <others />
                    </module>
                    <module id="B011_6_1">
                        <others />
                    </module>
                    <module id="B011_6_2">
                        <others />
                    </module>
                    <module id="B0110">
                        <others />
                    </module>
                    <module id="B011_10">
                        <others />
                    </module>
                    <module id="B0110_1">
                        <others />
                    </module>
                    <module id="B0110_2">
                        <others />
                    </module>
                    <module id="B0110_7">
                        <others />
                    </module>
                    <module id="B0110_8">
                        <others />
                    </module>
                    <module id="B0110">
                        <others />
                    </module>
                    <module id="B08">
                        <others />
                    </module>
                    <module id="B081">
                        <action id="graphic" />
                        <action id="dsfz"  />
                        <action id="showModule" />
                        <action id="modify" />
                        <action id="writeOff"  />
                        <action id="print" />
						<action id="ehrview"  />
						<action id="excel"  />
                    </module>

                    <module id="B081f">
                        <others/>
                    </module>
                    <module id="B08M">
                        <others/>
                    </module>
                    <module id="B34">
                        <others />
                    </module>
                    <module id="B341">
                        <others />
                    </module>
                    <module id="B34101">
                        <others />
                    </module>
                    <module id="B3410101">
                        <others />
                    </module>
                    <module id="B3410102">
                        <others />
                    </module>
                    <module id="B34102">
                        <others/>
                    </module>
                    <module id="B184">
                        <others />
                    </module>
                    <module id="B181">
                        <others />
                    </module>
                    <module id="B182">
                        <others />
                    </module>
                    <!--<module id="B05">
                        <others/>
                    </module>-->
                    <module id="B051">
                        <others/>
                    </module>
                    <module id="A04">
                        <others />
                    </module>
                    <module id="A04_1">
                        <others />
                    </module>
                    <module id="A07">
                        <others />
                    </module>
                    <module id="A07_1">
                        <others />
                    </module>
                    <module id="B07">
                        <others />
                    </module>
                    <module id="B09">
                        <others />
                    </module>
                    <module id="B10">
                        <others />
                    </module>
                    <module id="B1001">
                        <others />
                    </module>
                    <module id="B11">
                        <others />
                    </module>
                    <module id="B1101">
                        <others />
                    </module>
                    <module id="B16">
                        <others />
                    </module>
                    <module id="B16-1">
                        <others />
                    </module>
                    <module id="HC01">
                        <others />
                    </module>
                    <module id="HC0101">
                        <others />
                    </module>
                    <module id="HC01_EHR">
                        <others/>
                    </module>
                    <module id="HC0101_GUIDE">
                        <others/>
                    </module>
                    <module id="YB01">
                        <others/>
                    </module>
                    <module id="D20">
                        <others />
                    </module>
                    <module id="D20_1">
                        <others />
                    </module>
                    <module id="D20_2">
                        <others />
                    </module>
                    <module id="D20_2_2">
                        <others />
                    </module>
                    <module id="D20_2_3">
                        <others />
                    </module>
                    <module id="D20_2_4">
                        <others />
                    </module>
                    <module id="D20_2_5">
                        <others />
                    </module>
                    <module id="D20_2_6">
                        <others />
                    </module>
                    <module id="D20_2_6_1">
                        <others />
                    </module>
                    <module id="D20_2_6_1_1">
                        <others />
                    </module>
                    <module id="D20_2_6_2">
                        <others />
                    </module>
                    <module id="D20_2_6_2_1">
                        <others />
                    </module>
                    <module id="D20_2_7">
                        <others />
                    </module>
                    <module id="D20_2_8">
                        <others />
                    </module>
                    <module id="D20_2_8_1">
                        <others />
                    </module>
                    <module id="HC04">
                        <others/>
                    </module>
                    <module id="B17">
                        <others />
                    </module>
                    <module id="B17-1">
                        <others />
                    </module>
                    <module id="B17-2">
                        <others />
                    </module>
                    <module id="B17-2-2">
                        <others />
                    </module>
                    <module id="B18">
                        <others />
                    </module>
                    <module id="B18-1">
                        <others />
                    </module>
                    <module id="B18-2">
                        <others />
                    </module>
                    <module id="B18-2-2">
                        <others />
                    </module>
                    <module id="B184">
                        <others />
                    </module>
                    <module id="B182">
                        <others />
                    </module>
				</catagory>
				<catagory id="SCM" >
					<others />
				</catagory>
			</app>
			<app id="chis.application.diseasemanage.DISEASEMANAGE">
				<catagory id="OHR">
					<others />
				</catagory>
				<catagory id="HY">
                    <module id="C30_list">
                        <others/>
                    </module>
                    <module id="C30_form">
                        <others/>
                    </module>
                    <module id="C30">
                        <others/>
                    </module>
                    <module id="C32">
                        <others/>
                    </module>
                    <module id="C32-1">
                        <others/>
                    </module>
                    <module id="C32-2">
                        <others/>
                    </module>
                    <module id="C17">
                        <others/>
                    </module>
                    <module id="C17-1">
                        <others/>
                    </module>
                    <module id="C18-1">
                        <others/>
                    </module>
                    <module id="C18-1-3">
                        <others/>
                    </module>
                    <module id="C18-2">
                        <others/>
                    </module>
                    <module id="C20">
                        <others/>
                    </module>
                    <module id="C20-1">
                        <others/>
                    </module>
                    <module id="C20-2">
                        <others/>
                    </module>
                    <module id="C20-2-1">
                        <others/>
                    </module>
                    <module id="C20-2-2">
                        <others/>
                    </module>
                    <module id="C19">
                        <others/>
                    </module>
                    <module id="C19-1">
                        <others/>
                    </module>
                    <module id="C19-1-1">
                        <others/>
                    </module>
                    <module id="C19-1-2">
                        <others/>
                    </module>
                    <module id="D01">
                        <others/>
                    </module>
                    <module id="D01-hr">
                        <others/>
                    </module>
                    <module id="A05">
                        <others/>
                    </module>
                    <module id="C21">
                        <others/>
                    </module>
                    <module id="C22">
                        <others/>
                    </module>
                    <module id="C23">
                        <others/>
                    </module>
                    <module id="D0-1">
                        <others/>
                    </module>
                    <module id="D11-6">
                        <others/>
                    </module>
                    <module id="D11-6-0">
                        <others/>
                    </module>
                    <module id="D11-6-1">
                        <others/>
                    </module>
                    <module id="D11-6-2">
                        <others/>
                    </module>
                    <module id="D11-6-3">
                        <others/>
                    </module>
                    <module id="D11-6-4">
                        <others/>
                    </module>
                    <module id="D0-1-1">
                        <others/>
                    </module>
                    <module id="D0-1-1-1">
                            <others />
                        </module>
                    <module id="D0-1-1-2">
                        <others/>
                    </module>
                    <module id="D0-1-1-2-0">
                        <others/>
                    </module>
                    <module id="D0-1-2">
                        <others/>
                    </module>
                    <module id="D0-1-2-1">
                        <others/>
                    </module>
                    <module id="D0-1-2-2">
                        <others/>
                    </module>
                    <module id="D0-1-3">
                        <others/>
                    </module>
                    <module id="HY01_01">
                        <others/>
                    </module>
                    <module id="D0-2-0">
                        <others/>
                    </module>
                    <module id="D0-2-1">
                        <others/>
                    </module>
                    <module id="D0-2-2">
                        <others/>
                    </module>
                    <module id="D0-2-2-1">
                        <others/>
                    </module>
                    <module id="D0-2-3">
                        <others/>
                    </module>
                    <module id="D0-2-4">
                        <others/>
                    </module>
                    <module id="DHI_01">
                        <others/>
                    </module>
                    <module id="DHI_01_01">
                        <others/>
                    </module>
                    <module id="DHI_01_02">
                        <others/>
                    </module>
                    <module id="D0-1-4">
                        <others/>
                    </module>
                    <module id="D02">
                        <others/>
                    </module>
                    <module id="D02-list">
                        <others/>
                    </module>
                    <module id="D03">
                        <others/>
                    </module>
                    <module id="D03-list">
                        <others/>
                    </module>
					<module id="D08">
						<others/>
					</module>
                    <module id="C18">
                        <others/>
                    </module>
                    <module id="D32">
                        <others/>
                    </module>
                    <module id="D32-1">
                        <action id="print"/>
                    </module>
                    <module id="D32-2">
                        <action id="cancel"/>
                    </module>
                    <module id="C24">
                        <others/>
                    </module>
                    <module id="C9">
                        <others/>
                    </module>
                    <module id="C10">
                        <others/>
                    </module>
				</catagory>
				<catagory id="DBS">
	                    <module id="D20">
    					    <others />
    					</module>
    					<module id="D20-1">
    					    <others />
    					</module>
    					<module id="D20-2">
    					    <others />
    					</module>
    					<module id="D01">
    					    <others />
    					</module>
    					<module id="D01-1">
    					    <others />
    					</module>
    					<module id="D01-2">
    					    <others />
    					</module>
    					<module id="D01-3">
    					    <others />
    					</module>
    					<module id="D01-4">
    					    <others />
    					</module>
    					<module id="D04">
    					    <others />
    					</module>
    					<module id="D04-1">
    					    <others />
    					</module>
    					<module id="D21">
    					    <others />
    					</module>
    					<module id="D21-1">
    					    <others />
    					</module>
    					<module id="D22">
    					    <others />
    					</module>
    					<module id="D23">
    					    <others />
    					</module>
    					<module id="D11">
    					    <others />
    					</module>
    					<module id="D11-6">
    					    <others />
    					</module>
    					<module id="D11-6-0">
    					    <others />
    					</module>
    					<module id="D11-6-1">
    					    <others />
    					</module>
    					<module id="D11-6-2">
    					    <others />
    					</module>
    					<module id="D11-6-3">
    					    <others />
    					</module>
    					<module id="D11-6-4">
    					    <others />
    					</module>
    					<module id="D11-1">
    					    <others />
    					</module>
    					<module id="D11-1-1">
    					    <others />
    					</module>
    					<module id="D11-1-2">
    					    <others />
    					</module>
    					<module id="D11-2">
    					    <others />
    					</module>
    					<module id="D11-2-1">
    					    <others />
    					</module>
    					<module id="D11-2-2">
    					    <others />
    					</module>
    					<module id="D11-3">
    					    <others />
    					</module>
    					<module id="D11-3-7">
    					    <others />
    					</module>
    					<module id="D11-3-7-1">
    					    <others />
    					</module>
    					<module id="D11-3-7-2">
    					    <others />
    					</module>
    					<module id="D11-3-0">
    					    <others />
    					</module>
    					<module id="D11-3-1">
    					    <others />
    					</module>
    					<module id="D11-3-1-1">
    					    <others />
    					</module>
    					<module id="D11-3-2">
    					    <others />
    					</module>
    					<module id="D11-3-5">
    					    <others />
    					</module>
    					<module id="D11-3-3">
    					    <others />
    					</module>
    					<module id="D11-3-4">
    					    <others />
    					</module>
    					<module id="D11-4">
    					    <others />
    					</module>
    					<module id="D11-4-1">
    					    <others />
    					</module>
    					<module id="D11-4-1-1">
    					    <others />
    					</module>
    					<module id="D11-4-4">
    					    <others />
    					</module>
    					<module id="D11-5">
    					    <others />
    					</module>
    					<module id="D11-5-0">
    					    <others />
    					</module>
    					<module id="D11-5-1">
    					    <others />
    					</module>
    					<module id="D05">
    					    <others />
    					</module>
    					<module id="D05-list">
    					    <others />
    					</module>
						<module id="D08">
							<others/>
						</module>
    					<module id="D17">
    					    <others />
    					</module>
    					<module id="D17-1">
    					    <others />
    					</module>
    					<module id="D18">
    					    <others />
    					</module>
    					<module id="D18-1">
    					    <others />
    					</module>
    					<module id="D18-1-1">
    					    <others />
    					</module>
    					<module id="D18-1-2">
    					    <others />
    					</module>
    					<module id="D18-1-3">
    					    <others />
    					</module>
    					<module id="D18-2">
    					    <others />
    					</module>
    					<module id="D19">
    					    <others />
    					</module>
    					<module id="D19-1">
    					    <others />
    					</module>
    					<module id="D19-1-1">
    					    <others />
    					</module>
    					<module id="D19-1-2">
    					    <others />
    					</module>
    					<module id="D33">
    					    <others />
    					</module>
    					<module id="D33-1">
    					    <others />
    					</module>
    					<module id="D33-2">
    					    <others />
    					</module>
    					<module id="D25">
    					    <others />
    					</module>
    					<module id="D24">
    					    <others />
    					</module>
    					<module id="C9">
    					    <others />
    					</module>
    					<module id="C10">
    					    <others />
    					</module>
				</catagory>
				<catagory id="TCM">
					<others/>
				</catagory>

			</app>
			
			<app id="chis.application.cdh.CDH">
				<catagory id="CDH">
					<others />
				</catagory>
			</app>
			
			<app id="chis.application.diseasecontrol.DISEASECONTROL">
				<catagory id="IDR">
					<others />
				</catagory>
			</app>
			<app id="chis.application.healthcheck.HEALTHCHECK">
				<catagory id="PER">
					<others />
				</catagory>
			</app>
			<app id="chis.application.scm.SCM">
				<catagory id="SCM">
					<others />
				</catagory>
			</app>
			<app id="chis.application.hc.HC">
				<catagory id="HC">
					<others />
				</catagory>
			</app>
			<app id="chis.application.hy.HY">
				<catagory id="HY">
					<others />
				</catagory>
			</app>

			<app id="chis.application.fhr.FHR">
				<catagory id="FHR">
					<others />
				</catagory>
			</app>



			<app id="phis.application.pha.PHA">
				<catagory id="PHA">
					<module id="PHA1201010201">
						<others />
					</module>
				</catagory>

			</app>
		</apps>
		
	</accredit>
</role>
