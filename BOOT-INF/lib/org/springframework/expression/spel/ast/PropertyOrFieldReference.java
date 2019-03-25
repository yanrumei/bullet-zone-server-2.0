/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.PropertyAccessor;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.CompilablePropertyAccessor;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.SpelParserConfiguration;
/*     */ import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertyOrFieldReference
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private final boolean nullSafe;
/*     */   private final String name;
/*     */   private volatile PropertyAccessor cachedReadAccessor;
/*     */   private volatile PropertyAccessor cachedWriteAccessor;
/*     */   
/*     */   public PropertyOrFieldReference(boolean nullSafe, String propertyOrFieldName, int pos)
/*     */   {
/*  58 */     super(pos, new SpelNodeImpl[0]);
/*  59 */     this.nullSafe = nullSafe;
/*  60 */     this.name = propertyOrFieldName;
/*     */   }
/*     */   
/*     */   public boolean isNullSafe()
/*     */   {
/*  65 */     return this.nullSafe;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  69 */     return this.name;
/*     */   }
/*     */   
/*     */   public ValueRef getValueRef(ExpressionState state)
/*     */     throws EvaluationException
/*     */   {
/*  75 */     return new AccessorLValue(this, state.getActiveContextObject(), state.getEvaluationContext(), state
/*  76 */       .getConfiguration().isAutoGrowNullReferences());
/*     */   }
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException
/*     */   {
/*  81 */     TypedValue tv = getValueInternal(state.getActiveContextObject(), state.getEvaluationContext(), state
/*  82 */       .getConfiguration().isAutoGrowNullReferences());
/*  83 */     PropertyAccessor accessorToUse = this.cachedReadAccessor;
/*  84 */     if ((accessorToUse instanceof CompilablePropertyAccessor)) {
/*  85 */       CompilablePropertyAccessor accessor = (CompilablePropertyAccessor)accessorToUse;
/*  86 */       this.exitTypeDescriptor = CodeFlow.toDescriptor(accessor.getPropertyType());
/*     */     }
/*  88 */     return tv;
/*     */   }
/*     */   
/*     */   private TypedValue getValueInternal(TypedValue contextObject, EvaluationContext evalContext, boolean isAutoGrowNullReferences)
/*     */     throws EvaluationException
/*     */   {
/*  94 */     TypedValue result = readProperty(contextObject, evalContext, this.name);
/*     */     
/*     */ 
/*  97 */     if ((result.getValue() == null) && (isAutoGrowNullReferences)) {
/*  98 */       if (nextChildIs(new Class[] { Indexer.class, PropertyOrFieldReference.class })) {
/*  99 */         TypeDescriptor resultDescriptor = result.getTypeDescriptor();
/*     */         
/* 101 */         if (List.class == resultDescriptor.getType()) {
/*     */           try {
/* 103 */             if (isWritableProperty(this.name, contextObject, evalContext)) {
/* 104 */               List<?> newList = (List)ArrayList.class.newInstance();
/* 105 */               writeProperty(contextObject, evalContext, this.name, newList);
/* 106 */               result = readProperty(contextObject, evalContext, this.name);
/*     */             }
/*     */           }
/*     */           catch (InstantiationException ex) {
/* 110 */             throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.UNABLE_TO_CREATE_LIST_FOR_INDEXING, new Object[0]);
/*     */           }
/*     */           catch (IllegalAccessException ex)
/*     */           {
/* 114 */             throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.UNABLE_TO_CREATE_LIST_FOR_INDEXING, new Object[0]);
/*     */           }
/*     */           
/*     */         }
/* 118 */         else if (Map.class == resultDescriptor.getType()) {
/*     */           try {
/* 120 */             if (isWritableProperty(this.name, contextObject, evalContext)) {
/* 121 */               Map<?, ?> newMap = (Map)HashMap.class.newInstance();
/* 122 */               writeProperty(contextObject, evalContext, this.name, newMap);
/* 123 */               result = readProperty(contextObject, evalContext, this.name);
/*     */             }
/*     */           }
/*     */           catch (InstantiationException ex) {
/* 127 */             throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.UNABLE_TO_CREATE_MAP_FOR_INDEXING, new Object[0]);
/*     */           }
/*     */           catch (IllegalAccessException ex)
/*     */           {
/* 131 */             throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.UNABLE_TO_CREATE_MAP_FOR_INDEXING, new Object[0]);
/*     */           }
/*     */           
/*     */         }
/*     */         else
/*     */           try
/*     */           {
/* 138 */             if (isWritableProperty(this.name, contextObject, evalContext)) {
/* 139 */               Object newObject = result.getTypeDescriptor().getType().newInstance();
/* 140 */               writeProperty(contextObject, evalContext, this.name, newObject);
/* 141 */               result = readProperty(contextObject, evalContext, this.name);
/*     */             }
/*     */           }
/*     */           catch (InstantiationException ex)
/*     */           {
/* 146 */             throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.UNABLE_TO_DYNAMICALLY_CREATE_OBJECT, new Object[] {result.getTypeDescriptor().getType() });
/*     */           }
/*     */           catch (IllegalAccessException ex)
/*     */           {
/* 150 */             throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.UNABLE_TO_DYNAMICALLY_CREATE_OBJECT, new Object[] {result.getTypeDescriptor().getType() });
/*     */           }
/*     */       }
/*     */     }
/* 154 */     return result;
/*     */   }
/*     */   
/*     */   public void setValue(ExpressionState state, Object newValue) throws EvaluationException
/*     */   {
/* 159 */     writeProperty(state.getActiveContextObject(), state.getEvaluationContext(), this.name, newValue);
/*     */   }
/*     */   
/*     */   public boolean isWritable(ExpressionState state) throws EvaluationException
/*     */   {
/* 164 */     return isWritableProperty(this.name, state.getActiveContextObject(), state.getEvaluationContext());
/*     */   }
/*     */   
/*     */   public String toStringAST()
/*     */   {
/* 169 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private TypedValue readProperty(TypedValue contextObject, EvaluationContext evalContext, String name)
/*     */     throws EvaluationException
/*     */   {
/* 180 */     Object targetObject = contextObject.getValue();
/* 181 */     if ((targetObject == null) && (this.nullSafe)) {
/* 182 */       return TypedValue.NULL;
/*     */     }
/*     */     
/* 185 */     PropertyAccessor accessorToUse = this.cachedReadAccessor;
/* 186 */     if (accessorToUse != null) {
/* 187 */       if (evalContext.getPropertyAccessors().contains(accessorToUse)) {
/*     */         try {
/* 189 */           return accessorToUse.read(evalContext, contextObject.getValue(), name);
/*     */         }
/*     */         catch (Exception localException1) {}
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 196 */       this.cachedReadAccessor = null;
/*     */     }
/*     */     
/*     */ 
/* 200 */     List<PropertyAccessor> accessorsToTry = getPropertyAccessorsToTry(contextObject.getValue(), evalContext.getPropertyAccessors());
/*     */     
/*     */ 
/*     */ 
/* 204 */     if (accessorsToTry != null) {
/*     */       try {
/* 206 */         for (PropertyAccessor accessor : accessorsToTry) {
/* 207 */           if (accessor.canRead(evalContext, contextObject.getValue(), name)) {
/* 208 */             if ((accessor instanceof ReflectivePropertyAccessor)) {
/* 209 */               accessor = ((ReflectivePropertyAccessor)accessor).createOptimalAccessor(evalContext, contextObject
/* 210 */                 .getValue(), name);
/*     */             }
/* 212 */             this.cachedReadAccessor = accessor;
/* 213 */             return accessor.read(evalContext, contextObject.getValue(), name);
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Exception ex) {
/* 218 */         throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_DURING_PROPERTY_READ, new Object[] { name, ex.getMessage() });
/*     */       }
/*     */     }
/* 221 */     if (contextObject.getValue() == null) {
/* 222 */       throw new SpelEvaluationException(SpelMessage.PROPERTY_OR_FIELD_NOT_READABLE_ON_NULL, new Object[] { name });
/*     */     }
/*     */     
/*     */ 
/* 226 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.PROPERTY_OR_FIELD_NOT_READABLE, new Object[] { name, FormatHelper.formatClassNameForMessage(getObjectClass(contextObject.getValue())) });
/*     */   }
/*     */   
/*     */ 
/*     */   private void writeProperty(TypedValue contextObject, EvaluationContext evalContext, String name, Object newValue)
/*     */     throws EvaluationException
/*     */   {
/* 233 */     if ((contextObject.getValue() == null) && (this.nullSafe)) {
/* 234 */       return;
/*     */     }
/*     */     
/* 237 */     PropertyAccessor accessorToUse = this.cachedWriteAccessor;
/* 238 */     if (accessorToUse != null) {
/* 239 */       if (evalContext.getPropertyAccessors().contains(accessorToUse)) {
/*     */         try {
/* 241 */           accessorToUse.write(evalContext, contextObject.getValue(), name, newValue);
/* 242 */           return;
/*     */         }
/*     */         catch (Exception localException) {}
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 249 */       this.cachedWriteAccessor = null;
/*     */     }
/*     */     
/*     */ 
/* 253 */     List<PropertyAccessor> accessorsToTry = getPropertyAccessorsToTry(contextObject.getValue(), evalContext.getPropertyAccessors());
/* 254 */     if (accessorsToTry != null) {
/*     */       try {
/* 256 */         for (PropertyAccessor accessor : accessorsToTry) {
/* 257 */           if (accessor.canWrite(evalContext, contextObject.getValue(), name)) {
/* 258 */             this.cachedWriteAccessor = accessor;
/* 259 */             accessor.write(evalContext, contextObject.getValue(), name, newValue);
/* 260 */             return;
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (AccessException ex)
/*     */       {
/* 266 */         throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.EXCEPTION_DURING_PROPERTY_WRITE, new Object[] { name, ex.getMessage() });
/*     */       }
/*     */     }
/* 269 */     if (contextObject.getValue() == null) {
/* 270 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.PROPERTY_OR_FIELD_NOT_WRITABLE_ON_NULL, new Object[] { name });
/*     */     }
/*     */     
/*     */ 
/* 274 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.PROPERTY_OR_FIELD_NOT_WRITABLE, new Object[] { name, FormatHelper.formatClassNameForMessage(getObjectClass(contextObject.getValue())) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isWritableProperty(String name, TypedValue contextObject, EvaluationContext evalContext)
/*     */     throws EvaluationException
/*     */   {
/* 282 */     List<PropertyAccessor> accessorsToTry = getPropertyAccessorsToTry(contextObject.getValue(), evalContext.getPropertyAccessors());
/* 283 */     if (accessorsToTry != null) {
/* 284 */       for (PropertyAccessor accessor : accessorsToTry) {
/*     */         try {
/* 286 */           if (accessor.canWrite(evalContext, contextObject.getValue(), name)) {
/* 287 */             return true;
/*     */           }
/*     */         }
/*     */         catch (AccessException localAccessException) {}
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 295 */     return false;
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
/*     */   private List<PropertyAccessor> getPropertyAccessorsToTry(Object contextObject, List<PropertyAccessor> propertyAccessors)
/*     */   {
/* 311 */     Class<?> targetType = contextObject != null ? contextObject.getClass() : null;
/*     */     
/* 313 */     List<PropertyAccessor> specificAccessors = new ArrayList();
/* 314 */     List<PropertyAccessor> generalAccessors = new ArrayList();
/* 315 */     for (PropertyAccessor resolver : propertyAccessors) {
/* 316 */       Class<?>[] targets = resolver.getSpecificTargetClasses();
/* 317 */       if (targets == null)
/*     */       {
/* 319 */         generalAccessors.add(resolver);
/*     */       }
/* 321 */       else if (targetType != null) {
/* 322 */         for (Class<?> clazz : targets) {
/* 323 */           if (clazz == targetType) {
/* 324 */             specificAccessors.add(resolver);
/* 325 */             break;
/*     */           }
/* 327 */           if (clazz.isAssignableFrom(targetType)) {
/* 328 */             generalAccessors.add(resolver);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 333 */     Object resolvers = new ArrayList();
/* 334 */     ((List)resolvers).addAll(specificAccessors);
/* 335 */     generalAccessors.removeAll(specificAccessors);
/* 336 */     ((List)resolvers).addAll(generalAccessors);
/* 337 */     return (List<PropertyAccessor>)resolvers;
/*     */   }
/*     */   
/*     */   public boolean isCompilable()
/*     */   {
/* 342 */     PropertyAccessor accessorToUse = this.cachedReadAccessor;
/* 343 */     return ((accessorToUse instanceof CompilablePropertyAccessor)) && 
/* 344 */       (((CompilablePropertyAccessor)accessorToUse).isCompilable());
/*     */   }
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf)
/*     */   {
/* 349 */     PropertyAccessor accessorToUse = this.cachedReadAccessor;
/* 350 */     if (!(accessorToUse instanceof CompilablePropertyAccessor)) {
/* 351 */       throw new IllegalStateException("Property accessor is not compilable: " + accessorToUse);
/*     */     }
/* 353 */     ((CompilablePropertyAccessor)accessorToUse).generateCode(this.name, mv, cf);
/* 354 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */   
/*     */ 
/*     */   private static class AccessorLValue
/*     */     implements ValueRef
/*     */   {
/*     */     private final PropertyOrFieldReference ref;
/*     */     
/*     */     private final TypedValue contextObject;
/*     */     
/*     */     private final EvaluationContext evalContext;
/*     */     private final boolean autoGrowNullReferences;
/*     */     
/*     */     public AccessorLValue(PropertyOrFieldReference propertyOrFieldReference, TypedValue activeContextObject, EvaluationContext evalContext, boolean autoGrowNullReferences)
/*     */     {
/* 370 */       this.ref = propertyOrFieldReference;
/* 371 */       this.contextObject = activeContextObject;
/* 372 */       this.evalContext = evalContext;
/* 373 */       this.autoGrowNullReferences = autoGrowNullReferences;
/*     */     }
/*     */     
/*     */ 
/*     */     public TypedValue getValue()
/*     */     {
/* 379 */       TypedValue value = this.ref.getValueInternal(this.contextObject, this.evalContext, this.autoGrowNullReferences);
/* 380 */       PropertyAccessor accessorToUse = this.ref.cachedReadAccessor;
/* 381 */       if ((accessorToUse instanceof CompilablePropertyAccessor))
/*     */       {
/* 383 */         this.ref.exitTypeDescriptor = CodeFlow.toDescriptor(((CompilablePropertyAccessor)accessorToUse).getPropertyType());
/*     */       }
/* 385 */       return value;
/*     */     }
/*     */     
/*     */     public void setValue(Object newValue)
/*     */     {
/* 390 */       this.ref.writeProperty(this.contextObject, this.evalContext, this.ref.name, newValue);
/*     */     }
/*     */     
/*     */     public boolean isWritable()
/*     */     {
/* 395 */       return this.ref.isWritableProperty(this.ref.name, this.contextObject, this.evalContext);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\ast\PropertyOrFieldReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */