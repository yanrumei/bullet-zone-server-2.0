/*     */ package org.springframework.boot.diagnostics.analyzer;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanCurrentlyInCreationException;
/*     */ import org.springframework.beans.factory.InjectionPoint;
/*     */ import org.springframework.beans.factory.UnsatisfiedDependencyException;
/*     */ import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
/*     */ import org.springframework.boot.diagnostics.FailureAnalysis;
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
/*     */ 
/*     */ class BeanCurrentlyInCreationFailureAnalyzer
/*     */   extends AbstractFailureAnalyzer<BeanCurrentlyInCreationException>
/*     */ {
/*     */   protected FailureAnalysis analyze(Throwable rootFailure, BeanCurrentlyInCreationException cause)
/*     */   {
/*  42 */     DependencyCycle dependencyCycle = findCycle(rootFailure);
/*  43 */     if (dependencyCycle == null) {
/*  44 */       return null;
/*     */     }
/*  46 */     return new FailureAnalysis(buildMessage(dependencyCycle), null, cause);
/*     */   }
/*     */   
/*     */   private DependencyCycle findCycle(Throwable rootFailure) {
/*  50 */     List<BeanInCycle> beansInCycle = new ArrayList();
/*  51 */     Throwable candidate = rootFailure;
/*  52 */     int cycleStart = -1;
/*  53 */     while (candidate != null) {
/*  54 */       BeanInCycle beanInCycle = BeanInCycle.get(candidate);
/*  55 */       if (beanInCycle != null) {
/*  56 */         int index = beansInCycle.indexOf(beanInCycle);
/*  57 */         if (index == -1) {
/*  58 */           beansInCycle.add(beanInCycle);
/*     */         }
/*  60 */         cycleStart = cycleStart == -1 ? index : cycleStart;
/*     */       }
/*  62 */       candidate = candidate.getCause();
/*     */     }
/*  64 */     if (cycleStart == -1) {
/*  65 */       return null;
/*     */     }
/*  67 */     return new DependencyCycle(beansInCycle, cycleStart, null);
/*     */   }
/*     */   
/*     */   private String buildMessage(DependencyCycle dependencyCycle) {
/*  71 */     StringBuilder message = new StringBuilder();
/*  72 */     message.append(String.format("The dependencies of some of the beans in the application context form a cycle:%n%n", new Object[0]));
/*     */     
/*  74 */     List<BeanInCycle> beansInCycle = dependencyCycle.getBeansInCycle();
/*  75 */     int cycleStart = dependencyCycle.getCycleStart();
/*  76 */     for (int i = 0; i < beansInCycle.size(); i++) {
/*  77 */       BeanInCycle beanInCycle = (BeanInCycle)beansInCycle.get(i);
/*  78 */       if (i == cycleStart) {
/*  79 */         message.append(String.format("┌─────┐%n", new Object[0]));
/*     */       }
/*  81 */       else if (i > 0) {
/*  82 */         String leftSide = i < cycleStart ? " " : "↑";
/*  83 */         message.append(String.format("%s     ↓%n", new Object[] { leftSide }));
/*     */       }
/*  85 */       String leftSide = i < cycleStart ? " " : "|";
/*  86 */       message.append(String.format("%s  %s%n", new Object[] { leftSide, beanInCycle }));
/*     */     }
/*  88 */     message.append(String.format("└─────┘%n", new Object[0]));
/*  89 */     return message.toString();
/*     */   }
/*     */   
/*     */   private static final class DependencyCycle
/*     */   {
/*     */     private final List<BeanCurrentlyInCreationFailureAnalyzer.BeanInCycle> beansInCycle;
/*     */     private final int cycleStart;
/*     */     
/*     */     private DependencyCycle(List<BeanCurrentlyInCreationFailureAnalyzer.BeanInCycle> beansInCycle, int cycleStart)
/*     */     {
/*  99 */       this.beansInCycle = beansInCycle;
/* 100 */       this.cycleStart = cycleStart;
/*     */     }
/*     */     
/*     */     public List<BeanCurrentlyInCreationFailureAnalyzer.BeanInCycle> getBeansInCycle() {
/* 104 */       return this.beansInCycle;
/*     */     }
/*     */     
/*     */     public int getCycleStart() {
/* 108 */       return this.cycleStart;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class BeanInCycle
/*     */   {
/*     */     private final String name;
/*     */     private final String description;
/*     */     
/*     */     private BeanInCycle(BeanCreationException ex)
/*     */     {
/* 120 */       this.name = ex.getBeanName();
/* 121 */       this.description = determineDescription(ex);
/*     */     }
/*     */     
/*     */     private String determineDescription(BeanCreationException ex) {
/* 125 */       if (StringUtils.hasText(ex.getResourceDescription())) {
/* 126 */         return String.format(" defined in %s", new Object[] { ex.getResourceDescription() });
/*     */       }
/* 128 */       InjectionPoint failedInjectionPoint = findFailedInjectionPoint(ex);
/* 129 */       if ((failedInjectionPoint != null) && (failedInjectionPoint.getField() != null)) {
/* 130 */         return String.format(" (field %s)", new Object[] { failedInjectionPoint.getField() });
/*     */       }
/* 132 */       return "";
/*     */     }
/*     */     
/*     */     private InjectionPoint findFailedInjectionPoint(BeanCreationException ex) {
/* 136 */       if (!(ex instanceof UnsatisfiedDependencyException)) {
/* 137 */         return null;
/*     */       }
/* 139 */       return ((UnsatisfiedDependencyException)ex).getInjectionPoint();
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 144 */       return this.name.hashCode();
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 149 */       if (this == obj) {
/* 150 */         return true;
/*     */       }
/* 152 */       if ((obj == null) || (getClass() != obj.getClass())) {
/* 153 */         return false;
/*     */       }
/* 155 */       return this.name.equals(((BeanInCycle)obj).name);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 160 */       return this.name + this.description;
/*     */     }
/*     */     
/*     */     public static BeanInCycle get(Throwable ex) {
/* 164 */       if ((ex instanceof BeanCreationException)) {
/* 165 */         return get((BeanCreationException)ex);
/*     */       }
/* 167 */       return null;
/*     */     }
/*     */     
/*     */     private static BeanInCycle get(BeanCreationException ex) {
/* 171 */       if (StringUtils.hasText(ex.getBeanName())) {
/* 172 */         return new BeanInCycle(ex);
/*     */       }
/* 174 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\diagnostics\analyzer\BeanCurrentlyInCreationFailureAnalyzer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */