/*     */ package org.springframework.boot.autoconfigure.mongo.embedded;
/*     */ 
/*     */ import de.flapdoodle.embed.mongo.distribution.Feature;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
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
/*     */ @ConfigurationProperties(prefix="spring.mongodb.embedded")
/*     */ public class EmbeddedMongoProperties
/*     */ {
/*     */   private String version;
/*     */   private final Storage storage;
/*     */   
/*     */   public EmbeddedMongoProperties()
/*     */   {
/*  40 */     this.version = "3.2.2";
/*     */     
/*  42 */     this.storage = new Storage();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  47 */   private Set<Feature> features = new HashSet(
/*  48 */     Collections.singletonList(Feature.SYNC_DELAY));
/*     */   
/*     */   public String getVersion() {
/*  51 */     return this.version;
/*     */   }
/*     */   
/*     */   public void setVersion(String version) {
/*  55 */     this.version = version;
/*     */   }
/*     */   
/*     */   public Set<Feature> getFeatures() {
/*  59 */     return this.features;
/*     */   }
/*     */   
/*     */   public void setFeatures(Set<Feature> features) {
/*  63 */     this.features = features;
/*     */   }
/*     */   
/*     */   public Storage getStorage() {
/*  67 */     return this.storage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Storage
/*     */   {
/*     */     private Integer oplogSize;
/*     */     
/*     */ 
/*     */ 
/*     */     private String replSetName;
/*     */     
/*     */ 
/*     */     private String databaseDir;
/*     */     
/*     */ 
/*     */ 
/*     */     public Integer getOplogSize()
/*     */     {
/*  88 */       return this.oplogSize;
/*     */     }
/*     */     
/*     */     public void setOplogSize(Integer oplogSize) {
/*  92 */       this.oplogSize = oplogSize;
/*     */     }
/*     */     
/*     */     public String getReplSetName() {
/*  96 */       return this.replSetName;
/*     */     }
/*     */     
/*     */     public void setReplSetName(String replSetName) {
/* 100 */       this.replSetName = replSetName;
/*     */     }
/*     */     
/*     */     public String getDatabaseDir() {
/* 104 */       return this.databaseDir;
/*     */     }
/*     */     
/*     */     public void setDatabaseDir(String databaseDir) {
/* 108 */       this.databaseDir = databaseDir;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\mongo\embedded\EmbeddedMongoProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */