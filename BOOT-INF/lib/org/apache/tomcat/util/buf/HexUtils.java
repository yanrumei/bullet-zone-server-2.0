/*     */ package org.apache.tomcat.util.buf;
/*     */ 
/*     */ import org.apache.tomcat.util.res.StringManager;
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
/*     */ public final class HexUtils
/*     */ {
/*  31 */   private static final StringManager sm = StringManager.getManager("org.apache.tomcat.util.buf");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  38 */   private static final int[] DEC = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15 };
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
/*  49 */   private static final byte[] HEX = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  58 */   private static final char[] hex = "0123456789abcdef".toCharArray();
/*     */   
/*     */ 
/*     */ 
/*     */   public static int getDec(int index)
/*     */   {
/*     */     try
/*     */     {
/*  66 */       return DEC[(index - 48)];
/*     */     } catch (ArrayIndexOutOfBoundsException ex) {}
/*  68 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   public static byte getHex(int index)
/*     */   {
/*  74 */     return HEX[index];
/*     */   }
/*     */   
/*     */   public static String toHexString(byte[] bytes)
/*     */   {
/*  79 */     if (null == bytes) {
/*  80 */       return null;
/*     */     }
/*     */     
/*  83 */     StringBuilder sb = new StringBuilder(bytes.length << 1);
/*     */     
/*  85 */     for (int i = 0; i < bytes.length; i++)
/*     */     {
/*  87 */       sb.append(hex[((bytes[i] & 0xF0) >> 4)]).append(hex[(bytes[i] & 0xF)]);
/*     */     }
/*     */     
/*     */ 
/*  91 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static byte[] fromHexString(String input)
/*     */   {
/*  96 */     if (input == null) {
/*  97 */       return null;
/*     */     }
/*     */     
/* 100 */     if ((input.length() & 0x1) == 1)
/*     */     {
/* 102 */       throw new IllegalArgumentException(sm.getString("hexUtils.fromHex.oddDigits"));
/*     */     }
/*     */     
/* 105 */     char[] inputChars = input.toCharArray();
/* 106 */     byte[] result = new byte[input.length() >> 1];
/* 107 */     for (int i = 0; i < result.length; i++) {
/* 108 */       int upperNibble = getDec(inputChars[(2 * i)]);
/* 109 */       int lowerNibble = getDec(inputChars[(2 * i + 1)]);
/* 110 */       if ((upperNibble < 0) || (lowerNibble < 0))
/*     */       {
/* 112 */         throw new IllegalArgumentException(sm.getString("hexUtils.fromHex.nonHex"));
/*     */       }
/* 114 */       result[i] = ((byte)((upperNibble << 4) + lowerNibble));
/*     */     }
/* 116 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\buf\HexUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */