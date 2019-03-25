/*     */ package org.apache.coyote.ajp;
/*     */ 
/*     */ import java.util.Hashtable;
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
/*     */   public static final int DEFAULT_CONNECTION_TIMEOUT = -1;
/*     */   public static final byte JK_AJP13_FORWARD_REQUEST = 2;
/*     */   public static final byte JK_AJP13_SHUTDOWN = 7;
/*     */   public static final byte JK_AJP13_PING_REQUEST = 8;
/*     */   public static final byte JK_AJP13_CPING_REQUEST = 10;
/*     */   public static final byte JK_AJP13_SEND_BODY_CHUNK = 3;
/*     */   public static final byte JK_AJP13_SEND_HEADERS = 4;
/*     */   public static final byte JK_AJP13_END_RESPONSE = 5;
/*     */   public static final byte JK_AJP13_GET_BODY_CHUNK = 6;
/*     */   public static final byte JK_AJP13_CPONG_REPLY = 9;
/*     */   public static final int SC_RESP_CONTENT_TYPE = 40961;
/*     */   public static final int SC_RESP_CONTENT_LANGUAGE = 40962;
/*     */   public static final int SC_RESP_CONTENT_LENGTH = 40963;
/*     */   public static final int SC_RESP_DATE = 40964;
/*     */   public static final int SC_RESP_LAST_MODIFIED = 40965;
/*     */   public static final int SC_RESP_LOCATION = 40966;
/*     */   public static final int SC_RESP_SET_COOKIE = 40967;
/*     */   public static final int SC_RESP_SET_COOKIE2 = 40968;
/*     */   public static final int SC_RESP_SERVLET_ENGINE = 40969;
/*     */   public static final int SC_RESP_STATUS = 40970;
/*     */   public static final int SC_RESP_WWW_AUTHENTICATE = 40971;
/*     */   public static final int SC_RESP_AJP13_MAX = 11;
/*     */   public static final byte SC_A_CONTEXT = 1;
/*     */   public static final byte SC_A_SERVLET_PATH = 2;
/*     */   public static final byte SC_A_REMOTE_USER = 3;
/*     */   public static final byte SC_A_AUTH_TYPE = 4;
/*     */   public static final byte SC_A_QUERY_STRING = 5;
/*     */   public static final byte SC_A_JVM_ROUTE = 6;
/*     */   public static final byte SC_A_SSL_CERT = 7;
/*     */   public static final byte SC_A_SSL_CIPHER = 8;
/*     */   public static final byte SC_A_SSL_SESSION = 9;
/*     */   public static final byte SC_A_SSL_KEY_SIZE = 11;
/*     */   public static final byte SC_A_SECRET = 12;
/*     */   public static final byte SC_A_STORED_METHOD = 13;
/*     */   public static final byte SC_A_REQ_ATTRIBUTE = 10;
/*     */   public static final String SC_A_REQ_LOCAL_ADDR = "AJP_LOCAL_ADDR";
/*     */   public static final String SC_A_REQ_REMOTE_PORT = "AJP_REMOTE_PORT";
/*     */   public static final String SC_A_SSL_PROTOCOL = "AJP_SSL_PROTOCOL";
/*     */   public static final byte SC_A_ARE_DONE = -1;
/*     */   public static final int MAX_PACKET_SIZE = 8192;
/*     */   public static final int H_SIZE = 4;
/*     */   public static final int READ_HEAD_LEN = 6;
/*     */   public static final int SEND_HEAD_LEN = 8;
/*     */   public static final int MAX_READ_SIZE = 8186;
/*     */   public static final int MAX_SEND_SIZE = 8184;
/* 108 */   private static final String[] methodTransArray = { "OPTIONS", "GET", "HEAD", "POST", "PUT", "DELETE", "TRACE", "PROPFIND", "PROPPATCH", "MKCOL", "COPY", "MOVE", "LOCK", "UNLOCK", "ACL", "REPORT", "VERSION-CONTROL", "CHECKIN", "CHECKOUT", "UNCHECKOUT", "SEARCH", "MKWORKSPACE", "UPDATE", "LABEL", "MERGE", "BASELINE-CONTROL", "MKACTIVITY" };
/*     */   
/*     */ 
/*     */   public static final int SC_M_JK_STORED = -1;
/*     */   
/*     */   public static final int SC_REQ_ACCEPT = 1;
/*     */   
/*     */   public static final int SC_REQ_ACCEPT_CHARSET = 2;
/*     */   
/*     */   public static final int SC_REQ_ACCEPT_ENCODING = 3;
/*     */   
/*     */   public static final int SC_REQ_ACCEPT_LANGUAGE = 4;
/*     */   
/*     */   public static final int SC_REQ_AUTHORIZATION = 5;
/*     */   
/*     */   public static final int SC_REQ_CONNECTION = 6;
/*     */   
/*     */   public static final int SC_REQ_CONTENT_TYPE = 7;
/*     */   
/*     */   public static final int SC_REQ_CONTENT_LENGTH = 8;
/*     */   
/*     */   public static final int SC_REQ_COOKIE = 9;
/*     */   
/*     */   public static final int SC_REQ_COOKIE2 = 10;
/*     */   
/*     */   public static final int SC_REQ_HOST = 11;
/*     */   
/*     */   public static final int SC_REQ_PRAGMA = 12;
/*     */   
/*     */   public static final int SC_REQ_REFERER = 13;
/*     */   
/*     */   public static final int SC_REQ_USER_AGENT = 14;
/*     */   
/*     */ 
/*     */   public static final String getMethodForCode(int code)
/*     */   {
/* 144 */     return methodTransArray[code];
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
/*     */ 
/* 166 */   private static final String[] headerTransArray = { "accept", "accept-charset", "accept-encoding", "accept-language", "authorization", "connection", "content-type", "content-length", "cookie", "cookie2", "host", "pragma", "referer", "user-agent" };
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
/*     */   public static final String getHeaderForCode(int code)
/*     */   {
/* 189 */     return headerTransArray[code];
/*     */   }
/*     */   
/*     */ 
/* 193 */   private static final String[] responseTransArray = { "Content-Type", "Content-Language", "Content-Length", "Date", "Last-Modified", "Location", "Set-Cookie", "Set-Cookie2", "Servlet-Engine", "Status", "WWW-Authenticate" };
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
/*     */   public static final String getResponseHeaderForCode(int code)
/*     */   {
/* 213 */     return responseTransArray[code];
/*     */   }
/*     */   
/* 216 */   private static final Hashtable<String, Integer> responseTransHash = new Hashtable(20);
/*     */   
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 222 */       for (int i = 0; i < 11; i++) {
/* 223 */         responseTransHash.put(getResponseHeaderForCode(i), 
/* 224 */           Integer.valueOf(40961 + i));
/*     */       }
/*     */     }
/*     */     catch (Exception localException) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int getResponseAjpIndex(String header)
/*     */   {
/* 234 */     Integer i = (Integer)responseTransHash.get(header);
/* 235 */     if (i == null) {
/* 236 */       return 0;
/*     */     }
/* 238 */     return i.intValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\ajp\Constants.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */