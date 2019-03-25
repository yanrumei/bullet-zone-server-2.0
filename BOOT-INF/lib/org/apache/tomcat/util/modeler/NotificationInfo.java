/*     */ package org.apache.tomcat.util.modeler;
/*     */ 
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import javax.management.MBeanNotificationInfo;
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
/*     */ public class NotificationInfo
/*     */   extends FeatureInfo
/*     */ {
/*     */   static final long serialVersionUID = -6319885418912650856L;
/*  42 */   transient MBeanNotificationInfo info = null;
/*  43 */   protected String[] notifTypes = new String[0];
/*  44 */   protected final ReadWriteLock notifTypesLock = new ReentrantReadWriteLock();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDescription(String description)
/*     */   {
/*  56 */     super.setDescription(description);
/*  57 */     this.info = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setName(String name)
/*     */   {
/*  68 */     super.setName(name);
/*  69 */     this.info = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getNotifTypes()
/*     */   {
/*  77 */     Lock readLock = this.notifTypesLock.readLock();
/*  78 */     readLock.lock();
/*     */     try {
/*  80 */       return this.notifTypes;
/*     */     } finally {
/*  82 */       readLock.unlock();
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
/*     */ 
/*     */   public void addNotifType(String notifType)
/*     */   {
/*  97 */     Lock writeLock = this.notifTypesLock.writeLock();
/*  98 */     writeLock.lock();
/*     */     try
/*     */     {
/* 101 */       String[] results = new String[this.notifTypes.length + 1];
/* 102 */       System.arraycopy(this.notifTypes, 0, results, 0, this.notifTypes.length);
/* 103 */       results[this.notifTypes.length] = notifType;
/* 104 */       this.notifTypes = results;
/* 105 */       this.info = null;
/*     */     } finally {
/* 107 */       writeLock.unlock();
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
/*     */   public MBeanNotificationInfo createNotificationInfo()
/*     */   {
/* 120 */     if (this.info != null) {
/* 121 */       return this.info;
/*     */     }
/*     */     
/*     */ 
/* 125 */     this.info = new MBeanNotificationInfo(getNotifTypes(), getName(), getDescription());
/*     */     
/*     */ 
/*     */ 
/* 129 */     return this.info;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public String toString()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 20	java/lang/StringBuilder
/*     */     //   3: dup
/*     */     //   4: ldc 21
/*     */     //   6: invokespecial 22	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   9: astore_1
/*     */     //   10: aload_1
/*     */     //   11: ldc 23
/*     */     //   13: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   16: pop
/*     */     //   17: aload_1
/*     */     //   18: aload_0
/*     */     //   19: getfield 25	org/apache/tomcat/util/modeler/NotificationInfo:name	Ljava/lang/String;
/*     */     //   22: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   25: pop
/*     */     //   26: aload_1
/*     */     //   27: ldc 26
/*     */     //   29: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   32: pop
/*     */     //   33: aload_1
/*     */     //   34: aload_0
/*     */     //   35: getfield 27	org/apache/tomcat/util/modeler/NotificationInfo:description	Ljava/lang/String;
/*     */     //   38: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   41: pop
/*     */     //   42: aload_1
/*     */     //   43: ldc 28
/*     */     //   45: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   48: pop
/*     */     //   49: aload_0
/*     */     //   50: getfield 7	org/apache/tomcat/util/modeler/NotificationInfo:notifTypesLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   53: invokeinterface 10 1 0
/*     */     //   58: astore_2
/*     */     //   59: aload_2
/*     */     //   60: invokeinterface 11 1 0
/*     */     //   65: aload_1
/*     */     //   66: aload_0
/*     */     //   67: getfield 4	org/apache/tomcat/util/modeler/NotificationInfo:notifTypes	[Ljava/lang/String;
/*     */     //   70: arraylength
/*     */     //   71: invokevirtual 29	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*     */     //   74: pop
/*     */     //   75: aload_2
/*     */     //   76: invokeinterface 12 1 0
/*     */     //   81: goto +12 -> 93
/*     */     //   84: astore_3
/*     */     //   85: aload_2
/*     */     //   86: invokeinterface 12 1 0
/*     */     //   91: aload_3
/*     */     //   92: athrow
/*     */     //   93: aload_1
/*     */     //   94: ldc 30
/*     */     //   96: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   99: pop
/*     */     //   100: aload_1
/*     */     //   101: invokevirtual 31	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   104: areturn
/*     */     // Line number table:
/*     */     //   Java source line #140	-> byte code offset #0
/*     */     //   Java source line #141	-> byte code offset #10
/*     */     //   Java source line #142	-> byte code offset #17
/*     */     //   Java source line #143	-> byte code offset #26
/*     */     //   Java source line #144	-> byte code offset #33
/*     */     //   Java source line #145	-> byte code offset #42
/*     */     //   Java source line #146	-> byte code offset #49
/*     */     //   Java source line #147	-> byte code offset #59
/*     */     //   Java source line #149	-> byte code offset #65
/*     */     //   Java source line #151	-> byte code offset #75
/*     */     //   Java source line #152	-> byte code offset #81
/*     */     //   Java source line #151	-> byte code offset #84
/*     */     //   Java source line #153	-> byte code offset #93
/*     */     //   Java source line #154	-> byte code offset #100
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	105	0	this	NotificationInfo
/*     */     //   9	92	1	sb	StringBuilder
/*     */     //   58	28	2	readLock	Lock
/*     */     //   84	8	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   65	75	84	finally
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\modeler\NotificationInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */