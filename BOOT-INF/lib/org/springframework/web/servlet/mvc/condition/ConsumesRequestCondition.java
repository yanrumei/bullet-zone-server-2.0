/*     */ package org.springframework.web.servlet.mvc.condition;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.http.InvalidMediaTypeException;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ConsumesRequestCondition
/*     */   extends AbstractRequestCondition<ConsumesRequestCondition>
/*     */ {
/*  51 */   private static final ConsumesRequestCondition PRE_FLIGHT_MATCH = new ConsumesRequestCondition(new String[0]);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final List<ConsumeMediaTypeExpression> expressions;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConsumesRequestCondition(String... consumes)
/*     */   {
/*  64 */     this(consumes, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConsumesRequestCondition(String[] consumes, String[] headers)
/*     */   {
/*  76 */     this(parseExpressions(consumes, headers));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private ConsumesRequestCondition(Collection<ConsumeMediaTypeExpression> expressions)
/*     */   {
/*  83 */     this.expressions = new ArrayList(expressions);
/*  84 */     Collections.sort(this.expressions);
/*     */   }
/*     */   
/*     */   private static Set<ConsumeMediaTypeExpression> parseExpressions(String[] consumes, String[] headers)
/*     */   {
/*  89 */     Set<ConsumeMediaTypeExpression> result = new LinkedHashSet();
/*  90 */     if (headers != null) { HeadersRequestCondition.HeaderExpression expr;
/*  91 */       for (String header : headers) {
/*  92 */         expr = new HeadersRequestCondition.HeaderExpression(header);
/*  93 */         if ("Content-Type".equalsIgnoreCase(expr.name)) {
/*  94 */           for (MediaType mediaType : MediaType.parseMediaTypes((String)expr.value)) {
/*  95 */             result.add(new ConsumeMediaTypeExpression(mediaType, expr.isNegated));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 100 */     if (consumes != null) {
/* 101 */       for (String consume : consumes) {
/* 102 */         result.add(new ConsumeMediaTypeExpression(consume));
/*     */       }
/*     */     }
/* 105 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<MediaTypeExpression> getExpressions()
/*     */   {
/* 113 */     return new LinkedHashSet(this.expressions);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Set<MediaType> getConsumableMediaTypes()
/*     */   {
/* 120 */     Set<MediaType> result = new LinkedHashSet();
/* 121 */     for (ConsumeMediaTypeExpression expression : this.expressions) {
/* 122 */       if (!expression.isNegated()) {
/* 123 */         result.add(expression.getMediaType());
/*     */       }
/*     */     }
/* 126 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 133 */     return this.expressions.isEmpty();
/*     */   }
/*     */   
/*     */   protected Collection<ConsumeMediaTypeExpression> getContent()
/*     */   {
/* 138 */     return this.expressions;
/*     */   }
/*     */   
/*     */   protected String getToStringInfix()
/*     */   {
/* 143 */     return " || ";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConsumesRequestCondition combine(ConsumesRequestCondition other)
/*     */   {
/* 153 */     return !other.expressions.isEmpty() ? other : this;
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
/*     */   public ConsumesRequestCondition getMatchingCondition(HttpServletRequest request)
/*     */   {
/* 168 */     if (CorsUtils.isPreFlightRequest(request)) {
/* 169 */       return PRE_FLIGHT_MATCH;
/*     */     }
/* 171 */     if (isEmpty()) {
/* 172 */       return this;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 177 */       contentType = StringUtils.hasLength(request.getContentType()) ? MediaType.parseMediaType(request.getContentType()) : MediaType.APPLICATION_OCTET_STREAM;
/*     */     }
/*     */     catch (InvalidMediaTypeException ex) {
/*     */       MediaType contentType;
/* 181 */       return null; }
/*     */     MediaType contentType;
/* 183 */     Set<ConsumeMediaTypeExpression> result = new LinkedHashSet(this.expressions);
/* 184 */     for (Iterator<ConsumeMediaTypeExpression> iterator = result.iterator(); iterator.hasNext();) {
/* 185 */       ConsumeMediaTypeExpression expression = (ConsumeMediaTypeExpression)iterator.next();
/* 186 */       if (!expression.match(contentType)) {
/* 187 */         iterator.remove();
/*     */       }
/*     */     }
/* 190 */     return result.isEmpty() ? null : new ConsumesRequestCondition(result);
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
/*     */   public int compareTo(ConsumesRequestCondition other, HttpServletRequest request)
/*     */   {
/* 206 */     if ((this.expressions.isEmpty()) && (other.expressions.isEmpty())) {
/* 207 */       return 0;
/*     */     }
/* 209 */     if (this.expressions.isEmpty()) {
/* 210 */       return 1;
/*     */     }
/* 212 */     if (other.expressions.isEmpty()) {
/* 213 */       return -1;
/*     */     }
/*     */     
/* 216 */     return ((ConsumeMediaTypeExpression)this.expressions.get(0)).compareTo((AbstractMediaTypeExpression)other.expressions.get(0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static class ConsumeMediaTypeExpression
/*     */     extends AbstractMediaTypeExpression
/*     */   {
/*     */     ConsumeMediaTypeExpression(String expression)
/*     */     {
/* 227 */       super();
/*     */     }
/*     */     
/*     */     ConsumeMediaTypeExpression(MediaType mediaType, boolean negated) {
/* 231 */       super(negated);
/*     */     }
/*     */     
/*     */     public final boolean match(MediaType contentType) {
/* 235 */       boolean match = getMediaType().includes(contentType);
/* 236 */       return !match ? true : !isNegated() ? match : false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\condition\ConsumesRequestCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */