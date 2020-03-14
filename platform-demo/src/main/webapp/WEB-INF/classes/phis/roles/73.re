<?xml version="1.0" encoding="UTF-8"?>
<role id="phis.73" name="医技角色" type="post" parent="base">
	<accredit>
		<apps acType="whitelist">
			<app id="phis.application.menu.COMM">
				<catagory id="PUB">
					<others />
				</catagory>
			</app>
			<app id="phis.application.top.TOP">
				<catagory id="TOPFUNC">
					<module id="MedicalSwitch">
						<others />
					</module>
				</catagory>
			</app>
			<app id="phis.application.menu.QKZL" acType="whitelist">
				<catagory id="MED" acType="blacklist">
					<module id="MED04">
						<others />
					</module>
					<module id="MED03">
						<others />
					</module>
				</catagory>
			</app>
			<app id="phis.application.menu.TJFX">
				<catagory id="MED">
					<others />
				</catagory>
			</app>
			<app id="phis.application.sys.SYS" acType="whitelist">
				<catagory id="YJ_CFG">
					<others />
				</catagory>
			</app>
		</apps>
		<service acType="whitelist"></service>
	</accredit>
</role>
