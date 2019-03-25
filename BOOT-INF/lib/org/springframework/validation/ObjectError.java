/*    */ package org.springframework.validation;
/*    */ 
/*    */ import org.springframework.context.support.DefaultMessageSourceResolvable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ObjectError
/*    */   extends DefaultMessageSourceResolvable
/*    */ {
/*    */   private final String objectName;
/*    */   
/*    */   public ObjectError(String objectName, String defaultMessage)
/*    */   {
/* 46 */     this(objectName, null, null, defaultMessage);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ObjectError(String objectName, String[] codes, Object[] arguments, String defaultMessage)
/*    */   {
/* 57 */     super(codes, arguments, defaultMessage);
/* 58 */     Assert.notNull(objectName, "Object name must not be null");
/* 59 */     this.objectName = objectName;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getObjectName()
/*    */   {
/* 67 */     return this.objectName;
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 73 */     return "Error in object '" + this.objectName + "': " + resolvableToString();
/*    */   }
/*    */   
/*    */   public boolean equals(Object other)
/*    */   {
/* 78 */     if (this == other) {
/* 79 */       return true;
/*    */     }
/* 81 */     if ((other == null) || (other.getClass() != getClass()) || (!super.equals(other))) {
/* 82 */       return false;
/*    */     }
/* 84 */     ObjectError otherError = (ObjectError)other;
/* 85 */     return getObjectName().equals(otherError.getObjectName());
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 90 */     return super.hashCode() * 29 + getObjectName().hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\validation\ObjectError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */