/*     */ package org.apache.naming.factory;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.RefAddr;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.spi.ObjectFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class FactoryBase
/*     */   implements ObjectFactory
/*     */ {
/*     */   public final Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment)
/*     */     throws Exception
/*     */   {
/*  43 */     if (isReferenceTypeSupported(obj)) {
/*  44 */       Reference ref = (Reference)obj;
/*     */       
/*  46 */       Object linked = getLinked(ref);
/*  47 */       if (linked != null) {
/*  48 */         return linked;
/*     */       }
/*     */       
/*  51 */       ObjectFactory factory = null;
/*  52 */       RefAddr factoryRefAddr = ref.get("factory");
/*  53 */       if (factoryRefAddr != null)
/*     */       {
/*  55 */         String factoryClassName = factoryRefAddr.getContent().toString();
/*     */         
/*  57 */         ClassLoader tcl = Thread.currentThread().getContextClassLoader();
/*  58 */         Class<?> factoryClass = null;
/*     */         try {
/*  60 */           if (tcl != null) {
/*  61 */             factoryClass = tcl.loadClass(factoryClassName);
/*     */           } else {
/*  63 */             factoryClass = Class.forName(factoryClassName);
/*     */           }
/*     */         } catch (ClassNotFoundException e) {
/*  66 */           NamingException ex = new NamingException("Could not load resource factory class");
/*     */           
/*  68 */           ex.initCause(e);
/*  69 */           throw ex;
/*     */         }
/*     */         try {
/*  72 */           factory = (ObjectFactory)factoryClass.getConstructor(new Class[0]).newInstance(new Object[0]);
/*     */         } catch (Throwable t) {
/*  74 */           if ((t instanceof NamingException)) {
/*  75 */             throw ((NamingException)t);
/*     */           }
/*  77 */           if ((t instanceof ThreadDeath)) {
/*  78 */             throw ((ThreadDeath)t);
/*     */           }
/*  80 */           if ((t instanceof VirtualMachineError)) {
/*  81 */             throw ((VirtualMachineError)t);
/*     */           }
/*  83 */           NamingException ex = new NamingException("Could not create resource factory instance");
/*     */           
/*  85 */           ex.initCause(t);
/*  86 */           throw ex;
/*     */         }
/*     */       }
/*     */       else {
/*  90 */         factory = getDefaultFactory(ref);
/*     */       }
/*     */       
/*  93 */       if (factory != null) {
/*  94 */         return factory.getObjectInstance(obj, name, nameCtx, environment);
/*     */       }
/*  96 */       throw new NamingException("Cannot create resource instance");
/*     */     }
/*     */     
/*     */ 
/* 100 */     return null;
/*     */   }
/*     */   
/*     */   protected abstract boolean isReferenceTypeSupported(Object paramObject);
/*     */   
/*     */   protected abstract ObjectFactory getDefaultFactory(Reference paramReference)
/*     */     throws NamingException;
/*     */   
/*     */   protected abstract Object getLinked(Reference paramReference)
/*     */     throws NamingException;
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\factory\FactoryBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */