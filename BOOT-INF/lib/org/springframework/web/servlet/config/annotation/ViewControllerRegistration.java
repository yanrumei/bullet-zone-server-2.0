/*    */ package org.springframework.web.servlet.config.annotation;
/*    */ 
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.http.HttpStatus;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.servlet.mvc.ParameterizableViewController;
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
/*    */ public class ViewControllerRegistration
/*    */ {
/*    */   private final String urlPath;
/* 36 */   private final ParameterizableViewController controller = new ParameterizableViewController();
/*    */   
/*    */   public ViewControllerRegistration(String urlPath)
/*    */   {
/* 40 */     Assert.notNull(urlPath, "'urlPath' is required.");
/* 41 */     this.urlPath = urlPath;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ViewControllerRegistration setStatusCode(HttpStatus statusCode)
/*    */   {
/* 51 */     this.controller.setStatusCode(statusCode);
/* 52 */     return this;
/*    */   }
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
/*    */   public void setViewName(String viewName)
/*    */   {
/* 66 */     this.controller.setViewName(viewName);
/*    */   }
/*    */   
/*    */   protected void setApplicationContext(ApplicationContext applicationContext) {
/* 70 */     this.controller.setApplicationContext(applicationContext);
/*    */   }
/*    */   
/*    */   protected String getUrlPath() {
/* 74 */     return this.urlPath;
/*    */   }
/*    */   
/*    */   protected ParameterizableViewController getViewController() {
/* 78 */     return this.controller;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\annotation\ViewControllerRegistration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */