/*    */ package org.springframework.boot.autoconfigure.condition;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase;
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
/*    */ public abstract class NoneNestedConditions
/*    */   extends AbstractNestedCondition
/*    */ {
/*    */   public NoneNestedConditions(ConfigurationCondition.ConfigurationPhase configurationPhase)
/*    */   {
/* 58 */     super(configurationPhase);
/*    */   }
/*    */   
/*    */   protected ConditionOutcome getFinalMatchOutcome(AbstractNestedCondition.MemberMatchOutcomes memberOutcomes)
/*    */   {
/* 63 */     boolean match = memberOutcomes.getMatches().isEmpty();
/* 64 */     List<ConditionMessage> messages = new ArrayList();
/* 65 */     messages.add(ConditionMessage.forCondition("NoneNestedConditions", new Object[0])
/* 66 */       .because(memberOutcomes.getMatches().size() + " matched " + memberOutcomes
/* 67 */       .getNonMatches().size() + " did not"));
/* 68 */     for (ConditionOutcome outcome : memberOutcomes.getAll()) {
/* 69 */       messages.add(outcome.getConditionMessage());
/*    */     }
/* 71 */     return new ConditionOutcome(match, ConditionMessage.of(messages));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\condition\NoneNestedConditions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */