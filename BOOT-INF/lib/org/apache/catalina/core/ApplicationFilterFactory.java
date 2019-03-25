/*     */ package org.apache.catalina.core;
/*     */ 
/*     */ import javax.servlet.DispatcherType;
/*     */ import javax.servlet.Servlet;
/*     */ import javax.servlet.ServletRequest;
/*     */ import org.apache.catalina.Globals;
/*     */ import org.apache.catalina.Wrapper;
/*     */ import org.apache.catalina.connector.Request;
/*     */ import org.apache.tomcat.util.descriptor.web.FilterMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ApplicationFilterFactory
/*     */ {
/*     */   public static ApplicationFilterChain createFilterChain(ServletRequest request, Wrapper wrapper, Servlet servlet)
/*     */   {
/*  57 */     if (servlet == null) {
/*  58 */       return null;
/*     */     }
/*     */     
/*  61 */     ApplicationFilterChain filterChain = null;
/*  62 */     if ((request instanceof Request)) {
/*  63 */       Request req = (Request)request;
/*  64 */       if (Globals.IS_SECURITY_ENABLED)
/*     */       {
/*  66 */         filterChain = new ApplicationFilterChain();
/*     */       } else {
/*  68 */         filterChain = (ApplicationFilterChain)req.getFilterChain();
/*  69 */         if (filterChain == null) {
/*  70 */           filterChain = new ApplicationFilterChain();
/*  71 */           req.setFilterChain(filterChain);
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/*  76 */       filterChain = new ApplicationFilterChain();
/*     */     }
/*     */     
/*  79 */     filterChain.setServlet(servlet);
/*  80 */     filterChain.setServletSupportsAsync(wrapper.isAsyncSupported());
/*     */     
/*     */ 
/*  83 */     StandardContext context = (StandardContext)wrapper.getParent();
/*  84 */     FilterMap[] filterMaps = context.findFilterMaps();
/*     */     
/*     */ 
/*  87 */     if ((filterMaps == null) || (filterMaps.length == 0)) {
/*  88 */       return filterChain;
/*     */     }
/*     */     
/*     */ 
/*  92 */     DispatcherType dispatcher = (DispatcherType)request.getAttribute("org.apache.catalina.core.DISPATCHER_TYPE");
/*     */     
/*  94 */     String requestPath = null;
/*  95 */     Object attribute = request.getAttribute("org.apache.catalina.core.DISPATCHER_REQUEST_PATH");
/*  96 */     if (attribute != null) {
/*  97 */       requestPath = attribute.toString();
/*     */     }
/*     */     
/* 100 */     String servletName = wrapper.getName();
/*     */     
/*     */ 
/* 103 */     for (int i = 0; i < filterMaps.length; i++) {
/* 104 */       if (matchDispatcher(filterMaps[i], dispatcher))
/*     */       {
/*     */ 
/* 107 */         if (matchFiltersURL(filterMaps[i], requestPath))
/*     */         {
/*     */ 
/* 110 */           ApplicationFilterConfig filterConfig = (ApplicationFilterConfig)context.findFilterConfig(filterMaps[i].getFilterName());
/* 111 */           if (filterConfig != null)
/*     */           {
/*     */ 
/*     */ 
/* 115 */             filterChain.addFilter(filterConfig); }
/*     */         }
/*     */       }
/*     */     }
/* 119 */     for (int i = 0; i < filterMaps.length; i++) {
/* 120 */       if (matchDispatcher(filterMaps[i], dispatcher))
/*     */       {
/*     */ 
/* 123 */         if (matchFiltersServlet(filterMaps[i], servletName))
/*     */         {
/*     */ 
/* 126 */           ApplicationFilterConfig filterConfig = (ApplicationFilterConfig)context.findFilterConfig(filterMaps[i].getFilterName());
/* 127 */           if (filterConfig != null)
/*     */           {
/*     */ 
/*     */ 
/* 131 */             filterChain.addFilter(filterConfig); }
/*     */         }
/*     */       }
/*     */     }
/* 135 */     return filterChain;
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
/*     */   private static boolean matchFiltersURL(FilterMap filterMap, String requestPath)
/*     */   {
/* 154 */     if (filterMap.getMatchAllUrlPatterns()) {
/* 155 */       return true;
/*     */     }
/* 157 */     if (requestPath == null) {
/* 158 */       return false;
/*     */     }
/*     */     
/* 161 */     String[] testPaths = filterMap.getURLPatterns();
/*     */     
/* 163 */     for (int i = 0; i < testPaths.length; i++) {
/* 164 */       if (matchFiltersURL(testPaths[i], requestPath)) {
/* 165 */         return true;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 170 */     return false;
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
/*     */   private static boolean matchFiltersURL(String testPath, String requestPath)
/*     */   {
/* 185 */     if (testPath == null) {
/* 186 */       return false;
/*     */     }
/*     */     
/* 189 */     if (testPath.equals(requestPath)) {
/* 190 */       return true;
/*     */     }
/*     */     
/* 193 */     if (testPath.equals("/*"))
/* 194 */       return true;
/* 195 */     if (testPath.endsWith("/*")) {
/* 196 */       if (testPath.regionMatches(0, requestPath, 0, testPath
/* 197 */         .length() - 2)) {
/* 198 */         if (requestPath.length() == testPath.length() - 2)
/* 199 */           return true;
/* 200 */         if ('/' == requestPath.charAt(testPath.length() - 2)) {
/* 201 */           return true;
/*     */         }
/*     */       }
/* 204 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 208 */     if (testPath.startsWith("*.")) {
/* 209 */       int slash = requestPath.lastIndexOf('/');
/* 210 */       int period = requestPath.lastIndexOf('.');
/* 211 */       if ((slash >= 0) && (period > slash) && 
/* 212 */         (period != requestPath.length() - 1))
/*     */       {
/* 214 */         if (requestPath.length() - period == testPath.length() - 1) {
/* 215 */           return testPath.regionMatches(2, requestPath, period + 1, testPath
/* 216 */             .length() - 2);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 221 */     return false;
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
/*     */   private static boolean matchFiltersServlet(FilterMap filterMap, String servletName)
/*     */   {
/* 237 */     if (servletName == null) {
/* 238 */       return false;
/*     */     }
/*     */     
/* 241 */     if (filterMap.getMatchAllServletNames()) {
/* 242 */       return true;
/*     */     }
/* 244 */     String[] servletNames = filterMap.getServletNames();
/* 245 */     for (int i = 0; i < servletNames.length; i++) {
/* 246 */       if (servletName.equals(servletNames[i])) {
/* 247 */         return true;
/*     */       }
/*     */     }
/* 250 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean matchDispatcher(FilterMap filterMap, DispatcherType type)
/*     */   {
/* 261 */     switch (type) {
/*     */     case FORWARD: 
/* 263 */       if ((filterMap.getDispatcherMapping() & 0x2) != 0) {
/* 264 */         return true;
/*     */       }
/*     */       break;
/*     */     case INCLUDE: 
/* 268 */       if ((filterMap.getDispatcherMapping() & 0x4) != 0) {
/* 269 */         return true;
/*     */       }
/*     */       break;
/*     */     case REQUEST: 
/* 273 */       if ((filterMap.getDispatcherMapping() & 0x8) != 0) {
/* 274 */         return true;
/*     */       }
/*     */       break;
/*     */     case ERROR: 
/* 278 */       if ((filterMap.getDispatcherMapping() & 0x1) != 0) {
/* 279 */         return true;
/*     */       }
/*     */       break;
/*     */     case ASYNC: 
/* 283 */       if ((filterMap.getDispatcherMapping() & 0x10) != 0) {
/* 284 */         return true;
/*     */       }
/*     */       break;
/*     */     }
/* 288 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\ApplicationFilterFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */