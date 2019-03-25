/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InputTag
/*     */   extends AbstractHtmlInputElementTag
/*     */ {
/*     */   public static final String SIZE_ATTRIBUTE = "size";
/*     */   public static final String MAXLENGTH_ATTRIBUTE = "maxlength";
/*     */   public static final String ALT_ATTRIBUTE = "alt";
/*     */   public static final String ONSELECT_ATTRIBUTE = "onselect";
/*     */   public static final String READONLY_ATTRIBUTE = "readonly";
/*     */   public static final String AUTOCOMPLETE_ATTRIBUTE = "autocomplete";
/*     */   private String size;
/*     */   private String maxlength;
/*     */   private String alt;
/*     */   private String onselect;
/*     */   private String autocomplete;
/*     */   
/*     */   public void setSize(String size)
/*     */   {
/*  62 */     this.size = size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getSize()
/*     */   {
/*  69 */     return this.size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMaxlength(String maxlength)
/*     */   {
/*  77 */     this.maxlength = maxlength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getMaxlength()
/*     */   {
/*  84 */     return this.maxlength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAlt(String alt)
/*     */   {
/*  92 */     this.alt = alt;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getAlt()
/*     */   {
/*  99 */     return this.alt;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOnselect(String onselect)
/*     */   {
/* 107 */     this.onselect = onselect;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getOnselect()
/*     */   {
/* 114 */     return this.onselect;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAutocomplete(String autocomplete)
/*     */   {
/* 122 */     this.autocomplete = autocomplete;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getAutocomplete()
/*     */   {
/* 129 */     return this.autocomplete;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int writeTagContent(TagWriter tagWriter)
/*     */     throws JspException
/*     */   {
/* 140 */     tagWriter.startTag("input");
/*     */     
/* 142 */     writeDefaultAttributes(tagWriter);
/* 143 */     if (!hasDynamicTypeAttribute()) {
/* 144 */       tagWriter.writeAttribute("type", getType());
/*     */     }
/* 146 */     writeValue(tagWriter);
/*     */     
/*     */ 
/* 149 */     writeOptionalAttribute(tagWriter, "size", getSize());
/* 150 */     writeOptionalAttribute(tagWriter, "maxlength", getMaxlength());
/* 151 */     writeOptionalAttribute(tagWriter, "alt", getAlt());
/* 152 */     writeOptionalAttribute(tagWriter, "onselect", getOnselect());
/* 153 */     writeOptionalAttribute(tagWriter, "autocomplete", getAutocomplete());
/*     */     
/* 155 */     tagWriter.endTag();
/* 156 */     return 0;
/*     */   }
/*     */   
/*     */   private boolean hasDynamicTypeAttribute() {
/* 160 */     return (getDynamicAttributes() != null) && (getDynamicAttributes().containsKey("type"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void writeValue(TagWriter tagWriter)
/*     */     throws JspException
/*     */   {
/* 169 */     String value = getDisplayString(getBoundValue(), getPropertyEditor());
/* 170 */     String type = hasDynamicTypeAttribute() ? (String)getDynamicAttributes().get("type") : getType();
/* 171 */     tagWriter.writeAttribute("value", processFieldValue(getName(), value, type));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isValidDynamicAttribute(String localName, Object value)
/*     */   {
/* 180 */     if (("type".equals(localName)) && (
/* 181 */       ("checkbox".equals(value)) || ("radio".equals(value)))) {
/* 182 */       return false;
/*     */     }
/*     */     
/* 185 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getType()
/*     */   {
/* 194 */     return "text";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\form\InputTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */