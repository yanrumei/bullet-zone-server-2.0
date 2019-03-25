/*     */ package org.springframework.boot.jta.narayana;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
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
/*     */ @ConfigurationProperties(prefix="spring.jta.narayana")
/*     */ public class NarayanaProperties
/*     */ {
/*     */   public static final String PROPERTIES_PREFIX = "spring.jta.narayana";
/*     */   private String logDir;
/*  49 */   private String transactionManagerId = "1";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  54 */   private boolean onePhaseCommit = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  59 */   private int defaultTimeout = 60;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  64 */   private int periodicRecoveryPeriod = 120;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  69 */   private int recoveryBackoffPeriod = 10;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  74 */   private String recoveryDbUser = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  79 */   private String recoveryDbPass = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  84 */   private String recoveryJmsUser = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  89 */   private String recoveryJmsPass = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  94 */   private List<String> xaResourceOrphanFilters = new ArrayList(Arrays.asList(new String[] { "com.arjuna.ats.internal.jta.recovery.arjunacore.JTATransactionLogXAResourceOrphanFilter", "com.arjuna.ats.internal.jta.recovery.arjunacore.JTANodeNameXAResourceOrphanFilter" }));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 101 */   private List<String> recoveryModules = new ArrayList(Arrays.asList(new String[] { "com.arjuna.ats.internal.arjuna.recovery.AtomicActionRecoveryModule", "com.arjuna.ats.internal.jta.recovery.arjunacore.XARecoveryModule" }));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 108 */   private List<String> expiryScanners = new ArrayList(Collections.singletonList("com.arjuna.ats.internal.arjuna.recovery.ExpiredTransactionStatusManagerScanner"));
/*     */   
/*     */   public String getLogDir()
/*     */   {
/* 112 */     return this.logDir;
/*     */   }
/*     */   
/*     */   public void setLogDir(String logDir) {
/* 116 */     this.logDir = logDir;
/*     */   }
/*     */   
/*     */   public String getTransactionManagerId() {
/* 120 */     return this.transactionManagerId;
/*     */   }
/*     */   
/*     */   public void setTransactionManagerId(String transactionManagerId) {
/* 124 */     this.transactionManagerId = transactionManagerId;
/*     */   }
/*     */   
/*     */   public boolean isOnePhaseCommit() {
/* 128 */     return this.onePhaseCommit;
/*     */   }
/*     */   
/*     */   public void setOnePhaseCommit(boolean onePhaseCommit) {
/* 132 */     this.onePhaseCommit = onePhaseCommit;
/*     */   }
/*     */   
/*     */   public int getDefaultTimeout() {
/* 136 */     return this.defaultTimeout;
/*     */   }
/*     */   
/*     */   public int getPeriodicRecoveryPeriod() {
/* 140 */     return this.periodicRecoveryPeriod;
/*     */   }
/*     */   
/*     */   public void setPeriodicRecoveryPeriod(int periodicRecoveryPeriod) {
/* 144 */     this.periodicRecoveryPeriod = periodicRecoveryPeriod;
/*     */   }
/*     */   
/*     */   public int getRecoveryBackoffPeriod() {
/* 148 */     return this.recoveryBackoffPeriod;
/*     */   }
/*     */   
/*     */   public void setRecoveryBackoffPeriod(int recoveryBackoffPeriod) {
/* 152 */     this.recoveryBackoffPeriod = recoveryBackoffPeriod;
/*     */   }
/*     */   
/*     */   public void setDefaultTimeout(int defaultTimeout) {
/* 156 */     this.defaultTimeout = defaultTimeout;
/*     */   }
/*     */   
/*     */   public List<String> getXaResourceOrphanFilters() {
/* 160 */     return this.xaResourceOrphanFilters;
/*     */   }
/*     */   
/*     */   public void setXaResourceOrphanFilters(List<String> xaResourceOrphanFilters) {
/* 164 */     this.xaResourceOrphanFilters = xaResourceOrphanFilters;
/*     */   }
/*     */   
/*     */   public List<String> getRecoveryModules() {
/* 168 */     return this.recoveryModules;
/*     */   }
/*     */   
/*     */   public void setRecoveryModules(List<String> recoveryModules) {
/* 172 */     this.recoveryModules = recoveryModules;
/*     */   }
/*     */   
/*     */   public List<String> getExpiryScanners() {
/* 176 */     return this.expiryScanners;
/*     */   }
/*     */   
/*     */   public void setExpiryScanners(List<String> expiryScanners) {
/* 180 */     this.expiryScanners = expiryScanners;
/*     */   }
/*     */   
/*     */   public String getRecoveryDbUser() {
/* 184 */     return this.recoveryDbUser;
/*     */   }
/*     */   
/*     */   public void setRecoveryDbUser(String recoveryDbUser) {
/* 188 */     this.recoveryDbUser = recoveryDbUser;
/*     */   }
/*     */   
/*     */   public String getRecoveryDbPass() {
/* 192 */     return this.recoveryDbPass;
/*     */   }
/*     */   
/*     */   public void setRecoveryDbPass(String recoveryDbPass) {
/* 196 */     this.recoveryDbPass = recoveryDbPass;
/*     */   }
/*     */   
/*     */   public String getRecoveryJmsUser() {
/* 200 */     return this.recoveryJmsUser;
/*     */   }
/*     */   
/*     */   public void setRecoveryJmsUser(String recoveryJmsUser) {
/* 204 */     this.recoveryJmsUser = recoveryJmsUser;
/*     */   }
/*     */   
/*     */   public String getRecoveryJmsPass() {
/* 208 */     return this.recoveryJmsPass;
/*     */   }
/*     */   
/*     */   public void setRecoveryJmsPass(String recoveryJmsPass) {
/* 212 */     this.recoveryJmsPass = recoveryJmsPass;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\jta\narayana\NarayanaProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */