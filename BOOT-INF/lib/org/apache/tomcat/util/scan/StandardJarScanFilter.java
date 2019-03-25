/*     */ package org.apache.tomcat.util.scan;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import org.apache.tomcat.JarScanFilter;
/*     */ import org.apache.tomcat.JarScanType;
/*     */ import org.apache.tomcat.util.file.Matcher;
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
/*     */ public class StandardJarScanFilter
/*     */   implements JarScanFilter
/*     */ {
/*  32 */   private final ReadWriteLock configurationLock = new ReentrantReadWriteLock();
/*     */   
/*     */   private static final String defaultSkip;
/*     */   
/*     */   private static final String defaultScan;
/*  37 */   private static final Set<String> defaultSkipSet = new HashSet();
/*  38 */   private static final Set<String> defaultScanSet = new HashSet();
/*     */   private String tldSkip;
/*     */   private String tldScan;
/*     */   
/*  42 */   static { defaultSkip = System.getProperty("tomcat.util.scan.StandardJarScanFilter.jarsToSkip");
/*  43 */     populateSetFromAttribute(defaultSkip, defaultSkipSet);
/*  44 */     defaultScan = System.getProperty("tomcat.util.scan.StandardJarScanFilter.jarsToScan");
/*  45 */     populateSetFromAttribute(defaultScan, defaultScanSet);
/*     */   }
/*     */   
/*     */ 
/*     */   private final Set<String> tldSkipSet;
/*     */   
/*     */   private final Set<String> tldScanSet;
/*  52 */   private boolean defaultTldScan = true;
/*     */   
/*     */   private String pluggabilitySkip;
/*     */   private String pluggabilityScan;
/*     */   private final Set<String> pluggabilitySkipSet;
/*     */   private final Set<String> pluggabilityScanSet;
/*  58 */   private boolean defaultPluggabilityScan = true;
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
/*     */   public StandardJarScanFilter()
/*     */   {
/*  90 */     this.tldSkip = defaultSkip;
/*  91 */     this.tldSkipSet = new HashSet(defaultSkipSet);
/*  92 */     this.tldScan = defaultScan;
/*  93 */     this.tldScanSet = new HashSet(defaultScanSet);
/*  94 */     this.pluggabilitySkip = defaultSkip;
/*  95 */     this.pluggabilitySkipSet = new HashSet(defaultSkipSet);
/*  96 */     this.pluggabilityScan = defaultScan;
/*  97 */     this.pluggabilityScanSet = new HashSet(defaultScanSet);
/*     */   }
/*     */   
/*     */   public String getTldSkip()
/*     */   {
/* 102 */     return this.tldSkip;
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
/*     */   public String getTldScan()
/*     */   {
/* 119 */     return this.tldScan;
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
/*     */   public boolean isDefaultTldScan()
/*     */   {
/* 136 */     return this.defaultTldScan;
/*     */   }
/*     */   
/*     */   public void setDefaultTldScan(boolean defaultTldScan)
/*     */   {
/* 141 */     this.defaultTldScan = defaultTldScan;
/*     */   }
/*     */   
/*     */   public String getPluggabilitySkip()
/*     */   {
/* 146 */     return this.pluggabilitySkip;
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
/*     */   public String getPluggabilityScan()
/*     */   {
/* 163 */     return this.pluggabilityScan;
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
/*     */   public boolean isDefaultPluggabilityScan()
/*     */   {
/* 180 */     return this.defaultPluggabilityScan;
/*     */   }
/*     */   
/*     */   public void setDefaultPluggabilityScan(boolean defaultPluggabilityScan)
/*     */   {
/* 185 */     this.defaultPluggabilityScan = defaultPluggabilityScan;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean check(JarScanType jarScanType, String jarName)
/*     */   {
/* 191 */     Lock readLock = this.configurationLock.readLock();
/* 192 */     readLock.lock();
/*     */     try { Set<String> toScan;
/*     */       Set<String> toScan;
/*     */       boolean defaultScan;
/*     */       Set<String> toSkip;
/* 197 */       Set<String> toScan; switch (jarScanType) {
/*     */       case TLD: 
/* 199 */         boolean defaultScan = this.defaultTldScan;
/* 200 */         Set<String> toSkip = this.tldSkipSet;
/* 201 */         toScan = this.tldScanSet;
/* 202 */         break;
/*     */       
/*     */       case PLUGGABILITY: 
/* 205 */         boolean defaultScan = this.defaultPluggabilityScan;
/* 206 */         Set<String> toSkip = this.pluggabilitySkipSet;
/* 207 */         toScan = this.pluggabilityScanSet;
/* 208 */         break;
/*     */       
/*     */       case OTHER: 
/*     */       default: 
/* 212 */         defaultScan = true;
/* 213 */         toSkip = defaultSkipSet;
/* 214 */         toScan = defaultScanSet;
/*     */       }
/*     */       boolean bool1;
/* 217 */       if (defaultScan) {
/* 218 */         if (Matcher.matchName(toSkip, jarName)) {
/* 219 */           if (Matcher.matchName(toScan, jarName)) {
/* 220 */             return true;
/*     */           }
/* 222 */           return false;
/*     */         }
/*     */         
/* 225 */         return true;
/*     */       }
/* 227 */       if (Matcher.matchName(toScan, jarName)) {
/* 228 */         if (Matcher.matchName(toSkip, jarName)) {
/* 229 */           return false;
/*     */         }
/* 231 */         return true;
/*     */       }
/*     */       
/* 234 */       return false;
/*     */     }
/*     */     finally {
/* 237 */       readLock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   private static void populateSetFromAttribute(String attribute, Set<String> set) {
/* 242 */     set.clear();
/* 243 */     if (attribute != null) {
/* 244 */       StringTokenizer tokenizer = new StringTokenizer(attribute, ",");
/* 245 */       while (tokenizer.hasMoreElements()) {
/* 246 */         String token = tokenizer.nextToken().trim();
/* 247 */         if (token.length() > 0) {
/* 248 */           set.add(token);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void setTldSkip(String tldSkip)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: putfield 8	org/apache/tomcat/util/scan/StandardJarScanFilter:tldSkip	Ljava/lang/String;
/*     */     //   5: aload_0
/*     */     //   6: getfield 4	org/apache/tomcat/util/scan/StandardJarScanFilter:configurationLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   9: invokeinterface 21 1 0
/*     */     //   14: astore_2
/*     */     //   15: aload_2
/*     */     //   16: invokeinterface 22 1 0
/*     */     //   21: aload_1
/*     */     //   22: aload_0
/*     */     //   23: getfield 12	org/apache/tomcat/util/scan/StandardJarScanFilter:tldSkipSet	Ljava/util/Set;
/*     */     //   26: invokestatic 23	org/apache/tomcat/util/scan/StandardJarScanFilter:populateSetFromAttribute	(Ljava/lang/String;Ljava/util/Set;)V
/*     */     //   29: aload_2
/*     */     //   30: invokeinterface 24 1 0
/*     */     //   35: goto +12 -> 47
/*     */     //   38: astore_3
/*     */     //   39: aload_2
/*     */     //   40: invokeinterface 24 1 0
/*     */     //   45: aload_3
/*     */     //   46: athrow
/*     */     //   47: return
/*     */     // Line number table:
/*     */     //   Java source line #107	-> byte code offset #0
/*     */     //   Java source line #108	-> byte code offset #5
/*     */     //   Java source line #109	-> byte code offset #15
/*     */     //   Java source line #111	-> byte code offset #21
/*     */     //   Java source line #113	-> byte code offset #29
/*     */     //   Java source line #114	-> byte code offset #35
/*     */     //   Java source line #113	-> byte code offset #38
/*     */     //   Java source line #115	-> byte code offset #47
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	48	0	this	StandardJarScanFilter
/*     */     //   0	48	1	tldSkip	String
/*     */     //   14	26	2	writeLock	Lock
/*     */     //   38	8	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   21	29	38	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void setTldScan(String tldScan)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: putfield 14	org/apache/tomcat/util/scan/StandardJarScanFilter:tldScan	Ljava/lang/String;
/*     */     //   5: aload_0
/*     */     //   6: getfield 4	org/apache/tomcat/util/scan/StandardJarScanFilter:configurationLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   9: invokeinterface 21 1 0
/*     */     //   14: astore_2
/*     */     //   15: aload_2
/*     */     //   16: invokeinterface 22 1 0
/*     */     //   21: aload_1
/*     */     //   22: aload_0
/*     */     //   23: getfield 16	org/apache/tomcat/util/scan/StandardJarScanFilter:tldScanSet	Ljava/util/Set;
/*     */     //   26: invokestatic 23	org/apache/tomcat/util/scan/StandardJarScanFilter:populateSetFromAttribute	(Ljava/lang/String;Ljava/util/Set;)V
/*     */     //   29: aload_2
/*     */     //   30: invokeinterface 24 1 0
/*     */     //   35: goto +12 -> 47
/*     */     //   38: astore_3
/*     */     //   39: aload_2
/*     */     //   40: invokeinterface 24 1 0
/*     */     //   45: aload_3
/*     */     //   46: athrow
/*     */     //   47: return
/*     */     // Line number table:
/*     */     //   Java source line #124	-> byte code offset #0
/*     */     //   Java source line #125	-> byte code offset #5
/*     */     //   Java source line #126	-> byte code offset #15
/*     */     //   Java source line #128	-> byte code offset #21
/*     */     //   Java source line #130	-> byte code offset #29
/*     */     //   Java source line #131	-> byte code offset #35
/*     */     //   Java source line #130	-> byte code offset #38
/*     */     //   Java source line #132	-> byte code offset #47
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	48	0	this	StandardJarScanFilter
/*     */     //   0	48	1	tldScan	String
/*     */     //   14	26	2	writeLock	Lock
/*     */     //   38	8	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   21	29	38	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void setPluggabilitySkip(String pluggabilitySkip)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: putfield 17	org/apache/tomcat/util/scan/StandardJarScanFilter:pluggabilitySkip	Ljava/lang/String;
/*     */     //   5: aload_0
/*     */     //   6: getfield 4	org/apache/tomcat/util/scan/StandardJarScanFilter:configurationLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   9: invokeinterface 21 1 0
/*     */     //   14: astore_2
/*     */     //   15: aload_2
/*     */     //   16: invokeinterface 22 1 0
/*     */     //   21: aload_1
/*     */     //   22: aload_0
/*     */     //   23: getfield 18	org/apache/tomcat/util/scan/StandardJarScanFilter:pluggabilitySkipSet	Ljava/util/Set;
/*     */     //   26: invokestatic 23	org/apache/tomcat/util/scan/StandardJarScanFilter:populateSetFromAttribute	(Ljava/lang/String;Ljava/util/Set;)V
/*     */     //   29: aload_2
/*     */     //   30: invokeinterface 24 1 0
/*     */     //   35: goto +12 -> 47
/*     */     //   38: astore_3
/*     */     //   39: aload_2
/*     */     //   40: invokeinterface 24 1 0
/*     */     //   45: aload_3
/*     */     //   46: athrow
/*     */     //   47: return
/*     */     // Line number table:
/*     */     //   Java source line #151	-> byte code offset #0
/*     */     //   Java source line #152	-> byte code offset #5
/*     */     //   Java source line #153	-> byte code offset #15
/*     */     //   Java source line #155	-> byte code offset #21
/*     */     //   Java source line #157	-> byte code offset #29
/*     */     //   Java source line #158	-> byte code offset #35
/*     */     //   Java source line #157	-> byte code offset #38
/*     */     //   Java source line #159	-> byte code offset #47
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	48	0	this	StandardJarScanFilter
/*     */     //   0	48	1	pluggabilitySkip	String
/*     */     //   14	26	2	writeLock	Lock
/*     */     //   38	8	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   21	29	38	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void setPluggabilityScan(String pluggabilityScan)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: putfield 19	org/apache/tomcat/util/scan/StandardJarScanFilter:pluggabilityScan	Ljava/lang/String;
/*     */     //   5: aload_0
/*     */     //   6: getfield 4	org/apache/tomcat/util/scan/StandardJarScanFilter:configurationLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   9: invokeinterface 21 1 0
/*     */     //   14: astore_2
/*     */     //   15: aload_2
/*     */     //   16: invokeinterface 22 1 0
/*     */     //   21: aload_1
/*     */     //   22: aload_0
/*     */     //   23: getfield 20	org/apache/tomcat/util/scan/StandardJarScanFilter:pluggabilityScanSet	Ljava/util/Set;
/*     */     //   26: invokestatic 23	org/apache/tomcat/util/scan/StandardJarScanFilter:populateSetFromAttribute	(Ljava/lang/String;Ljava/util/Set;)V
/*     */     //   29: aload_2
/*     */     //   30: invokeinterface 24 1 0
/*     */     //   35: goto +12 -> 47
/*     */     //   38: astore_3
/*     */     //   39: aload_2
/*     */     //   40: invokeinterface 24 1 0
/*     */     //   45: aload_3
/*     */     //   46: athrow
/*     */     //   47: return
/*     */     // Line number table:
/*     */     //   Java source line #168	-> byte code offset #0
/*     */     //   Java source line #169	-> byte code offset #5
/*     */     //   Java source line #170	-> byte code offset #15
/*     */     //   Java source line #172	-> byte code offset #21
/*     */     //   Java source line #174	-> byte code offset #29
/*     */     //   Java source line #175	-> byte code offset #35
/*     */     //   Java source line #174	-> byte code offset #38
/*     */     //   Java source line #176	-> byte code offset #47
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	48	0	this	StandardJarScanFilter
/*     */     //   0	48	1	pluggabilityScan	String
/*     */     //   14	26	2	writeLock	Lock
/*     */     //   38	8	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   21	29	38	finally
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\scan\StandardJarScanFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */