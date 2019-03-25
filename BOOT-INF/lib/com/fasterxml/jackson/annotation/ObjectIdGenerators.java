/*     */ package com.fasterxml.jackson.annotation;
/*     */ 
/*     */ import java.util.UUID;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ObjectIdGenerators
/*     */ {
/*     */   private static abstract class Base<T>
/*     */     extends ObjectIdGenerator<T>
/*     */   {
/*     */     protected final Class<?> _scope;
/*     */     
/*     */     protected Base(Class<?> scope)
/*     */     {
/*  25 */       this._scope = scope;
/*     */     }
/*     */     
/*     */     public final Class<?> getScope()
/*     */     {
/*  30 */       return this._scope;
/*     */     }
/*     */     
/*     */     public boolean canUseFor(ObjectIdGenerator<?> gen)
/*     */     {
/*  35 */       return (gen.getClass() == getClass()) && (gen.getScope() == this._scope);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract T generateId(Object paramObject);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static abstract class None
/*     */     extends ObjectIdGenerator<Object>
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static abstract class PropertyGenerator
/*     */     extends ObjectIdGenerators.Base<Object>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected PropertyGenerator(Class<?> scope)
/*     */     {
/*  68 */       super();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static final class IntSequenceGenerator
/*     */     extends ObjectIdGenerators.Base<Integer>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected transient int _nextValue;
/*     */     
/*  81 */     public IntSequenceGenerator() { this(Object.class, -1); }
/*     */     
/*  83 */     public IntSequenceGenerator(Class<?> scope, int fv) { super();
/*  84 */       this._nextValue = fv;
/*     */     }
/*     */     
/*  87 */     protected int initialValue() { return 1; }
/*     */     
/*     */     public ObjectIdGenerator<Integer> forScope(Class<?> scope)
/*     */     {
/*  91 */       return this._scope == scope ? this : new IntSequenceGenerator(scope, this._nextValue);
/*     */     }
/*     */     
/*     */     public ObjectIdGenerator<Integer> newForSerialization(Object context)
/*     */     {
/*  96 */       return new IntSequenceGenerator(this._scope, initialValue());
/*     */     }
/*     */     
/*     */ 
/*     */     public ObjectIdGenerator.IdKey key(Object key)
/*     */     {
/* 102 */       if (key == null) {
/* 103 */         return null;
/*     */       }
/* 105 */       return new ObjectIdGenerator.IdKey(getClass(), this._scope, key);
/*     */     }
/*     */     
/*     */ 
/*     */     public Integer generateId(Object forPojo)
/*     */     {
/* 111 */       if (forPojo == null) {
/* 112 */         return null;
/*     */       }
/* 114 */       int id = this._nextValue;
/* 115 */       this._nextValue += 1;
/* 116 */       return Integer.valueOf(id);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final class UUIDGenerator
/*     */     extends ObjectIdGenerators.Base<UUID>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 133 */     public UUIDGenerator() { this(Object.class); }
/*     */     
/* 135 */     private UUIDGenerator(Class<?> scope) { super(); }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ObjectIdGenerator<UUID> forScope(Class<?> scope)
/*     */     {
/* 143 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public ObjectIdGenerator<UUID> newForSerialization(Object context)
/*     */     {
/* 151 */       return this;
/*     */     }
/*     */     
/*     */     public UUID generateId(Object forPojo)
/*     */     {
/* 156 */       return UUID.randomUUID();
/*     */     }
/*     */     
/*     */ 
/*     */     public ObjectIdGenerator.IdKey key(Object key)
/*     */     {
/* 162 */       if (key == null) {
/* 163 */         return null;
/*     */       }
/* 165 */       return new ObjectIdGenerator.IdKey(getClass(), null, key);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean canUseFor(ObjectIdGenerator<?> gen)
/*     */     {
/* 173 */       return gen.getClass() == getClass();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final class StringIdGenerator
/*     */     extends ObjectIdGenerators.Base<String>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 194 */     public StringIdGenerator() { this(Object.class); }
/*     */     
/* 196 */     private StringIdGenerator(Class<?> scope) { super(); }
/*     */     
/*     */ 
/*     */ 
/*     */     public ObjectIdGenerator<String> forScope(Class<?> scope)
/*     */     {
/* 202 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */     public ObjectIdGenerator<String> newForSerialization(Object context)
/*     */     {
/* 208 */       return this;
/*     */     }
/*     */     
/*     */     public String generateId(Object forPojo)
/*     */     {
/* 213 */       return UUID.randomUUID().toString();
/*     */     }
/*     */     
/*     */     public ObjectIdGenerator.IdKey key(Object key)
/*     */     {
/* 218 */       if (key == null) {
/* 219 */         return null;
/*     */       }
/* 221 */       return new ObjectIdGenerator.IdKey(getClass(), null, key);
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean canUseFor(ObjectIdGenerator<?> gen)
/*     */     {
/* 227 */       return gen instanceof StringIdGenerator;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-annotations-2.8.0.jar!\com\fasterxml\jackson\annotation\ObjectIdGenerators.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */