/*     */ package org.springframework.boot.autoconfigure.jdbc;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.annotation.PostConstruct;
/*     */ import javax.sql.DataSource;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.boot.context.config.ResourceNotFoundException;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.jdbc.config.SortedResourcesFactoryBean;
/*     */ import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
/*     */ import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
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
/*     */ class DataSourceInitializer
/*     */   implements ApplicationListener<DataSourceInitializedEvent>
/*     */ {
/*  53 */   private static final Log logger = LogFactory.getLog(DataSourceInitializer.class);
/*     */   
/*     */   private final DataSourceProperties properties;
/*     */   
/*     */   private final ApplicationContext applicationContext;
/*     */   
/*     */   private DataSource dataSource;
/*     */   
/*  61 */   private boolean initialized = false;
/*     */   
/*     */   DataSourceInitializer(DataSourceProperties properties, ApplicationContext applicationContext)
/*     */   {
/*  65 */     this.properties = properties;
/*  66 */     this.applicationContext = applicationContext;
/*     */   }
/*     */   
/*     */   @PostConstruct
/*     */   public void init() {
/*  71 */     if (!this.properties.isInitialize()) {
/*  72 */       logger.debug("Initialization disabled (not running DDL scripts)");
/*  73 */       return;
/*     */     }
/*  75 */     if (this.applicationContext.getBeanNamesForType(DataSource.class, false, false).length > 0)
/*     */     {
/*  77 */       this.dataSource = ((DataSource)this.applicationContext.getBean(DataSource.class));
/*     */     }
/*  79 */     if (this.dataSource == null) {
/*  80 */       logger.debug("No DataSource found so not initializing");
/*  81 */       return;
/*     */     }
/*  83 */     runSchemaScripts();
/*     */   }
/*     */   
/*     */   private void runSchemaScripts() {
/*  87 */     List<Resource> scripts = getScripts("spring.datasource.schema", this.properties
/*  88 */       .getSchema(), "schema");
/*  89 */     if (!scripts.isEmpty()) {
/*  90 */       String username = this.properties.getSchemaUsername();
/*  91 */       String password = this.properties.getSchemaPassword();
/*  92 */       runScripts(scripts, username, password);
/*     */       try
/*     */       {
/*  95 */         this.applicationContext.publishEvent(new DataSourceInitializedEvent(this.dataSource));
/*     */         
/*  97 */         if (!this.initialized) {
/*  98 */           runDataScripts();
/*  99 */           this.initialized = true;
/*     */         }
/*     */       }
/*     */       catch (IllegalStateException ex) {
/* 103 */         logger.warn("Could not send event to complete DataSource initialization (" + ex
/* 104 */           .getMessage() + ")");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void onApplicationEvent(DataSourceInitializedEvent event)
/*     */   {
/* 111 */     if (!this.properties.isInitialize()) {
/* 112 */       logger.debug("Initialization disabled (not running data scripts)");
/* 113 */       return;
/*     */     }
/*     */     
/*     */ 
/* 117 */     if (!this.initialized) {
/* 118 */       runDataScripts();
/* 119 */       this.initialized = true;
/*     */     }
/*     */   }
/*     */   
/*     */   private void runDataScripts() {
/* 124 */     List<Resource> scripts = getScripts("spring.datasource.data", this.properties
/* 125 */       .getData(), "data");
/* 126 */     String username = this.properties.getDataUsername();
/* 127 */     String password = this.properties.getDataPassword();
/* 128 */     runScripts(scripts, username, password);
/*     */   }
/*     */   
/*     */   private List<Resource> getScripts(String propertyName, List<String> resources, String fallback)
/*     */   {
/* 133 */     if (resources != null) {
/* 134 */       return getResources(propertyName, resources, true);
/*     */     }
/* 136 */     String platform = this.properties.getPlatform();
/* 137 */     List<String> fallbackResources = new ArrayList();
/* 138 */     fallbackResources.add("classpath*:" + fallback + "-" + platform + ".sql");
/* 139 */     fallbackResources.add("classpath*:" + fallback + ".sql");
/* 140 */     return getResources(propertyName, fallbackResources, false);
/*     */   }
/*     */   
/*     */   private List<Resource> getResources(String propertyName, List<String> locations, boolean validate)
/*     */   {
/* 145 */     List<Resource> resources = new ArrayList();
/* 146 */     for (String location : locations) {
/* 147 */       for (Resource resource : doGetResources(location)) {
/* 148 */         if (resource.exists()) {
/* 149 */           resources.add(resource);
/*     */         }
/* 151 */         else if (validate) {
/* 152 */           throw new ResourceNotFoundException(propertyName, resource);
/*     */         }
/*     */       }
/*     */     }
/* 156 */     return resources;
/*     */   }
/*     */   
/*     */   private Resource[] doGetResources(String location)
/*     */   {
/*     */     try {
/* 162 */       SortedResourcesFactoryBean factory = new SortedResourcesFactoryBean(this.applicationContext, Collections.singletonList(location));
/* 163 */       factory.afterPropertiesSet();
/* 164 */       return (Resource[])factory.getObject();
/*     */     }
/*     */     catch (Exception ex) {
/* 167 */       throw new IllegalStateException("Unable to load resources from " + location, ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private void runScripts(List<Resource> resources, String username, String password)
/*     */   {
/* 173 */     if (resources.isEmpty()) {
/* 174 */       return;
/*     */     }
/* 176 */     ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
/* 177 */     populator.setContinueOnError(this.properties.isContinueOnError());
/* 178 */     populator.setSeparator(this.properties.getSeparator());
/* 179 */     if (this.properties.getSqlScriptEncoding() != null) {
/* 180 */       populator.setSqlScriptEncoding(this.properties.getSqlScriptEncoding().name());
/*     */     }
/* 182 */     for (Resource resource : resources) {
/* 183 */       populator.addScript(resource);
/*     */     }
/* 185 */     DataSource dataSource = this.dataSource;
/* 186 */     if ((StringUtils.hasText(username)) && (StringUtils.hasText(password)))
/*     */     {
/*     */ 
/*     */ 
/* 190 */       dataSource = DataSourceBuilder.create(this.properties.getClassLoader()).driverClassName(this.properties.determineDriverClassName()).url(this.properties.determineUrl()).username(username).password(password).build();
/*     */     }
/* 192 */     DatabasePopulatorUtils.execute(populator, dataSource);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jdbc\DataSourceInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */