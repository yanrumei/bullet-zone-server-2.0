/*     */ package com.google.common.xml;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.escape.Escaper;
/*     */ import com.google.common.escape.Escapers;
/*     */ import com.google.common.escape.Escapers.Builder;
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
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public class XmlEscapers
/*     */ {
/*     */   private static final char MIN_ASCII_CONTROL_CHAR = '\000';
/*     */   private static final char MAX_ASCII_CONTROL_CHAR = '\037';
/*     */   private static final Escaper XML_ESCAPER;
/*     */   private static final Escaper XML_CONTENT_ESCAPER;
/*     */   private static final Escaper XML_ATTRIBUTE_ESCAPER;
/*     */   
/*     */   public static Escaper xmlContentEscaper()
/*     */   {
/*  76 */     return XML_CONTENT_ESCAPER;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Escaper xmlAttributeEscaper()
/*     */   {
/* 100 */     return XML_ATTRIBUTE_ESCAPER;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/* 108 */     Escapers.Builder builder = Escapers.builder();
/*     */     
/*     */ 
/*     */ 
/* 112 */     builder.setSafeRange('\000', 65533);
/*     */     
/* 114 */     builder.setUnsafeReplacement("�");
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
/* 125 */     for (char c = '\000'; c <= '\037'; c = (char)(c + '\001')) {
/* 126 */       if ((c != '\t') && (c != '\n') && (c != '\r')) {
/* 127 */         builder.addEscape(c, "�");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 133 */     builder.addEscape('&', "&amp;");
/* 134 */     builder.addEscape('<', "&lt;");
/* 135 */     builder.addEscape('>', "&gt;");
/* 136 */     XML_CONTENT_ESCAPER = builder.build();
/* 137 */     builder.addEscape('\'', "&apos;");
/* 138 */     builder.addEscape('"', "&quot;");
/* 139 */     XML_ESCAPER = builder.build();
/* 140 */     builder.addEscape('\t', "&#x9;");
/* 141 */     builder.addEscape('\n', "&#xA;");
/* 142 */     builder.addEscape('\r', "&#xD;");
/* 143 */     XML_ATTRIBUTE_ESCAPER = builder.build();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\xml\XmlEscapers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */