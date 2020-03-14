$package("phis.script")
$import("util.rmi.miniJsonRequestSync", "util.rmi.miniJsonRequestAsync",
		"util.codec.RSAUtils")
phis.script.PhisLogon = function(config) {
	this.forConfig = false
	this.deep = false
	this.photoHome = "photo/"
	this.requestDefineDeep = 3
	phis.script.PhisLogon.superclass.constructor.apply(this, [config]);
}

Ext.extend(phis.script.PhisLogon, app.desktop.Module, {
	init : function() {
		this.dataMap = new Ext.util.MixedCollection();
		this.addEvents({
					"logonSuccess" : true,
					"logonCancel" : true
				})
		this.initPanel()
	},

	initPanel : function() {
		if (this.isAuto()) {
			this.autoLogon()
			return
		}
		var user = this.createUserCombox();
		user.focus(200);
		// user.setValue("5031")
		this.user = user
		var role = this.createRoleCombox()
		this.role = role
		var unit = this.createUnitCombox()
		this.unit = unit

		var pwdEL = Ext.get("pwd")
		this.pwdEL = pwdEL
		var uid = this.user.getValue();
		this.pwdEL.dom.value = this.getCookie("password_" + uid) || ''
		var forget = Ext.get("forget");
		this.forget = forget
		forget.dom.checked = this.getCookie("forget");
		pwdEL.on("blur", this.loadRole, this)
		user.on("keypress", function(f, e) {
					if (e.getKey() == e.ENTER) {
						var raw = user.getRawValue()
						if (raw) {
							user.beforeBlur()
							this.pwdEL.focus(true)
						}
					} else {
						if (e.getKey() > 47 && e.getKey() < 112
								|| e.getKey() == 8 || e.getKey() == 46) {
							this.pwdEL.dom.value = '';
							this.role.setRawValue("")
							this.unit.setRawValue("")
						}
					}
				}, this)

		pwdEL.on("keypress", function(e) {
					if (e.getKey() == e.ENTER) {
						if (pwdEL.getValue()) {
							this.loadRole();
							this.role.focus(true)
						}
					}
				}, this)

		role.on("select", function(combo, record, index) {
					var data = record.data;
					var curRoleId = data.roleId;
					var unitList = [];
					for (var i = 0, len = this.tokens.length; i < len; i++) {
						var token = this.tokens[i];
						var roleId = token.roleId
						if (curRoleId == roleId) {
							unitList.push(token);
						}
					}
					this.unit.getStore().loadData(unitList);
					var manageUnit = data.manageUnitId;
					this.unit.setValue(manageUnit);
					if (unitList.length == 1) {
						this.unit.disable();
					} else {
						this.unit.enable();
					}
				}, this)
		role.on("keypress", function(f, e) {
					if (e.getKey() == e.ENTER) {
						this.doLogon();
					}
				}, this)
		unit.on("select", function(combo, record, index) {
					var data = record.data;
					var curId = data.id;
					var curRoleId = data.roleId;
					var roleList = [];
					var roleMap = new Ext.util.MixedCollection();
					for (var i = 0, len = this.tokens.length; i < len; i++) {
						var token = this.tokens[i];
						var roleId = token.roleId
						var tId = token.id;
						if (curId != tId && curRoleId == roleId) {
							continue;
						}
						if (!roleMap.containsKey(roleId)) {
							roleMap.add(roleId, roleId)
							roleList.push(token);
						}
					}
					this.role.getStore().loadData(roleList);
					this.role.setValue(curId);
				}, this);
		unit.on("keypress", function(f, e) {
					if (e.getKey() == e.ENTER) {
						this.doLogon();
					}
				}, this)
		var logon = Ext.get("logon")
		logon.on("click", this.doLogon, this)
		var clear = Ext.get("clear")
		clear.on("click", this.doClear, this)
		this.loadRole();
	},

	createUserCombox : function() {
		var data = this.getCookieValue()
		var store = new Ext.data.JsonStore({
					root : 'body',
					id : 'userId',
					data : data,
					fields : ['userId', 'userName', 'userPic']
				})

		var tpl = new Ext.XTemplate('<tpl for=".">'
				+ '<ul class="useru" onmouseover="this.className=\'userus\';" onmouseout="this.className=\'useru\';">'
				// + '<li><img src="*.photo?img={userPic}" width="30"
				// height="30" /></li>'
				+ '<li>{userName}-{userId}</li>'
				+ '<span id="{userId}">X</span>' + '</ul></tpl>')

		var userCombox = new Ext.form.ComboBox({
					tpl : tpl,
					store : store,
					minChars : 2,
					selectOnFocus : true,
					enableKeyEvents : true,
					editable : true,
					valueField : 'userId',
					displayField : 'userName',
					triggerAction : "all",
					mode : 'local',
					itemSelector : "ul",
					width : 185,
					renderTo : "usertext",
					style : "height:24px;"
				})
		userCombox.setValue((this.getCookie("userId") || ''));
		userCombox.on("expand", function() {
					if (!data.body)
						return;
					for (var i = 0; i < data.body.length; i++) {
						var img = data.body[i].userId
						if (!this.dataMap.containsKey(img)) {
							var el = Ext.get(img)
							el.on("click", this.deleteUserCache, this)
							this.dataMap.add(img, el)
						}
					}
				}, this)
		userCombox.on("blur", function() {
					var raw = userCombox.getRawValue()
					if (raw) {
						userCombox.beforeBlur()
						this.pwdEL.focus(true)
					}
					this.loadRole();
				}, this)
		userCombox.on("select", function() {
					this.pwdEL.dom.value = ""
				}, this)
		return userCombox
	},

	setUserImg : function(pic) {
		if (Ext.fly("pop")) {
			var img = Ext.fly("pop").dom
			img.src = "*.photo?img=" + pic
		}

	},

	createRoleCombox : function() {
		var tpl = new Ext.XTemplate('<tpl for=".">'
				+ '<ul class="roleu" onmouseover="this.className=\'roleus\';" onmouseout="this.className=\'roleu\';">'
				// + '<li><img src="*.photo?img={userPic}" width="30"
				// height="30" /></li>'
				+ '<li>{roleName}</li>' + '<span id="{id}"></span>'
				+ '</ul></tpl>')
		var com = new Ext.form.ComboBox({
					tpl : tpl,
					width : 185,
					style : "height:24px;",
					displayField : 'roleName',
					valueField : 'id',
					autoSelect : true,
					editable : false,
					mode : 'local',
					triggerAction : 'all',
					renderTo : 'select-role',
					enableKeyEvents : true,
					enableKeyEvents : true,
					itemSelector : "ul",
					store : new Ext.data.JsonStore({
								data : {},
								fields : ["id", 'displayName', 'userId',
										'userName', 'organId', 'roleId',
										'roleName', 'manageUnitId',
										'manageUnitName', 'manageUnit']
							})
				})
		com.store.on("load", function(s, rs) {
					var uid = this.user.getValue().trim();
					var pwd = this.pwdEL.getValue();
					var k = uid + pwd;
					if (com.findRecord('id', this.getCookie("userRole_" + k)) != null) {
						com.setValue(this.getCookie("userRole_" + k));
					} else {
						com.setValue(rs[0].data.id);
					}
				}, this)
		return com
	},

	createUnitCombox : function() {
		var tpl = new Ext.XTemplate('<tpl for=".">'
				+ '<ul class="orgu" onmouseover="this.className=\'orgus\';" onmouseout="this.className=\'orgu\';">'
				+ '<li>{manageUnitName}</li>'
				+ '<span id="{manageUnitId}"></span>' + '</ul></tpl>')
		var com = new Ext.form.ComboBox({
					tpl : tpl,
					width : 185,
					style : "height:24px;",
					displayField : 'manageUnitName',
					valueField : 'manageUnitId',
					autoSelect : true,
					editable : false,
					disabled : true,
					mode : 'local',
					triggerAction : 'all',
					renderTo : 'select-org',
					enableKeyEvents : true,
					enableKeyEvents : true,
					itemSelector : "ul",
					store : new Ext.data.JsonStore({
								data : {},
								fields : ["id", 'displayName', 'userId',
										'userName', 'roleId', 'roleName',
										'manageUnitId', 'manageUnitName']
							})
				})
		var uid = this.user.getValue().trim();
		var pwd = Ext.get("pwd").getValue();
		var k = uid + pwd;
		com.setValue((this.getCookie("userUnit_" + k) || ''));
		com.store.on("load", function(s, rs) {
			if (com.findRecord('manageUnitId', this.getCookie("userUnit_" + k)) != null) {
				com.setValue(this.getCookie("userUnit_" + k));
			} else {
				com.setValue(rs[0].data.manageUnitId);
			}
		}, this)
		return com
	},
	roleFilter : function(tokens, curTokenId, curRoleId) {
		var roleList = [];
		var crId = curRoleId;
		if (!crId) {
			crId = this.getCurRoleId(tokens, curTokenId);
		}
		var roleMap = new Ext.util.MixedCollection();
		for (var i = 0, len = tokens.length; i < len; i++) {
			var token = tokens[i];
			var tId = token.id;
			var roleId = token.roleId;
			if (curTokenId != tId && crId == roleId) {
				continue;
			}
			if (!roleMap.containsKey(roleId)) {
				roleMap.add(roleId, roleId);
				roleList.push(token);
			}
		}
		return roleList;
	},
	unitFilter : function(tokens, curTokenId, curRoleId) {
		var rs = {};
		var unitList = [];
		var crId = curRoleId;
		if (!crId) {
			crId = this.getCurRoleId(tokens, curTokenId);
		}
		for (var i = 0, len = tokens.length; i < len; i++) {
			var token = tokens[i];
			var tId = token.id;
			var roleId = token.roleId;
			var manageUnit = token.manageUnitId;
			if (curTokenId == tId) {
				rs.manageUnitId = manageUnit;
			}
			if (crId == roleId) {
				unitList.push(token);
			}
		}
		rs.unitList = unitList;
		return rs;
	},
	getCurRoleId : function(tokens, curTokenId) {
		if (!curTokenId) {
			return '';
		}
		var crId = '';
		for (var i = 0, len = tokens.length; i < len; i++) {
			var token = tokens[i];
			var tId = token.id;
			var roleId = token.roleId;
			if (curTokenId == tId) {
				crId = roleId;
				break;
			}
		}
		return crId;
	},
	loadRole : function() {
		var uid = this.user.getValue();
		var pwd = this.pwdEL.getValue();
		if (!uid || !pwd) {
			return
		}
		var k = uid + pwd
		// 先有缓存中读取
		if (!this.dataMap) {
			this.dataMap = new Ext.util.MixedCollection();
		}
		if (!this.dataMap.containsKey(k)) {
			var strTokens = this.getCookie("userTokens_" + k);
			if (strTokens && strTokens != 'undefined' && strTokens != 'null') {
				this.dataMap.add(k, $decode(strTokens));
			}
		}
		// remove by yangl 解决部分用户退出后切换角色不能登录问题
		// if (this.dataMap.containsKey(k)) {
		// var data = this.dataMap.get(k)
		// this.tokens = data;
		// this.role.setRawValue("")
		// var cookieUserRole = this.getCookie("userRole_" + k);
		// var curRoleId = this.getCurRoleId(data, cookieUserRole);
		// var roleList = this.roleFilter(data, cookieUserRole, curRoleId);
		// this.role.getStore().loadData(roleList);
		// var curRole = roleList[0].id;
		// if (!curRoleId) {
		// curRoleId = roleList[0].roleId;
		// } else {
		// curRole = cookieUserRole;
		// }
		// this.role.setValue(curRole);
		// var rs = this.unitFilter(data, curRole, curRoleId);
		// var unitList = rs.unitList;
		// this.unit.setRawValue("")
		// this.unit.getStore().loadData(unitList);
		// var unitId = rs.manageUnitId;
		// if (!unitId) {
		// unitId = roleList[0].manageUnitId;
		// }
		// this.unit.setValue(unitId);
		// if (unitList.length == 1) {
		// this.unit.disable();
		// } else {
		// this.unit.enable();
		// }
		// this.forbid = false
		// return
		// }
		var key = this.getPublicKey();
		if (key) {
			pwd = util.codec.RSAUtils.encryptedString(key, pwd)
		}
		var json = util.rmi.miniJsonRequestSync({
					url : 'logon/myRoles',
					uid : uid,
					pwd : pwd
				})
		if (json.code == 200) {
			var rec = json.json.body
			this.role.setRawValue("")
			var tokens = rec.tokens;
			this.tokens = tokens;
			if (tokens.length == 0) {
				Ext.Msg.alert("提示", "该用户还没有角色，请联系管理员");
				return;
			}
			var cookieUserRole = this.getCookie("userRole_" + k);
			var curRoleId = this.getCurRoleId(tokens, cookieUserRole);
			var roleList = this.roleFilter(tokens, cookieUserRole, curRoleId);
			this.role.getStore().loadData(roleList)
			var curRole = roleList[0].id;
			if (!curRoleId) {
				curRoleId = roleList[0].roleId;
			} else {
				curRole = cookieUserRole;
			}
			this.role.setValue(curRole);
			var rs = this.unitFilter(tokens, curRole, curRoleId);
			var unitList = rs.unitList;
			this.unit.getStore().loadData(unitList);
			var unitId = rs.manageUnitId;
			if (!unitId) {
				unitId = roleList[0].manageUnitId;
			}
			this.unit.setValue(unitId);
			if (unitList.length == 1) {
				this.unit.disable();
			} else {
				this.unit.enable();
			}
			this.dataMap.add(k, rec.tokens);
			this.setUserImg(rec.userPhoto);
			this.forbid = false
		} else {
			this.role.clearValue()
			this.role.getStore().removeAll()
			this.unit.clearValue()
			this.unit.getStore().removeAll()
			if (json.code == 404) {
				this.forbid = true
				this.messageDialog("错误", "用户不存在或已禁用", Ext.MessageBox.ERROR)
			} else if (json.code == 501) {
				this.messageDialog("错误", "密码错误", Ext.MessageBox.ERROR)
			}
		}
	},

	doLogon : function() {
		var uid = this.user.getValue().trim()
		var uname = this.user.getRawValue().trim()
		var pwd = this.pwdEL.getValue();
		if (!uid || !pwd || this.forbid) {
			return;
		}
		var urt = this.role.getValue()
		if (!urt) {
			this.messageDialog("错误", "请选择登陆角色!", Ext.MessageBox.ERROR)
			return
		}
		if (this.logoning)
			return;
		this.logoning = true;
		this.userRoleToken = this.role.getStore().getById(urt).data
		if (this.userRoleToken) {
			this.doLoadAppDefines(urt)
		}
		var userUnit = this.unit.getValue();
		var k = uid + pwd
		if (this.forget.dom.checked) {
			this.setCookie("userId", uid);
			this.setCookie("password_" + uid, pwd);
			this.setCookie("userRole_" + k, urt);
			this.setCookie("userUnit_" + k, userUnit);
			this.setCookie("forget", true);
			this.setCookie("userTokens_" + k, $encode(this.tokens));
		} else {
			this.delCookie("userId_" + uid);
			this.delCookie("password_" + uid);
			this.delCookie("userRole_" + k);
			this.delCookie("userUnit_" + k);
			this.delCookie("userTokens_" + k);
			this.delCookie("forget")
		}
	},
	doClear : function() {
		this.user.clearValue();
		this.role.clearValue();
		this.unit.clearValue();
		this.role.store.removeAll();
		this.unit.store.removeAll();
		this.pwdEL.dom.value = "";
	},
	/**
	 * 
	 * @param {}
	 *            urt
	 * @param {}
	 *            forceLogon 1:强制登录
	 */
	doLoadAppDefines : function(urt, forceLogon) {
		var uid = this.user.getValue().trim();
		var pwd = this.pwdEL.getValue();
		util.rmi.miniJsonRequestAsync({
					url : "logon/myApps?urt=" + urt + "&uid=" + uid + "&pwd="
							+ pwd + "&deep=" + this.requestDefineDeep
							+ "&forceLogon=" + (forceLogon || 0),
					httpMethod : "POST"
				}, function(code, msg, json) {
					if (code < 400) {
						var appData = json.body;
						this.fireEvent("appsDefineLoaded", this.userRoleToken,
								appData)
						this.saveUrtToAppContext(this.userRoleToken);
						this.doRelogonCallback();
						var t = this.userRoleToken
						this.addCookie(t.userId, t.userName, appData.userPhoto)
					} else {
						if (code == 502) {
							Ext.Msg.alert("提示", "登录失败：当前用户已在别处登录!");
						} else if (code == 503) {
							Ext.Msg.confirm("提示", "当前用户已在别处登录，是否强制登录？",
									function(btn) {
										if (btn == 'yes') {
											this.doLoadAppDefines(urt, 1)
										}
									}, this);
						}
					}
					this.relogonCallbackContext = {};
					this.logoning = false;
				}, this);
	},
	doChangeUserRoleToken : function(urt, toke) {
		if (toke) {
			this.userRoleToken = toke
		} else {
			if (this.role.getStore().getById(urt)) {
				this.userRoleToken = this.role.getStore().getById(urt).data
			}
		}

	},
	// ===================cookie=s========================
	setCookie : function(key, value) {
		var date = new Date()
		date.setTime(date.getTime() + 100 * 24 * 3600 * 1000)
		document.cookie = key + "=" + escape(value) + ";expires="
				+ date.toGMTString();
	},
	getCookieValue : function() {
		var data = {
			body : []
		}
		var cookie = this.getCookie()
		var cs = cookie.split(";")
		for (var i = 0; i < cs.length; i++) {
			var v = cs[i].split("=")
			if (v.length == 1) {
				break
			}
			var uid = v[0].trim()
			var temp = v[1].split("@")
			if (temp.length != 2) {
				continue
			}
			var uname = unescape(temp[0])
			if (uname == "del") {
				continue
			}
			var upic = temp[1]
			var o = {
				userId : uid,
				userName : uname,
				userPic : upic
			}
			data.body.push(o)
		}
		return data
	},
	delCookie : function(key) {
		var date = new Date()
		date.setTime(date.getTime() - 1);
		document.cookie = key + "=; expires=" + date.toGMTString();
	},
	addCookie : function(userId, userName, userPic) {
		var date = new Date()
		date.setTime(date.getTime() + 30 * 24 * 3600 * 1000)
		document.cookie = userId + "=" + escape(userName) + "@" + userPic
				+ "; expires=" + date.toGMTString()
	},

	getCookie : function(key) {
		if (!key) {
			return document.cookie;
		} else {
			var strCookies = document.cookie
			var arrCookies = strCookies.split("; ")
			for (var i = 0; i < arrCookies.length; i++) {
				var arrCookie = arrCookies[i].split("=")
				if (this.lefttrim(arrCookie[0]) == key) {
					return unescape(arrCookie[1])
				}
			}
		}
	},
	lefttrim : function(str) {
		var index = 0
		for (var i = 0; i < str.length; i++) {
			if (str[i] != " ") {
				index = i
				break
			}
		}
		return str.substring(index)
	},
	deleteCookie : function(userId) {
		var date = new Date();
		date.setTime(date.getTime() - 100000);
		document.cookie = userId + "=del@del; expire=" + date.toGMTString();
	},
	// ======================cookie=e==========================

	deleteUserCache : function(e, element) {
		var uid = element.id
		var store = this.user.getStore()
		var r = store.getById(uid)
		store.remove(r)
		this.user.setValue();
		var pwd = this.getCookie("password_" + uid);
		var k = uid + pwd;
		var cuid = this.getCookie("userId");
		if (cuid == uid) {
			this.delCookie("userId");
		}
		this.delCookie("password_" + uid);
		this.delCookie("userRole_" + k);
		this.delCookie("userUnit_" + k);
		this.delCookie("userTokens_" + k);
		if (this.dataMap.containsKey(k)) {
			this.dataMap.remove(k);
		}
		this.deleteCookie(uid);
	},

	doCancel : function() {
		this.deleteCookie("system")
	},

	messageDialog : function(title, msg, icon) {
		Ext.MessageBox.show({
					title : title,
					msg : msg,
					buttons : Ext.MessageBox.OK,
					icon : icon
				})
	},

	getPublicKey : function() {
		var key = this.publicKey;
		if (!key) {
			var result = util.rmi.miniJsonRequestSync({
						url : "logon/publicKey",
						httpMethod : "GET"
					});
			if (result.code == 200) {
				var modulus = result.json.modulus;
				var exponent = result.json.exponent
				key = util.codec.RSAUtils.getKeyPair(exponent, '', modulus);
				this.publicKey = key;
			}
		}
		return key;
	},

	doGetCurrentUrt : function() {
		var result = util.rmi.miniJsonRequestSync({
					url : "logon/currentUrt",
					httpMethod : "GET"
				});
		if (result.code == 200) {
			var ur = result.json.body
			this.userRoleToken = ur;
			return ur;
		} else {
			return null;
		}
	},

	saveUrtToAppContext : function(urt) {
		$AppContext["urt"] = urt;
	},

	setRelogonCallback : function(func, args, scope) {
		this.relogonCallbackContext.func = func;
		this.relogonCallbackContext.args = args;
		this.relogonCallbackContext.scope = scope;
	},

	doRelogonCallback : function() {
		var context = this.relogonCallbackContext;
		if (!context || typeof context.func != "function") {
			return;
		}
		context.func.apply(context.scope, context.args)
		this.relogonCallbackContext = {};
	},
	isAuto : function() {
		if (typeof($sso) != 'undefined' && $sso) {
			return true
		}
		return false
	},

	autoLogon : function() {
		var json = util.rmi.miniJsonRequestSync({
					url : 'sso/autoLogon'
				})
		if (json.code == 200) {
			var token = json.json.body
			this.userRoleToken = token
			this.doLoadAppDefines(token.id)
			$sso = false
		}
		if (json.code == 404) {
			this.messageDialog("错误", "用户不存在或已禁用", Ext.MessageBox.ERROR)
		}
		if (json.code == 501) {
			this.messageDialog("错误", "密码错误", Ext.MessageBox.ERROR)
		}
	}
})