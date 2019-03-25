/*     */ package org.springframework.boot.autoconfigure.jdbc;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.sql.DataSource;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.boot.bind.RelaxedDataBinder;
/*     */ import org.springframework.boot.jdbc.DatabaseDriver;
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
/*     */ public class DataSourceBuilder
/*     */ {
/*  44 */   private static final String[] DATA_SOURCE_TYPE_NAMES = { "org.apache.tomcat.jdbc.pool.DataSource", "com.zaxxer.hikari.HikariDataSource", "org.apache.commons.dbcp.BasicDataSource", "org.apache.commons.dbcp2.BasicDataSource" };
/*     */   
/*     */ 
/*     */ 
/*     */   private Class<? extends DataSource> type;
/*     */   
/*     */ 
/*     */   private ClassLoader classLoader;
/*     */   
/*     */ 
/*  54 */   private Map<String, String> properties = new HashMap();
/*     */   
/*     */   public static DataSourceBuilder create() {
/*  57 */     return new DataSourceBuilder(null);
/*     */   }
/*     */   
/*     */   public static DataSourceBuilder create(ClassLoader classLoader) {
/*  61 */     return new DataSourceBuilder(classLoader);
/*     */   }
/*     */   
/*     */   public DataSourceBuilder(ClassLoader classLoader) {
/*  65 */     this.classLoader = classLoader;
/*     */   }
/*     */   
/*     */   public DataSource build() {
/*  69 */     Class<? extends DataSource> type = getType();
/*  70 */     DataSource result = (DataSource)BeanUtils.instantiate(type);
/*  71 */     maybeGetDriverClassName();
/*  72 */     bind(result);
/*  73 */     return result;
/*     */   }
/*     */   
/*     */   private void maybeGetDriverClassName() {
/*  77 */     if ((!this.properties.containsKey("driverClassName")) && 
/*  78 */       (this.properties.containsKey("url"))) {
/*  79 */       String url = (String)this.properties.get("url");
/*  80 */       String driverClass = DatabaseDriver.fromJdbcUrl(url).getDriverClassName();
/*  81 */       this.properties.put("driverClassName", driverClass);
/*     */     }
/*     */   }
/*     */   
/*     */   private void bind(DataSource result) {
/*  86 */     MutablePropertyValues properties = new MutablePropertyValues(this.properties);
/*  87 */     new RelaxedDataBinder(result).withAlias("url", new String[] { "jdbcUrl" })
/*  88 */       .withAlias("username", new String[] { "user" }).bind(properties);
/*     */   }
/*     */   
/*     */   public DataSourceBuilder type(Class<? extends DataSource> type) {
/*  92 */     this.type = type;
/*  93 */     return this;
/*     */   }
/*     */   
/*     */   public DataSourceBuilder url(String url) {
/*  97 */     this.properties.put("url", url);
/*  98 */     return this;
/*     */   }
/*     */   
/*     */   public DataSourceBuilder driverClassName(String driverClassName) {
/* 102 */     this.properties.put("driverClassName", driverClassName);
/* 103 */     return this;
/*     */   }
/*     */   
/*     */   public DataSourceBuilder username(String username) {
/* 107 */     this.properties.put("username", username);
/* 108 */     return this;
/*     */   }
/*     */   
/*     */   public DataSourceBuilder password(String password) {
/* 112 */     this.properties.put("password", password);
/* 113 */     return this;
/*     */   }
/*     */   
/*     */   public Class<? extends DataSource> findType()
/*     */   {
/* 118 */     if (this.type != null) {
/* 119 */       return this.type;
/*     */     }
/* 121 */     for (String name : DATA_SOURCE_TYPE_NAMES) {
/*     */       try {
/* 123 */         return ClassUtils.forName(name, this.classLoader);
/*     */       }
/*     */       catch (Exception localException) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 130 */     return null;
/*     */   }
/*     */   
/*     */   private Class<? extends DataSource> getType() {
/* 134 */     Class<? extends DataSource> type = findType();
/* 135 */     if (type != null) {
/* 136 */       return type;
/*     */     }
/* 138 */     throw new IllegalStateException("No supported DataSource type found");
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jdbc\DataSourceBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */