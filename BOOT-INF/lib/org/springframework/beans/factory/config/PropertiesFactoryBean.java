/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Properties;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.core.io.support.PropertiesLoaderSupport;
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
/*     */ public class PropertiesFactoryBean
/*     */   extends PropertiesLoaderSupport
/*     */   implements FactoryBean<Properties>, InitializingBean
/*     */ {
/*  48 */   private boolean singleton = true;
/*     */   
/*     */ 
/*     */ 
/*     */   private Properties singletonInstance;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setSingleton(boolean singleton)
/*     */   {
/*  59 */     this.singleton = singleton;
/*     */   }
/*     */   
/*     */   public final boolean isSingleton()
/*     */   {
/*  64 */     return this.singleton;
/*     */   }
/*     */   
/*     */   public final void afterPropertiesSet()
/*     */     throws IOException
/*     */   {
/*  70 */     if (this.singleton) {
/*  71 */       this.singletonInstance = createProperties();
/*     */     }
/*     */   }
/*     */   
/*     */   public final Properties getObject() throws IOException
/*     */   {
/*  77 */     if (this.singleton) {
/*  78 */       return this.singletonInstance;
/*     */     }
/*     */     
/*  81 */     return createProperties();
/*     */   }
/*     */   
/*     */ 
/*     */   public Class<Properties> getObjectType()
/*     */   {
/*  87 */     return Properties.class;
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
/*     */     throws IOException
/*     */   {
/* 102 */     return mergeProperties();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\config\PropertiesFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */