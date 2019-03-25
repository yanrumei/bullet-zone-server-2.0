/*     */ package org.apache.catalina.manager;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.text.NumberFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import org.apache.catalina.Session;
/*     */ import org.apache.catalina.manager.util.SessionUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JspHelper
/*     */ {
/*     */   private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
/*     */   private static final int HIGHEST_SPECIAL = 62;
/*     */   
/*  56 */   public static String guessDisplayLocaleFromSession(Session in_session) { return localeToString(SessionUtils.guessLocaleFromSession(in_session)); }
/*     */   
/*     */   private static String localeToString(Locale locale) {
/*  59 */     if (locale != null) {
/*  60 */       return escapeXml(locale.toString());
/*     */     }
/*  62 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String guessDisplayUserFromSession(Session in_session)
/*     */   {
/*  72 */     Object user = SessionUtils.guessUserFromSession(in_session);
/*  73 */     return escapeXml(user);
/*     */   }
/*     */   
/*     */   public static String getDisplayCreationTimeForSession(Session in_session)
/*     */   {
/*     */     try {
/*  79 */       if (in_session.getCreationTime() == 0L) {
/*  80 */         return "";
/*     */       }
/*  82 */       DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/*  83 */       return formatter.format(new Date(in_session.getCreationTime()));
/*     */     }
/*     */     catch (IllegalStateException ise) {}
/*  86 */     return "";
/*     */   }
/*     */   
/*     */   public static String getDisplayLastAccessedTimeForSession(Session in_session)
/*     */   {
/*     */     try {
/*  92 */       if (in_session.getLastAccessedTime() == 0L) {
/*  93 */         return "";
/*     */       }
/*  95 */       DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/*  96 */       return formatter.format(new Date(in_session.getLastAccessedTime()));
/*     */     }
/*     */     catch (IllegalStateException ise) {}
/*  99 */     return "";
/*     */   }
/*     */   
/*     */   public static String getDisplayUsedTimeForSession(Session in_session)
/*     */   {
/*     */     try {
/* 105 */       if (in_session.getCreationTime() == 0L) {
/* 106 */         return "";
/*     */       }
/*     */     }
/*     */     catch (IllegalStateException ise) {
/* 110 */       return "";
/*     */     }
/* 112 */     return secondsToTimeString(SessionUtils.getUsedTimeForSession(in_session) / 1000L);
/*     */   }
/*     */   
/*     */   public static String getDisplayTTLForSession(Session in_session) {
/*     */     try {
/* 117 */       if (in_session.getCreationTime() == 0L) {
/* 118 */         return "";
/*     */       }
/*     */     }
/*     */     catch (IllegalStateException ise) {
/* 122 */       return "";
/*     */     }
/* 124 */     return secondsToTimeString(SessionUtils.getTTLForSession(in_session) / 1000L);
/*     */   }
/*     */   
/*     */   public static String getDisplayInactiveTimeForSession(Session in_session) {
/*     */     try {
/* 129 */       if (in_session.getCreationTime() == 0L) {
/* 130 */         return "";
/*     */       }
/*     */     }
/*     */     catch (IllegalStateException ise) {
/* 134 */       return "";
/*     */     }
/* 136 */     return secondsToTimeString(SessionUtils.getInactiveTimeForSession(in_session) / 1000L);
/*     */   }
/*     */   
/*     */   public static String secondsToTimeString(long in_seconds) {
/* 140 */     StringBuilder buff = new StringBuilder(9);
/* 141 */     if (in_seconds < 0L) {
/* 142 */       buff.append('-');
/* 143 */       in_seconds = -in_seconds;
/*     */     }
/* 145 */     long rest = in_seconds;
/* 146 */     long hour = rest / 3600L;
/* 147 */     rest %= 3600L;
/* 148 */     long minute = rest / 60L;
/* 149 */     rest %= 60L;
/* 150 */     long second = rest;
/* 151 */     if (hour < 10L) {
/* 152 */       buff.append('0');
/*     */     }
/* 154 */     buff.append(hour);
/* 155 */     buff.append(':');
/* 156 */     if (minute < 10L) {
/* 157 */       buff.append('0');
/*     */     }
/* 159 */     buff.append(minute);
/* 160 */     buff.append(':');
/* 161 */     if (second < 10L) {
/* 162 */       buff.append('0');
/*     */     }
/* 164 */     buff.append(second);
/* 165 */     return buff.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 174 */   private static final char[][] specialCharactersRepresentation = new char[63][];
/*     */   
/*     */   static {
/* 177 */     specialCharactersRepresentation[38] = "&amp;".toCharArray();
/* 178 */     specialCharactersRepresentation[60] = "&lt;".toCharArray();
/* 179 */     specialCharactersRepresentation[62] = "&gt;".toCharArray();
/* 180 */     specialCharactersRepresentation[34] = "&#034;".toCharArray();
/* 181 */     specialCharactersRepresentation[39] = "&#039;".toCharArray();
/*     */   }
/*     */   
/*     */   public static String escapeXml(Object obj) {
/* 185 */     String value = null;
/*     */     try {
/* 187 */       value = obj == null ? null : obj.toString();
/*     */     }
/*     */     catch (Exception localException) {}
/*     */     
/* 191 */     return escapeXml(value);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String escapeXml(String buffer)
/*     */   {
/* 210 */     if (buffer == null) {
/* 211 */       return "";
/*     */     }
/* 213 */     int start = 0;
/* 214 */     int length = buffer.length();
/* 215 */     char[] arrayBuffer = buffer.toCharArray();
/* 216 */     StringBuilder escapedBuffer = null;
/*     */     
/* 218 */     for (int i = 0; i < length; i++) {
/* 219 */       char c = arrayBuffer[i];
/* 220 */       if (c <= '>') {
/* 221 */         char[] escaped = specialCharactersRepresentation[c];
/* 222 */         if (escaped != null)
/*     */         {
/* 224 */           if (start == 0) {
/* 225 */             escapedBuffer = new StringBuilder(length + 5);
/*     */           }
/*     */           
/* 228 */           if (start < i) {
/* 229 */             escapedBuffer.append(arrayBuffer, start, i - start);
/*     */           }
/* 231 */           start = i + 1;
/*     */           
/* 233 */           escapedBuffer.append(escaped);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 238 */     if (start == 0) {
/* 239 */       return buffer;
/*     */     }
/*     */     
/* 242 */     if (start < length) {
/* 243 */       escapedBuffer.append(arrayBuffer, start, length - start);
/*     */     }
/* 245 */     return escapedBuffer.toString();
/*     */   }
/*     */   
/*     */   public static String formatNumber(long number) {
/* 249 */     return NumberFormat.getNumberInstance().format(number);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\manager\JspHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */