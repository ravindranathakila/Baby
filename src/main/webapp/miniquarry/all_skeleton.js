var ilp_tagLines = ["it's social, it's real!", "go places!", "go please!", "socialize for real!", "it's complicated, it's i like places!", "just... have fun!", "the worlds playground!", "when you decide to travel!", "head out and explore!", "escape... not press, just do it!", "now that's how you socialize!", "we take you places!", "building a happier planet!", "spinning a happier planet!", "but, why not travel?!", "fun at home? oh please!", "food for thought, place to be happy!", "where do you think you are going?!", "are there heavenly places on earth?!", "we love places, do you?!", "place matters.. when it's social..", "come.. let's go somewhere..!", "admit it! places ARE fun!", "how on earth did we miss these places?!", "that WOEID stands for Where On Earth ID!", "even when places move, WOEID's hang on to them!", "did you know you can obtain a WOEID for a place?!", "man this is boring. let's go some place!", "all places personal and public!", "no place on earth we'd like to miss!", "hey! going somewhere? tell us about it!", "i miss u.. u miss me.. it's complicatd.. it's i like places.", "u didn't see us coming did u? we come from places!", "places even Gods dig!", "have u seen heaven? it's more like some of these places!", "u know, if u do it right, most fun and places cost nothing!", "no hard feelings... get's softer with places!", "some go heaven.. we go places!", "go to hell! that's a place! even hell is fun at i like places!", "if u keep messing with me, i'll take u places!", "get lost.. in some place!", "whenever u say home, we say places!", "i'm done with you, go somewhere!", "is this some kind of joke? go some other place!", "i can't quite place u, where r u from?!", "oops we did again, things r never gonna be the same... place!", "i'm fed up with you. have u no other place to go?!", "just fooling around eh? y not go some place fun?!", "there's a place... in your heart...!", "places at best... when you say nothing at all...!", "waka waka eh eh... this time for africa!", "hw do u differentiate good tips 4m these? Rant! these R good tips!", "hw do u differentiate good tips 4m these? simple letters & ends with !", "do u knw y we wrote this stuff? too fool u! kidding!", "do u knw y we wrote this stuff? we can't call u, can we?!", "how many of these msgs do u think r there? know it? let US know!", "how many of these msgs do u think r there? just enuf to drive u crazy!"];
function ilp_getRandomTagLine() {
    return ilp_tagLines[Math.floor(Math.random() * ilp_tagLines.length)]
}
this.vtip = function () {
    this.xOffset = -5;
    this.yOffset = 30;
    $(".vtip").unbind().hover(function (a) {
        this.t = (this.title == undefined || this.title == "undefined" || this.title == "") ? ilp_getRandomTagLine() : this.title;
        this.title = "";
        this.top = (a.pageY + yOffset);
        this.left = (a.pageX + xOffset);
        $("body").append('<p id="vtip"><img id="vtipArrow" />' + this.t + "</p>");
        $("p#vtip #vtipArrow").attr("src", "/images/vtip_arrow.png");
        $("p#vtip").css("top", this.top + "px").css("left", this.left + "px").fadeIn("slow")
    },function () {
        this.title = this.t;
        $("p#vtip").fadeOut("slow").remove()
    }).mousemove(function (a) {
        this.top = (a.pageY + yOffset);
        this.left = (a.pageX + xOffset);
        $("p#vtip").css("top", this.top + "px").css("left", this.left + "px")
    });
    $(".ilp_tagl").unbind().hover(function (a) {
        this.t = ilp_getRandomTagLine();
        this.title = "";
        this.top = (a.pageY + yOffset);
        this.left = (a.pageX + xOffset);
        $("body").append('<p id="vtip"><img id="vtipArrow" />' + this.t + "</p>");
        $("p#vtip #vtipArrow").attr("src", "/images/vtip_arrow.png");
        $("p#vtip").css("top", this.top + "px").css("left", this.left + "px").fadeIn("slow")
    },function () {
        this.title = ilp_getRandomTagLine();
        $("p#vtip").fadeOut("slow").remove()
    }).mousemove(function (a) {
        this.top = (a.pageY + yOffset);
        this.left = (a.pageX + xOffset);
        $("p#vtip").css("top", this.top + "px").css("left", this.left + "px")
    })
};
(function (a) {
    a.prompt = function (p, q) {
        q = a.extend({}, a.prompt.defaults, q);
        a.prompt.currentPrefix = q.prefix;
        var e = (a.browser.msie && a.browser.version < 7);
        var g = a(document.body);
        var c = a(window);
        var b = '<div class="' + q.prefix + 'box" id="' + q.prefix + 'box">';
        if (q.useiframe && ((a("object, applet").length > 0) || e)) {
            b += '<iframe src="javascript:false;" style="display:block;position:absolute;z-index:-1;" class="' + q.prefix + 'fade" id="' + q.prefix + 'fade"></iframe>'
        } else {
            if (e) {
                a("select").css("visibility", "hidden")
            }
            b += '<div class="' + q.prefix + 'fade" id="' + q.prefix + 'fade"></div>'
        }
        b += '<div class="' + q.prefix + '" id="' + q.prefix + '"><div class="' + q.prefix + 'container"><div class="';
        b += q.prefix + 'close">X</div><div id="' + q.prefix + 'states"></div>';
        b += "</div></div></div>";
        var o = a(b).appendTo(g);
        var l = o.children("#" + q.prefix);
        var m = o.children("#" + q.prefix + "fade");
        if (p.constructor == String) {
            p = {state0: {html: p, buttons: q.buttons, focus: q.focus, submit: q.submit}}
        }
        var n = "";
        a.each(p, function (s, r) {
            r = a.extend({}, a.prompt.defaults.state, r);
            p[s] = r;
            n += '<div id="' + q.prefix + "_state_" + s + '" class="' + q.prefix + '_state" style="display:none;"><div class="' + q.prefix + 'message">' + r.html + '</div><div class="' + q.prefix + 'buttons">';
            a.each(r.buttons, function (u, t) {
                n += '<button name="' + q.prefix + "_" + s + "_button" + u + '" id="' + q.prefix + "_" + s + "_button" + u + '" value="' + t + '">' + u + "</button>"
            });
            n += "</div></div>"
        });
        l.find("#" + q.prefix + "states").html(n).children("." + q.prefix + "_state:first").css("display", "block");
        l.find("." + q.prefix + "buttons:empty").css("display", "none");
        a.each(p, function (t, s) {
            var r = l.find("#" + q.prefix + "_state_" + t);
            r.children("." + q.prefix + "buttons").children("button").click(function () {
                var w = r.children("." + q.prefix + "message");
                var u = s.buttons[a(this).text()];
                var x = {};
                a.each(l.find("#" + q.prefix + "states :input").serializeArray(), function (y, z) {
                    if (x[z.name] === undefined) {
                        x[z.name] = z.value
                    } else {
                        if (typeof x[z.name] == Array) {
                            x[z.name].push(z.value)
                        } else {
                            x[z.name] = [x[z.name], z.value]
                        }
                    }
                });
                var v = s.submit(u, w, x);
                if (v === undefined || v) {
                    d(true, u, w, x)
                }
            });
            r.find("." + q.prefix + "buttons button:eq(" + s.focus + ")").addClass(q.prefix + "defaultbutton")
        });
        var f = function () {
            o.css({top: c.scrollTop()})
        };
        var j = function () {
            if (q.persistent) {
                var s = 0;
                o.addClass(q.prefix + "warning");
                var r = setInterval(function () {
                    o.toggleClass(q.prefix + "warning");
                    if (s++ > 1) {
                        clearInterval(r);
                        o.removeClass(q.prefix + "warning")
                    }
                }, 100)
            } else {
                d()
            }
        };
        var h = function (u) {
            var t = (window.event) ? event.keyCode : u.keyCode;
            if (t == 27) {
                d()
            }
            if (t == 9) {
                var v = a(":input:enabled:visible", o);
                var s = !u.shiftKey && u.target == v[v.length - 1];
                var r = u.shiftKey && u.target == v[0];
                if (s || r) {
                    setTimeout(function () {
                        if (!v) {
                            return
                        }
                        var w = v[r === true ? v.length - 1 : 0];
                        if (w) {
                            w.focus()
                        }
                    }, 10);
                    return false
                }
            }
        };
        var i = function () {
            o.css({position: (e) ? "absolute" : "fixed", height: c.height(), width: "100%", top: (e) ? c.scrollTop() : 0, left: 0, right: 0, bottom: 0});
            m.css({position: "absolute", height: c.height(), width: "100%", top: 0, left: 0, right: 0, bottom: 0});
            l.css({position: "absolute", top: q.top, left: "50%", marginLeft: ((l.outerWidth() / 2) * -1)})
        };
        var k = function () {
            m.css({zIndex: q.zIndex, display: "none", opacity: q.opacity});
            l.css({zIndex: q.zIndex + 1, display: "none"});
            o.css({zIndex: q.zIndex})
        };
        var d = function (t, s, u, r) {
            l.remove();
            if (e) {
                g.unbind("scroll", f)
            }
            c.unbind("resize", i);
            m.fadeOut(q.overlayspeed, function () {
                m.unbind("click", j);
                m.remove();
                if (t) {
                    q.callback(s, u, r)
                }
                o.unbind("keypress", h);
                o.remove();
                if (e && !q.useiframe) {
                    a("select").css("visibility", "visible")
                }
            })
        };
        i();
        k();
        if (e) {
            c.scroll(f)
        }
        m.click(j);
        c.resize(i);
        o.bind("keydown keypress", h);
        l.find("." + q.prefix + "close").click(d);
        m.fadeIn(q.overlayspeed);
        l[q.show](q.promptspeed, q.loaded);
        if (q.timeout > 0) {
            setTimeout(a.prompt.close, q.timeout)
        }
        return l.find("#" + q.prefix + "states ." + q.prefix + "_state:first ." + q.prefix + "defaultbutton").focus()
    };
    a.prompt.defaults = {prefix: "jqi", buttons: {Ok: true}, loaded: function () {
    }, submit: function () {
        return true
    }, callback: function () {
    }, opacity: 0.6, zIndex: 999, overlayspeed: "slow", promptspeed: "fast", show: "fadeIn", focus: 0, useiframe: false, top: "15%", persistent: true, timeout: 0, state: {html: "", buttons: {Ok: true}, focus: 0, submit: function () {
        return true
    }}};
    a.prompt.currentPrefix = a.prompt.defaults.prefix;
    a.prompt.setDefaults = function (b) {
        a.prompt.defaults = a.extend({}, a.prompt.defaults, b)
    };
    a.prompt.setStateDefaults = function (b) {
        a.prompt.defaults.state = a.extend({}, a.prompt.defaults.state, b)
    };
    a.prompt.getStateContent = function (b) {
        return a("#" + a.prompt.currentPrefix + "_state_" + b)
    };
    a.prompt.getCurrentState = function () {
        return a("." + a.prompt.currentPrefix + "_state:visible")
    };
    a.prompt.getCurrentStateName = function () {
        var b = a.prompt.getCurrentState().attr("id");
        return b.replace(a.prompt.currentPrefix + "_state_", "")
    };
    a.prompt.goToState = function (b) {
        a("." + a.prompt.currentPrefix + "_state").slideUp("slow");
        a("#" + a.prompt.currentPrefix + "_state_" + b).slideDown("slow", function () {
            a(this).find("." + a.prompt.currentPrefix + "defaultbutton").focus()
        })
    };
    a.prompt.nextState = function () {
        var b = a("." + a.prompt.currentPrefix + "_state:visible").next();
        a("." + a.prompt.currentPrefix + "_state").slideUp("slow");
        b.slideDown("slow", function () {
            b.find("." + a.prompt.currentPrefix + "defaultbutton").focus()
        })
    };
    a.prompt.prevState = function () {
        var b = a("." + a.prompt.currentPrefix + "_state:visible").prev();
        a("." + a.prompt.currentPrefix + "_state").slideUp("slow");
        b.slideDown("slow", function () {
            b.find("." + a.prompt.currentPrefix + "defaultbutton").focus()
        })
    };
    a.prompt.close = function () {
        a("#" + a.prompt.currentPrefix + "box").fadeOut("fast", function () {
            a(this).remove()
        })
    }
})(jQuery);
function hasParams() {
    return(window.location.href + "").indexOf("?") != -1
}
function hasNavHash() {
    return(window.location.href + "").indexOf("#") != -1
}
function makeParam(a, b) {
    return(hasParams() ? "&" : "?") + a + "=" + b
}
function setDisplayName() {
    $.prompt({state0: {html: 'Enter a display name your friends know you as<br /> <input type="text" id="NameInput" name="NameInput" value="" />', buttons: {Cancel: false, Save: true}, submit: function (b, a, c) {
        if (b) {
            window.location = (window.location.href + "").replace(/displayname/g, "displaynameold") + makeParam("displayname", c.NameInput)
        }
        return true
    }}})
}
function ygeoarea(a) {
    switch (a) {
        case 29:
            return 3;
            break;
        case 19:
            return 3;
            break;
        case 12:
            return 5;
            break;
        case 8:
            return 8;
            break;
        case 9:
            return 11;
            break;
        case 7:
            return 12;
            break;
        case 13:
            return 13;
            break;
        default:
            return 8
    }
}
function trimInput(b, a) {
    return b.length > a ? b.toString().slice(0, a) : b
}
function ilpAlert(b, a, c) {
    $.prompt(c.photoDescriptionAlert)
}
var dataReady = false;
function checkIfDtaReady() {
    if (!dataReady) {
        var a = window.setTimeout("checkIfDtaReady()", 1000);
        return false
    } else {
        return true
    }
}
function getExt(a) {
    return(/[.]/.exec(a)) ? /[^.]+$/.exec(a.toLowerCase()) : ""
}
(function (a) {
    a.fn.appear = function (d, b) {
        var c = a.extend({data: undefined, one: true}, b);
        return this.each(function () {
            var g = a(this);
            g.appeared = false;
            if (!d) {
                g.trigger("appear", c.data);
                return
            }
            var f = a(window);
            var e = function () {
                if (!g.is(":visible")) {
                    g.appeared = false;
                    return
                }
                var k = f.scrollLeft();
                var j = f.scrollTop();
                var l = g.offset();
                var i = l.left;
                var m = l.top;
                if (m + g.height() >= j && m <= j + f.height() && i + g.width() >= k && i <= k + f.width()) {
                    if (!g.appeared) {
                        g.trigger("appear", c.data)
                    }
                } else {
                    g.appeared = false
                }
            };
            var h = function () {
                g.appeared = true;
                if (c.one) {
                    f.unbind("scroll", e);
                    var j = a.inArray(e, a.fn.appear.checks);
                    if (j >= 0) {
                        a.fn.appear.checks.splice(j, 1)
                    }
                }
                d.apply(this, arguments)
            };
            if (c.one) {
                g.one("appear", c.data, h)
            } else {
                g.bind("appear", c.data, h)
            }
            f.scroll(e);
            a.fn.appear.checks.push(e);
            (e)()
        })
    };
    a.extend(a.fn.appear, {checks: [], timeout: null, checkAll: function () {
        var b = a.fn.appear.checks.length;
        if (b > 0) {
            while (b--) {
                (a.fn.appear.checks[b])()
            }
        }
    }, run: function () {
        if (a.fn.appear.timeout) {
            clearTimeout(a.fn.appear.timeout)
        }
        a.fn.appear.timeout = setTimeout(a.fn.appear.checkAll, 20)
    }});
    a.each(["append", "prepend", "after", "before", "attr", "removeAttr", "addClass", "removeClass", "toggleClass", "remove", "css", "show", "hide"], function (c, d) {
        var b = a.fn[d];
        if (b) {
            a.fn[d] = function () {
                var e = b.apply(this, arguments);
                a.fn.appear.run();
                return e
            }
        }
    })
})(jQuery);
$(document).ready(function () {
    vtip();
    $(".gain_attn").mouseenter(function () {
        $(".lose_attn").fadeTo("fast", 0.6)
    });
    $(".gain_attn").mouseleave(function () {
        $(".lose_attn").fadeTo("fast", 1)
    });
    $(".ajax_image").appear(function () {
        this.src = this.title;
        this.title = ""
    });
    $("#SignInOn").attr("style", "display:block");
    $(window).bind("hashchange", function () {
        var a = "" + window.location.hash;
        a = a.replace(".", "\\.");
        a = a.replace(/#/g, "");
        if (a != "" && a != "JSFriends") {
            $(".sidebars").css("display", "none");
            $(".non_sidebar").addClass("use100").removeClass("use528");
            $(".carouselable").hide();
            $("." + a).fadeIn("slow");
            $("." + a).find(".wall_input").focus();
            $("." + a).find(".wall_input").blur();
            $("#" + this.id).parent().parent().appendTo(".carousel")
        }
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