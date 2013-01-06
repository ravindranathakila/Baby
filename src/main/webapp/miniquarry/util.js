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
};
