/*     */ package org.apache.catalina.mbeans;
/*     */ 
/*     */ import javax.management.Attribute;
/*     */ import javax.management.AttributeNotFoundException;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.ReflectionException;
/*     */ import javax.management.RuntimeOperationsException;
/*     */ import javax.management.modelmbean.InvalidTargetObjectTypeException;
/*     */ import org.apache.tomcat.util.descriptor.web.ContextEnvironment;
/*     */ import org.apache.tomcat.util.descriptor.web.NamingResources;
/*     */ import org.apache.tomcat.util.modeler.BaseModelMBean;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContextEnvironmentMBean
/*     */   extends BaseModelMBean
/*     */ {
/*     */   public ContextEnvironmentMBean()
/*     */     throws MBeanException, RuntimeOperationsException
/*     */   {}
/*     */   
/*     */   public void setAttribute(Attribute attribute)
/*     */     throws AttributeNotFoundException, MBeanException, ReflectionException
/*     */   {
/*  85 */     super.setAttribute(attribute);
/*     */     
/*  87 */     ContextEnvironment ce = null;
/*     */     try {
/*  89 */       ce = (ContextEnvironment)getManagedResource();
/*     */     } catch (InstanceNotFoundException e) {
/*  91 */       throw new MBeanException(e);
/*     */     } catch (InvalidTargetObjectTypeException e) {
/*  93 */       throw new MBeanException(e);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  98 */     NamingResources nr = ce.getNamingResources();
/*  99 */     nr.removeEnvironment(ce.getName());
/* 100 */     nr.addEnvironment(ce);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\mbeans\ContextEnvironmentMBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */