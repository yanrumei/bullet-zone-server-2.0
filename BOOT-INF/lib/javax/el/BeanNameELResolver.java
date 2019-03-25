/*     */ package javax.el;
/*     */ 
/*     */ import java.beans.FeatureDescriptor;
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
/*     */ 
/*     */ 
/*     */ public class BeanNameELResolver
/*     */   extends ELResolver
/*     */ {
/*     */   private final BeanNameResolver beanNameResolver;
/*     */   
/*     */   public BeanNameELResolver(BeanNameResolver beanNameResolver)
/*     */   {
/*  31 */     this.beanNameResolver = beanNameResolver;
/*     */   }
/*     */   
/*     */   public Object getValue(ELContext context, Object base, Object property)
/*     */   {
/*  36 */     Objects.requireNonNull(context);
/*  37 */     if ((base != null) || (!(property instanceof String))) {
/*  38 */       return null;
/*     */     }
/*     */     
/*  41 */     String beanName = (String)property;
/*     */     
/*  43 */     if (this.beanNameResolver.isNameResolved(beanName)) {
/*     */       try {
/*  45 */         Object result = this.beanNameResolver.getBean(beanName);
/*  46 */         context.setPropertyResolved(base, property);
/*  47 */         return result;
/*     */       } catch (Throwable t) {
/*  49 */         Util.handleThrowable(t);
/*  50 */         throw new ELException(t);
/*     */       }
/*     */     }
/*     */     
/*  54 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setValue(ELContext context, Object base, Object property, Object value)
/*     */   {
/*  60 */     Objects.requireNonNull(context);
/*  61 */     if ((base != null) || (!(property instanceof String))) {
/*  62 */       return;
/*     */     }
/*     */     
/*  65 */     String beanName = (String)property;
/*     */     
/*  67 */     boolean isResolved = context.isPropertyResolved();
/*     */     
/*     */     try
/*     */     {
/*  71 */       isReadOnly = isReadOnly(context, base, property);
/*     */     } catch (Throwable t) { boolean isReadOnly;
/*  73 */       Util.handleThrowable(t);
/*  74 */       throw new ELException(t);
/*     */     } finally {
/*  76 */       context.setPropertyResolved(isResolved);
/*     */     }
/*     */     boolean isReadOnly;
/*  79 */     if (isReadOnly) {
/*  80 */       throw new PropertyNotWritableException(Util.message(context, "beanNameELResolver.beanReadOnly", new Object[] { beanName }));
/*     */     }
/*     */     
/*     */ 
/*  84 */     if ((this.beanNameResolver.isNameResolved(beanName)) || 
/*  85 */       (this.beanNameResolver.canCreateBean(beanName))) {
/*     */       try {
/*  87 */         this.beanNameResolver.setBeanValue(beanName, value);
/*  88 */         context.setPropertyResolved(base, property);
/*     */       } catch (Throwable t) {
/*  90 */         Util.handleThrowable(t);
/*  91 */         throw new ELException(t);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Class<?> getType(ELContext context, Object base, Object property)
/*     */   {
/*  98 */     Objects.requireNonNull(context);
/*  99 */     if ((base != null) || (!(property instanceof String))) {
/* 100 */       return null;
/*     */     }
/*     */     
/* 103 */     String beanName = (String)property;
/*     */     try
/*     */     {
/* 106 */       if (this.beanNameResolver.isNameResolved(beanName)) {
/* 107 */         Class<?> result = this.beanNameResolver.getBean(beanName).getClass();
/* 108 */         context.setPropertyResolved(base, property);
/* 109 */         return result;
/*     */       }
/*     */     } catch (Throwable t) {
/* 112 */       Util.handleThrowable(t);
/* 113 */       throw new ELException(t);
/*     */     }
/*     */     
/* 116 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isReadOnly(ELContext context, Object base, Object property)
/*     */   {
/* 121 */     Objects.requireNonNull(context);
/* 122 */     if ((base != null) || (!(property instanceof String)))
/*     */     {
/* 124 */       return false;
/*     */     }
/*     */     
/* 127 */     String beanName = (String)property;
/*     */     
/* 129 */     if (this.beanNameResolver.isNameResolved(beanName))
/*     */     {
/*     */       try {
/* 132 */         result = this.beanNameResolver.isReadOnly(beanName);
/*     */       } catch (Throwable t) { boolean result;
/* 134 */         Util.handleThrowable(t);
/* 135 */         throw new ELException(t); }
/*     */       boolean result;
/* 137 */       context.setPropertyResolved(base, property);
/* 138 */       return result;
/*     */     }
/*     */     
/*     */ 
/* 142 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base)
/*     */   {
/* 148 */     return null;
/*     */   }
/*     */   
/*     */   public Class<?> getCommonPropertyType(ELContext context, Object base)
/*     */   {
/* 153 */     return String.class;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\BeanNameELResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */