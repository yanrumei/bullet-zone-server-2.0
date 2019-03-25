/*     */ package org.apache.tomcat.util.buf;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.CharConversionException;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
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
/*     */ 
/*     */ public final class UDecoder
/*     */ {
/*  41 */   private static final StringManager sm = StringManager.getManager(UDecoder.class);
/*     */   
/*  43 */   private static final Log log = LogFactory.getLog(UDecoder.class);
/*     */   
/*     */ 
/*  46 */   public static final boolean ALLOW_ENCODED_SLASH = Boolean.parseBoolean(System.getProperty("org.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH", "false"));
/*     */   
/*     */   private static class DecodeException extends CharConversionException {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*  51 */     public DecodeException(String s) { super(); }
/*     */     
/*     */ 
/*     */ 
/*     */     public synchronized Throwable fillInStackTrace()
/*     */     {
/*  57 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*  62 */   private static final IOException EXCEPTION_EOF = new DecodeException("EOF");
/*     */   
/*     */ 
/*  65 */   private static final IOException EXCEPTION_NOT_HEX_DIGIT = new DecodeException("isHexDigit");
/*     */   
/*     */ 
/*     */ 
/*  69 */   private static final IOException EXCEPTION_SLASH = new DecodeException("noSlash");
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
/*     */   public void convert(ByteChunk mb, boolean query)
/*     */     throws IOException
/*     */   {
/*  85 */     int start = mb.getOffset();
/*  86 */     byte[] buff = mb.getBytes();
/*  87 */     int end = mb.getEnd();
/*     */     
/*  89 */     int idx = ByteChunk.findByte(buff, start, end, (byte)37);
/*  90 */     int idx2 = -1;
/*  91 */     if (query) {
/*  92 */       idx2 = ByteChunk.findByte(buff, start, idx >= 0 ? idx : end, (byte)43);
/*     */     }
/*  94 */     if ((idx < 0) && (idx2 < 0)) {
/*  95 */       return;
/*     */     }
/*     */     
/*     */ 
/*  99 */     if (((idx2 >= 0) && (idx2 < idx)) || (idx < 0)) {
/* 100 */       idx = idx2;
/*     */     }
/*     */     
/* 103 */     boolean noSlash = (!ALLOW_ENCODED_SLASH) && (!query);
/*     */     
/* 105 */     for (int j = idx; j < end; idx++) {
/* 106 */       if ((buff[j] == 43) && (query)) {
/* 107 */         buff[idx] = 32;
/* 108 */       } else if (buff[j] != 37) {
/* 109 */         buff[idx] = buff[j];
/*     */       }
/*     */       else {
/* 112 */         if (j + 2 >= end) {
/* 113 */           throw EXCEPTION_EOF;
/*     */         }
/* 115 */         byte b1 = buff[(j + 1)];
/* 116 */         byte b2 = buff[(j + 2)];
/* 117 */         if ((!isHexDigit(b1)) || (!isHexDigit(b2))) {
/* 118 */           throw EXCEPTION_NOT_HEX_DIGIT;
/*     */         }
/*     */         
/* 121 */         j += 2;
/* 122 */         int res = x2c(b1, b2);
/* 123 */         if ((noSlash) && (res == 47)) {
/* 124 */           throw EXCEPTION_SLASH;
/*     */         }
/* 126 */         buff[idx] = ((byte)res);
/*     */       }
/* 105 */       j++;
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
/* 130 */     mb.setEnd(idx);
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
/*     */   public void convert(CharChunk mb, boolean query)
/*     */     throws IOException
/*     */   {
/* 148 */     int start = mb.getOffset();
/* 149 */     char[] buff = mb.getBuffer();
/* 150 */     int cend = mb.getEnd();
/*     */     
/* 152 */     int idx = CharChunk.indexOf(buff, start, cend, '%');
/* 153 */     int idx2 = -1;
/* 154 */     if (query) {
/* 155 */       idx2 = CharChunk.indexOf(buff, start, idx >= 0 ? idx : cend, '+');
/*     */     }
/* 157 */     if ((idx < 0) && (idx2 < 0)) {
/* 158 */       return;
/*     */     }
/*     */     
/*     */ 
/* 162 */     if (((idx2 >= 0) && (idx2 < idx)) || (idx < 0)) {
/* 163 */       idx = idx2;
/*     */     }
/*     */     
/* 166 */     boolean noSlash = (!ALLOW_ENCODED_SLASH) && (!query);
/*     */     
/* 168 */     for (int j = idx; j < cend; idx++) {
/* 169 */       if ((buff[j] == '+') && (query)) {
/* 170 */         buff[idx] = ' ';
/* 171 */       } else if (buff[j] != '%') {
/* 172 */         buff[idx] = buff[j];
/*     */       }
/*     */       else {
/* 175 */         if (j + 2 >= cend)
/*     */         {
/* 177 */           throw EXCEPTION_EOF;
/*     */         }
/* 179 */         char b1 = buff[(j + 1)];
/* 180 */         char b2 = buff[(j + 2)];
/* 181 */         if ((!isHexDigit(b1)) || (!isHexDigit(b2))) {
/* 182 */           throw EXCEPTION_NOT_HEX_DIGIT;
/*     */         }
/*     */         
/* 185 */         j += 2;
/* 186 */         int res = x2c(b1, b2);
/* 187 */         if ((noSlash) && (res == 47)) {
/* 188 */           throw EXCEPTION_SLASH;
/*     */         }
/* 190 */         buff[idx] = ((char)res);
/*     */       }
/* 168 */       j++;
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
/* 193 */     mb.setEnd(idx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void convert(MessageBytes mb, boolean query)
/*     */     throws IOException
/*     */   {
/* 206 */     switch (mb.getType()) {
/*     */     case 1: 
/* 208 */       String strValue = mb.toString();
/* 209 */       if (strValue == null) {
/* 210 */         return;
/*     */       }
/*     */       try {
/* 213 */         mb.setString(convert(strValue, query));
/*     */       } catch (RuntimeException ex) {
/* 215 */         throw new DecodeException(ex.getMessage());
/*     */       }
/*     */     
/*     */     case 3: 
/* 219 */       CharChunk charC = mb.getCharChunk();
/* 220 */       convert(charC, query);
/* 221 */       break;
/*     */     case 2: 
/* 223 */       ByteChunk bytesC = mb.getByteChunk();
/* 224 */       convert(bytesC, query);
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String convert(String str, boolean query)
/*     */   {
/* 237 */     if (str == null) {
/* 238 */       return null;
/*     */     }
/*     */     
/* 241 */     if (((!query) || (str.indexOf('+') < 0)) && (str.indexOf('%') < 0)) {
/* 242 */       return str;
/*     */     }
/*     */     
/* 245 */     boolean noSlash = (!ALLOW_ENCODED_SLASH) && (!query);
/*     */     
/* 247 */     StringBuilder dec = new StringBuilder();
/* 248 */     int strPos = 0;
/* 249 */     int strLen = str.length();
/*     */     
/* 251 */     dec.ensureCapacity(str.length());
/* 252 */     while (strPos < strLen)
/*     */     {
/*     */ 
/*     */ 
/* 256 */       for (int laPos = strPos; laPos < strLen; laPos++) {
/* 257 */         char laChar = str.charAt(laPos);
/* 258 */         if (((laChar == '+') && (query)) || (laChar == '%')) {
/*     */           break;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 264 */       if (laPos > strPos) {
/* 265 */         dec.append(str.substring(strPos, laPos));
/* 266 */         strPos = laPos;
/*     */       }
/*     */       
/*     */ 
/* 270 */       if (strPos >= strLen) {
/*     */         break;
/*     */       }
/*     */       
/*     */ 
/* 275 */       char metaChar = str.charAt(strPos);
/* 276 */       if (metaChar == '+') {
/* 277 */         dec.append(' ');
/* 278 */         strPos++;
/*     */       }
/* 280 */       else if (metaChar == '%')
/*     */       {
/*     */ 
/*     */ 
/* 284 */         char res = (char)Integer.parseInt(str
/* 285 */           .substring(strPos + 1, strPos + 3), 16);
/* 286 */         if ((noSlash) && (res == '/')) {
/* 287 */           throw new IllegalArgumentException("noSlash");
/*     */         }
/* 289 */         dec.append(res);
/* 290 */         strPos += 3;
/*     */       }
/*     */     }
/*     */     
/* 294 */     return dec.toString();
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
/*     */   public static String URLDecode(String str)
/*     */   {
/* 310 */     return URLDecode(str, StandardCharsets.ISO_8859_1);
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
/*     */   @Deprecated
/*     */   public static String URLDecode(String str, String enc)
/*     */   {
/* 329 */     return URLDecode(str, enc, false);
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
/*     */   public static String URLDecode(String str, Charset charset)
/*     */   {
/* 345 */     return URLDecode(str, charset, false);
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
/*     */   @Deprecated
/*     */   public static String URLDecode(String str, String enc, boolean isQuery)
/*     */   {
/* 364 */     Charset charset = null;
/*     */     
/* 366 */     if (enc != null) {
/*     */       try {
/* 368 */         charset = B2CConverter.getCharset(enc);
/*     */       } catch (UnsupportedEncodingException uee) {
/* 370 */         if (log.isDebugEnabled()) {
/* 371 */           log.debug(sm.getString("uDecoder.urlDecode.uee", new Object[] { enc }), uee);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 376 */     return URLDecode(str, charset, isQuery);
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
/*     */   @Deprecated
/*     */   public static String URLDecode(byte[] bytes, String enc, boolean isQuery)
/*     */   {
/* 395 */     throw new IllegalArgumentException(sm.getString("udecoder.urlDecode.iae"));
/*     */   }
/*     */   
/*     */ 
/*     */   private static String URLDecode(String str, Charset charset, boolean isQuery)
/*     */   {
/* 401 */     if (str == null) {
/* 402 */       return null;
/*     */     }
/*     */     
/* 405 */     if (str.indexOf('%') == -1)
/*     */     {
/* 407 */       return str;
/*     */     }
/*     */     
/* 410 */     if (charset == null) {
/* 411 */       charset = StandardCharsets.ISO_8859_1;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 430 */     ByteArrayOutputStream baos = new ByteArrayOutputStream(str.length() * 2);
/*     */     
/* 432 */     OutputStreamWriter osw = new OutputStreamWriter(baos, charset);
/*     */     
/* 434 */     char[] sourceChars = str.toCharArray();
/* 435 */     int len = sourceChars.length;
/* 436 */     int ix = 0;
/*     */     try
/*     */     {
/* 439 */       while (ix < len) {
/* 440 */         char c = sourceChars[(ix++)];
/* 441 */         if (c == '%') {
/* 442 */           osw.flush();
/* 443 */           if (ix + 2 > len)
/*     */           {
/* 445 */             throw new IllegalArgumentException(sm.getString("uDecoder.urlDecode.missingDigit", new Object[] { str }));
/*     */           }
/* 447 */           char c1 = sourceChars[(ix++)];
/* 448 */           char c2 = sourceChars[(ix++)];
/* 449 */           if ((isHexDigit(c1)) && (isHexDigit(c2))) {
/* 450 */             baos.write(x2c(c1, c2));
/*     */           }
/*     */           else {
/* 453 */             throw new IllegalArgumentException(sm.getString("uDecoder.urlDecode.missingDigit", new Object[] { str }));
/*     */           }
/* 455 */         } else if ((c == '+') && (isQuery)) {
/* 456 */           osw.append(' ');
/*     */         } else {
/* 458 */           osw.append(c);
/*     */         }
/*     */       }
/* 461 */       osw.flush();
/*     */       
/* 463 */       return baos.toString(charset.name());
/*     */     }
/*     */     catch (IOException ioe) {
/* 466 */       throw new IllegalArgumentException(sm.getString("uDecoder.urlDecode.conversionError", new Object[] { str, charset.name() }), ioe);
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean isHexDigit(int c)
/*     */   {
/* 472 */     return ((c >= 48) && (c <= 57)) || ((c >= 97) && (c <= 102)) || ((c >= 65) && (c <= 70));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static int x2c(byte b1, byte b2)
/*     */   {
/* 479 */     int digit = b1 >= 65 ? (b1 & 0xDF) - 65 + 10 : b1 - 48;
/*     */     
/* 481 */     digit *= 16;
/* 482 */     digit += (b2 >= 65 ? (b2 & 0xDF) - 65 + 10 : b2 - 48);
/*     */     
/* 484 */     return digit;
/*     */   }
/*     */   
/*     */   private static int x2c(char b1, char b2)
/*     */   {
/* 489 */     int digit = b1 >= 'A' ? (b1 & 0xDF) - 'A' + 10 : b1 - '0';
/*     */     
/* 491 */     digit *= 16;
/* 492 */     digit += (b2 >= 'A' ? (b2 & 0xDF) - 'A' + 10 : b2 - '0');
/*     */     
/* 494 */     return digit;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\buf\UDecoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */