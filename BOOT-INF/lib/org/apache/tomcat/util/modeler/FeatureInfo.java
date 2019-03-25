/*    */ package org.apache.tomcat.util.modeler;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import javax.management.MBeanFeatureInfo;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FeatureInfo
/*    */   implements Serializable
/*    */ {
/*    */   static final long serialVersionUID = -911529176124712296L;
/* 37 */   protected String description = null;
/* 38 */   protected String name = null;
/* 39 */   protected MBeanFeatureInfo info = null;
/*    */   
/*    */ 
/* 42 */   protected String type = null;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getDescription()
/*    */   {
/* 51 */     return this.description;
/*    */   }
/*    */   
/*    */   public void setDescription(String description) {
/* 55 */     this.description = description;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getName()
/*    */   {
/* 64 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 68 */     this.name = name;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getType()
/*    */   {
/* 75 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(String type) {
/* 79 */     this.type = type;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\modeler\FeatureInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */