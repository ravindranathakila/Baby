try {
    setTimeout("initialize_gmap_scripts_transparently()", 7000)
} catch (err) {
    alert(err);
    if (confirm("You internet connection seems to be flaky. Reload web page?")) {
        try {
            setTimeout("initialize_gmap_scripts_transparently()", 7000)
        } catch (err) {
            alert("Sorry, Still could not load required stuff.")
        }
    } else {
        alert("Ok. Not reloading. However things might not work!")
    }
}
(function (b) {
    b.prompt = function (r, a) {
        a = b.extend({}, b.prompt.defaults, a);
        b.prompt.currentPrefix = a.prefix;
        var C = (b.browser.msie && b.browser.version < 7);
        var A = b(document.body);
        var E = b(window);
        var F = '<div class="' + a.prefix + 'box" id="' + a.prefix + 'box">';
        if (a.useiframe && ((b("object, applet").length > 0) || C)) {
            F += '<iframe src="javascript:false;" style="display:block;position:absolute;z-index:-1;" class="' + a.prefix + 'fade" id="' + a.prefix + 'fade"></iframe>'
        } else {
            if (C) {
                b("select").css("visibility", "hidden")
            }
            F += '<div class="' + a.prefix + 'fade" id="' + a.prefix + 'fade"></div>'
        }
        F += '<div class="' + a.prefix + '" id="' + a.prefix + '"><div class="' + a.prefix + 'container"><div class="';
        F += a.prefix + 'close">X</div><div id="' + a.prefix + 'states"></div>';
        F += "</div></div></div>";
        var s = b(F).appendTo(A);
        var v = s.children("#" + a.prefix);
        var u = s.children("#" + a.prefix + "fade");
        if (r.constructor == String) {
            r = {state0: {html: r, buttons: a.buttons, focus: a.focus, submit: a.submit}}
        }
        var t = "";
        b.each(r, function (c, d) {
            d = b.extend({}, b.prompt.defaults.state, d);
            r[c] = d;
            t += '<div id="' + a.prefix + "_state_" + c + '" class="' + a.prefix + '_state" style="display:none;"><div class="' + a.prefix + 'message">' + d.html + '</div><div class="' + a.prefix + 'buttons">';
            b.each(d.buttons, function (e, f) {
                t += '<button name="' + a.prefix + "_" + c + "_button" + e + '" id="' + a.prefix + "_" + c + "_button" + e + '" value="' + f + '">' + e + "</button>"
            });
            t += "</div></div>"
        });
        v.find("#" + a.prefix + "states").html(t).children("." + a.prefix + "_state:first").css("display", "block");
        v.find("." + a.prefix + "buttons:empty").css("display", "none");
        b.each(r, function (c, d) {
            var e = v.find("#" + a.prefix + "_state_" + c);
            e.children("." + a.prefix + "buttons").children("button").click(function () {
                var g = e.children("." + a.prefix + "message");
                var i = d.buttons[b(this).text()];
                var f = {};
                b.each(v.find("#" + a.prefix + "states :input").serializeArray(), function (k, j) {
                    if (f[j.name] === undefined) {
                        f[j.name] = j.value
                    } else {
                        if (typeof f[j.name] == Array) {
                            f[j.name].push(j.value)
                        } else {
                            f[j.name] = [f[j.name], j.value]
                        }
                    }
                });
                var h = d.submit(i, g, f);
                if (h === undefined || h) {
                    D(true, i, g, f)
                }
            });
            e.find("." + a.prefix + "buttons button:eq(" + d.focus + ")").addClass(a.prefix + "defaultbutton")
        });
        var B = function () {
            s.css({top: E.scrollTop()})
        };
        var x = function () {
            if (a.persistent) {
                var c = 0;
                s.addClass(a.prefix + "warning");
                var d = setInterval(function () {
                    s.toggleClass(a.prefix + "warning");
                    if (c++ > 1) {
                        clearInterval(d);
                        s.removeClass(a.prefix + "warning")
                    }
                }, 100)
            } else {
                D()
            }
        };
        var z = function (d) {
            var e = (window.event) ? event.keyCode : d.keyCode;
            if (e == 27) {
                D()
            }
            if (e == 9) {
                var c = b(":input:enabled:visible", s);
                var f = !d.shiftKey && d.target == c[c.length - 1];
                var g = d.shiftKey && d.target == c[0];
                if (f || g) {
                    setTimeout(function () {
                        if (!c) {
                            return
                        }
                        var h = c[g === true ? c.length - 1 : 0];
                        if (h) {
                            h.focus()
                        }
                    }, 10);
                    return false
                }
            }
        };
        var y = function () {
            s.css({position: (C) ? "absolute" : "fixed", height: E.height(), width: "100%", top: (C) ? E.scrollTop() : 0, left: 0, right: 0, bottom: 0});
            u.css({position: "absolute", height: E.height(), width: "100%", top: 0, left: 0, right: 0, bottom: 0});
            v.css({position: "absolute", top: a.top, left: "50%", marginLeft: ((v.outerWidth() / 2) * -1)})
        };
        var w = function () {
            u.css({zIndex: a.zIndex, display: "none", opacity: a.opacity});
            v.css({zIndex: a.zIndex + 1, display: "none"});
            s.css({zIndex: a.zIndex})
        };
        var D = function (d, e, c, f) {
            v.remove();
            if (C) {
                A.unbind("scroll", B)
            }
            E.unbind("resize", y);
            u.fadeOut(a.overlayspeed, function () {
                u.unbind("click", x);
                u.remove();
                if (d) {
                    a.callback(e, c, f)
                }
                s.unbind("keypress", z);
                s.remove();
                if (C && !a.useiframe) {
                    b("select").css("visibility", "visible")
                }
            })
        };
        y();
        w();
        if (C) {
            E.scroll(B)
        }
        u.click(x);
        E.resize(y);
        s.bind("keydown keypress", z);
        v.find("." + a.prefix + "close").click(D);
        u.fadeIn(a.overlayspeed);
        v[a.show](a.promptspeed, a.loaded);
        if (a.timeout > 0) {
            setTimeout(b.prompt.close, a.timeout)
        }
        return v.find("#" + a.prefix + "states ." + a.prefix + "_state:first ." + a.prefix + "defaultbutton").focus()
    };
    b.prompt.defaults = {prefix: "jqi", buttons: {Ok: true}, loaded: function () {
    }, submit: function () {
        return true
    }, callback: function () {
    }, opacity: 0.6, zIndex: 999, overlayspeed: "slow", promptspeed: "fast", show: "fadeIn", focus: 0, useiframe: false, top: "15%", persistent: true, timeout: 0, state: {html: "", buttons: {Ok: true}, focus: 0, submit: function () {
        return true
    }}};
    b.prompt.currentPrefix = b.prompt.defaults.prefix;
    b.prompt.setDefaults = function (a) {
        b.prompt.defaults = b.extend({}, b.prompt.defaults, a)
    };
    b.prompt.setStateDefaults = function (a) {
        b.prompt.defaults.state = b.extend({}, b.prompt.defaults.state, a)
    };
    b.prompt.getStateContent = function (a) {
        return b("#" + b.prompt.currentPrefix + "_state_" + a)
    };
    b.prompt.getCurrentState = function () {
        return b("." + b.prompt.currentPrefix + "_state:visible")
    };
    b.prompt.getCurrentStateName = function () {
        var a = b.prompt.getCurrentState().attr("id");
        return a.replace(b.prompt.currentPrefix + "_state_", "")
    };
    b.prompt.goToState = function (a) {
        b("." + b.prompt.currentPrefix + "_state").slideUp("slow");
        b("#" + b.prompt.currentPrefix + "_state_" + a).slideDown("slow", function () {
            b(this).find("." + b.prompt.currentPrefix + "defaultbutton").focus()
        })
    };
    b.prompt.nextState = function () {
        var a = b("." + b.prompt.currentPrefix + "_state:visible").next();
        b("." + b.prompt.currentPrefix + "_state").slideUp("slow");
        a.slideDown("slow", function () {
            a.find("." + b.prompt.currentPrefix + "defaultbutton").focus()
        })
    };
    b.prompt.prevState = function () {
        var a = b("." + b.prompt.currentPrefix + "_state:visible").prev();
        b("." + b.prompt.currentPrefix + "_state").slideUp("slow");
        a.slideDown("slow", function () {
            a.find("." + b.prompt.currentPrefix + "defaultbutton").focus()
        })
    };
    b.prompt.close = function () {
        b("#" + b.prompt.currentPrefix + "box").fadeOut("fast", function () {
            b(this).remove()
        })
    }
})(jQuery);
function setCookie(g, i, f) {
    var h = new Date();
    h.setDate(h.getDate() + f);
    var j = escape(i) + ((f == null) ? "" : "; expires=" + h.toUTCString());
    document.cookie = g + "=" + j
}
function getCookie(f) {
    var j, g, h, i = document.cookie.split(";");
    for (j = 0; j < i.length; j++) {
        g = i[j].substr(0, i[j].indexOf("="));
        h = i[j].substr(i[j].indexOf("=") + 1);
        g = g.replace(/^\s+|\s+$/g, "");
        if (g == f) {
            return unescape(h)
        }
    }
}
function gup(e) {
    e = e.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var f = "[\\?&]" + e + "=([^&#]*)";
    var g = new RegExp(f);
    var h = g.exec(window.location.href);
    if (h == null) {
        return""
    } else {
        return h[1]
    }
}
(function (b) {
    b.fn.appear = function (e, a) {
        var f = b.extend({data: undefined, one: true}, a);
        return this.each(function () {
            var d = b(this);
            d.appeared = false;
            if (!e) {
                d.trigger("appear", f.data);
                return
            }
            var i = b(window);
            var j = function () {
                if (!d.is(":visible")) {
                    d.appeared = false;
                    return
                }
                var n = i.scrollLeft();
                var o = i.scrollTop();
                var h = d.offset();
                var p = h.left;
                var g = h.top;
                if (g + d.height() >= o && g <= o + i.height() && p + d.width() >= n && p <= n + i.width()) {
                    if (!d.appeared) {
                        d.trigger("appear", f.data)
                    }
                } else {
                    d.appeared = false
                }
            };
            var c = function () {
                d.appeared = true;
                if (f.one) {
                    i.unbind("scroll", j);
                    var g = b.inArray(j, b.fn.appear.checks);
                    if (g >= 0) {
                        b.fn.appear.checks.splice(g, 1)
                    }
                }
                e.apply(this, arguments)
            };
            if (f.one) {
                d.one("appear", f.data, c)
            } else {
                d.bind("appear", f.data, c)
            }
            i.scroll(j);
            b.fn.appear.checks.push(j);
            (j)()
        })
    };
    b.extend(b.fn.appear, {checks: [], timeout: null, checkAll: function () {
        var a = b.fn.appear.checks.length;
        if (a > 0) {
            while (a--) {
                (b.fn.appear.checks[a])()
            }
        }
    }, run: function () {
        if (b.fn.appear.timeout) {
            clearTimeout(b.fn.appear.timeout)
        }
        b.fn.appear.timeout = setTimeout(b.fn.appear.checkAll, 20)
    }});
    b.each(["append", "prepend", "after", "before", "attr", "removeAttr", "addClass", "removeClass", "toggleClass", "remove", "css", "show", "hide"], function (f, e) {
        var a = b.fn[e];
        if (a) {
            b.fn[e] = function () {
                var c = a.apply(this, arguments);
                b.fn.appear.run();
                return c
            }
        }
    })
})(jQuery);
$(document).ready(function () {
    $(".lose_attn").mouseenter(function () {
        $(".lose_attn").fadeTo("fast", 1)
    });
    $(".lose_attn").mouseleave(function () {
        $(".lose_attn").fadeTo("fast", 0.1)
    });
    var f = $("#impromptuPlaceDetails").html();
    $("#impromptuPlaceDetails").remove();
    impromptuPlaceDetailsPopup = {state0: {html: f, buttons: {"ADD PLACE": 0, Cancel: 1}, focus: 0, submit: function (j, k, b) {
        switch (j) {
            case 0:
                var a = $("#impromptuPlaceDetailsPlaceName").attr("value");
                a = a.substring(0, 29);
                var c = $("#impromptuPlaceDetailsPlaceDetails").attr("value");
                c = c.substring(0, 499);
                setNotification("Be patient while you are taken to the place you just created!");
                window.location.href = "/page/_org?category=143&woehint=" + lastSelectedLocation.lat + "," + lastSelectedLocation.lng + "&placename=" + a + "&placedetails=" + c;
                return false;
            case 1:
                $.prompt.close();
                return false
        }
    }}};
    $.prompt.defaults.opacity = 1;
    focusLat = 0;
    focusLng = 0;
    zoom = 2;
    try {
        var e = "http://www.geoplugin.net/json.gp?jsoncallback=?";
        $.getJSON(e, function (a) {
            focusLng = a.geoplugin_longitude;
            focusLat = a.geoplugin_latitude;
            zoom = a.geoplugin_city != null ? 11 : (a.geoplugin_region != null ? 9 : (a.geoplugin_countryName ? 6 : 2));
            browserLocation = (a.geoplugin_city != null ? a.geoplugin_city + "," : "") + (a.geoplugin_region != null ? a.geoplugin_region + "," : "") + (a.geoplugin_countryName != null ? a.geoplugin_countryName : "");
            document.getElementById("searchboxmap").value = browserLocation
        })
    } catch (d) {
        alert(d)
    }
    initialize_gmap_scripts();
    $(".ajax_image").appear(function () {
        this.src = this.title;
        this.title = ""
    });
    $("#Juice").mouseenter(function (a) {
        a.preventDefault();
        $("html,body").animate({scrollTop: $("#Juice").offset().top}, 500)
    });
    $(".click_indicator").hover(function () {
        $(this).fadeTo(0, 0.8)
    },function () {
        $(this).fadeTo(0, 1)
    }).click(function () {
        $(this).fadeTo(0, 0.3)
    });
    $(".click_indicator_strong").hover(function () {
        $(this).fadeTo(0, 0.5)
    },function () {
        $(this).fadeTo(0, 1)
    }).click(function () {
        $(this).fadeTo(0, 0.3)
    });
    $(".fillPageType").addClass($(".pageType").attr("value"))
});
focusMapWithLatLngLoop = undefined;
function focusMapWithLatLng() {
    try {
        if (gup("latitude") != "" && gup("longitude") != "") {
            map.setCenter(new google.maps.LatLng(gup("latitude"), gup("longitude")));
            if (gup("type") != "") {
                var d = gup("type");
                if (d == "building") {
                    map.setZoom(17)
                } else {
                    if (d == "area") {
                        map.setZoom(15)
                    } else {
                        if (d == "town") {
                            map.setZoom(13)
                        } else {
                            if (d == "city") {
                                map.setZoom(11)
                            } else {
                                map.setZoom(10)
                            }
                        }
                    }
                }
            }
            clearInterval(focusMapWithLatLngLoop);
            try {
                $.prompt.close()
            } catch (c) {
            }
        } else {
        }
    } catch (c) {
    }
}
focusMapWithLatLngLoop = setInterval("focusMapWithLatLng()", 2000);
zoomInHint = true;
