/*    */ package org.apache.tomcat.jni;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OS
/*    */ {
/*    */   private static final int UNIX = 1;
/*    */   
/*    */ 
/*    */   private static final int NETWARE = 2;
/*    */   
/*    */ 
/*    */   private static final int WIN32 = 3;
/*    */   
/*    */ 
/*    */   private static final int WIN64 = 4;
/*    */   
/*    */ 
/*    */   private static final int LINUX = 5;
/*    */   
/*    */ 
/*    */   private static final int SOLARIS = 6;
/*    */   
/*    */ 
/*    */   private static final int BSD = 7;
/*    */   
/*    */ 
/*    */   private static final int MACOSX = 8;
/*    */   
/*    */ 
/*    */   public static final int LOG_EMERG = 1;
/*    */   
/*    */ 
/*    */   public static final int LOG_ERROR = 2;
/*    */   
/*    */ 
/*    */   public static final int LOG_NOTICE = 3;
/*    */   
/*    */ 
/*    */   public static final int LOG_WARN = 4;
/*    */   
/*    */ 
/*    */   public static final int LOG_INFO = 5;
/*    */   
/*    */ 
/*    */   public static final int LOG_DEBUG = 6;
/*    */   
/*    */ 
/* 49 */   public static final boolean IS_UNIX = is(1);
/* 50 */   public static final boolean IS_NETWARE = is(2);
/* 51 */   public static final boolean IS_WIN32 = is(3);
/* 52 */   public static final boolean IS_WIN64 = is(4);
/* 53 */   public static final boolean IS_LINUX = is(5);
/* 54 */   public static final boolean IS_SOLARIS = is(6);
/* 55 */   public static final boolean IS_BSD = is(7);
/* 56 */   public static final boolean IS_MACOSX = is(8);
/*    */   
/*    */   private static native boolean is(int paramInt);
/*    */   
/*    */   public static native String defaultEncoding(long paramLong);
/*    */   
/*    */   public static native String localeEncoding(long paramLong);
/*    */   
/*    */   public static native int random(byte[] paramArrayOfByte, int paramInt);
/*    */   
/*    */   public static native int info(long[] paramArrayOfLong);
/*    */   
/*    */   public static native String expand(String paramString);
/*    */   
/*    */   public static native void sysloginit(String paramString);
/*    */   
/*    */   public static native void syslog(int paramInt, String paramString);
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomcat\jni\OS.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */