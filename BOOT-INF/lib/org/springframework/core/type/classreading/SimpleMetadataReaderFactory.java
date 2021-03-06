/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import org.springframework.core.io.DefaultResourceLoader;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleMetadataReaderFactory
/*     */   implements MetadataReaderFactory
/*     */ {
/*     */   private final ResourceLoader resourceLoader;
/*     */   
/*     */   public SimpleMetadataReaderFactory()
/*     */   {
/*  43 */     this.resourceLoader = new DefaultResourceLoader();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleMetadataReaderFactory(ResourceLoader resourceLoader)
/*     */   {
/*  52 */     this.resourceLoader = (resourceLoader != null ? resourceLoader : new DefaultResourceLoader());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleMetadataReaderFactory(ClassLoader classLoader)
/*     */   {
/*  60 */     this.resourceLoader = (classLoader != null ? new DefaultResourceLoader(classLoader) : new DefaultResourceLoader());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final ResourceLoader getResourceLoader()
/*     */   {
/*  70 */     return this.resourceLoader;
/*     */   }
/*     */   
/*     */   public MetadataReader getMetadataReader(String className)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  78 */       String resourcePath = "classpath:" + ClassUtils.convertClassNameToResourcePath(className) + ".class";
/*  79 */       Resource resource = this.resourceLoader.getResource(resourcePath);
/*  80 */       return getMetadataReader(resource);
/*     */ 
/*     */     }
/*     */     catch (FileNotFoundException ex)
/*     */     {
/*  85 */       int lastDotIndex = className.lastIndexOf('.');
/*  86 */       if (lastDotIndex != -1)
/*     */       {
/*  88 */         String innerClassName = className.substring(0, lastDotIndex) + '$' + className.substring(lastDotIndex + 1);
/*     */         
/*  90 */         String innerClassResourcePath = "classpath:" + ClassUtils.convertClassNameToResourcePath(innerClassName) + ".class";
/*  91 */         Resource innerClassResource = this.resourceLoader.getResource(innerClassResourcePath);
/*  92 */         if (innerClassResource.exists()) {
/*  93 */           return getMetadataReader(innerClassResource);
/*     */         }
/*     */       }
/*  96 */       throw ex;
/*     */     }
/*     */   }
/*     */   
/*     */   public MetadataReader getMetadataReader(Resource resource) throws IOException
/*     */   {
/* 102 */     return new SimpleMetadataReader(resource, this.resourceLoader.getClassLoader());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\type\classreading\SimpleMetadataReaderFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */