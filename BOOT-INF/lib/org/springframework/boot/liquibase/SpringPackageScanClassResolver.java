/*     */ package org.springframework.boot.liquibase;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import liquibase.servicelocator.DefaultPackageScanClassResolver;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
/*     */ import org.springframework.core.io.support.ResourcePatternResolver;
/*     */ import org.springframework.core.type.ClassMetadata;
/*     */ import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
/*     */ import org.springframework.core.type.classreading.MetadataReader;
/*     */ import org.springframework.core.type.classreading.MetadataReaderFactory;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SpringPackageScanClassResolver
/*     */   extends DefaultPackageScanClassResolver
/*     */ {
/*     */   private final Log logger;
/*     */   
/*     */   public SpringPackageScanClassResolver(Log logger)
/*     */   {
/*  45 */     this.logger = logger;
/*     */   }
/*     */   
/*     */   protected void findAllClasses(String packageName, ClassLoader loader)
/*     */   {
/*  50 */     MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(loader);
/*     */     try
/*     */     {
/*  53 */       Resource[] resources = scan(loader, packageName);
/*  54 */       for (Resource resource : resources) {
/*  55 */         Class<?> clazz = loadClass(loader, metadataReaderFactory, resource);
/*  56 */         if (clazz != null) {
/*  57 */           addFoundClass(clazz);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (IOException ex) {
/*  62 */       throw new IllegalStateException(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private Resource[] scan(ClassLoader loader, String packageName) throws IOException {
/*  67 */     ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(loader);
/*     */     
/*     */ 
/*  70 */     String pattern = "classpath*:" + ClassUtils.convertClassNameToResourcePath(packageName) + "/**/*.class";
/*  71 */     Resource[] resources = resolver.getResources(pattern);
/*  72 */     return resources;
/*     */   }
/*     */   
/*     */   private Class<?> loadClass(ClassLoader loader, MetadataReaderFactory readerFactory, Resource resource)
/*     */   {
/*     */     try {
/*  78 */       MetadataReader reader = readerFactory.getMetadataReader(resource);
/*  79 */       return ClassUtils.forName(reader.getClassMetadata().getClassName(), loader);
/*     */     }
/*     */     catch (ClassNotFoundException ex) {
/*  82 */       handleFailure(resource, ex);
/*  83 */       return null;
/*     */     }
/*     */     catch (LinkageError ex) {
/*  86 */       handleFailure(resource, ex);
/*  87 */       return null;
/*     */     }
/*     */     catch (Throwable ex) {
/*  90 */       if (this.logger.isWarnEnabled()) {
/*  91 */         this.logger.warn("Unexpected failure when loading class resource " + resource, ex);
/*     */       }
/*     */     }
/*  94 */     return null;
/*     */   }
/*     */   
/*     */   private void handleFailure(Resource resource, Throwable ex)
/*     */   {
/*  99 */     if (this.logger.isDebugEnabled()) {
/* 100 */       this.logger.debug("Ignoring candidate class resource " + resource + " due to " + ex);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\liquibase\SpringPackageScanClassResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */