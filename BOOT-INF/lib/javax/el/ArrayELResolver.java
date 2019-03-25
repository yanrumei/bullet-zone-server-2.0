/*     */ package javax.el;
/*     */ 
/*     */ import java.beans.FeatureDescriptor;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Iterator;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArrayELResolver
/*     */   extends ELResolver
/*     */ {
/*     */   private final boolean readOnly;
/*     */   
/*     */   public ArrayELResolver()
/*     */   {
/*  30 */     this.readOnly = false;
/*     */   }
/*     */   
/*     */   public ArrayELResolver(boolean readOnly) {
/*  34 */     this.readOnly = readOnly;
/*     */   }
/*     */   
/*     */   public Class<?> getType(ELContext context, Object base, Object property)
/*     */   {
/*  39 */     Objects.requireNonNull(context);
/*     */     
/*  41 */     if ((base != null) && (base.getClass().isArray())) {
/*  42 */       context.setPropertyResolved(base, property);
/*     */       try {
/*  44 */         int idx = coerce(property);
/*  45 */         checkBounds(base, idx);
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException) {}
/*     */       
/*  49 */       return base.getClass().getComponentType();
/*     */     }
/*     */     
/*  52 */     return null;
/*     */   }
/*     */   
/*     */   public Object getValue(ELContext context, Object base, Object property)
/*     */   {
/*  57 */     Objects.requireNonNull(context);
/*     */     
/*  59 */     if ((base != null) && (base.getClass().isArray())) {
/*  60 */       context.setPropertyResolved(base, property);
/*  61 */       int idx = coerce(property);
/*  62 */       if ((idx < 0) || (idx >= Array.getLength(base))) {
/*  63 */         return null;
/*     */       }
/*  65 */       return Array.get(base, idx);
/*     */     }
/*     */     
/*  68 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setValue(ELContext context, Object base, Object property, Object value)
/*     */   {
/*  74 */     Objects.requireNonNull(context);
/*     */     
/*  76 */     if ((base != null) && (base.getClass().isArray())) {
/*  77 */       context.setPropertyResolved(base, property);
/*     */       
/*  79 */       if (this.readOnly) {
/*  80 */         throw new PropertyNotWritableException(Util.message(context, "resolverNotWriteable", new Object[] {base
/*  81 */           .getClass().getName() }));
/*     */       }
/*     */       
/*  84 */       int idx = coerce(property);
/*  85 */       checkBounds(base, idx);
/*  86 */       if ((value != null) && (!Util.isAssignableFrom(value.getClass(), base
/*  87 */         .getClass().getComponentType()))) {
/*  88 */         throw new ClassCastException(Util.message(context, "objectNotAssignable", new Object[] {value
/*  89 */           .getClass().getName(), base
/*  90 */           .getClass().getComponentType().getName() }));
/*     */       }
/*  92 */       Array.set(base, idx, value);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isReadOnly(ELContext context, Object base, Object property)
/*     */   {
/*  98 */     Objects.requireNonNull(context);
/*     */     
/* 100 */     if ((base != null) && (base.getClass().isArray())) {
/* 101 */       context.setPropertyResolved(base, property);
/*     */       try {
/* 103 */         int idx = coerce(property);
/* 104 */         checkBounds(base, idx);
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException) {}
/*     */     }
/*     */     
/*     */ 
/* 110 */     return this.readOnly;
/*     */   }
/*     */   
/*     */   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base)
/*     */   {
/* 115 */     return null;
/*     */   }
/*     */   
/*     */   public Class<?> getCommonPropertyType(ELContext context, Object base)
/*     */   {
/* 120 */     if ((base != null) && (base.getClass().isArray())) {
/* 121 */       return Integer.class;
/*     */     }
/* 123 */     return null;
/*     */   }
/*     */   
/*     */   private static final void checkBounds(Object base, int idx) {
/* 127 */     if ((idx < 0) || (idx >= Array.getLength(base)))
/*     */     {
/* 129 */       throw new PropertyNotFoundException(new ArrayIndexOutOfBoundsException(idx).getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   private static final int coerce(Object property) {
/* 134 */     if ((property instanceof Number)) {
/* 135 */       return ((Number)property).intValue();
/*     */     }
/* 137 */     if ((property instanceof Character)) {
/* 138 */       return ((Character)property).charValue();
/*     */     }
/* 140 */     if ((property instanceof Boolean)) {
/* 141 */       return ((Boolean)property).booleanValue() ? 1 : 0;
/*     */     }
/* 143 */     if ((property instanceof String)) {
/* 144 */       return Integer.parseInt((String)property);
/*     */     }
/*     */     
/* 147 */     throw new IllegalArgumentException(property != null ? property.toString() : "null");
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\ArrayELResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */