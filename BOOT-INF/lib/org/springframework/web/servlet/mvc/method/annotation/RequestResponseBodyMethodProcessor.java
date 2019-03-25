/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.core.Conventions;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*     */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*     */ import org.springframework.http.server.ServletServerHttpRequest;
/*     */ import org.springframework.http.server.ServletServerHttpResponse;
/*     */ import org.springframework.validation.BindingResult;
/*     */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*     */ import org.springframework.web.HttpMediaTypeNotSupportedException;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.bind.MethodArgumentNotValidException;
/*     */ import org.springframework.web.bind.WebDataBinder;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.ResponseBody;
/*     */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
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
/*     */ public class RequestResponseBodyMethodProcessor
/*     */   extends AbstractMessageConverterMethodProcessor
/*     */ {
/*     */   public RequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters)
/*     */   {
/*  68 */     super(converters);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters, ContentNegotiationManager manager)
/*     */   {
/*  80 */     super(converters, manager);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters, List<Object> requestResponseBodyAdvice)
/*     */   {
/*  92 */     super(converters, null, requestResponseBodyAdvice);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters, ContentNegotiationManager manager, List<Object> requestResponseBodyAdvice)
/*     */   {
/* 102 */     super(converters, manager, requestResponseBodyAdvice);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean supportsParameter(MethodParameter parameter)
/*     */   {
/* 108 */     return parameter.hasParameterAnnotation(RequestBody.class);
/*     */   }
/*     */   
/*     */   public boolean supportsReturnType(MethodParameter returnType)
/*     */   {
/* 113 */     return (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseBody.class)) || 
/* 114 */       (returnType.hasMethodAnnotation(ResponseBody.class));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
/*     */     throws Exception
/*     */   {
/* 127 */     parameter = parameter.nestedIfOptional();
/* 128 */     Object arg = readWithMessageConverters(webRequest, parameter, parameter.getNestedGenericParameterType());
/* 129 */     String name = Conventions.getVariableNameForParameter(parameter);
/*     */     
/* 131 */     WebDataBinder binder = binderFactory.createBinder(webRequest, arg, name);
/* 132 */     if (arg != null) {
/* 133 */       validateIfApplicable(binder, parameter);
/* 134 */       if ((binder.getBindingResult().hasErrors()) && (isBindExceptionRequired(binder, parameter))) {
/* 135 */         throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
/*     */       }
/*     */     }
/* 138 */     mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
/*     */     
/* 140 */     return adaptArgumentIfNecessary(arg, parameter);
/*     */   }
/*     */   
/*     */ 
/*     */   protected <T> Object readWithMessageConverters(NativeWebRequest webRequest, MethodParameter parameter, Type paramType)
/*     */     throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException
/*     */   {
/* 147 */     HttpServletRequest servletRequest = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/* 148 */     ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(servletRequest);
/*     */     
/* 150 */     Object arg = readWithMessageConverters(inputMessage, parameter, paramType);
/* 151 */     if ((arg == null) && 
/* 152 */       (checkRequired(parameter)))
/*     */     {
/* 154 */       throw new HttpMessageNotReadableException("Required request body is missing: " + parameter.getMethod().toGenericString());
/*     */     }
/*     */     
/* 157 */     return arg;
/*     */   }
/*     */   
/*     */   protected boolean checkRequired(MethodParameter parameter) {
/* 161 */     return (((RequestBody)parameter.getParameterAnnotation(RequestBody.class)).required()) && (!parameter.isOptional());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
/*     */     throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException
/*     */   {
/* 169 */     mavContainer.setRequestHandled(true);
/* 170 */     ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
/* 171 */     ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);
/*     */     
/*     */ 
/* 174 */     writeWithMessageConverters(returnValue, returnType, inputMessage, outputMessage);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\RequestResponseBodyMethodProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */