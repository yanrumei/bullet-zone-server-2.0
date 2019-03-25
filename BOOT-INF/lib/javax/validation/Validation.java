/*     */ package javax.validation;
/*     */ 
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ServiceConfigurationError;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.WeakHashMap;
/*     */ import javax.validation.bootstrap.GenericBootstrap;
/*     */ import javax.validation.bootstrap.ProviderSpecificBootstrap;
/*     */ import javax.validation.spi.BootstrapState;
/*     */ import javax.validation.spi.ValidationProvider;
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
/*     */ public class Validation
/*     */ {
/*     */   public static ValidatorFactory buildDefaultValidatorFactory()
/*     */   {
/* 110 */     return byDefaultProvider().configure().buildValidatorFactory();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static GenericBootstrap byDefaultProvider()
/*     */   {
/* 131 */     return new GenericBootstrapImpl(null);
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
/*     */   public static <T extends Configuration<T>, U extends ValidationProvider<T>> ProviderSpecificBootstrap<T> byProvider(Class<U> providerType)
/*     */   {
/* 159 */     return new ProviderSpecificBootstrapImpl(providerType);
/*     */   }
/*     */   
/*     */   private static class ProviderSpecificBootstrapImpl<T extends Configuration<T>, U extends ValidationProvider<T>>
/*     */     implements ProviderSpecificBootstrap<T>
/*     */   {
/*     */     private final Class<U> validationProviderClass;
/*     */     private ValidationProviderResolver resolver;
/*     */     
/*     */     public ProviderSpecificBootstrapImpl(Class<U> validationProviderClass)
/*     */     {
/* 170 */       this.validationProviderClass = validationProviderClass;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ProviderSpecificBootstrap<T> providerResolver(ValidationProviderResolver resolver)
/*     */     {
/* 182 */       this.resolver = resolver;
/* 183 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public T configure()
/*     */     {
/* 194 */       if (this.validationProviderClass == null) {
/* 195 */         throw new ValidationException("builder is mandatory. Use Validation.byDefaultProvider() to use the generic provider discovery mechanism");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 200 */       Validation.GenericBootstrapImpl state = new Validation.GenericBootstrapImpl(null);
/* 201 */       if (this.resolver == null) {
/* 202 */         this.resolver = state.getDefaultValidationProviderResolver();
/*     */       }
/*     */       else
/*     */       {
/* 206 */         state.providerResolver(this.resolver);
/*     */       }
/*     */       List<ValidationProvider<?>> resolvers;
/*     */       try
/*     */       {
/* 211 */         resolvers = this.resolver.getValidationProviders();
/*     */       }
/*     */       catch (RuntimeException re) {
/* 214 */         throw new ValidationException("Unable to get available provider resolvers.", re);
/*     */       }
/*     */       
/* 217 */       for (ValidationProvider provider : resolvers) {
/* 218 */         if (this.validationProviderClass.isAssignableFrom(provider.getClass())) {
/* 219 */           ValidationProvider<T> specificProvider = (ValidationProvider)this.validationProviderClass.cast(provider);
/* 220 */           return specificProvider.createSpecializedConfiguration(state);
/*     */         }
/*     */       }
/*     */       
/* 224 */       throw new ValidationException("Unable to find provider: " + this.validationProviderClass);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class GenericBootstrapImpl implements GenericBootstrap, BootstrapState
/*     */   {
/*     */     private ValidationProviderResolver resolver;
/*     */     private ValidationProviderResolver defaultResolver;
/*     */     
/*     */     public GenericBootstrap providerResolver(ValidationProviderResolver resolver)
/*     */     {
/* 235 */       this.resolver = resolver;
/* 236 */       return this;
/*     */     }
/*     */     
/*     */     public ValidationProviderResolver getValidationProviderResolver() {
/* 240 */       return this.resolver;
/*     */     }
/*     */     
/*     */     public ValidationProviderResolver getDefaultValidationProviderResolver() {
/* 244 */       if (this.defaultResolver == null) {
/* 245 */         this.defaultResolver = new Validation.DefaultValidationProviderResolver(null);
/*     */       }
/* 247 */       return this.defaultResolver;
/*     */     }
/*     */     
/*     */     public Configuration<?> configure() {
/* 251 */       ValidationProviderResolver resolver = this.resolver == null ? getDefaultValidationProviderResolver() : this.resolver;
/*     */       
/*     */       List<ValidationProvider<?>> validationProviders;
/*     */       
/*     */       try
/*     */       {
/* 257 */         validationProviders = resolver.getValidationProviders();
/*     */       }
/*     */       catch (ValidationException e)
/*     */       {
/* 261 */         throw e;
/*     */       }
/*     */       catch (RuntimeException re)
/*     */       {
/* 265 */         throw new ValidationException("Unable to get available provider resolvers.", re);
/*     */       }
/*     */       
/* 268 */       if (validationProviders.size() == 0) {
/* 269 */         String msg = "Unable to create a Configuration, because no Bean Validation provider could be found. Add a provider like Hibernate Validator (RI) to your classpath.";
/*     */         
/* 271 */         throw new ValidationException(msg);
/*     */       }
/*     */       Configuration<?> config;
/*     */       try
/*     */       {
/* 276 */         config = ((ValidationProvider)resolver.getValidationProviders().get(0)).createGenericConfiguration(this);
/*     */       }
/*     */       catch (RuntimeException re) {
/* 279 */         throw new ValidationException("Unable to instantiate Configuration.", re);
/*     */       }
/*     */       
/* 282 */       return config;
/*     */     }
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
/*     */   private static class DefaultValidationProviderResolver
/*     */     implements ValidationProviderResolver
/*     */   {
/*     */     public List<ValidationProvider<?>> getValidationProviders()
/*     */     {
/* 299 */       return Validation.GetValidationProviderListAction.getValidationProviderList();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class GetValidationProviderListAction
/*     */     implements PrivilegedAction<List<ValidationProvider<?>>>
/*     */   {
/* 307 */     private static final WeakHashMap<ClassLoader, SoftReference<List<ValidationProvider<?>>>> providersPerClassloader = new WeakHashMap();
/*     */     
/*     */     public static List<ValidationProvider<?>> getValidationProviderList()
/*     */     {
/* 311 */       GetValidationProviderListAction action = new GetValidationProviderListAction();
/* 312 */       if (System.getSecurityManager() != null) {
/* 313 */         return (List)AccessController.doPrivileged(action);
/*     */       }
/*     */       
/* 316 */       return action.run();
/*     */     }
/*     */     
/*     */ 
/*     */     public List<ValidationProvider<?>> run()
/*     */     {
/* 322 */       ClassLoader classloader = Thread.currentThread().getContextClassLoader();
/* 323 */       List<ValidationProvider<?>> cachedContextClassLoaderProviderList = getCachedValidationProviders(classloader);
/* 324 */       if (cachedContextClassLoaderProviderList != null)
/*     */       {
/* 326 */         return cachedContextClassLoaderProviderList;
/*     */       }
/*     */       
/* 329 */       List<ValidationProvider<?>> validationProviderList = loadProviders(classloader);
/*     */       
/*     */ 
/* 332 */       if (validationProviderList.isEmpty()) {
/* 333 */         classloader = Validation.DefaultValidationProviderResolver.class.getClassLoader();
/* 334 */         List<ValidationProvider<?>> cachedCurrentClassLoaderProviderList = getCachedValidationProviders(classloader);
/*     */         
/*     */ 
/* 337 */         if (cachedCurrentClassLoaderProviderList != null)
/*     */         {
/* 339 */           return cachedCurrentClassLoaderProviderList;
/*     */         }
/* 341 */         validationProviderList = loadProviders(classloader);
/*     */       }
/*     */       
/*     */ 
/* 345 */       cacheValidationProviders(classloader, validationProviderList);
/*     */       
/* 347 */       return validationProviderList;
/*     */     }
/*     */     
/*     */     private List<ValidationProvider<?>> loadProviders(ClassLoader classloader) {
/* 351 */       ServiceLoader<ValidationProvider> loader = ServiceLoader.load(ValidationProvider.class, classloader);
/* 352 */       Iterator<ValidationProvider> providerIterator = loader.iterator();
/* 353 */       List<ValidationProvider<?>> validationProviderList = new ArrayList();
/* 354 */       while (providerIterator.hasNext()) {
/*     */         try {
/* 356 */           validationProviderList.add(providerIterator.next());
/*     */         }
/*     */         catch (ServiceConfigurationError e) {}
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 364 */       return validationProviderList;
/*     */     }
/*     */     
/*     */     private synchronized List<ValidationProvider<?>> getCachedValidationProviders(ClassLoader classLoader) {
/* 368 */       SoftReference<List<ValidationProvider<?>>> ref = (SoftReference)providersPerClassloader.get(classLoader);
/* 369 */       return ref != null ? (List)ref.get() : null;
/*     */     }
/*     */     
/*     */     private synchronized void cacheValidationProviders(ClassLoader classLoader, List<ValidationProvider<?>> providers) {
/* 373 */       providersPerClassloader.put(classLoader, new SoftReference(providers));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\Validation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */