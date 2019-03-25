/*    */ package org.apache.tomcat.util.descriptor.web;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public class InjectionTarget
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private String targetClass;
/*    */   private String targetName;
/*    */   
/*    */   public InjectionTarget() {}
/*    */   
/*    */   public InjectionTarget(String targetClass, String targetName)
/*    */   {
/* 34 */     this.targetClass = targetClass;
/* 35 */     this.targetName = targetName;
/*    */   }
/*    */   
/*    */   public String getTargetClass() {
/* 39 */     return this.targetClass;
/*    */   }
/*    */   
/*    */   public void setTargetClass(String targetClass) {
/* 43 */     this.targetClass = targetClass;
/*    */   }
/*    */   
/*    */   public String getTargetName() {
/* 47 */     return this.targetName;
/*    */   }
/*    */   
/*    */   public void setTargetName(String targetName) {
/* 51 */     this.targetName = targetName;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\InjectionTarget.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */