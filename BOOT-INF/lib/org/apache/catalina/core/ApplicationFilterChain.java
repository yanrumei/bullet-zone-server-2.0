/*     */ package org.apache.catalina.core;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.AccessController;
/*     */ import java.security.Principal;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Set;
/*     */ import javax.servlet.Filter;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.Servlet;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.catalina.Globals;
/*     */ import org.apache.catalina.security.SecurityUtil;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
/*     */ import org.apache.tomcat.util.descriptor.web.FilterDef;
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
/*     */ public final class ApplicationFilterChain
/*     */   implements FilterChain
/*     */ {
/*     */   private static final ThreadLocal<ServletRequest> lastServicedRequest;
/*     */   private static final ThreadLocal<ServletResponse> lastServicedResponse;
/*     */   public static final int INCREMENT = 10;
/*     */   
/*     */   static
/*     */   {
/*  54 */     if (ApplicationDispatcher.WRAP_SAME_OBJECT) {
/*  55 */       lastServicedRequest = new ThreadLocal();
/*  56 */       lastServicedResponse = new ThreadLocal();
/*     */     } else {
/*  58 */       lastServicedRequest = null;
/*  59 */       lastServicedResponse = null;
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
/*  74 */   private ApplicationFilterConfig[] filters = new ApplicationFilterConfig[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  81 */   private int pos = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  87 */   private int n = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  93 */   private Servlet servlet = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  99 */   private boolean servletSupportsAsync = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 105 */   private static final StringManager sm = StringManager.getManager("org.apache.catalina.core");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 112 */   private static final Class<?>[] classType = { ServletRequest.class, ServletResponse.class, FilterChain.class };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 119 */   private static final Class<?>[] classTypeUsedInService = { ServletRequest.class, ServletResponse.class };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doFilter(ServletRequest request, ServletResponse response)
/*     */     throws IOException, ServletException
/*     */   {
/* 140 */     if (Globals.IS_SECURITY_ENABLED) {
/* 141 */       final ServletRequest req = request;
/* 142 */       final ServletResponse res = response;
/*     */       try {
/* 144 */         AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public Void run()
/*     */             throws ServletException, IOException
/*     */           {
/* 149 */             ApplicationFilterChain.this.internalDoFilter(req, res);
/* 150 */             return null;
/*     */           }
/*     */         });
/*     */       }
/*     */       catch (PrivilegedActionException pe) {
/* 155 */         Exception e = pe.getException();
/* 156 */         if ((e instanceof ServletException))
/* 157 */           throw ((ServletException)e);
/* 158 */         if ((e instanceof IOException))
/* 159 */           throw ((IOException)e);
/* 160 */         if ((e instanceof RuntimeException)) {
/* 161 */           throw ((RuntimeException)e);
/*     */         }
/* 163 */         throw new ServletException(e.getMessage(), e);
/*     */       }
/*     */     } else {
/* 166 */       internalDoFilter(request, response);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void internalDoFilter(ServletRequest request, ServletResponse response)
/*     */     throws IOException, ServletException
/*     */   {
/* 175 */     if (this.pos < this.n) {
/* 176 */       ApplicationFilterConfig filterConfig = this.filters[(this.pos++)];
/*     */       try {
/* 178 */         Filter filter = filterConfig.getFilter();
/*     */         
/* 180 */         if ((request.isAsyncSupported()) && ("false".equalsIgnoreCase(filterConfig
/* 181 */           .getFilterDef().getAsyncSupported()))) {
/* 182 */           request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", Boolean.FALSE);
/*     */         }
/* 184 */         if (Globals.IS_SECURITY_ENABLED) {
/* 185 */           ServletRequest req = request;
/* 186 */           ServletResponse res = response;
/*     */           
/* 188 */           Principal principal = ((HttpServletRequest)req).getUserPrincipal();
/*     */           
/* 190 */           Object[] args = { req, res, this };
/* 191 */           SecurityUtil.doAsPrivilege("doFilter", filter, classType, args, principal);
/*     */         } else {
/* 193 */           filter.doFilter(request, response, this);
/*     */         }
/*     */       } catch (IOException|ServletException|RuntimeException e) {
/* 196 */         throw e;
/*     */       } catch (Throwable e) {
/* 198 */         e = ExceptionUtils.unwrapInvocationTargetException(e);
/* 199 */         ExceptionUtils.handleThrowable(e);
/* 200 */         throw new ServletException(sm.getString("filterChain.filter"), e);
/*     */       }
/* 202 */       return;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 207 */       if (ApplicationDispatcher.WRAP_SAME_OBJECT) {
/* 208 */         lastServicedRequest.set(request);
/* 209 */         lastServicedResponse.set(response);
/*     */       }
/*     */       
/* 212 */       if ((request.isAsyncSupported()) && (!this.servletSupportsAsync)) {
/* 213 */         request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", Boolean.FALSE);
/*     */       }
/*     */       
/*     */ 
/* 217 */       if (((request instanceof HttpServletRequest)) && ((response instanceof HttpServletResponse)) && (Globals.IS_SECURITY_ENABLED))
/*     */       {
/*     */ 
/* 220 */         ServletRequest req = request;
/* 221 */         ServletResponse res = response;
/*     */         
/* 223 */         Principal principal = ((HttpServletRequest)req).getUserPrincipal();
/* 224 */         Object[] args = { req, res };
/* 225 */         SecurityUtil.doAsPrivilege("service", this.servlet, classTypeUsedInService, args, principal);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/* 231 */         this.servlet.service(request, response);
/*     */       }
/*     */     } catch (IOException|ServletException|RuntimeException e) {
/* 234 */       throw e;
/*     */     } catch (Throwable e) {
/* 236 */       e = ExceptionUtils.unwrapInvocationTargetException(e);
/* 237 */       ExceptionUtils.handleThrowable(e);
/* 238 */       throw new ServletException(sm.getString("filterChain.servlet"), e);
/*     */     } finally {
/* 240 */       if (ApplicationDispatcher.WRAP_SAME_OBJECT) {
/* 241 */         lastServicedRequest.set(null);
/* 242 */         lastServicedResponse.set(null);
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
/*     */   public static ServletRequest getLastServicedRequest()
/*     */   {
/* 255 */     return (ServletRequest)lastServicedRequest.get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ServletResponse getLastServicedResponse()
/*     */   {
/* 266 */     return (ServletResponse)lastServicedResponse.get();
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
/*     */   void addFilter(ApplicationFilterConfig filterConfig)
/*     */   {
/* 280 */     for (ApplicationFilterConfig filter : this.filters) {
/* 281 */       if (filter == filterConfig)
/* 282 */         return;
/*     */     }
/* 284 */     if (this.n == this.filters.length) {
/* 285 */       ApplicationFilterConfig[] newFilters = new ApplicationFilterConfig[this.n + 10];
/*     */       
/* 287 */       System.arraycopy(this.filters, 0, newFilters, 0, this.n);
/* 288 */       this.filters = newFilters;
/*     */     }
/* 290 */     this.filters[(this.n++)] = filterConfig;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void release()
/*     */   {
/* 299 */     for (int i = 0; i < this.n; i++) {
/* 300 */       this.filters[i] = null;
/*     */     }
/* 302 */     this.n = 0;
/* 303 */     this.pos = 0;
/* 304 */     this.servlet = null;
/* 305 */     this.servletSupportsAsync = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void reuse()
/*     */   {
/* 313 */     this.pos = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void setServlet(Servlet servlet)
/*     */   {
/* 323 */     this.servlet = servlet;
/*     */   }
/*     */   
/*     */   void setServletSupportsAsync(boolean servletSupportsAsync)
/*     */   {
/* 328 */     this.servletSupportsAsync = servletSupportsAsync;
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
/*     */   public void findNonAsyncFilters(Set<String> result)
/*     */   {
/* 341 */     for (int i = 0; i < this.n; i++) {
/* 342 */       ApplicationFilterConfig filter = this.filters[i];
/* 343 */       if ("false".equalsIgnoreCase(filter.getFilterDef().getAsyncSupported())) {
/* 344 */         result.add(filter.getFilterClass());
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\ApplicationFilterChain.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */