/*     */ package org.springframework.boot.autoconfigure.diagnostics.analyzer;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport.ConditionAndOutcome;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport.ConditionAndOutcomes;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
/*     */ import org.springframework.boot.diagnostics.FailureAnalysis;
/*     */ import org.springframework.boot.diagnostics.analyzer.AbstractInjectionFailureAnalyzer;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.MethodMetadata;
/*     */ import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
/*     */ import org.springframework.core.type.classreading.MetadataReader;
/*     */ import org.springframework.core.type.classreading.MetadataReaderFactory;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class NoSuchBeanDefinitionFailureAnalyzer
/*     */   extends AbstractInjectionFailureAnalyzer<NoSuchBeanDefinitionException>
/*     */   implements BeanFactoryAware
/*     */ {
/*     */   private ConfigurableListableBeanFactory beanFactory;
/*     */   private MetadataReaderFactory metadataReaderFactory;
/*     */   private ConditionEvaluationReport report;
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory)
/*     */     throws BeansException
/*     */   {
/*  65 */     Assert.isInstanceOf(ConfigurableListableBeanFactory.class, beanFactory);
/*  66 */     this.beanFactory = ((ConfigurableListableBeanFactory)beanFactory);
/*     */     
/*  68 */     this.metadataReaderFactory = new CachingMetadataReaderFactory(this.beanFactory.getBeanClassLoader());
/*     */     
/*  70 */     this.report = ConditionEvaluationReport.get(this.beanFactory);
/*     */   }
/*     */   
/*     */ 
/*     */   protected FailureAnalysis analyze(Throwable rootFailure, NoSuchBeanDefinitionException cause, String description)
/*     */   {
/*  76 */     if (cause.getNumberOfBeansFound() != 0) {
/*  77 */       return null;
/*     */     }
/*  79 */     List<AutoConfigurationResult> autoConfigurationResults = getAutoConfigurationResults(cause);
/*     */     
/*  81 */     StringBuilder message = new StringBuilder();
/*  82 */     message.append(String.format("%s required %s that could not be found.%n", new Object[] { description == null ? "A component" : description, 
/*     */     
/*  84 */       getBeanDescription(cause) }));
/*  85 */     if (!autoConfigurationResults.isEmpty()) {
/*  86 */       for (AutoConfigurationResult provider : autoConfigurationResults) {
/*  87 */         message.append(String.format("\t- %s%n", new Object[] { provider }));
/*     */       }
/*     */     }
/*  90 */     String action = String.format("Consider %s %s in your configuration.", new Object[] {
/*  91 */       !autoConfigurationResults.isEmpty() ? "revisiting the conditions above or defining" : "defining", 
/*     */       
/*  93 */       getBeanDescription(cause) });
/*  94 */     return new FailureAnalysis(message.toString(), action, cause);
/*     */   }
/*     */   
/*     */   private String getBeanDescription(NoSuchBeanDefinitionException cause) {
/*  98 */     if (cause.getResolvableType() != null) {
/*  99 */       Class<?> type = extractBeanType(cause.getResolvableType());
/* 100 */       return "a bean of type '" + type.getName() + "'";
/*     */     }
/* 102 */     return "a bean named '" + cause.getBeanName() + "'";
/*     */   }
/*     */   
/*     */   private Class<?> extractBeanType(ResolvableType resolvableType) {
/* 106 */     ResolvableType collectionType = resolvableType.asCollection();
/* 107 */     if (!collectionType.equals(ResolvableType.NONE)) {
/* 108 */       return collectionType.getGeneric(new int[] { 0 }).getRawClass();
/*     */     }
/* 110 */     ResolvableType mapType = resolvableType.asMap();
/* 111 */     if (!mapType.equals(ResolvableType.NONE)) {
/* 112 */       return mapType.getGeneric(new int[] { 1 }).getRawClass();
/*     */     }
/* 114 */     return resolvableType.getRawClass();
/*     */   }
/*     */   
/*     */   private List<AutoConfigurationResult> getAutoConfigurationResults(NoSuchBeanDefinitionException cause)
/*     */   {
/* 119 */     List<AutoConfigurationResult> results = new ArrayList();
/* 120 */     collectReportedConditionOutcomes(cause, results);
/* 121 */     collectExcludedAutoConfiguration(cause, results);
/* 122 */     return results;
/*     */   }
/*     */   
/*     */   private void collectReportedConditionOutcomes(NoSuchBeanDefinitionException cause, List<AutoConfigurationResult> results)
/*     */   {
/* 127 */     for (Map.Entry<String, ConditionEvaluationReport.ConditionAndOutcomes> entry : this.report
/* 128 */       .getConditionAndOutcomesBySource().entrySet()) {
/* 129 */       source = new Source((String)entry.getKey());
/* 130 */       ConditionEvaluationReport.ConditionAndOutcomes conditionAndOutcomes = (ConditionEvaluationReport.ConditionAndOutcomes)entry.getValue();
/* 131 */       if (!conditionAndOutcomes.isFullMatch()) {
/* 132 */         methods = new BeanMethods(source, cause);
/* 133 */         for (localIterator2 = conditionAndOutcomes.iterator(); localIterator2.hasNext();) { conditionAndOutcome = (ConditionEvaluationReport.ConditionAndOutcome)localIterator2.next();
/* 134 */           if (!conditionAndOutcome.getOutcome().isMatch())
/* 135 */             for (MethodMetadata method : methods)
/* 136 */               results.add(new AutoConfigurationResult(method, conditionAndOutcome
/* 137 */                 .getOutcome(), source.isMethod()));
/*     */         }
/*     */       }
/*     */     }
/*     */     Source source;
/*     */     BeanMethods methods;
/*     */     Iterator localIterator2;
/*     */     ConditionEvaluationReport.ConditionAndOutcome conditionAndOutcome;
/*     */   }
/*     */   
/* 147 */   private void collectExcludedAutoConfiguration(NoSuchBeanDefinitionException cause, List<AutoConfigurationResult> results) { for (Iterator localIterator1 = this.report.getExclusions().iterator(); localIterator1.hasNext();) { excludedClass = (String)localIterator1.next();
/* 148 */       Source source = new Source(excludedClass);
/* 149 */       BeanMethods methods = new BeanMethods(source, cause);
/* 150 */       for (MethodMetadata method : methods) {
/* 151 */         String message = String.format("auto-configuration '%s' was excluded", new Object[] {
/* 152 */           ClassUtils.getShortName(excludedClass) });
/* 153 */         results.add(new AutoConfigurationResult(method, new ConditionOutcome(false, message), false));
/*     */       }
/*     */     }
/*     */     String excludedClass;
/*     */   }
/*     */   
/*     */   private class Source
/*     */   {
/*     */     private final String className;
/*     */     private final String methodName;
/*     */     
/*     */     Source(String source)
/*     */     {
/* 166 */       String[] tokens = source.split("#");
/* 167 */       this.className = (tokens.length > 1 ? tokens[0] : source);
/* 168 */       this.methodName = (tokens.length == 2 ? tokens[1] : null);
/*     */     }
/*     */     
/*     */     public String getClassName() {
/* 172 */       return this.className;
/*     */     }
/*     */     
/*     */     public String getMethodName() {
/* 176 */       return this.methodName;
/*     */     }
/*     */     
/*     */     public boolean isMethod() {
/* 180 */       return this.methodName != null;
/*     */     }
/*     */   }
/*     */   
/*     */   private class BeanMethods implements Iterable<MethodMetadata>
/*     */   {
/*     */     private final List<MethodMetadata> methods;
/*     */     
/*     */     BeanMethods(NoSuchBeanDefinitionFailureAnalyzer.Source source, NoSuchBeanDefinitionException cause)
/*     */     {
/* 190 */       this.methods = findBeanMethods(source, cause);
/*     */     }
/*     */     
/*     */     private List<MethodMetadata> findBeanMethods(NoSuchBeanDefinitionFailureAnalyzer.Source source, NoSuchBeanDefinitionException cause)
/*     */     {
/*     */       try
/*     */       {
/* 197 */         MetadataReader classMetadata = NoSuchBeanDefinitionFailureAnalyzer.this.metadataReaderFactory.getMetadataReader(source.getClassName());
/*     */         
/* 199 */         Set<MethodMetadata> candidates = classMetadata.getAnnotationMetadata().getAnnotatedMethods(Bean.class.getName());
/* 200 */         List<MethodMetadata> result = new ArrayList();
/* 201 */         for (MethodMetadata candidate : candidates) {
/* 202 */           if (isMatch(candidate, source, cause)) {
/* 203 */             result.add(candidate);
/*     */           }
/*     */         }
/* 206 */         return Collections.unmodifiableList(result);
/*     */       }
/*     */       catch (Exception ex) {}
/* 209 */       return Collections.emptyList();
/*     */     }
/*     */     
/*     */ 
/*     */     private boolean isMatch(MethodMetadata candidate, NoSuchBeanDefinitionFailureAnalyzer.Source source, NoSuchBeanDefinitionException cause)
/*     */     {
/* 215 */       if ((source.getMethodName() != null) && 
/* 216 */         (!source.getMethodName().equals(candidate.getMethodName()))) {
/* 217 */         return false;
/*     */       }
/* 219 */       String name = cause.getBeanName();
/* 220 */       ResolvableType resolvableType = cause.getResolvableType();
/* 221 */       return ((name != null) && (hasName(candidate, name))) || ((resolvableType != null) && 
/* 222 */         (hasType(candidate, NoSuchBeanDefinitionFailureAnalyzer.this.extractBeanType(resolvableType))));
/*     */     }
/*     */     
/*     */     private boolean hasName(MethodMetadata methodMetadata, String name)
/*     */     {
/* 227 */       Map<String, Object> attributes = methodMetadata.getAnnotationAttributes(Bean.class.getName());
/*     */       
/* 229 */       String[] candidates = attributes == null ? null : (String[])attributes.get("name");
/* 230 */       if (candidates != null) {
/* 231 */         for (String candidate : candidates) {
/* 232 */           if (candidate.equals(name)) {
/* 233 */             return true;
/*     */           }
/*     */         }
/* 236 */         return false;
/*     */       }
/* 238 */       return methodMetadata.getMethodName().equals(name);
/*     */     }
/*     */     
/*     */     private boolean hasType(MethodMetadata candidate, Class<?> type) {
/* 242 */       String returnTypeName = candidate.getReturnTypeName();
/* 243 */       if (type.getName().equals(returnTypeName)) {
/* 244 */         return true;
/*     */       }
/*     */       try {
/* 247 */         Class<?> returnType = ClassUtils.forName(returnTypeName, 
/* 248 */           NoSuchBeanDefinitionFailureAnalyzer.this.beanFactory
/* 249 */           .getBeanClassLoader());
/* 250 */         return type.isAssignableFrom(returnType);
/*     */       }
/*     */       catch (Throwable ex) {}
/* 253 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public Iterator<MethodMetadata> iterator()
/*     */     {
/* 259 */       return this.methods.iterator();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private class AutoConfigurationResult
/*     */   {
/*     */     private final MethodMetadata methodMetadata;
/*     */     
/*     */     private final ConditionOutcome conditionOutcome;
/*     */     
/*     */     private final boolean methodEvaluated;
/*     */     
/*     */     AutoConfigurationResult(MethodMetadata methodMetadata, ConditionOutcome conditionOutcome, boolean methodEvaluated)
/*     */     {
/* 274 */       this.methodMetadata = methodMetadata;
/* 275 */       this.conditionOutcome = conditionOutcome;
/* 276 */       this.methodEvaluated = methodEvaluated;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 281 */       if (this.methodEvaluated) {
/* 282 */         return String.format("Bean method '%s' in '%s' not loaded because %s", new Object[] {this.methodMetadata
/* 283 */           .getMethodName(), 
/* 284 */           ClassUtils.getShortName(this.methodMetadata
/* 285 */           .getDeclaringClassName()), this.conditionOutcome
/* 286 */           .getMessage() });
/*     */       }
/* 288 */       return String.format("Bean method '%s' not loaded because %s", new Object[] {this.methodMetadata
/* 289 */         .getMethodName(), this.conditionOutcome
/* 290 */         .getMessage() });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\diagnostics\analyzer\NoSuchBeanDefinitionFailureAnalyzer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */