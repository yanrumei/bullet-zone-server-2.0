/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.json.MappingJacksonValue;
/*     */ import org.springframework.http.server.ServerHttpRequest;
/*     */ import org.springframework.http.server.ServerHttpResponse;
/*     */ import org.springframework.http.server.ServletServerHttpRequest;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractJsonpResponseBodyAdvice
/*     */   extends AbstractMappingJacksonResponseBodyAdvice
/*     */ {
/*  54 */   private static final Pattern CALLBACK_PARAM_PATTERN = Pattern.compile("[0-9A-Za-z_\\.]*");
/*     */   
/*     */ 
/*  57 */   private final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private final String[] jsonpQueryParamNames;
/*     */   
/*     */   protected AbstractJsonpResponseBodyAdvice(String... queryParamNames)
/*     */   {
/*  63 */     Assert.isTrue(!ObjectUtils.isEmpty(queryParamNames), "At least one query param name is required");
/*  64 */     this.jsonpQueryParamNames = queryParamNames;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType, MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response)
/*     */   {
/*  72 */     HttpServletRequest servletRequest = ((ServletServerHttpRequest)request).getServletRequest();
/*     */     
/*  74 */     for (String name : this.jsonpQueryParamNames) {
/*  75 */       String value = servletRequest.getParameter(name);
/*  76 */       if (value != null) {
/*  77 */         if (!isValidJsonpQueryParam(value)) {
/*  78 */           if (this.logger.isDebugEnabled()) {
/*  79 */             this.logger.debug("Ignoring invalid jsonp parameter value: " + value);
/*     */           }
/*     */         }
/*     */         else {
/*  83 */           MediaType contentTypeToUse = getContentType(contentType, request, response);
/*  84 */           response.getHeaders().setContentType(contentTypeToUse);
/*  85 */           bodyContainer.setJsonpFunction(value);
/*  86 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isValidJsonpQueryParam(String value)
/*     */   {
/*  99 */     return CALLBACK_PARAM_PATTERN.matcher(value).matches();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MediaType getContentType(MediaType contentType, ServerHttpRequest request, ServerHttpResponse response)
/*     */   {
/* 111 */     return new MediaType("application", "javascript");
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\AbstractJsonpResponseBodyAdvice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */