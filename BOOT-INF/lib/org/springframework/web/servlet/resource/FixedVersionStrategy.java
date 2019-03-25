/*    */ package org.springframework.web.servlet.resource;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FixedVersionStrategy
/*    */   extends AbstractVersionStrategy
/*    */ {
/*    */   private final String version;
/*    */   
/*    */   public FixedVersionStrategy(String version)
/*    */   {
/* 44 */     super(new AbstractVersionStrategy.PrefixVersionPathStrategy(version));
/* 45 */     this.version = version;
/*    */   }
/*    */   
/*    */ 
/*    */   public String getResourceVersion(Resource resource)
/*    */   {
/* 51 */     return this.version;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\FixedVersionStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */