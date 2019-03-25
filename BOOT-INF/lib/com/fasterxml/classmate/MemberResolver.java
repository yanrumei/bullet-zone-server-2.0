/*     */ package com.fasterxml.classmate;
/*     */ 
/*     */ import com.fasterxml.classmate.members.HierarchicType;
/*     */ import com.fasterxml.classmate.members.RawConstructor;
/*     */ import com.fasterxml.classmate.members.RawField;
/*     */ import com.fasterxml.classmate.members.RawMethod;
/*     */ import com.fasterxml.classmate.util.ClassKey;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MemberResolver
/*     */   implements Serializable
/*     */ {
/*     */   protected final TypeResolver _typeResolver;
/*     */   protected boolean _cfgIncludeLangObject;
/*     */   protected Filter<RawField> _fieldFilter;
/*     */   protected Filter<RawMethod> _methodFilter;
/*     */   protected Filter<RawConstructor> _constructorFilter;
/*     */   
/*     */   public MemberResolver(TypeResolver typeResolver)
/*     */   {
/*  72 */     this._typeResolver = typeResolver;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MemberResolver setIncludeLangObject(boolean state)
/*     */   {
/*  81 */     this._cfgIncludeLangObject = state;
/*  82 */     return this;
/*     */   }
/*     */   
/*     */   public MemberResolver setFieldFilter(Filter<RawField> f) {
/*  86 */     this._fieldFilter = f;
/*  87 */     return this;
/*     */   }
/*     */   
/*     */   public MemberResolver setMethodFilter(Filter<RawMethod> f) {
/*  91 */     this._methodFilter = f;
/*  92 */     return this;
/*     */   }
/*     */   
/*     */   public MemberResolver setConstructorFilter(Filter<RawConstructor> f) {
/*  96 */     this._constructorFilter = f;
/*  97 */     return this;
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
/*     */ 
/*     */ 
/*     */   public ResolvedTypeWithMembers resolve(ResolvedType mainType, AnnotationConfiguration annotationConfig, AnnotationOverrides annotationOverrides)
/*     */   {
/* 120 */     List<ResolvedType> types = new ArrayList();
/* 121 */     HashSet<ClassKey> seenTypes = new HashSet();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 128 */     if ((!this._cfgIncludeLangObject) && (mainType.getErasedType() == Object.class)) {
/* 129 */       types = new ArrayList(1);
/* 130 */       types.add(mainType);
/* 131 */       seenTypes.add(new ClassKey(Object.class));
/*     */     } else {
/* 133 */       types = new ArrayList();
/* 134 */       _gatherTypes(mainType, seenTypes, types);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 139 */     HierarchicType mainHierarchicType = null;
/*     */     
/*     */     HierarchicType[] htypes;
/* 142 */     if (annotationOverrides == null) {
/* 143 */       int len = types.size();
/* 144 */       HierarchicType[] htypes = new HierarchicType[len];
/* 145 */       for (int i = 0; i < len; i++)
/*     */       {
/* 147 */         htypes[i] = new HierarchicType((ResolvedType)types.get(i), false, i);
/*     */       }
/* 149 */       mainHierarchicType = htypes[0];
/*     */     } else {
/* 151 */       ArrayList<HierarchicType> typesWithMixins = new ArrayList();
/* 152 */       for (ResolvedType type : types)
/*     */       {
/* 154 */         List<Class<?>> m = annotationOverrides.mixInsFor(type.getErasedType());
/* 155 */         if (m != null) {
/* 156 */           for (Class<?> mixinClass : m) {
/* 157 */             _addOverrides(typesWithMixins, seenTypes, mixinClass);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 162 */         HierarchicType ht = new HierarchicType(type, false, typesWithMixins.size());
/* 163 */         if (mainHierarchicType == null) {
/* 164 */           mainHierarchicType = ht;
/*     */         }
/* 166 */         typesWithMixins.add(ht);
/*     */       }
/* 168 */       htypes = (HierarchicType[])typesWithMixins.toArray(new HierarchicType[typesWithMixins.size()]);
/*     */     }
/*     */     
/* 171 */     return new ResolvedTypeWithMembers(this._typeResolver, annotationConfig, mainHierarchicType, htypes, this._constructorFilter, this._fieldFilter, this._methodFilter);
/*     */   }
/*     */   
/*     */ 
/*     */   private void _addOverrides(List<HierarchicType> typesWithOverrides, Set<ClassKey> seenTypes, Class<?> override)
/*     */   {
/* 177 */     ClassKey key = new ClassKey(override);
/* 178 */     if (!seenTypes.contains(key)) {
/* 179 */       seenTypes.add(key);
/* 180 */       ResolvedType resolvedOverride = this._typeResolver.resolve(override, new Type[0]);
/* 181 */       typesWithOverrides.add(new HierarchicType(resolvedOverride, true, typesWithOverrides.size()));
/* 182 */       for (ResolvedType r : resolvedOverride.getImplementedInterfaces()) {
/* 183 */         _addOverrides(typesWithOverrides, seenTypes, r);
/*     */       }
/* 185 */       ResolvedType superClass = resolvedOverride.getParentClass();
/* 186 */       _addOverrides(typesWithOverrides, seenTypes, superClass);
/*     */     }
/*     */   }
/*     */   
/*     */   private void _addOverrides(List<HierarchicType> typesWithOverrides, Set<ClassKey> seenTypes, ResolvedType override)
/*     */   {
/* 192 */     if (override == null) { return;
/*     */     }
/* 194 */     Class<?> raw = override.getErasedType();
/* 195 */     if ((!this._cfgIncludeLangObject) && (Object.class == raw)) return;
/* 196 */     ClassKey key = new ClassKey(raw);
/* 197 */     if (!seenTypes.contains(key)) {
/* 198 */       seenTypes.add(key);
/* 199 */       typesWithOverrides.add(new HierarchicType(override, true, typesWithOverrides.size()));
/* 200 */       for (ResolvedType r : override.getImplementedInterfaces()) {
/* 201 */         _addOverrides(typesWithOverrides, seenTypes, r);
/*     */       }
/* 203 */       ResolvedType superClass = override.getParentClass();
/* 204 */       if (superClass != null) {
/* 205 */         _addOverrides(typesWithOverrides, seenTypes, superClass);
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
/*     */   protected void _gatherTypes(ResolvedType currentType, Set<ClassKey> seenTypes, List<ResolvedType> types)
/*     */   {
/* 220 */     if (currentType == null) {
/* 221 */       return;
/*     */     }
/* 223 */     Class<?> raw = currentType.getErasedType();
/*     */     
/* 225 */     if ((!this._cfgIncludeLangObject) && (raw == Object.class)) {
/* 226 */       return;
/*     */     }
/*     */     
/* 229 */     ClassKey key = new ClassKey(currentType.getErasedType());
/* 230 */     if (seenTypes.contains(key)) {
/* 231 */       return;
/*     */     }
/*     */     
/* 234 */     seenTypes.add(key);
/* 235 */     types.add(currentType);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 242 */     for (ResolvedType t : currentType.getImplementedInterfaces()) {
/* 243 */       _gatherTypes(t, seenTypes, types);
/*     */     }
/*     */     
/* 246 */     _gatherTypes(currentType.getParentClass(), seenTypes, types);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmate\MemberResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */