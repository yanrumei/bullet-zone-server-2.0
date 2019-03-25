/*     */ package org.springframework.boot.web.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.servlet.Filter;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.FilterConfig;
/*     */ import javax.servlet.RequestDispatcher;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpServletResponseWrapper;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.boot.web.servlet.ErrorPage;
/*     */ import org.springframework.boot.web.servlet.ErrorPageRegistry;
/*     */ import org.springframework.core.annotation.Order;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.web.filter.OncePerRequestFilter;
/*     */ import org.springframework.web.util.NestedServletException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Order(Integer.MIN_VALUE)
/*     */ public class ErrorPageFilter
/*     */   implements Filter, ErrorPageRegistry
/*     */ {
/*  61 */   private static final Log logger = LogFactory.getLog(ErrorPageFilter.class);
/*     */   
/*     */ 
/*     */   private static final String ERROR_EXCEPTION = "javax.servlet.error.exception";
/*     */   
/*     */ 
/*     */   private static final String ERROR_EXCEPTION_TYPE = "javax.servlet.error.exception_type";
/*     */   
/*     */ 
/*     */   private static final String ERROR_MESSAGE = "javax.servlet.error.message";
/*     */   
/*     */ 
/*     */   public static final String ERROR_REQUEST_URI = "javax.servlet.error.request_uri";
/*     */   
/*     */ 
/*     */   private static final String ERROR_STATUS_CODE = "javax.servlet.error.status_code";
/*     */   
/*     */   private String global;
/*     */   
/*  80 */   private final Map<Integer, String> statuses = new HashMap();
/*     */   
/*  82 */   private final Map<Class<?>, String> exceptions = new HashMap();
/*     */   
/*  84 */   private final OncePerRequestFilter delegate = new OncePerRequestFilter()
/*     */   {
/*     */ 
/*     */     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
/*     */       throws ServletException, IOException
/*     */     {
/*  90 */       ErrorPageFilter.this.doFilter(request, response, chain);
/*     */     }
/*     */     
/*     */     protected boolean shouldNotFilterAsyncDispatch()
/*     */     {
/*  95 */       return false;
/*     */     }
/*     */   };
/*     */   
/*     */   public void init(FilterConfig filterConfig)
/*     */     throws ServletException
/*     */   {
/* 102 */     this.delegate.init(filterConfig);
/*     */   }
/*     */   
/*     */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
/*     */     throws IOException, ServletException
/*     */   {
/* 108 */     this.delegate.doFilter(request, response, chain);
/*     */   }
/*     */   
/*     */   private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException
/*     */   {
/* 113 */     ErrorWrapperResponse wrapped = new ErrorWrapperResponse(response);
/*     */     try {
/* 115 */       chain.doFilter(request, wrapped);
/* 116 */       if (wrapped.hasErrorToSend()) {
/* 117 */         handleErrorStatus(request, response, wrapped.getStatus(), wrapped
/* 118 */           .getMessage());
/* 119 */         response.flushBuffer();
/*     */       }
/* 121 */       else if ((!request.isAsyncStarted()) && (!response.isCommitted())) {
/* 122 */         response.flushBuffer();
/*     */       }
/*     */     }
/*     */     catch (Throwable ex) {
/* 126 */       Throwable exceptionToHandle = ex;
/* 127 */       if ((ex instanceof NestedServletException)) {
/* 128 */         exceptionToHandle = ((NestedServletException)ex).getRootCause();
/*     */       }
/* 130 */       handleException(request, response, wrapped, exceptionToHandle);
/* 131 */       response.flushBuffer();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleErrorStatus(HttpServletRequest request, HttpServletResponse response, int status, String message)
/*     */     throws ServletException, IOException
/*     */   {
/* 138 */     if (response.isCommitted()) {
/* 139 */       handleCommittedResponse(request, null);
/* 140 */       return;
/*     */     }
/* 142 */     String errorPath = getErrorPath(this.statuses, Integer.valueOf(status));
/* 143 */     if (errorPath == null) {
/* 144 */       response.sendError(status, message);
/* 145 */       return;
/*     */     }
/* 147 */     response.setStatus(status);
/* 148 */     setErrorAttributes(request, status, message);
/* 149 */     request.getRequestDispatcher(errorPath).forward(request, response);
/*     */   }
/*     */   
/*     */   private void handleException(HttpServletRequest request, HttpServletResponse response, ErrorWrapperResponse wrapped, Throwable ex)
/*     */     throws IOException, ServletException
/*     */   {
/* 155 */     Class<?> type = ex.getClass();
/* 156 */     String errorPath = getErrorPath(type);
/* 157 */     if (errorPath == null) {
/* 158 */       rethrow(ex);
/* 159 */       return;
/*     */     }
/* 161 */     if (response.isCommitted()) {
/* 162 */       handleCommittedResponse(request, ex);
/* 163 */       return;
/*     */     }
/*     */     
/* 166 */     forwardToErrorPage(errorPath, request, wrapped, ex);
/*     */   }
/*     */   
/*     */   private void forwardToErrorPage(String path, HttpServletRequest request, HttpServletResponse response, Throwable ex)
/*     */     throws ServletException, IOException
/*     */   {
/* 172 */     if (logger.isErrorEnabled())
/*     */     {
/* 174 */       String message = "Forwarding to error page from request " + getDescription(request) + " due to exception [" + ex.getMessage() + "]";
/*     */       
/* 176 */       logger.error(message, ex);
/*     */     }
/* 178 */     setErrorAttributes(request, 500, ex.getMessage());
/* 179 */     request.setAttribute("javax.servlet.error.exception", ex);
/* 180 */     request.setAttribute("javax.servlet.error.exception_type", ex.getClass());
/* 181 */     response.reset();
/* 182 */     response.sendError(500, ex.getMessage());
/* 183 */     request.getRequestDispatcher(path).forward(request, response);
/* 184 */     request.removeAttribute("javax.servlet.error.exception");
/* 185 */     request.removeAttribute("javax.servlet.error.exception_type");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDescription(HttpServletRequest request)
/*     */   {
/* 196 */     return 
/* 197 */       "[" + request.getServletPath() + (request.getPathInfo() == null ? "" : request.getPathInfo()) + "]";
/*     */   }
/*     */   
/*     */   private void handleCommittedResponse(HttpServletRequest request, Throwable ex)
/*     */   {
/* 202 */     String message = "Cannot forward to error page for request " + getDescription(request) + " as the response has already been committed. As a result, the response may have the wrong status code. If your application is running on WebSphere Application Server you may be able to resolve this problem by setting com.ibm.ws.webcontainer.invokeFlushAfterService to false";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 207 */     if (ex == null) {
/* 208 */       logger.error(message);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 213 */       logger.error(message, ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private String getErrorPath(Map<Integer, String> map, Integer status) {
/* 218 */     if (map.containsKey(status)) {
/* 219 */       return (String)map.get(status);
/*     */     }
/* 221 */     return this.global;
/*     */   }
/*     */   
/*     */   private String getErrorPath(Class<?> type) {
/* 225 */     while (type != Object.class) {
/* 226 */       String path = (String)this.exceptions.get(type);
/* 227 */       if (path != null) {
/* 228 */         return path;
/*     */       }
/* 230 */       type = type.getSuperclass();
/*     */     }
/* 232 */     return this.global;
/*     */   }
/*     */   
/*     */   private void setErrorAttributes(HttpServletRequest request, int status, String message)
/*     */   {
/* 237 */     request.setAttribute("javax.servlet.error.status_code", Integer.valueOf(status));
/* 238 */     request.setAttribute("javax.servlet.error.message", message);
/* 239 */     request.setAttribute("javax.servlet.error.request_uri", request.getRequestURI());
/*     */   }
/*     */   
/*     */   private void rethrow(Throwable ex) throws IOException, ServletException {
/* 243 */     if ((ex instanceof RuntimeException)) {
/* 244 */       throw ((RuntimeException)ex);
/*     */     }
/* 246 */     if ((ex instanceof Error)) {
/* 247 */       throw ((Error)ex);
/*     */     }
/* 249 */     if ((ex instanceof IOException)) {
/* 250 */       throw ((IOException)ex);
/*     */     }
/* 252 */     if ((ex instanceof ServletException)) {
/* 253 */       throw ((ServletException)ex);
/*     */     }
/* 255 */     throw new IllegalStateException(ex);
/*     */   }
/*     */   
/*     */   public void addErrorPages(ErrorPage... errorPages)
/*     */   {
/* 260 */     for (ErrorPage errorPage : errorPages) {
/* 261 */       if (errorPage.isGlobal()) {
/* 262 */         this.global = errorPage.getPath();
/*     */       }
/* 264 */       else if (errorPage.getStatus() != null) {
/* 265 */         this.statuses.put(Integer.valueOf(errorPage.getStatus().value()), errorPage.getPath());
/*     */       }
/*     */       else {
/* 268 */         this.exceptions.put(errorPage.getException(), errorPage.getPath());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void destroy() {}
/*     */   
/*     */ 
/*     */   private static class ErrorWrapperResponse
/*     */     extends HttpServletResponseWrapper
/*     */   {
/*     */     private int status;
/*     */     
/*     */     private String message;
/* 283 */     private boolean hasErrorToSend = false;
/*     */     
/*     */     ErrorWrapperResponse(HttpServletResponse response) {
/* 286 */       super();
/*     */     }
/*     */     
/*     */     public void sendError(int status) throws IOException
/*     */     {
/* 291 */       sendError(status, null);
/*     */     }
/*     */     
/*     */     public void sendError(int status, String message) throws IOException
/*     */     {
/* 296 */       this.status = status;
/* 297 */       this.message = message;
/* 298 */       this.hasErrorToSend = true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public int getStatus()
/*     */     {
/* 305 */       if (this.hasErrorToSend) {
/* 306 */         return this.status;
/*     */       }
/*     */       
/* 309 */       return super.getStatus();
/*     */     }
/*     */     
/*     */     public void flushBuffer() throws IOException
/*     */     {
/* 314 */       if ((this.hasErrorToSend) && (!isCommitted())) {
/* 315 */         ((HttpServletResponse)getResponse()).sendError(this.status, this.message);
/*     */       }
/*     */       
/* 318 */       super.flushBuffer();
/*     */     }
/*     */     
/*     */     public String getMessage() {
/* 322 */       return this.message;
/*     */     }
/*     */     
/*     */     public boolean hasErrorToSend() {
/* 326 */       return this.hasErrorToSend;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\support\ErrorPageFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */