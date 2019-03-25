/*     */ package javax.el;
/*     */ 
/*     */ import java.beans.FeatureDescriptor;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public class CompositeELResolver
/*     */   extends ELResolver
/*     */ {
/*     */   private static final Class<?> SCOPED_ATTRIBUTE_EL_RESOLVER;
/*     */   private int size;
/*     */   private ELResolver[] resolvers;
/*     */   
/*     */   static
/*     */   {
/*  28 */     Class<?> clazz = null;
/*     */     try {
/*  30 */       clazz = Class.forName("javax.servlet.jsp.el.ScopedAttributeELResolver");
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {}
/*     */     
/*  34 */     SCOPED_ATTRIBUTE_EL_RESOLVER = clazz;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CompositeELResolver()
/*     */   {
/*  42 */     this.size = 0;
/*  43 */     this.resolvers = new ELResolver[8];
/*     */   }
/*     */   
/*     */   public void add(ELResolver elResolver) {
/*  47 */     Objects.requireNonNull(elResolver);
/*     */     
/*  49 */     if (this.size >= this.resolvers.length) {
/*  50 */       ELResolver[] nr = new ELResolver[this.size * 2];
/*  51 */       System.arraycopy(this.resolvers, 0, nr, 0, this.size);
/*  52 */       this.resolvers = nr;
/*     */     }
/*  54 */     this.resolvers[(this.size++)] = elResolver;
/*     */   }
/*     */   
/*     */   public Object getValue(ELContext context, Object base, Object property)
/*     */   {
/*  59 */     context.setPropertyResolved(false);
/*  60 */     int sz = this.size;
/*  61 */     for (int i = 0; i < sz; i++) {
/*  62 */       Object result = this.resolvers[i].getValue(context, base, property);
/*  63 */       if (context.isPropertyResolved()) {
/*  64 */         return result;
/*     */       }
/*     */     }
/*  67 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object invoke(ELContext context, Object base, Object method, Class<?>[] paramTypes, Object[] params)
/*     */   {
/*  76 */     context.setPropertyResolved(false);
/*  77 */     int sz = this.size;
/*  78 */     for (int i = 0; i < sz; i++) {
/*  79 */       Object obj = this.resolvers[i].invoke(context, base, method, paramTypes, params);
/*  80 */       if (context.isPropertyResolved()) {
/*  81 */         return obj;
/*     */       }
/*     */     }
/*  84 */     return null;
/*     */   }
/*     */   
/*     */   public Class<?> getType(ELContext context, Object base, Object property)
/*     */   {
/*  89 */     context.setPropertyResolved(false);
/*  90 */     int sz = this.size;
/*  91 */     for (int i = 0; i < sz; i++) {
/*  92 */       Class<?> type = this.resolvers[i].getType(context, base, property);
/*  93 */       if (context.isPropertyResolved()) {
/*  94 */         if ((SCOPED_ATTRIBUTE_EL_RESOLVER != null) && 
/*  95 */           (SCOPED_ATTRIBUTE_EL_RESOLVER.isAssignableFrom(this.resolvers[i].getClass())))
/*     */         {
/*     */ 
/*     */ 
/*  99 */           Object value = this.resolvers[i].getValue(context, base, property);
/* 100 */           if (value != null) {
/* 101 */             return value.getClass();
/*     */           }
/*     */         }
/* 104 */         return type;
/*     */       }
/*     */     }
/* 107 */     return null;
/*     */   }
/*     */   
/*     */   public void setValue(ELContext context, Object base, Object property, Object value)
/*     */   {
/* 112 */     context.setPropertyResolved(false);
/* 113 */     int sz = this.size;
/* 114 */     for (int i = 0; i < sz; i++) {
/* 115 */       this.resolvers[i].setValue(context, base, property, value);
/* 116 */       if (context.isPropertyResolved()) {
/* 117 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isReadOnly(ELContext context, Object base, Object property)
/*     */   {
/* 124 */     context.setPropertyResolved(false);
/* 125 */     int sz = this.size;
/* 126 */     for (int i = 0; i < sz; i++) {
/* 127 */       boolean readOnly = this.resolvers[i].isReadOnly(context, base, property);
/* 128 */       if (context.isPropertyResolved()) {
/* 129 */         return readOnly;
/*     */       }
/*     */     }
/* 132 */     return false;
/*     */   }
/*     */   
/*     */   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base)
/*     */   {
/* 137 */     return new FeatureIterator(context, base, this.resolvers, this.size);
/*     */   }
/*     */   
/*     */   public Class<?> getCommonPropertyType(ELContext context, Object base)
/*     */   {
/* 142 */     Class<?> commonType = null;
/* 143 */     int sz = this.size;
/* 144 */     for (int i = 0; i < sz; i++) {
/* 145 */       Class<?> type = this.resolvers[i].getCommonPropertyType(context, base);
/* 146 */       if ((type != null) && ((commonType == null) || (commonType.isAssignableFrom(type)))) {
/* 147 */         commonType = type;
/*     */       }
/*     */     }
/* 150 */     return commonType;
/*     */   }
/*     */   
/*     */   public Object convertToType(ELContext context, Object obj, Class<?> type)
/*     */   {
/* 155 */     context.setPropertyResolved(false);
/* 156 */     int sz = this.size;
/* 157 */     for (int i = 0; i < sz; i++) {
/* 158 */       Object result = this.resolvers[i].convertToType(context, obj, type);
/* 159 */       if (context.isPropertyResolved()) {
/* 160 */         return result;
/*     */       }
/*     */     }
/* 163 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class FeatureIterator
/*     */     implements Iterator<FeatureDescriptor>
/*     */   {
/*     */     private final ELContext context;
/*     */     
/*     */     private final Object base;
/*     */     
/*     */     private final ELResolver[] resolvers;
/*     */     
/*     */     private final int size;
/*     */     private Iterator<FeatureDescriptor> itr;
/*     */     private int idx;
/*     */     private FeatureDescriptor next;
/*     */     
/*     */     public FeatureIterator(ELContext context, Object base, ELResolver[] resolvers, int size)
/*     */     {
/* 183 */       this.context = context;
/* 184 */       this.base = base;
/* 185 */       this.resolvers = resolvers;
/* 186 */       this.size = size;
/*     */       
/* 188 */       this.idx = 0;
/* 189 */       guaranteeIterator();
/*     */     }
/*     */     
/*     */     private void guaranteeIterator() {
/* 193 */       while ((this.itr == null) && (this.idx < this.size)) {
/* 194 */         this.itr = this.resolvers[this.idx].getFeatureDescriptors(this.context, this.base);
/* 195 */         this.idx += 1;
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean hasNext()
/*     */     {
/* 201 */       if (this.next != null)
/* 202 */         return true;
/* 203 */       if (this.itr != null) {
/* 204 */         while ((this.next == null) && (this.itr.hasNext())) {
/* 205 */           this.next = ((FeatureDescriptor)this.itr.next());
/*     */         }
/*     */       }
/* 208 */       return false;
/*     */       
/* 210 */       if (this.next == null) {
/* 211 */         this.itr = null;
/* 212 */         guaranteeIterator();
/*     */       }
/* 214 */       return hasNext();
/*     */     }
/*     */     
/*     */     public FeatureDescriptor next()
/*     */     {
/* 219 */       if (!hasNext()) {
/* 220 */         throw new NoSuchElementException();
/*     */       }
/* 222 */       FeatureDescriptor result = this.next;
/* 223 */       this.next = null;
/* 224 */       return result;
/*     */     }
/*     */     
/*     */ 
/*     */     public void remove()
/*     */     {
/* 230 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\CompositeELResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */