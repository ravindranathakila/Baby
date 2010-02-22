/*!
 * query.yoxview v0.9
 * jQuery image gallery viewer
 * http://yoxigen.com/yoxview
 *
 * Copyright (c) 2010 Yossi Kolesnicov
 *
 * Licensed under the MIT license.
 * http://www.opensource.org/licenses/mit-license.php
 *
 * Date: 15th Fabruary, 2010
 * Version : 0.9
 */ 

(function($){
    var yoxviewCreated = false;
    var yoxviewObj;
        
    $.fn.yoxview = function(opt) {
    
        if (this.length == 0)
            return this;
        
         // Load the language file if not already loaded:
        this.loadLanguage = function(lang, callBack)
        {
            var self = this;
            if (languagePacks[lang] == undefined)
            {
                $.getJSON(yoxviewPath + "lang/" + lang + ".js", function(data){
                    languagePacks[lang] = data;
                    callBack(self, data);
                });
            }
            else
                callBack(self, data);
        }
    
        var defaults = {
            backgroundColor : "#000",
            backgroundOpacity : 0.8,
            playDelay : 3000, // Time in milliseconds to display each image
            popupMargin : 20, // the minimum margin between the popup and the window
            infoBackOpacity : 0.5,
            infoBackColor : "Black",
            imagesFolder : yoxviewPath + "images/",
            cacheImagesInBackground : true, // If true, full-size images are cached even while the gallery hasn't been opened yet.
            displayImageTitleByDefault : true, // If true, the image title is always displayed. If false, it's only displayed if hovered on.
            titleDisplayDuration : 2000, // The time in ms to display the image's title, after which it fades out.
            titlePadding : 6, // Padding in pixels for the image's title
            buttonsFadeTime : 500, 
            loopPlay : true, // If true, slideshow play starts over after the last image
            isRTL : false, // Switch direction. For RTL languages such as Hebrew or Arabic, for example.
            lang : "en" // The language for texts. The relevant language file should exist in the /lang folder.
        }

        var options = $.extend(defaults, opt); 

        if (!yoxviewCreated)
        {
            yoxviewCreated = true;
            this.loadLanguage(options.lang, function(views){
                yoxviewObj = new YoxView(views, options);
                
            });
        }
        else
        {
            this.loadLanguage(options.lang, function(views){
                yoxviewObj.AddViews(views, options);
            });
        }   
        
        return this;
    };
})(jQuery);

function ImageDimensions(widthV, heightV)
{
    this.Width = widthV;
    this.Height = heightV;
}

var languagePacks = new Array();
function YoxView(_views, _options)
{  
    var yoxviewObj = this;
    var defaultOptions = _options;
    var options = defaultOptions;
    var currentLanguage = {};
    var views = new Array();
    var currentViewIndex = 0;
    var images;
    var imagesCount = 0;
    var popup;
    var currentItemIndex = 0;
    var thumbnail;
    var thumbnailImg;
    var thumbnailPos;
    var thumbnailProperties;
    var firstImage = true;
    var image1;
    var image2;
    var itemVar;
    var prevBtn;
    var nextBtn;
    var ajaxLoader;
    var helpPanel;
    var popupInfo;
    var popupInfoTitle;
    var popupInfoTitleMinHeight = 28 - 2*options.titlePadding;
    var popupInfoBack;
    var countDisplay;
    var isPlaying = false;
    var notifications = new Array();
    var tempImg = new Image();
    var cacheImg = new Image();
    var currentCacheImg = 0;
    var cachedImages;
    var popupBars;
    this.isOpen = false;
    var isResizing = false;
    var firstViewWithImages;

    this.AddViews = function(_views, options)
    {
        var popupIsCreated = firstViewWithImages != undefined;
        
        jQuery.each(_views, function(){
            setView(this, views.length, options);
            views[views.length] = this;
            if (firstViewWithImages == undefined && $(this).data("Data").images.length != 0)
                firstViewWithImages = this;
        });
        
        if (!popupIsCreated && firstViewWithImages != undefined)
        {
            loadViewImages(firstViewWithImages);
            createPopup();
            if(options.cacheImagesInBackground && imagesCount != 0)
                cacheImages(0);

            popupIsCreated = true;
        }
    }
    function resetPopup()
    {
        popup.remove();
        popup = undefined;
        prevBtn = undefined;
        nextBtn = undefined;
        image1 = undefined;
        image2 = undefined;
        currentItemIndex = 0;
        currentCacheImg = 0;
        createPopup();
    }
    function loadViewImages(_view)
    {
        var viewData = $(_view).data("Data");
        if (currentViewIndex != viewData.viewindex)
        {
            images = viewData.images;
            imagesCount = images.length;
            currentViewIndex = viewData.viewIndex;
            cachedImages = new Array(imagesCount);

            var isResetPopup = false;
            
            if (viewData.options != undefined && options != viewData.options)
            {
                options = viewData.options;
                isResetPopup = true;
            }
            else if (viewData.options == undefined && options != defaultOptions)
            {
                options = defaultOptions;
                isResetPopup = true;
            }
            else if ((prevBtn != undefined && imagesCount == 1) || (popup != undefined && prevBtn == undefined && imagesCount > 0))
                isResetPopup = true;

            if (isResetPopup)
                resetPopup();
        }
    }
    
    function setView(_view, _viewIndex, _options)
    {
        var view = $(_view);
        var viewImages = view.find("a > img");
        var viewImagesCount = viewImages.length;
        view.data("Data", {viewIndex : _viewIndex, images : viewImages});
        if (_options != options)
            view.data("Data").options = _options;
        
        var i = 0;
        jQuery.each(viewImages, function(){
            var link = $(this).parent();
            link.data("Data", {viewIndex : _viewIndex, imageIndex : i});
            link.click(function(){
                var imageData = $(this).data("Data");
                yoxviewObj.openGallery(imageData.viewIndex, imageData.imageIndex);
                return false;
            });
            i++;
        });
    }
    
    function setThumbnail(setToPopupImage)
    {  
        thumbnailImg = $(images[currentItemIndex]);
        if (setToPopupImage && image1 != undefined)
            image1.attr("src", thumbnailImg.attr("src"));
        
        thumbnail = thumbnailImg.parent();
        thumbnailPos = thumbnailImg.offset();
        thumbnailProperties = {width: thumbnailImg.width(), height: thumbnailImg.height(), top: thumbnailPos.top - $(window).scrollTop(), left: thumbnailPos.left };
    }
    
    this.openGallery = function(viewIndex, initialItemIndex)
    {
        loadViewImages(views[viewIndex]);
        if (popup == undefined && imagesCount != 0)
            createPopup();
            
        if(options.cacheImagesInBackground)
            cacheImages(initialItemIndex);
                
        this.selectImage(initialItemIndex);
        popup.parent().fadeIn("slow");
        return false;
    }

    this.selectImage = function(itemIndex)
    {
        currentItemIndex = itemIndex;
        setThumbnail(true);
        thumbnail.blur();
        
        if (!firstImage)
        {
            image1.css({
                "display" : "block",
                "z-index" : "1",
                "width" : thumbnailProperties.width + "px", 
                "height" : thumbnailProperties.height + "px"
            });
            image2.css({
                "display" : "none",
                "z-index" : "2"
            });
            
            firstImage = true;
        }

        popup.css({
            "width" : thumbnailProperties.width + "px",
            "height" : thumbnailProperties.height + "px",
            "top" : thumbnailProperties.top + "px",
            "left" : thumbnailProperties.left + "px"
        });
        this.select(itemIndex, null);
        this.isOpen = true;
    }
    this.refresh = function()
    {
        var wasPlaying = isPlaying;
        if (isPlaying)
            stopPlay();

        setImage(currentItemIndex);
        
        if (wasPlaying)
            startPlay();
    }
    this.select = function(itemIndex, pressedBtn)
    {   
        if (!isResizing)
        {   
            if (itemIndex < 0)
                itemIndex = imagesCount - 1;
            else if (itemIndex == imagesCount)
                itemIndex = 0;
                
            if (!isPlaying && pressedBtn != null)
                flicker(pressedBtn);
            currentItemIndex = itemIndex;
            setImage(currentItemIndex);
        }
    }
    this.prev = function()
    {
        this.select(currentItemIndex - 1, $(prevBtn));
        return false;
    }
    this.next = function()
    {
        this.select(currentItemIndex + 1, $(nextBtn));
        return false;
    }
    this.first = function()
    {
        longFlicker(notifications["first"]);
        this.select(0, null);
        return false;
    }
    this.last = function()
    {
        longFlicker(notifications["last"]);
        this.select(imagesCount - 1, null);
        return false;
    }
    this.play = function()
    {
        if (!isPlaying)
        {
            longFlicker(notifications["play"]);
            startPlay();
        }
        else
        {
            longFlicker(notifications["pause"]);
            stopPlay();
        }
    }
    function flicker(button)
    {
        if (button.css("opacity") == 0)
        {
            button.stop().animate({ opacity : 0 }, options.buttonsFadeTime, fadeOut(button));
        }
    }
    function longFlicker(button)
    {
        button.stop().animate({opacity: 0.6 }, options.buttonsFadeTime).oneTime(1000, function(){
                $(this).stop().animate({ opacity: 0 }, options.buttonsFadeTime);
            });
    }
    function fadeIn(button)
    {
        $(button).stop().animate({ opacity : 0 }, options.buttonsFadeTime);
    }
    function fadeOut(button)
    {
        $(button).stop().animate({ opacity : 0.5 }, options.buttonsFadeTime);
    }

    this.close = function()
    {
        this.closeHelp();
        setThumbnail(false);
        resizePopup(thumbnailProperties.width, thumbnailProperties.height, thumbnailProperties.top, thumbnailProperties.left);
        popup.parent().fadeOut(yoxviewObj.clear);
        popupBars.css("display", "none");
        this.isOpen = false;
        isResizing = false;
    }
    this.help = function()
    {
        if (helpPanel.css("display") == "none")
            helpPanel.css("display", "block").stop().animate({ opacity : 0.8 }, options.buttonsFadeTime);
        else
            this.closeHelp();
    }
    this.closeHelp = function()
    {
        if (helpPanel.css("display") != "none")
        helpPanel.stop().animate({ opacity: 0 }, options.buttonsFadeTime, function(){
                helpPanel.css("display", "none");
            });
    }
    this.clickBtn = function(fn, stopPlaying)
    {
        if (stopPlaying && isPlaying)
            stopPlay();
            
        fn.call(this);
        return false;
    }
    
    function getEventCode(e)
    {
        evt = (e) ? e : window.event;
        var type = evt.type;
        pK = e ? e.keyCode : window.event.keyCode;
        return pK;
    }

    function catchPress(evt)
    {
        if (yoxviewObj != undefined && yoxviewObj.isOpen)
        {
            var pK = getEventCode(evt);
            if (pK == 39)
                return(yoxviewObj.clickBtn(options.isRTL ? yoxviewObj.prev : yoxviewObj.next, true));
            else if (pK == 37)
                return(yoxviewObj.clickBtn(options.isRTL ? yoxviewObj.next : yoxviewObj.prev, true));
            else if (pK == 32)
                return(yoxviewObj.clickBtn(yoxviewObj.next, true));
            else if (pK == 27)
                return(yoxviewObj.clickBtn(yoxviewObj.close, true));
            else if (pK == 13){
                yoxviewObj.play();
                return false;
            }
            else if (pK == 36)
                return(yoxviewObj.clickBtn(yoxviewObj.first, true));
            else if (pK == 35)
                return(yoxviewObj.clickBtn(yoxviewObj.last, true));
            else if (pK == 72)
                return yoxviewObj.clickBtn(yoxviewObj.help, false);
        }
        return true;
    }
    
    function createMenuButton(_imageSrc, _title, btnFunction, stopPlay)
    {
        var btn = $("<a>", {
            href : "#",
            click : function(){
                return yoxviewObj.clickBtn(btnFunction, stopPlay);
            }         
        });
        var btnSpan = $("<span>" + _title + "</span>");
        btnSpan.css("opacity", "0")
        .appendTo(btn);
        
        btn.append(createImage(_imageSrc, _title, "18", "16"))
        .hover( 
            function(){ $(this).stop().animate({ top : "8px" }, "fast").find("span").stop().animate({opacity:1}, "fast") },
            function(){ $(this).stop().animate({ top : "0" }, "fast").find("span").stop().animate({opacity:0}, "fast") }
        );

        return btn;
    }
    
    function createNavButton(_function, _side, _title)
    {      
        var navBtnImg = new Image();
        navBtnImg.src = options.imagesFolder + _side + ".png";
        var navBtn = $("<a>", {
            css : {
                "background" : "url(" + navBtnImg.src + ") no-repeat " + _side + " center",
                "opacity" : "0",
                "outline" : "0"
            },
            className : "yoxview_ctlBtn",
            title : _title,
            href : "#",
            click : function(){
                this.blur();
                return yoxviewObj.clickBtn(_function, true);
            }
        });
        navBtn.css(_side, "0");
        return navBtn; 
    }
    
    // INIT:

    this.AddViews(_views, options);

    document.onkeydown = catchPress;
    window.onresize = function()
    { 
        if (yoxviewObj.isOpen)
            yoxviewObj.resize();
    };
        
    function createPopup()
    {
        currentLanguage = languagePacks[options.lang];
        setThumbnail(true);
        thumbnail.blur();

        popup = $("<div>", {
            id : 'yoxview',
            css : {
                "width" : thumbnailProperties.width + "px",
                "height" : thumbnailProperties.height + "px",
                "top" : thumbnailProperties.top + "px",
                "left" : thumbnailProperties.left + "px"
            }
        });
        popup.appendTo("body");

        // the first image:
        image1 = $("<img />", {
            src : thumbnailImg.attr('src'),
            className : "yoxview_fadeImg",
            css : {"z-index" : "2"}
        });
        popup.append(image1[0]);

        // the second image:
        image2 = $("<img />", {
            className : "yoxview_fadeImg",
            css : {"display" : "none", "z-index" : "1"}
        });
        popup.append(image2[0]);

        // the menu:
        var popupMenuPanel = $("<div>", {
            id : "yoxview_popupMenuPanel",
            className : "yoxview_popupBarPanel yoxview_top",
            css : {"opacity" : "0" }
        });

        var popupMenu = $("<div>", {
            id : "yoxview_popupMenu",
            className : "yoxview_popupBar",
            css : { "opacity" : "0.8" }
        });
        
        var popupMenuBackImg = new Image();
        popupMenuBackImg.src = options.imagesFolder + "menu_back.png";

        popupMenu.append(createMenuButton("close.png", currentLanguage.Close, yoxviewObj.close, true))
        .append(createMenuButton("help.png", currentLanguage.Help, yoxviewObj.help, false));
        
        var playBtn = createMenuButton("play.png", currentLanguage.Slideshow, yoxviewObj.play, false);
  
        if (imagesCount > 1)
        {
            // prev and next buttons:            
            prevBtn = createNavButton(yoxviewObj.prev, options.isRTL ? "right" : "left", currentLanguage.PrevImage);
            prevBtn.appendTo(popup);
            
            nextBtn = createNavButton(yoxviewObj.next, options.isRTL ? "left" : "right", currentLanguage.NextImage);
            nextBtn.appendTo(popup);
        }
        else{
            playBtn.css("opacity", "0.2").unbind("click").click(function(){return false; });
        }
        popupMenu.append(playBtn)
        .find("a:last-child").attr("class", "last");
        
        popupMenuPanel.append(popupMenu).appendTo(popup);

        // add the ajax loader:
        ajaxLoader = $("<div>", {
            id : "yoxview_ajaxLoader",
            className : "yoxview_notification",
            css : { "opacity" : 0 }
        });
        ajaxLoader.append(createImage("popup_ajax_loader.gif", currentLanguage.Loading, "32", "32"))
        .appendTo(popup);
        
        // notification images:
        var notificationsNames = ["play", "pause", "first", "last"];
        jQuery.each(notificationsNames, function(){
            var notification = $("<img />", {
                className : "yoxview_notification",
                alt : this,
                src : options.imagesFolder + "popup_" + this + ".png",
                css : { "opacity" : 0 }
            });
            notification.appendTo(popup);
            notifications[this] = notification;
        });
        
        // help:
        helpPanel = $("<div>", {
            id : "yoxview_helpPanel", 
            href : "#", 
            title : currentLanguage.CloseHelp,
            css : {
                "background" : "url(" + options.imagesFolder + "help_panel.png) no-repeat center top",
                "direction" : currentLanguage.Direction,
                "opacity" : "0"
            },
            click : function(){
                return yoxviewObj.clickBtn(yoxviewObj.help, false);
            }
        });
        
        var helpTitle = document.createElement("h1");
        helpTitle.innerHTML = currentLanguage.Help.toUpperCase();

        var helpText = document.createElement("p");
        helpText.innerHTML = currentLanguage.HelpText;
        
        var closeHelp = document.createElement("span");
        closeHelp.id = "yoxview_closeHelp";
        closeHelp.innerHTML = currentLanguage.CloseHelp;
        
        helpPanel.append(helpTitle).append(helpText).append(closeHelp).appendTo(popup);
        
        // popup info:
        popupInfo = $("<div>", {
            id : "yoxview_popupInfo",
            className : "yoxview_popupBarPanel yoxview_bottom"
        });

        popupInfoBack = $("<div>", {
            className : "yoxview_popupBar yoxview_bottom",
            css : {
                "background" : options.infoBackColor,
                "opacity" : options.infoBackOpacity,
                "z-index" : "1",
                "padding" : options.titlePadding,
                "min-height" : popupInfoTitleMinHeight
            }
        });
        popupInfoBack.appendTo(popupInfo);
        
        popupInfoTitle = $("<h1>", {
            className : "yoxview_popupBar",
            css : {
                "z-index" : "2", 
                "bottom" : options.titlePadding, 
                "opacity" : "1" 
            }
        });
        popupInfoTitle.appendTo(popupInfo);
        
        countDisplay = $("<span>", {
            css : {"opacity" : "1"}
        });
        countDisplay.appendTo(popupInfo);
        popup.append(popupInfo);
        
        // wrap for the popup and the background:
        var popupWrap = $("<div>", {
            id : "yoxview_popupWrap",
            css : {
                "position" : "fixed",
                "top" : "0",
                "left" : "0",
                "width" : "100%",
                "display" : "none",
                "z-index" : "100"
            }
        });
        popup.wrap(popupWrap);

        // set the background:
        $("<div>", {
            css : {
                "position" : "fixed",
                "height" : "100%",
                "width" : "100%",
                "top" : "0",
                "left" : "0",
                "background" : options.backgroundColor,
                "z-index" : "1",
                "opacity" : options.backgroundOpacity
            },
            click : function(){  
                return yoxviewObj.clickBtn(yoxviewObj.close, true);
            }  
        }).appendTo(popup.parent());
        
        popup.find(".yoxview_ctlBtn").hover(
            function(){ 
                $(this).stop().animate({ opacity : 0.5 }, options.buttonsFadeTime);
            },
            function(){
                $(this).stop().animate({ opacity : 0 }, options.buttonsFadeTime);
            }
        );
        
        popupBars = popup.children(".yoxview_popupBarPanel");
        
        popupBars.hover(
            function(){
                $(this).stop().animate({opacity : 1}, options.buttonsFadeTime);
            },
            function(){
                $(this).stop().animate({opacity : 0}, options.buttonsFadeTime);
            }
        );
    }
    $(cacheImg).load(function()
    {
        cachedImages[currentCacheImg] = true;
        if (currentCacheImg < imagesCount - 1)
            cacheImages(currentCacheImg + 1);
    });
    function cacheImages(imageIndexToCache)
    {
        if (!cachedImages[imageIndexToCache])
        {
            currentCacheImg = imageIndexToCache;
            cacheImg.src = $(images[imageIndexToCache]).parent().attr("href");
        }
        else if (imageIndexToCache < imagesCount - 1)
            cacheImages(imageIndexToCache + 1);
    }
    function createImage(_src, _alt, _width, _height)
    {
        var theImg = document.createElement("img");
        $(theImg).attr({
            "src" : options.imagesFolder + _src,
            "alt" : _alt,
            "width" : _width,
            "height" : _height
        });
        
        return theImg;
    }
    this.clear = function()
    {
        image1.attr('src', '');
        image2.attr('src', '');
    }
    function setImage(itemIndex)
    {
        if (!isPlaying)
        {
            ajaxLoader.stop().stopTime()
            .oneTime(options.buttonsFadeTime, function()
            {
                $(this).animate({opacity : 0.5}, options.buttonsFadeTime);
            });
        }

        thumbnail = $(images[itemIndex]).parent();
        tempImg.src = thumbnail.attr("href");
    }
    
    function fitImageSize(imageWidth, imageHeight, targetWidth, targetHeight)
    {
        var resultSize = new ImageDimensions(imageWidth, imageHeight);
        if (imageWidth > targetWidth)
        {
            resultSize.Height = Math.round((targetWidth / imageWidth) * imageHeight);
            resultSize.Width = targetWidth;
        }
        if (resultSize.Height > targetHeight)
        {
            resultSize.Width = Math.round((targetHeight / resultSize.Height) * resultSize.Width);
            resultSize.Height = targetHeight;
        }
        
        return resultSize;
    }
    function resizePopup(_width, _height, _top, _left, callBack)
    {
        popup.stop().animate({
            width: _width,
            height: _height,
            top: _top,
            left: _left
        }, "slow", callBack);
    }
    function startPlay()
    {
        isPlaying = true;
        if (currentItemIndex < imagesCount - 1)
        {
            popup.oneTime(options.playDelay, "play", function(){
                yoxviewObj.next();
            });
        }
        else
        {
            if (options.loopPlay)
                popup.oneTime(options.playDelay, "play", function(){
                    yoxviewObj.select(0, null);
                });
            else
                stopPlay();
        }
    }
    function stopPlay()
    {
        popup.stopTime("play");
        isPlaying = false;
    }

    function blink(_element)
    {
        _element.animate({ opacity : 0.8 }, 1000, function()
        {
            $(this).animate({opacity: 0.2}, 1000, blink($(this)));
        });
    }
    
    var newImage = image1;
    var oldImage = image2;
    
    function getWindowDimensions()
    {
        var widthVal = $(window).width();
        var heightVal = $(window).height();
        var returnValue = {
            height : heightVal,
            width : widthVal,
            usableHeight : heightVal - options.popupMargin * 2,
            usableWidth : widthVal - options.popupMargin * 2
        };
        return returnValue;
    }
    this.resize = function()
    {
        var windowDimensions = getWindowDimensions();
        var imageMaxSize = newImage.data("Data").maxSize;
        
        var newImageDimensions = fitImageSize(
            imageMaxSize.Width,
            imageMaxSize.Height,
            windowDimensions.usableWidth,
            windowDimensions.usableHeight);

        newImage.css({"width" : "100%", "height" : "100%"});
        
        var marginTop = Math.round((windowDimensions.height - newImageDimensions.Height) / 2);
        var marginLeft = Math.round((windowDimensions.width - newImageDimensions.Width) / 2);
        
        isResizing = true;
        resizePopup(newImageDimensions.Width,
            newImageDimensions.Height,
            marginTop,
            marginLeft,
            function(){
                var newImageWidth = popup.width();
                var newImageHeight = popup.height();

                newImage.css({ "width" : newImageWidth + "px", "height" : newImageHeight + "px" });
                isResizing = false;
            }
        );
    }
    
    $(tempImg).load(function()
    {
        if (this.width == 0)
            return;
            
        if (image1.css('z-index') == 1)
        {
            newImage = image1;
            oldImage = image2;
        }
        else
        {
            newImage = image2;
            oldImage = image1;
        }

        newImage.data("Data", { maxSize :new ImageDimensions(this.width, this.height)});
        var windowDimensions = getWindowDimensions();
            
        var newImageDimensions = fitImageSize(
            this.width,
            this.height,
            windowDimensions.usableWidth,
            windowDimensions.usableHeight);

        var imageTitle = thumbnail.find("img").attr("alt");
        popupInfoTitle.html(imageTitle);
        if (imagesCount > 1)
            countDisplay.html(currentItemIndex + 1 + "/" + imagesCount);
        
        newImage.attr({
            src : this.src,
            title : imageTitle
        })
        .css({
            "width" : newImageDimensions.Width + "px",
            "height" : newImageDimensions.Height + "px"
        });
        
        var marginTop = Math.round((windowDimensions.height - newImageDimensions.Height) / 2);
        var marginLeft = Math.round((windowDimensions.width - newImageDimensions.Width) / 2);
              
        if (!isPlaying)
            ajaxLoader.stop().stopTime().animate( {opacity: 0}, options.buttonsFadeTime );

        isResizing = true;
        resizePopup(newImageDimensions.Width,
            newImageDimensions.Height,
            marginTop,
            marginLeft,
            function()
            {
                if (firstImage)
                {
                    popupBars.css("display", "block");

                    popup.children(".yoxview_ctlBtn").animate({opacity: 0.5}, 1500).oneTime(1700, function(){
                        $(this).animate({opacity : 0}, 1500);
                    });
                    popupBars
                        .animate({ opacity: 1}, 1500)
                        .oneTime(1700, function(){
                            $(this).animate({opacity : 0}, 1500);
                        });

                    firstImage = false;
                }
                isResizing = false;
            }
        );

        newImage.css('z-index', '2');
        oldImage.css('z-index', '1');
        
        newImage.fadeIn("slow", function(){
            oldImage.css('display', 'none');

            if (imageTitle != "")
            {
                popupInfo.css({
                    "display" : "block"          
                });
                
                var titleHeight = popupInfoTitle.height() - 2*options.titlePadding;
                if (titleHeight < popupInfoTitleMinHeight)
                    titleHeight = popupInfoTitleMinHeight;
                    
                popupInfoBack.css("height", titleHeight);
                if (options.displayImageTitleByDefault)
                {
                    popupInfo.stop().stopTime().animate({ opacity: 1}, 500, function()
                        {
                            $(this).oneTime(options.titleDisplayDuration, function()
                                {
                                    $(this).stop().animate({opacity : 0}, 500);
                                });
                        }
                    );
                }
            }
            else if (popupInfo.css("display") != "none")
            {
                popupInfo.fadeOut(options.buttonsFadeTime);
            }   
            if (imagesCount > 1)
            {
                if (currentItemIndex < imagesCount - 1){
                    if(options.cacheImagesInBackground)
                        cacheImages(currentItemIndex + 1);
                }
                if (isPlaying)
                    startPlay();
            }
        });
        
        tempImg.src = "";
    });
}