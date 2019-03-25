/*     */ package org.springframework.web.servlet;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HandlerExecutionChain
/*     */ {
/*  40 */   private static final Log logger = LogFactory.getLog(HandlerExecutionChain.class);
/*     */   
/*     */   private final Object handler;
/*     */   
/*     */   private HandlerInterceptor[] interceptors;
/*     */   
/*     */   private List<HandlerInterceptor> interceptorList;
/*     */   
/*  48 */   private int interceptorIndex = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HandlerExecutionChain(Object handler)
/*     */   {
/*  56 */     this(handler, (HandlerInterceptor[])null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HandlerExecutionChain(Object handler, HandlerInterceptor... interceptors)
/*     */   {
/*  66 */     if ((handler instanceof HandlerExecutionChain)) {
/*  67 */       HandlerExecutionChain originalChain = (HandlerExecutionChain)handler;
/*  68 */       this.handler = originalChain.getHandler();
/*  69 */       this.interceptorList = new ArrayList();
/*  70 */       CollectionUtils.mergeArrayIntoCollection(originalChain.getInterceptors(), this.interceptorList);
/*  71 */       CollectionUtils.mergeArrayIntoCollection(interceptors, this.interceptorList);
/*     */     }
/*     */     else {
/*  74 */       this.handler = handler;
/*  75 */       this.interceptors = interceptors;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getHandler()
/*     */   {
/*  85 */     return this.handler;
/*     */   }
/*     */   
/*     */   public void addInterceptor(HandlerInterceptor interceptor) {
/*  89 */     initInterceptorList().add(interceptor);
/*     */   }
/*     */   
/*     */   public void addInterceptors(HandlerInterceptor... interceptors) {
/*  93 */     if (!ObjectUtils.isEmpty(interceptors)) {
/*  94 */       CollectionUtils.mergeArrayIntoCollection(interceptors, initInterceptorList());
/*     */     }
/*     */   }
/*     */   
/*     */   private List<HandlerInterceptor> initInterceptorList() {
/*  99 */     if (this.interceptorList == null) {
/* 100 */       this.interceptorList = new ArrayList();
/* 101 */       if (this.interceptors != null)
/*     */       {
/* 103 */         CollectionUtils.mergeArrayIntoCollection(this.interceptors, this.interceptorList);
/*     */       }
/*     */     }
/* 106 */     this.interceptors = null;
/* 107 */     return this.interceptorList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HandlerInterceptor[] getInterceptors()
/*     */   {
/* 115 */     if ((this.interceptors == null) && (this.interceptorList != null)) {
/* 116 */       this.interceptors = ((HandlerInterceptor[])this.interceptorList.toArray(new HandlerInterceptor[this.interceptorList.size()]));
/*     */     }
/* 118 */     return this.interceptors;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 129 */     HandlerInterceptor[] interceptors = getInterceptors();
/* 130 */     if (!ObjectUtils.isEmpty(interceptors)) {
/* 131 */       for (int i = 0; i < interceptors.length; i++) {
/* 132 */         HandlerInterceptor interceptor = interceptors[i];
/* 133 */         if (!interceptor.preHandle(request, response, this.handler)) {
/* 134 */           triggerAfterCompletion(request, response, null);
/* 135 */           return false;
/*     */         }
/* 137 */         this.interceptorIndex = i;
/*     */       }
/*     */     }
/* 140 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   void applyPostHandle(HttpServletRequest request, HttpServletResponse response, ModelAndView mv)
/*     */     throws Exception
/*     */   {
/* 147 */     HandlerInterceptor[] interceptors = getInterceptors();
/* 148 */     if (!ObjectUtils.isEmpty(interceptors)) {
/* 149 */       for (int i = interceptors.length - 1; i >= 0; i--) {
/* 150 */         HandlerInterceptor interceptor = interceptors[i];
/* 151 */         interceptor.postHandle(request, response, this.handler, mv);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, Exception ex)
/*     */     throws Exception
/*     */   {
/* 164 */     HandlerInterceptor[] interceptors = getInterceptors();
/* 165 */     if (!ObjectUtils.isEmpty(interceptors)) {
/* 166 */       for (int i = this.interceptorIndex; i >= 0; i--) {
/* 167 */         HandlerInterceptor interceptor = interceptors[i];
/*     */         try {
/* 169 */           interceptor.afterCompletion(request, response, this.handler, ex);
/*     */         }
/*     */         catch (Throwable ex2) {
/* 172 */           logger.error("HandlerInterceptor.afterCompletion threw exception", ex2);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void applyAfterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response)
/*     */   {
/* 182 */     HandlerInterceptor[] interceptors = getInterceptors();
/* 183 */     if (!ObjectUtils.isEmpty(interceptors)) {
/* 184 */       for (int i = interceptors.length - 1; i >= 0; i--) {
/* 185 */         if ((interceptors[i] instanceof AsyncHandlerInterceptor)) {
/*     */           try {
/* 187 */             AsyncHandlerInterceptor asyncInterceptor = (AsyncHandlerInterceptor)interceptors[i];
/* 188 */             asyncInterceptor.afterConcurrentHandlingStarted(request, response, this.handler);
/*     */           }
/*     */           catch (Throwable ex) {
/* 191 */             logger.error("Interceptor [" + interceptors[i] + "] failed in afterConcurrentHandlingStarted", ex);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 204 */     Object handler = getHandler();
/* 205 */     if (handler == null) {
/* 206 */       return "HandlerExecutionChain with no handler";
/*     */     }
/* 208 */     StringBuilder sb = new StringBuilder();
/* 209 */     sb.append("HandlerExecutionChain with handler [").append(handler).append("]");
/* 210 */     HandlerInterceptor[] interceptors = getInterceptors();
/* 211 */     if (!ObjectUtils.isEmpty(interceptors)) {
/* 212 */       sb.append(" and ").append(interceptors.length).append(" interceptor");
/* 213 */       if (interceptors.length > 1) {
/* 214 */         sb.append("s");
/*     */       }
/*     */     }
/* 217 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\HandlerExecutionChain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */