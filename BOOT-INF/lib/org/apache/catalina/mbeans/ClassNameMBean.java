/*    */ package org.apache.catalina.mbeans;
/*    */ 
/*    */ import javax.management.MBeanException;
/*    */ import javax.management.RuntimeOperationsException;
/*    */ import org.apache.tomcat.util.modeler.BaseModelMBean;
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
/*    */ public class ClassNameMBean
/*    */   extends BaseModelMBean
/*    */ {
/*    */   public ClassNameMBean()
/*    */     throws MBeanException, RuntimeOperationsException
/*    */   {}
/*    */   
/*    */   public String getClassName()
/*    */   {
/* 71 */     return this.resource.getClass().getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\mbeans\ClassNameMBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */