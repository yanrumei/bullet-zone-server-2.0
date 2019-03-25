/*     */ package org.apache.catalina.ssi;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletOutputStream;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpServletResponseWrapper;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResponseIncludeWrapper
/*     */   extends HttpServletResponseWrapper
/*     */ {
/*  52 */   protected long lastModified = -1L;
/*  53 */   private String contentType = null;
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
/*  66 */   private static final DateFormat RFC1123_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
/*  67 */   static { RFC1123_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT")); }
/*     */   
/*     */ 
/*     */   private static final String CONTENT_TYPE = "content-type";
/*     */   
/*     */   private static final String LAST_MODIFIED = "last-modified";
/*     */   
/*     */   private static final String RFC1123_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";
/*     */   protected final ServletOutputStream captureServletOutputStream;
/*     */   protected ServletOutputStream servletOutputStream;
/*     */   protected PrintWriter printWriter;
/*     */   private final ServletContext context;
/*     */   private final HttpServletRequest request;
/*     */   public ResponseIncludeWrapper(ServletContext context, HttpServletRequest request, HttpServletResponse response, ServletOutputStream captureServletOutputStream)
/*     */   {
/*  82 */     super(response);
/*  83 */     this.context = context;
/*  84 */     this.request = request;
/*  85 */     this.captureServletOutputStream = captureServletOutputStream;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void flushOutputStreamOrWriter()
/*     */     throws IOException
/*     */   {
/*  96 */     if (this.servletOutputStream != null) {
/*  97 */       this.servletOutputStream.flush();
/*     */     }
/*  99 */     if (this.printWriter != null) {
/* 100 */       this.printWriter.flush();
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
/*     */   public PrintWriter getWriter()
/*     */     throws IOException
/*     */   {
/* 115 */     if (this.servletOutputStream == null) {
/* 116 */       if (this.printWriter == null) {
/* 117 */         setCharacterEncoding(getCharacterEncoding());
/*     */         
/*     */ 
/* 120 */         this.printWriter = new PrintWriter(new OutputStreamWriter(this.captureServletOutputStream, getCharacterEncoding()));
/*     */       }
/* 122 */       return this.printWriter;
/*     */     }
/* 124 */     throw new IllegalStateException();
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
/*     */   public ServletOutputStream getOutputStream()
/*     */     throws IOException
/*     */   {
/* 138 */     if (this.printWriter == null) {
/* 139 */       if (this.servletOutputStream == null) {
/* 140 */         this.servletOutputStream = this.captureServletOutputStream;
/*     */       }
/* 142 */       return this.servletOutputStream;
/*     */     }
/* 144 */     throw new IllegalStateException();
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
/*     */   public long getLastModified()
/*     */   {
/* 157 */     if (this.lastModified == -1L)
/*     */     {
/*     */ 
/* 160 */       return -1L;
/*     */     }
/* 162 */     return this.lastModified;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContentType()
/*     */   {
/* 173 */     if (this.contentType == null) {
/* 174 */       String url = this.request.getRequestURI();
/* 175 */       String mime = this.context.getMimeType(url);
/* 176 */       if (mime != null) {
/* 177 */         setContentType(mime);
/*     */       }
/*     */       else {
/* 180 */         setContentType("application/x-octet-stream");
/*     */       }
/*     */     }
/* 183 */     return this.contentType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContentType(String mime)
/*     */   {
/* 193 */     this.contentType = mime;
/* 194 */     if (this.contentType != null) {
/* 195 */       getResponse().setContentType(this.contentType);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void addDateHeader(String name, long value)
/*     */   {
/* 202 */     super.addDateHeader(name, value);
/* 203 */     String lname = name.toLowerCase(Locale.ENGLISH);
/* 204 */     if (lname.equals("last-modified")) {
/* 205 */       this.lastModified = value;
/*     */     }
/*     */   }
/*     */   
/*     */   public void addHeader(String name, String value)
/*     */   {
/* 211 */     super.addHeader(name, value);
/* 212 */     String lname = name.toLowerCase(Locale.ENGLISH);
/* 213 */     if (lname.equals("last-modified")) {
/*     */       try {
/* 215 */         synchronized (RFC1123_FORMAT) {
/* 216 */           this.lastModified = RFC1123_FORMAT.parse(value).getTime();
/*     */         }
/*     */       } catch (Throwable ignore) {
/* 219 */         ExceptionUtils.handleThrowable(ignore);
/*     */       }
/* 221 */     } else if (lname.equals("content-type")) {
/* 222 */       this.contentType = value;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setDateHeader(String name, long value)
/*     */   {
/* 228 */     super.setDateHeader(name, value);
/* 229 */     String lname = name.toLowerCase(Locale.ENGLISH);
/* 230 */     if (lname.equals("last-modified")) {
/* 231 */       this.lastModified = value;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setHeader(String name, String value)
/*     */   {
/* 237 */     super.setHeader(name, value);
/* 238 */     String lname = name.toLowerCase(Locale.ENGLISH);
/* 239 */     if (lname.equals("last-modified")) {
/*     */       try {
/* 241 */         synchronized (RFC1123_FORMAT) {
/* 242 */           this.lastModified = RFC1123_FORMAT.parse(value).getTime();
/*     */         }
/*     */       } catch (Throwable ignore) {
/* 245 */         ExceptionUtils.handleThrowable(ignore);
/*     */       }
/*     */       
/* 248 */     } else if (lname.equals("content-type"))
/*     */     {
/* 250 */       this.contentType = value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\ssi\ResponseIncludeWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */