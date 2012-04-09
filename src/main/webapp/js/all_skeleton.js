/**
Vertigo Tip by www.vertigo-project.com
Requires jQuery
Edited by Ravindranath Akila
*/

var ilp_tagLines = ["it's social, it's real!",
                    "go places!",
                    "go please!",
                    "socialize for real!",
                    "it's complicated, it's i like places!",
                    "just... have fun!",
                    "the worlds playground!",
                    "when you decide to travel!",
                    "head out and explore!",
                    "escape... not press, just do it!",
                    "now that's how you socialize!",
                    "we take you places!",
                    "building a happier planet!",
                    "spinning a happier planet!",
                    "but, why not travel?!",
                    "fun at home? oh please!",
                    "food for thought, place to be happy!",
                    "where do you think you are going?!",
                    "are there heavenly places on earth?!",
                    "we love places, do you?!",
                    "place matters.. when it's social..",
                    "come.. let's go somewhere..!",
                    "admit it! places ARE fun!",
                    "how on earth did we miss these places?!",
                    "that WOEID stands for Where On Earth ID!",
                    "even when places move, WOEID's hang on to them!",
                    "did you know you can obtain a WOEID for a place?!",
                    "man this is boring. let's go some place!",
                    "all places personal and public!",
                    "no place on earth we'd like to miss!",
                    "hey! going somewhere? tell us about it!",
                    "i miss u.. u miss me.. it's complicatd.. it's i like places.",
                    "u didn't see us coming did u? we come from places!",
                    "places even Gods dig!",
                    "have u seen heaven? it's more like some of these places!",
                    "u know, if u do it right, most fun and places cost nothing!",
                    "no hard feelings... get's softer with places!",
                    "some go heaven.. we go places!",
                    "go to hell! that's a place! even hell is fun at i like places!",
                    "if u keep messing with me, i'll take u places!",
                    "get lost.. in some place!",
                    "whenever u say home, we say places!",
                    "i'm done with you, go somewhere!",
                    "is this some kind of joke? go some other place!",
                    "i can't quite place u, where r u from?!",
                    "oops we did again, things r never gonna be the same... place!",
                    "i'm fed up with you. have u no other place to go?!",
                    "just fooling around eh? y not go some place fun?!",
                    "there's a place... in your heart...!",//song
                    "places at best... when you say nothing at all...!",//song
                    "waka waka eh eh... this time for africa!",//song
                    "hw do u differentiate good tips 4m these? Rant! these R good tips!",//duet
                    "hw do u differentiate good tips 4m these? simple letters & ends with !",//coduet
                    "do u knw y we wrote this stuff? too fool u! kidding!",//duet
                    "do u knw y we wrote this stuff? we can't call u, can we?!",//coduet
                    "how many of these msgs do u think r there? know it? let US know!",//duet
                    "how many of these msgs do u think r there? just enuf to drive u crazy!"];//coduet


function ilp_getRandomTagLine(){
    return ilp_tagLines[Math.floor(Math.random() * ilp_tagLines.length)];
}

this.vtip = function() {
    this.xOffset = -5; // x distance from mouse
    this.yOffset = 30; // y distance from mouse

    $(".vtip").unbind().hover(
        function(e) {
            this.t = (this.title == undefined || this.title == 'undefined' || this.title == '') ? ilp_getRandomTagLine() : this.title;
            this.title = '';

	        this.top = (e.pageY + yOffset); this.left = (e.pageX + xOffset);

            $('body').append( '<p id="vtip"><img id="vtipArrow" />' + this.t + '</p>' );

            $('p#vtip #vtipArrow').attr("src", '/images/vtip_arrow.png');
            $('p#vtip').css("top", this.top+"px").css("left", this.left+"px").fadeIn("slow");

        },
        function() {
            this.title = this.t;
            $("p#vtip").fadeOut("slow").remove();
        }
    ).mousemove(
        function(e) {
            this.top = (e.pageY + yOffset);
            this.left = (e.pageX + xOffset);

            $("p#vtip").css("top", this.top+"px").css("left", this.left+"px");
        }
    );

    $(".ilp_tagl").unbind().hover(
        function(e) {
            this.t = ilp_getRandomTagLine();
            this.title = '';

	    this.top = (e.pageY + yOffset); this.left = (e.pageX + xOffset);

            $('body').append( '<p id="vtip"><img id="vtipArrow" />' + this.t + '</p>' );

            $('p#vtip #vtipArrow').attr("src", '/images/vtip_arrow.png');
            $('p#vtip').css("top", this.top+"px").css("left", this.left+"px").fadeIn("slow");

        },
        function() {
            this.title = ilp_getRandomTagLine();
            $("p#vtip").fadeOut("slow").remove();
        }
    ).mousemove(
        function(e) {
            this.top = (e.pageY + yOffset);
            this.left = (e.pageX + xOffset);

            $("p#vtip").css("top", this.top+"px").css("left", this.left+"px");
        }
    );

};



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



////// UTIL.JS ////////////

// DO NOT EDIT THIS FILE! see README.TXT - Javascript Compressor 1.1.0


/*Utility Functions*/
function hasParams()
{
    return (window.location.href +"").indexOf("?") != -1;
}
function hasNavHash()
{
    return (window.location.href +"").indexOf("#") != -1;
}
function makeParam(key,value)
{
    return ( hasParams() ? "&" : "?") + key + "=" + value;
}


/*non utility functions*/

function setDisplayName(){
$.prompt( {
                state0: {
                    html:'Enter a display name your friends know you as<br /> ' +
                         '<input type=\"text\" id=\"NameInput\" name=\"NameInput\" value=\"\" />',
                    buttons:{
                        Cancel:false,
                        Save:true
                    },
                    submit:function(v,m,f){
                        if(v)
                        {
                          window.location = (window.location.href + "").replace(/displayname/g,"displaynameold") + makeParam("displayname",f.NameInput);
                        }
                        return true;
                    }
                }
                }
                );
 }

 function ygeoarea(placeTypeCode){
 switch(placeTypeCode)
  {
    case 29://Continent
     return 3;
     break;
    case 19://Super Place
     return 3;
     break;
    case 12://Country
      return 5;
      break;
    case 8://Admin1
      return 8;
      break;
    case 9://Admin2
      return 11;
      break;
    case 7://Town
      return 12;
      break;
    case 13://Town
      return 13;
      break;
    default:
      return 8;
    }
 }



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
    vtip();

      //LOSE ATTN
    $('.gain_attn').mouseenter(function() {
       $('.lose_attn').fadeTo("fast",0.6);
    });


    $('.gain_attn').mouseleave(function() {
        $('.lose_attn').fadeTo("fast",1.0);
    });


    $('.ajax_image').appear(
        function(){
            this.src = this.title;
            this.title= '';
        }
    );

    $('#SignInOn').attr('style','display:block');


    ////////////////// HASH CHANGE DETECTION
    $(window).bind('hashchange', function() {
        var url = '' + window.location.hash;
        url = url.replace('.','\\.');
        url = url.replace(/#/g,'');
        if(url != '' && url != 'JSFriends'){
            $('.sidebars').css('display','none');
            $('.non_sidebar').addClass('use100').removeClass('use528');
            $('.carouselable').hide();
            $('.' + url).fadeIn('slow');
            $('.' + url).find('.wall_input').focus();
            $('.' + url).find('.wall_input').blur();
            //fireEvent($('.' + url).children('.wall_submit')[0],"click");
            $('#' + this.id).parent().parent().appendTo('.carousel');
        }
    });

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




