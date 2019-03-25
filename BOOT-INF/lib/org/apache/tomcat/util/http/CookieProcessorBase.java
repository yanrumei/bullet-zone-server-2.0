/*    */ package org.apache.tomcat.util.http;
/*    */ 
/*    */ import java.text.DateFormat;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.Locale;
/*    */ import java.util.TimeZone;
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
/*    */ public abstract class CookieProcessorBase
/*    */   implements CookieProcessor
/*    */ {
/*    */   private static final String COOKIE_DATE_PATTERN = "EEE, dd-MMM-yyyy HH:mm:ss z";
/* 29 */   protected static final ThreadLocal<DateFormat> COOKIE_DATE_FORMAT = new ThreadLocal()
/*    */   {
/*    */     protected DateFormat initialValue()
/*    */     {
/* 33 */       DateFormat df = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss z", Locale.US);
/*    */       
/* 35 */       df.setTimeZone(TimeZone.getTimeZone("GMT"));
/* 36 */       return df;
/*    */     }
/*    */   };
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 43 */   protected static final String ANCIENT_DATE = ((DateFormat)COOKIE_DATE_FORMAT.get()).format(new Date(10000L));
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\CookieProcessorBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */