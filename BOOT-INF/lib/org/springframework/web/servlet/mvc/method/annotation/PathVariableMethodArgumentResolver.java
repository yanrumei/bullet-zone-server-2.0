/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.bind.MissingPathVariableException;
/*     */ import org.springframework.web.bind.ServletRequestBindingException;
/*     */ import org.springframework.web.bind.annotation.PathVariable;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;
/*     */ import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver.NamedValueInfo;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
/*     */ import org.springframework.web.method.support.UriComponentsContributor;
/*     */ import org.springframework.web.servlet.HandlerMapping;
/*     */ import org.springframework.web.servlet.View;
/*     */ import org.springframework.web.util.UriComponentsBuilder;
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
/*     */ public class PathVariableMethodArgumentResolver
/*     */   extends AbstractNamedValueMethodArgumentResolver
/*     */   implements UriComponentsContributor
/*     */ {
/*  66 */   private static final TypeDescriptor STRING_TYPE_DESCRIPTOR = TypeDescriptor.valueOf(String.class);
/*     */   
/*     */ 
/*     */   public boolean supportsParameter(MethodParameter parameter)
/*     */   {
/*  71 */     if (!parameter.hasParameterAnnotation(PathVariable.class)) {
/*  72 */       return false;
/*     */     }
/*  74 */     if (Map.class.isAssignableFrom(parameter.nestedIfOptional().getNestedParameterType())) {
/*  75 */       String paramName = ((PathVariable)parameter.getParameterAnnotation(PathVariable.class)).value();
/*  76 */       return StringUtils.hasText(paramName);
/*     */     }
/*  78 */     return true;
/*     */   }
/*     */   
/*     */   protected AbstractNamedValueMethodArgumentResolver.NamedValueInfo createNamedValueInfo(MethodParameter parameter)
/*     */   {
/*  83 */     PathVariable annotation = (PathVariable)parameter.getParameterAnnotation(PathVariable.class);
/*  84 */     return new PathVariableNamedValueInfo(annotation);
/*     */   }
/*     */   
/*     */   protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request)
/*     */     throws Exception
/*     */   {
/*  90 */     Map<String, String> uriTemplateVars = (Map)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, 0);
/*     */     
/*  92 */     return uriTemplateVars != null ? (String)uriTemplateVars.get(name) : null;
/*     */   }
/*     */   
/*     */   protected void handleMissingValue(String name, MethodParameter parameter) throws ServletRequestBindingException
/*     */   {
/*  97 */     throw new MissingPathVariableException(name, parameter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void handleResolvedValue(Object arg, String name, MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest request)
/*     */   {
/* 105 */     String key = View.PATH_VARIABLES;
/* 106 */     int scope = 0;
/* 107 */     Map<String, Object> pathVars = (Map)request.getAttribute(key, scope);
/* 108 */     if (pathVars == null) {
/* 109 */       pathVars = new HashMap();
/* 110 */       request.setAttribute(key, pathVars, scope);
/*     */     }
/* 112 */     pathVars.put(name, arg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void contributeMethodArgument(MethodParameter parameter, Object value, UriComponentsBuilder builder, Map<String, Object> uriVariables, ConversionService conversionService)
/*     */   {
/* 119 */     if (Map.class.isAssignableFrom(parameter.nestedIfOptional().getNestedParameterType())) {
/* 120 */       return;
/*     */     }
/*     */     
/* 123 */     PathVariable ann = (PathVariable)parameter.getParameterAnnotation(PathVariable.class);
/* 124 */     String name = (ann != null) && (!StringUtils.isEmpty(ann.value())) ? ann.value() : parameter.getParameterName();
/* 125 */     value = formatUriValue(conversionService, new TypeDescriptor(parameter.nestedIfOptional()), value);
/* 126 */     uriVariables.put(name, value);
/*     */   }
/*     */   
/*     */   protected String formatUriValue(ConversionService cs, TypeDescriptor sourceType, Object value) {
/* 130 */     if (value == null) {
/* 131 */       return null;
/*     */     }
/* 133 */     if ((value instanceof String)) {
/* 134 */       return (String)value;
/*     */     }
/* 136 */     if (cs != null) {
/* 137 */       return (String)cs.convert(value, sourceType, STRING_TYPE_DESCRIPTOR);
/*     */     }
/*     */     
/* 140 */     return value.toString();
/*     */   }
/*     */   
/*     */   private static class PathVariableNamedValueInfo
/*     */     extends AbstractNamedValueMethodArgumentResolver.NamedValueInfo
/*     */   {
/*     */     public PathVariableNamedValueInfo(PathVariable annotation)
/*     */     {
/* 148 */       super(annotation.required(), "\n\t\t\n\t\t\n\n\t\t\t\t\n");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\PathVariableMethodArgumentResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */