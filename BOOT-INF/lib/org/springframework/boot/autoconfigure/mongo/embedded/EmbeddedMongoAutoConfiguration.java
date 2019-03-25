/*     */ package org.springframework.boot.autoconfigure.mongo.embedded;
/*     */ 
/*     */ import com.mongodb.MongoClient;
/*     */ import de.flapdoodle.embed.mongo.Command;
/*     */ import de.flapdoodle.embed.mongo.MongodExecutable;
/*     */ import de.flapdoodle.embed.mongo.MongodStarter;
/*     */ import de.flapdoodle.embed.mongo.config.ExtractedArtifactStoreBuilder;
/*     */ import de.flapdoodle.embed.mongo.config.IMongodConfig;
/*     */ import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
/*     */ import de.flapdoodle.embed.mongo.config.Net;
/*     */ import de.flapdoodle.embed.mongo.config.Storage;
/*     */ import de.flapdoodle.embed.mongo.distribution.Feature;
/*     */ import de.flapdoodle.embed.mongo.distribution.IFeatureAwareVersion;
/*     */ import de.flapdoodle.embed.process.config.IRuntimeConfig;
/*     */ import de.flapdoodle.embed.process.config.io.ProcessOutput;
/*     */ import de.flapdoodle.embed.process.io.Processors;
/*     */ import de.flapdoodle.embed.process.io.Slf4jLevel;
/*     */ import de.flapdoodle.embed.process.io.progress.Slf4jProgressListener;
/*     */ import de.flapdoodle.embed.process.runtime.Network;
/*     */ import de.flapdoodle.embed.process.store.ArtifactStoreBuilder;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureBefore;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.data.mongo.MongoClientDependsOnBeanFactoryPostProcessor;
/*     */ import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.mongo.MongoProperties;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.env.MapPropertySource;
/*     */ import org.springframework.core.env.MutablePropertySources;
/*     */ import org.springframework.core.env.PropertySource;
/*     */ import org.springframework.data.mongodb.core.MongoClientFactoryBean;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @EnableConfigurationProperties({MongoProperties.class, EmbeddedMongoProperties.class})
/*     */ @AutoConfigureBefore({MongoAutoConfiguration.class})
/*     */ @ConditionalOnClass({MongoClient.class, MongodStarter.class})
/*     */ public class EmbeddedMongoAutoConfiguration
/*     */ {
/*  82 */   private static final byte[] IP4_LOOPBACK_ADDRESS = { Byte.MAX_VALUE, 0, 0, 1 };
/*     */   
/*  84 */   private static final byte[] IP6_LOOPBACK_ADDRESS = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 };
/*     */   
/*     */ 
/*     */   private final MongoProperties properties;
/*     */   
/*     */   private final EmbeddedMongoProperties embeddedProperties;
/*     */   
/*     */   private final ApplicationContext context;
/*     */   
/*     */   private final IRuntimeConfig runtimeConfig;
/*     */   
/*     */ 
/*     */   public EmbeddedMongoAutoConfiguration(MongoProperties properties, EmbeddedMongoProperties embeddedProperties, ApplicationContext context, IRuntimeConfig runtimeConfig)
/*     */   {
/*  98 */     this.properties = properties;
/*  99 */     this.embeddedProperties = embeddedProperties;
/* 100 */     this.context = context;
/* 101 */     this.runtimeConfig = runtimeConfig;
/*     */   }
/*     */   
/*     */   @Bean(initMethod="start", destroyMethod="stop")
/*     */   @ConditionalOnMissingBean
/*     */   public MongodExecutable embeddedMongoServer(IMongodConfig mongodConfig) throws IOException
/*     */   {
/* 108 */     Integer configuredPort = this.properties.getPort();
/* 109 */     if ((configuredPort == null) || (configuredPort.intValue() == 0)) {
/* 110 */       setEmbeddedPort(mongodConfig.net().getPort());
/*     */     }
/* 112 */     MongodStarter mongodStarter = getMongodStarter(this.runtimeConfig);
/* 113 */     return (MongodExecutable)mongodStarter.prepare(mongodConfig);
/*     */   }
/*     */   
/*     */   private MongodStarter getMongodStarter(IRuntimeConfig runtimeConfig) {
/* 117 */     if (runtimeConfig == null) {
/* 118 */       return MongodStarter.getDefaultInstance();
/*     */     }
/* 120 */     return MongodStarter.getInstance(runtimeConfig);
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public IMongodConfig embeddedMongoConfiguration()
/*     */     throws IOException
/*     */   {
/* 128 */     IFeatureAwareVersion featureAwareVersion = new ToStringFriendlyFeatureAwareVersion(this.embeddedProperties.getVersion(), this.embeddedProperties.getFeatures(), null);
/*     */     
/* 130 */     MongodConfigBuilder builder = new MongodConfigBuilder().version(featureAwareVersion);
/* 131 */     if (this.embeddedProperties.getStorage() != null) {
/* 132 */       builder.replication(new Storage(this.embeddedProperties
/* 133 */         .getStorage().getDatabaseDir(), this.embeddedProperties
/* 134 */         .getStorage().getReplSetName(), this.embeddedProperties
/* 135 */         .getStorage().getOplogSize() != null ? this.embeddedProperties
/* 136 */         .getStorage().getOplogSize().intValue() : 0));
/*     */     }
/*     */     
/* 139 */     Integer configuredPort = this.properties.getPort();
/* 140 */     if ((configuredPort != null) && (configuredPort.intValue() > 0)) {
/* 141 */       builder.net(new Net(getHost().getHostAddress(), configuredPort.intValue(), 
/* 142 */         Network.localhostIsIPv6()));
/*     */     }
/*     */     else {
/* 145 */       builder.net(new Net(getHost().getHostAddress(), 
/* 146 */         Network.getFreeServerPort(getHost()), Network.localhostIsIPv6()));
/*     */     }
/* 148 */     return builder.build();
/*     */   }
/*     */   
/*     */   private InetAddress getHost() throws UnknownHostException {
/* 152 */     if (this.properties.getHost() == null) {
/* 153 */       return InetAddress.getByAddress(Network.localhostIsIPv6() ? IP6_LOOPBACK_ADDRESS : IP4_LOOPBACK_ADDRESS);
/*     */     }
/*     */     
/* 156 */     return InetAddress.getByName(this.properties.getHost());
/*     */   }
/*     */   
/*     */   private void setEmbeddedPort(int port) {
/* 160 */     setPortProperty(this.context, port);
/*     */   }
/*     */   
/*     */   private void setPortProperty(ApplicationContext currentContext, int port) {
/* 164 */     if ((currentContext instanceof ConfigurableApplicationContext))
/*     */     {
/* 166 */       MutablePropertySources sources = ((ConfigurableApplicationContext)currentContext).getEnvironment().getPropertySources();
/* 167 */       getMongoPorts(sources).put("local.mongo.port", Integer.valueOf(port));
/*     */     }
/* 169 */     if (currentContext.getParent() != null) {
/* 170 */       setPortProperty(currentContext.getParent(), port);
/*     */     }
/*     */   }
/*     */   
/*     */   private Map<String, Object> getMongoPorts(MutablePropertySources sources)
/*     */   {
/* 176 */     PropertySource<?> propertySource = sources.get("mongo.ports");
/* 177 */     if (propertySource == null) {
/* 178 */       propertySource = new MapPropertySource("mongo.ports", new HashMap());
/*     */       
/* 180 */       sources.addFirst(propertySource);
/*     */     }
/* 182 */     return (Map)propertySource.getSource();
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @ConditionalOnClass({Logger.class})
/*     */   @ConditionalOnMissingBean({IRuntimeConfig.class})
/*     */   static class RuntimeConfigConfiguration
/*     */   {
/*     */     @Bean
/*     */     public IRuntimeConfig embeddedMongoRuntimeConfig()
/*     */     {
/* 193 */       Logger logger = LoggerFactory.getLogger(getClass().getPackage().getName() + ".EmbeddedMongo");
/*     */       
/*     */ 
/* 196 */       ProcessOutput processOutput = new ProcessOutput(Processors.logTo(logger, Slf4jLevel.INFO), Processors.logTo(logger, Slf4jLevel.ERROR), Processors.named("[console>]", 
/* 197 */         Processors.logTo(logger, Slf4jLevel.DEBUG)));
/* 198 */       return new de.flapdoodle.embed.mongo.config.RuntimeConfigBuilder().defaultsWithLogger(Command.MongoD, logger)
/* 199 */         .processOutput(processOutput).artifactStore(getArtifactStore(logger))
/* 200 */         .build();
/*     */     }
/*     */     
/*     */     private ArtifactStoreBuilder getArtifactStore(Logger logger) {
/* 204 */       return 
/* 205 */         new ExtractedArtifactStoreBuilder().defaults(Command.MongoD).download(new de.flapdoodle.embed.mongo.config.DownloadConfigBuilder()
/* 206 */         .defaultsForCommand(Command.MongoD)
/* 207 */         .progressListener(new Slf4jProgressListener(logger)).build());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnClass({MongoClient.class, MongoClientFactoryBean.class})
/*     */   protected static class EmbeddedMongoDependencyConfiguration
/*     */     extends MongoClientDependsOnBeanFactoryPostProcessor
/*     */   {
/*     */     public EmbeddedMongoDependencyConfiguration()
/*     */     {
/* 222 */       super();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final class ToStringFriendlyFeatureAwareVersion
/*     */     implements IFeatureAwareVersion
/*     */   {
/*     */     private final String version;
/*     */     
/*     */ 
/*     */     private final Set<Feature> features;
/*     */     
/*     */ 
/*     */ 
/*     */     private ToStringFriendlyFeatureAwareVersion(String version, Set<Feature> features)
/*     */     {
/* 240 */       Assert.notNull(version, "version must not be null");
/* 241 */       this.version = version;
/* 242 */       this.features = (features == null ? Collections.emptySet() : features);
/*     */     }
/*     */     
/*     */ 
/*     */     public String asInDownloadPath()
/*     */     {
/* 248 */       return this.version;
/*     */     }
/*     */     
/*     */     public boolean enabled(Feature feature)
/*     */     {
/* 253 */       return this.features.contains(feature);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 258 */       return this.version;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 263 */       int prime = 31;
/* 264 */       int result = 1;
/* 265 */       result = 31 * result + this.features.hashCode();
/* 266 */       result = 31 * result + this.version.hashCode();
/* 267 */       return result;
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 272 */       if (this == obj) {
/* 273 */         return true;
/*     */       }
/* 275 */       if (obj == null) {
/* 276 */         return false;
/*     */       }
/* 278 */       if (getClass() == obj.getClass()) {
/* 279 */         ToStringFriendlyFeatureAwareVersion other = (ToStringFriendlyFeatureAwareVersion)obj;
/* 280 */         boolean equals = true;
/* 281 */         equals = (equals) && (this.features.equals(other.features));
/* 282 */         equals = (equals) && (this.version.equals(other.version));
/* 283 */         return equals;
/*     */       }
/* 285 */       return super.equals(obj);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\mongo\embedded\EmbeddedMongoAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */