/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.servlet.handler.AbstractHandlerMapping;
/*     */ import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
/*     */ import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;
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
/*     */ public class DefaultServletHandlerConfigurer
/*     */ {
/*     */   private final ServletContext servletContext;
/*     */   private DefaultServletHttpRequestHandler handler;
/*     */   
/*     */   public DefaultServletHandlerConfigurer(ServletContext servletContext)
/*     */   {
/*  55 */     Assert.notNull(servletContext, "ServletContext is required");
/*  56 */     this.servletContext = servletContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void enable()
/*     */   {
/*  68 */     enable(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void enable(String defaultServletName)
/*     */   {
/*  78 */     this.handler = new DefaultServletHttpRequestHandler();
/*  79 */     this.handler.setDefaultServletName(defaultServletName);
/*  80 */     this.handler.setServletContext(this.servletContext);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SimpleUrlHandlerMapping buildHandlerMapping()
/*     */   {
/*  91 */     if (this.handler == null) {
/*  92 */       return null;
/*     */     }
/*     */     
/*  95 */     SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
/*  96 */     handlerMapping.setUrlMap(Collections.singletonMap("/**", this.handler));
/*  97 */     handlerMapping.setOrder(Integer.MAX_VALUE);
/*  98 */     return handlerMapping;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected AbstractHandlerMapping getHandlerMapping()
/*     */   {
/* 106 */     return buildHandlerMapping();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\annotation\DefaultServletHandlerConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */