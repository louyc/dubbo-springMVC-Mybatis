/*
 * jquery-audioPlay.js
 * by zhangxinxu	http://www.zhangxinxu.com/
 * 2011-04-28 v1.0
 */
 
(function($) {
	$.fn.audioPlay = function(options) {
		var defaults = {
			name: "audioPlay",
			urlMp3: "",
			urlOgg: "",
			clone: false	
		};
		var params = $.extend(defaults, options || {}), audioHtml = "";
		
		$(this).each(function(i) {
			var strIdRoot = params.name;
				tmpAutioHtml = '<audio id="'+ strIdRoot + i +'" controls="controls" preload="auto" style="position:absolute; visibility:hidden;">' +
					'<source src="'+ params.urlMp3 +'"></source>' +
					'<source src="'+ params.urlOgg +'"></source>' +
				'</audio>';
			if (params.clone) {
				audioHtml = audioHtml + tmpAutioHtml;
				$(this).data("targetId", strIdRoot + i);
			} else {
				if (!i) {
					audioHtml = tmpAutioHtml;
				}
				$(this).data("targetId", strIdRoot + "0");
			}
		});
		$("body").append(audioHtml);
		$(this).mouseenter(function() {
			var targetId = $(this).data("targetId");
			$("#" + targetId).get(0).play();
		});
	};
})(jQuery);
 
 
 
 