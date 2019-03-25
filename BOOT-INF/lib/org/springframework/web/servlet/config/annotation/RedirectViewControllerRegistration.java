/*    */ package org.springframework.web.servlet.config.annotation;
/*    */ 
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.http.HttpStatus;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.servlet.mvc.ParameterizableViewController;
/*    */ import org.springframework.web.servlet.view.RedirectView;
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
/*    */ public class RedirectViewControllerRegistration
/*    */ {
/*    */   private final String urlPath;
/*    */   private final RedirectView redirectView;
/* 37 */   private final ParameterizableViewController controller = new ParameterizableViewController();
/*    */   
/*    */   public RedirectViewControllerRegistration(String urlPath, String redirectUrl)
/*    */   {
/* 41 */     Assert.notNull(urlPath, "'urlPath' is required.");
/* 42 */     Assert.notNull(redirectUrl, "'redirectUrl' is required.");
/* 43 */     this.urlPath = urlPath;
/* 44 */     this.redirectView = new RedirectView(redirectUrl);
/* 45 */     this.redirectView.setContextRelative(true);
/* 46 */     this.controller.setView(this.redirectView);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public RedirectViewControllerRegistration setStatusCode(HttpStatus statusCode)
/*    */   {
/* 56 */     Assert.isTrue(statusCode.is3xxRedirection(), "Not a redirect status code");
/* 57 */     this.redirectView.setStatusCode(statusCode);
/* 58 */     return this;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public RedirectViewControllerRegistration setContextRelative(boolean contextRelative)
/*    */   {
/* 68 */     this.redirectView.setContextRelative(contextRelative);
/* 69 */     return this;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public RedirectViewControllerRegistration setKeepQueryParams(boolean propagate)
/*    */   {
/* 78 */     this.redirectView.setPropagateQueryParams(propagate);
/* 79 */     return this;
/*    */   }
/*    */   
/*    */   protected void setApplicationContext(ApplicationContext applicationContext) {
/* 83 */     this.controller.setApplicationContext(applicationContext);
/* 84 */     this.redirectView.setApplicationContext(applicationContext);
/*    */   }
/*    */   
/*    */   protected String getUrlPath() {
/* 88 */     return this.urlPath;
/*    */   }
/*    */   
/*    */   protected ParameterizableViewController getViewController() {
/* 92 */     return this.controller;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\annotation\RedirectViewControllerRegistration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */