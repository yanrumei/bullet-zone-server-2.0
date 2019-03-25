/*    */ package org.apache.catalina;
/*    */ 
/*    */ import java.util.EventObject;
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
/*    */ public final class LifecycleEvent
/*    */   extends EventObject
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final Object data;
/*    */   private final String type;
/*    */   
/*    */   public LifecycleEvent(Lifecycle lifecycle, String type, Object data)
/*    */   {
/* 40 */     super(lifecycle);
/* 41 */     this.type = type;
/* 42 */     this.data = data;
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
/*    */ 
/*    */ 
/*    */ 
/*    */   public Object getData()
/*    */   {
/* 62 */     return this.data;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Lifecycle getLifecycle()
/*    */   {
/* 70 */     return (Lifecycle)getSource();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getType()
/*    */   {
/* 78 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\LifecycleEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */