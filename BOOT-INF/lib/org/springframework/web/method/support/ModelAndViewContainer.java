/*     */ package org.springframework.web.method.support;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.ui.ModelMap;
/*     */ import org.springframework.validation.support.BindingAwareModelMap;
/*     */ import org.springframework.web.bind.support.SessionStatus;
/*     */ import org.springframework.web.bind.support.SimpleSessionStatus;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModelAndViewContainer
/*     */ {
/*  50 */   private boolean ignoreDefaultModelOnRedirect = false;
/*     */   
/*     */   private Object view;
/*     */   
/*  54 */   private final ModelMap defaultModel = new BindingAwareModelMap();
/*     */   
/*     */   private ModelMap redirectModel;
/*     */   
/*  58 */   private boolean redirectModelScenario = false;
/*     */   
/*     */   private HttpStatus status;
/*     */   
/*  62 */   private final Set<String> noBinding = new HashSet(4);
/*     */   
/*  64 */   private final Set<String> bindingDisabled = new HashSet(4);
/*     */   
/*  66 */   private final SessionStatus sessionStatus = new SimpleSessionStatus();
/*     */   
/*  68 */   private boolean requestHandled = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIgnoreDefaultModelOnRedirect(boolean ignoreDefaultModelOnRedirect)
/*     */   {
/*  84 */     this.ignoreDefaultModelOnRedirect = ignoreDefaultModelOnRedirect;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setViewName(String viewName)
/*     */   {
/*  92 */     this.view = viewName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getViewName()
/*     */   {
/* 100 */     return (this.view instanceof String) ? (String)this.view : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setView(Object view)
/*     */   {
/* 108 */     this.view = view;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getView()
/*     */   {
/* 116 */     return this.view;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isViewReference()
/*     */   {
/* 124 */     return this.view instanceof String;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ModelMap getModel()
/*     */   {
/* 134 */     if (useDefaultModel()) {
/* 135 */       return this.defaultModel;
/*     */     }
/*     */     
/* 138 */     if (this.redirectModel == null) {
/* 139 */       this.redirectModel = new ModelMap();
/*     */     }
/* 141 */     return this.redirectModel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean useDefaultModel()
/*     */   {
/* 149 */     return (!this.redirectModelScenario) || ((this.redirectModel == null) && (!this.ignoreDefaultModelOnRedirect));
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
/*     */   public ModelMap getDefaultModel()
/*     */   {
/* 163 */     return this.defaultModel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRedirectModel(ModelMap redirectModel)
/*     */   {
/* 173 */     this.redirectModel = redirectModel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRedirectModelScenario(boolean redirectModelScenario)
/*     */   {
/* 181 */     this.redirectModelScenario = redirectModelScenario;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStatus(HttpStatus status)
/*     */   {
/* 190 */     this.status = status;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpStatus getStatus()
/*     */   {
/* 198 */     return this.status;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBindingDisabled(String attributeName)
/*     */   {
/* 208 */     this.bindingDisabled.add(attributeName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBindingDisabled(String name)
/*     */   {
/* 216 */     return (this.bindingDisabled.contains(name)) || (this.noBinding.contains(name));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBinding(String attributeName, boolean enabled)
/*     */   {
/* 228 */     if (!enabled) {
/* 229 */       this.noBinding.add(attributeName);
/*     */     }
/*     */     else {
/* 232 */       this.noBinding.remove(attributeName);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SessionStatus getSessionStatus()
/*     */   {
/* 241 */     return this.sessionStatus;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRequestHandled(boolean requestHandled)
/*     */   {
/* 252 */     this.requestHandled = requestHandled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isRequestHandled()
/*     */   {
/* 259 */     return this.requestHandled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ModelAndViewContainer addAttribute(String name, Object value)
/*     */   {
/* 267 */     getModel().addAttribute(name, value);
/* 268 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ModelAndViewContainer addAttribute(Object value)
/*     */   {
/* 276 */     getModel().addAttribute(value);
/* 277 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ModelAndViewContainer addAllAttributes(Map<String, ?> attributes)
/*     */   {
/* 285 */     getModel().addAllAttributes(attributes);
/* 286 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ModelAndViewContainer mergeAttributes(Map<String, ?> attributes)
/*     */   {
/* 295 */     getModel().mergeAttributes(attributes);
/* 296 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ModelAndViewContainer removeAttributes(Map<String, ?> attributes)
/*     */   {
/* 303 */     if (attributes != null) {
/* 304 */       for (String key : attributes.keySet()) {
/* 305 */         getModel().remove(key);
/*     */       }
/*     */     }
/* 308 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean containsAttribute(String name)
/*     */   {
/* 316 */     return getModel().containsAttribute(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 325 */     StringBuilder sb = new StringBuilder("ModelAndViewContainer: ");
/* 326 */     if (!isRequestHandled()) {
/* 327 */       if (isViewReference()) {
/* 328 */         sb.append("reference to view with name '").append(this.view).append("'");
/*     */       }
/*     */       else {
/* 331 */         sb.append("View is [").append(this.view).append(']');
/*     */       }
/* 333 */       if (useDefaultModel()) {
/* 334 */         sb.append("; default model ");
/*     */       }
/*     */       else {
/* 337 */         sb.append("; redirect model ");
/*     */       }
/* 339 */       sb.append(getModel());
/*     */     }
/*     */     else {
/* 342 */       sb.append("Request handled directly");
/*     */     }
/* 344 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\method\support\ModelAndViewContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */