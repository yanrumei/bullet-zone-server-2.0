/*    */ package com.google.common.html;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.escape.Escaper;
/*    */ import com.google.common.escape.Escapers;
/*    */ import com.google.common.escape.Escapers.Builder;
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
/*    */ @Beta
/*    */ @GwtCompatible
/*    */ public final class HtmlEscapers
/*    */ {
/*    */   public static Escaper htmlEscaper()
/*    */   {
/* 55 */     return HTML_ESCAPER;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 62 */   private static final Escaper HTML_ESCAPER = Escapers.builder()
/* 63 */     .addEscape('"', "&quot;")
/*    */     
/* 65 */     .addEscape('\'', "&#39;")
/* 66 */     .addEscape('&', "&amp;")
/* 67 */     .addEscape('<', "&lt;")
/* 68 */     .addEscape('>', "&gt;")
/* 69 */     .build();
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\html\HtmlEscapers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */