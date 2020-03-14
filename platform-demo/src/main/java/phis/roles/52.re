<?xml version="1.0" encoding="UTF-8"?>

<role id="phis.52" name="住院护士角色" parent="base" version="1338862773000">
	<accredit>
		<apps acType="whitelist">
			<app id="phis.application.menu.COMM">
				<catagory id="PUB">
					<others />
				</catagory>
			</app>
			<app id="phis.application.top.TOP" acType="whitelist">
				<catagory id="TOPFUNC">
					<module id="WardSwitch">
						<others />
					</module>
				</catagory>
			</app>
			 
			<app id="phis.application.menu.ZYGL" >
				<catagory id="WAR" acType="blacklist">
					<module id="WAR20">
						<others />
					</module>
					<module id="WAR01">
						<others />
					</module>
					<module id="WAR12">
						<others />
					</module>
					<module id="WAR32">
						<others />
					</module>
					<module id="WAR34">
						<others />
					</module>
					<module id="WAR48">
						<others />
					</module>
					<module id="WAR50">
						<others />
					</module>
					<module id="WAR51">
						<others />
					</module>
					
					<module id="WAR38">
						<others />
					</module>
					<module id="WAR54">
						<others />
					</module>
					<module id="WAR55">
						<others />
					</module>
					<module id="WAR58">
						<others />
					</module>
										
					<module id="WAR82">
						<others />
					</module>
					<module id="WAR94">
						<others />
					</module>
					<module id="WAR99">
						<others />
					</module>
					<module id="WAR101">
						<others />
					</module>
					<module id="CFG13">
						<others />
					</module>
					<module id="CFG1301">
						<others />
					</module>

				</catagory>
				 <catagory id="MOB" >
				 <others />
				 </catagory>
				 <catagory id="STMZY" >
				 <others />
				 </catagory>
			</app>
			<app id="phis.application.war.WAR">
				<catagory id="WAR">
					<module id="WAR000103">
				      <others />
			       	</module>
			       	<module id="WAR00010301">
				      <others />
			       	</module>
			       	<module id="WAR000104">
				      <others />
			       	</module>
				</catagory>
			</app>
			<app id="phis.application.menu.TJFX">
				<catagory id="HOS">
					<others />
				</catagory>
			</app>
			<app id="phis.application.sys.SYS">
				<catagory id="WAR_CFG">
					<module id="WAR01">
						<others />
					</module>
					<module id="WAR48">
						<others />
					</module>
					<module id="WAR4801">
						<others />
					</module>
					<module id="WAR4802">
						<others />
					</module>
					<module id="WAR94">
						<others />
					</module>
					<module id="WAR12">
						<others />
					</module>
					<module id="WAR1201">
						<others />
					</module>
					<module id="WAR1202">
						<others />
					</module>
					<module id="WAR120101">
						<others />
					</module>
					<module id="REG32">
						<others />
					</module>
					<module id="REG33_1">
						<others />
					</module>
					<module id="REG33_2">
						<others />
					</module>
					<module id="REG3301">
						<others />
					</module>
					<module id="REG3302">
						<others />
					</module>
				</catagory>
				
			</app>
			<!--zyang注释妇幼保健
			<app id="chis.application.gynecology.GYNECOLOGY">
				<catagory id="CDH">
					<module id="H0111_1">
						<others />
					</module>
					<module id="H0111_2">
						<others />
					</module>
					<module id="H0111_2_1">
						<others />
					</module>
					<module id="H03">
						<others />
					</module>
					<module id="H97">
						<others />
					</module>
					<module id="H98">
						<others />
					</module>
					<module id="H99">
						<others />
					</module>
					<module id="H97-1">
						<others />
					</module>
					<module id="H98-1">
						<others />
					</module>
					<module id="H99-1">
						<others />
					</module>
					<module id="H13-1">
						<others />
					</module>
					<module id="H97-2">
						<others />
					</module>
					<module id="H97-3">
						<others />
					</module>
				</catagory>
			</app>

			<app id="chis.application.diseasecontrol.DISEASECONTROL">
				<catagory id="IDR">
					<module id="DCIDR_03">
						<others />
					</module>
				</catagory>
			</app>
			<app id="chis.application.hc.HC">
				<catagory id="HC">
					<others />
				</catagory>
			</app>
			-->
		</apps>
	</accredit>
</role>
