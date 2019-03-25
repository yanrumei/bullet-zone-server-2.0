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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractSingleCheckedElementTag
/*    */   extends AbstractCheckedElementTag
/*    */ {
/*    */   private Object value;
/*    */   private Object label;
/*    */   
/*    */   public void setValue(Object value)
/*    */   {
/* 49 */     this.value = value;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected Object getValue()
/*    */   {
/* 56 */     return this.value;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setLabel(Object label)
/*    */   {
/* 64 */     this.label = label;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected Object getLabel()
/*    */   {
/* 71 */     return this.label;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected int writeTagContent(TagWriter tagWriter)
/*    */     throws JspException
/*    */   {
/* 82 */     tagWriter.startTag("input");
/* 83 */     String id = resolveId();
/* 84 */     writeOptionalAttribute(tagWriter, "id", id);
/* 85 */     writeOptionalAttribute(tagWriter, "name", getName());
/* 86 */     writeOptionalAttributes(tagWriter);
/* 87 */     writeTagDetails(tagWriter);
/* 88 */     tagWriter.endTag();
/*    */     
/* 90 */     Object resolvedLabel = evaluate("label", getLabel());
/* 91 */     if (resolvedLabel != null) {
/* 92 */       tagWriter.startTag("label");
/* 93 */       tagWriter.writeAttribute("for", id);
/* 94 */       tagWriter.appendValue(convertToDisplayString(resolvedLabel));
/* 95 */       tagWriter.endTag();
/*    */     }
/*    */     
/* 98 */     return 0;
/*    */   }
/*    */   
/*    */   protected abstract void writeTagDetails(TagWriter paramTagWriter)
/*    */     throws JspException;
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\form\AbstractSingleCheckedElementTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */