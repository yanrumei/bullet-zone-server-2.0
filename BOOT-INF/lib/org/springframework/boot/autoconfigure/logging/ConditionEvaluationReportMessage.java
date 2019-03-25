/*     */ package org.springframework.boot.autoconfigure.logging;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport.ConditionAndOutcome;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport.ConditionAndOutcomes;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
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
/*     */ public class ConditionEvaluationReportMessage
/*     */ {
/*     */   private StringBuilder message;
/*     */   
/*     */   public ConditionEvaluationReportMessage(ConditionEvaluationReport report)
/*     */   {
/*  43 */     this.message = getLogMessage(report);
/*     */   }
/*     */   
/*     */   private StringBuilder getLogMessage(ConditionEvaluationReport report) {
/*  47 */     StringBuilder message = new StringBuilder();
/*  48 */     message.append(String.format("%n%n%n", new Object[0]));
/*  49 */     message.append(String.format("=========================%n", new Object[0]));
/*  50 */     message.append(String.format("AUTO-CONFIGURATION REPORT%n", new Object[0]));
/*  51 */     message.append(String.format("=========================%n%n%n", new Object[0]));
/*  52 */     message.append(String.format("Positive matches:%n", new Object[0]));
/*  53 */     message.append(String.format("-----------------%n", new Object[0]));
/*  54 */     Map<String, ConditionEvaluationReport.ConditionAndOutcomes> shortOutcomes = orderByName(report
/*  55 */       .getConditionAndOutcomesBySource());
/*  56 */     for (Map.Entry<String, ConditionEvaluationReport.ConditionAndOutcomes> entry : shortOutcomes.entrySet()) {
/*  57 */       if (((ConditionEvaluationReport.ConditionAndOutcomes)entry.getValue()).isFullMatch()) {
/*  58 */         addMatchLogMessage(message, (String)entry.getKey(), (ConditionEvaluationReport.ConditionAndOutcomes)entry.getValue());
/*     */       }
/*     */     }
/*  61 */     message.append(String.format("%n%n", new Object[0]));
/*  62 */     message.append(String.format("Negative matches:%n", new Object[0]));
/*  63 */     message.append(String.format("-----------------%n", new Object[0]));
/*  64 */     for (Map.Entry<String, ConditionEvaluationReport.ConditionAndOutcomes> entry : shortOutcomes.entrySet()) {
/*  65 */       if (!((ConditionEvaluationReport.ConditionAndOutcomes)entry.getValue()).isFullMatch()) {
/*  66 */         addNonMatchLogMessage(message, (String)entry.getKey(), (ConditionEvaluationReport.ConditionAndOutcomes)entry.getValue());
/*     */       }
/*     */     }
/*  69 */     message.append(String.format("%n%n", new Object[0]));
/*  70 */     message.append(String.format("Exclusions:%n", new Object[0]));
/*  71 */     message.append(String.format("-----------%n", new Object[0]));
/*  72 */     if (report.getExclusions().isEmpty()) {
/*  73 */       message.append(String.format("%n    None%n", new Object[0]));
/*     */     }
/*     */     else {
/*  76 */       for (String exclusion : report.getExclusions()) {
/*  77 */         message.append(String.format("%n    %s%n", new Object[] { exclusion }));
/*     */       }
/*     */     }
/*  80 */     message.append(String.format("%n%n", new Object[0]));
/*  81 */     message.append(String.format("Unconditional classes:%n", new Object[0]));
/*  82 */     message.append(String.format("----------------------%n", new Object[0]));
/*  83 */     if (report.getUnconditionalClasses().isEmpty()) {
/*  84 */       message.append(String.format("%n    None%n", new Object[0]));
/*     */     }
/*     */     else {
/*  87 */       for (String unconditionalClass : report.getUnconditionalClasses()) {
/*  88 */         message.append(String.format("%n    %s%n", new Object[] { unconditionalClass }));
/*     */       }
/*     */     }
/*  91 */     message.append(String.format("%n%n", new Object[0]));
/*  92 */     return message;
/*     */   }
/*     */   
/*     */   private Map<String, ConditionEvaluationReport.ConditionAndOutcomes> orderByName(Map<String, ConditionEvaluationReport.ConditionAndOutcomes> outcomes)
/*     */   {
/*  97 */     Map<String, ConditionEvaluationReport.ConditionAndOutcomes> result = new LinkedHashMap();
/*  98 */     List<String> names = new ArrayList();
/*  99 */     Map<String, String> classNames = new HashMap();
/* 100 */     for (String name : outcomes.keySet()) {
/* 101 */       String shortName = ClassUtils.getShortName(name);
/* 102 */       names.add(shortName);
/* 103 */       classNames.put(shortName, name);
/*     */     }
/* 105 */     Collections.sort(names);
/* 106 */     for (String shortName : names) {
/* 107 */       result.put(shortName, outcomes.get(classNames.get(shortName)));
/*     */     }
/* 109 */     return result;
/*     */   }
/*     */   
/*     */   private void addMatchLogMessage(StringBuilder message, String source, ConditionEvaluationReport.ConditionAndOutcomes matches)
/*     */   {
/* 114 */     message.append(String.format("%n   %s matched:%n", new Object[] { source }));
/* 115 */     for (ConditionEvaluationReport.ConditionAndOutcome match : matches) {
/* 116 */       logConditionAndOutcome(message, "      ", match);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addNonMatchLogMessage(StringBuilder message, String source, ConditionEvaluationReport.ConditionAndOutcomes conditionAndOutcomes)
/*     */   {
/* 122 */     message.append(String.format("%n   %s:%n", new Object[] { source }));
/* 123 */     List<ConditionEvaluationReport.ConditionAndOutcome> matches = new ArrayList();
/* 124 */     List<ConditionEvaluationReport.ConditionAndOutcome> nonMatches = new ArrayList();
/* 125 */     for (ConditionEvaluationReport.ConditionAndOutcome conditionAndOutcome : conditionAndOutcomes) {
/* 126 */       if (conditionAndOutcome.getOutcome().isMatch()) {
/* 127 */         matches.add(conditionAndOutcome);
/*     */       }
/*     */       else {
/* 130 */         nonMatches.add(conditionAndOutcome);
/*     */       }
/*     */     }
/* 133 */     message.append(String.format("      Did not match:%n", new Object[0]));
/* 134 */     for (ConditionEvaluationReport.ConditionAndOutcome nonMatch : nonMatches) {
/* 135 */       logConditionAndOutcome(message, "         ", nonMatch);
/*     */     }
/* 137 */     if (!matches.isEmpty()) {
/* 138 */       message.append(String.format("      Matched:%n", new Object[0]));
/* 139 */       for (ConditionEvaluationReport.ConditionAndOutcome match : matches) {
/* 140 */         logConditionAndOutcome(message, "         ", match);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void logConditionAndOutcome(StringBuilder message, String indent, ConditionEvaluationReport.ConditionAndOutcome conditionAndOutcome)
/*     */   {
/* 147 */     message.append(String.format("%s- ", new Object[] { indent }));
/* 148 */     String outcomeMessage = conditionAndOutcome.getOutcome().getMessage();
/* 149 */     if (StringUtils.hasLength(outcomeMessage)) {
/* 150 */       message.append(outcomeMessage);
/*     */     }
/*     */     else {
/* 153 */       message.append(conditionAndOutcome.getOutcome().isMatch() ? "matched" : "did not match");
/*     */     }
/*     */     
/* 156 */     message.append(" (");
/* 157 */     message.append(
/* 158 */       ClassUtils.getShortName(conditionAndOutcome.getCondition().getClass()));
/* 159 */     message.append(String.format(")%n", new Object[0]));
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 164 */     return this.message.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\logging\ConditionEvaluationReportMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */