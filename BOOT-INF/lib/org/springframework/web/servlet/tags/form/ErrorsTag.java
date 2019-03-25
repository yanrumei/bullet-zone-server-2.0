/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.PageContext;
/*     */ import javax.servlet.jsp.tagext.BodyTag;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class ErrorsTag
/*     */   extends AbstractHtmlElementBodyTag
/*     */   implements BodyTag
/*     */ {
/*     */   public static final String MESSAGES_ATTRIBUTE = "messages";
/*     */   public static final String SPAN_TAG = "span";
/*  61 */   private String element = "span";
/*     */   
/*  63 */   private String delimiter = "<br/>";
/*     */   
/*     */ 
/*     */ 
/*     */   private Object oldMessages;
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean errorMessagesWereExposed;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setElement(String element)
/*     */   {
/*  78 */     Assert.hasText(element, "'element' cannot be null or blank");
/*  79 */     this.element = element;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getElement()
/*     */   {
/*  86 */     return this.element;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDelimiter(String delimiter)
/*     */   {
/*  94 */     this.delimiter = delimiter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getDelimiter()
/*     */   {
/* 101 */     return this.delimiter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String autogenerateId()
/*     */     throws JspException
/*     */   {
/* 115 */     String path = getPropertyPath();
/* 116 */     if (("".equals(path)) || ("*".equals(path))) {
/* 117 */       path = (String)this.pageContext.getAttribute(FormTag.MODEL_ATTRIBUTE_VARIABLE_NAME, 2);
/*     */     }
/*     */     
/* 120 */     return StringUtils.deleteAny(path, "[]") + ".errors";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getName()
/*     */     throws JspException
/*     */   {
/* 130 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean shouldRender()
/*     */     throws JspException
/*     */   {
/*     */     try
/*     */     {
/* 141 */       return getBindStatus().isError();
/*     */     }
/*     */     catch (IllegalStateException ex) {}
/*     */     
/* 145 */     return false;
/*     */   }
/*     */   
/*     */   protected void renderDefaultContent(TagWriter tagWriter)
/*     */     throws JspException
/*     */   {
/* 151 */     tagWriter.startTag(getElement());
/* 152 */     writeDefaultAttributes(tagWriter);
/* 153 */     String delimiter = ObjectUtils.getDisplayString(evaluate("delimiter", getDelimiter()));
/* 154 */     String[] errorMessages = getBindStatus().getErrorMessages();
/* 155 */     for (int i = 0; i < errorMessages.length; i++) {
/* 156 */       String errorMessage = errorMessages[i];
/* 157 */       if (i > 0) {
/* 158 */         tagWriter.appendValue(delimiter);
/*     */       }
/* 160 */       tagWriter.appendValue(getDisplayString(errorMessage));
/*     */     }
/* 162 */     tagWriter.endTag();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void exposeAttributes()
/*     */     throws JspException
/*     */   {
/* 173 */     List<String> errorMessages = new ArrayList();
/* 174 */     errorMessages.addAll(Arrays.asList(getBindStatus().getErrorMessages()));
/* 175 */     this.oldMessages = this.pageContext.getAttribute("messages", 1);
/* 176 */     this.pageContext.setAttribute("messages", errorMessages, 1);
/* 177 */     this.errorMessagesWereExposed = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void removeAttributes()
/*     */   {
/* 187 */     if (this.errorMessagesWereExposed) {
/* 188 */       if (this.oldMessages != null) {
/* 189 */         this.pageContext.setAttribute("messages", this.oldMessages, 1);
/* 190 */         this.oldMessages = null;
/*     */       }
/*     */       else {
/* 193 */         this.pageContext.removeAttribute("messages", 1);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\form\ErrorsTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */