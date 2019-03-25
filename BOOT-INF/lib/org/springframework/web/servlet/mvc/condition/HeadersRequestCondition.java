/*     */ package org.springframework.web.servlet.mvc.condition;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.web.cors.CorsUtils;
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
/*     */ public final class HeadersRequestCondition
/*     */   extends AbstractRequestCondition<HeadersRequestCondition>
/*     */ {
/*  43 */   private static final HeadersRequestCondition PRE_FLIGHT_MATCH = new HeadersRequestCondition(new String[0]);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Set<HeaderExpression> expressions;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HeadersRequestCondition(String... headers)
/*     */   {
/*  57 */     this(parseExpressions(headers));
/*     */   }
/*     */   
/*     */   private HeadersRequestCondition(Collection<HeaderExpression> conditions) {
/*  61 */     this.expressions = Collections.unmodifiableSet(new LinkedHashSet(conditions));
/*     */   }
/*     */   
/*     */   private static Collection<HeaderExpression> parseExpressions(String... headers)
/*     */   {
/*  66 */     Set<HeaderExpression> expressions = new LinkedHashSet();
/*  67 */     if (headers != null) {
/*  68 */       for (String header : headers) {
/*  69 */         HeaderExpression expr = new HeaderExpression(header);
/*  70 */         if ((!"Accept".equalsIgnoreCase(expr.name)) && (!"Content-Type".equalsIgnoreCase(expr.name)))
/*     */         {
/*     */ 
/*  73 */           expressions.add(expr); }
/*     */       }
/*     */     }
/*  76 */     return expressions;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Set<NameValueExpression<String>> getExpressions()
/*     */   {
/*  83 */     return new LinkedHashSet(this.expressions);
/*     */   }
/*     */   
/*     */   protected Collection<HeaderExpression> getContent()
/*     */   {
/*  88 */     return this.expressions;
/*     */   }
/*     */   
/*     */   protected String getToStringInfix()
/*     */   {
/*  93 */     return " && ";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HeadersRequestCondition combine(HeadersRequestCondition other)
/*     */   {
/* 102 */     Set<HeaderExpression> set = new LinkedHashSet(this.expressions);
/* 103 */     set.addAll(other.expressions);
/* 104 */     return new HeadersRequestCondition(set);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HeadersRequestCondition getMatchingCondition(HttpServletRequest request)
/*     */   {
/* 113 */     if (CorsUtils.isPreFlightRequest(request)) {
/* 114 */       return PRE_FLIGHT_MATCH;
/*     */     }
/* 116 */     for (HeaderExpression expression : this.expressions) {
/* 117 */       if (!expression.match(request)) {
/* 118 */         return null;
/*     */       }
/*     */     }
/* 121 */     return this;
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
/*     */   public int compareTo(HeadersRequestCondition other, HttpServletRequest request)
/*     */   {
/* 137 */     return other.expressions.size() - this.expressions.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static class HeaderExpression
/*     */     extends AbstractNameValueExpression<String>
/*     */   {
/*     */     public HeaderExpression(String expression)
/*     */     {
/* 147 */       super();
/*     */     }
/*     */     
/*     */     protected boolean isCaseSensitiveName()
/*     */     {
/* 152 */       return false;
/*     */     }
/*     */     
/*     */     protected String parseValue(String valueExpression)
/*     */     {
/* 157 */       return valueExpression;
/*     */     }
/*     */     
/*     */     protected boolean matchName(HttpServletRequest request)
/*     */     {
/* 162 */       return request.getHeader(this.name) != null;
/*     */     }
/*     */     
/*     */     protected boolean matchValue(HttpServletRequest request)
/*     */     {
/* 167 */       return ObjectUtils.nullSafeEquals(this.value, request.getHeader(this.name));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\condition\HeadersRequestCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */