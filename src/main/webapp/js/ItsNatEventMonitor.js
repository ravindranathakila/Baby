    bef =  function(){
		$.blockUI()
		};

    aft = function(){
		$.unblockUI()
		};

function EventMonitor()
{
    this.before = before;
    this.after = after;

    this.monitor = document.getElementById("monitorId");
    this.count = 0;

    post_fun_arg = {
                  msg:"Your Request Was Completed Successfully!"
                };

    post_fun = function(obj){
                 //alert(obj.msg);
                };


    function before(evt)
    {
        if (this.count == 0){
            //this.monitor.style.position = "relative";
            //this.monitor.style.pixelTop= event.clientX;
            //this.monitor.style.pixelLeft = event.clientY;
            //this.monitor.style.display = "block";
            $('#monitorId').show();
            alert(evt.pageX + "," + evt.pageY);
        }

        this.count++;
        //bef();
    }

    function after(evt)
    {
        if (this.count == 0){
            return; // to avoid some pending event before registering
        }

        this.count--;

        if (this.count == 0) {
            //this.monitor.style.display = "none";
            $('#monitorId').hide();
        }

        post_fun(post_fun_arg);
    }

    post_fun_arg = {
                };

    post_fun = function(obj){
                };

}