/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.PageContext;
/*     */ import org.springframework.core.Conventions;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.support.RequestContext;
/*     */ import org.springframework.web.servlet.support.RequestDataValueProcessor;
/*     */ import org.springframework.web.util.HtmlUtils;
/*     */ import org.springframework.web.util.UriUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FormTag
/*     */   extends AbstractHtmlElementTag
/*     */ {
/*     */   private static final String DEFAULT_METHOD = "post";
/*     */   public static final String DEFAULT_COMMAND_NAME = "command";
/*     */   private static final String MODEL_ATTRIBUTE = "modelAttribute";
/*  69 */   public static final String MODEL_ATTRIBUTE_VARIABLE_NAME = Conventions.getQualifiedAttributeName(AbstractFormTag.class, "modelAttribute");
/*     */   
/*     */ 
/*     */   private static final String DEFAULT_METHOD_PARAM = "_method";
/*     */   
/*     */ 
/*     */   private static final String FORM_TAG = "form";
/*     */   
/*     */   private static final String INPUT_TAG = "input";
/*     */   
/*     */   private static final String ACTION_ATTRIBUTE = "action";
/*     */   
/*     */   private static final String METHOD_ATTRIBUTE = "method";
/*     */   
/*     */   private static final String TARGET_ATTRIBUTE = "target";
/*     */   
/*     */   private static final String ENCTYPE_ATTRIBUTE = "enctype";
/*     */   
/*     */   private static final String ACCEPT_CHARSET_ATTRIBUTE = "accept-charset";
/*     */   
/*     */   private static final String ONSUBMIT_ATTRIBUTE = "onsubmit";
/*     */   
/*     */   private static final String ONRESET_ATTRIBUTE = "onreset";
/*     */   
/*     */   private static final String AUTOCOMPLETE_ATTRIBUTE = "autocomplete";
/*     */   
/*     */   private static final String NAME_ATTRIBUTE = "name";
/*     */   
/*     */   private static final String VALUE_ATTRIBUTE = "value";
/*     */   
/*     */   private static final String TYPE_ATTRIBUTE = "type";
/*     */   
/*     */   private TagWriter tagWriter;
/*     */   
/* 103 */   private String modelAttribute = "command";
/*     */   
/*     */   private String name;
/*     */   
/*     */   private String action;
/*     */   
/*     */   private String servletRelativeAction;
/*     */   
/* 111 */   private String method = "post";
/*     */   
/*     */   private String target;
/*     */   
/*     */   private String enctype;
/*     */   
/*     */   private String acceptCharset;
/*     */   
/*     */   private String onsubmit;
/*     */   
/*     */   private String onreset;
/*     */   
/*     */   private String autocomplete;
/*     */   
/* 125 */   private String methodParam = "_method";
/*     */   
/*     */ 
/*     */ 
/*     */   private String previousNestedPath;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setModelAttribute(String modelAttribute)
/*     */   {
/* 136 */     this.modelAttribute = modelAttribute;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getModelAttribute()
/*     */   {
/* 143 */     return this.modelAttribute;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void setCommandName(String commandName)
/*     */   {
/* 154 */     this.modelAttribute = commandName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected String getCommandName()
/*     */   {
/* 164 */     return this.modelAttribute;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setName(String name)
/*     */   {
/* 174 */     this.name = name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getName()
/*     */     throws JspException
/*     */   {
/* 182 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAction(String action)
/*     */   {
/* 190 */     this.action = (action != null ? action : "");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getAction()
/*     */   {
/* 197 */     return this.action;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setServletRelativeAction(String servletRelativeAction)
/*     */   {
/* 207 */     this.servletRelativeAction = (servletRelativeAction != null ? servletRelativeAction : "");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getServletRelativeAction()
/*     */   {
/* 215 */     return this.servletRelativeAction;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMethod(String method)
/*     */   {
/* 223 */     this.method = method;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getMethod()
/*     */   {
/* 230 */     return this.method;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTarget(String target)
/*     */   {
/* 238 */     this.target = target;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getTarget()
/*     */   {
/* 245 */     return this.target;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEnctype(String enctype)
/*     */   {
/* 253 */     this.enctype = enctype;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getEnctype()
/*     */   {
/* 260 */     return this.enctype;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAcceptCharset(String acceptCharset)
/*     */   {
/* 268 */     this.acceptCharset = acceptCharset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getAcceptCharset()
/*     */   {
/* 275 */     return this.acceptCharset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOnsubmit(String onsubmit)
/*     */   {
/* 283 */     this.onsubmit = onsubmit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getOnsubmit()
/*     */   {
/* 290 */     return this.onsubmit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOnreset(String onreset)
/*     */   {
/* 298 */     this.onreset = onreset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getOnreset()
/*     */   {
/* 305 */     return this.onreset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAutocomplete(String autocomplete)
/*     */   {
/* 313 */     this.autocomplete = autocomplete;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getAutocomplete()
/*     */   {
/* 320 */     return this.autocomplete;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setMethodParam(String methodParam)
/*     */   {
/* 327 */     this.methodParam = methodParam;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getMethodParam()
/*     */   {
/* 336 */     return getMethodParameter();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected String getMethodParameter()
/*     */   {
/* 346 */     return this.methodParam;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean isMethodBrowserSupported(String method)
/*     */   {
/* 353 */     return ("get".equalsIgnoreCase(method)) || ("post".equalsIgnoreCase(method));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int writeTagContent(TagWriter tagWriter)
/*     */     throws JspException
/*     */   {
/* 365 */     this.tagWriter = tagWriter;
/*     */     
/* 367 */     tagWriter.startTag("form");
/* 368 */     writeDefaultAttributes(tagWriter);
/* 369 */     tagWriter.writeAttribute("action", resolveAction());
/* 370 */     writeOptionalAttribute(tagWriter, "method", getHttpMethod());
/* 371 */     writeOptionalAttribute(tagWriter, "target", getTarget());
/* 372 */     writeOptionalAttribute(tagWriter, "enctype", getEnctype());
/* 373 */     writeOptionalAttribute(tagWriter, "accept-charset", getAcceptCharset());
/* 374 */     writeOptionalAttribute(tagWriter, "onsubmit", getOnsubmit());
/* 375 */     writeOptionalAttribute(tagWriter, "onreset", getOnreset());
/* 376 */     writeOptionalAttribute(tagWriter, "autocomplete", getAutocomplete());
/*     */     
/* 378 */     tagWriter.forceBlock();
/*     */     
/* 380 */     if (!isMethodBrowserSupported(getMethod())) {
/* 381 */       assertHttpMethod(getMethod());
/* 382 */       String inputName = getMethodParam();
/* 383 */       String inputType = "hidden";
/* 384 */       tagWriter.startTag("input");
/* 385 */       writeOptionalAttribute(tagWriter, "type", inputType);
/* 386 */       writeOptionalAttribute(tagWriter, "name", inputName);
/* 387 */       writeOptionalAttribute(tagWriter, "value", processFieldValue(inputName, getMethod(), inputType));
/* 388 */       tagWriter.endTag();
/*     */     }
/*     */     
/*     */ 
/* 392 */     String modelAttribute = resolveModelAttribute();
/* 393 */     this.pageContext.setAttribute(MODEL_ATTRIBUTE_VARIABLE_NAME, modelAttribute, 2);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 398 */     this.previousNestedPath = ((String)this.pageContext.getAttribute("nestedPath", 2));
/* 399 */     this.pageContext.setAttribute("nestedPath", modelAttribute + ".", 2);
/*     */     
/*     */ 
/* 402 */     return 1;
/*     */   }
/*     */   
/*     */   private String getHttpMethod() {
/* 406 */     return isMethodBrowserSupported(getMethod()) ? getMethod() : "post";
/*     */   }
/*     */   
/*     */   private void assertHttpMethod(String method) {
/* 410 */     for (HttpMethod httpMethod : ) {
/* 411 */       if (httpMethod.name().equalsIgnoreCase(method)) {
/* 412 */         return;
/*     */       }
/*     */     }
/* 415 */     throw new IllegalArgumentException("Invalid HTTP method: " + method);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String autogenerateId()
/*     */     throws JspException
/*     */   {
/* 423 */     return resolveModelAttribute();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String resolveModelAttribute()
/*     */     throws JspException
/*     */   {
/* 431 */     Object resolvedModelAttribute = evaluate("modelAttribute", getModelAttribute());
/* 432 */     if (resolvedModelAttribute == null) {
/* 433 */       throw new IllegalArgumentException("modelAttribute must not be null");
/*     */     }
/* 435 */     return (String)resolvedModelAttribute;
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
/*     */   protected String resolveAction()
/*     */     throws JspException
/*     */   {
/* 449 */     String action = getAction();
/* 450 */     String servletRelativeAction = getServletRelativeAction();
/* 451 */     if (StringUtils.hasText(action)) {
/* 452 */       action = getDisplayString(evaluate("action", action));
/* 453 */       return processAction(action);
/*     */     }
/* 455 */     if (StringUtils.hasText(servletRelativeAction)) {
/* 456 */       String pathToServlet = getRequestContext().getPathToServlet();
/* 457 */       if ((servletRelativeAction.startsWith("/")) && 
/* 458 */         (!servletRelativeAction.startsWith(getRequestContext().getContextPath()))) {
/* 459 */         servletRelativeAction = pathToServlet + servletRelativeAction;
/*     */       }
/* 461 */       servletRelativeAction = getDisplayString(evaluate("action", servletRelativeAction));
/* 462 */       return processAction(servletRelativeAction);
/*     */     }
/*     */     
/* 465 */     String requestUri = getRequestContext().getRequestUri();
/* 466 */     String encoding = this.pageContext.getResponse().getCharacterEncoding();
/*     */     try {
/* 468 */       requestUri = UriUtils.encodePath(requestUri, encoding);
/*     */     }
/*     */     catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
/*     */     
/*     */ 
/* 473 */     ServletResponse response = this.pageContext.getResponse();
/* 474 */     if ((response instanceof HttpServletResponse)) {
/* 475 */       requestUri = ((HttpServletResponse)response).encodeURL(requestUri);
/* 476 */       String queryString = getRequestContext().getQueryString();
/* 477 */       if (StringUtils.hasText(queryString)) {
/* 478 */         requestUri = requestUri + "?" + HtmlUtils.htmlEscape(queryString);
/*     */       }
/*     */     }
/* 481 */     if (StringUtils.hasText(requestUri)) {
/* 482 */       return processAction(requestUri);
/*     */     }
/*     */     
/* 485 */     throw new IllegalArgumentException("Attribute 'action' is required. Attempted to resolve against current request URI but request URI was null.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String processAction(String action)
/*     */   {
/* 496 */     RequestDataValueProcessor processor = getRequestContext().getRequestDataValueProcessor();
/* 497 */     ServletRequest request = this.pageContext.getRequest();
/* 498 */     if ((processor != null) && ((request instanceof HttpServletRequest))) {
/* 499 */       action = processor.processAction((HttpServletRequest)request, action, getHttpMethod());
/*     */     }
/* 501 */     return action;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int doEndTag()
/*     */     throws JspException
/*     */   {
/* 510 */     RequestDataValueProcessor processor = getRequestContext().getRequestDataValueProcessor();
/* 511 */     ServletRequest request = this.pageContext.getRequest();
/* 512 */     if ((processor != null) && ((request instanceof HttpServletRequest))) {
/* 513 */       writeHiddenFields(processor.getExtraHiddenFields((HttpServletRequest)request));
/*     */     }
/* 515 */     this.tagWriter.endTag();
/* 516 */     return 6;
/*     */   }
/*     */   
/*     */ 
/*     */   private void writeHiddenFields(Map<String, String> hiddenFields)
/*     */     throws JspException
/*     */   {
/* 523 */     if (hiddenFields != null) {
/* 524 */       this.tagWriter.appendValue("<div>\n");
/* 525 */       for (String name : hiddenFields.keySet()) {
/* 526 */         this.tagWriter.appendValue("<input type=\"hidden\" ");
/* 527 */         this.tagWriter.appendValue("name=\"" + name + "\" value=\"" + (String)hiddenFields.get(name) + "\" ");
/* 528 */         this.tagWriter.appendValue("/>\n");
/*     */       }
/* 530 */       this.tagWriter.appendValue("</div>");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doFinally()
/*     */   {
/* 539 */     super.doFinally();
/* 540 */     this.pageContext.removeAttribute(MODEL_ATTRIBUTE_VARIABLE_NAME, 2);
/* 541 */     if (this.previousNestedPath != null)
/*     */     {
/* 543 */       this.pageContext.setAttribute("nestedPath", this.previousNestedPath, 2);
/*     */     }
/*     */     else
/*     */     {
/* 547 */       this.pageContext.removeAttribute("nestedPath", 2);
/*     */     }
/* 549 */     this.tagWriter = null;
/* 550 */     this.previousNestedPath = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String resolveCssClass()
/*     */     throws JspException
/*     */   {
/* 559 */     return ObjectUtils.getDisplayString(evaluate("cssClass", getCssClass()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPath(String path)
/*     */   {
/* 568 */     throw new UnsupportedOperationException("The 'path' attribute is not supported for forms");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCssErrorClass(String cssErrorClass)
/*     */   {
/* 577 */     throw new UnsupportedOperationException("The 'cssErrorClass' attribute is not supported for forms");
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\form\FormTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */