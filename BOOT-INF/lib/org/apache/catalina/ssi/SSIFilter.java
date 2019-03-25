/*     */ package org.apache.catalina.ssi;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Reader;
/*     */ import java.util.Date;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.servlet.Filter;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.FilterConfig;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
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
/*     */ public class SSIFilter
/*     */   implements Filter
/*     */ {
/*  49 */   protected FilterConfig config = null;
/*     */   
/*  51 */   protected int debug = 0;
/*     */   
/*  53 */   protected Long expires = null;
/*     */   
/*  55 */   protected boolean isVirtualWebappRelative = false;
/*     */   
/*  57 */   protected Pattern contentTypeRegEx = null;
/*     */   
/*     */ 
/*  60 */   protected final Pattern shtmlRegEx = Pattern.compile("text/x-server-parsed-html(;.*)?");
/*     */   
/*  62 */   protected boolean allowExec = false;
/*     */   
/*     */   public void init(FilterConfig config)
/*     */     throws ServletException
/*     */   {
/*  67 */     this.config = config;
/*     */     
/*  69 */     if (config.getInitParameter("debug") != null) {
/*  70 */       this.debug = Integer.parseInt(config.getInitParameter("debug"));
/*     */     }
/*     */     
/*  73 */     if (config.getInitParameter("contentType") != null) {
/*  74 */       this.contentTypeRegEx = Pattern.compile(config.getInitParameter("contentType"));
/*     */     } else {
/*  76 */       this.contentTypeRegEx = this.shtmlRegEx;
/*     */     }
/*     */     
/*     */ 
/*  80 */     this.isVirtualWebappRelative = Boolean.parseBoolean(config.getInitParameter("isVirtualWebappRelative"));
/*     */     
/*  82 */     if (config.getInitParameter("expires") != null) {
/*  83 */       this.expires = Long.valueOf(config.getInitParameter("expires"));
/*     */     }
/*  85 */     this.allowExec = Boolean.parseBoolean(config.getInitParameter("allowExec"));
/*     */     
/*  87 */     if (this.debug > 0) {
/*  88 */       config.getServletContext().log("SSIFilter.init() SSI invoker started with 'debug'=" + this.debug);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
/*     */     throws IOException, ServletException
/*     */   {
/*  96 */     HttpServletRequest req = (HttpServletRequest)request;
/*  97 */     HttpServletResponse res = (HttpServletResponse)response;
/*     */     
/*     */ 
/* 100 */     req.setAttribute("org.apache.catalina.ssi.SSIServlet", "true");
/*     */     
/*     */ 
/* 103 */     ByteArrayServletOutputStream basos = new ByteArrayServletOutputStream();
/*     */     
/* 105 */     ResponseIncludeWrapper responseIncludeWrapper = new ResponseIncludeWrapper(this.config.getServletContext(), req, res, basos);
/*     */     
/*     */ 
/* 108 */     chain.doFilter(req, responseIncludeWrapper);
/*     */     
/*     */ 
/* 111 */     responseIncludeWrapper.flushOutputStreamOrWriter();
/* 112 */     byte[] bytes = basos.toByteArray();
/*     */     
/*     */ 
/* 115 */     String contentType = responseIncludeWrapper.getContentType();
/*     */     
/*     */ 
/* 118 */     if (this.contentTypeRegEx.matcher(contentType).matches()) {
/* 119 */       String encoding = res.getCharacterEncoding();
/*     */       
/*     */ 
/*     */ 
/* 123 */       SSIExternalResolver ssiExternalResolver = new SSIServletExternalResolver(this.config.getServletContext(), req, res, this.isVirtualWebappRelative, this.debug, encoding);
/*     */       
/* 125 */       SSIProcessor ssiProcessor = new SSIProcessor(ssiExternalResolver, this.debug, this.allowExec);
/*     */       
/*     */ 
/*     */ 
/* 129 */       Reader reader = new InputStreamReader(new ByteArrayInputStream(bytes), encoding);
/*     */       
/* 131 */       ByteArrayOutputStream ssiout = new ByteArrayOutputStream();
/* 132 */       PrintWriter writer = new PrintWriter(new OutputStreamWriter(ssiout, encoding));
/*     */       
/*     */ 
/*     */ 
/* 136 */       long lastModified = ssiProcessor.process(reader, responseIncludeWrapper
/* 137 */         .getLastModified(), writer);
/*     */       
/*     */ 
/* 140 */       writer.flush();
/* 141 */       bytes = ssiout.toByteArray();
/*     */       
/*     */ 
/* 144 */       if (this.expires != null) {
/* 145 */         res.setDateHeader("expires", new Date().getTime() + this.expires
/* 146 */           .longValue() * 1000L);
/*     */       }
/* 148 */       if (lastModified > 0L) {
/* 149 */         res.setDateHeader("last-modified", lastModified);
/*     */       }
/* 151 */       res.setContentLength(bytes.length);
/*     */       
/*     */ 
/* 154 */       Matcher shtmlMatcher = this.shtmlRegEx.matcher(responseIncludeWrapper.getContentType());
/* 155 */       if (shtmlMatcher.matches())
/*     */       {
/*     */ 
/* 158 */         String enc = shtmlMatcher.group(1);
/* 159 */         res.setContentType("text/html" + (enc != null ? enc : ""));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 164 */     OutputStream out = null;
/*     */     try {
/* 166 */       out = res.getOutputStream();
/*     */     }
/*     */     catch (IllegalStateException localIllegalStateException) {}
/*     */     
/* 170 */     if (out == null) {
/* 171 */       res.getWriter().write(new String(bytes));
/*     */     } else {
/* 173 */       out.write(bytes);
/*     */     }
/*     */   }
/*     */   
/*     */   public void destroy() {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\ssi\SSIFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */