/*    */ package org.apache.catalina.mbeans;
/*    */ 
/*    */ import javax.management.MBeanException;
/*    */ import javax.management.RuntimeOperationsException;
/*    */ import org.apache.tomcat.util.modeler.BaseModelMBean;
/*    */ import org.apache.tomcat.util.modeler.ManagedBean;
/*    */ import org.apache.tomcat.util.modeler.Registry;
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
/*    */ public class RoleMBean
/*    */   extends BaseModelMBean
/*    */ {
/* 64 */   protected final Registry registry = MBeanUtils.createRegistry();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 70 */   protected final ManagedBean managed = this.registry.findManagedBean("Role");
/*    */   
/*    */   public RoleMBean()
/*    */     throws MBeanException, RuntimeOperationsException
/*    */   {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\mbeans\RoleMBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */