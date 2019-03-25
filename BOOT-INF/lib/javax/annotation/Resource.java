/*    */ package javax.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Target({java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD})
/*    */ @Retention(RetentionPolicy.RUNTIME)
/*    */ public @interface Resource
/*    */ {
/*    */   String name() default "";
/*    */   
/*    */   Class<?> type() default Object.class;
/*    */   
/*    */   AuthenticationType authenticationType() default AuthenticationType.CONTAINER;
/*    */   
/*    */   boolean shareable() default true;
/*    */   
/*    */   String description() default "";
/*    */   
/*    */   String mappedName() default "";
/*    */   
/*    */   String lookup() default "";
/*    */   
/*    */   public static enum AuthenticationType
/*    */   {
/* 31 */     CONTAINER, 
/* 32 */     APPLICATION;
/*    */     
/*    */     private AuthenticationType() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-annotations-api-8.5.27.jar!\javax\annotation\Resource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */