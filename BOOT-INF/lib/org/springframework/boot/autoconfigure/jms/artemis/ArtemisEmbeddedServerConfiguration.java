/*     */ package org.springframework.boot.autoconfigure.jms.artemis;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import org.apache.activemq.artemis.jms.server.config.JMSConfiguration;
/*     */ import org.apache.activemq.artemis.jms.server.config.JMSQueueConfiguration;
/*     */ import org.apache.activemq.artemis.jms.server.config.TopicConfiguration;
/*     */ import org.apache.activemq.artemis.jms.server.config.impl.JMSConfigurationImpl;
/*     */ import org.apache.activemq.artemis.jms.server.config.impl.JMSQueueConfigurationImpl;
/*     */ import org.apache.activemq.artemis.jms.server.config.impl.TopicConfigurationImpl;
/*     */ import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @org.springframework.context.annotation.Configuration
/*     */ @ConditionalOnClass(name={"org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS"})
/*     */ @ConditionalOnProperty(prefix="spring.artemis.embedded", name={"enabled"}, havingValue="true", matchIfMissing=true)
/*     */ class ArtemisEmbeddedServerConfiguration
/*     */ {
/*     */   private final ArtemisProperties properties;
/*     */   private final List<ArtemisConfigurationCustomizer> configurationCustomizers;
/*     */   private final List<JMSQueueConfiguration> queuesConfiguration;
/*     */   private final List<TopicConfiguration> topicsConfiguration;
/*     */   
/*     */   ArtemisEmbeddedServerConfiguration(ArtemisProperties properties, ObjectProvider<List<ArtemisConfigurationCustomizer>> configurationCustomizers, ObjectProvider<List<JMSQueueConfiguration>> queuesConfiguration, ObjectProvider<List<TopicConfiguration>> topicsConfiguration)
/*     */   {
/*  62 */     this.properties = properties;
/*  63 */     this.configurationCustomizers = ((List)configurationCustomizers.getIfAvailable());
/*  64 */     this.queuesConfiguration = ((List)queuesConfiguration.getIfAvailable());
/*  65 */     this.topicsConfiguration = ((List)topicsConfiguration.getIfAvailable());
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public org.apache.activemq.artemis.core.config.Configuration artemisConfiguration() {
/*  71 */     return 
/*  72 */       new ArtemisEmbeddedConfigurationFactory(this.properties).createConfiguration();
/*     */   }
/*     */   
/*     */ 
/*     */   @Bean(initMethod="start", destroyMethod="stop")
/*     */   @ConditionalOnMissingBean
/*     */   public EmbeddedJMS artemisServer(org.apache.activemq.artemis.core.config.Configuration configuration, JMSConfiguration jmsConfiguration)
/*     */   {
/*  80 */     EmbeddedJMS server = new EmbeddedJMS();
/*  81 */     customize(configuration);
/*  82 */     server.setConfiguration(configuration);
/*  83 */     server.setJmsConfiguration(jmsConfiguration);
/*  84 */     server.setRegistry(new ArtemisNoOpBindingRegistry());
/*  85 */     return server;
/*     */   }
/*     */   
/*     */   private void customize(org.apache.activemq.artemis.core.config.Configuration configuration)
/*     */   {
/*  90 */     if (this.configurationCustomizers != null) {
/*  91 */       AnnotationAwareOrderComparator.sort(this.configurationCustomizers);
/*  92 */       for (ArtemisConfigurationCustomizer customizer : this.configurationCustomizers) {
/*  93 */         customizer.customize(configuration);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public JMSConfiguration artemisJmsConfiguration() {
/* 101 */     JMSConfiguration configuration = new JMSConfigurationImpl();
/* 102 */     addAll(configuration.getQueueConfigurations(), this.queuesConfiguration);
/* 103 */     addAll(configuration.getTopicConfigurations(), this.topicsConfiguration);
/* 104 */     addQueues(configuration, this.properties.getEmbedded().getQueues());
/* 105 */     addTopics(configuration, this.properties.getEmbedded().getTopics());
/* 106 */     return configuration;
/*     */   }
/*     */   
/*     */   private <T> void addAll(List<T> list, Collection<? extends T> items) {
/* 110 */     if (items != null) {
/* 111 */       list.addAll(items);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addQueues(JMSConfiguration configuration, String[] queues) {
/* 116 */     boolean persistent = this.properties.getEmbedded().isPersistent();
/* 117 */     for (String queue : queues) {
/* 118 */       JMSQueueConfigurationImpl jmsQueueConfiguration = new JMSQueueConfigurationImpl();
/* 119 */       jmsQueueConfiguration.setName(queue);
/* 120 */       jmsQueueConfiguration.setDurable(persistent);
/* 121 */       jmsQueueConfiguration.setBindings(new String[] { "/queue/" + queue });
/* 122 */       configuration.getQueueConfigurations().add(jmsQueueConfiguration);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addTopics(JMSConfiguration configuration, String[] topics) {
/* 127 */     for (String topic : topics) {
/* 128 */       TopicConfigurationImpl topicConfiguration = new TopicConfigurationImpl();
/* 129 */       topicConfiguration.setName(topic);
/* 130 */       topicConfiguration.setBindings(new String[] { "/topic/" + topic });
/* 131 */       configuration.getTopicConfigurations().add(topicConfiguration);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jms\artemis\ArtemisEmbeddedServerConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */