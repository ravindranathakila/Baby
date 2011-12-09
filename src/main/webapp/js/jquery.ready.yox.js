RIGHT_CLICKS=0;
RIGHT_CLICK_MSG="Geee! We are open-source. Shall I give u the sources?;)\n"+
"(Ur ryt-click menu will work, just ryt-click again ;)";
function click(e){
    if(RIGHT_CLICKS==0){
        RIGHT_CLICKS++;
        if(navigator.appName=='Netscape'
            &&e.which==3){
            if(confirm(RIGHT_CLICK_MSG)){
                window.location="http://tinyurl.com/ilikeplaces-sources";
            }
            return false;
        }
        else{
            if(navigator.appName=='Microsoft Internet Explorer'
                &&event.button==2)
                if(confirm(RIGHT_CLICK_MSG)){
                    window.location="http://tinyurl.com/ilikeplaces-sources";
                }
            return false;
        }
        return true;
    }
}
document.onmousedown=click;
function trimInput(value,trimLength){
    return value.length>trimLength?value.toString().slice(0,trimLength):value;
}
function ilpAlert(v,m,f){
    $.prompt(f.photoDescriptionAlert);
}
function getExt(file){
    return(/[.]/.exec(file))?/[^.]+$/.exec(file.toLowerCase()):'';
}
$(document).ready(function(){
    ilp_progress(60,'Loading tooltips and help...');
    vtip();

    ilp_progress(65,'Focusing '+getLocationName()+' search ...');
    $('#q').liveUpdate('#place_list').focus();

    ilp_progress(80,'Loading social networks and comments..');

    disqus_identifier="WOEID="+getLocationId();

    runontime2 = function(){
        var dsq=document.createElement('script');
        dsq.type='text/javascript';
        dsq.async=true;
        dsq.src='http://ilikeplaces.disqus.com/embed.js';
        (document.getElementsByTagName('head')[0]||document.getElementsByTagName('body')[0]).appendChild(dsq);
    }
    setTimeout("runontime2();", 10000);

    ilp_progress(85,'Checking Where On Earth ID...');
    $.getJSON(
        "http://where.yahooapis.com/v1/place/"+getLocationId()+"?format=json&appid=wr4tLgnV34GR76Hsrb4iSmeK7Ww754TDrp6cHp8E.J0onXtJDo8U_7AO6I5_gWbVnS1upw1GRI4-&callback=?",
        function(y){
            var ylocdata=y;
            try{
				ilp_progress(90,'Checking photos for '+getLocationName()+' ..');//Have to do this due to a bug in gmap

                $.getJSON(
                    "http://www.panoramio.com/map/get_panoramas.php?order=popularity&set=public&from=0&to=20"+
                    "&minx="+(ylocdata.place.boundingBox.southWest.longitude)+
                    "&miny="+(ylocdata.place.boundingBox.southWest.latitude)+
                    "&maxx="+(ylocdata.place.boundingBox.northEast.longitude)+
                    "&maxy="+(ylocdata.place.boundingBox.northEast.latitude)+
                    "&size=original&callback=?",
                    function(data){
                        if(data.count>0){
                            runontime = function(){
                                $('body').css('background-image','url('+data.photos[0].photo_file_url+')');
                                $('#Main_location_photo').attr("src",data.photos[0].photo_file_url);
                                $('#Main_othersidebar_identity').text(data.photos[0].photo_title);
                            }
                            setTimeout("runontime();", 10000);
                            $('body').css('background-attachment','fixed');
                            $('body').css('background-repeat','repeat');
                            $('body').css('background-size','100%');
                            $('body').css('background-position','center center');
                            var isCtrl=false;
                            var i=0;
                            $(document).keyup(function(event){
                                var ckup=event.keyCode?event.keyCode:event.which?event.which:event.charCode;
                                if(ckup==17)isCtrl=false;
                            }).keydown(function(event){
                                var ckdp=event.keyCode?event.keyCode:event.which?event.which:event.charCode;
                                if(ckdp==17)isCtrl=true;
                                if(ckdp==221&&isCtrl==true){
                                    var indexi=i>data.count-1?data.count-1:++i;
                                    $('body').css('background','url('+data.photos[indexi].photo_file_url+')');
                                    $('#Main_location_photo').attr("src",data.photos[indexi].photo_file_url);
                                    $('#Main_othersidebar_identity').text(data.photos[indexi].photo_title);
                                    return false;
                                }
                            });
                            $(document).keyup(function(event){
                                var ckun=event.keyCode?event.keyCode:event.which?event.which:event.charCode;
                                if(ckun==17)isCtrl=false;
                            }).keydown(function(event){
                                var ckdn=event.keyCode?event.keyCode:event.which?event.which:event.charCode;
                                if(ckdn==17)isCtrl=true;
                                if(ckdn==219&&isCtrl==true){
                                    var indexj=i<1?0:--i;
                                    $('body').css('background','url('+data.photos[indexj].photo_file_url+')');
                                    $('#Main_location_photo').attr("src",data.photos[indexj].photo_file_url);
                                    $('#Main_othersidebar_identity').text(data.photos[indexj].photo_title);
                                    return false;
                                }
                            });
                            $(document).keyup(function(event){
                                var ckuphoto=event.keyCode?event.keyCode:event.which?event.which:event.charCode;
                                if(ckuphoto==17)isCtrl=false;
                            }).keydown(function(event){
                                var ckdphoto=event.keyCode?event.keyCode:event.which?event.which:event.charCode;
                                if(ckdphoto==17)isCtrl=true;
                                if(ckdphoto==186&&isCtrl==true){
                                    window.open(data.photos[i].photo_url,'Photo','width=960,height=600');
                                    return false;
                                }
                            });
                            $(document).keyup(function(event){
                                var ckuowner=event.keyCode?event.keyCode:event.which?event.which:event.charCode;
                                if(ckuowner==17)isCtrl=false;
                            }).keydown(function(event){
                                var ckdowner=event.keyCode?event.keyCode:event.which?event.which:event.charCode;
                                if(ckdowner==17)isCtrl=true;
                                if(ckdowner==222&&isCtrl==true){
                                    window.open(data.photos[i].owner_url,'Owner','width=960,height=600');
                                    return false;
                                }
                            });
                            $(document).keyup(function(event){
                                var ckuh=event.keyCode?event.keyCode:event.which?event.which:event.charCode;
                                if(ckuh==17)isCtrl=false;
                            }).keydown(function(event){
                                var ckdh=event.keyCode?event.keyCode:event.which?event.which:event.charCode;
                                if(ckdh==17)isCtrl=true;
                                if(ckdh==220&&isCtrl==true){
                                    $('#Main_display').hide();
                                    setTimeout("$('#Main_display').show()",7000);
                                    return false;
                                }
                            });
                        }
                    });


				//ilp_progress(100,'Almost at '+getLocationName()+' ..:)');//Have to do this due to a bug in gmap
				//setTimeout("ilp_load_map()",30000);
				ilp_load_map = function(){};//The definition of the following function will take time to initialize
                ilp_load_map = function(){
					$("#ilp_progress_block").hide();
					$("#ilp_main_content").show();

                    $("#map_static").attr('src',
                    'http://maps.googleapis.com/maps/api/staticmap?center='
                    +y.place.centroid.latitude + ','+y.place.centroid.longitude
                    +'&zoom='
                    +ygeoarea(y.place['placeTypeName attrs'].code)
                    +'&size=600x600&sensor=false');

//					$("#map").gMap(
//					{
//						zoom:ygeoarea(y.place['placeTypeName attrs'].code),
//						latitude:y.place.centroid.latitude,
//						longitude:y.place.centroid.longitude,
//						address:getLocationName(),
//						markers:[{
//							latitude:y.place.centroid.latitude,
//							longitude:y.place.centroid.longitude,
//							html:"<div class='center'><pre class='center' style='font-size:12px;color:purple;'>"
//							+getLocationName()
//							+"<br/><a href='http://travel.ilikeplaces.com?cid=317285' target='_blank'  style='font-weight:strong;'>Best Travel Deals</a>"
//							+"<br/>Call with Discount Code:317285"
//							+"<br/>US and Canada: 1-800-780-5733"
//							+"<br/>Europe: 00-800-11-20-11-40"
//							+"</pre></div>",
//							popup:true
//						}]
//					}
//					);
				};
            }catch(err){
                $("#map").hide();
                alert(err);
            }
        }
        );

	ilp_progress(100,'Painting Backdrops ..');//Nifty will work at this moment and  also in case errors occurred

});
