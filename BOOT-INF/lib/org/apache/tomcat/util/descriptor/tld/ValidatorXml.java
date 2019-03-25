/*    */ package org.apache.tomcat.util.descriptor.tld;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ public class ValidatorXml
/*    */ {
/*    */   private String validatorClass;
/* 27 */   private final Map<String, String> initParams = new HashMap();
/*    */   
/*    */   public String getValidatorClass() {
/* 30 */     return this.validatorClass;
/*    */   }
/*    */   
/*    */   public void setValidatorClass(String validatorClass) {
/* 34 */     this.validatorClass = validatorClass;
/*    */   }
/*    */   
/*    */   public void addInitParam(String name, String value) {
/* 38 */     this.initParams.put(name, value);
/*    */   }
/*    */   
/*    */   public Map<String, String> getInitParams() {
/* 42 */     return this.initParams;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\tld\ValidatorXml.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */