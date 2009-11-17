function EventMonitor()
{
    this.before = before;
    this.after = after;

    this.monitor = document.getElementById("monitorId");
    this.count = 0;


    function before(evt)
    {
        if (this.count == 0){
            this.monitor.style.position = "relative";
            //this.monitor.style.pixelTop= event.clientX;
            //this.monitor.style.pixelLeft = event.clientY;
            //$("#monitorId").css( { "left": 100 + "px", "top":100 + "px" } );
            this.monitor.style.display = "block";
        }

        this.count++;
    }

    function after(evt)
    {
        if (this.count == 0) return; // to avoid some pending event before registering

        this.count--;

        if (this.count == 0)
            this.monitor.style.display = "none";
    }
}