$package("phis.script")

$import("util.Accredit", "phis.script.SimpleForm",
    "org.ext.ux.layout.TableFormLayout")

phis.script.TableForm = function (cfg) {
    this.colCount = 3;
    this.autoFieldWidth = true;
    this.disAutoHeight = false;
    phis.script.TableForm.superclass.constructor.apply(this, [cfg])
}
Ext.extend(phis.script.TableForm, phis.script.SimpleForm, {
    initPanel: function (sc) {
        if (this.form) {
            if (!this.isCombined) {
                this.addPanelToWin();
            }
            return this.form;
        }
        var schema = sc
        if (!schema) {
            var re = util.schema.loadSync(this.entryName);
            if (re.code == 200) {
                schema = re.schema;
            } else {
                this.processReturnMsg(re.code, re.msg, this.initPanel)
                return;
            }
        }
        var ac = util.Accredit;
        var defaultWidth = this.fldDefaultWidth || 200
        var items = schema.items
        if (!this.fireEvent("changeDic", items)) {
            return
        }
        var colCount = this.colCount;

        var table = {
            layout: 'tableform',
            layoutConfig: {
                columns: colCount,
                tableAttrs: {
                    border: 0,
                    cellpadding: '2',
                    cellspacing: "2"
                }
            },
            items: []
        }
        if (!this.autoFieldWidth) {
            var forceViewWidth = (defaultWidth + (this.labelWidth || 80))
                * colCount
            table.layoutConfig.forceWidth = forceViewWidth
        }
        var size = items.length
        for (var i = 0; i < size; i++) {
            var it = items[i]
            if ((it.display == 0 || it.display == 1) || !ac.canRead(it.acValue)) {
                continue;
            }
            var f = this.createField(it)
            f.labelSeparator = ":"
            f.index = i;
            f.anchor = it.anchor || "100%"
            delete f.width

            f.colspan = parseInt(it.colspan)
            f.rowspan = parseInt(it.rowspan)

            if (!this.fireEvent("addfield", f, it)) {
                continue;
            }
            table.items.push(f)
        }

        var cfg = {
            buttonAlign: 'center',
            labelAlign: this.labelAlign || "left",
            labelWidth: this.labelWidth || 80,
            frame: true,
            shadow: false,
            border: false,
            collapsible: false,
            //autoWidth : true,
            autoHeight: true,
            autoScroll: true,
            floating: false
        }

        if (this.isCombined) {
            cfg.frame = true
            cfg.shadow = false
            // cfg.width = this.width
            cfg.height = this.height
        } else {
            //cfg.autoWidth = true
            cfg.autoHeight = true
        }
        if (this.disAutoHeight) {
            delete cfg.autoHeight;
        }
        this.initBars(cfg);
        Ext.apply(table, cfg)
        this.expansion(table);// add by yangl
        this.form = new Ext.FormPanel(table)
        this.form.on("afterrender", this.onReady, this)

        this.schema = schema;
        this.setKeyReadOnly(true)
        if (!this.isCombined) {
            this.addPanelToWin();
        }
        return this.form
    },
    expansion: function (cfg) {//

    }

});
