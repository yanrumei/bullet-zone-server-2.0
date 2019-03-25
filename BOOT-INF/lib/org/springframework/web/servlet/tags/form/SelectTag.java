/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.PageContext;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.web.servlet.support.BindStatus;
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
/*     */ public class SelectTag
/*     */   extends AbstractHtmlInputElementTag
/*     */ {
/*     */   public static final String LIST_VALUE_PAGE_ATTRIBUTE = "org.springframework.web.servlet.tags.form.SelectTag.listValue";
/*  56 */   private static final Object EMPTY = new Object();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Object items;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String itemValue;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String itemLabel;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String size;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Object multiple;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private TagWriter tagWriter;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setItems(Object items)
/*     */   {
/* 105 */     this.items = (items != null ? items : EMPTY);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object getItems()
/*     */   {
/* 113 */     return this.items;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setItemValue(String itemValue)
/*     */   {
/* 124 */     this.itemValue = itemValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getItemValue()
/*     */   {
/* 132 */     return this.itemValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setItemLabel(String itemLabel)
/*     */   {
/* 141 */     this.itemLabel = itemLabel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getItemLabel()
/*     */   {
/* 149 */     return this.itemLabel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSize(String size)
/*     */   {
/* 157 */     this.size = size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getSize()
/*     */   {
/* 164 */     return this.size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMultiple(Object multiple)
/*     */   {
/* 172 */     this.multiple = multiple;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object getMultiple()
/*     */   {
/* 180 */     return this.multiple;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int writeTagContent(TagWriter tagWriter)
/*     */     throws JspException
/*     */   {
/* 193 */     tagWriter.startTag("select");
/* 194 */     writeDefaultAttributes(tagWriter);
/* 195 */     if (isMultiple()) {
/* 196 */       tagWriter.writeAttribute("multiple", "multiple");
/*     */     }
/* 198 */     tagWriter.writeOptionalAttributeValue("size", getDisplayString(evaluate("size", getSize())));
/*     */     
/* 200 */     Object items = getItems();
/* 201 */     if (items != null)
/*     */     {
/* 203 */       if (items != EMPTY) {
/* 204 */         Object itemsObject = evaluate("items", items);
/* 205 */         if (itemsObject != null) {
/* 206 */           final String selectName = getName();
/*     */           
/* 208 */           String valueProperty = getItemValue() != null ? ObjectUtils.getDisplayString(evaluate("itemValue", getItemValue())) : null;
/*     */           
/* 210 */           String labelProperty = getItemLabel() != null ? ObjectUtils.getDisplayString(evaluate("itemLabel", getItemLabel())) : null;
/*     */           
/* 212 */           OptionWriter optionWriter = new OptionWriter(itemsObject, getBindStatus(), valueProperty, labelProperty, isHtmlEscape())
/*     */           {
/*     */             protected String processOptionValue(String resolvedValue)
/*     */             {
/* 215 */               return SelectTag.this.processFieldValue(selectName, resolvedValue, "option");
/*     */             }
/* 217 */           };
/* 218 */           optionWriter.writeOptions(tagWriter);
/*     */         }
/*     */       }
/* 221 */       tagWriter.endTag(true);
/* 222 */       writeHiddenTagIfNecessary(tagWriter);
/* 223 */       return 0;
/*     */     }
/*     */     
/*     */ 
/* 227 */     tagWriter.forceBlock();
/* 228 */     this.tagWriter = tagWriter;
/* 229 */     this.pageContext.setAttribute("org.springframework.web.servlet.tags.form.SelectTag.listValue", getBindStatus());
/* 230 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void writeHiddenTagIfNecessary(TagWriter tagWriter)
/*     */     throws JspException
/*     */   {
/* 240 */     if (isMultiple()) {
/* 241 */       tagWriter.startTag("input");
/* 242 */       tagWriter.writeAttribute("type", "hidden");
/* 243 */       String name = "_" + getName();
/* 244 */       tagWriter.writeAttribute("name", name);
/* 245 */       tagWriter.writeAttribute("value", processFieldValue(name, "1", "hidden"));
/* 246 */       tagWriter.endTag();
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isMultiple() throws JspException {
/* 251 */     Object multiple = getMultiple();
/* 252 */     if (multiple != null) {
/* 253 */       String stringValue = multiple.toString();
/* 254 */       return ("multiple".equalsIgnoreCase(stringValue)) || (Boolean.parseBoolean(stringValue));
/*     */     }
/* 256 */     return forceMultiple();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean forceMultiple()
/*     */     throws JspException
/*     */   {
/* 264 */     BindStatus bindStatus = getBindStatus();
/* 265 */     Class<?> valueType = bindStatus.getValueType();
/* 266 */     if ((valueType != null) && (typeRequiresMultiple(valueType))) {
/* 267 */       return true;
/*     */     }
/* 269 */     if (bindStatus.getEditor() != null) {
/* 270 */       Object editorValue = bindStatus.getEditor().getValue();
/* 271 */       if ((editorValue != null) && (typeRequiresMultiple(editorValue.getClass()))) {
/* 272 */         return true;
/*     */       }
/*     */     }
/* 275 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean typeRequiresMultiple(Class<?> type)
/*     */   {
/* 283 */     return (type.isArray()) || (Collection.class.isAssignableFrom(type)) || (Map.class.isAssignableFrom(type));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int doEndTag()
/*     */     throws JspException
/*     */   {
/* 292 */     if (this.tagWriter != null) {
/* 293 */       this.tagWriter.endTag();
/* 294 */       writeHiddenTagIfNecessary(this.tagWriter);
/*     */     }
/* 296 */     return 6;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doFinally()
/*     */   {
/* 305 */     super.doFinally();
/* 306 */     this.tagWriter = null;
/* 307 */     this.pageContext.removeAttribute("org.springframework.web.servlet.tags.form.SelectTag.listValue");
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\form\SelectTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */