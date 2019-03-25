/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import javax.servlet.jsp.JspException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ButtonTag
/*     */   extends AbstractHtmlElementTag
/*     */ {
/*     */   public static final String DISABLED_ATTRIBUTE = "disabled";
/*     */   private TagWriter tagWriter;
/*     */   private String name;
/*     */   private String value;
/*     */   private boolean disabled;
/*     */   
/*     */   public void setName(String name)
/*     */   {
/*  52 */     this.name = name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/*  60 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setValue(String value)
/*     */   {
/*  67 */     this.value = value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getValue()
/*     */   {
/*  74 */     return this.value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setDisabled(boolean disabled)
/*     */   {
/*  81 */     this.disabled = disabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isDisabled()
/*     */   {
/*  88 */     return this.disabled;
/*     */   }
/*     */   
/*     */   protected int writeTagContent(TagWriter tagWriter)
/*     */     throws JspException
/*     */   {
/*  94 */     tagWriter.startTag("button");
/*  95 */     writeDefaultAttributes(tagWriter);
/*  96 */     tagWriter.writeAttribute("type", getType());
/*  97 */     writeValue(tagWriter);
/*  98 */     if (isDisabled()) {
/*  99 */       tagWriter.writeAttribute("disabled", "disabled");
/*     */     }
/* 101 */     tagWriter.forceBlock();
/* 102 */     this.tagWriter = tagWriter;
/* 103 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void writeValue(TagWriter tagWriter)
/*     */     throws JspException
/*     */   {
/* 112 */     String valueToUse = getValue() != null ? getValue() : getDefaultValue();
/* 113 */     tagWriter.writeAttribute("value", processFieldValue(getName(), valueToUse, getType()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDefaultValue()
/*     */   {
/* 121 */     return "Submit";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getType()
/*     */   {
/* 130 */     return "submit";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int doEndTag()
/*     */     throws JspException
/*     */   {
/* 138 */     this.tagWriter.endTag();
/* 139 */     return 6;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\form\ButtonTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */