/*     */ package org.apache.catalina.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.FilterConfig;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpServletResponseWrapper;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.res.StringManager;
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
/*     */ 
/*     */ 
/*     */ public class AddDefaultCharsetFilter
/*     */   extends FilterBase
/*     */ {
/*  51 */   private static final Log log = LogFactory.getLog(AddDefaultCharsetFilter.class);
/*     */   
/*     */   private static final String DEFAULT_ENCODING = "ISO-8859-1";
/*     */   private String encoding;
/*     */   
/*     */   public void setEncoding(String encoding)
/*     */   {
/*  58 */     this.encoding = encoding;
/*     */   }
/*     */   
/*     */   protected Log getLogger()
/*     */   {
/*  63 */     return log;
/*     */   }
/*     */   
/*     */   public void init(FilterConfig filterConfig) throws ServletException
/*     */   {
/*  68 */     super.init(filterConfig);
/*  69 */     if ((this.encoding == null) || (this.encoding.length() == 0) || 
/*  70 */       (this.encoding.equalsIgnoreCase("default"))) {
/*  71 */       this.encoding = "ISO-8859-1";
/*  72 */     } else if (this.encoding.equalsIgnoreCase("system")) {
/*  73 */       this.encoding = Charset.defaultCharset().name();
/*  74 */     } else if (!Charset.isSupported(this.encoding)) {
/*  75 */       throw new IllegalArgumentException(sm.getString("addDefaultCharset.unsupportedCharset", new Object[] { this.encoding }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
/*     */     throws IOException, ServletException
/*     */   {
/*  85 */     if ((response instanceof HttpServletResponse)) {
/*  86 */       ResponseWrapper wrapped = new ResponseWrapper((HttpServletResponse)response, this.encoding);
/*     */       
/*  88 */       chain.doFilter(request, wrapped);
/*     */     } else {
/*  90 */       chain.doFilter(request, response);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class ResponseWrapper
/*     */     extends HttpServletResponseWrapper
/*     */   {
/*     */     private String encoding;
/*     */     
/*     */ 
/*     */     public ResponseWrapper(HttpServletResponse response, String encoding)
/*     */     {
/* 103 */       super();
/* 104 */       this.encoding = encoding;
/*     */     }
/*     */     
/*     */ 
/*     */     public void setContentType(String ct)
/*     */     {
/* 110 */       if ((ct != null) && (ct.startsWith("text/"))) {
/* 111 */         if (ct.indexOf("charset=") < 0) {
/* 112 */           super.setContentType(ct + ";charset=" + this.encoding);
/*     */         } else {
/* 114 */           super.setContentType(ct);
/* 115 */           this.encoding = getCharacterEncoding();
/*     */         }
/*     */       } else {
/* 118 */         super.setContentType(ct);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void setHeader(String name, String value)
/*     */     {
/* 125 */       if (name.trim().equalsIgnoreCase("content-type")) {
/* 126 */         setContentType(value);
/*     */       } else {
/* 128 */         super.setHeader(name, value);
/*     */       }
/*     */     }
/*     */     
/*     */     public void addHeader(String name, String value)
/*     */     {
/* 134 */       if (name.trim().equalsIgnoreCase("content-type")) {
/* 135 */         setContentType(value);
/*     */       } else {
/* 137 */         super.addHeader(name, value);
/*     */       }
/*     */     }
/*     */     
/*     */     public void setCharacterEncoding(String charset)
/*     */     {
/* 143 */       super.setCharacterEncoding(charset);
/* 144 */       this.encoding = charset;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\filters\AddDefaultCharsetFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */