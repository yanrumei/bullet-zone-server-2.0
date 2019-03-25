/*     */ package org.springframework.web.servlet.support;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.http.HttpRequest;
/*     */ import org.springframework.http.server.ServletServerHttpRequest;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.context.request.RequestAttributes;
/*     */ import org.springframework.web.context.request.RequestContextHolder;
/*     */ import org.springframework.web.context.request.ServletRequestAttributes;
/*     */ import org.springframework.web.util.UriComponents;
/*     */ import org.springframework.web.util.UriComponentsBuilder;
/*     */ import org.springframework.web.util.UriUtils;
/*     */ import org.springframework.web.util.UrlPathHelper;
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
/*     */ public class ServletUriComponentsBuilder
/*     */   extends UriComponentsBuilder
/*     */ {
/*     */   private String originalPath;
/*     */   
/*     */   protected ServletUriComponentsBuilder() {}
/*     */   
/*     */   protected ServletUriComponentsBuilder(ServletUriComponentsBuilder other)
/*     */   {
/*  72 */     super(other);
/*  73 */     this.originalPath = other.originalPath;
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
/*     */   public static ServletUriComponentsBuilder fromContextPath(HttpServletRequest request)
/*     */   {
/*  87 */     ServletUriComponentsBuilder builder = initFromRequest(request);
/*  88 */     builder.replacePath(prependForwardedPrefix(request, request.getContextPath()));
/*  89 */     return builder;
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
/*     */   public static ServletUriComponentsBuilder fromServletMapping(HttpServletRequest request)
/*     */   {
/* 104 */     ServletUriComponentsBuilder builder = fromContextPath(request);
/* 105 */     if (StringUtils.hasText(new UrlPathHelper().getPathWithinServletMapping(request))) {
/* 106 */       builder.path(request.getServletPath());
/*     */     }
/* 108 */     return builder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ServletUriComponentsBuilder fromRequestUri(HttpServletRequest request)
/*     */   {
/* 119 */     ServletUriComponentsBuilder builder = initFromRequest(request);
/* 120 */     builder.initPath(prependForwardedPrefix(request, request.getRequestURI()));
/* 121 */     return builder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ServletUriComponentsBuilder fromRequest(HttpServletRequest request)
/*     */   {
/* 132 */     ServletUriComponentsBuilder builder = initFromRequest(request);
/* 133 */     builder.initPath(prependForwardedPrefix(request, request.getRequestURI()));
/* 134 */     builder.query(request.getQueryString());
/* 135 */     return builder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static ServletUriComponentsBuilder initFromRequest(HttpServletRequest request)
/*     */   {
/* 142 */     HttpRequest httpRequest = new ServletServerHttpRequest(request);
/* 143 */     UriComponents uriComponents = UriComponentsBuilder.fromHttpRequest(httpRequest).build();
/* 144 */     String scheme = uriComponents.getScheme();
/* 145 */     String host = uriComponents.getHost();
/* 146 */     int port = uriComponents.getPort();
/*     */     
/* 148 */     ServletUriComponentsBuilder builder = new ServletUriComponentsBuilder();
/* 149 */     builder.scheme(scheme);
/* 150 */     builder.host(host);
/* 151 */     if ((("http".equals(scheme)) && (port != 80)) || (("https".equals(scheme)) && (port != 443))) {
/* 152 */       builder.port(port);
/*     */     }
/* 154 */     return builder;
/*     */   }
/*     */   
/*     */   private static String prependForwardedPrefix(HttpServletRequest request, String path) {
/* 158 */     String prefix = null;
/* 159 */     Enumeration<String> names = request.getHeaderNames();
/* 160 */     while (names.hasMoreElements()) {
/* 161 */       String name = (String)names.nextElement();
/* 162 */       if ("X-Forwarded-Prefix".equalsIgnoreCase(name)) {
/* 163 */         prefix = request.getHeader(name);
/*     */       }
/*     */     }
/* 166 */     if (prefix != null) {
/* 167 */       path = prefix + path;
/*     */     }
/* 169 */     return path;
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
/*     */   public static ServletUriComponentsBuilder fromCurrentContextPath()
/*     */   {
/* 183 */     return fromContextPath(getCurrentRequest());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ServletUriComponentsBuilder fromCurrentServletMapping()
/*     */   {
/* 194 */     return fromServletMapping(getCurrentRequest());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ServletUriComponentsBuilder fromCurrentRequestUri()
/*     */   {
/* 205 */     return fromRequestUri(getCurrentRequest());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ServletUriComponentsBuilder fromCurrentRequest()
/*     */   {
/* 216 */     return fromRequest(getCurrentRequest());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected static HttpServletRequest getCurrentRequest()
/*     */   {
/* 223 */     RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
/* 224 */     Assert.state(attrs instanceof ServletRequestAttributes, "No current ServletRequestAttributes");
/* 225 */     return ((ServletRequestAttributes)attrs).getRequest();
/*     */   }
/*     */   
/*     */   private void initPath(String path)
/*     */   {
/* 230 */     this.originalPath = path;
/* 231 */     replacePath(path);
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
/*     */   public String removePathExtension()
/*     */   {
/* 250 */     String extension = null;
/* 251 */     if (this.originalPath != null) {
/* 252 */       extension = UriUtils.extractFileExtension(this.originalPath);
/* 253 */       if (!StringUtils.isEmpty(extension)) {
/* 254 */         int end = this.originalPath.length() - (extension.length() + 1);
/* 255 */         replacePath(this.originalPath.substring(0, end));
/*     */       }
/* 257 */       this.originalPath = null;
/*     */     }
/* 259 */     return extension;
/*     */   }
/*     */   
/*     */   public ServletUriComponentsBuilder cloneBuilder()
/*     */   {
/* 264 */     return new ServletUriComponentsBuilder(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\support\ServletUriComponentsBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */