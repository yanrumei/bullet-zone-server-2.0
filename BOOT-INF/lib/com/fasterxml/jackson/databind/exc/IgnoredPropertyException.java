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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IgnoredPropertyException
/*    */   extends PropertyBindingException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public IgnoredPropertyException(JsonParser p, String msg, JsonLocation loc, Class<?> referringClass, String propName, Collection<Object> propertyIds)
/*    */   {
/* 28 */     super(p, msg, loc, referringClass, propName, propertyIds);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   public IgnoredPropertyException(String msg, JsonLocation loc, Class<?> referringClass, String propName, Collection<Object> propertyIds)
/*    */   {
/* 39 */     super(msg, loc, referringClass, propName, propertyIds);
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
/*    */ 
/*    */   public static IgnoredPropertyException from(JsonParser p, Object fromObjectOrClass, String propertyName, Collection<Object> propertyIds)
/*    */   {
/* 57 */     if (fromObjectOrClass == null)
/* 58 */       throw new IllegalArgumentException();
/*    */     Class<?> ref;
/*    */     Class<?> ref;
/* 61 */     if ((fromObjectOrClass instanceof Class)) {
/* 62 */       ref = (Class)fromObjectOrClass;
/*    */     } else {
/* 64 */       ref = fromObjectOrClass.getClass();
/*    */     }
/* 66 */     String msg = "Ignored field \"" + propertyName + "\" (class " + ref.getName() + ") encountered; mapper configured not to allow this";
/*    */     
/* 68 */     IgnoredPropertyException e = new IgnoredPropertyException(p, msg, p.getCurrentLocation(), ref, propertyName, propertyIds);
/*    */     
/*    */ 
/* 71 */     e.prependPath(fromObjectOrClass, propertyName);
/* 72 */     return e;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\exc\IgnoredPropertyException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */