webpackJsonp([50],{1224:function(e,t){},1225:function(e,t){},1336:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{attrs:{id:"app"}},[n("div",{staticClass:"content"},[n("div",{staticClass:"header"},[n("h4",[e._v(e._s(e.info.name))]),e._v(" "),e.info.createName?n("span",[e._v("来源："+e._s(e.info.createName))]):e._e()]),e._v(" "),n("div",{staticClass:"main",attrs:{id:"main"}},[e._v("\n            简介："),n("span",[e._v(e._s(e.info.desc))])]),e._v(" "),n("div",{staticClass:"qrCode"},[n("img",{attrs:{src:"https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+e.info.ticket}})]),e._v(" "),n("div",{staticClass:"text"},[e._v("\n            长按识别二维码关注小语健康\n        ")])]),e._v(" "),e._m(0)])},staticRenderFns:[function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"footer"},[n("p",[e._v("智能评估，私人看护")]),e._v(" "),n("p",[e._v("一键关注，时时相伴")])])}]}},1406:function(e,t,n){function r(e){n(1224),n(1225)}var o=n(0)(n(758),n(1336),r,"data-v-a7da9baa",null);e.exports=o.exports},1492:function(e,t,n){n(5),e.exports=n(669)},2:function(e,t,n){"use strict";var r="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(e){return typeof e}:function(e){return e&&"function"==typeof Symbol&&e.constructor===Symbol&&e!==Symbol.prototype?"symbol":typeof e};t.a={setCookie:function(e,t,n){n||(n=30);var r=new Date;r.setMinutes(r.getMinutes()+n),document.cookie=e+"="+escape(t)+";path=/;expires ="+r.toGMTString()},getCookie:function(e){var t=document.cookie.split(";"),n={};if("token"==e)for(var r=0;r<t.length;r++){var o=t[r].indexOf("="),i=[t[r].substr(0,o),t[r].substring(o+2,t[r].length-1)];i[0]&&i[1]&&(n[i[0].replace(/^\s+/,"")]=i[1].replace(/^\s+/,""))}else for(var r=0;r<t.length;r++){var i=t[r].split("=");i[0]&&i[1]&&(n[i[0].replace(/^\s+/,"")]=i[1].replace(/^\s+/,""))}return e?unescape(n[e])||"":n},removeCookie:function(e){this.setCookie(e,1,-1)},simpleDateFormat:function(e,t){var n=new Date(e),r=function(e){return(e<10?"0":"")+e};return t.replace(/yyyy|MM|dd|HH|mm|ss/g,function(e){switch(e){case"yyyy":return r(n.getFullYear());case"MM":return r(n.getMonth()+1);case"mm":return r(n.getMinutes());case"dd":return r(n.getDate());case"HH":return r(n.getHours());case"ss":return r(n.getSeconds())}})},audioSet:function(){var e=new Date;return(e.getHours()>9?e.getHours():"0"+e.getHours())+":"+(e.getMinutes()>9?e.getMinutes():"0"+e.getMinutes())+":"+(e.getSeconds()>9?e.getSeconds():"0"+e.getSeconds())},getObjLength:function(e){var t=0;for(var n in e)t++;return t},getRequestParam:function(e){for(var t=location.search.replace(/^\?/,"").split("&"),n={},r=0;r<t.length;r++){var o=t[r].split("=");n[o[0]]=unescape(decodeURI(o[1]))}return e?n[e]||"":n},delURLQuery:function(e){var t=window.location,n=t.origin+t.pathname+"?",r=t.search.substr(1);if(r.indexOf(e)>-1){for(var o={},i=r.split("&"),s=0;s<i.length;s++)i[s]=i[s].split("="),o[i[s][0]]=i[s][1];delete o[e];return n+JSON.stringify(o).replace(/[\"\{\}]/g,"").replace(/\:/g,"=").replace(/\,/g,"&")}},next:function(e){var t=(new Date).getTime();e+=e.indexOf("?")>=0?"&_="+t:"?_="+t,window.location.href=e},mul:function(e,t){var n=0,r=0;return e=""+e,t=""+t,~e.indexOf(".")&&(n=e.split(".")[1].length),~t.indexOf(".")&&(r=t.split(".")[1].length),e=1*e.replace(".",""),t=1*t.replace(".",""),e*t/Math.pow(10,n+r)},wxConfig:function(e,t,n){e.post("/api/WeiXin/jssdkconfig.action",{url:t.url}).then(function(e){e=e.data;var r={debug:!1,appId:e.appId,timestamp:e.timeStamp,nonceStr:e.nonceStr,signature:e.signature,jsApiList:t.jsList};e?(wx.config(r),n(e)):console.error("调用返回信息错误",e)},function(e){console.error("调用返回信息错误==",e)})},isObj:function(e){return e&&"object"==(void 0===e?"undefined":r(e))&&"[object object]"==Object.prototype.toString.call(e).toLowerCase()},isArray:function(e){return e&&"object"==(void 0===e?"undefined":r(e))&&e.constructor==Array},deepCopy:function(e){if(!e||"object"!=(void 0===e?"undefined":r(e)))return e;var t=Array.isArray(e)?[]:{},n=Object.keys(e);o.push(e);var i=!0,s=!1,a=void 0;try{for(var u,c=n[Symbol.iterator]();!(i=(u=c.next()).done);i=!0){var f=u.value,d=e[f];if(o.indexOf(d)>-1)throw Error("检测到属性循环引用");t[f]=this.deepCopy(d)}}catch(e){s=!0,a=e}finally{try{!i&&c.return&&c.return()}finally{if(s)throw a}}return o.pop(),t},getPercent:function(e,t){return e=parseFloat(e),t=parseFloat(t),isNaN(e)||isNaN(t)?"-":t<=0?"0%":Math.round(e/t*1e4)/100+"%"},getArrIndex:function(e,t){for(var n=0;n<t.length;n++)if(t[n]==e)return n}};var o=[]},6:function(e,t,n){"use strict";function r(e){return!e||200!==e.status&&304!==e.status&&400!==e.status?{status:-404,msg:"网络异常"}:e}function o(e){return console.log(e),-404===e.status&&alert(e.msg),e}var i=n(88),s=(n.n(i),n(93)),a=(n.n(s),n(96)),u=(n.n(a),n(97)),c=(n.n(u),n(91)),f=(n.n(c),n(94)),d=(n.n(f),n(92)),l=(n.n(d),n(95)),p=(n.n(l),n(89)),h=(n.n(p),n(90)),v=(n.n(h),n(24)),g=(n.n(v),n(26)),m=(n.n(g),n(27)),y=(n.n(m),n(98)),b=(n.n(y),n(63)),_=(n.n(b),n(64)),w=(n.n(_),n(65)),S=(n.n(w),n(66)),x=(n.n(S),n(69)),C=(n.n(x),n(67)),M=(n.n(C),n(68)),j=(n.n(M),n(70)),O=(n.n(j),n(71)),k=(n.n(O),n(72)),q=(n.n(k),n(73)),H=(n.n(q),n(75)),I=(n.n(H),n(74)),P=(n.n(I),n(62)),R=(n.n(P),n(87)),L=(n.n(R),n(59)),N=(n.n(L),n(60)),T=(n.n(N),n(61)),U=(n.n(T),n(34)),A=(n.n(U),n(84)),D=(n.n(A),n(82)),F=(n.n(D),n(80)),E=(n.n(F),n(85)),X=(n.n(E),n(86)),W=(n.n(X),n(81)),$=(n.n(W),n(83)),G=(n.n($),n(25)),J=(n.n(G),n(76)),B=(n.n(J),n(77)),Q=(n.n(B),n(79)),Y=(n.n(Q),n(78)),z=(n.n(Y),n(32)),K=(n.n(z),n(33)),V=(n.n(K),n(28)),Z=(n.n(V),n(31)),ee=(n.n(Z),n(30)),te=(n.n(ee),n(29)),ne=(n.n(te),n(23)),re=(n.n(ne),n(53)),oe=(n.n(re),n(54)),ie=(n.n(oe),n(56)),se=(n.n(ie),n(55)),ae=(n.n(se),n(52)),ue=(n.n(ae),n(58)),ce=(n.n(ue),n(57)),fe=(n.n(ce),n(35)),de=(n.n(fe),n(36)),le=(n.n(de),n(37)),pe=(n.n(le),n(38)),he=(n.n(pe),n(39)),ve=(n.n(he),n(40)),ge=(n.n(ve),n(41)),me=(n.n(ge),n(42)),ye=(n.n(me),n(43)),be=(n.n(ye),n(44)),_e=(n.n(be),n(46)),we=(n.n(_e),n(45)),Se=(n.n(we),n(47)),xe=(n.n(Se),n(48)),Ce=(n.n(xe),n(49)),Me=(n.n(Ce),n(50)),je=(n.n(Me),n(51)),Oe=(n.n(je),n(99)),ke=(n.n(Oe),n(102)),qe=(n.n(ke),n(100)),He=(n.n(qe),n(101)),Ie=(n.n(He),n(104)),Pe=(n.n(Ie),n(103)),Re=(n.n(Pe),n(107)),Le=(n.n(Re),n(106)),Ne=(n.n(Le),n(105)),Te=(n.n(Ne),n(108)),Ue=(n.n(Te),n(22)),Ae=n.n(Ue),De=n(109),Fe=n.n(De);Ae.a.interceptors.request.use(function(e){return e},function(e){return Promise.reject(e)}),Ae.a.interceptors.response.use(function(e){return e},function(e){return Promise.resolve(e.response)}),t.a={post:function(e,t){return Ae()({method:"POST",url:e,data:Fe.a.stringify(t),timeout:1e4,withCredentials:!0,headers:{"X-Requested-With":"XMLHttpRequest","Content-Type":"application/x-www-form-urlencoded; charset=UTF-8"}}).then(function(e){return r(e)}).then(function(e){return o(e)})},get:function(e,t){return Ae()({method:"GET",url:e,params:t,timeout:1e4,withCredentials:!0,headers:{"X-Requested-With":"XMLHttpRequest"}}).then(function(e){return r(e)}).then(function(e){return o(e)})},axios:function(e){return Ae()(e).then(function(e){return r(e)}).then(function(e){return o(e)})},axiosPost:function(e,t,n){return Ae.a.post(e,t,n).then(function(e){return r(e)}).then(function(e){return o(e)})}}},669:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r=n(21),o=n(1406),i=n.n(o),s=n(6);r.a.prototype.$http=s.a,r.a.config.productionTip=!1,n(7).attach(document.body),new r.a({el:"#app",template:"<sharing/>",components:{sharing:i.a}})},758:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r=n(2);t.default={name:"sharing",data:function(){return{id:"",type:1,sourceUserId:"",info:{}}},created:function(){var e=r.a.getRequestParam();this.type=e.type,this.id=e.id,this.sourceUserId=e.sourceUserId||""},mounted:function(){this.getService()},methods:{getService:function(){var e=this;if(!this.id||!this.type)return alert("参数错误"),!1;this.$http.post("/qrcode/getShareServiceCode?paramsId="+this.id+"&type="+this.type+"&sourceUserId="+this.sourceUserId,{}).then(function(t){var n=t.data;if(n.successful&&200==n.status){if(n.data&&(e.info=n.data,4==e.type)){document.getElementById("main").innerHTML=n.data.desc;for(var r=document.querySelectorAll("#main img"),o=0;o<r.length;o++)r[o].className="img-responsive",r[o].style.display="block",r[o].style.maxWidth="100%",r[o].style.height="auto"}}else alert("查询失败"),console.error(n)},function(e){alert("查询失败"),console.error(e)})}}}}},[1492]);
//# sourceMappingURL=sharing.6b0554939ad1a7645681.js.map