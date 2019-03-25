/*     */ package com.fasterxml.classmate;
/*     */ 
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TypeBindings
/*     */ {
/*  12 */   private static final String[] NO_STRINGS = new String[0];
/*     */   
/*  14 */   private static final ResolvedType[] NO_TYPES = new ResolvedType[0];
/*     */   
/*  16 */   private static final TypeBindings EMPTY = new TypeBindings(NO_STRINGS, NO_TYPES, null);
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
/*     */   private final ResolvedType[] _types;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final String[] _unboundVariables;
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
/*     */   private TypeBindings(String[] names, ResolvedType[] types, String[] uvars)
/*     */   {
/*  45 */     this._names = (names == null ? NO_STRINGS : names);
/*  46 */     this._types = (types == null ? NO_TYPES : types);
/*  47 */     if (this._names.length != this._types.length) {
/*  48 */       throw new IllegalArgumentException("Mismatching names (" + this._names.length + "), types (" + this._types.length + ")");
/*     */     }
/*  50 */     int h = 1;
/*  51 */     int i = 0; for (int len = this._types.length; i < len; i++) {
/*  52 */       h += this._types[i].hashCode();
/*     */     }
/*  54 */     this._unboundVariables = uvars;
/*  55 */     this._hashCode = h;
/*     */   }
/*     */   
/*     */   public static TypeBindings emptyBindings() {
/*  59 */     return EMPTY;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TypeBindings create(Class<?> erasedType, List<ResolvedType> typeList)
/*     */   {
/*  68 */     ResolvedType[] types = (typeList == null) || (typeList.isEmpty()) ? NO_TYPES : (ResolvedType[])typeList.toArray(new ResolvedType[typeList.size()]);
/*     */     
/*  70 */     return create(erasedType, types);
/*     */   }
/*     */   
/*     */   public static TypeBindings create(Class<?> erasedType, ResolvedType[] types)
/*     */   {
/*  75 */     if (types == null) {
/*  76 */       types = NO_TYPES;
/*     */     }
/*  78 */     TypeVariable<?>[] vars = erasedType.getTypeParameters();
/*     */     String[] names;
/*  80 */     String[] names; if ((vars == null) || (vars.length == 0)) {
/*  81 */       names = NO_STRINGS;
/*     */     } else {
/*  83 */       int len = vars.length;
/*  84 */       names = new String[len];
/*  85 */       for (int i = 0; i < len; i++) {
/*  86 */         names[i] = vars[i].getName();
/*     */       }
/*     */     }
/*     */     
/*  90 */     if (names.length != types.length) {
/*  91 */       throw new IllegalArgumentException("Can not create TypeBinding for class " + erasedType.getName() + " with " + types.length + " type parameter" + (types.length == 1 ? "" : "s") + ": class expects " + names.length);
/*     */     }
/*     */     
/*     */ 
/*  95 */     return new TypeBindings(names, types, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TypeBindings withUnboundVariable(String name)
/*     */   {
/* 107 */     int len = this._unboundVariables == null ? 0 : this._unboundVariables.length;
/* 108 */     String[] names = len == 0 ? new String[1] : (String[])Arrays.copyOf(this._unboundVariables, len + 1);
/*     */     
/* 110 */     names[len] = name;
/* 111 */     return new TypeBindings(this._names, this._types, names);
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
/*     */   public ResolvedType findBoundType(String name)
/*     */   {
/* 125 */     int i = 0; for (int len = this._names.length; i < len; i++) {
/* 126 */       if (name.equals(this._names[i])) {
/* 127 */         return this._types[i];
/*     */       }
/*     */     }
/* 130 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 134 */     return this._types.length == 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 141 */     return this._types.length;
/*     */   }
/*     */   
/*     */   public String getBoundName(int index)
/*     */   {
/* 146 */     if ((index < 0) || (index >= this._names.length)) {
/* 147 */       return null;
/*     */     }
/* 149 */     return this._names[index];
/*     */   }
/*     */   
/*     */   public ResolvedType getBoundType(int index)
/*     */   {
/* 154 */     if ((index < 0) || (index >= this._types.length)) {
/* 155 */       return null;
/*     */     }
/* 157 */     return this._types[index];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<ResolvedType> getTypeParameters()
/*     */   {
/* 165 */     if (this._types.length == 0) {
/* 166 */       return Collections.emptyList();
/*     */     }
/* 168 */     return Arrays.asList(this._types);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean hasUnbound(String name)
/*     */   {
/* 175 */     if (this._unboundVariables != null) {
/* 176 */       int i = this._unboundVariables.length; do { i--; if (i < 0) break;
/* 177 */       } while (!name.equals(this._unboundVariables[i]));
/* 178 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 182 */     return false;
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
/* 193 */     if (this._types.length == 0) {
/* 194 */       return "";
/*     */     }
/* 196 */     StringBuilder sb = new StringBuilder();
/* 197 */     sb.append('<');
/* 198 */     int i = 0; for (int len = this._types.length; i < len; i++) {
/* 199 */       if (i > 0) {
/* 200 */         sb.append(',');
/*     */       }
/* 202 */       sb = this._types[i].appendBriefDescription(sb);
/*     */     }
/* 204 */     sb.append('>');
/* 205 */     return sb.toString();
/*     */   }
/*     */   
/* 208 */   public int hashCode() { return this._hashCode; }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 212 */     if (o == this) return true;
/* 213 */     if ((o == null) || (o.getClass() != getClass())) return false;
/* 214 */     TypeBindings other = (TypeBindings)o;
/* 215 */     int len = this._types.length;
/* 216 */     if (len != other.size()) {
/* 217 */       return false;
/*     */     }
/* 219 */     ResolvedType[] otherTypes = other._types;
/* 220 */     for (int i = 0; i < len; i++) {
/* 221 */       if (!otherTypes[i].equals(this._types[i])) {
/* 222 */         return false;
/*     */       }
/*     */     }
/* 225 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ResolvedType[] typeParameterArray()
/*     */   {
/* 235 */     return this._types;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmate\TypeBindings.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */