<?xml version="1.0" encoding="UTF-8"?>

<role id="chis.3301" name="系统管理员" parent="chis.base">
    <accredit>
        <apps acType="whitelist">
            <app id="phis.application.sys.SYS">
                <catagory id="REG" acType="blacklist">
                    <module id="REG_08"/>
                </catagory>
                <catagory id="SYS">
                    <others />
                </catagory>
                <catagory id="LOG">
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
