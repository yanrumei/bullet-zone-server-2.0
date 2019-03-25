/*    */ package org.springframework.boot.bind;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import java.net.InetAddress;
/*    */ import java.net.UnknownHostException;
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
/*    */ public class InetAddressEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   public String getAsText()
/*    */   {
/* 33 */     return ((InetAddress)getValue()).getHostAddress();
/*    */   }
/*    */   
/*    */   public void setAsText(String text) throws IllegalArgumentException
/*    */   {
/*    */     try {
/* 39 */       setValue(InetAddress.getByName(text));
/*    */     }
/*    */     catch (UnknownHostException ex) {
/* 42 */       throw new IllegalArgumentException("Cannot locate host", ex);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\bind\InetAddressEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */