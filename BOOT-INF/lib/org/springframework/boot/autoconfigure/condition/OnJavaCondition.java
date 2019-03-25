/*    */ package org.springframework.boot.autoconfigure.condition;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.springframework.context.annotation.ConditionContext;
/*    */ import org.springframework.core.annotation.Order;
/*    */ import org.springframework.core.type.AnnotatedTypeMetadata;
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
/*    */ @Order(-2147483628)
/*    */ class OnJavaCondition
/*    */   extends SpringBootCondition
/*    */ {
/* 40 */   private static final ConditionalOnJava.JavaVersion JVM_VERSION = ;
/*    */   
/*    */ 
/*    */ 
/*    */   public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*    */   {
/* 46 */     Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnJava.class.getName());
/* 47 */     ConditionalOnJava.Range range = (ConditionalOnJava.Range)attributes.get("range");
/* 48 */     ConditionalOnJava.JavaVersion version = (ConditionalOnJava.JavaVersion)attributes.get("value");
/* 49 */     return getMatchOutcome(range, JVM_VERSION, version);
/*    */   }
/*    */   
/*    */   protected ConditionOutcome getMatchOutcome(ConditionalOnJava.Range range, ConditionalOnJava.JavaVersion runningVersion, ConditionalOnJava.JavaVersion version)
/*    */   {
/* 54 */     boolean match = runningVersion.isWithin(range, version);
/* 55 */     String expected = String.format(range == ConditionalOnJava.Range.EQUAL_OR_NEWER ? "(%s or newer)" : "(older than %s)", new Object[] { version });
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 60 */     ConditionMessage message = ConditionMessage.forCondition(ConditionalOnJava.class, new Object[] { expected }).foundExactly(runningVersion);
/* 61 */     return new ConditionOutcome(match, message);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\condition\OnJavaCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */