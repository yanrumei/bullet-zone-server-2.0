/*    */ package javax.security.auth.message.callback;
/*    */ 
/*    */ import java.security.Principal;
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
/*    */ 
/*    */ public class CallerPrincipalCallback
/*    */   implements Callback
/*    */ {
/*    */   private final Subject subject;
/*    */   private final Principal principal;
/*    */   private final String name;
/*    */   
/*    */   public CallerPrincipalCallback(Subject subject, Principal principal)
/*    */   {
/* 35 */     this.subject = subject;
/* 36 */     this.principal = principal;
/* 37 */     this.name = null;
/*    */   }
/*    */   
/*    */   public CallerPrincipalCallback(Subject subject, String name) {
/* 41 */     this.subject = subject;
/* 42 */     this.principal = null;
/* 43 */     this.name = name;
/*    */   }
/*    */   
/*    */   public Subject getSubject() {
/* 47 */     return this.subject;
/*    */   }
/*    */   
/*    */   public Principal getPrincipal() {
/* 51 */     return this.principal;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 55 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\security\auth\message\callback\CallerPrincipalCallback.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */