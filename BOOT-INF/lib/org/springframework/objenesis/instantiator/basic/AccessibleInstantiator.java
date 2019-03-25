/*    */ package org.springframework.objenesis.instantiator.basic;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import org.springframework.objenesis.instantiator.annotations.Instantiator;
/*    */ import org.springframework.objenesis.instantiator.annotations.Typology;
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
/*    */ @Instantiator(Typology.NOT_COMPLIANT)
/*    */ public class AccessibleInstantiator<T>
/*    */   extends ConstructorInstantiator<T>
/*    */ {
/*    */   public AccessibleInstantiator(Class<T> type)
/*    */   {
/* 33 */     super(type);
/* 34 */     if (this.constructor != null) {
/* 35 */       this.constructor.setAccessible(true);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\objenesis\instantiator\basic\AccessibleInstantiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */