$package("phis.application.eg.script")
$import("phis.script.SimpleList")

phis.application.eg.script.Cf01List = function(cfg) {
    phis.application.eg.script.Cf01List.superclass.constructor.apply(this,
        [cfg])
    this.autoLoadData=false;
}
Ext.extend(phis.application.eg.script.Cf01List, phis.script.SimpleList, {
            doCs:function () {
               this.loadData();
            },
            loadData:function(){
                this.requestData.serviceId="phis.cxService";
                this.requestData.serviceAction="cxcf01";
                phis.application.eg.script.Cf01List.superclass.loadData.call(this);
            }

}
)