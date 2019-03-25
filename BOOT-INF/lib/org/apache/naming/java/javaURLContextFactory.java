/*     */ package org.apache.naming.java;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.spi.InitialContextFactory;
/*     */ import javax.naming.spi.ObjectFactory;
/*     */ import org.apache.naming.ContextBindings;
/*     */ import org.apache.naming.NamingContext;
/*     */ import org.apache.naming.SelectorContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class javaURLContextFactory
/*     */   implements ObjectFactory, InitialContextFactory
/*     */ {
/*     */   public static final String MAIN = "initialContext";
/*  69 */   protected static volatile Context initialContext = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment)
/*     */     throws NamingException
/*     */   {
/*  86 */     if ((ContextBindings.isThreadBound()) || 
/*  87 */       (ContextBindings.isClassLoaderBound())) {
/*  88 */       return new SelectorContext(environment);
/*     */     }
/*  90 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Context getInitialContext(Hashtable<?, ?> environment)
/*     */     throws NamingException
/*     */   {
/* 101 */     if ((ContextBindings.isThreadBound()) || 
/* 102 */       (ContextBindings.isClassLoaderBound()))
/*     */     {
/* 104 */       return new SelectorContext(environment, true);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 109 */     if (initialContext == null) {
/* 110 */       synchronized (javaURLContextFactory.class) {
/* 111 */         if (initialContext == null) {
/* 112 */           initialContext = new NamingContext(environment, "initialContext");
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 117 */     return initialContext;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\java\javaURLContextFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */