/*    */ package org.apache.tomcat.util.security;
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
/*    */ public final class MD5Encoder
/*    */ {
/* 36 */   private static final char[] hexadecimal = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
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
/*    */   public static String encode(byte[] binaryData)
/*    */   {
/* 49 */     if (binaryData.length != 16) {
/* 50 */       return null;
/*    */     }
/* 52 */     char[] buffer = new char[32];
/*    */     
/* 54 */     for (int i = 0; i < 16; i++) {
/* 55 */       int low = binaryData[i] & 0xF;
/* 56 */       int high = (binaryData[i] & 0xF0) >> 4;
/* 57 */       buffer[(i * 2)] = hexadecimal[high];
/* 58 */       buffer[(i * 2 + 1)] = hexadecimal[low];
/*    */     }
/*    */     
/* 61 */     return new String(buffer);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\security\MD5Encoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */