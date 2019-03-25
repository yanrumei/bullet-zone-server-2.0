/*     */ package javax.el;
/*     */ 
/*     */ import java.beans.FeatureDescriptor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.Objects;
/*     */ import java.util.ResourceBundle;
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
/*     */ public class ResourceBundleELResolver
/*     */   extends ELResolver
/*     */ {
/*     */   public Object getValue(ELContext context, Object base, Object property)
/*     */   {
/*  37 */     Objects.requireNonNull(context);
/*     */     
/*  39 */     if ((base instanceof ResourceBundle)) {
/*  40 */       context.setPropertyResolved(base, property);
/*     */       
/*  42 */       if (property != null) {
/*     */         try {
/*  44 */           return ((ResourceBundle)base).getObject(property
/*  45 */             .toString());
/*     */         } catch (MissingResourceException mre) {
/*  47 */           return "???" + property.toString() + "???";
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  52 */     return null;
/*     */   }
/*     */   
/*     */   public Class<?> getType(ELContext context, Object base, Object property)
/*     */   {
/*  57 */     Objects.requireNonNull(context);
/*     */     
/*  59 */     if ((base instanceof ResourceBundle)) {
/*  60 */       context.setPropertyResolved(base, property);
/*     */     }
/*     */     
/*  63 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setValue(ELContext context, Object base, Object property, Object value)
/*     */   {
/*  69 */     Objects.requireNonNull(context);
/*     */     
/*  71 */     if ((base instanceof ResourceBundle)) {
/*  72 */       context.setPropertyResolved(base, property);
/*  73 */       throw new PropertyNotWritableException(Util.message(context, "resolverNotWriteable", new Object[] {base
/*  74 */         .getClass().getName() }));
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isReadOnly(ELContext context, Object base, Object property)
/*     */   {
/*  80 */     Objects.requireNonNull(context);
/*     */     
/*  82 */     if ((base instanceof ResourceBundle)) {
/*  83 */       context.setPropertyResolved(base, property);
/*  84 */       return true;
/*     */     }
/*     */     
/*  87 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base)
/*     */   {
/*  93 */     if ((base instanceof ResourceBundle)) {
/*  94 */       List<FeatureDescriptor> feats = new ArrayList();
/*  95 */       Enumeration<String> e = ((ResourceBundle)base).getKeys();
/*     */       
/*     */ 
/*  98 */       while (e.hasMoreElements()) {
/*  99 */         String key = (String)e.nextElement();
/* 100 */         FeatureDescriptor feat = new FeatureDescriptor();
/* 101 */         feat.setDisplayName(key);
/* 102 */         feat.setShortDescription("");
/* 103 */         feat.setExpert(false);
/* 104 */         feat.setHidden(false);
/* 105 */         feat.setName(key);
/* 106 */         feat.setPreferred(true);
/* 107 */         feat.setValue("resolvableAtDesignTime", Boolean.TRUE);
/* 108 */         feat.setValue("type", String.class);
/* 109 */         feats.add(feat);
/*     */       }
/* 111 */       return feats.iterator();
/*     */     }
/* 113 */     return null;
/*     */   }
/*     */   
/*     */   public Class<?> getCommonPropertyType(ELContext context, Object base)
/*     */   {
/* 118 */     if ((base instanceof ResourceBundle)) {
/* 119 */       return String.class;
/*     */     }
/* 121 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\ResourceBundleELResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */