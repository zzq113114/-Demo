<?xml version="1.0" encoding="UTF-8"?>

<role id="phis.79" name="DEMO事例" parent="base" pageCount="2"
	version="1388456433586">
	<accredit>
		<!--<apps acType="whitelist">-->
			<!--<app id="phis.application.menu.QKZL">-->
				<!--<catagory id="STM" acType="blacklist">-->
					<!--<module id="STM12">-->
						<!--<others />-->
					<!--</module>-->
				<!--</catagory>-->
			<!--</app>-->
			<!--<app id="phis.application.menu.TJFX">-->
				<!--<catagory id="STM" acType="whitelist">-->
					<!--<module id="STM12">-->
						<!--<others />-->
					<!--</module>-->
				<!--</catagory>-->
			<!--</app>-->
		<!--</apps>-->
		<apps acType="whitelist">
			<app id="phis.application.eg.EG">
				<others />
			</app>
			<app id="phis.application.cnd.CND">
				<catagory id="CND_1">
					<others />
				</catagory>
			</app>
			<app id="phis.application.evt.EVT">
				<catagory id="EVT_1">
					<module id="EVT_1_1" />
					<module id="EVT_1_2" >
						<action id="create" />
					</module>
				</catagory>
			</app>
			<app id="phis.application.sys.SYS">
				<catagory id="REG">
					<others />
				</catagory>
				<catagory id="SYS">
					<others />
				</catagory>
			</app>
		</apps>
		<service acType="whitelist"></service>
		<storage acType="whitelist">
			<store id="phis.application.cfg.schemas.GY_YLMX_DR" acValue="1111">
				<conditions>
					<condition type="filter">['eq',['$','a.JGID'],["$",'%user.manageUnit.id']]
					</condition>
				</conditions>
				<items>
					<others acValue="1111" />
				</items>
			</store>
			<others acValue="1111"/>
		</storage>
	</accredit>
</role>
