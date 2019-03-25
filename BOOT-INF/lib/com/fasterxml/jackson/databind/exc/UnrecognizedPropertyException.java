/*    */ package com.fasterxml.jackson.databind.exc;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonLocation;
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import java.util.Collection;
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
/*    */ public class UnrecognizedPropertyException
/*    */   extends PropertyBindingException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public UnrecognizedPropertyException(JsonParser p, String msg, JsonLocation loc, Class<?> referringClass, String propName, Collection<Object> propertyIds)
/*    */   {
/* 24 */     super(p, msg, loc, referringClass, propName, propertyIds);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   public UnrecognizedPropertyException(String msg, JsonLocation loc, Class<?> referringClass, String propName, Collection<Object> propertyIds)
/*    */   {
/* 35 */     super(msg, loc, referringClass, propName, propertyIds);
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static UnrecognizedPropertyException from(JsonParser p, Object fromObjectOrClass, String propertyName, Collection<Object> propertyIds)
/*    */   {
/* 52 */     if (fromObjectOrClass == null)
/* 53 */       throw new IllegalArgumentException();
/*    */     Class<?> ref;
/*    */     Class<?> ref;
/* 56 */     if ((fromObjectOrClass instanceof Class)) {
/* 57 */       ref = (Class)fromObjectOrClass;
/*    */     } else {
/* 59 */       ref = fromObjectOrClass.getClass();
/*    */     }
/* 61 */     String msg = "Unrecognized field \"" + propertyName + "\" (class " + ref.getName() + "), not marked as ignorable";
/* 62 */     UnrecognizedPropertyException e = new UnrecognizedPropertyException(p, msg, p.getCurrentLocation(), ref, propertyName, propertyIds);
/*    */     
/*    */ 
/* 65 */     e.prependPath(fromObjectOrClass, propertyName);
/* 66 */     return e;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\exc\UnrecognizedPropertyException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */