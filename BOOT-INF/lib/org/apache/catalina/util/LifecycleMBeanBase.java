/*     */ package org.apache.catalina.util;
/*     */ 
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.MBeanRegistrationException;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.catalina.JmxEnabled;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.modeler.Registry;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class LifecycleMBeanBase
/*     */   extends LifecycleBase
/*     */   implements JmxEnabled
/*     */ {
/*  37 */   private static final Log log = LogFactory.getLog(LifecycleMBeanBase.class);
/*     */   
/*     */ 
/*  40 */   private static final StringManager sm = StringManager.getManager("org.apache.catalina.util");
/*     */   
/*     */ 
/*     */ 
/*  44 */   private String domain = null;
/*  45 */   private ObjectName oname = null;
/*  46 */   protected MBeanServer mserver = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initInternal()
/*     */     throws LifecycleException
/*     */   {
/*  58 */     if (this.oname == null) {
/*  59 */       this.mserver = Registry.getRegistry(null, null).getMBeanServer();
/*     */       
/*  61 */       this.oname = register(this, getObjectNameKeyProperties());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void destroyInternal()
/*     */     throws LifecycleException
/*     */   {
/*  73 */     unregister(this.oname);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setDomain(String domain)
/*     */   {
/*  84 */     this.domain = domain;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String getDomain()
/*     */   {
/*  94 */     if (this.domain == null) {
/*  95 */       this.domain = getDomainInternal();
/*     */     }
/*     */     
/*  98 */     if (this.domain == null) {
/*  99 */       this.domain = "Catalina";
/*     */     }
/*     */     
/* 102 */     return this.domain;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract String getDomainInternal();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final ObjectName getObjectName()
/*     */   {
/* 120 */     return this.oname;
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
/*     */   protected abstract String getObjectNameKeyProperties();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final ObjectName register(Object obj, String objectNameKeyProperties)
/*     */   {
/* 152 */     StringBuilder name = new StringBuilder(getDomain());
/* 153 */     name.append(':');
/* 154 */     name.append(objectNameKeyProperties);
/*     */     
/* 156 */     ObjectName on = null;
/*     */     try
/*     */     {
/* 159 */       on = new ObjectName(name.toString());
/*     */       
/* 161 */       Registry.getRegistry(null, null).registerComponent(obj, on, null);
/*     */     } catch (MalformedObjectNameException e) {
/* 163 */       log.warn(sm.getString("lifecycleMBeanBase.registerFail", new Object[] { obj, name }), e);
/*     */     }
/*     */     catch (Exception e) {
/* 166 */       log.warn(sm.getString("lifecycleMBeanBase.registerFail", new Object[] { obj, name }), e);
/*     */     }
/*     */     
/*     */ 
/* 170 */     return on;
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
/*     */   protected final void unregister(ObjectName on)
/*     */   {
/* 186 */     if (on == null) {
/* 187 */       return;
/*     */     }
/*     */     
/*     */ 
/* 191 */     if (this.mserver == null) {
/* 192 */       log.warn(sm.getString("lifecycleMBeanBase.unregisterNoServer", new Object[] { on }));
/* 193 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 197 */       this.mserver.unregisterMBean(on);
/*     */     } catch (MBeanRegistrationException e) {
/* 199 */       log.warn(sm.getString("lifecycleMBeanBase.unregisterFail", new Object[] { on }), e);
/*     */     } catch (InstanceNotFoundException e) {
/* 201 */       log.warn(sm.getString("lifecycleMBeanBase.unregisterFail", new Object[] { on }), e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void postDeregister() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void postRegister(Boolean registrationDone) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void preDeregister()
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final ObjectName preRegister(MBeanServer server, ObjectName name)
/*     */     throws Exception
/*     */   {
/* 242 */     this.mserver = server;
/* 243 */     this.oname = name;
/* 244 */     this.domain = name.getDomain();
/*     */     
/* 246 */     return this.oname;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\util\LifecycleMBeanBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */