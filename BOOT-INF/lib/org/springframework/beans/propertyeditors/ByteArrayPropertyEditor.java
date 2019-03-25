/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
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
/*    */ public class ByteArrayPropertyEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   public void setAsText(String text)
/*    */   {
/* 33 */     setValue(text != null ? text.getBytes() : null);
/*    */   }
/*    */   
/*    */   public String getAsText()
/*    */   {
/* 38 */     byte[] value = (byte[])getValue();
/* 39 */     return value != null ? new String(value) : "";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\propertyeditors\ByteArrayPropertyEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */