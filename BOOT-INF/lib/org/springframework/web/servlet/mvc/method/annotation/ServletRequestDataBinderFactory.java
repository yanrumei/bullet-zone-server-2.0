/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.springframework.web.bind.ServletRequestDataBinder;
/*    */ import org.springframework.web.bind.support.WebBindingInitializer;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.method.annotation.InitBinderDataBinderFactory;
/*    */ import org.springframework.web.method.support.InvocableHandlerMethod;
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
/*    */ public class ServletRequestDataBinderFactory
/*    */   extends InitBinderDataBinderFactory
/*    */ {
/*    */   public ServletRequestDataBinderFactory(List<InvocableHandlerMethod> binderMethods, WebBindingInitializer initializer)
/*    */   {
/* 41 */     super(binderMethods, initializer);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ServletRequestDataBinder createBinderInstance(Object target, String objectName, NativeWebRequest request)
/*    */   {
/* 49 */     return new ExtendedServletRequestDataBinder(target, objectName);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\ServletRequestDataBinderFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */