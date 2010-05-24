                function trimInput(value,trimLength){
                    return value.length > trimLength ? value.toString().slice(0,trimLength) : value;
                }

                function ilpAlert(v,m,f){
                    $.prompt(f.photoDescriptionAlert);
                }

                function getExt(file){
                    return (/[.]/.exec(file)) ? /[^.]+$/.exec(file.toLowerCase()) : '';
                }


                $(document).ready(function(){

                document.getElementById('panoramio_frame').src = 'http://www.panoramio.com/wapi/template/photo_list.html?position=left&tag='+getLocationName().split(' of ')[0].replace('/',' ')+'&amp;width=190&amp;height=190&amp;bgcolor=%23ffffff';

                $('#q').liveUpdate('#place_list').focus();

                $.getJSON(
                	"http://where.yahooapis.com/v1/place/"+getLocationId()+"?format=json&appid=wr4tLgnV34GR76Hsrb4iSmeK7Ww754TDrp6cHp8E.J0onXtJDo8U_7AO6I5_gWbVnS1upw1GRI4-&callback=?",
                	function(y){
                		var ylocdata=y;
                		try{
                		$("#map").gMap(
                			{
                			zoom:ygeoarea(y.place['placeTypeName attrs'].code),
                			latitude: y.place.centroid.latitude,
                			longitude: y.place.centroid.longitude,
                			markers: [{
                				latitude: y.place.centroid.latitude,
                				longitude: y.place.centroid.longitude,
                				html: "<div class='center'><sub>"
                                                                +getLocationName()
                                                                +"<br/>"
                                                                +"&nbsp;&nbsp;<a href='http://travel.ilikeplaces.com/hotels/index.jsp?cid=317285&specials=true' target='_blank'>Book a Hotel</a>"
                                                                +"&nbsp;&nbsp;<a href='http://travel.ilikeplaces.com/cars/index.jsp?cid=317285' target='_blank'>Hire a Car</a>"
                                                                +"<br/><a href='http://www.tkqlhce.com/click-3813950-10486476' target='_blank'>Lonely Planet Guidebooks</a><img src='http://www.ftjcfx.com/image-3813950-10486476' width='1' height='1' border='0'/>"
                                                                +"&nbsp;&nbsp;<a href='http://dg.ilikeplaces.com/index.jsp?cid=317285' target='_blank'>Guides to Plan Your Trip</a>"
                                                                +"<br/><br/>Call Us & Get a QUOTE:"
                                                                +"<br/>US and Canada: 1-800-780-5733"
                                                                +"&nbsp;&nbsp;Europe: 00-800-11-20-11-40"
                                                                +"<br/>Remember Your DISCOUNT CODE:317285"
                				                        		+"<br/>Lowest Price Guaranteed! <a href='http://travel.ilikeplaces.com/index.jsp?pageName=guarantee' target='_blank'>details</a> | <a href='http://travel.ilikeplaces.com/index.jsp?pageName=guarForm&cid=317285&guarantee=Price+Guarantee+form' target='_blank'>report</a>"


                                                                + "</sub></div>",
                				popup:true
                			}]
                			}
                		);

                $.getJSON(
                	//"http://www.panoramio.com/map/get_panoramas.php?order=popularity&set=full&from=0&to=1&minx=-180&miny=-90&maxx=180&maxy=90&size=original&callback=?",
                		"http://www.panoramio.com/map/get_panoramas.php?order=popularity&set=public&from=0&to=20&minx="+
                		//(ylocdata.place.centroid.longitude-1)+"&miny="+
                		//(ylocdata.place.centroid.latitude-1)+"&maxx="+
                		//(ylocdata.place.centroid.longitude+1)+"&maxy=4"+
                		//(ylocdata.place.centroid.latitude+1)+"&size=original&callback=?",
                		(ylocdata.place.boundingBox.southWest.longitude)+"&miny="+
                		(ylocdata.place.boundingBox.southWest.latitude)+"&maxx="+
                		(ylocdata.place.boundingBox.northEast.longitude)+"&maxy="+
                		(ylocdata.place.boundingBox.northEast.latitude)+"&size=original&callback=?",

                function(data){
                //alert(data.count);
                //alert(data.photos[0].photo_file_url);
                $('body').css('background-image','url('+data.photos[0].photo_file_url+')');
                $('body').attr('title','Photo by '+data.photos[0].owner_name);
                $('body').css('background-attachment','fixed');
                $('body').css('background-repeat','none');
                $('body').css('background-size','100%');
                $('body').css('background-position','center center');


                //alert(data.photos[1].photo_file_url);
                });

                		}catch(err){
                	    $("#map").hide();
                		alert(err);
                		}
                	}
                );

                //alert(ylocdata.place.centroid.longitude);
                //alert(ylocdata.place.centroid.latitude);

                $("#Main_display")
                .mouseover(function() {
                //$('#Main_display').css('opacity','0.9');
                //$('#Main_display').css('filter','alpha(opacity=90)');
                //$('#Main_display').fadeIn('fast');
                })
                .mouseout(function(){
                //$('#Main_display').css('opacity','0.0');
                //$('#Main_display').css('filter','alpha(opacity=0)');
                //$('#Main_display').fadeOut('fast');
                });

                vtip();

                    $('.yoxview').yoxview();

                    ////////////////////////////////////////////////////////////////
                    var button = $('#button1'), interval;
                    buttonText = 'uploading';


                    var fub = new AjaxUpload(button,{
                        action: '/fileuploads',
                        name: 'myfile',
                        onChange : function(file, ext){

                            var hostInput = new Object(fub._input);
                            //alert(hostInput);
                            thisVal = this;

                            var locationId = getLocationId();


                            var isPublic;
                            var isPrivate;
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
                                        isPrivate = !v;
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
                                            'isPrivate':isPrivate,
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
