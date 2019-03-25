/*     */ package org.springframework.boot.autoconfigure.condition;
/*     */ 
/*     */ import java.security.AccessControlException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
/*     */ import org.springframework.context.annotation.ConditionContext;
/*     */ import org.springframework.core.annotation.Order;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Order(Integer.MIN_VALUE)
/*     */ class OnClassCondition
/*     */   extends SpringBootCondition
/*     */   implements AutoConfigurationImportFilter, BeanFactoryAware, BeanClassLoaderAware
/*     */ {
/*     */   private BeanFactory beanFactory;
/*     */   private ClassLoader beanClassLoader;
/*     */   
/*     */   public boolean[] match(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata)
/*     */   {
/*  62 */     ConditionEvaluationReport report = getConditionEvaluationReport();
/*  63 */     ConditionOutcome[] outcomes = getOutcomes(autoConfigurationClasses, autoConfigurationMetadata);
/*     */     
/*  65 */     boolean[] match = new boolean[outcomes.length];
/*  66 */     for (int i = 0; i < outcomes.length; i++) {
/*  67 */       match[i] = ((outcomes[i] == null) || (outcomes[i].isMatch()) ? 1 : false);
/*  68 */       if ((match[i] == 0) && (outcomes[i] != null)) {
/*  69 */         logOutcome(autoConfigurationClasses[i], outcomes[i]);
/*  70 */         if (report != null) {
/*  71 */           report.recordConditionEvaluation(autoConfigurationClasses[i], this, outcomes[i]);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  76 */     return match;
/*     */   }
/*     */   
/*     */   private ConditionEvaluationReport getConditionEvaluationReport() {
/*  80 */     if ((this.beanFactory != null) && ((this.beanFactory instanceof ConfigurableBeanFactory)))
/*     */     {
/*  82 */       return 
/*  83 */         ConditionEvaluationReport.get((ConfigurableListableBeanFactory)this.beanFactory);
/*     */     }
/*  85 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ConditionOutcome[] getOutcomes(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata)
/*     */   {
/*  93 */     int split = autoConfigurationClasses.length / 2;
/*  94 */     OutcomesResolver firstHalfResolver = createOutcomesResolver(autoConfigurationClasses, 0, split, autoConfigurationMetadata);
/*     */     
/*  96 */     OutcomesResolver secondHalfResolver = new StandardOutcomesResolver(autoConfigurationClasses, split, autoConfigurationClasses.length, autoConfigurationMetadata, this.beanClassLoader, null);
/*     */     
/*     */ 
/*  99 */     ConditionOutcome[] secondHalf = secondHalfResolver.resolveOutcomes();
/* 100 */     ConditionOutcome[] firstHalf = firstHalfResolver.resolveOutcomes();
/* 101 */     ConditionOutcome[] outcomes = new ConditionOutcome[autoConfigurationClasses.length];
/* 102 */     System.arraycopy(firstHalf, 0, outcomes, 0, firstHalf.length);
/* 103 */     System.arraycopy(secondHalf, 0, outcomes, split, secondHalf.length);
/* 104 */     return outcomes;
/*     */   }
/*     */   
/*     */   private OutcomesResolver createOutcomesResolver(String[] autoConfigurationClasses, int start, int end, AutoConfigurationMetadata autoConfigurationMetadata)
/*     */   {
/* 109 */     OutcomesResolver outcomesResolver = new StandardOutcomesResolver(autoConfigurationClasses, start, end, autoConfigurationMetadata, this.beanClassLoader, null);
/*     */     
/*     */     try
/*     */     {
/* 113 */       return new ThreadedOutcomesResolver(outcomesResolver, null);
/*     */     }
/*     */     catch (AccessControlException ex) {}
/* 116 */     return outcomesResolver;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*     */   {
/* 123 */     ClassLoader classLoader = context.getClassLoader();
/* 124 */     ConditionMessage matchMessage = ConditionMessage.empty();
/* 125 */     List<String> onClasses = getCandidates(metadata, ConditionalOnClass.class);
/* 126 */     if (onClasses != null) {
/* 127 */       List<String> missing = getMatches(onClasses, MatchType.MISSING, classLoader);
/* 128 */       if (!missing.isEmpty()) {
/* 129 */         return 
/* 130 */           ConditionOutcome.noMatch(ConditionMessage.forCondition(ConditionalOnClass.class, new Object[0])
/* 131 */           .didNotFind("required class", "required classes")
/* 132 */           .items(ConditionMessage.Style.QUOTE, missing));
/*     */       }
/*     */       
/* 135 */       matchMessage = matchMessage.andCondition(ConditionalOnClass.class, new Object[0]).found("required class", "required classes").items(ConditionMessage.Style.QUOTE, 
/* 136 */         getMatches(onClasses, MatchType.PRESENT, classLoader));
/*     */     }
/* 138 */     List<String> onMissingClasses = getCandidates(metadata, ConditionalOnMissingClass.class);
/*     */     
/* 140 */     if (onMissingClasses != null) {
/* 141 */       List<String> present = getMatches(onMissingClasses, MatchType.PRESENT, classLoader);
/*     */       
/* 143 */       if (!present.isEmpty()) {
/* 144 */         return ConditionOutcome.noMatch(
/* 145 */           ConditionMessage.forCondition(ConditionalOnMissingClass.class, new Object[0])
/* 146 */           .found("unwanted class", "unwanted classes")
/* 147 */           .items(ConditionMessage.Style.QUOTE, present));
/*     */       }
/*     */       
/* 150 */       matchMessage = matchMessage.andCondition(ConditionalOnMissingClass.class, new Object[0]).didNotFind("unwanted class", "unwanted classes").items(ConditionMessage.Style.QUOTE, 
/* 151 */         getMatches(onMissingClasses, MatchType.MISSING, classLoader));
/*     */     }
/* 153 */     return ConditionOutcome.match(matchMessage);
/*     */   }
/*     */   
/*     */ 
/*     */   private List<String> getCandidates(AnnotatedTypeMetadata metadata, Class<?> annotationType)
/*     */   {
/* 159 */     MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(annotationType.getName(), true);
/* 160 */     List<String> candidates = new ArrayList();
/* 161 */     if (attributes == null) {
/* 162 */       return Collections.emptyList();
/*     */     }
/* 164 */     addAll(candidates, (List)attributes.get("value"));
/* 165 */     addAll(candidates, (List)attributes.get("name"));
/* 166 */     return candidates;
/*     */   }
/*     */   
/*     */   private void addAll(List<String> list, List<Object> itemsToAdd) {
/* 170 */     if (itemsToAdd != null) {
/* 171 */       for (Object item : itemsToAdd) {
/* 172 */         Collections.addAll(list, (String[])item);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private List<String> getMatches(Collection<String> candidates, MatchType matchType, ClassLoader classLoader)
/*     */   {
/* 179 */     List<String> matches = new ArrayList(candidates.size());
/* 180 */     for (String candidate : candidates) {
/* 181 */       if (matchType.matches(candidate, classLoader)) {
/* 182 */         matches.add(candidate);
/*     */       }
/*     */     }
/* 185 */     return matches;
/*     */   }
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) throws BeansException
/*     */   {
/* 190 */     this.beanFactory = beanFactory;
/*     */   }
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader)
/*     */   {
/* 195 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */   
/*     */   private static abstract enum MatchType
/*     */   {
/* 200 */     PRESENT, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 209 */     MISSING;
/*     */     
/*     */ 
/*     */ 
/*     */     private MatchType() {}
/*     */     
/*     */ 
/*     */ 
/*     */     private static boolean isPresent(String className, ClassLoader classLoader)
/*     */     {
/* 219 */       if (classLoader == null) {
/* 220 */         classLoader = ClassUtils.getDefaultClassLoader();
/*     */       }
/*     */       try {
/* 223 */         forName(className, classLoader);
/* 224 */         return true;
/*     */       }
/*     */       catch (Throwable ex) {}
/* 227 */       return false;
/*     */     }
/*     */     
/*     */     private static Class<?> forName(String className, ClassLoader classLoader)
/*     */       throws ClassNotFoundException
/*     */     {
/* 233 */       if (classLoader != null) {
/* 234 */         return classLoader.loadClass(className);
/*     */       }
/* 236 */       return Class.forName(className);
/*     */     }
/*     */     
/*     */ 
/*     */     public abstract boolean matches(String paramString, ClassLoader paramClassLoader);
/*     */   }
/*     */   
/*     */   private static abstract interface OutcomesResolver
/*     */   {
/*     */     public abstract ConditionOutcome[] resolveOutcomes();
/*     */   }
/*     */   
/*     */   private static final class ThreadedOutcomesResolver
/*     */     implements OnClassCondition.OutcomesResolver
/*     */   {
/*     */     private final Thread thread;
/*     */     private volatile ConditionOutcome[] outcomes;
/*     */     
/*     */     private ThreadedOutcomesResolver(final OnClassCondition.OutcomesResolver outcomesResolver)
/*     */     {
/* 256 */       this.thread = new Thread(new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/* 260 */           OnClassCondition.ThreadedOutcomesResolver.this.outcomes = outcomesResolver
/* 261 */             .resolveOutcomes();
/*     */         }
/*     */         
/* 264 */       });
/* 265 */       this.thread.start();
/*     */     }
/*     */     
/*     */     public ConditionOutcome[] resolveOutcomes()
/*     */     {
/*     */       try {
/* 271 */         this.thread.join();
/*     */       }
/*     */       catch (InterruptedException ex) {
/* 274 */         Thread.currentThread().interrupt();
/*     */       }
/* 276 */       return this.outcomes;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private final class StandardOutcomesResolver
/*     */     implements OnClassCondition.OutcomesResolver
/*     */   {
/*     */     private final String[] autoConfigurationClasses;
/*     */     
/*     */     private final int start;
/*     */     
/*     */     private final int end;
/*     */     
/*     */     private final AutoConfigurationMetadata autoConfigurationMetadata;
/*     */     
/*     */     private final ClassLoader beanClassLoader;
/*     */     
/*     */     private StandardOutcomesResolver(String[] autoConfigurationClasses, int start, int end, AutoConfigurationMetadata autoConfigurationMetadata, ClassLoader beanClassLoader)
/*     */     {
/* 296 */       this.autoConfigurationClasses = autoConfigurationClasses;
/* 297 */       this.start = start;
/* 298 */       this.end = end;
/* 299 */       this.autoConfigurationMetadata = autoConfigurationMetadata;
/* 300 */       this.beanClassLoader = beanClassLoader;
/*     */     }
/*     */     
/*     */     public ConditionOutcome[] resolveOutcomes()
/*     */     {
/* 305 */       return getOutcomes(this.autoConfigurationClasses, this.start, this.end, this.autoConfigurationMetadata);
/*     */     }
/*     */     
/*     */ 
/*     */     private ConditionOutcome[] getOutcomes(String[] autoConfigurationClasses, int start, int end, AutoConfigurationMetadata autoConfigurationMetadata)
/*     */     {
/* 311 */       ConditionOutcome[] outcomes = new ConditionOutcome[end - start];
/* 312 */       for (int i = start; i < end; i++) {
/* 313 */         String autoConfigurationClass = autoConfigurationClasses[i];
/*     */         
/* 315 */         Set<String> candidates = autoConfigurationMetadata.getSet(autoConfigurationClass, "ConditionalOnClass");
/* 316 */         if (candidates != null) {
/* 317 */           outcomes[(i - start)] = getOutcome(candidates);
/*     */         }
/*     */       }
/* 320 */       return outcomes;
/*     */     }
/*     */     
/*     */     private ConditionOutcome getOutcome(Set<String> candidates) {
/*     */       try {
/* 325 */         List<String> missing = OnClassCondition.this.getMatches(candidates, OnClassCondition.MatchType.MISSING, this.beanClassLoader);
/*     */         
/* 327 */         if (!missing.isEmpty()) {
/* 328 */           return ConditionOutcome.noMatch(
/* 329 */             ConditionMessage.forCondition(ConditionalOnClass.class, new Object[0])
/* 330 */             .didNotFind("required class", "required classes")
/* 331 */             .items(ConditionMessage.Style.QUOTE, missing));
/*     */         }
/*     */       }
/*     */       catch (Exception localException) {}
/*     */       
/*     */ 
/* 337 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\condition\OnClassCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */