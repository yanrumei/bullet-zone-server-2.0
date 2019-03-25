/*     */ package javax.el;
/*     */ 
/*     */ import java.beans.BeanInfo;
/*     */ import java.beans.FeatureDescriptor;
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.Introspector;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public class BeanELResolver
/*     */   extends ELResolver
/*     */ {
/*     */   private static final int CACHE_SIZE;
/*     */   private static final String CACHE_SIZE_PROP = "org.apache.el.BeanELResolver.CACHE_SIZE";
/*     */   private final boolean readOnly;
/*     */   
/*     */   static
/*     */   {
/*     */     String cacheSizeStr;
/*     */     String cacheSizeStr;
/*  45 */     if (System.getSecurityManager() == null) {
/*  46 */       cacheSizeStr = System.getProperty("org.apache.el.BeanELResolver.CACHE_SIZE", "1000");
/*     */     } else {
/*  48 */       cacheSizeStr = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */ 
/*     */         public String run()
/*     */         {
/*  53 */           return System.getProperty("org.apache.el.BeanELResolver.CACHE_SIZE", "1000");
/*     */         }
/*     */       });
/*     */     }
/*  57 */     CACHE_SIZE = Integer.parseInt(cacheSizeStr);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  62 */   private final ConcurrentCache<String, BeanProperties> cache = new ConcurrentCache(CACHE_SIZE);
/*     */   
/*     */   public BeanELResolver()
/*     */   {
/*  66 */     this.readOnly = false;
/*     */   }
/*     */   
/*     */   public BeanELResolver(boolean readOnly) {
/*  70 */     this.readOnly = readOnly;
/*     */   }
/*     */   
/*     */   public Class<?> getType(ELContext context, Object base, Object property)
/*     */   {
/*  75 */     Objects.requireNonNull(context);
/*  76 */     if ((base == null) || (property == null)) {
/*  77 */       return null;
/*     */     }
/*     */     
/*  80 */     context.setPropertyResolved(base, property);
/*  81 */     return property(context, base, property).getPropertyType();
/*     */   }
/*     */   
/*     */   public Object getValue(ELContext context, Object base, Object property)
/*     */   {
/*  86 */     Objects.requireNonNull(context);
/*  87 */     if ((base == null) || (property == null)) {
/*  88 */       return null;
/*     */     }
/*     */     
/*  91 */     context.setPropertyResolved(base, property);
/*  92 */     Method m = property(context, base, property).read(context);
/*     */     try {
/*  94 */       return m.invoke(base, (Object[])null);
/*     */     } catch (InvocationTargetException e) {
/*  96 */       Throwable cause = e.getCause();
/*  97 */       Util.handleThrowable(cause);
/*  98 */       throw new ELException(Util.message(context, "propertyReadError", new Object[] {base
/*  99 */         .getClass().getName(), property.toString() }), cause);
/*     */     } catch (Exception e) {
/* 101 */       throw new ELException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void setValue(ELContext context, Object base, Object property, Object value)
/*     */   {
/* 108 */     Objects.requireNonNull(context);
/* 109 */     if ((base == null) || (property == null)) {
/* 110 */       return;
/*     */     }
/*     */     
/* 113 */     context.setPropertyResolved(base, property);
/*     */     
/* 115 */     if (this.readOnly) {
/* 116 */       throw new PropertyNotWritableException(Util.message(context, "resolverNotWriteable", new Object[] {base
/* 117 */         .getClass().getName() }));
/*     */     }
/*     */     
/* 120 */     Method m = property(context, base, property).write(context);
/*     */     try {
/* 122 */       m.invoke(base, new Object[] { value });
/*     */     } catch (InvocationTargetException e) {
/* 124 */       Throwable cause = e.getCause();
/* 125 */       Util.handleThrowable(cause);
/* 126 */       throw new ELException(Util.message(context, "propertyWriteError", new Object[] {base
/* 127 */         .getClass().getName(), property.toString() }), cause);
/*     */     } catch (Exception e) {
/* 129 */       throw new ELException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object invoke(ELContext context, Object base, Object method, Class<?>[] paramTypes, Object[] params)
/*     */   {
/* 139 */     Objects.requireNonNull(context);
/* 140 */     if ((base == null) || (method == null)) {
/* 141 */       return null;
/*     */     }
/*     */     
/* 144 */     ExpressionFactory factory = ELManager.getExpressionFactory();
/*     */     
/* 146 */     String methodName = (String)factory.coerceToType(method, String.class);
/*     */     
/*     */ 
/*     */ 
/* 150 */     Method matchingMethod = Util.findMethod(base.getClass(), methodName, paramTypes, params);
/*     */     
/* 152 */     Object[] parameters = Util.buildParameters(matchingMethod
/* 153 */       .getParameterTypes(), matchingMethod.isVarArgs(), params);
/*     */     
/*     */ 
/* 156 */     Object result = null;
/*     */     try {
/* 158 */       result = matchingMethod.invoke(base, parameters);
/*     */     } catch (IllegalArgumentException|IllegalAccessException e) {
/* 160 */       throw new ELException(e);
/*     */     } catch (InvocationTargetException e) {
/* 162 */       Throwable cause = e.getCause();
/* 163 */       Util.handleThrowable(cause);
/* 164 */       throw new ELException(cause);
/*     */     }
/*     */     
/* 167 */     context.setPropertyResolved(base, method);
/* 168 */     return result;
/*     */   }
/*     */   
/*     */   public boolean isReadOnly(ELContext context, Object base, Object property)
/*     */   {
/* 173 */     Objects.requireNonNull(context);
/* 174 */     if ((base == null) || (property == null)) {
/* 175 */       return false;
/*     */     }
/*     */     
/* 178 */     context.setPropertyResolved(base, property);
/* 179 */     return (this.readOnly) || (property(context, base, property).isReadOnly());
/*     */   }
/*     */   
/*     */   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base)
/*     */   {
/* 184 */     if (base == null) {
/* 185 */       return null;
/*     */     }
/*     */     try
/*     */     {
/* 189 */       BeanInfo info = Introspector.getBeanInfo(base.getClass());
/* 190 */       PropertyDescriptor[] pds = info.getPropertyDescriptors();
/* 191 */       for (int i = 0; i < pds.length; i++) {
/* 192 */         pds[i].setValue("resolvableAtDesignTime", Boolean.TRUE);
/* 193 */         pds[i].setValue("type", pds[i].getPropertyType());
/*     */       }
/* 195 */       return Arrays.asList((FeatureDescriptor[])pds).iterator();
/*     */     }
/*     */     catch (IntrospectionException localIntrospectionException) {}
/*     */     
/*     */ 
/* 200 */     return null;
/*     */   }
/*     */   
/*     */   public Class<?> getCommonPropertyType(ELContext context, Object base)
/*     */   {
/* 205 */     if (base != null) {
/* 206 */       return Object.class;
/*     */     }
/*     */     
/* 209 */     return null;
/*     */   }
/*     */   
/*     */   static final class BeanProperties
/*     */   {
/*     */     private final Map<String, BeanELResolver.BeanProperty> properties;
/*     */     private final Class<?> type;
/*     */     
/*     */     public BeanProperties(Class<?> type) throws ELException {
/* 218 */       this.type = type;
/* 219 */       this.properties = new HashMap();
/*     */       try {
/* 221 */         BeanInfo info = Introspector.getBeanInfo(this.type);
/* 222 */         PropertyDescriptor[] pds = info.getPropertyDescriptors();
/* 223 */         for (PropertyDescriptor pd : pds) {
/* 224 */           this.properties.put(pd.getName(), new BeanELResolver.BeanProperty(type, pd));
/*     */         }
/* 226 */         if (System.getSecurityManager() != null)
/*     */         {
/*     */ 
/* 229 */           populateFromInterfaces(type);
/*     */         }
/*     */       } catch (IntrospectionException ie) {
/* 232 */         throw new ELException(ie);
/*     */       }
/*     */     }
/*     */     
/*     */     private void populateFromInterfaces(Class<?> aClass) throws IntrospectionException {
/* 237 */       Class<?>[] interfaces = aClass.getInterfaces();
/* 238 */       if (interfaces.length > 0) {
/* 239 */         for (Class<?> ifs : interfaces) {
/* 240 */           BeanInfo info = Introspector.getBeanInfo(ifs);
/* 241 */           PropertyDescriptor[] pds = info.getPropertyDescriptors();
/* 242 */           for (PropertyDescriptor pd : pds) {
/* 243 */             if (!this.properties.containsKey(pd.getName())) {
/* 244 */               this.properties.put(pd.getName(), new BeanELResolver.BeanProperty(this.type, pd));
/*     */             }
/*     */           }
/*     */           
/* 248 */           populateFromInterfaces(ifs);
/*     */         }
/*     */       }
/* 251 */       Object superclass = aClass.getSuperclass();
/* 252 */       if (superclass != null) {
/* 253 */         populateFromInterfaces((Class)superclass);
/*     */       }
/*     */     }
/*     */     
/*     */     private BeanELResolver.BeanProperty get(ELContext ctx, String name) {
/* 258 */       BeanELResolver.BeanProperty property = (BeanELResolver.BeanProperty)this.properties.get(name);
/* 259 */       if (property == null) {
/* 260 */         throw new PropertyNotFoundException(Util.message(ctx, "propertyNotFound", new Object[] {this.type
/* 261 */           .getName(), name }));
/*     */       }
/* 263 */       return property;
/*     */     }
/*     */     
/*     */     public BeanELResolver.BeanProperty getBeanProperty(String name) {
/* 267 */       return get(null, name);
/*     */     }
/*     */     
/*     */     private Class<?> getType() {
/* 271 */       return this.type;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static final class BeanProperty
/*     */   {
/*     */     private final Class<?> type;
/*     */     
/*     */     private final Class<?> owner;
/*     */     private final PropertyDescriptor descriptor;
/*     */     private Method read;
/*     */     private Method write;
/*     */     
/*     */     public BeanProperty(Class<?> owner, PropertyDescriptor descriptor)
/*     */     {
/* 287 */       this.owner = owner;
/* 288 */       this.descriptor = descriptor;
/* 289 */       this.type = descriptor.getPropertyType();
/*     */     }
/*     */     
/*     */ 
/*     */     public Class getPropertyType()
/*     */     {
/* 295 */       return this.type;
/*     */     }
/*     */     
/*     */     public boolean isReadOnly() {
/* 299 */       return (this.write == null) && 
/* 300 */         (null == (this.write = Util.getMethod(this.owner, this.descriptor.getWriteMethod())));
/*     */     }
/*     */     
/*     */     public Method getWriteMethod() {
/* 304 */       return write(null);
/*     */     }
/*     */     
/*     */     public Method getReadMethod() {
/* 308 */       return read(null);
/*     */     }
/*     */     
/*     */     private Method write(ELContext ctx) {
/* 312 */       if (this.write == null) {
/* 313 */         this.write = Util.getMethod(this.owner, this.descriptor.getWriteMethod());
/* 314 */         if (this.write == null) {
/* 315 */           throw new PropertyNotWritableException(Util.message(ctx, "propertyNotWritable", new Object[] {this.owner
/*     */           
/* 317 */             .getName(), this.descriptor.getName() }));
/*     */         }
/*     */       }
/* 320 */       return this.write;
/*     */     }
/*     */     
/*     */     private Method read(ELContext ctx) {
/* 324 */       if (this.read == null) {
/* 325 */         this.read = Util.getMethod(this.owner, this.descriptor.getReadMethod());
/* 326 */         if (this.read == null) {
/* 327 */           throw new PropertyNotFoundException(Util.message(ctx, "propertyNotReadable", new Object[] {this.owner
/*     */           
/* 329 */             .getName(), this.descriptor.getName() }));
/*     */         }
/*     */       }
/* 332 */       return this.read;
/*     */     }
/*     */   }
/*     */   
/*     */   private final BeanProperty property(ELContext ctx, Object base, Object property)
/*     */   {
/* 338 */     Class<?> type = base.getClass();
/* 339 */     String prop = property.toString();
/*     */     
/* 341 */     BeanProperties props = (BeanProperties)this.cache.get(type.getName());
/* 342 */     if ((props == null) || (type != props.getType())) {
/* 343 */       props = new BeanProperties(type);
/* 344 */       this.cache.put(type.getName(), props);
/*     */     }
/*     */     
/* 347 */     return props.get(ctx, prop);
/*     */   }
/*     */   
/*     */   private static final class ConcurrentCache<K, V>
/*     */   {
/*     */     private final int size;
/*     */     private final Map<K, V> eden;
/*     */     private final Map<K, V> longterm;
/*     */     
/*     */     public ConcurrentCache(int size) {
/* 357 */       this.size = size;
/* 358 */       this.eden = new ConcurrentHashMap(size);
/* 359 */       this.longterm = new WeakHashMap(size);
/*     */     }
/*     */     
/*     */     public V get(K key) {
/* 363 */       V value = this.eden.get(key);
/* 364 */       if (value == null) {
/* 365 */         synchronized (this.longterm) {
/* 366 */           value = this.longterm.get(key);
/*     */         }
/* 368 */         if (value != null) {
/* 369 */           this.eden.put(key, value);
/*     */         }
/*     */       }
/* 372 */       return value;
/*     */     }
/*     */     
/*     */     public void put(K key, V value) {
/* 376 */       if (this.eden.size() >= this.size) {
/* 377 */         synchronized (this.longterm) {
/* 378 */           this.longterm.putAll(this.eden);
/*     */         }
/* 380 */         this.eden.clear();
/*     */       }
/* 382 */       this.eden.put(key, value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\BeanELResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */