/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import java.time.ZoneId;
/*    */ import org.springframework.lang.UsesJava8;
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
/*    */ @UsesJava8
/*    */ public class ZoneIdEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   public void setAsText(String text)
/*    */     throws IllegalArgumentException
/*    */   {
/* 38 */     setValue(ZoneId.of(text));
/*    */   }
/*    */   
/*    */   public String getAsText()
/*    */   {
/* 43 */     ZoneId value = (ZoneId)getValue();
/* 44 */     return value != null ? value.getId() : "";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\propertyeditors\ZoneIdEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */