/*    */ package org.apache.tomcat.util.descriptor;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import org.apache.tomcat.util.ExceptionUtils;
/*    */ import org.xml.sax.InputSource;
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
/*    */ public final class InputSourceUtil
/*    */ {
/*    */   public static void close(InputSource inputSource)
/*    */   {
/* 32 */     if (inputSource == null)
/*    */     {
/* 34 */       return;
/*    */     }
/*    */     
/* 37 */     InputStream is = inputSource.getByteStream();
/* 38 */     if (is != null) {
/*    */       try {
/* 40 */         is.close();
/*    */       } catch (Throwable t) {
/* 42 */         ExceptionUtils.handleThrowable(t);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\InputSourceUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */