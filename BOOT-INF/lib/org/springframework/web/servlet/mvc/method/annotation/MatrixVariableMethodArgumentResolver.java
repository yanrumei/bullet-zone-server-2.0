/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.bind.ServletRequestBindingException;
/*     */ import org.springframework.web.bind.annotation.MatrixVariable;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;
/*     */ import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver.NamedValueInfo;
/*     */ import org.springframework.web.servlet.HandlerMapping;
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
/*     */ public class MatrixVariableMethodArgumentResolver
/*     */   extends AbstractNamedValueMethodArgumentResolver
/*     */ {
/*     */   public MatrixVariableMethodArgumentResolver()
/*     */   {
/*  48 */     super(null);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean supportsParameter(MethodParameter parameter)
/*     */   {
/*  54 */     if (!parameter.hasParameterAnnotation(MatrixVariable.class)) {
/*  55 */       return false;
/*     */     }
/*  57 */     if (Map.class.isAssignableFrom(parameter.nestedIfOptional().getNestedParameterType())) {
/*  58 */       String variableName = ((MatrixVariable)parameter.getParameterAnnotation(MatrixVariable.class)).name();
/*  59 */       return StringUtils.hasText(variableName);
/*     */     }
/*  61 */     return true;
/*     */   }
/*     */   
/*     */   protected AbstractNamedValueMethodArgumentResolver.NamedValueInfo createNamedValueInfo(MethodParameter parameter)
/*     */   {
/*  66 */     MatrixVariable annotation = (MatrixVariable)parameter.getParameterAnnotation(MatrixVariable.class);
/*  67 */     return new MatrixVariableNamedValueInfo(annotation, null);
/*     */   }
/*     */   
/*     */ 
/*     */   protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request)
/*     */     throws Exception
/*     */   {
/*  74 */     Map<String, MultiValueMap<String, String>> pathParameters = (Map)request.getAttribute(HandlerMapping.MATRIX_VARIABLES_ATTRIBUTE, 0);
/*  75 */     if (CollectionUtils.isEmpty(pathParameters)) {
/*  76 */       return null;
/*     */     }
/*     */     
/*  79 */     String pathVar = ((MatrixVariable)parameter.getParameterAnnotation(MatrixVariable.class)).pathVar();
/*  80 */     List<String> paramValues = null;
/*     */     boolean found;
/*  82 */     if (!pathVar.equals("\n\t\t\n\t\t\n\n\t\t\t\t\n")) {
/*  83 */       if (pathParameters.containsKey(pathVar)) {
/*  84 */         paramValues = (List)((MultiValueMap)pathParameters.get(pathVar)).get(name);
/*     */       }
/*     */     }
/*     */     else {
/*  88 */       found = false;
/*  89 */       paramValues = new ArrayList();
/*  90 */       for (MultiValueMap<String, String> params : pathParameters.values()) {
/*  91 */         if (params.containsKey(name)) {
/*  92 */           if (found) {
/*  93 */             String paramType = parameter.getNestedParameterType().getName();
/*  94 */             throw new ServletRequestBindingException("Found more than one match for URI path parameter '" + name + "' for parameter type [" + paramType + "]. Use 'pathVar' attribute to disambiguate.");
/*     */           }
/*     */           
/*     */ 
/*  98 */           paramValues.addAll((Collection)params.get(name));
/*  99 */           found = true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 104 */     if (CollectionUtils.isEmpty(paramValues)) {
/* 105 */       return null;
/*     */     }
/* 107 */     if (paramValues.size() == 1) {
/* 108 */       return paramValues.get(0);
/*     */     }
/*     */     
/* 111 */     return paramValues;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void handleMissingValue(String name, MethodParameter parameter)
/*     */     throws ServletRequestBindingException
/*     */   {
/* 118 */     throw new ServletRequestBindingException("Missing matrix variable '" + name + "' for method parameter of type " + parameter.getNestedParameterType().getSimpleName());
/*     */   }
/*     */   
/*     */   private static class MatrixVariableNamedValueInfo extends AbstractNamedValueMethodArgumentResolver.NamedValueInfo
/*     */   {
/*     */     private MatrixVariableNamedValueInfo(MatrixVariable annotation)
/*     */     {
/* 125 */       super(annotation.required(), annotation.defaultValue());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\MatrixVariableMethodArgumentResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */