/*     */ package com.fasterxml.jackson.databind.type;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class TypeBindings implements java.io.Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  16 */   private static final String[] NO_STRINGS = new String[0];
/*     */   
/*  18 */   private static final JavaType[] NO_TYPES = new JavaType[0];
/*     */   
/*  20 */   private static final TypeBindings EMPTY = new TypeBindings(NO_STRINGS, NO_TYPES, null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final String[] _names;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final JavaType[] _types;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final String[] _unboundVariables;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int _hashCode;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private TypeBindings(String[] names, JavaType[] types, String[] uvars)
/*     */   {
/*  53 */     this._names = (names == null ? NO_STRINGS : names);
/*  54 */     this._types = (types == null ? NO_TYPES : types);
/*  55 */     if (this._names.length != this._types.length) {
/*  56 */       throw new IllegalArgumentException("Mismatching names (" + this._names.length + "), types (" + this._types.length + ")");
/*     */     }
/*  58 */     int h = 1;
/*  59 */     int i = 0; for (int len = this._types.length; i < len; i++) {
/*  60 */       h += this._types[i].hashCode();
/*     */     }
/*  62 */     this._unboundVariables = uvars;
/*  63 */     this._hashCode = h;
/*     */   }
/*     */   
/*     */   public static TypeBindings emptyBindings() {
/*  67 */     return EMPTY;
/*     */   }
/*     */   
/*     */   protected Object readResolve()
/*     */   {
/*  72 */     if ((this._names == null) || (this._names.length == 0)) {
/*  73 */       return EMPTY;
/*     */     }
/*  75 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TypeBindings create(Class<?> erasedType, List<JavaType> typeList)
/*     */   {
/*  84 */     JavaType[] types = (typeList == null) || (typeList.isEmpty()) ? NO_TYPES : (JavaType[])typeList.toArray(new JavaType[typeList.size()]);
/*     */     
/*  86 */     return create(erasedType, types);
/*     */   }
/*     */   
/*     */   public static TypeBindings create(Class<?> erasedType, JavaType[] types)
/*     */   {
/*  91 */     if (types == null)
/*  92 */       types = NO_TYPES; else
/*  93 */       switch (types.length) {
/*     */       case 1: 
/*  95 */         return create(erasedType, types[0]);
/*     */       case 2: 
/*  97 */         return create(erasedType, types[0], types[1]);
/*     */       }
/*  99 */     TypeVariable<?>[] vars = erasedType.getTypeParameters();
/*     */     String[] names;
/* 101 */     String[] names; if ((vars == null) || (vars.length == 0)) {
/* 102 */       names = NO_STRINGS;
/*     */     } else {
/* 104 */       int len = vars.length;
/* 105 */       names = new String[len];
/* 106 */       for (int i = 0; i < len; i++) {
/* 107 */         names[i] = vars[i].getName();
/*     */       }
/*     */     }
/*     */     
/* 111 */     if (names.length != types.length) {
/* 112 */       throw new IllegalArgumentException("Can not create TypeBindings for class " + erasedType.getName() + " with " + types.length + " type parameter" + (types.length == 1 ? "" : "s") + ": class expects " + names.length);
/*     */     }
/*     */     
/*     */ 
/* 116 */     return new TypeBindings(names, types, null);
/*     */   }
/*     */   
/*     */ 
/*     */   public static TypeBindings create(Class<?> erasedType, JavaType typeArg1)
/*     */   {
/* 122 */     TypeVariable<?>[] vars = TypeParamStash.paramsFor1(erasedType);
/* 123 */     int varLen = vars == null ? 0 : vars.length;
/* 124 */     if (varLen != 1) {
/* 125 */       throw new IllegalArgumentException("Can not create TypeBindings for class " + erasedType.getName() + " with 1 type parameter: class expects " + varLen);
/*     */     }
/*     */     
/* 128 */     return new TypeBindings(new String[] { vars[0].getName() }, new JavaType[] { typeArg1 }, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static TypeBindings create(Class<?> erasedType, JavaType typeArg1, JavaType typeArg2)
/*     */   {
/* 135 */     TypeVariable<?>[] vars = TypeParamStash.paramsFor2(erasedType);
/* 136 */     int varLen = vars == null ? 0 : vars.length;
/* 137 */     if (varLen != 2) {
/* 138 */       throw new IllegalArgumentException("Can not create TypeBindings for class " + erasedType.getName() + " with 2 type parameters: class expects " + varLen);
/*     */     }
/*     */     
/* 141 */     return new TypeBindings(new String[] { vars[0].getName(), vars[1].getName() }, new JavaType[] { typeArg1, typeArg2 }, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TypeBindings createIfNeeded(Class<?> erasedType, JavaType typeArg1)
/*     */   {
/* 152 */     TypeVariable<?>[] vars = erasedType.getTypeParameters();
/* 153 */     int varLen = vars == null ? 0 : vars.length;
/* 154 */     if (varLen == 0) {
/* 155 */       return EMPTY;
/*     */     }
/* 157 */     if (varLen != 1) {
/* 158 */       throw new IllegalArgumentException("Can not create TypeBindings for class " + erasedType.getName() + " with 1 type parameter: class expects " + varLen);
/*     */     }
/*     */     
/* 161 */     return new TypeBindings(new String[] { vars[0].getName() }, new JavaType[] { typeArg1 }, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TypeBindings createIfNeeded(Class<?> erasedType, JavaType[] types)
/*     */   {
/* 172 */     TypeVariable<?>[] vars = erasedType.getTypeParameters();
/* 173 */     if ((vars == null) || (vars.length == 0)) {
/* 174 */       return EMPTY;
/*     */     }
/* 176 */     if (types == null) {
/* 177 */       types = NO_TYPES;
/*     */     }
/* 179 */     int len = vars.length;
/* 180 */     String[] names = new String[len];
/* 181 */     for (int i = 0; i < len; i++) {
/* 182 */       names[i] = vars[i].getName();
/*     */     }
/*     */     
/* 185 */     if (names.length != types.length) {
/* 186 */       throw new IllegalArgumentException("Can not create TypeBindings for class " + erasedType.getName() + " with " + types.length + " type parameter" + (types.length == 1 ? "" : "s") + ": class expects " + names.length);
/*     */     }
/*     */     
/*     */ 
/* 190 */     return new TypeBindings(names, types, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TypeBindings withUnboundVariable(String name)
/*     */   {
/* 200 */     int len = this._unboundVariables == null ? 0 : this._unboundVariables.length;
/* 201 */     String[] names = len == 0 ? new String[1] : (String[])Arrays.copyOf(this._unboundVariables, len + 1);
/*     */     
/* 203 */     names[len] = name;
/* 204 */     return new TypeBindings(this._names, this._types, names);
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
/*     */   public JavaType findBoundType(String name)
/*     */   {
/* 218 */     int i = 0; for (int len = this._names.length; i < len; i++) {
/* 219 */       if (name.equals(this._names[i])) {
/* 220 */         JavaType t = this._types[i];
/* 221 */         if ((t instanceof ResolvedRecursiveType)) {
/* 222 */           ResolvedRecursiveType rrt = (ResolvedRecursiveType)t;
/* 223 */           JavaType t2 = rrt.getSelfReferencedType();
/* 224 */           if (t2 != null) {
/* 225 */             t = t2;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 238 */         return t;
/*     */       }
/*     */     }
/* 241 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 245 */     return this._types.length == 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 252 */     return this._types.length;
/*     */   }
/*     */   
/*     */   public String getBoundName(int index)
/*     */   {
/* 257 */     if ((index < 0) || (index >= this._names.length)) {
/* 258 */       return null;
/*     */     }
/* 260 */     return this._names[index];
/*     */   }
/*     */   
/*     */   public JavaType getBoundType(int index)
/*     */   {
/* 265 */     if ((index < 0) || (index >= this._types.length)) {
/* 266 */       return null;
/*     */     }
/* 268 */     return this._types[index];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<JavaType> getTypeParameters()
/*     */   {
/* 276 */     if (this._types.length == 0) {
/* 277 */       return java.util.Collections.emptyList();
/*     */     }
/* 279 */     return Arrays.asList(this._types);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean hasUnbound(String name)
/*     */   {
/* 286 */     if (this._unboundVariables != null) {
/* 287 */       int i = this._unboundVariables.length; do { i--; if (i < 0) break;
/* 288 */       } while (!name.equals(this._unboundVariables[i]));
/* 289 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 293 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object asKey(Class<?> rawBase)
/*     */   {
/* 305 */     return new AsKey(rawBase, this._types, this._hashCode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 316 */     if (this._types.length == 0) {
/* 317 */       return "<>";
/*     */     }
/* 319 */     StringBuilder sb = new StringBuilder();
/* 320 */     sb.append('<');
/* 321 */     int i = 0; for (int len = this._types.length; i < len; i++) {
/* 322 */       if (i > 0) {
/* 323 */         sb.append(',');
/*     */       }
/*     */       
/* 326 */       String sig = this._types[i].getGenericSignature();
/* 327 */       sb.append(sig);
/*     */     }
/* 329 */     sb.append('>');
/* 330 */     return sb.toString();
/*     */   }
/*     */   
/* 333 */   public int hashCode() { return this._hashCode; }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 337 */     if (o == this) return true;
/* 338 */     if ((o == null) || (o.getClass() != getClass())) return false;
/* 339 */     TypeBindings other = (TypeBindings)o;
/* 340 */     int len = this._types.length;
/* 341 */     if (len != other.size()) {
/* 342 */       return false;
/*     */     }
/* 344 */     JavaType[] otherTypes = other._types;
/* 345 */     for (int i = 0; i < len; i++) {
/* 346 */       if (!otherTypes[i].equals(this._types[i])) {
/* 347 */         return false;
/*     */       }
/*     */     }
/* 350 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JavaType[] typeParameterArray()
/*     */   {
/* 360 */     return this._types;
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
/*     */ 
/*     */   static class TypeParamStash
/*     */   {
/* 381 */     private static final TypeVariable<?>[] VARS_ABSTRACT_LIST = java.util.AbstractList.class.getTypeParameters();
/* 382 */     private static final TypeVariable<?>[] VARS_COLLECTION = Collection.class.getTypeParameters();
/* 383 */     private static final TypeVariable<?>[] VARS_ITERABLE = Iterable.class.getTypeParameters();
/* 384 */     private static final TypeVariable<?>[] VARS_LIST = List.class.getTypeParameters();
/* 385 */     private static final TypeVariable<?>[] VARS_ARRAY_LIST = ArrayList.class.getTypeParameters();
/*     */     
/* 387 */     private static final TypeVariable<?>[] VARS_MAP = Map.class.getTypeParameters();
/* 388 */     private static final TypeVariable<?>[] VARS_HASH_MAP = HashMap.class.getTypeParameters();
/* 389 */     private static final TypeVariable<?>[] VARS_LINKED_HASH_MAP = LinkedHashMap.class.getTypeParameters();
/*     */     
/*     */     public static TypeVariable<?>[] paramsFor1(Class<?> erasedType)
/*     */     {
/* 393 */       if (erasedType == Collection.class) {
/* 394 */         return VARS_COLLECTION;
/*     */       }
/* 396 */       if (erasedType == List.class) {
/* 397 */         return VARS_LIST;
/*     */       }
/* 399 */       if (erasedType == ArrayList.class) {
/* 400 */         return VARS_ARRAY_LIST;
/*     */       }
/* 402 */       if (erasedType == java.util.AbstractList.class) {
/* 403 */         return VARS_ABSTRACT_LIST;
/*     */       }
/* 405 */       if (erasedType == Iterable.class) {
/* 406 */         return VARS_ITERABLE;
/*     */       }
/* 408 */       return erasedType.getTypeParameters();
/*     */     }
/*     */     
/*     */     public static TypeVariable<?>[] paramsFor2(Class<?> erasedType)
/*     */     {
/* 413 */       if (erasedType == Map.class) {
/* 414 */         return VARS_MAP;
/*     */       }
/* 416 */       if (erasedType == HashMap.class) {
/* 417 */         return VARS_HASH_MAP;
/*     */       }
/* 419 */       if (erasedType == LinkedHashMap.class) {
/* 420 */         return VARS_LINKED_HASH_MAP;
/*     */       }
/* 422 */       return erasedType.getTypeParameters();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static final class AsKey
/*     */   {
/*     */     private final Class<?> _raw;
/*     */     
/*     */     private final JavaType[] _params;
/*     */     
/*     */     private final int _hash;
/*     */     
/*     */     public AsKey(Class<?> raw, JavaType[] params, int hash)
/*     */     {
/* 437 */       this._raw = raw;
/* 438 */       this._params = params;
/* 439 */       this._hash = hash;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 443 */       return this._hash;
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 447 */       if (o == this) return true;
/* 448 */       if (o == null) return false;
/* 449 */       if (o.getClass() != getClass()) return false;
/* 450 */       AsKey other = (AsKey)o;
/*     */       
/* 452 */       if ((this._hash == other._hash) && (this._raw == other._raw)) {
/* 453 */         JavaType[] otherParams = other._params;
/* 454 */         int len = this._params.length;
/*     */         
/* 456 */         if (len == otherParams.length) {
/* 457 */           for (int i = 0; i < len; i++) {
/* 458 */             if (!this._params[i].equals(otherParams[i])) {
/* 459 */               return false;
/*     */             }
/*     */           }
/* 462 */           return true;
/*     */         }
/*     */       }
/* 465 */       return false;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 470 */       return this._raw.getName() + "<>";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\type\TypeBindings.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */