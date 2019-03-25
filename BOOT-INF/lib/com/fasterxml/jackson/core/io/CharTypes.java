/*     */ package com.fasterxml.jackson.core.io;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public final class CharTypes
/*     */ {
/*   7 */   private static final char[] HC = "0123456789ABCDEF".toCharArray();
/*     */   private static final byte[] HB;
/*     */   
/*  10 */   static { int len = HC.length;
/*  11 */     HB = new byte[len];
/*  12 */     for (int i = 0; i < len; i++) {
/*  13 */       HB[i] = ((byte)HC[i]);
/*     */     }
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
/*  28 */     int[] table = new int['Ā'];
/*     */     
/*  30 */     for (int i = 0; i < 32; i++) {
/*  31 */       table[i] = -1;
/*     */     }
/*     */     
/*  34 */     table[34] = 1;
/*  35 */     table[92] = 1;
/*  36 */     sInputCodes = table;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  45 */     int[] table = new int[sInputCodes.length];
/*  46 */     System.arraycopy(sInputCodes, 0, table, 0, table.length);
/*  47 */     for (int c = 128; c < 256; c++)
/*     */     {
/*     */       int code;
/*     */       int code;
/*  51 */       if ((c & 0xE0) == 192) {
/*  52 */         code = 2; } else { int code;
/*  53 */         if ((c & 0xF0) == 224) {
/*  54 */           code = 3; } else { int code;
/*  55 */           if ((c & 0xF8) == 240)
/*     */           {
/*  57 */             code = 4;
/*     */           }
/*     */           else
/*  60 */             code = -1;
/*     */         } }
/*  62 */       table[c] = code;
/*     */     }
/*  64 */     sInputCodesUTF8 = table;
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
/*  75 */     int[] table = new int['Ā'];
/*     */     
/*  77 */     Arrays.fill(table, -1);
/*     */     
/*  79 */     for (int i = 33; i < 256; i++) {
/*  80 */       if (Character.isJavaIdentifierPart((char)i)) {
/*  81 */         table[i] = 0;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  87 */     table[64] = 0;
/*  88 */     table[35] = 0;
/*  89 */     table[42] = 0;
/*  90 */     table[45] = 0;
/*  91 */     table[43] = 0;
/*  92 */     sInputCodesJsNames = table;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 102 */     int[] table = new int['Ā'];
/*     */     
/* 104 */     System.arraycopy(sInputCodesJsNames, 0, table, 0, table.length);
/* 105 */     Arrays.fill(table, 128, 128, 0);
/* 106 */     sInputCodesUtf8JsNames = table;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 115 */     int[] buf = new int['Ā'];
/*     */     
/* 117 */     System.arraycopy(sInputCodesUTF8, 128, buf, 128, 128);
/*     */     
/*     */ 
/* 120 */     Arrays.fill(buf, 0, 32, -1);
/* 121 */     buf[9] = 0;
/* 122 */     buf[10] = 10;
/* 123 */     buf[13] = 13;
/* 124 */     buf[42] = 42;
/* 125 */     sInputCodesComment = buf;
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
/* 136 */     int[] buf = new int['Ā'];
/* 137 */     System.arraycopy(sInputCodesUTF8, 128, buf, 128, 128);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 142 */     Arrays.fill(buf, 0, 32, -1);
/* 143 */     buf[32] = 1;
/* 144 */     buf[9] = 1;
/* 145 */     buf[10] = 10;
/* 146 */     buf[13] = 13;
/* 147 */     buf[47] = 47;
/* 148 */     buf[35] = 35;
/* 149 */     sInputCodesWS = buf;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 158 */     int[] table = new int[''];
/*     */     
/* 160 */     for (int i = 0; i < 32; i++)
/*     */     {
/* 162 */       table[i] = -1;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 167 */     table[34] = 34;
/* 168 */     table[92] = 92;
/*     */     
/* 170 */     table[8] = 98;
/* 171 */     table[9] = 116;
/* 172 */     table[12] = 102;
/* 173 */     table[10] = 110;
/* 174 */     table[13] = 114;
/* 175 */     sOutputEscapes128 = table;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 183 */     sHexValues = new int[''];
/*     */     
/* 185 */     Arrays.fill(sHexValues, -1);
/* 186 */     for (int i = 0; i < 10; i++) {
/* 187 */       sHexValues[(48 + i)] = i;
/*     */     }
/* 189 */     for (int i = 0; i < 6; i++) {
/* 190 */       sHexValues[(97 + i)] = (10 + i);
/* 191 */       sHexValues[(65 + i)] = (10 + i); } }
/*     */   
/*     */   private static final int[] sInputCodes;
/*     */   
/* 195 */   public static int[] getInputCodeLatin1() { return sInputCodes; }
/* 196 */   public static int[] getInputCodeUtf8() { return sInputCodesUTF8; }
/*     */   
/* 198 */   public static int[] getInputCodeLatin1JsNames() { return sInputCodesJsNames; }
/* 199 */   public static int[] getInputCodeUtf8JsNames() { return sInputCodesUtf8JsNames; }
/*     */   
/* 201 */   public static int[] getInputCodeComment() { return sInputCodesComment; }
/* 202 */   public static int[] getInputCodeWS() { return sInputCodesWS; }
/*     */   
/*     */   private static final int[] sInputCodesUTF8;
/*     */   private static final int[] sInputCodesJsNames;
/*     */   private static final int[] sInputCodesUtf8JsNames;
/*     */   private static final int[] sInputCodesComment;
/*     */   private static final int[] sInputCodesWS;
/*     */   private static final int[] sOutputEscapes128;
/*     */   private static final int[] sHexValues;
/* 211 */   public static int[] get7BitOutputEscapes() { return sOutputEscapes128; }
/*     */   
/*     */ 
/*     */   public static int charToHex(int ch) {
/* 215 */     return ch > 127 ? -1 : sHexValues[ch];
/*     */   }
/*     */   
/*     */   public static void appendQuoted(StringBuilder sb, String content)
/*     */   {
/* 220 */     int[] escCodes = sOutputEscapes128;
/* 221 */     int escLen = escCodes.length;
/* 222 */     int i = 0; for (int len = content.length(); i < len; i++) {
/* 223 */       char c = content.charAt(i);
/* 224 */       if ((c >= escLen) || (escCodes[c] == 0)) {
/* 225 */         sb.append(c);
/*     */       }
/*     */       else {
/* 228 */         sb.append('\\');
/* 229 */         int escCode = escCodes[c];
/* 230 */         if (escCode < 0)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 239 */           sb.append('u');
/* 240 */           sb.append('0');
/* 241 */           sb.append('0');
/* 242 */           int value = c;
/* 243 */           sb.append(HC[(value >> 4)]);
/* 244 */           sb.append(HC[(value & 0xF)]);
/*     */         } else {
/* 246 */           sb.append((char)escCode);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/* 252 */   public static char[] copyHexChars() { return (char[])HC.clone(); }
/*     */   
/*     */   public static byte[] copyHexBytes()
/*     */   {
/* 256 */     return (byte[])HB.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\io\CharTypes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */