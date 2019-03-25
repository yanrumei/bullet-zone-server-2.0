/*     */ package org.apache.catalina.mbeans;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.RuntimeOperationsException;
/*     */ import javax.management.modelmbean.InvalidTargetObjectTypeException;
/*     */ import org.apache.catalina.Executor;
/*     */ import org.apache.catalina.Service;
/*     */ import org.apache.catalina.connector.Connector;
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
/*     */ public class ServiceMBean
/*     */   extends BaseModelMBean
/*     */ {
/*     */   public ServiceMBean()
/*     */     throws MBeanException, RuntimeOperationsException
/*     */   {}
/*     */   
/*     */   public void addConnector(String address, int port, boolean isAjp, boolean isSSL)
/*     */     throws MBeanException
/*     */   {
/*     */     try
/*     */     {
/*  54 */       service = (Service)getManagedResource();
/*     */     } catch (InstanceNotFoundException e) { Service service;
/*  56 */       throw new MBeanException(e);
/*     */     } catch (RuntimeOperationsException e) {
/*  58 */       throw new MBeanException(e);
/*     */     } catch (InvalidTargetObjectTypeException e) {
/*  60 */       throw new MBeanException(e); }
/*     */     Service service;
/*  62 */     String protocol = isAjp ? "AJP/1.3" : "HTTP/1.1";
/*  63 */     Connector connector = new Connector(protocol);
/*  64 */     if ((address != null) && (address.length() > 0)) {
/*  65 */       connector.setProperty("address", address);
/*     */     }
/*  67 */     connector.setPort(port);
/*  68 */     connector.setSecure(isSSL);
/*  69 */     connector.setScheme(isSSL ? "https" : "http");
/*     */     
/*  71 */     service.addConnector(connector);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addExecutor(String type)
/*     */     throws MBeanException
/*     */   {
/*     */     try
/*     */     {
/*  84 */       service = (Service)getManagedResource();
/*     */     } catch (InstanceNotFoundException e) { Service service;
/*  86 */       throw new MBeanException(e);
/*     */     } catch (RuntimeOperationsException e) {
/*  88 */       throw new MBeanException(e);
/*     */     } catch (InvalidTargetObjectTypeException e) {
/*  90 */       throw new MBeanException(e);
/*     */     }
/*     */     Service service;
/*     */     try
/*     */     {
/*  95 */       executor = (Executor)Class.forName(type).getConstructor(new Class[0]).newInstance(new Object[0]);
/*     */     } catch (ReflectiveOperationException e) { Executor executor;
/*  97 */       throw new MBeanException(e);
/*     */     }
/*     */     Executor executor;
/* 100 */     service.addExecutor(executor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] findConnectors()
/*     */     throws MBeanException
/*     */   {
/*     */     try
/*     */     {
/* 113 */       service = (Service)getManagedResource();
/*     */     } catch (InstanceNotFoundException e) { Service service;
/* 115 */       throw new MBeanException(e);
/*     */     } catch (RuntimeOperationsException e) {
/* 117 */       throw new MBeanException(e);
/*     */     } catch (InvalidTargetObjectTypeException e) {
/* 119 */       throw new MBeanException(e);
/*     */     }
/*     */     Service service;
/* 122 */     Connector[] connectors = service.findConnectors();
/* 123 */     String[] str = new String[connectors.length];
/*     */     
/* 125 */     for (int i = 0; i < connectors.length; i++) {
/* 126 */       str[i] = connectors[i].toString();
/*     */     }
/*     */     
/* 129 */     return str;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] findExecutors()
/*     */     throws MBeanException
/*     */   {
/*     */     try
/*     */     {
/* 142 */       service = (Service)getManagedResource();
/*     */     } catch (InstanceNotFoundException e) { Service service;
/* 144 */       throw new MBeanException(e);
/*     */     } catch (RuntimeOperationsException e) {
/* 146 */       throw new MBeanException(e);
/*     */     } catch (InvalidTargetObjectTypeException e) {
/* 148 */       throw new MBeanException(e);
/*     */     }
/*     */     Service service;
/* 151 */     Executor[] executors = service.findExecutors();
/* 152 */     String[] str = new String[executors.length];
/*     */     
/* 154 */     for (int i = 0; i < executors.length; i++) {
/* 155 */       str[i] = executors[i].toString();
/*     */     }
/*     */     
/* 158 */     return str;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getExecutor(String name)
/*     */     throws MBeanException
/*     */   {
/*     */     try
/*     */     {
/* 171 */       service = (Service)getManagedResource();
/*     */     } catch (InstanceNotFoundException e) { Service service;
/* 173 */       throw new MBeanException(e);
/*     */     } catch (RuntimeOperationsException e) {
/* 175 */       throw new MBeanException(e);
/*     */     } catch (InvalidTargetObjectTypeException e) {
/* 177 */       throw new MBeanException(e);
/*     */     }
/*     */     Service service;
/* 180 */     Executor executor = service.getExecutor(name);
/* 181 */     return executor.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\mbeans\ServiceMBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */