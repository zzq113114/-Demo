<?xml version="1.0" encoding="UTF-8"?>
<role id="phis.56" name="住院收费角色" type="post" parent="base">
	<accredit>
		<apps>
			<app id="phis.application.menu.COMM">
				<catagory id="PUB">
					<others />
				</catagory>
			</app>
			<app id="phis.application.top.TOP" acType="whitelist">
				<catagory id="TOPFUNC">
					<module id="DepartmentSwitch_in">
						<others />
					</module>
				</catagory>
			</app>
			<app id="phis.application.menu.ZYGL">
				<catagory id="HOS">
					<others />
				</catagory>
			</app>
			<app id="phis.application.sys.SYS">
				<catagory id="HOS_CFG">
					<others />
				</catagory>
			</app>
			<app id="phis.application.menu.TJFX">
				<catagory id="HOS">
					<others />
				</catagory>
			</app>
		</apps>
		<service acType="whitelist"></service>
	</accredit>
</role>
