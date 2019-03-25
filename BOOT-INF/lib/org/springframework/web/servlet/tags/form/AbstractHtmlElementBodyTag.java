/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.tagext.BodyContent;
/*     */ import javax.servlet.jsp.tagext.BodyTag;
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
/*     */ public abstract class AbstractHtmlElementBodyTag
/*     */   extends AbstractHtmlElementTag
/*     */   implements BodyTag
/*     */ {
/*     */   private BodyContent bodyContent;
/*     */   private TagWriter tagWriter;
/*     */   
/*     */   protected int writeTagContent(TagWriter tagWriter)
/*     */     throws JspException
/*     */   {
/*  45 */     onWriteTagContent();
/*  46 */     this.tagWriter = tagWriter;
/*  47 */     if (shouldRender()) {
/*  48 */       exposeAttributes();
/*  49 */       return 2;
/*     */     }
/*     */     
/*  52 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int doEndTag()
/*     */     throws JspException
/*     */   {
/*  64 */     if (shouldRender()) {
/*  65 */       if ((this.bodyContent != null) && (StringUtils.hasText(this.bodyContent.getString()))) {
/*  66 */         renderFromBodyContent(this.bodyContent, this.tagWriter);
/*     */       }
/*     */       else {
/*  69 */         renderDefaultContent(this.tagWriter);
/*     */       }
/*     */     }
/*  72 */     return 6;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void renderFromBodyContent(BodyContent bodyContent, TagWriter tagWriter)
/*     */     throws JspException
/*     */   {
/*  82 */     flushBufferedBodyContent(this.bodyContent);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doFinally()
/*     */   {
/*  90 */     super.doFinally();
/*  91 */     removeAttributes();
/*  92 */     this.tagWriter = null;
/*  93 */     this.bodyContent = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onWriteTagContent() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean shouldRender()
/*     */     throws JspException
/*     */   {
/* 114 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void exposeAttributes()
/*     */     throws JspException
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void removeAttributes() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void flushBufferedBodyContent(BodyContent bodyContent)
/*     */     throws JspException
/*     */   {
/*     */     try
/*     */     {
/* 137 */       bodyContent.writeOut(bodyContent.getEnclosingWriter());
/*     */     }
/*     */     catch (IOException ex) {
/* 140 */       throw new JspException("Unable to write buffered body content.", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract void renderDefaultContent(TagWriter paramTagWriter)
/*     */     throws JspException;
/*     */   
/*     */ 
/*     */ 
/*     */   public void doInitBody()
/*     */     throws JspException
/*     */   {}
/*     */   
/*     */ 
/*     */   public void setBodyContent(BodyContent bodyContent)
/*     */   {
/* 158 */     this.bodyContent = bodyContent;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\form\AbstractHtmlElementBodyTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */