/*     */ package org.apache.catalina.authenticator;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.Principal;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.SessionCookieConfig;
/*     */ import javax.servlet.http.Cookie;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.Engine;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.Manager;
/*     */ import org.apache.catalina.Realm;
/*     */ import org.apache.catalina.Session;
/*     */ import org.apache.catalina.SessionListener;
/*     */ import org.apache.catalina.Valve;
/*     */ import org.apache.catalina.connector.Request;
/*     */ import org.apache.catalina.connector.Response;
/*     */ import org.apache.catalina.valves.ValveBase;
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
/*     */ public class SingleSignOn
/*     */   extends ValveBase
/*     */ {
/*  62 */   private static final StringManager sm = StringManager.getManager(SingleSignOn.class);
/*     */   
/*     */ 
/*     */ 
/*     */   private Engine engine;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SingleSignOn()
/*     */   {
/*  73 */     super(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  83 */   protected Map<String, SingleSignOnEntry> cache = new ConcurrentHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  90 */   private boolean requireReauthentication = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String cookieDomain;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCookieDomain()
/*     */   {
/* 107 */     return this.cookieDomain;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCookieDomain(String cookieDomain)
/*     */   {
/* 117 */     if ((cookieDomain != null) && (cookieDomain.trim().length() == 0)) {
/* 118 */       this.cookieDomain = null;
/*     */     } else {
/* 120 */       this.cookieDomain = cookieDomain;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getRequireReauthentication()
/*     */   {
/* 143 */     return this.requireReauthentication;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRequireReauthentication(boolean required)
/*     */   {
/* 188 */     this.requireReauthentication = required;
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
/*     */   public void invoke(Request request, Response response)
/*     */     throws IOException, ServletException
/*     */   {
/* 207 */     request.removeNote("org.apache.catalina.request.SSOID");
/*     */     
/*     */ 
/* 210 */     if (this.containerLog.isDebugEnabled()) {
/* 211 */       this.containerLog.debug(sm.getString("singleSignOn.debug.invoke", new Object[] { request.getRequestURI() }));
/*     */     }
/* 213 */     if (request.getUserPrincipal() != null) {
/* 214 */       if (this.containerLog.isDebugEnabled()) {
/* 215 */         this.containerLog.debug(sm.getString("singleSignOn.debug.hasPrincipal", new Object[] {request
/* 216 */           .getUserPrincipal().getName() }));
/*     */       }
/* 218 */       getNext().invoke(request, response);
/* 219 */       return;
/*     */     }
/*     */     
/*     */ 
/* 223 */     if (this.containerLog.isDebugEnabled()) {
/* 224 */       this.containerLog.debug(sm.getString("singleSignOn.debug.cookieCheck"));
/*     */     }
/* 226 */     Cookie cookie = null;
/* 227 */     Cookie[] cookies = request.getCookies();
/* 228 */     if (cookies != null) {
/* 229 */       for (int i = 0; i < cookies.length; i++) {
/* 230 */         if (Constants.SINGLE_SIGN_ON_COOKIE.equals(cookies[i].getName())) {
/* 231 */           cookie = cookies[i];
/* 232 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 236 */     if (cookie == null) {
/* 237 */       if (this.containerLog.isDebugEnabled()) {
/* 238 */         this.containerLog.debug(sm.getString("singleSignOn.debug.cookieNotFound"));
/*     */       }
/* 240 */       getNext().invoke(request, response);
/* 241 */       return;
/*     */     }
/*     */     
/*     */ 
/* 245 */     if (this.containerLog.isDebugEnabled()) {
/* 246 */       this.containerLog.debug(sm.getString("singleSignOn.debug.principalCheck", new Object[] {cookie
/* 247 */         .getValue() }));
/*     */     }
/* 249 */     SingleSignOnEntry entry = (SingleSignOnEntry)this.cache.get(cookie.getValue());
/* 250 */     if (entry != null) {
/* 251 */       if (this.containerLog.isDebugEnabled()) {
/* 252 */         this.containerLog.debug(sm.getString("singleSignOn.debug.principalFound", new Object[] {entry
/* 253 */           .getPrincipal() != null ? entry.getPrincipal().getName() : "", entry
/* 254 */           .getAuthType() }));
/*     */       }
/* 256 */       request.setNote("org.apache.catalina.request.SSOID", cookie.getValue());
/*     */       
/* 258 */       if (!getRequireReauthentication()) {
/* 259 */         request.setAuthType(entry.getAuthType());
/* 260 */         request.setUserPrincipal(entry.getPrincipal());
/*     */       }
/*     */     } else {
/* 263 */       if (this.containerLog.isDebugEnabled()) {
/* 264 */         this.containerLog.debug(sm.getString("singleSignOn.debug.principalNotFound", new Object[] {cookie
/* 265 */           .getValue() }));
/*     */       }
/*     */       
/* 268 */       cookie.setValue("REMOVE");
/*     */       
/* 270 */       cookie.setMaxAge(0);
/*     */       
/*     */ 
/* 273 */       cookie.setPath("/");
/* 274 */       String domain = getCookieDomain();
/* 275 */       if (domain != null) {
/* 276 */         cookie.setDomain(domain);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 281 */       cookie.setSecure(request.isSecure());
/* 282 */       if ((request.getServletContext().getSessionCookieConfig().isHttpOnly()) || 
/* 283 */         (request.getContext().getUseHttpOnly())) {
/* 284 */         cookie.setHttpOnly(true);
/*     */       }
/*     */       
/* 287 */       response.addCookie(cookie);
/*     */     }
/*     */     
/*     */ 
/* 291 */     getNext().invoke(request, response);
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
/*     */   public void sessionDestroyed(String ssoId, Session session)
/*     */   {
/* 308 */     if (!getState().isAvailable()) {
/* 309 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 316 */     if (((session.getMaxInactiveInterval() > 0) && 
/* 317 */       (session.getIdleTimeInternal() >= session.getMaxInactiveInterval() * 1000)) || 
/* 318 */       (!session.getManager().getContext().getState().isAvailable())) {
/* 319 */       if (this.containerLog.isDebugEnabled()) {
/* 320 */         this.containerLog.debug(sm.getString("singleSignOn.debug.sessionTimeout", new Object[] { ssoId, session }));
/*     */       }
/*     */       
/* 323 */       removeSession(ssoId, session);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 328 */       if (this.containerLog.isDebugEnabled()) {
/* 329 */         this.containerLog.debug(sm.getString("singleSignOn.debug.sessionLogout", new Object[] { ssoId, session }));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 336 */       removeSession(ssoId, session);
/*     */       
/*     */ 
/* 339 */       if (this.cache.containsKey(ssoId)) {
/* 340 */         deregister(ssoId);
/*     */       }
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
/*     */   protected boolean associate(String ssoId, Session session)
/*     */   {
/* 357 */     SingleSignOnEntry sso = (SingleSignOnEntry)this.cache.get(ssoId);
/* 358 */     if (sso == null) {
/* 359 */       if (this.containerLog.isDebugEnabled()) {
/* 360 */         this.containerLog.debug(sm.getString("singleSignOn.debug.associateFail", new Object[] { ssoId, session }));
/*     */       }
/*     */       
/* 363 */       return false;
/*     */     }
/* 365 */     if (this.containerLog.isDebugEnabled()) {
/* 366 */       this.containerLog.debug(sm.getString("singleSignOn.debug.associate", new Object[] { ssoId, session }));
/*     */     }
/*     */     
/* 369 */     sso.addSession(this, ssoId, session);
/* 370 */     return true;
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
/*     */   protected void deregister(String ssoId)
/*     */   {
/* 384 */     SingleSignOnEntry sso = (SingleSignOnEntry)this.cache.remove(ssoId);
/*     */     
/* 386 */     if (sso == null) {
/* 387 */       if (this.containerLog.isDebugEnabled()) {
/* 388 */         this.containerLog.debug(sm.getString("singleSignOn.debug.deregisterFail", new Object[] { ssoId }));
/*     */       }
/* 390 */       return;
/*     */     }
/*     */     
/*     */ 
/* 394 */     Set<SingleSignOnSessionKey> ssoKeys = sso.findSessions();
/* 395 */     if ((ssoKeys.size() == 0) && 
/* 396 */       (this.containerLog.isDebugEnabled())) {
/* 397 */       this.containerLog.debug(sm.getString("singleSignOn.debug.deregisterNone", new Object[] { ssoId }));
/*     */     }
/*     */     
/* 400 */     for (SingleSignOnSessionKey ssoKey : ssoKeys) {
/* 401 */       if (this.containerLog.isDebugEnabled()) {
/* 402 */         this.containerLog.debug(sm.getString("singleSignOn.debug.deregister", new Object[] { ssoKey, ssoId }));
/*     */       }
/*     */       
/* 405 */       expire(ssoKey);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void expire(SingleSignOnSessionKey key)
/*     */   {
/* 415 */     if (this.engine == null) {
/* 416 */       this.containerLog.warn(sm.getString("singleSignOn.sessionExpire.engineNull", new Object[] { key }));
/* 417 */       return;
/*     */     }
/* 419 */     Container host = this.engine.findChild(key.getHostName());
/* 420 */     if (host == null) {
/* 421 */       this.containerLog.warn(sm.getString("singleSignOn.sessionExpire.hostNotFound", new Object[] { key }));
/* 422 */       return;
/*     */     }
/* 424 */     Context context = (Context)host.findChild(key.getContextName());
/* 425 */     if (context == null) {
/* 426 */       this.containerLog.warn(sm.getString("singleSignOn.sessionExpire.contextNotFound", new Object[] { key }));
/* 427 */       return;
/*     */     }
/* 429 */     Manager manager = context.getManager();
/* 430 */     if (manager == null) {
/* 431 */       this.containerLog.warn(sm.getString("singleSignOn.sessionExpire.managerNotFound", new Object[] { key }));
/* 432 */       return;
/*     */     }
/* 434 */     Session session = null;
/*     */     try {
/* 436 */       session = manager.findSession(key.getSessionId());
/*     */     } catch (IOException e) {
/* 438 */       this.containerLog.warn(sm.getString("singleSignOn.sessionExpire.managerError", new Object[] { key }), e);
/* 439 */       return;
/*     */     }
/* 441 */     if (session == null) {
/* 442 */       this.containerLog.warn(sm.getString("singleSignOn.sessionExpire.sessionNotFound", new Object[] { key }));
/* 443 */       return;
/*     */     }
/* 445 */     session.expire();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean reauthenticate(String ssoId, Realm realm, Request request)
/*     */   {
/* 473 */     if ((ssoId == null) || (realm == null)) {
/* 474 */       return false;
/*     */     }
/*     */     
/* 477 */     boolean reauthenticated = false;
/*     */     
/* 479 */     SingleSignOnEntry entry = (SingleSignOnEntry)this.cache.get(ssoId);
/* 480 */     if ((entry != null) && (entry.getCanReauthenticate()))
/*     */     {
/* 482 */       String username = entry.getUsername();
/* 483 */       if (username != null)
/*     */       {
/* 485 */         Principal reauthPrincipal = realm.authenticate(username, entry.getPassword());
/* 486 */         if (reauthPrincipal != null) {
/* 487 */           reauthenticated = true;
/*     */           
/* 489 */           request.setAuthType(entry.getAuthType());
/* 490 */           request.setUserPrincipal(reauthPrincipal);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 495 */     return reauthenticated;
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
/*     */   protected void register(String ssoId, Principal principal, String authType, String username, String password)
/*     */   {
/* 513 */     if (this.containerLog.isDebugEnabled()) {
/* 514 */       this.containerLog.debug(sm.getString("singleSignOn.debug.register", new Object[] { ssoId, principal != null ? principal
/* 515 */         .getName() : "", authType }));
/*     */     }
/*     */     
/* 518 */     this.cache.put(ssoId, new SingleSignOnEntry(principal, authType, username, password));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean update(String ssoId, Principal principal, String authType, String username, String password)
/*     */   {
/* 553 */     SingleSignOnEntry sso = (SingleSignOnEntry)this.cache.get(ssoId);
/* 554 */     if ((sso != null) && (!sso.getCanReauthenticate())) {
/* 555 */       if (this.containerLog.isDebugEnabled()) {
/* 556 */         this.containerLog.debug(sm.getString("singleSignOn.debug.update", new Object[] { ssoId, authType }));
/*     */       }
/*     */       
/* 559 */       sso.updateCredentials(principal, authType, username, password);
/* 560 */       return true;
/*     */     }
/* 562 */     return false;
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
/*     */   protected void removeSession(String ssoId, Session session)
/*     */   {
/* 575 */     if (this.containerLog.isDebugEnabled()) {
/* 576 */       this.containerLog.debug(sm.getString("singleSignOn.debug.removeSession", new Object[] { session, ssoId }));
/*     */     }
/*     */     
/*     */ 
/* 580 */     SingleSignOnEntry entry = (SingleSignOnEntry)this.cache.get(ssoId);
/* 581 */     if (entry == null) {
/* 582 */       return;
/*     */     }
/*     */     
/*     */ 
/* 586 */     entry.removeSession(session);
/*     */     
/*     */ 
/*     */ 
/* 590 */     if (entry.findSessions().size() == 0) {
/* 591 */       deregister(ssoId);
/*     */     }
/*     */   }
/*     */   
/*     */   protected SessionListener getSessionListener(String ssoId)
/*     */   {
/* 597 */     return new SingleSignOnListener(ssoId);
/*     */   }
/*     */   
/*     */   protected synchronized void startInternal()
/*     */     throws LifecycleException
/*     */   {
/* 603 */     Container c = getContainer();
/* 604 */     while ((c != null) && (!(c instanceof Engine))) {
/* 605 */       c = c.getParent();
/*     */     }
/* 607 */     if ((c instanceof Engine)) {
/* 608 */       this.engine = ((Engine)c);
/*     */     }
/* 610 */     super.startInternal();
/*     */   }
/*     */   
/*     */   protected synchronized void stopInternal()
/*     */     throws LifecycleException
/*     */   {
/* 616 */     super.stopInternal();
/* 617 */     this.engine = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\authenticator\SingleSignOn.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */