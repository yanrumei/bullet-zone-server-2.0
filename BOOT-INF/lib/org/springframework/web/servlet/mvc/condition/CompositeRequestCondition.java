/*     */ package org.springframework.web.servlet.mvc.condition;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompositeRequestCondition
/*     */   extends AbstractRequestCondition<CompositeRequestCondition>
/*     */ {
/*     */   private final RequestConditionHolder[] requestConditions;
/*     */   
/*     */   public CompositeRequestCondition(RequestCondition<?>... requestConditions)
/*     */   {
/*  53 */     this.requestConditions = wrap(requestConditions);
/*     */   }
/*     */   
/*     */   private CompositeRequestCondition(RequestConditionHolder[] requestConditions) {
/*  57 */     this.requestConditions = requestConditions;
/*     */   }
/*     */   
/*     */   private RequestConditionHolder[] wrap(RequestCondition<?>... rawConditions)
/*     */   {
/*  62 */     RequestConditionHolder[] wrappedConditions = new RequestConditionHolder[rawConditions.length];
/*  63 */     for (int i = 0; i < rawConditions.length; i++) {
/*  64 */       wrappedConditions[i] = new RequestConditionHolder(rawConditions[i]);
/*     */     }
/*  66 */     return wrappedConditions;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/*  73 */     return ObjectUtils.isEmpty(this.requestConditions);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<RequestCondition<?>> getConditions()
/*     */   {
/*  80 */     return unwrap();
/*     */   }
/*     */   
/*     */   private List<RequestCondition<?>> unwrap() {
/*  84 */     List<RequestCondition<?>> result = new ArrayList();
/*  85 */     for (RequestConditionHolder holder : this.requestConditions) {
/*  86 */       result.add(holder.getCondition());
/*     */     }
/*  88 */     return result;
/*     */   }
/*     */   
/*     */   protected Collection<?> getContent()
/*     */   {
/*  93 */     return isEmpty() ? Collections.emptyList() : getConditions();
/*     */   }
/*     */   
/*     */   protected String getToStringInfix()
/*     */   {
/*  98 */     return " && ";
/*     */   }
/*     */   
/*     */   private int getLength() {
/* 102 */     return this.requestConditions.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CompositeRequestCondition combine(CompositeRequestCondition other)
/*     */   {
/* 112 */     if ((isEmpty()) && (other.isEmpty())) {
/* 113 */       return this;
/*     */     }
/* 115 */     if (other.isEmpty()) {
/* 116 */       return this;
/*     */     }
/* 118 */     if (isEmpty()) {
/* 119 */       return other;
/*     */     }
/*     */     
/* 122 */     assertNumberOfConditions(other);
/* 123 */     RequestConditionHolder[] combinedConditions = new RequestConditionHolder[getLength()];
/* 124 */     for (int i = 0; i < getLength(); i++) {
/* 125 */       combinedConditions[i] = this.requestConditions[i].combine(other.requestConditions[i]);
/*     */     }
/* 127 */     return new CompositeRequestCondition(combinedConditions);
/*     */   }
/*     */   
/*     */   private void assertNumberOfConditions(CompositeRequestCondition other)
/*     */   {
/* 132 */     Assert.isTrue(getLength() == other.getLength(), "Cannot combine CompositeRequestConditions with a different number of conditions. " + 
/*     */     
/* 134 */       ObjectUtils.nullSafeToString(this.requestConditions) + " and  " + 
/* 135 */       ObjectUtils.nullSafeToString(other.requestConditions));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CompositeRequestCondition getMatchingCondition(HttpServletRequest request)
/*     */   {
/* 145 */     if (isEmpty()) {
/* 146 */       return this;
/*     */     }
/* 148 */     RequestConditionHolder[] matchingConditions = new RequestConditionHolder[getLength()];
/* 149 */     for (int i = 0; i < getLength(); i++) {
/* 150 */       matchingConditions[i] = this.requestConditions[i].getMatchingCondition(request);
/* 151 */       if (matchingConditions[i] == null) {
/* 152 */         return null;
/*     */       }
/*     */     }
/* 155 */     return new CompositeRequestCondition(matchingConditions);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int compareTo(CompositeRequestCondition other, HttpServletRequest request)
/*     */   {
/* 164 */     if ((isEmpty()) && (other.isEmpty())) {
/* 165 */       return 0;
/*     */     }
/* 167 */     if (isEmpty()) {
/* 168 */       return 1;
/*     */     }
/* 170 */     if (other.isEmpty()) {
/* 171 */       return -1;
/*     */     }
/*     */     
/* 174 */     assertNumberOfConditions(other);
/* 175 */     for (int i = 0; i < getLength(); i++) {
/* 176 */       int result = this.requestConditions[i].compareTo(other.requestConditions[i], request);
/* 177 */       if (result != 0) {
/* 178 */         return result;
/*     */       }
/*     */     }
/* 181 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\condition\CompositeRequestCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */