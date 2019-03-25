/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
/*     */ import org.springframework.web.accept.ContentNegotiationStrategy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContentNegotiationConfigurer
/*     */ {
/*  94 */   private final ContentNegotiationManagerFactoryBean factory = new ContentNegotiationManagerFactoryBean();
/*     */   
/*  96 */   private final Map<String, MediaType> mediaTypes = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ContentNegotiationConfigurer(ServletContext servletContext)
/*     */   {
/* 103 */     this.factory.setServletContext(servletContext);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ContentNegotiationConfigurer favorPathExtension(boolean favorPathExtension)
/*     */   {
/* 115 */     this.factory.setFavorPathExtension(favorPathExtension);
/* 116 */     return this;
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
/*     */   public ContentNegotiationConfigurer mediaType(String extension, MediaType mediaType)
/*     */   {
/* 135 */     this.mediaTypes.put(extension, mediaType);
/* 136 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ContentNegotiationConfigurer mediaTypes(Map<String, MediaType> mediaTypes)
/*     */   {
/* 145 */     if (mediaTypes != null) {
/* 146 */       this.mediaTypes.putAll(mediaTypes);
/*     */     }
/* 148 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ContentNegotiationConfigurer replaceMediaTypes(Map<String, MediaType> mediaTypes)
/*     */   {
/* 157 */     this.mediaTypes.clear();
/* 158 */     mediaTypes(mediaTypes);
/* 159 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ContentNegotiationConfigurer ignoreUnknownPathExtensions(boolean ignore)
/*     */   {
/* 169 */     this.factory.setIgnoreUnknownPathExtensions(ignore);
/* 170 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ContentNegotiationConfigurer useJaf(boolean useJaf)
/*     */   {
/* 181 */     this.factory.setUseJaf(useJaf);
/* 182 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ContentNegotiationConfigurer favorParameter(boolean favorParameter)
/*     */   {
/* 193 */     this.factory.setFavorParameter(favorParameter);
/* 194 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ContentNegotiationConfigurer parameterName(String parameterName)
/*     */   {
/* 202 */     this.factory.setParameterName(parameterName);
/* 203 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ContentNegotiationConfigurer ignoreAcceptHeader(boolean ignoreAcceptHeader)
/*     */   {
/* 211 */     this.factory.setIgnoreAcceptHeader(ignoreAcceptHeader);
/* 212 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ContentNegotiationConfigurer defaultContentType(MediaType defaultContentType)
/*     */   {
/* 221 */     this.factory.setDefaultContentType(defaultContentType);
/* 222 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ContentNegotiationConfigurer defaultContentTypeStrategy(ContentNegotiationStrategy defaultStrategy)
/*     */   {
/* 233 */     this.factory.setDefaultContentTypeStrategy(defaultStrategy);
/* 234 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ContentNegotiationManager buildContentNegotiationManager()
/*     */   {
/* 244 */     this.factory.addMediaTypes(this.mediaTypes);
/* 245 */     this.factory.afterPropertiesSet();
/* 246 */     return this.factory.getObject();
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   protected ContentNegotiationManager getContentNegotiationManager()
/*     */     throws Exception
/*     */   {
/* 254 */     return buildContentNegotiationManager();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\annotation\ContentNegotiationConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */