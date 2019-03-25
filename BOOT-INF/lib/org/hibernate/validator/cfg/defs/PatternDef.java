/*    */ package org.hibernate.validator.cfg.defs;
/*    */ 
/*    */ import javax.validation.constraints.Pattern;
/*    */ import javax.validation.constraints.Pattern.Flag;
/*    */ import org.hibernate.validator.cfg.ConstraintDef;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PatternDef
/*    */   extends ConstraintDef<PatternDef, Pattern>
/*    */ {
/*    */   public PatternDef()
/*    */   {
/* 20 */     super(Pattern.class);
/*    */   }
/*    */   
/*    */   public PatternDef flags(Pattern.Flag[] flags) {
/* 24 */     addParameter("flags", flags);
/* 25 */     return this;
/*    */   }
/*    */   
/*    */   public PatternDef regexp(String regexp) {
/* 29 */     addParameter("regexp", regexp);
/* 30 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\cfg\defs\PatternDef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */