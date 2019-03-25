/*     */ package org.springframework.boot.autoconfigure.kafka;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.kafka.common.serialization.StringDeserializer;
/*     */ import org.apache.kafka.common.serialization.StringSerializer;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.kafka.listener.AbstractMessageListenerContainer.AckMode;
/*     */ import org.springframework.util.CollectionUtils;
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
/*     */ @ConfigurationProperties(prefix="spring.kafka")
/*     */ public class KafkaProperties
/*     */ {
/*  56 */   private List<String> bootstrapServers = new ArrayList(
/*  57 */     Collections.singletonList("localhost:9092"));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String clientId;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  67 */   private Map<String, String> properties = new HashMap();
/*     */   
/*  69 */   private final Consumer consumer = new Consumer();
/*     */   
/*  71 */   private final Producer producer = new Producer();
/*     */   
/*  73 */   private final Listener listener = new Listener();
/*     */   
/*  75 */   private final Ssl ssl = new Ssl();
/*     */   
/*  77 */   private final Template template = new Template();
/*     */   
/*     */   public List<String> getBootstrapServers() {
/*  80 */     return this.bootstrapServers;
/*     */   }
/*     */   
/*     */   public void setBootstrapServers(List<String> bootstrapServers) {
/*  84 */     this.bootstrapServers = bootstrapServers;
/*     */   }
/*     */   
/*     */   public String getClientId() {
/*  88 */     return this.clientId;
/*     */   }
/*     */   
/*     */   public void setClientId(String clientId) {
/*  92 */     this.clientId = clientId;
/*     */   }
/*     */   
/*     */   public Map<String, String> getProperties() {
/*  96 */     return this.properties;
/*     */   }
/*     */   
/*     */   public void setProperties(Map<String, String> properties) {
/* 100 */     this.properties = properties;
/*     */   }
/*     */   
/*     */   public Consumer getConsumer() {
/* 104 */     return this.consumer;
/*     */   }
/*     */   
/*     */   public Producer getProducer() {
/* 108 */     return this.producer;
/*     */   }
/*     */   
/*     */   public Listener getListener() {
/* 112 */     return this.listener;
/*     */   }
/*     */   
/*     */   public Ssl getSsl() {
/* 116 */     return this.ssl;
/*     */   }
/*     */   
/*     */   public Template getTemplate() {
/* 120 */     return this.template;
/*     */   }
/*     */   
/*     */   private Map<String, Object> buildCommonProperties() {
/* 124 */     Map<String, Object> properties = new HashMap();
/* 125 */     if (this.bootstrapServers != null) {
/* 126 */       properties.put("bootstrap.servers", this.bootstrapServers);
/*     */     }
/*     */     
/* 129 */     if (this.clientId != null) {
/* 130 */       properties.put("client.id", this.clientId);
/*     */     }
/* 132 */     if (this.ssl.getKeyPassword() != null) {
/* 133 */       properties.put("ssl.key.password", this.ssl.getKeyPassword());
/*     */     }
/* 135 */     if (this.ssl.getKeystoreLocation() != null) {
/* 136 */       properties.put("ssl.keystore.location", 
/* 137 */         resourceToPath(this.ssl.getKeystoreLocation()));
/*     */     }
/* 139 */     if (this.ssl.getKeystorePassword() != null) {
/* 140 */       properties.put("ssl.keystore.password", this.ssl
/* 141 */         .getKeystorePassword());
/*     */     }
/* 143 */     if (this.ssl.getTruststoreLocation() != null) {
/* 144 */       properties.put("ssl.truststore.location", 
/* 145 */         resourceToPath(this.ssl.getTruststoreLocation()));
/*     */     }
/* 147 */     if (this.ssl.getTruststorePassword() != null) {
/* 148 */       properties.put("ssl.truststore.password", this.ssl
/* 149 */         .getTruststorePassword());
/*     */     }
/* 151 */     if (!CollectionUtils.isEmpty(this.properties)) {
/* 152 */       properties.putAll(this.properties);
/*     */     }
/* 154 */     return properties;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, Object> buildConsumerProperties()
/*     */   {
/* 166 */     Map<String, Object> properties = buildCommonProperties();
/* 167 */     properties.putAll(this.consumer.buildProperties());
/* 168 */     return properties;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, Object> buildProducerProperties()
/*     */   {
/* 180 */     Map<String, Object> properties = buildCommonProperties();
/* 181 */     properties.putAll(this.producer.buildProperties());
/* 182 */     return properties;
/*     */   }
/*     */   
/*     */   private static String resourceToPath(Resource resource) {
/*     */     try {
/* 187 */       return resource.getFile().getAbsolutePath();
/*     */     }
/*     */     catch (IOException ex) {
/* 190 */       throw new IllegalStateException("Resource '" + resource + "' must be on a file system", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Consumer
/*     */   {
/* 197 */     private final KafkaProperties.Ssl ssl = new KafkaProperties.Ssl();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private Integer autoCommitInterval;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private String autoOffsetReset;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private List<String> bootstrapServers;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private String clientId;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private Boolean enableAutoCommit;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private Integer fetchMaxWait;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private Integer fetchMinSize;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private String groupId;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private Integer heartbeatInterval;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 252 */     private Class<?> keyDeserializer = StringDeserializer.class;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 257 */     private Class<?> valueDeserializer = StringDeserializer.class;
/*     */     
/*     */ 
/*     */     private Integer maxPollRecords;
/*     */     
/*     */ 
/*     */     public KafkaProperties.Ssl getSsl()
/*     */     {
/* 265 */       return this.ssl;
/*     */     }
/*     */     
/*     */     public Integer getAutoCommitInterval() {
/* 269 */       return this.autoCommitInterval;
/*     */     }
/*     */     
/*     */     public void setAutoCommitInterval(Integer autoCommitInterval) {
/* 273 */       this.autoCommitInterval = autoCommitInterval;
/*     */     }
/*     */     
/*     */     public String getAutoOffsetReset() {
/* 277 */       return this.autoOffsetReset;
/*     */     }
/*     */     
/*     */     public void setAutoOffsetReset(String autoOffsetReset) {
/* 281 */       this.autoOffsetReset = autoOffsetReset;
/*     */     }
/*     */     
/*     */     public List<String> getBootstrapServers() {
/* 285 */       return this.bootstrapServers;
/*     */     }
/*     */     
/*     */     public void setBootstrapServers(List<String> bootstrapServers) {
/* 289 */       this.bootstrapServers = bootstrapServers;
/*     */     }
/*     */     
/*     */     public String getClientId() {
/* 293 */       return this.clientId;
/*     */     }
/*     */     
/*     */     public void setClientId(String clientId) {
/* 297 */       this.clientId = clientId;
/*     */     }
/*     */     
/*     */     public Boolean getEnableAutoCommit() {
/* 301 */       return this.enableAutoCommit;
/*     */     }
/*     */     
/*     */     public void setEnableAutoCommit(Boolean enableAutoCommit) {
/* 305 */       this.enableAutoCommit = enableAutoCommit;
/*     */     }
/*     */     
/*     */     public Integer getFetchMaxWait() {
/* 309 */       return this.fetchMaxWait;
/*     */     }
/*     */     
/*     */     public void setFetchMaxWait(Integer fetchMaxWait) {
/* 313 */       this.fetchMaxWait = fetchMaxWait;
/*     */     }
/*     */     
/*     */     public Integer getFetchMinSize() {
/* 317 */       return this.fetchMinSize;
/*     */     }
/*     */     
/*     */     public void setFetchMinSize(Integer fetchMinSize) {
/* 321 */       this.fetchMinSize = fetchMinSize;
/*     */     }
/*     */     
/*     */     public String getGroupId() {
/* 325 */       return this.groupId;
/*     */     }
/*     */     
/*     */     public void setGroupId(String groupId) {
/* 329 */       this.groupId = groupId;
/*     */     }
/*     */     
/*     */     public Integer getHeartbeatInterval() {
/* 333 */       return this.heartbeatInterval;
/*     */     }
/*     */     
/*     */     public void setHeartbeatInterval(Integer heartbeatInterval) {
/* 337 */       this.heartbeatInterval = heartbeatInterval;
/*     */     }
/*     */     
/*     */     public Class<?> getKeyDeserializer() {
/* 341 */       return this.keyDeserializer;
/*     */     }
/*     */     
/*     */     public void setKeyDeserializer(Class<?> keyDeserializer) {
/* 345 */       this.keyDeserializer = keyDeserializer;
/*     */     }
/*     */     
/*     */     public Class<?> getValueDeserializer() {
/* 349 */       return this.valueDeserializer;
/*     */     }
/*     */     
/*     */     public void setValueDeserializer(Class<?> valueDeserializer) {
/* 353 */       this.valueDeserializer = valueDeserializer;
/*     */     }
/*     */     
/*     */     public Integer getMaxPollRecords() {
/* 357 */       return this.maxPollRecords;
/*     */     }
/*     */     
/*     */     public void setMaxPollRecords(Integer maxPollRecords) {
/* 361 */       this.maxPollRecords = maxPollRecords;
/*     */     }
/*     */     
/*     */     public Map<String, Object> buildProperties() {
/* 365 */       Map<String, Object> properties = new HashMap();
/* 366 */       if (this.autoCommitInterval != null) {
/* 367 */         properties.put("auto.commit.interval.ms", this.autoCommitInterval);
/*     */       }
/*     */       
/* 370 */       if (this.autoOffsetReset != null) {
/* 371 */         properties.put("auto.offset.reset", this.autoOffsetReset);
/*     */       }
/*     */       
/* 374 */       if (this.bootstrapServers != null) {
/* 375 */         properties.put("bootstrap.servers", this.bootstrapServers);
/*     */       }
/*     */       
/* 378 */       if (this.clientId != null) {
/* 379 */         properties.put("client.id", this.clientId);
/*     */       }
/* 381 */       if (this.enableAutoCommit != null) {
/* 382 */         properties.put("enable.auto.commit", this.enableAutoCommit);
/*     */       }
/*     */       
/* 385 */       if (this.fetchMaxWait != null) {
/* 386 */         properties.put("fetch.max.wait.ms", this.fetchMaxWait);
/*     */       }
/*     */       
/* 389 */       if (this.fetchMinSize != null) {
/* 390 */         properties.put("fetch.min.bytes", this.fetchMinSize);
/*     */       }
/* 392 */       if (this.groupId != null) {
/* 393 */         properties.put("group.id", this.groupId);
/*     */       }
/* 395 */       if (this.heartbeatInterval != null) {
/* 396 */         properties.put("heartbeat.interval.ms", this.heartbeatInterval);
/*     */       }
/*     */       
/* 399 */       if (this.keyDeserializer != null) {
/* 400 */         properties.put("key.deserializer", this.keyDeserializer);
/*     */       }
/*     */       
/* 403 */       if (this.ssl.getKeyPassword() != null) {
/* 404 */         properties.put("ssl.key.password", this.ssl
/* 405 */           .getKeyPassword());
/*     */       }
/* 407 */       if (this.ssl.getKeystoreLocation() != null) {
/* 408 */         properties.put("ssl.keystore.location", 
/* 409 */           KafkaProperties.resourceToPath(this.ssl.getKeystoreLocation()));
/*     */       }
/* 411 */       if (this.ssl.getKeystorePassword() != null) {
/* 412 */         properties.put("ssl.keystore.password", this.ssl
/* 413 */           .getKeystorePassword());
/*     */       }
/* 415 */       if (this.ssl.getTruststoreLocation() != null) {
/* 416 */         properties.put("ssl.truststore.location", 
/* 417 */           KafkaProperties.resourceToPath(this.ssl.getTruststoreLocation()));
/*     */       }
/* 419 */       if (this.ssl.getTruststorePassword() != null) {
/* 420 */         properties.put("ssl.truststore.password", this.ssl
/* 421 */           .getTruststorePassword());
/*     */       }
/* 423 */       if (this.valueDeserializer != null) {
/* 424 */         properties.put("value.deserializer", this.valueDeserializer);
/*     */       }
/*     */       
/* 427 */       if (this.maxPollRecords != null) {
/* 428 */         properties.put("max.poll.records", this.maxPollRecords);
/*     */       }
/*     */       
/* 431 */       return properties;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Producer
/*     */   {
/* 438 */     private final KafkaProperties.Ssl ssl = new KafkaProperties.Ssl();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private String acks;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private Integer batchSize;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private List<String> bootstrapServers;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private Long bufferMemory;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private String clientId;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private String compressionType;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 476 */     private Class<?> keySerializer = StringSerializer.class;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 481 */     private Class<?> valueSerializer = StringSerializer.class;
/*     */     
/*     */ 
/*     */     private Integer retries;
/*     */     
/*     */ 
/*     */     public KafkaProperties.Ssl getSsl()
/*     */     {
/* 489 */       return this.ssl;
/*     */     }
/*     */     
/*     */     public String getAcks() {
/* 493 */       return this.acks;
/*     */     }
/*     */     
/*     */     public void setAcks(String acks) {
/* 497 */       this.acks = acks;
/*     */     }
/*     */     
/*     */     public Integer getBatchSize() {
/* 501 */       return this.batchSize;
/*     */     }
/*     */     
/*     */     public void setBatchSize(Integer batchSize) {
/* 505 */       this.batchSize = batchSize;
/*     */     }
/*     */     
/*     */     public List<String> getBootstrapServers() {
/* 509 */       return this.bootstrapServers;
/*     */     }
/*     */     
/*     */     public void setBootstrapServers(List<String> bootstrapServers) {
/* 513 */       this.bootstrapServers = bootstrapServers;
/*     */     }
/*     */     
/*     */     public Long getBufferMemory() {
/* 517 */       return this.bufferMemory;
/*     */     }
/*     */     
/*     */     public void setBufferMemory(Long bufferMemory) {
/* 521 */       this.bufferMemory = bufferMemory;
/*     */     }
/*     */     
/*     */     public String getClientId() {
/* 525 */       return this.clientId;
/*     */     }
/*     */     
/*     */     public void setClientId(String clientId) {
/* 529 */       this.clientId = clientId;
/*     */     }
/*     */     
/*     */     public String getCompressionType() {
/* 533 */       return this.compressionType;
/*     */     }
/*     */     
/*     */     public void setCompressionType(String compressionType) {
/* 537 */       this.compressionType = compressionType;
/*     */     }
/*     */     
/*     */     public Class<?> getKeySerializer() {
/* 541 */       return this.keySerializer;
/*     */     }
/*     */     
/*     */     public void setKeySerializer(Class<?> keySerializer) {
/* 545 */       this.keySerializer = keySerializer;
/*     */     }
/*     */     
/*     */     public Class<?> getValueSerializer() {
/* 549 */       return this.valueSerializer;
/*     */     }
/*     */     
/*     */     public void setValueSerializer(Class<?> valueSerializer) {
/* 553 */       this.valueSerializer = valueSerializer;
/*     */     }
/*     */     
/*     */     public Integer getRetries() {
/* 557 */       return this.retries;
/*     */     }
/*     */     
/*     */     public void setRetries(Integer retries) {
/* 561 */       this.retries = retries;
/*     */     }
/*     */     
/*     */     public Map<String, Object> buildProperties() {
/* 565 */       Map<String, Object> properties = new HashMap();
/* 566 */       if (this.acks != null) {
/* 567 */         properties.put("acks", this.acks);
/*     */       }
/* 569 */       if (this.batchSize != null) {
/* 570 */         properties.put("batch.size", this.batchSize);
/*     */       }
/* 572 */       if (this.bootstrapServers != null) {
/* 573 */         properties.put("bootstrap.servers", this.bootstrapServers);
/*     */       }
/*     */       
/* 576 */       if (this.bufferMemory != null) {
/* 577 */         properties.put("buffer.memory", this.bufferMemory);
/*     */       }
/* 579 */       if (this.clientId != null) {
/* 580 */         properties.put("client.id", this.clientId);
/*     */       }
/* 582 */       if (this.compressionType != null) {
/* 583 */         properties.put("compression.type", this.compressionType);
/*     */       }
/*     */       
/* 586 */       if (this.keySerializer != null) {
/* 587 */         properties.put("key.serializer", this.keySerializer);
/*     */       }
/*     */       
/* 590 */       if (this.retries != null) {
/* 591 */         properties.put("retries", this.retries);
/*     */       }
/* 593 */       if (this.ssl.getKeyPassword() != null) {
/* 594 */         properties.put("ssl.key.password", this.ssl
/* 595 */           .getKeyPassword());
/*     */       }
/* 597 */       if (this.ssl.getKeystoreLocation() != null) {
/* 598 */         properties.put("ssl.keystore.location", 
/* 599 */           KafkaProperties.resourceToPath(this.ssl.getKeystoreLocation()));
/*     */       }
/* 601 */       if (this.ssl.getKeystorePassword() != null) {
/* 602 */         properties.put("ssl.keystore.password", this.ssl
/* 603 */           .getKeystorePassword());
/*     */       }
/* 605 */       if (this.ssl.getTruststoreLocation() != null) {
/* 606 */         properties.put("ssl.truststore.location", 
/* 607 */           KafkaProperties.resourceToPath(this.ssl.getTruststoreLocation()));
/*     */       }
/* 609 */       if (this.ssl.getTruststorePassword() != null) {
/* 610 */         properties.put("ssl.truststore.password", this.ssl
/* 611 */           .getTruststorePassword());
/*     */       }
/* 613 */       if (this.valueSerializer != null) {
/* 614 */         properties.put("value.serializer", this.valueSerializer);
/*     */       }
/*     */       
/* 617 */       return properties;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class Template
/*     */   {
/*     */     private String defaultTopic;
/*     */     
/*     */ 
/*     */     public String getDefaultTopic()
/*     */     {
/* 630 */       return this.defaultTopic;
/*     */     }
/*     */     
/*     */     public void setDefaultTopic(String defaultTopic) {
/* 634 */       this.defaultTopic = defaultTopic;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Listener
/*     */   {
/*     */     private AbstractMessageListenerContainer.AckMode ackMode;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private Integer concurrency;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private Long pollTimeout;
/*     */     
/*     */ 
/*     */ 
/*     */     private Integer ackCount;
/*     */     
/*     */ 
/*     */ 
/*     */     private Long ackTime;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public AbstractMessageListenerContainer.AckMode getAckMode()
/*     */     {
/* 669 */       return this.ackMode;
/*     */     }
/*     */     
/*     */     public void setAckMode(AbstractMessageListenerContainer.AckMode ackMode) {
/* 673 */       this.ackMode = ackMode;
/*     */     }
/*     */     
/*     */     public Integer getConcurrency() {
/* 677 */       return this.concurrency;
/*     */     }
/*     */     
/*     */     public void setConcurrency(Integer concurrency) {
/* 681 */       this.concurrency = concurrency;
/*     */     }
/*     */     
/*     */     public Long getPollTimeout() {
/* 685 */       return this.pollTimeout;
/*     */     }
/*     */     
/*     */     public void setPollTimeout(Long pollTimeout) {
/* 689 */       this.pollTimeout = pollTimeout;
/*     */     }
/*     */     
/*     */     public Integer getAckCount() {
/* 693 */       return this.ackCount;
/*     */     }
/*     */     
/*     */     public void setAckCount(Integer ackCount) {
/* 697 */       this.ackCount = ackCount;
/*     */     }
/*     */     
/*     */     public Long getAckTime() {
/* 701 */       return this.ackTime;
/*     */     }
/*     */     
/*     */     public void setAckTime(Long ackTime) {
/* 705 */       this.ackTime = ackTime;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Ssl
/*     */   {
/*     */     private String keyPassword;
/*     */     
/*     */ 
/*     */ 
/*     */     private Resource keystoreLocation;
/*     */     
/*     */ 
/*     */ 
/*     */     private String keystorePassword;
/*     */     
/*     */ 
/*     */ 
/*     */     private Resource truststoreLocation;
/*     */     
/*     */ 
/*     */ 
/*     */     private String truststorePassword;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getKeyPassword()
/*     */     {
/* 738 */       return this.keyPassword;
/*     */     }
/*     */     
/*     */     public void setKeyPassword(String keyPassword) {
/* 742 */       this.keyPassword = keyPassword;
/*     */     }
/*     */     
/*     */     public Resource getKeystoreLocation() {
/* 746 */       return this.keystoreLocation;
/*     */     }
/*     */     
/*     */     public void setKeystoreLocation(Resource keystoreLocation) {
/* 750 */       this.keystoreLocation = keystoreLocation;
/*     */     }
/*     */     
/*     */     public String getKeystorePassword() {
/* 754 */       return this.keystorePassword;
/*     */     }
/*     */     
/*     */     public void setKeystorePassword(String keystorePassword) {
/* 758 */       this.keystorePassword = keystorePassword;
/*     */     }
/*     */     
/*     */     public Resource getTruststoreLocation() {
/* 762 */       return this.truststoreLocation;
/*     */     }
/*     */     
/*     */     public void setTruststoreLocation(Resource truststoreLocation) {
/* 766 */       this.truststoreLocation = truststoreLocation;
/*     */     }
/*     */     
/*     */     public String getTruststorePassword() {
/* 770 */       return this.truststorePassword;
/*     */     }
/*     */     
/*     */     public void setTruststorePassword(String truststorePassword) {
/* 774 */       this.truststorePassword = truststorePassword;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\kafka\KafkaProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */