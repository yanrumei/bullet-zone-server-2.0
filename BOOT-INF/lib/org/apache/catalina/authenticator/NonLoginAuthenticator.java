/*     */ package org.apache.catalina.authenticator;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.catalina.Session;
/*     */ import org.apache.catalina.connector.Request;
/*     */ import org.apache.juli.logging.Log;
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
/*     */ public final class NonLoginAuthenticator
/*     */   extends AuthenticatorBase
/*     */ {
/*     */   protected boolean doAuthenticate(Request request, HttpServletResponse response)
/*     */     throws IOException
/*     */   {
/*  82 */     if (checkForCachedAuthentication(request, response, true))
/*     */     {
/*     */ 
/*  85 */       if (this.cache) {
/*  86 */         request.getSessionInternal(true).setPrincipal(request.getUserPrincipal());
/*     */       }
/*  88 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  96 */     if (this.containerLog.isDebugEnabled())
/*  97 */       this.containerLog.debug("User authenticated without any roles");
/*  98 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getAuthMethod()
/*     */   {
/* 108 */     return "NONE";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\authenticator\NonLoginAuthenticator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */