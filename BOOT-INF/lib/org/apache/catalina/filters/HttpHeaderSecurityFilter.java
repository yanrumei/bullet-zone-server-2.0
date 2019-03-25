/*     */ package org.apache.catalina.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.FilterConfig;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletResponse;
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
/*     */ public class HttpHeaderSecurityFilter
/*     */   extends FilterBase
/*     */ {
/*  39 */   private static final Log log = LogFactory.getLog(HttpHeaderSecurityFilter.class);
/*     */   
/*     */   private static final String HSTS_HEADER_NAME = "Strict-Transport-Security";
/*     */   
/*  43 */   private boolean hstsEnabled = true;
/*  44 */   private int hstsMaxAgeSeconds = 0;
/*  45 */   private boolean hstsIncludeSubDomains = false;
/*  46 */   private boolean hstsPreload = false;
/*     */   
/*     */   private String hstsHeaderValue;
/*     */   
/*     */   private static final String ANTI_CLICK_JACKING_HEADER_NAME = "X-Frame-Options";
/*  51 */   private boolean antiClickJackingEnabled = true;
/*  52 */   private XFrameOption antiClickJackingOption = XFrameOption.DENY;
/*     */   
/*     */   private URI antiClickJackingUri;
/*     */   
/*     */   private String antiClickJackingHeaderValue;
/*     */   private static final String BLOCK_CONTENT_TYPE_SNIFFING_HEADER_NAME = "X-Content-Type-Options";
/*     */   private static final String BLOCK_CONTENT_TYPE_SNIFFING_HEADER_VALUE = "nosniff";
/*  59 */   private boolean blockContentTypeSniffingEnabled = true;
/*     */   
/*     */   private static final String XSS_PROTECTION_HEADER_NAME = "X-XSS-Protection";
/*     */   
/*     */   private static final String XSS_PROTECTION_HEADER_VALUE = "1; mode=block";
/*  64 */   private boolean xssProtectionEnabled = true;
/*     */   
/*     */   public void init(FilterConfig filterConfig) throws ServletException
/*     */   {
/*  68 */     super.init(filterConfig);
/*     */     
/*     */ 
/*  71 */     StringBuilder hstsValue = new StringBuilder("max-age=");
/*  72 */     hstsValue.append(this.hstsMaxAgeSeconds);
/*  73 */     if (this.hstsIncludeSubDomains) {
/*  74 */       hstsValue.append(";includeSubDomains");
/*     */     }
/*  76 */     if (this.hstsPreload) {
/*  77 */       hstsValue.append(";preload");
/*     */     }
/*  79 */     this.hstsHeaderValue = hstsValue.toString();
/*     */     
/*     */ 
/*  82 */     StringBuilder cjValue = new StringBuilder(this.antiClickJackingOption.headerValue);
/*  83 */     if (this.antiClickJackingOption == XFrameOption.ALLOW_FROM) {
/*  84 */       cjValue.append(' ');
/*  85 */       cjValue.append(this.antiClickJackingUri);
/*     */     }
/*  87 */     this.antiClickJackingHeaderValue = cjValue.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
/*     */     throws IOException, ServletException
/*     */   {
/*  95 */     if ((response instanceof HttpServletResponse)) {
/*  96 */       HttpServletResponse httpResponse = (HttpServletResponse)response;
/*     */       
/*  98 */       if (response.isCommitted()) {
/*  99 */         throw new ServletException(sm.getString("httpHeaderSecurityFilter.committed"));
/*     */       }
/*     */       
/*     */ 
/* 103 */       if ((this.hstsEnabled) && (request.isSecure())) {
/* 104 */         httpResponse.setHeader("Strict-Transport-Security", this.hstsHeaderValue);
/*     */       }
/*     */       
/*     */ 
/* 108 */       if (this.antiClickJackingEnabled) {
/* 109 */         httpResponse.setHeader("X-Frame-Options", this.antiClickJackingHeaderValue);
/*     */       }
/*     */       
/*     */ 
/* 113 */       if (this.blockContentTypeSniffingEnabled) {
/* 114 */         httpResponse.setHeader("X-Content-Type-Options", "nosniff");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 119 */       if (this.xssProtectionEnabled) {
/* 120 */         httpResponse.setHeader("X-XSS-Protection", "1; mode=block");
/*     */       }
/*     */     }
/*     */     
/* 124 */     chain.doFilter(request, response);
/*     */   }
/*     */   
/*     */ 
/*     */   protected Log getLogger()
/*     */   {
/* 130 */     return log;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isConfigProblemFatal()
/*     */   {
/* 138 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isHstsEnabled()
/*     */   {
/* 143 */     return this.hstsEnabled;
/*     */   }
/*     */   
/*     */   public void setHstsEnabled(boolean hstsEnabled)
/*     */   {
/* 148 */     this.hstsEnabled = hstsEnabled;
/*     */   }
/*     */   
/*     */   public int getHstsMaxAgeSeconds()
/*     */   {
/* 153 */     return this.hstsMaxAgeSeconds;
/*     */   }
/*     */   
/*     */   public void setHstsMaxAgeSeconds(int hstsMaxAgeSeconds)
/*     */   {
/* 158 */     if (hstsMaxAgeSeconds < 0) {
/* 159 */       this.hstsMaxAgeSeconds = 0;
/*     */     } else {
/* 161 */       this.hstsMaxAgeSeconds = hstsMaxAgeSeconds;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isHstsIncludeSubDomains()
/*     */   {
/* 167 */     return this.hstsIncludeSubDomains;
/*     */   }
/*     */   
/*     */   public void setHstsIncludeSubDomains(boolean hstsIncludeSubDomains)
/*     */   {
/* 172 */     this.hstsIncludeSubDomains = hstsIncludeSubDomains;
/*     */   }
/*     */   
/*     */   public boolean isHstsPreload()
/*     */   {
/* 177 */     return this.hstsPreload;
/*     */   }
/*     */   
/*     */   public void setHstsPreload(boolean hstsPreload)
/*     */   {
/* 182 */     this.hstsPreload = hstsPreload;
/*     */   }
/*     */   
/*     */   public boolean isAntiClickJackingEnabled()
/*     */   {
/* 187 */     return this.antiClickJackingEnabled;
/*     */   }
/*     */   
/*     */   public void setAntiClickJackingEnabled(boolean antiClickJackingEnabled)
/*     */   {
/* 192 */     this.antiClickJackingEnabled = antiClickJackingEnabled;
/*     */   }
/*     */   
/*     */   public String getAntiClickJackingOption()
/*     */   {
/* 197 */     return this.antiClickJackingOption.toString();
/*     */   }
/*     */   
/*     */   public void setAntiClickJackingOption(String antiClickJackingOption)
/*     */   {
/* 202 */     for (XFrameOption option : ) {
/* 203 */       if (option.getHeaderValue().equalsIgnoreCase(antiClickJackingOption)) {
/* 204 */         this.antiClickJackingOption = option;
/* 205 */         return;
/*     */       }
/*     */     }
/*     */     
/* 209 */     throw new IllegalArgumentException(sm.getString("httpHeaderSecurityFilter.clickjack.invalid", new Object[] { antiClickJackingOption }));
/*     */   }
/*     */   
/*     */   public String getAntiClickJackingUri()
/*     */   {
/* 214 */     return this.antiClickJackingUri.toString();
/*     */   }
/*     */   
/*     */   public boolean isBlockContentTypeSniffingEnabled()
/*     */   {
/* 219 */     return this.blockContentTypeSniffingEnabled;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setBlockContentTypeSniffingEnabled(boolean blockContentTypeSniffingEnabled)
/*     */   {
/* 225 */     this.blockContentTypeSniffingEnabled = blockContentTypeSniffingEnabled;
/*     */   }
/*     */   
/*     */   public void setAntiClickJackingUri(String antiClickJackingUri)
/*     */   {
/*     */     try
/*     */     {
/* 232 */       uri = new URI(antiClickJackingUri);
/*     */     } catch (URISyntaxException e) { URI uri;
/* 234 */       throw new IllegalArgumentException(e); }
/*     */     URI uri;
/* 236 */     this.antiClickJackingUri = uri;
/*     */   }
/*     */   
/*     */   public boolean isXssProtectionEnabled()
/*     */   {
/* 241 */     return this.xssProtectionEnabled;
/*     */   }
/*     */   
/*     */   public void setXssProtectionEnabled(boolean xssProtectionEnabled)
/*     */   {
/* 246 */     this.xssProtectionEnabled = xssProtectionEnabled;
/*     */   }
/*     */   
/*     */   private static enum XFrameOption
/*     */   {
/* 251 */     DENY("DENY"), 
/* 252 */     SAME_ORIGIN("SAMEORIGIN"), 
/* 253 */     ALLOW_FROM("ALLOW-FROM");
/*     */     
/*     */     private final String headerValue;
/*     */     
/*     */     private XFrameOption(String headerValue)
/*     */     {
/* 259 */       this.headerValue = headerValue;
/*     */     }
/*     */     
/*     */     public String getHeaderValue() {
/* 263 */       return this.headerValue;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\filters\HttpHeaderSecurityFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */