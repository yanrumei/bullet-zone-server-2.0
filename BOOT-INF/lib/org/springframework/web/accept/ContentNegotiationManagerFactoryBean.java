/*     */ package org.springframework.web.accept;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.web.context.ServletContextAware;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContentNegotiationManagerFactoryBean
/*     */   implements FactoryBean<ContentNegotiationManager>, ServletContextAware, InitializingBean
/*     */ {
/*  94 */   private boolean favorPathExtension = true;
/*     */   
/*  96 */   private boolean favorParameter = false;
/*     */   
/*  98 */   private boolean ignoreAcceptHeader = false;
/*     */   
/* 100 */   private Map<String, MediaType> mediaTypes = new HashMap();
/*     */   
/* 102 */   private boolean ignoreUnknownPathExtensions = true;
/*     */   
/*     */   private Boolean useJaf;
/*     */   
/* 106 */   private String parameterName = "format";
/*     */   
/*     */ 
/*     */ 
/*     */   private ContentNegotiationStrategy defaultNegotiationStrategy;
/*     */   
/*     */ 
/*     */ 
/*     */   private ContentNegotiationManager contentNegotiationManager;
/*     */   
/*     */ 
/*     */   private ServletContext servletContext;
/*     */   
/*     */ 
/*     */ 
/*     */   public void setFavorPathExtension(boolean favorPathExtension)
/*     */   {
/* 123 */     this.favorPathExtension = favorPathExtension;
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
/*     */   public void setMediaTypes(Properties mediaTypes)
/*     */   {
/* 141 */     if (!CollectionUtils.isEmpty(mediaTypes)) {
/* 142 */       for (Map.Entry<Object, Object> entry : mediaTypes.entrySet()) {
/* 143 */         String extension = ((String)entry.getKey()).toLowerCase(Locale.ENGLISH);
/* 144 */         MediaType mediaType = MediaType.valueOf((String)entry.getValue());
/* 145 */         this.mediaTypes.put(extension, mediaType);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addMediaType(String fileExtension, MediaType mediaType)
/*     */   {
/* 156 */     this.mediaTypes.put(fileExtension, mediaType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addMediaTypes(Map<String, MediaType> mediaTypes)
/*     */   {
/* 165 */     if (mediaTypes != null) {
/* 166 */       this.mediaTypes.putAll(mediaTypes);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIgnoreUnknownPathExtensions(boolean ignore)
/*     */   {
/* 177 */     this.ignoreUnknownPathExtensions = ignore;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUseJaf(boolean useJaf)
/*     */   {
/* 188 */     this.useJaf = Boolean.valueOf(useJaf);
/*     */   }
/*     */   
/*     */   private boolean isUseJafTurnedOff() {
/* 192 */     return (this.useJaf != null) && (!this.useJaf.booleanValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFavorParameter(boolean favorParameter)
/*     */   {
/* 203 */     this.favorParameter = favorParameter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setParameterName(String parameterName)
/*     */   {
/* 211 */     Assert.notNull(parameterName, "parameterName is required");
/* 212 */     this.parameterName = parameterName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIgnoreAcceptHeader(boolean ignoreAcceptHeader)
/*     */   {
/* 220 */     this.ignoreAcceptHeader = ignoreAcceptHeader;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDefaultContentType(MediaType contentType)
/*     */   {
/* 229 */     this.defaultNegotiationStrategy = new FixedContentNegotiationStrategy(contentType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDefaultContentTypeStrategy(ContentNegotiationStrategy strategy)
/*     */   {
/* 240 */     this.defaultNegotiationStrategy = strategy;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setServletContext(ServletContext servletContext)
/*     */   {
/* 248 */     this.servletContext = servletContext;
/*     */   }
/*     */   
/*     */ 
/*     */   public void afterPropertiesSet()
/*     */   {
/* 254 */     List<ContentNegotiationStrategy> strategies = new ArrayList();
/*     */     
/* 256 */     if (this.favorPathExtension) { PathExtensionContentNegotiationStrategy strategy;
/*     */       PathExtensionContentNegotiationStrategy strategy;
/* 258 */       if ((this.servletContext != null) && (!isUseJafTurnedOff())) {
/* 259 */         strategy = new ServletPathExtensionContentNegotiationStrategy(this.servletContext, this.mediaTypes);
/*     */       }
/*     */       else {
/* 262 */         strategy = new PathExtensionContentNegotiationStrategy(this.mediaTypes);
/*     */       }
/* 264 */       strategy.setIgnoreUnknownExtensions(this.ignoreUnknownPathExtensions);
/* 265 */       if (this.useJaf != null) {
/* 266 */         strategy.setUseJaf(this.useJaf.booleanValue());
/*     */       }
/* 268 */       strategies.add(strategy);
/*     */     }
/*     */     
/* 271 */     if (this.favorParameter) {
/* 272 */       ParameterContentNegotiationStrategy strategy = new ParameterContentNegotiationStrategy(this.mediaTypes);
/* 273 */       strategy.setParameterName(this.parameterName);
/* 274 */       strategies.add(strategy);
/*     */     }
/*     */     
/* 277 */     if (!this.ignoreAcceptHeader) {
/* 278 */       strategies.add(new HeaderContentNegotiationStrategy());
/*     */     }
/*     */     
/* 281 */     if (this.defaultNegotiationStrategy != null) {
/* 282 */       strategies.add(this.defaultNegotiationStrategy);
/*     */     }
/*     */     
/* 285 */     this.contentNegotiationManager = new ContentNegotiationManager(strategies);
/*     */   }
/*     */   
/*     */   public ContentNegotiationManager getObject()
/*     */   {
/* 290 */     return this.contentNegotiationManager;
/*     */   }
/*     */   
/*     */   public Class<?> getObjectType()
/*     */   {
/* 295 */     return ContentNegotiationManager.class;
/*     */   }
/*     */   
/*     */   public boolean isSingleton()
/*     */   {
/* 300 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\accept\ContentNegotiationManagerFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */