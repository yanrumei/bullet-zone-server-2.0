/*    */ package org.apache.catalina.util;
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
/*    */ public class StandardSessionIdGenerator
/*    */   extends SessionIdGeneratorBase
/*    */ {
/*    */   public String generateSessionId(String route)
/*    */   {
/* 24 */     byte[] random = new byte[16];
/* 25 */     int sessionIdLength = getSessionIdLength();
/*    */     
/*    */ 
/*    */ 
/* 29 */     StringBuilder buffer = new StringBuilder(2 * sessionIdLength + 20);
/*    */     
/* 31 */     int resultLenBytes = 0;
/* 33 */     for (; 
/* 33 */         resultLenBytes < sessionIdLength; 
/*    */         
/*    */ 
/*    */ 
/* 37 */         goto 42)
/*    */     {
/* 34 */       getRandomBytes(random);
/* 35 */       int j = 0;
/* 36 */       if ((j < random.length) && (resultLenBytes < sessionIdLength))
/*    */       {
/* 38 */         byte b1 = (byte)((random[j] & 0xF0) >> 4);
/* 39 */         byte b2 = (byte)(random[j] & 0xF);
/* 40 */         if (b1 < 10) {
/* 41 */           buffer.append((char)(48 + b1));
/*    */         } else
/* 43 */           buffer.append((char)(65 + (b1 - 10)));
/* 44 */         if (b2 < 10) {
/* 45 */           buffer.append((char)(48 + b2));
/*    */         } else
/* 47 */           buffer.append((char)(65 + (b2 - 10)));
/* 48 */         resultLenBytes++;j++;
/*    */       }
/*    */     }
/*    */     
/* 52 */     if ((route != null) && (route.length() > 0)) {
/* 53 */       buffer.append('.').append(route);
/*    */     } else {
/* 55 */       String jvmRoute = getJvmRoute();
/* 56 */       if ((jvmRoute != null) && (jvmRoute.length() > 0)) {
/* 57 */         buffer.append('.').append(jvmRoute);
/*    */       }
/*    */     }
/*    */     
/* 61 */     return buffer.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\util\StandardSessionIdGenerator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */