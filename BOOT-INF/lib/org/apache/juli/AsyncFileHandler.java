/*     */ package org.apache.juli;
/*     */ 
/*     */ import java.util.concurrent.LinkedBlockingDeque;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.logging.LogRecord;
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
/*     */ public class AsyncFileHandler
/*     */   extends FileHandler
/*     */ {
/*     */   public static final int OVERFLOW_DROP_LAST = 1;
/*     */   public static final int OVERFLOW_DROP_FIRST = 2;
/*     */   public static final int OVERFLOW_DROP_FLUSH = 3;
/*     */   public static final int OVERFLOW_DROP_CURRENT = 4;
/*     */   public static final int DEFAULT_OVERFLOW_DROP_TYPE = 1;
/*     */   public static final int DEFAULT_MAX_RECORDS = 10000;
/*     */   public static final int DEFAULT_LOGGER_SLEEP_TIME = 1000;
/*  52 */   public static final int OVERFLOW_DROP_TYPE = Integer.parseInt(
/*  53 */     System.getProperty("org.apache.juli.AsyncOverflowDropType", 
/*  54 */     Integer.toString(1)));
/*  55 */   public static final int MAX_RECORDS = Integer.parseInt(
/*  56 */     System.getProperty("org.apache.juli.AsyncMaxRecordCount", 
/*  57 */     Integer.toString(10000)));
/*  58 */   public static final int LOGGER_SLEEP_TIME = Integer.parseInt(
/*  59 */     System.getProperty("org.apache.juli.AsyncLoggerPollInterval", 
/*  60 */     Integer.toString(1000)));
/*     */   
/*  62 */   protected static final LinkedBlockingDeque<LogEntry> queue = new LinkedBlockingDeque(MAX_RECORDS);
/*     */   
/*     */ 
/*  65 */   protected static final LoggerThread logger = new LoggerThread();
/*     */   
/*     */   static {
/*  68 */     logger.start();
/*     */   }
/*     */   
/*  71 */   protected volatile boolean closed = false;
/*     */   
/*     */   public AsyncFileHandler() {
/*  74 */     this(null, null, null, -1);
/*     */   }
/*     */   
/*     */   public AsyncFileHandler(String directory, String prefix, String suffix) {
/*  78 */     this(directory, prefix, suffix, -1);
/*     */   }
/*     */   
/*     */   public AsyncFileHandler(String directory, String prefix, String suffix, int maxDays) {
/*  82 */     super(directory, prefix, suffix, maxDays);
/*  83 */     open();
/*     */   }
/*     */   
/*     */   public void close()
/*     */   {
/*  88 */     if (this.closed) {
/*  89 */       return;
/*     */     }
/*  91 */     this.closed = true;
/*  92 */     super.close();
/*     */   }
/*     */   
/*     */   protected void open()
/*     */   {
/*  97 */     if (!this.closed) {
/*  98 */       return;
/*     */     }
/* 100 */     this.closed = false;
/* 101 */     super.open();
/*     */   }
/*     */   
/*     */ 
/*     */   public void publish(LogRecord record)
/*     */   {
/* 107 */     if (!isLoggable(record)) {
/* 108 */       return;
/*     */     }
/*     */     
/*     */ 
/* 112 */     record.getSourceMethodName();
/* 113 */     LogEntry entry = new LogEntry(record, this);
/* 114 */     boolean added = false;
/*     */     try {
/* 116 */       while ((!added) && (!queue.offer(entry))) {
/* 117 */         switch (OVERFLOW_DROP_TYPE)
/*     */         {
/*     */         case 1: 
/* 120 */           queue.pollLast();
/* 121 */           break;
/*     */         
/*     */ 
/*     */         case 2: 
/* 125 */           queue.pollFirst();
/* 126 */           break;
/*     */         
/*     */         case 3: 
/* 129 */           added = queue.offer(entry, 1000L, TimeUnit.MILLISECONDS);
/* 130 */           break;
/*     */         
/*     */         case 4: 
/* 133 */           added = true;
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */     catch (InterruptedException localInterruptedException) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void publishInternal(LogRecord record)
/*     */   {
/* 146 */     super.publish(record);
/*     */   }
/*     */   
/*     */   protected static class LoggerThread extends Thread {
/* 150 */     protected final boolean run = true;
/*     */     
/* 152 */     public LoggerThread() { setDaemon(true);
/* 153 */       setName("AsyncFileHandlerWriter-" + System.identityHashCode(this));
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/*     */       for (;;) {
/*     */         try {
/* 160 */           AsyncFileHandler.LogEntry entry = (AsyncFileHandler.LogEntry)AsyncFileHandler.queue.poll(AsyncFileHandler.LOGGER_SLEEP_TIME, TimeUnit.MILLISECONDS);
/* 161 */           if (entry != null) {
/* 162 */             entry.flush();
/*     */           }
/*     */         }
/*     */         catch (InterruptedException localInterruptedException) {}catch (Exception x)
/*     */         {
/* 167 */           x.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class LogEntry {
/*     */     private final LogRecord record;
/*     */     private final AsyncFileHandler handler;
/*     */     
/*     */     public LogEntry(LogRecord record, AsyncFileHandler handler) {
/* 178 */       this.record = record;
/* 179 */       this.handler = handler;
/*     */     }
/*     */     
/*     */     public boolean flush() {
/* 183 */       if (this.handler.closed) {
/* 184 */         return false;
/*     */       }
/* 186 */       this.handler.publishInternal(this.record);
/* 187 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\juli\AsyncFileHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */