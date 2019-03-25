/*     */ package org.apache.catalina.authenticator.jaspic;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import javax.security.auth.message.config.AuthConfigFactory;
/*     */ import javax.security.auth.message.config.AuthConfigFactory.RegistrationContext;
/*     */ import javax.security.auth.message.config.AuthConfigProvider;
/*     */ import javax.security.auth.message.config.RegistrationListener;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.res.StringManager;
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
/*     */ public class AuthConfigFactoryImpl
/*     */   extends AuthConfigFactory
/*     */ {
/*  44 */   private static final Log log = LogFactory.getLog(AuthConfigFactoryImpl.class);
/*  45 */   private static final StringManager sm = StringManager.getManager(AuthConfigFactoryImpl.class);
/*     */   
/*     */   private static final String CONFIG_PATH = "conf/jaspic-providers.xml";
/*  48 */   private static final File CONFIG_FILE = new File(
/*  49 */     System.getProperty("catalina.base"), "conf/jaspic-providers.xml");
/*  50 */   private static final Object CONFIG_FILE_LOCK = new Object();
/*     */   
/*  52 */   private static final String[] EMPTY_STRING_ARRAY = new String[0];
/*     */   
/*  54 */   private static String DEFAULT_REGISTRATION_ID = getRegistrationID(null, null);
/*     */   
/*  56 */   private final Map<String, RegistrationContextImpl> layerAppContextRegistrations = new ConcurrentHashMap();
/*     */   
/*  58 */   private final Map<String, RegistrationContextImpl> appContextRegistrations = new ConcurrentHashMap();
/*     */   
/*  60 */   private final Map<String, RegistrationContextImpl> layerRegistrations = new ConcurrentHashMap();
/*     */   
/*     */ 
/*     */ 
/*  64 */   private final Map<String, RegistrationContextImpl> defaultRegistration = new ConcurrentHashMap(1);
/*     */   
/*     */ 
/*     */   public AuthConfigFactoryImpl()
/*     */   {
/*  69 */     loadPersistentRegistrations();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AuthConfigProvider getConfigProvider(String layer, String appContext, RegistrationListener listener)
/*     */   {
/*  77 */     RegistrationContextImpl registrationContext = findRegistrationContextImpl(layer, appContext);
/*  78 */     if (registrationContext != null) {
/*  79 */       if (listener != null) {
/*  80 */         RegistrationListenerWrapper wrapper = new RegistrationListenerWrapper(layer, appContext, listener);
/*     */         
/*  82 */         registrationContext.addListener(wrapper);
/*     */       }
/*  84 */       return registrationContext.getProvider();
/*     */     }
/*  86 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String registerConfigProvider(String className, Map properties, String layer, String appContext, String description)
/*     */   {
/*  95 */     String registrationID = doRegisterConfigProvider(className, properties, layer, appContext, description);
/*  96 */     savePersistentRegistrations();
/*  97 */     return registrationID;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String doRegisterConfigProvider(String className, Map properties, String layer, String appContext, String description)
/*     */   {
/* 105 */     if (log.isDebugEnabled()) {
/* 106 */       log.debug(sm.getString("authConfigFactoryImpl.registerClass", new Object[] { className, layer, appContext }));
/*     */     }
/*     */     
/*     */ 
/* 110 */     AuthConfigProvider provider = null;
/* 111 */     if (className != null) {
/* 112 */       provider = createAuthConfigProvider(className, properties);
/*     */     }
/*     */     
/* 115 */     String registrationID = getRegistrationID(layer, appContext);
/* 116 */     RegistrationContextImpl registrationContextImpl = new RegistrationContextImpl(layer, appContext, description, true, provider, properties, null);
/*     */     
/* 118 */     addRegistrationContextImpl(layer, appContext, registrationID, registrationContextImpl);
/* 119 */     return registrationID;
/*     */   }
/*     */   
/*     */   private AuthConfigProvider createAuthConfigProvider(String className, Map properties)
/*     */     throws SecurityException
/*     */   {
/* 125 */     Class<?> clazz = null;
/* 126 */     AuthConfigProvider provider = null;
/*     */     try {
/* 128 */       clazz = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {}
/*     */     try
/*     */     {
/* 133 */       if (clazz == null) {
/* 134 */         clazz = Class.forName(className);
/*     */       }
/* 136 */       Constructor<?> constructor = clazz.getConstructor(new Class[] { Map.class, AuthConfigFactory.class });
/* 137 */       provider = (AuthConfigProvider)constructor.newInstance(new Object[] { properties, null });
/*     */     }
/*     */     catch (ClassNotFoundException|NoSuchMethodException|InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException e) {
/* 140 */       throw new SecurityException(e);
/*     */     }
/* 142 */     return provider;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String registerConfigProvider(AuthConfigProvider provider, String layer, String appContext, String description)
/*     */   {
/* 149 */     if (log.isDebugEnabled()) {
/* 150 */       log.debug(sm.getString("authConfigFactoryImpl.registerInstance", new Object[] {provider
/* 151 */         .getClass().getName(), layer, appContext }));
/*     */     }
/* 153 */     String registrationID = getRegistrationID(layer, appContext);
/* 154 */     RegistrationContextImpl registrationContextImpl = new RegistrationContextImpl(layer, appContext, description, false, provider, null, null);
/*     */     
/* 156 */     addRegistrationContextImpl(layer, appContext, registrationID, registrationContextImpl);
/* 157 */     return registrationID;
/*     */   }
/*     */   
/*     */ 
/*     */   private void addRegistrationContextImpl(String layer, String appContext, String registrationID, RegistrationContextImpl registrationContextImpl)
/*     */   {
/* 163 */     RegistrationContextImpl previous = null;
/*     */     
/*     */ 
/* 166 */     if ((layer != null) && (appContext != null)) {
/* 167 */       previous = (RegistrationContextImpl)this.layerAppContextRegistrations.put(registrationID, registrationContextImpl);
/* 168 */     } else if ((layer == null) && (appContext != null)) {
/* 169 */       previous = (RegistrationContextImpl)this.appContextRegistrations.put(registrationID, registrationContextImpl);
/* 170 */     } else if ((layer != null) && (appContext == null)) {
/* 171 */       previous = (RegistrationContextImpl)this.layerRegistrations.put(registrationID, registrationContextImpl);
/*     */     } else
/* 173 */       previous = (RegistrationContextImpl)this.defaultRegistration.put(registrationID, registrationContextImpl);
/*     */     RegistrationContextImpl registration;
/*     */     RegistrationListenerWrapper wrapper;
/* 176 */     RegistrationContextImpl registration; if (previous == null)
/*     */     {
/*     */       Iterator localIterator;
/*     */       
/*     */ 
/* 181 */       if ((layer != null) && (appContext != null))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 186 */         registration = (RegistrationContextImpl)this.appContextRegistrations.get(getRegistrationID(null, appContext));
/* 187 */         if (registration != null) {
/* 188 */           for (localIterator = registration.listeners.iterator(); localIterator.hasNext();) { wrapper = (RegistrationListenerWrapper)localIterator.next();
/* 189 */             if ((layer.equals(wrapper.getMessageLayer())) && 
/* 190 */               (appContext.equals(wrapper.getAppContext()))) {
/* 191 */               registration.listeners.remove(wrapper);
/* 192 */               wrapper.listener.notify(wrapper.messageLayer, wrapper.appContext);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 197 */       if (appContext != null)
/*     */       {
/*     */ 
/*     */ 
/* 201 */         for (registration = this.layerRegistrations.values().iterator(); registration.hasNext();) { registration = (RegistrationContextImpl)registration.next();
/* 202 */           for (RegistrationListenerWrapper wrapper : registration.listeners)
/* 203 */             if (appContext.equals(wrapper.getAppContext())) {
/* 204 */               registration.listeners.remove(wrapper);
/* 205 */               wrapper.listener.notify(wrapper.messageLayer, wrapper.appContext);
/*     */             }
/*     */         }
/*     */       }
/*     */       RegistrationContextImpl registration;
/* 210 */       if ((layer != null) || (appContext != null))
/*     */       {
/* 212 */         for (registration = this.defaultRegistration.values().iterator(); registration.hasNext();) { registration = (RegistrationContextImpl)registration.next();
/* 213 */           for (RegistrationListenerWrapper wrapper : registration.listeners) {
/* 214 */             if (((appContext != null) && (appContext.equals(wrapper.getAppContext()))) || ((layer != null) && 
/* 215 */               (layer.equals(wrapper.getMessageLayer())))) {
/* 216 */               registration.listeners.remove(wrapper);
/* 217 */               wrapper.listener.notify(wrapper.messageLayer, wrapper.appContext);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 224 */       for (RegistrationListenerWrapper wrapper : previous.listeners) {
/* 225 */         previous.listeners.remove(wrapper);
/* 226 */         wrapper.listener.notify(wrapper.messageLayer, wrapper.appContext);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean removeRegistration(String registrationID)
/*     */   {
/* 234 */     RegistrationContextImpl registration = null;
/* 235 */     if (DEFAULT_REGISTRATION_ID.equals(registrationID)) {
/* 236 */       registration = (RegistrationContextImpl)this.defaultRegistration.remove(registrationID);
/*     */     }
/* 238 */     if (registration == null) {
/* 239 */       registration = (RegistrationContextImpl)this.layerAppContextRegistrations.remove(registrationID);
/*     */     }
/* 241 */     if (registration == null) {
/* 242 */       registration = (RegistrationContextImpl)this.appContextRegistrations.remove(registrationID);
/*     */     }
/* 244 */     if (registration == null) {
/* 245 */       registration = (RegistrationContextImpl)this.layerRegistrations.remove(registrationID);
/*     */     }
/*     */     
/* 248 */     if (registration == null) {
/* 249 */       return false;
/*     */     }
/* 251 */     for (RegistrationListenerWrapper wrapper : registration.listeners) {
/* 252 */       wrapper.getListener().notify(wrapper.getMessageLayer(), wrapper.getAppContext());
/*     */     }
/* 254 */     if (registration.isPersistent()) {
/* 255 */       savePersistentRegistrations();
/*     */     }
/* 257 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String[] detachListener(RegistrationListener listener, String layer, String appContext)
/*     */   {
/* 264 */     String registrationID = getRegistrationID(layer, appContext);
/* 265 */     RegistrationContextImpl registrationContext = findRegistrationContextImpl(layer, appContext);
/* 266 */     if ((registrationContext != null) && (registrationContext.removeListener(listener))) {
/* 267 */       return new String[] { registrationID };
/*     */     }
/* 269 */     return EMPTY_STRING_ARRAY;
/*     */   }
/*     */   
/*     */ 
/*     */   public String[] getRegistrationIDs(AuthConfigProvider provider)
/*     */   {
/* 275 */     List<String> result = new ArrayList();
/* 276 */     if (provider == null) {
/* 277 */       result.addAll(this.layerAppContextRegistrations.keySet());
/* 278 */       result.addAll(this.appContextRegistrations.keySet());
/* 279 */       result.addAll(this.layerRegistrations.keySet());
/* 280 */       if (!this.defaultRegistration.isEmpty()) {
/* 281 */         result.add(DEFAULT_REGISTRATION_ID);
/*     */       }
/*     */     } else {
/* 284 */       findProvider(provider, this.layerAppContextRegistrations, result);
/* 285 */       findProvider(provider, this.appContextRegistrations, result);
/* 286 */       findProvider(provider, this.layerRegistrations, result);
/* 287 */       findProvider(provider, this.defaultRegistration, result);
/*     */     }
/* 289 */     return (String[])result.toArray(EMPTY_STRING_ARRAY);
/*     */   }
/*     */   
/*     */ 
/*     */   private void findProvider(AuthConfigProvider provider, Map<String, RegistrationContextImpl> registrations, List<String> result)
/*     */   {
/* 295 */     for (Map.Entry<String, RegistrationContextImpl> entry : registrations.entrySet()) {
/* 296 */       if (provider.equals(((RegistrationContextImpl)entry.getValue()).getProvider())) {
/* 297 */         result.add(entry.getKey());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public AuthConfigFactory.RegistrationContext getRegistrationContext(String registrationID)
/*     */   {
/* 305 */     AuthConfigFactory.RegistrationContext result = (AuthConfigFactory.RegistrationContext)this.defaultRegistration.get(registrationID);
/* 306 */     if (result == null) {
/* 307 */       result = (AuthConfigFactory.RegistrationContext)this.layerAppContextRegistrations.get(registrationID);
/*     */     }
/* 309 */     if (result == null) {
/* 310 */       result = (AuthConfigFactory.RegistrationContext)this.appContextRegistrations.get(registrationID);
/*     */     }
/* 312 */     if (result == null) {
/* 313 */       result = (AuthConfigFactory.RegistrationContext)this.layerRegistrations.get(registrationID);
/*     */     }
/* 315 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public void refresh()
/*     */   {
/* 321 */     loadPersistentRegistrations();
/*     */   }
/*     */   
/*     */   private static String getRegistrationID(String layer, String appContext)
/*     */   {
/* 326 */     if ((layer != null) && (layer.length() == 0))
/*     */     {
/* 328 */       throw new IllegalArgumentException(sm.getString("authConfigFactoryImpl.zeroLengthMessageLayer"));
/*     */     }
/* 330 */     if ((appContext != null) && (appContext.length() == 0))
/*     */     {
/* 332 */       throw new IllegalArgumentException(sm.getString("authConfigFactoryImpl.zeroLengthAppContext"));
/*     */     }
/* 334 */     return (layer == null ? "" : layer) + ":" + (appContext == null ? "" : appContext);
/*     */   }
/*     */   
/*     */   private void loadPersistentRegistrations()
/*     */   {
/* 339 */     synchronized (CONFIG_FILE_LOCK) {
/* 340 */       if (log.isDebugEnabled()) {
/* 341 */         log.debug(sm.getString("authConfigFactoryImpl.load", new Object[] {CONFIG_FILE
/* 342 */           .getAbsolutePath() }));
/*     */       }
/* 344 */       if (!CONFIG_FILE.isFile()) {
/* 345 */         return;
/*     */       }
/* 347 */       PersistentProviderRegistrations.Providers providers = PersistentProviderRegistrations.loadProviders(CONFIG_FILE);
/* 348 */       for (PersistentProviderRegistrations.Provider provider : providers.getProviders()) {
/* 349 */         doRegisterConfigProvider(provider.getClassName(), provider.getProperties(), provider
/* 350 */           .getLayer(), provider.getAppContext(), provider.getDescription());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void savePersistentRegistrations()
/*     */   {
/* 357 */     synchronized (CONFIG_FILE_LOCK) {
/* 358 */       PersistentProviderRegistrations.Providers providers = new PersistentProviderRegistrations.Providers();
/* 359 */       savePersistentProviders(providers, this.layerAppContextRegistrations);
/* 360 */       savePersistentProviders(providers, this.appContextRegistrations);
/* 361 */       savePersistentProviders(providers, this.layerRegistrations);
/* 362 */       savePersistentProviders(providers, this.defaultRegistration);
/* 363 */       PersistentProviderRegistrations.writeProviders(providers, CONFIG_FILE);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void savePersistentProviders(PersistentProviderRegistrations.Providers providers, Map<String, RegistrationContextImpl> registrations)
/*     */   {
/* 370 */     for (Map.Entry<String, RegistrationContextImpl> entry : registrations.entrySet()) {
/* 371 */       savePersistentProvider(providers, (RegistrationContextImpl)entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void savePersistentProvider(PersistentProviderRegistrations.Providers providers, RegistrationContextImpl registrationContextImpl)
/*     */   {
/* 378 */     if ((registrationContextImpl != null) && (registrationContextImpl.isPersistent())) {
/* 379 */       PersistentProviderRegistrations.Provider provider = new PersistentProviderRegistrations.Provider();
/* 380 */       provider.setAppContext(registrationContextImpl.getAppContext());
/* 381 */       if (registrationContextImpl.getProvider() != null) {
/* 382 */         provider.setClassName(registrationContextImpl.getProvider().getClass().getName());
/*     */       }
/* 384 */       provider.setDescription(registrationContextImpl.getDescription());
/* 385 */       provider.setLayer(registrationContextImpl.getMessageLayer());
/* 386 */       for (Map.Entry<String, String> property : registrationContextImpl.getProperties().entrySet()) {
/* 387 */         provider.addProperty((String)property.getKey(), (String)property.getValue());
/*     */       }
/* 389 */       providers.addProvider(provider);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private RegistrationContextImpl findRegistrationContextImpl(String layer, String appContext)
/*     */   {
/* 396 */     RegistrationContextImpl result = (RegistrationContextImpl)this.layerAppContextRegistrations.get(getRegistrationID(layer, appContext));
/* 397 */     if (result == null) {
/* 398 */       result = (RegistrationContextImpl)this.appContextRegistrations.get(getRegistrationID(null, appContext));
/*     */     }
/* 400 */     if (result == null) {
/* 401 */       result = (RegistrationContextImpl)this.layerRegistrations.get(getRegistrationID(layer, null));
/*     */     }
/* 403 */     if (result == null) {
/* 404 */       result = (RegistrationContextImpl)this.defaultRegistration.get(DEFAULT_REGISTRATION_ID);
/*     */     }
/* 406 */     return result;
/*     */   }
/*     */   
/*     */   private static class RegistrationContextImpl implements AuthConfigFactory.RegistrationContext {
/*     */     private final String messageLayer;
/*     */     private final String appContext;
/*     */     private final String description;
/*     */     
/* 414 */     private RegistrationContextImpl(String messageLayer, String appContext, String description, boolean persistent, AuthConfigProvider provider, Map<String, String> properties) { this.messageLayer = messageLayer;
/* 415 */       this.appContext = appContext;
/* 416 */       this.description = description;
/* 417 */       this.persistent = persistent;
/* 418 */       this.provider = provider;
/* 419 */       Map<String, String> propertiesCopy = new HashMap();
/* 420 */       if (properties != null) {
/* 421 */         propertiesCopy.putAll(properties);
/*     */       }
/* 423 */       this.properties = Collections.unmodifiableMap(propertiesCopy);
/*     */     }
/*     */     
/*     */ 
/*     */     private final boolean persistent;
/*     */     
/*     */     private final AuthConfigProvider provider;
/*     */     
/*     */     private final Map<String, String> properties;
/* 432 */     private final List<AuthConfigFactoryImpl.RegistrationListenerWrapper> listeners = new CopyOnWriteArrayList();
/*     */     
/*     */     public String getMessageLayer()
/*     */     {
/* 436 */       return this.messageLayer;
/*     */     }
/*     */     
/*     */ 
/*     */     public String getAppContext()
/*     */     {
/* 442 */       return this.appContext;
/*     */     }
/*     */     
/*     */     public String getDescription()
/*     */     {
/* 447 */       return this.description;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean isPersistent()
/*     */     {
/* 453 */       return this.persistent;
/*     */     }
/*     */     
/*     */     private AuthConfigProvider getProvider()
/*     */     {
/* 458 */       return this.provider;
/*     */     }
/*     */     
/*     */     private void addListener(AuthConfigFactoryImpl.RegistrationListenerWrapper listener)
/*     */     {
/* 463 */       if (listener != null) {
/* 464 */         this.listeners.add(listener);
/*     */       }
/*     */     }
/*     */     
/*     */     private Map<String, String> getProperties()
/*     */     {
/* 470 */       return this.properties;
/*     */     }
/*     */     
/*     */     private boolean removeListener(RegistrationListener listener)
/*     */     {
/* 475 */       boolean result = false;
/* 476 */       for (AuthConfigFactoryImpl.RegistrationListenerWrapper wrapper : this.listeners) {
/* 477 */         if (wrapper.getListener().equals(listener)) {
/* 478 */           this.listeners.remove(wrapper);
/* 479 */           result = true;
/*     */         }
/*     */       }
/* 482 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class RegistrationListenerWrapper
/*     */   {
/*     */     private final String messageLayer;
/*     */     
/*     */     private final String appContext;
/*     */     private final RegistrationListener listener;
/*     */     
/*     */     public RegistrationListenerWrapper(String messageLayer, String appContext, RegistrationListener listener)
/*     */     {
/* 496 */       this.messageLayer = messageLayer;
/* 497 */       this.appContext = appContext;
/* 498 */       this.listener = listener;
/*     */     }
/*     */     
/*     */     public String getMessageLayer()
/*     */     {
/* 503 */       return this.messageLayer;
/*     */     }
/*     */     
/*     */     public String getAppContext()
/*     */     {
/* 508 */       return this.appContext;
/*     */     }
/*     */     
/*     */     public RegistrationListener getListener()
/*     */     {
/* 513 */       return this.listener;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\authenticator\jaspic\AuthConfigFactoryImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */