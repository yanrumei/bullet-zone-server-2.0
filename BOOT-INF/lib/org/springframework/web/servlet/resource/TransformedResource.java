/*    */ package org.springframework.web.servlet.resource;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.springframework.core.io.ByteArrayResource;
/*    */ import org.springframework.core.io.Resource;
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
/*    */ public class TransformedResource
/*    */   extends ByteArrayResource
/*    */ {
/*    */   private final String filename;
/*    */   private final long lastModified;
/*    */   
/*    */   public TransformedResource(Resource original, byte[] transformedContent)
/*    */   {
/* 41 */     super(transformedContent);
/* 42 */     this.filename = original.getFilename();
/*    */     try {
/* 44 */       this.lastModified = original.lastModified();
/*    */     }
/*    */     catch (IOException ex)
/*    */     {
/* 48 */       throw new IllegalArgumentException(ex);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public String getFilename()
/*    */   {
/* 55 */     return this.filename;
/*    */   }
/*    */   
/*    */   public long lastModified() throws IOException
/*    */   {
/* 60 */     return this.lastModified;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\TransformedResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */