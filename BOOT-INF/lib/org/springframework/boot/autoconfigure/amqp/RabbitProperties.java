/*     */ package org.springframework.boot.autoconfigure.amqp;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.amqp.core.AcknowledgeMode;
/*     */ import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.CacheMode;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ import org.springframework.boot.context.properties.DeprecatedConfigurationProperty;
/*     */ import org.springframework.boot.context.properties.NestedConfigurationProperty;
/*     */ import org.springframework.util.CollectionUtils;
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
/*     */ @ConfigurationProperties(prefix="spring.rabbitmq")
/*     */ public class RabbitProperties
/*     */ {
/*  46 */   private String host = "localhost";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  51 */   private int port = 5672;
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
/*  66 */   private final Ssl ssl = new Ssl();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String virtualHost;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String addresses;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Integer requestedHeartbeat;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean publisherConfirms;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean publisherReturns;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Integer connectionTimeout;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 101 */   private final Cache cache = new Cache();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 106 */   private final Listener listener = new Listener();
/*     */   
/* 108 */   private final Template template = new Template();
/*     */   private List<Address> parsedAddresses;
/*     */   
/*     */   public String getHost()
/*     */   {
/* 113 */     return this.host;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String determineHost()
/*     */   {
/* 124 */     if (CollectionUtils.isEmpty(this.parsedAddresses)) {
/* 125 */       return getHost();
/*     */     }
/* 127 */     return ((Address)this.parsedAddresses.get(0)).host;
/*     */   }
/*     */   
/*     */   public void setHost(String host) {
/* 131 */     this.host = host;
/*     */   }
/*     */   
/*     */   public int getPort() {
/* 135 */     return this.port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int determinePort()
/*     */   {
/* 146 */     if (CollectionUtils.isEmpty(this.parsedAddresses)) {
/* 147 */       return getPort();
/*     */     }
/* 149 */     Address address = (Address)this.parsedAddresses.get(0);
/* 150 */     return address.port;
/*     */   }
/*     */   
/*     */   public void setPort(int port) {
/* 154 */     this.port = port;
/*     */   }
/*     */   
/*     */   public String getAddresses() {
/* 158 */     return this.addresses;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String determineAddresses()
/*     */   {
/* 167 */     if (CollectionUtils.isEmpty(this.parsedAddresses)) {
/* 168 */       return this.host + ":" + this.port;
/*     */     }
/* 170 */     List<String> addressStrings = new ArrayList();
/* 171 */     for (Address parsedAddress : this.parsedAddresses) {
/* 172 */       addressStrings.add(parsedAddress.host + ":" + parsedAddress.port);
/*     */     }
/* 174 */     return StringUtils.collectionToCommaDelimitedString(addressStrings);
/*     */   }
/*     */   
/*     */   public void setAddresses(String addresses) {
/* 178 */     this.addresses = addresses;
/* 179 */     this.parsedAddresses = parseAddresses(addresses);
/*     */   }
/*     */   
/*     */   private List<Address> parseAddresses(String addresses) {
/* 183 */     List<Address> parsedAddresses = new ArrayList();
/* 184 */     for (String address : StringUtils.commaDelimitedListToStringArray(addresses)) {
/* 185 */       parsedAddresses.add(new Address(address, null));
/*     */     }
/* 187 */     return parsedAddresses;
/*     */   }
/*     */   
/*     */   public String getUsername() {
/* 191 */     return this.username;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String determineUsername()
/*     */   {
/* 202 */     if (CollectionUtils.isEmpty(this.parsedAddresses)) {
/* 203 */       return this.username;
/*     */     }
/* 205 */     Address address = (Address)this.parsedAddresses.get(0);
/* 206 */     return address.username == null ? this.username : address.username;
/*     */   }
/*     */   
/*     */   public void setUsername(String username) {
/* 210 */     this.username = username;
/*     */   }
/*     */   
/*     */   public String getPassword() {
/* 214 */     return this.password;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String determinePassword()
/*     */   {
/* 225 */     if (CollectionUtils.isEmpty(this.parsedAddresses)) {
/* 226 */       return getPassword();
/*     */     }
/* 228 */     Address address = (Address)this.parsedAddresses.get(0);
/* 229 */     return address.password == null ? getPassword() : address.password;
/*     */   }
/*     */   
/*     */   public void setPassword(String password) {
/* 233 */     this.password = password;
/*     */   }
/*     */   
/*     */   public Ssl getSsl() {
/* 237 */     return this.ssl;
/*     */   }
/*     */   
/*     */   public String getVirtualHost() {
/* 241 */     return this.virtualHost;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String determineVirtualHost()
/*     */   {
/* 252 */     if (CollectionUtils.isEmpty(this.parsedAddresses)) {
/* 253 */       return getVirtualHost();
/*     */     }
/* 255 */     Address address = (Address)this.parsedAddresses.get(0);
/* 256 */     return address.virtualHost == null ? getVirtualHost() : address.virtualHost;
/*     */   }
/*     */   
/*     */   public void setVirtualHost(String virtualHost) {
/* 260 */     this.virtualHost = ("".equals(virtualHost) ? "/" : virtualHost);
/*     */   }
/*     */   
/*     */   public Integer getRequestedHeartbeat() {
/* 264 */     return this.requestedHeartbeat;
/*     */   }
/*     */   
/*     */   public void setRequestedHeartbeat(Integer requestedHeartbeat) {
/* 268 */     this.requestedHeartbeat = requestedHeartbeat;
/*     */   }
/*     */   
/*     */   public boolean isPublisherConfirms() {
/* 272 */     return this.publisherConfirms;
/*     */   }
/*     */   
/*     */   public void setPublisherConfirms(boolean publisherConfirms) {
/* 276 */     this.publisherConfirms = publisherConfirms;
/*     */   }
/*     */   
/*     */   public boolean isPublisherReturns() {
/* 280 */     return this.publisherReturns;
/*     */   }
/*     */   
/*     */   public void setPublisherReturns(boolean publisherReturns) {
/* 284 */     this.publisherReturns = publisherReturns;
/*     */   }
/*     */   
/*     */   public Integer getConnectionTimeout() {
/* 288 */     return this.connectionTimeout;
/*     */   }
/*     */   
/*     */   public void setConnectionTimeout(Integer connectionTimeout) {
/* 292 */     this.connectionTimeout = connectionTimeout;
/*     */   }
/*     */   
/*     */   public Cache getCache() {
/* 296 */     return this.cache;
/*     */   }
/*     */   
/*     */   public Listener getListener() {
/* 300 */     return this.listener;
/*     */   }
/*     */   
/*     */   public Template getTemplate() {
/* 304 */     return this.template;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Ssl
/*     */   {
/*     */     private boolean enabled;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private String keyStore;
/*     */     
/*     */ 
/*     */ 
/*     */     private String keyStorePassword;
/*     */     
/*     */ 
/*     */ 
/*     */     private String trustStore;
/*     */     
/*     */ 
/*     */ 
/*     */     private String trustStorePassword;
/*     */     
/*     */ 
/*     */ 
/*     */     private String algorithm;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isEnabled()
/*     */     {
/* 341 */       return this.enabled;
/*     */     }
/*     */     
/*     */     public void setEnabled(boolean enabled) {
/* 345 */       this.enabled = enabled;
/*     */     }
/*     */     
/*     */     public String getKeyStore() {
/* 349 */       return this.keyStore;
/*     */     }
/*     */     
/*     */     public void setKeyStore(String keyStore) {
/* 353 */       this.keyStore = keyStore;
/*     */     }
/*     */     
/*     */     public String getKeyStorePassword() {
/* 357 */       return this.keyStorePassword;
/*     */     }
/*     */     
/*     */     public void setKeyStorePassword(String keyStorePassword) {
/* 361 */       this.keyStorePassword = keyStorePassword;
/*     */     }
/*     */     
/*     */     public String getTrustStore() {
/* 365 */       return this.trustStore;
/*     */     }
/*     */     
/*     */     public void setTrustStore(String trustStore) {
/* 369 */       this.trustStore = trustStore;
/*     */     }
/*     */     
/*     */     public String getTrustStorePassword() {
/* 373 */       return this.trustStorePassword;
/*     */     }
/*     */     
/*     */     public void setTrustStorePassword(String trustStorePassword) {
/* 377 */       this.trustStorePassword = trustStorePassword;
/*     */     }
/*     */     
/*     */     public String getAlgorithm() {
/* 381 */       return this.algorithm;
/*     */     }
/*     */     
/*     */     public void setAlgorithm(String sslAlgorithm) {
/* 385 */       this.algorithm = sslAlgorithm;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Cache
/*     */   {
/* 392 */     private final Channel channel = new Channel();
/*     */     
/* 394 */     private final Connection connection = new Connection();
/*     */     
/*     */     public Channel getChannel() {
/* 397 */       return this.channel;
/*     */     }
/*     */     
/*     */     public Connection getConnection() {
/* 401 */       return this.connection;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public static class Channel
/*     */     {
/*     */       private Integer size;
/*     */       
/*     */ 
/*     */ 
/*     */       private Long checkoutTimeout;
/*     */       
/*     */ 
/*     */ 
/*     */       public Integer getSize()
/*     */       {
/* 419 */         return this.size;
/*     */       }
/*     */       
/*     */       public void setSize(Integer size) {
/* 423 */         this.size = size;
/*     */       }
/*     */       
/*     */       public Long getCheckoutTimeout() {
/* 427 */         return this.checkoutTimeout;
/*     */       }
/*     */       
/*     */       public void setCheckoutTimeout(Long checkoutTimeout) {
/* 431 */         this.checkoutTimeout = checkoutTimeout;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public static class Connection
/*     */     {
/* 441 */       private CachingConnectionFactory.CacheMode mode = CachingConnectionFactory.CacheMode.CHANNEL;
/*     */       
/*     */ 
/*     */       private Integer size;
/*     */       
/*     */ 
/*     */       public CachingConnectionFactory.CacheMode getMode()
/*     */       {
/* 449 */         return this.mode;
/*     */       }
/*     */       
/*     */       public void setMode(CachingConnectionFactory.CacheMode mode) {
/* 453 */         this.mode = mode;
/*     */       }
/*     */       
/*     */       public Integer getSize() {
/* 457 */         return this.size;
/*     */       }
/*     */       
/*     */       public void setSize(Integer size) {
/* 461 */         this.size = size;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Listener
/*     */   {
/*     */     @NestedConfigurationProperty
/* 470 */     private final RabbitProperties.AmqpContainer simple = new RabbitProperties.AmqpContainer();
/*     */     
/*     */     @DeprecatedConfigurationProperty(replacement="spring.rabbitmq.listener.simple.auto-startup")
/*     */     @Deprecated
/*     */     public boolean isAutoStartup()
/*     */     {
/* 476 */       return getSimple().isAutoStartup();
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public void setAutoStartup(boolean autoStartup) {
/* 481 */       getSimple().setAutoStartup(autoStartup);
/*     */     }
/*     */     
/*     */     @DeprecatedConfigurationProperty(replacement="spring.rabbitmq.listener.simple.acknowledge-mode")
/*     */     @Deprecated
/*     */     public AcknowledgeMode getAcknowledgeMode() {
/* 487 */       return getSimple().getAcknowledgeMode();
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public void setAcknowledgeMode(AcknowledgeMode acknowledgeMode) {
/* 492 */       getSimple().setAcknowledgeMode(acknowledgeMode);
/*     */     }
/*     */     
/*     */     @DeprecatedConfigurationProperty(replacement="spring.rabbitmq.listener.simple.concurrency")
/*     */     @Deprecated
/*     */     public Integer getConcurrency() {
/* 498 */       return getSimple().getConcurrency();
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public void setConcurrency(Integer concurrency) {
/* 503 */       getSimple().setConcurrency(concurrency);
/*     */     }
/*     */     
/*     */     @DeprecatedConfigurationProperty(replacement="spring.rabbitmq.listener.simple.max-concurrency")
/*     */     @Deprecated
/*     */     public Integer getMaxConcurrency() {
/* 509 */       return getSimple().getMaxConcurrency();
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public void setMaxConcurrency(Integer maxConcurrency) {
/* 514 */       getSimple().setMaxConcurrency(maxConcurrency);
/*     */     }
/*     */     
/*     */     @DeprecatedConfigurationProperty(replacement="spring.rabbitmq.listener.simple.prefetch")
/*     */     @Deprecated
/*     */     public Integer getPrefetch() {
/* 520 */       return getSimple().getPrefetch();
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public void setPrefetch(Integer prefetch) {
/* 525 */       getSimple().setPrefetch(prefetch);
/*     */     }
/*     */     
/*     */     @DeprecatedConfigurationProperty(replacement="spring.rabbitmq.listener.simple.transaction-size")
/*     */     @Deprecated
/*     */     public Integer getTransactionSize() {
/* 531 */       return getSimple().getTransactionSize();
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public void setTransactionSize(Integer transactionSize) {
/* 536 */       getSimple().setTransactionSize(transactionSize);
/*     */     }
/*     */     
/*     */     @DeprecatedConfigurationProperty(replacement="spring.rabbitmq.listener.simple.default-requeue-rejected")
/*     */     @Deprecated
/*     */     public Boolean getDefaultRequeueRejected() {
/* 542 */       return getSimple().getDefaultRequeueRejected();
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public void setDefaultRequeueRejected(Boolean defaultRequeueRejected) {
/* 547 */       getSimple().setDefaultRequeueRejected(defaultRequeueRejected);
/*     */     }
/*     */     
/*     */     @DeprecatedConfigurationProperty(replacement="spring.rabbitmq.listener.simple.idle-event-interval")
/*     */     @Deprecated
/*     */     public Long getIdleEventInterval() {
/* 553 */       return getSimple().getIdleEventInterval();
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public void setIdleEventInterval(Long idleEventInterval) {
/* 558 */       getSimple().setIdleEventInterval(idleEventInterval);
/*     */     }
/*     */     
/*     */     @DeprecatedConfigurationProperty(replacement="spring.rabbitmq.listener.simple.retry")
/*     */     @Deprecated
/*     */     public RabbitProperties.ListenerRetry getRetry() {
/* 564 */       return getSimple().getRetry();
/*     */     }
/*     */     
/*     */     public RabbitProperties.AmqpContainer getSimple() {
/* 568 */       return this.simple;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class AmqpContainer
/*     */   {
/* 578 */     private boolean autoStartup = true;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private AcknowledgeMode acknowledgeMode;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private Integer concurrency;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private Integer maxConcurrency;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private Integer prefetch;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private Integer transactionSize;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private Boolean defaultRequeueRejected;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private Long idleEventInterval;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     @NestedConfigurationProperty
/* 620 */     private final RabbitProperties.ListenerRetry retry = new RabbitProperties.ListenerRetry();
/*     */     
/*     */     public boolean isAutoStartup()
/*     */     {
/* 624 */       return this.autoStartup;
/*     */     }
/*     */     
/*     */     public void setAutoStartup(boolean autoStartup) {
/* 628 */       this.autoStartup = autoStartup;
/*     */     }
/*     */     
/*     */     public AcknowledgeMode getAcknowledgeMode() {
/* 632 */       return this.acknowledgeMode;
/*     */     }
/*     */     
/*     */     public void setAcknowledgeMode(AcknowledgeMode acknowledgeMode) {
/* 636 */       this.acknowledgeMode = acknowledgeMode;
/*     */     }
/*     */     
/*     */     public Integer getConcurrency() {
/* 640 */       return this.concurrency;
/*     */     }
/*     */     
/*     */     public void setConcurrency(Integer concurrency) {
/* 644 */       this.concurrency = concurrency;
/*     */     }
/*     */     
/*     */     public Integer getMaxConcurrency() {
/* 648 */       return this.maxConcurrency;
/*     */     }
/*     */     
/*     */     public void setMaxConcurrency(Integer maxConcurrency) {
/* 652 */       this.maxConcurrency = maxConcurrency;
/*     */     }
/*     */     
/*     */     public Integer getPrefetch() {
/* 656 */       return this.prefetch;
/*     */     }
/*     */     
/*     */     public void setPrefetch(Integer prefetch) {
/* 660 */       this.prefetch = prefetch;
/*     */     }
/*     */     
/*     */     public Integer getTransactionSize() {
/* 664 */       return this.transactionSize;
/*     */     }
/*     */     
/*     */     public void setTransactionSize(Integer transactionSize) {
/* 668 */       this.transactionSize = transactionSize;
/*     */     }
/*     */     
/*     */     public Boolean getDefaultRequeueRejected() {
/* 672 */       return this.defaultRequeueRejected;
/*     */     }
/*     */     
/*     */     public void setDefaultRequeueRejected(Boolean defaultRequeueRejected) {
/* 676 */       this.defaultRequeueRejected = defaultRequeueRejected;
/*     */     }
/*     */     
/*     */     public Long getIdleEventInterval() {
/* 680 */       return this.idleEventInterval;
/*     */     }
/*     */     
/*     */     public void setIdleEventInterval(Long idleEventInterval) {
/* 684 */       this.idleEventInterval = idleEventInterval;
/*     */     }
/*     */     
/*     */     public RabbitProperties.ListenerRetry getRetry() {
/* 688 */       return this.retry;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Template
/*     */   {
/*     */     @NestedConfigurationProperty
/* 695 */     private final RabbitProperties.Retry retry = new RabbitProperties.Retry();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private Boolean mandatory;
/*     */     
/*     */ 
/*     */ 
/*     */     private Long receiveTimeout;
/*     */     
/*     */ 
/*     */ 
/*     */     private Long replyTimeout;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public RabbitProperties.Retry getRetry()
/*     */     {
/* 715 */       return this.retry;
/*     */     }
/*     */     
/*     */     public Boolean getMandatory() {
/* 719 */       return this.mandatory;
/*     */     }
/*     */     
/*     */     public void setMandatory(Boolean mandatory) {
/* 723 */       this.mandatory = mandatory;
/*     */     }
/*     */     
/*     */     public Long getReceiveTimeout() {
/* 727 */       return this.receiveTimeout;
/*     */     }
/*     */     
/*     */     public void setReceiveTimeout(Long receiveTimeout) {
/* 731 */       this.receiveTimeout = receiveTimeout;
/*     */     }
/*     */     
/*     */     public Long getReplyTimeout() {
/* 735 */       return this.replyTimeout;
/*     */     }
/*     */     
/*     */     public void setReplyTimeout(Long replyTimeout) {
/* 739 */       this.replyTimeout = replyTimeout;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Retry
/*     */   {
/*     */     private boolean enabled;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 754 */     private int maxAttempts = 3;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 759 */     private long initialInterval = 1000L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 764 */     private double multiplier = 1.0D;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 769 */     private long maxInterval = 10000L;
/*     */     
/*     */     public boolean isEnabled() {
/* 772 */       return this.enabled;
/*     */     }
/*     */     
/*     */     public void setEnabled(boolean enabled) {
/* 776 */       this.enabled = enabled;
/*     */     }
/*     */     
/*     */     public int getMaxAttempts() {
/* 780 */       return this.maxAttempts;
/*     */     }
/*     */     
/*     */     public void setMaxAttempts(int maxAttempts) {
/* 784 */       this.maxAttempts = maxAttempts;
/*     */     }
/*     */     
/*     */     public long getInitialInterval() {
/* 788 */       return this.initialInterval;
/*     */     }
/*     */     
/*     */     public void setInitialInterval(long initialInterval) {
/* 792 */       this.initialInterval = initialInterval;
/*     */     }
/*     */     
/*     */     public double getMultiplier() {
/* 796 */       return this.multiplier;
/*     */     }
/*     */     
/*     */     public void setMultiplier(double multiplier) {
/* 800 */       this.multiplier = multiplier;
/*     */     }
/*     */     
/*     */     public long getMaxInterval() {
/* 804 */       return this.maxInterval;
/*     */     }
/*     */     
/*     */     public void setMaxInterval(long maxInterval) {
/* 808 */       this.maxInterval = maxInterval;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class ListenerRetry
/*     */     extends RabbitProperties.Retry
/*     */   {
/* 818 */     private boolean stateless = true;
/*     */     
/*     */     public boolean isStateless() {
/* 821 */       return this.stateless;
/*     */     }
/*     */     
/*     */     public void setStateless(boolean stateless) {
/* 825 */       this.stateless = stateless;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class Address
/*     */   {
/*     */     private static final String PREFIX_AMQP = "amqp://";
/*     */     
/*     */     private static final int DEFAULT_PORT = 5672;
/*     */     
/*     */     private String host;
/*     */     
/*     */     private int port;
/*     */     
/*     */     private String username;
/*     */     
/*     */     private String password;
/*     */     private String virtualHost;
/*     */     
/*     */     private Address(String input)
/*     */     {
/* 847 */       input = input.trim();
/* 848 */       input = trimPrefix(input);
/* 849 */       input = parseUsernameAndPassword(input);
/* 850 */       input = parseVirtualHost(input);
/* 851 */       parseHostAndPort(input);
/*     */     }
/*     */     
/*     */     private String trimPrefix(String input) {
/* 855 */       if (input.startsWith("amqp://")) {
/* 856 */         input = input.substring("amqp://".length());
/*     */       }
/* 858 */       return input;
/*     */     }
/*     */     
/*     */     private String parseUsernameAndPassword(String input) {
/* 862 */       if (input.contains("@")) {
/* 863 */         String[] split = StringUtils.split(input, "@");
/* 864 */         String creds = split[0];
/* 865 */         input = split[1];
/* 866 */         split = StringUtils.split(creds, ":");
/* 867 */         this.username = split[0];
/* 868 */         if (split.length > 0) {
/* 869 */           this.password = split[1];
/*     */         }
/*     */       }
/* 872 */       return input;
/*     */     }
/*     */     
/*     */     private String parseVirtualHost(String input) {
/* 876 */       int hostIndex = input.indexOf("/");
/* 877 */       if (hostIndex >= 0) {
/* 878 */         this.virtualHost = input.substring(hostIndex + 1);
/* 879 */         if (this.virtualHost.isEmpty()) {
/* 880 */           this.virtualHost = "/";
/*     */         }
/* 882 */         input = input.substring(0, hostIndex);
/*     */       }
/* 884 */       return input;
/*     */     }
/*     */     
/*     */     private void parseHostAndPort(String input) {
/* 888 */       int portIndex = input.indexOf(':');
/* 889 */       if (portIndex == -1) {
/* 890 */         this.host = input;
/* 891 */         this.port = 5672;
/*     */       }
/*     */       else {
/* 894 */         this.host = input.substring(0, portIndex);
/* 895 */         this.port = Integer.valueOf(input.substring(portIndex + 1)).intValue();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\amqp\RabbitProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */