/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.validation.BindingResult;
/*     */ import org.springframework.web.bind.MethodArgumentNotValidException;
/*     */ import org.springframework.web.bind.WebDataBinder;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.annotation.RequestPart;
/*     */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
/*     */ import org.springframework.web.multipart.MultipartException;
/*     */ import org.springframework.web.multipart.support.MissingServletRequestPartException;
/*     */ import org.springframework.web.multipart.support.MultipartResolutionDelegate;
/*     */ import org.springframework.web.multipart.support.RequestPartServletServerHttpRequest;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RequestPartMethodArgumentResolver
/*     */   extends AbstractMessageConverterMethodArgumentResolver
/*     */ {
/*     */   public RequestPartMethodArgumentResolver(List<HttpMessageConverter<?>> messageConverters)
/*     */   {
/*  73 */     super(messageConverters);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RequestPartMethodArgumentResolver(List<HttpMessageConverter<?>> messageConverters, List<Object> requestResponseBodyAdvice)
/*     */   {
/*  83 */     super(messageConverters, requestResponseBodyAdvice);
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
/*     */   public boolean supportsParameter(MethodParameter parameter)
/*     */   {
/*  97 */     if (parameter.hasParameterAnnotation(RequestPart.class)) {
/*  98 */       return true;
/*     */     }
/*     */     
/* 101 */     if (parameter.hasParameterAnnotation(RequestParam.class)) {
/* 102 */       return false;
/*     */     }
/* 104 */     return MultipartResolutionDelegate.isMultipartArgument(parameter.nestedIfOptional());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest request, WebDataBinderFactory binderFactory)
/*     */     throws Exception
/*     */   {
/* 112 */     HttpServletRequest servletRequest = (HttpServletRequest)request.getNativeRequest(HttpServletRequest.class);
/* 113 */     RequestPart requestPart = (RequestPart)parameter.getParameterAnnotation(RequestPart.class);
/* 114 */     boolean isRequired = ((requestPart == null) || (requestPart.required())) && (!parameter.isOptional());
/*     */     
/* 116 */     String name = getPartName(parameter, requestPart);
/* 117 */     parameter = parameter.nestedIfOptional();
/* 118 */     Object arg = null;
/*     */     
/* 120 */     Object mpArg = MultipartResolutionDelegate.resolveMultipartArgument(name, parameter, servletRequest);
/* 121 */     if (mpArg != MultipartResolutionDelegate.UNRESOLVABLE) {
/* 122 */       arg = mpArg;
/*     */     } else {
/*     */       try
/*     */       {
/* 126 */         HttpInputMessage inputMessage = new RequestPartServletServerHttpRequest(servletRequest, name);
/* 127 */         arg = readWithMessageConverters(inputMessage, parameter, parameter.getNestedGenericParameterType());
/* 128 */         WebDataBinder binder = binderFactory.createBinder(request, arg, name);
/* 129 */         if (arg != null) {
/* 130 */           validateIfApplicable(binder, parameter);
/* 131 */           if ((binder.getBindingResult().hasErrors()) && (isBindExceptionRequired(binder, parameter))) {
/* 132 */             throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
/*     */           }
/*     */         }
/* 135 */         mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
/*     */       }
/*     */       catch (MissingServletRequestPartException ex) {
/* 138 */         if (isRequired) {
/* 139 */           throw ex;
/*     */         }
/*     */       }
/*     */       catch (MultipartException ex) {
/* 143 */         if (isRequired) {
/* 144 */           throw ex;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 149 */     if ((arg == null) && (isRequired)) {
/* 150 */       if (!MultipartResolutionDelegate.isMultipartRequest(servletRequest)) {
/* 151 */         throw new MultipartException("Current request is not a multipart request");
/*     */       }
/*     */       
/* 154 */       throw new MissingServletRequestPartException(name);
/*     */     }
/*     */     
/* 157 */     return adaptArgumentIfNecessary(arg, parameter);
/*     */   }
/*     */   
/*     */   private String getPartName(MethodParameter methodParam, RequestPart requestPart) {
/* 161 */     String partName = requestPart != null ? requestPart.name() : "";
/* 162 */     if (partName.isEmpty()) {
/* 163 */       partName = methodParam.getParameterName();
/* 164 */       if (partName == null)
/*     */       {
/* 166 */         throw new IllegalArgumentException("Request part name for argument type [" + methodParam.getNestedParameterType().getName() + "] not specified, and parameter name information not found in class file either.");
/*     */       }
/*     */     }
/*     */     
/* 170 */     return partName;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\RequestPartMethodArgumentResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */