/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanCurrentlyInCreationException;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.FactoryBeanNotInitializedException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class FactoryBeanRegistrySupport
/*     */   extends DefaultSingletonBeanRegistry
/*     */ {
/*  46 */   private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap(16);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Class<?> getTypeForFactoryBean(final FactoryBean<?> factoryBean)
/*     */   {
/*     */     try
/*     */     {
/*  57 */       if (System.getSecurityManager() != null) {
/*  58 */         (Class)AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Class<?> run() {
/*  61 */             return factoryBean.getObjectType();
/*     */           }
/*  63 */         }, getAccessControlContext());
/*     */       }
/*     */       
/*  66 */       return factoryBean.getObjectType();
/*     */ 
/*     */     }
/*     */     catch (Throwable ex)
/*     */     {
/*  71 */       this.logger.warn("FactoryBean threw exception from getObjectType, despite the contract saying that it should return null if the type of its object cannot be determined yet", ex);
/*     */     }
/*  73 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object getCachedObjectForFactoryBean(String beanName)
/*     */   {
/*  85 */     Object object = this.factoryBeanObjectCache.get(beanName);
/*  86 */     return object != NULL_OBJECT ? object : null;
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
/*     */   protected Object getObjectFromFactoryBean(FactoryBean<?> factory, String beanName, boolean shouldPostProcess)
/*     */   {
/*  99 */     if ((factory.isSingleton()) && (containsSingleton(beanName))) {
/* 100 */       synchronized (getSingletonMutex()) {
/* 101 */         Object object = this.factoryBeanObjectCache.get(beanName);
/* 102 */         if (object == null) {
/* 103 */           object = doGetObjectFromFactoryBean(factory, beanName);
/*     */           
/*     */ 
/* 106 */           Object alreadyThere = this.factoryBeanObjectCache.get(beanName);
/* 107 */           if (alreadyThere != null) {
/* 108 */             object = alreadyThere;
/*     */           }
/*     */           else {
/* 111 */             if ((object != null) && (shouldPostProcess)) {
/*     */               try {
/* 113 */                 object = postProcessObjectFromFactoryBean(object, beanName);
/*     */               }
/*     */               catch (Throwable ex) {
/* 116 */                 throw new BeanCreationException(beanName, "Post-processing of FactoryBean's singleton object failed", ex);
/*     */               }
/*     */             }
/*     */             
/* 120 */             this.factoryBeanObjectCache.put(beanName, object != null ? object : NULL_OBJECT);
/*     */           }
/*     */         }
/* 123 */         return object != NULL_OBJECT ? object : null;
/*     */       }
/*     */     }
/*     */     
/* 127 */     Object object = doGetObjectFromFactoryBean(factory, beanName);
/* 128 */     if ((object != null) && (shouldPostProcess)) {
/*     */       try {
/* 130 */         object = postProcessObjectFromFactoryBean(object, beanName);
/*     */       }
/*     */       catch (Throwable ex) {
/* 133 */         throw new BeanCreationException(beanName, "Post-processing of FactoryBean's object failed", ex);
/*     */       }
/*     */     }
/* 136 */     return object;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Object doGetObjectFromFactoryBean(final FactoryBean<?> factory, String beanName)
/*     */     throws BeanCreationException
/*     */   {
/*     */     try
/*     */     {
/*     */       Object object;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 153 */       if (System.getSecurityManager() != null) {
/* 154 */         AccessControlContext acc = getAccessControlContext();
/*     */         try {
/* 156 */           object = AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */           {
/*     */ 
/* 159 */             public Object run() throws Exception { return factory.getObject(); } }, acc);
/*     */         }
/*     */         catch (PrivilegedActionException pae)
/*     */         {
/*     */           Object object;
/* 164 */           throw pae.getException();
/*     */         }
/*     */       }
/*     */       else {
/* 168 */         object = factory.getObject();
/*     */       }
/*     */     } catch (FactoryBeanNotInitializedException ex) {
/*     */       Object object;
/* 172 */       throw new BeanCurrentlyInCreationException(beanName, ex.toString());
/*     */     }
/*     */     catch (Throwable ex) {
/* 175 */       throw new BeanCreationException(beanName, "FactoryBean threw exception on object creation", ex);
/*     */     }
/*     */     
/*     */     Object object;
/*     */     
/* 180 */     if ((object == null) && (isSingletonCurrentlyInCreation(beanName))) {
/* 181 */       throw new BeanCurrentlyInCreationException(beanName, "FactoryBean which is currently in creation returned null from getObject");
/*     */     }
/*     */     
/* 184 */     return object;
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
/*     */   protected Object postProcessObjectFromFactoryBean(Object object, String beanName)
/*     */     throws BeansException
/*     */   {
/* 198 */     return object;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected FactoryBean<?> getFactoryBean(String beanName, Object beanInstance)
/*     */     throws BeansException
/*     */   {
/* 209 */     if (!(beanInstance instanceof FactoryBean))
/*     */     {
/* 211 */       throw new BeanCreationException(beanName, "Bean instance of type [" + beanInstance.getClass() + "] is not a FactoryBean");
/*     */     }
/* 213 */     return (FactoryBean)beanInstance;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void removeSingleton(String beanName)
/*     */   {
/* 221 */     super.removeSingleton(beanName);
/* 222 */     this.factoryBeanObjectCache.remove(beanName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void destroySingletons()
/*     */   {
/* 230 */     super.destroySingletons();
/* 231 */     this.factoryBeanObjectCache.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AccessControlContext getAccessControlContext()
/*     */   {
/* 241 */     return AccessController.getContext();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\support\FactoryBeanRegistrySupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */