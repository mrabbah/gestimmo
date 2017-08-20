
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>Plan de masse</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <script type="text/JavaScript" src="js/jsDraw2D.js"></script>
    <style type="text/css">
      .canvas {
        position:absolute;
        width:1px;
        height:1px;
        display: inline-block;
        opacity: .5; /* Standards Compliant Browsers */
        filter: alpha(opacity=50); /* IE 7 and Earlier */
        /* Next 2 lines IE8 */
        -ms-filter: "progid:DXImageTransform.Microsoft.Alpha(Opacity=50)";
        filter: progid:DXImageTransform.Microsoft.Alpha(Opacity=50);      
      }


    </style>
  </head>
  <body bgcolor="#ffffff" onload="verifBrowser();">
    <g:if test="${flash.message}">
      <div class="errors">${flash.message}</div>
    </g:if>
    <div id="canvas" class="canvas">      
    </div> 
    <img name="planmasse" src="images/plans/<% if(projet != null )%><%=projet.plan%>" width="2791" height="800" border="0" id="planmasse" usemap="#m_planmasse" alt="" />
    <map name="m_planmasse" id="m_planmasse">
<% biens.each { bien ->
  if(bien.typeCoordonnees != null && bien.coordonnees != null && bien.statut.code.equals("04")) {
    %>
      <area shape="<%=bien.typeCoordonnees%>" coords="<%=bien.coordonnees%>" href="zul/reservation.zul?cb=<%=bien.numero%>&cp=<%=projet.code%>" alt="" TITLE = "Superficie : <%=bien.superficie%> &#013 Contenance : <%=bien.contenance%> &#013 Type : <%=bien.type%> &#013 Statut : <%=bien.statut%>"/>
    <%
  }
} %>
    </map>

    <script type="text/JavaScript">

        //Create jsGraphics object
        var gr = new jsGraphics(document.getElementById("canvas"));
        //Create jsColor object
        var red = new jsColor("red");
        var yellow = new jsColor("yellow");
        var green = new jsColor("green");
        var blue = new jsColor("blue");

<% biens.each { bien ->
  def pen = ""
  if(bien.statut.code.equals("04")) {
    pen = "green"
  } else if(bien.statut.code.equals("25")) {
    pen = "blue"
  } else if(bien.statut.code.equals("05")) {
    pen = "yellow"
  } else if(bien.statut.code.equals("06")) {
    pen = "red"
  }
  if(!bien.statut.code.equals("04") && bien.typeCoordonnees != null && bien.coordonnees != null && bien.typeCoordonnees.equals("poly")) {
    def xys = bien.coordonnees.split(",")
    def nb = xys.size();
    def ptss = []
    for(int i = 0; i < nb; i+=2 ) {
      ptss.add("new jsPoint(" + xys[i] +"," + xys[i+1] + ")")
    }
    if(ptss != null) {
      def pts = ptss.join(",")                        
      %>
              gr.fillPolygon(<%=pen%>,new Array(<%=pts%>));
      <%
    } 
  } else if(!bien.statut.code.equals("04") && bien.typeCoordonnees != null && bien.coordonnees != null && bien.typeCoordonnees.equals("rect")) {
    def xys = bien.coordonnees.split(",")
    def x1 = xys[0]
    def y1 = xys[1]
    def width = new Integer(xys[2]) - new Integer(xys[0])
    def height = new Integer(xys[3]) - new Integer(xys[1])
    %>
               gr.fillRectangle(<%=pen%>,new jsPoint(
    <%=x1%>,<%=y1%>),<%=width%> , <%=height%>);
    <%
  }
} %>    

function verifBrowser(){
if (navigator.appName == "Microsoft Internet Explorer"){
var version = navigator.appVersion.split(';');
var browserVersion;
if (version.length > 0 ){
 browserVersion =version[1] ;
 if ( browserVersion == "MSIE 8.0"){
   alert('Vous utilisez Internet Explorer 8, vous pouvez faire votre demande sans aucun problème \n\nPour une meilleur visualisation veillez mettre à jour votre navigateur ou essayer un autre navigateur');
 }

 }
}

}

</script>
</body>
</html>