/*    */ package org.springframework.web.servlet.tags.form;
/*    */ 
/*    */ import java.beans.PropertyEditor;
/*    */ import org.springframework.util.ObjectUtils;
/*    */ import org.springframework.web.util.HtmlUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class ValueFormatter
/*    */ {
/*    */   public static String getDisplayString(Object value, boolean htmlEscape)
/*    */   {
/* 47 */     String displayValue = ObjectUtils.getDisplayString(value);
/* 48 */     return htmlEscape ? HtmlUtils.htmlEscape(displayValue) : displayValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String getDisplayString(Object value, PropertyEditor propertyEditor, boolean htmlEscape)
/*    */   {
/* 59 */     if ((propertyEditor != null) && (!(value instanceof String))) {
/*    */       try {
/* 61 */         propertyEditor.setValue(value);
/* 62 */         String text = propertyEditor.getAsText();
/* 63 */         if (text != null) {
/* 64 */           return getDisplayString(text, htmlEscape);
/*    */         }
/*    */       }
/*    */       catch (Throwable localThrowable) {}
/*    */     }
/*    */     
/*    */ 
/* 71 */     return getDisplayString(value, htmlEscape);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\form\ValueFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */