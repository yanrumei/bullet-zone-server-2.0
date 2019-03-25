/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.reflect.Array;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ import org.springframework.web.servlet.FlashMap;
/*     */ import org.springframework.web.servlet.FlashMapManager;
/*     */ import org.springframework.web.servlet.HandlerMapping;
/*     */ import org.springframework.web.servlet.SmartView;
/*     */ import org.springframework.web.servlet.View;
/*     */ import org.springframework.web.servlet.support.RequestContextUtils;
/*     */ import org.springframework.web.servlet.support.RequestDataValueProcessor;
/*     */ import org.springframework.web.util.UriComponents;
/*     */ import org.springframework.web.util.UriComponentsBuilder;
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
/*     */ public class RedirectView
/*     */   extends AbstractUrlBasedView
/*     */   implements SmartView
/*     */ {
/*  90 */   private static final Pattern URI_TEMPLATE_VARIABLE_PATTERN = Pattern.compile("\\{([^/]+?)\\}");
/*     */   
/*     */ 
/*  93 */   private boolean contextRelative = false;
/*     */   
/*  95 */   private boolean http10Compatible = true;
/*     */   
/*  97 */   private boolean exposeModelAttributes = true;
/*     */   
/*     */   private String encodingScheme;
/*     */   
/*     */   private HttpStatus statusCode;
/*     */   
/* 103 */   private boolean expandUriTemplateVariables = true;
/*     */   
/* 105 */   private boolean propagateQueryParams = false;
/*     */   
/*     */ 
/*     */   private String[] hosts;
/*     */   
/*     */ 
/*     */ 
/*     */   public RedirectView()
/*     */   {
/* 114 */     setExposePathVariables(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RedirectView(String url)
/*     */   {
/* 125 */     super(url);
/* 126 */     setExposePathVariables(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RedirectView(String url, boolean contextRelative)
/*     */   {
/* 136 */     super(url);
/* 137 */     this.contextRelative = contextRelative;
/* 138 */     setExposePathVariables(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RedirectView(String url, boolean contextRelative, boolean http10Compatible)
/*     */   {
/* 149 */     super(url);
/* 150 */     this.contextRelative = contextRelative;
/* 151 */     this.http10Compatible = http10Compatible;
/* 152 */     setExposePathVariables(false);
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
/*     */   public RedirectView(String url, boolean contextRelative, boolean http10Compatible, boolean exposeModelAttributes)
/*     */   {
/* 165 */     super(url);
/* 166 */     this.contextRelative = contextRelative;
/* 167 */     this.http10Compatible = http10Compatible;
/* 168 */     this.exposeModelAttributes = exposeModelAttributes;
/* 169 */     setExposePathVariables(false);
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
/*     */   public void setContextRelative(boolean contextRelative)
/*     */   {
/* 183 */     this.contextRelative = contextRelative;
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
/*     */   public void setHttp10Compatible(boolean http10Compatible)
/*     */   {
/* 198 */     this.http10Compatible = http10Compatible;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExposeModelAttributes(boolean exposeModelAttributes)
/*     */   {
/* 207 */     this.exposeModelAttributes = exposeModelAttributes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEncodingScheme(String encodingScheme)
/*     */   {
/* 216 */     this.encodingScheme = encodingScheme;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStatusCode(HttpStatus statusCode)
/*     */   {
/* 225 */     this.statusCode = statusCode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExpandUriTemplateVariables(boolean expandUriTemplateVariables)
/*     */   {
/* 236 */     this.expandUriTemplateVariables = expandUriTemplateVariables;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPropagateQueryParams(boolean propagateQueryParams)
/*     */   {
/* 246 */     this.propagateQueryParams = propagateQueryParams;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isPropagateQueryProperties()
/*     */   {
/* 254 */     return this.propagateQueryParams;
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
/*     */   public void setHosts(String... hosts)
/*     */   {
/* 268 */     this.hosts = hosts;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getHosts()
/*     */   {
/* 276 */     return this.hosts;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRedirectView()
/*     */   {
/* 284 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isContextRequired()
/*     */   {
/* 292 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*     */     throws IOException
/*     */   {
/* 305 */     String targetUrl = createTargetUrl(model, request);
/* 306 */     targetUrl = updateTargetUrl(targetUrl, model, request, response);
/*     */     
/* 308 */     FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
/* 309 */     if (!CollectionUtils.isEmpty(flashMap)) {
/* 310 */       UriComponents uriComponents = UriComponentsBuilder.fromUriString(targetUrl).build();
/* 311 */       flashMap.setTargetRequestPath(uriComponents.getPath());
/* 312 */       flashMap.addTargetRequestParams(uriComponents.getQueryParams());
/* 313 */       FlashMapManager flashMapManager = RequestContextUtils.getFlashMapManager(request);
/* 314 */       if (flashMapManager == null) {
/* 315 */         throw new IllegalStateException("FlashMapManager not found despite output FlashMap having been set");
/*     */       }
/* 317 */       flashMapManager.saveOutputFlashMap(flashMap, request, response);
/*     */     }
/*     */     
/* 320 */     sendRedirect(request, response, targetUrl, this.http10Compatible);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String createTargetUrl(Map<String, Object> model, HttpServletRequest request)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 332 */     StringBuilder targetUrl = new StringBuilder();
/* 333 */     if ((this.contextRelative) && (getUrl().startsWith("/")))
/*     */     {
/* 335 */       targetUrl.append(request.getContextPath());
/*     */     }
/* 337 */     targetUrl.append(getUrl());
/*     */     
/* 339 */     String enc = this.encodingScheme;
/* 340 */     if (enc == null) {
/* 341 */       enc = request.getCharacterEncoding();
/*     */     }
/* 343 */     if (enc == null) {
/* 344 */       enc = "ISO-8859-1";
/*     */     }
/*     */     
/* 347 */     if ((this.expandUriTemplateVariables) && (StringUtils.hasText(targetUrl))) {
/* 348 */       Map<String, String> variables = getCurrentRequestUriVariables(request);
/* 349 */       targetUrl = replaceUriTemplateVariables(targetUrl.toString(), model, variables, enc);
/*     */     }
/* 351 */     if (isPropagateQueryProperties()) {
/* 352 */       appendCurrentQueryParams(targetUrl, request);
/*     */     }
/* 354 */     if (this.exposeModelAttributes) {
/* 355 */       appendQueryProperties(targetUrl, model, enc);
/*     */     }
/*     */     
/* 358 */     return targetUrl.toString();
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
/*     */   protected StringBuilder replaceUriTemplateVariables(String targetUrl, Map<String, Object> model, Map<String, String> currentUriVariables, String encodingScheme)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 375 */     StringBuilder result = new StringBuilder();
/* 376 */     Matcher matcher = URI_TEMPLATE_VARIABLE_PATTERN.matcher(targetUrl);
/* 377 */     int endLastMatch = 0;
/* 378 */     while (matcher.find()) {
/* 379 */       String name = matcher.group(1);
/* 380 */       Object value = model.containsKey(name) ? model.remove(name) : currentUriVariables.get(name);
/* 381 */       if (value == null) {
/* 382 */         throw new IllegalArgumentException("Model has no value for key '" + name + "'");
/*     */       }
/* 384 */       result.append(targetUrl.substring(endLastMatch, matcher.start()));
/* 385 */       result.append(UriUtils.encodePathSegment(value.toString(), encodingScheme));
/* 386 */       endLastMatch = matcher.end();
/*     */     }
/* 388 */     result.append(targetUrl.substring(endLastMatch, targetUrl.length()));
/* 389 */     return result;
/*     */   }
/*     */   
/*     */   private Map<String, String> getCurrentRequestUriVariables(HttpServletRequest request)
/*     */   {
/* 394 */     String name = HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;
/* 395 */     Map<String, String> uriVars = (Map)request.getAttribute(name);
/* 396 */     return uriVars != null ? uriVars : Collections.emptyMap();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void appendCurrentQueryParams(StringBuilder targetUrl, HttpServletRequest request)
/*     */   {
/* 406 */     String query = request.getQueryString();
/* 407 */     if (StringUtils.hasText(query))
/*     */     {
/* 409 */       String fragment = null;
/* 410 */       int anchorIndex = targetUrl.indexOf("#");
/* 411 */       if (anchorIndex > -1) {
/* 412 */         fragment = targetUrl.substring(anchorIndex);
/* 413 */         targetUrl.delete(anchorIndex, targetUrl.length());
/*     */       }
/*     */       
/* 416 */       if (targetUrl.toString().indexOf('?') < 0) {
/* 417 */         targetUrl.append('?').append(query);
/*     */       }
/*     */       else {
/* 420 */         targetUrl.append('&').append(query);
/*     */       }
/*     */       
/* 423 */       if (fragment != null) {
/* 424 */         targetUrl.append(fragment);
/*     */       }
/*     */     }
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
/*     */   protected void appendQueryProperties(StringBuilder targetUrl, Map<String, Object> model, String encodingScheme)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 443 */     String fragment = null;
/* 444 */     int anchorIndex = targetUrl.indexOf("#");
/* 445 */     if (anchorIndex > -1) {
/* 446 */       fragment = targetUrl.substring(anchorIndex);
/* 447 */       targetUrl.delete(anchorIndex, targetUrl.length());
/*     */     }
/*     */     
/*     */ 
/* 451 */     boolean first = targetUrl.toString().indexOf('?') < 0;
/* 452 */     for (Map.Entry<String, Object> entry : queryProperties(model).entrySet()) {
/* 453 */       Object rawValue = entry.getValue();
/*     */       Iterator<Object> valueIter;
/* 455 */       Iterator<Object> valueIter; if ((rawValue != null) && (rawValue.getClass().isArray())) {
/* 456 */         valueIter = Arrays.asList(ObjectUtils.toObjectArray(rawValue)).iterator();
/*     */       } else { Iterator<Object> valueIter;
/* 458 */         if ((rawValue instanceof Collection)) {
/* 459 */           valueIter = ((Collection)rawValue).iterator();
/*     */         }
/*     */         else
/* 462 */           valueIter = Collections.singleton(rawValue).iterator();
/*     */       }
/* 464 */       while (valueIter.hasNext()) {
/* 465 */         Object value = valueIter.next();
/* 466 */         if (first) {
/* 467 */           targetUrl.append('?');
/* 468 */           first = false;
/*     */         }
/*     */         else {
/* 471 */           targetUrl.append('&');
/*     */         }
/* 473 */         String encodedKey = urlEncode((String)entry.getKey(), encodingScheme);
/* 474 */         String encodedValue = value != null ? urlEncode(value.toString(), encodingScheme) : "";
/* 475 */         targetUrl.append(encodedKey).append('=').append(encodedValue);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 480 */     if (fragment != null) {
/* 481 */       targetUrl.append(fragment);
/*     */     }
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
/*     */   protected Map<String, Object> queryProperties(Map<String, Object> model)
/*     */   {
/* 496 */     Map<String, Object> result = new LinkedHashMap();
/* 497 */     for (Map.Entry<String, Object> entry : model.entrySet()) {
/* 498 */       if (isEligibleProperty((String)entry.getKey(), entry.getValue())) {
/* 499 */         result.put(entry.getKey(), entry.getValue());
/*     */       }
/*     */     }
/* 502 */     return result;
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
/*     */   protected boolean isEligibleProperty(String key, Object value)
/*     */   {
/* 516 */     if (value == null) {
/* 517 */       return false;
/*     */     }
/* 519 */     if (isEligibleValue(value))
/* 520 */       return true;
/*     */     int i;
/* 522 */     if (value.getClass().isArray()) {
/* 523 */       int length = Array.getLength(value);
/* 524 */       if (length == 0) {
/* 525 */         return false;
/*     */       }
/* 527 */       for (i = 0; i < length; i++) {
/* 528 */         Object element = Array.get(value, i);
/* 529 */         if (!isEligibleValue(element)) {
/* 530 */           return false;
/*     */         }
/*     */       }
/* 533 */       return true;
/*     */     }
/* 535 */     if ((value instanceof Collection)) {
/* 536 */       Collection<?> coll = (Collection)value;
/* 537 */       if (coll.isEmpty()) {
/* 538 */         return false;
/*     */       }
/* 540 */       for (Object element : coll) {
/* 541 */         if (!isEligibleValue(element)) {
/* 542 */           return false;
/*     */         }
/*     */       }
/* 545 */       return true;
/*     */     }
/* 547 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isEligibleValue(Object value)
/*     */   {
/* 559 */     return (value != null) && (BeanUtils.isSimpleValueType(value.getClass()));
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
/*     */   protected String urlEncode(String input, String encodingScheme)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 573 */     return input != null ? URLEncoder.encode(input, encodingScheme) : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String updateTargetUrl(String targetUrl, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*     */   {
/* 585 */     WebApplicationContext wac = getWebApplicationContext();
/* 586 */     if (wac == null) {
/* 587 */       wac = RequestContextUtils.findWebApplicationContext(request, getServletContext());
/*     */     }
/*     */     
/* 590 */     if ((wac != null) && (wac.containsBean("requestDataValueProcessor"))) {
/* 591 */       RequestDataValueProcessor processor = (RequestDataValueProcessor)wac.getBean("requestDataValueProcessor", RequestDataValueProcessor.class);
/*     */       
/* 593 */       return processor.processUrl(request, targetUrl);
/*     */     }
/*     */     
/* 596 */     return targetUrl;
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
/*     */   protected void sendRedirect(HttpServletRequest request, HttpServletResponse response, String targetUrl, boolean http10Compatible)
/*     */     throws IOException
/*     */   {
/* 610 */     String encodedURL = isRemoteHost(targetUrl) ? targetUrl : response.encodeRedirectURL(targetUrl);
/* 611 */     if (http10Compatible) {
/* 612 */       HttpStatus attributeStatusCode = (HttpStatus)request.getAttribute(View.RESPONSE_STATUS_ATTRIBUTE);
/* 613 */       if (this.statusCode != null) {
/* 614 */         response.setStatus(this.statusCode.value());
/* 615 */         response.setHeader("Location", encodedURL);
/*     */       }
/* 617 */       else if (attributeStatusCode != null) {
/* 618 */         response.setStatus(attributeStatusCode.value());
/* 619 */         response.setHeader("Location", encodedURL);
/*     */       }
/*     */       else
/*     */       {
/* 623 */         response.sendRedirect(encodedURL);
/*     */       }
/*     */     }
/*     */     else {
/* 627 */       HttpStatus statusCode = getHttp11StatusCode(request, response, targetUrl);
/* 628 */       response.setStatus(statusCode.value());
/* 629 */       response.setHeader("Location", encodedURL);
/*     */     }
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
/*     */   protected boolean isRemoteHost(String targetUrl)
/*     */   {
/* 644 */     if (ObjectUtils.isEmpty(getHosts())) {
/* 645 */       return false;
/*     */     }
/* 647 */     String targetHost = UriComponentsBuilder.fromUriString(targetUrl).build().getHost();
/* 648 */     if (StringUtils.isEmpty(targetHost)) {
/* 649 */       return false;
/*     */     }
/* 651 */     for (String host : getHosts()) {
/* 652 */       if (targetHost.equals(host)) {
/* 653 */         return false;
/*     */       }
/*     */     }
/* 656 */     return true;
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
/*     */   protected HttpStatus getHttp11StatusCode(HttpServletRequest request, HttpServletResponse response, String targetUrl)
/*     */   {
/* 672 */     if (this.statusCode != null) {
/* 673 */       return this.statusCode;
/*     */     }
/* 675 */     HttpStatus attributeStatusCode = (HttpStatus)request.getAttribute(View.RESPONSE_STATUS_ATTRIBUTE);
/* 676 */     if (attributeStatusCode != null) {
/* 677 */       return attributeStatusCode;
/*     */     }
/* 679 */     return HttpStatus.SEE_OTHER;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\RedirectView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */