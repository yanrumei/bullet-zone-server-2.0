/*     */ package ch.qos.logback.core.helpers;
/*     */ 
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class Transform
/*     */ {
/*     */   private static final String CDATA_START = "<![CDATA[";
/*     */   private static final String CDATA_END = "]]>";
/*     */   private static final String CDATA_PSEUDO_END = "]]&gt;";
/*     */   private static final String CDATA_EMBEDED_END = "]]>]]&gt;<![CDATA[";
/*  29 */   private static final int CDATA_END_LEN = "]]>".length();
/*  30 */   private static final Pattern UNSAFE_XML_CHARS = Pattern.compile("[\000-\b\013\f\016-\037<>&'\"]");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String escapeTags(String input)
/*     */   {
/*  41 */     if ((input == null) || (input.length() == 0) || (!UNSAFE_XML_CHARS.matcher(input).find())) {
/*  42 */       return input;
/*     */     }
/*  44 */     StringBuffer buf = new StringBuffer(input);
/*  45 */     return escapeTags(buf);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String escapeTags(StringBuffer buf)
/*     */   {
/*  56 */     for (int i = 0; i < buf.length(); i++) {
/*  57 */       char ch = buf.charAt(i);
/*  58 */       switch (ch)
/*     */       {
/*     */       case '\t': 
/*     */       case '\n': 
/*     */       case '\r': 
/*     */         break;
/*     */       case '&': 
/*  65 */         buf.replace(i, i + 1, "&amp;");
/*  66 */         break;
/*     */       case '<': 
/*  68 */         buf.replace(i, i + 1, "&lt;");
/*  69 */         break;
/*     */       case '>': 
/*  71 */         buf.replace(i, i + 1, "&gt;");
/*  72 */         break;
/*     */       case '"': 
/*  74 */         buf.replace(i, i + 1, "&quot;");
/*  75 */         break;
/*     */       case '\'': 
/*  77 */         buf.replace(i, i + 1, "&#39;");
/*  78 */         break;
/*     */       default: 
/*  80 */         if (ch < ' ')
/*     */         {
/*     */ 
/*  83 */           buf.replace(i, i + 1, "�");
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/*  88 */     return buf.toString();
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
/*     */   public static void appendEscapingCDATA(StringBuilder output, String str)
/*     */   {
/* 103 */     if (str == null) {
/* 104 */       return;
/*     */     }
/*     */     
/* 107 */     int end = str.indexOf("]]>");
/*     */     
/* 109 */     if (end < 0) {
/* 110 */       output.append(str);
/*     */       
/* 112 */       return;
/*     */     }
/*     */     
/* 115 */     int start = 0;
/*     */     
/* 117 */     while (end > -1) {
/* 118 */       output.append(str.substring(start, end));
/* 119 */       output.append("]]>]]&gt;<![CDATA[");
/* 120 */       start = end + CDATA_END_LEN;
/*     */       
/* 122 */       if (start < str.length()) {
/* 123 */         end = str.indexOf("]]>", start);
/*     */       } else {
/* 125 */         return;
/*     */       }
/*     */     }
/*     */     
/* 129 */     output.append(str.substring(start));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\helpers\Transform.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */