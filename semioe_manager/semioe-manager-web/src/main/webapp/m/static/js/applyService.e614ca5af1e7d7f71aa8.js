webpackJsonp([48],{10:function(t,e){},11:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"vux-toast"},[n("div",{directives:[{name:"show",rawName:"v-show",value:t.isShowMask&&t.show,expression:"isShowMask && show"}],staticClass:"weui-mask_transparent"}),t._v(" "),n("transition",{attrs:{name:t.currentTransition}},[n("div",{directives:[{name:"show",rawName:"v-show",value:t.show,expression:"show"}],staticClass:"weui-toast",class:t.toastClass,style:{width:t.width}},[n("i",{directives:[{name:"show",rawName:"v-show",value:"text"!==t.type,expression:"type !== 'text'"}],staticClass:"weui-icon-success-no-circle weui-icon_toast"}),t._v(" "),t.text?n("p",{staticClass:"weui-toast__content",style:t.style,domProps:{innerHTML:t._s(t.text)}}):n("p",{staticClass:"weui-toast__content"},[t._t("default")],2)])])],1)},staticRenderFns:[]}},1160:function(t,e){},1161:function(t,e){},1302:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"applyService"},[n("div",{staticClass:"apply-title"},[t._v("\n        医生将可以看到您健康档案的所有信息，并为您设计私人专属健康服务。\n    ")]),t._v(" "),n("div",{staticClass:"apply-input"},[n("div",{staticClass:"apply-input-title"},[t._v("\n            请描述您的症状：（必填）\n        ")]),t._v(" "),n("textarea",{directives:[{name:"model",rawName:"v-model",value:t.symptom,expression:"symptom"}],attrs:{cols:"30",rows:"10",minlength:"10"},domProps:{value:t.symptom},on:{input:function(e){e.target.composing||(t.symptom=e.target.value)}}})]),t._v(" "),n("div",{staticClass:"apply-submit",staticStyle:{"margin-top":"3rem"}},[n("div",{staticClass:"apply-submit-btn",on:{click:t.save}},[t._v("\n            提交申请\n        ")])]),t._v(" "),n("toast",{attrs:{text:t.toastMsg,type:"text",width:"25em"},model:{value:t.showToast,callback:function(e){t.showToast=e},expression:"showToast"}})],1)},staticRenderFns:[]}},1352:function(t,e,n){function o(t){n(1160),n(1161)}var s=n(0)(n(702),n(1302),o,"data-v-3a617551",null);t.exports=s.exports},1445:function(t,e,n){n(5),t.exports=n(622)},2:function(t,e,n){"use strict";var o="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(t){return typeof t}:function(t){return t&&"function"==typeof Symbol&&t.constructor===Symbol&&t!==Symbol.prototype?"symbol":typeof t};e.a={setCookie:function(t,e,n){n||(n=30);var o=new Date;o.setMinutes(o.getMinutes()+n),document.cookie=t+"="+escape(e)+";path=/;expires ="+o.toGMTString()},getCookie:function(t){var e=document.cookie.split(";"),n={};if("token"==t)for(var o=0;o<e.length;o++){var s=e[o].indexOf("="),i=[e[o].substr(0,s),e[o].substring(s+2,e[o].length-1)];i[0]&&i[1]&&(n[i[0].replace(/^\s+/,"")]=i[1].replace(/^\s+/,""))}else for(var o=0;o<e.length;o++){var i=e[o].split("=");i[0]&&i[1]&&(n[i[0].replace(/^\s+/,"")]=i[1].replace(/^\s+/,""))}return t?unescape(n[t])||"":n},removeCookie:function(t){this.setCookie(t,1,-1)},simpleDateFormat:function(t,e){var n=new Date(t),o=function(t){return(t<10?"0":"")+t};return e.replace(/yyyy|MM|dd|HH|mm|ss/g,function(t){switch(t){case"yyyy":return o(n.getFullYear());case"MM":return o(n.getMonth()+1);case"mm":return o(n.getMinutes());case"dd":return o(n.getDate());case"HH":return o(n.getHours());case"ss":return o(n.getSeconds())}})},audioSet:function(){var t=new Date;return(t.getHours()>9?t.getHours():"0"+t.getHours())+":"+(t.getMinutes()>9?t.getMinutes():"0"+t.getMinutes())+":"+(t.getSeconds()>9?t.getSeconds():"0"+t.getSeconds())},getObjLength:function(t){var e=0;for(var n in t)e++;return e},getRequestParam:function(t){for(var e=location.search.replace(/^\?/,"").split("&"),n={},o=0;o<e.length;o++){var s=e[o].split("=");n[s[0]]=unescape(decodeURI(s[1]))}return t?n[t]||"":n},delURLQuery:function(t){var e=window.location,n=e.origin+e.pathname+"?",o=e.search.substr(1);if(o.indexOf(t)>-1){for(var s={},i=o.split("&"),r=0;r<i.length;r++)i[r]=i[r].split("="),s[i[r][0]]=i[r][1];delete s[t];return n+JSON.stringify(s).replace(/[\"\{\}]/g,"").replace(/\:/g,"=").replace(/\,/g,"&")}},next:function(t){var e=(new Date).getTime();t+=t.indexOf("?")>=0?"&_="+e:"?_="+e,window.location.href=t},mul:function(t,e){var n=0,o=0;return t=""+t,e=""+e,~t.indexOf(".")&&(n=t.split(".")[1].length),~e.indexOf(".")&&(o=e.split(".")[1].length),t=1*t.replace(".",""),e=1*e.replace(".",""),t*e/Math.pow(10,n+o)},wxConfig:function(t,e,n){t.post("/api/WeiXin/jssdkconfig.action",{url:e.url}).then(function(t){t=t.data;var o={debug:!1,appId:t.appId,timestamp:t.timeStamp,nonceStr:t.nonceStr,signature:t.signature,jsApiList:e.jsList};t?(wx.config(o),n(t)):console.error("调用返回信息错误",t)},function(t){console.error("调用返回信息错误==",t)})},isObj:function(t){return t&&"object"==(void 0===t?"undefined":o(t))&&"[object object]"==Object.prototype.toString.call(t).toLowerCase()},isArray:function(t){return t&&"object"==(void 0===t?"undefined":o(t))&&t.constructor==Array},deepCopy:function(t){if(!t||"object"!=(void 0===t?"undefined":o(t)))return t;var e=Array.isArray(t)?[]:{},n=Object.keys(t);s.push(t);var i=!0,r=!1,a=void 0;try{for(var u,c=n[Symbol.iterator]();!(i=(u=c.next()).done);i=!0){var l=u.value,p=t[l];if(s.indexOf(p)>-1)throw Error("检测到属性循环引用");e[l]=this.deepCopy(p)}}catch(t){r=!0,a=t}finally{try{!i&&c.return&&c.return()}finally{if(r)throw a}}return s.pop(),e},getPercent:function(t,e){return t=parseFloat(t),e=parseFloat(e),isNaN(t)||isNaN(e)?"-":e<=0?"0%":Math.round(t/e*1e4)/100+"%"},getArrIndex:function(t,e){for(var n=0;n<e.length;n++)if(e[n]==t)return n}};var s=[]},4:function(t,e,n){function o(t){n(10)}var s=n(0)(n(9),n(11),o,null,null);t.exports=s.exports},6:function(t,e,n){"use strict";function o(t){return!t||200!==t.status&&304!==t.status&&400!==t.status?{status:-404,msg:"网络异常"}:t}function s(t){return console.log(t),-404===t.status&&alert(t.msg),t}var i=n(88),r=(n.n(i),n(93)),a=(n.n(r),n(96)),u=(n.n(a),n(97)),c=(n.n(u),n(91)),l=(n.n(c),n(94)),p=(n.n(l),n(92)),d=(n.n(p),n(95)),f=(n.n(d),n(89)),m=(n.n(f),n(90)),h=(n.n(m),n(24)),v=(n.n(h),n(26)),g=(n.n(v),n(27)),y=(n.n(g),n(98)),w=(n.n(y),n(63)),x=(n.n(w),n(64)),S=(n.n(x),n(65)),b=(n.n(S),n(66)),_=(n.n(b),n(69)),C=(n.n(_),n(67)),M=(n.n(C),n(68)),T=(n.n(M),n(70)),k=(n.n(T),n(71)),I=(n.n(k),n(72)),O=(n.n(I),n(73)),j=(n.n(O),n(75)),P=(n.n(j),n(74)),R=(n.n(P),n(62)),H=(n.n(R),n(87)),L=(n.n(H),n(59)),N=(n.n(L),n(60)),q=(n.n(N),n(61)),$=(n.n(q),n(34)),A=(n.n($),n(84)),F=(n.n(A),n(82)),U=(n.n(F),n(80)),D=(n.n(U),n(85)),E=(n.n(D),n(86)),X=(n.n(E),n(81)),W=(n.n(X),n(83)),B=(n.n(W),n(25)),G=(n.n(B),n(76)),J=(n.n(G),n(77)),Q=(n.n(J),n(79)),Y=(n.n(Q),n(78)),z=(n.n(Y),n(32)),K=(n.n(z),n(33)),V=(n.n(K),n(28)),Z=(n.n(V),n(31)),tt=(n.n(Z),n(30)),et=(n.n(tt),n(29)),nt=(n.n(et),n(23)),ot=(n.n(nt),n(53)),st=(n.n(ot),n(54)),it=(n.n(st),n(56)),rt=(n.n(it),n(55)),at=(n.n(rt),n(52)),ut=(n.n(at),n(58)),ct=(n.n(ut),n(57)),lt=(n.n(ct),n(35)),pt=(n.n(lt),n(36)),dt=(n.n(pt),n(37)),ft=(n.n(dt),n(38)),mt=(n.n(ft),n(39)),ht=(n.n(mt),n(40)),vt=(n.n(ht),n(41)),gt=(n.n(vt),n(42)),yt=(n.n(gt),n(43)),wt=(n.n(yt),n(44)),xt=(n.n(wt),n(46)),St=(n.n(xt),n(45)),bt=(n.n(St),n(47)),_t=(n.n(bt),n(48)),Ct=(n.n(_t),n(49)),Mt=(n.n(Ct),n(50)),Tt=(n.n(Mt),n(51)),kt=(n.n(Tt),n(99)),It=(n.n(kt),n(102)),Ot=(n.n(It),n(100)),jt=(n.n(Ot),n(101)),Pt=(n.n(jt),n(104)),Rt=(n.n(Pt),n(103)),Ht=(n.n(Rt),n(107)),Lt=(n.n(Ht),n(106)),Nt=(n.n(Lt),n(105)),qt=(n.n(Nt),n(108)),$t=(n.n(qt),n(22)),At=n.n($t),Ft=n(109),Ut=n.n(Ft);At.a.interceptors.request.use(function(t){return t},function(t){return Promise.reject(t)}),At.a.interceptors.response.use(function(t){return t},function(t){return Promise.resolve(t.response)}),e.a={post:function(t,e){return At()({method:"POST",url:t,data:Ut.a.stringify(e),timeout:1e4,withCredentials:!0,headers:{"X-Requested-With":"XMLHttpRequest","Content-Type":"application/x-www-form-urlencoded; charset=UTF-8"}}).then(function(t){return o(t)}).then(function(t){return s(t)})},get:function(t,e){return At()({method:"GET",url:t,params:e,timeout:1e4,withCredentials:!0,headers:{"X-Requested-With":"XMLHttpRequest"}}).then(function(t){return o(t)}).then(function(t){return s(t)})},axios:function(t){return At()(t).then(function(t){return o(t)}).then(function(t){return s(t)})},axiosPost:function(t,e,n){return At.a.post(t,e,n).then(function(t){return o(t)}).then(function(t){return s(t)})}}},622:function(t,e,n){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var o=n(21),s=n(1352),i=n.n(s),r=n(6);o.a.prototype.$http=r.a,o.a.config.productionTip=!1,n(7).attach(document.body),new o.a({el:"#app",template:"<applyService/>",components:{applyService:i.a}})},702:function(t,e,n){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var o=n(2),s=n(4),i=n.n(s);e.default={name:"applyService",components:{Toast:i.a},data:function(){return{showToast:!1,toastMsg:"",symptom:"",doctorId:"",managerId:""}},mounted:function(){var t=o.a.getRequestParam("doctorId"),e=localStorage.getItem("md")||o.a.getCookie("user_id");if(!e)return sessionStorage.setItem("tempURL",window.location.href),o.a.next("/m/login.html?tempURL="+window.location.href),!1;this.doctorId=t,this.managerId=e},methods:{save:function(){var t=this;if(!this.symptom||this.symptom.length<10)return t.showToast=!0,t.toastMsg="请输入申请理由，最少10字",!1;var e={managerId:this.managerId,doctorId:this.doctorId,buildType:2,symptom:this.symptom};this.$http.axios({method:"post",url:"/api/contracte/addUserArchives",data:e}).then(function(e){var n=e.data;n.successful&&200==n.status?(t.showToast=!0,t.toastMsg=n.resultCode.message,"SUCCESS"==n.resultCode.code&&setTimeout(function(){var t=o.a.getRequestParam("doctorId");o.a.next("/m/doctorIndex.html?md="+t)},800)):(t.showToast=!0,t.toastMsg=n.resultCode.message,console.log(n.resultCode))},function(e){console.log(e),t.showToast=!0,t.toastMsg="查询失败"})}}}},9:function(t,e,n){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var o=n(111);e.default={name:"toast",mixins:[o.a],props:{value:Boolean,time:{type:Number,default:2e3},type:{type:String,default:"success"},transition:String,width:{type:String,default:"7.6em"},isShowMask:{type:Boolean,default:!1},text:String,position:String},data:function(){return{show:!1}},created:function(){this.value&&(this.show=!0)},computed:{currentTransition:function(){return this.transition?this.transition:"top"===this.position?"vux-slide-from-top":"bottom"===this.position?"vux-slide-from-bottom":"vux-fade"},toastClass:function(){return{"weui-toast_forbidden":"warn"===this.type,"weui-toast_cancel":"cancel"===this.type,"weui-toast_success":"success"===this.type,"weui-toast_text":"text"===this.type,"vux-toast-top":"top"===this.position,"vux-toast-bottom":"bottom"===this.position,"vux-toast-middle":"middle"===this.position}},style:function(){if("text"===this.type&&"auto"===this.width)return{padding:"10px"}}},watch:{show:function(t){var e=this;t&&(this.$emit("input",!0),this.$emit("on-show"),this.fixSafariOverflowScrolling("auto"),clearTimeout(this.timeout),this.timeout=setTimeout(function(){e.show=!1,e.$emit("input",!1),e.$emit("on-hide"),e.fixSafariOverflowScrolling("touch")},this.time))},value:function(t){this.show=t}}}}},[1445]);
//# sourceMappingURL=applyService.e614ca5af1e7d7f71aa8.js.map