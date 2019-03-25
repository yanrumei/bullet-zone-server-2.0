/*    */ package org.springframework.boot.autoconfigure.web;
/*    */ 
/*    */ import java.util.Set;
/*    */ import org.springframework.util.PropertyPlaceholderHelper;
/*    */ import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;
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
/*    */ class NonRecursivePropertyPlaceholderHelper
/*    */   extends PropertyPlaceholderHelper
/*    */ {
/*    */   NonRecursivePropertyPlaceholderHelper(String placeholderPrefix, String placeholderSuffix)
/*    */   {
/* 32 */     super(placeholderPrefix, placeholderSuffix);
/*    */   }
/*    */   
/*    */ 
/*    */   protected String parseStringValue(String strVal, PropertyPlaceholderHelper.PlaceholderResolver placeholderResolver, Set<String> visitedPlaceholders)
/*    */   {
/* 38 */     return super.parseStringValue(strVal, new NonRecursivePlaceholderResolver(placeholderResolver), visitedPlaceholders);
/*    */   }
/*    */   
/*    */   private static class NonRecursivePlaceholderResolver
/*    */     implements PropertyPlaceholderHelper.PlaceholderResolver
/*    */   {
/*    */     private final PropertyPlaceholderHelper.PlaceholderResolver resolver;
/*    */     
/*    */     NonRecursivePlaceholderResolver(PropertyPlaceholderHelper.PlaceholderResolver resolver)
/*    */     {
/* 48 */       this.resolver = resolver;
/*    */     }
/*    */     
/*    */     public String resolvePlaceholder(String placeholderName)
/*    */     {
/* 53 */       if ((this.resolver instanceof NonRecursivePlaceholderResolver)) {
/* 54 */         return null;
/*    */       }
/* 56 */       return this.resolver.resolvePlaceholder(placeholderName);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\NonRecursivePropertyPlaceholderHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */