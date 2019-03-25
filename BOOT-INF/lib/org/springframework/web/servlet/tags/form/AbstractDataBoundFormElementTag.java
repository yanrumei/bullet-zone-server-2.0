/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.PageContext;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.support.BindStatus;
/*     */ import org.springframework.web.servlet.support.RequestContext;
/*     */ import org.springframework.web.servlet.support.RequestDataValueProcessor;
/*     */ import org.springframework.web.servlet.tags.EditorAwareTag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractDataBoundFormElementTag
/*     */   extends AbstractFormTag
/*     */   implements EditorAwareTag
/*     */ {
/*     */   protected static final String NESTED_PATH_VARIABLE_NAME = "nestedPath";
/*     */   private String path;
/*     */   private String id;
/*     */   private BindStatus bindStatus;
/*     */   
/*     */   public void setPath(String path)
/*     */   {
/*  75 */     this.path = path;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final String getPath()
/*     */     throws JspException
/*     */   {
/*  83 */     String resolvedPath = (String)evaluate("path", this.path);
/*  84 */     return resolvedPath != null ? resolvedPath : "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setId(String id)
/*     */   {
/*  94 */     this.id = id;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getId()
/*     */   {
/* 102 */     return this.id;
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
/*     */   protected void writeDefaultAttributes(TagWriter tagWriter)
/*     */     throws JspException
/*     */   {
/* 116 */     writeOptionalAttribute(tagWriter, "id", resolveId());
/* 117 */     writeOptionalAttribute(tagWriter, "name", getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String resolveId()
/*     */     throws JspException
/*     */   {
/* 127 */     Object id = evaluate("id", getId());
/* 128 */     if (id != null) {
/* 129 */       String idString = id.toString();
/* 130 */       return StringUtils.hasText(idString) ? idString : null;
/*     */     }
/* 132 */     return autogenerateId();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String autogenerateId()
/*     */     throws JspException
/*     */   {
/* 141 */     return StringUtils.deleteAny(getName(), "[]");
/*     */   }
/*     */   
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
/* 154 */     return getPropertyPath();
/*     */   }
/*     */   
/*     */ 
/*     */   protected BindStatus getBindStatus()
/*     */     throws JspException
/*     */   {
/* 161 */     if (this.bindStatus == null)
/*     */     {
/* 163 */       String nestedPath = getNestedPath();
/* 164 */       String pathToUse = nestedPath != null ? nestedPath + getPath() : getPath();
/* 165 */       if (pathToUse.endsWith(".")) {
/* 166 */         pathToUse = pathToUse.substring(0, pathToUse.length() - 1);
/*     */       }
/* 168 */       this.bindStatus = new BindStatus(getRequestContext(), pathToUse, false);
/*     */     }
/* 170 */     return this.bindStatus;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getNestedPath()
/*     */   {
/* 178 */     return (String)this.pageContext.getAttribute("nestedPath", 2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getPropertyPath()
/*     */     throws JspException
/*     */   {
/* 188 */     String expression = getBindStatus().getExpression();
/* 189 */     return expression != null ? expression : "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final Object getBoundValue()
/*     */     throws JspException
/*     */   {
/* 197 */     return getBindStatus().getValue();
/*     */   }
/*     */   
/*     */ 
/*     */   protected PropertyEditor getPropertyEditor()
/*     */     throws JspException
/*     */   {
/* 204 */     return getBindStatus().getEditor();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final PropertyEditor getEditor()
/*     */     throws JspException
/*     */   {
/* 213 */     return getPropertyEditor();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String convertToDisplayString(Object value)
/*     */     throws JspException
/*     */   {
/* 221 */     PropertyEditor editor = value != null ? getBindStatus().findEditor(value.getClass()) : null;
/* 222 */     return getDisplayString(value, editor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String processFieldValue(String name, String value, String type)
/*     */   {
/* 230 */     RequestDataValueProcessor processor = getRequestContext().getRequestDataValueProcessor();
/* 231 */     ServletRequest request = this.pageContext.getRequest();
/* 232 */     if ((processor != null) && ((request instanceof HttpServletRequest))) {
/* 233 */       value = processor.processFormFieldValue((HttpServletRequest)request, name, value, type);
/*     */     }
/* 235 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doFinally()
/*     */   {
/* 243 */     super.doFinally();
/* 244 */     this.bindStatus = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\form\AbstractDataBoundFormElementTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */