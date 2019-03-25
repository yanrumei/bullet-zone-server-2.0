/*    */ package org.springframework.beans.factory.support;
/*    */ 
/*    */ import org.springframework.beans.BeanMetadataAttributeAccessor;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AutowireCandidateQualifier
/*    */   extends BeanMetadataAttributeAccessor
/*    */ {
/* 35 */   public static String VALUE_KEY = "value";
/*    */   
/*    */ 
/*    */ 
/*    */   private final String typeName;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public AutowireCandidateQualifier(Class<?> type)
/*    */   {
/* 46 */     this(type.getName());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public AutowireCandidateQualifier(String typeName)
/*    */   {
/* 57 */     Assert.notNull(typeName, "Type name must not be null");
/* 58 */     this.typeName = typeName;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public AutowireCandidateQualifier(Class<?> type, Object value)
/*    */   {
/* 69 */     this(type.getName(), value);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public AutowireCandidateQualifier(String typeName, Object value)
/*    */   {
/* 82 */     Assert.notNull(typeName, "Type name must not be null");
/* 83 */     this.typeName = typeName;
/* 84 */     setAttribute(VALUE_KEY, value);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getTypeName()
/*    */   {
/* 94 */     return this.typeName;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\support\AutowireCandidateQualifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */