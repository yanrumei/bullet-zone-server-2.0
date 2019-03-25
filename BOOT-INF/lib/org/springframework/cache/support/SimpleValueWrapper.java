/*    */ package org.springframework.cache.support;
/*    */ 
/*    */ import org.springframework.cache.Cache.ValueWrapper;
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
/*    */ public class SimpleValueWrapper
/*    */   implements Cache.ValueWrapper
/*    */ {
/*    */   private final Object value;
/*    */   
/*    */   public SimpleValueWrapper(Object value)
/*    */   {
/* 38 */     this.value = value;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Object get()
/*    */   {
/* 47 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\support\SimpleValueWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */