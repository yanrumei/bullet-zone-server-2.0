/*    */ package org.hibernate.validator.internal.xml;
/*    */ 
/*    */ import javax.validation.executable.ExecutableType;
/*    */ import javax.xml.bind.annotation.adapters.XmlAdapter;
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
/*    */ public class Adapter1
/*    */   extends XmlAdapter<String, ExecutableType>
/*    */ {
/*    */   public ExecutableType unmarshal(String value)
/*    */   {
/* 20 */     return ExecutableType.valueOf(value);
/*    */   }
/*    */   
/*    */   public String marshal(ExecutableType value) {
/* 24 */     if (value == null) {
/* 25 */       return null;
/*    */     }
/* 27 */     return value.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\Adapter1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */