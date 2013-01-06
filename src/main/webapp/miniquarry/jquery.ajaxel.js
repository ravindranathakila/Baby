(function ($) {
    $.fn.extend({ajaxel: function (options) {
        var defaults = {classname: "ajaxel", callback: "ajaxel_popup", type: "text"};
        var optionsINNER = $.extend(defaults, options);
        return this.each(function () {
            $("a." + optionsINNER.classname).each(function () {
                var a_href = $(this).attr("href");
                $(this).attr("href", "#");
                $(this).click(function (e) {
                    $.get(a_href, {ajax: "true"}, function (data) {
                        eval(optionsINNER.callback)(data)
                    }, optionsINNER.type)
                })
            });
            $("form." + optionsINNER.classname).each(function () {
                var form_id = $(this).attr("id");
                var form_action = $(this).attr("action");
                var form_target = $(this).attr("target");
                if (form_target == "") {
                    $("#" + form_id).submit(function () {
                        return false
                    });
                    $("#" + form_id + " input:submit").click(function (e) {
                        ajaxel_send_form(form_id, form_action)
                    })
                } else {
                    $("#" + form_id + " input:submit").after("<iframe width=0 height=0 frameborder=0 name=" + form_target + "></iframe>");
                    $("html").mousemove(function (e) {
                        var html_iframe = $("iframe[name=" + form_target + "]").contents().find("body").html();
                        $("iframe[name=" + form_target + "]").contents().find("body").html("");
                        if (html_iframe != "") {
                            if (optionsINNER.type == "json") {
                                var html_iframe = (eval("(" + html_iframe + ")"))
                            }
                            eval(optionsINNER.callback)(html_iframe);
                            $("iframe[name=" + form_target + "]").contents().find("body").html("")
                        }
                    })
                }
                function ajaxel_send_form(el, page) {
                    var fields = $("#" + el).serializeArray();
                    $.post(page, fields, function (data) {
                        eval(optionsINNER.callback)(data)
                    }, optionsINNER.type)
                }
            });
            function ajaxel_popup(data) {
                confirm("click?");
                alert(data)
            }
        })
    }})
})(jQuery);
