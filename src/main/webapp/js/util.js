
/*Utility Functions*/
function hasParams()
{
    return (window.location.href +"").indexOf("?") != -1;
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