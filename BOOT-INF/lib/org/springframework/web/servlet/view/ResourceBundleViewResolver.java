/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.web.context.support.GenericWebApplicationContext;
/*     */ import org.springframework.web.servlet.View;
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
/*     */ public class ResourceBundleViewResolver
/*     */   extends AbstractCachingViewResolver
/*     */   implements Ordered, InitializingBean, DisposableBean
/*     */ {
/*     */   public static final String DEFAULT_BASENAME = "views";
/*  69 */   private int order = Integer.MAX_VALUE;
/*     */   
/*  71 */   private String[] basenames = { "views" };
/*     */   
/*  73 */   private ClassLoader bundleClassLoader = Thread.currentThread().getContextClassLoader();
/*     */   
/*     */ 
/*     */   private String defaultParentView;
/*     */   
/*     */   private Locale[] localesToInitialize;
/*     */   
/*  80 */   private final Map<Locale, BeanFactory> localeCache = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*  84 */   private final Map<List<ResourceBundle>, ConfigurableApplicationContext> bundleCache = new HashMap();
/*     */   
/*     */ 
/*     */   public void setOrder(int order)
/*     */   {
/*  89 */     this.order = order;
/*     */   }
/*     */   
/*     */   public int getOrder()
/*     */   {
/*  94 */     return this.order;
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
/*     */   public void setBasename(String basename)
/*     */   {
/* 111 */     setBasenames(new String[] { basename });
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
/*     */   public void setBasenames(String... basenames)
/*     */   {
/* 132 */     this.basenames = basenames;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBundleClassLoader(ClassLoader classLoader)
/*     */   {
/* 140 */     this.bundleClassLoader = classLoader;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ClassLoader getBundleClassLoader()
/*     */   {
/* 149 */     return this.bundleClassLoader;
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
/*     */   public void setDefaultParentView(String defaultParentView)
/*     */   {
/* 166 */     this.defaultParentView = defaultParentView;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLocalesToInitialize(Locale... localesToInitialize)
/*     */   {
/* 175 */     this.localesToInitialize = localesToInitialize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void afterPropertiesSet()
/*     */     throws BeansException
/*     */   {
/* 184 */     if (this.localesToInitialize != null) {
/* 185 */       for (Locale locale : this.localesToInitialize) {
/* 186 */         initFactory(locale);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected View loadView(String viewName, Locale locale)
/*     */     throws Exception
/*     */   {
/* 194 */     BeanFactory factory = initFactory(locale);
/*     */     try {
/* 196 */       return (View)factory.getBean(viewName, View.class);
/*     */     }
/*     */     catch (NoSuchBeanDefinitionException ex) {}
/*     */     
/* 200 */     return null;
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
/*     */   protected synchronized BeanFactory initFactory(Locale locale)
/*     */     throws BeansException
/*     */   {
/* 215 */     if (isCache()) {
/* 216 */       BeanFactory cachedFactory = (BeanFactory)this.localeCache.get(locale);
/* 217 */       if (cachedFactory != null) {
/* 218 */         return cachedFactory;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 223 */     List<ResourceBundle> bundles = new LinkedList();
/* 224 */     for (String basename : this.basenames) {
/* 225 */       ResourceBundle bundle = getBundle(basename, locale);
/* 226 */       bundles.add(bundle);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 231 */     if (isCache()) {
/* 232 */       BeanFactory cachedFactory = (BeanFactory)this.bundleCache.get(bundles);
/* 233 */       if (cachedFactory != null) {
/* 234 */         this.localeCache.put(locale, cachedFactory);
/* 235 */         return cachedFactory;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 240 */     GenericWebApplicationContext factory = new GenericWebApplicationContext();
/* 241 */     factory.setParent(getApplicationContext());
/* 242 */     factory.setServletContext(getServletContext());
/*     */     
/*     */ 
/* 245 */     PropertiesBeanDefinitionReader reader = new PropertiesBeanDefinitionReader(factory);
/* 246 */     reader.setDefaultParentBean(this.defaultParentView);
/* 247 */     for (ResourceBundle bundle : bundles) {
/* 248 */       reader.registerBeanDefinitions(bundle);
/*     */     }
/*     */     
/* 251 */     factory.refresh();
/*     */     
/*     */ 
/* 254 */     if (isCache()) {
/* 255 */       this.localeCache.put(locale, factory);
/* 256 */       this.bundleCache.put(bundles, factory);
/*     */     }
/*     */     
/* 259 */     return factory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ResourceBundle getBundle(String basename, Locale locale)
/*     */     throws MissingResourceException
/*     */   {
/* 271 */     return ResourceBundle.getBundle(basename, locale, getBundleClassLoader());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void destroy()
/*     */     throws BeansException
/*     */   {
/* 280 */     for (ConfigurableApplicationContext factory : this.bundleCache.values()) {
/* 281 */       factory.close();
/*     */     }
/* 283 */     this.localeCache.clear();
/* 284 */     this.bundleCache.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\ResourceBundleViewResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */