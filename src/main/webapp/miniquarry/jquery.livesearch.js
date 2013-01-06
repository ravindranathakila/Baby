jQuery.fn.liveUpdate = function (g) {
    g = jQuery(g);
    if (g.length) {
        var h = g.children("li"), f = h.map(function () {
            return $("a", $("div", this)[0])[0].innerHTML.toLowerCase()
        });
        this.keyup(e).keyup().parents("form").submit(function () {
            return false
        })
    }
    return this;
    function e() {
        var b = jQuery.trim(jQuery(this).val().toLowerCase()), a = [];
        if (!b) {
            h.show()
        } else {
            h.hide();
            f.each(function (d) {
                var c = this.score(b);
                if (c > 0) {
                    a.push([c, d])
                }
            });
            jQuery.each(a.sort(function (c, d) {
                return d[0] - c[0]
            }), function () {
                jQuery(h[this[1]]).show()
            })
        }
    }
};
String.prototype.score = function (j, t) {
    t = t || 0;
    if (j.length == 0) {
        return 0.9
    }
    if (j.length > this.length) {
        return 0
    }
    for (var q = j.length; q > 0; q--) {
        var c = j.substring(0, q);
        var p = this.indexOf(c);
        if (p < 0) {
            continue
        }
        if (p + j.length > this.length + t) {
            continue
        }
        var i = this.substring(p + c.length);
        var r = null;
        if (q >= j.length) {
            r = ""
        } else {
            r = j.substring(q)
        }
        var s = i.score(r, t + p);
        if (s > 0) {
            var v = this.length - i.length;
            if (p != 0) {
                var u = 0;
                var o = this.charCodeAt(p - 1);
                if (o == 32 || o == 9) {
                    for (var u = (p - 2); u >= 0; u--) {
                        o = this.charCodeAt(u);
                        v -= ((o == 32 || o == 9) ? 1 : 0.15)
                    }
                } else {
                    v -= p
                }
            }
            v += s * i.length;
            v /= this.length;
            return v
        }
    }
    return 0
};
