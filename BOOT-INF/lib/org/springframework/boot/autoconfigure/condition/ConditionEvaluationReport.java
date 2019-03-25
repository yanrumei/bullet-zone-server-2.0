/*     */ package org.springframework.boot.autoconfigure.condition;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.context.annotation.Condition;
/*     */ import org.springframework.context.annotation.ConditionContext;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ConditionEvaluationReport
/*     */ {
/*     */   private static final String BEAN_NAME = "autoConfigurationReport";
/*  52 */   private static final AncestorsMatchedCondition ANCESTOR_CONDITION = new AncestorsMatchedCondition(null);
/*     */   
/*  54 */   private final SortedMap<String, ConditionAndOutcomes> outcomes = new TreeMap();
/*     */   
/*     */   private boolean addedAncestorOutcomes;
/*     */   
/*     */   private ConditionEvaluationReport parent;
/*     */   
/*  60 */   private List<String> exclusions = Collections.emptyList();
/*     */   
/*  62 */   private Set<String> unconditionalClasses = new HashSet();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void recordConditionEvaluation(String source, Condition condition, ConditionOutcome outcome)
/*     */   {
/*  79 */     Assert.notNull(source, "Source must not be null");
/*  80 */     Assert.notNull(condition, "Condition must not be null");
/*  81 */     Assert.notNull(outcome, "Outcome must not be null");
/*  82 */     this.unconditionalClasses.remove(source);
/*  83 */     if (!this.outcomes.containsKey(source)) {
/*  84 */       this.outcomes.put(source, new ConditionAndOutcomes());
/*     */     }
/*  86 */     ((ConditionAndOutcomes)this.outcomes.get(source)).add(condition, outcome);
/*  87 */     this.addedAncestorOutcomes = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void recordExclusions(Collection<String> exclusions)
/*     */   {
/*  95 */     Assert.notNull(exclusions, "exclusions must not be null");
/*  96 */     this.exclusions = new ArrayList(exclusions);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void recordEvaluationCandidates(List<String> evaluationCandidates)
/*     */   {
/* 105 */     Assert.notNull(evaluationCandidates, "evaluationCandidates must not be null");
/* 106 */     this.unconditionalClasses = new HashSet(evaluationCandidates);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, ConditionAndOutcomes> getConditionAndOutcomesBySource()
/*     */   {
/* 114 */     if (!this.addedAncestorOutcomes) {
/* 115 */       for (Map.Entry<String, ConditionAndOutcomes> entry : this.outcomes
/* 116 */         .entrySet()) {
/* 117 */         if (!((ConditionAndOutcomes)entry.getValue()).isFullMatch()) {
/* 118 */           addNoMatchOutcomeToAncestors((String)entry.getKey());
/*     */         }
/*     */       }
/* 121 */       this.addedAncestorOutcomes = true;
/*     */     }
/* 123 */     return Collections.unmodifiableMap(this.outcomes);
/*     */   }
/*     */   
/*     */   private void addNoMatchOutcomeToAncestors(String source) {
/* 127 */     String prefix = source + "$";
/* 128 */     for (Map.Entry<String, ConditionAndOutcomes> entry : this.outcomes.entrySet()) {
/* 129 */       if (((String)entry.getKey()).startsWith(prefix)) {
/* 130 */         ConditionOutcome outcome = ConditionOutcome.noMatch(
/* 131 */           ConditionMessage.forCondition("Ancestor " + source, new Object[0]).because("did not match"));
/* 132 */         ((ConditionAndOutcomes)entry.getValue()).add(ANCESTOR_CONDITION, outcome);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> getExclusions()
/*     */   {
/* 142 */     return Collections.unmodifiableList(this.exclusions);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<String> getUnconditionalClasses()
/*     */   {
/* 150 */     return Collections.unmodifiableSet(this.unconditionalClasses);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConditionEvaluationReport getParent()
/*     */   {
/* 158 */     return this.parent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ConditionEvaluationReport get(ConfigurableListableBeanFactory beanFactory)
/*     */   {
/* 168 */     synchronized (beanFactory) { ConditionEvaluationReport report;
/*     */       ConditionEvaluationReport report;
/* 170 */       if (beanFactory.containsSingleton("autoConfigurationReport")) {
/* 171 */         report = (ConditionEvaluationReport)beanFactory.getBean("autoConfigurationReport", ConditionEvaluationReport.class);
/*     */       }
/*     */       else {
/* 174 */         report = new ConditionEvaluationReport();
/* 175 */         beanFactory.registerSingleton("autoConfigurationReport", report);
/*     */       }
/* 177 */       locateParent(beanFactory.getParentBeanFactory(), report);
/* 178 */       return report;
/*     */     }
/*     */   }
/*     */   
/*     */   private static void locateParent(BeanFactory beanFactory, ConditionEvaluationReport report)
/*     */   {
/* 184 */     if ((beanFactory != null) && (report.parent == null) && 
/* 185 */       (beanFactory.containsBean("autoConfigurationReport"))) {
/* 186 */       report.parent = ((ConditionEvaluationReport)beanFactory.getBean("autoConfigurationReport", ConditionEvaluationReport.class));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class ConditionAndOutcomes
/*     */     implements Iterable<ConditionEvaluationReport.ConditionAndOutcome>
/*     */   {
/* 196 */     private final Set<ConditionEvaluationReport.ConditionAndOutcome> outcomes = new LinkedHashSet();
/*     */     
/*     */     public void add(Condition condition, ConditionOutcome outcome) {
/* 199 */       this.outcomes.add(new ConditionEvaluationReport.ConditionAndOutcome(condition, outcome));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isFullMatch()
/*     */     {
/* 207 */       for (ConditionEvaluationReport.ConditionAndOutcome conditionAndOutcomes : this) {
/* 208 */         if (!conditionAndOutcomes.getOutcome().isMatch()) {
/* 209 */           return false;
/*     */         }
/*     */       }
/* 212 */       return true;
/*     */     }
/*     */     
/*     */     public Iterator<ConditionEvaluationReport.ConditionAndOutcome> iterator()
/*     */     {
/* 217 */       return Collections.unmodifiableSet(this.outcomes).iterator();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class ConditionAndOutcome
/*     */   {
/*     */     private final Condition condition;
/*     */     
/*     */     private final ConditionOutcome outcome;
/*     */     
/*     */ 
/*     */     public ConditionAndOutcome(Condition condition, ConditionOutcome outcome)
/*     */     {
/* 232 */       this.condition = condition;
/* 233 */       this.outcome = outcome;
/*     */     }
/*     */     
/*     */     public Condition getCondition() {
/* 237 */       return this.condition;
/*     */     }
/*     */     
/*     */     public ConditionOutcome getOutcome() {
/* 241 */       return this.outcome;
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 246 */       if (this == obj) {
/* 247 */         return true;
/*     */       }
/* 249 */       if ((obj == null) || (getClass() != obj.getClass())) {
/* 250 */         return false;
/*     */       }
/* 252 */       ConditionAndOutcome other = (ConditionAndOutcome)obj;
/* 253 */       if (ObjectUtils.nullSafeEquals(this.condition.getClass(), other.condition
/* 254 */         .getClass())) {}
/* 253 */       return 
/*     */       
/* 255 */         ObjectUtils.nullSafeEquals(this.outcome, other.outcome);
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 260 */       return this.condition.getClass().hashCode() * 31 + this.outcome.hashCode();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 265 */       return this.condition.getClass() + " " + this.outcome;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class AncestorsMatchedCondition
/*     */     implements Condition
/*     */   {
/*     */     public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata)
/*     */     {
/* 274 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\condition\ConditionEvaluationReport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */