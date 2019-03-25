/*    */ package org.hibernate.validator.cfg.defs;
/*    */ 
/*    */ import org.hibernate.validator.cfg.ConstraintDef;
/*    */ import org.hibernate.validator.constraints.ScriptAssert;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ScriptAssertDef
/*    */   extends ConstraintDef<ScriptAssertDef, ScriptAssert>
/*    */ {
/*    */   public ScriptAssertDef()
/*    */   {
/* 18 */     super(ScriptAssert.class);
/*    */   }
/*    */   
/*    */   public ScriptAssertDef lang(String lang) {
/* 22 */     addParameter("lang", lang);
/* 23 */     return this;
/*    */   }
/*    */   
/*    */   public ScriptAssertDef script(String script) {
/* 27 */     addParameter("script", script);
/* 28 */     return this;
/*    */   }
/*    */   
/*    */   public ScriptAssertDef alias(String alias) {
/* 32 */     addParameter("alias", alias);
/* 33 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\cfg\defs\ScriptAssertDef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */