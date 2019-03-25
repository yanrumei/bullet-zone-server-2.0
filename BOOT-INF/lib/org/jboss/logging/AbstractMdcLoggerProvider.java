/*    */ package org.jboss.logging;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ abstract class AbstractMdcLoggerProvider
/*    */   extends AbstractLoggerProvider
/*    */ {
/* 27 */   private final ThreadLocal<Map<String, Object>> mdcMap = new ThreadLocal();
/*    */   
/*    */   public void clearMdc() {
/* 30 */     Map<String, Object> map = (Map)this.mdcMap.get();
/* 31 */     if (map != null) {
/* 32 */       map.clear();
/*    */     }
/*    */   }
/*    */   
/*    */   public Object getMdc(String key) {
/* 37 */     return this.mdcMap.get() == null ? null : ((Map)this.mdcMap.get()).get(key);
/*    */   }
/*    */   
/*    */   public Map<String, Object> getMdcMap() {
/* 41 */     Map<String, Object> map = (Map)this.mdcMap.get();
/* 42 */     return map == null ? Collections.emptyMap() : map;
/*    */   }
/*    */   
/*    */   public Object putMdc(String key, Object value) {
/* 46 */     Map<String, Object> map = (Map)this.mdcMap.get();
/* 47 */     if (map == null) {
/* 48 */       map = new HashMap();
/* 49 */       this.mdcMap.set(map);
/*    */     }
/* 51 */     return map.put(key, value);
/*    */   }
/*    */   
/*    */   public void removeMdc(String key) {
/* 55 */     Map<String, Object> map = (Map)this.mdcMap.get();
/* 56 */     if (map == null)
/* 57 */       return;
/* 58 */     map.remove(key);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jboss-logging-3.3.1.Final.jar!\org\jboss\logging\AbstractMdcLoggerProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */