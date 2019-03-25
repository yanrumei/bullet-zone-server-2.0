/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletRequest;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.validation.DataBinder;
/*     */ import org.springframework.web.bind.ServletRequestDataBinder;
/*     */ import org.springframework.web.bind.WebDataBinder;
/*     */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletModelAttributeMethodProcessor
/*     */   extends ModelAttributeMethodProcessor
/*     */ {
/*     */   public ServletModelAttributeMethodProcessor(boolean annotationNotRequired)
/*     */   {
/*  58 */     super(annotationNotRequired);
/*     */   }
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
/*     */   protected final Object createAttribute(String attributeName, MethodParameter parameter, WebDataBinderFactory binderFactory, NativeWebRequest request)
/*     */     throws Exception
/*     */   {
/*  73 */     String value = getRequestValueForAttribute(attributeName, request);
/*  74 */     if (value != null) {
/*  75 */       Object attribute = createAttributeFromRequestValue(value, attributeName, parameter, binderFactory, request);
/*     */       
/*  77 */       if (attribute != null) {
/*  78 */         return attribute;
/*     */       }
/*     */     }
/*     */     
/*  82 */     return super.createAttribute(attributeName, parameter, binderFactory, request);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getRequestValueForAttribute(String attributeName, NativeWebRequest request)
/*     */   {
/*  95 */     Map<String, String> variables = getUriTemplateVariables(request);
/*  96 */     String variableValue = (String)variables.get(attributeName);
/*  97 */     if (StringUtils.hasText(variableValue)) {
/*  98 */       return variableValue;
/*     */     }
/* 100 */     String parameterValue = request.getParameter(attributeName);
/* 101 */     if (StringUtils.hasText(parameterValue)) {
/* 102 */       return parameterValue;
/*     */     }
/* 104 */     return null;
/*     */   }
/*     */   
/*     */   protected final Map<String, String> getUriTemplateVariables(NativeWebRequest request)
/*     */   {
/* 109 */     Map<String, String> variables = (Map)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, 0);
/*     */     
/* 111 */     return variables != null ? variables : Collections.emptyMap();
/*     */   }
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
/*     */   protected Object createAttributeFromRequestValue(String sourceValue, String attributeName, MethodParameter parameter, WebDataBinderFactory binderFactory, NativeWebRequest request)
/*     */     throws Exception
/*     */   {
/* 131 */     DataBinder binder = binderFactory.createBinder(request, null, attributeName);
/* 132 */     ConversionService conversionService = binder.getConversionService();
/* 133 */     if (conversionService != null) {
/* 134 */       TypeDescriptor source = TypeDescriptor.valueOf(String.class);
/* 135 */       TypeDescriptor target = new TypeDescriptor(parameter);
/* 136 */       if (conversionService.canConvert(source, target)) {
/* 137 */         return binder.convertIfNecessary(sourceValue, parameter.getParameterType(), parameter);
/*     */       }
/*     */     }
/* 140 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request)
/*     */   {
/* 150 */     ServletRequest servletRequest = (ServletRequest)request.getNativeRequest(ServletRequest.class);
/* 151 */     ServletRequestDataBinder servletBinder = (ServletRequestDataBinder)binder;
/* 152 */     servletBinder.bind(servletRequest);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\ServletModelAttributeMethodProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */