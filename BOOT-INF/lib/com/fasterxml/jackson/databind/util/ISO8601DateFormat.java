/*    */ package com.fasterxml.jackson.databind.util;
/*    */ 
/*    */ import java.text.DateFormat;
/*    */ import java.text.DecimalFormat;
/*    */ import java.text.FieldPosition;
/*    */ import java.text.NumberFormat;
/*    */ import java.text.ParseException;
/*    */ import java.text.ParsePosition;
/*    */ import java.util.Calendar;
/*    */ import java.util.Date;
/*    */ import java.util.GregorianCalendar;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ISO8601DateFormat
/*    */   extends DateFormat
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 23 */   private static Calendar CALENDAR = new GregorianCalendar();
/* 24 */   private static NumberFormat NUMBER_FORMAT = new DecimalFormat();
/*    */   
/*    */   public ISO8601DateFormat() {
/* 27 */     this.numberFormat = NUMBER_FORMAT;
/* 28 */     this.calendar = CALENDAR;
/*    */   }
/*    */   
/*    */   public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition)
/*    */   {
/* 33 */     String value = ISO8601Utils.format(date);
/* 34 */     toAppendTo.append(value);
/* 35 */     return toAppendTo;
/*    */   }
/*    */   
/*    */   public Date parse(String source, ParsePosition pos)
/*    */   {
/*    */     try {
/* 41 */       return ISO8601Utils.parse(source, pos);
/*    */     }
/*    */     catch (ParseException e) {}
/* 44 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Date parse(String source)
/*    */     throws ParseException
/*    */   {
/* 52 */     return ISO8601Utils.parse(source, new ParsePosition(0));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Object clone()
/*    */   {
/* 61 */     return this;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 65 */     return getClass().getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databin\\util\ISO8601DateFormat.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */