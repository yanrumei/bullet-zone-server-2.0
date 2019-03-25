/*    */ package org.springframework.core.io;
/*    */ 
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
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
/*    */ 
/*    */ public class DescriptiveResource
/*    */   extends AbstractResource
/*    */ {
/*    */   private final String description;
/*    */   
/*    */   public DescriptiveResource(String description)
/*    */   {
/* 43 */     this.description = (description != null ? description : "");
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean exists()
/*    */   {
/* 49 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isReadable()
/*    */   {
/* 54 */     return false;
/*    */   }
/*    */   
/*    */   public InputStream getInputStream()
/*    */     throws IOException
/*    */   {
/* 60 */     throw new FileNotFoundException(getDescription() + " cannot be opened because it does not point to a readable resource");
/*    */   }
/*    */   
/*    */   public String getDescription()
/*    */   {
/* 65 */     return this.description;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean equals(Object obj)
/*    */   {
/* 74 */     return (obj == this) || (((obj instanceof DescriptiveResource)) && 
/* 75 */       (((DescriptiveResource)obj).description.equals(this.description)));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 83 */     return this.description.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\io\DescriptiveResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */