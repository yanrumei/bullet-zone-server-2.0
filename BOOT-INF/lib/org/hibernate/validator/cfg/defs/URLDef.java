/*    */ package org.hibernate.validator.cfg.defs;
/*    */ 
/*    */ import javax.validation.constraints.Pattern.Flag;
/*    */ import org.hibernate.validator.cfg.ConstraintDef;
/*    */ import org.hibernate.validator.constraints.URL;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class URLDef
/*    */   extends ConstraintDef<URLDef, URL>
/*    */ {
/*    */   public URLDef()
/*    */   {
/* 20 */     super(URL.class);
/*    */   }
/*    */   
/*    */   public URLDef protocol(String protocol) {
/* 24 */     addParameter("protocol", protocol);
/* 25 */     return this;
/*    */   }
/*    */   
/*    */   public URLDef host(String host) {
/* 29 */     addParameter("host", host);
/* 30 */     return this;
/*    */   }
/*    */   
/*    */   public URLDef port(int port) {
/* 34 */     addParameter("port", Integer.valueOf(port));
/* 35 */     return this;
/*    */   }
/*    */   
/*    */   public URLDef regexp(String regexp) {
/* 39 */     addParameter("regexp", regexp);
/* 40 */     return this;
/*    */   }
/*    */   
/*    */   public URLDef flags(Pattern.Flag... flags) {
/* 44 */     addParameter("flags", flags);
/* 45 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\cfg\defs\URLDef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */