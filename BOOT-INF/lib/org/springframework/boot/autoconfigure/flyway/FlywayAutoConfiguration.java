/*     */ package org.springframework.boot.autoconfigure.flyway;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.annotation.PostConstruct;
/*     */ import javax.sql.DataSource;
/*     */ import org.flywaydb.core.Flyway;
/*     */ import org.flywaydb.core.api.MigrationVersion;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*     */ import org.springframework.boot.autoconfigure.data.jpa.EntityManagerFactoryDependsOnPostProcessor;
/*     */ import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.boot.jdbc.DatabaseDriver;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.core.convert.converter.GenericConverter;
/*     */ import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.jdbc.support.JdbcUtils;
/*     */ import org.springframework.jdbc.support.MetaDataAccessException;
/*     */ import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
/*     */ import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Configuration
/*     */ @ConditionalOnClass({Flyway.class})
/*     */ @ConditionalOnBean({DataSource.class})
/*     */ @ConditionalOnProperty(prefix="flyway", name={"enabled"}, matchIfMissing=true)
/*     */ @AutoConfigureAfter({DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
/*     */ public class FlywayAutoConfiguration
/*     */ {
/*     */   @Bean
/*     */   @ConfigurationPropertiesBinding
/*     */   public StringOrNumberToMigrationVersionConverter stringOrNumberMigrationVersionConverter()
/*     */   {
/*  77 */     return new StringOrNumberToMigrationVersionConverter(null);
/*     */   }
/*     */   
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnMissingBean({Flyway.class})
/*     */   @EnableConfigurationProperties({FlywayProperties.class})
/*     */   public static class FlywayConfiguration
/*     */   {
/*     */     private final FlywayProperties properties;
/*     */     
/*     */     private final ResourceLoader resourceLoader;
/*     */     
/*     */     private final DataSource dataSource;
/*     */     
/*     */     private final DataSource flywayDataSource;
/*     */     
/*     */     private final FlywayMigrationStrategy migrationStrategy;
/*     */     
/*     */ 
/*     */     public FlywayConfiguration(FlywayProperties properties, ResourceLoader resourceLoader, ObjectProvider<DataSource> dataSource, @FlywayDataSource ObjectProvider<DataSource> flywayDataSource, ObjectProvider<FlywayMigrationStrategy> migrationStrategy)
/*     */     {
/*  99 */       this.properties = properties;
/* 100 */       this.resourceLoader = resourceLoader;
/* 101 */       this.dataSource = ((DataSource)dataSource.getIfUnique());
/* 102 */       this.flywayDataSource = ((DataSource)flywayDataSource.getIfAvailable());
/* 103 */       this.migrationStrategy = ((FlywayMigrationStrategy)migrationStrategy.getIfAvailable());
/*     */     }
/*     */     
/*     */     @PostConstruct
/*     */     public void checkLocationExists() {
/* 108 */       if (this.properties.isCheckLocation()) {
/* 109 */         Assert.state(!this.properties.getLocations().isEmpty(), "Migration script locations not configured");
/*     */         
/* 111 */         boolean exists = hasAtLeastOneLocation();
/* 112 */         Assert.state(exists, "Cannot find migrations location in: " + this.properties
/*     */         
/* 114 */           .getLocations() + " (please add migrations or check your Flyway configuration)");
/*     */       }
/*     */     }
/*     */     
/*     */     private boolean hasAtLeastOneLocation()
/*     */     {
/* 120 */       for (String location : this.properties.getLocations()) {
/* 121 */         if (this.resourceLoader.getResource(location).exists()) {
/* 122 */           return true;
/*     */         }
/*     */       }
/* 125 */       return false;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConfigurationProperties(prefix="flyway")
/*     */     public Flyway flyway() {
/* 131 */       Flyway flyway = new FlywayAutoConfiguration.SpringBootFlyway(null);
/* 132 */       if (this.properties.isCreateDataSource()) {
/* 133 */         flyway.setDataSource(this.properties.getUrl(), this.properties.getUser(), this.properties
/* 134 */           .getPassword(), 
/* 135 */           (String[])this.properties.getInitSqls().toArray(new String[0]));
/*     */       }
/* 137 */       else if (this.flywayDataSource != null) {
/* 138 */         flyway.setDataSource(this.flywayDataSource);
/*     */       }
/*     */       else {
/* 141 */         flyway.setDataSource(this.dataSource);
/*     */       }
/* 143 */       flyway.setLocations((String[])this.properties.getLocations().toArray(new String[0]));
/* 144 */       return flyway;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConditionalOnMissingBean
/*     */     public FlywayMigrationInitializer flywayInitializer(Flyway flyway) {
/* 150 */       return new FlywayMigrationInitializer(flyway, this.migrationStrategy);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     @Configuration
/*     */     @ConditionalOnClass({LocalContainerEntityManagerFactoryBean.class})
/*     */     @ConditionalOnBean({AbstractEntityManagerFactoryBean.class})
/*     */     protected static class FlywayInitializerJpaDependencyConfiguration
/*     */       extends EntityManagerFactoryDependsOnPostProcessor
/*     */     {
/*     */       public FlywayInitializerJpaDependencyConfiguration()
/*     */       {
/* 164 */         super();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnClass({LocalContainerEntityManagerFactoryBean.class})
/*     */   @ConditionalOnBean({AbstractEntityManagerFactoryBean.class})
/*     */   protected static class FlywayJpaDependencyConfiguration
/*     */     extends EntityManagerFactoryDependsOnPostProcessor
/*     */   {
/*     */     public FlywayJpaDependencyConfiguration()
/*     */     {
/* 182 */       super();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SpringBootFlyway
/*     */     extends Flyway
/*     */   {
/*     */     private static final String VENDOR_PLACEHOLDER = "{vendor}";
/*     */     
/*     */     public void setLocations(String... locations)
/*     */     {
/* 193 */       if (usesVendorLocation(locations)) {
/*     */         try
/*     */         {
/* 196 */           String url = (String)JdbcUtils.extractDatabaseMetaData(getDataSource(), "getURL");
/* 197 */           DatabaseDriver vendor = DatabaseDriver.fromJdbcUrl(url);
/* 198 */           if (vendor != DatabaseDriver.UNKNOWN) {
/* 199 */             for (int i = 0; i < locations.length; i++) {
/* 200 */               locations[i] = locations[i].replace("{vendor}", vendor
/* 201 */                 .getId());
/*     */             }
/*     */           }
/*     */         }
/*     */         catch (MetaDataAccessException ex) {
/* 206 */           throw new IllegalStateException(ex);
/*     */         }
/*     */       }
/* 209 */       super.setLocations(locations);
/*     */     }
/*     */     
/*     */     private boolean usesVendorLocation(String... locations) {
/* 213 */       for (String location : locations) {
/* 214 */         if (location.contains("{vendor}")) {
/* 215 */           return true;
/*     */         }
/*     */       }
/* 218 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class StringOrNumberToMigrationVersionConverter
/*     */     implements GenericConverter
/*     */   {
/*     */     private static final Set<GenericConverter.ConvertiblePair> CONVERTIBLE_TYPES;
/*     */     
/*     */ 
/*     */     static
/*     */     {
/* 232 */       Set<GenericConverter.ConvertiblePair> types = new HashSet(2);
/* 233 */       types.add(new GenericConverter.ConvertiblePair(String.class, MigrationVersion.class));
/* 234 */       types.add(new GenericConverter.ConvertiblePair(Number.class, MigrationVersion.class));
/* 235 */       CONVERTIBLE_TYPES = Collections.unmodifiableSet(types);
/*     */     }
/*     */     
/*     */     public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
/*     */     {
/* 240 */       return CONVERTIBLE_TYPES;
/*     */     }
/*     */     
/*     */ 
/*     */     public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/*     */     {
/* 246 */       String value = ObjectUtils.nullSafeToString(source);
/* 247 */       return MigrationVersion.fromVersion(value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\flyway\FlywayAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */