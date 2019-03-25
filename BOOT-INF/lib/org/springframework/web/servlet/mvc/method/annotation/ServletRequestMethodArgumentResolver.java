/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.security.Principal;
/*     */ import java.time.ZoneId;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.lang.UsesJava8;
/*     */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.WebRequest;
/*     */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
/*     */ import org.springframework.web.multipart.MultipartRequest;
/*     */ import org.springframework.web.servlet.support.RequestContextUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletRequestMethodArgumentResolver
/*     */   implements HandlerMethodArgumentResolver
/*     */ {
/*     */   public boolean supportsParameter(MethodParameter parameter)
/*     */   {
/*  65 */     Class<?> paramType = parameter.getParameterType();
/*  66 */     return (WebRequest.class.isAssignableFrom(paramType)) || 
/*  67 */       (ServletRequest.class.isAssignableFrom(paramType)) || 
/*  68 */       (MultipartRequest.class.isAssignableFrom(paramType)) || 
/*  69 */       (HttpSession.class.isAssignableFrom(paramType)) || 
/*  70 */       (Principal.class.isAssignableFrom(paramType)) || 
/*  71 */       (InputStream.class.isAssignableFrom(paramType)) || 
/*  72 */       (Reader.class.isAssignableFrom(paramType)) || (HttpMethod.class == paramType) || (Locale.class == paramType) || (TimeZone.class == paramType) || 
/*     */       
/*     */ 
/*     */ 
/*  76 */       ("java.time.ZoneId".equals(paramType.getName()));
/*     */   }
/*     */   
/*     */ 
/*     */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
/*     */     throws Exception
/*     */   {
/*  83 */     Class<?> paramType = parameter.getParameterType();
/*  84 */     if (WebRequest.class.isAssignableFrom(paramType)) {
/*  85 */       if (!paramType.isInstance(webRequest))
/*     */       {
/*  87 */         throw new IllegalStateException("Current request is not of type [" + paramType.getName() + "]: " + webRequest);
/*     */       }
/*  89 */       return webRequest;
/*     */     }
/*     */     
/*  92 */     HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/*  93 */     if ((ServletRequest.class.isAssignableFrom(paramType)) || (MultipartRequest.class.isAssignableFrom(paramType))) {
/*  94 */       Object nativeRequest = webRequest.getNativeRequest(paramType);
/*  95 */       if (nativeRequest == null)
/*     */       {
/*  97 */         throw new IllegalStateException("Current request is not of type [" + paramType.getName() + "]: " + request);
/*     */       }
/*  99 */       return nativeRequest;
/*     */     }
/* 101 */     if (HttpSession.class.isAssignableFrom(paramType)) {
/* 102 */       HttpSession session = request.getSession();
/* 103 */       if ((session != null) && (!paramType.isInstance(session)))
/*     */       {
/* 105 */         throw new IllegalStateException("Current session is not of type [" + paramType.getName() + "]: " + session);
/*     */       }
/* 107 */       return session;
/*     */     }
/* 109 */     if (InputStream.class.isAssignableFrom(paramType)) {
/* 110 */       InputStream inputStream = request.getInputStream();
/* 111 */       if ((inputStream != null) && (!paramType.isInstance(inputStream)))
/*     */       {
/* 113 */         throw new IllegalStateException("Request input stream is not of type [" + paramType.getName() + "]: " + inputStream);
/*     */       }
/* 115 */       return inputStream;
/*     */     }
/* 117 */     if (Reader.class.isAssignableFrom(paramType)) {
/* 118 */       Reader reader = request.getReader();
/* 119 */       if ((reader != null) && (!paramType.isInstance(reader)))
/*     */       {
/* 121 */         throw new IllegalStateException("Request body reader is not of type [" + paramType.getName() + "]: " + reader);
/*     */       }
/* 123 */       return reader;
/*     */     }
/* 125 */     if (Principal.class.isAssignableFrom(paramType)) {
/* 126 */       Principal userPrincipal = request.getUserPrincipal();
/* 127 */       if ((userPrincipal != null) && (!paramType.isInstance(userPrincipal)))
/*     */       {
/* 129 */         throw new IllegalStateException("Current user principal is not of type [" + paramType.getName() + "]: " + userPrincipal);
/*     */       }
/* 131 */       return userPrincipal;
/*     */     }
/* 133 */     if (HttpMethod.class == paramType) {
/* 134 */       return HttpMethod.resolve(request.getMethod());
/*     */     }
/* 136 */     if (Locale.class == paramType) {
/* 137 */       return RequestContextUtils.getLocale(request);
/*     */     }
/* 139 */     if (TimeZone.class == paramType) {
/* 140 */       TimeZone timeZone = RequestContextUtils.getTimeZone(request);
/* 141 */       return timeZone != null ? timeZone : TimeZone.getDefault();
/*     */     }
/* 143 */     if ("java.time.ZoneId".equals(paramType.getName())) {
/* 144 */       return ZoneIdResolver.resolveZoneId(request);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 149 */     throw new UnsupportedOperationException("Unknown parameter type [" + paramType.getName() + "] in " + parameter.getMethod());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @UsesJava8
/*     */   private static class ZoneIdResolver
/*     */   {
/*     */     public static Object resolveZoneId(HttpServletRequest request)
/*     */     {
/* 161 */       TimeZone timeZone = RequestContextUtils.getTimeZone(request);
/* 162 */       return timeZone != null ? timeZone.toZoneId() : ZoneId.systemDefault();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\ServletRequestMethodArgumentResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */