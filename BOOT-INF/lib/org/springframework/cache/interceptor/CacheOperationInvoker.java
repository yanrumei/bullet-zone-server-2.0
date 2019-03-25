/*    */ package org.springframework.cache.interceptor;
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
/*    */ public abstract interface CacheOperationInvoker
/*    */ {
/*    */   public abstract Object invoke()
/*    */     throws CacheOperationInvoker.ThrowableWrapper;
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
/*    */   public static class ThrowableWrapper
/*    */     extends RuntimeException
/*    */   {
/*    */     private final Throwable original;
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
/*    */     public ThrowableWrapper(Throwable original)
/*    */     {
/* 50 */       super(original);
/* 51 */       this.original = original;
/*    */     }
/*    */     
/*    */     public Throwable getOriginal() {
/* 55 */       return this.original;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\interceptor\CacheOperationInvoker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */