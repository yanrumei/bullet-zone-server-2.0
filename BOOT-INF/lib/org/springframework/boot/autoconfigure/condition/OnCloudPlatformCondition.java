/*    */ package org.springframework.boot.autoconfigure.condition;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.springframework.boot.cloud.CloudPlatform;
/*    */ import org.springframework.context.annotation.ConditionContext;
/*    */ import org.springframework.core.env.Environment;
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
/*    */ class OnCloudPlatformCondition
/*    */   extends SpringBootCondition
/*    */ {
/*    */   public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*    */   {
/* 39 */     Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnCloudPlatform.class.getName());
/* 40 */     CloudPlatform cloudPlatform = (CloudPlatform)attributes.get("value");
/* 41 */     return getMatchOutcome(context.getEnvironment(), cloudPlatform);
/*    */   }
/*    */   
/*    */   private ConditionOutcome getMatchOutcome(Environment environment, CloudPlatform cloudPlatform)
/*    */   {
/* 46 */     String name = cloudPlatform.name();
/*    */     
/* 48 */     ConditionMessage.Builder message = ConditionMessage.forCondition(ConditionalOnCloudPlatform.class, new Object[0]);
/* 49 */     if (cloudPlatform.isActive(environment)) {
/* 50 */       return ConditionOutcome.match(message.foundExactly(name));
/*    */     }
/* 52 */     return ConditionOutcome.noMatch(message.didNotFind(name).atAll());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\condition\OnCloudPlatformCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */