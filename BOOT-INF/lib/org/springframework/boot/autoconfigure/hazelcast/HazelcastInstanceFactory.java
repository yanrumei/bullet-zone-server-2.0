/*    */ package org.springframework.boot.autoconfigure.hazelcast;
/*    */ 
/*    */ import com.hazelcast.config.Config;
/*    */ import com.hazelcast.config.XmlConfigBuilder;
/*    */ import com.hazelcast.core.Hazelcast;
/*    */ import com.hazelcast.core.HazelcastInstance;
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.ResourceUtils;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ public class HazelcastInstanceFactory
/*    */ {
/*    */   private Config config;
/*    */   
/*    */   public HazelcastInstanceFactory(Resource configLocation)
/*    */     throws IOException
/*    */   {
/* 49 */     Assert.notNull(configLocation, "ConfigLocation must not be null");
/* 50 */     this.config = getConfig(configLocation);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public HazelcastInstanceFactory(Config config)
/*    */   {
/* 58 */     Assert.notNull(config, "Config must not be null");
/* 59 */     this.config = config;
/*    */   }
/*    */   
/*    */   private Config getConfig(Resource configLocation) throws IOException {
/* 63 */     URL configUrl = configLocation.getURL();
/* 64 */     Config config = new XmlConfigBuilder(configUrl).build();
/* 65 */     if (ResourceUtils.isFileURL(configUrl)) {
/* 66 */       config.setConfigurationFile(configLocation.getFile());
/*    */     }
/*    */     else {
/* 69 */       config.setConfigurationUrl(configUrl);
/*    */     }
/* 71 */     return config;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public HazelcastInstance getHazelcastInstance()
/*    */   {
/* 79 */     if (StringUtils.hasText(this.config.getInstanceName())) {
/* 80 */       return Hazelcast.getOrCreateHazelcastInstance(this.config);
/*    */     }
/* 82 */     return Hazelcast.newHazelcastInstance(this.config);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\hazelcast\HazelcastInstanceFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */