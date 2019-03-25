/*    */ package org.springframework.web.servlet.view.tiles2;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import org.apache.tiles.TilesException;
/*    */ import org.apache.tiles.preparer.NoSuchPreparerException;
/*    */ import org.apache.tiles.preparer.PreparerException;
/*    */ import org.apache.tiles.preparer.ViewPreparer;
/*    */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*    */ import org.springframework.web.context.WebApplicationContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class SimpleSpringPreparerFactory
/*    */   extends AbstractSpringPreparerFactory
/*    */ {
/* 47 */   private final Map<String, ViewPreparer> sharedPreparers = new ConcurrentHashMap(16);
/*    */   
/*    */ 
/*    */   protected ViewPreparer getPreparer(String name, WebApplicationContext context)
/*    */     throws TilesException
/*    */   {
/* 53 */     ViewPreparer preparer = (ViewPreparer)this.sharedPreparers.get(name);
/* 54 */     if (preparer == null) {
/* 55 */       synchronized (this.sharedPreparers) {
/* 56 */         preparer = (ViewPreparer)this.sharedPreparers.get(name);
/* 57 */         if (preparer == null) {
/*    */           try {
/* 59 */             Class<?> beanClass = context.getClassLoader().loadClass(name);
/* 60 */             if (!ViewPreparer.class.isAssignableFrom(beanClass)) {
/* 61 */               throw new PreparerException("Invalid preparer class [" + name + "]: does not implement ViewPreparer interface");
/*    */             }
/*    */             
/* 64 */             preparer = (ViewPreparer)context.getAutowireCapableBeanFactory().createBean(beanClass);
/* 65 */             this.sharedPreparers.put(name, preparer);
/*    */           }
/*    */           catch (ClassNotFoundException ex) {
/* 68 */             throw new NoSuchPreparerException("Preparer class [" + name + "] not found", ex);
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/* 73 */     return preparer;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\tiles2\SimpleSpringPreparerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */