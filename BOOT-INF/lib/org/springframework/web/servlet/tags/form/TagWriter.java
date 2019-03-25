/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.Stack;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.PageContext;
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
/*     */ public class TagWriter
/*     */ {
/*     */   private final SafeWriter writer;
/*  47 */   private final Stack<TagStateEntry> tagState = new Stack();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TagWriter(PageContext pageContext)
/*     */   {
/*  56 */     Assert.notNull(pageContext, "PageContext must not be null");
/*  57 */     this.writer = new SafeWriter(pageContext);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TagWriter(Writer writer)
/*     */   {
/*  66 */     Assert.notNull(writer, "Writer must not be null");
/*  67 */     this.writer = new SafeWriter(writer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startTag(String tagName)
/*     */     throws JspException
/*     */   {
/*  77 */     if (inTag()) {
/*  78 */       closeTagAndMarkAsBlock();
/*     */     }
/*  80 */     push(tagName);
/*  81 */     this.writer.append("<").append(tagName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeAttribute(String attributeName, String attributeValue)
/*     */     throws JspException
/*     */   {
/*  91 */     if (currentState().isBlockTag()) {
/*  92 */       throw new IllegalStateException("Cannot write attributes after opening tag is closed.");
/*     */     }
/*     */     
/*  95 */     this.writer.append(" ").append(attributeName).append("=\"").append(attributeValue).append("\"");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeOptionalAttributeValue(String attributeName, String attributeValue)
/*     */     throws JspException
/*     */   {
/* 104 */     if (StringUtils.hasText(attributeValue)) {
/* 105 */       writeAttribute(attributeName, attributeValue);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void appendValue(String value)
/*     */     throws JspException
/*     */   {
/* 115 */     if (!inTag()) {
/* 116 */       throw new IllegalStateException("Cannot write tag value. No open tag available.");
/*     */     }
/* 118 */     closeTagAndMarkAsBlock();
/* 119 */     this.writer.append(value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void forceBlock()
/*     */     throws JspException
/*     */   {
/* 130 */     if (currentState().isBlockTag()) {
/* 131 */       return;
/*     */     }
/* 133 */     closeTagAndMarkAsBlock();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void endTag()
/*     */     throws JspException
/*     */   {
/* 142 */     endTag(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void endTag(boolean enforceClosingTag)
/*     */     throws JspException
/*     */   {
/* 153 */     if (!inTag()) {
/* 154 */       throw new IllegalStateException("Cannot write end of tag. No open tag available.");
/*     */     }
/* 156 */     boolean renderClosingTag = true;
/* 157 */     if (!currentState().isBlockTag())
/*     */     {
/* 159 */       if (enforceClosingTag) {
/* 160 */         this.writer.append(">");
/*     */       }
/*     */       else {
/* 163 */         this.writer.append("/>");
/* 164 */         renderClosingTag = false;
/*     */       }
/*     */     }
/* 167 */     if (renderClosingTag) {
/* 168 */       this.writer.append("</").append(currentState().getTagName()).append(">");
/*     */     }
/* 170 */     this.tagState.pop();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void push(String tagName)
/*     */   {
/* 178 */     this.tagState.push(new TagStateEntry(tagName));
/*     */   }
/*     */   
/*     */ 
/*     */   private void closeTagAndMarkAsBlock()
/*     */     throws JspException
/*     */   {
/* 185 */     if (!currentState().isBlockTag()) {
/* 186 */       currentState().markAsBlockTag();
/* 187 */       this.writer.append(">");
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean inTag() {
/* 192 */     return this.tagState.size() > 0;
/*     */   }
/*     */   
/*     */   private TagStateEntry currentState() {
/* 196 */     return (TagStateEntry)this.tagState.peek();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class TagStateEntry
/*     */   {
/*     */     private final String tagName;
/*     */     
/*     */     private boolean blockTag;
/*     */     
/*     */ 
/*     */     public TagStateEntry(String tagName)
/*     */     {
/* 210 */       this.tagName = tagName;
/*     */     }
/*     */     
/*     */     public String getTagName() {
/* 214 */       return this.tagName;
/*     */     }
/*     */     
/*     */     public void markAsBlockTag() {
/* 218 */       this.blockTag = true;
/*     */     }
/*     */     
/*     */     public boolean isBlockTag() {
/* 222 */       return this.blockTag;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final class SafeWriter
/*     */   {
/*     */     private PageContext pageContext;
/*     */     
/*     */ 
/*     */     private Writer writer;
/*     */     
/*     */ 
/*     */     public SafeWriter(PageContext pageContext)
/*     */     {
/* 238 */       this.pageContext = pageContext;
/*     */     }
/*     */     
/*     */     public SafeWriter(Writer writer) {
/* 242 */       this.writer = writer;
/*     */     }
/*     */     
/*     */     public SafeWriter append(String value) throws JspException {
/*     */       try {
/* 247 */         getWriterToUse().write(String.valueOf(value));
/* 248 */         return this;
/*     */       }
/*     */       catch (IOException ex) {
/* 251 */         throw new JspException("Unable to write to JspWriter", ex);
/*     */       }
/*     */     }
/*     */     
/*     */     private Writer getWriterToUse() {
/* 256 */       return this.pageContext != null ? this.pageContext.getOut() : this.writer;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\form\TagWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */