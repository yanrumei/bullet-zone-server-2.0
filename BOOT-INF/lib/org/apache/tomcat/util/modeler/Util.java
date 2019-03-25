/*    */ package org.apache.tomcat.util.modeler;
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
/*    */ public class Util
/*    */ {
/*    */   public static boolean objectNameValueNeedsQuote(String input)
/*    */   {
/* 26 */     for (int i = 0; i < input.length(); i++) {
/* 27 */       char ch = input.charAt(i);
/* 28 */       if ((ch == ',') || (ch == '=') || (ch == ':') || (ch == '*') || (ch == '?')) {
/* 29 */         return true;
/*    */       }
/*    */     }
/* 32 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\modeler\Util.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */