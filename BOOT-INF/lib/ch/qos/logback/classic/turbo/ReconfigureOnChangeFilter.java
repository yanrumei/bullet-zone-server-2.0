/*     */ package ch.qos.logback.classic.turbo;
/*     */ 
/*     */ import ch.qos.logback.classic.Level;
/*     */ import ch.qos.logback.classic.Logger;
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.gaffer.GafferUtil;
/*     */ import ch.qos.logback.classic.joran.JoranConfigurator;
/*     */ import ch.qos.logback.classic.util.EnvUtil;
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.joran.event.SaxEvent;
/*     */ import ch.qos.logback.core.joran.spi.ConfigurationWatchList;
/*     */ import ch.qos.logback.core.joran.spi.JoranException;
/*     */ import ch.qos.logback.core.joran.util.ConfigurationWatchListUtil;
/*     */ import ch.qos.logback.core.spi.FilterReply;
/*     */ import ch.qos.logback.core.status.StatusUtil;
/*     */ import java.io.File;
/*     */ import java.net.URL;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import org.slf4j.Marker;
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
/*     */ public class ReconfigureOnChangeFilter
/*     */   extends TurboFilter
/*     */ {
/*     */   public static final long DEFAULT_REFRESH_PERIOD = 60000L;
/*  51 */   long refreshPeriod = 60000L;
/*     */   
/*     */   URL mainConfigurationURL;
/*     */   protected volatile long nextCheck;
/*     */   ConfigurationWatchList configurationWatchList;
/*     */   
/*     */   public void start()
/*     */   {
/*  59 */     this.configurationWatchList = ConfigurationWatchListUtil.getConfigurationWatchList(this.context);
/*  60 */     if (this.configurationWatchList != null) {
/*  61 */       this.mainConfigurationURL = this.configurationWatchList.getMainURL();
/*  62 */       if (this.mainConfigurationURL == null) {
/*  63 */         addWarn("Due to missing top level configuration file, automatic reconfiguration is impossible.");
/*  64 */         return;
/*     */       }
/*  66 */       List<File> watchList = this.configurationWatchList.getCopyOfFileWatchList();
/*  67 */       long inSeconds = this.refreshPeriod / 1000L;
/*  68 */       addInfo("Will scan for changes in [" + watchList + "] every " + inSeconds + " seconds. ");
/*  69 */       synchronized (this.configurationWatchList) {
/*  70 */         updateNextCheck(System.currentTimeMillis());
/*     */       }
/*  72 */       super.start();
/*     */     } else {
/*  74 */       addWarn("Empty ConfigurationWatchList in context");
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  80 */     return "ReconfigureOnChangeFilter{invocationCounter=" + this.invocationCounter + '}';
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  89 */   private long invocationCounter = 0L;
/*     */   
/*  91 */   private volatile long mask = 15L;
/*  92 */   private volatile long lastMaskCheck = System.currentTimeMillis();
/*     */   private static final int MAX_MASK = 65535;
/*     */   
/*     */   public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t) {
/*  96 */     if (!isStarted()) {
/*  97 */       return FilterReply.NEUTRAL;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 103 */     if ((this.invocationCounter++ & this.mask) != this.mask) {
/* 104 */       return FilterReply.NEUTRAL;
/*     */     }
/*     */     
/* 107 */     long now = System.currentTimeMillis();
/*     */     
/* 109 */     synchronized (this.configurationWatchList) {
/* 110 */       updateMaskIfNecessary(now);
/* 111 */       if (changeDetected(now))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 116 */         disableSubsequentReconfiguration();
/* 117 */         detachReconfigurationToNewThread();
/*     */       }
/*     */     }
/*     */     
/* 121 */     return FilterReply.NEUTRAL;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final long MASK_INCREASE_THRESHOLD = 100L;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final long MASK_DECREASE_THRESHOLD = 800L;
/*     */   
/*     */ 
/*     */ 
/*     */   private void updateMaskIfNecessary(long now)
/*     */   {
/* 138 */     long timeElapsedSinceLastMaskUpdateCheck = now - this.lastMaskCheck;
/* 139 */     this.lastMaskCheck = now;
/* 140 */     if ((timeElapsedSinceLastMaskUpdateCheck < 100L) && (this.mask < 65535L)) {
/* 141 */       this.mask = (this.mask << 1 | 1L);
/* 142 */     } else if (timeElapsedSinceLastMaskUpdateCheck > 800L) {
/* 143 */       this.mask >>>= 2;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void detachReconfigurationToNewThread()
/*     */   {
/* 151 */     addInfo("Detected change in [" + this.configurationWatchList.getCopyOfFileWatchList() + "]");
/* 152 */     this.context.getExecutorService().submit(new ReconfiguringThread());
/*     */   }
/*     */   
/*     */   void updateNextCheck(long now) {
/* 156 */     this.nextCheck = (now + this.refreshPeriod);
/*     */   }
/*     */   
/*     */   protected boolean changeDetected(long now) {
/* 160 */     if (now >= this.nextCheck) {
/* 161 */       updateNextCheck(now);
/* 162 */       return this.configurationWatchList.changeDetected();
/*     */     }
/* 164 */     return false;
/*     */   }
/*     */   
/*     */   void disableSubsequentReconfiguration() {
/* 168 */     this.nextCheck = Long.MAX_VALUE;
/*     */   }
/*     */   
/*     */   public long getRefreshPeriod() {
/* 172 */     return this.refreshPeriod;
/*     */   }
/*     */   
/*     */ 
/* 176 */   public void setRefreshPeriod(long refreshPeriod) { this.refreshPeriod = refreshPeriod; }
/*     */   
/*     */   class ReconfiguringThread implements Runnable {
/*     */     ReconfiguringThread() {}
/*     */     
/* 181 */     public void run() { if (ReconfigureOnChangeFilter.this.mainConfigurationURL == null) {
/* 182 */         ReconfigureOnChangeFilter.this.addInfo("Due to missing top level configuration file, skipping reconfiguration");
/* 183 */         return;
/*     */       }
/* 185 */       LoggerContext lc = (LoggerContext)ReconfigureOnChangeFilter.this.context;
/* 186 */       ReconfigureOnChangeFilter.this.addInfo("Will reset and reconfigure context named [" + ReconfigureOnChangeFilter.this.context.getName() + "]");
/* 187 */       if (ReconfigureOnChangeFilter.this.mainConfigurationURL.toString().endsWith("xml")) {
/* 188 */         performXMLConfiguration(lc);
/* 189 */       } else if (ReconfigureOnChangeFilter.this.mainConfigurationURL.toString().endsWith("groovy")) {
/* 190 */         if (EnvUtil.isGroovyAvailable()) {
/* 191 */           lc.reset();
/*     */           
/*     */ 
/* 194 */           GafferUtil.runGafferConfiguratorOn(lc, this, ReconfigureOnChangeFilter.this.mainConfigurationURL);
/*     */         } else {
/* 196 */           ReconfigureOnChangeFilter.this.addError("Groovy classes are not available on the class path. ABORTING INITIALIZATION.");
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private void performXMLConfiguration(LoggerContext lc) {
/* 202 */       JoranConfigurator jc = new JoranConfigurator();
/* 203 */       jc.setContext(ReconfigureOnChangeFilter.this.context);
/* 204 */       StatusUtil statusUtil = new StatusUtil(ReconfigureOnChangeFilter.this.context);
/* 205 */       List<SaxEvent> eventList = jc.recallSafeConfiguration();
/* 206 */       URL mainURL = ConfigurationWatchListUtil.getMainWatchURL(ReconfigureOnChangeFilter.this.context);
/* 207 */       lc.reset();
/* 208 */       long threshold = System.currentTimeMillis();
/*     */       try {
/* 210 */         jc.doConfigure(ReconfigureOnChangeFilter.this.mainConfigurationURL);
/* 211 */         if (statusUtil.hasXMLParsingErrors(threshold)) {
/* 212 */           fallbackConfiguration(lc, eventList, mainURL);
/*     */         }
/*     */       } catch (JoranException localJoranException) {
/* 215 */         fallbackConfiguration(lc, eventList, mainURL);
/*     */       }
/*     */     }
/*     */     
/*     */     private void fallbackConfiguration(LoggerContext lc, List<SaxEvent> eventList, URL mainURL) {
/* 220 */       JoranConfigurator joranConfigurator = new JoranConfigurator();
/* 221 */       joranConfigurator.setContext(ReconfigureOnChangeFilter.this.context);
/* 222 */       if (eventList != null) {
/* 223 */         ReconfigureOnChangeFilter.this.addWarn("Falling back to previously registered safe configuration.");
/*     */         try {
/* 225 */           lc.reset();
/* 226 */           JoranConfigurator.informContextOfURLUsedForConfiguration(ReconfigureOnChangeFilter.this.context, mainURL);
/* 227 */           joranConfigurator.doConfigure(eventList);
/* 228 */           ReconfigureOnChangeFilter.this.addInfo("Re-registering previous fallback configuration once more as a fallback configuration point");
/* 229 */           joranConfigurator.registerSafeConfiguration(eventList);
/*     */         } catch (JoranException e) {
/* 231 */           ReconfigureOnChangeFilter.this.addError("Unexpected exception thrown by a configuration considered safe.", e);
/*     */         }
/*     */       } else {
/* 234 */         ReconfigureOnChangeFilter.this.addWarn("No previous configuration to fall back on.");
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\turbo\ReconfigureOnChangeFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */