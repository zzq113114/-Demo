$package("phis.application.pha.script")
$import("phis.script.SimpleList", "phis.script.rmi.jsonRequest")

phis.application.pha.script.PharmacyCheckInList = function(cfg) {
	cfg.width = 1024;
	cfg.height = 550;
	cfg.cnds = ['eq', ['$', 'RKPB'], ['i', 1]]
	cfg.modal  = true;
	cfg.autoLoadData = false;
	cfg.importMethods=["createSpinner","simpleLodopPrint","doAjax"];
	phis.application.pha.script.PharmacyCheckInList.superclass.constructor.apply(this,
			[cfg])
	this.on("beforeclose", this.doCancel, this);
}
Ext.extend(phis.application.pha.script.PharmacyCheckInList, phis.script.SimpleList,
		{
			initPanel:function(){
			this.importMethod();
			return phis.application.pha.script.PharmacyCheckInList.superclass.initPanel.call(this);
			},
			// 生成日期框
			getCndBar : function(items) {
				this.simple=this.createSpinner("month","财务月份","storeDate");
				return [this.simple];
			},
			// 条件查询
			doCndQuery : function(button, e, addNavCnd) {
				var initCnd = this.initCnd;
				var simple = this.simple;
				var cnd = null;
				if (simple) {
					var stroeDate = simple.getValue();
					if (stroeDate != null && stroeDate != "") {
						var begin = stroeDate + "-01";// 页面财务月份起始时间
						var dateSplit = stroeDate.split("-");
						var end = this.getLastDay(dateSplit[0], dateSplit[1]);// 页面财务月份的结束时间
						var prior_begin = this.getLastMonth(dateSplit[0],
								dateSplit[1]);// 界面财务月份前一个月的月初时间
						var body = {"begin":begin,"end":end,"prior_begin":prior_begin};
						var r = this.doAjax(this.serviceId,this.dateQueryActionId,body);
						if (r.code > 300) {
							this.processReturnMsg(r.code, r.msg,
									this.onBeforeSave);
							return;
						} else {
							var dates = r.json.body;
							if (dates.length != 2) {
								return;
							}
							cnd = [
									'and',
									[
											'ge',
											['$',
													"str(RKRQ,'yyyy-mm-dd hh24:mi:ss')"],
											['s', dates[0]]],
									[
											'le',
											['$',
													"str(RKRQ,'yyyy-mm-dd hh24:mi:ss')"],
											['s', dates[1]]]];
							if (initCnd) {
								cnd = ['and', initCnd, cnd];
							}

						}

					}
				}
				// 没选财务月份不让查询
				if (cnd == null) {
					return;
				}
				cnd = ['and', cnd, this.cnds];
				if (this.checkInWayValue) {
					cnd = ['and',
							['eq', ['$', 'RKFS'], ['i', this.checkInWayValue]],
							cnd];
				}
				this.requestData.cnd = cnd;
				this.refresh();
			},
			// 页面打开时记录前增加确认图标
			onRenderer : function(value, metaData, r) {
				return "<img src='" + ClassLoader.appRootOffsetPath
									+ "resources/phis/resources/images/grid.png'/>"
			},
			// 页面打开时默认的时间
			getDate : function() {
				var r=this.doAjax(this.serviceId,this.initDateQueryActionId);
				if (r.code > 300) {
					this.processReturnMsg(r.code, r.msg, this.getDate);
					return;
				}
				return r.json.date;
			},
			// 获取该月最后一天
			getLastDay : function(year, month) {
				var new_year = year; // 取当前的年份
				var new_month = month;// 取下一个月的第一天，方便计算（最后一天不固定）
				if (new_month >= 12) // 如果当前大于12月，则年份转到下一年
				{
					new_month -= 12; // 月份减
					new_year++; // 年份增
				}
				var newnew_date = new Date(new_year, new_month, 1); // 取当年当月中的第一天
				return year
						+ "-"
						+ month
						+ "-"
						+ (new Date(newnew_date.getTime() - 1000 * 60 * 60 * 24))
								.getDate();// 获取当月最后一天日期
			},
			// 获取界面财务月份前一个月的月初时间
			getLastMonth : function(year, month) {
				var new_year = year; // 取当前的年份
				var new_month = month - 1;// 取上一个月
				if (month == 1) {
					new_month = 12;
					new_year--;
				}
				return new_year + "-" + new_month + "-01";
			},
			// 查看
			doLook : function() {
				var r = this.getSelectedRecord()
				if (r == null) {
					return;
				}
				var initDataBody = {"YFSB":r.data.YFSB,"RKFS":r.data.RKFS,"RKDH":r.data.RKDH};
				this.storageModule = this.createModule("storageModule",
						this.readRef);
				//监听保存和关闭事件
				this.storageModule.on("save", this.onSave, this);
				this.storageModule.on("winClose", this.onClose, this);
				var win = this.getWin();
				win.add(this.storageModule.initPanel());
				win.show()
				win.center()
				if (!win.hidden) {
					this.storageModule.doRead(initDataBody);
				}

			},
			//双击查看
			onDblClick : function(grid, index, e) {
				var item = {};
				item.text = "查看";
				item.cmd = "look";
				this.doAction(item, e)

			},
			//详细界面关闭
			onClose : function() {
				this.getWin().hide();
			},
			doCancel : function() {
				if (this.storageModule) {
					return this.storageModule.doClose();
				}
			},
			doPrint : function() {
				var r = this.getSelectedRecord()
				if (r == null) {
					MyMessageTip.msg("提示", "打印失败：无效的入库单信息!", true);
					return;
				}
				var url = "resources/phis.prints.jrxml.yfyprkd.print?silentPrint=1&temp=" + new Date().getTime()
						+ "&yfsb="+r.data.YFSB+"&rkfs="+r.data.RKFS+"&rkdh="+r.data.RKDH;
				var LODOP = getLodop();
				LODOP.PRINT_INIT("打印控件");
				LODOP.SET_PRINT_PAGESIZE("0", "", "", "");
				var rehtm = util.rmi.loadXML({
							url : url,
							httpMethod : "get"
						});
				LODOP.ADD_PRINT_HTM("0", "0", "100%", "100%", rehtm);
				LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", "Full-Width");
				//预览
				LODOP.PREVIEW();
				//直接打印
				//LODOP.PRINT();
			}
		})