/*    */ package org.springframework.web.servlet.handler;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.web.method.HandlerMethod;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractHandlerMethodExceptionResolver
/*    */   extends AbstractHandlerExceptionResolver
/*    */ {
/*    */   protected boolean shouldApplyTo(HttpServletRequest request, Object handler)
/*    */   {
/* 42 */     if (handler == null) {
/* 43 */       return super.shouldApplyTo(request, handler);
/*    */     }
/* 45 */     if ((handler instanceof HandlerMethod)) {
/* 46 */       HandlerMethod handlerMethod = (HandlerMethod)handler;
/* 47 */       handler = handlerMethod.getBean();
/* 48 */       return super.shouldApplyTo(request, handler);
/*    */     }
/*    */     
/* 51 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected final ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/*    */   {
/* 59 */     return doResolveHandlerMethodException(request, response, (HandlerMethod)handler, ex);
/*    */   }
/*    */   
/*    */   protected abstract ModelAndView doResolveHandlerMethodException(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, HandlerMethod paramHandlerMethod, Exception paramException);
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\handler\AbstractHandlerMethodExceptionResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */