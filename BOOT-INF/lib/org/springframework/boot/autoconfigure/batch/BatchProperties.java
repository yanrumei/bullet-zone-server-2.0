/*     */ package org.springframework.boot.autoconfigure.batch;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ConfigurationProperties(prefix="spring.batch")
/*     */ public class BatchProperties
/*     */ {
/*     */   private static final String DEFAULT_SCHEMA_LOCATION = "classpath:org/springframework/batch/core/schema-@@platform@@.sql";
/*  38 */   private String schema = "classpath:org/springframework/batch/core/schema-@@platform@@.sql";
/*     */   
/*     */ 
/*     */ 
/*     */   private String tablePrefix;
/*     */   
/*     */ 
/*  45 */   private final Initializer initializer = new Initializer();
/*     */   
/*  47 */   private final Job job = new Job();
/*     */   
/*     */   public String getSchema() {
/*  50 */     return this.schema;
/*     */   }
/*     */   
/*     */   public void setSchema(String schema) {
/*  54 */     this.schema = schema;
/*     */   }
/*     */   
/*     */   public String getTablePrefix() {
/*  58 */     return this.tablePrefix;
/*     */   }
/*     */   
/*     */   public void setTablePrefix(String tablePrefix) {
/*  62 */     this.tablePrefix = tablePrefix;
/*     */   }
/*     */   
/*     */   public Initializer getInitializer() {
/*  66 */     return this.initializer;
/*     */   }
/*     */   
/*     */   public Job getJob() {
/*  70 */     return this.job;
/*     */   }
/*     */   
/*     */ 
/*     */   public class Initializer
/*     */   {
/*     */     private Boolean enabled;
/*     */     
/*     */     public Initializer() {}
/*     */     
/*     */     public boolean isEnabled()
/*     */     {
/*  82 */       if (this.enabled != null) {
/*  83 */         return this.enabled.booleanValue();
/*     */       }
/*  85 */       boolean defaultTablePrefix = BatchProperties.this.getTablePrefix() == null;
/*     */       
/*  87 */       boolean customSchema = !"classpath:org/springframework/batch/core/schema-@@platform@@.sql".equals(BatchProperties.this.getSchema());
/*  88 */       return (defaultTablePrefix) || (customSchema);
/*     */     }
/*     */     
/*     */     public void setEnabled(boolean enabled) {
/*  92 */       this.enabled = Boolean.valueOf(enabled);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Job
/*     */   {
/* 103 */     private String names = "";
/*     */     
/*     */     public String getNames() {
/* 106 */       return this.names;
/*     */     }
/*     */     
/*     */     public void setNames(String names) {
/* 110 */       this.names = names;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\batch\BatchProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */