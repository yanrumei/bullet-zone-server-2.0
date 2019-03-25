/*     */ package org.springframework.web.servlet.tags;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.JspWriter;
/*     */ import javax.servlet.jsp.PageContext;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.support.RequestContext;
/*     */ import org.springframework.web.servlet.support.RequestDataValueProcessor;
/*     */ import org.springframework.web.util.JavaScriptUtils;
/*     */ import org.springframework.web.util.TagUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UrlTag
/*     */   extends HtmlEscapingAwareTag
/*     */   implements ParamAware
/*     */ {
/*     */   private static final String URL_TEMPLATE_DELIMITER_PREFIX = "{";
/*     */   private static final String URL_TEMPLATE_DELIMITER_SUFFIX = "}";
/*     */   private static final String URL_TYPE_ABSOLUTE = "://";
/*     */   private List<Param> params;
/*     */   private Set<String> templateParams;
/*     */   private UrlType type;
/*     */   private String value;
/*     */   private String context;
/*     */   private String var;
/*  97 */   private int scope = 1;
/*     */   
/*  99 */   private boolean javaScriptEscape = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValue(String value)
/*     */   {
/* 106 */     if (value.contains("://")) {
/* 107 */       this.type = UrlType.ABSOLUTE;
/* 108 */       this.value = value;
/*     */     }
/* 110 */     else if (value.startsWith("/")) {
/* 111 */       this.type = UrlType.CONTEXT_RELATIVE;
/* 112 */       this.value = value;
/*     */     }
/*     */     else {
/* 115 */       this.type = UrlType.RELATIVE;
/* 116 */       this.value = value;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setContext(String context)
/*     */   {
/* 124 */     if (context.startsWith("/")) {
/* 125 */       this.context = context;
/*     */     }
/*     */     else {
/* 128 */       this.context = ("/" + context);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVar(String var)
/*     */   {
/* 137 */     this.var = var;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setScope(String scope)
/*     */   {
/* 145 */     this.scope = TagUtils.getScope(scope);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setJavaScriptEscape(boolean javaScriptEscape)
/*     */     throws JspException
/*     */   {
/* 153 */     this.javaScriptEscape = javaScriptEscape;
/*     */   }
/*     */   
/*     */   public void addParam(Param param)
/*     */   {
/* 158 */     this.params.add(param);
/*     */   }
/*     */   
/*     */   public int doStartTagInternal()
/*     */     throws JspException
/*     */   {
/* 164 */     this.params = new LinkedList();
/* 165 */     this.templateParams = new HashSet();
/* 166 */     return 1;
/*     */   }
/*     */   
/*     */   public int doEndTag() throws JspException
/*     */   {
/* 171 */     String url = createUrl();
/*     */     
/* 173 */     RequestDataValueProcessor processor = getRequestContext().getRequestDataValueProcessor();
/* 174 */     ServletRequest request = this.pageContext.getRequest();
/* 175 */     if ((processor != null) && ((request instanceof HttpServletRequest))) {
/* 176 */       url = processor.processUrl((HttpServletRequest)request, url);
/*     */     }
/*     */     
/* 179 */     if (this.var == null) {
/*     */       try
/*     */       {
/* 182 */         this.pageContext.getOut().print(url);
/*     */       }
/*     */       catch (IOException ex) {
/* 185 */         throw new JspException(ex);
/*     */       }
/*     */       
/*     */     }
/*     */     else {
/* 190 */       this.pageContext.setAttribute(this.var, url, this.scope);
/*     */     }
/* 192 */     return 6;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   String createUrl()
/*     */     throws JspException
/*     */   {
/* 201 */     HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
/* 202 */     HttpServletResponse response = (HttpServletResponse)this.pageContext.getResponse();
/*     */     
/* 204 */     StringBuilder url = new StringBuilder();
/* 205 */     if (this.type == UrlType.CONTEXT_RELATIVE)
/*     */     {
/* 207 */       if (this.context == null) {
/* 208 */         url.append(request.getContextPath());
/*     */ 
/*     */       }
/* 211 */       else if (this.context.endsWith("/")) {
/* 212 */         url.append(this.context.substring(0, this.context.length() - 1));
/*     */       }
/*     */       else {
/* 215 */         url.append(this.context);
/*     */       }
/*     */     }
/*     */     
/* 219 */     if ((this.type != UrlType.RELATIVE) && (this.type != UrlType.ABSOLUTE) && (!this.value.startsWith("/"))) {
/* 220 */       url.append("/");
/*     */     }
/* 222 */     url.append(replaceUriTemplateParams(this.value, this.params, this.templateParams));
/* 223 */     url.append(createQueryString(this.params, this.templateParams, url.indexOf("?") == -1));
/*     */     
/* 225 */     String urlStr = url.toString();
/* 226 */     if (this.type != UrlType.ABSOLUTE)
/*     */     {
/*     */ 
/* 229 */       urlStr = response.encodeURL(urlStr);
/*     */     }
/*     */     
/*     */ 
/* 233 */     urlStr = htmlEscape(urlStr);
/* 234 */     urlStr = this.javaScriptEscape ? JavaScriptUtils.javaScriptEscape(urlStr) : urlStr;
/*     */     
/* 236 */     return urlStr;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String createQueryString(List<Param> params, Set<String> usedParams, boolean includeQueryStringDelimiter)
/*     */     throws JspException
/*     */   {
/* 253 */     String encoding = this.pageContext.getResponse().getCharacterEncoding();
/* 254 */     StringBuilder qs = new StringBuilder();
/* 255 */     for (Param param : params) {
/* 256 */       if ((!usedParams.contains(param.getName())) && (StringUtils.hasLength(param.getName()))) {
/* 257 */         if ((includeQueryStringDelimiter) && (qs.length() == 0)) {
/* 258 */           qs.append("?");
/*     */         }
/*     */         else {
/* 261 */           qs.append("&");
/*     */         }
/*     */         try {
/* 264 */           qs.append(UriUtils.encodeQueryParam(param.getName(), encoding));
/* 265 */           if (param.getValue() != null) {
/* 266 */             qs.append("=");
/* 267 */             qs.append(UriUtils.encodeQueryParam(param.getValue(), encoding));
/*     */           }
/*     */         }
/*     */         catch (UnsupportedEncodingException ex) {
/* 271 */           throw new JspException(ex);
/*     */         }
/*     */       }
/*     */     }
/* 275 */     return qs.toString();
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
/*     */ 
/*     */   protected String replaceUriTemplateParams(String uri, List<Param> params, Set<String> usedParams)
/*     */     throws JspException
/*     */   {
/* 290 */     String encoding = this.pageContext.getResponse().getCharacterEncoding();
/* 291 */     for (Param param : params) {
/* 292 */       String template = "{" + param.getName() + "}";
/* 293 */       if (uri.contains(template)) {
/* 294 */         usedParams.add(param.getName());
/*     */         try {
/* 296 */           uri = uri.replace(template, UriUtils.encodePath(param.getValue(), encoding));
/*     */         }
/*     */         catch (UnsupportedEncodingException ex) {
/* 299 */           throw new JspException(ex);
/*     */         }
/*     */       }
/*     */       else {
/* 303 */         template = "{/" + param.getName() + "}";
/* 304 */         if (uri.contains(template)) {
/* 305 */           usedParams.add(param.getName());
/*     */           try {
/* 307 */             uri = uri.replace(template, UriUtils.encodePathSegment(param.getValue(), encoding));
/*     */           }
/*     */           catch (UnsupportedEncodingException ex) {
/* 310 */             throw new JspException(ex);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 315 */     return uri;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static enum UrlType
/*     */   {
/* 324 */     CONTEXT_RELATIVE,  RELATIVE,  ABSOLUTE;
/*     */     
/*     */     private UrlType() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\UrlTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */