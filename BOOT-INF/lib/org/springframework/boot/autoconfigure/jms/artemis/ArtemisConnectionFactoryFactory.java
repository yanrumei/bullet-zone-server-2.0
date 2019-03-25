/*     */ package org.springframework.boot.autoconfigure.jms.artemis;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.activemq.artemis.api.core.TransportConfiguration;
/*     */ import org.apache.activemq.artemis.api.core.client.ActiveMQClient;
/*     */ import org.apache.activemq.artemis.api.core.client.ServerLocator;
/*     */ import org.apache.activemq.artemis.core.remoting.impl.invm.InVMConnectorFactory;
/*     */ import org.apache.activemq.artemis.core.remoting.impl.netty.NettyConnectorFactory;
/*     */ import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ class ArtemisConnectionFactoryFactory
/*     */ {
/*     */   static final String EMBEDDED_JMS_CLASS = "org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS";
/*     */   private final ArtemisProperties properties;
/*     */   private final ListableBeanFactory beanFactory;
/*     */   
/*     */   ArtemisConnectionFactoryFactory(ListableBeanFactory beanFactory, ArtemisProperties properties)
/*     */   {
/*  54 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/*  55 */     Assert.notNull(properties, "Properties must not be null");
/*  56 */     this.beanFactory = beanFactory;
/*  57 */     this.properties = properties;
/*     */   }
/*     */   
/*     */   public <T extends ActiveMQConnectionFactory> T createConnectionFactory(Class<T> factoryClass)
/*     */   {
/*     */     try {
/*  63 */       startEmbeddedJms();
/*  64 */       return doCreateConnectionFactory(factoryClass);
/*     */     }
/*     */     catch (Exception ex) {
/*  67 */       throw new IllegalStateException("Unable to create ActiveMQConnectionFactory", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private void startEmbeddedJms()
/*     */   {
/*  73 */     if (ClassUtils.isPresent("org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS", null)) {
/*     */       try {
/*  75 */         this.beanFactory.getBeansOfType(Class.forName("org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS"));
/*     */       }
/*     */       catch (Exception localException) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private <T extends ActiveMQConnectionFactory> T doCreateConnectionFactory(Class<T> factoryClass)
/*     */     throws Exception
/*     */   {
/*  85 */     ArtemisMode mode = this.properties.getMode();
/*  86 */     if (mode == null) {
/*  87 */       mode = deduceMode();
/*     */     }
/*  89 */     if (mode == ArtemisMode.EMBEDDED) {
/*  90 */       return createEmbeddedConnectionFactory(factoryClass);
/*     */     }
/*  92 */     return createNativeConnectionFactory(factoryClass);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ArtemisMode deduceMode()
/*     */   {
/* 100 */     if ((this.properties.getEmbedded().isEnabled()) && 
/* 101 */       (ClassUtils.isPresent("org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS", null))) {
/* 102 */       return ArtemisMode.EMBEDDED;
/*     */     }
/* 104 */     return ArtemisMode.NATIVE;
/*     */   }
/*     */   
/*     */   private <T extends ActiveMQConnectionFactory> T createEmbeddedConnectionFactory(Class<T> factoryClass)
/*     */     throws Exception
/*     */   {
/*     */     try
/*     */     {
/* 112 */       TransportConfiguration transportConfiguration = new TransportConfiguration(InVMConnectorFactory.class.getName(), this.properties.getEmbedded().generateTransportParameters());
/*     */       
/* 114 */       ServerLocator serviceLocator = ActiveMQClient.createServerLocatorWithoutHA(new TransportConfiguration[] { transportConfiguration });
/* 115 */       return 
/* 116 */         (ActiveMQConnectionFactory)factoryClass.getConstructor(new Class[] { ServerLocator.class }).newInstance(new Object[] { serviceLocator });
/*     */     }
/*     */     catch (NoClassDefFoundError ex) {
/* 119 */       throw new IllegalStateException("Unable to create InVM Artemis connection, ensure that artemis-jms-server.jar is in the classpath", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private <T extends ActiveMQConnectionFactory> T createNativeConnectionFactory(Class<T> factoryClass)
/*     */     throws Exception
/*     */   {
/* 127 */     Map<String, Object> params = new HashMap();
/* 128 */     params.put("host", this.properties.getHost());
/* 129 */     params.put("port", Integer.valueOf(this.properties.getPort()));
/*     */     
/* 131 */     TransportConfiguration transportConfiguration = new TransportConfiguration(NettyConnectorFactory.class.getName(), params);
/* 132 */     Constructor<T> constructor = factoryClass.getConstructor(new Class[] { Boolean.TYPE, TransportConfiguration[].class });
/*     */     
/* 134 */     T connectionFactory = (ActiveMQConnectionFactory)constructor.newInstance(new Object[] { Boolean.valueOf(false), { transportConfiguration } });
/*     */     
/* 136 */     String user = this.properties.getUser();
/* 137 */     if (StringUtils.hasText(user)) {
/* 138 */       connectionFactory.setUser(user);
/* 139 */       connectionFactory.setPassword(this.properties.getPassword());
/*     */     }
/* 141 */     return connectionFactory;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jms\artemis\ArtemisConnectionFactoryFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */