bef = function () {
    $.blockUI()
};
aft = function () {
    $.unblockUI()
};
function EventMonitor() {
    this.before = d;
    this.after = c;
    this.monitor = document.getElementById("monitorId");
    this.count = 0;
    post_fun_arg = {msg: "Your Request Was Completed Successfully!"};
    post_fun = function (a) {
    };
    function d(a) {
        if (this.count == 0) {
            $("#monitorId").show();
            alert(a.pageX + "," + a.pageY)
        }
        this.count++
    }

    function c(a) {
        if (this.count == 0) {
            return
        }
        this.count--;
        if (this.count == 0) {
            $("#monitorId").hide()
        }
        post_fun(post_fun_arg)
    }

    post_fun_arg = {};
    post_fun = function (a) {
    }
};
