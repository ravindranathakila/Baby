/*
 * ajaxel - jQuery Plugin
 * http://www.svacant.com
 *
 * Licensed under the GPL license
 *
 * $Date: 2009-09-25 19:53:22 $
 * $version: 0.2
 */
 
(function($){
	
	$.fn.extend( {
    ajaxel: function(options){
	
	var defaults = {
	
	classname: 'ajaxel',
	callback: 'ajaxel_popup',
	type: 'text'
	
	};
    var options = $.extend(defaults, options);

	return this.each(function(){

	/* Ajaxify a elements */
	$('a.'+options.classname).each(function(){
	var a_href = $(this).attr('href');
	$(this).attr('href','#');
	$(this).click(function(e){
	$.get(a_href,{'ajax' : 'true' },function(data){ eval(options.callback)(data); },options.type);
	});
	});
	
	
	/** Ajaxify form elements */
	$('form.'+options.classname).each(function(){
	
	
	var form_id = $(this).attr('id');
	var form_action = $(this).attr('action');
	var form_target = $(this).attr('target');
	
	
	/** If haven't form target proceed with true ajax */	
	if(form_target == ''){

	$('#'+form_id).submit(function(){ return false });
	$('#'+form_id+' input:submit').click(function(e){ ajaxel_send_form(form_id,form_action); });

	}else{
	
	/** Go to build false ajax, iframe! */

	$('#'+form_id+' input:submit').after('<iframe width=0 height=0 frameborder=0 name='+form_target+'></iframe>');

	$('html').mousemove(function(e){

	var html_iframe = $('iframe[name='+form_target+']').contents().find('body').html();
	
	$('iframe[name='+form_target+']').contents().find('body').html('');
	
	if(html_iframe != ''){

	//If json convert the value in object
	if(options.type == 'json'){ var html_iframe = (eval("(" + html_iframe+ ")")); }

	//Callback
	eval(options.callback)(html_iframe);
	
	$('iframe[name='+form_target+']').contents().find('body').html('');
	
	
	}
	
	});
	}
	
	
	/** Send form function method post **/

	function ajaxel_send_form(el,page){

	var fields = $('#'+el).serializeArray();

	$.post(page,fields,function(data){
	eval(options.callback)(data);
	},options.type);

	}
	});
	
	function ajaxel_popup(data){

	alert(data);
        confirm("click?");

	}
	});

	}   

    });

})(jQuery);
