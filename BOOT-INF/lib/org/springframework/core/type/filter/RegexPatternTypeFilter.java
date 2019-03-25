/*    */ package org.springframework.core.type.filter;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import org.springframework.core.type.ClassMetadata;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class RegexPatternTypeFilter
/*    */   extends AbstractClassTestingTypeFilter
/*    */ {
/*    */   private final Pattern pattern;
/*    */   
/*    */   public RegexPatternTypeFilter(Pattern pattern)
/*    */   {
/* 37 */     Assert.notNull(pattern, "Pattern must not be null");
/* 38 */     this.pattern = pattern;
/*    */   }
/*    */   
/*    */ 
/*    */   protected boolean match(ClassMetadata metadata)
/*    */   {
/* 44 */     return this.pattern.matcher(metadata.getClassName()).matches();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\type\filter\RegexPatternTypeFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */