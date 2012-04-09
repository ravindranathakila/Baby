// PROGRESSIVE-1

try{
    setTimeout("initialize_gmap_scripts_transparently()", 7000);
}catch(err){
    alert(err);
    if(confirm("You internet connection seems to be flaky. Reload web page?")){
        try{
            setTimeout("initialize_gmap_scripts_transparently()", 7000);
        }catch(err){
            alert("Sorry, Still could not load required stuff.");
        }
    }else{
        alert("Ok. Not reloading. However things might not work!");
    }
}

// IMPROMPTU


/*
 * jQuery Impromptu
 * By: Trent Richardson [http://trentrichardson.com]
 * Version 2.7
 * Last Modified: 6/7/2009
 *
 * Copyright 2009 Trent Richardson
 * Dual licensed under the MIT and GPL licenses.
 * http://trentrichardson.com/Impromptu/GPL-LICENSE.txt
 * http://trentrichardson.com/Impromptu/MIT-LICENSE.txt
 *
 */
(function($){
    $.prompt=function(message,options){
        options=$.extend({},$.prompt.defaults,options);
        $.prompt.currentPrefix=options.prefix;
        var ie6=($.browser.msie&&$.browser.version<7);
        var $body=$(document.body);
        var $window=$(window);
        var msgbox='<div class="'+options.prefix+'box" id="'+options.prefix+'box">';
        if(options.useiframe&&(($('object, applet').length>0)||ie6)){
            msgbox+='<iframe src="javascript:false;" style="display:block;position:absolute;z-index:-1;" class="'+options.prefix+'fade" id="'+options.prefix+'fade"></iframe>';
        }else{
            if(ie6){
                $('select').css('visibility','hidden');
            }
            msgbox+='<div class="'+options.prefix+'fade" id="'+options.prefix+'fade"></div>';
        }
        msgbox+='<div class="'+options.prefix+'" id="'+options.prefix+'"><div class="'+options.prefix+'container"><div class="';
        msgbox+=options.prefix+'close">X</div><div id="'+options.prefix+'states"></div>';
        msgbox+='</div></div></div>';
        var $jqib=$(msgbox).appendTo($body);
        var $jqi=$jqib.children('#'+options.prefix);
        var $jqif=$jqib.children('#'+options.prefix+'fade');
        if(message.constructor==String){
            message={
                state0:{
                    html:message,
                    buttons:options.buttons,
                    focus:options.focus,
                    submit:options.submit
                }
            };

        }
        var states="";
        $.each(message,function(statename,stateobj){
            stateobj=$.extend({},$.prompt.defaults.state,stateobj);
            message[statename]=stateobj;
            states+='<div id="'+options.prefix+'_state_'+statename+'" class="'+options.prefix+'_state" style="display:none;"><div class="'+options.prefix+'message">'+stateobj.html+'</div><div class="'+options.prefix+'buttons">';
            $.each(stateobj.buttons,function(k,v){
                states+='<button name="'+options.prefix+'_'+statename+'_button'+k+'" id="'+options.prefix+'_'+statename+'_button'+k+'" value="'+v+'">'+k+'</button>';
            });
            states+='</div></div>';
        });
        $jqi.find('#'+options.prefix+'states').html(states).children('.'+options.prefix+'_state:first').css('display','block');
        $jqi.find('.'+options.prefix+'buttons:empty').css('display','none');
        $.each(message,function(statename,stateobj){
            var $state=$jqi.find('#'+options.prefix+'_state_'+statename);
            $state.children('.'+options.prefix+'buttons').children('button').click(function(){
                var msg=$state.children('.'+options.prefix+'message');
                var clicked=stateobj.buttons[$(this).text()];
                var forminputs={};

                $.each($jqi.find('#'+options.prefix+'states :input').serializeArray(),function(i,obj){
                    if(forminputs[obj.name]===undefined){
                        forminputs[obj.name]=obj.value;
                    }else if(typeof forminputs[obj.name]==Array){
                        forminputs[obj.name].push(obj.value);
                    }else{
                        forminputs[obj.name]=[forminputs[obj.name],obj.value];
                    }
                });
                var close=stateobj.submit(clicked,msg,forminputs);
                if(close===undefined||close){
                    removePrompt(true,clicked,msg,forminputs);
                }
            });
            $state.find('.'+options.prefix+'buttons button:eq('+stateobj.focus+')').addClass(options.prefix+'defaultbutton');
        });
        var ie6scroll=function(){
            $jqib.css({
                top:$window.scrollTop()
            });
        };

        var fadeClicked=function(){
            if(options.persistent){
                var i=0;
                $jqib.addClass(options.prefix+'warning');
                var intervalid=setInterval(function(){
                    $jqib.toggleClass(options.prefix+'warning');
                    if(i++>1){
                        clearInterval(intervalid);
                        $jqib.removeClass(options.prefix+'warning');
                    }
                },100);
            }else{
                removePrompt();
            }
        };

        var keyPressEventHandler=function(e){
            var key=(window.event)?event.keyCode:e.keyCode;
            if(key==27){
                removePrompt();
            }
            if(key==9){
                var $inputels=$(':input:enabled:visible',$jqib);
                var fwd=!e.shiftKey&&e.target==$inputels[$inputels.length-1];
                var back=e.shiftKey&&e.target==$inputels[0];
                if(fwd||back){
                    setTimeout(function(){
                        if(!$inputels)return;
                        var el=$inputels[back===true?$inputels.length-1:0];
                        if(el)el.focus();
                    },10);
                    return false;
                }
            }
        };

        var positionPrompt=function(){
            $jqib.css({
                position:(ie6)?"absolute":"fixed",
                height:$window.height(),
                width:"100%",
                top:(ie6)?$window.scrollTop():0,
                left:0,
                right:0,
                bottom:0
            });
            $jqif.css({
                position:"absolute",
                height:$window.height(),
                width:"100%",
                top:0,
                left:0,
                right:0,
                bottom:0
            });
            $jqi.css({
                position:"absolute",
                top:options.top,
                left:"50%",
                marginLeft:(($jqi.outerWidth()/2)*-1)
            });
        };

        var stylePrompt=function(){
            $jqif.css({
                zIndex:options.zIndex,
                display:"none",
                opacity:options.opacity
            });
            $jqi.css({
                zIndex:options.zIndex+1,
                display:"none"
            });
            $jqib.css({
                zIndex:options.zIndex
            });
        };

        var removePrompt=function(callCallback,clicked,msg,formvals){
            $jqi.remove();
            if(ie6){
                $body.unbind('scroll',ie6scroll);
            }
            $window.unbind('resize',positionPrompt);
            $jqif.fadeOut(options.overlayspeed,function(){
                $jqif.unbind('click',fadeClicked);
                $jqif.remove();
                if(callCallback){
                    options.callback(clicked,msg,formvals);
                }
                $jqib.unbind('keypress',keyPressEventHandler);
                $jqib.remove();
                if(ie6&&!options.useiframe){
                    $('select').css('visibility','visible');
                }
            });
        };

        positionPrompt();
        stylePrompt();
        if(ie6){
            $window.scroll(ie6scroll);
        }
        $jqif.click(fadeClicked);
        $window.resize(positionPrompt);
        $jqib.bind("keydown keypress",keyPressEventHandler);
        $jqi.find('.'+options.prefix+'close').click(removePrompt);
        $jqif.fadeIn(options.overlayspeed);
        $jqi[options.show](options.promptspeed,options.loaded);
        //$jqi.find('#'+options.prefix+'states .'+options.prefix+'_state:first .'+options.prefix+'defaultbutton').focus();
        if(options.timeout>0)setTimeout($.prompt.close,options.timeout);
        return $jqi.find('#'+options.prefix+'states .'+options.prefix+'_state:first .'+options.prefix+'defaultbutton').focus();
    //return $jqib;
    };

    $.prompt.defaults={
        prefix:'jqi',
        buttons:{
            Ok:true
        },
        loaded:function(){},
        submit:function(){
            return true;
        },
        callback:function(){},
        opacity:0.6,
        zIndex:999,
        overlayspeed:'slow',
        promptspeed:'fast',
        show:'fadeIn',
        focus:0,
        useiframe:false,
        top:"15%",
        persistent:true,
        timeout:0,
        state:{
            html:'',
            buttons:{
                Ok:true
            },
            focus:0,
            submit:function(){
                return true;
            }
        }
    };

    $.prompt.currentPrefix=$.prompt.defaults.prefix;
    $.prompt.setDefaults=function(o){
        $.prompt.defaults=$.extend({},$.prompt.defaults,o);
    };

    $.prompt.setStateDefaults=function(o){
        $.prompt.defaults.state=$.extend({},$.prompt.defaults.state,o);
    };

    $.prompt.getStateContent=function(state){
        return $('#'+$.prompt.currentPrefix+'_state_'+state);
    };

    $.prompt.getCurrentState=function(){
        return $('.'+$.prompt.currentPrefix+'_state:visible');
    };

    $.prompt.getCurrentStateName=function(){
        var stateid=$.prompt.getCurrentState().attr('id');
        return stateid.replace($.prompt.currentPrefix+'_state_','');
    };

    $.prompt.goToState=function(state){
        $('.'+$.prompt.currentPrefix+'_state').slideUp('slow');
        $('#'+$.prompt.currentPrefix+'_state_'+state).slideDown('slow',function(){
            $(this).find('.'+$.prompt.currentPrefix+'defaultbutton').focus();
        });
    };

    $.prompt.nextState=function(){
        var $next=$('.'+$.prompt.currentPrefix+'_state:visible').next();
        $('.'+$.prompt.currentPrefix+'_state').slideUp('slow');
        $next.slideDown('slow',function(){
            $next.find('.'+$.prompt.currentPrefix+'defaultbutton').focus();
        });
    };

    $.prompt.prevState=function(){
        var $next=$('.'+$.prompt.currentPrefix+'_state:visible').prev();
        $('.'+$.prompt.currentPrefix+'_state').slideUp('slow');
        $next.slideDown('slow',function(){
            $next.find('.'+$.prompt.currentPrefix+'defaultbutton').focus();
        });
    };

    $.prompt.close=function(){
        $('#'+$.prompt.currentPrefix+'box').fadeOut('fast',function(){
            $(this).remove();
        });
    };

})(jQuery);



// UTILITY METHODS


function setCookie(c_name,value,exdays){
    var exdate=new Date();
    exdate.setDate(exdate.getDate() + exdays);
    var c_value=escape(value) + ((exdays==null) ? "" : "; expires="+exdate.toUTCString());
    document.cookie=c_name + "=" + c_value;
}

function getCookie(c_name) {
    var i,x,y,ARRcookies=document.cookie.split(";");
    for (i=0;i<ARRcookies.length;i++){
        x=ARRcookies[i].substr(0,ARRcookies[i].indexOf("="));
        y=ARRcookies[i].substr(ARRcookies[i].indexOf("=")+1);
        x=x.replace(/^\s+|\s+$/g,"");
        if (x==c_name){
            return unescape(y);
        }
    }
}

function gup( name ){//thank you lobo235, http://www.netlobo.com/url_query_string_javascript.html
    name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
    var regexS = "[\\?&]"+name+"=([^&#]*)";
    var regex = new RegExp( regexS );
    var results = regex.exec( window.location.href );
    if( results == null )
        return "";
    else
        return results[1];
}

// APPEAR


// APPEAR

/*
 * jQuery.appear
 * http://code.google.com/p/jquery-appear/
 *
 * Copyright (c) 2009 Michael Hixson
 * Licensed under the MIT license (http://www.opensource.org/licenses/mit-license.php)
*/
(function($) {

  $.fn.appear = function(fn, options) {

    var settings = $.extend({

      //arbitrary data to pass to fn
      data: undefined,

      //call fn only on the first appear?
      one: true

    }, options);

    return this.each(function() {

      var t = $(this);

      //whether the element is currently visible
      t.appeared = false;

      if (!fn) {

        //trigger the custom event
        t.trigger('appear', settings.data);
        return;
      }

      var w = $(window);

      //fires the appear event when appropriate
      var check = function() {

        //is the element hidden?
        if (!t.is(':visible')) {

          //it became hidden
          t.appeared = false;
          return;
        }

        //is the element inside the visible window?
        var a = w.scrollLeft();
        var b = w.scrollTop();
        var o = t.offset();
        var x = o.left;
        var y = o.top;

        if (y + t.height() >= b &&
            y <= b + w.height() &&
            x + t.width() >= a &&
            x <= a + w.width()) {

          //trigger the custom event
          if (!t.appeared) t.trigger('appear', settings.data);

        } else {

          //it scrolled out of view
          t.appeared = false;
        }
      };

      //create a modified fn with some additional logic
      var modifiedFn = function() {

        //mark the element as visible
        t.appeared = true;

        //is this supposed to happen only once?
        if (settings.one) {

          //remove the check
          w.unbind('scroll', check);
          var i = $.inArray(check, $.fn.appear.checks);
          if (i >= 0) $.fn.appear.checks.splice(i, 1);
        }

        //trigger the original fn
        fn.apply(this, arguments);
      };

      //bind the modified fn to the element
      if (settings.one) t.one('appear', settings.data, modifiedFn);
      else t.bind('appear', settings.data, modifiedFn);

      //check whenever the window scrolls
      w.scroll(check);

      //check whenever the dom changes
      $.fn.appear.checks.push(check);

      //check now
      (check)();
    });
  };

  //keep a queue of appearance checks
  $.extend($.fn.appear, {

    checks: [],
    timeout: null,

    //process the queue
    checkAll: function() {
      var length = $.fn.appear.checks.length;
      if (length > 0) while (length--) ($.fn.appear.checks[length])();
    },

    //check the queue asynchronously
    run: function() {
      if ($.fn.appear.timeout) clearTimeout($.fn.appear.timeout);
      $.fn.appear.timeout = setTimeout($.fn.appear.checkAll, 20);
    }
  });

  //run checks when these methods are called
  $.each(['append', 'prepend', 'after', 'before', 'attr',
          'removeAttr', 'addClass', 'removeClass', 'toggleClass',
          'remove', 'css', 'show', 'hide'], function(i, n) {
    var old = $.fn[n];
    if (old) {
      $.fn[n] = function() {
        var r = old.apply(this, arguments);
        $.fn.appear.run();
        return r;
      }
    }
  });

})(jQuery);


// READY



$(document).ready(function(){

    //LOSE ATTN
    $('.lose_attn').mouseenter(function() {
        $('.lose_attn').fadeTo("fast",1.0);
    });


    $('.lose_attn').mouseleave(function() {
        $('.lose_attn').fadeTo("fast",0.1);
    });

    //EO LOSE ATTN

    //MAIN IMPROMPTU

    var impromptuPlaceDetails = $('#impromptuPlaceDetails').html();
    $('#impromptuPlaceDetails').remove();

    impromptuPlaceDetailsPopup = {
        state0: {
            html:impromptuPlaceDetails,
            buttons:{
                "ADD PLACE":0,
                "Cancel":1
            },
            focus: 0,
            submit:function(v,m,f){
                switch(v){
                    case 0:{
                        var placename = $("#impromptuPlaceDetailsPlaceName").attr('value');
                        placename = placename.substring(0,29);
                        var placedetails = $("#impromptuPlaceDetailsPlaceDetails").attr('value');
                        placedetails = placedetails.substring(0,499);
                        setNotification("Be patient while you are taken to the place you just created!");
                        window.location.href = '/page/_org?category=143' + '&woehint=' + lastSelectedLocation.lat + ',' + lastSelectedLocation.lng + '&placename=' + placename + '&placedetails=' + placedetails;
                        return false;
                    }
                    case 1:{
                        $.prompt.close();
                        return false;
                    }
                }
            }
        }
    }


    $.prompt.defaults.opacity = 1.0;

    focusLat=0//36.11464603272065;//L.A.
    focusLng=0//-115.17281600000001;//L.A.
    zoom=2;//12;//Setting default zoom in case ip based settings fail

    try{//Attempting to geo locate the user based on ip
        // Build the URL to query
        var url = 'http://www.geoplugin.net/json.gp?jsoncallback=?';
        // Utilize the JSONP API
        $.getJSON(url, function(data){
            focusLng = data.geoplugin_longitude;
            focusLat = data.geoplugin_latitude;
            zoom = data.geoplugin_city != null ? 11 : (data.geoplugin_region != null ? 9 : (data.geoplugin_countryName ? 6 : 2 ) );
            browserLocation = (data.geoplugin_city != null ? data.geoplugin_city + ',': '')
            + (data.geoplugin_region != null ? data.geoplugin_region + ',' : '')
            + (data.geoplugin_countryName != null ? data.geoplugin_countryName : '');
            document.getElementById('searchboxmap').value = browserLocation;
        });
    }catch(err){
        alert(err);
    }

    initialize_gmap_scripts();

    $('.ajax_image').appear(
        function(){
            this.src = this.title;
            this.title= '';
        }
    );

    $("#Juice").mouseenter(
        function(event){
            event.preventDefault();
            $('html,body').animate(
                {
                    scrollTop:$("#Juice").offset().top
                },
                500
            );

        }
    );
    $("#Juice").mouseleave(
        function(event){
            event.preventDefault();
            $('html,body').animate(
                {
                    scrollTop:$("#body").offset().top
                },
                500
            );

        }
    );

   $('.click_indicator')
    .hover(
           function(){
             $(this).fadeTo(0,0.8);
           },
           function(){
             $(this).fadeTo(0,1);
           })
    .click(
           function(){
             $(this).fadeTo(0,0.3);
           });

   $('.click_indicator_strong')
        .hover(
           function(){
             $(this).fadeTo(0,0.5);
           },
           function(){
             $(this).fadeTo(0,1);
           })
        .click(
            function(){
             $(this).fadeTo(0,0.3);
           });

   $('.fillPageType').addClass($('.pageType').attr('value'));

});

//PROGRESSIVE-2

focusMapWithLatLngLoop = undefined;

function focusMapWithLatLng(){
    try{
        if(gup("latitude") != "" && gup("longitude") != ""){
            map.setCenter(new google.maps.LatLng(gup("latitude"),gup("longitude")));
            if(gup("type") != ""){
                var type = gup("type");
                if(type == "building"){
                    map.setZoom(17);
                }
                else if(type == "area"){
                    map.setZoom(15);
                }
                else if(type == "town"){
                    map.setZoom(13);
                }
                else if(type == "city"){
                    map.setZoom(11);
                }else{
                    map.setZoom(10);
                }
            }
            clearInterval(focusMapWithLatLngLoop);
            try{
                $.prompt.close();
            }catch(err){
            //ignoring
            }
        }else{
    }
    }catch(err){
    //alert(err);//debug
    }
}

focusMapWithLatLngLoop = setInterval('focusMapWithLatLng()',2000);

zoomInHint = true;

// EO PROGRESSIVE-2