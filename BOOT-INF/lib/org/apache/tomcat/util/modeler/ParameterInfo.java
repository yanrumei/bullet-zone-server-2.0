/*    */ package org.apache.tomcat.util.modeler;
/*    */ 
/*    */ import javax.management.MBeanParameterInfo;
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
/*    */ public class ParameterInfo
/*    */   extends FeatureInfo
/*    */ {
/*    */   static final long serialVersionUID = 2222796006787664020L;
/*    */   
/*    */   public MBeanParameterInfo createParameterInfo()
/*    */   {
/* 51 */     if (this.info == null)
/*    */     {
/* 53 */       this.info = new MBeanParameterInfo(getName(), getType(), getDescription());
/*    */     }
/* 55 */     return (MBeanParameterInfo)this.info;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\modeler\ParameterInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */