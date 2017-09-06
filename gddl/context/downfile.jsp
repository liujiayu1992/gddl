<%
    String   filename   = request.getParameter("filename");
    String   filepath   = request.getParameter("filepath");
    String filenameutf =  java.net.URLEncoder.encode(filename,"UTF-8");
    //String abspath = application.getRealPath("");
    //System.out.println("1"+abspath);
    //String oldpath = request.getContextPath();
    //System.out.println("2"+oldpath);
    //oldpath = oldpath.replaceAll("/","");
    //System.out.println("3"+oldpath);
    //abspath = abspath.replaceAll(oldpath,filepath);
    //System.out.println("4"+abspath);
    //abspath = abspath+"\\"+filename;
    //System.out.println("5"+abspath);
    java.io.File file = new java.io.File(filepath+"/"+filename);
    //System.out.println(filepath);
    String   CONTENT_TYPE   =   "text/html;   charset=GB2312";
    response.setContentType(CONTENT_TYPE);
    response.setContentType("APPLICATION/OCTET-STREAM");
    //System.out.println(filepath+filename);
    response.setHeader("Content-Disposition","attachment;   filename=\""   +   filenameutf   +   "\"");
    java.io.FileInputStream   fileInputStream   =
      new   java.io.FileInputStream(file);

    int   i;
    while   ((i=fileInputStream.read())   !=   -1)   {
      response.getOutputStream().write(i);
    }
    fileInputStream.close();
    response.getOutputStream().close();
  %>
