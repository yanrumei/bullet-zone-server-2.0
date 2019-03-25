/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.deser.CreatorProperty;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotationMap;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ 
/*     */ public class CreatorCollector
/*     */ {
/*     */   protected static final int C_DEFAULT = 0;
/*     */   protected static final int C_STRING = 1;
/*     */   protected static final int C_INT = 2;
/*     */   protected static final int C_LONG = 3;
/*     */   protected static final int C_DOUBLE = 4;
/*     */   protected static final int C_BOOLEAN = 5;
/*     */   protected static final int C_DELEGATE = 6;
/*     */   protected static final int C_PROPS = 7;
/*     */   protected static final int C_ARRAY_DELEGATE = 8;
/*  33 */   protected static final String[] TYPE_DESCS = { "default", "from-String", "from-int", "from-long", "from-double", "from-boolean", "delegate", "property-based" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final BeanDescription _beanDesc;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final boolean _canFixAccess;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final boolean _forceAccess;
/*     */   
/*     */ 
/*     */ 
/*  52 */   protected final AnnotatedWithParams[] _creators = new AnnotatedWithParams[9];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  61 */   protected int _explicitCreators = 0;
/*     */   
/*  63 */   protected boolean _hasNonDefaultCreator = false;
/*     */   
/*     */ 
/*     */   protected SettableBeanProperty[] _delegateArgs;
/*     */   
/*     */ 
/*     */   protected SettableBeanProperty[] _arrayDelegateArgs;
/*     */   
/*     */ 
/*     */   protected SettableBeanProperty[] _propertyBasedArgs;
/*     */   
/*     */ 
/*     */   protected AnnotatedParameter _incompleteParameter;
/*     */   
/*     */ 
/*     */   public CreatorCollector(BeanDescription beanDesc, MapperConfig<?> config)
/*     */   {
/*  80 */     this._beanDesc = beanDesc;
/*  81 */     this._canFixAccess = config.canOverrideAccessModifiers();
/*  82 */     this._forceAccess = config.isEnabled(com.fasterxml.jackson.databind.MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS);
/*     */   }
/*     */   
/*     */ 
/*     */   public com.fasterxml.jackson.databind.deser.ValueInstantiator constructValueInstantiator(DeserializationConfig config)
/*     */   {
/*  88 */     JavaType delegateType = _computeDelegateType(this._creators[6], this._delegateArgs);
/*     */     
/*  90 */     JavaType arrayDelegateType = _computeDelegateType(this._creators[8], this._arrayDelegateArgs);
/*     */     
/*  92 */     JavaType type = this._beanDesc.getType();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  97 */     AnnotatedWithParams defaultCtor = StdTypeConstructor.tryToOptimize(this._creators[0]);
/*     */     
/*     */ 
/* 100 */     StdValueInstantiator inst = new StdValueInstantiator(config, type);
/* 101 */     inst.configureFromObjectSettings(defaultCtor, this._creators[6], delegateType, this._delegateArgs, this._creators[7], this._propertyBasedArgs);
/*     */     
/*     */ 
/* 104 */     inst.configureFromArraySettings(this._creators[8], arrayDelegateType, this._arrayDelegateArgs);
/*     */     
/* 106 */     inst.configureFromStringCreator(this._creators[1]);
/* 107 */     inst.configureFromIntCreator(this._creators[2]);
/* 108 */     inst.configureFromLongCreator(this._creators[3]);
/* 109 */     inst.configureFromDoubleCreator(this._creators[4]);
/* 110 */     inst.configureFromBooleanCreator(this._creators[5]);
/* 111 */     inst.configureIncompleteParameter(this._incompleteParameter);
/* 112 */     return inst;
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
/*     */   public void setDefaultCreator(AnnotatedWithParams creator)
/*     */   {
/* 131 */     this._creators[0] = ((AnnotatedWithParams)_fixAccess(creator));
/*     */   }
/*     */   
/*     */   public void addStringCreator(AnnotatedWithParams creator, boolean explicit)
/*     */   {
/* 136 */     verifyNonDup(creator, 1, explicit);
/*     */   }
/*     */   
/*     */   public void addIntCreator(AnnotatedWithParams creator, boolean explicit) {
/* 140 */     verifyNonDup(creator, 2, explicit);
/*     */   }
/*     */   
/*     */   public void addLongCreator(AnnotatedWithParams creator, boolean explicit) {
/* 144 */     verifyNonDup(creator, 3, explicit);
/*     */   }
/*     */   
/*     */   public void addDoubleCreator(AnnotatedWithParams creator, boolean explicit)
/*     */   {
/* 149 */     verifyNonDup(creator, 4, explicit);
/*     */   }
/*     */   
/*     */   public void addBooleanCreator(AnnotatedWithParams creator, boolean explicit)
/*     */   {
/* 154 */     verifyNonDup(creator, 5, explicit);
/*     */   }
/*     */   
/*     */   public void addDelegatingCreator(AnnotatedWithParams creator, boolean explicit, SettableBeanProperty[] injectables)
/*     */   {
/* 159 */     if (creator.getParameterType(0).isCollectionLikeType()) {
/* 160 */       if (verifyNonDup(creator, 8, explicit)) {
/* 161 */         this._arrayDelegateArgs = injectables;
/*     */       }
/*     */     }
/* 164 */     else if (verifyNonDup(creator, 6, explicit)) {
/* 165 */       this._delegateArgs = injectables;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void addPropertyCreator(AnnotatedWithParams creator, boolean explicit, SettableBeanProperty[] properties)
/*     */   {
/* 172 */     if (verifyNonDup(creator, 7, explicit))
/*     */     {
/* 174 */       if (properties.length > 1) {
/* 175 */         HashMap<String, Integer> names = new HashMap();
/* 176 */         int i = 0; for (int len = properties.length; i < len; i++) {
/* 177 */           String name = properties[i].getName();
/*     */           
/*     */ 
/* 180 */           if ((name.length() != 0) || (properties[i].getInjectableValueId() == null))
/*     */           {
/*     */ 
/*     */ 
/* 184 */             Integer old = (Integer)names.put(name, Integer.valueOf(i));
/* 185 */             if (old != null) {
/* 186 */               throw new IllegalArgumentException(String.format("Duplicate creator property \"%s\" (index %s vs %d)", new Object[] { name, old, Integer.valueOf(i) }));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 192 */       this._propertyBasedArgs = properties;
/*     */     }
/*     */   }
/*     */   
/*     */   public void addIncompeteParameter(AnnotatedParameter parameter) {
/* 197 */     if (this._incompleteParameter == null) {
/* 198 */       this._incompleteParameter = parameter;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   public void addStringCreator(AnnotatedWithParams creator)
/*     */   {
/* 206 */     addStringCreator(creator, false);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void addIntCreator(AnnotatedWithParams creator) {
/* 211 */     addBooleanCreator(creator, false);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void addLongCreator(AnnotatedWithParams creator) {
/* 216 */     addBooleanCreator(creator, false);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void addDoubleCreator(AnnotatedWithParams creator) {
/* 221 */     addBooleanCreator(creator, false);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void addBooleanCreator(AnnotatedWithParams creator) {
/* 226 */     addBooleanCreator(creator, false);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void addDelegatingCreator(AnnotatedWithParams creator, CreatorProperty[] injectables)
/*     */   {
/* 232 */     addDelegatingCreator(creator, false, injectables);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void addPropertyCreator(AnnotatedWithParams creator, CreatorProperty[] properties)
/*     */   {
/* 238 */     addPropertyCreator(creator, false, properties);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasDefaultCreator()
/*     */   {
/* 250 */     return this._creators[0] != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean hasDelegatingCreator()
/*     */   {
/* 257 */     return this._creators[6] != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean hasPropertyBasedCreator()
/*     */   {
/* 264 */     return this._creators[7] != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private JavaType _computeDelegateType(AnnotatedWithParams creator, SettableBeanProperty[] delegateArgs)
/*     */   {
/* 274 */     if ((!this._hasNonDefaultCreator) || (creator == null)) {
/* 275 */       return null;
/*     */     }
/*     */     
/* 278 */     int ix = 0;
/* 279 */     if (delegateArgs != null) {
/* 280 */       int i = 0; for (int len = delegateArgs.length; i < len; i++) {
/* 281 */         if (delegateArgs[i] == null) {
/* 282 */           ix = i;
/* 283 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 287 */     return creator.getParameterType(ix);
/*     */   }
/*     */   
/*     */   private <T extends AnnotatedMember> T _fixAccess(T member) {
/* 291 */     if ((member != null) && (this._canFixAccess)) {
/* 292 */       com.fasterxml.jackson.databind.util.ClassUtil.checkAndFixAccess((Member)member.getAnnotated(), this._forceAccess);
/*     */     }
/*     */     
/* 295 */     return member;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean verifyNonDup(AnnotatedWithParams newOne, int typeIndex, boolean explicit)
/*     */   {
/* 303 */     int mask = 1 << typeIndex;
/* 304 */     this._hasNonDefaultCreator = true;
/* 305 */     AnnotatedWithParams oldOne = this._creators[typeIndex];
/*     */     
/* 307 */     if (oldOne != null) { boolean verify;
/*     */       boolean verify;
/* 309 */       if ((this._explicitCreators & mask) != 0)
/*     */       {
/* 311 */         if (!explicit) {
/* 312 */           return false;
/*     */         }
/*     */         
/* 315 */         verify = true;
/*     */       }
/*     */       else {
/* 318 */         verify = !explicit;
/*     */       }
/*     */       
/*     */ 
/* 322 */       if ((verify) && (oldOne.getClass() == newOne.getClass()))
/*     */       {
/* 324 */         Class<?> oldType = oldOne.getRawParameterType(0);
/* 325 */         Class<?> newType = newOne.getRawParameterType(0);
/*     */         
/* 327 */         if (oldType == newType)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 333 */           if (_isEnumValueOf(newOne)) {
/* 334 */             return false;
/*     */           }
/* 336 */           if (!_isEnumValueOf(oldOne))
/*     */           {
/*     */ 
/* 339 */             throw new IllegalArgumentException(String.format("Conflicting %s creators: already had %s creator %s, encountered another: %s", new Object[] { TYPE_DESCS[typeIndex], explicit ? "explicitly marked" : "implicitly discovered", oldOne, newOne }));
/*     */ 
/*     */ 
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/* 348 */         else if (newType.isAssignableFrom(oldType))
/*     */         {
/* 350 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 355 */     if (explicit) {
/* 356 */       this._explicitCreators |= mask;
/*     */     }
/* 358 */     this._creators[typeIndex] = ((AnnotatedWithParams)_fixAccess(newOne));
/* 359 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean _isEnumValueOf(AnnotatedWithParams creator)
/*     */   {
/* 368 */     return (creator.getDeclaringClass().isEnum()) && ("valueOf".equals(creator.getName()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final class StdTypeConstructor
/*     */     extends AnnotatedWithParams
/*     */     implements java.io.Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/*     */     public static final int TYPE_ARRAY_LIST = 1;
/*     */     
/*     */ 
/*     */     public static final int TYPE_HASH_MAP = 2;
/*     */     
/*     */ 
/*     */     public static final int TYPE_LINKED_HASH_MAP = 3;
/*     */     
/*     */ 
/*     */     private final AnnotatedWithParams _base;
/*     */     
/*     */ 
/*     */     private final int _type;
/*     */     
/*     */ 
/*     */ 
/*     */     public StdTypeConstructor(AnnotatedWithParams base, int t)
/*     */     {
/* 400 */       super(null);
/* 401 */       this._base = base;
/* 402 */       this._type = t;
/*     */     }
/*     */     
/*     */     public static AnnotatedWithParams tryToOptimize(AnnotatedWithParams src)
/*     */     {
/* 407 */       if (src != null) {
/* 408 */         Class<?> rawType = src.getDeclaringClass();
/* 409 */         if ((rawType == java.util.List.class) || (rawType == ArrayList.class)) {
/* 410 */           return new StdTypeConstructor(src, 1);
/*     */         }
/* 412 */         if (rawType == LinkedHashMap.class) {
/* 413 */           return new StdTypeConstructor(src, 3);
/*     */         }
/* 415 */         if (rawType == HashMap.class) {
/* 416 */           return new StdTypeConstructor(src, 2);
/*     */         }
/*     */       }
/* 419 */       return src;
/*     */     }
/*     */     
/*     */     protected final Object _construct() {
/* 423 */       switch (this._type) {
/*     */       case 1: 
/* 425 */         return new ArrayList();
/*     */       case 3: 
/* 427 */         return new LinkedHashMap();
/*     */       case 2: 
/* 429 */         return new HashMap();
/*     */       }
/* 431 */       throw new IllegalStateException("Unknown type " + this._type);
/*     */     }
/*     */     
/*     */     public int getParameterCount()
/*     */     {
/* 436 */       return this._base.getParameterCount();
/*     */     }
/*     */     
/*     */     public Class<?> getRawParameterType(int index)
/*     */     {
/* 441 */       return this._base.getRawParameterType(index);
/*     */     }
/*     */     
/*     */     public JavaType getParameterType(int index)
/*     */     {
/* 446 */       return this._base.getParameterType(index);
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public Type getGenericParameterType(int index)
/*     */     {
/* 452 */       return this._base.getGenericParameterType(index);
/*     */     }
/*     */     
/*     */     public Object call() throws Exception
/*     */     {
/* 457 */       return _construct();
/*     */     }
/*     */     
/*     */     public Object call(Object[] args) throws Exception
/*     */     {
/* 462 */       return _construct();
/*     */     }
/*     */     
/*     */     public Object call1(Object arg) throws Exception
/*     */     {
/* 467 */       return _construct();
/*     */     }
/*     */     
/*     */     public Class<?> getDeclaringClass()
/*     */     {
/* 472 */       return this._base.getDeclaringClass();
/*     */     }
/*     */     
/*     */     public Member getMember()
/*     */     {
/* 477 */       return this._base.getMember();
/*     */     }
/*     */     
/*     */     public void setValue(Object pojo, Object value)
/*     */       throws UnsupportedOperationException, IllegalArgumentException
/*     */     {
/* 483 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public Object getValue(Object pojo)
/*     */       throws UnsupportedOperationException, IllegalArgumentException
/*     */     {
/* 489 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public Annotated withAnnotations(AnnotationMap fallback)
/*     */     {
/* 494 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public AnnotatedElement getAnnotated()
/*     */     {
/* 499 */       return this._base.getAnnotated();
/*     */     }
/*     */     
/*     */     protected int getModifiers()
/*     */     {
/* 504 */       return this._base.getMember().getModifiers();
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 509 */       return this._base.getName();
/*     */     }
/*     */     
/*     */     public JavaType getType()
/*     */     {
/* 514 */       return this._base.getType();
/*     */     }
/*     */     
/*     */     public Class<?> getRawType()
/*     */     {
/* 519 */       return this._base.getRawType();
/*     */     }
/*     */     
/*     */     public boolean equals(Object o)
/*     */     {
/* 524 */       return o == this;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 529 */       return this._base.hashCode();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 534 */       return this._base.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\impl\CreatorCollector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */