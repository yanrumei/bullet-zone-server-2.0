/*     */ package org.springframework.boot;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.RuntimeMXBean;
/*     */ import java.net.InetAddress;
/*     */ import java.util.concurrent.Callable;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StopWatch;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ class StartupInfoLogger
/*     */ {
/*     */   private final Class<?> sourceClass;
/*     */   
/*     */   StartupInfoLogger(Class<?> sourceClass)
/*     */   {
/*  42 */     this.sourceClass = sourceClass;
/*     */   }
/*     */   
/*     */   public void logStarting(Log log) {
/*  46 */     Assert.notNull(log, "Log must not be null");
/*  47 */     if (log.isInfoEnabled()) {
/*  48 */       log.info(getStartupMessage());
/*     */     }
/*  50 */     if (log.isDebugEnabled()) {
/*  51 */       log.debug(getRunningMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   public void logStarted(Log log, StopWatch stopWatch) {
/*  56 */     if (log.isInfoEnabled()) {
/*  57 */       log.info(getStartedMessage(stopWatch));
/*     */     }
/*     */   }
/*     */   
/*     */   private String getStartupMessage() {
/*  62 */     StringBuilder message = new StringBuilder();
/*  63 */     message.append("Starting ");
/*  64 */     message.append(getApplicationName());
/*  65 */     message.append(getVersion(this.sourceClass));
/*  66 */     message.append(getOn());
/*  67 */     message.append(getPid());
/*  68 */     message.append(getContext());
/*  69 */     return message.toString();
/*     */   }
/*     */   
/*     */   private StringBuilder getRunningMessage() {
/*  73 */     StringBuilder message = new StringBuilder();
/*  74 */     message.append("Running with Spring Boot");
/*  75 */     message.append(getVersion(getClass()));
/*  76 */     message.append(", Spring");
/*  77 */     message.append(getVersion(ApplicationContext.class));
/*  78 */     return message;
/*     */   }
/*     */   
/*     */   private StringBuilder getStartedMessage(StopWatch stopWatch) {
/*  82 */     StringBuilder message = new StringBuilder();
/*  83 */     message.append("Started ");
/*  84 */     message.append(getApplicationName());
/*  85 */     message.append(" in ");
/*  86 */     message.append(stopWatch.getTotalTimeSeconds());
/*     */     try {
/*  88 */       double uptime = ManagementFactory.getRuntimeMXBean().getUptime() / 1000.0D;
/*  89 */       message.append(" seconds (JVM running for " + uptime + ")");
/*     */     }
/*     */     catch (Throwable localThrowable) {}
/*     */     
/*     */ 
/*  94 */     return message;
/*     */   }
/*     */   
/*     */   private String getApplicationName() {
/*  98 */     return this.sourceClass != null ? ClassUtils.getShortName(this.sourceClass) : "application";
/*     */   }
/*     */   
/*     */   private String getVersion(final Class<?> source)
/*     */   {
/* 103 */     getValue(" v", new Callable()
/*     */     {
/*     */ 
/* 106 */       public Object call() throws Exception { return source.getPackage().getImplementationVersion(); } }, "");
/*     */   }
/*     */   
/*     */ 
/*     */   private String getOn()
/*     */   {
/* 112 */     getValue(" on ", new Callable()
/*     */     {
/*     */       public Object call() throws Exception {
/* 115 */         return InetAddress.getLocalHost().getHostName();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private String getPid() {
/* 121 */     getValue(" with PID ", new Callable()
/*     */     {
/*     */       public Object call() throws Exception {
/* 124 */         return System.getProperty("PID");
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private String getContext() {
/* 130 */     String startedBy = getValue("started by ", new Callable()
/*     */     {
/*     */       public Object call() throws Exception {
/* 133 */         return System.getProperty("user.name");
/*     */       }
/* 135 */     });
/* 136 */     String in = getValue("in ", new Callable()
/*     */     {
/*     */       public Object call() throws Exception {
/* 139 */         return System.getProperty("user.dir");
/*     */       }
/* 141 */     });
/* 142 */     ApplicationHome home = new ApplicationHome(this.sourceClass);
/*     */     
/* 144 */     String path = home.getSource() == null ? "" : home.getSource().getAbsolutePath();
/* 145 */     if ((startedBy == null) && (path == null)) {
/* 146 */       return "";
/*     */     }
/* 148 */     if ((StringUtils.hasLength(startedBy)) && (StringUtils.hasLength(path))) {
/* 149 */       startedBy = " " + startedBy;
/*     */     }
/* 151 */     if ((StringUtils.hasLength(in)) && (StringUtils.hasLength(startedBy))) {
/* 152 */       in = " " + in;
/*     */     }
/* 154 */     return " (" + path + startedBy + in + ")";
/*     */   }
/*     */   
/*     */   private String getValue(String prefix, Callable<Object> call) {
/* 158 */     return getValue(prefix, call, "");
/*     */   }
/*     */   
/*     */   private String getValue(String prefix, Callable<Object> call, String defaultValue) {
/*     */     try {
/* 163 */       Object value = call.call();
/* 164 */       if ((value != null) && (StringUtils.hasLength(value.toString()))) {
/* 165 */         return prefix + value;
/*     */       }
/*     */     }
/*     */     catch (Exception localException) {}
/*     */     
/*     */ 
/* 171 */     return defaultValue;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\StartupInfoLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */