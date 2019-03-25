/*     */ package org.springframework.boot.orm.jpa;
/*     */ 
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.sql.DataSource;
/*     */ import org.springframework.orm.jpa.JpaVendorAdapter;
/*     */ import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
/*     */ import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class EntityManagerFactoryBuilder
/*     */ {
/*     */   private final JpaVendorAdapter jpaVendorAdapter;
/*     */   private final PersistenceUnitManager persistenceUnitManager;
/*     */   private final Map<String, Object> jpaProperties;
/*     */   private final URL persistenceUnitRootLocation;
/*     */   private EntityManagerFactoryBeanCallback callback;
/*     */   
/*     */   public EntityManagerFactoryBuilder(JpaVendorAdapter jpaVendorAdapter, Map<String, ?> jpaProperties, PersistenceUnitManager persistenceUnitManager)
/*     */   {
/*  68 */     this(jpaVendorAdapter, jpaProperties, persistenceUnitManager, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public EntityManagerFactoryBuilder(JpaVendorAdapter jpaVendorAdapter, Map<String, ?> jpaProperties, PersistenceUnitManager persistenceUnitManager, URL persistenceUnitRootLocation)
/*     */   {
/*  85 */     this.jpaVendorAdapter = jpaVendorAdapter;
/*  86 */     this.persistenceUnitManager = persistenceUnitManager;
/*  87 */     this.jpaProperties = new LinkedHashMap(jpaProperties);
/*  88 */     this.persistenceUnitRootLocation = persistenceUnitRootLocation;
/*     */   }
/*     */   
/*     */   public Builder dataSource(DataSource dataSource) {
/*  92 */     return new Builder(dataSource, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCallback(EntityManagerFactoryBeanCallback callback)
/*     */   {
/* 100 */     this.callback = callback;
/*     */   }
/*     */   
/*     */ 
/*     */   public static abstract interface EntityManagerFactoryBeanCallback
/*     */   {
/*     */     public abstract void execute(LocalContainerEntityManagerFactoryBean paramLocalContainerEntityManagerFactoryBean);
/*     */   }
/*     */   
/*     */   public final class Builder
/*     */   {
/*     */     private DataSource dataSource;
/*     */     private String[] packagesToScan;
/*     */     private String persistenceUnit;
/* 114 */     private Map<String, Object> properties = new HashMap();
/*     */     private boolean jta;
/*     */     
/*     */     private Builder(DataSource dataSource)
/*     */     {
/* 119 */       this.dataSource = dataSource;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder packages(String... packagesToScan)
/*     */     {
/* 128 */       this.packagesToScan = packagesToScan;
/* 129 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder packages(Class<?>... basePackageClasses)
/*     */     {
/* 138 */       Set<String> packages = new HashSet();
/* 139 */       for (Class<?> type : basePackageClasses) {
/* 140 */         packages.add(ClassUtils.getPackageName(type));
/*     */       }
/* 142 */       this.packagesToScan = ((String[])packages.toArray(new String[0]));
/* 143 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder persistenceUnit(String persistenceUnit)
/*     */     {
/* 154 */       this.persistenceUnit = persistenceUnit;
/* 155 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder properties(Map<String, ?> properties)
/*     */     {
/* 165 */       this.properties.putAll(properties);
/* 166 */       return this;
/*     */     }
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
/*     */     public Builder jta(boolean jta)
/*     */     {
/* 180 */       this.jta = jta;
/* 181 */       return this;
/*     */     }
/*     */     
/*     */     public LocalContainerEntityManagerFactoryBean build() {
/* 185 */       LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
/* 186 */       if (EntityManagerFactoryBuilder.this.persistenceUnitManager != null) {
/* 187 */         entityManagerFactoryBean.setPersistenceUnitManager(
/* 188 */           EntityManagerFactoryBuilder.this.persistenceUnitManager);
/*     */       }
/* 190 */       if (this.persistenceUnit != null) {
/* 191 */         entityManagerFactoryBean.setPersistenceUnitName(this.persistenceUnit);
/*     */       }
/* 193 */       entityManagerFactoryBean.setJpaVendorAdapter(
/* 194 */         EntityManagerFactoryBuilder.this.jpaVendorAdapter);
/*     */       
/* 196 */       if (this.jta) {
/* 197 */         entityManagerFactoryBean.setJtaDataSource(this.dataSource);
/*     */       }
/*     */       else {
/* 200 */         entityManagerFactoryBean.setDataSource(this.dataSource);
/*     */       }
/* 202 */       entityManagerFactoryBean.setPackagesToScan(this.packagesToScan);
/* 203 */       entityManagerFactoryBean.getJpaPropertyMap()
/* 204 */         .putAll(EntityManagerFactoryBuilder.this.jpaProperties);
/* 205 */       entityManagerFactoryBean.getJpaPropertyMap().putAll(this.properties);
/* 206 */       URL rootLocation = EntityManagerFactoryBuilder.this.persistenceUnitRootLocation;
/* 207 */       if (rootLocation != null)
/*     */       {
/* 209 */         entityManagerFactoryBean.setPersistenceUnitRootLocation(rootLocation.toString());
/*     */       }
/* 211 */       if (EntityManagerFactoryBuilder.this.callback != null)
/*     */       {
/* 213 */         EntityManagerFactoryBuilder.this.callback.execute(entityManagerFactoryBean);
/*     */       }
/* 215 */       return entityManagerFactoryBean;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\orm\jpa\EntityManagerFactoryBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */