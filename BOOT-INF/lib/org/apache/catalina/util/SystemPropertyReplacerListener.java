/*    */ package org.apache.catalina.util;
/*    */ 
/*    */ import org.apache.catalina.LifecycleEvent;
/*    */ import org.apache.catalina.LifecycleListener;
/*    */ import org.apache.tomcat.util.digester.Digester;
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
/*    */ public class SystemPropertyReplacerListener
/*    */   implements LifecycleListener
/*    */ {
/*    */   public void lifecycleEvent(LifecycleEvent event)
/*    */   {
/* 42 */     if ("before_init".equals(event.getType())) {
/* 43 */       Digester.replaceSystemProperties();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\util\SystemPropertyReplacerListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */