/*     */ package org.springframework.aop.framework;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import org.springframework.beans.factory.Aware;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProxyProcessorSupport
/*     */   extends ProxyConfig
/*     */   implements Ordered, BeanClassLoaderAware, AopInfrastructureBean
/*     */ {
/*  45 */   private int order = Integer.MAX_VALUE;
/*     */   
/*  47 */   private ClassLoader proxyClassLoader = ClassUtils.getDefaultClassLoader();
/*     */   
/*  49 */   private boolean classLoaderConfigured = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOrder(int order)
/*     */   {
/*  59 */     this.order = order;
/*     */   }
/*     */   
/*     */   public int getOrder()
/*     */   {
/*  64 */     return this.order;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProxyClassLoader(ClassLoader classLoader)
/*     */   {
/*  74 */     this.proxyClassLoader = classLoader;
/*  75 */     this.classLoaderConfigured = (classLoader != null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected ClassLoader getProxyClassLoader()
/*     */   {
/*  82 */     return this.proxyClassLoader;
/*     */   }
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader)
/*     */   {
/*  87 */     if (!this.classLoaderConfigured) {
/*  88 */       this.proxyClassLoader = classLoader;
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
/*     */   protected void evaluateProxyInterfaces(Class<?> beanClass, ProxyFactory proxyFactory)
/*     */   {
/* 102 */     Class<?>[] targetInterfaces = ClassUtils.getAllInterfacesForClass(beanClass, getProxyClassLoader());
/* 103 */     boolean hasReasonableProxyInterface = false;
/* 104 */     for (Class<?> ifc : targetInterfaces) {
/* 105 */       if ((!isConfigurationCallbackInterface(ifc)) && (!isInternalLanguageInterface(ifc)) && 
/* 106 */         (ifc.getMethods().length > 0)) {
/* 107 */         hasReasonableProxyInterface = true;
/* 108 */         break;
/*     */       }
/*     */     }
/* 111 */     if (hasReasonableProxyInterface)
/*     */     {
/* 113 */       for (Class<?> ifc : targetInterfaces) {
/* 114 */         proxyFactory.addInterface(ifc);
/*     */       }
/*     */       
/*     */     } else {
/* 118 */       proxyFactory.setProxyTargetClass(true);
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
/*     */   protected boolean isConfigurationCallbackInterface(Class<?> ifc)
/*     */   {
/* 131 */     return (InitializingBean.class == ifc) || (DisposableBean.class == ifc) || (Closeable.class == ifc) || 
/* 132 */       ("java.lang.AutoCloseable".equals(ifc.getName())) || 
/* 133 */       (ObjectUtils.containsElement(ifc.getInterfaces(), Aware.class));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isInternalLanguageInterface(Class<?> ifc)
/*     */   {
/* 145 */     return (ifc.getName().equals("groovy.lang.GroovyObject")) || 
/* 146 */       (ifc.getName().endsWith(".cglib.proxy.Factory")) || 
/* 147 */       (ifc.getName().endsWith(".bytebuddy.MockAccess"));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\framework\ProxyProcessorSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */