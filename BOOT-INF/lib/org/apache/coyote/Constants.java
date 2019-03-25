/*    */ package org.apache.coyote;
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
/*    */ public final class Constants
/*    */ {
/*    */   @Deprecated
/*    */   public static final String DEFAULT_CHARACTER_ENCODING = "ISO-8859-1";
/* 35 */   public static final Charset DEFAULT_URI_CHARSET = StandardCharsets.ISO_8859_1;
/* 36 */   public static final Charset DEFAULT_BODY_CHARSET = StandardCharsets.ISO_8859_1;
/*    */   
/*    */   public static final int MAX_NOTES = 32;
/*    */   
/*    */   public static final int STAGE_NEW = 0;
/*    */   
/*    */   public static final int STAGE_PARSE = 1;
/*    */   
/*    */   public static final int STAGE_PREPARE = 2;
/*    */   
/*    */   public static final int STAGE_SERVICE = 3;
/*    */   
/*    */   public static final int STAGE_ENDINPUT = 4;
/*    */   
/*    */   public static final int STAGE_ENDOUTPUT = 5;
/*    */   
/*    */   public static final int STAGE_KEEPALIVE = 6;
/*    */   
/*    */   public static final int STAGE_ENDED = 7;
/*    */   
/*    */   public static final int DEFAULT_CONNECTION_LINGER = -1;
/*    */   public static final boolean DEFAULT_TCP_NO_DELAY = true;
/* 58 */   public static final boolean IS_SECURITY_ENABLED = System.getSecurityManager() != null;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   @Deprecated
/* 68 */   public static final boolean USE_CUSTOM_STATUS_MSG_IN_HEADER = Boolean.getBoolean("org.apache.coyote.USE_CUSTOM_STATUS_MSG_IN_HEADER");
/*    */   public static final String SENDFILE_SUPPORTED_ATTR = "org.apache.tomcat.sendfile.support";
/*    */   public static final String SENDFILE_FILENAME_ATTR = "org.apache.tomcat.sendfile.filename";
/*    */   public static final String SENDFILE_FILE_START_ATTR = "org.apache.tomcat.sendfile.start";
/*    */   public static final String SENDFILE_FILE_END_ATTR = "org.apache.tomcat.sendfile.end";
/*    */   public static final String REMOTE_ADDR_ATTRIBUTE = "org.apache.tomcat.remoteAddr";
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\Constants.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */