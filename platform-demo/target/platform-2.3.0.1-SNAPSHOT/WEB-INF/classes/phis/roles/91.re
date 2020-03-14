<?xml version="1.0" encoding="UTF-8"?>

<role id="phis.91" name="门诊护士" parent="base" pageCount="2"
	version="1388456433586">
	<accredit>
		<apps acType="whitelist">
			<app id="phis.application.menu.QKZL">
				<catagory id="STM" acType="blacklist">
					<module id="STM12">
						<others />
					</module>
				</catagory>
			</app>
			<app id="phis.application.menu.TJFX">
				<catagory id="STM" acType="whitelist">
					<module id="STM12">
						<others />
					</module>
				</catagory>
			</app>
			<app id="phis.application.sys.SYS">
				<catagory id="WAR_CFG" acType="whitelist">
					<!--<module id="REG32">
						<others />
					</module>-->
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
		</apps>
	</accredit>
</role>
