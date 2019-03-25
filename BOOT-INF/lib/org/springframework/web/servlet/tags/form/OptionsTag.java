/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.PageContext;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.support.BindStatus;
/*     */ import org.springframework.web.util.TagUtils;
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
/*     */ public class OptionsTag
/*     */   extends AbstractHtmlElementTag
/*     */ {
/*     */   private Object items;
/*     */   private String itemValue;
/*     */   private String itemLabel;
/*     */   private boolean disabled;
/*     */   
/*     */   public void setItems(Object items)
/*     */   {
/*  71 */     this.items = items;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object getItems()
/*     */   {
/*  80 */     return this.items;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setItemValue(String itemValue)
/*     */   {
/*  90 */     Assert.hasText(itemValue, "'itemValue' must not be empty");
/*  91 */     this.itemValue = itemValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getItemValue()
/*     */   {
/*  99 */     return this.itemValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setItemLabel(String itemLabel)
/*     */   {
/* 107 */     Assert.hasText(itemLabel, "'itemLabel' must not be empty");
/* 108 */     this.itemLabel = itemLabel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getItemLabel()
/*     */   {
/* 116 */     return this.itemLabel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setDisabled(boolean disabled)
/*     */   {
/* 123 */     this.disabled = disabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean isDisabled()
/*     */   {
/* 130 */     return this.disabled;
/*     */   }
/*     */   
/*     */   protected int writeTagContent(TagWriter tagWriter)
/*     */     throws JspException
/*     */   {
/* 136 */     SelectTag selectTag = getSelectTag();
/* 137 */     Object items = getItems();
/* 138 */     Object itemsObject = null;
/* 139 */     if (items != null) {
/* 140 */       itemsObject = (items instanceof String) ? evaluate("items", items) : items;
/*     */     }
/*     */     else {
/* 143 */       Class<?> selectTagBoundType = selectTag.getBindStatus().getValueType();
/* 144 */       if ((selectTagBoundType != null) && (selectTagBoundType.isEnum())) {
/* 145 */         itemsObject = selectTagBoundType.getEnumConstants();
/*     */       }
/*     */     }
/* 148 */     if (itemsObject != null) {
/* 149 */       String selectName = selectTag.getName();
/* 150 */       String itemValue = getItemValue();
/* 151 */       String itemLabel = getItemLabel();
/*     */       
/* 153 */       String valueProperty = itemValue != null ? ObjectUtils.getDisplayString(evaluate("itemValue", itemValue)) : null;
/*     */       
/* 155 */       String labelProperty = itemLabel != null ? ObjectUtils.getDisplayString(evaluate("itemLabel", itemLabel)) : null;
/* 156 */       OptionsWriter optionWriter = new OptionsWriter(selectName, itemsObject, valueProperty, labelProperty);
/* 157 */       optionWriter.writeOptions(tagWriter);
/*     */     }
/* 159 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String resolveId()
/*     */     throws JspException
/*     */   {
/* 168 */     Object id = evaluate("id", getId());
/* 169 */     if (id != null) {
/* 170 */       String idString = id.toString();
/* 171 */       return StringUtils.hasText(idString) ? TagIdGenerator.nextId(idString, this.pageContext) : null;
/*     */     }
/* 173 */     return null;
/*     */   }
/*     */   
/*     */   private SelectTag getSelectTag() {
/* 177 */     TagUtils.assertHasAncestorOfType(this, SelectTag.class, "options", "select");
/* 178 */     return (SelectTag)findAncestorWithClass(this, SelectTag.class);
/*     */   }
/*     */   
/*     */   protected BindStatus getBindStatus()
/*     */   {
/* 183 */     return (BindStatus)this.pageContext.getAttribute("org.springframework.web.servlet.tags.form.SelectTag.listValue");
/*     */   }
/*     */   
/*     */ 
/*     */   private class OptionsWriter
/*     */     extends OptionWriter
/*     */   {
/*     */     private final String selectName;
/*     */     
/*     */ 
/*     */     public OptionsWriter(String selectName, Object optionSource, String valueProperty, String labelProperty)
/*     */     {
/* 195 */       super(OptionsTag.this.getBindStatus(), valueProperty, labelProperty, OptionsTag.this.isHtmlEscape());
/* 196 */       this.selectName = selectName;
/*     */     }
/*     */     
/*     */     protected boolean isOptionDisabled() throws JspException
/*     */     {
/* 201 */       return OptionsTag.this.isDisabled();
/*     */     }
/*     */     
/*     */     protected void writeCommonAttributes(TagWriter tagWriter) throws JspException
/*     */     {
/* 206 */       OptionsTag.this.writeOptionalAttribute(tagWriter, "id", OptionsTag.this.resolveId());
/* 207 */       OptionsTag.this.writeOptionalAttributes(tagWriter);
/*     */     }
/*     */     
/*     */     protected String processOptionValue(String value)
/*     */     {
/* 212 */       return OptionsTag.this.processFieldValue(this.selectName, value, "option");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\form\OptionsTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */