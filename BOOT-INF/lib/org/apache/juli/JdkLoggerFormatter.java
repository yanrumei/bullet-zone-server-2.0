/*     */ package org.apache.juli;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.util.logging.Formatter;
/*     */ import java.util.logging.Level;
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
/*     */ public class JdkLoggerFormatter
/*     */   extends Formatter
/*     */ {
/*     */   public static final int LOG_LEVEL_TRACE = 400;
/*     */   public static final int LOG_LEVEL_DEBUG = 500;
/*     */   public static final int LOG_LEVEL_INFO = 800;
/*     */   public static final int LOG_LEVEL_WARN = 900;
/*     */   public static final int LOG_LEVEL_ERROR = 1000;
/*     */   public static final int LOG_LEVEL_FATAL = 1000;
/*     */   
/*     */   public String format(LogRecord record)
/*     */   {
/*  52 */     Throwable t = record.getThrown();
/*  53 */     int level = record.getLevel().intValue();
/*  54 */     String name = record.getLoggerName();
/*  55 */     long time = record.getMillis();
/*  56 */     String message = formatMessage(record);
/*     */     
/*     */ 
/*  59 */     if (name.indexOf('.') >= 0) {
/*  60 */       name = name.substring(name.lastIndexOf('.') + 1);
/*     */     }
/*     */     
/*  63 */     StringBuilder buf = new StringBuilder();
/*     */     
/*  65 */     buf.append(time);
/*     */     
/*     */ 
/*  68 */     for (int i = 0; i < 8 - buf.length(); i++) { buf.append(" ");
/*     */     }
/*     */     
/*  71 */     switch (level) {
/*  72 */     case 400:  buf.append(" T "); break;
/*  73 */     case 500:  buf.append(" D "); break;
/*  74 */     case 800:  buf.append(" I "); break;
/*  75 */     case 900:  buf.append(" W "); break;
/*  76 */     case 1000:  buf.append(" E "); break;
/*     */     default: 
/*  78 */       buf.append("   ");
/*     */     }
/*     */     
/*     */     
/*     */ 
/*  83 */     buf.append(name);
/*  84 */     buf.append(" ");
/*     */     
/*     */ 
/*  87 */     for (int i = 0; i < 8 - buf.length(); i++) { buf.append(" ");
/*     */     }
/*     */     
/*  90 */     buf.append(message);
/*     */     
/*     */ 
/*  93 */     if (t != null) {
/*  94 */       buf.append(System.lineSeparator());
/*     */       
/*  96 */       StringWriter sw = new StringWriter(1024);
/*  97 */       PrintWriter pw = new PrintWriter(sw);
/*  98 */       t.printStackTrace(pw);
/*  99 */       pw.close();
/* 100 */       buf.append(sw.toString());
/*     */     }
/*     */     
/* 103 */     buf.append(System.lineSeparator());
/*     */     
/* 105 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\juli\JdkLoggerFormatter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */