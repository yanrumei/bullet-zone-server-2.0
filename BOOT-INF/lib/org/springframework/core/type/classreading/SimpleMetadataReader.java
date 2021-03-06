/*    */ package org.springframework.core.type.classreading;
/*    */ 
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.springframework.asm.ClassReader;
/*    */ import org.springframework.core.NestedIOException;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.core.type.AnnotationMetadata;
/*    */ import org.springframework.core.type.ClassMetadata;
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
/*    */ final class SimpleMetadataReader
/*    */   implements MetadataReader
/*    */ {
/*    */   private final Resource resource;
/*    */   private final ClassMetadata classMetadata;
/*    */   private final AnnotationMetadata annotationMetadata;
/*    */   
/*    */   SimpleMetadataReader(Resource resource, ClassLoader classLoader)
/*    */     throws IOException
/*    */   {
/* 50 */     InputStream is = new BufferedInputStream(resource.getInputStream());
/*    */     try
/*    */     {
/* 53 */       classReader = new ClassReader(is);
/*    */     } catch (IllegalArgumentException ex) {
/*    */       ClassReader classReader;
/* 56 */       throw new NestedIOException("ASM ClassReader failed to parse class file - probably due to a new Java class file version that isn't supported yet: " + resource, ex);
/*    */     }
/*    */     finally
/*    */     {
/* 60 */       is.close();
/*    */     }
/*    */     ClassReader classReader;
/* 63 */     AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor(classLoader);
/* 64 */     classReader.accept(visitor, 2);
/*    */     
/* 66 */     this.annotationMetadata = visitor;
/*    */     
/* 68 */     this.classMetadata = visitor;
/* 69 */     this.resource = resource;
/*    */   }
/*    */   
/*    */ 
/*    */   public Resource getResource()
/*    */   {
/* 75 */     return this.resource;
/*    */   }
/*    */   
/*    */   public ClassMetadata getClassMetadata()
/*    */   {
/* 80 */     return this.classMetadata;
/*    */   }
/*    */   
/*    */   public AnnotationMetadata getAnnotationMetadata()
/*    */   {
/* 85 */     return this.annotationMetadata;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\type\classreading\SimpleMetadataReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */