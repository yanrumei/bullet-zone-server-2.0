/*     */ package org.springframework.boot.autoconfigure.condition;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.context.annotation.Condition;
/*     */ import org.springframework.context.annotation.ConditionContext;
/*     */ import org.springframework.context.annotation.Conditional;
/*     */ import org.springframework.context.annotation.ConfigurationCondition;
/*     */ import org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.classreading.MetadataReader;
/*     */ import org.springframework.core.type.classreading.MetadataReaderFactory;
/*     */ import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
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
/*     */ abstract class AbstractNestedCondition
/*     */   extends SpringBootCondition
/*     */   implements ConfigurationCondition
/*     */ {
/*     */   private final ConfigurationCondition.ConfigurationPhase configurationPhase;
/*     */   
/*     */   AbstractNestedCondition(ConfigurationCondition.ConfigurationPhase configurationPhase)
/*     */   {
/*  50 */     Assert.notNull(configurationPhase, "ConfigurationPhase must not be null");
/*  51 */     this.configurationPhase = configurationPhase;
/*     */   }
/*     */   
/*     */   public ConfigurationCondition.ConfigurationPhase getConfigurationPhase()
/*     */   {
/*  56 */     return this.configurationPhase;
/*     */   }
/*     */   
/*     */ 
/*     */   public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*     */   {
/*  62 */     String className = getClass().getName();
/*  63 */     MemberConditions memberConditions = new MemberConditions(context, className);
/*  64 */     MemberMatchOutcomes memberOutcomes = new MemberMatchOutcomes(memberConditions);
/*  65 */     return getFinalMatchOutcome(memberOutcomes);
/*     */   }
/*     */   
/*     */ 
/*     */   protected abstract ConditionOutcome getFinalMatchOutcome(MemberMatchOutcomes paramMemberMatchOutcomes);
/*     */   
/*     */ 
/*     */   protected static class MemberMatchOutcomes
/*     */   {
/*     */     private final List<ConditionOutcome> all;
/*     */     private final List<ConditionOutcome> matches;
/*     */     private final List<ConditionOutcome> nonMatches;
/*     */     
/*     */     public MemberMatchOutcomes(AbstractNestedCondition.MemberConditions memberConditions)
/*     */     {
/*  80 */       this.all = Collections.unmodifiableList(memberConditions.getMatchOutcomes());
/*  81 */       List<ConditionOutcome> matches = new ArrayList();
/*  82 */       List<ConditionOutcome> nonMatches = new ArrayList();
/*  83 */       for (ConditionOutcome outcome : this.all) {
/*  84 */         (outcome.isMatch() ? matches : nonMatches).add(outcome);
/*     */       }
/*  86 */       this.matches = Collections.unmodifiableList(matches);
/*  87 */       this.nonMatches = Collections.unmodifiableList(nonMatches);
/*     */     }
/*     */     
/*     */     public List<ConditionOutcome> getAll() {
/*  91 */       return this.all;
/*     */     }
/*     */     
/*     */     public List<ConditionOutcome> getMatches() {
/*  95 */       return this.matches;
/*     */     }
/*     */     
/*     */     public List<ConditionOutcome> getNonMatches() {
/*  99 */       return this.nonMatches;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class MemberConditions
/*     */   {
/*     */     private final ConditionContext context;
/*     */     
/*     */     private final MetadataReaderFactory readerFactory;
/*     */     private final Map<AnnotationMetadata, List<Condition>> memberConditions;
/*     */     
/*     */     MemberConditions(ConditionContext context, String className)
/*     */     {
/* 113 */       this.context = context;
/*     */       
/* 115 */       this.readerFactory = new SimpleMetadataReaderFactory(context.getResourceLoader());
/* 116 */       String[] members = getMetadata(className).getMemberClassNames();
/* 117 */       this.memberConditions = getMemberConditions(members);
/*     */     }
/*     */     
/*     */     private Map<AnnotationMetadata, List<Condition>> getMemberConditions(String[] members)
/*     */     {
/* 122 */       MultiValueMap<AnnotationMetadata, Condition> memberConditions = new LinkedMultiValueMap();
/* 123 */       AnnotationMetadata metadata; for (String member : members) {
/* 124 */         metadata = getMetadata(member);
/* 125 */         for (String[] conditionClasses : getConditionClasses(metadata)) {
/* 126 */           for (String conditionClass : conditionClasses) {
/* 127 */             Condition condition = getCondition(conditionClass);
/* 128 */             memberConditions.add(metadata, condition);
/*     */           }
/*     */         }
/*     */       }
/* 132 */       return Collections.unmodifiableMap(memberConditions);
/*     */     }
/*     */     
/*     */     private AnnotationMetadata getMetadata(String className) {
/*     */       try {
/* 137 */         return 
/* 138 */           this.readerFactory.getMetadataReader(className).getAnnotationMetadata();
/*     */       }
/*     */       catch (IOException ex) {
/* 141 */         throw new IllegalStateException(ex);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     private List<String[]> getConditionClasses(AnnotatedTypeMetadata metadata)
/*     */     {
/* 148 */       MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(Conditional.class.getName(), true);
/* 149 */       Object values = attributes != null ? (List)attributes.get("value") : null;
/* 150 */       return (List)(values != null ? values : Collections.emptyList());
/*     */     }
/*     */     
/*     */     private Condition getCondition(String conditionClassName) {
/* 154 */       Class<?> conditionClass = ClassUtils.resolveClassName(conditionClassName, this.context
/* 155 */         .getClassLoader());
/* 156 */       return (Condition)BeanUtils.instantiateClass(conditionClass);
/*     */     }
/*     */     
/*     */     public List<ConditionOutcome> getMatchOutcomes() {
/* 160 */       List<ConditionOutcome> outcomes = new ArrayList();
/* 161 */       for (Map.Entry<AnnotationMetadata, List<Condition>> entry : this.memberConditions
/* 162 */         .entrySet()) {
/* 163 */         AnnotationMetadata metadata = (AnnotationMetadata)entry.getKey();
/* 164 */         List<Condition> conditions = (List)entry.getValue();
/* 165 */         outcomes.add(new AbstractNestedCondition.MemberOutcomes(this.context, metadata, conditions)
/* 166 */           .getUltimateOutcome());
/*     */       }
/* 168 */       return Collections.unmodifiableList(outcomes);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class MemberOutcomes
/*     */   {
/*     */     private final ConditionContext context;
/*     */     
/*     */     private final AnnotationMetadata metadata;
/*     */     
/*     */     private final List<ConditionOutcome> outcomes;
/*     */     
/*     */     MemberOutcomes(ConditionContext context, AnnotationMetadata metadata, List<Condition> conditions)
/*     */     {
/* 183 */       this.context = context;
/* 184 */       this.metadata = metadata;
/* 185 */       this.outcomes = new ArrayList(conditions.size());
/* 186 */       for (Condition condition : conditions) {
/* 187 */         this.outcomes.add(getConditionOutcome(metadata, condition));
/*     */       }
/*     */     }
/*     */     
/*     */     private ConditionOutcome getConditionOutcome(AnnotationMetadata metadata, Condition condition)
/*     */     {
/* 193 */       if ((condition instanceof SpringBootCondition)) {
/* 194 */         return ((SpringBootCondition)condition).getMatchOutcome(this.context, metadata);
/*     */       }
/*     */       
/* 197 */       return new ConditionOutcome(condition.matches(this.context, metadata), 
/* 198 */         ConditionMessage.empty());
/*     */     }
/*     */     
/*     */     public ConditionOutcome getUltimateOutcome()
/*     */     {
/* 203 */       ConditionMessage.Builder message = ConditionMessage.forCondition("NestedCondition on " + 
/* 204 */         ClassUtils.getShortName(this.metadata.getClassName()), new Object[0]);
/* 205 */       if (this.outcomes.size() == 1) {
/* 206 */         ConditionOutcome outcome = (ConditionOutcome)this.outcomes.get(0);
/* 207 */         return new ConditionOutcome(outcome.isMatch(), message
/* 208 */           .because(outcome.getMessage()));
/*     */       }
/* 210 */       List<ConditionOutcome> match = new ArrayList();
/* 211 */       List<ConditionOutcome> nonMatch = new ArrayList();
/* 212 */       for (ConditionOutcome outcome : this.outcomes) {
/* 213 */         (outcome.isMatch() ? match : nonMatch).add(outcome);
/*     */       }
/* 215 */       if (nonMatch.isEmpty()) {
/* 216 */         return 
/* 217 */           ConditionOutcome.match(message.found("matching nested conditions").items(match));
/*     */       }
/* 219 */       return ConditionOutcome.noMatch(message
/* 220 */         .found("non-matching nested conditions").items(nonMatch));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\condition\AbstractNestedCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */