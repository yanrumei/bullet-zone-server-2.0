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
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.web.HttpMediaTypeException;
/*     */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.context.request.ServletWebRequest;
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
/*     */ public final class ProducesRequestCondition
/*     */   extends AbstractRequestCondition<ProducesRequestCondition>
/*     */ {
/*  50 */   private static final ProducesRequestCondition PRE_FLIGHT_MATCH = new ProducesRequestCondition(new String[0]);
/*     */   
/*  52 */   private static final ProducesRequestCondition EMPTY_CONDITION = new ProducesRequestCondition(new String[0]);
/*     */   
/*     */ 
/*     */ 
/*  56 */   private final List<ProduceMediaTypeExpression> MEDIA_TYPE_ALL_LIST = Collections.singletonList(new ProduceMediaTypeExpression("*/*"));
/*     */   
/*     */ 
/*     */ 
/*     */   private final List<ProduceMediaTypeExpression> expressions;
/*     */   
/*     */ 
/*     */   private final ContentNegotiationManager contentNegotiationManager;
/*     */   
/*     */ 
/*     */ 
/*     */   public ProducesRequestCondition(String... produces)
/*     */   {
/*  69 */     this(produces, (String[])null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ProducesRequestCondition(String[] produces, String[] headers)
/*     */   {
/*  81 */     this(produces, headers, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ProducesRequestCondition(String[] produces, String[] headers, ContentNegotiationManager manager)
/*     */   {
/*  92 */     this.expressions = new ArrayList(parseExpressions(produces, headers));
/*  93 */     Collections.sort(this.expressions);
/*  94 */     this.contentNegotiationManager = (manager != null ? manager : new ContentNegotiationManager());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private ProducesRequestCondition(Collection<ProduceMediaTypeExpression> expressions, ContentNegotiationManager manager)
/*     */   {
/* 101 */     this.expressions = new ArrayList(expressions);
/* 102 */     Collections.sort(this.expressions);
/* 103 */     this.contentNegotiationManager = (manager != null ? manager : new ContentNegotiationManager());
/*     */   }
/*     */   
/*     */   private Set<ProduceMediaTypeExpression> parseExpressions(String[] produces, String[] headers)
/*     */   {
/* 108 */     Set<ProduceMediaTypeExpression> result = new LinkedHashSet();
/* 109 */     if (headers != null) { HeadersRequestCondition.HeaderExpression expr;
/* 110 */       for (String header : headers) {
/* 111 */         expr = new HeadersRequestCondition.HeaderExpression(header);
/* 112 */         if ("Accept".equalsIgnoreCase(expr.name)) {
/* 113 */           for (MediaType mediaType : MediaType.parseMediaTypes((String)expr.value)) {
/* 114 */             result.add(new ProduceMediaTypeExpression(mediaType, expr.isNegated));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 119 */     if (produces != null) {
/* 120 */       for (String produce : produces) {
/* 121 */         result.add(new ProduceMediaTypeExpression(produce));
/*     */       }
/*     */     }
/* 124 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Set<MediaTypeExpression> getExpressions()
/*     */   {
/* 131 */     return new LinkedHashSet(this.expressions);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Set<MediaType> getProducibleMediaTypes()
/*     */   {
/* 138 */     Set<MediaType> result = new LinkedHashSet();
/* 139 */     for (ProduceMediaTypeExpression expression : this.expressions) {
/* 140 */       if (!expression.isNegated()) {
/* 141 */         result.add(expression.getMediaType());
/*     */       }
/*     */     }
/* 144 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 151 */     return this.expressions.isEmpty();
/*     */   }
/*     */   
/*     */   protected List<ProduceMediaTypeExpression> getContent()
/*     */   {
/* 156 */     return this.expressions;
/*     */   }
/*     */   
/*     */   protected String getToStringInfix()
/*     */   {
/* 161 */     return " || ";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ProducesRequestCondition combine(ProducesRequestCondition other)
/*     */   {
/* 171 */     return !other.expressions.isEmpty() ? other : this;
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
/*     */   public ProducesRequestCondition getMatchingCondition(HttpServletRequest request)
/*     */   {
/* 186 */     if (CorsUtils.isPreFlightRequest(request)) {
/* 187 */       return PRE_FLIGHT_MATCH;
/*     */     }
/* 189 */     if (isEmpty()) {
/* 190 */       return this;
/*     */     }
/*     */     try
/*     */     {
/* 194 */       acceptedMediaTypes = getAcceptedMediaTypes(request);
/*     */     } catch (HttpMediaTypeException ex) {
/*     */       List<MediaType> acceptedMediaTypes;
/* 197 */       return null; }
/*     */     List<MediaType> acceptedMediaTypes;
/* 199 */     Set<ProduceMediaTypeExpression> result = new LinkedHashSet(this.expressions);
/* 200 */     for (Iterator<ProduceMediaTypeExpression> iterator = result.iterator(); iterator.hasNext();) {
/* 201 */       ProduceMediaTypeExpression expression = (ProduceMediaTypeExpression)iterator.next();
/* 202 */       if (!expression.match(acceptedMediaTypes)) {
/* 203 */         iterator.remove();
/*     */       }
/*     */     }
/* 206 */     if (!result.isEmpty()) {
/* 207 */       return new ProducesRequestCondition(result, this.contentNegotiationManager);
/*     */     }
/* 209 */     if (acceptedMediaTypes.contains(MediaType.ALL)) {
/* 210 */       return EMPTY_CONDITION;
/*     */     }
/*     */     
/* 213 */     return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int compareTo(ProducesRequestCondition other, HttpServletRequest request)
/*     */   {
/*     */     try
/*     */     {
/* 237 */       List<MediaType> acceptedMediaTypes = getAcceptedMediaTypes(request);
/* 238 */       for (MediaType acceptedMediaType : acceptedMediaTypes) {
/* 239 */         int thisIndex = indexOfEqualMediaType(acceptedMediaType);
/* 240 */         int otherIndex = other.indexOfEqualMediaType(acceptedMediaType);
/* 241 */         int result = compareMatchingMediaTypes(this, thisIndex, other, otherIndex);
/* 242 */         if (result != 0) {
/* 243 */           return result;
/*     */         }
/* 245 */         thisIndex = indexOfIncludedMediaType(acceptedMediaType);
/* 246 */         otherIndex = other.indexOfIncludedMediaType(acceptedMediaType);
/* 247 */         result = compareMatchingMediaTypes(this, thisIndex, other, otherIndex);
/* 248 */         if (result != 0) {
/* 249 */           return result;
/*     */         }
/*     */       }
/* 252 */       return 0;
/*     */     }
/*     */     catch (HttpMediaTypeNotAcceptableException ex)
/*     */     {
/* 256 */       throw new IllegalStateException("Cannot compare without having any requested media types", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private List<MediaType> getAcceptedMediaTypes(HttpServletRequest request) throws HttpMediaTypeNotAcceptableException {
/* 261 */     List<MediaType> mediaTypes = this.contentNegotiationManager.resolveMediaTypes(new ServletWebRequest(request));
/* 262 */     return mediaTypes.isEmpty() ? Collections.singletonList(MediaType.ALL) : mediaTypes;
/*     */   }
/*     */   
/*     */   private int indexOfEqualMediaType(MediaType mediaType) {
/* 266 */     for (int i = 0; i < getExpressionsToCompare().size(); i++) {
/* 267 */       MediaType currentMediaType = ((ProduceMediaTypeExpression)getExpressionsToCompare().get(i)).getMediaType();
/* 268 */       if ((mediaType.getType().equalsIgnoreCase(currentMediaType.getType())) && 
/* 269 */         (mediaType.getSubtype().equalsIgnoreCase(currentMediaType.getSubtype()))) {
/* 270 */         return i;
/*     */       }
/*     */     }
/* 273 */     return -1;
/*     */   }
/*     */   
/*     */   private int indexOfIncludedMediaType(MediaType mediaType) {
/* 277 */     for (int i = 0; i < getExpressionsToCompare().size(); i++) {
/* 278 */       if (mediaType.includes(((ProduceMediaTypeExpression)getExpressionsToCompare().get(i)).getMediaType())) {
/* 279 */         return i;
/*     */       }
/*     */     }
/* 282 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   private int compareMatchingMediaTypes(ProducesRequestCondition condition1, int index1, ProducesRequestCondition condition2, int index2)
/*     */   {
/* 288 */     int result = 0;
/* 289 */     if (index1 != index2) {
/* 290 */       result = index2 - index1;
/*     */     }
/* 292 */     else if (index1 != -1) {
/* 293 */       ProduceMediaTypeExpression expr1 = (ProduceMediaTypeExpression)condition1.getExpressionsToCompare().get(index1);
/* 294 */       ProduceMediaTypeExpression expr2 = (ProduceMediaTypeExpression)condition2.getExpressionsToCompare().get(index2);
/* 295 */       result = expr1.compareTo(expr2);
/* 296 */       result = result != 0 ? result : expr1.getMediaType().compareTo(expr2.getMediaType());
/*     */     }
/* 298 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<ProduceMediaTypeExpression> getExpressionsToCompare()
/*     */   {
/* 306 */     return this.expressions.isEmpty() ? this.MEDIA_TYPE_ALL_LIST : this.expressions;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   class ProduceMediaTypeExpression
/*     */     extends AbstractMediaTypeExpression
/*     */   {
/*     */     ProduceMediaTypeExpression(MediaType mediaType, boolean negated)
/*     */     {
/* 316 */       super(negated);
/*     */     }
/*     */     
/*     */     ProduceMediaTypeExpression(String expression) {
/* 320 */       super();
/*     */     }
/*     */     
/*     */     public final boolean match(List<MediaType> acceptedMediaTypes) {
/* 324 */       boolean match = matchMediaType(acceptedMediaTypes);
/* 325 */       return !match ? true : !isNegated() ? match : false;
/*     */     }
/*     */     
/*     */     private boolean matchMediaType(List<MediaType> acceptedMediaTypes) {
/* 329 */       for (MediaType acceptedMediaType : acceptedMediaTypes) {
/* 330 */         if (getMediaType().isCompatibleWith(acceptedMediaType)) {
/* 331 */           return true;
/*     */         }
/*     */       }
/* 334 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\condition\ProducesRequestCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */