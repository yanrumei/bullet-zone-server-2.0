/*     */ package org.springframework.web.servlet.mvc.condition;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.web.util.WebUtils;
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
/*     */ public final class ParamsRequestCondition
/*     */   extends AbstractRequestCondition<ParamsRequestCondition>
/*     */ {
/*     */   private final Set<ParamExpression> expressions;
/*     */   
/*     */   public ParamsRequestCondition(String... params)
/*     */   {
/*  48 */     this(parseExpressions(params));
/*     */   }
/*     */   
/*     */   private ParamsRequestCondition(Collection<ParamExpression> conditions) {
/*  52 */     this.expressions = Collections.unmodifiableSet(new LinkedHashSet(conditions));
/*     */   }
/*     */   
/*     */   private static Collection<ParamExpression> parseExpressions(String... params)
/*     */   {
/*  57 */     Set<ParamExpression> expressions = new LinkedHashSet();
/*  58 */     if (params != null) {
/*  59 */       for (String param : params) {
/*  60 */         expressions.add(new ParamExpression(param));
/*     */       }
/*     */     }
/*  63 */     return expressions;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<NameValueExpression<String>> getExpressions()
/*     */   {
/*  71 */     return new LinkedHashSet(this.expressions);
/*     */   }
/*     */   
/*     */   protected Collection<ParamExpression> getContent()
/*     */   {
/*  76 */     return this.expressions;
/*     */   }
/*     */   
/*     */   protected String getToStringInfix()
/*     */   {
/*  81 */     return " && ";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ParamsRequestCondition combine(ParamsRequestCondition other)
/*     */   {
/*  90 */     Set<ParamExpression> set = new LinkedHashSet(this.expressions);
/*  91 */     set.addAll(other.expressions);
/*  92 */     return new ParamsRequestCondition(set);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ParamsRequestCondition getMatchingCondition(HttpServletRequest request)
/*     */   {
/* 101 */     for (ParamExpression expression : this.expressions) {
/* 102 */       if (!expression.match(request)) {
/* 103 */         return null;
/*     */       }
/*     */     }
/* 106 */     return this;
/*     */   }
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
/*     */   public int compareTo(ParamsRequestCondition other, HttpServletRequest request)
/*     */   {
/* 122 */     return other.expressions.size() - this.expressions.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static class ParamExpression
/*     */     extends AbstractNameValueExpression<String>
/*     */   {
/*     */     ParamExpression(String expression)
/*     */     {
/* 132 */       super();
/*     */     }
/*     */     
/*     */     protected boolean isCaseSensitiveName()
/*     */     {
/* 137 */       return true;
/*     */     }
/*     */     
/*     */     protected String parseValue(String valueExpression)
/*     */     {
/* 142 */       return valueExpression;
/*     */     }
/*     */     
/*     */     protected boolean matchName(HttpServletRequest request)
/*     */     {
/* 147 */       return WebUtils.hasSubmitParameter(request, this.name);
/*     */     }
/*     */     
/*     */     protected boolean matchValue(HttpServletRequest request)
/*     */     {
/* 152 */       return ObjectUtils.nullSafeEquals(this.value, request.getParameter(this.name));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\condition\ParamsRequestCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */