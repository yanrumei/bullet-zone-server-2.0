/*     */ package org.springframework.web.servlet.mvc.condition;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import javax.servlet.http.HttpServletRequest;
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
/*     */ public final class RequestConditionHolder
/*     */   extends AbstractRequestCondition<RequestConditionHolder>
/*     */ {
/*     */   private final RequestCondition<Object> condition;
/*     */   
/*     */   public RequestConditionHolder(RequestCondition<?> requestCondition)
/*     */   {
/*  48 */     this.condition = requestCondition;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RequestCondition<?> getCondition()
/*     */   {
/*  56 */     return this.condition;
/*     */   }
/*     */   
/*     */   protected Collection<?> getContent()
/*     */   {
/*  61 */     return this.condition != null ? Collections.singleton(this.condition) : Collections.emptyList();
/*     */   }
/*     */   
/*     */   protected String getToStringInfix()
/*     */   {
/*  66 */     return " ";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RequestConditionHolder combine(RequestConditionHolder other)
/*     */   {
/*  76 */     if ((this.condition == null) && (other.condition == null)) {
/*  77 */       return this;
/*     */     }
/*  79 */     if (this.condition == null) {
/*  80 */       return other;
/*     */     }
/*  82 */     if (other.condition == null) {
/*  83 */       return this;
/*     */     }
/*     */     
/*  86 */     assertEqualConditionTypes(other);
/*  87 */     RequestCondition<?> combined = (RequestCondition)this.condition.combine(other.condition);
/*  88 */     return new RequestConditionHolder(combined);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void assertEqualConditionTypes(RequestConditionHolder other)
/*     */   {
/*  96 */     Class<?> clazz = this.condition.getClass();
/*  97 */     Class<?> otherClazz = other.condition.getClass();
/*  98 */     if (!clazz.equals(otherClazz)) {
/*  99 */       throw new ClassCastException("Incompatible request conditions: " + clazz + " and " + otherClazz);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RequestConditionHolder getMatchingCondition(HttpServletRequest request)
/*     */   {
/* 110 */     if (this.condition == null) {
/* 111 */       return this;
/*     */     }
/* 113 */     RequestCondition<?> match = (RequestCondition)this.condition.getMatchingCondition(request);
/* 114 */     return match != null ? new RequestConditionHolder(match) : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int compareTo(RequestConditionHolder other, HttpServletRequest request)
/*     */   {
/* 124 */     if ((this.condition == null) && (other.condition == null)) {
/* 125 */       return 0;
/*     */     }
/* 127 */     if (this.condition == null) {
/* 128 */       return 1;
/*     */     }
/* 130 */     if (other.condition == null) {
/* 131 */       return -1;
/*     */     }
/*     */     
/* 134 */     assertEqualConditionTypes(other);
/* 135 */     return this.condition.compareTo(other.condition, request);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\condition\RequestConditionHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */