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
$(document).ready(function () {
    vtip()
});
