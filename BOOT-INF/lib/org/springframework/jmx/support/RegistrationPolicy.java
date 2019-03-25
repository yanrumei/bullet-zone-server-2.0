/*    */ package org.springframework.jmx.support;
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
/*    */ public enum RegistrationPolicy
/*    */ {
/* 33 */   FAIL_ON_EXISTING, 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 39 */   IGNORE_EXISTING, 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 45 */   REPLACE_EXISTING;
/*    */   
/*    */ 
/*    */ 
/*    */   private RegistrationPolicy() {}
/*    */   
/*    */ 
/*    */ 
/*    */   static RegistrationPolicy valueOf(int registrationBehavior)
/*    */   {
/* 55 */     switch (registrationBehavior) {
/*    */     case 1: 
/* 57 */       return IGNORE_EXISTING;
/*    */     case 2: 
/* 59 */       return REPLACE_EXISTING;
/*    */     case 0: 
/* 61 */       return FAIL_ON_EXISTING;
/*    */     }
/* 63 */     throw new IllegalArgumentException("Unknown MBean registration behavior: " + registrationBehavior);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jmx\support\RegistrationPolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */