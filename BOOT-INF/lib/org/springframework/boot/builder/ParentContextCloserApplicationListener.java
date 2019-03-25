/*     */ package org.springframework.boot.builder;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.context.event.ContextClosedEvent;
/*     */ import org.springframework.core.Ordered;
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
/*     */ public class ParentContextCloserApplicationListener
/*     */   implements ApplicationListener<ParentContextApplicationContextInitializer.ParentContextAvailableEvent>, ApplicationContextAware, Ordered
/*     */ {
/*  43 */   private int order = 2147483637;
/*     */   
/*     */   private ApplicationContext context;
/*     */   
/*     */   public int getOrder()
/*     */   {
/*  49 */     return this.order;
/*     */   }
/*     */   
/*     */   public void setApplicationContext(ApplicationContext context) throws BeansException
/*     */   {
/*  54 */     this.context = context;
/*     */   }
/*     */   
/*     */   public void onApplicationEvent(ParentContextApplicationContextInitializer.ParentContextAvailableEvent event)
/*     */   {
/*  59 */     maybeInstallListenerInParent(event.getApplicationContext());
/*     */   }
/*     */   
/*     */   private void maybeInstallListenerInParent(ConfigurableApplicationContext child) {
/*  63 */     if ((child == this.context) && 
/*  64 */       ((child.getParent() instanceof ConfigurableApplicationContext)))
/*     */     {
/*  66 */       ConfigurableApplicationContext parent = (ConfigurableApplicationContext)child.getParent();
/*  67 */       parent.addApplicationListener(createContextCloserListener(child));
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
/*     */   protected ContextCloserListener createContextCloserListener(ConfigurableApplicationContext child)
/*     */   {
/*  80 */     return new ContextCloserListener(child);
/*     */   }
/*     */   
/*     */ 
/*     */   protected static class ContextCloserListener
/*     */     implements ApplicationListener<ContextClosedEvent>
/*     */   {
/*     */     private WeakReference<ConfigurableApplicationContext> childContext;
/*     */     
/*     */ 
/*     */     public ContextCloserListener(ConfigurableApplicationContext childContext)
/*     */     {
/*  92 */       this.childContext = new WeakReference(childContext);
/*     */     }
/*     */     
/*     */ 
/*     */     public void onApplicationEvent(ContextClosedEvent event)
/*     */     {
/*  98 */       ConfigurableApplicationContext context = (ConfigurableApplicationContext)this.childContext.get();
/*  99 */       if ((context != null) && 
/* 100 */         (event.getApplicationContext() == context.getParent()) && 
/* 101 */         (context.isActive())) {
/* 102 */         context.close();
/*     */       }
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 108 */       return ObjectUtils.nullSafeHashCode(this.childContext.get());
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 113 */       if (this == obj) {
/* 114 */         return true;
/*     */       }
/* 116 */       if (obj == null) {
/* 117 */         return false;
/*     */       }
/* 119 */       if ((obj instanceof ContextCloserListener)) {
/* 120 */         ContextCloserListener other = (ContextCloserListener)obj;
/* 121 */         return ObjectUtils.nullSafeEquals(this.childContext.get(), other.childContext
/* 122 */           .get());
/*     */       }
/* 124 */       return super.equals(obj);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\builder\ParentContextCloserApplicationListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */