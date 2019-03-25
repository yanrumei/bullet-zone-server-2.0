/*     */ package org.springframework.boot.autoconfigure.condition;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.context.annotation.Condition;
/*     */ import org.springframework.context.annotation.ConditionContext;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.ClassMetadata;
/*     */ import org.springframework.core.type.MethodMetadata;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SpringBootCondition
/*     */   implements Condition
/*     */ {
/*  40 */   private final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */ 
/*     */   public final boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata)
/*     */   {
/*  45 */     String classOrMethodName = getClassOrMethodName(metadata);
/*     */     try {
/*  47 */       ConditionOutcome outcome = getMatchOutcome(context, metadata);
/*  48 */       logOutcome(classOrMethodName, outcome);
/*  49 */       recordEvaluation(context, classOrMethodName, outcome);
/*  50 */       return outcome.isMatch();
/*     */ 
/*     */     }
/*     */     catch (NoClassDefFoundError ex)
/*     */     {
/*  55 */       throw new IllegalStateException("Could not evaluate condition on " + classOrMethodName + " due to " + ex.getMessage() + " not found. Make sure your own configuration does not rely on that class. This can also happen if you are @ComponentScanning a springframework package (e.g. if you put a @ComponentScan in the default package by mistake)", ex);
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (RuntimeException ex)
/*     */     {
/*     */ 
/*     */ 
/*  64 */       throw new IllegalStateException("Error processing condition on " + getName(metadata), ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private String getName(AnnotatedTypeMetadata metadata) {
/*  69 */     if ((metadata instanceof AnnotationMetadata)) {
/*  70 */       return ((AnnotationMetadata)metadata).getClassName();
/*     */     }
/*  72 */     if ((metadata instanceof MethodMetadata)) {
/*  73 */       MethodMetadata methodMetadata = (MethodMetadata)metadata;
/*  74 */       return methodMetadata.getDeclaringClassName() + "." + methodMetadata
/*  75 */         .getMethodName();
/*     */     }
/*  77 */     return metadata.toString();
/*     */   }
/*     */   
/*     */   private static String getClassOrMethodName(AnnotatedTypeMetadata metadata) {
/*  81 */     if ((metadata instanceof ClassMetadata)) {
/*  82 */       ClassMetadata classMetadata = (ClassMetadata)metadata;
/*  83 */       return classMetadata.getClassName();
/*     */     }
/*  85 */     MethodMetadata methodMetadata = (MethodMetadata)metadata;
/*  86 */     return methodMetadata.getDeclaringClassName() + "#" + methodMetadata
/*  87 */       .getMethodName();
/*     */   }
/*     */   
/*     */   protected final void logOutcome(String classOrMethodName, ConditionOutcome outcome) {
/*  91 */     if (this.logger.isTraceEnabled()) {
/*  92 */       this.logger.trace(getLogMessage(classOrMethodName, outcome));
/*     */     }
/*     */   }
/*     */   
/*     */   private StringBuilder getLogMessage(String classOrMethodName, ConditionOutcome outcome)
/*     */   {
/*  98 */     StringBuilder message = new StringBuilder();
/*  99 */     message.append("Condition ");
/* 100 */     message.append(ClassUtils.getShortName(getClass()));
/* 101 */     message.append(" on ");
/* 102 */     message.append(classOrMethodName);
/* 103 */     message.append(outcome.isMatch() ? " matched" : " did not match");
/* 104 */     if (StringUtils.hasLength(outcome.getMessage())) {
/* 105 */       message.append(" due to ");
/* 106 */       message.append(outcome.getMessage());
/*     */     }
/* 108 */     return message;
/*     */   }
/*     */   
/*     */   private void recordEvaluation(ConditionContext context, String classOrMethodName, ConditionOutcome outcome)
/*     */   {
/* 113 */     if (context.getBeanFactory() != null)
/*     */     {
/* 115 */       ConditionEvaluationReport.get(context.getBeanFactory()).recordConditionEvaluation(classOrMethodName, this, outcome);
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
/*     */   public abstract ConditionOutcome getMatchOutcome(ConditionContext paramConditionContext, AnnotatedTypeMetadata paramAnnotatedTypeMetadata);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final boolean anyMatches(ConditionContext context, AnnotatedTypeMetadata metadata, Condition... conditions)
/*     */   {
/* 137 */     for (Condition condition : conditions) {
/* 138 */       if (matches(context, metadata, condition)) {
/* 139 */         return true;
/*     */       }
/*     */     }
/* 142 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata, Condition condition)
/*     */   {
/* 154 */     if ((condition instanceof SpringBootCondition)) {
/* 155 */       return 
/* 156 */         ((SpringBootCondition)condition).getMatchOutcome(context, metadata).isMatch();
/*     */     }
/* 158 */     return condition.matches(context, metadata);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\condition\SpringBootCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */