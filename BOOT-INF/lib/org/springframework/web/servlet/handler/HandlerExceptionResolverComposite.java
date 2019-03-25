/*    */ package org.springframework.web.servlet.handler;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.core.Ordered;
/*    */ import org.springframework.web.servlet.HandlerExceptionResolver;
/*    */ import org.springframework.web.servlet.ModelAndView;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HandlerExceptionResolverComposite
/*    */   implements HandlerExceptionResolver, Ordered
/*    */ {
/*    */   private List<HandlerExceptionResolver> resolvers;
/* 38 */   private int order = Integer.MAX_VALUE;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers)
/*    */   {
/* 45 */     this.resolvers = exceptionResolvers;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public List<HandlerExceptionResolver> getExceptionResolvers()
/*    */   {
/* 52 */     return this.resolvers != null ? Collections.unmodifiableList(this.resolvers) : 
/* 53 */       Collections.emptyList();
/*    */   }
/*    */   
/*    */   public void setOrder(int order) {
/* 57 */     this.order = order;
/*    */   }
/*    */   
/*    */   public int getOrder()
/*    */   {
/* 62 */     return this.order;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/*    */   {
/* 74 */     if (this.resolvers != null) {
/* 75 */       for (HandlerExceptionResolver handlerExceptionResolver : this.resolvers) {
/* 76 */         ModelAndView mav = handlerExceptionResolver.resolveException(request, response, handler, ex);
/* 77 */         if (mav != null) {
/* 78 */           return mav;
/*    */         }
/*    */       }
/*    */     }
/* 82 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\handler\HandlerExceptionResolverComposite.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */