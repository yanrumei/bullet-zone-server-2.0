/*     */ package com.fasterxml.jackson.databind.type;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CollectionType
/*     */   extends CollectionLikeType
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   private CollectionType(Class<?> collT, TypeBindings bindings, JavaType superClass, JavaType[] superInts, JavaType elemT, Object valueHandler, Object typeHandler, boolean asStatic)
/*     */   {
/*  25 */     super(collT, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected CollectionType(TypeBase base, JavaType elemT)
/*     */   {
/*  32 */     super(base, elemT);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static CollectionType construct(Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInts, JavaType elemT)
/*     */   {
/*  40 */     return new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static CollectionType construct(Class<?> rawType, JavaType elemT)
/*     */   {
/*  51 */     TypeVariable<?>[] vars = rawType.getTypeParameters();
/*     */     TypeBindings bindings;
/*  53 */     TypeBindings bindings; if ((vars == null) || (vars.length != 1)) {
/*  54 */       bindings = TypeBindings.emptyBindings();
/*     */     } else {
/*  56 */       bindings = TypeBindings.create(rawType, elemT);
/*     */     }
/*  58 */     return new CollectionType(rawType, bindings, _bogusSuperClass(rawType), null, elemT, null, null, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected JavaType _narrow(Class<?> subclass)
/*     */   {
/*  67 */     return new CollectionType(subclass, this._bindings, this._superClass, this._superInterfaces, this._elementType, null, null, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */   public JavaType withContentType(JavaType contentType)
/*     */   {
/*  73 */     if (this._elementType == contentType) {
/*  74 */       return this;
/*     */     }
/*  76 */     return new CollectionType(this._class, this._bindings, this._superClass, this._superInterfaces, contentType, this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */   public CollectionType withTypeHandler(Object h)
/*     */   {
/*  82 */     return new CollectionType(this._class, this._bindings, this._superClass, this._superInterfaces, this._elementType, this._valueHandler, h, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public CollectionType withContentTypeHandler(Object h)
/*     */   {
/*  89 */     return new CollectionType(this._class, this._bindings, this._superClass, this._superInterfaces, this._elementType.withTypeHandler(h), this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public CollectionType withValueHandler(Object h)
/*     */   {
/*  96 */     return new CollectionType(this._class, this._bindings, this._superClass, this._superInterfaces, this._elementType, h, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */   public CollectionType withContentValueHandler(Object h)
/*     */   {
/* 102 */     return new CollectionType(this._class, this._bindings, this._superClass, this._superInterfaces, this._elementType.withValueHandler(h), this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public CollectionType withStaticTyping()
/*     */   {
/* 109 */     if (this._asStatic) {
/* 110 */       return this;
/*     */     }
/* 112 */     return new CollectionType(this._class, this._bindings, this._superClass, this._superInterfaces, this._elementType.withStaticTyping(), this._valueHandler, this._typeHandler, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType refine(Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces)
/*     */   {
/* 120 */     return new CollectionType(rawType, bindings, superClass, superInterfaces, this._elementType, this._valueHandler, this._typeHandler, this._asStatic);
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
/*     */   public String toString()
/*     */   {
/* 134 */     return "[collection type; class " + this._class.getName() + ", contains " + this._elementType + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\type\CollectionType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */