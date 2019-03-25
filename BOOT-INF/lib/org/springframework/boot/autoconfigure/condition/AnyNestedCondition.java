/*    */ package org.springframework.boot.autoconfigure.condition;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase;
/*    */ import org.springframework.core.annotation.Order;
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
/*    */ @Order(2147483627)
/*    */ public abstract class AnyNestedCondition
/*    */   extends AbstractNestedCondition
/*    */ {
/*    */   public AnyNestedCondition(ConfigurationCondition.ConfigurationPhase configurationPhase)
/*    */   {
/* 61 */     super(configurationPhase);
/*    */   }
/*    */   
/*    */   protected ConditionOutcome getFinalMatchOutcome(AbstractNestedCondition.MemberMatchOutcomes memberOutcomes)
/*    */   {
/* 66 */     boolean match = !memberOutcomes.getMatches().isEmpty();
/* 67 */     List<ConditionMessage> messages = new ArrayList();
/* 68 */     messages.add(ConditionMessage.forCondition("AnyNestedCondition", new Object[0])
/* 69 */       .because(memberOutcomes.getMatches().size() + " matched " + memberOutcomes
/* 70 */       .getNonMatches().size() + " did not"));
/* 71 */     for (ConditionOutcome outcome : memberOutcomes.getAll()) {
/* 72 */       messages.add(outcome.getConditionMessage());
/*    */     }
/* 74 */     return new ConditionOutcome(match, ConditionMessage.of(messages));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\condition\AnyNestedCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */