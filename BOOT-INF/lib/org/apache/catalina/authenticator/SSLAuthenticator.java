/*     */ package org.apache.catalina.authenticator;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.Principal;
/*     */ import java.security.cert.X509Certificate;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.Realm;
/*     */ import org.apache.catalina.connector.Request;
/*     */ import org.apache.juli.logging.Log;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SSLAuthenticator
/*     */   extends AuthenticatorBase
/*     */ {
/*     */   protected boolean doAuthenticate(Request request, HttpServletResponse response)
/*     */     throws IOException
/*     */   {
/*  60 */     if (checkForCachedAuthentication(request, response, false)) {
/*  61 */       return true;
/*     */     }
/*     */     
/*     */ 
/*  65 */     if (this.containerLog.isDebugEnabled()) {
/*  66 */       this.containerLog.debug(" Looking up certificates");
/*     */     }
/*     */     
/*  69 */     X509Certificate[] certs = getRequestCertificates(request);
/*     */     
/*  71 */     if ((certs == null) || (certs.length < 1)) {
/*  72 */       if (this.containerLog.isDebugEnabled()) {
/*  73 */         this.containerLog.debug("  No certificates included with this request");
/*     */       }
/*  75 */       response.sendError(401, sm
/*  76 */         .getString("authenticator.certificates"));
/*  77 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  81 */     Principal principal = this.context.getRealm().authenticate(certs);
/*  82 */     if (principal == null) {
/*  83 */       if (this.containerLog.isDebugEnabled()) {
/*  84 */         this.containerLog.debug("  Realm.authenticate() returned false");
/*     */       }
/*  86 */       response.sendError(401, sm
/*  87 */         .getString("authenticator.unauthorized"));
/*  88 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  92 */     register(request, response, principal, "CLIENT_CERT", null, null);
/*     */     
/*  94 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getAuthMethod()
/*     */   {
/* 101 */     return "CLIENT_CERT";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\authenticator\SSLAuthenticator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */