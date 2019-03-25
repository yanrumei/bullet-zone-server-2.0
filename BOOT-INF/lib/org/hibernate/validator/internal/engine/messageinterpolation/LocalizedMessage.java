/*    */ package org.hibernate.validator.internal.engine.messageinterpolation;
/*    */ 
/*    */ import java.util.Locale;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LocalizedMessage
/*    */ {
/*    */   private final String message;
/*    */   private final Locale locale;
/*    */   
/*    */   public LocalizedMessage(String message, Locale locale)
/*    */   {
/* 19 */     this.message = message;
/* 20 */     this.locale = locale;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 25 */     if (this == o) {
/* 26 */       return true;
/*    */     }
/* 28 */     if ((o == null) || (getClass() != o.getClass())) {
/* 29 */       return false;
/*    */     }
/*    */     
/* 32 */     LocalizedMessage that = (LocalizedMessage)o;
/*    */     
/* 34 */     if (this.locale != null ? !this.locale.equals(that.locale) : that.locale != null) {
/* 35 */       return false;
/*    */     }
/* 37 */     if (this.message != null ? !this.message.equals(that.message) : that.message != null) {
/* 38 */       return false;
/*    */     }
/*    */     
/* 41 */     return true;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 46 */     int result = this.message != null ? this.message.hashCode() : 0;
/* 47 */     result = 31 * result + (this.locale != null ? this.locale.hashCode() : 0);
/* 48 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\messageinterpolation\LocalizedMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */