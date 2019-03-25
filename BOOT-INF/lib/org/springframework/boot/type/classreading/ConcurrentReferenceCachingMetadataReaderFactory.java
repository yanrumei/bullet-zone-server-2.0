/*    */ package org.springframework.boot.type.classreading;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.core.io.ResourceLoader;
/*    */ import org.springframework.core.type.classreading.MetadataReader;
/*    */ import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
/*    */ import org.springframework.util.ConcurrentReferenceHashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConcurrentReferenceCachingMetadataReaderFactory
/*    */   extends SimpleMetadataReaderFactory
/*    */ {
/* 42 */   private final Map<Resource, MetadataReader> cache = new ConcurrentReferenceHashMap();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ConcurrentReferenceCachingMetadataReaderFactory() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ConcurrentReferenceCachingMetadataReaderFactory(ResourceLoader resourceLoader)
/*    */   {
/* 60 */     super(resourceLoader);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ConcurrentReferenceCachingMetadataReaderFactory(ClassLoader classLoader)
/*    */   {
/* 69 */     super(classLoader);
/*    */   }
/*    */   
/*    */   public MetadataReader getMetadataReader(Resource resource) throws IOException
/*    */   {
/* 74 */     MetadataReader metadataReader = (MetadataReader)this.cache.get(resource);
/* 75 */     if (metadataReader == null) {
/* 76 */       metadataReader = createMetadataReader(resource);
/* 77 */       this.cache.put(resource, metadataReader);
/*    */     }
/* 79 */     return metadataReader;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected MetadataReader createMetadataReader(Resource resource)
/*    */     throws IOException
/*    */   {
/* 89 */     return super.getMetadataReader(resource);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void clearCache()
/*    */   {
/* 96 */     this.cache.clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\type\classreading\ConcurrentReferenceCachingMetadataReaderFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */