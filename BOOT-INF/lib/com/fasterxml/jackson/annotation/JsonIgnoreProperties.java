/*     */ package com.fasterxml.jackson.annotation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.annotation.Target;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Target({java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.FIELD})
/*     */ @Retention(RetentionPolicy.RUNTIME)
/*     */ @JacksonAnnotation
/*     */ public @interface JsonIgnoreProperties
/*     */ {
/*     */   String[] value() default {};
/*     */   
/*     */   boolean ignoreUnknown() default false;
/*     */   
/*     */   boolean allowGetters() default false;
/*     */   
/*     */   boolean allowSetters() default false;
/*     */   
/*     */   public static class Value
/*     */     implements JacksonAnnotationValue<JsonIgnoreProperties>, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/* 108 */     protected static final Value EMPTY = new Value(Collections.emptySet(), false, false, false, true);
/*     */     
/*     */ 
/*     */     protected final Set<String> _ignored;
/*     */     
/*     */ 
/*     */     protected final boolean _ignoreUnknown;
/*     */     
/*     */ 
/*     */     protected final boolean _allowGetters;
/*     */     
/*     */     protected final boolean _allowSetters;
/*     */     
/*     */     protected final boolean _merge;
/*     */     
/*     */ 
/*     */     protected Value(Set<String> ignored, boolean ignoreUnknown, boolean allowGetters, boolean allowSetters, boolean merge)
/*     */     {
/* 126 */       if (ignored == null) {
/* 127 */         this._ignored = Collections.emptySet();
/*     */       } else {
/* 129 */         this._ignored = ignored;
/*     */       }
/* 131 */       this._ignoreUnknown = ignoreUnknown;
/* 132 */       this._allowGetters = allowGetters;
/* 133 */       this._allowSetters = allowSetters;
/* 134 */       this._merge = merge;
/*     */     }
/*     */     
/*     */     public static Value from(JsonIgnoreProperties src) {
/* 138 */       if (src == null) {
/* 139 */         return null;
/*     */       }
/* 141 */       return construct(_asSet(src.value()), src.ignoreUnknown(), src.allowGetters(), src.allowSetters(), false);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public static Value construct(Set<String> ignored, boolean ignoreUnknown, boolean allowGetters, boolean allowSetters, boolean merge)
/*     */     {
/* 159 */       if (_empty(ignored, ignoreUnknown, allowGetters, allowSetters, merge)) {
/* 160 */         return EMPTY;
/*     */       }
/* 162 */       if (_empty(ignored, ignoreUnknown, allowGetters, allowSetters, merge)) {
/* 163 */         return EMPTY;
/*     */       }
/* 165 */       return new Value(ignored, ignoreUnknown, allowGetters, allowSetters, merge);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public static Value empty()
/*     */     {
/* 185 */       return EMPTY;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public static Value merge(Value base, Value overrides)
/*     */     {
/* 199 */       return base == null ? overrides : base.withOverrides(overrides);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public static Value mergeAll(Value... values)
/*     */     {
/* 208 */       Value result = null;
/* 209 */       for (Value curr : values) {
/* 210 */         if (curr != null) {
/* 211 */           result = result == null ? curr : result.withOverrides(curr);
/*     */         }
/*     */       }
/* 214 */       return result;
/*     */     }
/*     */     
/*     */     public static Value forIgnoredProperties(Set<String> propNames) {
/* 218 */       return EMPTY.withIgnored(propNames);
/*     */     }
/*     */     
/*     */     public static Value forIgnoredProperties(String... propNames) {
/* 222 */       if (propNames.length == 0) {
/* 223 */         return EMPTY;
/*     */       }
/* 225 */       return EMPTY.withIgnored(_asSet(propNames));
/*     */     }
/*     */     
/*     */     public static Value forIgnoreUnknown(boolean state) {
/* 229 */       return state ? EMPTY.withIgnoreUnknown() : EMPTY.withoutIgnoreUnknown();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Value withOverrides(Value overrides)
/*     */     {
/* 240 */       if ((overrides == null) || (overrides == EMPTY)) {
/* 241 */         return this;
/*     */       }
/*     */       
/*     */ 
/* 245 */       if (!overrides._merge) {
/* 246 */         return overrides;
/*     */       }
/* 248 */       if (_equals(this, overrides)) {
/* 249 */         return this;
/*     */       }
/*     */       
/*     */ 
/* 253 */       Set<String> ignored = _merge(this._ignored, overrides._ignored);
/* 254 */       boolean ignoreUnknown = (this._ignoreUnknown) || (overrides._ignoreUnknown);
/* 255 */       boolean allowGetters = (this._allowGetters) || (overrides._allowGetters);
/* 256 */       boolean allowSetters = (this._allowSetters) || (overrides._allowSetters);
/*     */       
/*     */ 
/* 259 */       return construct(ignored, ignoreUnknown, allowGetters, allowSetters, true);
/*     */     }
/*     */     
/*     */     public Value withIgnored(Set<String> ignored) {
/* 263 */       return construct(ignored, this._ignoreUnknown, this._allowGetters, this._allowSetters, this._merge);
/*     */     }
/*     */     
/*     */     public Value withIgnored(String... ignored) {
/* 267 */       return construct(_asSet(ignored), this._ignoreUnknown, this._allowGetters, this._allowSetters, this._merge);
/*     */     }
/*     */     
/*     */     public Value withoutIgnored() {
/* 271 */       return construct(null, this._ignoreUnknown, this._allowGetters, this._allowSetters, this._merge);
/*     */     }
/*     */     
/*     */     public Value withIgnoreUnknown() {
/* 275 */       return this._ignoreUnknown ? this : construct(this._ignored, true, this._allowGetters, this._allowSetters, this._merge);
/*     */     }
/*     */     
/*     */     public Value withoutIgnoreUnknown() {
/* 279 */       return !this._ignoreUnknown ? this : construct(this._ignored, false, this._allowGetters, this._allowSetters, this._merge);
/*     */     }
/*     */     
/*     */     public Value withAllowGetters()
/*     */     {
/* 284 */       return this._allowGetters ? this : construct(this._ignored, this._ignoreUnknown, true, this._allowSetters, this._merge);
/*     */     }
/*     */     
/*     */     public Value withoutAllowGetters() {
/* 288 */       return !this._allowGetters ? this : construct(this._ignored, this._ignoreUnknown, false, this._allowSetters, this._merge);
/*     */     }
/*     */     
/*     */     public Value withAllowSetters()
/*     */     {
/* 293 */       return this._allowSetters ? this : construct(this._ignored, this._ignoreUnknown, this._allowGetters, true, this._merge);
/*     */     }
/*     */     
/*     */     public Value withoutAllowSetters() {
/* 297 */       return !this._allowSetters ? this : construct(this._ignored, this._ignoreUnknown, this._allowGetters, false, this._merge);
/*     */     }
/*     */     
/*     */     public Value withMerge()
/*     */     {
/* 302 */       return this._merge ? this : construct(this._ignored, this._ignoreUnknown, this._allowGetters, this._allowSetters, true);
/*     */     }
/*     */     
/*     */     public Value withoutMerge()
/*     */     {
/* 307 */       return !this._merge ? this : construct(this._ignored, this._ignoreUnknown, this._allowGetters, this._allowSetters, false);
/*     */     }
/*     */     
/*     */ 
/*     */     public Class<JsonIgnoreProperties> valueFor()
/*     */     {
/* 313 */       return JsonIgnoreProperties.class;
/*     */     }
/*     */     
/*     */     protected Object readResolve()
/*     */     {
/* 318 */       if (_empty(this._ignored, this._ignoreUnknown, this._allowGetters, this._allowSetters, this._merge)) {
/* 319 */         return EMPTY;
/*     */       }
/* 321 */       return this;
/*     */     }
/*     */     
/*     */     public Set<String> getIgnored() {
/* 325 */       return this._ignored;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Set<String> findIgnoredForSerialization()
/*     */     {
/* 336 */       if (this._allowGetters) {
/* 337 */         return Collections.emptySet();
/*     */       }
/* 339 */       return this._ignored;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Set<String> findIgnoredForDeserialization()
/*     */     {
/* 350 */       if (this._allowSetters) {
/* 351 */         return Collections.emptySet();
/*     */       }
/* 353 */       return this._ignored;
/*     */     }
/*     */     
/*     */     public boolean getIgnoreUnknown() {
/* 357 */       return this._ignoreUnknown;
/*     */     }
/*     */     
/*     */     public boolean getAllowGetters() {
/* 361 */       return this._allowGetters;
/*     */     }
/*     */     
/*     */     public boolean getAllowSetters() {
/* 365 */       return this._allowSetters;
/*     */     }
/*     */     
/*     */     public boolean getMerge() {
/* 369 */       return this._merge;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 374 */       return String.format("[ignored=%s,ignoreUnknown=%s,allowGetters=%s,allowSetters=%s,merge=%s]", new Object[] { this._ignored, Boolean.valueOf(this._ignoreUnknown), Boolean.valueOf(this._allowGetters), Boolean.valueOf(this._allowSetters), Boolean.valueOf(this._merge) });
/*     */     }
/*     */     
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 380 */       return this._ignored.size() + (this._ignoreUnknown ? 1 : -3) + (this._allowGetters ? 3 : -7) + (this._allowSetters ? 7 : -11) + (this._merge ? 11 : -13);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean equals(Object o)
/*     */     {
/* 390 */       if (o == this) return true;
/* 391 */       if (o == null) return false;
/* 392 */       return (o.getClass() == getClass()) && (_equals(this, (Value)o));
/*     */     }
/*     */     
/*     */ 
/*     */     private static boolean _equals(Value a, Value b)
/*     */     {
/* 398 */       return (a._ignoreUnknown == b._ignoreUnknown) && (a._merge == b._merge) && (a._allowGetters == b._allowGetters) && (a._allowSetters == b._allowSetters) && (a._ignored.equals(b._ignored));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private static Set<String> _asSet(String[] v)
/*     */     {
/* 408 */       if ((v == null) || (v.length == 0)) {
/* 409 */         return Collections.emptySet();
/*     */       }
/* 411 */       Set<String> s = new HashSet(v.length);
/* 412 */       for (String str : v) {
/* 413 */         s.add(str);
/*     */       }
/* 415 */       return s;
/*     */     }
/*     */     
/*     */     private static Set<String> _merge(Set<String> s1, Set<String> s2)
/*     */     {
/* 420 */       if (s1.isEmpty())
/* 421 */         return s2;
/* 422 */       if (s2.isEmpty()) {
/* 423 */         return s1;
/*     */       }
/* 425 */       HashSet<String> result = new HashSet(s1.size() + s2.size());
/* 426 */       result.addAll(s1);
/* 427 */       result.addAll(s2);
/* 428 */       return result;
/*     */     }
/*     */     
/*     */ 
/*     */     private static boolean _empty(Set<String> ignored, boolean ignoreUnknown, boolean allowGetters, boolean allowSetters, boolean merge)
/*     */     {
/* 434 */       if ((ignoreUnknown == EMPTY._ignoreUnknown) && (allowGetters == EMPTY._allowGetters) && (allowSetters == EMPTY._allowSetters) && (merge == EMPTY._merge))
/*     */       {
/*     */ 
/*     */ 
/* 438 */         return (ignored == null) || (ignored.size() == 0);
/*     */       }
/* 440 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-annotations-2.8.0.jar!\com\fasterxml\jackson\annotation\JsonIgnoreProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */