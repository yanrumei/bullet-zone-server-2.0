/*     */ package org.springframework.web.servlet.mvc.method;
/*     */ 
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.web.method.HandlerMethod;
/*     */ import org.springframework.web.servlet.HandlerAdapter;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ import org.springframework.web.servlet.support.WebContentGenerator;
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
/*     */ public abstract class AbstractHandlerMethodAdapter
/*     */   extends WebContentGenerator
/*     */   implements HandlerAdapter, Ordered
/*     */ {
/*  37 */   private int order = Integer.MAX_VALUE;
/*     */   
/*     */ 
/*     */   public AbstractHandlerMethodAdapter()
/*     */   {
/*  42 */     super(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOrder(int order)
/*     */   {
/*  52 */     this.order = order;
/*     */   }
/*     */   
/*     */   public int getOrder()
/*     */   {
/*  57 */     return this.order;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean supports(Object handler)
/*     */   {
/*  68 */     return ((handler instanceof HandlerMethod)) && (supportsInternal((HandlerMethod)handler));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean supportsInternal(HandlerMethod paramHandlerMethod);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
/*     */     throws Exception
/*     */   {
/*  85 */     return handleInternal(request, response, (HandlerMethod)handler);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract ModelAndView handleInternal(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, HandlerMethod paramHandlerMethod)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final long getLastModified(HttpServletRequest request, Object handler)
/*     */   {
/* 106 */     return getLastModifiedInternal(request, (HandlerMethod)handler);
/*     */   }
/*     */   
/*     */   protected abstract long getLastModifiedInternal(HttpServletRequest paramHttpServletRequest, HandlerMethod paramHandlerMethod);
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\AbstractHandlerMethodAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */