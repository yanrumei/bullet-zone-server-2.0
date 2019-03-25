/*     */ package com.fasterxml.jackson.databind.jsontype.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
/*     */ import com.fasterxml.jackson.databind.DatabindContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.type.CollectionType;
/*     */ import com.fasterxml.jackson.databind.type.MapType;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.IOException;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ 
/*     */ public class ClassNameIdResolver
/*     */   extends TypeIdResolverBase
/*     */ {
/*     */   public ClassNameIdResolver(JavaType baseType, TypeFactory typeFactory)
/*     */   {
/*  20 */     super(baseType, typeFactory);
/*     */   }
/*     */   
/*     */   public JsonTypeInfo.Id getMechanism() {
/*  24 */     return JsonTypeInfo.Id.CLASS;
/*     */   }
/*     */   
/*     */ 
/*     */   public void registerSubtype(Class<?> type, String name) {}
/*     */   
/*     */   public String idFromValue(Object value)
/*     */   {
/*  32 */     return _idFrom(value, value.getClass(), this._typeFactory);
/*     */   }
/*     */   
/*     */   public String idFromValueAndType(Object value, Class<?> type)
/*     */   {
/*  37 */     return _idFrom(value, type, this._typeFactory);
/*     */   }
/*     */   
/*     */   public JavaType typeFromId(DatabindContext context, String id) throws IOException
/*     */   {
/*  42 */     return _typeFromId(id, context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JavaType _typeFromId(String id, DatabindContext ctxt)
/*     */     throws IOException
/*     */   {
/*  51 */     TypeFactory tf = ctxt.getTypeFactory();
/*  52 */     if (id.indexOf('<') > 0)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*  57 */       JavaType t = tf.constructFromCanonical(id);
/*  58 */       if (!t.isTypeOrSubTypeOf(this._baseType.getRawClass()))
/*     */       {
/*  60 */         throw new IllegalArgumentException(String.format("Class %s not subtype of %s", new Object[] { t.getRawClass().getName(), this._baseType }));
/*     */       }
/*     */       
/*  63 */       return t;
/*     */     }
/*     */     Class<?> cls;
/*     */     try {
/*  67 */       cls = tf.findClass(id);
/*     */     }
/*     */     catch (ClassNotFoundException e)
/*     */     {
/*  71 */       if ((ctxt instanceof DeserializationContext)) {
/*  72 */         DeserializationContext dctxt = (DeserializationContext)ctxt;
/*     */         
/*  74 */         return dctxt.handleUnknownTypeId(this._baseType, id, this, "no such class found");
/*     */       }
/*     */       
/*  77 */       return null;
/*     */     } catch (Exception e) {
/*  79 */       throw new IllegalArgumentException("Invalid type id '" + id + "' (for id type 'Id.class'): " + e.getMessage(), e);
/*     */     }
/*  81 */     return tf.constructSpecializedType(this._baseType, cls);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String _idFrom(Object value, Class<?> cls, TypeFactory typeFactory)
/*     */   {
/*  93 */     if ((Enum.class.isAssignableFrom(cls)) && 
/*  94 */       (!cls.isEnum())) {
/*  95 */       cls = cls.getSuperclass();
/*     */     }
/*     */     
/*  98 */     String str = cls.getName();
/*  99 */     if (str.startsWith("java.util"))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 106 */       if ((value instanceof EnumSet)) {
/* 107 */         Class<?> enumClass = ClassUtil.findEnumType((EnumSet)value);
/*     */         
/* 109 */         str = typeFactory.constructCollectionType(EnumSet.class, enumClass).toCanonical();
/* 110 */       } else if ((value instanceof EnumMap)) {
/* 111 */         Class<?> enumClass = ClassUtil.findEnumType((EnumMap)value);
/* 112 */         Class<?> valueClass = Object.class;
/*     */         
/* 114 */         str = typeFactory.constructMapType(EnumMap.class, enumClass, valueClass).toCanonical();
/*     */       } else {
/* 116 */         String end = str.substring(9);
/* 117 */         if (((end.startsWith(".Arrays$")) || (end.startsWith(".Collections$"))) && (str.indexOf("List") >= 0))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 125 */           str = "java.util.ArrayList";
/*     */         }
/*     */       }
/* 128 */     } else if (str.indexOf('$') >= 0)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 136 */       Class<?> outer = ClassUtil.getOuterClass(cls);
/* 137 */       if (outer != null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 142 */         Class<?> staticType = this._baseType.getRawClass();
/* 143 */         if (ClassUtil.getOuterClass(staticType) == null)
/*     */         {
/* 145 */           cls = this._baseType.getRawClass();
/* 146 */           str = cls.getName();
/*     */         }
/*     */       }
/*     */     }
/* 150 */     return str;
/*     */   }
/*     */   
/*     */   public String getDescForKnownTypeIds()
/*     */   {
/* 155 */     return "class name used as type id";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\jsontype\impl\ClassNameIdResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */