/*     */ package org.springframework.web.servlet.mvc.support;
/*     */ 
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class ControllerClassNameHandlerMapping
/*     */   extends AbstractControllerUrlHandlerMapping
/*     */ {
/*     */   private static final String CONTROLLER_SUFFIX = "Controller";
/*  70 */   private boolean caseSensitive = false;
/*     */   
/*     */ 
/*     */ 
/*     */   private String pathPrefix;
/*     */   
/*     */ 
/*     */ 
/*     */   private String basePackage;
/*     */   
/*     */ 
/*     */ 
/*     */   public void setCaseSensitive(boolean caseSensitive)
/*     */   {
/*  84 */     this.caseSensitive = caseSensitive;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPathPrefix(String prefixPath)
/*     */   {
/*  94 */     this.pathPrefix = prefixPath;
/*  95 */     if (StringUtils.hasLength(this.pathPrefix)) {
/*  96 */       if (!this.pathPrefix.startsWith("/")) {
/*  97 */         this.pathPrefix = ("/" + this.pathPrefix);
/*     */       }
/*  99 */       if (this.pathPrefix.endsWith("/")) {
/* 100 */         this.pathPrefix = this.pathPrefix.substring(0, this.pathPrefix.length() - 1);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBasePackage(String basePackage)
/*     */   {
/* 118 */     this.basePackage = basePackage;
/* 119 */     if ((StringUtils.hasLength(this.basePackage)) && (!this.basePackage.endsWith("."))) {
/* 120 */       this.basePackage += ".";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected String[] buildUrlsForHandler(String beanName, Class<?> beanClass)
/*     */   {
/* 127 */     return generatePathMappings(beanClass);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String[] generatePathMappings(Class<?> beanClass)
/*     */   {
/* 138 */     StringBuilder pathMapping = buildPathPrefix(beanClass);
/* 139 */     String className = ClassUtils.getShortName(beanClass);
/*     */     
/* 141 */     String path = className.endsWith("Controller") ? className.substring(0, className.lastIndexOf("Controller")) : className;
/* 142 */     if (path.length() > 0) {
/* 143 */       if (this.caseSensitive) {
/* 144 */         pathMapping.append(path.substring(0, 1).toLowerCase()).append(path.substring(1));
/*     */       }
/*     */       else {
/* 147 */         pathMapping.append(path.toLowerCase());
/*     */       }
/*     */     }
/* 150 */     if (isMultiActionControllerType(beanClass)) {
/* 151 */       return new String[] { pathMapping.toString(), pathMapping.toString() + "/*" };
/*     */     }
/*     */     
/* 154 */     return new String[] { pathMapping.toString() + "*" };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private StringBuilder buildPathPrefix(Class<?> beanClass)
/*     */   {
/* 164 */     StringBuilder pathMapping = new StringBuilder();
/* 165 */     if (this.pathPrefix != null) {
/* 166 */       pathMapping.append(this.pathPrefix);
/* 167 */       pathMapping.append("/");
/*     */     }
/*     */     else {
/* 170 */       pathMapping.append("/");
/*     */     }
/* 172 */     if (this.basePackage != null) {
/* 173 */       String packageName = ClassUtils.getPackageName(beanClass);
/* 174 */       if (packageName.startsWith(this.basePackage)) {
/* 175 */         String subPackage = packageName.substring(this.basePackage.length()).replace('.', '/');
/* 176 */         pathMapping.append(this.caseSensitive ? subPackage : subPackage.toLowerCase());
/* 177 */         pathMapping.append("/");
/*     */       }
/*     */     }
/* 180 */     return pathMapping;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\support\ControllerClassNameHandlerMapping.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */