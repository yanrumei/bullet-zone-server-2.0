/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonAutoDetect;
/*     */ import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
/*     */ import com.fasterxml.jackson.annotation.PropertyAccessor;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract interface VisibilityChecker<T extends VisibilityChecker<T>>
/*     */ {
/*     */   public abstract T with(JsonAutoDetect paramJsonAutoDetect);
/*     */   
/*     */   public abstract T with(JsonAutoDetect.Visibility paramVisibility);
/*     */   
/*     */   public abstract T withVisibility(PropertyAccessor paramPropertyAccessor, JsonAutoDetect.Visibility paramVisibility);
/*     */   
/*     */   public abstract T withGetterVisibility(JsonAutoDetect.Visibility paramVisibility);
/*     */   
/*     */   public abstract T withIsGetterVisibility(JsonAutoDetect.Visibility paramVisibility);
/*     */   
/*     */   public abstract T withSetterVisibility(JsonAutoDetect.Visibility paramVisibility);
/*     */   
/*     */   public abstract T withCreatorVisibility(JsonAutoDetect.Visibility paramVisibility);
/*     */   
/*     */   public abstract T withFieldVisibility(JsonAutoDetect.Visibility paramVisibility);
/*     */   
/*     */   public abstract boolean isGetterVisible(Method paramMethod);
/*     */   
/*     */   public abstract boolean isGetterVisible(AnnotatedMethod paramAnnotatedMethod);
/*     */   
/*     */   public abstract boolean isIsGetterVisible(Method paramMethod);
/*     */   
/*     */   public abstract boolean isIsGetterVisible(AnnotatedMethod paramAnnotatedMethod);
/*     */   
/*     */   public abstract boolean isSetterVisible(Method paramMethod);
/*     */   
/*     */   public abstract boolean isSetterVisible(AnnotatedMethod paramAnnotatedMethod);
/*     */   
/*     */   public abstract boolean isCreatorVisible(Member paramMember);
/*     */   
/*     */   public abstract boolean isCreatorVisible(AnnotatedMember paramAnnotatedMember);
/*     */   
/*     */   public abstract boolean isFieldVisible(Field paramField);
/*     */   
/*     */   public abstract boolean isFieldVisible(AnnotatedField paramAnnotatedField);
/*     */   
/*     */   @JsonAutoDetect(getterVisibility=JsonAutoDetect.Visibility.PUBLIC_ONLY, isGetterVisibility=JsonAutoDetect.Visibility.PUBLIC_ONLY, setterVisibility=JsonAutoDetect.Visibility.ANY, creatorVisibility=JsonAutoDetect.Visibility.ANY, fieldVisibility=JsonAutoDetect.Visibility.PUBLIC_ONLY)
/*     */   public static class Std
/*     */     implements VisibilityChecker<Std>, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/* 170 */     protected static final Std DEFAULT = new Std((JsonAutoDetect)Std.class.getAnnotation(JsonAutoDetect.class));
/*     */     protected final JsonAutoDetect.Visibility _getterMinLevel;
/*     */     protected final JsonAutoDetect.Visibility _isGetterMinLevel;
/*     */     protected final JsonAutoDetect.Visibility _setterMinLevel;
/*     */     protected final JsonAutoDetect.Visibility _creatorMinLevel;
/*     */     protected final JsonAutoDetect.Visibility _fieldMinLevel;
/*     */     
/*     */     public static Std defaultInstance() {
/* 178 */       return DEFAULT;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Std(JsonAutoDetect ann)
/*     */     {
/* 189 */       this._getterMinLevel = ann.getterVisibility();
/* 190 */       this._isGetterMinLevel = ann.isGetterVisibility();
/* 191 */       this._setterMinLevel = ann.setterVisibility();
/* 192 */       this._creatorMinLevel = ann.creatorVisibility();
/* 193 */       this._fieldMinLevel = ann.fieldVisibility();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Std(JsonAutoDetect.Visibility getter, JsonAutoDetect.Visibility isGetter, JsonAutoDetect.Visibility setter, JsonAutoDetect.Visibility creator, JsonAutoDetect.Visibility field)
/*     */     {
/* 201 */       this._getterMinLevel = getter;
/* 202 */       this._isGetterMinLevel = isGetter;
/* 203 */       this._setterMinLevel = setter;
/* 204 */       this._creatorMinLevel = creator;
/* 205 */       this._fieldMinLevel = field;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Std(JsonAutoDetect.Visibility v)
/*     */     {
/* 217 */       if (v == JsonAutoDetect.Visibility.DEFAULT) {
/* 218 */         this._getterMinLevel = DEFAULT._getterMinLevel;
/* 219 */         this._isGetterMinLevel = DEFAULT._isGetterMinLevel;
/* 220 */         this._setterMinLevel = DEFAULT._setterMinLevel;
/* 221 */         this._creatorMinLevel = DEFAULT._creatorMinLevel;
/* 222 */         this._fieldMinLevel = DEFAULT._fieldMinLevel;
/*     */       } else {
/* 224 */         this._getterMinLevel = v;
/* 225 */         this._isGetterMinLevel = v;
/* 226 */         this._setterMinLevel = v;
/* 227 */         this._creatorMinLevel = v;
/* 228 */         this._fieldMinLevel = v;
/*     */       }
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
/*     */     public Std with(JsonAutoDetect ann)
/*     */     {
/* 242 */       Std curr = this;
/* 243 */       if (ann != null) {
/* 244 */         curr = curr.withGetterVisibility(ann.getterVisibility());
/* 245 */         curr = curr.withIsGetterVisibility(ann.isGetterVisibility());
/* 246 */         curr = curr.withSetterVisibility(ann.setterVisibility());
/* 247 */         curr = curr.withCreatorVisibility(ann.creatorVisibility());
/* 248 */         curr = curr.withFieldVisibility(ann.fieldVisibility());
/*     */       }
/* 250 */       return curr;
/*     */     }
/*     */     
/*     */ 
/*     */     public Std with(JsonAutoDetect.Visibility v)
/*     */     {
/* 256 */       if (v == JsonAutoDetect.Visibility.DEFAULT) {
/* 257 */         return DEFAULT;
/*     */       }
/* 259 */       return new Std(v);
/*     */     }
/*     */     
/*     */ 
/*     */     public Std withVisibility(PropertyAccessor method, JsonAutoDetect.Visibility v)
/*     */     {
/* 265 */       switch (VisibilityChecker.1.$SwitchMap$com$fasterxml$jackson$annotation$PropertyAccessor[method.ordinal()]) {
/*     */       case 1: 
/* 267 */         return withGetterVisibility(v);
/*     */       case 2: 
/* 269 */         return withSetterVisibility(v);
/*     */       case 3: 
/* 271 */         return withCreatorVisibility(v);
/*     */       case 4: 
/* 273 */         return withFieldVisibility(v);
/*     */       case 5: 
/* 275 */         return withIsGetterVisibility(v);
/*     */       case 6: 
/* 277 */         return with(v);
/*     */       }
/*     */       
/*     */       
/* 281 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */     public Std withGetterVisibility(JsonAutoDetect.Visibility v)
/*     */     {
/* 287 */       if (v == JsonAutoDetect.Visibility.DEFAULT) v = DEFAULT._getterMinLevel;
/* 288 */       if (this._getterMinLevel == v) return this;
/* 289 */       return new Std(v, this._isGetterMinLevel, this._setterMinLevel, this._creatorMinLevel, this._fieldMinLevel);
/*     */     }
/*     */     
/*     */     public Std withIsGetterVisibility(JsonAutoDetect.Visibility v)
/*     */     {
/* 294 */       if (v == JsonAutoDetect.Visibility.DEFAULT) v = DEFAULT._isGetterMinLevel;
/* 295 */       if (this._isGetterMinLevel == v) return this;
/* 296 */       return new Std(this._getterMinLevel, v, this._setterMinLevel, this._creatorMinLevel, this._fieldMinLevel);
/*     */     }
/*     */     
/*     */     public Std withSetterVisibility(JsonAutoDetect.Visibility v)
/*     */     {
/* 301 */       if (v == JsonAutoDetect.Visibility.DEFAULT) v = DEFAULT._setterMinLevel;
/* 302 */       if (this._setterMinLevel == v) return this;
/* 303 */       return new Std(this._getterMinLevel, this._isGetterMinLevel, v, this._creatorMinLevel, this._fieldMinLevel);
/*     */     }
/*     */     
/*     */     public Std withCreatorVisibility(JsonAutoDetect.Visibility v)
/*     */     {
/* 308 */       if (v == JsonAutoDetect.Visibility.DEFAULT) v = DEFAULT._creatorMinLevel;
/* 309 */       if (this._creatorMinLevel == v) return this;
/* 310 */       return new Std(this._getterMinLevel, this._isGetterMinLevel, this._setterMinLevel, v, this._fieldMinLevel);
/*     */     }
/*     */     
/*     */     public Std withFieldVisibility(JsonAutoDetect.Visibility v)
/*     */     {
/* 315 */       if (v == JsonAutoDetect.Visibility.DEFAULT) v = DEFAULT._fieldMinLevel;
/* 316 */       if (this._fieldMinLevel == v) return this;
/* 317 */       return new Std(this._getterMinLevel, this._isGetterMinLevel, this._setterMinLevel, this._creatorMinLevel, v);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isCreatorVisible(Member m)
/*     */     {
/* 328 */       return this._creatorMinLevel.isVisible(m);
/*     */     }
/*     */     
/*     */     public boolean isCreatorVisible(AnnotatedMember m)
/*     */     {
/* 333 */       return isCreatorVisible(m.getMember());
/*     */     }
/*     */     
/*     */     public boolean isFieldVisible(Field f)
/*     */     {
/* 338 */       return this._fieldMinLevel.isVisible(f);
/*     */     }
/*     */     
/*     */     public boolean isFieldVisible(AnnotatedField f)
/*     */     {
/* 343 */       return isFieldVisible(f.getAnnotated());
/*     */     }
/*     */     
/*     */     public boolean isGetterVisible(Method m)
/*     */     {
/* 348 */       return this._getterMinLevel.isVisible(m);
/*     */     }
/*     */     
/*     */     public boolean isGetterVisible(AnnotatedMethod m)
/*     */     {
/* 353 */       return isGetterVisible(m.getAnnotated());
/*     */     }
/*     */     
/*     */     public boolean isIsGetterVisible(Method m)
/*     */     {
/* 358 */       return this._isGetterMinLevel.isVisible(m);
/*     */     }
/*     */     
/*     */     public boolean isIsGetterVisible(AnnotatedMethod m)
/*     */     {
/* 363 */       return isIsGetterVisible(m.getAnnotated());
/*     */     }
/*     */     
/*     */     public boolean isSetterVisible(Method m)
/*     */     {
/* 368 */       return this._setterMinLevel.isVisible(m);
/*     */     }
/*     */     
/*     */     public boolean isSetterVisible(AnnotatedMethod m)
/*     */     {
/* 373 */       return isSetterVisible(m.getAnnotated());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String toString()
/*     */     {
/* 384 */       return "[Visibility:" + " getter: " + this._getterMinLevel + ", isGetter: " + this._isGetterMinLevel + ", setter: " + this._setterMinLevel + ", creator: " + this._creatorMinLevel + ", field: " + this._fieldMinLevel + "]";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\introspect\VisibilityChecker.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */