/*     */ package org.apache.coyote.http11;
/*     */ 
/*     */ import org.apache.tomcat.util.buf.ByteChunk;
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
/*     */ public final class Constants
/*     */ {
/*     */   public static final int DEFAULT_CONNECTION_TIMEOUT = 60000;
/*     */   public static final String CRLF = "\r\n";
/*     */   public static final byte CR = 13;
/*     */   public static final byte LF = 10;
/*     */   public static final byte SP = 32;
/*     */   public static final byte HT = 9;
/*     */   public static final byte COLON = 58;
/*     */   public static final byte SEMI_COLON = 59;
/*     */   public static final byte A = 65;
/*     */   public static final byte a = 97;
/*     */   public static final byte Z = 90;
/*     */   public static final byte QUESTION = 63;
/*     */   public static final byte LC_OFFSET = -32;
/*     */   public static final String CONNECTION = "Connection";
/*     */   public static final String CLOSE = "close";
/* 106 */   public static final byte[] CLOSE_BYTES = ByteChunk.convertToBytes("close");
/*     */   public static final String KEEPALIVE = "keep-alive";
/* 108 */   public static final byte[] KEEPALIVE_BYTES = ByteChunk.convertToBytes("keep-alive");
/*     */   
/*     */ 
/*     */ 
/*     */   public static final String CHUNKED = "chunked";
/*     */   
/*     */ 
/*     */   @Deprecated
/* 116 */   public static final byte[] ACK_BYTES_REASON = ByteChunk.convertToBytes("HTTP/1.1 100 Continue\r\n\r\n");
/* 117 */   public static final byte[] ACK_BYTES = ByteChunk.convertToBytes("HTTP/1.1 100 \r\n\r\n");
/*     */   public static final String TRANSFERENCODING = "Transfer-Encoding";
/* 119 */   public static final byte[] _200_BYTES = ByteChunk.convertToBytes("200");
/* 120 */   public static final byte[] _400_BYTES = ByteChunk.convertToBytes("400");
/* 121 */   public static final byte[] _404_BYTES = ByteChunk.convertToBytes("404");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int IDENTITY_FILTER = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int CHUNKED_FILTER = 1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int VOID_FILTER = 2;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int GZIP_FILTER = 3;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int BUFFERED_FILTER = 3;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String HTTP_10 = "HTTP/1.0";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String HTTP_11 = "HTTP/1.1";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 164 */   public static final byte[] HTTP_11_BYTES = ByteChunk.convertToBytes("HTTP/1.1");
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http11\Constants.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */