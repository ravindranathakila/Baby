function trimInput(value,trimLength){
    return value.length > trimLength ? value.toString().slice(0,trimLength) : value;
}

function ilpAlert(v,m,f){
    $.prompt(f.photoDescriptionAlert);
}

var dataReady = false;

function checkIfDtaReady(){
    if(!dataReady){
        var ingnoredVal = window.setTimeout("checkIfDtaReady()",1000);
        return false;
    } else {
        return true;
    }
}

function getExt(file){
    return (/[.]/.exec(file)) ? /[^.]+$/.exec(file.toLowerCase()) : '';
}


$(document).ready(function(){
    vtip();
});