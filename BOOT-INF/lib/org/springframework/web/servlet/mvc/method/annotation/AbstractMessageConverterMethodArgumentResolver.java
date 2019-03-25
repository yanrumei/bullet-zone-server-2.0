/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PushbackInputStream;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.HttpRequest;
/*     */ import org.springframework.http.InvalidMediaTypeException;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.GenericHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*     */ import org.springframework.http.server.ServletServerHttpRequest;
/*     */ import org.springframework.lang.UsesJava8;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.validation.Errors;
/*     */ import org.springframework.validation.annotation.Validated;
/*     */ import org.springframework.web.HttpMediaTypeNotSupportedException;
/*     */ import org.springframework.web.bind.WebDataBinder;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
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
/*     */ public abstract class AbstractMessageConverterMethodArgumentResolver
/*     */   implements HandlerMethodArgumentResolver
/*     */ {
/*  71 */   private static final Set<HttpMethod> SUPPORTED_METHODS = EnumSet.of(HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH);
/*     */   
/*  73 */   private static final Object NO_VALUE = new Object();
/*     */   
/*     */ 
/*  76 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */ 
/*     */   protected final List<HttpMessageConverter<?>> messageConverters;
/*     */   
/*     */ 
/*     */   protected final List<MediaType> allSupportedMediaTypes;
/*     */   
/*     */   private final RequestResponseBodyAdviceChain advice;
/*     */   
/*     */ 
/*     */   public AbstractMessageConverterMethodArgumentResolver(List<HttpMessageConverter<?>> converters)
/*     */   {
/*  89 */     this(converters, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractMessageConverterMethodArgumentResolver(List<HttpMessageConverter<?>> converters, List<Object> requestResponseBodyAdvice)
/*     */   {
/*  99 */     Assert.notEmpty(converters, "'messageConverters' must not be empty");
/* 100 */     this.messageConverters = converters;
/* 101 */     this.allSupportedMediaTypes = getAllSupportedMediaTypes(converters);
/* 102 */     this.advice = new RequestResponseBodyAdviceChain(requestResponseBodyAdvice);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static List<MediaType> getAllSupportedMediaTypes(List<HttpMessageConverter<?>> messageConverters)
/*     */   {
/* 111 */     Set<MediaType> allSupportedMediaTypes = new LinkedHashSet();
/* 112 */     for (HttpMessageConverter<?> messageConverter : messageConverters) {
/* 113 */       allSupportedMediaTypes.addAll(messageConverter.getSupportedMediaTypes());
/*     */     }
/* 115 */     Object result = new ArrayList(allSupportedMediaTypes);
/* 116 */     MediaType.sortBySpecificity((List)result);
/* 117 */     return Collections.unmodifiableList((List)result);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected RequestResponseBodyAdviceChain getAdvice()
/*     */   {
/* 127 */     return this.advice;
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
/*     */   protected <T> Object readWithMessageConverters(NativeWebRequest webRequest, MethodParameter parameter, Type paramType)
/*     */     throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException
/*     */   {
/* 144 */     HttpInputMessage inputMessage = createInputMessage(webRequest);
/* 145 */     return readWithMessageConverters(inputMessage, parameter, paramType);
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
/*     */   protected <T> Object readWithMessageConverters(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType)
/*     */     throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException
/*     */   {
/* 165 */     boolean noContentType = false;
/*     */     try {
/* 167 */       contentType = inputMessage.getHeaders().getContentType();
/*     */     } catch (InvalidMediaTypeException ex) {
/*     */       MediaType contentType;
/* 170 */       throw new HttpMediaTypeNotSupportedException(ex.getMessage()); }
/*     */     MediaType contentType;
/* 172 */     if (contentType == null) {
/* 173 */       noContentType = true;
/* 174 */       contentType = MediaType.APPLICATION_OCTET_STREAM;
/*     */     }
/*     */     
/* 177 */     Class<?> contextClass = parameter != null ? parameter.getContainingClass() : null;
/* 178 */     Class<T> targetClass = (targetType instanceof Class) ? (Class)targetType : null;
/* 179 */     if (targetClass == null)
/*     */     {
/* 181 */       ResolvableType resolvableType = parameter != null ? ResolvableType.forMethodParameter(parameter) : ResolvableType.forType(targetType);
/* 182 */       targetClass = resolvableType.resolve();
/*     */     }
/*     */     
/* 185 */     HttpMethod httpMethod = ((HttpRequest)inputMessage).getMethod();
/* 186 */     Object body = NO_VALUE;
/*     */     try
/*     */     {
/* 189 */       inputMessage = new EmptyBodyCheckingHttpInputMessage(inputMessage);
/*     */       
/* 191 */       for (HttpMessageConverter<?> converter : this.messageConverters) {
/* 192 */         Class<HttpMessageConverter<?>> converterType = converter.getClass();
/* 193 */         if ((converter instanceof GenericHttpMessageConverter)) {
/* 194 */           GenericHttpMessageConverter<?> genericConverter = (GenericHttpMessageConverter)converter;
/* 195 */           if (genericConverter.canRead(targetType, contextClass, contentType)) {
/* 196 */             if (this.logger.isDebugEnabled()) {
/* 197 */               this.logger.debug("Read [" + targetType + "] as \"" + contentType + "\" with [" + converter + "]");
/*     */             }
/* 199 */             if (inputMessage.getBody() != null) {
/* 200 */               inputMessage = getAdvice().beforeBodyRead(inputMessage, parameter, targetType, converterType);
/* 201 */               body = genericConverter.read(targetType, contextClass, inputMessage);
/* 202 */               body = getAdvice().afterBodyRead(body, inputMessage, parameter, targetType, converterType); break;
/*     */             }
/*     */             
/* 205 */             body = getAdvice().handleEmptyBody(null, inputMessage, parameter, targetType, converterType);
/*     */             
/* 207 */             break;
/*     */           }
/*     */         }
/* 210 */         else if ((targetClass != null) && 
/* 211 */           (converter.canRead(targetClass, contentType))) {
/* 212 */           if (this.logger.isDebugEnabled()) {
/* 213 */             this.logger.debug("Read [" + targetType + "] as \"" + contentType + "\" with [" + converter + "]");
/*     */           }
/* 215 */           if (inputMessage.getBody() != null) {
/* 216 */             inputMessage = getAdvice().beforeBodyRead(inputMessage, parameter, targetType, converterType);
/* 217 */             body = converter.read(targetClass, inputMessage);
/* 218 */             body = getAdvice().afterBodyRead(body, inputMessage, parameter, targetType, converterType); break;
/*     */           }
/*     */           
/* 221 */           body = getAdvice().handleEmptyBody(null, inputMessage, parameter, targetType, converterType);
/*     */           
/* 223 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/* 229 */       throw new HttpMessageNotReadableException("I/O error while reading input message", ex);
/*     */     }
/*     */     
/* 232 */     if (body == NO_VALUE) {
/* 233 */       if ((httpMethod == null) || (!SUPPORTED_METHODS.contains(httpMethod)) || ((noContentType) && 
/* 234 */         (inputMessage.getBody() == null))) {
/* 235 */         return null;
/*     */       }
/* 237 */       throw new HttpMediaTypeNotSupportedException(contentType, this.allSupportedMediaTypes);
/*     */     }
/*     */     
/* 240 */     return body;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ServletServerHttpRequest createInputMessage(NativeWebRequest webRequest)
/*     */   {
/* 249 */     HttpServletRequest servletRequest = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/* 250 */     return new ServletServerHttpRequest(servletRequest);
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
/*     */   protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter)
/*     */   {
/* 264 */     Annotation[] annotations = parameter.getParameterAnnotations();
/* 265 */     for (Annotation ann : annotations) {
/* 266 */       Validated validatedAnn = (Validated)AnnotationUtils.getAnnotation(ann, Validated.class);
/* 267 */       if ((validatedAnn != null) || (ann.annotationType().getSimpleName().startsWith("Valid"))) {
/* 268 */         Object hints = validatedAnn != null ? validatedAnn.value() : AnnotationUtils.getValue(ann);
/* 269 */         Object[] validationHints = { (hints instanceof Object[]) ? (Object[])hints : hints };
/* 270 */         binder.validate(validationHints);
/* 271 */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter)
/*     */   {
/* 284 */     int i = parameter.getParameterIndex();
/* 285 */     Class<?>[] paramTypes = parameter.getMethod().getParameterTypes();
/* 286 */     boolean hasBindingResult = (paramTypes.length > i + 1) && (Errors.class.isAssignableFrom(paramTypes[(i + 1)]));
/* 287 */     return !hasBindingResult;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object adaptArgumentIfNecessary(Object arg, MethodParameter parameter)
/*     */   {
/* 298 */     return parameter.isOptional() ? OptionalResolver.resolveValue(arg) : arg;
/*     */   }
/*     */   
/*     */ 
/*     */   private static class EmptyBodyCheckingHttpInputMessage
/*     */     implements HttpInputMessage
/*     */   {
/*     */     private final HttpHeaders headers;
/*     */     private final InputStream body;
/*     */     private final HttpMethod method;
/*     */     
/*     */     public EmptyBodyCheckingHttpInputMessage(HttpInputMessage inputMessage)
/*     */       throws IOException
/*     */     {
/* 312 */       this.headers = inputMessage.getHeaders();
/* 313 */       InputStream inputStream = inputMessage.getBody();
/* 314 */       if (inputStream == null) {
/* 315 */         this.body = null;
/*     */       }
/* 317 */       else if (inputStream.markSupported()) {
/* 318 */         inputStream.mark(1);
/* 319 */         this.body = (inputStream.read() != -1 ? inputStream : null);
/* 320 */         inputStream.reset();
/*     */       }
/*     */       else {
/* 323 */         PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
/* 324 */         int b = pushbackInputStream.read();
/* 325 */         if (b == -1) {
/* 326 */           this.body = null;
/*     */         }
/*     */         else {
/* 329 */           this.body = pushbackInputStream;
/* 330 */           pushbackInputStream.unread(b);
/*     */         }
/*     */       }
/* 333 */       this.method = ((HttpRequest)inputMessage).getMethod();
/*     */     }
/*     */     
/*     */     public HttpHeaders getHeaders()
/*     */     {
/* 338 */       return this.headers;
/*     */     }
/*     */     
/*     */     public InputStream getBody() throws IOException
/*     */     {
/* 343 */       return this.body;
/*     */     }
/*     */     
/*     */     public HttpMethod getMethod() {
/* 347 */       return this.method;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @UsesJava8
/*     */   private static class OptionalResolver
/*     */   {
/*     */     public static Object resolveValue(Object value)
/*     */     {
/* 359 */       if ((value == null) || (((value instanceof Collection)) && (((Collection)value).isEmpty())) || (((value instanceof Object[])) && (((Object[])value).length == 0)))
/*     */       {
/* 361 */         return Optional.empty();
/*     */       }
/* 363 */       return Optional.of(value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\AbstractMessageConverterMethodArgumentResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */