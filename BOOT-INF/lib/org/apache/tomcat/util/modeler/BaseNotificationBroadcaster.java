/*     */ package org.apache.tomcat.util.modeler;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import javax.management.ListenerNotFoundException;
/*     */ import javax.management.MBeanNotificationInfo;
/*     */ import javax.management.Notification;
/*     */ import javax.management.NotificationBroadcaster;
/*     */ import javax.management.NotificationFilter;
/*     */ import javax.management.NotificationListener;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BaseNotificationBroadcaster
/*     */   implements NotificationBroadcaster
/*     */ {
/*  56 */   protected ArrayList<BaseNotificationBroadcasterEntry> entries = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback)
/*     */     throws IllegalArgumentException
/*     */   {
/*  80 */     synchronized (this.entries)
/*     */     {
/*     */ 
/*  83 */       if ((filter instanceof BaseAttributeFilter)) {
/*  84 */         BaseAttributeFilter newFilter = (BaseAttributeFilter)filter;
/*     */         
/*  86 */         Iterator<BaseNotificationBroadcasterEntry> items = this.entries.iterator();
/*  87 */         while (items.hasNext()) {
/*  88 */           BaseNotificationBroadcasterEntry item = (BaseNotificationBroadcasterEntry)items.next();
/*  89 */           if ((item.listener == listener) && (item.filter != null) && ((item.filter instanceof BaseAttributeFilter)) && (item.handback == handback))
/*     */           {
/*     */ 
/*     */ 
/*  93 */             BaseAttributeFilter oldFilter = (BaseAttributeFilter)item.filter;
/*     */             
/*  95 */             String[] newNames = newFilter.getNames();
/*  96 */             String[] oldNames = oldFilter.getNames();
/*  97 */             if (newNames.length == 0) {
/*  98 */               oldFilter.clear();
/*     */             }
/* 100 */             else if (oldNames.length != 0) {
/* 101 */               for (int i = 0; i < newNames.length; i++) {
/* 102 */                 oldFilter.addAttribute(newNames[i]);
/*     */               }
/*     */             }
/* 105 */             return;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 111 */       this.entries.add(new BaseNotificationBroadcasterEntry(listener, filter, handback));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MBeanNotificationInfo[] getNotificationInfo()
/*     */   {
/* 125 */     return new MBeanNotificationInfo[0];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeNotificationListener(NotificationListener listener)
/*     */     throws ListenerNotFoundException
/*     */   {
/* 143 */     synchronized (this.entries)
/*     */     {
/* 145 */       Iterator<BaseNotificationBroadcasterEntry> items = this.entries.iterator();
/* 146 */       while (items.hasNext()) {
/* 147 */         BaseNotificationBroadcasterEntry item = (BaseNotificationBroadcasterEntry)items.next();
/* 148 */         if (item.listener == listener) {
/* 149 */           items.remove();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sendNotification(Notification notification)
/*     */   {
/* 163 */     synchronized (this.entries)
/*     */     {
/* 165 */       Iterator<BaseNotificationBroadcasterEntry> items = this.entries.iterator();
/* 166 */       while (items.hasNext()) {
/* 167 */         BaseNotificationBroadcasterEntry item = (BaseNotificationBroadcasterEntry)items.next();
/* 168 */         if ((item.filter == null) || 
/* 169 */           (item.filter.isNotificationEnabled(notification)))
/*     */         {
/* 171 */           item.listener.handleNotification(notification, item.handback);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\modeler\BaseNotificationBroadcaster.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */