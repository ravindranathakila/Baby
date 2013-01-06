(function (b) {
    b.fn.filestyle = function (a) {
        var d = {width: 250};
        if (a) {
            b.extend(d, a)
        }
        return this.each(function () {
            var g = this;
            var c = b("<div>").css({width: d.imagewidth + "px", height: d.imageheight + "px", background: "url(" + d.image + ") 0 0 no-repeat", "background-position": "right", display: "inline", position: "absolute", overflow: "hidden"});
            var h = b('<input class="file">').addClass(b(g).attr("class")).css({display: "inline", width: d.width + "px"});
            b(g).before(h);
            b(g).wrap(c);
            b(g).css({position: "relative", height: d.imageheight + "px", width: d.width + "px", display: "inline", cursor: "pointer", opacity: "0.0"});
            if (b.browser.mozilla) {
                if (/Win/.test(navigator.platform)) {
                    b(g).css("margin-left", "-142px")
                } else {
                    b(g).css("margin-left", "-168px")
                }
            } else {
                b(g).css("margin-left", d.imagewidth - d.width + "px")
            }
            b(g).bind("change", function () {
                h.val(b(g).val())
            })
        })
    }
})(jQuery);
