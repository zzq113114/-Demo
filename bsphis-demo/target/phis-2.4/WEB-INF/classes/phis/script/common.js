$package("phis.script")

$import("util.schema.SchemaLoader", "phis.script.widgets.MyMessageTip",
		"phis.script.util.DateUtil", "phis.script.rmi.jsonRequest",
		"phis.script.rmi.miniJsonRequestSync",
		"phis.script.rmi.miniJsonRequestAsync", "phis.script.widgets.ymPrompt",
		"phis.script.widgets.PrintWin");
$styleSheet("phis.resources.css.app.desktop.Desktop")
$styleSheet("phis.resources.css.skin.qq.ymPrompt")
var phisDomain = "phis";
overrideFunc();
phis.script.common = {
	/**
	 * 系统日志记录
	 * 
	 * @param {}
	 *            module 对应的模块对象,便于日志分类
	 * @param {}
	 *            desc 自定义描述信息
	 * @param {}
	 *            snapshot 是否记录快照 可选值 auto: 支持canvas的浏览器以图片形式存储,否则以html文本存储;
	 *            image: 以图片形式存储,不支持canvas的浏览器则不记录快照; html: 以html方式存储快照
	 */
	oplog : function(module, desc, snapshot) {
		if (snapshot) {
			html2canvas(document.getElementById(Ext.getBody().first().id), {
						onrendered : function(canvas) {
							var data = canvas.toDataURL();
							// 存储日志信息
							phis.script.rmi.jsonRequest({
										serviceId : "phis.publicService",
										serviceAction : "saveOpLog",
										body : {
											moduleId : module.id,
											desc : desc || module.name,
											logInfo : data
										}
									}, function(code, msg, json) {
										if (code > 200) {
											MyMessageTip.msg("警告",
													"业务操作日志记录失败,请及时反馈管理员!",
													true);
										}
									}, this);
						}
					});
		} else {
			phis.script.rmi.jsonRequest({
						serviceId : "phis.publicService",
						serviceAction : "saveOpLog",
						body : {
							moduleId : module.id,
							desc : desc || module.name
						}
					}, function(code, msg, json) {
						if (code > 200) {
							MyMessageTip
									.msg("警告", "业务操作日志记录失败,请及时反馈管理员!", true);
						}
					}, this);
		}
	},
	/**
	 * 载入系统参数
	 * 
	 * @param body {
	 *            commons : ['MZYP', 'JSYP', 'XYF', 'ZYF', 'CYF'], privates
	 *            :['YYJGTS', 'YS_MZ_FYYF_XY', 'YS_MZ_FYYF_ZY'], personals : [] }
	 *            其中:commons为公有参数 privates为私有参数 personals个人参数(如医生医疗角色等)
	 */
	loadSystemParams : function(body) {
		var res = phis.script.rmi.miniJsonRequestSync({
					serviceId : "publicService",
					serviceAction : "loadSystemParams",
					body : body
				});
		var code = res.code;
		var msg = res.msg;
		if (code >= 300) {
			this.processReturnMsg(code, msg);
			return;
		}
		return res.json.body;
	},
	/**
	 * 业务类型 YWXH (必填) 病人ID BRID (需要对同一个病人做控制) 业务序号 SDXH (如就诊序号等)
	 * 
	 * @param {}
	 *            p
	 * @return {Boolean}
	 */
	bclLock : function(p, el) {
		if (!this.QYYWS) {
			var body = this.loadSystemParams({
						'privates' : ['QYYWS']
					});
			if (body) {
				this.QYYWS = body.QYYWS;
			}
		}
		if (this.QYYWS != '1')
			return true;
		var res = phis.script.rmi.miniJsonRequestSync({
					serviceId : "publicService",
					serviceAction : "bclLock",
					body : p
				});
		var code = res.code;
		var msg = res.msg;
		if (code > 200) {
			if (!el) {
				this.processReturnMsg(code, msg);
			} else {
				el.lock(msg);
				this.bclLocked = true;// 是否是被锁定状态(其它模块占用锁)
			}
			return false;
		}
		if (el) {
			el.unlock();
		}
		this.ownerLock = true;// 当前是否获取锁
		this.bclLocked = false;
		this.bclLockKey = res.json.body;
		return true
	},
	/**
	 * 业务类型 YWXH (必填)
	 * 
	 * @param {}
	 *            p
	 * @return {Boolean}
	 */
	bclUnlock : function(p, el) {
		if (!this.QYYWS) {
			var body = this.loadSystemParams({
						'privates' : ['QYYWS']
					});
			if (body) {
				this.QYYWS = body.QYYWS;
			}
		}
		if (this.QYYWS != '1')// 未开启业务锁或者当前模块没有获得锁
			return true;
		if (!this.ownerLock) {
			if (el) {
				el.unlock();
			}
			return true;
		}
		if ((p instanceof Array)) {
			for (var i = 0; i < p.length; i++) {
				p[i].JLXH = this.bclLockKey[p[i].YWXH];
			}
		} else {
			p.JLXH = this.bclLockKey[p.YWXH];
		}
		var res = phis.script.rmi.miniJsonRequestSync({
					serviceId : "publicService",
					serviceAction : "bclUnlock",
					body : p
				});
		var code = res.code;
		var msg = res.msg;
		if (code > 200) {
			if (!el) {
				this.processReturnMsg(code, msg);
			} else {
				el.unlock();
			}
			// this.bclLocked = false;
			return false;
		}
		this.ownerLock = false;
		this.bclLockKey = null;
		return true
	},
	/**
	 * 判断锁定信息有效性
	 * 
	 * @param {}
	 *            p
	 * @return {boolean} true表示当前锁定状态正确 false表示锁失效
	 */
	checkBclLock : function(p) {
		if (!this.QYYWS) {
			var body = this.loadSystemParams({
						'privates' : ['QYYWS']
					});
			if (body) {
				this.QYYWS = body.QYYWS;
			}
		}
		if (this.QYYWS != '1')
			return true;
		if ((p instanceof Array)) {
			for (var i = 0; i < p.length; i++) {
				p[i].bclLockKey = this.bclLockKey[p[i].YWXH];
			}
		} else {
			p.bclLockKey = this.bclLockKey[p.YWXH];
		}

		var res = phis.script.rmi.miniJsonRequestSync({
					serviceId : "publicService",
					serviceAction : "checkBclLock",
					body : p
				});
		var code = res.code;
		var msg = res.msg;
		if (code > 200) {
			this.processReturnMsg(code, msg);
			return false;
		}
		var body = res.json.body;
		if ((p instanceof Array)) {
			for (var i = 0; i < p.length; i++) {
				if (body[p[i].YWXH] < 1) {
					return false;
				}
			}
		} else {
			return body[p.YWXH] > 0;
		}
		return true
	},
	/**
	 * 默认实现方式，已实现以下功能，如特殊需求，可扩展此功能 //优先判断是否有直接的实现方法 如：ctrl_D : function(){} 规则
	 * 1、ctrl、shift、alt未组合键,组合建对应的方法格式为 ctrl_shift_alt_X(X为A-Z 0-9);
	 * 组合键数量可以自定义，但是顺序必须按示例所示，如 ctrl_D alt_F ctrl_shift_A
	 * 2、指定action的accessKey,如：accessKey="doSubmit" 则会调用对应module的doSubmit方法
	 * 3、默认仍采用以前的F1-F12的方式
	 * 
	 * 遗留问题：当快捷键调用的方法中存在同步的ajax请求时，时间无法中断，会调用到浏览器的快捷键
	 * 
	 * @param {}
	 *            keyCode 按键值
	 * @param {}
	 *            keyName 按键名称
	 * @return {Boolean} 返回false表示事件被监听，屏蔽浏览器按键事件
	 */
	shortcutKeyFunc : function(keyCode, keyName) {
		// 锁定窗口不响应事件
		if (this.bclLocked)
			return false;
		var actions = this.actions
		// MyMessageTip.msg("提示", "actions:" + Ext.encode(actions), true);
		// 优先当前模块的实现
		// 先处理按键直接对应的实现方法
		try {
			// 增加快捷键统一处理功能(总入口，由相应模块自己处理)
			if (eval("this.keyManageFunc")) {
				eval("this.keyManageFunc(keyCode,keyName)");
				return false;
			}
			// 单个事件实现
			if (eval("this." + keyName)) {
				eval("this." + keyName + "()");
				return false;
			}
		} catch (e) {
			return true;
		}
		// 处理按钮事件
		if (actions) {
			for (var j = 0; j < actions.length; j++) {
				if (actions[j].accessKey) {
					if (eval("this." + actions[j].accessKey)) {
						eval("this." + actions[j].accessKey + "(keyCode)");
						return false;
					}
				} else {// 默认取F1-F12
					if (this.noDefaultBtnKey)
						return false;
					var defaultKey = 112 + (this.buttonIndex || 0) + j;
					if (keyCode == defaultKey) {
						if (eval("this.btnAccessKeys")) {
							eval("this.doAction(this.btnAccessKeys[keyCode])");
						}
						return false;
					}
				}
			}
		}
		// MyMessageTip.msg("提示", "缓存的module：" + this.listenModules.length,
		// true);
		// 禁止多个module多窗口之间的传递
		// 遍历其它模块
		// if (this.listenModules) {
		// for (var i = 0; i < this.listenModules.length; i++) {
		// var m = this.listenModules[i];
		// // MyMessageTip.msg("提示", "next1", true);
		// if (!m.shortcutKeyFunc(keyCode, keyName)) {
		// return false;
		// }
		// }
		// // return false;
		// }
		return true;
	},
	createButtons : function(level) {
		var actions = this.actions
		var buttons = []
		if (!actions) {
			return buttons
		}
		if (this.butRule) {
			var ac = util.Accredit
			if (ac.canCreate(this.butRule)) {
				this.actions.unshift({
							id : "create",
							name : "新建"
						})
			}
		}
		var f1 = 112
		for (var i = 0; i < actions.length; i++) {
			var action = actions[i];
			if (action.properties && action.properties.hide) {
				continue
			}
			level = level || 'one';
			if (action.properties) {
				action.properties.level = action.properties.level || 'one';
			} else {
				action.properties = {};
				action.properties.level = 'one';
			}
			if (action.properties && action.properties.level != level) {
				continue;
			}
			// ** add by yzh **
			var btnFlag;
			if (action.notReadOnly)
				btnFlag = false
			else
				btnFlag = (this.exContext && this.exContext.readOnly) || false

			if (action.properties && action.properties.scale) {
				action.scale = action.properties.scale
			}
			var btn = {
				accessKey : f1 + i,
				text : action.name
						+ (this.noDefaultBtnKey ? "" : "(F" + (i + 1) + ")"),
				ref : action.ref,
				target : action.target,
				cmd : action.delegate || action.id,
				iconCls : action.iconCls || action.id,
				enableToggle : (action.toggle == "true"),
				scale : action.scale || "small",
				// ** add by yzh **
				disabled : btnFlag,
				notReadOnly : action.notReadOnly,

				script : action.script,
				handler : this.doAction,
				scope : this
			}
			buttons.push(btn)
		}
		return buttons

	},

	/**
	 * 根据模块的编号，从配置文件中读取数据，创建模块并初始化面板
	 * 
	 * @param {}
	 *            moduleName 模块名称
	 * @param {}
	 *            moduleId 模块编号
	 * @return {}
	 */
	getModule : function(moduleName, moduleId) {
		var item = this.createModule(moduleName, moduleId);
		return item.initPanel();
	},

	/**
	 * 根据模块的编号，从配置文件中读取数据，创建模块不初始化面板
	 * 
	 * @param {}
	 *            moduleName 模块名称
	 * @param {}
	 *            moduleId 模块编号
	 */
	createModule : function(moduleName, moduleId, exCfg) {
		var item = this.midiModules[moduleName]
		if (!item) {
			var moduleCfg = null;;
			var res = this.mainApp.taskManager.loadModuleCfg(moduleId);
			if (!res.code) {
				moduleCfg = res;
			} else if (res.code != 200) {
				if (res.msg == "NotLogon") {
					this.mainApp.logon()
					this.mainApp.desktop.mainTab.el.unmask()
					throw new Error("NotLogon");
				} else {
					if (typeof fCallback == "function") {
						fCallback.apply(this, []);
					}
					if (res.msg.indexOf("is not accessible") > 0) {
						this.mainApp.desktop.mainTab.el.unmask()
						Ext.MessageBox.alert("错误", "当前角色没有权限,请联系管理员")
					}
				}
				return
			}
			if (!moduleCfg) {
				moduleCfg = res.json.body;
			}
			// add by yangl 删除app载入的id，允许多个地方调用相同配置模块
			delete moduleCfg.id;
			var cfg = {
				showButtonOnTop : true,
				border : false,
				frame : false,
				autoLoadSchema : false,
				isCombined : true,
				exContext : {}
			};
			Ext.apply(moduleCfg, moduleCfg.properties);
			Ext.apply(cfg, exCfg);
			Ext.apply(cfg, moduleCfg);
			var cls = moduleCfg.script;
			if (!cls) {
				return;
			}
			if (!this.fireEvent("beforeLoadModule", moduleName, cfg)) {
				return;
			}
			$import(cls);
			item = eval("new " + cls + "(cfg)");
			item.setMainApp(this.mainApp);
			item.noDefaultBtnKey = this.noDefaultBtnKey;
			// if (!item.noExternalCall) {// 是否允许外部模块调用
			// if (!this.listenModules) {
			// this.listenModules = [];
			// }
			// this.listenModules.push(item);
			// item.on("shortcutKey", function(keyCode, keyName) {
			// return this.fireEvent("shortcutKey", keyCode,
			// keyName);// 需要处理的module层统一分发事件
			// }, this)
			// }
			this.midiModules[moduleName] = item;
		}
		return item;
	},
	// add by caijy at 2016年5月16日 10:24:50 for 判断数据是否为空,为空返回true
	isNull : function(value) {
		if (value == null || value == "" || value == undefined) {
			return true;
		}
		return false;
	},
	isZero : function(value) {
		if (value == null || value == 0 || value == '' || value == undefined) {
			return true;
		}
		return false;
	},
	// add by caijy at 2016年5月16日 14:11:52 for 通用的后台访问方法(同步)
	doAjax : function(sId, sAction, b) {
		var request = {
			serviceId : sId,
			serviceAction : sAction
		};
		if (b && b != null) {
			request["body"] = b;
		}
		return phis.script.rmi.miniJsonRequestSync(request);
	},
	// add by caijy at 2016年5月16日 15:08:22 for
	// 简单的lodop打印方法.reportName报表名字,urlCondition后面的Url条件,比如&cfsb=1(&需要写)
	simpleLodopPrint : function(reportName, urlCondition) {
		var url = "resources/phis.prints.jrxml." + reportName
				+ ".print?silentPrint=1&temp=" + new Date().getTime()
				+ urlCondition;
		var LODOP = getLodop();
		LODOP.PRINT_INIT("打印控件");
		LODOP.SET_PRINT_PAGESIZE("0", "", "", "");
		var rehtm = util.rmi.loadXML({
					url : url,
					httpMethod : "get"
				})
		rehtm = rehtm.replace(/table style=\"/g,
				"table style=\"page-break-after:always;");
		// 去掉打印body的边框
		rehtm = rehtm.replace("<body", "<body style='margin: 0'")
		LODOP.ADD_PRINT_HTM("0", "0", "100%", "100%", rehtm);
		LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", "Full-Width");
		// 预览
		LODOP.PREVIEW();
		// 直接打印
		// LODOP.PRINT();
	},
	// add by caijy at 2016年5月18日 08:52:46 for 刷新字段缓存
	reloadDics : function(dics) {
		phis.script.rmi.jsonRequest({
					serviceId : "publicService",
					serviceAction : "reloadDictionarys",
					body : dics
				}, function(code, msg, json) {

				}, this);
	},
	// add by caijy at 2016年5月30日 13:43:13 for 创建静态字典
	// store是字典项例如[[0, '全部'], [1, '西药'], [2, '中成药'], [3, '草药']],defaultValue是默认值
	createStaticDic : function(storeData, defaultValue) {
		var store = new Ext.data.SimpleStore({
					fields : ['value', 'text'],
					data : storeData
				});
		return new Ext.form.ComboBox({
					store : store,
					valueField : "value",
					displayField : "text",
					editable : false,
					selectOnFocus : true,
					triggerAction : 'all',
					mode : 'local',
					emptyText : '',
					width : 80,
					value : defaultValue || 0
				});
	},
	// add by caijy at 2016年6月7日 15:15:41 for 创建简单的日期控件
	// strategy暂时只支持month,dateTime,year,date. fieldLabel 日期控件前面的label,name
	// 控件name
	createSpinner : function(strategy, fieldLabel, name) {
		var value;;
		if (strategy == "month") {
			value = new Date().format('Y-m');
		} else if (strategy == "dateTime") {
			value = new Date().format('Y-m-d H:i:s');
		} else if (strategy == "year") {
			value = new Date().format('Y');
		} else if (strategy == "date") {
			value = new Date().format('Y-m-d');
		} else {
			return null;
		}
		return new Ext.ux.form.Spinner({
					fieldLabel : fieldLabel,
					name : name,
					value : value,
					strategy : {
						xtype : strategy
					},
					width : 100
				})
	}

	// loadModuleCfg : function(id) {
	// var result = phis.script.rmi.miniJsonRequestSync({
	// serviceId : "moduleConfigLocator",
	// id : id
	// })
	// if (result.code != 200) {
	// if (result.msg = "NotLogon") {
	// this.mainApp.logon(this.loadModuleCfg, this, [id])
	// }
	// return null;
	// }
	// return result.json.body;
	// }
}

function Map() {
	var struct = function(key, value) {
		this.key = key;
		this.value = value;
	}

	var put = function(key, value) {
		for (var i = 0; i < this.arr.length; i++) {
			if (this.arr[i].key === key) {
				this.arr[i].value = value;
				return;
			}
		}
		this.arr[this.arr.length] = new struct(key, value);
	}

	var get = function(key) {
		for (var i = 0; i < this.arr.length; i++) {
			if (this.arr[i].key === key) {
				return this.arr[i].value;
			}
		}
		return null;
	}

	var remove = function(key) {
		var v;
		for (var i = 0; i < this.arr.length; i++) {
			v = this.arr.pop();
			if (v.key === key) {
				continue;
			}
			this.arr.unshift(v);
		}
	}

	var size = function() {
		return this.arr.length;
	}

	var isEmpty = function() {
		return this.arr.length <= 0;
	}
	this.arr = new Array();
	this.get = get;
	this.put = put;
	this.remove = remove;
	this.size = size;
	this.isEmpty = isEmpty;
}

function overrideFunc() {
	// MyMessageTip.msg("1",'11---',true)
	Ext.useShims = true;
	// IE下关闭模块报错
	Ext.override(Ext.layout.FormLayout, {
				// add by yangl 非友好方式，不知道为什么IE下会报错，临时处理如下
				onRemove : function(c) {
					Ext.layout.FormLayout.superclass.onRemove.call(this, c);
					if (this.trackLabels) {
						c.un('show', this.onFieldShow, this);
						c.un('hide', this.onFieldHide, this);
					}

					var el = c.getPositionEl(), ct = c.getItemCt
							&& c.getItemCt();
					if (c.rendered && ct) {
						if (el && el.dom) {
							try {
								el.insertAfter(ct);
							} catch (e) {
								// toolbar中存在radioGroup的时候，IE下关闭的时候此处会报JS错误，导致关闭失败
							}
						}
						Ext.destroy(ct);
						Ext.destroyMembers(c, 'label', 'itemCt');
						if (c.customItemCt) {
							Ext.destroyMembers(c, 'getItemCt', 'customItemCt');
						}
					}
				}
			});
	// 控件增加锁定信息
	Ext.Element.addMethods(function() {
		var VISIBILITY = "visibility", DISPLAY = "display", HIDDEN = "hidden", NONE = "none", XMASKED = "x-masked", XMASKEDRELATIVE = "x-masked-relative", data = Ext.Element.data;

		return {
			lock : function(msg, msgCls) {

				var me = this, dom = me.dom, dh = Ext.DomHelper, EXTELMASKMSG = "ext-el-lock-msg", el, mask;

				if (!(/^body/i.test(dom.tagName) && me.getStyle('position') == 'static')) {
					me.addClass(XMASKEDRELATIVE);
				}
				if (el = data(dom, 'lockMsg')) {
					el.remove();
				}
				if (el = data(dom, 'lock')) {
					el.remove();
				}

				mask = dh.append(dom, {
							cls : "ext-el-lock"
						}, true);
				data(dom, 'lock', mask);

				me.addClass(XMASKED);
				mask.setDisplayed(true);

				if (typeof msg == 'string') {
					var mm = dh.append(dom, {
								cls : EXTELMASKMSG,
								cn : {
									tag : 'div'
								}
							}, true);
					data(dom, 'lockMsg', mm);
					mm.dom.className = msgCls
							? EXTELMASKMSG + " " + msgCls
							: EXTELMASKMSG;
					mm.dom.firstChild.innerHTML = msg;
					mm.setDisplayed(true);
					mm.center(me);
				}

				if (Ext.isIE && !(Ext.isIE7 && Ext.isStrict)
						&& me.getStyle('height') == 'auto') {
					mask.setSize(undefined, me.getHeight());
				}

				return mask;
			},

			unlock : function() {
				var me = this, dom = me.dom, mask = data(dom, 'lock'), maskMsg = data(
						dom, 'lockMsg');

				if (mask) {
					if (maskMsg) {
						maskMsg.remove();
						data(dom, 'lockMsg', undefined);
					}

					mask.remove();
					data(dom, 'lock', undefined);
					me.removeClass([XMASKED, XMASKEDRELATIVE]);
				}
			},

			isLocked : function() {
				var m = data(this.dom, 'lock');
				return m && m.isVisible();
			}
		};
	}());

	// 捕捉GridView的一些异常，防止IE下操作有问题
	Ext.override(Ext.grid.GridView, {
				getCell : function(row, col) {
					try {
						return Ext.fly(this.getRow(row))
								.query(this.cellSelector)[col];
					} catch (e) {
						return null;
					}
				},
				getResolvedXY : function(resolved) {
					if (!resolved) {
						return null;
					}
					var cell = resolved.cell, row = resolved.row;
					if (cell) {
						return Ext.fly(cell).getXY();
					} else {
						if (!row)
							return null;
						return [this.el.getX(), Ext.fly(row).getY()];
					}
				}
			});

	Ext.override(Ext.Button, {
				onClick : function(e) {
					if (e) {
						e.preventDefault();
					}
					if (e.button !== 0) {
						return;
					}
					if (!this.lastEnterTime) {
						this.lastEnterTime = 0;
					}
					var thisEnterTime = new Date().getTime();
					// 两次执行之间的间隔，防止重复操作（如果有必要，可增加参数控制哪些按钮需要这个延迟判断）
					if (thisEnterTime - this.lastEnterTime < 800) {
						// MyMessageTip.msg("提示", "两次操作间隔太短，忽略本次操作", false);
						return;
					}
					this.lastEnterTime = thisEnterTime;
					if (!this.disabled) {
						this.doToggle();
						if (this.menu && !this.hasVisibleMenu()
								&& !this.ignoreNextClick) {
							this.showMenu();
						}
						this.fireEvent('click', this, e);
						if (this.handler) {
							// this.el.removeClass('x-btn-over');
							this.handler.call(this.scope || this, this, e);
						}
					}
				}
			});
	// add by yangl 解决toFixed四舍五入问题
	Ext.applyIf(Number.prototype, {
				toFixed : function(s) {
					return (parseInt(this * Math.pow(10, s) + 0.5) / Math.pow(
							10, s)).toString();
				}
			});
	// add by yangl 解决多工具栏时grid内容区域高度不对的问题
	Ext.override(Ext.grid.GridView, {
		layout : function(initial) {
			if (!this.mainBody) {
				return; // not rendered
			}

			var grid = this.grid, gridEl = grid.getGridEl(), gridSize = gridEl
					.getSize(true), gridWidth = gridSize.width, gridHeight = gridSize.height, scroller = this.scroller, scrollStyle, headerHeight, scrollHeight;

			if (gridWidth < 20 || gridHeight < 20) {
				return;
			}

			if (grid.autoHeight) {
				scrollStyle = scroller.dom.style;
				scrollStyle.overflow = 'visible';

				if (Ext.isWebKit) {
					scrollStyle.position = 'static';
				}
			} else {
				this.el.setSize(gridWidth, gridHeight);

				headerHeight = this.mainHd.getHeight();
				scrollHeight = gridHeight - headerHeight;
				if (this.showBtnOnLevel) {
					scrollHeight = scrollHeight - 27;
				}
				scroller.setSize(gridWidth, scrollHeight);

				if (this.innerHd) {
					this.innerHd.style.width = (gridWidth) + "px";
				}
			}

			if (this.forceFit || (initial === true && this.autoFill)) {
				if (this.lastViewWidth != gridWidth) {
					this.fitColumns(false, false);
					this.lastViewWidth = gridWidth;
				}
			} else {
				this.autoExpand();
				this.syncHeaderScroll();
			}
			this.onLayout(gridWidth, scrollHeight);
		}
	});
	/**
	 * add by yangl 1、解决editorlist中编辑项输入非法值后不失去焦点 2、解决combo远程查询支持回车查询
	 */
	Ext.override(Ext.Editor, {
		onSpecialKey : function(field, e) {
			if (!this.lastEnterTime) {
				this.lastEnterTime = 0;
			}
			var thisEnterTime = new Date().getTime();
			if (thisEnterTime - this.lastEnterTime < 50) {// 不知道为什么，回车事件会被执行两次
				// yangl
				e.stopEvent();
				return;
			}
			// if (field.isExpanded)
			// MyMessageTip.msg("提示", "desktop:" + field.isExpanded(),
			// true);
			this.lastEnterTime = thisEnterTime;

			var key = e.getKey(), complete = this.completeOnEnter
					&& key == e.ENTER, cancel = this.cancelOnEsc
					&& key == e.ESC;
			if (key == e.TAB && !field.validate()) {
				e.stopEvent();
				return;
			}
			if (complete || cancel) {
				e.stopEvent();
				if (complete) {
					if (!field.validate()) {
						return;
					}

					if (key == e.ENTER && field.isSearchField) {
						if (!field.isExpanded()) {
							// 是否是字母
							// var patrn = /^[a-zA-Z.]+$/;
							// if (patrn.exec(field.getValue())) {
							// 防止查询不出数据或者按回车速度过快导致上次输入结果直接被调入
							field.getStore().removeAll();
							field.lastQuery = "";
							if (field.doQuery(field.getValue(), true) !== false) {
								e.stopEvent();
								return;
							}
						}
						// }
					}
					if (key == e.ENTER
							&& field instanceof util.widgets.MyCombox
							&& !field.isSearchField) {
						var searchValue = field.getRawValue()
						var searchType = field.searchField;
						var st = field.getStore();
						var sign = false;
						if (searchValue.length > 0) {
							for (var i = 0; i < st.getCount(); i++) {
								var r = st.getAt(i)
								var reg = new RegExp("^"
										+ searchValue.toLowerCase());
								if (reg.test(r.get(searchType).toLowerCase())) {
									if (sign)
										continue;
									sign = true;
									if (field.getValue() != r.get("key")) {
										field.setValue(r.get("key"));
									}
								}
							}
						}
						// field.setValue(searchValue)
					}
					this.completeEdit();
				} else {
					this.cancelEdit();
				}
				if (field.triggerBlur) {
					field.triggerBlur();
				}
			}
			this.fireEvent('specialkey', field, e);
		}
	});
	// EMR ocx中IE下删除键无效
	if (Ext.isIE) {
		document.onkeydown = function() {
			if (window.event.keyCode == 8 || window.event.keyCode == 9
					|| window.event.keyCode == 37 || window.event.keyCode == 38
					|| window.event.keyCode == 39 || window.event.keyCode == 40) {
				window.event.keyCode = 0;
			}
		}
	}
	// var d = Ext.MessageBox.getDialog("&#160;");
	// d.on("show",function(){
	// d.center();
	// })
}