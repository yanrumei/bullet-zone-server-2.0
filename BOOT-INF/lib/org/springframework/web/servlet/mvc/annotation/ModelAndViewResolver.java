/*    */ package org.springframework.web.servlet.mvc.annotation;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.ui.ExtendedModelMap;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
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
/*    */ public abstract interface ModelAndViewResolver
/*    */ {
/* 52 */   public static final ModelAndView UNRESOLVED = new ModelAndView();
/*    */   
/*    */   public abstract ModelAndView resolveModelAndView(Method paramMethod, Class<?> paramClass, Object paramObject, ExtendedModelMap paramExtendedModelMap, NativeWebRequest paramNativeWebRequest);
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\annotation\ModelAndViewResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */