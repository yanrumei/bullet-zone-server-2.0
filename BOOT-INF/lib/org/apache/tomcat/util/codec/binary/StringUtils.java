/*    */ package org.apache.tomcat.util.codec.binary;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.StandardCharsets;
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
/*    */ public class StringUtils
/*    */ {
/*    */   private static byte[] getBytes(String string, Charset charset)
/*    */   {
/* 44 */     if (string == null) {
/* 45 */       return null;
/*    */     }
/* 47 */     return string.getBytes(charset);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static byte[] getBytesUtf8(String string)
/*    */   {
/* 60 */     return getBytes(string, StandardCharsets.UTF_8);
/*    */   }
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
/*    */   private static String newString(byte[] bytes, Charset charset)
/*    */   {
/* 74 */     return bytes == null ? null : new String(bytes, charset);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String newStringUsAscii(byte[] bytes)
/*    */   {
/* 86 */     return newString(bytes, StandardCharsets.US_ASCII);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String newStringUtf8(byte[] bytes)
/*    */   {
/* 98 */     return newString(bytes, StandardCharsets.UTF_8);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\codec\binary\StringUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */