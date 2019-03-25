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
/*     */ public class TextareaTag
/*     */   extends AbstractHtmlInputElementTag
/*     */ {
/*     */   public static final String ROWS_ATTRIBUTE = "rows";
/*     */   public static final String COLS_ATTRIBUTE = "cols";
/*     */   public static final String ONSELECT_ATTRIBUTE = "onselect";
/*     */   public static final String READONLY_ATTRIBUTE = "readonly";
/*     */   private String rows;
/*     */   private String cols;
/*     */   private String onselect;
/*     */   
/*     */   public void setRows(String rows)
/*     */   {
/*  52 */     this.rows = rows;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getRows()
/*     */   {
/*  59 */     return this.rows;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCols(String cols)
/*     */   {
/*  67 */     this.cols = cols;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getCols()
/*     */   {
/*  74 */     return this.cols;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOnselect(String onselect)
/*     */   {
/*  82 */     this.onselect = onselect;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getOnselect()
/*     */   {
/*  89 */     return this.onselect;
/*     */   }
/*     */   
/*     */   protected int writeTagContent(TagWriter tagWriter)
/*     */     throws JspException
/*     */   {
/*  95 */     tagWriter.startTag("textarea");
/*  96 */     writeDefaultAttributes(tagWriter);
/*  97 */     writeOptionalAttribute(tagWriter, "rows", getRows());
/*  98 */     writeOptionalAttribute(tagWriter, "cols", getCols());
/*  99 */     writeOptionalAttribute(tagWriter, "onselect", getOnselect());
/* 100 */     String value = getDisplayString(getBoundValue(), getPropertyEditor());
/* 101 */     tagWriter.appendValue("\r\n" + processFieldValue(getName(), value, "textarea"));
/* 102 */     tagWriter.endTag();
/* 103 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\form\TextareaTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */