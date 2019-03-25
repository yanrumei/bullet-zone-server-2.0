/*    */ package ch.qos.logback.core.status;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class StatusListenerAsList
/*    */   implements StatusListener
/*    */ {
/* 27 */   List<Status> statusList = new ArrayList();
/*    */   
/*    */   public void addStatusEvent(Status status) {
/* 30 */     this.statusList.add(status);
/*    */   }
/*    */   
/*    */   public List<Status> getStatusList() {
/* 34 */     return this.statusList;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\status\StatusListenerAsList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */