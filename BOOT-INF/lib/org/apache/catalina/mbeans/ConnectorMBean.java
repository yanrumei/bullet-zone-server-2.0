/*     */ package org.apache.catalina.mbeans;
/*     */ 
/*     */ import javax.management.Attribute;
/*     */ import javax.management.AttributeNotFoundException;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.ReflectionException;
/*     */ import javax.management.RuntimeOperationsException;
/*     */ import javax.management.modelmbean.InvalidTargetObjectTypeException;
/*     */ import org.apache.catalina.connector.Connector;
/*     */ import org.apache.tomcat.util.IntrospectionUtils;
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
/*     */ public class ConnectorMBean
/*     */   extends ClassNameMBean
/*     */ {
/*     */   public ConnectorMBean()
/*     */     throws MBeanException, RuntimeOperationsException
/*     */   {}
/*     */   
/*     */   public Object getAttribute(String name)
/*     */     throws AttributeNotFoundException, MBeanException, ReflectionException
/*     */   {
/*  81 */     if (name == null) {
/*  82 */       throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name is null"), "Attribute name is null");
/*     */     }
/*     */     
/*  85 */     Object result = null;
/*     */     try {
/*  87 */       Connector connector = (Connector)getManagedResource();
/*  88 */       result = IntrospectionUtils.getProperty(connector, name);
/*     */     } catch (InstanceNotFoundException e) {
/*  90 */       throw new MBeanException(e);
/*     */     } catch (InvalidTargetObjectTypeException e) {
/*  92 */       throw new MBeanException(e);
/*     */     }
/*     */     
/*  95 */     return result;
/*     */   }
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
/*     */   public void setAttribute(Attribute attribute)
/*     */     throws AttributeNotFoundException, MBeanException, ReflectionException
/*     */   {
/* 119 */     if (attribute == null) {
/* 120 */       throw new RuntimeOperationsException(new IllegalArgumentException("Attribute is null"), "Attribute is null");
/*     */     }
/* 122 */     String name = attribute.getName();
/* 123 */     Object value = attribute.getValue();
/* 124 */     if (name == null) {
/* 125 */       throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name is null"), "Attribute name is null");
/*     */     }
/*     */     try
/*     */     {
/* 129 */       Connector connector = (Connector)getManagedResource();
/* 130 */       IntrospectionUtils.setProperty(connector, name, String.valueOf(value));
/*     */     } catch (InstanceNotFoundException e) {
/* 132 */       throw new MBeanException(e);
/*     */     } catch (InvalidTargetObjectTypeException e) {
/* 134 */       throw new MBeanException(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\mbeans\ConnectorMBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */