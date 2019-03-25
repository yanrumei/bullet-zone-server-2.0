/*    */ package org.springframework.web.servlet.tags.form;
/*    */ 
/*    */ import javax.servlet.jsp.JspException;
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
/*    */ public class CheckboxesTag
/*    */   extends AbstractMultiCheckedElementTag
/*    */ {
/*    */   protected int writeTagContent(TagWriter tagWriter)
/*    */     throws JspException
/*    */   {
/* 39 */     super.writeTagContent(tagWriter);
/*    */     
/* 41 */     if (!isDisabled())
/*    */     {
/* 43 */       tagWriter.startTag("input");
/* 44 */       tagWriter.writeAttribute("type", "hidden");
/* 45 */       String name = "_" + getName();
/* 46 */       tagWriter.writeAttribute("name", name);
/* 47 */       tagWriter.writeAttribute("value", processFieldValue(name, "on", getInputType()));
/* 48 */       tagWriter.endTag();
/*    */     }
/*    */     
/* 51 */     return 0;
/*    */   }
/*    */   
/*    */   protected String getInputType()
/*    */   {
/* 56 */     return "checkbox";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\form\CheckboxesTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */