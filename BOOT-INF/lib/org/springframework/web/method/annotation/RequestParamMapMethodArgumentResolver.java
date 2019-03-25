/*    */ package org.springframework.web.method.annotation;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.util.LinkedMultiValueMap;
/*    */ import org.springframework.util.MultiValueMap;
/*    */ import org.springframework.util.StringUtils;
/*    */ import org.springframework.web.bind.annotation.RequestParam;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RequestParamMapMethodArgumentResolver
/*    */   implements HandlerMethodArgumentResolver
/*    */ {
/*    */   public boolean supportsParameter(MethodParameter parameter)
/*    */   {
/* 52 */     RequestParam requestParam = (RequestParam)parameter.getParameterAnnotation(RequestParam.class);
/* 53 */     if ((requestParam != null) && 
/* 54 */       (Map.class.isAssignableFrom(parameter.getParameterType()))) {
/* 55 */       return !StringUtils.hasText(requestParam.name());
/*    */     }
/*    */     
/* 58 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
/*    */     throws Exception
/*    */   {
/* 65 */     Class<?> paramType = parameter.getParameterType();
/*    */     
/* 67 */     Map<String, String[]> parameterMap = webRequest.getParameterMap();
/* 68 */     if (MultiValueMap.class.isAssignableFrom(paramType)) {
/* 69 */       MultiValueMap<String, String> result = new LinkedMultiValueMap(parameterMap.size());
/* 70 */       for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
/* 71 */         for (String value : (String[])entry.getValue()) {
/* 72 */           result.add(entry.getKey(), value);
/*    */         }
/*    */       }
/* 75 */       return result;
/*    */     }
/*    */     
/* 78 */     Map<String, String> result = new LinkedHashMap(parameterMap.size());
/* 79 */     for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
/* 80 */       if (((String[])entry.getValue()).length > 0) {
/* 81 */         result.put(entry.getKey(), ((String[])entry.getValue())[0]);
/*    */       }
/*    */     }
/* 84 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\method\annotation\RequestParamMapMethodArgumentResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */