/*     */ package org.springframework.jmx.export.assembler;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.management.Descriptor;
/*     */ import javax.management.JMException;
/*     */ import javax.management.MBeanParameterInfo;
/*     */ import javax.management.modelmbean.ModelMBeanAttributeInfo;
/*     */ import javax.management.modelmbean.ModelMBeanOperationInfo;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.core.DefaultParameterNameDiscoverer;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.jmx.support.JmxUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractReflectiveMBeanInfoAssembler
/*     */   extends AbstractMBeanInfoAssembler
/*     */ {
/*     */   protected static final String FIELD_GET_METHOD = "getMethod";
/*     */   protected static final String FIELD_SET_METHOD = "setMethod";
/*     */   protected static final String FIELD_ROLE = "role";
/*     */   protected static final String ROLE_GETTER = "getter";
/*     */   protected static final String ROLE_SETTER = "setter";
/*     */   protected static final String ROLE_OPERATION = "operation";
/*     */   protected static final String FIELD_VISIBILITY = "visibility";
/*     */   protected static final int ATTRIBUTE_OPERATION_VISIBILITY = 4;
/*     */   protected static final String FIELD_CLASS = "class";
/*     */   protected static final String FIELD_LOG = "log";
/*     */   protected static final String FIELD_LOG_FILE = "logFile";
/*     */   protected static final String FIELD_CURRENCY_TIME_LIMIT = "currencyTimeLimit";
/*     */   protected static final String FIELD_DEFAULT = "default";
/*     */   protected static final String FIELD_PERSIST_POLICY = "persistPolicy";
/*     */   protected static final String FIELD_PERSIST_PERIOD = "persistPeriod";
/*     */   protected static final String FIELD_PERSIST_LOCATION = "persistLocation";
/*     */   protected static final String FIELD_PERSIST_NAME = "persistName";
/*     */   protected static final String FIELD_DISPLAY_NAME = "displayName";
/*     */   protected static final String FIELD_UNITS = "units";
/*     */   protected static final String FIELD_METRIC_TYPE = "metricType";
/*     */   protected static final String FIELD_METRIC_CATEGORY = "metricCategory";
/*     */   private Integer defaultCurrencyTimeLimit;
/* 179 */   private boolean useStrictCasing = true;
/*     */   
/* 181 */   private boolean exposeClassDescriptor = false;
/*     */   
/* 183 */   private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDefaultCurrencyTimeLimit(Integer defaultCurrencyTimeLimit)
/*     */   {
/* 207 */     this.defaultCurrencyTimeLimit = defaultCurrencyTimeLimit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Integer getDefaultCurrencyTimeLimit()
/*     */   {
/* 214 */     return this.defaultCurrencyTimeLimit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUseStrictCasing(boolean useStrictCasing)
/*     */   {
/* 225 */     this.useStrictCasing = useStrictCasing;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean isUseStrictCasing()
/*     */   {
/* 232 */     return this.useStrictCasing;
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
/*     */   public void setExposeClassDescriptor(boolean exposeClassDescriptor)
/*     */   {
/* 252 */     this.exposeClassDescriptor = exposeClassDescriptor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean isExposeClassDescriptor()
/*     */   {
/* 259 */     return this.exposeClassDescriptor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer)
/*     */   {
/* 268 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ParameterNameDiscoverer getParameterNameDiscoverer()
/*     */   {
/* 276 */     return this.parameterNameDiscoverer;
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
/*     */   protected ModelMBeanAttributeInfo[] getAttributeInfo(Object managedBean, String beanKey)
/*     */     throws JMException
/*     */   {
/* 294 */     PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(getClassToExpose(managedBean));
/* 295 */     List<ModelMBeanAttributeInfo> infos = new ArrayList();
/*     */     
/* 297 */     for (PropertyDescriptor prop : props) {
/* 298 */       Method getter = prop.getReadMethod();
/* 299 */       if ((getter == null) || (getter.getDeclaringClass() != Object.class))
/*     */       {
/*     */ 
/* 302 */         if ((getter != null) && (!includeReadAttribute(getter, beanKey))) {
/* 303 */           getter = null;
/*     */         }
/*     */         
/* 306 */         Method setter = prop.getWriteMethod();
/* 307 */         if ((setter != null) && (!includeWriteAttribute(setter, beanKey))) {
/* 308 */           setter = null;
/*     */         }
/*     */         
/* 311 */         if ((getter != null) || (setter != null))
/*     */         {
/* 313 */           String attrName = JmxUtils.getAttributeName(prop, isUseStrictCasing());
/* 314 */           String description = getAttributeDescription(prop, beanKey);
/* 315 */           ModelMBeanAttributeInfo info = new ModelMBeanAttributeInfo(attrName, description, getter, setter);
/*     */           
/* 317 */           Descriptor desc = info.getDescriptor();
/* 318 */           if (getter != null) {
/* 319 */             desc.setField("getMethod", getter.getName());
/*     */           }
/* 321 */           if (setter != null) {
/* 322 */             desc.setField("setMethod", setter.getName());
/*     */           }
/*     */           
/* 325 */           populateAttributeDescriptor(desc, getter, setter, beanKey);
/* 326 */           info.setDescriptor(desc);
/* 327 */           infos.add(info);
/*     */         }
/*     */       }
/*     */     }
/* 331 */     return (ModelMBeanAttributeInfo[])infos.toArray(new ModelMBeanAttributeInfo[infos.size()]);
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
/*     */   protected ModelMBeanOperationInfo[] getOperationInfo(Object managedBean, String beanKey)
/*     */   {
/* 348 */     Method[] methods = getClassToExpose(managedBean).getMethods();
/* 349 */     List<ModelMBeanOperationInfo> infos = new ArrayList();
/*     */     
/* 351 */     for (Method method : methods) {
/* 352 */       if (!method.isSynthetic())
/*     */       {
/*     */ 
/* 355 */         if (Object.class != method.getDeclaringClass())
/*     */         {
/*     */ 
/*     */ 
/* 359 */           ModelMBeanOperationInfo info = null;
/* 360 */           PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
/* 361 */           if ((pd != null) && (
/* 362 */             ((method.equals(pd.getReadMethod())) && (includeReadAttribute(method, beanKey))) || (
/* 363 */             (method.equals(pd.getWriteMethod())) && (includeWriteAttribute(method, beanKey)))))
/*     */           {
/*     */ 
/* 366 */             info = createModelMBeanOperationInfo(method, pd.getName(), beanKey);
/* 367 */             Descriptor desc = info.getDescriptor();
/* 368 */             if (method.equals(pd.getReadMethod())) {
/* 369 */               desc.setField("role", "getter");
/*     */             }
/*     */             else {
/* 372 */               desc.setField("role", "setter");
/*     */             }
/* 374 */             desc.setField("visibility", Integer.valueOf(4));
/* 375 */             if (isExposeClassDescriptor()) {
/* 376 */               desc.setField("class", getClassForDescriptor(managedBean).getName());
/*     */             }
/* 378 */             info.setDescriptor(desc);
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 383 */           if ((info == null) && (includeOperation(method, beanKey))) {
/* 384 */             info = createModelMBeanOperationInfo(method, method.getName(), beanKey);
/* 385 */             Descriptor desc = info.getDescriptor();
/* 386 */             desc.setField("role", "operation");
/* 387 */             if (isExposeClassDescriptor()) {
/* 388 */               desc.setField("class", getClassForDescriptor(managedBean).getName());
/*     */             }
/* 390 */             populateOperationDescriptor(desc, method, beanKey);
/* 391 */             info.setDescriptor(desc);
/*     */           }
/*     */           
/* 394 */           if (info != null)
/* 395 */             infos.add(info);
/*     */         }
/*     */       }
/*     */     }
/* 399 */     return (ModelMBeanOperationInfo[])infos.toArray(new ModelMBeanOperationInfo[infos.size()]);
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
/*     */   protected ModelMBeanOperationInfo createModelMBeanOperationInfo(Method method, String name, String beanKey)
/*     */   {
/* 413 */     MBeanParameterInfo[] params = getOperationParameters(method, beanKey);
/* 414 */     if (params.length == 0) {
/* 415 */       return new ModelMBeanOperationInfo(getOperationDescription(method, beanKey), method);
/*     */     }
/*     */     
/* 418 */     return new ModelMBeanOperationInfo(method.getName(), 
/* 419 */       getOperationDescription(method, beanKey), 
/* 420 */       getOperationParameters(method, beanKey), method
/* 421 */       .getReturnType().getName(), 3);
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
/*     */   protected Class<?> getClassForDescriptor(Object managedBean)
/*     */   {
/* 438 */     if (AopUtils.isJdkDynamicProxy(managedBean)) {
/* 439 */       return org.springframework.aop.framework.AopProxyUtils.proxiedUserInterfaces(managedBean)[0];
/*     */     }
/* 441 */     return getClassToExpose(managedBean);
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
/*     */   protected abstract boolean includeReadAttribute(Method paramMethod, String paramString);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean includeWriteAttribute(Method paramMethod, String paramString);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean includeOperation(Method paramMethod, String paramString);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getAttributeDescription(PropertyDescriptor propertyDescriptor, String beanKey)
/*     */   {
/* 484 */     return propertyDescriptor.getDisplayName();
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
/*     */   protected String getOperationDescription(Method method, String beanKey)
/*     */   {
/* 497 */     return method.getName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MBeanParameterInfo[] getOperationParameters(Method method, String beanKey)
/*     */   {
/* 509 */     ParameterNameDiscoverer paramNameDiscoverer = getParameterNameDiscoverer();
/* 510 */     String[] paramNames = paramNameDiscoverer != null ? paramNameDiscoverer.getParameterNames(method) : null;
/* 511 */     if (paramNames == null) {
/* 512 */       return new MBeanParameterInfo[0];
/*     */     }
/*     */     
/* 515 */     MBeanParameterInfo[] info = new MBeanParameterInfo[paramNames.length];
/* 516 */     Class<?>[] typeParameters = method.getParameterTypes();
/* 517 */     for (int i = 0; i < info.length; i++) {
/* 518 */       info[i] = new MBeanParameterInfo(paramNames[i], typeParameters[i].getName(), paramNames[i]);
/*     */     }
/*     */     
/* 521 */     return info;
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
/*     */   protected void populateMBeanDescriptor(Descriptor descriptor, Object managedBean, String beanKey)
/*     */   {
/* 537 */     applyDefaultCurrencyTimeLimit(descriptor);
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
/*     */   protected void populateAttributeDescriptor(Descriptor desc, Method getter, Method setter, String beanKey)
/*     */   {
/* 554 */     applyDefaultCurrencyTimeLimit(desc);
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
/*     */   protected void populateOperationDescriptor(Descriptor desc, Method method, String beanKey)
/*     */   {
/* 570 */     applyDefaultCurrencyTimeLimit(desc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void applyDefaultCurrencyTimeLimit(Descriptor desc)
/*     */   {
/* 580 */     if (getDefaultCurrencyTimeLimit() != null) {
/* 581 */       desc.setField("currencyTimeLimit", getDefaultCurrencyTimeLimit().toString());
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
/*     */   protected void applyCurrencyTimeLimit(Descriptor desc, int currencyTimeLimit)
/*     */   {
/* 597 */     if (currencyTimeLimit > 0)
/*     */     {
/* 599 */       desc.setField("currencyTimeLimit", Integer.toString(currencyTimeLimit));
/*     */     }
/* 601 */     else if (currencyTimeLimit == 0)
/*     */     {
/* 603 */       desc.setField("currencyTimeLimit", Integer.toString(Integer.MAX_VALUE));
/*     */     }
/*     */     else
/*     */     {
/* 607 */       applyDefaultCurrencyTimeLimit(desc);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jmx\export\assembler\AbstractReflectiveMBeanInfoAssembler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */