/*    */ package org.springframework.core.env;
/*    */ 
/*    */ import java.util.LinkedHashSet;
/*    */ import java.util.Set;
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
/*    */ public class MissingRequiredPropertiesException
/*    */   extends IllegalStateException
/*    */ {
/* 34 */   private final Set<String> missingRequiredProperties = new LinkedHashSet();
/*    */   
/*    */   void addMissingRequiredProperty(String key)
/*    */   {
/* 38 */     this.missingRequiredProperties.add(key);
/*    */   }
/*    */   
/*    */   public String getMessage()
/*    */   {
/* 43 */     return 
/* 44 */       "The following properties were declared as required but could not be resolved: " + getMissingRequiredProperties();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Set<String> getMissingRequiredProperties()
/*    */   {
/* 54 */     return this.missingRequiredProperties;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\env\MissingRequiredPropertiesException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */