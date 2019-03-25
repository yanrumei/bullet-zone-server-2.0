/*     */ package org.springframework.web.servlet.mvc.condition;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.servlet.DispatcherType;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
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
/*     */ public final class RequestMethodsRequestCondition
/*     */   extends AbstractRequestCondition<RequestMethodsRequestCondition>
/*     */ {
/*  43 */   private static final RequestMethodsRequestCondition GET_CONDITION = new RequestMethodsRequestCondition(new RequestMethod[] { RequestMethod.GET });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Set<RequestMethod> methods;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RequestMethodsRequestCondition(RequestMethod... requestMethods)
/*     */   {
/*  56 */     this(asList(requestMethods));
/*     */   }
/*     */   
/*     */   private RequestMethodsRequestCondition(Collection<RequestMethod> requestMethods) {
/*  60 */     this.methods = Collections.unmodifiableSet(new LinkedHashSet(requestMethods));
/*     */   }
/*     */   
/*     */   private static List<RequestMethod> asList(RequestMethod... requestMethods)
/*     */   {
/*  65 */     return requestMethods != null ? Arrays.asList(requestMethods) : Collections.emptyList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<RequestMethod> getMethods()
/*     */   {
/*  73 */     return this.methods;
/*     */   }
/*     */   
/*     */   protected Collection<RequestMethod> getContent()
/*     */   {
/*  78 */     return this.methods;
/*     */   }
/*     */   
/*     */   protected String getToStringInfix()
/*     */   {
/*  83 */     return " || ";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RequestMethodsRequestCondition combine(RequestMethodsRequestCondition other)
/*     */   {
/*  92 */     Set<RequestMethod> set = new LinkedHashSet(this.methods);
/*  93 */     set.addAll(other.methods);
/*  94 */     return new RequestMethodsRequestCondition(set);
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
/*     */   public RequestMethodsRequestCondition getMatchingCondition(HttpServletRequest request)
/*     */   {
/* 108 */     if (CorsUtils.isPreFlightRequest(request)) {
/* 109 */       return matchPreFlight(request);
/*     */     }
/*     */     
/* 112 */     if (getMethods().isEmpty()) {
/* 113 */       if ((RequestMethod.OPTIONS.name().equals(request.getMethod())) && 
/* 114 */         (!DispatcherType.ERROR.equals(request.getDispatcherType())))
/*     */       {
/* 116 */         return null;
/*     */       }
/* 118 */       return this;
/*     */     }
/*     */     
/* 121 */     return matchRequestMethod(request.getMethod());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private RequestMethodsRequestCondition matchPreFlight(HttpServletRequest request)
/*     */   {
/* 130 */     if (getMethods().isEmpty()) {
/* 131 */       return this;
/*     */     }
/* 133 */     String expectedMethod = request.getHeader("Access-Control-Request-Method");
/* 134 */     return matchRequestMethod(expectedMethod);
/*     */   }
/*     */   
/*     */   private RequestMethodsRequestCondition matchRequestMethod(String httpMethodValue) {
/* 138 */     HttpMethod httpMethod = HttpMethod.resolve(httpMethodValue);
/* 139 */     if (httpMethod != null) {
/* 140 */       for (RequestMethod method : getMethods()) {
/* 141 */         if (httpMethod.matches(method.name())) {
/* 142 */           return new RequestMethodsRequestCondition(new RequestMethod[] { method });
/*     */         }
/*     */       }
/* 145 */       if ((httpMethod == HttpMethod.HEAD) && (getMethods().contains(RequestMethod.GET))) {
/* 146 */         return GET_CONDITION;
/*     */       }
/*     */     }
/* 149 */     return null;
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
/*     */   public int compareTo(RequestMethodsRequestCondition other, HttpServletRequest request)
/*     */   {
/* 165 */     if (other.methods.size() != this.methods.size()) {
/* 166 */       return other.methods.size() - this.methods.size();
/*     */     }
/* 168 */     if (this.methods.size() == 1) {
/* 169 */       if ((this.methods.contains(RequestMethod.HEAD)) && (other.methods.contains(RequestMethod.GET))) {
/* 170 */         return -1;
/*     */       }
/* 172 */       if ((this.methods.contains(RequestMethod.GET)) && (other.methods.contains(RequestMethod.HEAD))) {
/* 173 */         return 1;
/*     */       }
/*     */     }
/* 176 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\condition\RequestMethodsRequestCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */