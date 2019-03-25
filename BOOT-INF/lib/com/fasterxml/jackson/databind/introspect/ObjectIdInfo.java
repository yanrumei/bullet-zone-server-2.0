/*    */ package com.fasterxml.jackson.databind.introspect;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*    */ import com.fasterxml.jackson.annotation.ObjectIdResolver;
/*    */ import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
/*    */ import com.fasterxml.jackson.databind.PropertyName;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ObjectIdInfo
/*    */ {
/*    */   protected final PropertyName _propertyName;
/*    */   protected final Class<? extends ObjectIdGenerator<?>> _generator;
/*    */   protected final Class<? extends ObjectIdResolver> _resolver;
/*    */   protected final Class<?> _scope;
/*    */   protected final boolean _alwaysAsId;
/* 25 */   private static final ObjectIdInfo EMPTY = new ObjectIdInfo(PropertyName.NO_NAME, Object.class, null, false, null);
/*    */   
/*    */ 
/*    */   public ObjectIdInfo(PropertyName name, Class<?> scope, Class<? extends ObjectIdGenerator<?>> gen, Class<? extends ObjectIdResolver> resolver)
/*    */   {
/* 30 */     this(name, scope, gen, false, resolver);
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   public ObjectIdInfo(PropertyName name, Class<?> scope, Class<? extends ObjectIdGenerator<?>> gen)
/*    */   {
/* 36 */     this(name, scope, gen, false);
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   public ObjectIdInfo(String name, Class<?> scope, Class<? extends ObjectIdGenerator<?>> gen) {
/* 41 */     this(new PropertyName(name), scope, gen, false);
/*    */   }
/*    */   
/*    */ 
/*    */   protected ObjectIdInfo(PropertyName prop, Class<?> scope, Class<? extends ObjectIdGenerator<?>> gen, boolean alwaysAsId)
/*    */   {
/* 47 */     this(prop, scope, gen, alwaysAsId, SimpleObjectIdResolver.class);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected ObjectIdInfo(PropertyName prop, Class<?> scope, Class<? extends ObjectIdGenerator<?>> gen, boolean alwaysAsId, Class<? extends ObjectIdResolver> resolver)
/*    */   {
/* 54 */     this._propertyName = prop;
/* 55 */     this._scope = scope;
/* 56 */     this._generator = gen;
/* 57 */     this._alwaysAsId = alwaysAsId;
/* 58 */     if (resolver == null) {
/* 59 */       resolver = SimpleObjectIdResolver.class;
/*    */     }
/* 61 */     this._resolver = resolver;
/*    */   }
/*    */   
/*    */   public static ObjectIdInfo empty() {
/* 65 */     return EMPTY;
/*    */   }
/*    */   
/*    */   public ObjectIdInfo withAlwaysAsId(boolean state) {
/* 69 */     if (this._alwaysAsId == state) {
/* 70 */       return this;
/*    */     }
/* 72 */     return new ObjectIdInfo(this._propertyName, this._scope, this._generator, state, this._resolver);
/*    */   }
/*    */   
/* 75 */   public PropertyName getPropertyName() { return this._propertyName; }
/* 76 */   public Class<?> getScope() { return this._scope; }
/* 77 */   public Class<? extends ObjectIdGenerator<?>> getGeneratorType() { return this._generator; }
/* 78 */   public Class<? extends ObjectIdResolver> getResolverType() { return this._resolver; }
/* 79 */   public boolean getAlwaysAsId() { return this._alwaysAsId; }
/*    */   
/*    */   public String toString()
/*    */   {
/* 83 */     return "ObjectIdInfo: propName=" + this._propertyName + ", scope=" + (this._scope == null ? "null" : this._scope.getName()) + ", generatorType=" + (this._generator == null ? "null" : this._generator.getName()) + ", alwaysAsId=" + this._alwaysAsId;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\introspect\ObjectIdInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */