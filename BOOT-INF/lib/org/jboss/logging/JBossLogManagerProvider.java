/*     */ package org.jboss.logging;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.jboss.logmanager.LogContext;
/*     */ import org.jboss.logmanager.Logger.AttachmentKey;
/*     */ import org.jboss.logmanager.MDC;
/*     */ import org.jboss.logmanager.NDC;
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
/*     */ final class JBossLogManagerProvider
/*     */   implements LoggerProvider
/*     */ {
/*  35 */   private static final Logger.AttachmentKey<Logger> KEY = new Logger.AttachmentKey();
/*  36 */   private static final Logger.AttachmentKey<ConcurrentMap<String, Logger>> LEGACY_KEY = new Logger.AttachmentKey();
/*     */   
/*     */   public Logger getLogger(final String name) {
/*  39 */     SecurityManager sm = System.getSecurityManager();
/*  40 */     if (sm != null) {
/*  41 */       (Logger)AccessController.doPrivileged(new PrivilegedAction() {
/*     */         public Logger run() {
/*     */           try {
/*  44 */             return JBossLogManagerProvider.doGetLogger(name);
/*     */           }
/*     */           catch (NoSuchMethodError localNoSuchMethodError) {}
/*     */           
/*  48 */           return JBossLogManagerProvider.doLegacyGetLogger(name);
/*     */         }
/*     */       });
/*     */     }
/*     */     try {
/*  53 */       return doGetLogger(name);
/*     */     }
/*     */     catch (NoSuchMethodError localNoSuchMethodError) {}
/*     */     
/*  57 */     return doLegacyGetLogger(name);
/*     */   }
/*     */   
/*     */   private static Logger doLegacyGetLogger(String name)
/*     */   {
/*  62 */     org.jboss.logmanager.Logger lmLogger = LogContext.getLogContext().getLogger("");
/*  63 */     ConcurrentMap<String, Logger> loggers = (ConcurrentMap)lmLogger.getAttachment(LEGACY_KEY);
/*  64 */     if (loggers == null) {
/*  65 */       loggers = new ConcurrentHashMap();
/*  66 */       ConcurrentMap<String, Logger> appearing = (ConcurrentMap)lmLogger.attachIfAbsent(LEGACY_KEY, loggers);
/*  67 */       if (appearing != null) {
/*  68 */         loggers = appearing;
/*     */       }
/*     */     }
/*     */     
/*  72 */     Logger l = (Logger)loggers.get(name);
/*  73 */     if (l != null) {
/*  74 */       return l;
/*     */     }
/*     */     
/*  77 */     org.jboss.logmanager.Logger logger = org.jboss.logmanager.Logger.getLogger(name);
/*  78 */     l = new JBossLogManagerLogger(name, logger);
/*  79 */     Logger appearing = (Logger)loggers.putIfAbsent(name, l);
/*  80 */     if (appearing == null) {
/*  81 */       return l;
/*     */     }
/*  83 */     return appearing;
/*     */   }
/*     */   
/*     */   private static Logger doGetLogger(String name) {
/*  87 */     Logger l = (Logger)LogContext.getLogContext().getAttachment(name, KEY);
/*  88 */     if (l != null) {
/*  89 */       return l;
/*     */     }
/*  91 */     org.jboss.logmanager.Logger logger = org.jboss.logmanager.Logger.getLogger(name);
/*  92 */     l = new JBossLogManagerLogger(name, logger);
/*  93 */     Logger a = (Logger)logger.attachIfAbsent(KEY, l);
/*  94 */     if (a == null) {
/*  95 */       return l;
/*     */     }
/*  97 */     return a;
/*     */   }
/*     */   
/*     */ 
/*     */   public void clearMdc() {}
/*     */   
/*     */ 
/*     */   public Object putMdc(String key, Object value)
/*     */   {
/* 106 */     return MDC.put(key, String.valueOf(value));
/*     */   }
/*     */   
/*     */   public Object getMdc(String key) {
/* 110 */     return MDC.get(key);
/*     */   }
/*     */   
/*     */   public void removeMdc(String key) {
/* 114 */     MDC.remove(key);
/*     */   }
/*     */   
/*     */ 
/*     */   public Map<String, Object> getMdcMap()
/*     */   {
/* 120 */     return MDC.copy();
/*     */   }
/*     */   
/*     */ 
/*     */   public void clearNdc() {}
/*     */   
/*     */   public String getNdc()
/*     */   {
/* 128 */     return NDC.get();
/*     */   }
/*     */   
/*     */   public int getNdcDepth() {
/* 132 */     return NDC.getDepth();
/*     */   }
/*     */   
/*     */   public String popNdc() {
/* 136 */     return NDC.pop();
/*     */   }
/*     */   
/*     */   public String peekNdc() {
/* 140 */     return NDC.get();
/*     */   }
/*     */   
/*     */   public void pushNdc(String message) {
/* 144 */     NDC.push(message);
/*     */   }
/*     */   
/*     */   public void setNdcMaxDepth(int maxDepth) {
/* 148 */     NDC.trimTo(maxDepth);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jboss-logging-3.3.1.Final.jar!\org\jboss\logging\JBossLogManagerProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */