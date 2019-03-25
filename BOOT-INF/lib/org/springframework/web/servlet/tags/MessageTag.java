/*     */ package org.springframework.web.servlet.tags;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.JspTagException;
/*     */ import javax.servlet.jsp.JspWriter;
/*     */ import javax.servlet.jsp.PageContext;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.context.MessageSourceResolvable;
/*     */ import org.springframework.context.NoSuchMessageException;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.support.RequestContext;
/*     */ import org.springframework.web.util.JavaScriptUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MessageTag
/*     */   extends HtmlEscapingAwareTag
/*     */   implements ArgumentAware
/*     */ {
/*     */   public static final String DEFAULT_ARGUMENT_SEPARATOR = ",";
/*     */   private MessageSourceResolvable message;
/*     */   private String code;
/*     */   private Object arguments;
/*  73 */   private String argumentSeparator = ",";
/*     */   
/*     */   private List<Object> nestedArguments;
/*     */   
/*     */   private String text;
/*     */   
/*     */   private String var;
/*     */   
/*  81 */   private String scope = "page";
/*     */   
/*  83 */   private boolean javaScriptEscape = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMessage(MessageSourceResolvable message)
/*     */   {
/*  92 */     this.message = message;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setCode(String code)
/*     */   {
/*  99 */     this.code = code;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setArguments(Object arguments)
/*     */   {
/* 108 */     this.arguments = arguments;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setArgumentSeparator(String argumentSeparator)
/*     */   {
/* 117 */     this.argumentSeparator = argumentSeparator;
/*     */   }
/*     */   
/*     */   public void addArgument(Object argument) throws JspTagException
/*     */   {
/* 122 */     this.nestedArguments.add(argument);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setText(String text)
/*     */   {
/* 129 */     this.text = text;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVar(String var)
/*     */   {
/* 139 */     this.var = var;
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
/* 150 */     this.scope = scope;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setJavaScriptEscape(boolean javaScriptEscape)
/*     */     throws JspException
/*     */   {
/* 158 */     this.javaScriptEscape = javaScriptEscape;
/*     */   }
/*     */   
/*     */   protected final int doStartTagInternal()
/*     */     throws JspException, IOException
/*     */   {
/* 164 */     this.nestedArguments = new LinkedList();
/* 165 */     return 1;
/*     */   }
/*     */   
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
/*     */     try
/*     */     {
/* 180 */       String msg = resolveMessage();
/*     */       
/*     */ 
/* 183 */       msg = htmlEscape(msg);
/* 184 */       msg = this.javaScriptEscape ? JavaScriptUtils.javaScriptEscape(msg) : msg;
/*     */       
/*     */ 
/* 187 */       if (this.var != null) {
/* 188 */         this.pageContext.setAttribute(this.var, msg, TagUtils.getScope(this.scope));
/*     */       }
/*     */       else {
/* 191 */         writeMessage(msg);
/*     */       }
/*     */       
/* 194 */       return 6;
/*     */     }
/*     */     catch (IOException ex) {
/* 197 */       throw new JspTagException(ex.getMessage(), ex);
/*     */     }
/*     */     catch (NoSuchMessageException ex) {
/* 200 */       throw new JspTagException(getNoSuchMessageExceptionDescription(ex));
/*     */     }
/*     */   }
/*     */   
/*     */   public void release()
/*     */   {
/* 206 */     super.release();
/* 207 */     this.arguments = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String resolveMessage()
/*     */     throws JspException, NoSuchMessageException
/*     */   {
/* 215 */     MessageSource messageSource = getMessageSource();
/* 216 */     if (messageSource == null) {
/* 217 */       throw new JspTagException("No corresponding MessageSource found");
/*     */     }
/*     */     
/*     */ 
/* 221 */     if (this.message != null)
/*     */     {
/* 223 */       return messageSource.getMessage(this.message, getRequestContext().getLocale());
/*     */     }
/*     */     
/* 226 */     if ((this.code != null) || (this.text != null))
/*     */     {
/* 228 */       Object[] argumentsArray = resolveArguments(this.arguments);
/* 229 */       if (!this.nestedArguments.isEmpty()) {
/* 230 */         argumentsArray = appendArguments(argumentsArray, this.nestedArguments.toArray());
/*     */       }
/*     */       
/* 233 */       if (this.text != null)
/*     */       {
/* 235 */         return messageSource.getMessage(this.code, argumentsArray, this.text, 
/* 236 */           getRequestContext().getLocale());
/*     */       }
/*     */       
/*     */ 
/* 240 */       return messageSource.getMessage(this.code, argumentsArray, 
/* 241 */         getRequestContext().getLocale());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 246 */     return this.text;
/*     */   }
/*     */   
/*     */   private Object[] appendArguments(Object[] sourceArguments, Object[] additionalArguments) {
/* 250 */     if (ObjectUtils.isEmpty(sourceArguments)) {
/* 251 */       return additionalArguments;
/*     */     }
/* 253 */     Object[] arguments = new Object[sourceArguments.length + additionalArguments.length];
/* 254 */     System.arraycopy(sourceArguments, 0, arguments, 0, sourceArguments.length);
/* 255 */     System.arraycopy(additionalArguments, 0, arguments, sourceArguments.length, additionalArguments.length);
/* 256 */     return arguments;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object[] resolveArguments(Object arguments)
/*     */     throws JspException
/*     */   {
/* 267 */     if ((arguments instanceof String))
/*     */     {
/* 269 */       String[] stringArray = StringUtils.delimitedListToStringArray((String)arguments, this.argumentSeparator);
/* 270 */       if (stringArray.length == 1) {
/* 271 */         Object argument = stringArray[0];
/* 272 */         if ((argument != null) && (argument.getClass().isArray())) {
/* 273 */           return ObjectUtils.toObjectArray(argument);
/*     */         }
/*     */         
/* 276 */         return new Object[] { argument };
/*     */       }
/*     */       
/*     */ 
/* 280 */       return stringArray;
/*     */     }
/*     */     
/* 283 */     if ((arguments instanceof Object[])) {
/* 284 */       return (Object[])arguments;
/*     */     }
/* 286 */     if ((arguments instanceof Collection)) {
/* 287 */       return ((Collection)arguments).toArray();
/*     */     }
/* 289 */     if (arguments != null)
/*     */     {
/* 291 */       return new Object[] { arguments };
/*     */     }
/*     */     
/* 294 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void writeMessage(String msg)
/*     */     throws IOException
/*     */   {
/* 305 */     this.pageContext.getOut().write(String.valueOf(msg));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected MessageSource getMessageSource()
/*     */   {
/* 312 */     return getRequestContext().getMessageSource();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getNoSuchMessageExceptionDescription(NoSuchMessageException ex)
/*     */   {
/* 319 */     return ex.getMessage();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\MessageTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */