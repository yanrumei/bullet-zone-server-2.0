/*     */ package org.springframework.web.servlet.mvc.annotation;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.ReflectionUtils.MethodCallback;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.HttpRequestMethodNotSupportedException;
/*     */ import org.springframework.web.bind.ServletRequestBindingException;
/*     */ import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
/*     */ import org.springframework.web.servlet.handler.AbstractDetectingUrlHandlerMapping;
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
/*     */ @Deprecated
/*     */ public class DefaultAnnotationHandlerMapping
/*     */   extends AbstractDetectingUrlHandlerMapping
/*     */ {
/*  87 */   static final String USE_DEFAULT_SUFFIX_PATTERN = DefaultAnnotationHandlerMapping.class.getName() + ".useDefaultSuffixPattern";
/*     */   
/*  89 */   private boolean useDefaultSuffixPattern = true;
/*     */   
/*  91 */   private final Map<Class<?>, RequestMapping> cachedMappings = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUseDefaultSuffixPattern(boolean useDefaultSuffixPattern)
/*     */   {
/* 103 */     this.useDefaultSuffixPattern = useDefaultSuffixPattern;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String[] determineUrlsForHandler(String beanName)
/*     */   {
/* 113 */     ApplicationContext context = getApplicationContext();
/* 114 */     Class<?> handlerType = context.getType(beanName);
/* 115 */     RequestMapping mapping = (RequestMapping)context.findAnnotationOnBean(beanName, RequestMapping.class);
/* 116 */     if (mapping != null)
/*     */     {
/* 118 */       this.cachedMappings.put(handlerType, mapping);
/* 119 */       Set<String> urls = new LinkedHashSet();
/* 120 */       String[] typeLevelPatterns = mapping.value();
/* 121 */       if (typeLevelPatterns.length > 0)
/*     */       {
/* 123 */         String[] methodLevelPatterns = determineUrlsForHandlerMethods(handlerType, true);
/* 124 */         for (String typeLevelPattern : typeLevelPatterns) {
/* 125 */           if (!typeLevelPattern.startsWith("/")) {
/* 126 */             typeLevelPattern = "/" + typeLevelPattern;
/*     */           }
/* 128 */           boolean hasEmptyMethodLevelMappings = false;
/* 129 */           for (String methodLevelPattern : methodLevelPatterns) {
/* 130 */             if (methodLevelPattern == null) {
/* 131 */               hasEmptyMethodLevelMappings = true;
/*     */             }
/*     */             else {
/* 134 */               String combinedPattern = getPathMatcher().combine(typeLevelPattern, methodLevelPattern);
/* 135 */               addUrlsForPath(urls, combinedPattern);
/*     */             }
/*     */           }
/* 138 */           if ((hasEmptyMethodLevelMappings) || 
/* 139 */             (org.springframework.web.servlet.mvc.Controller.class.isAssignableFrom(handlerType))) {
/* 140 */             addUrlsForPath(urls, typeLevelPattern);
/*     */           }
/*     */         }
/* 143 */         return StringUtils.toStringArray(urls);
/*     */       }
/*     */       
/*     */ 
/* 147 */       return determineUrlsForHandlerMethods(handlerType, false);
/*     */     }
/*     */     
/* 150 */     if (AnnotationUtils.findAnnotation(handlerType, org.springframework.stereotype.Controller.class) != null)
/*     */     {
/* 152 */       return determineUrlsForHandlerMethods(handlerType, false);
/*     */     }
/*     */     
/* 155 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String[] determineUrlsForHandlerMethods(Class<?> handlerType, final boolean hasTypeLevelMapping)
/*     */   {
/* 167 */     String[] subclassResult = determineUrlsForHandlerMethods(handlerType);
/* 168 */     if (subclassResult != null) {
/* 169 */       return subclassResult;
/*     */     }
/*     */     
/* 172 */     final Set<String> urls = new LinkedHashSet();
/* 173 */     Set<Class<?>> handlerTypes = new LinkedHashSet();
/* 174 */     handlerTypes.add(handlerType);
/* 175 */     handlerTypes.addAll(Arrays.asList(handlerType.getInterfaces()));
/* 176 */     for (Class<?> currentHandlerType : handlerTypes) {
/* 177 */       ReflectionUtils.doWithMethods(currentHandlerType, new ReflectionUtils.MethodCallback()
/*     */       {
/*     */         public void doWith(Method method) {
/* 180 */           RequestMapping mapping = (RequestMapping)AnnotationUtils.findAnnotation(method, RequestMapping.class);
/* 181 */           if (mapping != null) {
/* 182 */             String[] mappedPatterns = mapping.value();
/* 183 */             if (mappedPatterns.length > 0) {
/* 184 */               for (String mappedPattern : mappedPatterns) {
/* 185 */                 if ((!hasTypeLevelMapping) && (!mappedPattern.startsWith("/"))) {
/* 186 */                   mappedPattern = "/" + mappedPattern;
/*     */                 }
/* 188 */                 DefaultAnnotationHandlerMapping.this.addUrlsForPath(urls, mappedPattern);
/*     */               }
/*     */               
/* 191 */             } else if (hasTypeLevelMapping)
/*     */             {
/* 193 */               urls.add(null); } } } }, ReflectionUtils.USER_DECLARED_METHODS);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 199 */     return StringUtils.toStringArray(urls);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String[] determineUrlsForHandlerMethods(Class<?> handlerType)
/*     */   {
/* 208 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addUrlsForPath(Set<String> urls, String path)
/*     */   {
/* 217 */     urls.add(path);
/* 218 */     if ((this.useDefaultSuffixPattern) && (path.indexOf('.') == -1) && (!path.endsWith("/"))) {
/* 219 */       urls.add(path + ".*");
/* 220 */       urls.add(path + "/");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void validateHandler(Object handler, HttpServletRequest request)
/*     */     throws Exception
/*     */   {
/* 231 */     RequestMapping mapping = (RequestMapping)this.cachedMappings.get(handler.getClass());
/* 232 */     if (mapping == null) {
/* 233 */       mapping = (RequestMapping)AnnotationUtils.findAnnotation(handler.getClass(), RequestMapping.class);
/*     */     }
/* 235 */     if (mapping != null) {
/* 236 */       validateMapping(mapping, request);
/*     */     }
/* 238 */     request.setAttribute(USE_DEFAULT_SUFFIX_PATTERN, Boolean.valueOf(this.useDefaultSuffixPattern));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void validateMapping(RequestMapping mapping, HttpServletRequest request)
/*     */     throws Exception
/*     */   {
/* 249 */     RequestMethod[] mappedMethods = mapping.method();
/* 250 */     if (!ServletAnnotationMappingUtils.checkRequestMethod(mappedMethods, request)) {
/* 251 */       String[] supportedMethods = new String[mappedMethods.length];
/* 252 */       for (int i = 0; i < mappedMethods.length; i++) {
/* 253 */         supportedMethods[i] = mappedMethods[i].name();
/*     */       }
/* 255 */       throw new HttpRequestMethodNotSupportedException(request.getMethod(), supportedMethods);
/*     */     }
/*     */     
/* 258 */     String[] mappedParams = mapping.params();
/* 259 */     if (!ServletAnnotationMappingUtils.checkParameters(mappedParams, request)) {
/* 260 */       throw new UnsatisfiedServletRequestParameterException(mappedParams, request.getParameterMap());
/*     */     }
/*     */     
/* 263 */     String[] mappedHeaders = mapping.headers();
/* 264 */     if (!ServletAnnotationMappingUtils.checkHeaders(mappedHeaders, request))
/*     */     {
/* 266 */       throw new ServletRequestBindingException("Header conditions \"" + StringUtils.arrayToDelimitedString(mappedHeaders, ", ") + "\" not met for actual request");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean supportsTypeLevelMappings()
/*     */   {
/* 273 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\annotation\DefaultAnnotationHandlerMapping.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */