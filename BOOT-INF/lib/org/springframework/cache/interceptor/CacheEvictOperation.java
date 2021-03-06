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
/*    */ public class CacheEvictOperation
/*    */   extends CacheOperation
/*    */ {
/*    */   private final boolean cacheWide;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private final boolean beforeInvocation;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public CacheEvictOperation(Builder b)
/*    */   {
/* 37 */     super(b);
/* 38 */     this.cacheWide = b.cacheWide;
/* 39 */     this.beforeInvocation = b.beforeInvocation;
/*    */   }
/*    */   
/*    */   public boolean isCacheWide()
/*    */   {
/* 44 */     return this.cacheWide;
/*    */   }
/*    */   
/*    */   public boolean isBeforeInvocation() {
/* 48 */     return this.beforeInvocation;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static class Builder
/*    */     extends CacheOperation.Builder
/*    */   {
/* 57 */     private boolean cacheWide = false;
/*    */     
/* 59 */     private boolean beforeInvocation = false;
/*    */     
/*    */     public void setCacheWide(boolean cacheWide) {
/* 62 */       this.cacheWide = cacheWide;
/*    */     }
/*    */     
/*    */     public void setBeforeInvocation(boolean beforeInvocation) {
/* 66 */       this.beforeInvocation = beforeInvocation;
/*    */     }
/*    */     
/*    */     protected StringBuilder getOperationDescription()
/*    */     {
/* 71 */       StringBuilder sb = super.getOperationDescription();
/* 72 */       sb.append(",");
/* 73 */       sb.append(this.cacheWide);
/* 74 */       sb.append(",");
/* 75 */       sb.append(this.beforeInvocation);
/* 76 */       return sb;
/*    */     }
/*    */     
/*    */     public CacheEvictOperation build() {
/* 80 */       return new CacheEvictOperation(this);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\interceptor\CacheEvictOperation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */