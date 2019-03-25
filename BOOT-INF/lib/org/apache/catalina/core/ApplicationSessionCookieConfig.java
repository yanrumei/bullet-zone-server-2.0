/*     */ package org.apache.catalina.core;
/*     */ 
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.SessionCookieConfig;
/*     */ import javax.servlet.http.Cookie;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.util.SessionConfig;
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
/*     */ public class ApplicationSessionCookieConfig
/*     */   implements SessionCookieConfig
/*     */ {
/*  34 */   private static final StringManager sm = StringManager.getManager("org.apache.catalina.core");
/*     */   
/*     */   private boolean httpOnly;
/*     */   private boolean secure;
/*  38 */   private int maxAge = -1;
/*     */   private String comment;
/*     */   private String domain;
/*     */   private String name;
/*     */   private String path;
/*     */   private StandardContext context;
/*     */   
/*     */   public ApplicationSessionCookieConfig(StandardContext context) {
/*  46 */     this.context = context;
/*     */   }
/*     */   
/*     */   public String getComment()
/*     */   {
/*  51 */     return this.comment;
/*     */   }
/*     */   
/*     */   public String getDomain()
/*     */   {
/*  56 */     return this.domain;
/*     */   }
/*     */   
/*     */   public int getMaxAge()
/*     */   {
/*  61 */     return this.maxAge;
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/*  66 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getPath()
/*     */   {
/*  71 */     return this.path;
/*     */   }
/*     */   
/*     */   public boolean isHttpOnly()
/*     */   {
/*  76 */     return this.httpOnly;
/*     */   }
/*     */   
/*     */   public boolean isSecure()
/*     */   {
/*  81 */     return this.secure;
/*     */   }
/*     */   
/*     */   public void setComment(String comment)
/*     */   {
/*  86 */     if (!this.context.getState().equals(LifecycleState.STARTING_PREP)) {
/*  87 */       throw new IllegalStateException(sm.getString("applicationSessionCookieConfig.ise", new Object[] { "comment", this.context
/*     */       
/*  89 */         .getPath() }));
/*     */     }
/*  91 */     this.comment = comment;
/*     */   }
/*     */   
/*     */   public void setDomain(String domain)
/*     */   {
/*  96 */     if (!this.context.getState().equals(LifecycleState.STARTING_PREP)) {
/*  97 */       throw new IllegalStateException(sm.getString("applicationSessionCookieConfig.ise", new Object[] { "domain name", this.context
/*     */       
/*  99 */         .getPath() }));
/*     */     }
/* 101 */     this.domain = domain;
/*     */   }
/*     */   
/*     */   public void setHttpOnly(boolean httpOnly)
/*     */   {
/* 106 */     if (!this.context.getState().equals(LifecycleState.STARTING_PREP)) {
/* 107 */       throw new IllegalStateException(sm.getString("applicationSessionCookieConfig.ise", new Object[] { "HttpOnly", this.context
/*     */       
/* 109 */         .getPath() }));
/*     */     }
/* 111 */     this.httpOnly = httpOnly;
/*     */   }
/*     */   
/*     */   public void setMaxAge(int maxAge)
/*     */   {
/* 116 */     if (!this.context.getState().equals(LifecycleState.STARTING_PREP)) {
/* 117 */       throw new IllegalStateException(sm.getString("applicationSessionCookieConfig.ise", new Object[] { "max age", this.context
/*     */       
/* 119 */         .getPath() }));
/*     */     }
/* 121 */     this.maxAge = maxAge;
/*     */   }
/*     */   
/*     */   public void setName(String name)
/*     */   {
/* 126 */     if (!this.context.getState().equals(LifecycleState.STARTING_PREP)) {
/* 127 */       throw new IllegalStateException(sm.getString("applicationSessionCookieConfig.ise", new Object[] { "name", this.context
/*     */       
/* 129 */         .getPath() }));
/*     */     }
/* 131 */     this.name = name;
/*     */   }
/*     */   
/*     */   public void setPath(String path)
/*     */   {
/* 136 */     if (!this.context.getState().equals(LifecycleState.STARTING_PREP)) {
/* 137 */       throw new IllegalStateException(sm.getString("applicationSessionCookieConfig.ise", new Object[] { "path", this.context
/*     */       
/* 139 */         .getPath() }));
/*     */     }
/* 141 */     this.path = path;
/*     */   }
/*     */   
/*     */   public void setSecure(boolean secure)
/*     */   {
/* 146 */     if (!this.context.getState().equals(LifecycleState.STARTING_PREP)) {
/* 147 */       throw new IllegalStateException(sm.getString("applicationSessionCookieConfig.ise", new Object[] { "secure", this.context
/*     */       
/* 149 */         .getPath() }));
/*     */     }
/* 151 */     this.secure = secure;
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
/*     */   public static Cookie createSessionCookie(Context context, String sessionId, boolean secure)
/*     */   {
/* 167 */     SessionCookieConfig scc = context.getServletContext().getSessionCookieConfig();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 175 */     Cookie cookie = new Cookie(SessionConfig.getSessionCookieName(context), sessionId);
/*     */     
/*     */ 
/* 178 */     cookie.setMaxAge(scc.getMaxAge());
/* 179 */     cookie.setComment(scc.getComment());
/*     */     
/* 181 */     if (context.getSessionCookieDomain() == null)
/*     */     {
/* 183 */       if (scc.getDomain() != null) {
/* 184 */         cookie.setDomain(scc.getDomain());
/*     */       }
/*     */     } else {
/* 187 */       cookie.setDomain(context.getSessionCookieDomain());
/*     */     }
/*     */     
/*     */ 
/* 191 */     if ((scc.isSecure()) || (secure)) {
/* 192 */       cookie.setSecure(true);
/*     */     }
/*     */     
/*     */ 
/* 196 */     if ((scc.isHttpOnly()) || (context.getUseHttpOnly())) {
/* 197 */       cookie.setHttpOnly(true);
/*     */     }
/*     */     
/* 200 */     String contextPath = context.getSessionCookiePath();
/* 201 */     if ((contextPath == null) || (contextPath.length() == 0)) {
/* 202 */       contextPath = scc.getPath();
/*     */     }
/* 204 */     if ((contextPath == null) || (contextPath.length() == 0)) {
/* 205 */       contextPath = context.getEncodedPath();
/*     */     }
/* 207 */     if (context.getSessionCookiePathUsesTrailingSlash())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 212 */       if (!contextPath.endsWith("/")) {
/* 213 */         contextPath = contextPath + "/";
/*     */       }
/*     */       
/*     */ 
/*     */     }
/* 218 */     else if (contextPath.length() == 0) {
/* 219 */       contextPath = "/";
/*     */     }
/*     */     
/* 222 */     cookie.setPath(contextPath);
/*     */     
/* 224 */     return cookie;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\ApplicationSessionCookieConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */