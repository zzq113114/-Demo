<?xml version="1.0" encoding="UTF-8"?>

<role id="phis.51" name="住院医生角色" parent="base" version="1338862773000">
    <accredit>
        <apps acType="whitelist">
            <app id="phis.application.menu.COMM">
                <catagory id="PUB">
                    <others/>
                </catagory>
            </app>
            <app id="phis.application.top.TOP">
                <catagory id="TOPFUNC">
                    <module id="WardSwitch">
                        <others/>
                    </module>
                </catagory>
            </app>
            <app id="phis.application.sur.SUR">
                <catagory id="SUR" acType="whitelist">
                    <others/>
                </catagory>
            </app>
            <app id="phis.application.menu.ZYGL">
                <catagory id="WAR" acType="blacklist">
                    <module id="YB0105">
                        <others/>
                    </module>
                    <module id="YB0106">
                        <others/>
                    </module>
                    <module id="YB0107">
                        <others/>
                    </module>
                    <module id="YB0108">
                        <others/>
                    </module>
                    <module id="WAR00">
                        <others/>
                    </module>
                    <module id="WAR01">
                        <others/>
                    </module>
                    <module id="WAR18">
                        <others/>
                    </module>
                    <module id="WAR04">
                        <others/>
                    </module>
                    <module id="WAR22">
                        <others/>
                    </module>
                    <module id="WAR11">
                        <others/>
                    </module>
                    <module id="WAR12">
                        <others/>
                    </module>
                    <module id="WAR32">
                        <others/>
                    </module>
                    <module id="WAR34">
                        <others/>
                    </module>
                    <module id="WAR48">
                        <others/>
                    </module>
                    <module id="WAR50">
                        <others/>
                    </module>
                    <module id="WAR51">
                        <others/>
                    </module>
                    <module id="WAR60">
                        <others/>
                    </module>
                    <module id="WAR38">
                        <others/>
                    </module>
                    <module id="WAR54">
                        <others/>
                    </module>
                    <module id="WAR55">
                        <others/>
                    </module>
                    <module id="WAR58">
                        <others/>
                    </module>
                    <module id="WAR60">
                        <others/>
                    </module>
                    <module id="WAR82">
                        <others/>
                    </module>
                    <module id="WAR94">
                        <others/>
                    </module>
                    <module id="WAR99">
                        <others/>
                    </module>
                    <module id="WAR101">
                        <others/>
                    </module>
                    <module id="WAR52">
                        <others/>
                    </module>
                    <module id="WAR98">
                        <others/>
                    </module>
                    <module id="WAR96">
                        <others/>
                    </module>
                    <module id="HOS07">
                        <others/>
                    </module>
                    <module id="WAR14">
                        <others/>
                    </module>
                    <module id="WAR08">
                        <others/>
                    </module>
                    <module id="WAR28">
                        <others/>
                    </module>

                </catagory>
                <catagory id="WARYS">
                    <module id="WAR000103">
                        <others/>
                    </module>
                    <module id="WAR00010301">
                        <others/>
                    </module>
                    <module id="WAR0001">
                        <others/>
                    </module>
                    <module id="WAR66">
                        <others/>
                    </module>
                    <module id="WAR0003">
                        <others/>
                    </module>

                </catagory>
                <catagory id="FSB">
                    <module id="FSB01">
                        <action id="add"/>
                        <action id="upd"/>
                        <action id="tjsq"/>
                        <action id="remove"/>
                        <action id="print"/>
                    </module>
                    <module id="FSB02">
                        <others/>
                    </module>
                    <module id="FSB0201">
                        <others/>
                    </module>
                    <module id="FSB03">
                        <others/>
                    </module>
                    <module id="FSB04">
                        <others/>
                    </module>
                    <module id="FSB0401">
                        <others/>
                    </module>
                    <module id="FSB0402">
                        <others/>
                    </module>
                    <module id="FSB0403">
                        <others/>
                    </module>
                    <module id="FSB06">
                        <others/>
                    </module>
                    <module id="FSB0601">
                        <others/>
                    </module>
                    <module id="FSB060101">
                        <others/>
                    </module>
                    <module id="FSB060102">
                        <others/>
                    </module>
                    <module id="FSB0602">
                        <others/>
                    </module>
                    <module id="FSB060201">
                        <others/>
                    </module>
                    <module id="FSB0603">
                        <others/>
                    </module>
                    <!-- 新增住院医生，中的住院病案首页功能模块 -->
                    <module id="WAR02">
                        <others/>
                    </module>
                    <module id="WAR000103">
                        <others/>
                    </module>
                    <module id="WAR00010301">
                        <others/>
                    </module>
                </catagory>
            </app>
            <app id="phis.application.sys.SYS">
                <catagory id="WAR_CFG">
                    <module id="WAR12">
                        <others/>
                    </module>
                    <module id="WAR1201">
                        <others/>
                    </module>
                    <module id="WAR120101">
                        <others/>
                    </module>
                    <module id="WAR1202">
                        <others/>
                    </module>
                    <module id="WAR94">
                        <others/>
                    </module>
                    <module id="WAR4801">
                        <others/>
                    </module>
                    <module id="WAR4802">
                        <others/>
                    </module>
                    <module id="WAR48">
                        <others/>
                    </module>
                    <module id="WAR4803">
                        <others/>
                    </module>

                </catagory>
            </app>
            <app id="phis.application.menu.TJFX">
                <catagory id="HOS">
                    <others/>
                </catagory>
            </app>
            <app id="phis.application.fsb.FSB">
                <catagory id="FSB">
                    <module id="FSB01">
                        <action id="add"/>
                        <action id="upd"/>
                        <action id="tjsq"/>
                        <action id="remove"/>
                        <action id="print"/>
                    </module>
                    <module id="FSB02">
                        <others/>
                    </module>
                    <module id="FSB0201">
                        <others/>
                    </module>
                    <module id="FSB03">
                        <others/>
                    </module>
                    <module id="FSB04">
                        <others/>
                    </module>
                    <module id="FSB0401">
                        <others/>
                    </module>
                    <module id="FSB0402">
                        <others/>
                    </module>
                    <module id="FSB0403">
                        <others/>
                    </module>
                    <module id="FSB06">
                        <others/>
                    </module>
                    <module id="FSB0601">
                        <others/>
                    </module>
                    <module id="FSB060101">
                        <others/>
                    </module>
                    <module id="FSB060102">
                        <others/>
                    </module>
                    <module id="FSB0602">
                        <others/>
                    </module>
                    <module id="FSB060201">
                        <others/>
                    </module>
                    <module id="FSB0603">
                        <others/>
                    </module>
                </catagory>
            </app>
            <app id="phis.application.ccl.CCL">
                <catagory id="CCL">
                    <others/>
                </catagory>
            </app>

        </apps>
    </accredit>
</role>
