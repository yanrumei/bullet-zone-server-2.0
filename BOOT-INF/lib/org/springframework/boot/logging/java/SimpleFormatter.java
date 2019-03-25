/*    */ package org.springframework.boot.logging.java;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.io.StringWriter;
/*    */ import java.util.Date;
/*    */ import java.util.logging.Formatter;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.LogRecord;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleFormatter
/*    */   extends Formatter
/*    */ {
/*    */   private static final String DEFAULT_FORMAT = "[%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL] - %8$s %4$s [%7$s] --- %3$s: %5$s%6$s%n";
/* 34 */   private final String format = getOrUseDefault("LOG_FORMAT", "[%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL] - %8$s %4$s [%7$s] --- %3$s: %5$s%6$s%n");
/*    */   
/* 36 */   private final String pid = getOrUseDefault("PID", "????");
/*    */   
/* 38 */   private final Date date = new Date();
/*    */   
/*    */   public synchronized String format(LogRecord record)
/*    */   {
/* 42 */     this.date.setTime(record.getMillis());
/* 43 */     String source = record.getLoggerName();
/* 44 */     String message = formatMessage(record);
/* 45 */     String throwable = getThrowable(record);
/* 46 */     String thread = getThreadName();
/* 47 */     return String.format(this.format, new Object[] { this.date, source, record.getLoggerName(), record
/* 48 */       .getLevel().getLocalizedName(), message, throwable, thread, this.pid });
/*    */   }
/*    */   
/*    */   private String getThrowable(LogRecord record)
/*    */   {
/* 53 */     if (record.getThrown() == null) {
/* 54 */       return "";
/*    */     }
/* 56 */     StringWriter stringWriter = new StringWriter();
/* 57 */     PrintWriter printWriter = new PrintWriter(stringWriter);
/* 58 */     printWriter.println();
/* 59 */     record.getThrown().printStackTrace(printWriter);
/* 60 */     printWriter.close();
/* 61 */     return stringWriter.toString();
/*    */   }
/*    */   
/*    */   private String getThreadName() {
/* 65 */     String name = Thread.currentThread().getName();
/* 66 */     return name == null ? "" : name;
/*    */   }
/*    */   
/*    */   private static String getOrUseDefault(String key, String defaultValue) {
/* 70 */     String value = null;
/*    */     try {
/* 72 */       value = System.getenv(key);
/*    */     }
/*    */     catch (Exception localException) {}
/*    */     
/*    */ 
/* 77 */     if (value == null) {
/* 78 */       value = defaultValue;
/*    */     }
/* 80 */     return System.getProperty(key, value);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\java\SimpleFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */