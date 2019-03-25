/*     */ package org.springframework.boot.autoconfigure.ldap.embedded;
/*     */ 
/*     */ import com.unboundid.ldap.listener.InMemoryDirectoryServer;
/*     */ import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
/*     */ import com.unboundid.ldap.listener.InMemoryListenerConfig;
/*     */ import com.unboundid.ldap.sdk.LDAPException;
/*     */ import com.unboundid.ldap.sdk.schema.Schema;
/*     */ import com.unboundid.ldif.LDIFReader;
/*     */ import java.io.InputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.annotation.PreDestroy;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureBefore;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*     */ import org.springframework.boot.autoconfigure.ldap.LdapAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.ldap.LdapProperties;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.annotation.DependsOn;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.MapPropertySource;
/*     */ import org.springframework.core.env.MutablePropertySources;
/*     */ import org.springframework.core.env.PropertySource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.ldap.core.ContextSource;
/*     */ import org.springframework.ldap.core.support.LdapContextSource;
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
/*     */ @Configuration
/*     */ @EnableConfigurationProperties({LdapProperties.class, EmbeddedLdapProperties.class})
/*     */ @AutoConfigureBefore({LdapAutoConfiguration.class})
/*     */ @ConditionalOnClass({InMemoryDirectoryServer.class})
/*     */ @ConditionalOnProperty(prefix="spring.ldap.embedded", name={"base-dn"})
/*     */ public class EmbeddedLdapAutoConfiguration
/*     */ {
/*     */   private static final String PROPERTY_SOURCE_NAME = "ldap.ports";
/*     */   private final EmbeddedLdapProperties embeddedProperties;
/*     */   private final LdapProperties properties;
/*     */   private final ConfigurableApplicationContext applicationContext;
/*     */   private final Environment environment;
/*     */   private InMemoryDirectoryServer server;
/*     */   
/*     */   public EmbeddedLdapAutoConfiguration(EmbeddedLdapProperties embeddedProperties, LdapProperties properties, ConfigurableApplicationContext applicationContext, Environment environment)
/*     */   {
/*  84 */     this.embeddedProperties = embeddedProperties;
/*  85 */     this.properties = properties;
/*  86 */     this.applicationContext = applicationContext;
/*  87 */     this.environment = environment;
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @DependsOn({"directoryServer"})
/*     */   @ConditionalOnMissingBean
/*     */   public ContextSource ldapContextSource() {
/*  94 */     LdapContextSource source = new LdapContextSource();
/*  95 */     if (hasCredentials(this.embeddedProperties.getCredential())) {
/*  96 */       source.setUserDn(this.embeddedProperties.getCredential().getUsername());
/*  97 */       source.setPassword(this.embeddedProperties.getCredential().getPassword());
/*     */     }
/*  99 */     source.setUrls(this.properties.determineUrls(this.environment));
/* 100 */     return source;
/*     */   }
/*     */   
/*     */   @Bean
/*     */   public InMemoryDirectoryServer directoryServer() throws LDAPException
/*     */   {
/* 106 */     InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig(new String[] { this.embeddedProperties.getBaseDn() });
/* 107 */     if (hasCredentials(this.embeddedProperties.getCredential())) {
/* 108 */       config.addAdditionalBindCredentials(this.embeddedProperties
/* 109 */         .getCredential().getUsername(), this.embeddedProperties
/* 110 */         .getCredential().getPassword());
/*     */     }
/* 112 */     setSchema(config);
/*     */     
/* 114 */     InMemoryListenerConfig listenerConfig = InMemoryListenerConfig.createLDAPConfig("LDAP", this.embeddedProperties.getPort());
/* 115 */     config.setListenerConfigs(new InMemoryListenerConfig[] { listenerConfig });
/* 116 */     this.server = new InMemoryDirectoryServer(config);
/* 117 */     importLdif();
/* 118 */     this.server.startListening();
/* 119 */     setPortProperty(this.applicationContext, this.server.getListenPort());
/* 120 */     return this.server;
/*     */   }
/*     */   
/*     */   private void setSchema(InMemoryDirectoryServerConfig config) {
/* 124 */     if (!this.embeddedProperties.getValidation().isEnabled()) {
/* 125 */       config.setSchema(null);
/* 126 */       return;
/*     */     }
/* 128 */     Resource schema = this.embeddedProperties.getValidation().getSchema();
/* 129 */     if (schema != null) {
/* 130 */       setSchema(config, schema);
/*     */     }
/*     */   }
/*     */   
/*     */   private void setSchema(InMemoryDirectoryServerConfig config, Resource resource) {
/*     */     try {
/* 136 */       Schema defaultSchema = Schema.getDefaultStandardSchema();
/* 137 */       Schema schema = Schema.getSchema(resource.getInputStream());
/* 138 */       config.setSchema(Schema.mergeSchemas(new Schema[] { defaultSchema, schema }));
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 142 */       throw new IllegalStateException("Unable to load schema " + resource.getDescription(), ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean hasCredentials(EmbeddedLdapProperties.Credential credential) {
/* 147 */     return (StringUtils.hasText(credential.getUsername())) && 
/* 148 */       (StringUtils.hasText(credential.getPassword()));
/*     */   }
/*     */   
/*     */   private void importLdif() throws LDAPException {
/* 152 */     String location = this.embeddedProperties.getLdif();
/* 153 */     if (StringUtils.hasText(location)) {
/*     */       try {
/* 155 */         Resource resource = this.applicationContext.getResource(location);
/* 156 */         if (resource.exists()) {
/* 157 */           InputStream inputStream = resource.getInputStream();
/*     */           try {
/* 159 */             this.server.importFromLDIF(true, new LDIFReader(inputStream));
/*     */           }
/*     */           finally {
/* 162 */             inputStream.close();
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Exception ex) {
/* 167 */         throw new IllegalStateException("Unable to load LDIF " + location, ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void setPortProperty(ApplicationContext context, int port) {
/* 173 */     if ((context instanceof ConfigurableApplicationContext))
/*     */     {
/* 175 */       MutablePropertySources sources = ((ConfigurableApplicationContext)context).getEnvironment().getPropertySources();
/* 176 */       getLdapPorts(sources).put("local.ldap.port", Integer.valueOf(port));
/*     */     }
/* 178 */     if (context.getParent() != null) {
/* 179 */       setPortProperty(context.getParent(), port);
/*     */     }
/*     */   }
/*     */   
/*     */   private Map<String, Object> getLdapPorts(MutablePropertySources sources)
/*     */   {
/* 185 */     PropertySource<?> propertySource = sources.get("ldap.ports");
/* 186 */     if (propertySource == null) {
/* 187 */       propertySource = new MapPropertySource("ldap.ports", new HashMap());
/*     */       
/* 189 */       sources.addFirst(propertySource);
/*     */     }
/* 191 */     return (Map)propertySource.getSource();
/*     */   }
/*     */   
/*     */   @PreDestroy
/*     */   public void close() {
/* 196 */     if (this.server != null) {
/* 197 */       this.server.shutDown(true);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\ldap\embedded\EmbeddedLdapAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */