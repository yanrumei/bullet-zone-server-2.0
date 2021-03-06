/*     */ package org.springframework.ejb.access;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.ejb.CreateException;
/*     */ import javax.ejb.EJBLocalHome;
/*     */ import javax.ejb.EJBLocalObject;
/*     */ import javax.naming.NamingException;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.apache.commons.logging.Log;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocalSlsbInvokerInterceptor
/*     */   extends AbstractSlsbInvokerInterceptor
/*     */ {
/*  52 */   private volatile boolean homeAsComponent = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object invokeInContext(MethodInvocation invocation)
/*     */     throws Throwable
/*     */   {
/*  64 */     Object ejb = null;
/*     */     try {
/*  66 */       ejb = getSessionBeanInstance();
/*  67 */       Method method = invocation.getMethod();
/*  68 */       if (method.getDeclaringClass().isInstance(ejb))
/*     */       {
/*  70 */         return method.invoke(ejb, invocation.getArguments());
/*     */       }
/*     */       
/*     */ 
/*  74 */       Method ejbMethod = ejb.getClass().getMethod(method.getName(), method.getParameterTypes());
/*  75 */       return ejbMethod.invoke(ejb, invocation.getArguments());
/*     */     }
/*     */     catch (InvocationTargetException ex)
/*     */     {
/*  79 */       Throwable targetEx = ex.getTargetException();
/*  80 */       if (this.logger.isDebugEnabled()) {
/*  81 */         this.logger.debug("Method of local EJB [" + getJndiName() + "] threw exception", targetEx);
/*     */       }
/*  83 */       if ((targetEx instanceof CreateException)) {
/*  84 */         throw new EjbAccessException("Could not create local EJB [" + getJndiName() + "]", targetEx);
/*     */       }
/*     */       
/*  87 */       throw targetEx;
/*     */     }
/*     */     catch (NamingException ex)
/*     */     {
/*  91 */       throw new EjbAccessException("Failed to locate local EJB [" + getJndiName() + "]", ex);
/*     */     }
/*     */     catch (IllegalAccessException ex)
/*     */     {
/*  95 */       throw new EjbAccessException("Could not access method [" + invocation.getMethod().getName() + "] of local EJB [" + getJndiName() + "]", ex);
/*     */     }
/*     */     finally {
/*  98 */       if ((ejb instanceof EJBLocalObject)) {
/*  99 */         releaseSessionBeanInstance((EJBLocalObject)ejb);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Method getCreateMethod(Object home)
/*     */     throws EjbAccessException
/*     */   {
/* 109 */     if (this.homeAsComponent) {
/* 110 */       return null;
/*     */     }
/* 112 */     if (!(home instanceof EJBLocalHome))
/*     */     {
/* 114 */       this.homeAsComponent = true;
/* 115 */       return null;
/*     */     }
/* 117 */     return super.getCreateMethod(home);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object getSessionBeanInstance()
/*     */     throws NamingException, InvocationTargetException
/*     */   {
/* 128 */     return newSessionBeanInstance();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void releaseSessionBeanInstance(EJBLocalObject ejb)
/*     */   {
/* 138 */     removeSessionBeanInstance(ejb);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object newSessionBeanInstance()
/*     */     throws NamingException, InvocationTargetException
/*     */   {
/* 149 */     if (this.logger.isDebugEnabled()) {
/* 150 */       this.logger.debug("Trying to create reference to local EJB");
/*     */     }
/* 152 */     Object ejbInstance = create();
/* 153 */     if (this.logger.isDebugEnabled()) {
/* 154 */       this.logger.debug("Obtained reference to local EJB: " + ejbInstance);
/*     */     }
/* 156 */     return ejbInstance;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void removeSessionBeanInstance(EJBLocalObject ejb)
/*     */   {
/* 165 */     if ((ejb != null) && (!this.homeAsComponent)) {
/*     */       try {
/* 167 */         ejb.remove();
/*     */       }
/*     */       catch (Throwable ex) {
/* 170 */         this.logger.warn("Could not invoke 'remove' on local EJB proxy", ex);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\ejb\access\LocalSlsbInvokerInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */