/*     */ package ch.qos.logback.classic.joran;
/*     */ 
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.gaffer.GafferUtil;
/*     */ import ch.qos.logback.classic.util.EnvUtil;
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.joran.event.SaxEvent;
/*     */ import ch.qos.logback.core.joran.spi.ConfigurationWatchList;
/*     */ import ch.qos.logback.core.joran.spi.JoranException;
/*     */ import ch.qos.logback.core.joran.util.ConfigurationWatchListUtil;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import ch.qos.logback.core.status.StatusUtil;
/*     */ import java.io.File;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReconfigureOnChangeTask
/*     */   extends ContextAwareBase
/*     */   implements Runnable
/*     */ {
/*     */   public static final String DETECTED_CHANGE_IN_CONFIGURATION_FILES = "Detected change in configuration files.";
/*     */   static final String RE_REGISTERING_PREVIOUS_SAFE_CONFIGURATION = "Re-registering previous fallback configuration once more as a fallback configuration point";
/*     */   static final String FALLING_BACK_TO_SAFE_CONFIGURATION = "Given previous errors, falling back to previously registered safe configuration.";
/*  27 */   long birthdate = System.currentTimeMillis();
/*     */   List<ReconfigureOnChangeTaskListener> listeners;
/*     */   
/*     */   void addListener(ReconfigureOnChangeTaskListener listener)
/*     */   {
/*  32 */     if (this.listeners == null)
/*  33 */       this.listeners = new ArrayList();
/*  34 */     this.listeners.add(listener);
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/*  39 */     fireEnteredRunMethod();
/*     */     
/*  41 */     ConfigurationWatchList configurationWatchList = ConfigurationWatchListUtil.getConfigurationWatchList(this.context);
/*  42 */     if (configurationWatchList == null) {
/*  43 */       addWarn("Empty ConfigurationWatchList in context");
/*  44 */       return;
/*     */     }
/*     */     
/*  47 */     List<File> filesToWatch = configurationWatchList.getCopyOfFileWatchList();
/*  48 */     if ((filesToWatch == null) || (filesToWatch.isEmpty())) {
/*  49 */       addInfo("Empty watch file list. Disabling ");
/*  50 */       return;
/*     */     }
/*     */     
/*  53 */     if (!configurationWatchList.changeDetected()) {
/*  54 */       return;
/*     */     }
/*     */     
/*  57 */     fireChangeDetected();
/*  58 */     URL mainConfigurationURL = configurationWatchList.getMainURL();
/*     */     
/*  60 */     addInfo("Detected change in configuration files.");
/*  61 */     addInfo("Will reset and reconfigure context named [" + this.context.getName() + "]");
/*     */     
/*  63 */     LoggerContext lc = (LoggerContext)this.context;
/*  64 */     if (mainConfigurationURL.toString().endsWith("xml")) {
/*  65 */       performXMLConfiguration(lc, mainConfigurationURL);
/*  66 */     } else if (mainConfigurationURL.toString().endsWith("groovy")) {
/*  67 */       if (EnvUtil.isGroovyAvailable()) {
/*  68 */         lc.reset();
/*     */         
/*     */ 
/*  71 */         GafferUtil.runGafferConfiguratorOn(lc, this, mainConfigurationURL);
/*     */       } else {
/*  73 */         addError("Groovy classes are not available on the class path. ABORTING INITIALIZATION.");
/*     */       }
/*     */     }
/*  76 */     fireDoneReconfiguring();
/*     */   }
/*     */   
/*     */   private void fireEnteredRunMethod() {
/*  80 */     if (this.listeners == null) {
/*  81 */       return;
/*     */     }
/*  83 */     for (ReconfigureOnChangeTaskListener listener : this.listeners)
/*  84 */       listener.enteredRunMethod();
/*     */   }
/*     */   
/*     */   private void fireChangeDetected() {
/*  88 */     if (this.listeners == null) {
/*  89 */       return;
/*     */     }
/*  91 */     for (ReconfigureOnChangeTaskListener listener : this.listeners) {
/*  92 */       listener.changeDetected();
/*     */     }
/*     */   }
/*     */   
/*     */   private void fireDoneReconfiguring() {
/*  97 */     if (this.listeners == null) {
/*  98 */       return;
/*     */     }
/* 100 */     for (ReconfigureOnChangeTaskListener listener : this.listeners)
/* 101 */       listener.doneReconfiguring();
/*     */   }
/*     */   
/*     */   private void performXMLConfiguration(LoggerContext lc, URL mainConfigurationURL) {
/* 105 */     JoranConfigurator jc = new JoranConfigurator();
/* 106 */     jc.setContext(this.context);
/* 107 */     StatusUtil statusUtil = new StatusUtil(this.context);
/* 108 */     List<SaxEvent> eventList = jc.recallSafeConfiguration();
/*     */     
/* 110 */     URL mainURL = ConfigurationWatchListUtil.getMainWatchURL(this.context);
/* 111 */     lc.reset();
/* 112 */     long threshold = System.currentTimeMillis();
/*     */     try {
/* 114 */       jc.doConfigure(mainConfigurationURL);
/* 115 */       if (statusUtil.hasXMLParsingErrors(threshold)) {
/* 116 */         fallbackConfiguration(lc, eventList, mainURL);
/*     */       }
/*     */     } catch (JoranException localJoranException) {
/* 119 */       fallbackConfiguration(lc, eventList, mainURL);
/*     */     }
/*     */   }
/*     */   
/*     */   private List<SaxEvent> removeIncludeEvents(List<SaxEvent> unsanitizedEventList) {
/* 124 */     List<SaxEvent> sanitizedEvents = new ArrayList();
/* 125 */     if (unsanitizedEventList == null) {
/* 126 */       return sanitizedEvents;
/*     */     }
/* 128 */     for (SaxEvent e : unsanitizedEventList) {
/* 129 */       if (!"include".equalsIgnoreCase(e.getLocalName())) {
/* 130 */         sanitizedEvents.add(e);
/*     */       }
/*     */     }
/* 133 */     return sanitizedEvents;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void fallbackConfiguration(LoggerContext lc, List<SaxEvent> eventList, URL mainURL)
/*     */   {
/* 140 */     List<SaxEvent> failsafeEvents = removeIncludeEvents(eventList);
/* 141 */     JoranConfigurator joranConfigurator = new JoranConfigurator();
/* 142 */     joranConfigurator.setContext(this.context);
/* 143 */     ConfigurationWatchList oldCWL = ConfigurationWatchListUtil.getConfigurationWatchList(this.context);
/* 144 */     ConfigurationWatchList newCWL = oldCWL.buildClone();
/*     */     
/* 146 */     if ((failsafeEvents == null) || (failsafeEvents.isEmpty())) {
/* 147 */       addWarn("No previous configuration to fall back on.");
/*     */     } else {
/* 149 */       addWarn("Given previous errors, falling back to previously registered safe configuration.");
/*     */       try {
/* 151 */         lc.reset();
/* 152 */         ConfigurationWatchListUtil.registerConfigurationWatchList(this.context, newCWL);
/* 153 */         joranConfigurator.doConfigure(failsafeEvents);
/* 154 */         addInfo("Re-registering previous fallback configuration once more as a fallback configuration point");
/* 155 */         joranConfigurator.registerSafeConfiguration(eventList);
/*     */         
/* 157 */         addInfo("after registerSafeConfiguration: " + eventList);
/*     */       } catch (JoranException e) {
/* 159 */         addError("Unexpected exception thrown by a configuration considered safe.", e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 166 */     return "ReconfigureOnChangeTask(born:" + this.birthdate + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\joran\ReconfigureOnChangeTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */