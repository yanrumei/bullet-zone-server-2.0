/*    */ package org.apache.catalina.util;
/*    */ 
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.Locale;
/*    */ import java.util.Queue;
/*    */ import java.util.TimeZone;
/*    */ import java.util.concurrent.ConcurrentLinkedQueue;
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
/*    */ 
/*    */ public class ConcurrentDateFormat
/*    */ {
/*    */   private final String format;
/*    */   private final Locale locale;
/*    */   private final TimeZone timezone;
/* 36 */   private final Queue<SimpleDateFormat> queue = new ConcurrentLinkedQueue();
/*    */   
/*    */   public static final String RFC1123_DATE = "EEE, dd MMM yyyy HH:mm:ss zzz";
/* 39 */   public static final TimeZone GMT = TimeZone.getTimeZone("GMT");
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 44 */   private static final ConcurrentDateFormat FORMAT_RFC1123 = new ConcurrentDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US, GMT);
/*    */   
/*    */   public static String formatRfc1123(Date date)
/*    */   {
/* 48 */     return FORMAT_RFC1123.format(date);
/*    */   }
/*    */   
/*    */   public ConcurrentDateFormat(String format, Locale locale, TimeZone timezone)
/*    */   {
/* 53 */     this.format = format;
/* 54 */     this.locale = locale;
/* 55 */     this.timezone = timezone;
/* 56 */     SimpleDateFormat initial = createInstance();
/* 57 */     this.queue.add(initial);
/*    */   }
/*    */   
/*    */   public String format(Date date) {
/* 61 */     SimpleDateFormat sdf = (SimpleDateFormat)this.queue.poll();
/* 62 */     if (sdf == null) {
/* 63 */       sdf = createInstance();
/*    */     }
/* 65 */     String result = sdf.format(date);
/* 66 */     this.queue.add(sdf);
/* 67 */     return result;
/*    */   }
/*    */   
/*    */   private SimpleDateFormat createInstance() {
/* 71 */     SimpleDateFormat sdf = new SimpleDateFormat(this.format, this.locale);
/* 72 */     sdf.setTimeZone(this.timezone);
/* 73 */     return sdf;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\util\ConcurrentDateFormat.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */