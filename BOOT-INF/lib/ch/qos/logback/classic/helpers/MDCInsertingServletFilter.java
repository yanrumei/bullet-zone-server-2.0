/*    */ package ch.qos.logback.classic.helpers;
/*    */ 
/*    */ import javax.servlet.Filter;
/*    */ import javax.servlet.FilterConfig;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.ServletRequest;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.slf4j.MDC;
/*    */ 
/*    */ public class MDCInsertingServletFilter
/*    */   implements Filter
/*    */ {
/*    */   public void destroy() {}
/*    */   
/*    */   /* Error */
/*    */   public void doFilter(ServletRequest request, javax.servlet.ServletResponse response, javax.servlet.FilterChain chain)
/*    */     throws java.io.IOException, ServletException
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: invokevirtual 24	ch/qos/logback/classic/helpers/MDCInsertingServletFilter:insertIntoMDC	(Ljavax/servlet/ServletRequest;)V
/*    */     //   5: aload_3
/*    */     //   6: aload_1
/*    */     //   7: aload_2
/*    */     //   8: invokeinterface 28 3 0
/*    */     //   13: goto +12 -> 25
/*    */     //   16: astore 4
/*    */     //   18: aload_0
/*    */     //   19: invokevirtual 33	ch/qos/logback/classic/helpers/MDCInsertingServletFilter:clearMDC	()V
/*    */     //   22: aload 4
/*    */     //   24: athrow
/*    */     //   25: aload_0
/*    */     //   26: invokevirtual 33	ch/qos/logback/classic/helpers/MDCInsertingServletFilter:clearMDC	()V
/*    */     //   29: return
/*    */     // Line number table:
/*    */     //   Java source line #47	-> byte code offset #0
/*    */     //   Java source line #49	-> byte code offset #5
/*    */     //   Java source line #50	-> byte code offset #13
/*    */     //   Java source line #51	-> byte code offset #18
/*    */     //   Java source line #52	-> byte code offset #22
/*    */     //   Java source line #51	-> byte code offset #25
/*    */     //   Java source line #53	-> byte code offset #29
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	30	0	this	MDCInsertingServletFilter
/*    */     //   0	30	1	request	ServletRequest
/*    */     //   0	30	2	response	javax.servlet.ServletResponse
/*    */     //   0	30	3	chain	javax.servlet.FilterChain
/*    */     //   16	7	4	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   5	16	16	finally
/*    */   }
/*    */   
/*    */   void insertIntoMDC(ServletRequest request)
/*    */   {
/* 57 */     MDC.put("req.remoteHost", request.getRemoteHost());
/*    */     
/* 59 */     if ((request instanceof HttpServletRequest)) {
/* 60 */       HttpServletRequest httpServletRequest = (HttpServletRequest)request;
/* 61 */       MDC.put("req.requestURI", httpServletRequest.getRequestURI());
/* 62 */       StringBuffer requestURL = httpServletRequest.getRequestURL();
/* 63 */       if (requestURL != null) {
/* 64 */         MDC.put("req.requestURL", requestURL.toString());
/*    */       }
/* 66 */       MDC.put("req.method", httpServletRequest.getMethod());
/* 67 */       MDC.put("req.queryString", httpServletRequest.getQueryString());
/* 68 */       MDC.put("req.userAgent", httpServletRequest.getHeader("User-Agent"));
/* 69 */       MDC.put("req.xForwardedFor", httpServletRequest.getHeader("X-Forwarded-For"));
/*    */     }
/*    */   }
/*    */   
/*    */   void clearMDC()
/*    */   {
/* 75 */     MDC.remove("req.remoteHost");
/* 76 */     MDC.remove("req.requestURI");
/* 77 */     MDC.remove("req.queryString");
/*    */     
/* 79 */     MDC.remove("req.requestURL");
/* 80 */     MDC.remove("req.method");
/* 81 */     MDC.remove("req.userAgent");
/* 82 */     MDC.remove("req.xForwardedFor");
/*    */   }
/*    */   
/*    */   public void init(FilterConfig arg0)
/*    */     throws ServletException
/*    */   {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\helpers\MDCInsertingServletFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */