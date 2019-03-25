/*     */ package org.apache.tomcat.util.log;
/*     */ 
/*     */ import org.apache.juli.logging.Log;
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
/*     */ public class UserDataHelper
/*     */ {
/*     */   private final Log log;
/*     */   private final Config config;
/*     */   private final long suppressionTime;
/*  50 */   private volatile long lastInfoTime = 0L;
/*     */   
/*     */   public UserDataHelper(Log log)
/*     */   {
/*  54 */     this.log = log;
/*     */     
/*     */ 
/*  57 */     String configString = System.getProperty("org.apache.juli.logging.UserDataHelper.CONFIG");
/*     */     Config tempConfig;
/*  59 */     Config tempConfig; if (configString == null) {
/*  60 */       tempConfig = Config.INFO_THEN_DEBUG;
/*     */     } else {
/*     */       try {
/*  63 */         tempConfig = Config.valueOf(configString);
/*     */       } catch (IllegalArgumentException iae) {
/*     */         Config tempConfig;
/*  66 */         tempConfig = Config.INFO_THEN_DEBUG;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  73 */     this.suppressionTime = (Integer.getInteger("org.apache.juli.logging.UserDataHelper.SUPPRESSION_TIME", 86400).intValue() * 1000L);
/*     */     
/*  75 */     if (this.suppressionTime == 0L) {
/*  76 */       tempConfig = Config.INFO_ALL;
/*     */     }
/*     */     
/*  79 */     this.config = tempConfig;
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
/*     */   public Mode getNextMode()
/*     */   {
/*  94 */     if (Config.NONE == this.config)
/*  95 */       return null;
/*  96 */     if (Config.DEBUG_ALL == this.config)
/*  97 */       return this.log.isDebugEnabled() ? Mode.DEBUG : null;
/*  98 */     if (Config.INFO_THEN_DEBUG == this.config) {
/*  99 */       if (logAtInfo()) {
/* 100 */         return this.log.isInfoEnabled() ? Mode.INFO_THEN_DEBUG : null;
/*     */       }
/* 102 */       return this.log.isDebugEnabled() ? Mode.DEBUG : null;
/*     */     }
/* 104 */     if (Config.INFO_ALL == this.config) {
/* 105 */       return this.log.isInfoEnabled() ? Mode.INFO : null;
/*     */     }
/*     */     
/* 108 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean logAtInfo()
/*     */   {
/* 119 */     if ((this.suppressionTime < 0L) && (this.lastInfoTime > 0L)) {
/* 120 */       return false;
/*     */     }
/*     */     
/* 123 */     long now = System.currentTimeMillis();
/*     */     
/* 125 */     if (this.lastInfoTime + this.suppressionTime > now) {
/* 126 */       return false;
/*     */     }
/*     */     
/* 129 */     this.lastInfoTime = now;
/* 130 */     return true;
/*     */   }
/*     */   
/*     */   private static enum Config
/*     */   {
/* 135 */     NONE, 
/* 136 */     DEBUG_ALL, 
/* 137 */     INFO_THEN_DEBUG, 
/* 138 */     INFO_ALL;
/*     */     
/*     */     private Config() {}
/*     */   }
/*     */   
/*     */   public static enum Mode
/*     */   {
/* 145 */     DEBUG, 
/* 146 */     INFO_THEN_DEBUG, 
/* 147 */     INFO;
/*     */     
/*     */     private Mode() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\log\UserDataHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */