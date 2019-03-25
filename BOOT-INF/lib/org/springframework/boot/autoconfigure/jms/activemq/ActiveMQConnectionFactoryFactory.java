/*     */ package org.springframework.boot.autoconfigure.jms.activemq;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.activemq.ActiveMQConnectionFactory;
/*     */ import org.springframework.util.Assert;
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
/*     */ class ActiveMQConnectionFactoryFactory
/*     */ {
/*     */   private static final String DEFAULT_EMBEDDED_BROKER_URL = "vm://localhost?broker.persistent=false";
/*     */   private static final String DEFAULT_NETWORK_BROKER_URL = "tcp://localhost:61616";
/*     */   private final ActiveMQProperties properties;
/*     */   private final List<ActiveMQConnectionFactoryCustomizer> factoryCustomizers;
/*     */   
/*     */   ActiveMQConnectionFactoryFactory(ActiveMQProperties properties, List<ActiveMQConnectionFactoryCustomizer> factoryCustomizers)
/*     */   {
/*  49 */     Assert.notNull(properties, "Properties must not be null");
/*  50 */     this.properties = properties;
/*  51 */     this.factoryCustomizers = (factoryCustomizers != null ? factoryCustomizers : 
/*  52 */       Collections.emptyList());
/*     */   }
/*     */   
/*     */   public <T extends ActiveMQConnectionFactory> T createConnectionFactory(Class<T> factoryClass)
/*     */   {
/*     */     try {
/*  58 */       return doCreateConnectionFactory(factoryClass);
/*     */     }
/*     */     catch (Exception ex) {
/*  61 */       throw new IllegalStateException("Unable to create ActiveMQConnectionFactory", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private <T extends ActiveMQConnectionFactory> T doCreateConnectionFactory(Class<T> factoryClass)
/*     */     throws Exception
/*     */   {
/*  68 */     T factory = createConnectionFactoryInstance(factoryClass);
/*  69 */     factory.setCloseTimeout(this.properties.getCloseTimeout());
/*  70 */     factory.setNonBlockingRedelivery(this.properties.isNonBlockingRedelivery());
/*  71 */     factory.setSendTimeout(this.properties.getSendTimeout());
/*  72 */     ActiveMQProperties.Packages packages = this.properties.getPackages();
/*  73 */     if (packages.getTrustAll() != null) {
/*  74 */       factory.setTrustAllPackages(packages.getTrustAll().booleanValue());
/*     */     }
/*  76 */     if (!packages.getTrusted().isEmpty()) {
/*  77 */       factory.setTrustedPackages(packages.getTrusted());
/*     */     }
/*  79 */     customize(factory);
/*  80 */     return factory;
/*     */   }
/*     */   
/*     */   private <T extends ActiveMQConnectionFactory> T createConnectionFactoryInstance(Class<T> factoryClass)
/*     */     throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
/*     */   {
/*  86 */     String brokerUrl = determineBrokerUrl();
/*  87 */     String user = this.properties.getUser();
/*  88 */     String password = this.properties.getPassword();
/*  89 */     if ((StringUtils.hasLength(user)) && (StringUtils.hasLength(password))) {
/*  90 */       return 
/*  91 */         (ActiveMQConnectionFactory)factoryClass.getConstructor(new Class[] { String.class, String.class, String.class }).newInstance(new Object[] { user, password, brokerUrl });
/*     */     }
/*  93 */     return (ActiveMQConnectionFactory)factoryClass.getConstructor(new Class[] { String.class }).newInstance(new Object[] { brokerUrl });
/*     */   }
/*     */   
/*     */   private void customize(ActiveMQConnectionFactory connectionFactory) {
/*  97 */     for (ActiveMQConnectionFactoryCustomizer factoryCustomizer : this.factoryCustomizers) {
/*  98 */       factoryCustomizer.customize(connectionFactory);
/*     */     }
/*     */   }
/*     */   
/*     */   String determineBrokerUrl() {
/* 103 */     if (this.properties.getBrokerUrl() != null) {
/* 104 */       return this.properties.getBrokerUrl();
/*     */     }
/* 106 */     if (this.properties.isInMemory()) {
/* 107 */       return "vm://localhost?broker.persistent=false";
/*     */     }
/* 109 */     return "tcp://localhost:61616";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jms\activemq\ActiveMQConnectionFactoryFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */