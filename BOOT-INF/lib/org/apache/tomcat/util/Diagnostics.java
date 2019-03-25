/*     */ package org.apache.tomcat.util;
/*     */ 
/*     */ import java.lang.management.ClassLoadingMXBean;
/*     */ import java.lang.management.CompilationMXBean;
/*     */ import java.lang.management.GarbageCollectorMXBean;
/*     */ import java.lang.management.LockInfo;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.MemoryMXBean;
/*     */ import java.lang.management.MemoryManagerMXBean;
/*     */ import java.lang.management.MemoryPoolMXBean;
/*     */ import java.lang.management.MemoryUsage;
/*     */ import java.lang.management.MonitorInfo;
/*     */ import java.lang.management.OperatingSystemMXBean;
/*     */ import java.lang.management.PlatformLoggingMXBean;
/*     */ import java.lang.management.RuntimeMXBean;
/*     */ import java.lang.management.ThreadInfo;
/*     */ import java.lang.management.ThreadMXBean;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
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
/*     */ public class Diagnostics
/*     */ {
/*     */   private static final String PACKAGE = "org.apache.tomcat.util";
/*  69 */   private static final StringManager sm = StringManager.getManager("org.apache.tomcat.util");
/*     */   
/*     */   private static final String INDENT1 = "  ";
/*     */   
/*     */   private static final String INDENT2 = "\t";
/*     */   private static final String INDENT3 = "   ";
/*     */   private static final String CRLF = "\r\n";
/*     */   private static final String vminfoSystemProperty = "java.vm.info";
/*  77 */   private static final Log log = LogFactory.getLog(Diagnostics.class);
/*     */   
/*  79 */   private static final SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  84 */   private static final ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
/*     */   
/*  86 */   private static final CompilationMXBean compilationMXBean = ManagementFactory.getCompilationMXBean();
/*     */   
/*  88 */   private static final OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
/*     */   
/*  90 */   private static final RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
/*     */   
/*  92 */   private static final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  98 */   private static final PlatformLoggingMXBean loggingMXBean = (PlatformLoggingMXBean)ManagementFactory.getPlatformMXBean(PlatformLoggingMXBean.class);
/*     */   
/* 100 */   private static final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
/*     */   
/* 102 */   private static final List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
/*     */   
/* 104 */   private static final List<MemoryManagerMXBean> memoryManagerMXBeans = ManagementFactory.getMemoryManagerMXBeans();
/*     */   
/* 106 */   private static final List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isThreadContentionMonitoringEnabled()
/*     */   {
/* 114 */     return threadMXBean.isThreadContentionMonitoringEnabled();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setThreadContentionMonitoringEnabled(boolean enable)
/*     */   {
/* 123 */     threadMXBean.setThreadContentionMonitoringEnabled(enable);
/* 124 */     boolean checkValue = threadMXBean.isThreadContentionMonitoringEnabled();
/* 125 */     if (enable != checkValue) {
/* 126 */       log.error("Could not set threadContentionMonitoringEnabled to " + enable + ", got " + checkValue + " instead");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isThreadCpuTimeEnabled()
/*     */   {
/* 137 */     return threadMXBean.isThreadCpuTimeEnabled();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setThreadCpuTimeEnabled(boolean enable)
/*     */   {
/* 146 */     threadMXBean.setThreadCpuTimeEnabled(enable);
/* 147 */     boolean checkValue = threadMXBean.isThreadCpuTimeEnabled();
/* 148 */     if (enable != checkValue) {
/* 149 */       log.error("Could not set threadCpuTimeEnabled to " + enable + ", got " + checkValue + " instead");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void resetPeakThreadCount()
/*     */   {
/* 158 */     threadMXBean.resetPeakThreadCount();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setVerboseClassLoading(boolean verbose)
/*     */   {
/* 167 */     classLoadingMXBean.setVerbose(verbose);
/* 168 */     boolean checkValue = classLoadingMXBean.isVerbose();
/* 169 */     if (verbose != checkValue) {
/* 170 */       log.error("Could not set verbose class loading to " + verbose + ", got " + checkValue + " instead");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setLoggerLevel(String loggerName, String levelName)
/*     */   {
/* 182 */     loggingMXBean.setLoggerLevel(loggerName, levelName);
/* 183 */     String checkValue = loggingMXBean.getLoggerLevel(loggerName);
/* 184 */     if (!checkValue.equals(levelName)) {
/* 185 */       log.error("Could not set logger level for logger '" + loggerName + "' to '" + levelName + "', got '" + checkValue + "' instead");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setVerboseGarbageCollection(boolean verbose)
/*     */   {
/* 197 */     memoryMXBean.setVerbose(verbose);
/* 198 */     boolean checkValue = memoryMXBean.isVerbose();
/* 199 */     if (verbose != checkValue) {
/* 200 */       log.error("Could not set verbose garbage collection logging to " + verbose + ", got " + checkValue + " instead");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void gc()
/*     */   {
/* 209 */     memoryMXBean.gc();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void resetPeakUsage(String name)
/*     */   {
/* 218 */     for (MemoryPoolMXBean mbean : memoryPoolMXBeans) {
/* 219 */       if ((name.equals("all")) || (name.equals(mbean.getName()))) {
/* 220 */         mbean.resetPeakUsage();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean setUsageThreshold(String name, long threshold)
/*     */   {
/* 233 */     for (MemoryPoolMXBean mbean : memoryPoolMXBeans) {
/* 234 */       if (name.equals(mbean.getName())) {
/*     */         try {
/* 236 */           mbean.setUsageThreshold(threshold);
/* 237 */           return true;
/*     */         }
/*     */         catch (IllegalArgumentException localIllegalArgumentException) {}catch (UnsupportedOperationException localUnsupportedOperationException) {}
/*     */         
/*     */ 
/*     */ 
/* 243 */         return false;
/*     */       }
/*     */     }
/* 246 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean setCollectionUsageThreshold(String name, long threshold)
/*     */   {
/* 257 */     for (MemoryPoolMXBean mbean : memoryPoolMXBeans) {
/* 258 */       if (name.equals(mbean.getName())) {
/*     */         try {
/* 260 */           mbean.setCollectionUsageThreshold(threshold);
/* 261 */           return true;
/*     */         }
/*     */         catch (IllegalArgumentException localIllegalArgumentException) {}catch (UnsupportedOperationException localUnsupportedOperationException) {}
/*     */         
/*     */ 
/*     */ 
/* 267 */         return false;
/*     */       }
/*     */     }
/* 270 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String getThreadDumpHeader(ThreadInfo ti)
/*     */   {
/* 280 */     StringBuilder sb = new StringBuilder("\"" + ti.getThreadName() + "\"");
/* 281 */     sb.append(" Id=" + ti.getThreadId());
/* 282 */     sb.append(" cpu=" + threadMXBean.getThreadCpuTime(ti.getThreadId()) + " ns");
/*     */     
/* 284 */     sb.append(" usr=" + threadMXBean.getThreadUserTime(ti.getThreadId()) + " ns");
/*     */     
/* 286 */     sb.append(" blocked " + ti.getBlockedCount() + " for " + ti
/* 287 */       .getBlockedTime() + " ms");
/* 288 */     sb.append(" waited " + ti.getWaitedCount() + " for " + ti
/* 289 */       .getWaitedTime() + " ms");
/*     */     
/* 291 */     if (ti.isSuspended()) {
/* 292 */       sb.append(" (suspended)");
/*     */     }
/* 294 */     if (ti.isInNative()) {
/* 295 */       sb.append(" (running in native)");
/*     */     }
/* 297 */     sb.append("\r\n");
/* 298 */     sb.append("   java.lang.Thread.State: " + ti.getThreadState());
/* 299 */     sb.append("\r\n");
/* 300 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String getThreadDump(ThreadInfo ti)
/*     */   {
/* 310 */     StringBuilder sb = new StringBuilder(getThreadDumpHeader(ti));
/* 311 */     for (LockInfo li : ti.getLockedSynchronizers()) {
/* 312 */       sb.append("\tlocks " + li
/* 313 */         .toString() + "\r\n");
/*     */     }
/* 315 */     boolean start = true;
/* 316 */     StackTraceElement[] stes = ti.getStackTrace();
/* 317 */     Object[] monitorDepths = new Object[stes.length];
/* 318 */     MonitorInfo[] mis = ti.getLockedMonitors();
/* 319 */     for (int i = 0; i < mis.length; i++) {
/* 320 */       monitorDepths[mis[i].getLockedStackDepth()] = mis[i];
/*     */     }
/* 322 */     for (int i = 0; i < stes.length; i++) {
/* 323 */       StackTraceElement ste = stes[i];
/* 324 */       sb.append("\tat " + ste
/* 325 */         .toString() + "\r\n");
/* 326 */       if (start) {
/* 327 */         if (ti.getLockName() != null) {
/* 328 */           sb.append("\t- waiting on (a " + ti
/* 329 */             .getLockName() + ")");
/* 330 */           if (ti.getLockOwnerName() != null) {
/* 331 */             sb.append(" owned by " + ti.getLockOwnerName() + " Id=" + ti
/* 332 */               .getLockOwnerId());
/*     */           }
/* 334 */           sb.append("\r\n");
/*     */         }
/* 336 */         start = false;
/*     */       }
/* 338 */       if (monitorDepths[i] != null) {
/* 339 */         MonitorInfo mi = (MonitorInfo)monitorDepths[i];
/* 340 */         sb.append("\t- locked (a " + mi
/* 341 */           .toString() + ") index " + mi
/* 342 */           .getLockedStackDepth() + " frame " + mi
/* 343 */           .getLockedStackFrame().toString());
/* 344 */         sb.append("\r\n");
/*     */       }
/*     */     }
/*     */     
/* 348 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String getThreadDump(ThreadInfo[] tinfos)
/*     */   {
/* 358 */     StringBuilder sb = new StringBuilder();
/* 359 */     for (ThreadInfo tinfo : tinfos) {
/* 360 */       sb.append(getThreadDump(tinfo));
/* 361 */       sb.append("\r\n");
/*     */     }
/* 363 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String findDeadlock()
/*     */   {
/* 374 */     ThreadInfo[] tinfos = null;
/* 375 */     long[] ids = threadMXBean.findDeadlockedThreads();
/* 376 */     if (ids != null) {
/* 377 */       tinfos = threadMXBean.getThreadInfo(threadMXBean.findDeadlockedThreads(), true, true);
/*     */       
/* 379 */       if (tinfos != null) {
/* 380 */         StringBuilder sb = new StringBuilder("Deadlock found between the following threads:");
/*     */         
/* 382 */         sb.append("\r\n");
/* 383 */         sb.append(getThreadDump(tinfos));
/* 384 */         return sb.toString();
/*     */       }
/*     */     }
/* 387 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getThreadDump()
/*     */   {
/* 397 */     return getThreadDump(sm);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getThreadDump(Enumeration<Locale> requestedLocales)
/*     */   {
/* 409 */     return getThreadDump(
/* 410 */       StringManager.getManager("org.apache.tomcat.util", requestedLocales));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getThreadDump(StringManager requestedSm)
/*     */   {
/* 421 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 423 */     synchronized (timeformat) {
/* 424 */       sb.append(timeformat.format(new Date()));
/*     */     }
/* 426 */     sb.append("\r\n");
/*     */     
/* 428 */     sb.append(requestedSm.getString("diagnostics.threadDumpTitle"));
/* 429 */     sb.append(" ");
/* 430 */     sb.append(runtimeMXBean.getVmName());
/* 431 */     sb.append(" (");
/* 432 */     sb.append(runtimeMXBean.getVmVersion());
/* 433 */     String vminfo = System.getProperty("java.vm.info");
/* 434 */     if (vminfo != null) {
/* 435 */       sb.append(" " + vminfo);
/*     */     }
/* 437 */     sb.append("):\r\n");
/* 438 */     sb.append("\r\n");
/*     */     
/* 440 */     ThreadInfo[] tis = threadMXBean.dumpAllThreads(true, true);
/* 441 */     sb.append(getThreadDump(tis));
/*     */     
/* 443 */     sb.append(findDeadlock());
/* 444 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String formatMemoryUsage(String name, MemoryUsage usage)
/*     */   {
/* 454 */     if (usage != null) {
/* 455 */       StringBuilder sb = new StringBuilder();
/* 456 */       sb.append("  " + name + " init: " + usage.getInit() + "\r\n");
/* 457 */       sb.append("  " + name + " used: " + usage.getUsed() + "\r\n");
/* 458 */       sb.append("  " + name + " committed: " + usage.getCommitted() + "\r\n");
/* 459 */       sb.append("  " + name + " max: " + usage.getMax() + "\r\n");
/* 460 */       return sb.toString();
/*     */     }
/* 462 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getVMInfo()
/*     */   {
/* 472 */     return getVMInfo(sm);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getVMInfo(Enumeration<Locale> requestedLocales)
/*     */   {
/* 484 */     return getVMInfo(StringManager.getManager("org.apache.tomcat.util", requestedLocales));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getVMInfo(StringManager requestedSm)
/*     */   {
/* 495 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 497 */     synchronized (timeformat) {
/* 498 */       sb.append(timeformat.format(new Date()));
/*     */     }
/* 500 */     sb.append("\r\n");
/*     */     
/* 502 */     sb.append(requestedSm.getString("diagnostics.vmInfoRuntime"));
/* 503 */     sb.append(":\r\n");
/* 504 */     sb.append("  vmName: " + runtimeMXBean.getVmName() + "\r\n");
/* 505 */     sb.append("  vmVersion: " + runtimeMXBean.getVmVersion() + "\r\n");
/* 506 */     sb.append("  vmVendor: " + runtimeMXBean.getVmVendor() + "\r\n");
/* 507 */     sb.append("  specName: " + runtimeMXBean.getSpecName() + "\r\n");
/* 508 */     sb.append("  specVersion: " + runtimeMXBean.getSpecVersion() + "\r\n");
/* 509 */     sb.append("  specVendor: " + runtimeMXBean.getSpecVendor() + "\r\n");
/* 510 */     sb.append("  managementSpecVersion: " + runtimeMXBean
/* 511 */       .getManagementSpecVersion() + "\r\n");
/* 512 */     sb.append("  name: " + runtimeMXBean.getName() + "\r\n");
/* 513 */     sb.append("  startTime: " + runtimeMXBean.getStartTime() + "\r\n");
/* 514 */     sb.append("  uptime: " + runtimeMXBean.getUptime() + "\r\n");
/* 515 */     sb.append("  isBootClassPathSupported: " + runtimeMXBean
/* 516 */       .isBootClassPathSupported() + "\r\n");
/* 517 */     sb.append("\r\n");
/*     */     
/* 519 */     sb.append(requestedSm.getString("diagnostics.vmInfoOs"));
/* 520 */     sb.append(":\r\n");
/* 521 */     sb.append("  name: " + operatingSystemMXBean.getName() + "\r\n");
/* 522 */     sb.append("  version: " + operatingSystemMXBean.getVersion() + "\r\n");
/* 523 */     sb.append("  architecture: " + operatingSystemMXBean.getArch() + "\r\n");
/* 524 */     sb.append("  availableProcessors: " + operatingSystemMXBean
/* 525 */       .getAvailableProcessors() + "\r\n");
/* 526 */     sb.append("  systemLoadAverage: " + operatingSystemMXBean
/* 527 */       .getSystemLoadAverage() + "\r\n");
/* 528 */     sb.append("\r\n");
/*     */     
/* 530 */     sb.append(requestedSm.getString("diagnostics.vmInfoThreadMxBean"));
/* 531 */     sb.append(":\r\n");
/* 532 */     sb.append("  isCurrentThreadCpuTimeSupported: " + threadMXBean
/* 533 */       .isCurrentThreadCpuTimeSupported() + "\r\n");
/* 534 */     sb.append("  isThreadCpuTimeSupported: " + threadMXBean
/* 535 */       .isThreadCpuTimeSupported() + "\r\n");
/* 536 */     sb.append("  isThreadCpuTimeEnabled: " + threadMXBean
/* 537 */       .isThreadCpuTimeEnabled() + "\r\n");
/* 538 */     sb.append("  isObjectMonitorUsageSupported: " + threadMXBean
/* 539 */       .isObjectMonitorUsageSupported() + "\r\n");
/* 540 */     sb.append("  isSynchronizerUsageSupported: " + threadMXBean
/* 541 */       .isSynchronizerUsageSupported() + "\r\n");
/* 542 */     sb.append("  isThreadContentionMonitoringSupported: " + threadMXBean
/* 543 */       .isThreadContentionMonitoringSupported() + "\r\n");
/* 544 */     sb.append("  isThreadContentionMonitoringEnabled: " + threadMXBean
/* 545 */       .isThreadContentionMonitoringEnabled() + "\r\n");
/* 546 */     sb.append("\r\n");
/*     */     
/* 548 */     sb.append(requestedSm.getString("diagnostics.vmInfoThreadCounts"));
/* 549 */     sb.append(":\r\n");
/* 550 */     sb.append("  daemon: " + threadMXBean.getDaemonThreadCount() + "\r\n");
/* 551 */     sb.append("  total: " + threadMXBean.getThreadCount() + "\r\n");
/* 552 */     sb.append("  peak: " + threadMXBean.getPeakThreadCount() + "\r\n");
/* 553 */     sb.append("  totalStarted: " + threadMXBean
/* 554 */       .getTotalStartedThreadCount() + "\r\n");
/* 555 */     sb.append("\r\n");
/*     */     
/* 557 */     sb.append(requestedSm.getString("diagnostics.vmInfoStartup"));
/* 558 */     sb.append(":\r\n");
/* 559 */     for (??? = runtimeMXBean.getInputArguments().iterator(); ((Iterator)???).hasNext();) { String arg = (String)((Iterator)???).next();
/* 560 */       sb.append("  " + arg + "\r\n");
/*     */     }
/* 562 */     sb.append("\r\n");
/*     */     
/* 564 */     sb.append(requestedSm.getString("diagnostics.vmInfoPath"));
/* 565 */     sb.append(":\r\n");
/* 566 */     sb.append("  bootClassPath: " + runtimeMXBean.getBootClassPath() + "\r\n");
/* 567 */     sb.append("  classPath: " + runtimeMXBean.getClassPath() + "\r\n");
/* 568 */     sb.append("  libraryPath: " + runtimeMXBean.getLibraryPath() + "\r\n");
/* 569 */     sb.append("\r\n");
/*     */     
/* 571 */     sb.append(requestedSm.getString("diagnostics.vmInfoClassLoading"));
/* 572 */     sb.append(":\r\n");
/* 573 */     sb.append("  loaded: " + classLoadingMXBean
/* 574 */       .getLoadedClassCount() + "\r\n");
/* 575 */     sb.append("  unloaded: " + classLoadingMXBean
/* 576 */       .getUnloadedClassCount() + "\r\n");
/* 577 */     sb.append("  totalLoaded: " + classLoadingMXBean
/* 578 */       .getTotalLoadedClassCount() + "\r\n");
/* 579 */     sb.append("  isVerbose: " + classLoadingMXBean
/* 580 */       .isVerbose() + "\r\n");
/* 581 */     sb.append("\r\n");
/*     */     
/* 583 */     sb.append(requestedSm.getString("diagnostics.vmInfoClassCompilation"));
/* 584 */     sb.append(":\r\n");
/* 585 */     sb.append("  name: " + compilationMXBean.getName() + "\r\n");
/* 586 */     sb.append("  totalCompilationTime: " + compilationMXBean
/* 587 */       .getTotalCompilationTime() + "\r\n");
/* 588 */     sb.append("  isCompilationTimeMonitoringSupported: " + compilationMXBean
/* 589 */       .isCompilationTimeMonitoringSupported() + "\r\n");
/* 590 */     sb.append("\r\n");
/*     */     
/* 592 */     for (??? = memoryManagerMXBeans.iterator(); ((Iterator)???).hasNext();) { MemoryManagerMXBean mbean = (MemoryManagerMXBean)((Iterator)???).next();
/* 593 */       sb.append(requestedSm.getString("diagnostics.vmInfoMemoryManagers", new Object[] { mbean.getName() }));
/* 594 */       sb.append(":\r\n");
/* 595 */       sb.append("  isValid: " + mbean.isValid() + "\r\n");
/* 596 */       sb.append("  mbean.getMemoryPoolNames: \r\n");
/* 597 */       String[] names = mbean.getMemoryPoolNames();
/* 598 */       Arrays.sort(names);
/* 599 */       for (String name : names) {
/* 600 */         sb.append("\t" + name + "\r\n");
/*     */       }
/* 602 */       sb.append("\r\n");
/*     */     }
/*     */     
/* 605 */     for (??? = garbageCollectorMXBeans.iterator(); ((Iterator)???).hasNext();) { GarbageCollectorMXBean mbean = (GarbageCollectorMXBean)((Iterator)???).next();
/* 606 */       sb.append(requestedSm.getString("diagnostics.vmInfoGarbageCollectors", new Object[] { mbean.getName() }));
/* 607 */       sb.append(":\r\n");
/* 608 */       sb.append("  isValid: " + mbean.isValid() + "\r\n");
/* 609 */       sb.append("  mbean.getMemoryPoolNames: \r\n");
/* 610 */       String[] names = mbean.getMemoryPoolNames();
/* 611 */       Arrays.sort(names);
/* 612 */       for (String name : names) {
/* 613 */         sb.append("\t" + name + "\r\n");
/*     */       }
/* 615 */       sb.append("  getCollectionCount: " + mbean.getCollectionCount() + "\r\n");
/* 616 */       sb.append("  getCollectionTime: " + mbean.getCollectionTime() + "\r\n");
/* 617 */       sb.append("\r\n");
/*     */     }
/*     */     
/* 620 */     sb.append(requestedSm.getString("diagnostics.vmInfoMemory"));
/* 621 */     sb.append(":\r\n");
/* 622 */     sb.append("  isVerbose: " + memoryMXBean.isVerbose() + "\r\n");
/* 623 */     sb.append("  getObjectPendingFinalizationCount: " + memoryMXBean.getObjectPendingFinalizationCount() + "\r\n");
/* 624 */     sb.append(formatMemoryUsage("heap", memoryMXBean.getHeapMemoryUsage()));
/* 625 */     sb.append(formatMemoryUsage("non-heap", memoryMXBean.getNonHeapMemoryUsage()));
/* 626 */     sb.append("\r\n");
/*     */     
/* 628 */     for (??? = memoryPoolMXBeans.iterator(); ((Iterator)???).hasNext();) { MemoryPoolMXBean mbean = (MemoryPoolMXBean)((Iterator)???).next();
/* 629 */       sb.append(requestedSm.getString("diagnostics.vmInfoMemoryPools", new Object[] { mbean.getName() }));
/* 630 */       sb.append(":\r\n");
/* 631 */       sb.append("  isValid: " + mbean.isValid() + "\r\n");
/* 632 */       sb.append("  getType: " + mbean.getType() + "\r\n");
/* 633 */       sb.append("  mbean.getMemoryManagerNames: \r\n");
/* 634 */       names = mbean.getMemoryManagerNames();
/* 635 */       Arrays.sort(names);
/* 636 */       for (String name : names) {
/* 637 */         sb.append("\t" + name + "\r\n");
/*     */       }
/* 639 */       sb.append("  isUsageThresholdSupported: " + mbean.isUsageThresholdSupported() + "\r\n");
/*     */       try {
/* 641 */         sb.append("  isUsageThresholdExceeded: " + mbean.isUsageThresholdExceeded() + "\r\n");
/*     */       }
/*     */       catch (UnsupportedOperationException localUnsupportedOperationException6) {}
/*     */       
/* 645 */       sb.append("  isCollectionUsageThresholdSupported: " + mbean.isCollectionUsageThresholdSupported() + "\r\n");
/*     */       try {
/* 647 */         sb.append("  isCollectionUsageThresholdExceeded: " + mbean.isCollectionUsageThresholdExceeded() + "\r\n");
/*     */       }
/*     */       catch (UnsupportedOperationException localUnsupportedOperationException7) {}
/*     */       try
/*     */       {
/* 652 */         sb.append("  getUsageThreshold: " + mbean.getUsageThreshold() + "\r\n");
/*     */       }
/*     */       catch (UnsupportedOperationException localUnsupportedOperationException8) {}
/*     */       try
/*     */       {
/* 657 */         sb.append("  getUsageThresholdCount: " + mbean.getUsageThresholdCount() + "\r\n");
/*     */       }
/*     */       catch (UnsupportedOperationException localUnsupportedOperationException9) {}
/*     */       try
/*     */       {
/* 662 */         sb.append("  getCollectionUsageThreshold: " + mbean.getCollectionUsageThreshold() + "\r\n");
/*     */       }
/*     */       catch (UnsupportedOperationException localUnsupportedOperationException10) {}
/*     */       try
/*     */       {
/* 667 */         sb.append("  getCollectionUsageThresholdCount: " + mbean.getCollectionUsageThresholdCount() + "\r\n");
/*     */       }
/*     */       catch (UnsupportedOperationException localUnsupportedOperationException11) {}
/*     */       
/* 671 */       sb.append(formatMemoryUsage("current", mbean.getUsage()));
/* 672 */       sb.append(formatMemoryUsage("collection", mbean.getCollectionUsage()));
/* 673 */       sb.append(formatMemoryUsage("peak", mbean.getPeakUsage()));
/* 674 */       sb.append("\r\n");
/*     */     }
/*     */     
/*     */ 
/* 678 */     sb.append(requestedSm.getString("diagnostics.vmInfoSystem"));
/* 679 */     sb.append(":\r\n");
/* 680 */     Map<String, String> props = runtimeMXBean.getSystemProperties();
/* 681 */     Object keys = new ArrayList(props.keySet());
/* 682 */     Collections.sort((List)keys);
/* 683 */     for (String[] names = ((ArrayList)keys).iterator(); names.hasNext();) { prop = (String)names.next();
/* 684 */       sb.append("  " + (String)prop + ": " + (String)props.get(prop) + "\r\n");
/*     */     }
/* 686 */     sb.append("\r\n");
/*     */     
/* 688 */     sb.append(requestedSm.getString("diagnostics.vmInfoLogger"));
/* 689 */     sb.append(":\r\n");
/* 690 */     List<String> loggers = loggingMXBean.getLoggerNames();
/* 691 */     Collections.sort(loggers);
/* 692 */     for (Object prop = loggers.iterator(); ((Iterator)prop).hasNext();) { String logger = (String)((Iterator)prop).next();
/* 693 */       sb.append("  " + logger + ": level=" + loggingMXBean
/* 694 */         .getLoggerLevel(logger) + ", parent=" + loggingMXBean
/* 695 */         .getParentLoggerName(logger) + "\r\n");
/*     */     }
/* 697 */     sb.append("\r\n");
/*     */     
/* 699 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\Diagnostics.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */