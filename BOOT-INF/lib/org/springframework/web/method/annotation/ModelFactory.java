/*     */ package org.springframework.web.method.annotation;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.core.Conventions;
/*     */ import org.springframework.core.GenericTypeResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.ui.ModelMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.validation.BindingResult;
/*     */ import org.springframework.web.HttpSessionRequiredException;
/*     */ import org.springframework.web.bind.WebDataBinder;
/*     */ import org.springframework.web.bind.annotation.ModelAttribute;
/*     */ import org.springframework.web.bind.support.SessionStatus;
/*     */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.HandlerMethod;
/*     */ import org.springframework.web.method.support.InvocableHandlerMethod;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ModelFactory
/*     */ {
/*  62 */   private static final Log logger = LogFactory.getLog(ModelFactory.class);
/*     */   
/*  64 */   private final List<ModelMethod> modelMethods = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final WebDataBinderFactory dataBinderFactory;
/*     */   
/*     */ 
/*     */ 
/*     */   private final SessionAttributesHandler sessionAttributesHandler;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ModelFactory(List<InvocableHandlerMethod> handlerMethods, WebDataBinderFactory binderFactory, SessionAttributesHandler attributeHandler)
/*     */   {
/*  80 */     if (handlerMethods != null) {
/*  81 */       for (InvocableHandlerMethod handlerMethod : handlerMethods) {
/*  82 */         this.modelMethods.add(new ModelMethod(handlerMethod));
/*     */       }
/*     */     }
/*  85 */     this.dataBinderFactory = binderFactory;
/*  86 */     this.sessionAttributesHandler = attributeHandler;
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
/*     */ 
/*     */ 
/*     */   public void initModel(NativeWebRequest request, ModelAndViewContainer container, HandlerMethod handlerMethod)
/*     */     throws Exception
/*     */   {
/* 107 */     Map<String, ?> sessionAttributes = this.sessionAttributesHandler.retrieveAttributes(request);
/* 108 */     container.mergeAttributes(sessionAttributes);
/* 109 */     invokeModelAttributeMethods(request, container);
/*     */     
/* 111 */     for (String name : findSessionAttributeArguments(handlerMethod)) {
/* 112 */       if (!container.containsAttribute(name)) {
/* 113 */         Object value = this.sessionAttributesHandler.retrieveAttribute(request, name);
/* 114 */         if (value == null) {
/* 115 */           throw new HttpSessionRequiredException("Expected session attribute '" + name + "'", name);
/*     */         }
/* 117 */         container.addAttribute(name, value);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void invokeModelAttributeMethods(NativeWebRequest request, ModelAndViewContainer container)
/*     */     throws Exception
/*     */   {
/* 129 */     while (!this.modelMethods.isEmpty()) {
/* 130 */       InvocableHandlerMethod modelMethod = getNextModelMethod(container).getHandlerMethod();
/* 131 */       ModelAttribute ann = (ModelAttribute)modelMethod.getMethodAnnotation(ModelAttribute.class);
/* 132 */       if (container.containsAttribute(ann.name())) {
/* 133 */         if (!ann.binding()) {
/* 134 */           container.setBindingDisabled(ann.name());
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 139 */         Object returnValue = modelMethod.invokeForRequest(request, container, new Object[0]);
/* 140 */         if (!modelMethod.isVoid()) {
/* 141 */           String returnValueName = getNameForReturnValue(returnValue, modelMethod.getReturnType());
/* 142 */           if (!ann.binding()) {
/* 143 */             container.setBindingDisabled(returnValueName);
/*     */           }
/* 145 */           if (!container.containsAttribute(returnValueName))
/* 146 */             container.addAttribute(returnValueName, returnValue);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private ModelMethod getNextModelMethod(ModelAndViewContainer container) {
/* 153 */     for (ModelMethod modelMethod : this.modelMethods) {
/* 154 */       if (modelMethod.checkDependencies(container)) {
/* 155 */         if (logger.isTraceEnabled()) {
/* 156 */           logger.trace("Selected @ModelAttribute method " + modelMethod);
/*     */         }
/* 158 */         this.modelMethods.remove(modelMethod);
/* 159 */         return modelMethod;
/*     */       }
/*     */     }
/* 162 */     ModelMethod modelMethod = (ModelMethod)this.modelMethods.get(0);
/* 163 */     if (logger.isTraceEnabled()) {
/* 164 */       logger.trace("Selected @ModelAttribute method (not present: " + modelMethod
/* 165 */         .getUnresolvedDependencies(container) + ") " + modelMethod);
/*     */     }
/* 167 */     this.modelMethods.remove(modelMethod);
/* 168 */     return modelMethod;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private List<String> findSessionAttributeArguments(HandlerMethod handlerMethod)
/*     */   {
/* 175 */     List<String> result = new ArrayList();
/* 176 */     for (MethodParameter parameter : handlerMethod.getMethodParameters()) {
/* 177 */       if (parameter.hasParameterAnnotation(ModelAttribute.class)) {
/* 178 */         String name = getNameForParameter(parameter);
/* 179 */         Class<?> paramType = parameter.getParameterType();
/* 180 */         if (this.sessionAttributesHandler.isHandlerSessionAttribute(name, paramType)) {
/* 181 */           result.add(name);
/*     */         }
/*     */       }
/*     */     }
/* 185 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateModel(NativeWebRequest request, ModelAndViewContainer container)
/*     */     throws Exception
/*     */   {
/* 196 */     ModelMap defaultModel = container.getDefaultModel();
/* 197 */     if (container.getSessionStatus().isComplete()) {
/* 198 */       this.sessionAttributesHandler.cleanupAttributes(request);
/*     */     }
/*     */     else {
/* 201 */       this.sessionAttributesHandler.storeAttributes(request, defaultModel);
/*     */     }
/* 203 */     if ((!container.isRequestHandled()) && (container.getModel() == defaultModel)) {
/* 204 */       updateBindingResult(request, defaultModel);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void updateBindingResult(NativeWebRequest request, ModelMap model)
/*     */     throws Exception
/*     */   {
/* 212 */     List<String> keyNames = new ArrayList(model.keySet());
/* 213 */     for (String name : keyNames) {
/* 214 */       Object value = model.get(name);
/* 215 */       if (isBindingCandidate(name, value)) {
/* 216 */         String bindingResultKey = BindingResult.MODEL_KEY_PREFIX + name;
/* 217 */         if (!model.containsAttribute(bindingResultKey)) {
/* 218 */           WebDataBinder dataBinder = this.dataBinderFactory.createBinder(request, value, name);
/* 219 */           model.put(bindingResultKey, dataBinder.getBindingResult());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean isBindingCandidate(String attributeName, Object value)
/*     */   {
/* 229 */     if (attributeName.startsWith(BindingResult.MODEL_KEY_PREFIX)) {
/* 230 */       return false;
/*     */     }
/*     */     
/* 233 */     Class<?> attrType = value != null ? value.getClass() : null;
/* 234 */     if (this.sessionAttributesHandler.isHandlerSessionAttribute(attributeName, attrType)) {
/* 235 */       return true;
/*     */     }
/*     */     
/* 238 */     return (value != null) && (!value.getClass().isArray()) && (!(value instanceof Collection)) && (!(value instanceof Map)) && 
/* 239 */       (!BeanUtils.isSimpleValueType(value.getClass()));
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
/*     */   public static String getNameForParameter(MethodParameter parameter)
/*     */   {
/* 253 */     ModelAttribute ann = (ModelAttribute)parameter.getParameterAnnotation(ModelAttribute.class);
/* 254 */     String name = ann != null ? ann.value() : null;
/* 255 */     return StringUtils.hasText(name) ? name : Conventions.getVariableNameForParameter(parameter);
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
/*     */   public static String getNameForReturnValue(Object returnValue, MethodParameter returnType)
/*     */   {
/* 270 */     ModelAttribute ann = (ModelAttribute)returnType.getMethodAnnotation(ModelAttribute.class);
/* 271 */     if ((ann != null) && (StringUtils.hasText(ann.value()))) {
/* 272 */       return ann.value();
/*     */     }
/*     */     
/* 275 */     Method method = returnType.getMethod();
/* 276 */     Class<?> containingClass = returnType.getContainingClass();
/* 277 */     Class<?> resolvedType = GenericTypeResolver.resolveReturnType(method, containingClass);
/* 278 */     return Conventions.getVariableNameForReturnType(method, resolvedType, returnValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class ModelMethod
/*     */   {
/*     */     private final InvocableHandlerMethod handlerMethod;
/*     */     
/* 287 */     private final Set<String> dependencies = new HashSet();
/*     */     
/*     */     public ModelMethod(InvocableHandlerMethod handlerMethod) {
/* 290 */       this.handlerMethod = handlerMethod;
/* 291 */       for (MethodParameter parameter : handlerMethod.getMethodParameters()) {
/* 292 */         if (parameter.hasParameterAnnotation(ModelAttribute.class)) {
/* 293 */           this.dependencies.add(ModelFactory.getNameForParameter(parameter));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public InvocableHandlerMethod getHandlerMethod() {
/* 299 */       return this.handlerMethod;
/*     */     }
/*     */     
/*     */     public boolean checkDependencies(ModelAndViewContainer mavContainer) {
/* 303 */       for (String name : this.dependencies) {
/* 304 */         if (!mavContainer.containsAttribute(name)) {
/* 305 */           return false;
/*     */         }
/*     */       }
/* 308 */       return true;
/*     */     }
/*     */     
/*     */     public List<String> getUnresolvedDependencies(ModelAndViewContainer mavContainer) {
/* 312 */       List<String> result = new ArrayList(this.dependencies.size());
/* 313 */       for (String name : this.dependencies) {
/* 314 */         if (!mavContainer.containsAttribute(name)) {
/* 315 */           result.add(name);
/*     */         }
/*     */       }
/* 318 */       return result;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 323 */       return this.handlerMethod.getMethod().toGenericString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\method\annotation\ModelFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */