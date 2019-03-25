/*     */ package org.apache.catalina.authenticator;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.security.Principal;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.security.auth.Subject;
/*     */ import javax.security.auth.login.LoginContext;
/*     */ import javax.security.auth.login.LoginException;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.Realm;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.buf.ByteChunk;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
/*     */ import org.apache.tomcat.util.codec.binary.Base64;
/*     */ import org.apache.tomcat.util.compat.JreVendor;
/*     */ import org.apache.tomcat.util.http.MimeHeaders;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ import org.ietf.jgss.GSSContext;
/*     */ import org.ietf.jgss.GSSCredential;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.GSSManager;
/*     */ import org.ietf.jgss.Oid;
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
/*     */ public class SpnegoAuthenticator
/*     */   extends AuthenticatorBase
/*     */ {
/*  58 */   private static final Log log = LogFactory.getLog(SpnegoAuthenticator.class);
/*     */   
/*     */   private static final String AUTH_HEADER_VALUE_NEGOTIATE = "Negotiate";
/*  61 */   private String loginConfigName = "com.sun.security.jgss.krb5.accept";
/*     */   
/*  63 */   public String getLoginConfigName() { return this.loginConfigName; }
/*     */   
/*     */   public void setLoginConfigName(String loginConfigName) {
/*  66 */     this.loginConfigName = loginConfigName;
/*     */   }
/*     */   
/*  69 */   private boolean storeDelegatedCredential = true;
/*     */   
/*  71 */   public boolean isStoreDelegatedCredential() { return this.storeDelegatedCredential; }
/*     */   
/*     */   public void setStoreDelegatedCredential(boolean storeDelegatedCredential)
/*     */   {
/*  75 */     this.storeDelegatedCredential = storeDelegatedCredential;
/*     */   }
/*     */   
/*  78 */   private Pattern noKeepAliveUserAgents = null;
/*     */   
/*  80 */   public String getNoKeepAliveUserAgents() { Pattern p = this.noKeepAliveUserAgents;
/*  81 */     if (p == null) {
/*  82 */       return null;
/*     */     }
/*  84 */     return p.pattern();
/*     */   }
/*     */   
/*     */   public void setNoKeepAliveUserAgents(String noKeepAliveUserAgents) {
/*  88 */     if ((noKeepAliveUserAgents == null) || 
/*  89 */       (noKeepAliveUserAgents.length() == 0)) {
/*  90 */       this.noKeepAliveUserAgents = null;
/*     */     } else {
/*  92 */       this.noKeepAliveUserAgents = Pattern.compile(noKeepAliveUserAgents);
/*     */     }
/*     */   }
/*     */   
/*  96 */   private boolean applyJava8u40Fix = true;
/*     */   
/*  98 */   public boolean getApplyJava8u40Fix() { return this.applyJava8u40Fix; }
/*     */   
/*     */   public void setApplyJava8u40Fix(boolean applyJava8u40Fix) {
/* 101 */     this.applyJava8u40Fix = applyJava8u40Fix;
/*     */   }
/*     */   
/*     */ 
/*     */   protected String getAuthMethod()
/*     */   {
/* 107 */     return "SPNEGO";
/*     */   }
/*     */   
/*     */   protected void initInternal()
/*     */     throws LifecycleException
/*     */   {
/* 113 */     super.initInternal();
/*     */     
/*     */ 
/* 116 */     String krb5Conf = System.getProperty("java.security.krb5.conf");
/* 117 */     if (krb5Conf == null)
/*     */     {
/* 119 */       File krb5ConfFile = new File(this.container.getCatalinaBase(), "conf/krb5.ini");
/*     */       
/* 121 */       System.setProperty("java.security.krb5.conf", krb5ConfFile
/* 122 */         .getAbsolutePath());
/*     */     }
/*     */     
/*     */ 
/* 126 */     String jaasConf = System.getProperty("java.security.auth.login.config");
/* 127 */     if (jaasConf == null)
/*     */     {
/* 129 */       File jaasConfFile = new File(this.container.getCatalinaBase(), "conf/jaas.conf");
/*     */       
/* 131 */       System.setProperty("java.security.auth.login.config", jaasConfFile
/* 132 */         .getAbsolutePath());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean doAuthenticate(org.apache.catalina.connector.Request request, HttpServletResponse response)
/*     */     throws IOException
/*     */   {
/* 141 */     if (checkForCachedAuthentication(request, response, true)) {
/* 142 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 147 */     MessageBytes authorization = request.getCoyoteRequest().getMimeHeaders().getValue("authorization");
/*     */     
/* 149 */     if (authorization == null) {
/* 150 */       if (log.isDebugEnabled()) {
/* 151 */         log.debug(sm.getString("authenticator.noAuthHeader"));
/*     */       }
/* 153 */       response.setHeader("WWW-Authenticate", "Negotiate");
/* 154 */       response.sendError(401);
/* 155 */       return false;
/*     */     }
/*     */     
/* 158 */     authorization.toBytes();
/* 159 */     ByteChunk authorizationBC = authorization.getByteChunk();
/*     */     
/* 161 */     if (!authorizationBC.startsWithIgnoreCase("negotiate ", 0)) {
/* 162 */       if (log.isDebugEnabled()) {
/* 163 */         log.debug(sm.getString("spnegoAuthenticator.authHeaderNotNego"));
/*     */       }
/*     */       
/* 166 */       response.setHeader("WWW-Authenticate", "Negotiate");
/* 167 */       response.sendError(401);
/* 168 */       return false;
/*     */     }
/*     */     
/* 171 */     authorizationBC.setOffset(authorizationBC.getOffset() + 10);
/*     */     
/* 173 */     byte[] decoded = Base64.decodeBase64(authorizationBC.getBuffer(), authorizationBC
/* 174 */       .getOffset(), authorizationBC
/* 175 */       .getLength());
/*     */     
/* 177 */     if (getApplyJava8u40Fix()) {
/* 178 */       SpnegoTokenFixer.fix(decoded);
/*     */     }
/*     */     
/* 181 */     if (decoded.length == 0) {
/* 182 */       if (log.isDebugEnabled()) {
/* 183 */         log.debug(sm.getString("spnegoAuthenticator.authHeaderNoToken"));
/*     */       }
/*     */       
/* 186 */       response.setHeader("WWW-Authenticate", "Negotiate");
/* 187 */       response.sendError(401);
/* 188 */       return false;
/*     */     }
/*     */     
/* 191 */     LoginContext lc = null;
/* 192 */     GSSContext gssContext = null;
/* 193 */     byte[] outToken = null;
/* 194 */     Principal principal = null;
/*     */     try {
/*     */       try {
/* 197 */         lc = new LoginContext(getLoginConfigName());
/* 198 */         lc.login();
/*     */       } catch (LoginException e) {
/* 200 */         log.error(sm.getString("spnegoAuthenticator.serviceLoginFail"), e);
/*     */         
/* 202 */         response.sendError(500);
/*     */         
/* 204 */         return false;
/*     */       }
/*     */       
/* 207 */       Subject subject = lc.getSubject();
/*     */       
/*     */ 
/*     */ 
/* 211 */       final GSSManager manager = GSSManager.getInstance();
/*     */       int credentialLifetime;
/*     */       final int credentialLifetime;
/* 214 */       if (JreVendor.IS_IBM_JVM) {
/* 215 */         credentialLifetime = Integer.MAX_VALUE;
/*     */       } else {
/* 217 */         credentialLifetime = 0;
/*     */       }
/* 219 */       PrivilegedExceptionAction<GSSCredential> action = new PrivilegedExceptionAction()
/*     */       {
/*     */         public GSSCredential run() throws GSSException
/*     */         {
/* 223 */           return manager.createCredential(null, credentialLifetime, new Oid("1.3.6.1.5.5.2"), 2);
/*     */ 
/*     */         }
/*     */         
/*     */ 
/* 228 */       };
/* 229 */       gssContext = manager.createContext((GSSCredential)Subject.doAs(subject, action));
/*     */       
/* 231 */       outToken = (byte[])Subject.doAs(lc.getSubject(), new AcceptAction(gssContext, decoded));
/*     */       
/* 233 */       if (outToken == null) {
/* 234 */         if (log.isDebugEnabled()) {
/* 235 */           log.debug(sm.getString("spnegoAuthenticator.ticketValidateFail"));
/*     */         }
/*     */         
/*     */ 
/* 239 */         response.setHeader("WWW-Authenticate", "Negotiate");
/* 240 */         response.sendError(401);
/* 241 */         return false;
/*     */       }
/*     */       
/* 244 */       principal = (Principal)Subject.doAs(subject, new AuthenticateAction(this.context
/* 245 */         .getRealm(), gssContext, this.storeDelegatedCredential));
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
/* 267 */       if (gssContext != null) {
/*     */         try {
/* 269 */           gssContext.dispose();
/*     */         }
/*     */         catch (GSSException localGSSException3) {}
/*     */       }
/*     */       
/* 274 */       if (lc != null) {
/*     */         try {
/* 276 */           lc.logout();
/*     */         }
/*     */         catch (LoginException localLoginException3) {}
/*     */       }
/*     */       
/*     */ 
/*     */       Throwable cause;
/*     */       
/* 284 */       response.setHeader("WWW-Authenticate", "Negotiate " + 
/* 285 */         Base64.encodeBase64String(outToken));
/*     */     }
/*     */     catch (GSSException e)
/*     */     {
/* 248 */       if (log.isDebugEnabled()) {
/* 249 */         log.debug(sm.getString("spnegoAuthenticator.ticketValidateFail"), e);
/*     */       }
/* 251 */       response.setHeader("WWW-Authenticate", "Negotiate");
/* 252 */       response.sendError(401);
/* 253 */       return false;
/*     */     } catch (PrivilegedActionException e) {
/* 255 */       cause = e.getCause();
/* 256 */       if ((cause instanceof GSSException)) {
/* 257 */         if (log.isDebugEnabled()) {
/* 258 */           log.debug(sm.getString("spnegoAuthenticator.serviceLoginFail"), e);
/*     */         }
/*     */       } else {
/* 261 */         log.error(sm.getString("spnegoAuthenticator.serviceLoginFail"), e);
/*     */       }
/* 263 */       response.setHeader("WWW-Authenticate", "Negotiate");
/* 264 */       response.sendError(401);
/* 265 */       return 0;
/*     */     } finally {
/* 267 */       if (gssContext != null) {
/*     */         try {
/* 269 */           gssContext.dispose();
/*     */         }
/*     */         catch (GSSException localGSSException6) {}
/*     */       }
/*     */       
/* 274 */       if (lc != null) {
/*     */         try {
/* 276 */           lc.logout();
/*     */         }
/*     */         catch (LoginException localLoginException6) {}
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 287 */     if (principal != null) {
/* 288 */       register(request, response, principal, "SPNEGO", principal
/* 289 */         .getName(), null);
/*     */       
/* 291 */       Pattern p = this.noKeepAliveUserAgents;
/* 292 */       if (p != null)
/*     */       {
/* 294 */         MessageBytes ua = request.getCoyoteRequest().getMimeHeaders().getValue("user-agent");
/*     */         
/* 296 */         if ((ua != null) && (p.matcher(ua.toString()).matches())) {
/* 297 */           response.setHeader("Connection", "close");
/*     */         }
/*     */       }
/* 300 */       return true;
/*     */     }
/*     */     
/* 303 */     response.sendError(401);
/* 304 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public static class AcceptAction
/*     */     implements PrivilegedExceptionAction<byte[]>
/*     */   {
/*     */     GSSContext gssContext;
/*     */     
/*     */     byte[] decoded;
/*     */     
/*     */ 
/*     */     public AcceptAction(GSSContext context, byte[] decodedToken)
/*     */     {
/* 318 */       this.gssContext = context;
/* 319 */       this.decoded = decodedToken;
/*     */     }
/*     */     
/*     */     public byte[] run() throws GSSException
/*     */     {
/* 324 */       return this.gssContext.acceptSecContext(this.decoded, 0, this.decoded.length);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class AuthenticateAction
/*     */     implements PrivilegedAction<Principal>
/*     */   {
/*     */     private final Realm realm;
/*     */     private final GSSContext gssContext;
/*     */     private final boolean storeDelegatedCredential;
/*     */     
/*     */     public AuthenticateAction(Realm realm, GSSContext gssContext, boolean storeDelegatedCredential)
/*     */     {
/* 338 */       this.realm = realm;
/* 339 */       this.gssContext = gssContext;
/* 340 */       this.storeDelegatedCredential = storeDelegatedCredential;
/*     */     }
/*     */     
/*     */     public Principal run()
/*     */     {
/* 345 */       return this.realm.authenticate(this.gssContext, this.storeDelegatedCredential);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class SpnegoTokenFixer
/*     */   {
/*     */     private final byte[] token;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public static void fix(byte[] token)
/*     */     {
/* 366 */       SpnegoTokenFixer fixer = new SpnegoTokenFixer(token);
/* 367 */       fixer.fix();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 372 */     private int pos = 0;
/*     */     
/*     */     private SpnegoTokenFixer(byte[] token)
/*     */     {
/* 376 */       this.token = token;
/*     */     }
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
/*     */     private void fix()
/*     */     {
/* 391 */       if (!tag(96)) return;
/* 392 */       if (!length()) return;
/* 393 */       if (!oid("1.3.6.1.5.5.2")) return;
/* 394 */       if (!tag(160)) return;
/* 395 */       if (!length()) return;
/* 396 */       if (!tag(48)) return;
/* 397 */       if (!length()) return;
/* 398 */       if (!tag(160)) return;
/* 399 */       lengthAsInt();
/* 400 */       if (!tag(48)) { return;
/*     */       }
/*     */       
/* 403 */       int mechTypesLen = lengthAsInt();
/* 404 */       int mechTypesStart = this.pos;
/* 405 */       LinkedHashMap<String, int[]> mechTypeEntries = new LinkedHashMap();
/* 406 */       while (this.pos < mechTypesStart + mechTypesLen) {
/* 407 */         int[] value = new int[2];
/* 408 */         value[0] = this.pos;
/* 409 */         String key = oidAsString();
/* 410 */         value[1] = (this.pos - value[0]);
/* 411 */         mechTypeEntries.put(key, value);
/*     */       }
/*     */       
/* 414 */       byte[] replacement = new byte[mechTypesLen];
/* 415 */       int replacementPos = 0;
/*     */       
/* 417 */       int[] first = (int[])mechTypeEntries.remove("1.2.840.113554.1.2.2");
/* 418 */       if (first != null) {
/* 419 */         System.arraycopy(this.token, first[0], replacement, replacementPos, first[1]);
/* 420 */         replacementPos += first[1];
/*     */       }
/* 422 */       for (int[] markers : mechTypeEntries.values()) {
/* 423 */         System.arraycopy(this.token, markers[0], replacement, replacementPos, markers[1]);
/* 424 */         replacementPos += markers[1];
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 429 */       System.arraycopy(replacement, 0, this.token, mechTypesStart, mechTypesLen);
/*     */     }
/*     */     
/*     */     private boolean tag(int expected)
/*     */     {
/* 434 */       return (this.token[(this.pos++)] & 0xFF) == expected;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private boolean length()
/*     */     {
/* 441 */       int len = lengthAsInt();
/* 442 */       return this.pos + len == this.token.length;
/*     */     }
/*     */     
/*     */     private int lengthAsInt()
/*     */     {
/* 447 */       int len = this.token[(this.pos++)] & 0xFF;
/* 448 */       if (len > 127) {
/* 449 */         int bytes = len - 128;
/* 450 */         len = 0;
/* 451 */         for (int i = 0; i < bytes; i++) {
/* 452 */           len <<= 8;
/* 453 */           len += (this.token[(this.pos++)] & 0xFF);
/*     */         }
/*     */       }
/* 456 */       return len;
/*     */     }
/*     */     
/*     */     private boolean oid(String expected)
/*     */     {
/* 461 */       return expected.equals(oidAsString());
/*     */     }
/*     */     
/*     */     private String oidAsString()
/*     */     {
/* 466 */       if (!tag(6)) return null;
/* 467 */       StringBuilder result = new StringBuilder();
/* 468 */       int len = lengthAsInt();
/*     */       
/* 470 */       int v = this.token[(this.pos++)] & 0xFF;
/* 471 */       int c2 = v % 40;
/* 472 */       int c1 = (v - c2) / 40;
/* 473 */       result.append(c1);
/* 474 */       result.append('.');
/* 475 */       result.append(c2);
/* 476 */       int c = 0;
/* 477 */       boolean write = false;
/* 478 */       for (int i = 1; i < len; i++) {
/* 479 */         int b = this.token[(this.pos++)] & 0xFF;
/* 480 */         if (b > 127) {
/* 481 */           b -= 128;
/*     */         } else {
/* 483 */           write = true;
/*     */         }
/* 485 */         c <<= 7;
/* 486 */         c += b;
/* 487 */         if (write) {
/* 488 */           result.append('.');
/* 489 */           result.append(c);
/* 490 */           c = 0;
/* 491 */           write = false;
/*     */         }
/*     */       }
/* 494 */       return result.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\authenticator\SpnegoAuthenticator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */