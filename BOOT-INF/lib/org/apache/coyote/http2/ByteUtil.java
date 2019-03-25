/*    */ package org.apache.coyote.http2;
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
/*    */ public class ByteUtil
/*    */ {
/*    */   public static boolean isBit7Set(byte input)
/*    */   {
/* 30 */     return (input & 0x80) != 0;
/*    */   }
/*    */   
/*    */   public static int get31Bits(byte[] input, int firstByte)
/*    */   {
/* 35 */     return ((input[firstByte] & 0x7F) << 24) + ((input[(firstByte + 1)] & 0xFF) << 16) + ((input[(firstByte + 2)] & 0xFF) << 8) + (input[(firstByte + 3)] & 0xFF);
/*    */   }
/*    */   
/*    */ 
/*    */   public static void set31Bits(byte[] output, int firstByte, int value)
/*    */   {
/* 41 */     output[firstByte] = ((byte)((value & 0x7F000000) >> 24));
/* 42 */     output[(firstByte + 1)] = ((byte)((value & 0xFF0000) >> 16));
/* 43 */     output[(firstByte + 2)] = ((byte)((value & 0xFF00) >> 8));
/* 44 */     output[(firstByte + 3)] = ((byte)(value & 0xFF));
/*    */   }
/*    */   
/*    */   public static int getOneByte(byte[] input, int pos)
/*    */   {
/* 49 */     return input[pos] & 0xFF;
/*    */   }
/*    */   
/*    */   public static int getTwoBytes(byte[] input, int firstByte)
/*    */   {
/* 54 */     return ((input[firstByte] & 0xFF) << 8) + (input[(firstByte + 1)] & 0xFF);
/*    */   }
/*    */   
/*    */   public static int getThreeBytes(byte[] input, int firstByte)
/*    */   {
/* 59 */     return ((input[firstByte] & 0xFF) << 16) + ((input[(firstByte + 1)] & 0xFF) << 8) + (input[(firstByte + 2)] & 0xFF);
/*    */   }
/*    */   
/*    */ 
/*    */   public static void setOneBytes(byte[] output, int firstByte, int value)
/*    */   {
/* 65 */     output[firstByte] = ((byte)(value & 0xFF));
/*    */   }
/*    */   
/*    */   public static void setTwoBytes(byte[] output, int firstByte, int value)
/*    */   {
/* 70 */     output[firstByte] = ((byte)((value & 0xFF00) >> 8));
/* 71 */     output[(firstByte + 1)] = ((byte)(value & 0xFF));
/*    */   }
/*    */   
/*    */   public static void setThreeBytes(byte[] output, int firstByte, int value)
/*    */   {
/* 76 */     output[firstByte] = ((byte)((value & 0xFF0000) >> 16));
/* 77 */     output[(firstByte + 1)] = ((byte)((value & 0xFF00) >> 8));
/* 78 */     output[(firstByte + 2)] = ((byte)(value & 0xFF));
/*    */   }
/*    */   
/*    */   public static long getFourBytes(byte[] input, int firstByte)
/*    */   {
/* 83 */     return ((input[firstByte] & 0xFF) << 24) + ((input[(firstByte + 1)] & 0xFF) << 16) + ((input[(firstByte + 2)] & 0xFF) << 8) + (input[(firstByte + 3)] & 0xFF);
/*    */   }
/*    */   
/*    */ 
/*    */   public static void setFourBytes(byte[] output, int firstByte, long value)
/*    */   {
/* 89 */     output[firstByte] = ((byte)(int)((value & 0xFFFFFFFFFF000000) >> 24));
/* 90 */     output[(firstByte + 1)] = ((byte)(int)((value & 0xFF0000) >> 16));
/* 91 */     output[(firstByte + 2)] = ((byte)(int)((value & 0xFF00) >> 8));
/* 92 */     output[(firstByte + 3)] = ((byte)(int)(value & 0xFF));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http2\ByteUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */