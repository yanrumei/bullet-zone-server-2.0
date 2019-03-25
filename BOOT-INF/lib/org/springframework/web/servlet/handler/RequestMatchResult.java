/*    */ package org.springframework.web.servlet.handler;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.PathMatcher;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RequestMatchResult
/*    */ {
/*    */   private final String matchingPattern;
/*    */   private final String lookupPath;
/*    */   private final PathMatcher pathMatcher;
/*    */   
/*    */   public RequestMatchResult(String matchingPattern, String lookupPath, PathMatcher pathMatcher)
/*    */   {
/* 49 */     Assert.hasText(matchingPattern, "'matchingPattern' is required");
/* 50 */     Assert.hasText(lookupPath, "'lookupPath' is required");
/* 51 */     Assert.notNull(pathMatcher, "'pathMatcher' is required");
/* 52 */     this.matchingPattern = matchingPattern;
/* 53 */     this.lookupPath = lookupPath;
/* 54 */     this.pathMatcher = pathMatcher;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Map<String, String> extractUriTemplateVariables()
/*    */   {
/* 64 */     return this.pathMatcher.extractUriTemplateVariables(this.matchingPattern, this.lookupPath);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\handler\RequestMatchResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */