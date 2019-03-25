/*    */ package org.springframework.boot.autoconfigure;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.EventObject;
/*    */ import java.util.List;
/*    */ import java.util.Set;
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
/*    */ public class AutoConfigurationImportEvent
/*    */   extends EventObject
/*    */ {
/*    */   private final List<String> candidateConfigurations;
/*    */   private final Set<String> exclusions;
/*    */   
/*    */   public AutoConfigurationImportEvent(Object source, List<String> candidateConfigurations, Set<String> exclusions)
/*    */   {
/* 38 */     super(source);
/* 39 */     this.candidateConfigurations = 
/* 40 */       Collections.unmodifiableList(candidateConfigurations);
/* 41 */     this.exclusions = Collections.unmodifiableSet(exclusions);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public List<String> getCandidateConfigurations()
/*    */   {
/* 50 */     return this.candidateConfigurations;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Set<String> getExclusions()
/*    */   {
/* 58 */     return this.exclusions;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\AutoConfigurationImportEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */