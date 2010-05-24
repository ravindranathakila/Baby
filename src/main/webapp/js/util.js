
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