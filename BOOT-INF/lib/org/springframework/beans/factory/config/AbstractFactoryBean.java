/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.SimpleTypeConverter;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.FactoryBeanNotInitializedException;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractFactoryBean<T>
/*     */   implements FactoryBean<T>, BeanClassLoaderAware, BeanFactoryAware, InitializingBean, DisposableBean
/*     */ {
/*  64 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  66 */   private boolean singleton = true;
/*     */   
/*  68 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */   
/*     */   private BeanFactory beanFactory;
/*     */   
/*  72 */   private boolean initialized = false;
/*     */   
/*     */ 
/*     */   private T singletonInstance;
/*     */   
/*     */ 
/*     */   private T earlySingletonInstance;
/*     */   
/*     */ 
/*     */ 
/*     */   public void setSingleton(boolean singleton)
/*     */   {
/*  84 */     this.singleton = singleton;
/*     */   }
/*     */   
/*     */   public boolean isSingleton()
/*     */   {
/*  89 */     return this.singleton;
/*     */   }
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader)
/*     */   {
/*  94 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory)
/*     */   {
/*  99 */     this.beanFactory = beanFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected BeanFactory getBeanFactory()
/*     */   {
/* 106 */     return this.beanFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TypeConverter getBeanTypeConverter()
/*     */   {
/* 118 */     BeanFactory beanFactory = getBeanFactory();
/* 119 */     if ((beanFactory instanceof ConfigurableBeanFactory)) {
/* 120 */       return ((ConfigurableBeanFactory)beanFactory).getTypeConverter();
/*     */     }
/*     */     
/* 123 */     return new SimpleTypeConverter();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void afterPropertiesSet()
/*     */     throws Exception
/*     */   {
/* 132 */     if (isSingleton()) {
/* 133 */       this.initialized = true;
/* 134 */       this.singletonInstance = createInstance();
/* 135 */       this.earlySingletonInstance = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final T getObject()
/*     */     throws Exception
/*     */   {
/* 147 */     if (isSingleton()) {
/* 148 */       return (T)(this.initialized ? this.singletonInstance : getEarlySingletonInstance());
/*     */     }
/*     */     
/* 151 */     return (T)createInstance();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private T getEarlySingletonInstance()
/*     */     throws Exception
/*     */   {
/* 161 */     Class<?>[] ifcs = getEarlySingletonInterfaces();
/* 162 */     if (ifcs == null)
/*     */     {
/* 164 */       throw new FactoryBeanNotInitializedException(getClass().getName() + " does not support circular references");
/*     */     }
/* 166 */     if (this.earlySingletonInstance == null) {
/* 167 */       this.earlySingletonInstance = Proxy.newProxyInstance(this.beanClassLoader, ifcs, new EarlySingletonInvocationHandler(null));
/*     */     }
/*     */     
/* 170 */     return (T)this.earlySingletonInstance;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private T getSingletonInstance()
/*     */     throws IllegalStateException
/*     */   {
/* 179 */     if (!this.initialized) {
/* 180 */       throw new IllegalStateException("Singleton instance not initialized yet");
/*     */     }
/* 182 */     return (T)this.singletonInstance;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void destroy()
/*     */     throws Exception
/*     */   {
/* 191 */     if (isSingleton()) {
/* 192 */       destroyInstance(this.singletonInstance);
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
/*     */   public abstract Class<?> getObjectType();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract T createInstance()
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Class<?>[] getEarlySingletonInterfaces()
/*     */   {
/* 229 */     Class<?> type = getObjectType();
/* 230 */     return (type != null) && (type.isInterface()) ? new Class[] { type } : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void destroyInstance(T instance)
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private class EarlySingletonInvocationHandler
/*     */     implements InvocationHandler
/*     */   {
/*     */     private EarlySingletonInvocationHandler() {}
/*     */     
/*     */ 
/*     */ 
/*     */     public Object invoke(Object proxy, Method method, Object[] args)
/*     */       throws Throwable
/*     */     {
/* 253 */       if (ReflectionUtils.isEqualsMethod(method))
/*     */       {
/* 255 */         return Boolean.valueOf(proxy == args[0]);
/*     */       }
/* 257 */       if (ReflectionUtils.isHashCodeMethod(method))
/*     */       {
/* 259 */         return Integer.valueOf(System.identityHashCode(proxy));
/*     */       }
/* 261 */       if ((!AbstractFactoryBean.this.initialized) && (ReflectionUtils.isToStringMethod(method))) {
/* 262 */         return 
/* 263 */           "Early singleton proxy for interfaces " + ObjectUtils.nullSafeToString(AbstractFactoryBean.this.getEarlySingletonInterfaces());
/*     */       }
/*     */       try {
/* 266 */         return method.invoke(AbstractFactoryBean.this.getSingletonInstance(), args);
/*     */       }
/*     */       catch (InvocationTargetException ex) {
/* 269 */         throw ex.getTargetException();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\config\AbstractFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */