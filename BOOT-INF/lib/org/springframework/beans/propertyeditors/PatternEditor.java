/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import java.util.regex.Pattern;
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
/*    */ public class PatternEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   private final int flags;
/*    */   
/*    */   public PatternEditor()
/*    */   {
/* 40 */     this.flags = 0;
/*    */   }
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
/*    */   public PatternEditor(int flags)
/*    */   {
/* 54 */     this.flags = flags;
/*    */   }
/*    */   
/*    */ 
/*    */   public void setAsText(String text)
/*    */   {
/* 60 */     setValue(text != null ? Pattern.compile(text, this.flags) : null);
/*    */   }
/*    */   
/*    */   public String getAsText()
/*    */   {
/* 65 */     Pattern value = (Pattern)getValue();
/* 66 */     return value != null ? value.pattern() : "";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\propertyeditors\PatternEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */