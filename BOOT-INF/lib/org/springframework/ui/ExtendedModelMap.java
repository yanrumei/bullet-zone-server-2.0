/*    */ package org.springframework.ui;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
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
/*    */ public class ExtendedModelMap
/*    */   extends ModelMap
/*    */   implements Model
/*    */ {
/*    */   public ExtendedModelMap addAttribute(String attributeName, Object attributeValue)
/*    */   {
/* 39 */     super.addAttribute(attributeName, attributeValue);
/* 40 */     return this;
/*    */   }
/*    */   
/*    */   public ExtendedModelMap addAttribute(Object attributeValue)
/*    */   {
/* 45 */     super.addAttribute(attributeValue);
/* 46 */     return this;
/*    */   }
/*    */   
/*    */   public ExtendedModelMap addAllAttributes(Collection<?> attributeValues)
/*    */   {
/* 51 */     super.addAllAttributes(attributeValues);
/* 52 */     return this;
/*    */   }
/*    */   
/*    */   public ExtendedModelMap addAllAttributes(Map<String, ?> attributes)
/*    */   {
/* 57 */     super.addAllAttributes(attributes);
/* 58 */     return this;
/*    */   }
/*    */   
/*    */   public ExtendedModelMap mergeAttributes(Map<String, ?> attributes)
/*    */   {
/* 63 */     super.mergeAttributes(attributes);
/* 64 */     return this;
/*    */   }
/*    */   
/*    */   public Map<String, Object> asMap()
/*    */   {
/* 69 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframewor\\ui\ExtendedModelMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */