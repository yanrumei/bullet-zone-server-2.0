/*     */ package org.springframework.web.servlet.mvc.condition;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.util.AntPathMatcher;
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.util.UrlPathHelper;
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
/*     */ public final class PatternsRequestCondition
/*     */   extends AbstractRequestCondition<PatternsRequestCondition>
/*     */ {
/*     */   private final Set<String> patterns;
/*     */   private final UrlPathHelper pathHelper;
/*     */   private final PathMatcher pathMatcher;
/*     */   private final boolean useSuffixPatternMatch;
/*     */   private final boolean useTrailingSlashMatch;
/*  54 */   private final List<String> fileExtensions = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PatternsRequestCondition(String... patterns)
/*     */   {
/*  63 */     this(asList(patterns), null, null, true, true, null);
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
/*     */   public PatternsRequestCondition(String[] patterns, UrlPathHelper urlPathHelper, PathMatcher pathMatcher, boolean useSuffixPatternMatch, boolean useTrailingSlashMatch)
/*     */   {
/*  78 */     this(asList(patterns), urlPathHelper, pathMatcher, useSuffixPatternMatch, useTrailingSlashMatch, null);
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
/*     */   public PatternsRequestCondition(String[] patterns, UrlPathHelper urlPathHelper, PathMatcher pathMatcher, boolean useSuffixPatternMatch, boolean useTrailingSlashMatch, List<String> fileExtensions)
/*     */   {
/*  95 */     this(asList(patterns), urlPathHelper, pathMatcher, useSuffixPatternMatch, useTrailingSlashMatch, fileExtensions);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private PatternsRequestCondition(Collection<String> patterns, UrlPathHelper urlPathHelper, PathMatcher pathMatcher, boolean useSuffixPatternMatch, boolean useTrailingSlashMatch, List<String> fileExtensions)
/*     */   {
/* 105 */     this.patterns = Collections.unmodifiableSet(prependLeadingSlash(patterns));
/* 106 */     this.pathHelper = (urlPathHelper != null ? urlPathHelper : new UrlPathHelper());
/* 107 */     this.pathMatcher = (pathMatcher != null ? pathMatcher : new AntPathMatcher());
/* 108 */     this.useSuffixPatternMatch = useSuffixPatternMatch;
/* 109 */     this.useTrailingSlashMatch = useTrailingSlashMatch;
/* 110 */     if (fileExtensions != null) {
/* 111 */       for (String fileExtension : fileExtensions) {
/* 112 */         if (fileExtension.charAt(0) != '.') {
/* 113 */           fileExtension = "." + fileExtension;
/*     */         }
/* 115 */         this.fileExtensions.add(fileExtension);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static List<String> asList(String... patterns)
/*     */   {
/* 122 */     return patterns != null ? Arrays.asList(patterns) : Collections.emptyList();
/*     */   }
/*     */   
/*     */   private static Set<String> prependLeadingSlash(Collection<String> patterns) {
/* 126 */     if (patterns == null) {
/* 127 */       return Collections.emptySet();
/*     */     }
/* 129 */     Set<String> result = new LinkedHashSet(patterns.size());
/* 130 */     for (String pattern : patterns) {
/* 131 */       if ((StringUtils.hasLength(pattern)) && (!pattern.startsWith("/"))) {
/* 132 */         pattern = "/" + pattern;
/*     */       }
/* 134 */       result.add(pattern);
/*     */     }
/* 136 */     return result;
/*     */   }
/*     */   
/*     */   public Set<String> getPatterns() {
/* 140 */     return this.patterns;
/*     */   }
/*     */   
/*     */   protected Collection<String> getContent()
/*     */   {
/* 145 */     return this.patterns;
/*     */   }
/*     */   
/*     */   protected String getToStringInfix()
/*     */   {
/* 150 */     return " || ";
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
/*     */   public PatternsRequestCondition combine(PatternsRequestCondition other)
/*     */   {
/* 165 */     Set<String> result = new LinkedHashSet();
/* 166 */     Iterator localIterator1; String pattern1; if ((!this.patterns.isEmpty()) && (!other.patterns.isEmpty())) {
/* 167 */       for (localIterator1 = this.patterns.iterator(); localIterator1.hasNext();) { pattern1 = (String)localIterator1.next();
/* 168 */         for (String pattern2 : other.patterns) {
/* 169 */           result.add(this.pathMatcher.combine(pattern1, pattern2));
/*     */         }
/*     */         
/*     */       }
/* 173 */     } else if (!this.patterns.isEmpty()) {
/* 174 */       result.addAll(this.patterns);
/*     */     }
/* 176 */     else if (!other.patterns.isEmpty()) {
/* 177 */       result.addAll(other.patterns);
/*     */     }
/*     */     else {
/* 180 */       result.add("");
/*     */     }
/* 182 */     return new PatternsRequestCondition(result, this.pathHelper, this.pathMatcher, this.useSuffixPatternMatch, this.useTrailingSlashMatch, this.fileExtensions);
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
/*     */ 
/*     */   public PatternsRequestCondition getMatchingCondition(HttpServletRequest request)
/*     */   {
/* 205 */     if (this.patterns.isEmpty()) {
/* 206 */       return this;
/*     */     }
/*     */     
/* 209 */     String lookupPath = this.pathHelper.getLookupPathForRequest(request);
/* 210 */     List<String> matches = getMatchingPatterns(lookupPath);
/*     */     
/* 212 */     return matches.isEmpty() ? null : new PatternsRequestCondition(matches, this.pathHelper, this.pathMatcher, this.useSuffixPatternMatch, this.useTrailingSlashMatch, this.fileExtensions);
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
/*     */   public List<String> getMatchingPatterns(String lookupPath)
/*     */   {
/* 227 */     List<String> matches = new ArrayList();
/* 228 */     for (String pattern : this.patterns) {
/* 229 */       String match = getMatchingPattern(pattern, lookupPath);
/* 230 */       if (match != null) {
/* 231 */         matches.add(match);
/*     */       }
/*     */     }
/* 234 */     Collections.sort(matches, this.pathMatcher.getPatternComparator(lookupPath));
/* 235 */     return matches;
/*     */   }
/*     */   
/*     */   private String getMatchingPattern(String pattern, String lookupPath) {
/* 239 */     if (pattern.equals(lookupPath)) {
/* 240 */       return pattern;
/*     */     }
/* 242 */     if (this.useSuffixPatternMatch) {
/* 243 */       if ((!this.fileExtensions.isEmpty()) && (lookupPath.indexOf('.') != -1)) {
/* 244 */         for (String extension : this.fileExtensions) {
/* 245 */           if (this.pathMatcher.match(pattern + extension, lookupPath)) {
/* 246 */             return pattern + extension;
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 251 */         boolean hasSuffix = pattern.indexOf('.') != -1;
/* 252 */         if ((!hasSuffix) && (this.pathMatcher.match(pattern + ".*", lookupPath))) {
/* 253 */           return pattern + ".*";
/*     */         }
/*     */       }
/*     */     }
/* 257 */     if (this.pathMatcher.match(pattern, lookupPath)) {
/* 258 */       return pattern;
/*     */     }
/* 260 */     if ((this.useTrailingSlashMatch) && 
/* 261 */       (!pattern.endsWith("/")) && (this.pathMatcher.match(pattern + "/", lookupPath))) {
/* 262 */       return pattern + "/";
/*     */     }
/*     */     
/* 265 */     return null;
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
/*     */   public int compareTo(PatternsRequestCondition other, HttpServletRequest request)
/*     */   {
/* 281 */     String lookupPath = this.pathHelper.getLookupPathForRequest(request);
/* 282 */     Comparator<String> patternComparator = this.pathMatcher.getPatternComparator(lookupPath);
/* 283 */     Iterator<String> iterator = this.patterns.iterator();
/* 284 */     Iterator<String> iteratorOther = other.patterns.iterator();
/* 285 */     while ((iterator.hasNext()) && (iteratorOther.hasNext())) {
/* 286 */       int result = patternComparator.compare(iterator.next(), iteratorOther.next());
/* 287 */       if (result != 0) {
/* 288 */         return result;
/*     */       }
/*     */     }
/* 291 */     if (iterator.hasNext()) {
/* 292 */       return -1;
/*     */     }
/* 294 */     if (iteratorOther.hasNext()) {
/* 295 */       return 1;
/*     */     }
/*     */     
/* 298 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\condition\PatternsRequestCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */