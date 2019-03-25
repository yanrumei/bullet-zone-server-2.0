/*    */ package org.springframework.boot.autoconfigure.hazelcast;
/*    */ 
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.Builder;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
/*    */ import org.springframework.boot.autoconfigure.condition.ResourceCondition;
/*    */ import org.springframework.context.annotation.ConditionContext;
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
/*    */ public abstract class HazelcastConfigResourceCondition
/*    */   extends ResourceCondition
/*    */ {
/*    */   static final String CONFIG_SYSTEM_PROPERTY = "hazelcast.config";
/*    */   
/*    */   protected HazelcastConfigResourceCondition(String prefix, String propertyName)
/*    */   {
/* 38 */     super("Hazelcast", prefix, propertyName, new String[] { "file:./hazelcast.xml", "classpath:/hazelcast.xml" });
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected ConditionOutcome getResourceOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*    */   {
/* 45 */     if (System.getProperty("hazelcast.config") != null) {
/* 46 */       return ConditionOutcome.match(startConditionMessage()
/* 47 */         .because("System property 'hazelcast.config' is set."));
/*    */     }
/* 49 */     return super.getResourceOutcome(context, metadata);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\hazelcast\HazelcastConfigResourceCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */