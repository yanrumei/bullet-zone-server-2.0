/*    */ package org.apache.catalina;
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
/*    */ public enum LifecycleState
/*    */ {
/* 24 */   NEW(false, null), 
/* 25 */   INITIALIZING(false, "before_init"), 
/* 26 */   INITIALIZED(false, "after_init"), 
/* 27 */   STARTING_PREP(false, "before_start"), 
/* 28 */   STARTING(true, "start"), 
/* 29 */   STARTED(true, "after_start"), 
/* 30 */   STOPPING_PREP(true, "before_stop"), 
/* 31 */   STOPPING(false, "stop"), 
/* 32 */   STOPPED(false, "after_stop"), 
/* 33 */   DESTROYING(false, "before_destroy"), 
/* 34 */   DESTROYED(false, "after_destroy"), 
/* 35 */   FAILED(false, null);
/*    */   
/*    */   private final boolean available;
/*    */   private final String lifecycleEvent;
/*    */   
/*    */   private LifecycleState(boolean available, String lifecycleEvent) {
/* 41 */     this.available = available;
/* 42 */     this.lifecycleEvent = lifecycleEvent;
/*    */   }
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
/*    */   public boolean isAvailable()
/*    */   {
/* 59 */     return this.available;
/*    */   }
/*    */   
/*    */   public String getLifecycleEvent() {
/* 63 */     return this.lifecycleEvent;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\LifecycleState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */