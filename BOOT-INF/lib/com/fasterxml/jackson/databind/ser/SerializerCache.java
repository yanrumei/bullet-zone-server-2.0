/*     */ package com.fasterxml.jackson.databind.ser;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.ser.impl.ReadOnlyClassToSerializerMap;
/*     */ import com.fasterxml.jackson.databind.util.TypeKey;
/*     */ import java.util.HashMap;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SerializerCache
/*     */ {
/*  33 */   private final HashMap<TypeKey, JsonSerializer<Object>> _sharedMap = new HashMap(64);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  39 */   private final AtomicReference<ReadOnlyClassToSerializerMap> _readOnlyMap = new AtomicReference();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ReadOnlyClassToSerializerMap getReadOnlyLookupMap()
/*     */   {
/*  50 */     ReadOnlyClassToSerializerMap m = (ReadOnlyClassToSerializerMap)this._readOnlyMap.get();
/*  51 */     if (m != null) {
/*  52 */       return m;
/*     */     }
/*  54 */     return _makeReadOnlyLookupMap();
/*     */   }
/*     */   
/*     */ 
/*     */   private final synchronized ReadOnlyClassToSerializerMap _makeReadOnlyLookupMap()
/*     */   {
/*  60 */     ReadOnlyClassToSerializerMap m = (ReadOnlyClassToSerializerMap)this._readOnlyMap.get();
/*  61 */     if (m == null) {
/*  62 */       m = ReadOnlyClassToSerializerMap.from(this._sharedMap);
/*  63 */       this._readOnlyMap.set(m);
/*     */     }
/*  65 */     return m;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized int size()
/*     */   {
/*  75 */     return this._sharedMap.size();
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public JsonSerializer<Object> untypedValueSerializer(Class<?> type)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: dup
/*     */     //   2: astore_2
/*     */     //   3: monitorenter
/*     */     //   4: aload_0
/*     */     //   5: getfield 4	com/fasterxml/jackson/databind/ser/SerializerCache:_sharedMap	Ljava/util/HashMap;
/*     */     //   8: new 14	com/fasterxml/jackson/databind/util/TypeKey
/*     */     //   11: dup
/*     */     //   12: aload_1
/*     */     //   13: iconst_0
/*     */     //   14: invokespecial 15	com/fasterxml/jackson/databind/util/TypeKey:<init>	(Ljava/lang/Class;Z)V
/*     */     //   17: invokevirtual 16	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   20: checkcast 17	com/fasterxml/jackson/databind/JsonSerializer
/*     */     //   23: aload_2
/*     */     //   24: monitorexit
/*     */     //   25: areturn
/*     */     //   26: astore_3
/*     */     //   27: aload_2
/*     */     //   28: monitorexit
/*     */     //   29: aload_3
/*     */     //   30: athrow
/*     */     // Line number table:
/*     */     //   Java source line #84	-> byte code offset #0
/*     */     //   Java source line #85	-> byte code offset #4
/*     */     //   Java source line #86	-> byte code offset #26
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	31	0	this	SerializerCache
/*     */     //   0	31	1	type	Class<?>
/*     */     //   2	26	2	Ljava/lang/Object;	Object
/*     */     //   26	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	25	26	finally
/*     */     //   26	29	26	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public JsonSerializer<Object> untypedValueSerializer(JavaType type)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: dup
/*     */     //   2: astore_2
/*     */     //   3: monitorenter
/*     */     //   4: aload_0
/*     */     //   5: getfield 4	com/fasterxml/jackson/databind/ser/SerializerCache:_sharedMap	Ljava/util/HashMap;
/*     */     //   8: new 14	com/fasterxml/jackson/databind/util/TypeKey
/*     */     //   11: dup
/*     */     //   12: aload_1
/*     */     //   13: iconst_0
/*     */     //   14: invokespecial 18	com/fasterxml/jackson/databind/util/TypeKey:<init>	(Lcom/fasterxml/jackson/databind/JavaType;Z)V
/*     */     //   17: invokevirtual 16	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   20: checkcast 17	com/fasterxml/jackson/databind/JsonSerializer
/*     */     //   23: aload_2
/*     */     //   24: monitorexit
/*     */     //   25: areturn
/*     */     //   26: astore_3
/*     */     //   27: aload_2
/*     */     //   28: monitorexit
/*     */     //   29: aload_3
/*     */     //   30: athrow
/*     */     // Line number table:
/*     */     //   Java source line #91	-> byte code offset #0
/*     */     //   Java source line #92	-> byte code offset #4
/*     */     //   Java source line #93	-> byte code offset #26
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	31	0	this	SerializerCache
/*     */     //   0	31	1	type	JavaType
/*     */     //   2	26	2	Ljava/lang/Object;	Object
/*     */     //   26	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	25	26	finally
/*     */     //   26	29	26	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public JsonSerializer<Object> typedValueSerializer(JavaType type)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: dup
/*     */     //   2: astore_2
/*     */     //   3: monitorenter
/*     */     //   4: aload_0
/*     */     //   5: getfield 4	com/fasterxml/jackson/databind/ser/SerializerCache:_sharedMap	Ljava/util/HashMap;
/*     */     //   8: new 14	com/fasterxml/jackson/databind/util/TypeKey
/*     */     //   11: dup
/*     */     //   12: aload_1
/*     */     //   13: iconst_1
/*     */     //   14: invokespecial 18	com/fasterxml/jackson/databind/util/TypeKey:<init>	(Lcom/fasterxml/jackson/databind/JavaType;Z)V
/*     */     //   17: invokevirtual 16	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   20: checkcast 17	com/fasterxml/jackson/databind/JsonSerializer
/*     */     //   23: aload_2
/*     */     //   24: monitorexit
/*     */     //   25: areturn
/*     */     //   26: astore_3
/*     */     //   27: aload_2
/*     */     //   28: monitorexit
/*     */     //   29: aload_3
/*     */     //   30: athrow
/*     */     // Line number table:
/*     */     //   Java source line #98	-> byte code offset #0
/*     */     //   Java source line #99	-> byte code offset #4
/*     */     //   Java source line #100	-> byte code offset #26
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	31	0	this	SerializerCache
/*     */     //   0	31	1	type	JavaType
/*     */     //   2	26	2	Ljava/lang/Object;	Object
/*     */     //   26	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	25	26	finally
/*     */     //   26	29	26	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public JsonSerializer<Object> typedValueSerializer(Class<?> cls)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: dup
/*     */     //   2: astore_2
/*     */     //   3: monitorenter
/*     */     //   4: aload_0
/*     */     //   5: getfield 4	com/fasterxml/jackson/databind/ser/SerializerCache:_sharedMap	Ljava/util/HashMap;
/*     */     //   8: new 14	com/fasterxml/jackson/databind/util/TypeKey
/*     */     //   11: dup
/*     */     //   12: aload_1
/*     */     //   13: iconst_1
/*     */     //   14: invokespecial 15	com/fasterxml/jackson/databind/util/TypeKey:<init>	(Ljava/lang/Class;Z)V
/*     */     //   17: invokevirtual 16	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   20: checkcast 17	com/fasterxml/jackson/databind/JsonSerializer
/*     */     //   23: aload_2
/*     */     //   24: monitorexit
/*     */     //   25: areturn
/*     */     //   26: astore_3
/*     */     //   27: aload_2
/*     */     //   28: monitorexit
/*     */     //   29: aload_3
/*     */     //   30: athrow
/*     */     // Line number table:
/*     */     //   Java source line #105	-> byte code offset #0
/*     */     //   Java source line #106	-> byte code offset #4
/*     */     //   Java source line #107	-> byte code offset #26
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	31	0	this	SerializerCache
/*     */     //   0	31	1	cls	Class<?>
/*     */     //   2	26	2	Ljava/lang/Object;	Object
/*     */     //   26	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	25	26	finally
/*     */     //   26	29	26	finally
/*     */   }
/*     */   
/*     */   public void addTypedSerializer(JavaType type, JsonSerializer<Object> ser)
/*     */   {
/* 123 */     synchronized (this) {
/* 124 */       if (this._sharedMap.put(new TypeKey(type, true), ser) == null)
/*     */       {
/* 126 */         this._readOnlyMap.set(null);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void addTypedSerializer(Class<?> cls, JsonSerializer<Object> ser)
/*     */   {
/* 133 */     synchronized (this) {
/* 134 */       if (this._sharedMap.put(new TypeKey(cls, true), ser) == null)
/*     */       {
/* 136 */         this._readOnlyMap.set(null);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void addAndResolveNonTypedSerializer(Class<?> type, JsonSerializer<Object> ser, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 145 */     synchronized (this) {
/* 146 */       if (this._sharedMap.put(new TypeKey(type, false), ser) == null) {
/* 147 */         this._readOnlyMap.set(null);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 154 */       if ((ser instanceof ResolvableSerializer)) {
/* 155 */         ((ResolvableSerializer)ser).resolve(provider);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void addAndResolveNonTypedSerializer(JavaType type, JsonSerializer<Object> ser, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 164 */     synchronized (this) {
/* 165 */       if (this._sharedMap.put(new TypeKey(type, false), ser) == null) {
/* 166 */         this._readOnlyMap.set(null);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 173 */       if ((ser instanceof ResolvableSerializer)) {
/* 174 */         ((ResolvableSerializer)ser).resolve(provider);
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
/*     */   public void addAndResolveNonTypedSerializer(Class<?> rawType, JavaType fullType, JsonSerializer<Object> ser, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 190 */     synchronized (this) {
/* 191 */       Object ob1 = this._sharedMap.put(new TypeKey(rawType, false), ser);
/* 192 */       Object ob2 = this._sharedMap.put(new TypeKey(fullType, false), ser);
/* 193 */       if ((ob1 == null) || (ob2 == null)) {
/* 194 */         this._readOnlyMap.set(null);
/*     */       }
/* 196 */       if ((ser instanceof ResolvableSerializer)) {
/* 197 */         ((ResolvableSerializer)ser).resolve(provider);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void flush()
/*     */   {
/* 207 */     this._sharedMap.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\SerializerCache.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */