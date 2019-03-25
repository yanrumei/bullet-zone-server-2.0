/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.convert.Property;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class BeanWrapperImpl
/*     */   extends AbstractNestablePropertyAccessor
/*     */   implements BeanWrapper
/*     */ {
/*     */   private CachedIntrospectionResults cachedIntrospectionResults;
/*     */   private AccessControlContext acc;
/*     */   
/*     */   public BeanWrapperImpl()
/*     */   {
/*  83 */     this(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanWrapperImpl(boolean registerDefaultEditors)
/*     */   {
/*  93 */     super(registerDefaultEditors);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanWrapperImpl(Object object)
/*     */   {
/* 101 */     super(object);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanWrapperImpl(Class<?> clazz)
/*     */   {
/* 109 */     super(clazz);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanWrapperImpl(Object object, String nestedPath, Object rootObject)
/*     */   {
/* 120 */     super(object, nestedPath, rootObject);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private BeanWrapperImpl(Object object, String nestedPath, BeanWrapperImpl parent)
/*     */   {
/* 131 */     super(object, nestedPath, parent);
/* 132 */     setSecurityContext(parent.acc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBeanInstance(Object object)
/*     */   {
/* 143 */     this.wrappedObject = object;
/* 144 */     this.rootObject = object;
/* 145 */     this.typeConverterDelegate = new TypeConverterDelegate(this, this.wrappedObject);
/* 146 */     setIntrospectionClass(object.getClass());
/*     */   }
/*     */   
/*     */   public void setWrappedInstance(Object object, String nestedPath, Object rootObject)
/*     */   {
/* 151 */     super.setWrappedInstance(object, nestedPath, rootObject);
/* 152 */     setIntrospectionClass(getWrappedClass());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setIntrospectionClass(Class<?> clazz)
/*     */   {
/* 161 */     if ((this.cachedIntrospectionResults != null) && (this.cachedIntrospectionResults.getBeanClass() != clazz)) {
/* 162 */       this.cachedIntrospectionResults = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private CachedIntrospectionResults getCachedIntrospectionResults()
/*     */   {
/* 171 */     Assert.state(getWrappedInstance() != null, "BeanWrapper does not hold a bean instance");
/* 172 */     if (this.cachedIntrospectionResults == null) {
/* 173 */       this.cachedIntrospectionResults = CachedIntrospectionResults.forClass(getWrappedClass());
/*     */     }
/* 175 */     return this.cachedIntrospectionResults;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSecurityContext(AccessControlContext acc)
/*     */   {
/* 183 */     this.acc = acc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AccessControlContext getSecurityContext()
/*     */   {
/* 191 */     return this.acc;
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
/*     */   public Object convertForProperty(Object value, String propertyName)
/*     */     throws TypeMismatchException
/*     */   {
/* 206 */     CachedIntrospectionResults cachedIntrospectionResults = getCachedIntrospectionResults();
/* 207 */     PropertyDescriptor pd = cachedIntrospectionResults.getPropertyDescriptor(propertyName);
/* 208 */     if (pd == null) {
/* 209 */       throw new InvalidPropertyException(getRootClass(), getNestedPath() + propertyName, "No property '" + propertyName + "' found");
/*     */     }
/*     */     
/* 212 */     TypeDescriptor td = cachedIntrospectionResults.getTypeDescriptor(pd);
/* 213 */     if (td == null) {
/* 214 */       td = cachedIntrospectionResults.addTypeDescriptor(pd, new TypeDescriptor(property(pd)));
/*     */     }
/* 216 */     return convertForProperty(propertyName, null, value, td);
/*     */   }
/*     */   
/*     */   private Property property(PropertyDescriptor pd) {
/* 220 */     GenericTypeAwarePropertyDescriptor gpd = (GenericTypeAwarePropertyDescriptor)pd;
/* 221 */     return new Property(gpd.getBeanClass(), gpd.getReadMethod(), gpd.getWriteMethod(), gpd.getName());
/*     */   }
/*     */   
/*     */   protected BeanPropertyHandler getLocalPropertyHandler(String propertyName)
/*     */   {
/* 226 */     PropertyDescriptor pd = getCachedIntrospectionResults().getPropertyDescriptor(propertyName);
/* 227 */     if (pd != null) {
/* 228 */       return new BeanPropertyHandler(pd);
/*     */     }
/* 230 */     return null;
/*     */   }
/*     */   
/*     */   protected BeanWrapperImpl newNestedPropertyAccessor(Object object, String nestedPath)
/*     */   {
/* 235 */     return new BeanWrapperImpl(object, nestedPath, this);
/*     */   }
/*     */   
/*     */   protected NotWritablePropertyException createNotWritablePropertyException(String propertyName)
/*     */   {
/* 240 */     PropertyMatches matches = PropertyMatches.forProperty(propertyName, getRootClass());
/*     */     
/*     */ 
/* 243 */     throw new NotWritablePropertyException(getRootClass(), getNestedPath() + propertyName, matches.buildErrorMessage(), matches.getPossibleMatches());
/*     */   }
/*     */   
/*     */   public PropertyDescriptor[] getPropertyDescriptors()
/*     */   {
/* 248 */     return getCachedIntrospectionResults().getPropertyDescriptors();
/*     */   }
/*     */   
/*     */   public PropertyDescriptor getPropertyDescriptor(String propertyName) throws InvalidPropertyException
/*     */   {
/* 253 */     BeanWrapperImpl nestedBw = (BeanWrapperImpl)getPropertyAccessorForPropertyPath(propertyName);
/* 254 */     String finalPath = getFinalPath(nestedBw, propertyName);
/* 255 */     PropertyDescriptor pd = nestedBw.getCachedIntrospectionResults().getPropertyDescriptor(finalPath);
/* 256 */     if (pd == null) {
/* 257 */       throw new InvalidPropertyException(getRootClass(), getNestedPath() + propertyName, "No property '" + propertyName + "' found");
/*     */     }
/*     */     
/* 260 */     return pd;
/*     */   }
/*     */   
/*     */   private class BeanPropertyHandler extends AbstractNestablePropertyAccessor.PropertyHandler
/*     */   {
/*     */     private final PropertyDescriptor pd;
/*     */     
/*     */     public BeanPropertyHandler(PropertyDescriptor pd)
/*     */     {
/* 269 */       super(pd.getReadMethod() != null, pd.getWriteMethod() != null);
/* 270 */       this.pd = pd;
/*     */     }
/*     */     
/*     */     public ResolvableType getResolvableType()
/*     */     {
/* 275 */       return ResolvableType.forMethodReturnType(this.pd.getReadMethod());
/*     */     }
/*     */     
/*     */     public TypeDescriptor toTypeDescriptor()
/*     */     {
/* 280 */       return new TypeDescriptor(BeanWrapperImpl.this.property(this.pd));
/*     */     }
/*     */     
/*     */     public TypeDescriptor nested(int level)
/*     */     {
/* 285 */       return TypeDescriptor.nested(BeanWrapperImpl.this.property(this.pd), level);
/*     */     }
/*     */     
/*     */     public Object getValue() throws Exception
/*     */     {
/* 290 */       final Method readMethod = this.pd.getReadMethod();
/* 291 */       if ((!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) && (!readMethod.isAccessible())) {
/* 292 */         if (System.getSecurityManager() != null) {
/* 293 */           AccessController.doPrivileged(new PrivilegedAction()
/*     */           {
/*     */             public Object run() {
/* 296 */               readMethod.setAccessible(true);
/* 297 */               return null;
/*     */             }
/*     */             
/*     */           });
/*     */         } else {
/* 302 */           readMethod.setAccessible(true);
/*     */         }
/*     */       }
/* 305 */       if (System.getSecurityManager() != null) {
/*     */         try {
/* 307 */           AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */           {
/*     */             public Object run() throws Exception {
/* 310 */               return readMethod.invoke(BeanWrapperImpl.this.getWrappedInstance(), (Object[])null);
/*     */             }
/* 312 */           }, BeanWrapperImpl.this.acc);
/*     */         }
/*     */         catch (PrivilegedActionException pae) {
/* 315 */           throw pae.getException();
/*     */         }
/*     */       }
/*     */       
/* 319 */       return readMethod.invoke(BeanWrapperImpl.this.getWrappedInstance(), (Object[])null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void setValue(final Object object, Object valueToApply)
/*     */       throws Exception
/*     */     {
/* 327 */       final Method writeMethod = (this.pd instanceof GenericTypeAwarePropertyDescriptor) ? ((GenericTypeAwarePropertyDescriptor)this.pd).getWriteMethodForActualAccess() : this.pd.getWriteMethod();
/* 328 */       if ((!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) && (!writeMethod.isAccessible())) {
/* 329 */         if (System.getSecurityManager() != null) {
/* 330 */           AccessController.doPrivileged(new PrivilegedAction()
/*     */           {
/*     */             public Object run() {
/* 333 */               writeMethod.setAccessible(true);
/* 334 */               return null;
/*     */             }
/*     */             
/*     */           });
/*     */         } else {
/* 339 */           writeMethod.setAccessible(true);
/*     */         }
/*     */       }
/* 342 */       final Object value = valueToApply;
/* 343 */       if (System.getSecurityManager() != null) {
/*     */         try {
/* 345 */           AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */           {
/*     */             public Object run() throws Exception {
/* 348 */               writeMethod.invoke(object, new Object[] { value });
/* 349 */               return null;
/*     */             }
/* 351 */           }, BeanWrapperImpl.this.acc);
/*     */         }
/*     */         catch (PrivilegedActionException ex) {
/* 354 */           throw ex.getException();
/*     */         }
/*     */         
/*     */       } else {
/* 358 */         writeMethod.invoke(BeanWrapperImpl.this.getWrappedInstance(), new Object[] { value });
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\BeanWrapperImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */