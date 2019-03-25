/*    */ package org.springframework.http;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
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
/*    */ public class MediaTypeEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   public void setAsText(String text)
/*    */   {
/* 36 */     if (StringUtils.hasText(text)) {
/* 37 */       setValue(MediaType.parseMediaType(text));
/*    */     }
/*    */     else {
/* 40 */       setValue(null);
/*    */     }
/*    */   }
/*    */   
/*    */   public String getAsText()
/*    */   {
/* 46 */     MediaType mediaType = (MediaType)getValue();
/* 47 */     return mediaType != null ? mediaType.toString() : "";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\MediaTypeEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */