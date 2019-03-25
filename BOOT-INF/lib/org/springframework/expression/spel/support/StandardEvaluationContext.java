/*     */ package org.springframework.expression.spel.support;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.BeanResolver;
/*     */ import org.springframework.expression.ConstructorResolver;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.MethodFilter;
/*     */ import org.springframework.expression.MethodResolver;
/*     */ import org.springframework.expression.OperatorOverloader;
/*     */ import org.springframework.expression.PropertyAccessor;
/*     */ import org.springframework.expression.TypeComparator;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.TypeLocator;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StandardEvaluationContext
/*     */   implements EvaluationContext
/*     */ {
/*     */   private TypedValue rootObject;
/*     */   private List<ConstructorResolver> constructorResolvers;
/*     */   private List<MethodResolver> methodResolvers;
/*     */   private BeanResolver beanResolver;
/*     */   private ReflectiveMethodResolver reflectiveMethodResolver;
/*     */   private List<PropertyAccessor> propertyAccessors;
/*     */   private TypeLocator typeLocator;
/*     */   private TypeConverter typeConverter;
/*  67 */   private TypeComparator typeComparator = new StandardTypeComparator();
/*     */   
/*  69 */   private OperatorOverloader operatorOverloader = new StandardOperatorOverloader();
/*     */   
/*  71 */   private final Map<String, Object> variables = new HashMap();
/*     */   
/*     */   public StandardEvaluationContext()
/*     */   {
/*  75 */     setRootObject(null);
/*     */   }
/*     */   
/*     */   public StandardEvaluationContext(Object rootObject) {
/*  79 */     setRootObject(rootObject);
/*     */   }
/*     */   
/*     */   public void setRootObject(Object rootObject, TypeDescriptor typeDescriptor)
/*     */   {
/*  84 */     this.rootObject = new TypedValue(rootObject, typeDescriptor);
/*     */   }
/*     */   
/*     */   public void setRootObject(Object rootObject) {
/*  88 */     this.rootObject = (rootObject != null ? new TypedValue(rootObject) : TypedValue.NULL);
/*     */   }
/*     */   
/*     */   public TypedValue getRootObject()
/*     */   {
/*  93 */     return this.rootObject;
/*     */   }
/*     */   
/*     */   public void setPropertyAccessors(List<PropertyAccessor> propertyAccessors) {
/*  97 */     this.propertyAccessors = propertyAccessors;
/*     */   }
/*     */   
/*     */   public List<PropertyAccessor> getPropertyAccessors()
/*     */   {
/* 102 */     ensurePropertyAccessorsInitialized();
/* 103 */     return this.propertyAccessors;
/*     */   }
/*     */   
/*     */   public void addPropertyAccessor(PropertyAccessor accessor) {
/* 107 */     ensurePropertyAccessorsInitialized();
/* 108 */     this.propertyAccessors.add(this.propertyAccessors.size() - 1, accessor);
/*     */   }
/*     */   
/*     */   public boolean removePropertyAccessor(PropertyAccessor accessor) {
/* 112 */     return this.propertyAccessors.remove(accessor);
/*     */   }
/*     */   
/*     */   public void setConstructorResolvers(List<ConstructorResolver> constructorResolvers) {
/* 116 */     this.constructorResolvers = constructorResolvers;
/*     */   }
/*     */   
/*     */   public List<ConstructorResolver> getConstructorResolvers()
/*     */   {
/* 121 */     ensureConstructorResolversInitialized();
/* 122 */     return this.constructorResolvers;
/*     */   }
/*     */   
/*     */   public void addConstructorResolver(ConstructorResolver resolver) {
/* 126 */     ensureConstructorResolversInitialized();
/* 127 */     this.constructorResolvers.add(this.constructorResolvers.size() - 1, resolver);
/*     */   }
/*     */   
/*     */   public boolean removeConstructorResolver(ConstructorResolver resolver) {
/* 131 */     ensureConstructorResolversInitialized();
/* 132 */     return this.constructorResolvers.remove(resolver);
/*     */   }
/*     */   
/*     */   public void setMethodResolvers(List<MethodResolver> methodResolvers) {
/* 136 */     this.methodResolvers = methodResolvers;
/*     */   }
/*     */   
/*     */   public List<MethodResolver> getMethodResolvers()
/*     */   {
/* 141 */     ensureMethodResolversInitialized();
/* 142 */     return this.methodResolvers;
/*     */   }
/*     */   
/*     */   public void addMethodResolver(MethodResolver resolver) {
/* 146 */     ensureMethodResolversInitialized();
/* 147 */     this.methodResolvers.add(this.methodResolvers.size() - 1, resolver);
/*     */   }
/*     */   
/*     */   public boolean removeMethodResolver(MethodResolver methodResolver) {
/* 151 */     ensureMethodResolversInitialized();
/* 152 */     return this.methodResolvers.remove(methodResolver);
/*     */   }
/*     */   
/*     */   public void setBeanResolver(BeanResolver beanResolver) {
/* 156 */     this.beanResolver = beanResolver;
/*     */   }
/*     */   
/*     */   public BeanResolver getBeanResolver()
/*     */   {
/* 161 */     return this.beanResolver;
/*     */   }
/*     */   
/*     */   public void setTypeLocator(TypeLocator typeLocator) {
/* 165 */     Assert.notNull(typeLocator, "TypeLocator must not be null");
/* 166 */     this.typeLocator = typeLocator;
/*     */   }
/*     */   
/*     */   public TypeLocator getTypeLocator()
/*     */   {
/* 171 */     if (this.typeLocator == null) {
/* 172 */       this.typeLocator = new StandardTypeLocator();
/*     */     }
/* 174 */     return this.typeLocator;
/*     */   }
/*     */   
/*     */   public void setTypeConverter(TypeConverter typeConverter) {
/* 178 */     Assert.notNull(typeConverter, "TypeConverter must not be null");
/* 179 */     this.typeConverter = typeConverter;
/*     */   }
/*     */   
/*     */   public TypeConverter getTypeConverter()
/*     */   {
/* 184 */     if (this.typeConverter == null) {
/* 185 */       this.typeConverter = new StandardTypeConverter();
/*     */     }
/* 187 */     return this.typeConverter;
/*     */   }
/*     */   
/*     */   public void setTypeComparator(TypeComparator typeComparator) {
/* 191 */     Assert.notNull(typeComparator, "TypeComparator must not be null");
/* 192 */     this.typeComparator = typeComparator;
/*     */   }
/*     */   
/*     */   public TypeComparator getTypeComparator()
/*     */   {
/* 197 */     return this.typeComparator;
/*     */   }
/*     */   
/*     */   public void setOperatorOverloader(OperatorOverloader operatorOverloader) {
/* 201 */     Assert.notNull(operatorOverloader, "OperatorOverloader must not be null");
/* 202 */     this.operatorOverloader = operatorOverloader;
/*     */   }
/*     */   
/*     */   public OperatorOverloader getOperatorOverloader()
/*     */   {
/* 207 */     return this.operatorOverloader;
/*     */   }
/*     */   
/*     */   public void setVariable(String name, Object value)
/*     */   {
/* 212 */     this.variables.put(name, value);
/*     */   }
/*     */   
/*     */   public void setVariables(Map<String, Object> variables) {
/* 216 */     this.variables.putAll(variables);
/*     */   }
/*     */   
/*     */   public void registerFunction(String name, Method method) {
/* 220 */     this.variables.put(name, method);
/*     */   }
/*     */   
/*     */   public Object lookupVariable(String name)
/*     */   {
/* 225 */     return this.variables.get(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void registerMethodFilter(Class<?> type, MethodFilter filter)
/*     */     throws IllegalStateException
/*     */   {
/* 238 */     ensureMethodResolversInitialized();
/* 239 */     if (this.reflectiveMethodResolver != null) {
/* 240 */       this.reflectiveMethodResolver.registerMethodFilter(type, filter);
/*     */     }
/*     */     else {
/* 243 */       throw new IllegalStateException("Method filter cannot be set as the reflective method resolver is not in use");
/*     */     }
/*     */   }
/*     */   
/*     */   private void ensurePropertyAccessorsInitialized()
/*     */   {
/* 249 */     if (this.propertyAccessors == null) {
/* 250 */       initializePropertyAccessors();
/*     */     }
/*     */   }
/*     */   
/*     */   private synchronized void initializePropertyAccessors() {
/* 255 */     if (this.propertyAccessors == null) {
/* 256 */       List<PropertyAccessor> defaultAccessors = new ArrayList();
/* 257 */       defaultAccessors.add(new ReflectivePropertyAccessor());
/* 258 */       this.propertyAccessors = defaultAccessors;
/*     */     }
/*     */   }
/*     */   
/*     */   private void ensureConstructorResolversInitialized() {
/* 263 */     if (this.constructorResolvers == null) {
/* 264 */       initializeConstructorResolvers();
/*     */     }
/*     */   }
/*     */   
/*     */   private synchronized void initializeConstructorResolvers() {
/* 269 */     if (this.constructorResolvers == null) {
/* 270 */       List<ConstructorResolver> defaultResolvers = new ArrayList();
/* 271 */       defaultResolvers.add(new ReflectiveConstructorResolver());
/* 272 */       this.constructorResolvers = defaultResolvers;
/*     */     }
/*     */   }
/*     */   
/*     */   private void ensureMethodResolversInitialized() {
/* 277 */     if (this.methodResolvers == null) {
/* 278 */       initializeMethodResolvers();
/*     */     }
/*     */   }
/*     */   
/*     */   private synchronized void initializeMethodResolvers() {
/* 283 */     if (this.methodResolvers == null) {
/* 284 */       List<MethodResolver> defaultResolvers = new ArrayList();
/* 285 */       this.reflectiveMethodResolver = new ReflectiveMethodResolver();
/* 286 */       defaultResolvers.add(this.reflectiveMethodResolver);
/* 287 */       this.methodResolvers = defaultResolvers;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\support\StandardEvaluationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */