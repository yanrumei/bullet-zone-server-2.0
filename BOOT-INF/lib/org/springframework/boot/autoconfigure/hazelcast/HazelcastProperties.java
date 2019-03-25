/*    */ package org.springframework.boot.autoconfigure.hazelcast;
/*    */ 
/*    */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.util.Assert;
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
/*    */ @ConfigurationProperties(prefix="spring.hazelcast")
/*    */ public class HazelcastProperties
/*    */ {
/*    */   private Resource config;
/*    */   
/*    */   public Resource getConfig()
/*    */   {
/* 38 */     return this.config;
/*    */   }
/*    */   
/*    */   public void setConfig(Resource config) {
/* 42 */     this.config = config;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Resource resolveConfigLocation()
/*    */   {
/* 52 */     if (this.config == null) {
/* 53 */       return null;
/*    */     }
/* 55 */     Assert.isTrue(this.config.exists(), "Hazelcast configuration does not exist '" + this.config
/* 56 */       .getDescription() + "'");
/* 57 */     return this.config;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\hazelcast\HazelcastProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */