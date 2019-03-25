/*     */ package org.apache.catalina.mbeans;
/*     */ 
/*     */ import javax.management.Attribute;
/*     */ import javax.management.AttributeNotFoundException;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.ReflectionException;
/*     */ import javax.management.RuntimeOperationsException;
/*     */ import javax.management.modelmbean.InvalidTargetObjectTypeException;
/*     */ import org.apache.tomcat.util.descriptor.web.ContextResource;
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
/*     */ public class ContextResourceMBean
/*     */   extends BaseModelMBean
/*     */ {
/*     */   public ContextResourceMBean()
/*     */     throws MBeanException, RuntimeOperationsException
/*     */   {}
/*     */   
/*     */   public Object getAttribute(String name)
/*     */     throws AttributeNotFoundException, MBeanException, ReflectionException
/*     */   {
/*  85 */     if (name == null) {
/*  86 */       throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name is null"), "Attribute name is null");
/*     */     }
/*     */     
/*     */ 
/*  90 */     ContextResource cr = null;
/*     */     try {
/*  92 */       cr = (ContextResource)getManagedResource();
/*     */     } catch (InstanceNotFoundException e) {
/*  94 */       throw new MBeanException(e);
/*     */     } catch (InvalidTargetObjectTypeException e) {
/*  96 */       throw new MBeanException(e);
/*     */     }
/*     */     
/*  99 */     String value = null;
/* 100 */     if ("auth".equals(name))
/* 101 */       return cr.getAuth();
/* 102 */     if ("description".equals(name))
/* 103 */       return cr.getDescription();
/* 104 */     if ("name".equals(name))
/* 105 */       return cr.getName();
/* 106 */     if ("scope".equals(name))
/* 107 */       return cr.getScope();
/* 108 */     if ("type".equals(name)) {
/* 109 */       return cr.getType();
/*     */     }
/* 111 */     value = (String)cr.getProperty(name);
/* 112 */     if (value == null) {
/* 113 */       throw new AttributeNotFoundException("Cannot find attribute " + name);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 118 */     return value;
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
/* 142 */     if (attribute == null) {
/* 143 */       throw new RuntimeOperationsException(new IllegalArgumentException("Attribute is null"), "Attribute is null");
/*     */     }
/*     */     
/* 146 */     String name = attribute.getName();
/* 147 */     Object value = attribute.getValue();
/* 148 */     if (name == null) {
/* 149 */       throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name is null"), "Attribute name is null");
/*     */     }
/*     */     
/*     */ 
/* 153 */     ContextResource cr = null;
/*     */     try {
/* 155 */       cr = (ContextResource)getManagedResource();
/*     */     } catch (InstanceNotFoundException e) {
/* 157 */       throw new MBeanException(e);
/*     */     } catch (InvalidTargetObjectTypeException e) {
/* 159 */       throw new MBeanException(e);
/*     */     }
/*     */     
/* 162 */     if ("auth".equals(name)) {
/* 163 */       cr.setAuth((String)value);
/* 164 */     } else if ("description".equals(name)) {
/* 165 */       cr.setDescription((String)value);
/* 166 */     } else if ("name".equals(name)) {
/* 167 */       cr.setName((String)value);
/* 168 */     } else if ("scope".equals(name)) {
/* 169 */       cr.setScope((String)value);
/* 170 */     } else if ("type".equals(name)) {
/* 171 */       cr.setType((String)value);
/*     */     } else {
/* 173 */       cr.setProperty(name, "" + value);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 178 */     NamingResources nr = cr.getNamingResources();
/* 179 */     nr.removeResource(cr.getName());
/* 180 */     nr.addResource(cr);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\mbeans\ContextResourceMBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */