/*    */ package org.apache.coyote;
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
/*    */ public class ContainerThreadMarker
/*    */ {
/* 27 */   private static final ThreadLocal<Boolean> marker = new ThreadLocal();
/*    */   
/*    */   public static boolean isContainerThread() {
/* 30 */     Boolean flag = (Boolean)marker.get();
/* 31 */     if (flag == null) {
/* 32 */       return false;
/*    */     }
/* 34 */     return flag.booleanValue();
/*    */   }
/*    */   
/*    */   public static void set()
/*    */   {
/* 39 */     marker.set(Boolean.TRUE);
/*    */   }
/*    */   
/*    */   public static void clear() {
/* 43 */     marker.set(Boolean.FALSE);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\ContainerThreadMarker.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */