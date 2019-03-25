/*     */ package org.apache.catalina.core;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.DispatcherType;
/*     */ import javax.servlet.RequestDispatcher;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.Globals;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.Pipeline;
/*     */ import org.apache.catalina.Valve;
/*     */ import org.apache.catalina.Wrapper;
/*     */ import org.apache.catalina.connector.ClientAbortException;
/*     */ import org.apache.catalina.connector.Request;
/*     */ import org.apache.catalina.connector.Response;
/*     */ import org.apache.catalina.valves.ValveBase;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
/*     */ import org.apache.tomcat.util.descriptor.web.ErrorPage;
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
/*     */ final class StandardHostValve
/*     */   extends ValveBase
/*     */ {
/*  52 */   private static final Log log = LogFactory.getLog(StandardHostValve.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  57 */   private static final ClassLoader MY_CLASSLOADER = StandardHostValve.class
/*  58 */     .getClassLoader();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  65 */   static final boolean STRICT_SERVLET_COMPLIANCE = Globals.STRICT_SERVLET_COMPLIANCE;
/*     */   
/*  67 */   static { String accessSession = System.getProperty("org.apache.catalina.core.StandardHostValve.ACCESS_SESSION");
/*     */     
/*  69 */     if (accessSession == null) {
/*  70 */       ACCESS_SESSION = STRICT_SERVLET_COMPLIANCE;
/*     */     } else {
/*  72 */       ACCESS_SESSION = Boolean.parseBoolean(accessSession);
/*     */     }
/*     */   }
/*     */   
/*     */   public StandardHostValve()
/*     */   {
/*  78 */     super(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static final boolean ACCESS_SESSION;
/*     */   
/*     */ 
/*     */ 
/*  88 */   private static final StringManager sm = StringManager.getManager("org.apache.catalina.core");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void invoke(Request request, Response response)
/*     */     throws IOException, ServletException
/*     */   {
/* 109 */     Context context = request.getContext();
/* 110 */     if (context == null) {
/* 111 */       response.sendError(500, sm
/* 112 */         .getString("standardHost.noContext"));
/* 113 */       return;
/*     */     }
/*     */     
/* 116 */     if (request.isAsyncSupported()) {
/* 117 */       request.setAsyncSupported(context.getPipeline().isAsyncSupported());
/*     */     }
/*     */     
/* 120 */     boolean asyncAtStart = request.isAsync();
/* 121 */     boolean asyncDispatching = request.isAsyncDispatching();
/*     */     try
/*     */     {
/* 124 */       context.bind(Globals.IS_SECURITY_ENABLED, MY_CLASSLOADER);
/*     */       
/* 126 */       if ((!asyncAtStart) && (!context.fireRequestInitEvent(request.getRequest())))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 131 */         return;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       try
/*     */       {
/* 139 */         if ((!asyncAtStart) || (asyncDispatching)) {
/* 140 */           context.getPipeline().getFirst().invoke(request, response);
/*     */ 
/*     */ 
/*     */         }
/* 144 */         else if (!response.isErrorReportRequired()) {
/* 145 */           throw new IllegalStateException(sm.getString("standardHost.asyncStateError"));
/*     */         }
/*     */       }
/*     */       catch (Throwable t) {
/* 149 */         ExceptionUtils.handleThrowable(t);
/* 150 */         this.container.getLogger().error("Exception Processing " + request.getRequestURI(), t);
/*     */         
/*     */ 
/* 153 */         if (!response.isErrorReportRequired()) {
/* 154 */           request.setAttribute("javax.servlet.error.exception", t);
/* 155 */           throwable(request, response, t);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 162 */       response.setSuspended(false);
/*     */       
/* 164 */       Throwable t = (Throwable)request.getAttribute("javax.servlet.error.exception");
/*     */       
/*     */ 
/*     */ 
/* 168 */       if (!context.getState().isAvailable()) {
/* 169 */         return;
/*     */       }
/*     */       
/*     */ 
/* 173 */       if (response.isErrorReportRequired()) {
/* 174 */         if (t != null) {
/* 175 */           throwable(request, response, t);
/*     */         } else {
/* 177 */           status(request, response);
/*     */         }
/*     */       }
/*     */       
/* 181 */       if ((!request.isAsync()) && (!asyncAtStart)) {
/* 182 */         context.fireRequestDestroyEvent(request.getRequest());
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 187 */       if (ACCESS_SESSION) {
/* 188 */         request.getSession(false);
/*     */       }
/*     */       
/* 191 */       context.unbind(Globals.IS_SECURITY_ENABLED, MY_CLASSLOADER);
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
/*     */   private void status(Request request, Response response)
/*     */   {
/* 209 */     int statusCode = response.getStatus();
/*     */     
/*     */ 
/* 212 */     Context context = request.getContext();
/* 213 */     if (context == null) {
/* 214 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 222 */     if (!response.isError()) {
/* 223 */       return;
/*     */     }
/*     */     
/* 226 */     ErrorPage errorPage = context.findErrorPage(statusCode);
/* 227 */     if (errorPage == null)
/*     */     {
/* 229 */       errorPage = context.findErrorPage(0);
/*     */     }
/* 231 */     if ((errorPage != null) && (response.isErrorReportRequired())) {
/* 232 */       response.setAppCommitted(false);
/* 233 */       request.setAttribute("javax.servlet.error.status_code", 
/* 234 */         Integer.valueOf(statusCode));
/*     */       
/* 236 */       String message = response.getMessage();
/* 237 */       if (message == null) {
/* 238 */         message = "";
/*     */       }
/* 240 */       request.setAttribute("javax.servlet.error.message", message);
/* 241 */       request.setAttribute("org.apache.catalina.core.DISPATCHER_REQUEST_PATH", errorPage
/* 242 */         .getLocation());
/* 243 */       request.setAttribute("org.apache.catalina.core.DISPATCHER_TYPE", DispatcherType.ERROR);
/*     */       
/*     */ 
/*     */ 
/* 247 */       Wrapper wrapper = request.getWrapper();
/* 248 */       if (wrapper != null) {
/* 249 */         request.setAttribute("javax.servlet.error.servlet_name", wrapper
/* 250 */           .getName());
/*     */       }
/* 252 */       request.setAttribute("javax.servlet.error.request_uri", request
/* 253 */         .getRequestURI());
/* 254 */       if (custom(request, response, errorPage)) {
/* 255 */         response.setErrorReported();
/*     */         try {
/* 257 */           response.finishResponse();
/*     */         }
/*     */         catch (ClientAbortException localClientAbortException) {}catch (IOException e)
/*     */         {
/* 261 */           this.container.getLogger().warn("Exception Processing " + errorPage, e);
/*     */         }
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
/*     */ 
/*     */ 
/*     */   protected void throwable(Request request, Response response, Throwable throwable)
/*     */   {
/* 281 */     Context context = request.getContext();
/* 282 */     if (context == null) {
/* 283 */       return;
/*     */     }
/*     */     
/* 286 */     Throwable realError = throwable;
/*     */     
/* 288 */     if ((realError instanceof ServletException)) {
/* 289 */       realError = ((ServletException)realError).getRootCause();
/* 290 */       if (realError == null) {
/* 291 */         realError = throwable;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 296 */     if ((realError instanceof ClientAbortException)) {
/* 297 */       if (log.isDebugEnabled())
/*     */       {
/* 299 */         log.debug(sm.getString("standardHost.clientAbort", new Object[] {realError
/* 300 */           .getCause().getMessage() }));
/*     */       }
/* 302 */       return;
/*     */     }
/*     */     
/* 305 */     ErrorPage errorPage = findErrorPage(context, throwable);
/* 306 */     if ((errorPage == null) && (realError != throwable)) {
/* 307 */       errorPage = findErrorPage(context, realError);
/*     */     }
/*     */     
/* 310 */     if (errorPage != null) {
/* 311 */       if (response.setErrorReported()) {
/* 312 */         response.setAppCommitted(false);
/* 313 */         request.setAttribute("org.apache.catalina.core.DISPATCHER_REQUEST_PATH", errorPage
/* 314 */           .getLocation());
/* 315 */         request.setAttribute("org.apache.catalina.core.DISPATCHER_TYPE", DispatcherType.ERROR);
/*     */         
/* 317 */         request.setAttribute("javax.servlet.error.status_code", 
/* 318 */           Integer.valueOf(500));
/* 319 */         request.setAttribute("javax.servlet.error.message", throwable
/* 320 */           .getMessage());
/* 321 */         request.setAttribute("javax.servlet.error.exception", realError);
/*     */         
/* 323 */         Wrapper wrapper = request.getWrapper();
/* 324 */         if (wrapper != null) {
/* 325 */           request.setAttribute("javax.servlet.error.servlet_name", wrapper
/* 326 */             .getName());
/*     */         }
/* 328 */         request.setAttribute("javax.servlet.error.request_uri", request
/* 329 */           .getRequestURI());
/* 330 */         request.setAttribute("javax.servlet.error.exception_type", realError
/* 331 */           .getClass());
/* 332 */         if (custom(request, response, errorPage)) {
/*     */           try {
/* 334 */             response.finishResponse();
/*     */           } catch (IOException e) {
/* 336 */             this.container.getLogger().warn("Exception Processing " + errorPage, e);
/*     */           }
/*     */           
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 345 */       response.setStatus(500);
/*     */       
/* 347 */       response.setError();
/*     */       
/* 349 */       status(request, response);
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
/*     */   private boolean custom(Request request, Response response, ErrorPage errorPage)
/*     */   {
/* 369 */     if (this.container.getLogger().isDebugEnabled()) {
/* 370 */       this.container.getLogger().debug("Processing " + errorPage);
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 376 */       ServletContext servletContext = request.getContext().getServletContext();
/*     */       
/* 378 */       RequestDispatcher rd = servletContext.getRequestDispatcher(errorPage.getLocation());
/*     */       
/* 380 */       if (rd == null) {
/* 381 */         this.container.getLogger().error(sm
/* 382 */           .getString("standardHostValue.customStatusFailed", new Object[] {errorPage.getLocation() }));
/* 383 */         return false;
/*     */       }
/*     */       
/* 386 */       if (response.isCommitted())
/*     */       {
/*     */ 
/* 389 */         rd.include(request.getRequest(), response.getResponse());
/*     */       }
/*     */       else {
/* 392 */         response.resetBuffer(true);
/* 393 */         response.setContentLength(-1);
/*     */         
/* 395 */         rd.forward(request.getRequest(), response.getResponse());
/*     */         
/*     */ 
/* 398 */         response.setSuspended(false);
/*     */       }
/*     */       
/*     */ 
/* 402 */       return true;
/*     */     }
/*     */     catch (Throwable t) {
/* 405 */       ExceptionUtils.handleThrowable(t);
/*     */       
/* 407 */       this.container.getLogger().error("Exception Processing " + errorPage, t); }
/* 408 */     return false;
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
/*     */   private static ErrorPage findErrorPage(Context context, Throwable exception)
/*     */   {
/* 426 */     if (exception == null) {
/* 427 */       return null;
/*     */     }
/* 429 */     Class<?> clazz = exception.getClass();
/* 430 */     String name = clazz.getName();
/* 431 */     while (!Object.class.equals(clazz)) {
/* 432 */       ErrorPage errorPage = context.findErrorPage(name);
/* 433 */       if (errorPage != null) {
/* 434 */         return errorPage;
/*     */       }
/* 436 */       clazz = clazz.getSuperclass();
/* 437 */       if (clazz == null) {
/*     */         break;
/*     */       }
/* 440 */       name = clazz.getName();
/*     */     }
/* 442 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\StandardHostValve.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */