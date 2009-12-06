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

//function goMummy(self, file, filext){
//    var button = $('#button1'), interval;
//    buttonText = 'uploading';
//
//    var isPublic;// = confirm("Is this a public photo?");
//    var photoName;
//    var photoDescription;
//
//
//    /*Name and Id of Input Fields Should Be Made Equal*/
//    $.prompt( {
//        state0: {
//            html:'Which type is this photo?<br/> '+
//            'Public photos will be shared with everybody.<br/> '+
//            'Private photos will be shared only with people you have added.',
//            buttons:{
//                Public:true,
//                Private:false
//            },
//            submit:function(v,m,f){
//                isPublic = v;
//                $.prompt.goToState('state1');
//                return false;
//            }
//        },
//        state1: {
//            html:'Give a simple but descriptive name for your photo:<br /> ' +
//            '<input type=\"text\" id=\"photoNameInput\" name=\"photoNameInput\" value=\"\" />',
//            buttons: {
//                Back:-1,
//                Next: 1
//            },
//            focus: 1,
//            submit:function(v,m,f){
//                if(v == 1){
//                    an = m.children('#photoNameInput');
//                    if(!f.photoNameInput){
//                        an.css("border","solid #ff0000 1px");
//                        $.prompt.goToState('state1');
//                        return false;
//                    } else {
//                        photoName = f.photoNameInput;
//                        $.prompt.goToState('state2');
//                        return false;
//                    }
//                } else {
//                    $.prompt.goToState('state0');
//                    return false;
//                }
//            }
//        },
//        state2: {
//            html:"What is this photo all about?!"+
//            "<br />"+
//            "<textarea "+
//            "id='photoDescriptionAlert' "+
//            "name='photoDescriptionAlert' "+
//            "style='width:360px;height:370px;' "+
//            "class='dark_orange_fg' "+
//            "onkeyup='javascript:this.value = trimInput(this.value,900);'/>"+
//            "<br />",
//            buttons: {
//                Back:-1,
//                Next: 1
//            },
//            focus: 2,
//            submit:function(v,m,f){
//                if(v == 1){
//                    an = m.children('#photoDescriptionAlert');
//                    if(!f.photoDescriptionAlert){
//                        an.css("border","solid #ff0000 1px");
//                        $.prompt.goToState('state2');
//                        return false;
//                    } else {
//                        photoDescription = f.photoDescriptionAlert;
//                        /*this.setData({
//                        //'locationId':locationId,
//                        'isPublic':isPublic,
//                        'photoDescription':photoDescription,
//                        'photoName':photoName
//                    });*/
//                        $.prompt('Complete!<br/>'+
//                            'The photo is ' + (isPublic? 'public.':'private.') +'<br/>'+
//                            'The photo name is ' + photoName +'.<br/>'+
//                            'The photo description: ' + photoDescription);
//                        return true;
//                    }
//                }else{
//                    $.prompt.goToState('state1');
//                    return false;
//                }
//            }
//        }
//
//    },
//    {
//        callback:function(v,m,f){
//            //file, filext
//            new AjaxUpload(button,{
//                action: '/ilikeplaces/fileuploads',
//                name: 'myfile',
//                onChange : function(file, ext){
//
//                    //var locationId = getLocationId();
//
//                    var isPublic;// = confirm("Is this a public photo?");
//                    var photoName;
//                    var photoDescription;
//                },
//
//                onSubmit: function(file, ext){
//                    // change button text, when user selects file
//                    button.text(buttonText);
//
//                    // If you want to allow uploading only 1 file at time,
//                    // you can disable upload button
//                    this.disable();
//
//                    interval = window.setInterval(function(){
//                        var text = button.text();
//                        if (text.length < buttonText.length+10){
//                            button.text('^'+text + '^');
//                        } else {
//                            button.text(buttonText);
//                        }
//                    }, 200);
//
//                },
//
//                onComplete: function(file, response){
//                    window.clearInterval(interval);
//                    //button.text('Done! Click to Upload More');
//
//                    if(String(response).split("|")[0] == "ok"){
//                        alert("No Error!")
//                    } else {
//                        alert("An Error Occurred");
//                        alert(response);
//                    }
//
//
//                    // enable upload button
//                    this.enable();
//
//                    // add file to the list
//                    $('<li></li>').appendTo('#Main_file_list .files').text(file);
//                }
//
//
//            });
//
//
//            // get filename from input
//            var file = fileFromPath(this.value);
//            if(goMummy(self, file, getExt(file)) == false){//self._settings.onChange.call(self, file, getExt(file)) == false ){
//                return;
//            }
//            // Submit form when value is changed
//            if (self._settings.autoSubmit){
//                self.submit();
//            }
//
//
//        }
//    }
//    );
//}





$(document).ready(function(){


    $('a[rel=shareit], #shareit-box').mouseenter(shareEnter);

    //onmouse out hide the shareit box
    $('#shareit-box').mouseleave(shareLeave);

    //hightlight the textfield on click event
    $('#shareit-field').click(shareClick);
    ////////////////////////////////////////////////////////////////
    var button = $('#button1'), interval;
    buttonText = 'uploading';

    
    var fub = new AjaxUpload(button,{
        action: '/ilikeplaces/fileuploads',
        name: 'myfile',
        onChange : function(file, ext){

            var hostInput = new Object(fub._input);
            //alert(hostInput);
            thisVal = this;

            var locationId = getLocationId();

            
            var isPublic;
            var photoName;
            var photoDescription;
/*
            var cln = new Object(fub);
            //var inputVal = {input:cln._input,_settings:cln._settings};
            var inputVal = {
                input:cln._input,
                _settings:cln._settings
            };
            alert(inputVal.input.value);
            alert(inputVal._settings);*/

            $.prompt( {
                state0: {
                    html:'Which type is this photo?<br/> '+
                    'Public photos will be shared with everybody.<br/> '+
                    'Private photos will be shared only with people you have added.',
                    buttons:{
                        Public:true,
                        Private:false
                    },
                    submit:function(v,m,f){
                        isPublic = v;
                        $.prompt.goToState('state1');
                        return false;
                    }
                },
                state1: {
                    html:'Give a simple but descriptive name for your photo:<br /> ' +
                    '<input type=\"text\" id=\"photoNameInput\" name=\"photoNameInput\" value=\"\" />',
                    buttons: {
                        Back:-1,
                        Next: 1
                    },
                    focus: 1,
                    submit:function(v,m,f){
                        if(v == 1){
                            an = m.children('#photoNameInput');
                            if(!f.photoNameInput){
                                an.css("border","solid #ff0000 1px");
                                $.prompt.goToState('state1');
                                return false;
                            } else {
                                photoName = f.photoNameInput;
                                $.prompt.goToState('state2');
                                return false;
                            }
                        } else {
                            $.prompt.goToState('state0');
                            return false;
                        }
                    }
                },
                state2: {
                    html:"What is this photo all about?!"+
                    "<br />"+
                    "<textarea "+
                    "id='photoDescriptionAlert' "+
                    "name='photoDescriptionAlert' "+
                    "style='width:360px;height:370px;' "+
                    "class='dark_orange_fg' "+
                    "onkeyup='javascript:this.value = trimInput(this.value,900);'/>"+
                    "<br />",
                    buttons: {
                        Back:-1,
                        Next: 1
                    },
                    focus: 2,
                    submit:function(v,m,f){
                        if(v == 1){
                            an = m.children('#photoDescriptionAlert');
                            if(!f.photoDescriptionAlert){
                                an.css("border","solid #ff0000 1px");
                                $.prompt.goToState('state2');
                                return false;
                            } else {
                                photoDescription = f.photoDescriptionAlert;
                                
                                $.prompt('Complete!<br/>'+
                                    'The photo is ' + (isPublic? 'public.':'private.') +'<br/>'+
                                    'The photo name is ' + photoName +'.<br/>'+
                                    'The photo description: ' + photoDescription);
                                return true;
                            }
                        }else{
                            $.prompt.goToState('state1');
                            return false;
                        }
                    }
                }

            },
            {
                callback:function(v,m,f){

                    //fub._input = hostInput;

                    fub.onChange = function(file, extension){
                        fub.setData({
                            'locationId':locationId,
                            'isPublic':isPublic,
                            'photoDescription':photoDescription,
                            'photoName':photoName
                        });
                        return true;
                    }

                    //                    fub.onSubmit = function(file, ext){
                    //                        alert("onsubmit done");
                    //                        // change button text, when user selects file
                    //                        button.text(buttonText);
                    //
                    //                        // If you want to allow uploading only 1 file at time,
                    //                        // you can disable upload button
                    //                        fub.disable();
                    //
                    //                        interval = window.setInterval(function(){
                    //                            var text = button.text();
                    //                            if (text.length < buttonText.length+10){
                    //                                button.text('^'+text + '^');
                    //                            } else {
                    //                                button.text(buttonText);
                    //                            }
                    //                        }, 200);
                    //
                    //                    }
                    //                    fub.onComplete = function(file, response){
                    //                        window.clearInterval(interval);
                    //                        //button.text('Done! Click to Upload More');
                    //
                    //                        if(String(response).split("|")[0] == "ok"){
                    //                            alert("No Error!")
                    //                        } else {
                    //                            alert("An Error Occurred");
                    //                            alert(response);
                    //                        }
                    //
                    //
                    //                        // enable upload button
                    //                        fub.enable();
                    //
                    //                        // add file to the list
                    //                        $('<li></li>').appendTo('#Main_file_list .files').text(file);
                    //                    }

                    fub.onChange.call(thisVal, file, getExt(file));
                    fub.submit();
                //return true;

                /*cln._settings = inputVal['_settings'];
                    cln._input = inputVal.input;
                    //alert(cln._input);
                    cln.action =  '/ilikeplaces/fileuploads';
                    //cln.name = 'myfile';
                    cln._settings.data = {
                        //'locationId':locationId,
                        'isPublic':isPublic,
                        'photoDescription':photoDescription,
                        'photoName':photoName
                    };
                    
                    //fub.submit.call(thisArg, arg1, arg2);
                    //dataReady = true;
                    cln.onSubmit= function(file, ext){
                        // change button text, when user selects file
                        button.text(buttonText);

                        // If you want to allow uploading only 1 file at time,
                        // you can disable upload button
                        this.disable();

                        interval = window.setInterval(function(){
                            var text = button.text();
                            if (text.length < buttonText.length+10){
                                button.text('^'+text + '^');
                            } else {
                                button.text(buttonText);
                            }
                        }, 200);

                    }
                    cln.onComplete = function(file, response){
                        window.clearInterval(interval);
                        //button.text('Done! Click to Upload More');

                        if(String(response).split("|")[0] == "ok"){
                            alert("No Error!")
                        } else {
                            alert("An Error Occurred");
                            alert(response);
                        }


                        // enable upload button
                        this.enable();

                        // add file to the list
                        $('<li></li>').appendTo('#Main_file_list .files').text(file);
                    }*/
                                        
                }
            }
            );//TAG PROMPT
            return false;
        },
        onSubmit: function(file, ext){
            // change button text, when user selects file
            button.text(buttonText);

            // If you want to allow uploading only 1 file at time,
            // you can disable upload button
            fub.disable();

            interval = window.setInterval(function(){
                var text = button.text();
                if (text.length < buttonText.length+10){
                    button.text('^'+text + '^');
                } else {
                    button.text(buttonText);
                }
            }, 200);

        },
        onComplete : function(file, response){
            window.clearInterval(interval);
            //button.text('Done! Click to Upload More');

            if(String(response).split("|")[0] == "ok"){
                alert("No Error!")
            } else {
                alert("An Error Occurred");
                alert(response);
            }


            // enable upload button
            fub.enable();

            // add file to the list
            $('<li></li>').appendTo('#Main_file_list .files').text(file);
        }

    //TAG ONCHANGE
    }); //TAG AJAXUPLOAD

});
function shareEnter(){

    //get the height, top and calculate the left value for the sharebox
    var height = $(this).height();
    var top = $(this).offset().top;

    //get the left and find the center value
    var left = $(this).offset().left + ($(this).width() /2) - ($('#shareit-box').width() / 2);

    //grab the href value and explode the bar symbol to grab the url and title
    //the content should be in this format url|title
    var value = $(this).attr('href').split('|');

    //assign the value to variables and encode it to url friendly
    var field = value[0];
    var url = encodeURIComponent(value[0]);
    var title = encodeURIComponent(value[1]);

    //assign the height for the header, so that the link is cover
    $('#shareit-header').height(height);

    //display the box
    $('#shareit-box').show();

    //set the position, the box should appear under the link and centered
    $('#shareit-box').css({
        'top':top,
        'left':left
    });

    //assign the url to the textfield
    $('#shareit-field').val(field);

    //make the bookmark media open in new tab/window
    $('a.shareit-sm').attr('target','_blank');

    //Setup the bookmark media url and title
    $('a[rel=shareit-mail]').attr('href', 'http://mailto:?subject=' + title + '&body=' + url);
    $('a[rel=shareit-delicious]').attr('href', 'http://del.icio.us/post?v=4&noui&jump=close&url=' + url + '&title=' + title);
    $('a[rel=shareit-digg]').attr('href', 'http://digg.com/submit?phase=2&url=' + url + '&title=' + title);
    $('a[rel=shareit-stumbleupon]').attr('href', 'http://www.stumbleupon.com/submit?url=' + url + '&title=' + title);
    $('a[rel=shareit-twitter]').attr('href', 'http://twitter.com/home?status=' + title + '%20-%20' + title);  
    $('a[rel=shareit-facebook]').attr('href', 'http://www.facebook.com/share.php?u=' + url + '&t=' + title);
}

function shareLeave(){
    $('#shareit-field').val('');
    $(this).hide();
}
function shareClick() {
    $(this).select();
}