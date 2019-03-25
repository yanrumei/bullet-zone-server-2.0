/*     */ package com.fasterxml.classmate;
/*     */ 
/*     */ import com.fasterxml.classmate.members.RawConstructor;
/*     */ import com.fasterxml.classmate.members.RawField;
/*     */ import com.fasterxml.classmate.members.RawMethod;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ public abstract class ResolvedType implements java.lang.reflect.Type
/*     */ {
/*  15 */   public static final ResolvedType[] NO_TYPES = new ResolvedType[0];
/*     */   
/*  17 */   protected static final RawConstructor[] NO_CONSTRUCTORS = new RawConstructor[0];
/*  18 */   protected static final RawField[] NO_FIELDS = new RawField[0];
/*  19 */   protected static final RawMethod[] NO_METHODS = new RawMethod[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Class<?> _erasedType;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final TypeBindings _typeBindings;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ResolvedType(Class<?> cls, TypeBindings bindings)
/*     */   {
/*  37 */     this._erasedType = cls;
/*  38 */     this._typeBindings = (bindings == null ? TypeBindings.emptyBindings() : bindings);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract boolean canCreateSubtypes();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean canCreateSubtype(Class<?> subtype)
/*     */   {
/*  53 */     return (canCreateSubtypes()) && (this._erasedType.isAssignableFrom(subtype));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getErasedType()
/*     */   {
/*  65 */     return this._erasedType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract ResolvedType getParentClass();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract ResolvedType getSelfReferencedType();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract ResolvedType getArrayElementType();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract List<ResolvedType> getImplementedInterfaces();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<ResolvedType> getTypeParameters()
/*     */   {
/* 104 */     return this._typeBindings.getTypeParameters();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TypeBindings getTypeBindings()
/*     */   {
/* 115 */     return this._typeBindings;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<ResolvedType> typeParametersFor(Class<?> erasedSupertype)
/*     */   {
/* 127 */     ResolvedType type = findSupertype(erasedSupertype);
/* 128 */     if (type != null) {
/* 129 */       return type.getTypeParameters();
/*     */     }
/*     */     
/* 132 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResolvedType findSupertype(Class<?> erasedSupertype)
/*     */   {
/* 144 */     if (erasedSupertype == this._erasedType) {
/* 145 */       return this;
/*     */     }
/*     */     
/* 148 */     if (erasedSupertype.isInterface()) {
/* 149 */       for (ResolvedType it : getImplementedInterfaces()) {
/* 150 */         ResolvedType type = it.findSupertype(erasedSupertype);
/* 151 */         if (type != null) {
/* 152 */           return type;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 157 */     ResolvedType pc = getParentClass();
/* 158 */     if (pc != null) {
/* 159 */       ResolvedType type = pc.findSupertype(erasedSupertype);
/* 160 */       if (type != null) {
/* 161 */         return type;
/*     */       }
/*     */     }
/*     */     
/* 165 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract boolean isInterface();
/*     */   
/*     */ 
/*     */   public final boolean isConcrete()
/*     */   {
/* 175 */     return !isAbstract();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract boolean isAbstract();
/*     */   
/*     */ 
/*     */   public abstract boolean isArray();
/*     */   
/*     */ 
/*     */   public abstract boolean isPrimitive();
/*     */   
/*     */ 
/*     */   public final boolean isInstanceOf(Class<?> type)
/*     */   {
/* 191 */     return type.isAssignableFrom(this._erasedType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 200 */   public List<RawConstructor> getConstructors() { return Collections.emptyList(); }
/* 201 */   public List<RawField> getMemberFields() { return Collections.emptyList(); }
/* 202 */   public List<RawMethod> getMemberMethods() { return Collections.emptyList(); }
/* 203 */   public List<RawField> getStaticFields() { return Collections.emptyList(); }
/* 204 */   public List<RawMethod> getStaticMethods() { return Collections.emptyList(); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSignature()
/*     */   {
/* 217 */     StringBuilder sb = new StringBuilder();
/* 218 */     return appendSignature(sb).toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getErasedSignature()
/*     */   {
/* 226 */     StringBuilder sb = new StringBuilder();
/* 227 */     return appendErasedSignature(sb).toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFullDescription()
/*     */   {
/* 235 */     StringBuilder sb = new StringBuilder();
/* 236 */     return appendFullDescription(sb).toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getBriefDescription()
/*     */   {
/* 244 */     StringBuilder sb = new StringBuilder();
/* 245 */     return appendBriefDescription(sb).toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract StringBuilder appendBriefDescription(StringBuilder paramStringBuilder);
/*     */   
/*     */ 
/*     */   public abstract StringBuilder appendFullDescription(StringBuilder paramStringBuilder);
/*     */   
/*     */   public abstract StringBuilder appendSignature(StringBuilder paramStringBuilder);
/*     */   
/*     */   public abstract StringBuilder appendErasedSignature(StringBuilder paramStringBuilder);
/*     */   
/*     */   public String toString()
/*     */   {
/* 260 */     return getBriefDescription();
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 264 */     return this._erasedType.getName().hashCode() + this._typeBindings.hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 269 */     if (o == this) { return true;
/*     */     }
/* 271 */     if ((o == null) || (o.getClass() != getClass())) { return false;
/*     */     }
/* 273 */     ResolvedType other = (ResolvedType)o;
/* 274 */     if (other._erasedType != this._erasedType) {
/* 275 */       return false;
/*     */     }
/*     */     
/* 278 */     return this._typeBindings.equals(other._typeBindings);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected StringBuilder _appendClassSignature(StringBuilder sb)
/*     */   {
/* 289 */     sb.append('L');
/* 290 */     sb = _appendClassName(sb);
/* 291 */     int count = this._typeBindings.size();
/* 292 */     if (count > 0) {
/* 293 */       sb.append('<');
/* 294 */       for (int i = 0; i < count; i++) {
/* 295 */         sb = this._typeBindings.getBoundType(i).appendErasedSignature(sb);
/*     */       }
/* 297 */       sb.append('>');
/*     */     }
/* 299 */     sb.append(';');
/* 300 */     return sb;
/*     */   }
/*     */   
/*     */   protected StringBuilder _appendErasedClassSignature(StringBuilder sb)
/*     */   {
/* 305 */     sb.append('L');
/* 306 */     sb = _appendClassName(sb);
/* 307 */     sb.append(';');
/* 308 */     return sb;
/*     */   }
/*     */   
/*     */   protected StringBuilder _appendClassDescription(StringBuilder sb)
/*     */   {
/* 313 */     sb.append(this._erasedType.getName());
/* 314 */     int count = this._typeBindings.size();
/* 315 */     if (count > 0) {
/* 316 */       sb.append('<');
/* 317 */       for (int i = 0; i < count; i++) {
/* 318 */         if (i > 0) {
/* 319 */           sb.append(',');
/*     */         }
/* 321 */         sb = this._typeBindings.getBoundType(i).appendBriefDescription(sb);
/*     */       }
/* 323 */       sb.append('>');
/*     */     }
/* 325 */     return sb;
/*     */   }
/*     */   
/*     */   protected StringBuilder _appendClassName(StringBuilder sb)
/*     */   {
/* 330 */     String name = this._erasedType.getName();
/* 331 */     int i = 0; for (int len = name.length(); i < len; i++) {
/* 332 */       char c = name.charAt(i);
/* 333 */       if (c == '.') c = '/';
/* 334 */       sb.append(c);
/*     */     }
/* 336 */     return sb;
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
/*     */   protected RawField[] _getFields(boolean statics)
/*     */   {
/* 350 */     ArrayList<RawField> fields = new ArrayList();
/* 351 */     for (Field f : this._erasedType.getDeclaredFields())
/*     */     {
/* 353 */       if ((!f.isSynthetic()) && 
/* 354 */         (Modifier.isStatic(f.getModifiers()) == statics)) {
/* 355 */         fields.add(new RawField(this, f));
/*     */       }
/*     */     }
/*     */     
/* 359 */     if (fields.isEmpty()) {
/* 360 */       return NO_FIELDS;
/*     */     }
/* 362 */     return (RawField[])fields.toArray(new RawField[fields.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected RawMethod[] _getMethods(boolean statics)
/*     */   {
/* 370 */     ArrayList<RawMethod> methods = new ArrayList();
/* 371 */     for (Method m : this._erasedType.getDeclaredMethods())
/*     */     {
/* 373 */       if ((!m.isSynthetic()) && 
/* 374 */         (Modifier.isStatic(m.getModifiers()) == statics)) {
/* 375 */         methods.add(new RawMethod(this, m));
/*     */       }
/*     */     }
/*     */     
/* 379 */     if (methods.isEmpty()) {
/* 380 */       return NO_METHODS;
/*     */     }
/* 382 */     return (RawMethod[])methods.toArray(new RawMethod[methods.size()]);
/*     */   }
/*     */   
/*     */   protected RawConstructor[] _getConstructors()
/*     */   {
/* 387 */     ArrayList<RawConstructor> ctors = new ArrayList();
/* 388 */     for (java.lang.reflect.Constructor<?> c : this._erasedType.getDeclaredConstructors())
/*     */     {
/* 390 */       if (!c.isSynthetic()) {
/* 391 */         ctors.add(new RawConstructor(this, c));
/*     */       }
/*     */     }
/* 394 */     if (ctors.isEmpty()) {
/* 395 */       return NO_CONSTRUCTORS;
/*     */     }
/* 397 */     return (RawConstructor[])ctors.toArray(new RawConstructor[ctors.size()]);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmate\ResolvedType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */