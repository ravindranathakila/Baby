<%@page import="java.lang.management.ManagementFactory" %>
<%@page import="java.lang.management.MemoryPoolMXBean" %>
<%@ page import="java.util.Iterator" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
    <meta http-equiv="refresh" content="10"/>
</head>
<body>
JVM Memory Monitor
<%
    Iterator iter = ManagementFactory.getMemoryPoolMXBeans().iterator();

    while (iter.hasNext()) {
        MemoryPoolMXBean item = (MemoryPoolMXBean) iter.next();
%>
<hr/>
Memory MXBean
Heap Memory Usage<%=
ManagementFactory.getMemoryMXBean().getHeapMemoryUsage()
%>
Non-Heap Memory
Usage<%=
ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage()
%>
<%} %>

Memory Pool MXBeans
<%
    iter = ManagementFactory.getMemoryPoolMXBeans().iterator();
    while (iter.hasNext()) {
        MemoryPoolMXBean item = (MemoryPoolMXBean) iter.next();
%>
<hr/>
<%= item.getName() %>
Type<%= item.getType() %>
Usage<%= item.getUsage() %>
Peak Usage<%= item.getPeakUsage() %>
Collection Usage<%= item.getCollectionUsage() %>


<%
    }

%>

</body>
</html>
