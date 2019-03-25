/*     */ package org.apache.el.stream;
/*     */ 
/*     */ import java.beans.FeatureDescriptor;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import javax.el.ELContext;
/*     */ import javax.el.ELResolver;
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
/*     */ public class StreamELResolverImpl
/*     */   extends ELResolver
/*     */ {
/*     */   public Object getValue(ELContext context, Object base, Object property)
/*     */   {
/*  31 */     return null;
/*     */   }
/*     */   
/*     */   public Class<?> getType(ELContext context, Object base, Object property)
/*     */   {
/*  36 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setValue(ELContext context, Object base, Object property, Object value) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isReadOnly(ELContext context, Object base, Object property)
/*     */   {
/*  47 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base)
/*     */   {
/*  53 */     return null;
/*     */   }
/*     */   
/*     */   public Class<?> getCommonPropertyType(ELContext context, Object base)
/*     */   {
/*  58 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object invoke(ELContext context, Object base, Object method, Class<?>[] paramTypes, Object[] params)
/*     */   {
/*  65 */     if (("stream".equals(method)) && (params.length == 0)) {
/*  66 */       if (base.getClass().isArray()) {
/*  67 */         context.setPropertyResolved(true);
/*  68 */         return new Stream(new ArrayIterator(base)); }
/*  69 */       if ((base instanceof Collection)) {
/*  70 */         context.setPropertyResolved(true);
/*     */         
/*  72 */         Collection<Object> collection = (Collection)base;
/*  73 */         return new Stream(collection.iterator());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  78 */     return null;
/*     */   }
/*     */   
/*     */   private static class ArrayIterator
/*     */     implements Iterator<Object>
/*     */   {
/*     */     private final Object base;
/*     */     private final int size;
/*  86 */     private int index = 0;
/*     */     
/*     */     public ArrayIterator(Object base) {
/*  89 */       this.base = base;
/*  90 */       this.size = Array.getLength(base);
/*     */     }
/*     */     
/*     */     public boolean hasNext()
/*     */     {
/*  95 */       return this.size > this.index;
/*     */     }
/*     */     
/*     */     public Object next()
/*     */     {
/* 100 */       return Array.get(this.base, this.index++);
/*     */     }
/*     */     
/*     */     public void remove()
/*     */     {
/* 105 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\stream\StreamELResolverImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */