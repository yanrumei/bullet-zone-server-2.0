/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import java.io.OutputStream;
/*    */ import java.io.Writer;
/*    */ import java.lang.reflect.Method;
/*    */ import javax.servlet.ServletResponse;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*    */ import org.springframework.web.method.support.ModelAndViewContainer;
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
/*    */ public class ServletResponseMethodArgumentResolver
/*    */   implements HandlerMethodArgumentResolver
/*    */ {
/*    */   public boolean supportsParameter(MethodParameter parameter)
/*    */   {
/* 47 */     Class<?> paramType = parameter.getParameterType();
/* 48 */     return (ServletResponse.class.isAssignableFrom(paramType)) || 
/* 49 */       (OutputStream.class.isAssignableFrom(paramType)) || 
/* 50 */       (Writer.class.isAssignableFrom(paramType));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
/*    */     throws Exception
/*    */   {
/* 63 */     if (mavContainer != null) {
/* 64 */       mavContainer.setRequestHandled(true);
/*    */     }
/*    */     
/* 67 */     HttpServletResponse response = (HttpServletResponse)webRequest.getNativeResponse(HttpServletResponse.class);
/* 68 */     Class<?> paramType = parameter.getParameterType();
/*    */     
/* 70 */     if (ServletResponse.class.isAssignableFrom(paramType)) {
/* 71 */       Object nativeResponse = webRequest.getNativeResponse(paramType);
/* 72 */       if (nativeResponse == null)
/*    */       {
/* 74 */         throw new IllegalStateException("Current response is not of type [" + paramType.getName() + "]: " + response);
/*    */       }
/* 76 */       return nativeResponse;
/*    */     }
/* 78 */     if (OutputStream.class.isAssignableFrom(paramType)) {
/* 79 */       return response.getOutputStream();
/*    */     }
/* 81 */     if (Writer.class.isAssignableFrom(paramType)) {
/* 82 */       return response.getWriter();
/*    */     }
/*    */     
/*    */ 
/* 86 */     Method method = parameter.getMethod();
/* 87 */     throw new UnsupportedOperationException("Unknown parameter type: " + paramType + " in method: " + method);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\ServletResponseMethodArgumentResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */