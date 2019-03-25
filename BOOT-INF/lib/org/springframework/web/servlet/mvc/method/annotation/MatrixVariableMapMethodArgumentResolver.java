/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.bind.annotation.MatrixVariable;
/*     */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
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
/*     */ 
/*     */ 
/*     */ public class MatrixVariableMapMethodArgumentResolver
/*     */   implements HandlerMethodArgumentResolver
/*     */ {
/*     */   public boolean supportsParameter(MethodParameter parameter)
/*     */   {
/*  54 */     MatrixVariable matrixVariable = (MatrixVariable)parameter.getParameterAnnotation(MatrixVariable.class);
/*  55 */     if ((matrixVariable != null) && 
/*  56 */       (Map.class.isAssignableFrom(parameter.getParameterType()))) {
/*  57 */       return !StringUtils.hasText(matrixVariable.name());
/*     */     }
/*     */     
/*  60 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest request, WebDataBinderFactory binderFactory)
/*     */     throws Exception
/*     */   {
/*  69 */     Map<String, MultiValueMap<String, String>> matrixVariables = (Map)request.getAttribute(HandlerMapping.MATRIX_VARIABLES_ATTRIBUTE, 0);
/*     */     
/*     */ 
/*  72 */     if (CollectionUtils.isEmpty(matrixVariables)) {
/*  73 */       return Collections.emptyMap();
/*     */     }
/*     */     
/*  76 */     MultiValueMap<String, String> map = new LinkedMultiValueMap();
/*  77 */     String pathVariable = ((MatrixVariable)parameter.getParameterAnnotation(MatrixVariable.class)).pathVar();
/*     */     MultiValueMap<String, String> mapForPathVariable;
/*  79 */     if (!pathVariable.equals("\n\t\t\n\t\t\n\n\t\t\t\t\n")) {
/*  80 */       mapForPathVariable = (MultiValueMap)matrixVariables.get(pathVariable);
/*  81 */       if (mapForPathVariable == null) {
/*  82 */         return Collections.emptyMap();
/*     */       }
/*  84 */       map.putAll(mapForPathVariable);
/*     */     }
/*     */     else {
/*  87 */       for (mapForPathVariable = matrixVariables.values().iterator(); mapForPathVariable.hasNext();) { vars = (MultiValueMap)mapForPathVariable.next();
/*  88 */         for (localIterator1 = vars.keySet().iterator(); localIterator1.hasNext();) { name = (String)localIterator1.next();
/*  89 */           for (String value : (List)vars.get(name))
/*  90 */             map.add(name, value);
/*     */         }
/*     */       } }
/*     */     MultiValueMap<String, String> vars;
/*     */     Iterator localIterator1;
/*     */     String name;
/*  96 */     return isSingleValueMap(parameter) ? map.toSingleValueMap() : map;
/*     */   }
/*     */   
/*     */   private boolean isSingleValueMap(MethodParameter parameter) {
/* 100 */     if (!MultiValueMap.class.isAssignableFrom(parameter.getParameterType())) {
/* 101 */       ResolvableType[] genericTypes = ResolvableType.forMethodParameter(parameter).getGenerics();
/* 102 */       if (genericTypes.length == 2) {
/* 103 */         return !List.class.isAssignableFrom(genericTypes[1].getRawClass());
/*     */       }
/*     */     }
/* 106 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\MatrixVariableMapMethodArgumentResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */