/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.Serializable;
/*     */ import javax.annotation.Nullable;
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
/*     */ @GwtCompatible
/*     */ public enum CaseFormat
/*     */ {
/*  35 */   LOWER_HYPHEN(CharMatcher.is('-'), "-"), 
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
/*  56 */   LOWER_UNDERSCORE(CharMatcher.is('_'), "_"), 
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
/*  77 */   LOWER_CAMEL(CharMatcher.inRange('A', 'Z'), ""), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  87 */   UPPER_CAMEL(CharMatcher.inRange('A', 'Z'), ""), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  97 */   UPPER_UNDERSCORE(CharMatcher.is('_'), "_");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final CharMatcher wordBoundary;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final String wordSeparator;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private CaseFormat(CharMatcher wordBoundary, String wordSeparator)
/*     */   {
/* 119 */     this.wordBoundary = wordBoundary;
/* 120 */     this.wordSeparator = wordSeparator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String to(CaseFormat format, String str)
/*     */   {
/* 129 */     Preconditions.checkNotNull(format);
/* 130 */     Preconditions.checkNotNull(str);
/* 131 */     return format == this ? str : convert(format, str);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   String convert(CaseFormat format, String s)
/*     */   {
/* 139 */     StringBuilder out = null;
/* 140 */     int i = 0;
/* 141 */     int j = -1;
/* 142 */     while ((j = this.wordBoundary.indexIn(s, ++j)) != -1) {
/* 143 */       if (i == 0)
/*     */       {
/* 145 */         out = new StringBuilder(s.length() + 4 * this.wordSeparator.length());
/* 146 */         out.append(format.normalizeFirstWord(s.substring(i, j)));
/*     */       } else {
/* 148 */         out.append(format.normalizeWord(s.substring(i, j)));
/*     */       }
/* 150 */       out.append(format.wordSeparator);
/* 151 */       i = j + this.wordSeparator.length();
/*     */     }
/* 153 */     return 
/*     */     
/* 155 */       format.normalizeWord(s.substring(i));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 164 */   public Converter<String, String> converterTo(CaseFormat targetFormat) { return new StringConverter(this, targetFormat); }
/*     */   
/*     */   abstract String normalizeWord(String paramString);
/*     */   
/*     */   private static final class StringConverter extends Converter<String, String> implements Serializable {
/*     */     private final CaseFormat sourceFormat;
/*     */     private final CaseFormat targetFormat;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     StringConverter(CaseFormat sourceFormat, CaseFormat targetFormat) {
/* 174 */       this.sourceFormat = ((CaseFormat)Preconditions.checkNotNull(sourceFormat));
/* 175 */       this.targetFormat = ((CaseFormat)Preconditions.checkNotNull(targetFormat));
/*     */     }
/*     */     
/*     */     protected String doForward(String s)
/*     */     {
/* 180 */       return this.sourceFormat.to(this.targetFormat, s);
/*     */     }
/*     */     
/*     */     protected String doBackward(String s)
/*     */     {
/* 185 */       return this.targetFormat.to(this.sourceFormat, s);
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object object)
/*     */     {
/* 190 */       if ((object instanceof StringConverter)) {
/* 191 */         StringConverter that = (StringConverter)object;
/* 192 */         return (this.sourceFormat.equals(that.sourceFormat)) && (this.targetFormat.equals(that.targetFormat));
/*     */       }
/* 194 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 199 */       return this.sourceFormat.hashCode() ^ this.targetFormat.hashCode();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 204 */       return this.sourceFormat + ".converterTo(" + this.targetFormat + ")";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String normalizeFirstWord(String word)
/*     */   {
/* 213 */     return this == LOWER_CAMEL ? Ascii.toLowerCase(word) : normalizeWord(word);
/*     */   }
/*     */   
/*     */   private static String firstCharOnlyToUpper(String word) {
/* 217 */     return 
/*     */     
/* 219 */       Ascii.toUpperCase(word.charAt(0)) + Ascii.toLowerCase(word.substring(1));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\base\CaseFormat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */