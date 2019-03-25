/*     */ package org.springframework.aop.aspectj;
/*     */ 
/*     */ import org.aspectj.weaver.tools.PointcutParser;
/*     */ import org.aspectj.weaver.tools.TypePatternMatcher;
/*     */ import org.springframework.aop.ClassFilter;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class TypePatternClassFilter
/*     */   implements ClassFilter
/*     */ {
/*     */   private String typePattern;
/*     */   private TypePatternMatcher aspectJTypePatternMatcher;
/*     */   
/*     */   public TypePatternClassFilter() {}
/*     */   
/*     */   public TypePatternClassFilter(String typePattern)
/*     */   {
/*  58 */     setTypePattern(typePattern);
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
/*     */   public void setTypePattern(String typePattern)
/*     */   {
/*  80 */     Assert.notNull(typePattern, "Type pattern must not be null");
/*  81 */     this.typePattern = typePattern;
/*     */     
/*     */ 
/*  84 */     this.aspectJTypePatternMatcher = PointcutParser.getPointcutParserSupportingAllPrimitivesAndUsingContextClassloaderForResolution().parseTypePattern(replaceBooleanOperators(typePattern));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getTypePattern()
/*     */   {
/*  91 */     return this.typePattern;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean matches(Class<?> clazz)
/*     */   {
/* 103 */     Assert.state(this.aspectJTypePatternMatcher != null, "No type pattern has been set");
/* 104 */     return this.aspectJTypePatternMatcher.matches(clazz);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String replaceBooleanOperators(String pcExpr)
/*     */   {
/* 114 */     String result = StringUtils.replace(pcExpr, " and ", " && ");
/* 115 */     result = StringUtils.replace(result, " or ", " || ");
/* 116 */     return StringUtils.replace(result, " not ", " ! ");
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\aspectj\TypePatternClassFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */