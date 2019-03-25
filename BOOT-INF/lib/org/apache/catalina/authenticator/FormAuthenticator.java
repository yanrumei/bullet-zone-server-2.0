/*     */ package org.apache.catalina.authenticator;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.Principal;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import javax.servlet.RequestDispatcher;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.http.Cookie;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.Manager;
/*     */ import org.apache.catalina.Realm;
/*     */ import org.apache.catalina.Session;
/*     */ import org.apache.catalina.connector.Connector;
/*     */ import org.apache.catalina.connector.Response;
/*     */ import org.apache.coyote.ActionCode;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
/*     */ import org.apache.tomcat.util.buf.ByteChunk;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
/*     */ import org.apache.tomcat.util.descriptor.web.LoginConfig;
/*     */ import org.apache.tomcat.util.http.MimeHeaders;
/*     */ import org.apache.tomcat.util.http.Parameters;
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
/*     */ public class FormAuthenticator
/*     */   extends AuthenticatorBase
/*     */ {
/*  55 */   private static final Log log = LogFactory.getLog(FormAuthenticator.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  65 */   protected String characterEncoding = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  72 */   protected String landingPage = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCharacterEncoding()
/*     */   {
/*  83 */     return this.characterEncoding;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCharacterEncoding(String encoding)
/*     */   {
/*  93 */     this.characterEncoding = encoding;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLandingPage()
/*     */   {
/* 103 */     return this.landingPage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLandingPage(String landingPage)
/*     */   {
/* 114 */     this.landingPage = landingPage;
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
/*     */   protected boolean doAuthenticate(org.apache.catalina.connector.Request request, HttpServletResponse response)
/*     */     throws IOException
/*     */   {
/* 136 */     if (checkForCachedAuthentication(request, response, true)) {
/* 137 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 141 */     Session session = null;
/* 142 */     Principal principal = null;
/*     */     
/*     */ 
/* 145 */     if (!this.cache) {
/* 146 */       session = request.getSessionInternal(true);
/* 147 */       if (log.isDebugEnabled()) {
/* 148 */         log.debug("Checking for reauthenticate in session " + session);
/*     */       }
/*     */       
/* 151 */       String username = (String)session.getNote("org.apache.catalina.session.USERNAME");
/*     */       
/* 153 */       String password = (String)session.getNote("org.apache.catalina.session.PASSWORD");
/* 154 */       if ((username != null) && (password != null)) {
/* 155 */         if (log.isDebugEnabled()) {
/* 156 */           log.debug("Reauthenticating username '" + username + "'");
/*     */         }
/*     */         
/* 159 */         principal = this.context.getRealm().authenticate(username, password);
/* 160 */         if (principal != null) {
/* 161 */           session.setNote("org.apache.catalina.authenticator.PRINCIPAL", principal);
/* 162 */           if (!matchRequest(request)) {
/* 163 */             register(request, response, principal, "FORM", username, password);
/*     */             
/*     */ 
/* 166 */             return true;
/*     */           }
/*     */         }
/* 169 */         if (log.isDebugEnabled()) {
/* 170 */           log.debug("Reauthentication failed, proceed normally");
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 177 */     if (matchRequest(request)) {
/* 178 */       session = request.getSessionInternal(true);
/* 179 */       if (log.isDebugEnabled()) {
/* 180 */         log.debug("Restore request from session '" + session
/* 181 */           .getIdInternal() + "'");
/*     */       }
/*     */       
/*     */ 
/* 185 */       principal = (Principal)session.getNote("org.apache.catalina.authenticator.PRINCIPAL");
/* 186 */       register(request, response, principal, "FORM", 
/* 187 */         (String)session.getNote("org.apache.catalina.session.USERNAME"), 
/* 188 */         (String)session.getNote("org.apache.catalina.session.PASSWORD"));
/*     */       
/*     */ 
/* 191 */       if (this.cache) {
/* 192 */         session.removeNote("org.apache.catalina.session.USERNAME");
/* 193 */         session.removeNote("org.apache.catalina.session.PASSWORD");
/*     */       }
/* 195 */       if (restoreRequest(request, session)) {
/* 196 */         if (log.isDebugEnabled()) {
/* 197 */           log.debug("Proceed to restored request");
/*     */         }
/* 199 */         return true;
/*     */       }
/* 201 */       if (log.isDebugEnabled()) {
/* 202 */         log.debug("Restore of original request failed");
/*     */       }
/* 204 */       response.sendError(400);
/* 205 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 210 */     String contextPath = request.getContextPath();
/* 211 */     String requestURI = request.getDecodedRequestURI();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 216 */     boolean loginAction = (requestURI.startsWith(contextPath)) && (requestURI.endsWith("/j_security_check"));
/*     */     
/* 218 */     LoginConfig config = this.context.getLoginConfig();
/*     */     
/*     */ 
/* 221 */     if (!loginAction)
/*     */     {
/*     */ 
/*     */ 
/* 225 */       if ((request.getServletPath().length() == 0) && (request.getPathInfo() == null)) {
/* 226 */         StringBuilder location = new StringBuilder(requestURI);
/* 227 */         location.append('/');
/* 228 */         if (request.getQueryString() != null) {
/* 229 */           location.append('?');
/* 230 */           location.append(request.getQueryString());
/*     */         }
/* 232 */         response.sendRedirect(response.encodeRedirectURL(location.toString()));
/* 233 */         return false;
/*     */       }
/*     */       
/* 236 */       session = request.getSessionInternal(true);
/* 237 */       if (log.isDebugEnabled()) {
/* 238 */         log.debug("Save request in session '" + session.getIdInternal() + "'");
/*     */       }
/*     */       try {
/* 241 */         saveRequest(request, session);
/*     */       } catch (IOException ioe) {
/* 243 */         log.debug("Request body too big to save during authentication");
/* 244 */         response.sendError(403, sm
/* 245 */           .getString("authenticator.requestBodyTooBig"));
/* 246 */         return false;
/*     */       }
/* 248 */       forwardToLoginPage(request, response, config);
/* 249 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 254 */     request.getResponse().sendAcknowledgement();
/* 255 */     Realm realm = this.context.getRealm();
/* 256 */     if (this.characterEncoding != null) {
/* 257 */       request.setCharacterEncoding(this.characterEncoding);
/*     */     }
/* 259 */     String username = request.getParameter("j_username");
/* 260 */     String password = request.getParameter("j_password");
/* 261 */     if (log.isDebugEnabled()) {
/* 262 */       log.debug("Authenticating username '" + username + "'");
/*     */     }
/* 264 */     principal = realm.authenticate(username, password);
/* 265 */     if (principal == null) {
/* 266 */       forwardToErrorPage(request, response, config);
/* 267 */       return false;
/*     */     }
/*     */     
/* 270 */     if (log.isDebugEnabled()) {
/* 271 */       log.debug("Authentication of '" + username + "' was successful");
/*     */     }
/*     */     
/* 274 */     if (session == null) {
/* 275 */       session = request.getSessionInternal(false);
/*     */     }
/* 277 */     if (session == null) {
/* 278 */       if (this.containerLog.isDebugEnabled())
/*     */       {
/* 280 */         this.containerLog.debug("User took so long to log on the session expired");
/*     */       }
/* 282 */       if (this.landingPage == null) {
/* 283 */         response.sendError(408, sm
/* 284 */           .getString("authenticator.sessionExpired"));
/*     */       }
/*     */       else
/*     */       {
/* 288 */         String uri = request.getContextPath() + this.landingPage;
/* 289 */         SavedRequest saved = new SavedRequest();
/* 290 */         saved.setMethod("GET");
/* 291 */         saved.setRequestURI(uri);
/* 292 */         saved.setDecodedRequestURI(uri);
/* 293 */         request.getSessionInternal(true).setNote("org.apache.catalina.authenticator.REQUEST", saved);
/*     */         
/* 295 */         response.sendRedirect(response.encodeRedirectURL(uri));
/*     */       }
/* 297 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 301 */     session.setNote("org.apache.catalina.authenticator.PRINCIPAL", principal);
/*     */     
/*     */ 
/* 304 */     session.setNote("org.apache.catalina.session.USERNAME", username);
/* 305 */     session.setNote("org.apache.catalina.session.PASSWORD", password);
/*     */     
/*     */ 
/*     */ 
/* 309 */     requestURI = savedRequestURL(session);
/* 310 */     if (log.isDebugEnabled()) {
/* 311 */       log.debug("Redirecting to original '" + requestURI + "'");
/*     */     }
/* 313 */     if (requestURI == null) {
/* 314 */       if (this.landingPage == null) {
/* 315 */         response.sendError(400, sm
/* 316 */           .getString("authenticator.formlogin"));
/*     */       }
/*     */       else
/*     */       {
/* 320 */         String uri = request.getContextPath() + this.landingPage;
/* 321 */         SavedRequest saved = new SavedRequest();
/* 322 */         saved.setMethod("GET");
/* 323 */         saved.setRequestURI(uri);
/* 324 */         saved.setDecodedRequestURI(uri);
/* 325 */         session.setNote("org.apache.catalina.authenticator.REQUEST", saved);
/* 326 */         response.sendRedirect(response.encodeRedirectURL(uri));
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 331 */       Response internalResponse = request.getResponse();
/* 332 */       String location = response.encodeRedirectURL(requestURI);
/* 333 */       if ("HTTP/1.1".equals(request.getProtocol())) {
/* 334 */         internalResponse.sendRedirect(location, 303);
/*     */       }
/*     */       else {
/* 337 */         internalResponse.sendRedirect(location, 302);
/*     */       }
/*     */     }
/*     */     
/* 341 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isContinuationRequired(org.apache.catalina.connector.Request request)
/*     */   {
/* 351 */     String contextPath = this.context.getPath();
/* 352 */     String decodedRequestURI = request.getDecodedRequestURI();
/* 353 */     if ((decodedRequestURI.startsWith(contextPath)) && 
/* 354 */       (decodedRequestURI.endsWith("/j_security_check"))) {
/* 355 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 364 */     Session session = request.getSessionInternal(false);
/* 365 */     if (session != null) {
/* 366 */       SavedRequest savedRequest = (SavedRequest)session.getNote("org.apache.catalina.authenticator.REQUEST");
/* 367 */       if ((savedRequest != null) && 
/* 368 */         (decodedRequestURI.equals(savedRequest.getDecodedRequestURI()))) {
/* 369 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 373 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   protected String getAuthMethod()
/*     */   {
/* 379 */     return "FORM";
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
/*     */   protected void forwardToLoginPage(org.apache.catalina.connector.Request request, HttpServletResponse response, LoginConfig config)
/*     */     throws IOException
/*     */   {
/* 398 */     if (log.isDebugEnabled()) {
/* 399 */       log.debug(sm.getString("formAuthenticator.forwardLogin", new Object[] {request
/* 400 */         .getRequestURI(), request.getMethod(), config
/* 401 */         .getLoginPage(), this.context.getName() }));
/*     */     }
/*     */     
/* 404 */     String loginPage = config.getLoginPage();
/* 405 */     if ((loginPage == null) || (loginPage.length() == 0)) {
/* 406 */       String msg = sm.getString("formAuthenticator.noLoginPage", new Object[] {this.context
/* 407 */         .getName() });
/* 408 */       log.warn(msg);
/* 409 */       response.sendError(500, msg);
/*     */       
/* 411 */       return;
/*     */     }
/*     */     
/* 414 */     if (getChangeSessionIdOnAuthentication()) {
/* 415 */       Session session = request.getSessionInternal(false);
/* 416 */       if (session != null) {
/* 417 */         Manager manager = request.getContext().getManager();
/* 418 */         manager.changeSessionId(session);
/* 419 */         request.changeSessionId(session.getId());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 424 */     String oldMethod = request.getMethod();
/* 425 */     request.getCoyoteRequest().method().setString("GET");
/*     */     
/*     */ 
/* 428 */     RequestDispatcher disp = this.context.getServletContext().getRequestDispatcher(loginPage);
/*     */     try {
/* 430 */       if (this.context.fireRequestInitEvent(request.getRequest())) {
/* 431 */         disp.forward(request.getRequest(), response);
/* 432 */         this.context.fireRequestDestroyEvent(request.getRequest());
/*     */       }
/*     */     } catch (Throwable t) {
/* 435 */       ExceptionUtils.handleThrowable(t);
/* 436 */       String msg = sm.getString("formAuthenticator.forwardLoginFail");
/* 437 */       log.warn(msg, t);
/* 438 */       request.setAttribute("javax.servlet.error.exception", t);
/* 439 */       response.sendError(500, msg);
/*     */     }
/*     */     finally
/*     */     {
/* 443 */       request.getCoyoteRequest().method().setString(oldMethod);
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
/*     */   protected void forwardToErrorPage(org.apache.catalina.connector.Request request, HttpServletResponse response, LoginConfig config)
/*     */     throws IOException
/*     */   {
/* 463 */     String errorPage = config.getErrorPage();
/* 464 */     if ((errorPage == null) || (errorPage.length() == 0)) {
/* 465 */       String msg = sm.getString("formAuthenticator.noErrorPage", new Object[] {this.context
/* 466 */         .getName() });
/* 467 */       log.warn(msg);
/* 468 */       response.sendError(500, msg);
/*     */       
/* 470 */       return;
/*     */     }
/*     */     
/*     */ 
/* 474 */     RequestDispatcher disp = this.context.getServletContext().getRequestDispatcher(config.getErrorPage());
/*     */     try {
/* 476 */       if (this.context.fireRequestInitEvent(request.getRequest())) {
/* 477 */         disp.forward(request.getRequest(), response);
/* 478 */         this.context.fireRequestDestroyEvent(request.getRequest());
/*     */       }
/*     */     } catch (Throwable t) {
/* 481 */       ExceptionUtils.handleThrowable(t);
/* 482 */       String msg = sm.getString("formAuthenticator.forwardErrorFail");
/* 483 */       log.warn(msg, t);
/* 484 */       request.setAttribute("javax.servlet.error.exception", t);
/* 485 */       response.sendError(500, msg);
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
/*     */   protected boolean matchRequest(org.apache.catalina.connector.Request request)
/*     */   {
/* 500 */     Session session = request.getSessionInternal(false);
/* 501 */     if (session == null) {
/* 502 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 507 */     SavedRequest sreq = (SavedRequest)session.getNote("org.apache.catalina.authenticator.REQUEST");
/* 508 */     if (sreq == null) {
/* 509 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 513 */     if (session.getNote("org.apache.catalina.authenticator.PRINCIPAL") == null) {
/* 514 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 518 */     String decodedRequestURI = request.getDecodedRequestURI();
/* 519 */     if (decodedRequestURI == null) {
/* 520 */       return false;
/*     */     }
/* 522 */     return decodedRequestURI.equals(sreq.getDecodedRequestURI());
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
/*     */   protected boolean restoreRequest(org.apache.catalina.connector.Request request, Session session)
/*     */     throws IOException
/*     */   {
/* 542 */     SavedRequest saved = (SavedRequest)session.getNote("org.apache.catalina.authenticator.REQUEST");
/* 543 */     session.removeNote("org.apache.catalina.authenticator.REQUEST");
/* 544 */     session.removeNote("org.apache.catalina.authenticator.PRINCIPAL");
/* 545 */     if (saved == null) {
/* 546 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 553 */     byte[] buffer = new byte['က'];
/* 554 */     InputStream is = request.createInputStream();
/* 555 */     while (is.read(buffer) >= 0) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 560 */     request.clearCookies();
/* 561 */     Iterator<Cookie> cookies = saved.getCookies();
/* 562 */     while (cookies.hasNext()) {
/* 563 */       request.addCookie((Cookie)cookies.next());
/*     */     }
/*     */     
/* 566 */     String method = saved.getMethod();
/* 567 */     MimeHeaders rmh = request.getCoyoteRequest().getMimeHeaders();
/* 568 */     rmh.recycle();
/*     */     
/* 570 */     boolean cacheable = ("GET".equalsIgnoreCase(method)) || ("HEAD".equalsIgnoreCase(method));
/* 571 */     Iterator<String> names = saved.getHeaderNames();
/* 572 */     while (names.hasNext()) {
/* 573 */       String name = (String)names.next();
/*     */       
/*     */ 
/*     */ 
/* 577 */       if ((!"If-Modified-Since".equalsIgnoreCase(name)) && ((!cacheable) || 
/* 578 */         (!"If-None-Match".equalsIgnoreCase(name)))) {
/* 579 */         Iterator<String> values = saved.getHeaderValues(name);
/* 580 */         while (values.hasNext()) {
/* 581 */           rmh.addValue(name).setString((String)values.next());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 586 */     request.clearLocales();
/* 587 */     Iterator<Locale> locales = saved.getLocales();
/* 588 */     while (locales.hasNext()) {
/* 589 */       request.addLocale((Locale)locales.next());
/*     */     }
/*     */     
/* 592 */     request.getCoyoteRequest().getParameters().recycle();
/*     */     
/* 594 */     ByteChunk body = saved.getBody();
/*     */     
/* 596 */     if (body != null)
/*     */     {
/* 598 */       request.getCoyoteRequest().action(ActionCode.REQ_SET_BODY_REPLAY, body);
/*     */       
/*     */ 
/* 601 */       MessageBytes contentType = MessageBytes.newInstance();
/*     */       
/*     */ 
/* 604 */       String savedContentType = saved.getContentType();
/* 605 */       if ((savedContentType == null) && ("POST".equalsIgnoreCase(method))) {
/* 606 */         savedContentType = "application/x-www-form-urlencoded";
/*     */       }
/*     */       
/* 609 */       contentType.setString(savedContentType);
/* 610 */       request.getCoyoteRequest().setContentType(contentType);
/*     */     }
/*     */     
/* 613 */     request.getCoyoteRequest().method().setString(method);
/*     */     
/* 615 */     return true;
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
/*     */   protected void saveRequest(org.apache.catalina.connector.Request request, Session session)
/*     */     throws IOException
/*     */   {
/* 630 */     SavedRequest saved = new SavedRequest();
/* 631 */     Cookie[] cookies = request.getCookies();
/* 632 */     if (cookies != null) {
/* 633 */       for (int i = 0; i < cookies.length; i++) {
/* 634 */         saved.addCookie(cookies[i]);
/*     */       }
/*     */     }
/* 637 */     Enumeration<String> names = request.getHeaderNames();
/* 638 */     while (names.hasMoreElements()) {
/* 639 */       String name = (String)names.nextElement();
/* 640 */       Enumeration<String> values = request.getHeaders(name);
/* 641 */       while (values.hasMoreElements()) {
/* 642 */         String value = (String)values.nextElement();
/* 643 */         saved.addHeader(name, value);
/*     */       }
/*     */     }
/* 646 */     Enumeration<Locale> locales = request.getLocales();
/* 647 */     while (locales.hasMoreElements()) {
/* 648 */       Locale locale = (Locale)locales.nextElement();
/* 649 */       saved.addLocale(locale);
/*     */     }
/*     */     
/*     */ 
/* 653 */     request.getResponse().sendAcknowledgement();
/*     */     
/* 655 */     int maxSavePostSize = request.getConnector().getMaxSavePostSize();
/* 656 */     if (maxSavePostSize != 0) {
/* 657 */       ByteChunk body = new ByteChunk();
/* 658 */       body.setLimit(maxSavePostSize);
/*     */       
/* 660 */       byte[] buffer = new byte['က'];
/*     */       
/* 662 */       InputStream is = request.getInputStream();
/*     */       int bytesRead;
/* 664 */       while ((bytesRead = is.read(buffer)) >= 0) {
/* 665 */         body.append(buffer, 0, bytesRead);
/*     */       }
/*     */       
/*     */ 
/* 669 */       if (body.getLength() > 0) {
/* 670 */         saved.setContentType(request.getContentType());
/* 671 */         saved.setBody(body);
/*     */       }
/*     */     }
/*     */     
/* 675 */     saved.setMethod(request.getMethod());
/* 676 */     saved.setQueryString(request.getQueryString());
/* 677 */     saved.setRequestURI(request.getRequestURI());
/* 678 */     saved.setDecodedRequestURI(request.getDecodedRequestURI());
/*     */     
/*     */ 
/* 681 */     session.setNote("org.apache.catalina.authenticator.REQUEST", saved);
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
/*     */   protected String savedRequestURL(Session session)
/*     */   {
/* 695 */     SavedRequest saved = (SavedRequest)session.getNote("org.apache.catalina.authenticator.REQUEST");
/* 696 */     if (saved == null) {
/* 697 */       return null;
/*     */     }
/* 699 */     StringBuilder sb = new StringBuilder(saved.getRequestURI());
/* 700 */     if (saved.getQueryString() != null) {
/* 701 */       sb.append('?');
/* 702 */       sb.append(saved.getQueryString());
/*     */     }
/* 704 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\authenticator\FormAuthenticator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */