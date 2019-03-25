/*    */ package com.fasterxml.jackson.core.util;
/*    */ 
/*    */ import java.util.concurrent.ConcurrentHashMap;
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
/*    */ public final class InternCache
/*    */   extends ConcurrentHashMap<String, String>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private static final int MAX_ENTRIES = 180;
/* 30 */   public static final InternCache instance = new InternCache();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 37 */   private final Object lock = new Object();
/*    */   
/* 39 */   private InternCache() { super(180, 0.8F, 4); }
/*    */   
/*    */   public String intern(String input) {
/* 42 */     String result = (String)get(input);
/* 43 */     if (result != null) { return result;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 50 */     if (size() >= 180)
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/* 55 */       synchronized (this.lock) {
/* 56 */         if (size() >= 180) {
/* 57 */           clear();
/*    */         }
/*    */       }
/*    */     }
/* 61 */     result = input.intern();
/* 62 */     put(result, result);
/* 63 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\cor\\util\InternCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */