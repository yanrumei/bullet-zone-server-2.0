/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.PropertyAccessor;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.SpelNode;
/*     */ import org.springframework.expression.spel.SpelParserConfiguration;
/*     */ import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
/*     */ import org.springframework.expression.spel.support.ReflectivePropertyAccessor.OptimalPropertyAccessor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Indexer
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private String cachedReadName;
/*     */   private Class<?> cachedReadTargetType;
/*     */   private PropertyAccessor cachedReadAccessor;
/*     */   private String cachedWriteName;
/*     */   private Class<?> cachedWriteTargetType;
/*     */   private PropertyAccessor cachedWriteAccessor;
/*     */   private IndexedType indexedType;
/*     */   
/*     */   private static enum IndexedType
/*     */   {
/*  54 */     ARRAY,  LIST,  MAP,  STRING,  OBJECT;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private IndexedType() {}
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
/*     */   public Indexer(int pos, SpelNodeImpl expr)
/*     */   {
/*  83 */     super(pos, new SpelNodeImpl[] { expr });
/*     */   }
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state)
/*     */     throws EvaluationException
/*     */   {
/*  89 */     return getValueRef(state).getValue();
/*     */   }
/*     */   
/*     */   public void setValue(ExpressionState state, Object newValue) throws EvaluationException
/*     */   {
/*  94 */     getValueRef(state).setValue(newValue);
/*     */   }
/*     */   
/*     */   public boolean isWritable(ExpressionState expressionState) throws SpelEvaluationException
/*     */   {
/*  99 */     return true;
/*     */   }
/*     */   
/*     */   protected ValueRef getValueRef(ExpressionState state)
/*     */     throws EvaluationException
/*     */   {
/* 105 */     TypedValue context = state.getActiveContextObject();
/* 106 */     Object targetObject = context.getValue();
/* 107 */     TypeDescriptor targetDescriptor = context.getTypeDescriptor();
/* 108 */     TypedValue indexValue = null;
/* 109 */     Object index = null;
/*     */     
/*     */ 
/*     */ 
/* 113 */     if (((targetObject instanceof Map)) && ((this.children[0] instanceof PropertyOrFieldReference))) {
/* 114 */       PropertyOrFieldReference reference = (PropertyOrFieldReference)this.children[0];
/* 115 */       index = reference.getName();
/* 116 */       indexValue = new TypedValue(index);
/*     */     }
/*     */     else
/*     */     {
/*     */       try
/*     */       {
/* 122 */         state.pushActiveContextObject(state.getRootContextObject());
/* 123 */         indexValue = this.children[0].getValueInternal(state);
/* 124 */         index = indexValue.getValue();
/*     */       }
/*     */       finally {
/* 127 */         state.popActiveContextObject();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 132 */     if ((targetObject instanceof Map)) {
/* 133 */       Object key = index;
/* 134 */       if (targetDescriptor.getMapKeyTypeDescriptor() != null) {
/* 135 */         key = state.convertValue(key, targetDescriptor.getMapKeyTypeDescriptor());
/*     */       }
/* 137 */       this.indexedType = IndexedType.MAP;
/* 138 */       return new MapIndexingValueRef(state.getTypeConverter(), (Map)targetObject, key, targetDescriptor);
/*     */     }
/*     */     
/* 141 */     if (targetObject == null) {
/* 142 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.CANNOT_INDEX_INTO_NULL_VALUE, new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 147 */     if ((targetObject.getClass().isArray()) || ((targetObject instanceof Collection)) || ((targetObject instanceof String))) {
/* 148 */       int idx = ((Integer)state.convertValue(index, TypeDescriptor.valueOf(Integer.class))).intValue();
/* 149 */       if (targetObject.getClass().isArray()) {
/* 150 */         this.indexedType = IndexedType.ARRAY;
/* 151 */         return new ArrayIndexingValueRef(state.getTypeConverter(), targetObject, idx, targetDescriptor);
/*     */       }
/* 153 */       if ((targetObject instanceof Collection)) {
/* 154 */         if ((targetObject instanceof List)) {
/* 155 */           this.indexedType = IndexedType.LIST;
/*     */         }
/* 157 */         return new CollectionIndexingValueRef((Collection)targetObject, idx, targetDescriptor, state
/* 158 */           .getTypeConverter(), state.getConfiguration().isAutoGrowCollections(), state
/* 159 */           .getConfiguration().getMaximumAutoGrowSize());
/*     */       }
/*     */       
/* 162 */       this.indexedType = IndexedType.STRING;
/* 163 */       return new StringIndexingLValue((String)targetObject, idx, targetDescriptor);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 169 */     if (String.class == indexValue.getTypeDescriptor().getType()) {
/* 170 */       this.indexedType = IndexedType.OBJECT;
/* 171 */       return new PropertyIndexingValueRef(targetObject, (String)indexValue.getValue(), state
/* 172 */         .getEvaluationContext(), targetDescriptor);
/*     */     }
/*     */     
/*     */ 
/* 176 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, new Object[] {targetDescriptor.toString() });
/*     */   }
/*     */   
/*     */   public boolean isCompilable()
/*     */   {
/* 181 */     if (this.indexedType == IndexedType.ARRAY) {
/* 182 */       return this.exitTypeDescriptor != null;
/*     */     }
/* 184 */     if (this.indexedType == IndexedType.LIST) {
/* 185 */       return this.children[0].isCompilable();
/*     */     }
/* 187 */     if (this.indexedType == IndexedType.MAP) {
/* 188 */       return ((this.children[0] instanceof PropertyOrFieldReference)) || (this.children[0].isCompilable());
/*     */     }
/* 190 */     if (this.indexedType == IndexedType.OBJECT)
/*     */     {
/* 192 */       if ((this.cachedReadAccessor != null) && ((this.cachedReadAccessor instanceof ReflectivePropertyAccessor.OptimalPropertyAccessor)))
/*     */       {
/* 194 */         if ((getChild(0) instanceof StringLiteral))
/* 195 */           return true;
/*     */       }
/*     */     }
/* 198 */     return false;
/*     */   }
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf)
/*     */   {
/* 203 */     String descriptor = cf.lastDescriptor();
/* 204 */     if (descriptor == null)
/*     */     {
/* 206 */       cf.loadTarget(mv);
/*     */     }
/*     */     
/* 209 */     if (this.indexedType == IndexedType.ARRAY) { int insn;
/*     */       int insn;
/* 211 */       if ("D".equals(this.exitTypeDescriptor)) {
/* 212 */         mv.visitTypeInsn(192, "[D");
/* 213 */         insn = 49;
/*     */       } else { int insn;
/* 215 */         if ("F".equals(this.exitTypeDescriptor)) {
/* 216 */           mv.visitTypeInsn(192, "[F");
/* 217 */           insn = 48;
/*     */         } else { int insn;
/* 219 */           if ("J".equals(this.exitTypeDescriptor)) {
/* 220 */             mv.visitTypeInsn(192, "[J");
/* 221 */             insn = 47;
/*     */           } else { int insn;
/* 223 */             if ("I".equals(this.exitTypeDescriptor)) {
/* 224 */               mv.visitTypeInsn(192, "[I");
/* 225 */               insn = 46;
/*     */             } else { int insn;
/* 227 */               if ("S".equals(this.exitTypeDescriptor)) {
/* 228 */                 mv.visitTypeInsn(192, "[S");
/* 229 */                 insn = 53;
/*     */               } else { int insn;
/* 231 */                 if ("B".equals(this.exitTypeDescriptor)) {
/* 232 */                   mv.visitTypeInsn(192, "[B");
/* 233 */                   insn = 51;
/*     */                 } else { int insn;
/* 235 */                   if ("C".equals(this.exitTypeDescriptor)) {
/* 236 */                     mv.visitTypeInsn(192, "[C");
/* 237 */                     insn = 52;
/*     */                   }
/*     */                   else {
/* 240 */                     mv.visitTypeInsn(192, "[" + this.exitTypeDescriptor + (
/* 241 */                       CodeFlow.isPrimitiveArray(this.exitTypeDescriptor) ? "" : ";"));
/*     */                     
/* 243 */                     insn = 50;
/*     */                   } } } } } } }
/* 245 */       SpelNodeImpl index = this.children[0];
/* 246 */       cf.enterCompilationScope();
/* 247 */       index.generateCode(mv, cf);
/* 248 */       cf.exitCompilationScope();
/* 249 */       mv.visitInsn(insn);
/*     */ 
/*     */     }
/* 252 */     else if (this.indexedType == IndexedType.LIST) {
/* 253 */       mv.visitTypeInsn(192, "java/util/List");
/* 254 */       cf.enterCompilationScope();
/* 255 */       this.children[0].generateCode(mv, cf);
/* 256 */       cf.exitCompilationScope();
/* 257 */       mv.visitMethodInsn(185, "java/util/List", "get", "(I)Ljava/lang/Object;", true);
/*     */ 
/*     */     }
/* 260 */     else if (this.indexedType == IndexedType.MAP) {
/* 261 */       mv.visitTypeInsn(192, "java/util/Map");
/*     */       
/*     */ 
/* 264 */       if ((this.children[0] instanceof PropertyOrFieldReference)) {
/* 265 */         PropertyOrFieldReference reference = (PropertyOrFieldReference)this.children[0];
/* 266 */         String mapKeyName = reference.getName();
/* 267 */         mv.visitLdcInsn(mapKeyName);
/*     */       }
/*     */       else {
/* 270 */         cf.enterCompilationScope();
/* 271 */         this.children[0].generateCode(mv, cf);
/* 272 */         cf.exitCompilationScope();
/*     */       }
/* 274 */       mv.visitMethodInsn(185, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
/*     */ 
/*     */     }
/* 277 */     else if (this.indexedType == IndexedType.OBJECT) {
/* 278 */       ReflectivePropertyAccessor.OptimalPropertyAccessor accessor = (ReflectivePropertyAccessor.OptimalPropertyAccessor)this.cachedReadAccessor;
/*     */       
/* 280 */       Member member = accessor.member;
/* 281 */       boolean isStatic = Modifier.isStatic(member.getModifiers());
/* 282 */       String classDesc = member.getDeclaringClass().getName().replace('.', '/');
/*     */       
/* 284 */       if (!isStatic) {
/* 285 */         if (descriptor == null) {
/* 286 */           cf.loadTarget(mv);
/*     */         }
/* 288 */         if ((descriptor == null) || (!classDesc.equals(descriptor.substring(1)))) {
/* 289 */           mv.visitTypeInsn(192, classDesc);
/*     */         }
/*     */       }
/*     */       
/* 293 */       if ((member instanceof Method)) {
/* 294 */         mv.visitMethodInsn(isStatic ? 184 : 182, classDesc, member.getName(), 
/* 295 */           CodeFlow.createSignatureDescriptor((Method)member), false);
/*     */       }
/*     */       else {
/* 298 */         mv.visitFieldInsn(isStatic ? 178 : 180, classDesc, member.getName(), 
/* 299 */           CodeFlow.toJvmDescriptor(((Field)member).getType()));
/*     */       }
/*     */     }
/*     */     
/* 303 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */   
/*     */   public String toStringAST()
/*     */   {
/* 308 */     StringBuilder sb = new StringBuilder("[");
/* 309 */     for (int i = 0; i < getChildCount(); i++) {
/* 310 */       if (i > 0) {
/* 311 */         sb.append(",");
/*     */       }
/* 313 */       sb.append(getChild(i).toStringAST());
/*     */     }
/* 315 */     sb.append("]");
/* 316 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   private void setArrayElement(TypeConverter converter, Object ctx, int idx, Object newValue, Class<?> arrayComponentType)
/*     */     throws EvaluationException
/*     */   {
/* 323 */     if (arrayComponentType == Double.TYPE) {
/* 324 */       double[] array = (double[])ctx;
/* 325 */       checkAccess(array.length, idx);
/* 326 */       array[idx] = ((Double)convertValue(converter, newValue, Double.class)).doubleValue();
/*     */     }
/* 328 */     else if (arrayComponentType == Float.TYPE) {
/* 329 */       float[] array = (float[])ctx;
/* 330 */       checkAccess(array.length, idx);
/* 331 */       array[idx] = ((Float)convertValue(converter, newValue, Float.class)).floatValue();
/*     */     }
/* 333 */     else if (arrayComponentType == Long.TYPE) {
/* 334 */       long[] array = (long[])ctx;
/* 335 */       checkAccess(array.length, idx);
/* 336 */       array[idx] = ((Long)convertValue(converter, newValue, Long.class)).longValue();
/*     */     }
/* 338 */     else if (arrayComponentType == Integer.TYPE) {
/* 339 */       int[] array = (int[])ctx;
/* 340 */       checkAccess(array.length, idx);
/* 341 */       array[idx] = ((Integer)convertValue(converter, newValue, Integer.class)).intValue();
/*     */     }
/* 343 */     else if (arrayComponentType == Short.TYPE) {
/* 344 */       short[] array = (short[])ctx;
/* 345 */       checkAccess(array.length, idx);
/* 346 */       array[idx] = ((Short)convertValue(converter, newValue, Short.class)).shortValue();
/*     */     }
/* 348 */     else if (arrayComponentType == Byte.TYPE) {
/* 349 */       byte[] array = (byte[])ctx;
/* 350 */       checkAccess(array.length, idx);
/* 351 */       array[idx] = ((Byte)convertValue(converter, newValue, Byte.class)).byteValue();
/*     */     }
/* 353 */     else if (arrayComponentType == Character.TYPE) {
/* 354 */       char[] array = (char[])ctx;
/* 355 */       checkAccess(array.length, idx);
/* 356 */       array[idx] = ((Character)convertValue(converter, newValue, Character.class)).charValue();
/*     */     }
/* 358 */     else if (arrayComponentType == Boolean.TYPE) {
/* 359 */       boolean[] array = (boolean[])ctx;
/* 360 */       checkAccess(array.length, idx);
/* 361 */       array[idx] = ((Boolean)convertValue(converter, newValue, Boolean.class)).booleanValue();
/*     */     }
/*     */     else {
/* 364 */       Object[] array = (Object[])ctx;
/* 365 */       checkAccess(array.length, idx);
/* 366 */       array[idx] = convertValue(converter, newValue, arrayComponentType);
/*     */     }
/*     */   }
/*     */   
/*     */   private Object accessArrayElement(Object ctx, int idx) throws SpelEvaluationException {
/* 371 */     Class<?> arrayComponentType = ctx.getClass().getComponentType();
/* 372 */     if (arrayComponentType == Double.TYPE) {
/* 373 */       double[] array = (double[])ctx;
/* 374 */       checkAccess(array.length, idx);
/* 375 */       this.exitTypeDescriptor = "D";
/* 376 */       return Double.valueOf(array[idx]);
/*     */     }
/* 378 */     if (arrayComponentType == Float.TYPE) {
/* 379 */       float[] array = (float[])ctx;
/* 380 */       checkAccess(array.length, idx);
/* 381 */       this.exitTypeDescriptor = "F";
/* 382 */       return Float.valueOf(array[idx]);
/*     */     }
/* 384 */     if (arrayComponentType == Long.TYPE) {
/* 385 */       long[] array = (long[])ctx;
/* 386 */       checkAccess(array.length, idx);
/* 387 */       this.exitTypeDescriptor = "J";
/* 388 */       return Long.valueOf(array[idx]);
/*     */     }
/* 390 */     if (arrayComponentType == Integer.TYPE) {
/* 391 */       int[] array = (int[])ctx;
/* 392 */       checkAccess(array.length, idx);
/* 393 */       this.exitTypeDescriptor = "I";
/* 394 */       return Integer.valueOf(array[idx]);
/*     */     }
/* 396 */     if (arrayComponentType == Short.TYPE) {
/* 397 */       short[] array = (short[])ctx;
/* 398 */       checkAccess(array.length, idx);
/* 399 */       this.exitTypeDescriptor = "S";
/* 400 */       return Short.valueOf(array[idx]);
/*     */     }
/* 402 */     if (arrayComponentType == Byte.TYPE) {
/* 403 */       byte[] array = (byte[])ctx;
/* 404 */       checkAccess(array.length, idx);
/* 405 */       this.exitTypeDescriptor = "B";
/* 406 */       return Byte.valueOf(array[idx]);
/*     */     }
/* 408 */     if (arrayComponentType == Character.TYPE) {
/* 409 */       char[] array = (char[])ctx;
/* 410 */       checkAccess(array.length, idx);
/* 411 */       this.exitTypeDescriptor = "C";
/* 412 */       return Character.valueOf(array[idx]);
/*     */     }
/* 414 */     if (arrayComponentType == Boolean.TYPE) {
/* 415 */       boolean[] array = (boolean[])ctx;
/* 416 */       checkAccess(array.length, idx);
/* 417 */       this.exitTypeDescriptor = "Z";
/* 418 */       return Boolean.valueOf(array[idx]);
/*     */     }
/*     */     
/* 421 */     Object[] array = (Object[])ctx;
/* 422 */     checkAccess(array.length, idx);
/* 423 */     Object retValue = array[idx];
/* 424 */     this.exitTypeDescriptor = CodeFlow.toDescriptor(arrayComponentType);
/* 425 */     return retValue;
/*     */   }
/*     */   
/*     */   private void checkAccess(int arrayLength, int index) throws SpelEvaluationException
/*     */   {
/* 430 */     if (index > arrayLength)
/*     */     {
/* 432 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.ARRAY_INDEX_OUT_OF_BOUNDS, new Object[] {Integer.valueOf(arrayLength), Integer.valueOf(index) });
/*     */     }
/*     */   }
/*     */   
/*     */   private <T> T convertValue(TypeConverter converter, Object value, Class<T> targetType)
/*     */   {
/* 438 */     return (T)converter.convertValue(value, TypeDescriptor.forObject(value), TypeDescriptor.valueOf(targetType));
/*     */   }
/*     */   
/*     */ 
/*     */   private class ArrayIndexingValueRef
/*     */     implements ValueRef
/*     */   {
/*     */     private final TypeConverter typeConverter;
/*     */     
/*     */     private final Object array;
/*     */     private final int index;
/*     */     private final TypeDescriptor typeDescriptor;
/*     */     
/*     */     ArrayIndexingValueRef(TypeConverter typeConverter, Object array, int index, TypeDescriptor typeDescriptor)
/*     */     {
/* 453 */       this.typeConverter = typeConverter;
/* 454 */       this.array = array;
/* 455 */       this.index = index;
/* 456 */       this.typeDescriptor = typeDescriptor;
/*     */     }
/*     */     
/*     */     public TypedValue getValue()
/*     */     {
/* 461 */       Object arrayElement = Indexer.this.accessArrayElement(this.array, this.index);
/* 462 */       return new TypedValue(arrayElement, this.typeDescriptor.elementTypeDescriptor(arrayElement));
/*     */     }
/*     */     
/*     */     public void setValue(Object newValue)
/*     */     {
/* 467 */       Indexer.this.setArrayElement(this.typeConverter, this.array, this.index, newValue, this.typeDescriptor
/* 468 */         .getElementTypeDescriptor().getType());
/*     */     }
/*     */     
/*     */     public boolean isWritable()
/*     */     {
/* 473 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private class MapIndexingValueRef
/*     */     implements ValueRef
/*     */   {
/*     */     private final TypeConverter typeConverter;
/*     */     
/*     */     private final Map map;
/*     */     
/*     */     private final Object key;
/*     */     private final TypeDescriptor mapEntryDescriptor;
/*     */     
/*     */     public MapIndexingValueRef(TypeConverter typeConverter, Map map, Object key, TypeDescriptor mapEntryDescriptor)
/*     */     {
/* 490 */       this.typeConverter = typeConverter;
/* 491 */       this.map = map;
/* 492 */       this.key = key;
/* 493 */       this.mapEntryDescriptor = mapEntryDescriptor;
/*     */     }
/*     */     
/*     */     public TypedValue getValue()
/*     */     {
/* 498 */       Object value = this.map.get(this.key);
/* 499 */       Indexer.this.exitTypeDescriptor = CodeFlow.toDescriptor(Object.class);
/* 500 */       return new TypedValue(value, this.mapEntryDescriptor.getMapValueTypeDescriptor(value));
/*     */     }
/*     */     
/*     */     public void setValue(Object newValue)
/*     */     {
/* 505 */       if (this.mapEntryDescriptor.getMapValueTypeDescriptor() != null) {
/* 506 */         newValue = this.typeConverter.convertValue(newValue, TypeDescriptor.forObject(newValue), this.mapEntryDescriptor
/* 507 */           .getMapValueTypeDescriptor());
/*     */       }
/* 509 */       this.map.put(this.key, newValue);
/*     */     }
/*     */     
/*     */     public boolean isWritable()
/*     */     {
/* 514 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private class PropertyIndexingValueRef
/*     */     implements ValueRef
/*     */   {
/*     */     private final Object targetObject;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final EvaluationContext evaluationContext;
/*     */     private final TypeDescriptor targetObjectTypeDescriptor;
/*     */     
/*     */     public PropertyIndexingValueRef(Object targetObject, String value, EvaluationContext evaluationContext, TypeDescriptor targetObjectTypeDescriptor)
/*     */     {
/* 531 */       this.targetObject = targetObject;
/* 532 */       this.name = value;
/* 533 */       this.evaluationContext = evaluationContext;
/* 534 */       this.targetObjectTypeDescriptor = targetObjectTypeDescriptor;
/*     */     }
/*     */     
/*     */     public TypedValue getValue()
/*     */     {
/* 539 */       Class<?> targetObjectRuntimeClass = Indexer.this.getObjectClass(this.targetObject);
/*     */       try {
/* 541 */         if ((Indexer.this.cachedReadName != null) && (Indexer.this.cachedReadName.equals(this.name)) && 
/* 542 */           (Indexer.this.cachedReadTargetType != null) && 
/* 543 */           (Indexer.this.cachedReadTargetType.equals(targetObjectRuntimeClass)))
/*     */         {
/* 545 */           return Indexer.this.cachedReadAccessor.read(this.evaluationContext, this.targetObject, this.name);
/*     */         }
/* 547 */         List<PropertyAccessor> accessorsToTry = AstUtils.getPropertyAccessorsToTry(targetObjectRuntimeClass, this.evaluationContext
/* 548 */           .getPropertyAccessors());
/* 549 */         if (accessorsToTry != null) {
/* 550 */           for (PropertyAccessor accessor : accessorsToTry) {
/* 551 */             if (accessor.canRead(this.evaluationContext, this.targetObject, this.name)) {
/* 552 */               if ((accessor instanceof ReflectivePropertyAccessor)) {
/* 553 */                 accessor = ((ReflectivePropertyAccessor)accessor).createOptimalAccessor(this.evaluationContext, this.targetObject, this.name);
/*     */               }
/*     */               
/* 556 */               Indexer.this.cachedReadAccessor = accessor;
/* 557 */               Indexer.this.cachedReadName = this.name;
/* 558 */               Indexer.this.cachedReadTargetType = targetObjectRuntimeClass;
/* 559 */               if ((accessor instanceof ReflectivePropertyAccessor.OptimalPropertyAccessor)) {
/* 560 */                 ReflectivePropertyAccessor.OptimalPropertyAccessor optimalAccessor = (ReflectivePropertyAccessor.OptimalPropertyAccessor)accessor;
/*     */                 
/* 562 */                 Member member = optimalAccessor.member;
/* 563 */                 Indexer.this.exitTypeDescriptor = CodeFlow.toDescriptor((member instanceof Method) ? ((Method)member)
/* 564 */                   .getReturnType() : ((Field)member).getType());
/*     */               }
/* 566 */               return accessor.read(this.evaluationContext, this.targetObject, this.name);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (AccessException ex)
/*     */       {
/* 573 */         throw new SpelEvaluationException(Indexer.this.getStartPosition(), ex, SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, new Object[] {this.targetObjectTypeDescriptor.toString() });
/*     */       }
/*     */       
/* 576 */       throw new SpelEvaluationException(Indexer.this.getStartPosition(), SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, new Object[] {this.targetObjectTypeDescriptor.toString() });
/*     */     }
/*     */     
/*     */     public void setValue(Object newValue)
/*     */     {
/* 581 */       Class<?> contextObjectClass = Indexer.this.getObjectClass(this.targetObject);
/*     */       try {
/* 583 */         if ((Indexer.this.cachedWriteName != null) && (Indexer.this.cachedWriteName.equals(this.name)) && 
/* 584 */           (Indexer.this.cachedWriteTargetType != null) && 
/* 585 */           (Indexer.this.cachedWriteTargetType.equals(contextObjectClass)))
/*     */         {
/* 587 */           Indexer.this.cachedWriteAccessor.write(this.evaluationContext, this.targetObject, this.name, newValue);
/* 588 */           return;
/*     */         }
/*     */         
/* 591 */         List<PropertyAccessor> accessorsToTry = AstUtils.getPropertyAccessorsToTry(contextObjectClass, this.evaluationContext.getPropertyAccessors());
/* 592 */         if (accessorsToTry != null) {
/* 593 */           for (PropertyAccessor accessor : accessorsToTry) {
/* 594 */             if (accessor.canWrite(this.evaluationContext, this.targetObject, this.name)) {
/* 595 */               Indexer.this.cachedWriteName = this.name;
/* 596 */               Indexer.this.cachedWriteTargetType = contextObjectClass;
/* 597 */               Indexer.this.cachedWriteAccessor = accessor;
/* 598 */               accessor.write(this.evaluationContext, this.targetObject, this.name, newValue);
/* 599 */               return;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (AccessException ex)
/*     */       {
/* 606 */         throw new SpelEvaluationException(Indexer.this.getStartPosition(), ex, SpelMessage.EXCEPTION_DURING_PROPERTY_WRITE, new Object[] { this.name, ex.getMessage() });
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean isWritable()
/*     */     {
/* 612 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private class CollectionIndexingValueRef
/*     */     implements ValueRef
/*     */   {
/*     */     private final Collection collection;
/*     */     
/*     */     private final int index;
/*     */     
/*     */     private final TypeDescriptor collectionEntryDescriptor;
/*     */     
/*     */     private final TypeConverter typeConverter;
/*     */     
/*     */     private final boolean growCollection;
/*     */     
/*     */     private final int maximumSize;
/*     */     
/*     */     public CollectionIndexingValueRef(Collection collection, int index, TypeDescriptor collectionEntryTypeDescriptor, TypeConverter typeConverter, boolean growCollection, int maximumSize)
/*     */     {
/* 634 */       this.collection = collection;
/* 635 */       this.index = index;
/* 636 */       this.collectionEntryDescriptor = collectionEntryTypeDescriptor;
/* 637 */       this.typeConverter = typeConverter;
/* 638 */       this.growCollection = growCollection;
/* 639 */       this.maximumSize = maximumSize;
/*     */     }
/*     */     
/*     */     public TypedValue getValue()
/*     */     {
/* 644 */       growCollectionIfNecessary();
/* 645 */       if ((this.collection instanceof List)) {
/* 646 */         Object o = ((List)this.collection).get(this.index);
/* 647 */         Indexer.this.exitTypeDescriptor = CodeFlow.toDescriptor(Object.class);
/* 648 */         return new TypedValue(o, this.collectionEntryDescriptor.elementTypeDescriptor(o));
/*     */       }
/* 650 */       int pos = 0;
/* 651 */       for (Object o : this.collection) {
/* 652 */         if (pos == this.index) {
/* 653 */           return new TypedValue(o, this.collectionEntryDescriptor.elementTypeDescriptor(o));
/*     */         }
/* 655 */         pos++;
/*     */       }
/* 657 */       throw new IllegalStateException("Failed to find indexed element " + this.index + ": " + this.collection);
/*     */     }
/*     */     
/*     */     public void setValue(Object newValue)
/*     */     {
/* 662 */       growCollectionIfNecessary();
/* 663 */       if ((this.collection instanceof List)) {
/* 664 */         List list = (List)this.collection;
/* 665 */         if (this.collectionEntryDescriptor.getElementTypeDescriptor() != null) {
/* 666 */           newValue = this.typeConverter.convertValue(newValue, TypeDescriptor.forObject(newValue), this.collectionEntryDescriptor
/* 667 */             .getElementTypeDescriptor());
/*     */         }
/* 669 */         list.set(this.index, newValue);
/*     */       }
/*     */       else
/*     */       {
/* 673 */         throw new SpelEvaluationException(Indexer.this.getStartPosition(), SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, new Object[] {this.collectionEntryDescriptor.toString() });
/*     */       }
/*     */     }
/*     */     
/*     */     private void growCollectionIfNecessary() {
/* 678 */       if (this.index >= this.collection.size()) {
/* 679 */         if (!this.growCollection)
/*     */         {
/* 681 */           throw new SpelEvaluationException(Indexer.this.getStartPosition(), SpelMessage.COLLECTION_INDEX_OUT_OF_BOUNDS, new Object[] {Integer.valueOf(this.collection.size()), Integer.valueOf(this.index) });
/*     */         }
/* 683 */         if (this.index >= this.maximumSize) {
/* 684 */           throw new SpelEvaluationException(Indexer.this.getStartPosition(), SpelMessage.UNABLE_TO_GROW_COLLECTION, new Object[0]);
/*     */         }
/* 686 */         if (this.collectionEntryDescriptor.getElementTypeDescriptor() == null) {
/* 687 */           throw new SpelEvaluationException(Indexer.this.getStartPosition(), SpelMessage.UNABLE_TO_GROW_COLLECTION_UNKNOWN_ELEMENT_TYPE, new Object[0]);
/*     */         }
/* 689 */         TypeDescriptor elementType = this.collectionEntryDescriptor.getElementTypeDescriptor();
/*     */         try {
/* 691 */           int newElements = this.index - this.collection.size();
/* 692 */           while (newElements >= 0) {
/* 693 */             this.collection.add(elementType.getType().newInstance());
/* 694 */             newElements--;
/*     */           }
/*     */         }
/*     */         catch (Throwable ex) {
/* 698 */           throw new SpelEvaluationException(Indexer.this.getStartPosition(), ex, SpelMessage.UNABLE_TO_GROW_COLLECTION, new Object[0]);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean isWritable()
/*     */     {
/* 705 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private class StringIndexingLValue
/*     */     implements ValueRef
/*     */   {
/*     */     private final String target;
/*     */     private final int index;
/*     */     private final TypeDescriptor typeDescriptor;
/*     */     
/*     */     public StringIndexingLValue(String target, int index, TypeDescriptor typeDescriptor)
/*     */     {
/* 719 */       this.target = target;
/* 720 */       this.index = index;
/* 721 */       this.typeDescriptor = typeDescriptor;
/*     */     }
/*     */     
/*     */     public TypedValue getValue()
/*     */     {
/* 726 */       if (this.index >= this.target.length())
/*     */       {
/* 728 */         throw new SpelEvaluationException(Indexer.this.getStartPosition(), SpelMessage.STRING_INDEX_OUT_OF_BOUNDS, new Object[] {Integer.valueOf(this.target.length()), Integer.valueOf(this.index) });
/*     */       }
/* 730 */       return new TypedValue(String.valueOf(this.target.charAt(this.index)));
/*     */     }
/*     */     
/*     */ 
/*     */     public void setValue(Object newValue)
/*     */     {
/* 736 */       throw new SpelEvaluationException(Indexer.this.getStartPosition(), SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, new Object[] {this.typeDescriptor.toString() });
/*     */     }
/*     */     
/*     */     public boolean isWritable()
/*     */     {
/* 741 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\ast\Indexer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */