/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.servlet.ServletOutputStream;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.web.context.support.ContextExposingHttpServletRequest;
/*     */ import org.springframework.web.context.support.WebApplicationObjectSupport;
/*     */ import org.springframework.web.servlet.View;
/*     */ import org.springframework.web.servlet.support.RequestContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractView
/*     */   extends WebApplicationObjectSupport
/*     */   implements View, BeanNameAware
/*     */ {
/*     */   public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=ISO-8859-1";
/*     */   private static final int OUTPUT_BYTE_ARRAY_INITIAL_SIZE = 4096;
/*  69 */   private String contentType = "text/html;charset=ISO-8859-1";
/*     */   
/*     */   private String requestContextAttribute;
/*     */   
/*  73 */   private final Map<String, Object> staticAttributes = new LinkedHashMap();
/*     */   
/*  75 */   private boolean exposePathVariables = true;
/*     */   
/*  77 */   private boolean exposeContextBeansAsAttributes = false;
/*     */   
/*     */ 
/*     */ 
/*     */   private Set<String> exposedContextBeanNames;
/*     */   
/*     */ 
/*     */ 
/*     */   private String beanName;
/*     */   
/*     */ 
/*     */ 
/*     */   public void setContentType(String contentType)
/*     */   {
/*  91 */     this.contentType = contentType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContentType()
/*     */   {
/*  99 */     return this.contentType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRequestContextAttribute(String requestContextAttribute)
/*     */   {
/* 107 */     this.requestContextAttribute = requestContextAttribute;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getRequestContextAttribute()
/*     */   {
/* 114 */     return this.requestContextAttribute;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAttributesCSV(String propString)
/*     */     throws IllegalArgumentException
/*     */   {
/* 125 */     if (propString != null) {
/* 126 */       StringTokenizer st = new StringTokenizer(propString, ",");
/* 127 */       while (st.hasMoreTokens()) {
/* 128 */         String tok = st.nextToken();
/* 129 */         int eqIdx = tok.indexOf("=");
/* 130 */         if (eqIdx == -1) {
/* 131 */           throw new IllegalArgumentException("Expected = in attributes CSV string '" + propString + "'");
/*     */         }
/* 133 */         if (eqIdx >= tok.length() - 2) {
/* 134 */           throw new IllegalArgumentException("At least 2 characters ([]) required in attributes CSV string '" + propString + "'");
/*     */         }
/*     */         
/* 137 */         String name = tok.substring(0, eqIdx);
/* 138 */         String value = tok.substring(eqIdx + 1);
/*     */         
/*     */ 
/* 141 */         value = value.substring(1);
/* 142 */         value = value.substring(0, value.length() - 1);
/*     */         
/* 144 */         addStaticAttribute(name, value);
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
/*     */ 
/*     */   public void setAttributes(Properties attributes)
/*     */   {
/* 163 */     CollectionUtils.mergePropertiesIntoMap(attributes, this.staticAttributes);
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
/*     */   public void setAttributesMap(Map<String, ?> attributes)
/*     */   {
/* 176 */     if (attributes != null) {
/* 177 */       for (Map.Entry<String, ?> entry : attributes.entrySet()) {
/* 178 */         addStaticAttribute((String)entry.getKey(), entry.getValue());
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
/*     */   public Map<String, Object> getAttributesMap()
/*     */   {
/* 191 */     return this.staticAttributes;
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
/*     */   public void addStaticAttribute(String name, Object value)
/*     */   {
/* 205 */     this.staticAttributes.put(name, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, Object> getStaticAttributes()
/*     */   {
/* 215 */     return Collections.unmodifiableMap(this.staticAttributes);
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
/*     */   public void setExposePathVariables(boolean exposePathVariables)
/*     */   {
/* 230 */     this.exposePathVariables = exposePathVariables;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isExposePathVariables()
/*     */   {
/* 237 */     return this.exposePathVariables;
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
/*     */ 
/*     */ 
/*     */   public void setExposeContextBeansAsAttributes(boolean exposeContextBeansAsAttributes)
/*     */   {
/* 255 */     this.exposeContextBeansAsAttributes = exposeContextBeansAsAttributes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExposedContextBeanNames(String... exposedContextBeanNames)
/*     */   {
/* 267 */     this.exposedContextBeanNames = new HashSet(Arrays.asList(exposedContextBeanNames));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBeanName(String beanName)
/*     */   {
/* 276 */     this.beanName = beanName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getBeanName()
/*     */   {
/* 284 */     return this.beanName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 296 */     if (this.logger.isTraceEnabled()) {
/* 297 */       this.logger.trace("Rendering view with name '" + this.beanName + "' with model " + model + " and static attributes " + this.staticAttributes);
/*     */     }
/*     */     
/*     */ 
/* 301 */     Map<String, Object> mergedModel = createMergedOutputModel(model, request, response);
/* 302 */     prepareResponse(request, response);
/* 303 */     renderMergedOutputModel(mergedModel, getRequestToExpose(request), response);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Map<String, Object> createMergedOutputModel(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
/*     */   {
/* 315 */     Map<String, Object> pathVars = this.exposePathVariables ? (Map)request.getAttribute(View.PATH_VARIABLES) : null;
/*     */     
/*     */ 
/* 318 */     int size = this.staticAttributes.size();
/* 319 */     size += (model != null ? model.size() : 0);
/* 320 */     size += (pathVars != null ? pathVars.size() : 0);
/*     */     
/* 322 */     Map<String, Object> mergedModel = new LinkedHashMap(size);
/* 323 */     mergedModel.putAll(this.staticAttributes);
/* 324 */     if (pathVars != null) {
/* 325 */       mergedModel.putAll(pathVars);
/*     */     }
/* 327 */     if (model != null) {
/* 328 */       mergedModel.putAll(model);
/*     */     }
/*     */     
/*     */ 
/* 332 */     if (this.requestContextAttribute != null) {
/* 333 */       mergedModel.put(this.requestContextAttribute, createRequestContext(request, response, mergedModel));
/*     */     }
/*     */     
/* 336 */     return mergedModel;
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
/*     */ 
/*     */   protected RequestContext createRequestContext(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model)
/*     */   {
/* 353 */     return new RequestContext(request, response, getServletContext(), model);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void prepareResponse(HttpServletRequest request, HttpServletResponse response)
/*     */   {
/* 364 */     if (generatesDownloadContent()) {
/* 365 */       response.setHeader("Pragma", "private");
/* 366 */       response.setHeader("Cache-Control", "private, must-revalidate");
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
/*     */   protected boolean generatesDownloadContent()
/*     */   {
/* 381 */     return false;
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
/*     */   protected HttpServletRequest getRequestToExpose(HttpServletRequest originalRequest)
/*     */   {
/* 395 */     if ((this.exposeContextBeansAsAttributes) || (this.exposedContextBeanNames != null)) {
/* 396 */       return new ContextExposingHttpServletRequest(originalRequest, 
/* 397 */         getWebApplicationContext(), this.exposedContextBeanNames);
/*     */     }
/* 399 */     return originalRequest;
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
/*     */   protected abstract void renderMergedOutputModel(Map<String, Object> paramMap, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void exposeModelAsRequestAttributes(Map<String, Object> model, HttpServletRequest request)
/*     */     throws Exception
/*     */   {
/* 426 */     for (Map.Entry<String, Object> entry : model.entrySet()) {
/* 427 */       String modelName = (String)entry.getKey();
/* 428 */       Object modelValue = entry.getValue();
/* 429 */       if (modelValue != null) {
/* 430 */         request.setAttribute(modelName, modelValue);
/* 431 */         if (this.logger.isDebugEnabled()) {
/* 432 */           this.logger.debug("Added model object '" + modelName + "' of type [" + modelValue.getClass().getName() + "] to request in view with name '" + 
/* 433 */             getBeanName() + "'");
/*     */         }
/*     */       }
/*     */       else {
/* 437 */         request.removeAttribute(modelName);
/* 438 */         if (this.logger.isDebugEnabled()) {
/* 439 */           this.logger.debug("Removed model object '" + modelName + "' from request in view with name '" + 
/* 440 */             getBeanName() + "'");
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ByteArrayOutputStream createTemporaryOutputStream()
/*     */   {
/* 452 */     return new ByteArrayOutputStream(4096);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void writeToResponse(HttpServletResponse response, ByteArrayOutputStream baos)
/*     */     throws IOException
/*     */   {
/* 463 */     response.setContentType(getContentType());
/* 464 */     response.setContentLength(baos.size());
/*     */     
/*     */ 
/* 467 */     ServletOutputStream out = response.getOutputStream();
/* 468 */     baos.writeTo(out);
/* 469 */     out.flush();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setResponseContentType(HttpServletRequest request, HttpServletResponse response)
/*     */   {
/* 479 */     MediaType mediaType = (MediaType)request.getAttribute(View.SELECTED_CONTENT_TYPE);
/* 480 */     if ((mediaType != null) && (mediaType.isConcrete())) {
/* 481 */       response.setContentType(mediaType.toString());
/*     */     }
/*     */     else {
/* 484 */       response.setContentType(getContentType());
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 490 */     StringBuilder sb = new StringBuilder(getClass().getName());
/* 491 */     if (getBeanName() != null) {
/* 492 */       sb.append(": name '").append(getBeanName()).append("'");
/*     */     }
/*     */     else {
/* 495 */       sb.append(": unnamed");
/*     */     }
/* 497 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\AbstractView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */