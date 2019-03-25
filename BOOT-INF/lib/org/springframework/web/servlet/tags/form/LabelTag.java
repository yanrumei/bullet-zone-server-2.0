/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import javax.servlet.jsp.JspException;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class LabelTag
/*     */   extends AbstractHtmlElementTag
/*     */ {
/*     */   private static final String LABEL_TAG = "label";
/*     */   private static final String FOR_ATTRIBUTE = "for";
/*     */   private TagWriter tagWriter;
/*     */   private String forId;
/*     */   
/*     */   public void setFor(String forId)
/*     */   {
/*  67 */     Assert.notNull(forId, "'forId' must not be null");
/*  68 */     this.forId = forId;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFor()
/*     */   {
/*  76 */     return this.forId;
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
/*  87 */     tagWriter.startTag("label");
/*  88 */     tagWriter.writeAttribute("for", resolveFor());
/*  89 */     writeDefaultAttributes(tagWriter);
/*  90 */     tagWriter.forceBlock();
/*  91 */     this.tagWriter = tagWriter;
/*  92 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getName()
/*     */     throws JspException
/*     */   {
/* 104 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String resolveFor()
/*     */     throws JspException
/*     */   {
/* 114 */     if (StringUtils.hasText(this.forId)) {
/* 115 */       return getDisplayString(evaluate("for", this.forId));
/*     */     }
/*     */     
/* 118 */     return autogenerateFor();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String autogenerateFor()
/*     */     throws JspException
/*     */   {
/* 128 */     return StringUtils.deleteAny(getPropertyPath(), "[]");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int doEndTag()
/*     */     throws JspException
/*     */   {
/* 136 */     this.tagWriter.endTag();
/* 137 */     return 6;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doFinally()
/*     */   {
/* 145 */     super.doFinally();
/* 146 */     this.tagWriter = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\form\LabelTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */