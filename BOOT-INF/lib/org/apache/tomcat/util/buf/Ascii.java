/*     */ package org.apache.tomcat.util.buf;
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
/*     */ public final class Ascii
/*     */ {
/*  29 */   private static final byte[] toLower = new byte['Ā'];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  34 */   private static final boolean[] isDigit = new boolean['Ā'];
/*     */   
/*     */ 
/*     */   private static final long OVERFLOW_LIMIT = 922337203685477580L;
/*     */   
/*     */ 
/*     */   static
/*     */   {
/*  42 */     for (int i = 0; i < 256; i++) {
/*  43 */       toLower[i] = ((byte)i);
/*     */     }
/*     */     
/*  46 */     for (int lc = 97; lc <= 122; lc++) {
/*  47 */       int uc = lc + 65 - 97;
/*     */       
/*  49 */       toLower[uc] = ((byte)lc);
/*     */     }
/*     */     
/*  52 */     for (int d = 48; d <= 57; d++) {
/*  53 */       isDigit[d] = true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int toLower(int c)
/*     */   {
/*  63 */     return toLower[(c & 0xFF)] & 0xFF;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isDigit(int c)
/*     */   {
/*  71 */     return isDigit[(c & 0xFF)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long parseLong(byte[] b, int off, int len)
/*     */     throws NumberFormatException
/*     */   {
/*     */     int c;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  87 */     if ((b == null) || (len <= 0) || (!isDigit(c = b[(off++)]))) {
/*  88 */       throw new NumberFormatException();
/*     */     }
/*     */     int c;
/*  91 */     long n = c - 48;
/*  92 */     for (;;) { len--; if (len <= 0) break label111;
/*  93 */       if ((!isDigit(c = b[(off++)])) || ((n >= 922337203685477580L) && ((n != 922337203685477580L) || (c - 48 >= 8))))
/*     */         break;
/*  95 */       n = n * 10L + c - 48L;
/*     */     }
/*  97 */     throw new NumberFormatException();
/*     */     
/*     */     label111:
/*     */     
/* 101 */     return n;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\buf\Ascii.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */