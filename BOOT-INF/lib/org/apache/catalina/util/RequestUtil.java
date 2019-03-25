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
/*    */ public final class RequestUtil
/*    */ {
/*    */   @Deprecated
/*    */   public static String filter(String message)
/*    */   {
/* 41 */     if (message == null) {
/* 42 */       return null;
/*    */     }
/*    */     
/* 45 */     char[] content = new char[message.length()];
/* 46 */     message.getChars(0, message.length(), content, 0);
/* 47 */     StringBuilder result = new StringBuilder(content.length + 50);
/* 48 */     for (int i = 0; i < content.length; i++) {
/* 49 */       switch (content[i]) {
/*    */       case '<': 
/* 51 */         result.append("&lt;");
/* 52 */         break;
/*    */       case '>': 
/* 54 */         result.append("&gt;");
/* 55 */         break;
/*    */       case '&': 
/* 57 */         result.append("&amp;");
/* 58 */         break;
/*    */       case '"': 
/* 60 */         result.append("&quot;");
/* 61 */         break;
/*    */       default: 
/* 63 */         result.append(content[i]);
/*    */       }
/*    */     }
/* 66 */     return result.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\util\RequestUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */