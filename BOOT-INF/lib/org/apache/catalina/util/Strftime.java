/*     */ package org.apache.catalina.util;
/*     */ 
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.Properties;
/*     */ import java.util.TimeZone;
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
/*     */ 
/*     */ public class Strftime
/*     */ {
/*  52 */   protected static final Properties translate = new Properties();
/*  53 */   static { translate.put("a", "EEE");
/*  54 */     translate.put("A", "EEEE");
/*  55 */     translate.put("b", "MMM");
/*  56 */     translate.put("B", "MMMM");
/*  57 */     translate.put("c", "EEE MMM d HH:mm:ss yyyy");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  62 */     translate.put("d", "dd");
/*  63 */     translate.put("D", "MM/dd/yy");
/*  64 */     translate.put("e", "dd");
/*  65 */     translate.put("F", "yyyy-MM-dd");
/*  66 */     translate.put("g", "yy");
/*  67 */     translate.put("G", "yyyy");
/*  68 */     translate.put("H", "HH");
/*  69 */     translate.put("h", "MMM");
/*  70 */     translate.put("I", "hh");
/*  71 */     translate.put("j", "DDD");
/*  72 */     translate.put("k", "HH");
/*  73 */     translate.put("l", "hh");
/*  74 */     translate.put("m", "MM");
/*  75 */     translate.put("M", "mm");
/*  76 */     translate.put("n", "\n");
/*  77 */     translate.put("p", "a");
/*  78 */     translate.put("P", "a");
/*  79 */     translate.put("r", "hh:mm:ss a");
/*  80 */     translate.put("R", "HH:mm");
/*     */     
/*     */ 
/*  83 */     translate.put("S", "ss");
/*  84 */     translate.put("t", "\t");
/*  85 */     translate.put("T", "HH:mm:ss");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  92 */     translate.put("V", "ww");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  99 */     translate.put("X", "HH:mm:ss");
/* 100 */     translate.put("x", "MM/dd/yy");
/* 101 */     translate.put("y", "yy");
/* 102 */     translate.put("Y", "yyyy");
/* 103 */     translate.put("Z", "z");
/* 104 */     translate.put("z", "Z");
/* 105 */     translate.put("%", "%");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Strftime(String origFormat, Locale locale)
/*     */   {
/* 116 */     String convertedFormat = convertDateFormat(origFormat);
/* 117 */     this.simpleDateFormat = new SimpleDateFormat(convertedFormat, locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String format(Date date)
/*     */   {
/* 127 */     return this.simpleDateFormat.format(date);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TimeZone getTimeZone()
/*     */   {
/* 136 */     return this.simpleDateFormat.getTimeZone();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTimeZone(TimeZone timeZone)
/*     */   {
/* 146 */     this.simpleDateFormat.setTimeZone(timeZone);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String convertDateFormat(String pattern)
/*     */   {
/* 158 */     boolean inside = false;
/* 159 */     boolean mark = false;
/* 160 */     boolean modifiedCommand = false;
/*     */     
/* 162 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 164 */     for (int i = 0; i < pattern.length(); i++) {
/* 165 */       char c = pattern.charAt(i);
/*     */       
/* 167 */       if ((c == '%') && (!mark)) {
/* 168 */         mark = true;
/*     */       }
/* 170 */       else if (mark) {
/* 171 */         if (modifiedCommand)
/*     */         {
/* 173 */           modifiedCommand = false;
/* 174 */           mark = false;
/*     */         } else {
/* 176 */           inside = translateCommand(buf, pattern, i, inside);
/*     */           
/* 178 */           if ((c == 'O') || (c == 'E')) {
/* 179 */             modifiedCommand = true;
/*     */           } else {
/* 181 */             mark = false;
/*     */           }
/*     */         }
/*     */       } else {
/* 185 */         if ((!inside) && (c != ' '))
/*     */         {
/* 187 */           buf.append("'");
/* 188 */           inside = true;
/*     */         }
/*     */         
/* 191 */         buf.append(c);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 196 */     if (buf.length() > 0) {
/* 197 */       char lastChar = buf.charAt(buf.length() - 1);
/*     */       
/* 199 */       if ((lastChar != '\'') && (inside)) {
/* 200 */         buf.append('\'');
/*     */       }
/*     */     }
/* 203 */     return buf.toString();
/*     */   }
/*     */   
/*     */   protected String quote(String str, boolean insideQuotes) {
/* 207 */     String retVal = str;
/* 208 */     if (!insideQuotes) {
/* 209 */       retVal = '\'' + retVal + '\'';
/*     */     }
/* 211 */     return retVal;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final SimpleDateFormat simpleDateFormat;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean translateCommand(StringBuilder buf, String pattern, int index, boolean oldInside)
/*     */   {
/* 225 */     char firstChar = pattern.charAt(index);
/* 226 */     boolean newInside = oldInside;
/*     */     
/*     */ 
/*     */ 
/* 230 */     if ((firstChar == 'O') || (firstChar == 'E')) {
/* 231 */       if (index + 1 < pattern.length()) {
/* 232 */         newInside = translateCommand(buf, pattern, index + 1, oldInside);
/*     */       } else {
/* 234 */         buf.append(quote("%" + firstChar, oldInside));
/*     */       }
/*     */     } else {
/* 237 */       String command = translate.getProperty(String.valueOf(firstChar));
/*     */       
/*     */ 
/* 240 */       if (command == null) {
/* 241 */         buf.append(quote("%" + firstChar, oldInside));
/*     */       }
/*     */       else {
/* 244 */         if (oldInside) {
/* 245 */           buf.append('\'');
/*     */         }
/* 247 */         buf.append(command);
/* 248 */         newInside = false;
/*     */       }
/*     */     }
/* 251 */     return newInside;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\util\Strftime.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */