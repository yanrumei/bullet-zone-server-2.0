/*     */ package org.springframework.web.servlet.view.tiles3;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import javax.el.ArrayELResolver;
/*     */ import javax.el.BeanELResolver;
/*     */ import javax.el.CompositeELResolver;
/*     */ import javax.el.ListELResolver;
/*     */ import javax.el.MapELResolver;
/*     */ import javax.el.ResourceBundleELResolver;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.jsp.JspApplicationContext;
/*     */ import javax.servlet.jsp.JspFactory;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.tiles.TilesContainer;
/*     */ import org.apache.tiles.TilesException;
/*     */ import org.apache.tiles.definition.DefinitionsFactory;
/*     */ import org.apache.tiles.definition.DefinitionsReader;
/*     */ import org.apache.tiles.definition.dao.BaseLocaleUrlDefinitionDAO;
/*     */ import org.apache.tiles.definition.dao.CachingLocaleUrlDefinitionDAO;
/*     */ import org.apache.tiles.definition.digester.DigesterDefinitionsReader;
/*     */ import org.apache.tiles.el.ELAttributeEvaluator;
/*     */ import org.apache.tiles.el.ScopeELResolver;
/*     */ import org.apache.tiles.el.TilesContextBeanELResolver;
/*     */ import org.apache.tiles.el.TilesContextELResolver;
/*     */ import org.apache.tiles.evaluator.AttributeEvaluator;
/*     */ import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
/*     */ import org.apache.tiles.evaluator.BasicAttributeEvaluatorFactory;
/*     */ import org.apache.tiles.evaluator.impl.DirectAttributeEvaluator;
/*     */ import org.apache.tiles.extras.complete.CompleteAutoloadTilesContainerFactory;
/*     */ import org.apache.tiles.extras.complete.CompleteAutoloadTilesInitializer;
/*     */ import org.apache.tiles.factory.AbstractTilesContainerFactory;
/*     */ import org.apache.tiles.factory.BasicTilesContainerFactory;
/*     */ import org.apache.tiles.impl.mgmt.CachingTilesContainer;
/*     */ import org.apache.tiles.locale.LocaleResolver;
/*     */ import org.apache.tiles.preparer.factory.PreparerFactory;
/*     */ import org.apache.tiles.request.ApplicationContext;
/*     */ import org.apache.tiles.request.ApplicationContextAware;
/*     */ import org.apache.tiles.request.ApplicationResource;
/*     */ import org.apache.tiles.startup.DefaultTilesInitializer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TilesConfigurer
/*     */   implements ServletContextAware, InitializingBean, DisposableBean
/*     */ {
/* 128 */   private static final boolean tilesElPresent = ClassUtils.isPresent("org.apache.tiles.el.ELAttributeEvaluator", TilesConfigurer.class.getClassLoader());
/*     */   
/*     */ 
/* 131 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private TilesInitializer tilesInitializer;
/*     */   
/*     */   private String[] definitions;
/*     */   
/* 137 */   private boolean checkRefresh = false;
/*     */   
/* 139 */   private boolean validateDefinitions = true;
/*     */   
/*     */   private Class<? extends DefinitionsFactory> definitionsFactoryClass;
/*     */   
/*     */   private Class<? extends PreparerFactory> preparerFactoryClass;
/*     */   
/* 145 */   private boolean useMutableTilesContainer = false;
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
/* 159 */     this.tilesInitializer = tilesInitializer;
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
/* 173 */     if (completeAutoload) {
/*     */       try {
/* 175 */         this.tilesInitializer = new SpringCompleteAutoloadTilesInitializer(null);
/*     */       }
/*     */       catch (Throwable ex) {
/* 178 */         throw new IllegalStateException("Tiles-Extras 3.0 not available", ex);
/*     */       }
/*     */       
/*     */     } else {
/* 182 */       this.tilesInitializer = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDefinitions(String... definitions)
/*     */   {
/* 191 */     this.definitions = definitions;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCheckRefresh(boolean checkRefresh)
/*     */   {
/* 199 */     this.checkRefresh = checkRefresh;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setValidateDefinitions(boolean validateDefinitions)
/*     */   {
/* 206 */     this.validateDefinitions = validateDefinitions;
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
/* 219 */     this.definitionsFactoryClass = definitionsFactoryClass;
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
/* 242 */     this.preparerFactoryClass = preparerFactoryClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUseMutableTilesContainer(boolean useMutableTilesContainer)
/*     */   {
/* 252 */     this.useMutableTilesContainer = useMutableTilesContainer;
/*     */   }
/*     */   
/*     */   public void setServletContext(ServletContext servletContext)
/*     */   {
/* 257 */     this.servletContext = servletContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void afterPropertiesSet()
/*     */     throws TilesException
/*     */   {
/* 267 */     ApplicationContext preliminaryContext = new SpringWildcardServletTilesApplicationContext(this.servletContext);
/* 268 */     if (this.tilesInitializer == null) {
/* 269 */       this.tilesInitializer = new SpringTilesInitializer(null);
/*     */     }
/* 271 */     this.tilesInitializer.initialize(preliminaryContext);
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
/*     */   private class SpringTilesInitializer extends DefaultTilesInitializer
/*     */   {
/*     */     private SpringTilesInitializer() {}
/*     */     
/*     */     protected AbstractTilesContainerFactory createContainerFactory(ApplicationContext context) {
/* 288 */       return new TilesConfigurer.SpringTilesContainerFactory(TilesConfigurer.this, null);
/*     */     }
/*     */   }
/*     */   
/*     */   private class SpringTilesContainerFactory extends BasicTilesContainerFactory
/*     */   {
/*     */     private SpringTilesContainerFactory() {}
/*     */     
/*     */     protected TilesContainer createDecoratedContainer(TilesContainer originalContainer, ApplicationContext context) {
/* 297 */       return TilesConfigurer.this.useMutableTilesContainer ? new CachingTilesContainer(originalContainer) : originalContainer;
/*     */     }
/*     */     
/*     */     protected List<ApplicationResource> getSources(ApplicationContext applicationContext)
/*     */     {
/* 302 */       if (TilesConfigurer.this.definitions != null) {
/* 303 */         List<ApplicationResource> result = new LinkedList();
/* 304 */         for (String definition : TilesConfigurer.this.definitions) {
/* 305 */           Collection<ApplicationResource> resources = applicationContext.getResources(definition);
/* 306 */           if (resources != null) {
/* 307 */             result.addAll(resources);
/*     */           }
/*     */         }
/* 310 */         return result;
/*     */       }
/*     */       
/* 313 */       return super.getSources(applicationContext);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected BaseLocaleUrlDefinitionDAO instantiateLocaleDefinitionDao(ApplicationContext applicationContext, LocaleResolver resolver)
/*     */     {
/* 320 */       BaseLocaleUrlDefinitionDAO dao = super.instantiateLocaleDefinitionDao(applicationContext, resolver);
/* 321 */       if ((TilesConfigurer.this.checkRefresh) && ((dao instanceof CachingLocaleUrlDefinitionDAO))) {
/* 322 */         ((CachingLocaleUrlDefinitionDAO)dao).setCheckRefresh(true);
/*     */       }
/* 324 */       return dao;
/*     */     }
/*     */     
/*     */     protected DefinitionsReader createDefinitionsReader(ApplicationContext context)
/*     */     {
/* 329 */       DigesterDefinitionsReader reader = (DigesterDefinitionsReader)super.createDefinitionsReader(context);
/* 330 */       reader.setValidating(TilesConfigurer.this.validateDefinitions);
/* 331 */       return reader;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected DefinitionsFactory createDefinitionsFactory(ApplicationContext applicationContext, LocaleResolver resolver)
/*     */     {
/* 338 */       if (TilesConfigurer.this.definitionsFactoryClass != null) {
/* 339 */         DefinitionsFactory factory = (DefinitionsFactory)BeanUtils.instantiate(TilesConfigurer.this.definitionsFactoryClass);
/* 340 */         if ((factory instanceof ApplicationContextAware)) {
/* 341 */           ((ApplicationContextAware)factory).setApplicationContext(applicationContext);
/*     */         }
/* 343 */         BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(factory);
/* 344 */         if (bw.isWritableProperty("localeResolver")) {
/* 345 */           bw.setPropertyValue("localeResolver", resolver);
/*     */         }
/* 347 */         if (bw.isWritableProperty("definitionDAO")) {
/* 348 */           bw.setPropertyValue("definitionDAO", createLocaleDefinitionDao(applicationContext, resolver));
/*     */         }
/* 350 */         return factory;
/*     */       }
/*     */       
/* 353 */       return super.createDefinitionsFactory(applicationContext, resolver);
/*     */     }
/*     */     
/*     */ 
/*     */     protected PreparerFactory createPreparerFactory(ApplicationContext context)
/*     */     {
/* 359 */       if (TilesConfigurer.this.preparerFactoryClass != null) {
/* 360 */         return (PreparerFactory)BeanUtils.instantiate(TilesConfigurer.this.preparerFactoryClass);
/*     */       }
/*     */       
/* 363 */       return super.createPreparerFactory(context);
/*     */     }
/*     */     
/*     */ 
/*     */     protected LocaleResolver createLocaleResolver(ApplicationContext context)
/*     */     {
/* 369 */       return new SpringLocaleResolver();
/*     */     }
/*     */     
/*     */     protected AttributeEvaluatorFactory createAttributeEvaluatorFactory(ApplicationContext context, LocaleResolver resolver)
/*     */     {
/*     */       AttributeEvaluator evaluator;
/*     */       AttributeEvaluator evaluator;
/* 376 */       if ((TilesConfigurer.tilesElPresent) && (JspFactory.getDefaultFactory() != null)) {
/* 377 */         evaluator = new TilesConfigurer.TilesElActivator(TilesConfigurer.this, null).createEvaluator();
/*     */       }
/*     */       else {
/* 380 */         evaluator = new DirectAttributeEvaluator();
/*     */       }
/* 382 */       return new BasicAttributeEvaluatorFactory(evaluator);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SpringCompleteAutoloadTilesInitializer
/*     */     extends CompleteAutoloadTilesInitializer
/*     */   {
/*     */     protected AbstractTilesContainerFactory createContainerFactory(ApplicationContext context)
/*     */     {
/* 391 */       return new TilesConfigurer.SpringCompleteAutoloadTilesContainerFactory(null);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SpringCompleteAutoloadTilesContainerFactory
/*     */     extends CompleteAutoloadTilesContainerFactory
/*     */   {
/*     */     protected LocaleResolver createLocaleResolver(ApplicationContext applicationContext)
/*     */     {
/* 400 */       return new SpringLocaleResolver();
/*     */     }
/*     */   }
/*     */   
/*     */   private class TilesElActivator {
/*     */     private TilesElActivator() {}
/*     */     
/*     */     public AttributeEvaluator createEvaluator() {
/* 408 */       ELAttributeEvaluator evaluator = new ELAttributeEvaluator();
/* 409 */       evaluator.setExpressionFactory(
/* 410 */         JspFactory.getDefaultFactory().getJspApplicationContext(TilesConfigurer.this.servletContext).getExpressionFactory());
/* 411 */       evaluator.setResolver(new TilesConfigurer.CompositeELResolverImpl());
/* 412 */       return evaluator;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CompositeELResolverImpl extends CompositeELResolver
/*     */   {
/*     */     public CompositeELResolverImpl()
/*     */     {
/* 420 */       add(new ScopeELResolver());
/* 421 */       add(new TilesContextELResolver(new TilesContextBeanELResolver()));
/* 422 */       add(new TilesContextBeanELResolver());
/* 423 */       add(new ArrayELResolver(false));
/* 424 */       add(new ListELResolver(false));
/* 425 */       add(new MapELResolver(false));
/* 426 */       add(new ResourceBundleELResolver());
/* 427 */       add(new BeanELResolver(false));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\tiles3\TilesConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */