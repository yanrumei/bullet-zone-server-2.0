/*     */ package javax.el;
/*     */ 
/*     */ import java.beans.FeatureDescriptor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ListELResolver
/*     */   extends ELResolver
/*     */ {
/*     */   private final boolean readOnly;
/*  32 */   private static final Class<?> UNMODIFIABLE = Collections.unmodifiableList(new ArrayList()).getClass();
/*     */   
/*     */   public ListELResolver() {
/*  35 */     this.readOnly = false;
/*     */   }
/*     */   
/*     */   public ListELResolver(boolean readOnly) {
/*  39 */     this.readOnly = readOnly;
/*     */   }
/*     */   
/*     */   public Class<?> getType(ELContext context, Object base, Object property)
/*     */   {
/*  44 */     Objects.requireNonNull(context);
/*     */     
/*  46 */     if ((base instanceof List)) {
/*  47 */       context.setPropertyResolved(base, property);
/*  48 */       List<?> list = (List)base;
/*  49 */       int idx = coerce(property);
/*  50 */       if ((idx < 0) || (idx >= list.size()))
/*     */       {
/*  52 */         throw new PropertyNotFoundException(new ArrayIndexOutOfBoundsException(idx).getMessage());
/*     */       }
/*  54 */       return Object.class;
/*     */     }
/*     */     
/*  57 */     return null;
/*     */   }
/*     */   
/*     */   public Object getValue(ELContext context, Object base, Object property)
/*     */   {
/*  62 */     Objects.requireNonNull(context);
/*     */     
/*  64 */     if ((base instanceof List)) {
/*  65 */       context.setPropertyResolved(base, property);
/*  66 */       List<?> list = (List)base;
/*  67 */       int idx = coerce(property);
/*  68 */       if ((idx < 0) || (idx >= list.size())) {
/*  69 */         return null;
/*     */       }
/*  71 */       return list.get(idx);
/*     */     }
/*     */     
/*  74 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setValue(ELContext context, Object base, Object property, Object value)
/*     */   {
/*  80 */     Objects.requireNonNull(context);
/*     */     
/*  82 */     if ((base instanceof List)) {
/*  83 */       context.setPropertyResolved(base, property);
/*     */       
/*  85 */       List<Object> list = (List)base;
/*     */       
/*  87 */       if (this.readOnly) {
/*  88 */         throw new PropertyNotWritableException(Util.message(context, "resolverNotWriteable", new Object[] {base
/*  89 */           .getClass().getName() }));
/*     */       }
/*     */       
/*  92 */       int idx = coerce(property);
/*     */       try {
/*  94 */         list.set(idx, value);
/*     */       } catch (UnsupportedOperationException e) {
/*  96 */         throw new PropertyNotWritableException(e);
/*     */       } catch (IndexOutOfBoundsException e) {
/*  98 */         throw new PropertyNotFoundException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isReadOnly(ELContext context, Object base, Object property)
/*     */   {
/* 105 */     Objects.requireNonNull(context);
/*     */     
/* 107 */     if ((base instanceof List)) {
/* 108 */       context.setPropertyResolved(base, property);
/* 109 */       List<?> list = (List)base;
/*     */       try {
/* 111 */         int idx = coerce(property);
/* 112 */         if ((idx < 0) || (idx >= list.size()))
/*     */         {
/*     */ 
/* 115 */           throw new PropertyNotFoundException(new ArrayIndexOutOfBoundsException(idx).getMessage());
/*     */         }
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException) {}
/*     */       
/* 120 */       return (this.readOnly) || (UNMODIFIABLE.equals(list.getClass()));
/*     */     }
/*     */     
/* 123 */     return this.readOnly;
/*     */   }
/*     */   
/*     */   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base)
/*     */   {
/* 128 */     return null;
/*     */   }
/*     */   
/*     */   public Class<?> getCommonPropertyType(ELContext context, Object base)
/*     */   {
/* 133 */     if ((base instanceof List)) {
/* 134 */       return Integer.class;
/*     */     }
/* 136 */     return null;
/*     */   }
/*     */   
/*     */   private static final int coerce(Object property) {
/* 140 */     if ((property instanceof Number)) {
/* 141 */       return ((Number)property).intValue();
/*     */     }
/* 143 */     if ((property instanceof Character)) {
/* 144 */       return ((Character)property).charValue();
/*     */     }
/* 146 */     if ((property instanceof Boolean)) {
/* 147 */       return ((Boolean)property).booleanValue() ? 1 : 0;
/*     */     }
/* 149 */     if ((property instanceof String)) {
/* 150 */       return Integer.parseInt((String)property);
/*     */     }
/*     */     
/* 153 */     throw new IllegalArgumentException(property != null ? property.toString() : "null");
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\ListELResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */