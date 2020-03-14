<?xml version="1.0" encoding="UTF-8"?>

<role id="phis.75" name="全科药房角色" parent="base" version="1388656923974">
	<accredit>
		<apps>
			<app id="phis.application.menu.COMM">
				<catagory id="PUB">
					<others />
				</catagory>
			</app>
			<app id="phis.application.top.TOP">
				<catagory id="TOPFUNC">
					<module id="PharmacySwitch">
						<others />
					</module>
				</catagory>
			</app>
			<app id="phis.application.menu.YYGL">
				<catagory id="PHA" acType="blacklist">
				  <others />
				</catagory>
				<catagory id="STM">
					<others/>
				</catagory>
			</app>
			<app id="phis.application.sys.SYS">
			 <catagory id="PHA" acType="blacklist">
			    <module id="PCM02">
			      <others />
		        </module>
			  </catagory>
			</app>
			<app id="phis.application.menu.TJFX">
				<catagory id="PHA">
					<others />
				</catagory>
			</app>
		</apps>
	</accredit>
</role>
