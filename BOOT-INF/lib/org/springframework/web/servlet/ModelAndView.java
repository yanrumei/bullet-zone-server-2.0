/*     */ package org.springframework.web.servlet;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.ui.ModelMap;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModelAndView
/*     */ {
/*     */   private Object view;
/*     */   private ModelMap model;
/*     */   private HttpStatus status;
/*  58 */   private boolean cleared = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ModelAndView() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ModelAndView(String viewName)
/*     */   {
/*  78 */     this.view = viewName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ModelAndView(View view)
/*     */   {
/*  88 */     this.view = view;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ModelAndView(String viewName, Map<String, ?> model)
/*     */   {
/* 100 */     this.view = viewName;
/* 101 */     if (model != null) {
/* 102 */       getModelMap().addAllAttributes(model);
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
/*     */   public ModelAndView(View view, Map<String, ?> model)
/*     */   {
/* 117 */     this.view = view;
/* 118 */     if (model != null) {
/* 119 */       getModelMap().addAllAttributes(model);
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
/*     */   public ModelAndView(String viewName, HttpStatus status)
/*     */   {
/* 132 */     this.view = viewName;
/* 133 */     this.status = status;
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
/*     */   public ModelAndView(String viewName, Map<String, ?> model, HttpStatus status)
/*     */   {
/* 148 */     this.view = viewName;
/* 149 */     if (model != null) {
/* 150 */       getModelMap().addAllAttributes(model);
/*     */     }
/* 152 */     this.status = status;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ModelAndView(String viewName, String modelName, Object modelObject)
/*     */   {
/* 163 */     this.view = viewName;
/* 164 */     addObject(modelName, modelObject);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ModelAndView(View view, String modelName, Object modelObject)
/*     */   {
/* 174 */     this.view = view;
/* 175 */     addObject(modelName, modelObject);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setViewName(String viewName)
/*     */   {
/* 185 */     this.view = viewName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getViewName()
/*     */   {
/* 193 */     return (this.view instanceof String) ? (String)this.view : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setView(View view)
/*     */   {
/* 201 */     this.view = view;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public View getView()
/*     */   {
/* 209 */     return (this.view instanceof View) ? (View)this.view : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasView()
/*     */   {
/* 217 */     return this.view != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isReference()
/*     */   {
/* 226 */     return this.view instanceof String;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Map<String, Object> getModelInternal()
/*     */   {
/* 234 */     return this.model;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ModelMap getModelMap()
/*     */   {
/* 241 */     if (this.model == null) {
/* 242 */       this.model = new ModelMap();
/*     */     }
/* 244 */     return this.model;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, Object> getModel()
/*     */   {
/* 252 */     return getModelMap();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStatus(HttpStatus status)
/*     */   {
/* 261 */     this.status = status;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpStatus getStatus()
/*     */   {
/* 269 */     return this.status;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ModelAndView addObject(String attributeName, Object attributeValue)
/*     */   {
/* 281 */     getModelMap().addAttribute(attributeName, attributeValue);
/* 282 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ModelAndView addObject(Object attributeValue)
/*     */   {
/* 292 */     getModelMap().addAttribute(attributeValue);
/* 293 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ModelAndView addAllObjects(Map<String, ?> modelMap)
/*     */   {
/* 303 */     getModelMap().addAllAttributes(modelMap);
/* 304 */     return this;
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
/*     */   public void clear()
/*     */   {
/* 317 */     this.view = null;
/* 318 */     this.model = null;
/* 319 */     this.cleared = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 327 */     return (this.view == null) && (CollectionUtils.isEmpty(this.model));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean wasCleared()
/*     */   {
/* 338 */     return (this.cleared) && (isEmpty());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 347 */     StringBuilder sb = new StringBuilder("ModelAndView: ");
/* 348 */     if (isReference()) {
/* 349 */       sb.append("reference to view with name '").append(this.view).append("'");
/*     */     }
/*     */     else {
/* 352 */       sb.append("materialized View is [").append(this.view).append(']');
/*     */     }
/* 354 */     sb.append("; model is ").append(this.model);
/* 355 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\ModelAndView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */