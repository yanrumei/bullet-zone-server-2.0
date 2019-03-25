/*     */ package org.apache.catalina.startup;
/*     */ 
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.RuntimeMXBean;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import org.apache.catalina.LifecycleEvent;
/*     */ import org.apache.catalina.LifecycleListener;
/*     */ import org.apache.catalina.util.ServerInfo;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.res.StringManager;
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
/*     */ public class VersionLoggerListener
/*     */   implements LifecycleListener
/*     */ {
/*  38 */   private static final Log log = LogFactory.getLog(VersionLoggerListener.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  43 */   protected static final StringManager sm = StringManager.getManager("org.apache.catalina.startup");
/*     */   
/*  45 */   private boolean logArgs = true;
/*  46 */   private boolean logEnv = false;
/*  47 */   private boolean logProps = false;
/*     */   
/*     */   public boolean getLogArgs()
/*     */   {
/*  51 */     return this.logArgs;
/*     */   }
/*     */   
/*     */   public void setLogArgs(boolean logArgs)
/*     */   {
/*  56 */     this.logArgs = logArgs;
/*     */   }
/*     */   
/*     */   public boolean getLogEnv()
/*     */   {
/*  61 */     return this.logEnv;
/*     */   }
/*     */   
/*     */   public void setLogEnv(boolean logEnv)
/*     */   {
/*  66 */     this.logEnv = logEnv;
/*     */   }
/*     */   
/*     */   public boolean getLogProps()
/*     */   {
/*  71 */     return this.logProps;
/*     */   }
/*     */   
/*     */   public void setLogProps(boolean logProps)
/*     */   {
/*  76 */     this.logProps = logProps;
/*     */   }
/*     */   
/*     */ 
/*     */   public void lifecycleEvent(LifecycleEvent event)
/*     */   {
/*  82 */     if ("before_init".equals(event.getType())) {
/*  83 */       log();
/*     */     }
/*     */   }
/*     */   
/*     */   private void log()
/*     */   {
/*  89 */     log.info(sm.getString("versionLoggerListener.serverInfo.server.version", new Object[] {
/*  90 */       ServerInfo.getServerInfo() }));
/*  91 */     log.info(sm.getString("versionLoggerListener.serverInfo.server.built", new Object[] {
/*  92 */       ServerInfo.getServerBuilt() }));
/*  93 */     log.info(sm.getString("versionLoggerListener.serverInfo.server.number", new Object[] {
/*  94 */       ServerInfo.getServerNumber() }));
/*  95 */     log.info(sm.getString("versionLoggerListener.os.name", new Object[] {
/*  96 */       System.getProperty("os.name") }));
/*  97 */     log.info(sm.getString("versionLoggerListener.os.version", new Object[] {
/*  98 */       System.getProperty("os.version") }));
/*  99 */     log.info(sm.getString("versionLoggerListener.os.arch", new Object[] {
/* 100 */       System.getProperty("os.arch") }));
/* 101 */     log.info(sm.getString("versionLoggerListener.java.home", new Object[] {
/* 102 */       System.getProperty("java.home") }));
/* 103 */     log.info(sm.getString("versionLoggerListener.vm.version", new Object[] {
/* 104 */       System.getProperty("java.runtime.version") }));
/* 105 */     log.info(sm.getString("versionLoggerListener.vm.vendor", new Object[] {
/* 106 */       System.getProperty("java.vm.vendor") }));
/* 107 */     log.info(sm.getString("versionLoggerListener.catalina.base", new Object[] {
/* 108 */       System.getProperty("catalina.base") }));
/* 109 */     log.info(sm.getString("versionLoggerListener.catalina.home", new Object[] {
/* 110 */       System.getProperty("catalina.home") }));
/*     */     
/* 112 */     if (this.logArgs) {
/* 113 */       List<String> args = ManagementFactory.getRuntimeMXBean().getInputArguments();
/* 114 */       for (String arg : args) {
/* 115 */         log.info(sm.getString("versionLoggerListener.arg", new Object[] { arg }));
/*     */       }
/*     */     }
/*     */     
/* 119 */     if (this.logEnv) {
/* 120 */       SortedMap<String, String> sortedMap = new TreeMap(System.getenv());
/* 121 */       for (Map.Entry<String, String> e : sortedMap.entrySet()) {
/* 122 */         log.info(sm.getString("versionLoggerListener.env", new Object[] { e.getKey(), e.getValue() }));
/*     */       }
/*     */     }
/*     */     
/* 126 */     if (this.logProps) {
/* 127 */       SortedMap<String, String> sortedMap = new TreeMap();
/* 128 */       for (Map.Entry<Object, Object> e : System.getProperties().entrySet()) {
/* 129 */         sortedMap.put(String.valueOf(e.getKey()), String.valueOf(e.getValue()));
/*     */       }
/* 131 */       for (Map.Entry<String, String> e : sortedMap.entrySet()) {
/* 132 */         log.info(sm.getString("versionLoggerListener.prop", new Object[] { e.getKey(), e.getValue() }));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\VersionLoggerListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */