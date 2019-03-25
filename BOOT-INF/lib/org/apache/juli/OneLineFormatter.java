/*     */ package org.apache.juli;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.ThreadInfo;
/*     */ import java.lang.management.ThreadMXBean;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.logging.Formatter;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.LogManager;
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
/*     */ public class OneLineFormatter
/*     */   extends Formatter
/*     */ {
/*  41 */   private static final String ST_SEP = System.lineSeparator() + " ";
/*     */   private static final String UNKNOWN_THREAD_NAME = "Unknown thread with ID ";
/*  43 */   private static final Object threadMxBeanLock = new Object();
/*  44 */   private static volatile ThreadMXBean threadMxBean = null;
/*     */   private static final int THREAD_NAME_CACHE_SIZE = 10000;
/*  46 */   private static ThreadLocal<LinkedHashMap<Integer, String>> threadNameCache = new ThreadLocal()
/*     */   {
/*     */ 
/*     */     protected LinkedHashMap<Integer, String> initialValue()
/*     */     {
/*  51 */       new LinkedHashMap()
/*     */       {
/*     */         private static final long serialVersionUID = 1L;
/*     */         
/*     */ 
/*     */         protected boolean removeEldestEntry(Map.Entry<Integer, String> eldest)
/*     */         {
/*  58 */           return size() > 10000;
/*     */         }
/*     */       };
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String DEFAULT_TIME_FORMAT = "dd-MMM-yyyy HH:mm:ss";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int globalCacheSize = 30;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int localCacheSize = 5;
/*     */   
/*     */ 
/*     */ 
/*     */   private ThreadLocal<DateFormatCache> localDateCache;
/*     */   
/*     */ 
/*     */ 
/*     */   public OneLineFormatter()
/*     */   {
/*  84 */     String timeFormat = LogManager.getLogManager().getProperty(OneLineFormatter.class
/*  85 */       .getName() + ".timeFormat");
/*  86 */     if (timeFormat == null) {
/*  87 */       timeFormat = "dd-MMM-yyyy HH:mm:ss";
/*     */     }
/*  89 */     setTimeFormat(timeFormat);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTimeFormat(final String timeFormat)
/*     */   {
/* 100 */     final DateFormatCache globalDateCache = new DateFormatCache(30, timeFormat, null);
/*     */     
/* 102 */     this.localDateCache = new ThreadLocal()
/*     */     {
/*     */       protected DateFormatCache initialValue() {
/* 105 */         return new DateFormatCache(5, timeFormat, globalDateCache);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getTimeFormat()
/*     */   {
/* 117 */     return ((DateFormatCache)this.localDateCache.get()).getTimeFormat();
/*     */   }
/*     */   
/*     */ 
/*     */   public String format(LogRecord record)
/*     */   {
/* 123 */     StringBuilder sb = new StringBuilder();
/*     */     
/*     */ 
/* 126 */     addTimestamp(sb, record.getMillis());
/*     */     
/*     */ 
/* 129 */     sb.append(' ');
/* 130 */     sb.append(record.getLevel().getLocalizedName());
/*     */     
/*     */ 
/* 133 */     sb.append(' ');
/* 134 */     sb.append('[');
/* 135 */     if ((Thread.currentThread() instanceof AsyncFileHandler.LoggerThread))
/*     */     {
/*     */ 
/* 138 */       sb.append(getThreadName(record.getThreadID()));
/*     */     } else {
/* 140 */       sb.append(Thread.currentThread().getName());
/*     */     }
/* 142 */     sb.append(']');
/*     */     
/*     */ 
/* 145 */     sb.append(' ');
/* 146 */     sb.append(record.getSourceClassName());
/* 147 */     sb.append('.');
/* 148 */     sb.append(record.getSourceMethodName());
/*     */     
/*     */ 
/* 151 */     sb.append(' ');
/* 152 */     sb.append(formatMessage(record));
/*     */     
/*     */ 
/* 155 */     if (record.getThrown() != null) {
/* 156 */       sb.append(ST_SEP);
/* 157 */       StringWriter sw = new StringWriter();
/* 158 */       PrintWriter pw = new PrintWriter(sw);
/* 159 */       record.getThrown().printStackTrace(pw);
/* 160 */       pw.close();
/* 161 */       sb.append(sw.getBuffer());
/*     */     }
/*     */     
/*     */ 
/* 165 */     sb.append(System.lineSeparator());
/*     */     
/* 167 */     return sb.toString();
/*     */   }
/*     */   
/*     */   protected void addTimestamp(StringBuilder buf, long timestamp) {
/* 171 */     buf.append(((DateFormatCache)this.localDateCache.get()).getFormat(timestamp));
/* 172 */     long frac = timestamp % 1000L;
/* 173 */     buf.append('.');
/* 174 */     if (frac < 100L) {
/* 175 */       if (frac < 10L) {
/* 176 */         buf.append('0');
/* 177 */         buf.append('0');
/*     */       } else {
/* 179 */         buf.append('0');
/*     */       }
/*     */     }
/* 182 */     buf.append(frac);
/*     */   }
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
/*     */   private static String getThreadName(int logRecordThreadId)
/*     */   {
/* 196 */     Map<Integer, String> cache = (Map)threadNameCache.get();
/* 197 */     String result = null;
/*     */     
/* 199 */     if (logRecordThreadId > 1073741823) {
/* 200 */       result = (String)cache.get(Integer.valueOf(logRecordThreadId));
/*     */     }
/*     */     
/* 203 */     if (result != null) {
/* 204 */       return result;
/*     */     }
/*     */     
/* 207 */     if (logRecordThreadId > 1073741823) {
/* 208 */       result = "Unknown thread with ID " + logRecordThreadId;
/*     */     }
/*     */     else {
/* 211 */       if (threadMxBean == null) {
/* 212 */         synchronized (threadMxBeanLock) {
/* 213 */           if (threadMxBean == null) {
/* 214 */             threadMxBean = ManagementFactory.getThreadMXBean();
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 219 */       ThreadInfo threadInfo = threadMxBean.getThreadInfo(logRecordThreadId);
/* 220 */       if (threadInfo == null) {
/* 221 */         return Long.toString(logRecordThreadId);
/*     */       }
/* 223 */       result = threadInfo.getThreadName();
/*     */     }
/*     */     
/* 226 */     cache.put(Integer.valueOf(logRecordThreadId), result);
/*     */     
/* 228 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\juli\OneLineFormatter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */