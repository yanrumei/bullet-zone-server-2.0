/*     */ package org.springframework.boot.autoconfigure.jms;
/*     */ 
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
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
/*     */ @ConfigurationProperties(prefix="spring.jms")
/*     */ public class JmsProperties
/*     */ {
/*  34 */   private boolean pubSubDomain = false;
/*     */   
/*     */ 
/*     */ 
/*     */   private String jndiName;
/*     */   
/*     */ 
/*     */ 
/*  42 */   private final Listener listener = new Listener();
/*     */   
/*  44 */   private final Template template = new Template();
/*     */   
/*     */   public boolean isPubSubDomain() {
/*  47 */     return this.pubSubDomain;
/*     */   }
/*     */   
/*     */   public void setPubSubDomain(boolean pubSubDomain) {
/*  51 */     this.pubSubDomain = pubSubDomain;
/*     */   }
/*     */   
/*     */   public String getJndiName() {
/*  55 */     return this.jndiName;
/*     */   }
/*     */   
/*     */   public void setJndiName(String jndiName) {
/*  59 */     this.jndiName = jndiName;
/*     */   }
/*     */   
/*     */   public Listener getListener() {
/*  63 */     return this.listener;
/*     */   }
/*     */   
/*     */   public Template getTemplate() {
/*  67 */     return this.template;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Listener
/*     */   {
/*  75 */     private boolean autoStartup = true;
/*     */     
/*     */ 
/*     */ 
/*     */     private JmsProperties.AcknowledgeMode acknowledgeMode;
/*     */     
/*     */ 
/*     */ 
/*     */     private Integer concurrency;
/*     */     
/*     */ 
/*     */ 
/*     */     private Integer maxConcurrency;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isAutoStartup()
/*     */     {
/*  94 */       return this.autoStartup;
/*     */     }
/*     */     
/*     */     public void setAutoStartup(boolean autoStartup) {
/*  98 */       this.autoStartup = autoStartup;
/*     */     }
/*     */     
/*     */     public JmsProperties.AcknowledgeMode getAcknowledgeMode() {
/* 102 */       return this.acknowledgeMode;
/*     */     }
/*     */     
/*     */     public void setAcknowledgeMode(JmsProperties.AcknowledgeMode acknowledgeMode) {
/* 106 */       this.acknowledgeMode = acknowledgeMode;
/*     */     }
/*     */     
/*     */     public Integer getConcurrency() {
/* 110 */       return this.concurrency;
/*     */     }
/*     */     
/*     */     public void setConcurrency(Integer concurrency) {
/* 114 */       this.concurrency = concurrency;
/*     */     }
/*     */     
/*     */     public Integer getMaxConcurrency() {
/* 118 */       return this.maxConcurrency;
/*     */     }
/*     */     
/*     */     public void setMaxConcurrency(Integer maxConcurrency) {
/* 122 */       this.maxConcurrency = maxConcurrency;
/*     */     }
/*     */     
/*     */     public String formatConcurrency() {
/* 126 */       if (this.concurrency == null) {
/* 127 */         return this.maxConcurrency != null ? "1-" + this.maxConcurrency : null;
/*     */       }
/* 129 */       return this.maxConcurrency != null ? this.concurrency + "-" + this.maxConcurrency : 
/*     */       
/* 131 */         String.valueOf(this.concurrency);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Template
/*     */   {
/*     */     private String defaultDestination;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private Long deliveryDelay;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private JmsProperties.DeliveryMode deliveryMode;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private Integer priority;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private Long timeToLive;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private Boolean qosEnabled;
/*     */     
/*     */ 
/*     */ 
/*     */     private Long receiveTimeout;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getDefaultDestination()
/*     */     {
/* 177 */       return this.defaultDestination;
/*     */     }
/*     */     
/*     */     public void setDefaultDestination(String defaultDestination) {
/* 181 */       this.defaultDestination = defaultDestination;
/*     */     }
/*     */     
/*     */     public Long getDeliveryDelay() {
/* 185 */       return this.deliveryDelay;
/*     */     }
/*     */     
/*     */     public void setDeliveryDelay(Long deliveryDelay) {
/* 189 */       this.deliveryDelay = deliveryDelay;
/*     */     }
/*     */     
/*     */     public JmsProperties.DeliveryMode getDeliveryMode() {
/* 193 */       return this.deliveryMode;
/*     */     }
/*     */     
/*     */     public void setDeliveryMode(JmsProperties.DeliveryMode deliveryMode) {
/* 197 */       this.deliveryMode = deliveryMode;
/*     */     }
/*     */     
/*     */     public Integer getPriority() {
/* 201 */       return this.priority;
/*     */     }
/*     */     
/*     */     public void setPriority(Integer priority) {
/* 205 */       this.priority = priority;
/*     */     }
/*     */     
/*     */     public Long getTimeToLive() {
/* 209 */       return this.timeToLive;
/*     */     }
/*     */     
/*     */     public void setTimeToLive(Long timeToLive) {
/* 213 */       this.timeToLive = timeToLive;
/*     */     }
/*     */     
/*     */     public boolean determineQosEnabled() {
/* 217 */       if (this.qosEnabled != null) {
/* 218 */         return this.qosEnabled.booleanValue();
/*     */       }
/* 220 */       return (getDeliveryMode() != null) || (getPriority() != null) || 
/* 221 */         (getTimeToLive() != null);
/*     */     }
/*     */     
/*     */     public Boolean getQosEnabled() {
/* 225 */       return this.qosEnabled;
/*     */     }
/*     */     
/*     */     public void setQosEnabled(Boolean qosEnabled) {
/* 229 */       this.qosEnabled = qosEnabled;
/*     */     }
/*     */     
/*     */     public Long getReceiveTimeout() {
/* 233 */       return this.receiveTimeout;
/*     */     }
/*     */     
/*     */     public void setReceiveTimeout(Long receiveTimeout) {
/* 237 */       this.receiveTimeout = receiveTimeout;
/*     */     }
/*     */   }
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
/*     */   public static enum AcknowledgeMode
/*     */   {
/* 255 */     AUTO(1), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 262 */     CLIENT(2), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 269 */     DUPS_OK(3);
/*     */     
/*     */     private final int mode;
/*     */     
/*     */     private AcknowledgeMode(int mode) {
/* 274 */       this.mode = mode;
/*     */     }
/*     */     
/*     */     public int getMode() {
/* 278 */       return this.mode;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum DeliveryMode
/*     */   {
/* 290 */     NON_PERSISTENT(1), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 296 */     PERSISTENT(2);
/*     */     
/*     */     private final int value;
/*     */     
/*     */     private DeliveryMode(int value) {
/* 301 */       this.value = value;
/*     */     }
/*     */     
/*     */     public int getValue() {
/* 305 */       return this.value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jms\JmsProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */