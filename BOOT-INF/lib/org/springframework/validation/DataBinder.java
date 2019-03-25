/*     */ package org.springframework.validation;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.ConfigurablePropertyAccessor;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.PropertyAccessException;
/*     */ import org.springframework.beans.PropertyAccessorUtils;
/*     */ import org.springframework.beans.PropertyBatchUpdateException;
/*     */ import org.springframework.beans.PropertyEditorRegistry;
/*     */ import org.springframework.beans.PropertyValue;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.beans.SimpleTypeConverter;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.beans.TypeMismatchException;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.format.Formatter;
/*     */ import org.springframework.format.support.FormatterPropertyEditorAdapter;
/*     */ import org.springframework.lang.UsesJava8;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.PatternMatchUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DataBinder
/*     */   implements PropertyEditorRegistry, TypeConverter
/*     */ {
/*     */   public static final String DEFAULT_OBJECT_NAME = "target";
/*     */   public static final int DEFAULT_AUTO_GROW_COLLECTION_LIMIT = 256;
/* 127 */   protected static final Log logger = LogFactory.getLog(DataBinder.class);
/*     */   
/* 129 */   private static Class<?> javaUtilOptionalClass = null;
/*     */   private final Object target;
/*     */   private final String objectName;
/*     */   
/*     */   static {
/* 134 */     try { javaUtilOptionalClass = ClassUtils.forName("java.util.Optional", DataBinder.class.getClassLoader());
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private AbstractPropertyBindingResult bindingResult;
/*     */   
/*     */ 
/*     */ 
/*     */   private SimpleTypeConverter typeConverter;
/*     */   
/*     */ 
/*     */ 
/* 150 */   private boolean ignoreUnknownFields = true;
/*     */   
/* 152 */   private boolean ignoreInvalidFields = false;
/*     */   
/* 154 */   private boolean autoGrowNestedPaths = true;
/*     */   
/* 156 */   private int autoGrowCollectionLimit = 256;
/*     */   
/*     */   private String[] allowedFields;
/*     */   
/*     */   private String[] disallowedFields;
/*     */   
/*     */   private String[] requiredFields;
/*     */   
/*     */   private ConversionService conversionService;
/*     */   
/*     */   private MessageCodesResolver messageCodesResolver;
/*     */   
/* 168 */   private BindingErrorProcessor bindingErrorProcessor = new DefaultBindingErrorProcessor();
/*     */   
/* 170 */   private final List<Validator> validators = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DataBinder(Object target)
/*     */   {
/* 180 */     this(target, "target");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DataBinder(Object target, String objectName)
/*     */   {
/* 190 */     if ((target != null) && (target.getClass() == javaUtilOptionalClass)) {
/* 191 */       this.target = OptionalUnwrapper.unwrap(target);
/*     */     }
/*     */     else {
/* 194 */       this.target = target;
/*     */     }
/* 196 */     this.objectName = objectName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getTarget()
/*     */   {
/* 204 */     return this.target;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getObjectName()
/*     */   {
/* 211 */     return this.objectName;
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
/*     */   public void setAutoGrowNestedPaths(boolean autoGrowNestedPaths)
/*     */   {
/* 225 */     Assert.state(this.bindingResult == null, "DataBinder is already initialized - call setAutoGrowNestedPaths before other configuration methods");
/*     */     
/* 227 */     this.autoGrowNestedPaths = autoGrowNestedPaths;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isAutoGrowNestedPaths()
/*     */   {
/* 234 */     return this.autoGrowNestedPaths;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAutoGrowCollectionLimit(int autoGrowCollectionLimit)
/*     */   {
/* 245 */     Assert.state(this.bindingResult == null, "DataBinder is already initialized - call setAutoGrowCollectionLimit before other configuration methods");
/*     */     
/* 247 */     this.autoGrowCollectionLimit = autoGrowCollectionLimit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getAutoGrowCollectionLimit()
/*     */   {
/* 254 */     return this.autoGrowCollectionLimit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initBeanPropertyAccess()
/*     */   {
/* 264 */     Assert.state(this.bindingResult == null, "DataBinder is already initialized - call initBeanPropertyAccess before other configuration methods");
/*     */     
/* 266 */     this.bindingResult = createBeanPropertyBindingResult();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractPropertyBindingResult createBeanPropertyBindingResult()
/*     */   {
/* 276 */     BeanPropertyBindingResult result = new BeanPropertyBindingResult(getTarget(), getObjectName(), isAutoGrowNestedPaths(), getAutoGrowCollectionLimit());
/*     */     
/* 278 */     if (this.conversionService != null) {
/* 279 */       result.initConversion(this.conversionService);
/*     */     }
/* 281 */     if (this.messageCodesResolver != null) {
/* 282 */       result.setMessageCodesResolver(this.messageCodesResolver);
/*     */     }
/*     */     
/* 285 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initDirectFieldAccess()
/*     */   {
/* 295 */     Assert.state(this.bindingResult == null, "DataBinder is already initialized - call initDirectFieldAccess before other configuration methods");
/*     */     
/* 297 */     this.bindingResult = createDirectFieldBindingResult();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractPropertyBindingResult createDirectFieldBindingResult()
/*     */   {
/* 307 */     DirectFieldBindingResult result = new DirectFieldBindingResult(getTarget(), getObjectName(), isAutoGrowNestedPaths());
/*     */     
/* 309 */     if (this.conversionService != null) {
/* 310 */       result.initConversion(this.conversionService);
/*     */     }
/* 312 */     if (this.messageCodesResolver != null) {
/* 313 */       result.setMessageCodesResolver(this.messageCodesResolver);
/*     */     }
/*     */     
/* 316 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractPropertyBindingResult getInternalBindingResult()
/*     */   {
/* 324 */     if (this.bindingResult == null) {
/* 325 */       initBeanPropertyAccess();
/*     */     }
/* 327 */     return this.bindingResult;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected ConfigurablePropertyAccessor getPropertyAccessor()
/*     */   {
/* 334 */     return getInternalBindingResult().getPropertyAccessor();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected SimpleTypeConverter getSimpleTypeConverter()
/*     */   {
/* 341 */     if (this.typeConverter == null) {
/* 342 */       this.typeConverter = new SimpleTypeConverter();
/* 343 */       if (this.conversionService != null) {
/* 344 */         this.typeConverter.setConversionService(this.conversionService);
/*     */       }
/*     */     }
/* 347 */     return this.typeConverter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected PropertyEditorRegistry getPropertyEditorRegistry()
/*     */   {
/* 354 */     if (getTarget() != null) {
/* 355 */       return getInternalBindingResult().getPropertyAccessor();
/*     */     }
/*     */     
/* 358 */     return getSimpleTypeConverter();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TypeConverter getTypeConverter()
/*     */   {
/* 366 */     if (getTarget() != null) {
/* 367 */       return getInternalBindingResult().getPropertyAccessor();
/*     */     }
/*     */     
/* 370 */     return getSimpleTypeConverter();
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
/*     */   public BindingResult getBindingResult()
/*     */   {
/* 384 */     return getInternalBindingResult();
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
/*     */   public void setIgnoreUnknownFields(boolean ignoreUnknownFields)
/*     */   {
/* 399 */     this.ignoreUnknownFields = ignoreUnknownFields;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isIgnoreUnknownFields()
/*     */   {
/* 406 */     return this.ignoreUnknownFields;
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
/*     */   public void setIgnoreInvalidFields(boolean ignoreInvalidFields)
/*     */   {
/* 421 */     this.ignoreInvalidFields = ignoreInvalidFields;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isIgnoreInvalidFields()
/*     */   {
/* 428 */     return this.ignoreInvalidFields;
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
/*     */   public void setAllowedFields(String... allowedFields)
/*     */   {
/* 444 */     this.allowedFields = PropertyAccessorUtils.canonicalPropertyNames(allowedFields);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getAllowedFields()
/*     */   {
/* 452 */     return this.allowedFields;
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
/*     */   public void setDisallowedFields(String... disallowedFields)
/*     */   {
/* 468 */     this.disallowedFields = PropertyAccessorUtils.canonicalPropertyNames(disallowedFields);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getDisallowedFields()
/*     */   {
/* 476 */     return this.disallowedFields;
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
/*     */   public void setRequiredFields(String... requiredFields)
/*     */   {
/* 490 */     this.requiredFields = PropertyAccessorUtils.canonicalPropertyNames(requiredFields);
/* 491 */     if (logger.isDebugEnabled()) {
/* 492 */       logger.debug("DataBinder requires binding of required fields [" + 
/* 493 */         StringUtils.arrayToCommaDelimitedString(requiredFields) + "]");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getRequiredFields()
/*     */   {
/* 502 */     return this.requiredFields;
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
/*     */   @Deprecated
/*     */   public void setExtractOldValueForEditor(boolean extractOldValueForEditor)
/*     */   {
/* 516 */     getPropertyAccessor().setExtractOldValueForEditor(extractOldValueForEditor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMessageCodesResolver(MessageCodesResolver messageCodesResolver)
/*     */   {
/* 527 */     Assert.state(this.messageCodesResolver == null, "DataBinder is already initialized with MessageCodesResolver");
/* 528 */     this.messageCodesResolver = messageCodesResolver;
/* 529 */     if ((this.bindingResult != null) && (messageCodesResolver != null)) {
/* 530 */       this.bindingResult.setMessageCodesResolver(messageCodesResolver);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBindingErrorProcessor(BindingErrorProcessor bindingErrorProcessor)
/*     */   {
/* 541 */     Assert.notNull(bindingErrorProcessor, "BindingErrorProcessor must not be null");
/* 542 */     this.bindingErrorProcessor = bindingErrorProcessor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public BindingErrorProcessor getBindingErrorProcessor()
/*     */   {
/* 549 */     return this.bindingErrorProcessor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValidator(Validator validator)
/*     */   {
/* 558 */     assertValidators(new Validator[] { validator });
/* 559 */     this.validators.clear();
/* 560 */     this.validators.add(validator);
/*     */   }
/*     */   
/*     */   private void assertValidators(Validator... validators) {
/* 564 */     Assert.notNull(validators, "Validators required");
/* 565 */     for (Validator validator : validators) {
/* 566 */       if ((validator != null) && (getTarget() != null) && (!validator.supports(getTarget().getClass()))) {
/* 567 */         throw new IllegalStateException("Invalid target for Validator [" + validator + "]: " + getTarget());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addValidators(Validator... validators)
/*     */   {
/* 578 */     assertValidators(validators);
/* 579 */     this.validators.addAll(Arrays.asList(validators));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void replaceValidators(Validator... validators)
/*     */   {
/* 588 */     assertValidators(validators);
/* 589 */     this.validators.clear();
/* 590 */     this.validators.addAll(Arrays.asList(validators));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Validator getValidator()
/*     */   {
/* 597 */     return this.validators.size() > 0 ? (Validator)this.validators.get(0) : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<Validator> getValidators()
/*     */   {
/* 604 */     return Collections.unmodifiableList(this.validators);
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
/*     */   public void setConversionService(ConversionService conversionService)
/*     */   {
/* 617 */     Assert.state(this.conversionService == null, "DataBinder is already initialized with ConversionService");
/* 618 */     this.conversionService = conversionService;
/* 619 */     if ((this.bindingResult != null) && (conversionService != null)) {
/* 620 */       this.bindingResult.initConversion(conversionService);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ConversionService getConversionService()
/*     */   {
/* 628 */     return this.conversionService;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCustomFormatter(Formatter<?> formatter)
/*     */   {
/* 640 */     FormatterPropertyEditorAdapter adapter = new FormatterPropertyEditorAdapter(formatter);
/* 641 */     getPropertyEditorRegistry().registerCustomEditor(adapter.getFieldType(), adapter);
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
/*     */   public void addCustomFormatter(Formatter<?> formatter, String... fields)
/*     */   {
/* 654 */     FormatterPropertyEditorAdapter adapter = new FormatterPropertyEditorAdapter(formatter);
/* 655 */     Class<?> fieldType = adapter.getFieldType();
/* 656 */     if (ObjectUtils.isEmpty(fields)) {
/* 657 */       getPropertyEditorRegistry().registerCustomEditor(fieldType, adapter);
/*     */     }
/*     */     else {
/* 660 */       for (String field : fields) {
/* 661 */         getPropertyEditorRegistry().registerCustomEditor(fieldType, field, adapter);
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
/*     */   public void addCustomFormatter(Formatter<?> formatter, Class<?>... fieldTypes)
/*     */   {
/* 678 */     FormatterPropertyEditorAdapter adapter = new FormatterPropertyEditorAdapter(formatter);
/* 679 */     if (ObjectUtils.isEmpty(fieldTypes)) {
/* 680 */       getPropertyEditorRegistry().registerCustomEditor(adapter.getFieldType(), adapter);
/*     */     }
/*     */     else {
/* 683 */       for (Class<?> fieldType : fieldTypes) {
/* 684 */         getPropertyEditorRegistry().registerCustomEditor(fieldType, adapter);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor)
/*     */   {
/* 691 */     getPropertyEditorRegistry().registerCustomEditor(requiredType, propertyEditor);
/*     */   }
/*     */   
/*     */   public void registerCustomEditor(Class<?> requiredType, String field, PropertyEditor propertyEditor)
/*     */   {
/* 696 */     getPropertyEditorRegistry().registerCustomEditor(requiredType, field, propertyEditor);
/*     */   }
/*     */   
/*     */   public PropertyEditor findCustomEditor(Class<?> requiredType, String propertyPath)
/*     */   {
/* 701 */     return getPropertyEditorRegistry().findCustomEditor(requiredType, propertyPath);
/*     */   }
/*     */   
/*     */   public <T> T convertIfNecessary(Object value, Class<T> requiredType) throws TypeMismatchException
/*     */   {
/* 706 */     return (T)getTypeConverter().convertIfNecessary(value, requiredType);
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> T convertIfNecessary(Object value, Class<T> requiredType, MethodParameter methodParam)
/*     */     throws TypeMismatchException
/*     */   {
/* 713 */     return (T)getTypeConverter().convertIfNecessary(value, requiredType, methodParam);
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> T convertIfNecessary(Object value, Class<T> requiredType, Field field)
/*     */     throws TypeMismatchException
/*     */   {
/* 720 */     return (T)getTypeConverter().convertIfNecessary(value, requiredType, field);
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
/*     */   public void bind(PropertyValues pvs)
/*     */   {
/* 738 */     MutablePropertyValues mpvs = (pvs instanceof MutablePropertyValues) ? (MutablePropertyValues)pvs : new MutablePropertyValues(pvs);
/*     */     
/* 740 */     doBind(mpvs);
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
/*     */   protected void doBind(MutablePropertyValues mpvs)
/*     */   {
/* 753 */     checkAllowedFields(mpvs);
/* 754 */     checkRequiredFields(mpvs);
/* 755 */     applyPropertyValues(mpvs);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void checkAllowedFields(MutablePropertyValues mpvs)
/*     */   {
/* 766 */     PropertyValue[] pvs = mpvs.getPropertyValues();
/* 767 */     for (PropertyValue pv : pvs) {
/* 768 */       String field = PropertyAccessorUtils.canonicalPropertyName(pv.getName());
/* 769 */       if (!isAllowed(field)) {
/* 770 */         mpvs.removePropertyValue(pv);
/* 771 */         getBindingResult().recordSuppressedField(field);
/* 772 */         if (logger.isDebugEnabled()) {
/* 773 */           logger.debug("Field [" + field + "] has been removed from PropertyValues and will not be bound, because it has not been found in the list of allowed fields");
/*     */         }
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
/*     */ 
/*     */ 
/*     */   protected boolean isAllowed(String field)
/*     */   {
/* 795 */     String[] allowed = getAllowedFields();
/* 796 */     String[] disallowed = getDisallowedFields();
/* 797 */     return ((ObjectUtils.isEmpty(allowed)) || (PatternMatchUtils.simpleMatch(allowed, field))) && (
/* 798 */       (ObjectUtils.isEmpty(disallowed)) || (!PatternMatchUtils.simpleMatch(disallowed, field)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void checkRequiredFields(MutablePropertyValues mpvs)
/*     */   {
/* 810 */     String[] requiredFields = getRequiredFields();
/* 811 */     if (!ObjectUtils.isEmpty(requiredFields)) {
/* 812 */       Map<String, PropertyValue> propertyValues = new HashMap();
/* 813 */       PropertyValue[] pvs = mpvs.getPropertyValues();
/* 814 */       for (PropertyValue pv : pvs) {
/* 815 */         String canonicalName = PropertyAccessorUtils.canonicalPropertyName(pv.getName());
/* 816 */         propertyValues.put(canonicalName, pv);
/*     */       }
/* 818 */       for (String field : requiredFields) {
/* 819 */         PropertyValue pv = (PropertyValue)propertyValues.get(field);
/* 820 */         boolean empty = (pv == null) || (pv.getValue() == null);
/* 821 */         if (!empty) {
/* 822 */           if ((pv.getValue() instanceof String)) {
/* 823 */             empty = !StringUtils.hasText((String)pv.getValue());
/*     */           }
/* 825 */           else if ((pv.getValue() instanceof String[])) {
/* 826 */             String[] values = (String[])pv.getValue();
/* 827 */             empty = (values.length == 0) || (!StringUtils.hasText(values[0]));
/*     */           }
/*     */         }
/* 830 */         if (empty)
/*     */         {
/* 832 */           getBindingErrorProcessor().processMissingFieldError(field, getInternalBindingResult());
/*     */           
/*     */ 
/* 835 */           if (pv != null) {
/* 836 */             mpvs.removePropertyValue(pv);
/* 837 */             propertyValues.remove(field);
/*     */           }
/*     */         }
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
/*     */   protected void applyPropertyValues(MutablePropertyValues mpvs)
/*     */   {
/*     */     try
/*     */     {
/* 859 */       getPropertyAccessor().setPropertyValues(mpvs, isIgnoreUnknownFields(), isIgnoreInvalidFields());
/*     */     }
/*     */     catch (PropertyBatchUpdateException ex)
/*     */     {
/* 863 */       for (PropertyAccessException pae : ex.getPropertyAccessExceptions()) {
/* 864 */         getBindingErrorProcessor().processPropertyAccessException(pae, getInternalBindingResult());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void validate()
/*     */   {
/* 876 */     for (Validator validator : this.validators) {
/* 877 */       validator.validate(getTarget(), getBindingResult());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void validate(Object... validationHints)
/*     */   {
/* 889 */     for (Validator validator : getValidators()) {
/* 890 */       if ((!ObjectUtils.isEmpty(validationHints)) && ((validator instanceof SmartValidator))) {
/* 891 */         ((SmartValidator)validator).validate(getTarget(), getBindingResult(), validationHints);
/*     */       }
/* 893 */       else if (validator != null) {
/* 894 */         validator.validate(getTarget(), getBindingResult());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<?, ?> close()
/*     */     throws BindException
/*     */   {
/* 907 */     if (getBindingResult().hasErrors()) {
/* 908 */       throw new BindException(getBindingResult());
/*     */     }
/* 910 */     return getBindingResult().getModel();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @UsesJava8
/*     */   private static class OptionalUnwrapper
/*     */   {
/*     */     public static Object unwrap(Object optionalObject)
/*     */     {
/* 921 */       Optional<?> optional = (Optional)optionalObject;
/* 922 */       if (!optional.isPresent()) {
/* 923 */         return null;
/*     */       }
/* 925 */       Object result = optional.get();
/* 926 */       Assert.isTrue(!(result instanceof Optional), "Multi-level Optional usage not supported");
/* 927 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\validation\DataBinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */