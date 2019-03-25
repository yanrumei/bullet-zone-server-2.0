/*    */ package org.apache.tomcat.util.http.parser;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.StringReader;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Locale;
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
/*    */ public class AcceptLanguage
/*    */ {
/*    */   private final Locale locale;
/*    */   private final double quality;
/*    */   
/*    */   protected AcceptLanguage(Locale locale, double quality)
/*    */   {
/* 31 */     this.locale = locale;
/* 32 */     this.quality = quality;
/*    */   }
/*    */   
/*    */   public Locale getLocale() {
/* 36 */     return this.locale;
/*    */   }
/*    */   
/*    */   public double getQuality() {
/* 40 */     return this.quality;
/*    */   }
/*    */   
/*    */   public static List<AcceptLanguage> parse(StringReader input)
/*    */     throws IOException
/*    */   {
/* 46 */     List<AcceptLanguage> result = new ArrayList();
/*    */     
/*    */ 
/*    */ 
/*    */     for (;;)
/*    */     {
/* 52 */       String languageTag = HttpParser.readToken(input);
/* 53 */       if (languageTag == null)
/*    */       {
/* 55 */         HttpParser.skipUntil(input, 0, ',');
/*    */       }
/*    */       else
/*    */       {
/* 59 */         if (languageTag.length() == 0) {
/*    */           break;
/*    */         }
/*    */         
/*    */ 
/*    */ 
/* 65 */         double quality = 1.0D;
/* 66 */         SkipResult lookForSemiColon = HttpParser.skipConstant(input, ";");
/* 67 */         if (lookForSemiColon == SkipResult.FOUND) {
/* 68 */           quality = HttpParser.readWeight(input, ',');
/*    */         }
/*    */         
/* 71 */         if (quality > 0.0D) {
/* 72 */           result.add(new AcceptLanguage(Locale.forLanguageTag(languageTag), quality));
/*    */         }
/*    */       }
/*    */     }
/* 76 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\parser\AcceptLanguage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */