/*     */ package org.apache.catalina.mbeans;
/*     */ 
/*     */ import javax.management.Attribute;
/*     */ import javax.management.AttributeNotFoundException;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.ReflectionException;
/*     */ import javax.management.RuntimeOperationsException;
/*     */ import javax.management.modelmbean.InvalidTargetObjectTypeException;
/*     */ import org.apache.tomcat.util.descriptor.web.ContextResourceLink;
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
/*     */ public class ContextResourceLinkMBean
/*     */   extends BaseModelMBean
/*     */ {
/*     */   public ContextResourceLinkMBean()
/*     */     throws MBeanException, RuntimeOperationsException
/*     */   {}
/*     */   
/*     */   public Object getAttribute(String name)
/*     */     throws AttributeNotFoundException, MBeanException, ReflectionException
/*     */   {
/*  84 */     if (name == null) {
/*  85 */       throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name is null"), "Attribute name is null");
/*     */     }
/*     */     
/*     */ 
/*  89 */     ContextResourceLink cl = null;
/*     */     try {
/*  91 */       cl = (ContextResourceLink)getManagedResource();
/*     */     } catch (InstanceNotFoundException e) {
/*  93 */       throw new MBeanException(e);
/*     */     } catch (InvalidTargetObjectTypeException e) {
/*  95 */       throw new MBeanException(e);
/*     */     }
/*     */     
/*  98 */     String value = null;
/*  99 */     if ("global".equals(name))
/* 100 */       return cl.getGlobal();
/* 101 */     if ("description".equals(name))
/* 102 */       return cl.getDescription();
/* 103 */     if ("name".equals(name))
/* 104 */       return cl.getName();
/* 105 */     if ("type".equals(name)) {
/* 106 */       return cl.getType();
/*     */     }
/* 108 */     value = (String)cl.getProperty(name);
/* 109 */     if (value == null) {
/* 110 */       throw new AttributeNotFoundException("Cannot find attribute " + name);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 115 */     return value;
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
/*     */   public void setAttribute(Attribute attribute)
/*     */     throws AttributeNotFoundException, MBeanException, ReflectionException
/*     */   {
/* 138 */     if (attribute == null) {
/* 139 */       throw new RuntimeOperationsException(new IllegalArgumentException("Attribute is null"), "Attribute is null");
/*     */     }
/*     */     
/*     */ 
/* 143 */     String name = attribute.getName();
/* 144 */     Object value = attribute.getValue();
/* 145 */     if (name == null) {
/* 146 */       throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name is null"), "Attribute name is null");
/*     */     }
/*     */     
/*     */ 
/* 150 */     ContextResourceLink crl = null;
/*     */     try {
/* 152 */       crl = (ContextResourceLink)getManagedResource();
/*     */     } catch (InstanceNotFoundException e) {
/* 154 */       throw new MBeanException(e);
/*     */     } catch (InvalidTargetObjectTypeException e) {
/* 156 */       throw new MBeanException(e);
/*     */     }
/*     */     
/* 159 */     if ("global".equals(name)) {
/* 160 */       crl.setGlobal((String)value);
/* 161 */     } else if ("description".equals(name)) {
/* 162 */       crl.setDescription((String)value);
/* 163 */     } else if ("name".equals(name)) {
/* 164 */       crl.setName((String)value);
/* 165 */     } else if ("type".equals(name)) {
/* 166 */       crl.setType((String)value);
/*     */     } else {
/* 168 */       crl.setProperty(name, "" + value);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 173 */     NamingResources nr = crl.getNamingResources();
/* 174 */     nr.removeResourceLink(crl.getName());
/* 175 */     nr.addResourceLink(crl);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\mbeans\ContextResourceLinkMBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */