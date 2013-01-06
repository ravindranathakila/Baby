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
$(document).ready(function () {
    vtip()
});
