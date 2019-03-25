/*     */ package com.fasterxml.jackson.databind.type;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonSerializable;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ public abstract class TypeBase extends JavaType implements JsonSerializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  17 */   private static final TypeBindings NO_BINDINGS = ;
/*  18 */   private static final JavaType[] NO_TYPES = new JavaType[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JavaType _superClass;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JavaType[] _superInterfaces;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final TypeBindings _bindings;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   volatile transient String _canonicalName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TypeBase(Class<?> raw, TypeBindings bindings, JavaType superClass, JavaType[] superInts, int hash, Object valueHandler, Object typeHandler, boolean asStatic)
/*     */   {
/*  45 */     super(raw, hash, valueHandler, typeHandler, asStatic);
/*  46 */     this._bindings = (bindings == null ? NO_BINDINGS : bindings);
/*  47 */     this._superClass = superClass;
/*  48 */     this._superInterfaces = superInts;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TypeBase(TypeBase base)
/*     */   {
/*  57 */     super(base);
/*  58 */     this._superClass = base._superClass;
/*  59 */     this._superInterfaces = base._superInterfaces;
/*  60 */     this._bindings = base._bindings;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toCanonical()
/*     */   {
/*  66 */     String str = this._canonicalName;
/*  67 */     if (str == null) {
/*  68 */       str = buildCanonicalName();
/*     */     }
/*  70 */     return str;
/*     */   }
/*     */   
/*     */   protected String buildCanonicalName() {
/*  74 */     return this._class.getName();
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract StringBuilder getGenericSignature(StringBuilder paramStringBuilder);
/*     */   
/*     */ 
/*     */   public abstract StringBuilder getErasedSignature(StringBuilder paramStringBuilder);
/*     */   
/*     */   public TypeBindings getBindings()
/*     */   {
/*  85 */     return this._bindings;
/*     */   }
/*     */   
/*     */   public int containedTypeCount()
/*     */   {
/*  90 */     return this._bindings.size();
/*     */   }
/*     */   
/*     */   public JavaType containedType(int index)
/*     */   {
/*  95 */     return this._bindings.getBoundType(index);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public String containedTypeName(int index)
/*     */   {
/* 101 */     return this._bindings.getBoundName(index);
/*     */   }
/*     */   
/*     */   public JavaType getSuperClass()
/*     */   {
/* 106 */     return this._superClass;
/*     */   }
/*     */   
/*     */   public List<JavaType> getInterfaces()
/*     */   {
/* 111 */     if (this._superInterfaces == null) {
/* 112 */       return Collections.emptyList();
/*     */     }
/* 114 */     switch (this._superInterfaces.length) {
/*     */     case 0: 
/* 116 */       return Collections.emptyList();
/*     */     case 1: 
/* 118 */       return Collections.singletonList(this._superInterfaces[0]);
/*     */     }
/* 120 */     return Arrays.asList(this._superInterfaces);
/*     */   }
/*     */   
/*     */ 
/*     */   public final JavaType findSuperType(Class<?> rawTarget)
/*     */   {
/* 126 */     if (rawTarget == this._class) {
/* 127 */       return this;
/*     */     }
/*     */     
/* 130 */     if ((rawTarget.isInterface()) && (this._superInterfaces != null)) {
/* 131 */       int i = 0; for (int count = this._superInterfaces.length; i < count; i++) {
/* 132 */         JavaType type = this._superInterfaces[i].findSuperType(rawTarget);
/* 133 */         if (type != null) {
/* 134 */           return type;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 139 */     if (this._superClass != null) {
/* 140 */       JavaType type = this._superClass.findSuperType(rawTarget);
/* 141 */       if (type != null) {
/* 142 */         return type;
/*     */       }
/*     */     }
/* 145 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public JavaType[] findTypeParameters(Class<?> expType)
/*     */   {
/* 151 */     JavaType match = findSuperType(expType);
/* 152 */     if (match == null) {
/* 153 */       return NO_TYPES;
/*     */     }
/* 155 */     return match.getBindings().typeParameterArray();
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
/*     */   public void serializeWithType(JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 169 */     typeSer.writeTypePrefixForScalar(this, gen);
/* 170 */     serialize(gen, provider);
/* 171 */     typeSer.writeTypeSuffixForScalar(this, gen);
/*     */   }
/*     */   
/*     */ 
/*     */   public void serialize(JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 178 */     gen.writeString(toCanonical());
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
/*     */   protected static StringBuilder _classSignature(Class<?> cls, StringBuilder sb, boolean trailingSemicolon)
/*     */   {
/* 194 */     if (cls.isPrimitive()) {
/* 195 */       if (cls == Boolean.TYPE) {
/* 196 */         sb.append('Z');
/* 197 */       } else if (cls == Byte.TYPE) {
/* 198 */         sb.append('B');
/*     */       }
/* 200 */       else if (cls == Short.TYPE) {
/* 201 */         sb.append('S');
/*     */       }
/* 203 */       else if (cls == Character.TYPE) {
/* 204 */         sb.append('C');
/*     */       }
/* 206 */       else if (cls == Integer.TYPE) {
/* 207 */         sb.append('I');
/*     */       }
/* 209 */       else if (cls == Long.TYPE) {
/* 210 */         sb.append('J');
/*     */       }
/* 212 */       else if (cls == Float.TYPE) {
/* 213 */         sb.append('F');
/*     */       }
/* 215 */       else if (cls == Double.TYPE) {
/* 216 */         sb.append('D');
/*     */       }
/* 218 */       else if (cls == Void.TYPE) {
/* 219 */         sb.append('V');
/*     */       } else {
/* 221 */         throw new IllegalStateException("Unrecognized primitive type: " + cls.getName());
/*     */       }
/*     */     } else {
/* 224 */       sb.append('L');
/* 225 */       String name = cls.getName();
/* 226 */       int i = 0; for (int len = name.length(); i < len; i++) {
/* 227 */         char c = name.charAt(i);
/* 228 */         if (c == '.') c = '/';
/* 229 */         sb.append(c);
/*     */       }
/* 231 */       if (trailingSemicolon) {
/* 232 */         sb.append(';');
/*     */       }
/*     */     }
/* 235 */     return sb;
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
/*     */   protected static JavaType _bogusSuperClass(Class<?> cls)
/*     */   {
/* 248 */     Class<?> parent = cls.getSuperclass();
/* 249 */     if (parent == null) {
/* 250 */       return null;
/*     */     }
/* 252 */     return TypeFactory.unknownType();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\type\TypeBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */