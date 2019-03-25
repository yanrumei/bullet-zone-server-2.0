/*    */ package org.springframework.web.servlet.resource;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.util.DigestUtils;
/*    */ import org.springframework.util.FileCopyUtils;
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
/*    */ public class ContentVersionStrategy
/*    */   extends AbstractVersionStrategy
/*    */ {
/*    */   public ContentVersionStrategy()
/*    */   {
/* 38 */     super(new AbstractVersionStrategy.FileNameVersionPathStrategy());
/*    */   }
/*    */   
/*    */   public String getResourceVersion(Resource resource)
/*    */   {
/*    */     try {
/* 44 */       byte[] content = FileCopyUtils.copyToByteArray(resource.getInputStream());
/* 45 */       return DigestUtils.md5DigestAsHex(content);
/*    */     }
/*    */     catch (IOException ex) {
/* 48 */       throw new IllegalStateException("Failed to calculate hash for " + resource, ex);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\ContentVersionStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */