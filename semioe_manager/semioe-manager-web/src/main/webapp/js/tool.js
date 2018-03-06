(function(window, document) {
	window.utils = {
		/**
		 * 写入cookie expireTime 单位是分
		 */
		setCookie : function(name, value, expireTime) {
			if(!expireTime){
				expireTime = 30;
			}
			var time = new Date();
			time.setMinutes(time.getMinutes() + expireTime);
			document.cookie = name + '=' + escape(value) + ';path=/;expires ='
					+ time.toGMTString();
		},
		/**
		 * 获取cookie 不传name就是返回所有
		 * 
		 * @param name
		 * @returns
		 */
		getCookie : function(name) {
			var arr = document.cookie.split(';');
			var paramObj = {};
			if(name == 'token'){
				for (var key = 0; key < arr.length; key++) {
					var index = arr[key].indexOf('=');
					var param = [arr[key].substr(0,index),arr[key].substring(index+2,arr[key].length-1)];
					if (param[0] && param[1]) {
						paramObj[param[0].replace(/^\s+/, "")] = param[1].replace(
								/^\s+/, "");
					}
				}
			}else{
				for (var key = 0; key < arr.length; key++) {
					var param = arr[key].split('=');
					if (param[0] && param[1]) {
						paramObj[param[0].replace(/^\s+/, "")] = param[1].replace(
								/^\s+/, "");
					}
				}
			}
			if (name) {
				if(unescape(paramObj[name]) == "undefined"){
					if (self.frameElement && self.frameElement.tagName == "IFRAME"){
						parent.location.href="/index.html"
					}else{
						window.location.href="/index.html"
					}
				}
				return unescape(paramObj[name]) || '';
			}
			return paramObj;
		},
		/**
		 * 删除cookie
		 * 
		 * @param name
		 * @returns
		 */
		removeCookie : function(name) {
			this.setCookie(name, 1, -1);
		},
		/**
		 * 判断是否对象
		 * 
		 * @param obj
		 * @returns
		 */
		isEmptyObject : function(obj) {
			for ( var key in obj) {
				return false
			}
			return true;
		},
		/**
		 * 年-月-日 时:分:秒
		 */
		dateFormat : function(timer) {
			var month = timer.getMonth() + 1;
			month = month > 9 ? month : '0' + month;
			var day = timer.getDate();
			day = day > 9 ? day : '0' + day;
			var hourse = timer.getHours();
			hourse = hourse > 9 ? hourse : '0' + hourse;
			var minute = timer.getMinutes();
			minute = minute > 9 ? minute : '0' + minute;
			var second = timer.getSeconds();
			second = second > 9 ? second : '0' + second;
			return timer.getFullYear() + '-' + month + '-' + day + ' ' + hourse
					+ ':' + minute + ':' + second;
		},
		/**
		 * 自定义时间 time 时间 format 想要的格式
		 */
		simpleDateFormat : function(time, format) {
			var t = new Date(time);
			var tf = function(i) {
				return (i < 10 ? '0' : '') + i
			};
			return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a) {
				switch (a) {
					case 'yyyy':
						return tf(t.getFullYear()); 
						break;
					case 'MM':
						return tf(t.getMonth() + 1);
						break;
					case 'mm':
						return tf(t.getMinutes());
						break;
					case 'dd':
						return tf(t.getDate());
						break;
					case 'HH':
						return tf(t.getHours());
						break;
					case 'ss':
						return tf(t.getSeconds());
						break;
				}
			})
		},

		/**
		 * 当前时间 時分秒
		 */
		audioSet : function() {
			var mydate = new Date();
			var h = mydate.getHours() > 9 ? mydate.getHours() : "0"
					+ mydate.getHours();
			var m = mydate.getMinutes() > 9 ? mydate.getMinutes() : "0"
					+ mydate.getMinutes();
			var s = mydate.getSeconds() > 9 ? mydate.getSeconds() : "0"
					+ mydate.getSeconds();
			return h + ':' + m + ':' + s;
		},

		/**
		 * 对比两个对象是否相等
		 */
		isObjectValueEqual : function(x, y) {
			if (x === y) {
				return true;
			}
			if (!(x instanceof Object) || !(y instanceof Object)) {
				return false;
			}
			if (x.constructor !== y.constructor) {
				return false;
			}
			for ( var p in x) {
				if (x.hasOwnProperty(p)) {
					if (!y.hasOwnProperty(p)) {
						return false;
					}
					if (x[p] === y[p]) {
						continue;
					}
					if (typeof (x[p]) !== "object") {
						return false;
					}
					if (!Object.equals(x[p], y[p])) {
						return false;
					}
				}
			}
			for (p in y) {
				if (y.hasOwnProperty(p) && !x.hasOwnProperty(p)) {
					return false;
				}
			}
			return true;
		},
		/**
		 * 判断是否对象
		 * 
		 * @param object
		 * @returns
		 */
		isObj : function(object) {
			return object
					&& typeof (object) == 'object'
					&& Object.prototype.toString.call(object).toLowerCase() == "[object object]";
		},
		/**
		 * 判断是否数组
		 * 
		 * @param object
		 * @returns
		 */
		isArray : function(object) {
			return object && typeof (object) == 'object'
					&& object.constructor == Array;
		},
		/**
		 * 获取对象长度
		 * 
		 * @param object
		 * @returns
		 */
		getObjLength : function(object) {
			var count = 0;
			for ( var i in object)
				count++;
			return count;
		},
		/**
		 * 判断两个对象是否相等
		 */
		compare : function(objA, objB) {
			if (!this.isObj(objA) || !this.isObj(objB))
				return false; // 判断类型是否正确
			if (this.getObjLength(objA) != this.getObjLength(objB))
				return false; // 判断长度是否一致
			return this.compareObj(objA, objB, true); // 默认为true
		},
		compareObj : function(objA, objB, flag) {
			for ( var key in objA) {
				if (!flag) // 跳出整个循环
					break;
				if (!objB.hasOwnProperty(key)) {
					flag = false;
					break;
				}
				if (!this.isArray(objA[key])) { // 子级不是数组时,比较属性值
					if (objB[key] != objA[key]) {
						flag = false;
						break;
					}
				} else {
					if (!this.isArray(objB[key])) {
						flag = false;
						break;
					}
					var oA = objA[key], oB = objB[key];
					if (oA.length != oB.length) {
						flag = false;
						break;
					}
					for ( var k in oA) {
						if (!flag) // 这里跳出循环是为了不让递归继续
							break;
						flag = this.compareObj(oA[k], oB[k], flag);
					}
				}
			}
			return flag;
		},
		/**
		 * 获取url中‘？’后的参数
		 * 
		 * @param key
		 * @returns
		 */
		getRequestParam : function(item) {
			var url = location.search.replace(/^\?/,'').split('&');
		    var paramsObj = {};
		    for(var key=0;key<url.length;key++){
		    	var param =url[key].split('=');
		        paramsObj[param[0]]=unescape(decodeURI(param[1]));
		    }
		    if(item){
		        return paramsObj[item] || '';
		    }
		    return paramsObj;
		},
		/**
		 * 删除url中的参数
		 * 
		 * @param name
		 * @returns 删除后的url
		 */
		delURLQuery : function(name) {
			var loca = window.location;
			var baseUrl = loca.origin + loca.pathname + "?";
			var query = loca.search.substr(1);
			if (query.indexOf(name) > -1) {
				var obj = {}
				var arr = query.split("&");
				for (var i = 0; i < arr.length; i++) {
					arr[i] = arr[i].split("=");
					obj[arr[i][0]] = arr[i][1];
				}
				delete obj[name];
				var url = baseUrl
						+ JSON.stringify(obj).replace(/[\"\{\}]/g, "").replace(
								/\:/g, "=").replace(/\,/g, "&");
				return url
			}
		}
	}
})(window, document);
