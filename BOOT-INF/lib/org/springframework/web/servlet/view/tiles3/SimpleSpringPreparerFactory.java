/*    */ package org.springframework.web.servlet.view.tiles3;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import org.apache.tiles.TilesException;
/*    */ import org.apache.tiles.preparer.PreparerException;
/*    */ import org.apache.tiles.preparer.ViewPreparer;
/*    */ import org.apache.tiles.preparer.factory.NoSuchPreparerException;
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
/*    */ public class SimpleSpringPreparerFactory
/*    */   extends AbstractSpringPreparerFactory
/*    */ {
/* 42 */   private final Map<String, ViewPreparer> sharedPreparers = new ConcurrentHashMap(16);
/*    */   
/*    */ 
/*    */   protected ViewPreparer getPreparer(String name, WebApplicationContext context)
/*    */     throws TilesException
/*    */   {
/* 48 */     ViewPreparer preparer = (ViewPreparer)this.sharedPreparers.get(name);
/* 49 */     if (preparer == null) {
/* 50 */       synchronized (this.sharedPreparers) {
/* 51 */         preparer = (ViewPreparer)this.sharedPreparers.get(name);
/* 52 */         if (preparer == null) {
/*    */           try {
/* 54 */             Class<?> beanClass = context.getClassLoader().loadClass(name);
/* 55 */             if (!ViewPreparer.class.isAssignableFrom(beanClass)) {
/* 56 */               throw new PreparerException("Invalid preparer class [" + name + "]: does not implement ViewPreparer interface");
/*    */             }
/*    */             
/* 59 */             preparer = (ViewPreparer)context.getAutowireCapableBeanFactory().createBean(beanClass);
/* 60 */             this.sharedPreparers.put(name, preparer);
/*    */           }
/*    */           catch (ClassNotFoundException ex) {
/* 63 */             throw new NoSuchPreparerException("Preparer class [" + name + "] not found", ex);
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/* 68 */     return preparer;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\tiles3\SimpleSpringPreparerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */