/*    */ package org.springframework.web.method.annotation;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.springframework.web.bind.WebDataBinder;
/*    */ import org.springframework.web.bind.annotation.InitBinder;
/*    */ import org.springframework.web.bind.support.DefaultDataBinderFactory;
/*    */ import org.springframework.web.bind.support.WebBindingInitializer;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.method.HandlerMethod;
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
/*    */ 
/*    */ public class InitBinderDataBinderFactory
/*    */   extends DefaultDataBinderFactory
/*    */ {
/*    */   private final List<InvocableHandlerMethod> binderMethods;
/*    */   
/*    */   public InitBinderDataBinderFactory(List<InvocableHandlerMethod> binderMethods, WebBindingInitializer initializer)
/*    */   {
/* 49 */     super(initializer);
/* 50 */     this.binderMethods = (binderMethods != null ? binderMethods : Collections.emptyList());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void initBinder(WebDataBinder binder, NativeWebRequest request)
/*    */     throws Exception
/*    */   {
/* 62 */     for (InvocableHandlerMethod binderMethod : this.binderMethods) {
/* 63 */       if (isBinderMethodApplicable(binderMethod, binder)) {
/* 64 */         Object returnValue = binderMethod.invokeForRequest(request, null, new Object[] { binder });
/* 65 */         if (returnValue != null) {
/* 66 */           throw new IllegalStateException("@InitBinder methods should return void: " + binderMethod);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected boolean isBinderMethodApplicable(HandlerMethod initBinderMethod, WebDataBinder binder)
/*    */   {
/* 79 */     InitBinder ann = (InitBinder)initBinderMethod.getMethodAnnotation(InitBinder.class);
/* 80 */     Collection<String> names = Arrays.asList(ann.value());
/* 81 */     return (names.isEmpty()) || (names.contains(binder.getObjectName()));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\method\annotation\InitBinderDataBinderFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */