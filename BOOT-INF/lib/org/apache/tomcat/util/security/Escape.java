/*     */ package org.apache.tomcat.util.security;
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
/*     */ public class Escape
/*     */ {
/*     */   public static String htmlElementContent(String content)
/*     */   {
/*  42 */     if (content == null) {
/*  43 */       return null;
/*     */     }
/*     */     
/*  46 */     StringBuilder sb = new StringBuilder();
/*     */     
/*  48 */     for (int i = 0; i < content.length(); i++) {
/*  49 */       char c = content.charAt(i);
/*  50 */       if (c == '<') {
/*  51 */         sb.append("&lt;");
/*  52 */       } else if (c == '>') {
/*  53 */         sb.append("&gt;");
/*  54 */       } else if (c == '\'') {
/*  55 */         sb.append("&#39;");
/*  56 */       } else if (c == '&') {
/*  57 */         sb.append("&amp;");
/*  58 */       } else if (c == '"') {
/*  59 */         sb.append("&quot;");
/*  60 */       } else if (c == '/') {
/*  61 */         sb.append("&#47;");
/*     */       } else {
/*  63 */         sb.append(c);
/*     */       }
/*     */     }
/*     */     
/*  67 */     return sb.length() > content.length() ? sb.toString() : content;
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
/*     */   public static String htmlElementContext(Object obj)
/*     */   {
/*  81 */     if (obj == null) {
/*  82 */       return "?";
/*     */     }
/*     */     try
/*     */     {
/*  86 */       return htmlElementContent(obj.toString());
/*     */     } catch (Exception e) {}
/*  88 */     return null;
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
/*     */   public static String xml(String content)
/*     */   {
/* 102 */     return xml(null, content);
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
/*     */   public static String xml(String ifNull, String content)
/*     */   {
/* 116 */     return xml(ifNull, false, content);
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
/*     */   public static String xml(String ifNull, boolean escapeCRLF, String content)
/*     */   {
/* 131 */     if (content == null) {
/* 132 */       return ifNull;
/*     */     }
/*     */     
/* 135 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 137 */     for (int i = 0; i < content.length(); i++) {
/* 138 */       char c = content.charAt(i);
/* 139 */       if (c == '<') {
/* 140 */         sb.append("&lt;");
/* 141 */       } else if (c == '>') {
/* 142 */         sb.append("&gt;");
/* 143 */       } else if (c == '\'') {
/* 144 */         sb.append("&apos;");
/* 145 */       } else if (c == '&') {
/* 146 */         sb.append("&amp;");
/* 147 */       } else if (c == '"') {
/* 148 */         sb.append("&quot;");
/* 149 */       } else if ((escapeCRLF) && (c == '\r')) {
/* 150 */         sb.append("&#13;");
/* 151 */       } else if ((escapeCRLF) && (c == '\n')) {
/* 152 */         sb.append("&#10;");
/*     */       } else {
/* 154 */         sb.append(c);
/*     */       }
/*     */     }
/*     */     
/* 158 */     return sb.length() > content.length() ? sb.toString() : content;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\security\Escape.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */