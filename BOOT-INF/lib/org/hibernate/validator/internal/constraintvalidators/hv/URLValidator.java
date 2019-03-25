/*    */ package org.hibernate.validator.internal.constraintvalidators.hv;
/*    */ 
/*    */ import java.net.MalformedURLException;
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
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
/*    */ public class URLValidator
/*    */   implements ConstraintValidator<org.hibernate.validator.constraints.URL, CharSequence>
/*    */ {
/*    */   private String protocol;
/*    */   private String host;
/*    */   private int port;
/*    */   
/*    */   public void initialize(org.hibernate.validator.constraints.URL url)
/*    */   {
/* 25 */     this.protocol = url.protocol();
/* 26 */     this.host = url.host();
/* 27 */     this.port = url.port();
/*    */   }
/*    */   
/*    */   public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext)
/*    */   {
/* 32 */     if ((value == null) || (value.length() == 0)) {
/* 33 */       return true;
/*    */     }
/*    */     
/*    */     try
/*    */     {
/* 38 */       url = new java.net.URL(value.toString());
/*    */     } catch (MalformedURLException e) {
/*    */       java.net.URL url;
/* 41 */       return false;
/*    */     }
/*    */     java.net.URL url;
/* 44 */     if ((this.protocol != null) && (this.protocol.length() > 0) && (!url.getProtocol().equals(this.protocol))) {
/* 45 */       return false;
/*    */     }
/*    */     
/* 48 */     if ((this.host != null) && (this.host.length() > 0) && (!url.getHost().equals(this.host))) {
/* 49 */       return false;
/*    */     }
/*    */     
/* 52 */     if ((this.port != -1) && (url.getPort() != this.port)) {
/* 53 */       return false;
/*    */     }
/*    */     
/* 56 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\hv\URLValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */