var ilp_tagLines = ["it's social, it's real!", "go places!", "go please!", "socialize for real!", "it's complicated, it's i like places!", "just... have fun!", "the worlds playground!", "when you decide to travel!", "head out and explore!", "escape... not press, just do it!", "now that's how you socialize!", "we take you places!", "building a happier planet!", "spinning a happier planet!", "but, why not travel?!", "fun at home? oh please!", "food for thought, place to be happy!", "where do you think you are going?!", "are there heavenly places on earth?!", "we love places, do you?!", "place matters.. when it's social..", "come.. let's go somewhere..!", "admit it! places ARE fun!", "how on earth did we miss these places?!", "that WOEID stands for Where On Earth ID!", "even when places move, WOEID's hang on to them!", "did you know you can obtain a WOEID for a place?!", "man this is boring. let's go some place!", "all places personal and public!", "no place on earth we'd like to miss!", "hey! going somewhere? tell us about it!", "i miss u.. u miss me.. it's complicatd.. it's i like places.", "u didn't see us coming did u? we come from places!", "places even Gods dig!", "have u seen heaven? it's more like some of these places!", "u know, if u do it right, most fun and places cost nothing!", "no hard feelings... get's softer with places!", "some go heaven.. we go places!", "go to hell! that's a place! even hell is fun at i like places!", "if u keep messing with me, i'll take u places!", "get lost.. in some place!", "whenever u say home, we say places!", "i'm done with you, go somewhere!", "is this some kind of joke? go some other place!", "i can't quite place u, where r u from?!", "oops we did again, things r never gonna be the same... place!", "i'm fed up with you. have u no other place to go?!", "just fooling around eh? y not go some place fun?!", "there's a place... in your heart...!", "places at best... when you say nothing at all...!", "waka waka eh eh... this time for africa!", "hw do u differentiate good tips 4m these? Rant! these R good tips!", "hw do u differentiate good tips 4m these? simple letters & ends with !", "do u knw y we wrote this stuff? too fool u! kidding!", "do u knw y we wrote this stuff? we can't call u, can we?!", "how many of these msgs do u think r there? know it? let US know!", "how many of these msgs do u think r there? just enuf to drive u crazy!"];
function ilp_getRandomTagLine() {
    return ilp_tagLines[Math.floor(Math.random() * ilp_tagLines.length)]
}
this.vtip = function () {
    this.xOffset = -5;
    this.yOffset = 30;
    $(".vtip").unbind().hover(function (b) {
        this.t = (this.title == undefined || this.title == "undefined" || this.title == "") ? ilp_getRandomTagLine() : this.title;
        this.title = "";
        this.top = (b.pageY + yOffset);
        this.left = (b.pageX + xOffset);
        $("body").append('<p id="vtip"><img id="vtipArrow" />' + this.t + "</p>");
        $("p#vtip #vtipArrow").attr("src", "/images/vtip_arrow.png");
        $("p#vtip").css("top", this.top + "px").css("left", this.left + "px").fadeIn("slow")
    },function () {
        this.title = this.t;
        $("p#vtip").fadeOut("slow").remove()
    }).mousemove(function (b) {
        this.top = (b.pageY + yOffset);
        this.left = (b.pageX + xOffset);
        $("p#vtip").css("top", this.top + "px").css("left", this.left + "px")
    });
    $(".ilp_tagl").unbind().hover(function (b) {
        this.t = ilp_getRandomTagLine();
        this.title = "";
        this.top = (b.pageY + yOffset);
        this.left = (b.pageX + xOffset);
        $("body").append('<p id="vtip"><img id="vtipArrow" />' + this.t + "</p>");
        $("p#vtip #vtipArrow").attr("src", "/images/vtip_arrow.png");
        $("p#vtip").css("top", this.top + "px").css("left", this.left + "px").fadeIn("slow")
    },function () {
        this.title = ilp_getRandomTagLine();
        $("p#vtip").fadeOut("slow").remove()
    }).mousemove(function (b) {
        this.top = (b.pageY + yOffset);
        this.left = (b.pageX + xOffset);
        $("p#vtip").css("top", this.top + "px").css("left", this.left + "px")
    })
};
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
function hasParams() {
    return(window.location.href + "").indexOf("?") != -1
}
function hasNavHash() {
    return(window.location.href + "").indexOf("#") != -1
}
function makeParam(d, c) {
    return(hasParams() ? "&" : "?") + d + "=" + c
}
function setDisplayName() {
    $.prompt({state0: {html: 'Enter a display name your friends know you as<br /> <input type="text" id="NameInput" name="NameInput" value="" />', buttons: {Cancel: false, Save: true}, submit: function (d, e, f) {
        if (d) {
            window.location = (window.location.href + "").replace(/displayname/g, "displaynameold") + makeParam("displayname", f.NameInput)
        }
        return true
    }}})
}
function ygeoarea(b) {
    switch (b) {
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
function trimInput(c, d) {
    return c.length > d ? c.toString().slice(0, d) : c
}
function ilpAlert(d, e, f) {
    $.prompt(f.photoDescriptionAlert)
}
var dataReady = false;
function checkIfDtaReady() {
    if (!dataReady) {
        var b = window.setTimeout("checkIfDtaReady()", 1000);
        return false
    } else {
        return true
    }
}
function getExt(b) {
    return(/[.]/.exec(b)) ? /[^.]+$/.exec(b.toLowerCase()) : ""
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
        var b = "" + window.location.hash;
        b = b.replace(".", "\\.");
        b = b.replace(/#/g, "");
        if (b != "" && b != "JSFriends") {
            $(".sidebars").css("display", "none");
            $(".non_sidebar").addClass("use100").removeClass("use528");
            $(".carouselable").hide();
            $("." + b).fadeIn("slow");
            $("." + b).find(".wall_input").focus();
            $("." + b).find(".wall_input").blur();
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
