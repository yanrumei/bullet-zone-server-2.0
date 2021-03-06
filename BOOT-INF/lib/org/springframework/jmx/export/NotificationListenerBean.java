/*    */ package org.springframework.jmx.export;
/*    */ 
/*    */ import java.util.Set;
/*    */ import javax.management.NotificationListener;
/*    */ import org.springframework.beans.factory.InitializingBean;
/*    */ import org.springframework.jmx.support.NotificationListenerHolder;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class NotificationListenerBean
/*    */   extends NotificationListenerHolder
/*    */   implements InitializingBean
/*    */ {
/*    */   public NotificationListenerBean() {}
/*    */   
/*    */   public NotificationListenerBean(NotificationListener notificationListener)
/*    */   {
/* 59 */     Assert.notNull(notificationListener, "NotificationListener must not be null");
/* 60 */     setNotificationListener(notificationListener);
/*    */   }
/*    */   
/*    */ 
/*    */   public void afterPropertiesSet()
/*    */   {
/* 66 */     if (getNotificationListener() == null) {
/* 67 */       throw new IllegalArgumentException("Property 'notificationListener' is required");
/*    */     }
/*    */   }
/*    */   
/*    */   void replaceObjectName(Object originalName, Object newName) {
/* 72 */     if ((this.mappedObjectNames != null) && (this.mappedObjectNames.contains(originalName))) {
/* 73 */       this.mappedObjectNames.remove(originalName);
/* 74 */       this.mappedObjectNames.add(newName);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jmx\export\NotificationListenerBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */