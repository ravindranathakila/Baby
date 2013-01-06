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
};
