$(document).ready(function(){
    $('a[rel=shareit], #shareit-box').mouseenter(shareEnter);

    //onmouse out hide the shareit box
    $('#shareit-box').mouseleave(shareLeave);

    //hightlight the textfield on click event
    $('#shareit-field').click(shareClick);
    ////////////////////////////////////////////////////////////////
    var button = $('#button1'), interval;
    buttonText = 'uploading';
    new AjaxUpload(button,{
        action: '/ilikeplaces/fileuploads',
        name: 'myfile',
        onSubmit : function(file, ext){
            // change button text, when user selects file
            button.text(buttonText);

            // If you want to allow uploading only 1 file at time,
            // you can disable upload button
            this.disable();
            var locationId = getLocationId();
            var isPublic = confirm("Is this a public photo?");
            var photoName;
            var photoDescription;
            while(true){
                photoDescription = prompt("What is this photo all about?!");
                if(photoDescription != null){
                    break;
                }
            }
            while(true){
                photoName = prompt("Give your photo a name:");
                if(photoName != null){
                    break;
                }
            }

            this.setData({
                'locationId':locationId,
                'isPublic':isPublic,
                'photoDescription':photoDescription,
                'photoName':photoName
            });
            // Uploding -> Uploading. -> Uploading...
            interval = window.setInterval(function(){
                var text = button.text();
                if (text.length < buttonText.length+10){
                    button.text('^'+text + '^');
                } else {
                    button.text(buttonText);
                }
            }, 200);
        },
        onComplete: function(file, response){
            button.text('Done! Click to Upload More');
            alert(response);

            window.clearInterval(interval);

            // enable upload button
            this.enable();

            // add file to the list
            $('<li></li>').appendTo('#Main_file_list .files').text(file);
        //confirm('Shall I make this photo visible to the WWW?');
        //alert(prompt('What is this photo about? Describe it for your friends!','Another image on ilikeplaces'));
        }
    });
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
    $('a[rel=shareit-mail]').attr('href', 'http://mailto:?subject=' + title);
}

function shareLeave(){
    $('#shareit-field').val('');
    $(this).hide();
}
function shareClick() {
    $(this).select();
}