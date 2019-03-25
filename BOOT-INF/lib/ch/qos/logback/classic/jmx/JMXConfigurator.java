/*     */ package ch.qos.logback.classic.jmx;
/*     */ 
/*     */ import ch.qos.logback.classic.Level;
/*     */ import ch.qos.logback.classic.Logger;
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.spi.LoggerContextListener;
/*     */ import ch.qos.logback.classic.util.ContextInitializer;
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.joran.spi.JoranException;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import ch.qos.logback.core.status.Status;
/*     */ import ch.qos.logback.core.status.StatusListener;
/*     */ import ch.qos.logback.core.status.StatusManager;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.MBeanRegistrationException;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
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
/*     */ public class JMXConfigurator
/*     */   extends ContextAwareBase
/*     */   implements JMXConfiguratorMBean, LoggerContextListener
/*     */ {
/*  56 */   private static String EMPTY = "";
/*     */   
/*     */   LoggerContext loggerContext;
/*     */   
/*     */   MBeanServer mbs;
/*     */   
/*     */   ObjectName objectName;
/*     */   
/*     */   String objectNameAsString;
/*  65 */   boolean debug = true;
/*     */   boolean started;
/*     */   
/*     */   public JMXConfigurator(LoggerContext loggerContext, MBeanServer mbs, ObjectName objectName)
/*     */   {
/*  70 */     this.started = true;
/*  71 */     this.context = loggerContext;
/*  72 */     this.loggerContext = loggerContext;
/*  73 */     this.mbs = mbs;
/*  74 */     this.objectName = objectName;
/*  75 */     this.objectNameAsString = objectName.toString();
/*  76 */     if (previouslyRegisteredListenerWithSameObjectName()) {
/*  77 */       addError("Previously registered JMXConfigurator named [" + this.objectNameAsString + "] in the logger context named [" + loggerContext.getName() + "]");
/*     */     }
/*     */     else {
/*  80 */       loggerContext.addListener(this);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean previouslyRegisteredListenerWithSameObjectName() {
/*  85 */     List<LoggerContextListener> lcll = this.loggerContext.getCopyOfListenerList();
/*  86 */     for (LoggerContextListener lcl : lcll) {
/*  87 */       if ((lcl instanceof JMXConfigurator)) {
/*  88 */         JMXConfigurator jmxConfigurator = (JMXConfigurator)lcl;
/*  89 */         if (this.objectName.equals(jmxConfigurator.objectName)) {
/*  90 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*  94 */     return false;
/*     */   }
/*     */   
/*     */   public void reloadDefaultConfiguration() throws JoranException {
/*  98 */     ContextInitializer ci = new ContextInitializer(this.loggerContext);
/*  99 */     URL url = ci.findURLOfDefaultConfigurationFile(true);
/* 100 */     reloadByURL(url);
/*     */   }
/*     */   
/*     */   public void reloadByFileName(String fileName) throws JoranException, FileNotFoundException {
/* 104 */     File f = new File(fileName);
/* 105 */     if ((f.exists()) && (f.isFile()))
/*     */     {
/*     */       try {
/* 108 */         URL url = f.toURI().toURL();
/* 109 */         reloadByURL(url);
/*     */       } catch (MalformedURLException e) {
/* 111 */         throw new RuntimeException("Unexpected MalformedURLException occured. See nexted cause.", e);
/*     */       }
/*     */     }
/*     */     else {
/* 115 */       String errMsg = "Could not find [" + fileName + "]";
/* 116 */       addInfo(errMsg);
/* 117 */       throw new FileNotFoundException(errMsg);
/*     */     }
/*     */   }
/*     */   
/*     */   void addStatusListener(StatusListener statusListener) {
/* 122 */     StatusManager sm = this.loggerContext.getStatusManager();
/* 123 */     sm.add(statusListener);
/*     */   }
/*     */   
/*     */   void removeStatusListener(StatusListener statusListener) {
/* 127 */     StatusManager sm = this.loggerContext.getStatusManager();
/* 128 */     sm.remove(statusListener);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void reloadByURL(URL url)
/*     */     throws JoranException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 209	ch/qos/logback/core/status/StatusListenerAsList
/*     */     //   3: dup
/*     */     //   4: invokespecial 211	ch/qos/logback/core/status/StatusListenerAsList:<init>	()V
/*     */     //   7: astore_2
/*     */     //   8: aload_0
/*     */     //   9: aload_2
/*     */     //   10: invokevirtual 212	ch/qos/logback/classic/jmx/JMXConfigurator:addStatusListener	(Lch/qos/logback/core/status/StatusListener;)V
/*     */     //   13: aload_0
/*     */     //   14: new 60	java/lang/StringBuilder
/*     */     //   17: dup
/*     */     //   18: ldc -42
/*     */     //   20: invokespecial 64	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   23: aload_0
/*     */     //   24: getfield 42	ch/qos/logback/classic/jmx/JMXConfigurator:loggerContext	Lch/qos/logback/classic/LoggerContext;
/*     */     //   27: invokevirtual 73	ch/qos/logback/classic/LoggerContext:getName	()Ljava/lang/String;
/*     */     //   30: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   33: invokevirtual 80	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   36: invokevirtual 175	ch/qos/logback/classic/jmx/JMXConfigurator:addInfo	(Ljava/lang/String;)V
/*     */     //   39: aload_0
/*     */     //   40: getfield 42	ch/qos/logback/classic/jmx/JMXConfigurator:loggerContext	Lch/qos/logback/classic/LoggerContext;
/*     */     //   43: invokevirtual 216	ch/qos/logback/classic/LoggerContext:reset	()V
/*     */     //   46: aload_0
/*     */     //   47: aload_2
/*     */     //   48: invokevirtual 212	ch/qos/logback/classic/jmx/JMXConfigurator:addStatusListener	(Lch/qos/logback/core/status/StatusListener;)V
/*     */     //   51: aload_1
/*     */     //   52: ifnull +82 -> 134
/*     */     //   55: new 219	ch/qos/logback/classic/joran/JoranConfigurator
/*     */     //   58: dup
/*     */     //   59: invokespecial 221	ch/qos/logback/classic/joran/JoranConfigurator:<init>	()V
/*     */     //   62: astore_3
/*     */     //   63: aload_3
/*     */     //   64: aload_0
/*     */     //   65: getfield 42	ch/qos/logback/classic/jmx/JMXConfigurator:loggerContext	Lch/qos/logback/classic/LoggerContext;
/*     */     //   68: invokevirtual 222	ch/qos/logback/classic/joran/JoranConfigurator:setContext	(Lch/qos/logback/core/Context;)V
/*     */     //   71: aload_3
/*     */     //   72: aload_1
/*     */     //   73: invokevirtual 226	ch/qos/logback/classic/joran/JoranConfigurator:doConfigure	(Ljava/net/URL;)V
/*     */     //   76: aload_0
/*     */     //   77: new 60	java/lang/StringBuilder
/*     */     //   80: dup
/*     */     //   81: ldc -27
/*     */     //   83: invokespecial 64	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   86: aload_0
/*     */     //   87: getfield 42	ch/qos/logback/classic/jmx/JMXConfigurator:loggerContext	Lch/qos/logback/classic/LoggerContext;
/*     */     //   90: invokevirtual 73	ch/qos/logback/classic/LoggerContext:getName	()Ljava/lang/String;
/*     */     //   93: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   96: ldc -25
/*     */     //   98: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   101: invokevirtual 80	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   104: invokevirtual 175	ch/qos/logback/classic/jmx/JMXConfigurator:addInfo	(Ljava/lang/String;)V
/*     */     //   107: goto +27 -> 134
/*     */     //   110: astore 4
/*     */     //   112: aload_0
/*     */     //   113: aload_2
/*     */     //   114: invokevirtual 233	ch/qos/logback/classic/jmx/JMXConfigurator:removeStatusListener	(Lch/qos/logback/core/status/StatusListener;)V
/*     */     //   117: aload_0
/*     */     //   118: getfield 34	ch/qos/logback/classic/jmx/JMXConfigurator:debug	Z
/*     */     //   121: ifeq +10 -> 131
/*     */     //   124: aload_2
/*     */     //   125: invokevirtual 235	ch/qos/logback/core/status/StatusListenerAsList:getStatusList	()Ljava/util/List;
/*     */     //   128: invokestatic 238	ch/qos/logback/core/util/StatusPrinter:print	(Ljava/util/List;)V
/*     */     //   131: aload 4
/*     */     //   133: athrow
/*     */     //   134: aload_0
/*     */     //   135: aload_2
/*     */     //   136: invokevirtual 233	ch/qos/logback/classic/jmx/JMXConfigurator:removeStatusListener	(Lch/qos/logback/core/status/StatusListener;)V
/*     */     //   139: aload_0
/*     */     //   140: getfield 34	ch/qos/logback/classic/jmx/JMXConfigurator:debug	Z
/*     */     //   143: ifeq +10 -> 153
/*     */     //   146: aload_2
/*     */     //   147: invokevirtual 235	ch/qos/logback/core/status/StatusListenerAsList:getStatusList	()Ljava/util/List;
/*     */     //   150: invokestatic 238	ch/qos/logback/core/util/StatusPrinter:print	(Ljava/util/List;)V
/*     */     //   153: return
/*     */     // Line number table:
/*     */     //   Java source line #132	-> byte code offset #0
/*     */     //   Java source line #134	-> byte code offset #8
/*     */     //   Java source line #135	-> byte code offset #13
/*     */     //   Java source line #136	-> byte code offset #39
/*     */     //   Java source line #139	-> byte code offset #46
/*     */     //   Java source line #142	-> byte code offset #51
/*     */     //   Java source line #143	-> byte code offset #55
/*     */     //   Java source line #144	-> byte code offset #63
/*     */     //   Java source line #145	-> byte code offset #71
/*     */     //   Java source line #146	-> byte code offset #76
/*     */     //   Java source line #148	-> byte code offset #107
/*     */     //   Java source line #149	-> byte code offset #112
/*     */     //   Java source line #150	-> byte code offset #117
/*     */     //   Java source line #151	-> byte code offset #124
/*     */     //   Java source line #153	-> byte code offset #131
/*     */     //   Java source line #149	-> byte code offset #134
/*     */     //   Java source line #150	-> byte code offset #139
/*     */     //   Java source line #151	-> byte code offset #146
/*     */     //   Java source line #154	-> byte code offset #153
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	154	0	this	JMXConfigurator
/*     */     //   0	154	1	url	URL
/*     */     //   7	140	2	statusListenerAsList	ch.qos.logback.core.status.StatusListenerAsList
/*     */     //   62	10	3	configurator	ch.qos.logback.classic.joran.JoranConfigurator
/*     */     //   110	22	4	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   51	110	110	finally
/*     */   }
/*     */   
/*     */   public void setLoggerLevel(String loggerName, String levelStr)
/*     */   {
/* 157 */     if (loggerName == null) {
/* 158 */       return;
/*     */     }
/* 160 */     if (levelStr == null) {
/* 161 */       return;
/*     */     }
/* 163 */     loggerName = loggerName.trim();
/* 164 */     levelStr = levelStr.trim();
/*     */     
/* 166 */     addInfo("Trying to set level " + levelStr + " to logger " + loggerName);
/* 167 */     LoggerContext lc = (LoggerContext)this.context;
/*     */     
/* 169 */     Logger logger = lc.getLogger(loggerName);
/* 170 */     if ("null".equalsIgnoreCase(levelStr)) {
/* 171 */       logger.setLevel(null);
/*     */     } else {
/* 173 */       Level level = Level.toLevel(levelStr, null);
/* 174 */       if (level != null) {
/* 175 */         logger.setLevel(level);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String getLoggerLevel(String loggerName) {
/* 181 */     if (loggerName == null) {
/* 182 */       return EMPTY;
/*     */     }
/*     */     
/* 185 */     loggerName = loggerName.trim();
/*     */     
/* 187 */     LoggerContext lc = (LoggerContext)this.context;
/* 188 */     Logger logger = lc.exists(loggerName);
/* 189 */     if ((logger != null) && (logger.getLevel() != null)) {
/* 190 */       return logger.getLevel().toString();
/*     */     }
/* 192 */     return EMPTY;
/*     */   }
/*     */   
/*     */   public String getLoggerEffectiveLevel(String loggerName)
/*     */   {
/* 197 */     if (loggerName == null) {
/* 198 */       return EMPTY;
/*     */     }
/*     */     
/* 201 */     loggerName = loggerName.trim();
/*     */     
/* 203 */     LoggerContext lc = (LoggerContext)this.context;
/* 204 */     Logger logger = lc.exists(loggerName);
/* 205 */     if (logger != null) {
/* 206 */       return logger.getEffectiveLevel().toString();
/*     */     }
/* 208 */     return EMPTY;
/*     */   }
/*     */   
/*     */   public List<String> getLoggerList()
/*     */   {
/* 213 */     LoggerContext lc = (LoggerContext)this.context;
/* 214 */     List<String> strList = new ArrayList();
/* 215 */     Iterator<Logger> it = lc.getLoggerList().iterator();
/* 216 */     while (it.hasNext()) {
/* 217 */       Logger log = (Logger)it.next();
/* 218 */       strList.add(log.getName());
/*     */     }
/* 220 */     return strList;
/*     */   }
/*     */   
/*     */   public List<String> getStatuses() {
/* 224 */     List<String> list = new ArrayList();
/* 225 */     Iterator<Status> it = this.context.getStatusManager().getCopyOfStatusList().iterator();
/* 226 */     while (it.hasNext()) {
/* 227 */       list.add(((Status)it.next()).toString());
/*     */     }
/* 229 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onStop(LoggerContext context)
/*     */   {
/* 237 */     if (!this.started) {
/* 238 */       addInfo("onStop() method called on a stopped JMXActivator [" + this.objectNameAsString + "]");
/* 239 */       return;
/*     */     }
/* 241 */     if (this.mbs.isRegistered(this.objectName)) {
/*     */       try {
/* 243 */         addInfo("Unregistering mbean [" + this.objectNameAsString + "]");
/* 244 */         this.mbs.unregisterMBean(this.objectName);
/*     */       }
/*     */       catch (InstanceNotFoundException e) {
/* 247 */         addError("Unable to find a verifiably registered mbean [" + this.objectNameAsString + "]", e);
/*     */       } catch (MBeanRegistrationException e) {
/* 249 */         addError("Failed to unregister [" + this.objectNameAsString + "]", e);
/*     */       }
/*     */     } else {
/* 252 */       addInfo("mbean [" + this.objectNameAsString + "] was not in the mbean registry. This is OK.");
/*     */     }
/* 254 */     stop();
/*     */   }
/*     */   
/*     */ 
/*     */   public void onLevelChange(Logger logger, Level level) {}
/*     */   
/*     */   public void onReset(LoggerContext context)
/*     */   {
/* 262 */     addInfo("onReset() method called JMXActivator [" + this.objectNameAsString + "]");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isResetResistant()
/*     */   {
/* 271 */     return true;
/*     */   }
/*     */   
/*     */   private void clearFields() {
/* 275 */     this.mbs = null;
/* 276 */     this.objectName = null;
/* 277 */     this.loggerContext = null;
/*     */   }
/*     */   
/*     */   private void stop() {
/* 281 */     this.started = false;
/* 282 */     clearFields();
/*     */   }
/*     */   
/*     */ 
/*     */   public void onStart(LoggerContext context) {}
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 291 */     return getClass().getName() + "(" + this.context.getName() + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\jmx\JMXConfigurator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */