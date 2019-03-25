/*    */ package javax.security.auth.message;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MessagePolicy
/*    */ {
/*    */   private final TargetPolicy[] targetPolicies;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private final boolean mandatory;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public MessagePolicy(TargetPolicy[] targetPolicies, boolean mandatory)
/*    */   {
/* 25 */     if (targetPolicies == null) {
/* 26 */       throw new IllegalArgumentException("targetPolicies is null");
/*    */     }
/* 28 */     this.targetPolicies = targetPolicies;
/* 29 */     this.mandatory = mandatory;
/*    */   }
/*    */   
/*    */   public boolean isMandatory() {
/* 33 */     return this.mandatory;
/*    */   }
/*    */   
/*    */   public TargetPolicy[] getTargetPolicies() {
/* 37 */     if (this.targetPolicies.length == 0) {
/* 38 */       return null;
/*    */     }
/* 40 */     return this.targetPolicies;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static class TargetPolicy
/*    */   {
/*    */     private final MessagePolicy.Target[] targets;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     private final MessagePolicy.ProtectionPolicy protectionPolicy;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     public TargetPolicy(MessagePolicy.Target[] targets, MessagePolicy.ProtectionPolicy protectionPolicy)
/*    */     {
/* 67 */       if (protectionPolicy == null) {
/* 68 */         throw new IllegalArgumentException("protectionPolicy is null");
/*    */       }
/* 70 */       this.targets = targets;
/* 71 */       this.protectionPolicy = protectionPolicy;
/*    */     }
/*    */     
/*    */     public MessagePolicy.Target[] getTargets() {
/* 75 */       if ((this.targets == null) || (this.targets.length == 0)) {
/* 76 */         return null;
/*    */       }
/* 78 */       return this.targets;
/*    */     }
/*    */     
/*    */     public MessagePolicy.ProtectionPolicy getProtectionPolicy() {
/* 82 */       return this.protectionPolicy;
/*    */     }
/*    */   }
/*    */   
/*    */   public static abstract interface Target
/*    */   {
/*    */     public abstract Object get(MessageInfo paramMessageInfo);
/*    */     
/*    */     public abstract void remove(MessageInfo paramMessageInfo);
/*    */     
/*    */     public abstract void put(MessageInfo paramMessageInfo, Object paramObject);
/*    */   }
/*    */   
/*    */   public static abstract interface ProtectionPolicy
/*    */   {
/*    */     public static final String AUTHENTICATE_SENDER = "#authenticateSender";
/*    */     public static final String AUTHENTICATE_CONTENT = "#authenticateContent";
/*    */     public static final String AUTHENTICATE_RECIPIENT = "#authenticateRecipient";
/*    */     
/*    */     public abstract String getID();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\security\auth\message\MessagePolicy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */