(function(a){a.fn.filestyle=function(b){var c={width:250};if(b){a.extend(c,b)}return this.each(function(){var e=this;var f=a("<div>").css({width:c.imagewidth+"px",height:c.imageheight+"px",background:"url("+c.image+") 0 0 no-repeat","background-position":"right",display:"inline",position:"absolute",overflow:"hidden"});var d=a('<input class="file">').addClass(a(e).attr("class")).css({display:"inline",width:c.width+"px"});a(e).before(d);a(e).wrap(f);a(e).css({position:"relative",height:c.imageheight+"px",width:c.width+"px",display:"inline",cursor:"pointer",opacity:"0.0"});if(a.browser.mozilla){if(/Win/.test(navigator.platform)){a(e).css("margin-left","-142px")}else{a(e).css("margin-left","-168px")}}else{a(e).css("margin-left",c.imagewidth-c.width+"px")}a(e).bind("change",function(){d.val(a(e).val())})})}})(jQuery);