/*    */ package javax.servlet.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.annotation.Documented;
/*    */ import java.lang.annotation.Inherited;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
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
/*    */ @Inherited
/*    */ @Target({java.lang.annotation.ElementType.TYPE})
/*    */ @Retention(RetentionPolicy.RUNTIME)
/*    */ @Documented
/*    */ public @interface ServletSecurity
/*    */ {
/*    */   HttpConstraint value() default @HttpConstraint;
/*    */   
/*    */   HttpMethodConstraint[] httpMethodConstraints() default {};
/*    */   
/*    */   public static enum EmptyRoleSemantic
/*    */   {
/* 51 */     PERMIT, 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 56 */     DENY;
/*    */     
/*    */ 
/*    */ 
/*    */     private EmptyRoleSemantic() {}
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static enum TransportGuarantee
/*    */   {
/* 67 */     NONE, 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 72 */     CONFIDENTIAL;
/*    */     
/*    */     private TransportGuarantee() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\annotation\ServletSecurity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */