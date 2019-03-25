/*     */ package org.apache.catalina.realm;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.security.Principal;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.security.auth.Subject;
/*     */ import javax.security.auth.callback.Callback;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import javax.security.auth.callback.NameCallback;
/*     */ import javax.security.auth.callback.PasswordCallback;
/*     */ import javax.security.auth.callback.TextInputCallback;
/*     */ import javax.security.auth.callback.UnsupportedCallbackException;
/*     */ import javax.security.auth.login.FailedLoginException;
/*     */ import javax.security.auth.login.LoginException;
/*     */ import javax.security.auth.spi.LoginModule;
/*     */ import org.apache.catalina.CredentialHandler;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.IntrospectionUtils;
/*     */ import org.apache.tomcat.util.digester.Digester;
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
/*     */ public class JAASMemoryLoginModule
/*     */   extends MemoryRealm
/*     */   implements LoginModule
/*     */ {
/*  79 */   private static final Log log = LogFactory.getLog(JAASMemoryLoginModule.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  87 */   protected CallbackHandler callbackHandler = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  93 */   protected boolean committed = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  99 */   protected Map<String, ?> options = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 105 */   protected String pathname = "conf/tomcat-users.xml";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 112 */   protected Principal principal = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 119 */   protected Map<String, ?> sharedState = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 125 */   protected Subject subject = null;
/*     */   
/*     */ 
/*     */ 
/*     */   public JAASMemoryLoginModule()
/*     */   {
/* 131 */     if (log.isDebugEnabled()) {
/* 132 */       log.debug("MEMORY LOGIN MODULE");
/*     */     }
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
/*     */   public boolean abort()
/*     */     throws LoginException
/*     */   {
/* 151 */     if (this.principal == null) {
/* 152 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 156 */     if (this.committed) {
/* 157 */       logout();
/*     */     } else {
/* 159 */       this.committed = false;
/* 160 */       this.principal = null;
/*     */     }
/* 162 */     if (log.isDebugEnabled()) {
/* 163 */       log.debug("Abort");
/*     */     }
/* 165 */     return true;
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
/*     */   public boolean commit()
/*     */     throws LoginException
/*     */   {
/* 182 */     if (log.isDebugEnabled()) {
/* 183 */       log.debug("commit " + this.principal);
/*     */     }
/*     */     
/*     */ 
/* 187 */     if (this.principal == null) {
/* 188 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 192 */     if (!this.subject.getPrincipals().contains(this.principal)) {
/* 193 */       this.subject.getPrincipals().add(this.principal);
/*     */       
/*     */ 
/* 196 */       if ((this.principal instanceof GenericPrincipal)) {
/* 197 */         String[] roles = ((GenericPrincipal)this.principal).getRoles();
/* 198 */         for (int i = 0; i < roles.length; i++) {
/* 199 */           this.subject.getPrincipals().add(new GenericPrincipal(roles[i], null, null));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 205 */     this.committed = true;
/* 206 */     return true;
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
/*     */   public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options)
/*     */   {
/* 225 */     if (log.isDebugEnabled()) {
/* 226 */       log.debug("Init");
/*     */     }
/*     */     
/*     */ 
/* 230 */     this.subject = subject;
/* 231 */     this.callbackHandler = callbackHandler;
/* 232 */     this.sharedState = sharedState;
/* 233 */     this.options = options;
/*     */     
/*     */ 
/* 236 */     Object option = options.get("pathname");
/* 237 */     if ((option instanceof String)) {
/* 238 */       this.pathname = ((String)option);
/*     */     }
/*     */     
/* 241 */     CredentialHandler credentialHandler = null;
/* 242 */     option = options.get("credentialHandlerClassName");
/* 243 */     if ((option instanceof String)) {
/*     */       try {
/* 245 */         Class<?> clazz = Class.forName((String)option);
/* 246 */         credentialHandler = (CredentialHandler)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/*     */       } catch (ReflectiveOperationException e) {
/* 248 */         throw new IllegalArgumentException(e);
/*     */       }
/*     */     }
/* 251 */     if (credentialHandler == null) {
/* 252 */       credentialHandler = new MessageDigestCredentialHandler();
/*     */     }
/*     */     
/* 255 */     for (Map.Entry<String, ?> entry : options.entrySet()) {
/* 256 */       if ((!"pathname".equals(entry.getKey())) && 
/*     */       
/*     */ 
/* 259 */         (!"credentialHandlerClassName".equals(entry.getKey())))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 264 */         if ((entry.getValue() instanceof String))
/* 265 */           IntrospectionUtils.setProperty(credentialHandler, (String)entry.getKey(), 
/* 266 */             (String)entry.getValue());
/*     */       }
/*     */     }
/* 269 */     setCredentialHandler(credentialHandler);
/*     */     
/*     */ 
/* 272 */     load();
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
/*     */   public boolean login()
/*     */     throws LoginException
/*     */   {
/* 288 */     if (this.callbackHandler == null)
/* 289 */       throw new LoginException("No CallbackHandler specified");
/* 290 */     Callback[] callbacks = new Callback[9];
/* 291 */     callbacks[0] = new NameCallback("Username: ");
/* 292 */     callbacks[1] = new PasswordCallback("Password: ", false);
/* 293 */     callbacks[2] = new TextInputCallback("nonce");
/* 294 */     callbacks[3] = new TextInputCallback("nc");
/* 295 */     callbacks[4] = new TextInputCallback("cnonce");
/* 296 */     callbacks[5] = new TextInputCallback("qop");
/* 297 */     callbacks[6] = new TextInputCallback("realmName");
/* 298 */     callbacks[7] = new TextInputCallback("md5a2");
/* 299 */     callbacks[8] = new TextInputCallback("authMethod");
/*     */     
/*     */ 
/* 302 */     String username = null;
/* 303 */     String password = null;
/* 304 */     String nonce = null;
/* 305 */     String nc = null;
/* 306 */     String cnonce = null;
/* 307 */     String qop = null;
/* 308 */     String realmName = null;
/* 309 */     String md5a2 = null;
/* 310 */     String authMethod = null;
/*     */     try
/*     */     {
/* 313 */       this.callbackHandler.handle(callbacks);
/* 314 */       username = ((NameCallback)callbacks[0]).getName();
/*     */       
/* 316 */       password = new String(((PasswordCallback)callbacks[1]).getPassword());
/* 317 */       nonce = ((TextInputCallback)callbacks[2]).getText();
/* 318 */       nc = ((TextInputCallback)callbacks[3]).getText();
/* 319 */       cnonce = ((TextInputCallback)callbacks[4]).getText();
/* 320 */       qop = ((TextInputCallback)callbacks[5]).getText();
/* 321 */       realmName = ((TextInputCallback)callbacks[6]).getText();
/* 322 */       md5a2 = ((TextInputCallback)callbacks[7]).getText();
/* 323 */       authMethod = ((TextInputCallback)callbacks[8]).getText();
/*     */     } catch (IOException|UnsupportedCallbackException e) {
/* 325 */       throw new LoginException(e.toString());
/*     */     }
/*     */     
/*     */ 
/* 329 */     if (authMethod == null)
/*     */     {
/* 331 */       this.principal = super.authenticate(username, password);
/* 332 */     } else if (authMethod.equals("DIGEST")) {
/* 333 */       this.principal = super.authenticate(username, password, nonce, nc, cnonce, qop, realmName, md5a2);
/*     */     }
/* 335 */     else if (authMethod.equals("CLIENT_CERT")) {
/* 336 */       this.principal = super.getPrincipal(username);
/*     */     } else {
/* 338 */       throw new LoginException("Unknown authentication method");
/*     */     }
/*     */     
/* 341 */     if (log.isDebugEnabled()) {
/* 342 */       log.debug("login " + username + " " + this.principal);
/*     */     }
/*     */     
/*     */ 
/* 346 */     if (this.principal != null) {
/* 347 */       return true;
/*     */     }
/* 349 */     throw new FailedLoginException("Username or password is incorrect");
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
/*     */   public boolean logout()
/*     */     throws LoginException
/*     */   {
/* 364 */     this.subject.getPrincipals().remove(this.principal);
/* 365 */     this.committed = false;
/* 366 */     this.principal = null;
/* 367 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void load()
/*     */   {
/* 379 */     File file = new File(this.pathname);
/* 380 */     if (!file.isAbsolute()) {
/* 381 */       String catalinaBase = getCatalinaBase();
/* 382 */       if (catalinaBase == null) {
/* 383 */         log.warn("Unable to determine Catalina base to load file " + this.pathname);
/* 384 */         return;
/*     */       }
/* 386 */       file = new File(catalinaBase, this.pathname);
/*     */     }
/*     */     
/* 389 */     if (!file.canRead()) {
/* 390 */       log.warn("Cannot load configuration file " + file.getAbsolutePath());
/* 391 */       return;
/*     */     }
/*     */     
/*     */ 
/* 395 */     Digester digester = new Digester();
/* 396 */     digester.setValidating(false);
/* 397 */     digester.addRuleSet(new MemoryRuleSet());
/*     */     try {
/* 399 */       digester.push(this);
/* 400 */       digester.parse(file);
/*     */     } catch (Exception e) {
/* 402 */       log.warn("Error processing configuration file " + file.getAbsolutePath(), e);
/* 403 */       return;
/*     */     } finally {
/* 405 */       digester.reset();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String getCatalinaBase()
/*     */   {
/* 414 */     if (this.callbackHandler == null) {
/* 415 */       return null;
/*     */     }
/*     */     
/* 418 */     Callback[] callbacks = new Callback[1];
/* 419 */     callbacks[0] = new TextInputCallback("catalinaBase");
/*     */     
/* 421 */     String result = null;
/*     */     try
/*     */     {
/* 424 */       this.callbackHandler.handle(callbacks);
/* 425 */       result = ((TextInputCallback)callbacks[0]).getText();
/*     */     } catch (IOException|UnsupportedCallbackException e) {
/* 427 */       return null;
/*     */     }
/*     */     
/* 430 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\realm\JAASMemoryLoginModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */