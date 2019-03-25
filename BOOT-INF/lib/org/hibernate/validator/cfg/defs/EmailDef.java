/*    */ package org.hibernate.validator.cfg.defs;
/*    */ 
/*    */ import javax.validation.constraints.Pattern.Flag;
/*    */ import org.hibernate.validator.cfg.ConstraintDef;
/*    */ import org.hibernate.validator.constraints.Email;
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
/*    */ public class EmailDef
/*    */   extends ConstraintDef<EmailDef, Email>
/*    */ {
/*    */   public EmailDef()
/*    */   {
/* 21 */     super(Email.class);
/*    */   }
/*    */   
/*    */   public EmailDef regexp(String regexp) {
/* 25 */     addParameter("regexp", regexp);
/* 26 */     return this;
/*    */   }
/*    */   
/*    */   public EmailDef flags(Pattern.Flag... flags) {
/* 30 */     addParameter("flags", flags);
/* 31 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\cfg\defs\EmailDef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */