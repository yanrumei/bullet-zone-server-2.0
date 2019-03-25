/*     */ package org.apache.tomcat.util.http.fileupload.util.mime;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.tomcat.util.codec.binary.Base64;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MimeUtility
/*     */ {
/*     */   private static final String US_ASCII_CHARSET = "US-ASCII";
/*     */   private static final String BASE64_ENCODING_MARKER = "B";
/*     */   private static final String QUOTEDPRINTABLE_ENCODING_MARKER = "Q";
/*     */   private static final String ENCODED_TOKEN_MARKER = "=?";
/*     */   private static final String ENCODED_TOKEN_FINISHER = "?=";
/*     */   private static final String LINEAR_WHITESPACE = " \t\r\n";
/*  68 */   private static final Map<String, String> MIME2JAVA = new HashMap();
/*     */   
/*     */   static {
/*  71 */     MIME2JAVA.put("iso-2022-cn", "ISO2022CN");
/*  72 */     MIME2JAVA.put("iso-2022-kr", "ISO2022KR");
/*  73 */     MIME2JAVA.put("utf-8", "UTF8");
/*  74 */     MIME2JAVA.put("utf8", "UTF8");
/*  75 */     MIME2JAVA.put("ja_jp.iso2022-7", "ISO2022JP");
/*  76 */     MIME2JAVA.put("ja_jp.eucjp", "EUCJIS");
/*  77 */     MIME2JAVA.put("euc-kr", "KSC5601");
/*  78 */     MIME2JAVA.put("euckr", "KSC5601");
/*  79 */     MIME2JAVA.put("us-ascii", "ISO-8859-1");
/*  80 */     MIME2JAVA.put("x-us-ascii", "ISO-8859-1");
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
/*     */   public static String decodeText(String text)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 104 */     if (text.indexOf("=?") < 0) {
/* 105 */       return text;
/*     */     }
/*     */     
/* 108 */     int offset = 0;
/* 109 */     int endOffset = text.length();
/*     */     
/* 111 */     int startWhiteSpace = -1;
/* 112 */     int endWhiteSpace = -1;
/*     */     
/* 114 */     StringBuilder decodedText = new StringBuilder(text.length());
/*     */     
/* 116 */     boolean previousTokenEncoded = false;
/*     */     label229:
/* 118 */     while (offset < endOffset) {
/* 119 */       char ch = text.charAt(offset);
/*     */       
/*     */ 
/* 122 */       if (" \t\r\n".indexOf(ch) != -1) {
/* 123 */         startWhiteSpace = offset;
/* 124 */         for (;;) { if (offset >= endOffset)
/*     */             break label229;
/* 126 */           ch = text.charAt(offset);
/* 127 */           if (" \t\r\n".indexOf(ch) == -1) break;
/* 128 */           offset++;
/*     */         }
/*     */         
/*     */ 
/* 132 */         endWhiteSpace = offset;
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/* 138 */         int wordStart = offset;
/*     */         
/* 140 */         while (offset < endOffset)
/*     */         {
/* 142 */           ch = text.charAt(offset);
/* 143 */           if (" \t\r\n".indexOf(ch) != -1) break;
/* 144 */           offset++;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 152 */         String word = text.substring(wordStart, offset);
/*     */         
/* 154 */         if (word.startsWith("=?"))
/*     */         {
/*     */           try {
/* 157 */             String decodedWord = decodeWord(word);
/*     */             
/*     */ 
/* 160 */             if ((!previousTokenEncoded) && (startWhiteSpace != -1)) {
/* 161 */               decodedText.append(text.substring(startWhiteSpace, endWhiteSpace));
/* 162 */               startWhiteSpace = -1;
/*     */             }
/*     */             
/* 165 */             previousTokenEncoded = true;
/*     */             
/* 167 */             decodedText.append(decodedWord);
/*     */ 
/*     */ 
/*     */           }
/*     */           catch (ParseException localParseException) {}
/*     */ 
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/* 178 */           if (startWhiteSpace != -1) {
/* 179 */             decodedText.append(text.substring(startWhiteSpace, endWhiteSpace));
/* 180 */             startWhiteSpace = -1;
/*     */           }
/*     */           
/* 183 */           previousTokenEncoded = false;
/* 184 */           decodedText.append(word);
/*     */         }
/*     */       }
/*     */     }
/* 188 */     return decodedText.toString();
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
/*     */   private static String decodeWord(String word)
/*     */     throws ParseException, UnsupportedEncodingException
/*     */   {
/* 207 */     if (!word.startsWith("=?")) {
/* 208 */       throw new ParseException("Invalid RFC 2047 encoded-word: " + word);
/*     */     }
/*     */     
/* 211 */     int charsetPos = word.indexOf('?', 2);
/* 212 */     if (charsetPos == -1) {
/* 213 */       throw new ParseException("Missing charset in RFC 2047 encoded-word: " + word);
/*     */     }
/*     */     
/*     */ 
/* 217 */     String charset = word.substring(2, charsetPos).toLowerCase(Locale.ENGLISH);
/*     */     
/*     */ 
/* 220 */     int encodingPos = word.indexOf('?', charsetPos + 1);
/* 221 */     if (encodingPos == -1) {
/* 222 */       throw new ParseException("Missing encoding in RFC 2047 encoded-word: " + word);
/*     */     }
/*     */     
/* 225 */     String encoding = word.substring(charsetPos + 1, encodingPos);
/*     */     
/*     */ 
/* 228 */     int encodedTextPos = word.indexOf("?=", encodingPos + 1);
/* 229 */     if (encodedTextPos == -1) {
/* 230 */       throw new ParseException("Missing encoded text in RFC 2047 encoded-word: " + word);
/*     */     }
/*     */     
/* 233 */     String encodedText = word.substring(encodingPos + 1, encodedTextPos);
/*     */     
/*     */ 
/* 236 */     if (encodedText.length() == 0) {
/* 237 */       return "";
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 242 */       ByteArrayOutputStream out = new ByteArrayOutputStream(encodedText.length());
/*     */       
/*     */       byte[] decodedData;
/*     */       
/* 246 */       if (encoding.equals("B")) {
/* 247 */         decodedData = Base64.decodeBase64(encodedText); } else { byte[] decodedData;
/* 248 */         if (encoding.equals("Q")) {
/* 249 */           byte[] encodedData = encodedText.getBytes("US-ASCII");
/* 250 */           QuotedPrintableDecoder.decode(encodedData, out);
/* 251 */           decodedData = out.toByteArray();
/*     */         } else {
/* 253 */           throw new UnsupportedEncodingException("Unknown RFC 2047 encoding: " + encoding);
/*     */         } }
/*     */       byte[] decodedData;
/* 256 */       return new String(decodedData, javaCharset(charset));
/*     */     } catch (IOException e) {
/* 258 */       throw new UnsupportedEncodingException("Invalid RFC 2047 encoding");
/*     */     }
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
/*     */   private static String javaCharset(String charset)
/*     */   {
/* 272 */     if (charset == null) {
/* 273 */       return null;
/*     */     }
/*     */     
/* 276 */     String mappedCharset = (String)MIME2JAVA.get(charset.toLowerCase(Locale.ENGLISH));
/*     */     
/*     */ 
/* 279 */     if (mappedCharset == null) {
/* 280 */       return charset;
/*     */     }
/* 282 */     return mappedCharset;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\fileuploa\\util\mime\MimeUtility.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */