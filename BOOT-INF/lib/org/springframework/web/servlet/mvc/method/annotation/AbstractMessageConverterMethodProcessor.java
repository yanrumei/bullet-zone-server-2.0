/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.http.HttpEntity;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.GenericHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*     */ import org.springframework.http.server.ServletServerHttpRequest;
/*     */ import org.springframework.http.server.ServletServerHttpResponse;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.accept.PathExtensionContentNegotiationStrategy;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.ServletWebRequest;
/*     */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*     */ import org.springframework.web.servlet.HandlerMapping;
/*     */ import org.springframework.web.util.UrlPathHelper;
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
/*     */ public abstract class AbstractMessageConverterMethodProcessor
/*     */   extends AbstractMessageConverterMethodArgumentResolver
/*     */   implements HandlerMethodReturnValueHandler
/*     */ {
/*  66 */   private static final Set<String> WHITELISTED_EXTENSIONS = new HashSet(Arrays.asList(new String[] { "txt", "text", "yml", "properties", "csv", "json", "xml", "atom", "rss", "png", "jpe", "jpeg", "jpg", "gif", "wbmp", "bmp" }));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  71 */   private static final Set<String> WHITELISTED_MEDIA_BASE_TYPES = new HashSet(
/*  72 */     Arrays.asList(new String[] { "audio", "image", "video" }));
/*     */   
/*  74 */   private static final MediaType MEDIA_TYPE_APPLICATION = new MediaType("application");
/*     */   
/*  76 */   private static final UrlPathHelper DECODING_URL_PATH_HELPER = new UrlPathHelper();
/*     */   
/*  78 */   private static final UrlPathHelper RAW_URL_PATH_HELPER = new UrlPathHelper();
/*     */   private final ContentNegotiationManager contentNegotiationManager;
/*     */   
/*  81 */   static { RAW_URL_PATH_HELPER.setRemoveSemicolonContent(false);
/*  82 */     RAW_URL_PATH_HELPER.setUrlDecode(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private final PathExtensionContentNegotiationStrategy pathStrategy;
/*     */   
/*     */ 
/*  90 */   private final Set<String> safeExtensions = new HashSet();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractMessageConverterMethodProcessor(List<HttpMessageConverter<?>> converters)
/*     */   {
/*  97 */     this(converters, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractMessageConverterMethodProcessor(List<HttpMessageConverter<?>> converters, ContentNegotiationManager contentNegotiationManager)
/*     */   {
/* 106 */     this(converters, contentNegotiationManager, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractMessageConverterMethodProcessor(List<HttpMessageConverter<?>> converters, ContentNegotiationManager manager, List<Object> requestResponseBodyAdvice)
/*     */   {
/* 116 */     super(converters, requestResponseBodyAdvice);
/* 117 */     this.contentNegotiationManager = (manager != null ? manager : new ContentNegotiationManager());
/* 118 */     this.pathStrategy = initPathStrategy(this.contentNegotiationManager);
/* 119 */     this.safeExtensions.addAll(this.contentNegotiationManager.getAllFileExtensions());
/* 120 */     this.safeExtensions.addAll(WHITELISTED_EXTENSIONS);
/*     */   }
/*     */   
/*     */   private static PathExtensionContentNegotiationStrategy initPathStrategy(ContentNegotiationManager manager) {
/* 124 */     Class<PathExtensionContentNegotiationStrategy> clazz = PathExtensionContentNegotiationStrategy.class;
/* 125 */     PathExtensionContentNegotiationStrategy strategy = (PathExtensionContentNegotiationStrategy)manager.getStrategy(clazz);
/* 126 */     return strategy != null ? strategy : new PathExtensionContentNegotiationStrategy();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ServletServerHttpResponse createOutputMessage(NativeWebRequest webRequest)
/*     */   {
/* 136 */     HttpServletResponse response = (HttpServletResponse)webRequest.getNativeResponse(HttpServletResponse.class);
/* 137 */     return new ServletServerHttpResponse(response);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected <T> void writeWithMessageConverters(T value, MethodParameter returnType, NativeWebRequest webRequest)
/*     */     throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException
/*     */   {
/* 147 */     ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
/* 148 */     ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);
/* 149 */     writeWithMessageConverters(value, returnType, inputMessage, outputMessage);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected <T> void writeWithMessageConverters(T value, MethodParameter returnType, ServletServerHttpRequest inputMessage, ServletServerHttpResponse outputMessage)
/*     */     throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException
/*     */   {
/*     */     Type declaredType;
/*     */     
/*     */ 
/*     */ 
/*     */     Object outputValue;
/*     */     
/*     */ 
/*     */     Class<?> valueType;
/*     */     
/*     */ 
/*     */     Type declaredType;
/*     */     
/*     */ 
/* 171 */     if ((value instanceof CharSequence)) {
/* 172 */       Object outputValue = value.toString();
/* 173 */       Class<?> valueType = String.class;
/* 174 */       declaredType = String.class;
/*     */     }
/*     */     else {
/* 177 */       outputValue = value;
/* 178 */       valueType = getReturnValueType(outputValue, returnType);
/* 179 */       declaredType = getGenericType(returnType);
/*     */     }
/*     */     
/* 182 */     HttpServletRequest request = inputMessage.getServletRequest();
/* 183 */     List<MediaType> requestedMediaTypes = getAcceptableMediaTypes(request);
/* 184 */     List<MediaType> producibleMediaTypes = getProducibleMediaTypes(request, valueType, declaredType);
/*     */     
/* 186 */     if ((outputValue != null) && (producibleMediaTypes.isEmpty())) {
/* 187 */       throw new IllegalArgumentException("No converter found for return value of type: " + valueType);
/*     */     }
/*     */     
/* 190 */     Set<MediaType> compatibleMediaTypes = new LinkedHashSet();
/* 191 */     for (Iterator localIterator1 = requestedMediaTypes.iterator(); localIterator1.hasNext();) { requestedType = (MediaType)localIterator1.next();
/* 192 */       for (MediaType producibleType : producibleMediaTypes) {
/* 193 */         if (requestedType.isCompatibleWith(producibleType))
/* 194 */           compatibleMediaTypes.add(getMostSpecificMediaType(requestedType, producibleType));
/*     */       }
/*     */     }
/*     */     MediaType requestedType;
/* 198 */     if (compatibleMediaTypes.isEmpty()) {
/* 199 */       if (outputValue != null) {
/* 200 */         throw new HttpMediaTypeNotAcceptableException(producibleMediaTypes);
/*     */       }
/* 202 */       return;
/*     */     }
/*     */     
/* 205 */     Object mediaTypes = new ArrayList(compatibleMediaTypes);
/* 206 */     MediaType.sortBySpecificityAndQuality((List)mediaTypes);
/*     */     
/* 208 */     MediaType selectedMediaType = null;
/* 209 */     for (MediaType mediaType : (List)mediaTypes) {
/* 210 */       if (mediaType.isConcrete()) {
/* 211 */         selectedMediaType = mediaType;
/* 212 */         break;
/*     */       }
/* 214 */       if ((mediaType.equals(MediaType.ALL)) || (mediaType.equals(MEDIA_TYPE_APPLICATION))) {
/* 215 */         selectedMediaType = MediaType.APPLICATION_OCTET_STREAM;
/* 216 */         break;
/*     */       }
/*     */     }
/*     */     
/* 220 */     if (selectedMediaType != null) {
/* 221 */       selectedMediaType = selectedMediaType.removeQualityValue();
/* 222 */       for (HttpMessageConverter<?> messageConverter : this.messageConverters) {
/* 223 */         if ((messageConverter instanceof GenericHttpMessageConverter)) {
/* 224 */           if (((GenericHttpMessageConverter)messageConverter).canWrite(declaredType, valueType, selectedMediaType))
/*     */           {
/* 226 */             outputValue = getAdvice().beforeBodyWrite(outputValue, returnType, selectedMediaType, messageConverter
/* 227 */               .getClass(), inputMessage, outputMessage);
/*     */             
/* 229 */             if (outputValue != null) {
/* 230 */               addContentDispositionHeader(inputMessage, outputMessage);
/* 231 */               ((GenericHttpMessageConverter)messageConverter).write(outputValue, declaredType, selectedMediaType, outputMessage);
/*     */               
/* 233 */               if (this.logger.isDebugEnabled()) {
/* 234 */                 this.logger.debug("Written [" + outputValue + "] as \"" + selectedMediaType + "\" using [" + messageConverter + "]");
/*     */               }
/*     */               
/*     */             }
/*     */             
/*     */           }
/*     */         }
/* 241 */         else if (messageConverter.canWrite(valueType, selectedMediaType)) {
/* 242 */           outputValue = getAdvice().beforeBodyWrite(outputValue, returnType, selectedMediaType, messageConverter
/* 243 */             .getClass(), inputMessage, outputMessage);
/*     */           
/* 245 */           if (outputValue != null) {
/* 246 */             addContentDispositionHeader(inputMessage, outputMessage);
/* 247 */             messageConverter.write(outputValue, selectedMediaType, outputMessage);
/* 248 */             if (this.logger.isDebugEnabled()) {
/* 249 */               this.logger.debug("Written [" + outputValue + "] as \"" + selectedMediaType + "\" using [" + messageConverter + "]");
/*     */             }
/*     */           }
/*     */           
/* 253 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 258 */     if (outputValue != null) {
/* 259 */       throw new HttpMediaTypeNotAcceptableException(this.allSupportedMediaTypes);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Class<?> getReturnValueType(Object value, MethodParameter returnType)
/*     */   {
/* 270 */     return value != null ? value.getClass() : returnType.getParameterType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Type getGenericType(MethodParameter returnType)
/*     */   {
/* 278 */     if (HttpEntity.class.isAssignableFrom(returnType.getParameterType())) {
/* 279 */       return ResolvableType.forType(returnType.getGenericParameterType()).getGeneric(new int[] { 0 }).getType();
/*     */     }
/*     */     
/* 282 */     return returnType.getGenericParameterType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected List<MediaType> getProducibleMediaTypes(HttpServletRequest request, Class<?> valueClass)
/*     */   {
/* 291 */     return getProducibleMediaTypes(request, valueClass, null);
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
/*     */   protected List<MediaType> getProducibleMediaTypes(HttpServletRequest request, Class<?> valueClass, Type declaredType)
/*     */   {
/* 305 */     Set<MediaType> mediaTypes = (Set)request.getAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);
/* 306 */     if (!CollectionUtils.isEmpty(mediaTypes)) {
/* 307 */       return new ArrayList(mediaTypes);
/*     */     }
/* 309 */     if (!this.allSupportedMediaTypes.isEmpty()) {
/* 310 */       List<MediaType> result = new ArrayList();
/* 311 */       for (HttpMessageConverter<?> converter : this.messageConverters) {
/* 312 */         if (((converter instanceof GenericHttpMessageConverter)) && (declaredType != null)) {
/* 313 */           if (((GenericHttpMessageConverter)converter).canWrite(declaredType, valueClass, null)) {
/* 314 */             result.addAll(converter.getSupportedMediaTypes());
/*     */           }
/*     */         }
/* 317 */         else if (converter.canWrite(valueClass, null)) {
/* 318 */           result.addAll(converter.getSupportedMediaTypes());
/*     */         }
/*     */       }
/* 321 */       return result;
/*     */     }
/*     */     
/* 324 */     return Collections.singletonList(MediaType.ALL);
/*     */   }
/*     */   
/*     */   private List<MediaType> getAcceptableMediaTypes(HttpServletRequest request) throws HttpMediaTypeNotAcceptableException
/*     */   {
/* 329 */     List<MediaType> mediaTypes = this.contentNegotiationManager.resolveMediaTypes(new ServletWebRequest(request));
/* 330 */     return mediaTypes.isEmpty() ? Collections.singletonList(MediaType.ALL) : mediaTypes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private MediaType getMostSpecificMediaType(MediaType acceptType, MediaType produceType)
/*     */   {
/* 338 */     MediaType produceTypeToUse = produceType.copyQualityValue(acceptType);
/* 339 */     return MediaType.SPECIFICITY_COMPARATOR.compare(acceptType, produceTypeToUse) <= 0 ? acceptType : produceTypeToUse;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addContentDispositionHeader(ServletServerHttpRequest request, ServletServerHttpResponse response)
/*     */   {
/* 351 */     HttpHeaders headers = response.getHeaders();
/* 352 */     if (headers.containsKey("Content-Disposition")) {
/* 353 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 357 */       int status = response.getServletResponse().getStatus();
/* 358 */       if ((status < 200) || (status > 299)) {
/* 359 */         return;
/*     */       }
/*     */     }
/*     */     catch (Throwable localThrowable) {}
/*     */     
/*     */ 
/*     */ 
/* 366 */     HttpServletRequest servletRequest = request.getServletRequest();
/* 367 */     String requestUri = RAW_URL_PATH_HELPER.getOriginatingRequestUri(servletRequest);
/*     */     
/* 369 */     int index = requestUri.lastIndexOf('/') + 1;
/* 370 */     String filename = requestUri.substring(index);
/* 371 */     String pathParams = "";
/*     */     
/* 373 */     index = filename.indexOf(';');
/* 374 */     if (index != -1) {
/* 375 */       pathParams = filename.substring(index);
/* 376 */       filename = filename.substring(0, index);
/*     */     }
/*     */     
/* 379 */     filename = DECODING_URL_PATH_HELPER.decodeRequestString(servletRequest, filename);
/* 380 */     String ext = StringUtils.getFilenameExtension(filename);
/*     */     
/* 382 */     pathParams = DECODING_URL_PATH_HELPER.decodeRequestString(servletRequest, pathParams);
/* 383 */     String extInPathParams = StringUtils.getFilenameExtension(pathParams);
/*     */     
/* 385 */     if ((!safeExtension(servletRequest, ext)) || (!safeExtension(servletRequest, extInPathParams))) {
/* 386 */       headers.add("Content-Disposition", "inline;filename=f.txt");
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean safeExtension(HttpServletRequest request, String extension)
/*     */   {
/* 392 */     if (!StringUtils.hasText(extension)) {
/* 393 */       return true;
/*     */     }
/* 395 */     extension = extension.toLowerCase(Locale.ENGLISH);
/* 396 */     if (this.safeExtensions.contains(extension)) {
/* 397 */       return true;
/*     */     }
/* 399 */     String pattern = (String)request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
/* 400 */     if ((pattern != null) && (pattern.endsWith("." + extension))) {
/* 401 */       return true;
/*     */     }
/* 403 */     if (extension.equals("html")) {
/* 404 */       String name = HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE;
/* 405 */       Set<MediaType> mediaTypes = (Set)request.getAttribute(name);
/* 406 */       if ((!CollectionUtils.isEmpty(mediaTypes)) && (mediaTypes.contains(MediaType.TEXT_HTML))) {
/* 407 */         return true;
/*     */       }
/*     */     }
/* 410 */     return safeMediaTypesForExtension(extension);
/*     */   }
/*     */   
/*     */   private boolean safeMediaTypesForExtension(String extension) {
/* 414 */     List<MediaType> mediaTypes = null;
/*     */     try {
/* 416 */       mediaTypes = this.pathStrategy.resolveMediaTypeKey(null, extension);
/*     */     }
/*     */     catch (HttpMediaTypeNotAcceptableException localHttpMediaTypeNotAcceptableException) {}
/*     */     
/*     */ 
/* 421 */     if (CollectionUtils.isEmpty(mediaTypes)) {
/* 422 */       return false;
/*     */     }
/* 424 */     for (MediaType mediaType : mediaTypes) {
/* 425 */       if (!safeMediaType(mediaType)) {
/* 426 */         return false;
/*     */       }
/*     */     }
/* 429 */     return true;
/*     */   }
/*     */   
/*     */   private boolean safeMediaType(MediaType mediaType) {
/* 433 */     return (WHITELISTED_MEDIA_BASE_TYPES.contains(mediaType.getType())) || 
/* 434 */       (mediaType.getSubtype().endsWith("+xml"));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\AbstractMessageConverterMethodProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */