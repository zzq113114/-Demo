<?xml version="1.0" encoding="UTF-8"?>
<application id="phis.application.cnd.CND" name="表达式(CND)">
	<catagory id="CND_1" name="基础用法">
		<module id="CND01" name="用法介绍"
			script="phis.application.cnd.script.CndDesc">
		</module>
		<module id="CND02" name="初始Cnd"
			script="phis.application.cnd.script.CndListDemo_1">
			<properties>
				<p name="entryName">phis.application.cnd.schemas.MS_BRDA</p>
			</properties>
			<action id="logonOut" name="登出" iconCls="create"/>
			<action id="async" name="异步" />
			<action id="sync" name="同步" />
			<action id="screen" name="截图" />
		</module>
		<module id="CND03" name="动态Cnd"
			script="phis.application.cnd.script.CndListDemo_2">
			<properties>
				<p name="entryName">phis.application.cnd.schemas.MS_BRDA</p>
			</properties>
			<action id="BRXZ" name="病人性质过滤" iconCls="create"/>
			<action id="CSNY" name="出生年月过滤" iconCls="create"/>
			<action id="reset" name="重置cnd条件" iconCls="create"/>
		</module>
		
	</catagory>
</application>