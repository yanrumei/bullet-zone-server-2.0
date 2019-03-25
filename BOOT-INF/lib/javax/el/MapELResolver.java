/*     */ package javax.el;
/*     */ 
/*     */ import java.beans.FeatureDescriptor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
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
/*     */ public class MapELResolver
/*     */   extends ELResolver
/*     */ {
/*  32 */   private static final Class<?> UNMODIFIABLE = Collections.unmodifiableMap(new HashMap()).getClass();
/*     */   private final boolean readOnly;
/*     */   
/*     */   public MapELResolver()
/*     */   {
/*  37 */     this.readOnly = false;
/*     */   }
/*     */   
/*     */   public MapELResolver(boolean readOnly) {
/*  41 */     this.readOnly = readOnly;
/*     */   }
/*     */   
/*     */   public Class<?> getType(ELContext context, Object base, Object property)
/*     */   {
/*  46 */     Objects.requireNonNull(context);
/*     */     
/*  48 */     if ((base instanceof Map)) {
/*  49 */       context.setPropertyResolved(base, property);
/*  50 */       return Object.class;
/*     */     }
/*     */     
/*  53 */     return null;
/*     */   }
/*     */   
/*     */   public Object getValue(ELContext context, Object base, Object property)
/*     */   {
/*  58 */     Objects.requireNonNull(context);
/*     */     
/*  60 */     if ((base instanceof Map)) {
/*  61 */       context.setPropertyResolved(base, property);
/*  62 */       return ((Map)base).get(property);
/*     */     }
/*     */     
/*  65 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setValue(ELContext context, Object base, Object property, Object value)
/*     */   {
/*  71 */     Objects.requireNonNull(context);
/*     */     
/*  73 */     if ((base instanceof Map)) {
/*  74 */       context.setPropertyResolved(base, property);
/*     */       
/*  76 */       if (this.readOnly) {
/*  77 */         throw new PropertyNotWritableException(Util.message(context, "resolverNotWriteable", new Object[] {base
/*  78 */           .getClass().getName() }));
/*     */       }
/*     */       
/*     */       try
/*     */       {
/*  83 */         Map<Object, Object> map = (Map)base;
/*  84 */         map.put(property, value);
/*     */       } catch (UnsupportedOperationException e) {
/*  86 */         throw new PropertyNotWritableException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isReadOnly(ELContext context, Object base, Object property)
/*     */   {
/*  93 */     Objects.requireNonNull(context);
/*     */     
/*  95 */     if ((base instanceof Map)) {
/*  96 */       context.setPropertyResolved(base, property);
/*  97 */       return (this.readOnly) || (UNMODIFIABLE.equals(base.getClass()));
/*     */     }
/*     */     
/* 100 */     return this.readOnly;
/*     */   }
/*     */   
/*     */   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base)
/*     */   {
/* 105 */     if ((base instanceof Map)) {
/* 106 */       Iterator<?> itr = ((Map)base).keySet().iterator();
/* 107 */       List<FeatureDescriptor> feats = new ArrayList();
/*     */       
/*     */ 
/* 110 */       while (itr.hasNext()) {
/* 111 */         Object key = itr.next();
/* 112 */         FeatureDescriptor desc = new FeatureDescriptor();
/* 113 */         desc.setDisplayName(key.toString());
/* 114 */         desc.setShortDescription("");
/* 115 */         desc.setExpert(false);
/* 116 */         desc.setHidden(false);
/* 117 */         desc.setName(key.toString());
/* 118 */         desc.setPreferred(true);
/* 119 */         desc.setValue("resolvableAtDesignTime", Boolean.TRUE);
/* 120 */         desc.setValue("type", key.getClass());
/* 121 */         feats.add(desc);
/*     */       }
/* 123 */       return feats.iterator();
/*     */     }
/* 125 */     return null;
/*     */   }
/*     */   
/*     */   public Class<?> getCommonPropertyType(ELContext context, Object base)
/*     */   {
/* 130 */     if ((base instanceof Map)) {
/* 131 */       return Object.class;
/*     */     }
/* 133 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\MapELResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */