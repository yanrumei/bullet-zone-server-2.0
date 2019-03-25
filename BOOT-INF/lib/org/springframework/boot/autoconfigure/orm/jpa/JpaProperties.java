/*     */ package org.springframework.boot.autoconfigure.orm.jpa;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.sql.DataSource;
/*     */ import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ import org.springframework.boot.context.properties.NestedConfigurationProperty;
/*     */ import org.springframework.orm.jpa.vendor.Database;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ @ConfigurationProperties(prefix="spring.jpa")
/*     */ public class JpaProperties
/*     */ {
/*  45 */   private Map<String, String> properties = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String databasePlatform;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Database database;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  62 */   private boolean generateDdl = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  67 */   private boolean showSql = false;
/*     */   
/*  69 */   private Hibernate hibernate = new Hibernate();
/*     */   
/*     */   public Map<String, String> getProperties() {
/*  72 */     return this.properties;
/*     */   }
/*     */   
/*     */   public void setProperties(Map<String, String> properties) {
/*  76 */     this.properties = properties;
/*     */   }
/*     */   
/*     */   public String getDatabasePlatform() {
/*  80 */     return this.databasePlatform;
/*     */   }
/*     */   
/*     */   public void setDatabasePlatform(String databasePlatform) {
/*  84 */     this.databasePlatform = databasePlatform;
/*     */   }
/*     */   
/*     */   public Database getDatabase() {
/*  88 */     return this.database;
/*     */   }
/*     */   
/*     */   public void setDatabase(Database database) {
/*  92 */     this.database = database;
/*     */   }
/*     */   
/*     */   public boolean isGenerateDdl() {
/*  96 */     return this.generateDdl;
/*     */   }
/*     */   
/*     */   public void setGenerateDdl(boolean generateDdl) {
/* 100 */     this.generateDdl = generateDdl;
/*     */   }
/*     */   
/*     */   public boolean isShowSql() {
/* 104 */     return this.showSql;
/*     */   }
/*     */   
/*     */   public void setShowSql(boolean showSql) {
/* 108 */     this.showSql = showSql;
/*     */   }
/*     */   
/*     */   public Hibernate getHibernate() {
/* 112 */     return this.hibernate;
/*     */   }
/*     */   
/*     */   public void setHibernate(Hibernate hibernate) {
/* 116 */     this.hibernate = hibernate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, String> getHibernateProperties(DataSource dataSource)
/*     */   {
/* 126 */     return this.hibernate.getAdditionalProperties(this.properties, dataSource);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Database determineDatabase(DataSource dataSource)
/*     */   {
/* 136 */     if (this.database != null) {
/* 137 */       return this.database;
/*     */     }
/* 139 */     return DatabaseLookup.getDatabase(dataSource);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Hibernate
/*     */   {
/*     */     private static final String USE_NEW_ID_GENERATOR_MAPPINGS = "hibernate.id.new_generator_mappings";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private String ddlAuto;
/*     */     
/*     */ 
/*     */ 
/*     */     private Boolean useNewIdGeneratorMappings;
/*     */     
/*     */ 
/*     */ 
/*     */     @NestedConfigurationProperty
/* 162 */     private final JpaProperties.Naming naming = new JpaProperties.Naming();
/*     */     
/*     */     public String getDdlAuto()
/*     */     {
/* 166 */       return this.ddlAuto;
/*     */     }
/*     */     
/*     */     public void setDdlAuto(String ddlAuto) {
/* 170 */       this.ddlAuto = ddlAuto;
/*     */     }
/*     */     
/*     */     public boolean isUseNewIdGeneratorMappings() {
/* 174 */       return this.useNewIdGeneratorMappings.booleanValue();
/*     */     }
/*     */     
/*     */     public void setUseNewIdGeneratorMappings(boolean useNewIdGeneratorMappings) {
/* 178 */       this.useNewIdGeneratorMappings = Boolean.valueOf(useNewIdGeneratorMappings);
/*     */     }
/*     */     
/*     */     public JpaProperties.Naming getNaming() {
/* 182 */       return this.naming;
/*     */     }
/*     */     
/*     */     private Map<String, String> getAdditionalProperties(Map<String, String> existing, DataSource dataSource)
/*     */     {
/* 187 */       Map<String, String> result = new HashMap(existing);
/* 188 */       applyNewIdGeneratorMappings(result);
/* 189 */       getNaming().applyNamingStrategy(result);
/* 190 */       String ddlAuto = getOrDeduceDdlAuto(existing, dataSource);
/* 191 */       if ((StringUtils.hasText(ddlAuto)) && (!"none".equals(ddlAuto))) {
/* 192 */         result.put("hibernate.hbm2ddl.auto", ddlAuto);
/*     */       }
/*     */       else {
/* 195 */         result.remove("hibernate.hbm2ddl.auto");
/*     */       }
/* 197 */       return result;
/*     */     }
/*     */     
/*     */     private void applyNewIdGeneratorMappings(Map<String, String> result) {
/* 201 */       if (this.useNewIdGeneratorMappings != null) {
/* 202 */         result.put("hibernate.id.new_generator_mappings", this.useNewIdGeneratorMappings
/* 203 */           .toString());
/*     */       }
/* 205 */       else if ((HibernateVersion.getRunning() == HibernateVersion.V5) && 
/* 206 */         (!result.containsKey("hibernate.id.new_generator_mappings"))) {
/* 207 */         result.put("hibernate.id.new_generator_mappings", "false");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     private String getOrDeduceDdlAuto(Map<String, String> existing, DataSource dataSource)
/*     */     {
/* 214 */       String ddlAuto = this.ddlAuto != null ? this.ddlAuto : getDefaultDdlAuto(dataSource);
/* 215 */       if ((!existing.containsKey("hibernate.hbm2ddl.auto")) && 
/* 216 */         (!"none".equals(ddlAuto))) {
/* 217 */         return ddlAuto;
/*     */       }
/* 219 */       if (existing.containsKey("hibernate.hbm2ddl.auto")) {
/* 220 */         return (String)existing.get("hibernate.hbm2ddl.auto");
/*     */       }
/* 222 */       return "none";
/*     */     }
/*     */     
/*     */     private String getDefaultDdlAuto(DataSource dataSource) {
/* 226 */       if (EmbeddedDatabaseConnection.isEmbedded(dataSource)) {
/* 227 */         return "create-drop";
/*     */       }
/* 229 */       return "none";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Naming
/*     */   {
/*     */     private static final String DEFAULT_HIBERNATE4_STRATEGY = "org.springframework.boot.orm.jpa.hibernate.SpringNamingStrategy";
/*     */     
/*     */ 
/*     */     private static final String DEFAULT_PHYSICAL_STRATEGY = "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy";
/*     */     
/*     */ 
/*     */     private static final String DEFAULT_IMPLICIT_STRATEGY = "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy";
/*     */     
/*     */ 
/*     */     private String implicitStrategy;
/*     */     
/*     */ 
/*     */     private String physicalStrategy;
/*     */     
/*     */ 
/*     */     private String strategy;
/*     */     
/*     */ 
/*     */ 
/*     */     public String getImplicitStrategy()
/*     */     {
/* 259 */       return this.implicitStrategy;
/*     */     }
/*     */     
/*     */     public void setImplicitStrategy(String implicitStrategy) {
/* 263 */       this.implicitStrategy = implicitStrategy;
/*     */     }
/*     */     
/*     */     public String getPhysicalStrategy() {
/* 267 */       return this.physicalStrategy;
/*     */     }
/*     */     
/*     */     public void setPhysicalStrategy(String physicalStrategy) {
/* 271 */       this.physicalStrategy = physicalStrategy;
/*     */     }
/*     */     
/*     */     public String getStrategy() {
/* 275 */       return this.strategy;
/*     */     }
/*     */     
/*     */     public void setStrategy(String strategy) {
/* 279 */       this.strategy = strategy;
/*     */     }
/*     */     
/*     */     private void applyNamingStrategy(Map<String, String> properties) {
/* 283 */       switch (JpaProperties.1.$SwitchMap$org$springframework$boot$autoconfigure$orm$jpa$HibernateVersion[HibernateVersion.getRunning().ordinal()]) {
/*     */       case 1: 
/* 285 */         applyHibernate4NamingStrategy(properties);
/* 286 */         break;
/*     */       case 2: 
/* 288 */         applyHibernate5NamingStrategy(properties);
/*     */       }
/*     */     }
/*     */     
/*     */     private void applyHibernate5NamingStrategy(Map<String, String> properties)
/*     */     {
/* 294 */       applyHibernate5NamingStrategy(properties, "hibernate.implicit_naming_strategy", this.implicitStrategy, "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy");
/*     */       
/*     */ 
/* 297 */       applyHibernate5NamingStrategy(properties, "hibernate.physical_naming_strategy", this.physicalStrategy, "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void applyHibernate5NamingStrategy(Map<String, String> properties, String key, String strategy, String defaultStrategy)
/*     */     {
/* 304 */       if (strategy != null) {
/* 305 */         properties.put(key, strategy);
/*     */       }
/* 307 */       else if ((defaultStrategy != null) && (!properties.containsKey(key))) {
/* 308 */         properties.put(key, defaultStrategy);
/*     */       }
/*     */     }
/*     */     
/*     */     private void applyHibernate4NamingStrategy(Map<String, String> properties) {
/* 313 */       if (!properties.containsKey("hibernate.ejb.naming_strategy_delegator")) {
/* 314 */         properties.put("hibernate.ejb.naming_strategy", 
/* 315 */           getHibernate4NamingStrategy(properties));
/*     */       }
/*     */     }
/*     */     
/*     */     private String getHibernate4NamingStrategy(Map<String, String> existing) {
/* 320 */       if ((!existing.containsKey("hibernate.ejb.naming_strategy")) && (this.strategy != null))
/*     */       {
/* 322 */         return this.strategy;
/*     */       }
/* 324 */       return "org.springframework.boot.orm.jpa.hibernate.SpringNamingStrategy";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\orm\jpa\JpaProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */