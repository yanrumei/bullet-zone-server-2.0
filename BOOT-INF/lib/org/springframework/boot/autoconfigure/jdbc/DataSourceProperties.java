/*     */ package org.springframework.boot.autoconfigure.jdbc;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import javax.sql.DataSource;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ import org.springframework.boot.jdbc.DatabaseDriver;
/*     */ import org.springframework.context.EnvironmentAware;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @ConfigurationProperties(prefix="spring.datasource")
/*     */ public class DataSourceProperties
/*     */   implements BeanClassLoaderAware, EnvironmentAware, InitializingBean
/*     */ {
/*     */   private ClassLoader classLoader;
/*     */   private Environment environment;
/*  60 */   private String name = "testdb";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean generateUniqueName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Class<? extends DataSource> type;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String driverClassName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String url;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String username;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String password;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String jndiName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 102 */   private boolean initialize = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 108 */   private String platform = "all";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<String> schema;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String schemaUsername;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String schemaPassword;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<String> data;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String dataUsername;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String dataPassword;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 143 */   private boolean continueOnError = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 148 */   private String separator = ";";
/*     */   
/*     */ 
/*     */ 
/*     */   private Charset sqlScriptEncoding;
/*     */   
/*     */ 
/* 155 */   private EmbeddedDatabaseConnection embeddedDatabaseConnection = EmbeddedDatabaseConnection.NONE;
/*     */   
/* 157 */   private Xa xa = new Xa();
/*     */   
/*     */   private String uniqueName;
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader)
/*     */   {
/* 163 */     this.classLoader = classLoader;
/*     */   }
/*     */   
/*     */   public void setEnvironment(Environment environment)
/*     */   {
/* 168 */     this.environment = environment;
/*     */   }
/*     */   
/*     */   public void afterPropertiesSet()
/*     */     throws Exception
/*     */   {
/* 174 */     this.embeddedDatabaseConnection = EmbeddedDatabaseConnection.get(this.classLoader);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DataSourceBuilder initializeDataSourceBuilder()
/*     */   {
/* 183 */     return 
/*     */     
/* 185 */       DataSourceBuilder.create(getClassLoader()).type(getType()).driverClassName(determineDriverClassName()).url(determineUrl()).username(determineUsername()).password(determinePassword());
/*     */   }
/*     */   
/*     */   public String getName() {
/* 189 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/* 193 */     this.name = name;
/*     */   }
/*     */   
/*     */   public boolean isGenerateUniqueName() {
/* 197 */     return this.generateUniqueName;
/*     */   }
/*     */   
/*     */   public void setGenerateUniqueName(boolean generateUniqueName) {
/* 201 */     this.generateUniqueName = generateUniqueName;
/*     */   }
/*     */   
/*     */   public Class<? extends DataSource> getType() {
/* 205 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(Class<? extends DataSource> type) {
/* 209 */     this.type = type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDriverClassName()
/*     */   {
/* 218 */     return this.driverClassName;
/*     */   }
/*     */   
/*     */   public void setDriverClassName(String driverClassName) {
/* 222 */     this.driverClassName = driverClassName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String determineDriverClassName()
/*     */   {
/* 231 */     if (StringUtils.hasText(this.driverClassName)) {
/* 232 */       Assert.state(driverClassIsLoadable(), "Cannot load driver class: " + this.driverClassName);
/*     */       
/* 234 */       return this.driverClassName;
/*     */     }
/* 236 */     String driverClassName = null;
/*     */     
/* 238 */     if (StringUtils.hasText(this.url)) {
/* 239 */       driverClassName = DatabaseDriver.fromJdbcUrl(this.url).getDriverClassName();
/*     */     }
/*     */     
/* 242 */     if (!StringUtils.hasText(driverClassName)) {
/* 243 */       driverClassName = this.embeddedDatabaseConnection.getDriverClassName();
/*     */     }
/*     */     
/* 246 */     if (!StringUtils.hasText(driverClassName)) {
/* 247 */       throw new DataSourceBeanCreationException(this.embeddedDatabaseConnection, this.environment, "driver class");
/*     */     }
/*     */     
/* 250 */     return driverClassName;
/*     */   }
/*     */   
/*     */   private boolean driverClassIsLoadable() {
/*     */     try {
/* 255 */       ClassUtils.forName(this.driverClassName, null);
/* 256 */       return true;
/*     */     }
/*     */     catch (UnsupportedClassVersionError ex)
/*     */     {
/* 260 */       throw ex;
/*     */     }
/*     */     catch (Throwable ex) {}
/* 263 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUrl()
/*     */   {
/* 273 */     return this.url;
/*     */   }
/*     */   
/*     */   public void setUrl(String url) {
/* 277 */     this.url = url;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String determineUrl()
/*     */   {
/* 286 */     if (StringUtils.hasText(this.url)) {
/* 287 */       return this.url;
/*     */     }
/* 289 */     String url = this.embeddedDatabaseConnection.getUrl(determineDatabaseName());
/* 290 */     if (!StringUtils.hasText(url)) {
/* 291 */       throw new DataSourceBeanCreationException(this.embeddedDatabaseConnection, this.environment, "url");
/*     */     }
/*     */     
/* 294 */     return url;
/*     */   }
/*     */   
/*     */   private String determineDatabaseName() {
/* 298 */     if (this.generateUniqueName) {
/* 299 */       if (this.uniqueName == null) {
/* 300 */         this.uniqueName = UUID.randomUUID().toString();
/*     */       }
/* 302 */       return this.uniqueName;
/*     */     }
/* 304 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUsername()
/*     */   {
/* 313 */     return this.username;
/*     */   }
/*     */   
/*     */   public void setUsername(String username) {
/* 317 */     this.username = username;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String determineUsername()
/*     */   {
/* 326 */     if (StringUtils.hasText(this.username)) {
/* 327 */       return this.username;
/*     */     }
/* 329 */     if (EmbeddedDatabaseConnection.isEmbedded(determineDriverClassName())) {
/* 330 */       return "sa";
/*     */     }
/* 332 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPassword()
/*     */   {
/* 341 */     return this.password;
/*     */   }
/*     */   
/*     */   public void setPassword(String password) {
/* 345 */     this.password = password;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String determinePassword()
/*     */   {
/* 354 */     if (StringUtils.hasText(this.password)) {
/* 355 */       return this.password;
/*     */     }
/* 357 */     if (EmbeddedDatabaseConnection.isEmbedded(determineDriverClassName())) {
/* 358 */       return "";
/*     */     }
/* 360 */     return null;
/*     */   }
/*     */   
/*     */   public String getJndiName() {
/* 364 */     return this.jndiName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setJndiName(String jndiName)
/*     */   {
/* 374 */     this.jndiName = jndiName;
/*     */   }
/*     */   
/*     */   public boolean isInitialize() {
/* 378 */     return this.initialize;
/*     */   }
/*     */   
/*     */   public void setInitialize(boolean initialize) {
/* 382 */     this.initialize = initialize;
/*     */   }
/*     */   
/*     */   public String getPlatform() {
/* 386 */     return this.platform;
/*     */   }
/*     */   
/*     */   public void setPlatform(String platform) {
/* 390 */     this.platform = platform;
/*     */   }
/*     */   
/*     */   public List<String> getSchema() {
/* 394 */     return this.schema;
/*     */   }
/*     */   
/*     */   public void setSchema(List<String> schema) {
/* 398 */     this.schema = schema;
/*     */   }
/*     */   
/*     */   public String getSchemaUsername() {
/* 402 */     return this.schemaUsername;
/*     */   }
/*     */   
/*     */   public void setSchemaUsername(String schemaUsername) {
/* 406 */     this.schemaUsername = schemaUsername;
/*     */   }
/*     */   
/*     */   public String getSchemaPassword() {
/* 410 */     return this.schemaPassword;
/*     */   }
/*     */   
/*     */   public void setSchemaPassword(String schemaPassword) {
/* 414 */     this.schemaPassword = schemaPassword;
/*     */   }
/*     */   
/*     */   public List<String> getData() {
/* 418 */     return this.data;
/*     */   }
/*     */   
/*     */   public void setData(List<String> data) {
/* 422 */     this.data = data;
/*     */   }
/*     */   
/*     */   public String getDataUsername() {
/* 426 */     return this.dataUsername;
/*     */   }
/*     */   
/*     */   public void setDataUsername(String dataUsername) {
/* 430 */     this.dataUsername = dataUsername;
/*     */   }
/*     */   
/*     */   public String getDataPassword() {
/* 434 */     return this.dataPassword;
/*     */   }
/*     */   
/*     */   public void setDataPassword(String dataPassword) {
/* 438 */     this.dataPassword = dataPassword;
/*     */   }
/*     */   
/*     */   public boolean isContinueOnError() {
/* 442 */     return this.continueOnError;
/*     */   }
/*     */   
/*     */   public void setContinueOnError(boolean continueOnError) {
/* 446 */     this.continueOnError = continueOnError;
/*     */   }
/*     */   
/*     */   public String getSeparator() {
/* 450 */     return this.separator;
/*     */   }
/*     */   
/*     */   public void setSeparator(String separator) {
/* 454 */     this.separator = separator;
/*     */   }
/*     */   
/*     */   public Charset getSqlScriptEncoding() {
/* 458 */     return this.sqlScriptEncoding;
/*     */   }
/*     */   
/*     */   public void setSqlScriptEncoding(Charset sqlScriptEncoding) {
/* 462 */     this.sqlScriptEncoding = sqlScriptEncoding;
/*     */   }
/*     */   
/*     */   public ClassLoader getClassLoader() {
/* 466 */     return this.classLoader;
/*     */   }
/*     */   
/*     */   public Xa getXa() {
/* 470 */     return this.xa;
/*     */   }
/*     */   
/*     */   public void setXa(Xa xa) {
/* 474 */     this.xa = xa;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Xa
/*     */   {
/*     */     private String dataSourceClassName;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 490 */     private Map<String, String> properties = new LinkedHashMap();
/*     */     
/*     */     public String getDataSourceClassName() {
/* 493 */       return this.dataSourceClassName;
/*     */     }
/*     */     
/*     */     public void setDataSourceClassName(String dataSourceClassName) {
/* 497 */       this.dataSourceClassName = dataSourceClassName;
/*     */     }
/*     */     
/*     */     public Map<String, String> getProperties() {
/* 501 */       return this.properties;
/*     */     }
/*     */     
/*     */     public void setProperties(Map<String, String> properties) {
/* 505 */       this.properties = properties;
/*     */     }
/*     */   }
/*     */   
/*     */   static class DataSourceBeanCreationException
/*     */     extends BeanCreationException
/*     */   {
/*     */     DataSourceBeanCreationException(EmbeddedDatabaseConnection connection, Environment environment, String property)
/*     */     {
/* 514 */       super();
/*     */     }
/*     */     
/*     */     private static String getMessage(EmbeddedDatabaseConnection connection, Environment environment, String property)
/*     */     {
/* 519 */       StringBuilder message = new StringBuilder();
/* 520 */       message.append("Cannot determine embedded database " + property + " for database type " + connection + ". ");
/*     */       
/* 522 */       message.append("If you want an embedded database please put a supported one on the classpath. ");
/*     */       
/* 524 */       message.append("If you have database settings to be loaded from a particular profile you may need to active it");
/*     */       
/* 526 */       if (environment != null) {
/* 527 */         String[] profiles = environment.getActiveProfiles();
/* 528 */         if (ObjectUtils.isEmpty(profiles)) {
/* 529 */           message.append(" (no profiles are currently active)");
/*     */         }
/*     */         else {
/* 532 */           message.append(" (the profiles \"" + 
/* 533 */             StringUtils.arrayToCommaDelimitedString(environment
/* 534 */             .getActiveProfiles()) + "\" are currently active)");
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 539 */       message.append(".");
/* 540 */       return message.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jdbc\DataSourceProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */