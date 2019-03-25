/*     */ package org.springframework.web.servlet.tags;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.io.IOException;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.JspWriter;
/*     */ import javax.servlet.jsp.PageContext;
/*     */ import javax.servlet.jsp.tagext.TagSupport;
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
/*     */ public class TransformTag
/*     */   extends HtmlEscapingAwareTag
/*     */ {
/*     */   private Object value;
/*     */   private String var;
/*  50 */   private String scope = "page";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValue(Object value)
/*     */   {
/*  61 */     this.value = value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVar(String var)
/*     */   {
/*  71 */     this.var = var;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setScope(String scope)
/*     */   {
/*  82 */     this.scope = scope;
/*     */   }
/*     */   
/*     */   protected final int doStartTagInternal()
/*     */     throws JspException
/*     */   {
/*  88 */     if (this.value != null)
/*     */     {
/*  90 */       EditorAwareTag tag = (EditorAwareTag)TagSupport.findAncestorWithClass(this, EditorAwareTag.class);
/*  91 */       if (tag == null) {
/*  92 */         throw new JspException("TransformTag can only be used within EditorAwareTag (e.g. BindTag)");
/*     */       }
/*     */       
/*     */ 
/*  96 */       String result = null;
/*  97 */       PropertyEditor editor = tag.getEditor();
/*  98 */       if (editor != null)
/*     */       {
/* 100 */         editor.setValue(this.value);
/* 101 */         result = editor.getAsText();
/*     */       }
/*     */       else
/*     */       {
/* 105 */         result = this.value.toString();
/*     */       }
/* 107 */       result = htmlEscape(result);
/* 108 */       if (this.var != null) {
/* 109 */         this.pageContext.setAttribute(this.var, result, TagUtils.getScope(this.scope));
/*     */       }
/*     */       else {
/*     */         try
/*     */         {
/* 114 */           this.pageContext.getOut().print(result);
/*     */         }
/*     */         catch (IOException ex) {
/* 117 */           throw new JspException(ex);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 122 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\TransformTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */