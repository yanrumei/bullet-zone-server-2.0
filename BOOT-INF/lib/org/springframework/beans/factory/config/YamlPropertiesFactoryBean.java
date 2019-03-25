/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.core.CollectionFactory;
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
/*     */ public class YamlPropertiesFactoryBean
/*     */   extends YamlProcessor
/*     */   implements FactoryBean<Properties>, InitializingBean
/*     */ {
/*  84 */   private boolean singleton = true;
/*     */   
/*     */ 
/*     */ 
/*     */   private Properties properties;
/*     */   
/*     */ 
/*     */ 
/*     */   public void setSingleton(boolean singleton)
/*     */   {
/*  94 */     this.singleton = singleton;
/*     */   }
/*     */   
/*     */   public boolean isSingleton()
/*     */   {
/*  99 */     return this.singleton;
/*     */   }
/*     */   
/*     */   public void afterPropertiesSet()
/*     */   {
/* 104 */     if (isSingleton()) {
/* 105 */       this.properties = createProperties();
/*     */     }
/*     */   }
/*     */   
/*     */   public Properties getObject()
/*     */   {
/* 111 */     return this.properties != null ? this.properties : createProperties();
/*     */   }
/*     */   
/*     */   public Class<?> getObjectType()
/*     */   {
/* 116 */     return Properties.class;
/*     */   }
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
/*     */   protected Properties createProperties()
/*     */   {
/* 130 */     final Properties result = CollectionFactory.createStringAdaptingProperties();
/* 131 */     process(new YamlProcessor.MatchCallback()
/*     */     {
/*     */       public void process(Properties properties, Map<String, Object> map) {
/* 134 */         result.putAll(properties);
/*     */       }
/* 136 */     });
/* 137 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\config\YamlPropertiesFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */