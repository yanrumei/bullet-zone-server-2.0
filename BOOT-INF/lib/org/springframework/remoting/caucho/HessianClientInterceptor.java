/*     */ package org.springframework.remoting.caucho;
/*     */ 
/*     */ import com.caucho.hessian.HessianException;
/*     */ import com.caucho.hessian.client.HessianConnectionException;
/*     */ import com.caucho.hessian.client.HessianConnectionFactory;
/*     */ import com.caucho.hessian.client.HessianProxyFactory;
/*     */ import com.caucho.hessian.client.HessianRuntimeException;
/*     */ import com.caucho.hessian.io.SerializerFactory;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.UndeclaredThrowableException;
/*     */ import java.net.ConnectException;
/*     */ import java.net.MalformedURLException;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.remoting.RemoteAccessException;
/*     */ import org.springframework.remoting.RemoteConnectFailureException;
/*     */ import org.springframework.remoting.RemoteLookupFailureException;
/*     */ import org.springframework.remoting.RemoteProxyFailureException;
/*     */ import org.springframework.remoting.support.UrlBasedRemoteAccessor;
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
/*     */ public class HessianClientInterceptor
/*     */   extends UrlBasedRemoteAccessor
/*     */   implements MethodInterceptor
/*     */ {
/*  68 */   private HessianProxyFactory proxyFactory = new HessianProxyFactory();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Object hessianProxy;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProxyFactory(HessianProxyFactory proxyFactory)
/*     */   {
/*  80 */     this.proxyFactory = (proxyFactory != null ? proxyFactory : new HessianProxyFactory());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSerializerFactory(SerializerFactory serializerFactory)
/*     */   {
/*  90 */     this.proxyFactory.setSerializerFactory(serializerFactory);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSendCollectionType(boolean sendCollectionType)
/*     */   {
/*  98 */     this.proxyFactory.getSerializerFactory().setSendCollectionType(sendCollectionType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAllowNonSerializable(boolean allowNonSerializable)
/*     */   {
/* 106 */     this.proxyFactory.getSerializerFactory().setAllowNonSerializable(allowNonSerializable);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOverloadEnabled(boolean overloadEnabled)
/*     */   {
/* 115 */     this.proxyFactory.setOverloadEnabled(overloadEnabled);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUsername(String username)
/*     */   {
/* 125 */     this.proxyFactory.setUser(username);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPassword(String password)
/*     */   {
/* 135 */     this.proxyFactory.setPassword(password);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDebug(boolean debug)
/*     */   {
/* 144 */     this.proxyFactory.setDebug(debug);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setChunkedPost(boolean chunkedPost)
/*     */   {
/* 152 */     this.proxyFactory.setChunkedPost(chunkedPost);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setConnectionFactory(HessianConnectionFactory connectionFactory)
/*     */   {
/* 159 */     this.proxyFactory.setConnectionFactory(connectionFactory);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setConnectTimeout(long timeout)
/*     */   {
/* 167 */     this.proxyFactory.setConnectTimeout(timeout);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setReadTimeout(long timeout)
/*     */   {
/* 175 */     this.proxyFactory.setReadTimeout(timeout);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHessian2(boolean hessian2)
/*     */   {
/* 184 */     this.proxyFactory.setHessian2Request(hessian2);
/* 185 */     this.proxyFactory.setHessian2Reply(hessian2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHessian2Request(boolean hessian2)
/*     */   {
/* 194 */     this.proxyFactory.setHessian2Request(hessian2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHessian2Reply(boolean hessian2)
/*     */   {
/* 203 */     this.proxyFactory.setHessian2Reply(hessian2);
/*     */   }
/*     */   
/*     */ 
/*     */   public void afterPropertiesSet()
/*     */   {
/* 209 */     super.afterPropertiesSet();
/* 210 */     prepare();
/*     */   }
/*     */   
/*     */ 
/*     */   public void prepare()
/*     */     throws RemoteLookupFailureException
/*     */   {
/*     */     try
/*     */     {
/* 219 */       this.hessianProxy = createHessianProxy(this.proxyFactory);
/*     */     }
/*     */     catch (MalformedURLException ex) {
/* 222 */       throw new RemoteLookupFailureException("Service URL [" + getServiceUrl() + "] is invalid", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object createHessianProxy(HessianProxyFactory proxyFactory)
/*     */     throws MalformedURLException
/*     */   {
/* 234 */     Assert.notNull(getServiceInterface(), "'serviceInterface' is required");
/* 235 */     return proxyFactory.create(getServiceInterface(), getServiceUrl(), getBeanClassLoader());
/*     */   }
/*     */   
/*     */   public Object invoke(MethodInvocation invocation)
/*     */     throws Throwable
/*     */   {
/* 241 */     if (this.hessianProxy == null) {
/* 242 */       throw new IllegalStateException("HessianClientInterceptor is not properly initialized - invoke 'prepare' before attempting any operations");
/*     */     }
/*     */     
/*     */ 
/* 246 */     ClassLoader originalClassLoader = overrideThreadContextClassLoader();
/*     */     try {
/* 248 */       return invocation.getMethod().invoke(this.hessianProxy, invocation.getArguments());
/*     */     }
/*     */     catch (InvocationTargetException ex) {
/* 251 */       Throwable targetEx = ex.getTargetException();
/*     */       
/* 253 */       if ((targetEx instanceof InvocationTargetException)) {
/* 254 */         targetEx = ((InvocationTargetException)targetEx).getTargetException();
/*     */       }
/* 256 */       if ((targetEx instanceof HessianConnectionException)) {
/* 257 */         throw convertHessianAccessException(targetEx);
/*     */       }
/* 259 */       if (((targetEx instanceof HessianException)) || ((targetEx instanceof HessianRuntimeException))) {
/* 260 */         Throwable cause = targetEx.getCause();
/* 261 */         throw convertHessianAccessException(cause != null ? cause : targetEx);
/*     */       }
/* 263 */       if ((targetEx instanceof UndeclaredThrowableException)) {
/* 264 */         UndeclaredThrowableException utex = (UndeclaredThrowableException)targetEx;
/* 265 */         throw convertHessianAccessException(utex.getUndeclaredThrowable());
/*     */       }
/*     */       
/* 268 */       throw targetEx;
/*     */ 
/*     */     }
/*     */     catch (Throwable ex)
/*     */     {
/* 273 */       throw new RemoteProxyFailureException("Failed to invoke Hessian proxy for remote service [" + getServiceUrl() + "]", ex);
/*     */     }
/*     */     finally {
/* 276 */       resetThreadContextClassLoader(originalClassLoader);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected RemoteAccessException convertHessianAccessException(Throwable ex)
/*     */   {
/* 287 */     if (((ex instanceof HessianConnectionException)) || ((ex instanceof ConnectException))) {
/* 288 */       return new RemoteConnectFailureException("Cannot connect to Hessian remote service at [" + 
/* 289 */         getServiceUrl() + "]", ex);
/*     */     }
/*     */     
/* 292 */     return new RemoteAccessException("Cannot access Hessian remote service at [" + 
/* 293 */       getServiceUrl() + "]", ex);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\remoting\caucho\HessianClientInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */