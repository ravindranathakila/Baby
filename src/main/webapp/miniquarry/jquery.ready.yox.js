RIGHT_CLICKS = 0;
RIGHT_CLICK_MSG = "Geee! We are open-source. Shall I give u the sources?;)\n(Ur ryt-click menu will work, just ryt-click again ;)";
function click(b) {
    if (RIGHT_CLICKS == 0) {
        RIGHT_CLICKS++;
        if (navigator.appName == "Netscape" && b.which == 3) {
            if (confirm(RIGHT_CLICK_MSG)) {
                window.location = "http://tinyurl.com/ilikeplaces-sources"
            }
            return false
        } else {
            if (navigator.appName == "Microsoft Internet Explorer" && event.button == 2) {
                if (confirm(RIGHT_CLICK_MSG)) {
                    window.location = "http://tinyurl.com/ilikeplaces-sources"
                }
            }
            return false
        }
        return true
    }
}
document.onmousedown = click;
function trimInput(c, d) {
    return c.length > d ? c.toString().slice(0, d) : c
}
function ilpAlert(d, e, f) {
    $.prompt(f.photoDescriptionAlert)
}
function getExt(b) {
    return(/[.]/.exec(b)) ? /[^.]+$/.exec(b.toLowerCase()) : ""
}
$(document).ready(function () {
    ilp_progress(60, "Loading tooltips and help...");
    vtip();
    ilp_progress(65, "Focusing " + getLocationName() + " search ...");
    $("#q").liveUpdate("#place_list").focus();
    ilp_progress(80, "Loading social networks and comments..");
    disqus_identifier = "WOEID=" + getLocationId();
    runontime2 = function () {
        var b = document.createElement("script");
        b.type = "text/javascript";
        b.async = true;
        b.src = "http://ilikeplaces.disqus.com/embed.js";
        (document.getElementsByTagName("head")[0] || document.getElementsByTagName("body")[0]).appendChild(b)
    };
    setTimeout("runontime2();", 10000);
    ilp_progress(85, "Checking Where On Earth ID...");
    $.getJSON("http://where.yahooapis.com/v1/place/" + getLocationId() + "?format=json&appid=wr4tLgnV34GR76Hsrb4iSmeK7Ww754TDrp6cHp8E.J0onXtJDo8U_7AO6I5_gWbVnS1upw1GRI4-&callback=?", function (f) {
        var e = f;
        try {
            ilp_progress(90, "Checking photos for " + getLocationName() + " ..");
            $.getJSON("http://www.panoramio.com/map/get_panoramas.php?order=popularity&set=public&from=0&to=20&minx=" + (e.place.boundingBox.southWest.longitude) + "&miny=" + (e.place.boundingBox.southWest.latitude) + "&maxx=" + (e.place.boundingBox.northEast.longitude) + "&maxy=" + (e.place.boundingBox.northEast.latitude) + "&size=original&callback=?", function (b) {
                if (b.count > 0) {
                    runontime = function () {
                        $("body").css("background-image", "url(" + b.photos[0].photo_file_url + ")");
                        $("#Main_location_photo").attr("src", b.photos[0].photo_file_url);
                        $("#Main_othersidebar_identity").text(b.photos[0].photo_title)
                    };
                    setTimeout("runontime();", 10000);
                    $("body").css("background-attachment", "fixed");
                    $("body").css("background-repeat", "repeat");
                    $("body").css("background-size", "100%");
                    $("body").css("background-position", "center center");
                    var a = false;
                    var c = 0;
                    $(document).keyup(function (i) {
                        var j = i.keyCode ? i.keyCode : i.which ? i.which : i.charCode;
                        if (j == 17) {
                            a = false
                        }
                    }).keydown(function (k) {
                        var l = k.keyCode ? k.keyCode : k.which ? k.which : k.charCode;
                        if (l == 17) {
                            a = true
                        }
                        if (l == 221 && a == true) {
                            var j = c > b.count - 1 ? b.count - 1 : ++c;
                            $("body").css("background", "url(" + b.photos[j].photo_file_url + ")");
                            $("#Main_location_photo").attr("src", b.photos[j].photo_file_url);
                            $("#Main_othersidebar_identity").text(b.photos[j].photo_title);
                            return false
                        }
                    });
                    $(document).keyup(function (i) {
                        var j = i.keyCode ? i.keyCode : i.which ? i.which : i.charCode;
                        if (j == 17) {
                            a = false
                        }
                    }).keydown(function (k) {
                        var l = k.keyCode ? k.keyCode : k.which ? k.which : k.charCode;
                        if (l == 17) {
                            a = true
                        }
                        if (l == 219 && a == true) {
                            var j = c < 1 ? 0 : --c;
                            $("body").css("background", "url(" + b.photos[j].photo_file_url + ")");
                            $("#Main_location_photo").attr("src", b.photos[j].photo_file_url);
                            $("#Main_othersidebar_identity").text(b.photos[j].photo_title);
                            return false
                        }
                    });
                    $(document).keyup(function (i) {
                        var j = i.keyCode ? i.keyCode : i.which ? i.which : i.charCode;
                        if (j == 17) {
                            a = false
                        }
                    }).keydown(function (i) {
                        var j = i.keyCode ? i.keyCode : i.which ? i.which : i.charCode;
                        if (j == 17) {
                            a = true
                        }
                        if (j == 186 && a == true) {
                            window.open(b.photos[c].photo_url, "Photo", "width=960,height=600");
                            return false
                        }
                    });
                    $(document).keyup(function (j) {
                        var i = j.keyCode ? j.keyCode : j.which ? j.which : j.charCode;
                        if (i == 17) {
                            a = false
                        }
                    }).keydown(function (i) {
                        var j = i.keyCode ? i.keyCode : i.which ? i.which : i.charCode;
                        if (j == 17) {
                            a = true
                        }
                        if (j == 222 && a == true) {
                            window.open(b.photos[c].owner_url, "Owner", "width=960,height=600");
                            return false
                        }
                    });
                    $(document).keyup(function (j) {
                        var i = j.keyCode ? j.keyCode : j.which ? j.which : j.charCode;
                        if (i == 17) {
                            a = false
                        }
                    }).keydown(function (j) {
                        var i = j.keyCode ? j.keyCode : j.which ? j.which : j.charCode;
                        if (i == 17) {
                            a = true
                        }
                        if (i == 220 && a == true) {
                            $("#Main_display").hide();
                            setTimeout("$('#Main_display').show()", 7000);
                            return false
                        }
                    })
                }
            });
            ilp_load_map = function () {
            };
            ilp_load_map = function () {
                $("#ilp_progress_block").hide();
                $("#ilp_main_content").show();
                $("#map_static").attr("src", "http://maps.googleapis.com/maps/api/staticmap?center=" + f.place.centroid.latitude + "," + f.place.centroid.longitude + "&zoom=" + ygeoarea(f.place["placeTypeName attrs"].code) + "&size=600x600&sensor=false")
            }
        } catch (d) {
            $("#map").hide();
            alert(d)
        }
    });
    ilp_progress(100, "Painting Backdrops ..")
});
