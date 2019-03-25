/*     */ package org.apache.catalina.valves.rewrite;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Calendar;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.WebResource;
/*     */ import org.apache.catalina.WebResourceRoot;
/*     */ import org.apache.catalina.connector.Connector;
/*     */ import org.apache.catalina.connector.Request;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
/*     */ import org.apache.tomcat.util.http.FastHttpDateFormat;
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
/*     */ public class ResolverImpl
/*     */   extends Resolver
/*     */ {
/*  29 */   protected Request request = null;
/*     */   
/*     */   public ResolverImpl(Request request) {
/*  32 */     this.request = request;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String resolve(String key)
/*     */   {
/*  43 */     if (key.equals("HTTP_USER_AGENT"))
/*  44 */       return this.request.getHeader("user-agent");
/*  45 */     if (key.equals("HTTP_REFERER"))
/*  46 */       return this.request.getHeader("referer");
/*  47 */     if (key.equals("HTTP_COOKIE"))
/*  48 */       return this.request.getHeader("cookie");
/*  49 */     if (key.equals("HTTP_FORWARDED"))
/*  50 */       return this.request.getHeader("forwarded");
/*  51 */     if (key.equals("HTTP_HOST")) {
/*  52 */       String host = this.request.getHeader("host");
/*  53 */       if (host != null) {
/*  54 */         int index = host.indexOf(':');
/*  55 */         if (index != -1) {
/*  56 */           host = host.substring(0, index);
/*     */         }
/*     */       }
/*  59 */       return host; }
/*  60 */     if (key.equals("HTTP_PROXY_CONNECTION"))
/*  61 */       return this.request.getHeader("proxy-connection");
/*  62 */     if (key.equals("HTTP_ACCEPT"))
/*  63 */       return this.request.getHeader("accept");
/*  64 */     if (key.equals("REMOTE_ADDR"))
/*  65 */       return this.request.getRemoteAddr();
/*  66 */     if (key.equals("REMOTE_HOST"))
/*  67 */       return this.request.getRemoteHost();
/*  68 */     if (key.equals("REMOTE_PORT"))
/*  69 */       return String.valueOf(this.request.getRemotePort());
/*  70 */     if (key.equals("REMOTE_USER"))
/*  71 */       return this.request.getRemoteUser();
/*  72 */     if (key.equals("REMOTE_IDENT"))
/*  73 */       return this.request.getRemoteUser();
/*  74 */     if (key.equals("REQUEST_METHOD"))
/*  75 */       return this.request.getMethod();
/*  76 */     if (key.equals("SCRIPT_FILENAME"))
/*  77 */       return this.request.getServletContext().getRealPath(this.request.getServletPath());
/*  78 */     if (key.equals("REQUEST_PATH"))
/*  79 */       return this.request.getRequestPathMB().toString();
/*  80 */     if (key.equals("CONTEXT_PATH"))
/*  81 */       return this.request.getContextPath();
/*  82 */     if (key.equals("SERVLET_PATH"))
/*  83 */       return emptyStringIfNull(this.request.getServletPath());
/*  84 */     if (key.equals("PATH_INFO"))
/*  85 */       return emptyStringIfNull(this.request.getPathInfo());
/*  86 */     if (key.equals("QUERY_STRING"))
/*  87 */       return emptyStringIfNull(this.request.getQueryString());
/*  88 */     if (key.equals("AUTH_TYPE"))
/*  89 */       return this.request.getAuthType();
/*  90 */     if (key.equals("DOCUMENT_ROOT"))
/*  91 */       return this.request.getServletContext().getRealPath("/");
/*  92 */     if (key.equals("SERVER_NAME"))
/*  93 */       return this.request.getLocalName();
/*  94 */     if (key.equals("SERVER_ADDR"))
/*  95 */       return this.request.getLocalAddr();
/*  96 */     if (key.equals("SERVER_PORT"))
/*  97 */       return String.valueOf(this.request.getLocalPort());
/*  98 */     if (key.equals("SERVER_PROTOCOL"))
/*  99 */       return this.request.getProtocol();
/* 100 */     if (key.equals("SERVER_SOFTWARE"))
/* 101 */       return "tomcat";
/* 102 */     if (key.equals("THE_REQUEST"))
/* 103 */       return 
/* 104 */         this.request.getMethod() + " " + this.request.getRequestURI() + " " + this.request.getProtocol();
/* 105 */     if (key.equals("REQUEST_URI"))
/* 106 */       return this.request.getRequestURI();
/* 107 */     if (key.equals("REQUEST_FILENAME"))
/* 108 */       return this.request.getPathTranslated();
/* 109 */     if (key.equals("HTTPS"))
/* 110 */       return this.request.isSecure() ? "on" : "off";
/* 111 */     if (key.equals("TIME_YEAR"))
/* 112 */       return String.valueOf(Calendar.getInstance().get(1));
/* 113 */     if (key.equals("TIME_MON"))
/* 114 */       return String.valueOf(Calendar.getInstance().get(2));
/* 115 */     if (key.equals("TIME_DAY"))
/* 116 */       return String.valueOf(Calendar.getInstance().get(5));
/* 117 */     if (key.equals("TIME_HOUR"))
/* 118 */       return String.valueOf(Calendar.getInstance().get(11));
/* 119 */     if (key.equals("TIME_MIN"))
/* 120 */       return String.valueOf(Calendar.getInstance().get(12));
/* 121 */     if (key.equals("TIME_SEC"))
/* 122 */       return String.valueOf(Calendar.getInstance().get(13));
/* 123 */     if (key.equals("TIME_WDAY"))
/* 124 */       return String.valueOf(Calendar.getInstance().get(7));
/* 125 */     if (key.equals("TIME")) {
/* 126 */       return FastHttpDateFormat.getCurrentDate();
/*     */     }
/* 128 */     return null;
/*     */   }
/*     */   
/*     */   public String resolveEnv(String key)
/*     */   {
/* 133 */     Object result = this.request.getAttribute(key);
/* 134 */     return result != null ? result.toString() : System.getProperty(key);
/*     */   }
/*     */   
/*     */ 
/*     */   public String resolveSsl(String key)
/*     */   {
/* 140 */     return null;
/*     */   }
/*     */   
/*     */   public String resolveHttp(String key)
/*     */   {
/* 145 */     String header = this.request.getHeader(key);
/* 146 */     if (header == null) {
/* 147 */       return "";
/*     */     }
/* 149 */     return header;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean resolveResource(int type, String name)
/*     */   {
/* 155 */     WebResourceRoot resources = this.request.getContext().getResources();
/* 156 */     WebResource resource = resources.getResource(name);
/* 157 */     if (!resource.exists()) {
/* 158 */       return false;
/*     */     }
/* 160 */     switch (type) {
/*     */     case 0: 
/* 162 */       return resource.isDirectory();
/*     */     case 1: 
/* 164 */       return resource.isFile();
/*     */     case 2: 
/* 166 */       return (resource.isFile()) && (resource.getContentLength() > 0L);
/*     */     }
/* 168 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   private static final String emptyStringIfNull(String value)
/*     */   {
/* 174 */     if (value == null) {
/* 175 */       return "";
/*     */     }
/* 177 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   public String getUriEncoding()
/*     */   {
/* 184 */     return this.request.getConnector().getURIEncoding();
/*     */   }
/*     */   
/*     */   public Charset getUriCharset()
/*     */   {
/* 189 */     return this.request.getConnector().getURICharset();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\valves\rewrite\ResolverImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */