/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.util.CollectionUtils;
/*    */ import org.springframework.util.StringUtils;
/*    */ import org.springframework.web.bind.annotation.PathVariable;
/*    */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*    */ import org.springframework.web.method.support.ModelAndViewContainer;
/*    */ import org.springframework.web.servlet.HandlerMapping;
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
/*    */ public class PathVariableMapMethodArgumentResolver
/*    */   implements HandlerMethodArgumentResolver
/*    */ {
/*    */   public boolean supportsParameter(MethodParameter parameter)
/*    */   {
/* 47 */     PathVariable ann = (PathVariable)parameter.getParameterAnnotation(PathVariable.class);
/* 48 */     return (ann != null) && (Map.class.isAssignableFrom(parameter.getParameterType())) && 
/* 49 */       (!StringUtils.hasText(ann.value()));
/*    */   }
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
/* 61 */     Map<String, String> uriTemplateVars = (Map)webRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, 0);
/*    */     
/*    */ 
/* 64 */     if (!CollectionUtils.isEmpty(uriTemplateVars)) {
/* 65 */       return new LinkedHashMap(uriTemplateVars);
/*    */     }
/*    */     
/* 68 */     return Collections.emptyMap();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\PathVariableMapMethodArgumentResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */