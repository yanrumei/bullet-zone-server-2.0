/*    */ package javax.security.auth.message.callback;
/*    */ 
/*    */ import java.util.Arrays;
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
/*    */ public class PasswordValidationCallback
/*    */   implements Callback
/*    */ {
/*    */   private final Subject subject;
/*    */   private final String username;
/*    */   private char[] password;
/*    */   private boolean result;
/*    */   
/*    */   public PasswordValidationCallback(Subject subject, String username, char[] password)
/*    */   {
/* 36 */     this.subject = subject;
/* 37 */     this.username = username;
/* 38 */     this.password = password;
/*    */   }
/*    */   
/*    */   public Subject getSubject() {
/* 42 */     return this.subject;
/*    */   }
/*    */   
/*    */   public String getUsername() {
/* 46 */     return this.username;
/*    */   }
/*    */   
/*    */   public char[] getPassword() {
/* 50 */     return this.password;
/*    */   }
/*    */   
/*    */   public void clearPassword() {
/* 54 */     Arrays.fill(this.password, '\000');
/* 55 */     this.password = new char[0];
/*    */   }
/*    */   
/*    */   public void setResult(boolean result) {
/* 59 */     this.result = result;
/*    */   }
/*    */   
/*    */   public boolean getResult() {
/* 63 */     return this.result;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\security\auth\message\callback\PasswordValidationCallback.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */