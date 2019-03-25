/*     */ package org.apache.tomcat.util.modeler;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import javax.management.AttributeChangeNotification;
/*     */ import javax.management.Notification;
/*     */ import javax.management.NotificationFilter;
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
/*     */ public class BaseAttributeFilter
/*     */   implements NotificationFilter
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public BaseAttributeFilter(String name)
/*     */   {
/*  53 */     if (name != null) {
/*  54 */       addAttribute(name);
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
/*  66 */   private HashSet<String> names = new HashSet();
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
/*     */   public void addAttribute(String name)
/*     */   {
/*  79 */     synchronized (this.names) {
/*  80 */       this.names.add(name);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clear()
/*     */   {
/*  92 */     synchronized (this.names) {
/*  93 */       this.names.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public String[] getNames()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 4	org/apache/tomcat/util/modeler/BaseAttributeFilter:names	Ljava/util/HashSet;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 4	org/apache/tomcat/util/modeler/BaseAttributeFilter:names	Ljava/util/HashSet;
/*     */     //   11: aload_0
/*     */     //   12: getfield 4	org/apache/tomcat/util/modeler/BaseAttributeFilter:names	Ljava/util/HashSet;
/*     */     //   15: invokevirtual 8	java/util/HashSet:size	()I
/*     */     //   18: anewarray 9	java/lang/String
/*     */     //   21: invokevirtual 10	java/util/HashSet:toArray	([Ljava/lang/Object;)[Ljava/lang/Object;
/*     */     //   24: checkcast 11	[Ljava/lang/String;
/*     */     //   27: aload_1
/*     */     //   28: monitorexit
/*     */     //   29: areturn
/*     */     //   30: astore_2
/*     */     //   31: aload_1
/*     */     //   32: monitorexit
/*     */     //   33: aload_2
/*     */     //   34: athrow
/*     */     // Line number table:
/*     */     //   Java source line #107	-> byte code offset #0
/*     */     //   Java source line #108	-> byte code offset #7
/*     */     //   Java source line #109	-> byte code offset #30
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	35	0	this	BaseAttributeFilter
/*     */     //   5	27	1	Ljava/lang/Object;	Object
/*     */     //   30	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	29	30	finally
/*     */     //   30	33	30	finally
/*     */   }
/*     */   
/*     */   public boolean isNotificationEnabled(Notification notification)
/*     */   {
/* 127 */     if (notification == null)
/* 128 */       return false;
/* 129 */     if (!(notification instanceof AttributeChangeNotification))
/* 130 */       return false;
/* 131 */     AttributeChangeNotification acn = (AttributeChangeNotification)notification;
/*     */     
/* 133 */     if (!"jmx.attribute.change".equals(acn.getType()))
/* 134 */       return false;
/* 135 */     synchronized (this.names) {
/* 136 */       if (this.names.size() < 1) {
/* 137 */         return true;
/*     */       }
/* 139 */       return this.names.contains(acn.getAttributeName());
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
/*     */   public void removeAttribute(String name)
/*     */   {
/* 153 */     synchronized (this.names) {
/* 154 */       this.names.remove(name);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\modeler\BaseAttributeFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */