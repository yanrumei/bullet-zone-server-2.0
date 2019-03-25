/*     */ package ch.qos.logback.classic.selector;
/*     */ 
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.joran.JoranConfigurator;
/*     */ import ch.qos.logback.classic.util.ContextInitializer;
/*     */ import ch.qos.logback.classic.util.JNDIUtil;
/*     */ import ch.qos.logback.core.joran.spi.JoranException;
/*     */ import ch.qos.logback.core.status.InfoStatus;
/*     */ import ch.qos.logback.core.status.StatusManager;
/*     */ import ch.qos.logback.core.status.StatusUtil;
/*     */ import ch.qos.logback.core.status.WarnStatus;
/*     */ import ch.qos.logback.core.util.Loader;
/*     */ import ch.qos.logback.core.util.StatusPrinter;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.NamingException;
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
/*     */ public class ContextJNDISelector
/*     */   implements ContextSelector
/*     */ {
/*     */   private final Map<String, LoggerContext> synchronizedContextMap;
/*     */   private final LoggerContext defaultContext;
/*  57 */   private static final ThreadLocal<LoggerContext> threadLocal = new ThreadLocal();
/*     */   
/*     */   public ContextJNDISelector(LoggerContext context) {
/*  60 */     this.synchronizedContextMap = Collections.synchronizedMap(new HashMap());
/*  61 */     this.defaultContext = context;
/*     */   }
/*     */   
/*     */   public LoggerContext getDefaultLoggerContext() {
/*  65 */     return this.defaultContext;
/*     */   }
/*     */   
/*     */   public LoggerContext detachLoggerContext(String loggerContextName) {
/*  69 */     return (LoggerContext)this.synchronizedContextMap.remove(loggerContextName);
/*     */   }
/*     */   
/*     */   public LoggerContext getLoggerContext() {
/*  73 */     String contextName = null;
/*  74 */     Context ctx = null;
/*     */     
/*     */ 
/*  77 */     LoggerContext lc = (LoggerContext)threadLocal.get();
/*  78 */     if (lc != null) {
/*  79 */       return lc;
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/*  85 */       ctx = JNDIUtil.getInitialContext();
/*  86 */       contextName = JNDIUtil.lookup(ctx, "java:comp/env/logback/context-name");
/*     */     }
/*     */     catch (NamingException localNamingException) {}
/*     */     
/*     */ 
/*  91 */     if (contextName == null)
/*     */     {
/*  93 */       return this.defaultContext;
/*     */     }
/*     */     
/*  96 */     LoggerContext loggerContext = (LoggerContext)this.synchronizedContextMap.get(contextName);
/*     */     
/*  98 */     if (loggerContext == null)
/*     */     {
/* 100 */       loggerContext = new LoggerContext();
/* 101 */       loggerContext.setName(contextName);
/* 102 */       this.synchronizedContextMap.put(contextName, loggerContext);
/* 103 */       URL url = findConfigFileURL(ctx, loggerContext);
/* 104 */       if (url != null) {
/* 105 */         configureLoggerContextByURL(loggerContext, url);
/*     */       } else {
/*     */         try {
/* 108 */           new ContextInitializer(loggerContext).autoConfig();
/*     */         }
/*     */         catch (JoranException localJoranException) {}
/*     */       }
/*     */       
/* 113 */       if (!StatusUtil.contextHasStatusListener(loggerContext))
/* 114 */         StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext);
/*     */     }
/* 116 */     return loggerContext;
/*     */   }
/*     */   
/*     */   private String conventionalConfigFileName(String contextName)
/*     */   {
/* 121 */     return "logback-" + contextName + ".xml";
/*     */   }
/*     */   
/*     */   private URL findConfigFileURL(Context ctx, LoggerContext loggerContext) {
/* 125 */     StatusManager sm = loggerContext.getStatusManager();
/*     */     
/* 127 */     String jndiEntryForConfigResource = JNDIUtil.lookup(ctx, "java:comp/env/logback/configuration-resource");
/*     */     
/* 129 */     if (jndiEntryForConfigResource != null) {
/* 130 */       sm.add(new InfoStatus("Searching for [" + jndiEntryForConfigResource + "]", this));
/* 131 */       URL url = urlByResourceName(sm, jndiEntryForConfigResource);
/* 132 */       if (url == null) {
/* 133 */         String msg = "The jndi resource [" + jndiEntryForConfigResource + "] for context [" + loggerContext.getName() + 
/* 134 */           "] does not lead to a valid file";
/* 135 */         sm.add(new WarnStatus(msg, this));
/*     */       }
/* 137 */       return url;
/*     */     }
/* 139 */     String resourceByConvention = conventionalConfigFileName(loggerContext.getName());
/* 140 */     return urlByResourceName(sm, resourceByConvention);
/*     */   }
/*     */   
/*     */   private URL urlByResourceName(StatusManager sm, String resourceName)
/*     */   {
/* 145 */     sm.add(new InfoStatus("Searching for [" + resourceName + "]", this));
/* 146 */     URL url = Loader.getResource(resourceName, Loader.getTCL());
/* 147 */     if (url != null) {
/* 148 */       return url;
/*     */     }
/* 150 */     return Loader.getResourceBySelfClassLoader(resourceName);
/*     */   }
/*     */   
/*     */   private void configureLoggerContextByURL(LoggerContext context, URL url) {
/*     */     try {
/* 155 */       JoranConfigurator configurator = new JoranConfigurator();
/* 156 */       context.reset();
/* 157 */       configurator.setContext(context);
/* 158 */       configurator.doConfigure(url);
/*     */     }
/*     */     catch (JoranException localJoranException) {}
/* 161 */     StatusPrinter.printInCaseOfErrorsOrWarnings(context);
/*     */   }
/*     */   
/*     */   public List<String> getContextNames() {
/* 165 */     List<String> list = new ArrayList();
/* 166 */     list.addAll(this.synchronizedContextMap.keySet());
/* 167 */     return list;
/*     */   }
/*     */   
/*     */   public LoggerContext getLoggerContext(String name) {
/* 171 */     return (LoggerContext)this.synchronizedContextMap.get(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getCount()
/*     */   {
/* 180 */     return this.synchronizedContextMap.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLocalContext(LoggerContext context)
/*     */   {
/* 192 */     threadLocal.set(context);
/*     */   }
/*     */   
/*     */   public void removeLocalContext() {
/* 196 */     threadLocal.remove();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\selector\ContextJNDISelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */