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
/*     */ public abstract class AbstractHtmlInputElementTag
/*     */   extends AbstractHtmlElementTag
/*     */ {
/*     */   public static final String ONFOCUS_ATTRIBUTE = "onfocus";
/*     */   public static final String ONBLUR_ATTRIBUTE = "onblur";
/*     */   public static final String ONCHANGE_ATTRIBUTE = "onchange";
/*     */   public static final String ACCESSKEY_ATTRIBUTE = "accesskey";
/*     */   public static final String DISABLED_ATTRIBUTE = "disabled";
/*     */   public static final String READONLY_ATTRIBUTE = "readonly";
/*     */   private String onfocus;
/*     */   private String onblur;
/*     */   private String onchange;
/*     */   private String accesskey;
/*     */   private boolean disabled;
/*     */   private boolean readonly;
/*     */   
/*     */   public void setOnfocus(String onfocus)
/*     */   {
/*  84 */     this.onfocus = onfocus;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getOnfocus()
/*     */   {
/*  91 */     return this.onfocus;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOnblur(String onblur)
/*     */   {
/*  99 */     this.onblur = onblur;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getOnblur()
/*     */   {
/* 106 */     return this.onblur;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOnchange(String onchange)
/*     */   {
/* 114 */     this.onchange = onchange;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getOnchange()
/*     */   {
/* 121 */     return this.onchange;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAccesskey(String accesskey)
/*     */   {
/* 129 */     this.accesskey = accesskey;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getAccesskey()
/*     */   {
/* 136 */     return this.accesskey;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setDisabled(boolean disabled)
/*     */   {
/* 143 */     this.disabled = disabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean isDisabled()
/*     */   {
/* 150 */     return this.disabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setReadonly(boolean readonly)
/*     */   {
/* 157 */     this.readonly = readonly;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean isReadonly()
/*     */   {
/* 164 */     return this.readonly;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void writeOptionalAttributes(TagWriter tagWriter)
/*     */     throws JspException
/*     */   {
/* 173 */     super.writeOptionalAttributes(tagWriter);
/*     */     
/* 175 */     writeOptionalAttribute(tagWriter, "onfocus", getOnfocus());
/* 176 */     writeOptionalAttribute(tagWriter, "onblur", getOnblur());
/* 177 */     writeOptionalAttribute(tagWriter, "onchange", getOnchange());
/* 178 */     writeOptionalAttribute(tagWriter, "accesskey", getAccesskey());
/* 179 */     if (isDisabled()) {
/* 180 */       tagWriter.writeAttribute("disabled", "disabled");
/*     */     }
/* 182 */     if (isReadonly()) {
/* 183 */       writeOptionalAttribute(tagWriter, "readonly", "readonly");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\form\AbstractHtmlInputElementTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */