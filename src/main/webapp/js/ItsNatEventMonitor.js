function EventMonitor()
{
    this.before = before;
    this.after = after;

    this.monitor = document.getElementById("monitorId");
    this.count = 0;

    post_fun_arg = {
                  msg:"Your Request Was Comleted Successfully!"
                }

    post_fun = function(obj){
                alert(obj.msg);
                };


    function before(evt)
    {
        if (this.count == 0){
            //this.monitor.style.position = "relative";
            //this.monitor.style.pixelTop= event.clientX;
            //this.monitor.style.pixelLeft = event.clientY;
            //$("#monitorId").css( { "left": 100 + "px", "top":100 + "px" } );
            $('#body').css("cursor","wait");
            //this.monitor.style.display = "block";
        }

        this.count++;
    }

    function after(evt)
    {
        if (this.count == 0){
            return; // to avoid some pending event before registering
        }

        this.count--;

        if (this.count == 0) {
            $('#body').css("cursor","default");
            //this.monitor.style.display = "none";
        }

        post_fun(post_fun_arg);
    }

    post_fun_arg = {
                }

    post_fun = function(obj){
                };

}