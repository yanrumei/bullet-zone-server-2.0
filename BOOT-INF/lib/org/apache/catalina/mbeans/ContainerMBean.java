/*     */ package org.apache.catalina.mbeans;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.RuntimeOperationsException;
/*     */ import javax.management.modelmbean.InvalidTargetObjectTypeException;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.ContainerListener;
/*     */ import org.apache.catalina.JmxEnabled;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleListener;
/*     */ import org.apache.catalina.Pipeline;
/*     */ import org.apache.catalina.Valve;
/*     */ import org.apache.catalina.core.ContainerBase;
/*     */ import org.apache.catalina.core.StandardContext;
/*     */ import org.apache.catalina.core.StandardHost;
/*     */ import org.apache.catalina.startup.ContextConfig;
/*     */ import org.apache.catalina.startup.HostConfig;
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
/*     */ public class ContainerMBean
/*     */   extends BaseModelMBean
/*     */ {
/*     */   public ContainerMBean()
/*     */     throws MBeanException, RuntimeOperationsException
/*     */   {}
/*     */   
/*     */   public void addChild(String type, String name)
/*     */     throws MBeanException
/*     */   {
/*  71 */     Container contained = null;
/*     */     try {
/*  73 */       contained = (Container)Class.forName(type).getConstructor(new Class[0]).newInstance(new Object[0]);
/*  74 */       contained.setName(name);
/*     */       
/*  76 */       if ((contained instanceof StandardHost)) {
/*  77 */         HostConfig config = new HostConfig();
/*  78 */         contained.addLifecycleListener(config);
/*  79 */       } else if ((contained instanceof StandardContext)) {
/*  80 */         ContextConfig config = new ContextConfig();
/*  81 */         contained.addLifecycleListener(config);
/*     */       }
/*     */     }
/*     */     catch (ReflectiveOperationException e) {
/*  85 */       throw new MBeanException(e);
/*     */     }
/*     */     
/*  88 */     boolean oldValue = true;
/*     */     
/*  90 */     ContainerBase container = null;
/*     */     try {
/*  92 */       container = (ContainerBase)getManagedResource();
/*  93 */       oldValue = container.getStartChildren();
/*  94 */       container.setStartChildren(false);
/*  95 */       container.addChild(contained);
/*  96 */       contained.init();
/*     */     } catch (InstanceNotFoundException e) {
/*  98 */       throw new MBeanException(e);
/*     */     } catch (RuntimeOperationsException e) {
/* 100 */       throw new MBeanException(e);
/*     */     } catch (InvalidTargetObjectTypeException e) {
/* 102 */       throw new MBeanException(e);
/*     */     } catch (LifecycleException e) {
/* 104 */       throw new MBeanException(e);
/*     */     } finally {
/* 106 */       if (container != null) {
/* 107 */         container.setStartChildren(oldValue);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeChild(String name)
/*     */     throws MBeanException
/*     */   {
/* 120 */     if (name != null) {
/*     */       try {
/* 122 */         Container container = (Container)getManagedResource();
/* 123 */         Container contained = container.findChild(name);
/* 124 */         container.removeChild(contained);
/*     */       } catch (InstanceNotFoundException e) {
/* 126 */         throw new MBeanException(e);
/*     */       } catch (RuntimeOperationsException e) {
/* 128 */         throw new MBeanException(e);
/*     */       } catch (InvalidTargetObjectTypeException e) {
/* 130 */         throw new MBeanException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String addValve(String valveType)
/*     */     throws MBeanException
/*     */   {
/* 143 */     Valve valve = null;
/*     */     try {
/* 145 */       valve = (Valve)Class.forName(valveType).getConstructor(new Class[0]).newInstance(new Object[0]);
/*     */     } catch (ReflectiveOperationException e) {
/* 147 */       throw new MBeanException(e);
/*     */     }
/*     */     
/* 150 */     if (valve == null) {
/* 151 */       return null;
/*     */     }
/*     */     try
/*     */     {
/* 155 */       Container container = (Container)getManagedResource();
/* 156 */       container.getPipeline().addValve(valve);
/*     */     } catch (InstanceNotFoundException e) {
/* 158 */       throw new MBeanException(e);
/*     */     } catch (RuntimeOperationsException e) {
/* 160 */       throw new MBeanException(e);
/*     */     } catch (InvalidTargetObjectTypeException e) {
/* 162 */       throw new MBeanException(e);
/*     */     }
/*     */     
/* 165 */     if ((valve instanceof JmxEnabled)) {
/* 166 */       return ((JmxEnabled)valve).getObjectName().toString();
/*     */     }
/* 168 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeValve(String valveName)
/*     */     throws MBeanException
/*     */   {
/* 180 */     Container container = null;
/*     */     try {
/* 182 */       container = (Container)getManagedResource();
/*     */     } catch (InstanceNotFoundException e) {
/* 184 */       throw new MBeanException(e);
/*     */     } catch (RuntimeOperationsException e) {
/* 186 */       throw new MBeanException(e);
/*     */     } catch (InvalidTargetObjectTypeException e) {
/* 188 */       throw new MBeanException(e);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 193 */       oname = new ObjectName(valveName);
/*     */     } catch (MalformedObjectNameException e) { ObjectName oname;
/* 195 */       throw new MBeanException(e);
/*     */     } catch (NullPointerException e) {
/* 197 */       throw new MBeanException(e);
/*     */     }
/*     */     ObjectName oname;
/* 200 */     if (container != null) {
/* 201 */       Valve[] valves = container.getPipeline().getValves();
/* 202 */       for (int i = 0; i < valves.length; i++) {
/* 203 */         if ((valves[i] instanceof JmxEnabled))
/*     */         {
/* 205 */           ObjectName voname = ((JmxEnabled)valves[i]).getObjectName();
/* 206 */           if (voname.equals(oname)) {
/* 207 */             container.getPipeline().removeValve(valves[i]);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addLifecycleListener(String type)
/*     */     throws MBeanException
/*     */   {
/* 221 */     LifecycleListener listener = null;
/*     */     try {
/* 223 */       listener = (LifecycleListener)Class.forName(type).getConstructor(new Class[0]).newInstance(new Object[0]);
/*     */     } catch (ReflectiveOperationException e) {
/* 225 */       throw new MBeanException(e);
/*     */     }
/*     */     
/* 228 */     if (listener != null) {
/*     */       try {
/* 230 */         Container container = (Container)getManagedResource();
/* 231 */         container.addLifecycleListener(listener);
/*     */       } catch (InstanceNotFoundException e) {
/* 233 */         throw new MBeanException(e);
/*     */       } catch (RuntimeOperationsException e) {
/* 235 */         throw new MBeanException(e);
/*     */       } catch (InvalidTargetObjectTypeException e) {
/* 237 */         throw new MBeanException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeLifecycleListeners(String type)
/*     */     throws MBeanException
/*     */   {
/* 250 */     Container container = null;
/*     */     try {
/* 252 */       container = (Container)getManagedResource();
/*     */     } catch (InstanceNotFoundException e) {
/* 254 */       throw new MBeanException(e);
/*     */     } catch (RuntimeOperationsException e) {
/* 256 */       throw new MBeanException(e);
/*     */     } catch (InvalidTargetObjectTypeException e) {
/* 258 */       throw new MBeanException(e);
/*     */     }
/*     */     
/* 261 */     LifecycleListener[] listeners = container.findLifecycleListeners();
/* 262 */     for (LifecycleListener listener : listeners) {
/* 263 */       if (listener.getClass().getName().equals(type)) {
/* 264 */         container.removeLifecycleListener(listener);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] findLifecycleListenerNames()
/*     */     throws MBeanException
/*     */   {
/* 277 */     Container container = null;
/* 278 */     List<String> result = new ArrayList();
/*     */     try
/*     */     {
/* 281 */       container = (Container)getManagedResource();
/*     */     } catch (InstanceNotFoundException e) {
/* 283 */       throw new MBeanException(e);
/*     */     } catch (RuntimeOperationsException e) {
/* 285 */       throw new MBeanException(e);
/*     */     } catch (InvalidTargetObjectTypeException e) {
/* 287 */       throw new MBeanException(e);
/*     */     }
/*     */     
/* 290 */     LifecycleListener[] listeners = container.findLifecycleListeners();
/* 291 */     for (LifecycleListener listener : listeners) {
/* 292 */       result.add(listener.getClass().getName());
/*     */     }
/*     */     
/* 295 */     return (String[])result.toArray(new String[result.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] findContainerListenerNames()
/*     */     throws MBeanException
/*     */   {
/* 306 */     Container container = null;
/* 307 */     List<String> result = new ArrayList();
/*     */     try
/*     */     {
/* 310 */       container = (Container)getManagedResource();
/*     */     } catch (InstanceNotFoundException e) {
/* 312 */       throw new MBeanException(e);
/*     */     } catch (RuntimeOperationsException e) {
/* 314 */       throw new MBeanException(e);
/*     */     } catch (InvalidTargetObjectTypeException e) {
/* 316 */       throw new MBeanException(e);
/*     */     }
/*     */     
/* 319 */     ContainerListener[] listeners = container.findContainerListeners();
/* 320 */     for (ContainerListener listener : listeners) {
/* 321 */       result.add(listener.getClass().getName());
/*     */     }
/*     */     
/* 324 */     return (String[])result.toArray(new String[result.size()]);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\mbeans\ContainerMBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */