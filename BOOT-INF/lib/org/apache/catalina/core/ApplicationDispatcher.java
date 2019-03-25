/*      */ package org.apache.catalina.core;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.PrintWriter;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import javax.servlet.AsyncContext;
/*      */ import javax.servlet.DispatcherType;
/*      */ import javax.servlet.RequestDispatcher;
/*      */ import javax.servlet.Servlet;
/*      */ import javax.servlet.ServletException;
/*      */ import javax.servlet.ServletOutputStream;
/*      */ import javax.servlet.ServletRequest;
/*      */ import javax.servlet.ServletRequestWrapper;
/*      */ import javax.servlet.ServletResponse;
/*      */ import javax.servlet.ServletResponseWrapper;
/*      */ import javax.servlet.UnavailableException;
/*      */ import javax.servlet.http.HttpServletResponse;
/*      */ import org.apache.catalina.AsyncDispatcher;
/*      */ import org.apache.catalina.Context;
/*      */ import org.apache.catalina.Globals;
/*      */ import org.apache.catalina.Wrapper;
/*      */ import org.apache.catalina.connector.ClientAbortException;
/*      */ import org.apache.catalina.connector.Request;
/*      */ import org.apache.catalina.connector.RequestFacade;
/*      */ import org.apache.catalina.connector.Response;
/*      */ import org.apache.catalina.connector.ResponseFacade;
/*      */ import org.apache.catalina.servlet4preview.http.ServletMapping;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
/*      */ import org.apache.tomcat.util.res.StringManager;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ final class ApplicationDispatcher
/*      */   implements AsyncDispatcher, RequestDispatcher
/*      */ {
/*      */   public static final String ASYNC_MAPPING = "javax.servlet.async.mapping";
/*      */   public static final String FORWARD_MAPPING = "javax.servlet.forward.mapping";
/*      */   public static final String INCLUDE_MAPPING = "javax.servlet.include.mapping";
/*   76 */   static final boolean STRICT_SERVLET_COMPLIANCE = Globals.STRICT_SERVLET_COMPLIANCE;
/*      */   
/*   78 */   static { String wrapSameObject = System.getProperty("org.apache.catalina.core.ApplicationDispatcher.WRAP_SAME_OBJECT");
/*      */     
/*   80 */     if (wrapSameObject == null) {
/*   81 */       WRAP_SAME_OBJECT = STRICT_SERVLET_COMPLIANCE;
/*      */     } else {
/*   83 */       WRAP_SAME_OBJECT = Boolean.parseBoolean(wrapSameObject);
/*      */     }
/*      */   }
/*      */   
/*      */   protected class PrivilegedForward implements PrivilegedExceptionAction<Void>
/*      */   {
/*      */     private final ServletRequest request;
/*      */     private final ServletResponse response;
/*      */     
/*      */     PrivilegedForward(ServletRequest request, ServletResponse response)
/*      */     {
/*   94 */       this.request = request;
/*   95 */       this.response = response;
/*      */     }
/*      */     
/*      */     public Void run() throws Exception
/*      */     {
/*  100 */       ApplicationDispatcher.this.doForward(this.request, this.response);
/*  101 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */   protected class PrivilegedInclude implements PrivilegedExceptionAction<Void>
/*      */   {
/*      */     private final ServletRequest request;
/*      */     private final ServletResponse response;
/*      */     
/*      */     PrivilegedInclude(ServletRequest request, ServletResponse response) {
/*  111 */       this.request = request;
/*  112 */       this.response = response;
/*      */     }
/*      */     
/*      */     public Void run() throws ServletException, IOException
/*      */     {
/*  117 */       ApplicationDispatcher.this.doInclude(this.request, this.response);
/*  118 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */   protected class PrivilegedDispatch implements PrivilegedExceptionAction<Void>
/*      */   {
/*      */     private final ServletRequest request;
/*      */     private final ServletResponse response;
/*      */     
/*      */     PrivilegedDispatch(ServletRequest request, ServletResponse response) {
/*  128 */       this.request = request;
/*  129 */       this.response = response;
/*      */     }
/*      */     
/*      */     public Void run() throws ServletException, IOException
/*      */     {
/*  134 */       ApplicationDispatcher.this.doDispatch(this.request, this.response);
/*  135 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class State
/*      */   {
/*      */     State(ServletRequest request, ServletResponse response, boolean including)
/*      */     {
/*  148 */       this.outerRequest = request;
/*  149 */       this.outerResponse = response;
/*  150 */       this.including = including;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  156 */     ServletRequest outerRequest = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  162 */     ServletResponse outerResponse = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  167 */     ServletRequest wrapRequest = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  173 */     ServletResponse wrapResponse = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  178 */     boolean including = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  183 */     javax.servlet.http.HttpServletRequest hrequest = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  188 */     HttpServletResponse hresponse = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final boolean WRAP_SAME_OBJECT;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final Context context;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final String name;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final String pathInfo;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ApplicationDispatcher(Wrapper wrapper, String requestURI, String servletPath, String pathInfo, String queryString, ServletMapping mapping, String name)
/*      */   {
/*  219 */     this.wrapper = wrapper;
/*  220 */     this.context = ((Context)wrapper.getParent());
/*  221 */     this.requestURI = requestURI;
/*  222 */     this.servletPath = servletPath;
/*  223 */     this.pathInfo = pathInfo;
/*  224 */     this.queryString = queryString;
/*  225 */     this.mapping = mapping;
/*  226 */     this.name = name;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final String queryString;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final String requestURI;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final String servletPath;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final ServletMapping mapping;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  277 */   private static final StringManager sm = StringManager.getManager("org.apache.catalina.core");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final Wrapper wrapper;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void forward(ServletRequest request, ServletResponse response)
/*      */     throws ServletException, IOException
/*      */   {
/*  305 */     if (Globals.IS_SECURITY_ENABLED) {
/*      */       try {
/*  307 */         PrivilegedForward dp = new PrivilegedForward(request, response);
/*  308 */         AccessController.doPrivileged(dp);
/*      */       } catch (PrivilegedActionException pe) {
/*  310 */         Exception e = pe.getException();
/*  311 */         if ((e instanceof ServletException))
/*  312 */           throw ((ServletException)e);
/*  313 */         throw ((IOException)e);
/*      */       }
/*      */     } else {
/*  316 */       doForward(request, response);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void doForward(ServletRequest request, ServletResponse response)
/*      */     throws ServletException, IOException
/*      */   {
/*  325 */     if (response.isCommitted())
/*      */     {
/*  327 */       throw new IllegalStateException(sm.getString("applicationDispatcher.forward.ise"));
/*      */     }
/*      */     try {
/*  330 */       response.resetBuffer();
/*      */     } catch (IllegalStateException e) {
/*  332 */       throw e;
/*      */     }
/*      */     
/*      */ 
/*  336 */     State state = new State(request, response, false);
/*      */     
/*  338 */     if (WRAP_SAME_OBJECT)
/*      */     {
/*  340 */       checkSameObjects(request, response);
/*      */     }
/*      */     
/*  343 */     wrapResponse(state);
/*      */     
/*  345 */     if ((this.servletPath == null) && (this.pathInfo == null))
/*      */     {
/*      */ 
/*  348 */       ApplicationHttpRequest wrequest = (ApplicationHttpRequest)wrapRequest(state);
/*  349 */       javax.servlet.http.HttpServletRequest hrequest = state.hrequest;
/*  350 */       wrequest.setRequestURI(hrequest.getRequestURI());
/*  351 */       wrequest.setContextPath(hrequest.getContextPath());
/*  352 */       wrequest.setServletPath(hrequest.getServletPath());
/*  353 */       wrequest.setPathInfo(hrequest.getPathInfo());
/*  354 */       wrequest.setQueryString(hrequest.getQueryString());
/*      */       
/*  356 */       processRequest(request, response, state);
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*  362 */       ApplicationHttpRequest wrequest = (ApplicationHttpRequest)wrapRequest(state);
/*  363 */       javax.servlet.http.HttpServletRequest hrequest = state.hrequest;
/*  364 */       if (hrequest.getAttribute("javax.servlet.forward.request_uri") == null) {
/*  365 */         wrequest.setAttribute("javax.servlet.forward.request_uri", hrequest
/*  366 */           .getRequestURI());
/*  367 */         wrequest.setAttribute("javax.servlet.forward.context_path", hrequest
/*  368 */           .getContextPath());
/*  369 */         wrequest.setAttribute("javax.servlet.forward.servlet_path", hrequest
/*  370 */           .getServletPath());
/*  371 */         wrequest.setAttribute("javax.servlet.forward.path_info", hrequest
/*  372 */           .getPathInfo());
/*  373 */         wrequest.setAttribute("javax.servlet.forward.query_string", hrequest
/*  374 */           .getQueryString());
/*      */         ServletMapping mapping;
/*  376 */         ServletMapping mapping; if ((hrequest instanceof org.apache.catalina.servlet4preview.http.HttpServletRequest))
/*      */         {
/*  378 */           mapping = ((org.apache.catalina.servlet4preview.http.HttpServletRequest)hrequest).getServletMapping();
/*      */         } else {
/*  380 */           mapping = new ApplicationMapping(null).getServletMapping();
/*      */         }
/*  382 */         wrequest.setAttribute("javax.servlet.forward.mapping", mapping);
/*      */       }
/*      */       
/*  385 */       wrequest.setContextPath(this.context.getPath());
/*  386 */       wrequest.setRequestURI(this.requestURI);
/*  387 */       wrequest.setServletPath(this.servletPath);
/*  388 */       wrequest.setPathInfo(this.pathInfo);
/*  389 */       if (this.queryString != null) {
/*  390 */         wrequest.setQueryString(this.queryString);
/*  391 */         wrequest.setQueryParams(this.queryString);
/*      */       }
/*  393 */       wrequest.setMapping(this.mapping);
/*      */       
/*  395 */       processRequest(request, response, state);
/*      */     }
/*      */     
/*  398 */     if (request.isAsyncStarted())
/*      */     {
/*      */ 
/*  401 */       return;
/*      */     }
/*      */     
/*      */ 
/*  405 */     if (this.wrapper.getLogger().isDebugEnabled()) {
/*  406 */       this.wrapper.getLogger().debug(" Disabling the response for further output");
/*      */     }
/*  408 */     if ((response instanceof ResponseFacade)) {
/*  409 */       ((ResponseFacade)response).finish();
/*      */     }
/*      */     else
/*      */     {
/*  413 */       if (this.wrapper.getLogger().isDebugEnabled()) {
/*  414 */         this.wrapper.getLogger().debug(" The Response is vehiculed using a wrapper: " + response
/*  415 */           .getClass().getName());
/*      */       }
/*      */       
/*      */       try
/*      */       {
/*  420 */         PrintWriter writer = response.getWriter();
/*  421 */         writer.close();
/*      */       } catch (IllegalStateException e) {
/*      */         try {
/*  424 */           ServletOutputStream stream = response.getOutputStream();
/*  425 */           stream.close();
/*      */         }
/*      */         catch (IllegalStateException localIllegalStateException1) {}catch (IOException localIOException) {}
/*      */       }
/*      */       catch (IOException localIOException1) {}
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void processRequest(ServletRequest request, ServletResponse response, State state)
/*      */     throws IOException, ServletException
/*      */   {
/*  453 */     DispatcherType disInt = (DispatcherType)request.getAttribute("org.apache.catalina.core.DISPATCHER_TYPE");
/*  454 */     if (disInt != null) {
/*  455 */       boolean doInvoke = true;
/*      */       
/*  457 */       if ((this.context.getFireRequestListenersOnForwards()) && 
/*  458 */         (!this.context.fireRequestInitEvent(request))) {
/*  459 */         doInvoke = false;
/*      */       }
/*      */       
/*  462 */       if (doInvoke) {
/*  463 */         if (disInt != DispatcherType.ERROR) {
/*  464 */           state.outerRequest.setAttribute("org.apache.catalina.core.DISPATCHER_REQUEST_PATH", 
/*      */           
/*  466 */             getCombinedPath());
/*  467 */           state.outerRequest.setAttribute("org.apache.catalina.core.DISPATCHER_TYPE", DispatcherType.FORWARD);
/*      */           
/*      */ 
/*  470 */           invoke(state.outerRequest, response, state);
/*      */         } else {
/*  472 */           invoke(state.outerRequest, response, state);
/*      */         }
/*      */         
/*  475 */         if (this.context.getFireRequestListenersOnForwards()) {
/*  476 */           this.context.fireRequestDestroyEvent(request);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String getCombinedPath()
/*      */   {
/*  490 */     if (this.servletPath == null) {
/*  491 */       return null;
/*      */     }
/*  493 */     if (this.pathInfo == null) {
/*  494 */       return this.servletPath;
/*      */     }
/*  496 */     return this.servletPath + this.pathInfo;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void include(ServletRequest request, ServletResponse response)
/*      */     throws ServletException, IOException
/*      */   {
/*  515 */     if (Globals.IS_SECURITY_ENABLED) {
/*      */       try {
/*  517 */         PrivilegedInclude dp = new PrivilegedInclude(request, response);
/*  518 */         AccessController.doPrivileged(dp);
/*      */       } catch (PrivilegedActionException pe) {
/*  520 */         Exception e = pe.getException();
/*      */         
/*  522 */         if ((e instanceof ServletException))
/*  523 */           throw ((ServletException)e);
/*  524 */         throw ((IOException)e);
/*      */       }
/*      */     } else {
/*  527 */       doInclude(request, response);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void doInclude(ServletRequest request, ServletResponse response)
/*      */     throws ServletException, IOException
/*      */   {
/*  535 */     State state = new State(request, response, true);
/*      */     
/*  537 */     if (WRAP_SAME_OBJECT)
/*      */     {
/*  539 */       checkSameObjects(request, response);
/*      */     }
/*      */     
/*      */ 
/*  543 */     wrapResponse(state);
/*      */     
/*      */ 
/*  546 */     if (this.name != null)
/*      */     {
/*      */ 
/*  549 */       ApplicationHttpRequest wrequest = (ApplicationHttpRequest)wrapRequest(state);
/*  550 */       wrequest.setAttribute("org.apache.catalina.NAMED", this.name);
/*  551 */       if (this.servletPath != null)
/*  552 */         wrequest.setServletPath(this.servletPath);
/*  553 */       wrequest.setAttribute("org.apache.catalina.core.DISPATCHER_TYPE", DispatcherType.INCLUDE);
/*      */       
/*  555 */       wrequest.setAttribute("org.apache.catalina.core.DISPATCHER_REQUEST_PATH", 
/*  556 */         getCombinedPath());
/*  557 */       invoke(state.outerRequest, state.outerResponse, state);
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*  564 */       ApplicationHttpRequest wrequest = (ApplicationHttpRequest)wrapRequest(state);
/*  565 */       String contextPath = this.context.getPath();
/*  566 */       if (this.requestURI != null) {
/*  567 */         wrequest.setAttribute("javax.servlet.include.request_uri", this.requestURI);
/*      */       }
/*  569 */       if (contextPath != null) {
/*  570 */         wrequest.setAttribute("javax.servlet.include.context_path", contextPath);
/*      */       }
/*  572 */       if (this.servletPath != null) {
/*  573 */         wrequest.setAttribute("javax.servlet.include.servlet_path", this.servletPath);
/*      */       }
/*  575 */       if (this.pathInfo != null) {
/*  576 */         wrequest.setAttribute("javax.servlet.include.path_info", this.pathInfo);
/*      */       }
/*  578 */       if (this.queryString != null) {
/*  579 */         wrequest.setAttribute("javax.servlet.include.query_string", this.queryString);
/*      */         
/*  581 */         wrequest.setQueryParams(this.queryString);
/*      */       }
/*  583 */       if (this.mapping != null) {
/*  584 */         wrequest.setAttribute("javax.servlet.include.mapping", this.mapping);
/*      */       }
/*      */       
/*  587 */       wrequest.setAttribute("org.apache.catalina.core.DISPATCHER_TYPE", DispatcherType.INCLUDE);
/*      */       
/*  589 */       wrequest.setAttribute("org.apache.catalina.core.DISPATCHER_REQUEST_PATH", 
/*  590 */         getCombinedPath());
/*  591 */       invoke(state.outerRequest, state.outerResponse, state);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void dispatch(ServletRequest request, ServletResponse response)
/*      */     throws ServletException, IOException
/*      */   {
/*  600 */     if (Globals.IS_SECURITY_ENABLED) {
/*      */       try {
/*  602 */         PrivilegedDispatch dp = new PrivilegedDispatch(request, response);
/*  603 */         AccessController.doPrivileged(dp);
/*      */       } catch (PrivilegedActionException pe) {
/*  605 */         Exception e = pe.getException();
/*      */         
/*  607 */         if ((e instanceof ServletException))
/*  608 */           throw ((ServletException)e);
/*  609 */         throw ((IOException)e);
/*      */       }
/*      */     } else {
/*  612 */       doDispatch(request, response);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void doDispatch(ServletRequest request, ServletResponse response)
/*      */     throws ServletException, IOException
/*      */   {
/*  620 */     State state = new State(request, response, false);
/*      */     
/*      */ 
/*  623 */     wrapResponse(state);
/*      */     
/*  625 */     ApplicationHttpRequest wrequest = (ApplicationHttpRequest)wrapRequest(state);
/*  626 */     javax.servlet.http.HttpServletRequest hrequest = state.hrequest;
/*      */     
/*  628 */     wrequest.setAttribute("org.apache.catalina.core.DISPATCHER_TYPE", DispatcherType.ASYNC);
/*  629 */     wrequest.setAttribute("org.apache.catalina.core.DISPATCHER_REQUEST_PATH", getCombinedPath());
/*      */     ServletMapping mapping;
/*  631 */     ServletMapping mapping; if ((hrequest instanceof org.apache.catalina.servlet4preview.http.HttpServletRequest))
/*      */     {
/*  633 */       mapping = ((org.apache.catalina.servlet4preview.http.HttpServletRequest)hrequest).getServletMapping();
/*      */     } else {
/*  635 */       mapping = new ApplicationMapping(null).getServletMapping();
/*      */     }
/*  637 */     wrequest.setAttribute("javax.servlet.async.mapping", mapping);
/*      */     
/*  639 */     wrequest.setContextPath(this.context.getPath());
/*  640 */     wrequest.setRequestURI(this.requestURI);
/*  641 */     wrequest.setServletPath(this.servletPath);
/*  642 */     wrequest.setPathInfo(this.pathInfo);
/*  643 */     if (this.queryString != null) {
/*  644 */       wrequest.setQueryString(this.queryString);
/*  645 */       wrequest.setQueryParams(this.queryString);
/*      */     }
/*  647 */     wrequest.setMapping(this.mapping);
/*      */     
/*  649 */     invoke(state.outerRequest, state.outerResponse, state);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void invoke(ServletRequest request, ServletResponse response, State state)
/*      */     throws IOException, ServletException
/*      */   {
/*  677 */     ClassLoader oldCCL = this.context.bind(false, null);
/*      */     
/*      */ 
/*  680 */     HttpServletResponse hresponse = state.hresponse;
/*  681 */     Servlet servlet = null;
/*  682 */     IOException ioException = null;
/*  683 */     ServletException servletException = null;
/*  684 */     RuntimeException runtimeException = null;
/*  685 */     boolean unavailable = false;
/*      */     
/*      */ 
/*  688 */     if (this.wrapper.isUnavailable()) {
/*  689 */       this.wrapper.getLogger().warn(sm
/*  690 */         .getString("applicationDispatcher.isUnavailable", new Object[] {this.wrapper
/*  691 */         .getName() }));
/*  692 */       long available = this.wrapper.getAvailable();
/*  693 */       if ((available > 0L) && (available < Long.MAX_VALUE))
/*  694 */         hresponse.setDateHeader("Retry-After", available);
/*  695 */       hresponse.sendError(503, sm
/*  696 */         .getString("applicationDispatcher.isUnavailable", new Object[] {this.wrapper
/*  697 */         .getName() }));
/*  698 */       unavailable = true;
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  703 */       if (!unavailable) {
/*  704 */         servlet = this.wrapper.allocate();
/*      */       }
/*      */     } catch (ServletException e) {
/*  707 */       this.wrapper.getLogger().error(sm.getString("applicationDispatcher.allocateException", new Object[] {this.wrapper
/*  708 */         .getName() }), StandardWrapper.getRootCause(e));
/*  709 */       servletException = e;
/*      */     } catch (Throwable e) {
/*  711 */       ExceptionUtils.handleThrowable(e);
/*  712 */       this.wrapper.getLogger().error(sm.getString("applicationDispatcher.allocateException", new Object[] {this.wrapper
/*  713 */         .getName() }), e);
/*      */       
/*  715 */       servletException = new ServletException(sm.getString("applicationDispatcher.allocateException", new Object[] {this.wrapper
/*  716 */         .getName() }), e);
/*  717 */       servlet = null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  722 */     ApplicationFilterChain filterChain = ApplicationFilterFactory.createFilterChain(request, this.wrapper, servlet);
/*      */     
/*      */ 
/*      */     try
/*      */     {
/*  727 */       if ((servlet != null) && (filterChain != null)) {
/*  728 */         filterChain.doFilter(request, response);
/*      */       }
/*      */     }
/*      */     catch (ClientAbortException e) {
/*  732 */       ioException = e;
/*      */     } catch (IOException e) {
/*  734 */       this.wrapper.getLogger().error(sm.getString("applicationDispatcher.serviceException", new Object[] {this.wrapper
/*  735 */         .getName() }), e);
/*  736 */       ioException = e;
/*      */     } catch (UnavailableException e) {
/*  738 */       this.wrapper.getLogger().error(sm.getString("applicationDispatcher.serviceException", new Object[] {this.wrapper
/*  739 */         .getName() }), e);
/*  740 */       servletException = e;
/*  741 */       this.wrapper.unavailable(e);
/*      */     } catch (ServletException e) {
/*  743 */       Throwable rootCause = StandardWrapper.getRootCause(e);
/*  744 */       if (!(rootCause instanceof ClientAbortException)) {
/*  745 */         this.wrapper.getLogger().error(sm.getString("applicationDispatcher.serviceException", new Object[] {this.wrapper
/*  746 */           .getName() }), rootCause);
/*      */       }
/*  748 */       servletException = e;
/*      */     } catch (RuntimeException e) {
/*  750 */       this.wrapper.getLogger().error(sm.getString("applicationDispatcher.serviceException", new Object[] {this.wrapper
/*  751 */         .getName() }), e);
/*  752 */       runtimeException = e;
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  757 */       if (filterChain != null)
/*  758 */         filterChain.release();
/*      */     } catch (Throwable e) {
/*  760 */       ExceptionUtils.handleThrowable(e);
/*  761 */       this.wrapper.getLogger().error(sm.getString("standardWrapper.releaseFilters", new Object[] {this.wrapper
/*  762 */         .getName() }), e);
/*      */     }
/*      */     
/*      */ 
/*      */     try
/*      */     {
/*  768 */       if (servlet != null) {
/*  769 */         this.wrapper.deallocate(servlet);
/*      */       }
/*      */     } catch (ServletException e) {
/*  772 */       this.wrapper.getLogger().error(sm.getString("applicationDispatcher.deallocateException", new Object[] {this.wrapper
/*  773 */         .getName() }), e);
/*  774 */       servletException = e;
/*      */     } catch (Throwable e) {
/*  776 */       ExceptionUtils.handleThrowable(e);
/*  777 */       this.wrapper.getLogger().error(sm.getString("applicationDispatcher.deallocateException", new Object[] {this.wrapper
/*  778 */         .getName() }), e);
/*      */       
/*  780 */       servletException = new ServletException(sm.getString("applicationDispatcher.deallocateException", new Object[] {this.wrapper
/*  781 */         .getName() }), e);
/*      */     }
/*      */     
/*      */ 
/*  785 */     this.context.unbind(false, oldCCL);
/*      */     
/*      */ 
/*      */ 
/*  789 */     unwrapRequest(state);
/*  790 */     unwrapResponse(state);
/*      */     
/*  792 */     recycleRequestWrapper(state);
/*      */     
/*      */ 
/*  795 */     if (ioException != null)
/*  796 */       throw ioException;
/*  797 */     if (servletException != null)
/*  798 */       throw servletException;
/*  799 */     if (runtimeException != null) {
/*  800 */       throw runtimeException;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void unwrapRequest(State state)
/*      */   {
/*  810 */     if (state.wrapRequest == null) {
/*  811 */       return;
/*      */     }
/*  813 */     if ((state.outerRequest.isAsyncStarted()) && 
/*  814 */       (!state.outerRequest.getAsyncContext().hasOriginalRequestAndResponse())) {
/*  815 */       return;
/*      */     }
/*      */     
/*      */ 
/*  819 */     ServletRequest previous = null;
/*  820 */     ServletRequest current = state.outerRequest;
/*  821 */     while (current != null)
/*      */     {
/*      */ 
/*  824 */       if (((current instanceof Request)) || ((current instanceof RequestFacade))) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/*  829 */       if (current == state.wrapRequest)
/*      */       {
/*  831 */         ServletRequest next = ((ServletRequestWrapper)current).getRequest();
/*  832 */         if (previous == null) {
/*  833 */           state.outerRequest = next; break;
/*      */         }
/*  835 */         ((ServletRequestWrapper)previous).setRequest(next);
/*  836 */         break;
/*      */       }
/*      */       
/*      */ 
/*  840 */       previous = current;
/*  841 */       current = ((ServletRequestWrapper)current).getRequest();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void unwrapResponse(State state)
/*      */   {
/*  852 */     if (state.wrapResponse == null) {
/*  853 */       return;
/*      */     }
/*  855 */     if ((state.outerRequest.isAsyncStarted()) && 
/*  856 */       (!state.outerRequest.getAsyncContext().hasOriginalRequestAndResponse())) {
/*  857 */       return;
/*      */     }
/*      */     
/*      */ 
/*  861 */     ServletResponse previous = null;
/*  862 */     ServletResponse current = state.outerResponse;
/*  863 */     while (current != null)
/*      */     {
/*      */ 
/*  866 */       if (((current instanceof Response)) || ((current instanceof ResponseFacade))) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/*  871 */       if (current == state.wrapResponse)
/*      */       {
/*  873 */         ServletResponse next = ((ServletResponseWrapper)current).getResponse();
/*  874 */         if (previous == null) {
/*  875 */           state.outerResponse = next; break;
/*      */         }
/*  877 */         ((ServletResponseWrapper)previous).setResponse(next);
/*  878 */         break;
/*      */       }
/*      */       
/*      */ 
/*  882 */       previous = current;
/*  883 */       current = ((ServletResponseWrapper)current).getResponse();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private ServletRequest wrapRequest(State state)
/*      */   {
/*  897 */     ServletRequest previous = null;
/*  898 */     ServletRequest current = state.outerRequest;
/*  899 */     while (current != null) {
/*  900 */       if ((state.hrequest == null) && ((current instanceof javax.servlet.http.HttpServletRequest)))
/*  901 */         state.hrequest = ((javax.servlet.http.HttpServletRequest)current);
/*  902 */       if (!(current instanceof ServletRequestWrapper))
/*      */         break;
/*  904 */       if ((current instanceof ApplicationHttpRequest))
/*      */         break;
/*  906 */       if ((current instanceof ApplicationRequest))
/*      */         break;
/*  908 */       previous = current;
/*  909 */       current = ((ServletRequestWrapper)current).getRequest();
/*      */     }
/*      */     
/*      */ 
/*  913 */     ServletRequest wrapper = null;
/*  914 */     if (((current instanceof ApplicationHttpRequest)) || ((current instanceof Request)) || ((current instanceof javax.servlet.http.HttpServletRequest)))
/*      */     {
/*      */ 
/*      */ 
/*  918 */       javax.servlet.http.HttpServletRequest hcurrent = (javax.servlet.http.HttpServletRequest)current;
/*  919 */       boolean crossContext = false;
/*  920 */       if (((state.outerRequest instanceof ApplicationHttpRequest)) || ((state.outerRequest instanceof Request)) || ((state.outerRequest instanceof javax.servlet.http.HttpServletRequest)))
/*      */       {
/*      */ 
/*  923 */         javax.servlet.http.HttpServletRequest houterRequest = (javax.servlet.http.HttpServletRequest)state.outerRequest;
/*      */         
/*  925 */         Object contextPath = houterRequest.getAttribute("javax.servlet.include.context_path");
/*      */         
/*  927 */         if (contextPath == null)
/*      */         {
/*  929 */           contextPath = houterRequest.getContextPath();
/*      */         }
/*  931 */         crossContext = !this.context.getPath().equals(contextPath);
/*      */       }
/*  933 */       wrapper = new ApplicationHttpRequest(hcurrent, this.context, crossContext);
/*      */     }
/*      */     else {
/*  936 */       wrapper = new ApplicationRequest(current);
/*      */     }
/*  938 */     if (previous == null) {
/*  939 */       state.outerRequest = wrapper;
/*      */     } else
/*  941 */       ((ServletRequestWrapper)previous).setRequest(wrapper);
/*  942 */     state.wrapRequest = wrapper;
/*  943 */     return wrapper;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private ServletResponse wrapResponse(State state)
/*      */   {
/*  955 */     ServletResponse previous = null;
/*  956 */     ServletResponse current = state.outerResponse;
/*  957 */     while (current != null) {
/*  958 */       if ((state.hresponse == null) && ((current instanceof HttpServletResponse))) {
/*  959 */         state.hresponse = ((HttpServletResponse)current);
/*  960 */         if (!state.including)
/*  961 */           return null;
/*      */       }
/*  963 */       if (!(current instanceof ServletResponseWrapper))
/*      */         break;
/*  965 */       if ((current instanceof ApplicationHttpResponse))
/*      */         break;
/*  967 */       if ((current instanceof ApplicationResponse))
/*      */         break;
/*  969 */       previous = current;
/*  970 */       current = ((ServletResponseWrapper)current).getResponse();
/*      */     }
/*      */     
/*      */ 
/*  974 */     ServletResponse wrapper = null;
/*  975 */     if (((current instanceof ApplicationHttpResponse)) || ((current instanceof Response)) || ((current instanceof HttpServletResponse)))
/*      */     {
/*      */ 
/*  978 */       wrapper = new ApplicationHttpResponse((HttpServletResponse)current, state.including);
/*      */     }
/*      */     else
/*      */     {
/*  982 */       wrapper = new ApplicationResponse(current, state.including); }
/*  983 */     if (previous == null) {
/*  984 */       state.outerResponse = wrapper;
/*      */     } else
/*  986 */       ((ServletResponseWrapper)previous).setResponse(wrapper);
/*  987 */     state.wrapResponse = wrapper;
/*  988 */     return wrapper;
/*      */   }
/*      */   
/*      */ 
/*      */   private void checkSameObjects(ServletRequest appRequest, ServletResponse appResponse)
/*      */     throws ServletException
/*      */   {
/*  995 */     ServletRequest originalRequest = ApplicationFilterChain.getLastServicedRequest();
/*      */     
/*  997 */     ServletResponse originalResponse = ApplicationFilterChain.getLastServicedResponse();
/*      */     
/*      */ 
/* 1000 */     if ((originalRequest == null) || (originalResponse == null)) {
/* 1001 */       return;
/*      */     }
/*      */     
/* 1004 */     boolean same = false;
/* 1005 */     ServletRequest dispatchedRequest = appRequest;
/*      */     
/*      */ 
/* 1008 */     while (((originalRequest instanceof ServletRequestWrapper)) && 
/* 1009 */       (((ServletRequestWrapper)originalRequest).getRequest() != null))
/*      */     {
/* 1011 */       originalRequest = ((ServletRequestWrapper)originalRequest).getRequest();
/*      */     }
/*      */     
/* 1014 */     while (!same) {
/* 1015 */       if (originalRequest.equals(dispatchedRequest)) {
/* 1016 */         same = true;
/*      */       }
/* 1018 */       if ((same) || (!(dispatchedRequest instanceof ServletRequestWrapper)))
/*      */         break;
/* 1020 */       dispatchedRequest = ((ServletRequestWrapper)dispatchedRequest).getRequest();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1025 */     if (!same) {
/* 1026 */       throw new ServletException(sm.getString("applicationDispatcher.specViolation.request"));
/*      */     }
/*      */     
/*      */ 
/* 1030 */     same = false;
/* 1031 */     ServletResponse dispatchedResponse = appResponse;
/*      */     
/*      */ 
/* 1034 */     while (((originalResponse instanceof ServletResponseWrapper)) && 
/* 1035 */       (((ServletResponseWrapper)originalResponse).getResponse() != null))
/*      */     {
/*      */ 
/* 1038 */       originalResponse = ((ServletResponseWrapper)originalResponse).getResponse();
/*      */     }
/*      */     
/* 1041 */     while (!same) {
/* 1042 */       if (originalResponse.equals(dispatchedResponse)) {
/* 1043 */         same = true;
/*      */       }
/*      */       
/* 1046 */       if ((same) || (!(dispatchedResponse instanceof ServletResponseWrapper)))
/*      */         break;
/* 1048 */       dispatchedResponse = ((ServletResponseWrapper)dispatchedResponse).getResponse();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1054 */     if (!same) {
/* 1055 */       throw new ServletException(sm.getString("applicationDispatcher.specViolation.response"));
/*      */     }
/*      */   }
/*      */   
/*      */   private void recycleRequestWrapper(State state)
/*      */   {
/* 1061 */     if ((state.wrapRequest instanceof ApplicationHttpRequest)) {
/* 1062 */       ((ApplicationHttpRequest)state.wrapRequest).recycle();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\ApplicationDispatcher.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */