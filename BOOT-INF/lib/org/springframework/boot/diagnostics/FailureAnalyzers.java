/*     */ package org.springframework.boot.diagnostics;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.core.io.support.SpringFactoriesLoader;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FailureAnalyzers
/*     */ {
/*  51 */   private static final Log logger = LogFactory.getLog(FailureAnalyzers.class);
/*     */   
/*     */ 
/*     */   private final ClassLoader classLoader;
/*     */   
/*     */ 
/*     */   private final List<FailureAnalyzer> analyzers;
/*     */   
/*     */ 
/*     */ 
/*     */   public FailureAnalyzers(ConfigurableApplicationContext context)
/*     */   {
/*  63 */     this(context, null);
/*     */   }
/*     */   
/*     */   FailureAnalyzers(ConfigurableApplicationContext context, ClassLoader classLoader) {
/*  67 */     Assert.notNull(context, "Context must not be null");
/*  68 */     this.classLoader = (classLoader == null ? context.getClassLoader() : classLoader);
/*  69 */     this.analyzers = loadFailureAnalyzers(this.classLoader);
/*  70 */     prepareFailureAnalyzers(this.analyzers, context);
/*     */   }
/*     */   
/*     */   private List<FailureAnalyzer> loadFailureAnalyzers(ClassLoader classLoader)
/*     */   {
/*  75 */     List<String> analyzerNames = SpringFactoriesLoader.loadFactoryNames(FailureAnalyzer.class, classLoader);
/*  76 */     List<FailureAnalyzer> analyzers = new ArrayList();
/*  77 */     for (String analyzerName : analyzerNames) {
/*     */       try
/*     */       {
/*  80 */         Constructor<?> constructor = ClassUtils.forName(analyzerName, classLoader).getDeclaredConstructor(new Class[0]);
/*  81 */         ReflectionUtils.makeAccessible(constructor);
/*  82 */         analyzers.add((FailureAnalyzer)constructor.newInstance(new Object[0]));
/*     */       }
/*     */       catch (Throwable ex) {
/*  85 */         logger.trace("Failed to load " + analyzerName, ex);
/*     */       }
/*     */     }
/*  88 */     AnnotationAwareOrderComparator.sort(analyzers);
/*  89 */     return analyzers;
/*     */   }
/*     */   
/*     */   private void prepareFailureAnalyzers(List<FailureAnalyzer> analyzers, ConfigurableApplicationContext context)
/*     */   {
/*  94 */     for (FailureAnalyzer analyzer : analyzers) {
/*  95 */       prepareAnalyzer(context, analyzer);
/*     */     }
/*     */   }
/*     */   
/*     */   private void prepareAnalyzer(ConfigurableApplicationContext context, FailureAnalyzer analyzer)
/*     */   {
/* 101 */     if ((analyzer instanceof BeanFactoryAware)) {
/* 102 */       ((BeanFactoryAware)analyzer).setBeanFactory(context.getBeanFactory());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean analyzeAndReport(Throwable failure)
/*     */   {
/* 112 */     FailureAnalysis analysis = analyze(failure, this.analyzers);
/* 113 */     return report(analysis, this.classLoader);
/*     */   }
/*     */   
/*     */   private FailureAnalysis analyze(Throwable failure, List<FailureAnalyzer> analyzers) {
/* 117 */     for (FailureAnalyzer analyzer : analyzers) {
/*     */       try {
/* 119 */         FailureAnalysis analysis = analyzer.analyze(failure);
/* 120 */         if (analysis != null) {
/* 121 */           return analysis;
/*     */         }
/*     */       }
/*     */       catch (Throwable ex) {
/* 125 */         logger.debug("FailureAnalyzer " + analyzer + " failed", ex);
/*     */       }
/*     */     }
/* 128 */     return null;
/*     */   }
/*     */   
/*     */   private boolean report(FailureAnalysis analysis, ClassLoader classLoader)
/*     */   {
/* 133 */     List<FailureAnalysisReporter> reporters = SpringFactoriesLoader.loadFactories(FailureAnalysisReporter.class, classLoader);
/* 134 */     if ((analysis == null) || (reporters.isEmpty())) {
/* 135 */       return false;
/*     */     }
/* 137 */     for (FailureAnalysisReporter reporter : reporters) {
/* 138 */       reporter.report(analysis);
/*     */     }
/* 140 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\diagnostics\FailureAnalyzers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */