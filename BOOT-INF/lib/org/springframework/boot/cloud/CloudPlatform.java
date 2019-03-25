/*    */ package org.springframework.boot.cloud;
/*    */ 
/*    */ import org.springframework.core.env.Environment;
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
/*    */ public enum CloudPlatform
/*    */ {
/* 34 */   CLOUD_FOUNDRY, 
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
/* 47 */   HEROKU;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private CloudPlatform() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract boolean isActive(Environment paramEnvironment);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isUsingForwardHeaders()
/*    */   {
/* 69 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static CloudPlatform getActive(Environment environment)
/*    */   {
/* 78 */     if (environment != null) {
/* 79 */       for (CloudPlatform cloudPlatform : values()) {
/* 80 */         if (cloudPlatform.isActive(environment)) {
/* 81 */           return cloudPlatform;
/*    */         }
/*    */       }
/*    */     }
/* 85 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\cloud\CloudPlatform.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */