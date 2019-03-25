/*    */ package javax.security.auth.message.callback;
/*    */ 
/*    */ import javax.security.auth.Subject;
/*    */ import javax.security.auth.callback.Callback;
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
/*    */ public class GroupPrincipalCallback
/*    */   implements Callback
/*    */ {
/*    */   private final Subject subject;
/*    */   private final String[] groups;
/*    */   
/*    */   public GroupPrincipalCallback(Subject subject, String[] groups)
/*    */   {
/* 32 */     this.subject = subject;
/* 33 */     this.groups = groups;
/*    */   }
/*    */   
/*    */   public Subject getSubject() {
/* 37 */     return this.subject;
/*    */   }
/*    */   
/*    */   public String[] getGroups() {
/* 41 */     return this.groups;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\security\auth\message\callback\GroupPrincipalCallback.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */