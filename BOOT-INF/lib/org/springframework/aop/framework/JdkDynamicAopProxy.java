/*     */ package org.springframework.aop.framework;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.List;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.AopInvocationException;
/*     */ import org.springframework.aop.RawTargetAccess;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.core.DecoratingProxy;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class JdkDynamicAopProxy
/*     */   implements AopProxy, InvocationHandler, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 5531744639992436476L;
/*  79 */   private static final Log logger = LogFactory.getLog(JdkDynamicAopProxy.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final AdvisedSupport advised;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean equalsDefined;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean hashCodeDefined;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JdkDynamicAopProxy(AdvisedSupport config)
/*     */     throws AopConfigException
/*     */   {
/* 102 */     Assert.notNull(config, "AdvisedSupport must not be null");
/* 103 */     if ((config.getAdvisors().length == 0) && (config.getTargetSource() == AdvisedSupport.EMPTY_TARGET_SOURCE)) {
/* 104 */       throw new AopConfigException("No advisors and no TargetSource specified");
/*     */     }
/* 106 */     this.advised = config;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object getProxy()
/*     */   {
/* 112 */     return getProxy(ClassUtils.getDefaultClassLoader());
/*     */   }
/*     */   
/*     */   public Object getProxy(ClassLoader classLoader)
/*     */   {
/* 117 */     if (logger.isDebugEnabled()) {
/* 118 */       logger.debug("Creating JDK dynamic proxy: target source is " + this.advised.getTargetSource());
/*     */     }
/* 120 */     Class<?>[] proxiedInterfaces = AopProxyUtils.completeProxiedInterfaces(this.advised, true);
/* 121 */     findDefinedEqualsAndHashCodeMethods(proxiedInterfaces);
/* 122 */     return Proxy.newProxyInstance(classLoader, proxiedInterfaces, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void findDefinedEqualsAndHashCodeMethods(Class<?>[] proxiedInterfaces)
/*     */   {
/* 131 */     for (Class<?> proxiedInterface : proxiedInterfaces) {
/* 132 */       Method[] methods = proxiedInterface.getDeclaredMethods();
/* 133 */       for (Method method : methods) {
/* 134 */         if (AopUtils.isEqualsMethod(method)) {
/* 135 */           this.equalsDefined = true;
/*     */         }
/* 137 */         if (AopUtils.isHashCodeMethod(method)) {
/* 138 */           this.hashCodeDefined = true;
/*     */         }
/* 140 */         if ((this.equalsDefined) && (this.hashCodeDefined)) {
/* 141 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object invoke(Object proxy, Method method, Object[] args)
/*     */     throws Throwable
/*     */   {
/* 156 */     Object oldProxy = null;
/* 157 */     boolean setProxyContext = false;
/*     */     
/* 159 */     TargetSource targetSource = this.advised.targetSource;
/* 160 */     Class<?> targetClass = null;
/* 161 */     Object target = null;
/*     */     try {
/*     */       Object localObject1;
/* 164 */       if ((!this.equalsDefined) && (AopUtils.isEqualsMethod(method)))
/*     */       {
/* 166 */         return Boolean.valueOf(equals(args[0]));
/*     */       }
/* 168 */       if ((!this.hashCodeDefined) && (AopUtils.isHashCodeMethod(method)))
/*     */       {
/* 170 */         return Integer.valueOf(hashCode());
/*     */       }
/* 172 */       if (method.getDeclaringClass() == DecoratingProxy.class)
/*     */       {
/* 174 */         return AopProxyUtils.ultimateTargetClass(this.advised);
/*     */       }
/* 176 */       if ((!this.advised.opaque) && (method.getDeclaringClass().isInterface()) && 
/* 177 */         (method.getDeclaringClass().isAssignableFrom(Advised.class)))
/*     */       {
/* 179 */         return AopUtils.invokeJoinpointUsingReflection(this.advised, method, args);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 184 */       if (this.advised.exposeProxy)
/*     */       {
/* 186 */         oldProxy = AopContext.setCurrentProxy(proxy);
/* 187 */         setProxyContext = true;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 192 */       target = targetSource.getTarget();
/* 193 */       if (target != null) {
/* 194 */         targetClass = target.getClass();
/*     */       }
/*     */       
/*     */ 
/* 198 */       List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
/*     */       
/*     */       Object retVal;
/*     */       Object retVal;
/* 202 */       if (chain.isEmpty())
/*     */       {
/*     */ 
/*     */ 
/* 206 */         Object[] argsToUse = AopProxyUtils.adaptArgumentsIfNecessary(method, args);
/* 207 */         retVal = AopUtils.invokeJoinpointUsingReflection(target, method, argsToUse);
/*     */       }
/*     */       else
/*     */       {
/* 211 */         MethodInvocation invocation = new ReflectiveMethodInvocation(proxy, target, method, args, targetClass, chain);
/*     */         
/* 213 */         retVal = invocation.proceed();
/*     */       }
/*     */       
/*     */ 
/* 217 */       Class<?> returnType = method.getReturnType();
/* 218 */       if ((retVal != null) && (retVal == target) && (returnType != Object.class) && 
/* 219 */         (returnType.isInstance(proxy)) && 
/* 220 */         (!RawTargetAccess.class.isAssignableFrom(method.getDeclaringClass())))
/*     */       {
/*     */ 
/*     */ 
/* 224 */         retVal = proxy;
/*     */       }
/* 226 */       else if ((retVal == null) && (returnType != Void.TYPE) && (returnType.isPrimitive())) {
/* 227 */         throw new AopInvocationException("Null return value from advice does not match primitive return type for: " + method);
/*     */       }
/*     */       
/* 230 */       return retVal;
/*     */     }
/*     */     finally {
/* 233 */       if ((target != null) && (!targetSource.isStatic()))
/*     */       {
/* 235 */         targetSource.releaseTarget(target);
/*     */       }
/* 237 */       if (setProxyContext)
/*     */       {
/* 239 */         AopContext.setCurrentProxy(oldProxy);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 252 */     if (other == this) {
/* 253 */       return true;
/*     */     }
/* 255 */     if (other == null) {
/* 256 */       return false;
/*     */     }
/*     */     
/*     */     JdkDynamicAopProxy otherProxy;
/* 260 */     if ((other instanceof JdkDynamicAopProxy)) {
/* 261 */       otherProxy = (JdkDynamicAopProxy)other;
/*     */     } else { JdkDynamicAopProxy otherProxy;
/* 263 */       if (Proxy.isProxyClass(other.getClass())) {
/* 264 */         InvocationHandler ih = Proxy.getInvocationHandler(other);
/* 265 */         if (!(ih instanceof JdkDynamicAopProxy)) {
/* 266 */           return false;
/*     */         }
/* 268 */         otherProxy = (JdkDynamicAopProxy)ih;
/*     */       }
/*     */       else
/*     */       {
/* 272 */         return false;
/*     */       }
/*     */     }
/*     */     JdkDynamicAopProxy otherProxy;
/* 276 */     return AopProxyUtils.equalsInProxy(this.advised, otherProxy.advised);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 284 */     return JdkDynamicAopProxy.class.hashCode() * 13 + this.advised.getTargetSource().hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\framework\JdkDynamicAopProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */