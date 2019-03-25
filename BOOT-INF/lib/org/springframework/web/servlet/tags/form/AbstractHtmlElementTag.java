/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.tagext.DynamicAttributes;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public abstract class AbstractHtmlElementTag
/*     */   extends AbstractDataBoundFormElementTag
/*     */   implements DynamicAttributes
/*     */ {
/*     */   public static final String CLASS_ATTRIBUTE = "class";
/*     */   public static final String STYLE_ATTRIBUTE = "style";
/*     */   public static final String LANG_ATTRIBUTE = "lang";
/*     */   public static final String TITLE_ATTRIBUTE = "title";
/*     */   public static final String DIR_ATTRIBUTE = "dir";
/*     */   public static final String TABINDEX_ATTRIBUTE = "tabindex";
/*     */   public static final String ONCLICK_ATTRIBUTE = "onclick";
/*     */   public static final String ONDBLCLICK_ATTRIBUTE = "ondblclick";
/*     */   public static final String ONMOUSEDOWN_ATTRIBUTE = "onmousedown";
/*     */   public static final String ONMOUSEUP_ATTRIBUTE = "onmouseup";
/*     */   public static final String ONMOUSEOVER_ATTRIBUTE = "onmouseover";
/*     */   public static final String ONMOUSEMOVE_ATTRIBUTE = "onmousemove";
/*     */   public static final String ONMOUSEOUT_ATTRIBUTE = "onmouseout";
/*     */   public static final String ONKEYPRESS_ATTRIBUTE = "onkeypress";
/*     */   public static final String ONKEYUP_ATTRIBUTE = "onkeyup";
/*     */   public static final String ONKEYDOWN_ATTRIBUTE = "onkeydown";
/*     */   private String cssClass;
/*     */   private String cssErrorClass;
/*     */   private String cssStyle;
/*     */   private String lang;
/*     */   private String title;
/*     */   private String dir;
/*     */   private String tabindex;
/*     */   private String onclick;
/*     */   private String ondblclick;
/*     */   private String onmousedown;
/*     */   private String onmouseup;
/*     */   private String onmouseover;
/*     */   private String onmousemove;
/*     */   private String onmouseout;
/*     */   private String onkeypress;
/*     */   private String onkeyup;
/*     */   private String onkeydown;
/*     */   private Map<String, Object> dynamicAttributes;
/*     */   
/*     */   public void setCssClass(String cssClass)
/*     */   {
/* 121 */     this.cssClass = cssClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getCssClass()
/*     */   {
/* 129 */     return this.cssClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCssErrorClass(String cssErrorClass)
/*     */   {
/* 137 */     this.cssErrorClass = cssErrorClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getCssErrorClass()
/*     */   {
/* 145 */     return this.cssErrorClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCssStyle(String cssStyle)
/*     */   {
/* 153 */     this.cssStyle = cssStyle;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getCssStyle()
/*     */   {
/* 161 */     return this.cssStyle;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLang(String lang)
/*     */   {
/* 169 */     this.lang = lang;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getLang()
/*     */   {
/* 177 */     return this.lang;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTitle(String title)
/*     */   {
/* 185 */     this.title = title;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getTitle()
/*     */   {
/* 193 */     return this.title;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDir(String dir)
/*     */   {
/* 201 */     this.dir = dir;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDir()
/*     */   {
/* 209 */     return this.dir;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTabindex(String tabindex)
/*     */   {
/* 217 */     this.tabindex = tabindex;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getTabindex()
/*     */   {
/* 225 */     return this.tabindex;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOnclick(String onclick)
/*     */   {
/* 233 */     this.onclick = onclick;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getOnclick()
/*     */   {
/* 241 */     return this.onclick;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOndblclick(String ondblclick)
/*     */   {
/* 249 */     this.ondblclick = ondblclick;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getOndblclick()
/*     */   {
/* 257 */     return this.ondblclick;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOnmousedown(String onmousedown)
/*     */   {
/* 265 */     this.onmousedown = onmousedown;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getOnmousedown()
/*     */   {
/* 273 */     return this.onmousedown;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOnmouseup(String onmouseup)
/*     */   {
/* 281 */     this.onmouseup = onmouseup;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getOnmouseup()
/*     */   {
/* 289 */     return this.onmouseup;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOnmouseover(String onmouseover)
/*     */   {
/* 297 */     this.onmouseover = onmouseover;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getOnmouseover()
/*     */   {
/* 305 */     return this.onmouseover;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOnmousemove(String onmousemove)
/*     */   {
/* 313 */     this.onmousemove = onmousemove;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getOnmousemove()
/*     */   {
/* 321 */     return this.onmousemove;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOnmouseout(String onmouseout)
/*     */   {
/* 329 */     this.onmouseout = onmouseout;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getOnmouseout()
/*     */   {
/* 336 */     return this.onmouseout;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOnkeypress(String onkeypress)
/*     */   {
/* 344 */     this.onkeypress = onkeypress;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getOnkeypress()
/*     */   {
/* 352 */     return this.onkeypress;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOnkeyup(String onkeyup)
/*     */   {
/* 360 */     this.onkeyup = onkeyup;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getOnkeyup()
/*     */   {
/* 368 */     return this.onkeyup;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOnkeydown(String onkeydown)
/*     */   {
/* 376 */     this.onkeydown = onkeydown;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getOnkeydown()
/*     */   {
/* 384 */     return this.onkeydown;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Map<String, Object> getDynamicAttributes()
/*     */   {
/* 391 */     return this.dynamicAttributes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setDynamicAttribute(String uri, String localName, Object value)
/*     */     throws JspException
/*     */   {
/* 399 */     if (this.dynamicAttributes == null) {
/* 400 */       this.dynamicAttributes = new HashMap();
/*     */     }
/* 402 */     if (!isValidDynamicAttribute(localName, value)) {
/* 403 */       throw new IllegalArgumentException("Attribute " + localName + "=\"" + value + "\" is not allowed");
/*     */     }
/*     */     
/* 406 */     this.dynamicAttributes.put(localName, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean isValidDynamicAttribute(String localName, Object value)
/*     */   {
/* 413 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void writeDefaultAttributes(TagWriter tagWriter)
/*     */     throws JspException
/*     */   {
/* 422 */     super.writeDefaultAttributes(tagWriter);
/* 423 */     writeOptionalAttributes(tagWriter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void writeOptionalAttributes(TagWriter tagWriter)
/*     */     throws JspException
/*     */   {
/* 432 */     tagWriter.writeOptionalAttributeValue("class", resolveCssClass());
/* 433 */     tagWriter.writeOptionalAttributeValue("style", 
/* 434 */       ObjectUtils.getDisplayString(evaluate("cssStyle", getCssStyle())));
/* 435 */     writeOptionalAttribute(tagWriter, "lang", getLang());
/* 436 */     writeOptionalAttribute(tagWriter, "title", getTitle());
/* 437 */     writeOptionalAttribute(tagWriter, "dir", getDir());
/* 438 */     writeOptionalAttribute(tagWriter, "tabindex", getTabindex());
/* 439 */     writeOptionalAttribute(tagWriter, "onclick", getOnclick());
/* 440 */     writeOptionalAttribute(tagWriter, "ondblclick", getOndblclick());
/* 441 */     writeOptionalAttribute(tagWriter, "onmousedown", getOnmousedown());
/* 442 */     writeOptionalAttribute(tagWriter, "onmouseup", getOnmouseup());
/* 443 */     writeOptionalAttribute(tagWriter, "onmouseover", getOnmouseover());
/* 444 */     writeOptionalAttribute(tagWriter, "onmousemove", getOnmousemove());
/* 445 */     writeOptionalAttribute(tagWriter, "onmouseout", getOnmouseout());
/* 446 */     writeOptionalAttribute(tagWriter, "onkeypress", getOnkeypress());
/* 447 */     writeOptionalAttribute(tagWriter, "onkeyup", getOnkeyup());
/* 448 */     writeOptionalAttribute(tagWriter, "onkeydown", getOnkeydown());
/*     */     
/* 450 */     if (!CollectionUtils.isEmpty(this.dynamicAttributes)) {
/* 451 */       for (String attr : this.dynamicAttributes.keySet()) {
/* 452 */         tagWriter.writeOptionalAttributeValue(attr, getDisplayString(this.dynamicAttributes.get(attr)));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String resolveCssClass()
/*     */     throws JspException
/*     */   {
/* 462 */     if ((getBindStatus().isError()) && (StringUtils.hasText(getCssErrorClass()))) {
/* 463 */       return ObjectUtils.getDisplayString(evaluate("cssErrorClass", getCssErrorClass()));
/*     */     }
/*     */     
/* 466 */     return ObjectUtils.getDisplayString(evaluate("cssClass", getCssClass()));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\form\AbstractHtmlElementTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */