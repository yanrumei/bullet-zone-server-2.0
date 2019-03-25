/*     */ package org.springframework.jmx.support;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import javax.management.InstanceAlreadyExistsException;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.JMException;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectInstance;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.Constants;
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
/*     */ public class MBeanRegistrationSupport
/*     */ {
/*     */   @Deprecated
/*     */   public static final int REGISTRATION_FAIL_ON_EXISTING = 0;
/*     */   @Deprecated
/*     */   public static final int REGISTRATION_IGNORE_EXISTING = 1;
/*     */   @Deprecated
/*     */   public static final int REGISTRATION_REPLACE_EXISTING = 2;
/* 100 */   private static final Constants constants = new Constants(MBeanRegistrationSupport.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 105 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MBeanServer server;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 115 */   private final Set<ObjectName> registeredBeans = new LinkedHashSet();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 121 */   private RegistrationPolicy registrationPolicy = RegistrationPolicy.FAIL_ON_EXISTING;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setServer(MBeanServer server)
/*     */   {
/* 130 */     this.server = server;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final MBeanServer getServer()
/*     */   {
/* 137 */     return this.server;
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
/*     */   @Deprecated
/*     */   public void setRegistrationBehaviorName(String registrationBehavior)
/*     */   {
/* 151 */     setRegistrationBehavior(constants.asNumber(registrationBehavior).intValue());
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
/*     */   @Deprecated
/*     */   public void setRegistrationBehavior(int registrationBehavior)
/*     */   {
/* 166 */     setRegistrationPolicy(RegistrationPolicy.valueOf(registrationBehavior));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRegistrationPolicy(RegistrationPolicy registrationPolicy)
/*     */   {
/* 176 */     Assert.notNull(registrationPolicy, "RegistrationPolicy must not be null");
/* 177 */     this.registrationPolicy = registrationPolicy;
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
/*     */   protected void doRegister(Object mbean, ObjectName objectName)
/*     */     throws JMException
/*     */   {
/* 192 */     synchronized (this.registeredBeans) {
/* 193 */       ObjectInstance registeredBean = null;
/*     */       try {
/* 195 */         registeredBean = this.server.registerMBean(mbean, objectName);
/*     */       }
/*     */       catch (InstanceAlreadyExistsException ex) {
/* 198 */         if (this.registrationPolicy == RegistrationPolicy.IGNORE_EXISTING) {
/* 199 */           if (this.logger.isDebugEnabled()) {
/* 200 */             this.logger.debug("Ignoring existing MBean at [" + objectName + "]");
/*     */           }
/*     */         }
/* 203 */         else if (this.registrationPolicy == RegistrationPolicy.REPLACE_EXISTING) {
/*     */           try {
/* 205 */             if (this.logger.isDebugEnabled()) {
/* 206 */               this.logger.debug("Replacing existing MBean at [" + objectName + "]");
/*     */             }
/* 208 */             this.server.unregisterMBean(objectName);
/* 209 */             registeredBean = this.server.registerMBean(mbean, objectName);
/*     */           }
/*     */           catch (InstanceNotFoundException ex2) {
/* 212 */             this.logger.error("Unable to replace existing MBean at [" + objectName + "]", ex2);
/* 213 */             throw ex;
/*     */           }
/*     */           
/*     */         } else {
/* 217 */           throw ex;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 222 */       ObjectName actualObjectName = registeredBean != null ? registeredBean.getObjectName() : null;
/* 223 */       if (actualObjectName == null) {
/* 224 */         actualObjectName = objectName;
/*     */       }
/* 226 */       this.registeredBeans.add(actualObjectName);
/*     */     }
/*     */     ObjectName actualObjectName;
/* 229 */     onRegister(actualObjectName, mbean);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void unregisterBeans()
/*     */   {
/*     */     Set<ObjectName> snapshot;
/*     */     
/* 237 */     synchronized (this.registeredBeans) {
/* 238 */       snapshot = new LinkedHashSet(this.registeredBeans); }
/*     */     Set<ObjectName> snapshot;
/* 240 */     if (!snapshot.isEmpty()) {
/* 241 */       this.logger.info("Unregistering JMX-exposed beans");
/*     */     }
/* 243 */     for (??? = snapshot.iterator(); ((Iterator)???).hasNext();) { ObjectName objectName = (ObjectName)((Iterator)???).next();
/* 244 */       doUnregister(objectName);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doUnregister(ObjectName objectName)
/*     */   {
/* 253 */     boolean actuallyUnregistered = false;
/*     */     
/* 255 */     synchronized (this.registeredBeans) {
/* 256 */       if (this.registeredBeans.remove(objectName)) {
/*     */         try
/*     */         {
/* 259 */           if (this.server.isRegistered(objectName)) {
/* 260 */             this.server.unregisterMBean(objectName);
/* 261 */             actuallyUnregistered = true;
/*     */ 
/*     */           }
/* 264 */           else if (this.logger.isWarnEnabled()) {
/* 265 */             this.logger.warn("Could not unregister MBean [" + objectName + "] as said MBean is not registered (perhaps already unregistered by an external process)");
/*     */           }
/*     */           
/*     */         }
/*     */         catch (JMException ex)
/*     */         {
/* 271 */           if (this.logger.isErrorEnabled()) {
/* 272 */             this.logger.error("Could not unregister MBean [" + objectName + "]", ex);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 278 */     if (actuallyUnregistered) {
/* 279 */       onUnregister(objectName);
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected final ObjectName[] getRegisteredObjectNames()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 7	org/springframework/jmx/support/MBeanRegistrationSupport:registeredBeans	Ljava/util/Set;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 7	org/springframework/jmx/support/MBeanRegistrationSupport:registeredBeans	Ljava/util/Set;
/*     */     //   11: aload_0
/*     */     //   12: getfield 7	org/springframework/jmx/support/MBeanRegistrationSupport:registeredBeans	Ljava/util/Set;
/*     */     //   15: invokeinterface 58 1 0
/*     */     //   20: anewarray 47	javax/management/ObjectName
/*     */     //   23: invokeinterface 59 2 0
/*     */     //   28: checkcast 60	[Ljavax/management/ObjectName;
/*     */     //   31: aload_1
/*     */     //   32: monitorexit
/*     */     //   33: areturn
/*     */     //   34: astore_2
/*     */     //   35: aload_1
/*     */     //   36: monitorexit
/*     */     //   37: aload_2
/*     */     //   38: athrow
/*     */     // Line number table:
/*     */     //   Java source line #287	-> byte code offset #0
/*     */     //   Java source line #288	-> byte code offset #7
/*     */     //   Java source line #289	-> byte code offset #34
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	39	0	this	MBeanRegistrationSupport
/*     */     //   5	31	1	Ljava/lang/Object;	Object
/*     */     //   34	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	33	34	finally
/*     */     //   34	37	34	finally
/*     */   }
/*     */   
/*     */   protected void onRegister(ObjectName objectName, Object mbean)
/*     */   {
/* 301 */     onRegister(objectName);
/*     */   }
/*     */   
/*     */   protected void onRegister(ObjectName objectName) {}
/*     */   
/*     */   protected void onUnregister(ObjectName objectName) {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jmx\support\MBeanRegistrationSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */