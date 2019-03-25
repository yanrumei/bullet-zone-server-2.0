/*    */ package org.springframework.boot.bind;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import org.springframework.util.PatternMatchUtils;
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
/*    */ class PatternPropertyNamePatternsMatcher
/*    */   implements PropertyNamePatternsMatcher
/*    */ {
/*    */   private final String[] patterns;
/*    */   
/*    */   PatternPropertyNamePatternsMatcher(Collection<String> patterns)
/*    */   {
/* 36 */     this.patterns = (patterns == null ? new String[0] : (String[])patterns.toArray(new String[patterns.size()]));
/*    */   }
/*    */   
/*    */   public boolean matches(String propertyName)
/*    */   {
/* 41 */     return PatternMatchUtils.simpleMatch(this.patterns, propertyName);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\bind\PatternPropertyNamePatternsMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */