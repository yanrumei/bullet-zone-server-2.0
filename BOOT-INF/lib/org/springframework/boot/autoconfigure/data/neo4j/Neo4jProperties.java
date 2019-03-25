/*     */ package org.springframework.boot.autoconfigure.data.neo4j;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import org.neo4j.ogm.config.CompilerConfiguration;
/*     */ import org.neo4j.ogm.config.Configuration;
/*     */ import org.neo4j.ogm.config.DriverConfiguration;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
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
/*     */ 
/*     */ @ConfigurationProperties(prefix="spring.data.neo4j")
/*     */ public class Neo4jProperties
/*     */   implements ApplicationContextAware
/*     */ {
/*     */   static final String EMBEDDED_DRIVER = "org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver";
/*     */   static final String HTTP_DRIVER = "org.neo4j.ogm.drivers.http.driver.HttpDriver";
/*     */   static final String DEFAULT_HTTP_URI = "http://localhost:7474";
/*     */   static final String BOLT_DRIVER = "org.neo4j.ogm.drivers.bolt.driver.BoltDriver";
/*     */   private String uri;
/*     */   private String username;
/*     */   private String password;
/*     */   private String compiler;
/*  70 */   private final Embedded embedded = new Embedded();
/*     */   
/*  72 */   private ClassLoader classLoader = Neo4jProperties.class.getClassLoader();
/*     */   
/*     */   public String getUri() {
/*  75 */     return this.uri;
/*     */   }
/*     */   
/*     */   public void setUri(String uri) {
/*  79 */     this.uri = uri;
/*     */   }
/*     */   
/*     */   public String getUsername() {
/*  83 */     return this.username;
/*     */   }
/*     */   
/*     */   public void setUsername(String username) {
/*  87 */     this.username = username;
/*     */   }
/*     */   
/*     */   public String getPassword() {
/*  91 */     return this.password;
/*     */   }
/*     */   
/*     */   public void setPassword(String password) {
/*  95 */     this.password = password;
/*     */   }
/*     */   
/*     */   public String getCompiler() {
/*  99 */     return this.compiler;
/*     */   }
/*     */   
/*     */   public void setCompiler(String compiler) {
/* 103 */     this.compiler = compiler;
/*     */   }
/*     */   
/*     */   public Embedded getEmbedded() {
/* 107 */     return this.embedded;
/*     */   }
/*     */   
/*     */   public void setApplicationContext(ApplicationContext ctx) throws BeansException
/*     */   {
/* 112 */     this.classLoader = ctx.getClassLoader();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Configuration createConfiguration()
/*     */   {
/* 120 */     Configuration configuration = new Configuration();
/* 121 */     configureDriver(configuration.driverConfiguration());
/* 122 */     if (this.compiler != null) {
/* 123 */       configuration.compilerConfiguration().setCompilerClassName(this.compiler);
/*     */     }
/* 125 */     return configuration;
/*     */   }
/*     */   
/*     */   private void configureDriver(DriverConfiguration driverConfiguration) {
/* 129 */     if (this.uri != null) {
/* 130 */       configureDriverFromUri(driverConfiguration, this.uri);
/*     */     }
/*     */     else {
/* 133 */       configureDriverWithDefaults(driverConfiguration);
/*     */     }
/* 135 */     if ((this.username != null) && (this.password != null)) {
/* 136 */       driverConfiguration.setCredentials(this.username, this.password);
/*     */     }
/*     */   }
/*     */   
/*     */   private void configureDriverFromUri(DriverConfiguration driverConfiguration, String uri)
/*     */   {
/* 142 */     driverConfiguration.setDriverClassName(deduceDriverFromUri());
/* 143 */     driverConfiguration.setURI(uri);
/*     */   }
/*     */   
/*     */   private String deduceDriverFromUri() {
/*     */     try {
/* 148 */       URI uri = new URI(this.uri);
/* 149 */       String scheme = uri.getScheme();
/* 150 */       if ((scheme == null) || (scheme.equals("file"))) {
/* 151 */         return "org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver";
/*     */       }
/* 153 */       if (("http".equals(scheme)) || ("https".equals(scheme))) {
/* 154 */         return "org.neo4j.ogm.drivers.http.driver.HttpDriver";
/*     */       }
/* 156 */       if ("bolt".equals(scheme)) {
/* 157 */         return "org.neo4j.ogm.drivers.bolt.driver.BoltDriver";
/*     */       }
/* 159 */       throw new IllegalArgumentException("Could not deduce driver to use based on URI '" + uri + "'");
/*     */     }
/*     */     catch (URISyntaxException ex)
/*     */     {
/* 163 */       throw new IllegalArgumentException("Invalid URI for spring.data.neo4j.uri '" + this.uri + "'", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private void configureDriverWithDefaults(DriverConfiguration driverConfiguration)
/*     */   {
/* 169 */     if ((getEmbedded().isEnabled()) && 
/* 170 */       (ClassUtils.isPresent("org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver", this.classLoader))) {
/* 171 */       driverConfiguration.setDriverClassName("org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver");
/* 172 */       return;
/*     */     }
/* 174 */     driverConfiguration.setDriverClassName("org.neo4j.ogm.drivers.http.driver.HttpDriver");
/* 175 */     driverConfiguration.setURI("http://localhost:7474");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Embedded
/*     */   {
/* 183 */     private boolean enabled = true;
/*     */     
/*     */     public boolean isEnabled() {
/* 186 */       return this.enabled;
/*     */     }
/*     */     
/*     */     public void setEnabled(boolean enabled) {
/* 190 */       this.enabled = enabled;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\neo4j\Neo4jProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */