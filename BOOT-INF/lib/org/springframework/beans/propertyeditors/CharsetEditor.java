/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import java.nio.charset.Charset;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ public class CharsetEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   public void setAsText(String text)
/*    */     throws IllegalArgumentException
/*    */   {
/* 39 */     if (StringUtils.hasText(text)) {
/* 40 */       setValue(Charset.forName(text));
/*    */     }
/*    */     else {
/* 43 */       setValue(null);
/*    */     }
/*    */   }
/*    */   
/*    */   public String getAsText()
/*    */   {
/* 49 */     Charset value = (Charset)getValue();
/* 50 */     return value != null ? value.name() : "";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\propertyeditors\CharsetEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */