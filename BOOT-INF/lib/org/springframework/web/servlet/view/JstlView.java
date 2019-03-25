/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.web.servlet.support.JstlUtils;
/*     */ import org.springframework.web.servlet.support.RequestContext;
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
/*     */ public class JstlView
/*     */   extends InternalResourceView
/*     */ {
/*     */   private MessageSource messageSource;
/*     */   
/*     */   public JstlView() {}
/*     */   
/*     */   public JstlView(String url)
/*     */   {
/*  94 */     super(url);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JstlView(String url, MessageSource messageSource)
/*     */   {
/* 106 */     this(url);
/* 107 */     this.messageSource = messageSource;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initServletContext(ServletContext servletContext)
/*     */   {
/* 119 */     if (this.messageSource != null) {
/* 120 */       this.messageSource = JstlUtils.getJstlAwareMessageSource(servletContext, this.messageSource);
/*     */     }
/* 122 */     super.initServletContext(servletContext);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void exposeHelpers(HttpServletRequest request)
/*     */     throws Exception
/*     */   {
/* 131 */     if (this.messageSource != null) {
/* 132 */       JstlUtils.exposeLocalizationContext(request, this.messageSource);
/*     */     }
/*     */     else {
/* 135 */       JstlUtils.exposeLocalizationContext(new RequestContext(request, getServletContext()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\JstlView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */