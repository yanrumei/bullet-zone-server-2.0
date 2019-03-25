/*     */ package org.apache.catalina.ssi;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import javax.servlet.ServletConfig;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServlet;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SSIServlet
/*     */   extends HttpServlet
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  50 */   protected int debug = 0;
/*     */   
/*  52 */   protected boolean buffered = false;
/*     */   
/*  54 */   protected Long expires = null;
/*     */   
/*  56 */   protected boolean isVirtualWebappRelative = false;
/*     */   
/*  58 */   protected String inputEncoding = null;
/*     */   
/*  60 */   protected String outputEncoding = "UTF-8";
/*     */   
/*  62 */   protected boolean allowExec = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void init()
/*     */     throws ServletException
/*     */   {
/*  75 */     if (getServletConfig().getInitParameter("debug") != null) {
/*  76 */       this.debug = Integer.parseInt(getServletConfig().getInitParameter("debug"));
/*     */     }
/*     */     
/*  79 */     this.isVirtualWebappRelative = Boolean.parseBoolean(getServletConfig().getInitParameter("isVirtualWebappRelative"));
/*     */     
/*  81 */     if (getServletConfig().getInitParameter("expires") != null) {
/*  82 */       this.expires = Long.valueOf(getServletConfig().getInitParameter("expires"));
/*     */     }
/*  84 */     this.buffered = Boolean.parseBoolean(getServletConfig().getInitParameter("buffered"));
/*     */     
/*  86 */     this.inputEncoding = getServletConfig().getInitParameter("inputEncoding");
/*     */     
/*  88 */     if (getServletConfig().getInitParameter("outputEncoding") != null) {
/*  89 */       this.outputEncoding = getServletConfig().getInitParameter("outputEncoding");
/*     */     }
/*  91 */     this.allowExec = Boolean.parseBoolean(
/*  92 */       getServletConfig().getInitParameter("allowExec"));
/*     */     
/*  94 */     if (this.debug > 0) {
/*  95 */       log("SSIServlet.init() SSI invoker started with 'debug'=" + this.debug);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doGet(HttpServletRequest req, HttpServletResponse res)
/*     */     throws IOException, ServletException
/*     */   {
/* 115 */     if (this.debug > 0) log("SSIServlet.doGet()");
/* 116 */     requestHandler(req, res);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doPost(HttpServletRequest req, HttpServletResponse res)
/*     */     throws IOException, ServletException
/*     */   {
/* 136 */     if (this.debug > 0) log("SSIServlet.doPost()");
/* 137 */     requestHandler(req, res);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void requestHandler(HttpServletRequest req, HttpServletResponse res)
/*     */     throws IOException
/*     */   {
/* 152 */     ServletContext servletContext = getServletContext();
/* 153 */     String path = SSIServletRequestUtil.getRelativePath(req);
/* 154 */     if (this.debug > 0) {
/* 155 */       log("SSIServlet.requestHandler()\nServing " + (this.buffered ? "buffered " : "unbuffered ") + "resource '" + path + "'");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 160 */     if ((path == null) || (path.toUpperCase(Locale.ENGLISH).startsWith("/WEB-INF")) || 
/* 161 */       (path.toUpperCase(Locale.ENGLISH).startsWith("/META-INF"))) {
/* 162 */       res.sendError(404, path);
/* 163 */       log("Can't serve file: " + path);
/* 164 */       return;
/*     */     }
/* 166 */     URL resource = servletContext.getResource(path);
/* 167 */     if (resource == null) {
/* 168 */       res.sendError(404, path);
/* 169 */       log("Can't find file: " + path);
/* 170 */       return;
/*     */     }
/* 172 */     String resourceMimeType = servletContext.getMimeType(path);
/* 173 */     if (resourceMimeType == null) {
/* 174 */       resourceMimeType = "text/html";
/*     */     }
/* 176 */     res.setContentType(resourceMimeType + ";charset=" + this.outputEncoding);
/* 177 */     if (this.expires != null) {
/* 178 */       res.setDateHeader("Expires", new Date().getTime() + this.expires
/* 179 */         .longValue() * 1000L);
/*     */     }
/* 181 */     req.setAttribute("org.apache.catalina.ssi.SSIServlet", "true");
/* 182 */     processSSI(req, res, resource);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void processSSI(HttpServletRequest req, HttpServletResponse res, URL resource)
/*     */     throws IOException
/*     */   {
/* 189 */     SSIExternalResolver ssiExternalResolver = new SSIServletExternalResolver(getServletContext(), req, res, this.isVirtualWebappRelative, this.debug, this.inputEncoding);
/*     */     
/* 191 */     SSIProcessor ssiProcessor = new SSIProcessor(ssiExternalResolver, this.debug, this.allowExec);
/*     */     
/* 193 */     PrintWriter printWriter = null;
/* 194 */     StringWriter stringWriter = null;
/* 195 */     if (this.buffered) {
/* 196 */       stringWriter = new StringWriter();
/* 197 */       printWriter = new PrintWriter(stringWriter);
/*     */     } else {
/* 199 */       printWriter = res.getWriter();
/*     */     }
/*     */     
/* 202 */     URLConnection resourceInfo = resource.openConnection();
/* 203 */     InputStream resourceInputStream = resourceInfo.getInputStream();
/* 204 */     String encoding = resourceInfo.getContentEncoding();
/* 205 */     if (encoding == null)
/* 206 */       encoding = this.inputEncoding;
/*     */     InputStreamReader isr;
/*     */     InputStreamReader isr;
/* 209 */     if (encoding == null) {
/* 210 */       isr = new InputStreamReader(resourceInputStream);
/*     */     } else {
/* 212 */       isr = new InputStreamReader(resourceInputStream, encoding);
/*     */     }
/* 214 */     BufferedReader bufferedReader = new BufferedReader(isr);
/*     */     
/* 216 */     long lastModified = ssiProcessor.process(bufferedReader, resourceInfo
/* 217 */       .getLastModified(), printWriter);
/* 218 */     if (lastModified > 0L) {
/* 219 */       res.setDateHeader("last-modified", lastModified);
/*     */     }
/* 221 */     if (this.buffered) {
/* 222 */       printWriter.flush();
/*     */       
/* 224 */       String text = stringWriter.toString();
/* 225 */       res.getWriter().write(text);
/*     */     }
/* 227 */     bufferedReader.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\ssi\SSIServlet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */