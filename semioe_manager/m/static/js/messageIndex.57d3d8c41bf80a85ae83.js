webpackJsonp([44],{10:function(t,e){},11:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"vux-toast"},[n("div",{directives:[{name:"show",rawName:"v-show",value:t.isShowMask&&t.show,expression:"isShowMask && show"}],staticClass:"weui-mask_transparent"}),t._v(" "),n("transition",{attrs:{name:t.currentTransition}},[n("div",{directives:[{name:"show",rawName:"v-show",value:t.show,expression:"show"}],staticClass:"weui-toast",class:t.toastClass,style:{width:t.width}},[n("i",{directives:[{name:"show",rawName:"v-show",value:"text"!==t.type,expression:"type !== 'text'"}],staticClass:"weui-icon-success-no-circle weui-icon_toast"}),t._v(" "),t.text?n("p",{staticClass:"weui-toast__content",style:t.style,domProps:{innerHTML:t._s(t.text)}}):n("p",{staticClass:"weui-toast__content"},[t._t("default")],2)])])],1)},staticRenderFns:[]}},1158:function(t,e){},1159:function(t,e){},1283:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"messageIndex"},[n("div",{staticClass:"list"},t._l(t.newsList,function(e,s){return n("div",{key:s,staticClass:"list-item"},[n("div",{staticClass:"list-item-head"},[n("div",{staticClass:"list-item-head-img"},[n("img",{attrs:{src:e.messageFromImg}})])]),t._v(" "),n("div",{staticClass:"list-item-content"},[n("div",{staticClass:"list-item-content-name"},[t._v(t._s(e.messageFromName)+"：")]),t._v(" "),n("div",{staticClass:"list-item-content-text",domProps:{innerHTML:t._s(e.content)}}),t._v(" "),n("div",{staticClass:"list-item-content-time"},[t._v(t._s(e.createTime))])])])})),t._v(" "),n("toast",{attrs:{text:t.toastMsg,type:"text","is-show-mask":!0},model:{value:t.showToast,callback:function(e){t.showToast=e},expression:"showToast"}})],1)},staticRenderFns:[]}},1350:function(t,e,n){function s(t){n(1158),n(1159)}var o=n(0)(n(716),n(1283),s,"data-v-5a32ffd1",null);t.exports=o.exports},1432:function(t,e,n){n(6),t.exports=n(631)},2:function(t,e,n){"use strict";var s="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(t){return typeof t}:function(t){return t&&"function"==typeof Symbol&&t.constructor===Symbol&&t!==Symbol.prototype?"symbol":typeof t};e.a={setCookie:function(t,e,n){n||(n=30);var s=new Date;s.setMinutes(s.getMinutes()+n),document.cookie=t+"="+escape(e)+";path=/;expires ="+s.toGMTString()},getCookie:function(t){var e=document.cookie.split(";"),n={};if("token"==t)for(var s=0;s<e.length;s++){var o=e[s].indexOf("="),i=[e[s].substr(0,o),e[s].substring(o+2,e[s].length-1)];i[0]&&i[1]&&(n[i[0].replace(/^\s+/,"")]=i[1].replace(/^\s+/,""))}else for(var s=0;s<e.length;s++){var i=e[s].split("=");i[0]&&i[1]&&(n[i[0].replace(/^\s+/,"")]=i[1].replace(/^\s+/,""))}return t?unescape(n[t])||"":n},removeCookie:function(t){this.setCookie(t,1,-1)},simpleDateFormat:function(t,e){var n=new Date(t),s=function(t){return(t<10?"0":"")+t};return e.replace(/yyyy|MM|dd|HH|mm|ss/g,function(t){switch(t){case"yyyy":return s(n.getFullYear());case"MM":return s(n.getMonth()+1);case"mm":return s(n.getMinutes());case"dd":return s(n.getDate());case"HH":return s(n.getHours());case"ss":return s(n.getSeconds())}})},audioSet:function(){var t=new Date;return(t.getHours()>9?t.getHours():"0"+t.getHours())+":"+(t.getMinutes()>9?t.getMinutes():"0"+t.getMinutes())+":"+(t.getSeconds()>9?t.getSeconds():"0"+t.getSeconds())},getObjLength:function(t){var e=0;for(var n in t)e++;return e},getRequestParam:function(t){for(var e=location.search.replace(/^\?/,"").split("&"),n={},s=0;s<e.length;s++){var o=e[s].split("=");n[o[0]]=unescape(decodeURI(o[1]))}return t?n[t]||"":n},delURLQuery:function(t){var e=window.location,n=e.origin+e.pathname+"?",s=e.search.substr(1);if(s.indexOf(t)>-1){for(var o={},i=s.split("&"),r=0;r<i.length;r++)i[r]=i[r].split("="),o[i[r][0]]=i[r][1];delete o[t];return n+JSON.stringify(o).replace(/[\"\{\}]/g,"").replace(/\:/g,"=").replace(/\,/g,"&")}},next:function(t){var e=(new Date).getTime();t+=t.indexOf("?")>=0?"&_="+e:"?_="+e,window.location.href=t},mul:function(t,e){var n=0,s=0;return t=""+t,e=""+e,~t.indexOf(".")&&(n=t.split(".")[1].length),~e.indexOf(".")&&(s=e.split(".")[1].length),t=1*t.replace(".",""),e=1*e.replace(".",""),t*e/Math.pow(10,n+s)},wxConfig:function(t,e,n){t.post("/api/WeiXin/jssdkconfig.action",{url:e.url}).then(function(t){t=t.data;var s={debug:!1,appId:t.appId,timestamp:t.timeStamp,nonceStr:t.nonceStr,signature:t.signature,jsApiList:e.jsList};t?(wx.config(s),n(t)):console.error("调用返回信息错误",t)},function(t){console.error("调用返回信息错误==",t)})},isObj:function(t){return t&&"object"==(void 0===t?"undefined":s(t))&&"[object object]"==Object.prototype.toString.call(t).toLowerCase()},isArray:function(t){return t&&"object"==(void 0===t?"undefined":s(t))&&t.constructor==Array},deepCopy:function(t){if(!t||"object"!=(void 0===t?"undefined":s(t)))return t;var e=Array.isArray(t)?[]:{},n=Object.keys(t);o.push(t);var i=!0,r=!1,a=void 0;try{for(var u,c=n[Symbol.iterator]();!(i=(u=c.next()).done);i=!0){var l=u.value,d=t[l];if(o.indexOf(d)>-1)throw Error("检测到属性循环引用");e[l]=this.deepCopy(d)}}catch(t){r=!0,a=t}finally{try{!i&&c.return&&c.return()}finally{if(r)throw a}}return o.pop(),e},getPercent:function(t,e){return t=parseFloat(t),e=parseFloat(e),isNaN(t)||isNaN(e)?"-":e<=0?"0%":Math.round(t/e*1e4)/100+"%"},getArrIndex:function(t,e){for(var n=0;n<e.length;n++)if(e[n]==t)return n}};var o=[]},4:function(t,e,n){function s(t){n(10)}var o=n(0)(n(9),n(11),s,null,null);t.exports=o.exports},631:function(t,e,n){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var s=n(20),o=n(1350),i=n.n(o),r=n(7);s.a.prototype.$http=r.a,s.a.config.productionTip=!1,n(5).attach(document.body),new s.a({el:"#app",template:"<messageIndex/>",components:{messageIndex:i.a}})},7:function(t,e,n){"use strict";function s(t){return!t||200!==t.status&&304!==t.status&&400!==t.status?{status:-404,msg:"网络异常"}:t}function o(t){return console.log(t),-404===t.status&&alert(t.msg),t}var i=n(87),r=(n.n(i),n(92)),a=(n.n(r),n(95)),u=(n.n(a),n(96)),c=(n.n(u),n(90)),l=(n.n(c),n(93)),d=(n.n(l),n(91)),f=(n.n(d),n(94)),p=(n.n(f),n(88)),h=(n.n(p),n(89)),m=(n.n(h),n(23)),g=(n.n(m),n(25)),v=(n.n(g),n(26)),w=(n.n(v),n(97)),y=(n.n(w),n(62)),x=(n.n(y),n(63)),_=(n.n(x),n(64)),S=(n.n(_),n(65)),b=(n.n(S),n(68)),C=(n.n(b),n(66)),M=(n.n(C),n(67)),T=(n.n(M),n(69)),O=(n.n(T),n(70)),k=(n.n(O),n(71)),I=(n.n(k),n(72)),j=(n.n(I),n(74)),L=(n.n(j),n(73)),P=(n.n(L),n(61)),H=(n.n(P),n(86)),R=(n.n(H),n(58)),F=(n.n(R),n(59)),N=(n.n(F),n(60)),$=(n.n(N),n(33)),q=(n.n($),n(83)),A=(n.n(q),n(81)),D=(n.n(A),n(79)),U=(n.n(D),n(84)),X=(n.n(U),n(85)),E=(n.n(X),n(80)),W=(n.n(E),n(82)),B=(n.n(W),n(24)),G=(n.n(B),n(75)),J=(n.n(G),n(76)),z=(n.n(J),n(78)),Q=(n.n(z),n(77)),Y=(n.n(Q),n(31)),K=(n.n(Y),n(32)),V=(n.n(K),n(27)),Z=(n.n(V),n(30)),tt=(n.n(Z),n(29)),et=(n.n(tt),n(28)),nt=(n.n(et),n(22)),st=(n.n(nt),n(52)),ot=(n.n(st),n(53)),it=(n.n(ot),n(55)),rt=(n.n(it),n(54)),at=(n.n(rt),n(51)),ut=(n.n(at),n(57)),ct=(n.n(ut),n(56)),lt=(n.n(ct),n(34)),dt=(n.n(lt),n(35)),ft=(n.n(dt),n(36)),pt=(n.n(ft),n(37)),ht=(n.n(pt),n(38)),mt=(n.n(ht),n(39)),gt=(n.n(mt),n(40)),vt=(n.n(gt),n(41)),wt=(n.n(vt),n(42)),yt=(n.n(wt),n(43)),xt=(n.n(yt),n(45)),_t=(n.n(xt),n(44)),St=(n.n(_t),n(46)),bt=(n.n(St),n(47)),Ct=(n.n(bt),n(48)),Mt=(n.n(Ct),n(49)),Tt=(n.n(Mt),n(50)),Ot=(n.n(Tt),n(98)),kt=(n.n(Ot),n(101)),It=(n.n(kt),n(99)),jt=(n.n(It),n(100)),Lt=(n.n(jt),n(103)),Pt=(n.n(Lt),n(102)),Ht=(n.n(Pt),n(106)),Rt=(n.n(Ht),n(105)),Ft=(n.n(Rt),n(104)),Nt=(n.n(Ft),n(107)),$t=(n.n(Nt),n(21)),qt=n.n($t),At=n(108),Dt=n.n(At);qt.a.interceptors.request.use(function(t){return t},function(t){return Promise.reject(t)}),qt.a.interceptors.response.use(function(t){return t},function(t){return Promise.resolve(t.response)}),e.a={post:function(t,e){return qt()({method:"POST",url:t,data:Dt.a.stringify(e),timeout:1e4,withCredentials:!0,headers:{"X-Requested-With":"XMLHttpRequest","Content-Type":"application/x-www-form-urlencoded; charset=UTF-8"}}).then(function(t){return s(t)}).then(function(t){return o(t)})},get:function(t,e){return qt()({method:"GET",url:t,params:e,timeout:1e4,withCredentials:!0,headers:{"X-Requested-With":"XMLHttpRequest"}}).then(function(t){return s(t)}).then(function(t){return o(t)})},axios:function(t){return qt()(t).then(function(t){return s(t)}).then(function(t){return o(t)})},axiosPost:function(t,e,n){return qt.a.post(t,e,n).then(function(t){return s(t)}).then(function(t){return o(t)})}}},716:function(t,e,n){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var s=n(2),o=n(4),i=n.n(o);e.default={name:"messageIndex",components:{Toast:i.a},data:function(){return{showToast:!1,toastMsg:"",id:"",doctorId:"",newsList:[]}},mounted:function(){var t=localStorage.getItem("md")||s.a.getCookie("user_id");if(!t||"undefined"==t)return sessionStorage.setItem("tempURL",window.location.href),s.a.next("/m/login.html?tempURL="+window.location.href),!1;this.doctorId=s.a.getRequestParam("id"),this.id=t,this.updateState()},methods:{init:function(){var t=this;this.$http.axios({method:"POST",url:"/message/getPageMessages",data:{pageSize:1,pageNumber:100,messageTo:this.id,type:3,messageFrom:this.doctorId}}).then(function(e){var n=e.data;n.successful&&200==n.status?t.newsList=n.items:(console.log(n.resultCode),t.showToast=!0,t.toastMsg=n.resultCode.message)},function(e){console.log(e),t.showToast=!0,t.toastMsg="操作失败"})},updateState:function(){var t=this;this.$http.post("/message/readOverMessage",{userId:this.id,messageFromId:this.doctorId,type:3}).then(function(e){var n=e.data;n.successful&&200==n.status?t.init():(t.showToast=!0,t.toastMsg=n.resultCode.message,console.log(n.resultCode))},function(e){console.log(e),t.showToast=!0,t.toastMsg="查询失败"})}}}},9:function(t,e,n){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var s=n(111);e.default={name:"toast",mixins:[s.a],props:{value:Boolean,time:{type:Number,default:2e3},type:{type:String,default:"success"},transition:String,width:{type:String,default:"7.6em"},isShowMask:{type:Boolean,default:!1},text:String,position:String},data:function(){return{show:!1}},created:function(){this.value&&(this.show=!0)},computed:{currentTransition:function(){return this.transition?this.transition:"top"===this.position?"vux-slide-from-top":"bottom"===this.position?"vux-slide-from-bottom":"vux-fade"},toastClass:function(){return{"weui-toast_forbidden":"warn"===this.type,"weui-toast_cancel":"cancel"===this.type,"weui-toast_success":"success"===this.type,"weui-toast_text":"text"===this.type,"vux-toast-top":"top"===this.position,"vux-toast-bottom":"bottom"===this.position,"vux-toast-middle":"middle"===this.position}},style:function(){if("text"===this.type&&"auto"===this.width)return{padding:"10px"}}},watch:{show:function(t){var e=this;t&&(this.$emit("input",!0),this.$emit("on-show"),this.fixSafariOverflowScrolling("auto"),clearTimeout(this.timeout),this.timeout=setTimeout(function(){e.show=!1,e.$emit("input",!1),e.$emit("on-hide"),e.fixSafariOverflowScrolling("touch")},this.time))},value:function(t){this.show=t}}}}},[1432]);
//# sourceMappingURL=messageIndex.57d3d8c41bf80a85ae83.js.map