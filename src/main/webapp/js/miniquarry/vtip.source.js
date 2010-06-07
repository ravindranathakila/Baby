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
                    "whenever u say home, we say places. is that ok?!",
                    "i'm done with you, go somewhere!",
                    "is this some kind of joke? go some other place!",
                    "i can't quite place u, where r u from?!",
                    "oops we did again, thing r never gonna be the same... place!",
                    "i'm fed up with you. have u no other place to go?!",
                    "just fooling around eh? y not go some place fun?!",
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
            this.t = (this.title == undefined || this.title == '') ? ilp_getRandomTagLine() : this.title;
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


//Added to jquery.ready
//jQuery(document).ready(function($){vtip();})