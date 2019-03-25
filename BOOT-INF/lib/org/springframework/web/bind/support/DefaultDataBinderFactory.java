/*    */ package org.springframework.web.bind.support;
/*    */ 
/*    */ import org.springframework.web.bind.WebDataBinder;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
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
/*    */ public class DefaultDataBinderFactory
/*    */   implements WebDataBinderFactory
/*    */ {
/*    */   private final WebBindingInitializer initializer;
/*    */   
/*    */   public DefaultDataBinderFactory(WebBindingInitializer initializer)
/*    */   {
/* 40 */     this.initializer = initializer;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public final WebDataBinder createBinder(NativeWebRequest webRequest, Object target, String objectName)
/*    */     throws Exception
/*    */   {
/* 53 */     WebDataBinder dataBinder = createBinderInstance(target, objectName, webRequest);
/* 54 */     if (this.initializer != null) {
/* 55 */       this.initializer.initBinder(dataBinder, webRequest);
/*    */     }
/* 57 */     initBinder(dataBinder, webRequest);
/* 58 */     return dataBinder;
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
/*    */   protected WebDataBinder createBinderInstance(Object target, String objectName, NativeWebRequest webRequest)
/*    */     throws Exception
/*    */   {
/* 72 */     return new WebRequestDataBinder(target, objectName);
/*    */   }
/*    */   
/*    */   protected void initBinder(WebDataBinder dataBinder, NativeWebRequest webRequest)
/*    */     throws Exception
/*    */   {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\bind\support\DefaultDataBinderFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */