/*     */ package org.apache.tomcat.util.descriptor.web;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import javax.servlet.SessionTrackingMode;
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
/*     */ public class SessionConfig
/*     */ {
/*     */   private Integer sessionTimeout;
/*     */   private String cookieName;
/*     */   private String cookieDomain;
/*     */   private String cookiePath;
/*     */   private String cookieComment;
/*     */   private Boolean cookieHttpOnly;
/*     */   private Boolean cookieSecure;
/*     */   private Integer cookieMaxAge;
/*  39 */   private final EnumSet<SessionTrackingMode> sessionTrackingModes = EnumSet.noneOf(SessionTrackingMode.class);
/*     */   
/*     */   public Integer getSessionTimeout() {
/*  42 */     return this.sessionTimeout;
/*     */   }
/*     */   
/*  45 */   public void setSessionTimeout(String sessionTimeout) { this.sessionTimeout = Integer.valueOf(sessionTimeout); }
/*     */   
/*     */   public String getCookieName()
/*     */   {
/*  49 */     return this.cookieName;
/*     */   }
/*     */   
/*  52 */   public void setCookieName(String cookieName) { this.cookieName = cookieName; }
/*     */   
/*     */   public String getCookieDomain()
/*     */   {
/*  56 */     return this.cookieDomain;
/*     */   }
/*     */   
/*  59 */   public void setCookieDomain(String cookieDomain) { this.cookieDomain = cookieDomain; }
/*     */   
/*     */   public String getCookiePath()
/*     */   {
/*  63 */     return this.cookiePath;
/*     */   }
/*     */   
/*  66 */   public void setCookiePath(String cookiePath) { this.cookiePath = cookiePath; }
/*     */   
/*     */   public String getCookieComment()
/*     */   {
/*  70 */     return this.cookieComment;
/*     */   }
/*     */   
/*  73 */   public void setCookieComment(String cookieComment) { this.cookieComment = cookieComment; }
/*     */   
/*     */   public Boolean getCookieHttpOnly()
/*     */   {
/*  77 */     return this.cookieHttpOnly;
/*     */   }
/*     */   
/*  80 */   public void setCookieHttpOnly(String cookieHttpOnly) { this.cookieHttpOnly = Boolean.valueOf(cookieHttpOnly); }
/*     */   
/*     */   public Boolean getCookieSecure()
/*     */   {
/*  84 */     return this.cookieSecure;
/*     */   }
/*     */   
/*  87 */   public void setCookieSecure(String cookieSecure) { this.cookieSecure = Boolean.valueOf(cookieSecure); }
/*     */   
/*     */   public Integer getCookieMaxAge()
/*     */   {
/*  91 */     return this.cookieMaxAge;
/*     */   }
/*     */   
/*  94 */   public void setCookieMaxAge(String cookieMaxAge) { this.cookieMaxAge = Integer.valueOf(cookieMaxAge); }
/*     */   
/*     */ 
/*     */ 
/*  98 */   public EnumSet<SessionTrackingMode> getSessionTrackingModes() { return this.sessionTrackingModes; }
/*     */   
/*     */   public void addSessionTrackingMode(String sessionTrackingMode) {
/* 101 */     this.sessionTrackingModes.add(
/* 102 */       SessionTrackingMode.valueOf(sessionTrackingMode));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\SessionConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */