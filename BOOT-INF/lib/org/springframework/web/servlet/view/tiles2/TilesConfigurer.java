/*     */ package org.springframework.web.servlet.view.tiles2;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.jsp.JspFactory;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.tiles.TilesApplicationContext;
/*     */ import org.apache.tiles.TilesException;
/*     */ import org.apache.tiles.awareness.TilesApplicationContextAware;
/*     */ import org.apache.tiles.context.TilesRequestContextFactory;
/*     */ import org.apache.tiles.definition.DefinitionsFactory;
/*     */ import org.apache.tiles.definition.DefinitionsFactoryException;
/*     */ import org.apache.tiles.definition.DefinitionsReader;
/*     */ import org.apache.tiles.definition.Refreshable;
/*     */ import org.apache.tiles.definition.dao.BaseLocaleUrlDefinitionDAO;
/*     */ import org.apache.tiles.definition.dao.CachingLocaleUrlDefinitionDAO;
/*     */ import org.apache.tiles.definition.digester.DigesterDefinitionsReader;
/*     */ import org.apache.tiles.el.ELAttributeEvaluator;
/*     */ import org.apache.tiles.evaluator.AttributeEvaluator;
/*     */ import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
/*     */ import org.apache.tiles.evaluator.BasicAttributeEvaluatorFactory;
/*     */ import org.apache.tiles.evaluator.impl.DirectAttributeEvaluator;
/*     */ import org.apache.tiles.extras.complete.CompleteAutoloadTilesContainerFactory;
/*     */ import org.apache.tiles.extras.complete.CompleteAutoloadTilesInitializer;
/*     */ import org.apache.tiles.factory.AbstractTilesContainerFactory;
/*     */ import org.apache.tiles.factory.BasicTilesContainerFactory;
/*     */ import org.apache.tiles.impl.BasicTilesContainer;
/*     */ import org.apache.tiles.impl.mgmt.CachingTilesContainer;
/*     */ import org.apache.tiles.locale.LocaleResolver;
/*     */ import org.apache.tiles.preparer.PreparerFactory;
/*     */ import org.apache.tiles.startup.AbstractTilesInitializer;
/*     */ import org.apache.tiles.startup.TilesInitializer;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.BeanWrapper;
/*     */ import org.springframework.beans.PropertyAccessorFactory;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.web.context.ServletContextAware;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class TilesConfigurer
/*     */   implements ServletContextAware, InitializingBean, DisposableBean
/*     */ {
/* 118 */   private static final boolean tilesElPresent = ClassUtils.isPresent("org.apache.tiles.el.ELAttributeEvaluator", TilesConfigurer.class.getClassLoader());
/*     */   
/*     */ 
/* 121 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private TilesInitializer tilesInitializer;
/*     */   
/*     */   private String[] definitions;
/*     */   
/* 127 */   private boolean checkRefresh = false;
/*     */   
/* 129 */   private boolean validateDefinitions = true;
/*     */   
/*     */   private Class<? extends DefinitionsFactory> definitionsFactoryClass;
/*     */   
/*     */   private Class<? extends PreparerFactory> preparerFactoryClass;
/*     */   
/* 135 */   private boolean useMutableTilesContainer = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ServletContext servletContext;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTilesInitializer(TilesInitializer tilesInitializer)
/*     */   {
/* 149 */     this.tilesInitializer = tilesInitializer;
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
/*     */   public void setCompleteAutoload(boolean completeAutoload)
/*     */   {
/* 163 */     if (completeAutoload) {
/*     */       try {
/* 165 */         this.tilesInitializer = new SpringCompleteAutoloadTilesInitializer(null);
/*     */       }
/*     */       catch (Throwable ex) {
/* 168 */         throw new IllegalStateException("Tiles-Extras 2.2 not available", ex);
/*     */       }
/*     */       
/*     */     } else {
/* 172 */       this.tilesInitializer = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDefinitions(String... definitions)
/*     */   {
/* 181 */     this.definitions = definitions;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCheckRefresh(boolean checkRefresh)
/*     */   {
/* 189 */     this.checkRefresh = checkRefresh;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setValidateDefinitions(boolean validateDefinitions)
/*     */   {
/* 196 */     this.validateDefinitions = validateDefinitions;
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
/*     */   public void setDefinitionsFactoryClass(Class<? extends DefinitionsFactory> definitionsFactoryClass)
/*     */   {
/* 209 */     this.definitionsFactoryClass = definitionsFactoryClass;
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
/*     */   public void setPreparerFactoryClass(Class<? extends PreparerFactory> preparerFactoryClass)
/*     */   {
/* 232 */     this.preparerFactoryClass = preparerFactoryClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUseMutableTilesContainer(boolean useMutableTilesContainer)
/*     */   {
/* 242 */     this.useMutableTilesContainer = useMutableTilesContainer;
/*     */   }
/*     */   
/*     */   public void setServletContext(ServletContext servletContext)
/*     */   {
/* 247 */     this.servletContext = servletContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void afterPropertiesSet()
/*     */     throws TilesException
/*     */   {
/* 257 */     TilesApplicationContext preliminaryContext = new SpringWildcardServletTilesApplicationContext(this.servletContext);
/*     */     
/* 259 */     if (this.tilesInitializer == null) {
/* 260 */       this.tilesInitializer = createTilesInitializer();
/*     */     }
/* 262 */     this.tilesInitializer.initialize(preliminaryContext);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TilesInitializer createTilesInitializer()
/*     */   {
/* 271 */     return new SpringTilesInitializer(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void destroy()
/*     */     throws TilesException
/*     */   {
/* 280 */     this.tilesInitializer.destroy();
/*     */   }
/*     */   
/*     */   private class SpringTilesInitializer extends AbstractTilesInitializer
/*     */   {
/*     */     private SpringTilesInitializer() {}
/*     */     
/*     */     protected AbstractTilesContainerFactory createContainerFactory(TilesApplicationContext context) {
/* 288 */       return new TilesConfigurer.SpringTilesContainerFactory(TilesConfigurer.this, null);
/*     */     }
/*     */   }
/*     */   
/*     */   private class SpringTilesContainerFactory extends BasicTilesContainerFactory
/*     */   {
/*     */     private SpringTilesContainerFactory() {}
/*     */     
/*     */     protected BasicTilesContainer instantiateContainer(TilesApplicationContext context) {
/* 297 */       return TilesConfigurer.this.useMutableTilesContainer ? new CachingTilesContainer() : new BasicTilesContainer();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected void registerRequestContextFactory(String className, List<TilesRequestContextFactory> factories, TilesRequestContextFactory parent)
/*     */     {
/* 304 */       if (ClassUtils.isPresent(className, TilesConfigurer.class.getClassLoader())) {
/* 305 */         super.registerRequestContextFactory(className, factories, parent);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     protected List<URL> getSourceURLs(TilesApplicationContext applicationContext, TilesRequestContextFactory contextFactory)
/*     */     {
/* 312 */       if (TilesConfigurer.this.definitions != null) {
/*     */         try {
/* 314 */           List<URL> result = new LinkedList();
/* 315 */           for (String definition : TilesConfigurer.this.definitions) {
/* 316 */             Set<URL> resources = applicationContext.getResources(definition);
/* 317 */             if (resources != null) {
/* 318 */               result.addAll(resources);
/*     */             }
/*     */           }
/* 321 */           return result;
/*     */         }
/*     */         catch (IOException ex) {
/* 324 */           throw new DefinitionsFactoryException("Cannot load definition URLs", ex);
/*     */         }
/*     */       }
/*     */       
/* 328 */       return super.getSourceURLs(applicationContext, contextFactory);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected BaseLocaleUrlDefinitionDAO instantiateLocaleDefinitionDao(TilesApplicationContext applicationContext, TilesRequestContextFactory contextFactory, LocaleResolver resolver)
/*     */     {
/* 335 */       BaseLocaleUrlDefinitionDAO dao = super.instantiateLocaleDefinitionDao(applicationContext, contextFactory, resolver);
/*     */       
/* 337 */       if ((TilesConfigurer.this.checkRefresh) && ((dao instanceof CachingLocaleUrlDefinitionDAO))) {
/* 338 */         ((CachingLocaleUrlDefinitionDAO)dao).setCheckRefresh(true);
/*     */       }
/* 340 */       return dao;
/*     */     }
/*     */     
/*     */ 
/*     */     protected DefinitionsReader createDefinitionsReader(TilesApplicationContext applicationContext, TilesRequestContextFactory contextFactory)
/*     */     {
/* 346 */       DigesterDefinitionsReader reader = new DigesterDefinitionsReader();
/* 347 */       if (!TilesConfigurer.this.validateDefinitions) {
/* 348 */         Map<String, String> map = new HashMap();
/* 349 */         map.put("org.apache.tiles.definition.digester.DigesterDefinitionsReader.PARSER_VALIDATE", Boolean.FALSE.toString());
/* 350 */         reader.init(map);
/*     */       }
/* 352 */       return reader;
/*     */     }
/*     */     
/*     */ 
/*     */     protected DefinitionsFactory createDefinitionsFactory(TilesApplicationContext applicationContext, TilesRequestContextFactory contextFactory, LocaleResolver resolver)
/*     */     {
/* 358 */       if (TilesConfigurer.this.definitionsFactoryClass != null) {
/* 359 */         DefinitionsFactory factory = (DefinitionsFactory)BeanUtils.instantiate(TilesConfigurer.this.definitionsFactoryClass);
/* 360 */         if ((factory instanceof TilesApplicationContextAware)) {
/* 361 */           ((TilesApplicationContextAware)factory).setApplicationContext(applicationContext);
/*     */         }
/* 363 */         BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(factory);
/* 364 */         if (bw.isWritableProperty("localeResolver")) {
/* 365 */           bw.setPropertyValue("localeResolver", resolver);
/*     */         }
/* 367 */         if (bw.isWritableProperty("definitionDAO")) {
/* 368 */           bw.setPropertyValue("definitionDAO", 
/* 369 */             createLocaleDefinitionDao(applicationContext, contextFactory, resolver));
/*     */         }
/* 371 */         if ((factory instanceof Refreshable)) {
/* 372 */           ((Refreshable)factory).refresh();
/*     */         }
/* 374 */         return factory;
/*     */       }
/*     */       
/* 377 */       return super.createDefinitionsFactory(applicationContext, contextFactory, resolver);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected PreparerFactory createPreparerFactory(TilesApplicationContext applicationContext, TilesRequestContextFactory contextFactory)
/*     */     {
/* 384 */       if (TilesConfigurer.this.preparerFactoryClass != null) {
/* 385 */         return (PreparerFactory)BeanUtils.instantiate(TilesConfigurer.this.preparerFactoryClass);
/*     */       }
/*     */       
/* 388 */       return super.createPreparerFactory(applicationContext, contextFactory);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected LocaleResolver createLocaleResolver(TilesApplicationContext applicationContext, TilesRequestContextFactory contextFactory)
/*     */     {
/* 395 */       return new SpringLocaleResolver();
/*     */     }
/*     */     
/*     */     protected AttributeEvaluatorFactory createAttributeEvaluatorFactory(TilesApplicationContext applicationContext, TilesRequestContextFactory contextFactory, LocaleResolver resolver)
/*     */     {
/*     */       AttributeEvaluator evaluator;
/*     */       AttributeEvaluator evaluator;
/* 402 */       if ((TilesConfigurer.tilesElPresent) && (JspFactory.getDefaultFactory() != null)) {
/* 403 */         evaluator = TilesConfigurer.TilesElActivator.createEvaluator(applicationContext);
/*     */       }
/*     */       else {
/* 406 */         evaluator = new DirectAttributeEvaluator();
/*     */       }
/* 408 */       return new BasicAttributeEvaluatorFactory(evaluator);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SpringCompleteAutoloadTilesInitializer
/*     */     extends CompleteAutoloadTilesInitializer
/*     */   {
/*     */     protected AbstractTilesContainerFactory createContainerFactory(TilesApplicationContext context)
/*     */     {
/* 417 */       return new TilesConfigurer.SpringCompleteAutoloadTilesContainerFactory(null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class SpringCompleteAutoloadTilesContainerFactory
/*     */     extends CompleteAutoloadTilesContainerFactory
/*     */   {
/*     */     protected LocaleResolver createLocaleResolver(TilesApplicationContext applicationContext, TilesRequestContextFactory contextFactory)
/*     */     {
/* 427 */       return new SpringLocaleResolver();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TilesElActivator
/*     */   {
/*     */     public static AttributeEvaluator createEvaluator(TilesApplicationContext applicationContext)
/*     */     {
/* 435 */       ELAttributeEvaluator evaluator = new ELAttributeEvaluator();
/* 436 */       evaluator.setApplicationContext(applicationContext);
/* 437 */       evaluator.init(Collections.emptyMap());
/* 438 */       return evaluator;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\tiles2\TilesConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */