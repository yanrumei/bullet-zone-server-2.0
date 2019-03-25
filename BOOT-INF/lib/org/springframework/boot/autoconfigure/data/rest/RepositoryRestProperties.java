/*     */ package org.springframework.boot.autoconfigure.data.rest;
/*     */ 
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
/*     */ import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy.RepositoryDetectionStrategies;
/*     */ import org.springframework.http.MediaType;
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
/*     */ @ConfigurationProperties(prefix="spring.data.rest")
/*     */ public class RepositoryRestProperties
/*     */ {
/*     */   private String basePath;
/*     */   private Integer defaultPageSize;
/*     */   private Integer maxPageSize;
/*     */   private String pageParamName;
/*     */   private String limitParamName;
/*     */   private String sortParamName;
/*  68 */   private RepositoryDetectionStrategy.RepositoryDetectionStrategies detectionStrategy = RepositoryDetectionStrategy.RepositoryDetectionStrategies.DEFAULT;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private MediaType defaultMediaType;
/*     */   
/*     */ 
/*     */ 
/*     */   private Boolean returnBodyOnCreate;
/*     */   
/*     */ 
/*     */ 
/*     */   private Boolean returnBodyOnUpdate;
/*     */   
/*     */ 
/*     */ 
/*     */   private Boolean enableEnumTranslation;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getBasePath()
/*     */   {
/*  92 */     return this.basePath;
/*     */   }
/*     */   
/*     */   public void setBasePath(String basePath) {
/*  96 */     this.basePath = basePath;
/*     */   }
/*     */   
/*     */   public Integer getDefaultPageSize() {
/* 100 */     return this.defaultPageSize;
/*     */   }
/*     */   
/*     */   public void setDefaultPageSize(Integer defaultPageSize) {
/* 104 */     this.defaultPageSize = defaultPageSize;
/*     */   }
/*     */   
/*     */   public Integer getMaxPageSize() {
/* 108 */     return this.maxPageSize;
/*     */   }
/*     */   
/*     */   public void setMaxPageSize(Integer maxPageSize) {
/* 112 */     this.maxPageSize = maxPageSize;
/*     */   }
/*     */   
/*     */   public String getPageParamName() {
/* 116 */     return this.pageParamName;
/*     */   }
/*     */   
/*     */   public void setPageParamName(String pageParamName) {
/* 120 */     this.pageParamName = pageParamName;
/*     */   }
/*     */   
/*     */   public String getLimitParamName() {
/* 124 */     return this.limitParamName;
/*     */   }
/*     */   
/*     */   public void setLimitParamName(String limitParamName) {
/* 128 */     this.limitParamName = limitParamName;
/*     */   }
/*     */   
/*     */   public String getSortParamName() {
/* 132 */     return this.sortParamName;
/*     */   }
/*     */   
/*     */   public void setSortParamName(String sortParamName) {
/* 136 */     this.sortParamName = sortParamName;
/*     */   }
/*     */   
/*     */   public RepositoryDetectionStrategy.RepositoryDetectionStrategies getDetectionStrategy() {
/* 140 */     return this.detectionStrategy;
/*     */   }
/*     */   
/*     */   public void setDetectionStrategy(RepositoryDetectionStrategy.RepositoryDetectionStrategies detectionStrategy) {
/* 144 */     this.detectionStrategy = detectionStrategy;
/*     */   }
/*     */   
/*     */   public MediaType getDefaultMediaType() {
/* 148 */     return this.defaultMediaType;
/*     */   }
/*     */   
/*     */   public void setDefaultMediaType(MediaType defaultMediaType) {
/* 152 */     this.defaultMediaType = defaultMediaType;
/*     */   }
/*     */   
/*     */   public Boolean getReturnBodyOnCreate() {
/* 156 */     return this.returnBodyOnCreate;
/*     */   }
/*     */   
/*     */   public void setReturnBodyOnCreate(Boolean returnBodyOnCreate) {
/* 160 */     this.returnBodyOnCreate = returnBodyOnCreate;
/*     */   }
/*     */   
/*     */   public Boolean getReturnBodyOnUpdate() {
/* 164 */     return this.returnBodyOnUpdate;
/*     */   }
/*     */   
/*     */   public void setReturnBodyOnUpdate(Boolean returnBodyOnUpdate) {
/* 168 */     this.returnBodyOnUpdate = returnBodyOnUpdate;
/*     */   }
/*     */   
/*     */   public Boolean getEnableEnumTranslation() {
/* 172 */     return this.enableEnumTranslation;
/*     */   }
/*     */   
/*     */   public void setEnableEnumTranslation(Boolean enableEnumTranslation) {
/* 176 */     this.enableEnumTranslation = enableEnumTranslation;
/*     */   }
/*     */   
/*     */   public void applyTo(RepositoryRestConfiguration configuration) {
/* 180 */     if (this.basePath != null) {
/* 181 */       configuration.setBasePath(this.basePath);
/*     */     }
/* 183 */     if (this.defaultPageSize != null) {
/* 184 */       configuration.setDefaultPageSize(this.defaultPageSize.intValue());
/*     */     }
/* 186 */     if (this.maxPageSize != null) {
/* 187 */       configuration.setMaxPageSize(this.maxPageSize.intValue());
/*     */     }
/* 189 */     if (this.pageParamName != null) {
/* 190 */       configuration.setPageParamName(this.pageParamName);
/*     */     }
/* 192 */     if (this.limitParamName != null) {
/* 193 */       configuration.setLimitParamName(this.limitParamName);
/*     */     }
/* 195 */     if (this.sortParamName != null) {
/* 196 */       configuration.setSortParamName(this.sortParamName);
/*     */     }
/* 198 */     if (this.detectionStrategy != null) {
/* 199 */       configuration.setRepositoryDetectionStrategy(this.detectionStrategy);
/*     */     }
/* 201 */     if (this.defaultMediaType != null) {
/* 202 */       configuration.setDefaultMediaType(this.defaultMediaType);
/*     */     }
/* 204 */     if (this.returnBodyOnCreate != null) {
/* 205 */       configuration.setReturnBodyOnCreate(this.returnBodyOnCreate);
/*     */     }
/* 207 */     if (this.returnBodyOnUpdate != null) {
/* 208 */       configuration.setReturnBodyOnUpdate(this.returnBodyOnUpdate);
/*     */     }
/* 210 */     if (this.enableEnumTranslation != null) {
/* 211 */       configuration.setEnableEnumTranslation(this.enableEnumTranslation.booleanValue());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\rest\RepositoryRestProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */