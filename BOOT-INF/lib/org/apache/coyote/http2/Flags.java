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
/*    */ public class Flags
/*    */ {
/*    */   public static boolean isEndOfStream(int flags)
/*    */   {
/* 27 */     return (flags & 0x1) != 0;
/*    */   }
/*    */   
/*    */   public static boolean isAck(int flags)
/*    */   {
/* 32 */     return (flags & 0x1) != 0;
/*    */   }
/*    */   
/*    */   public static boolean isEndOfHeaders(int flags)
/*    */   {
/* 37 */     return (flags & 0x4) != 0;
/*    */   }
/*    */   
/*    */   public static boolean hasPadding(int flags)
/*    */   {
/* 42 */     return (flags & 0x8) != 0;
/*    */   }
/*    */   
/*    */   public static boolean hasPriority(int flags)
/*    */   {
/* 47 */     return (flags & 0x20) != 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http2\Flags.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */