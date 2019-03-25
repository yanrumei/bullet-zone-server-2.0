/*     */ package org.apache.catalina.mbeans;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.RuntimeOperationsException;
/*     */ import org.apache.catalina.deploy.NamingResourcesImpl;
/*     */ import org.apache.tomcat.util.descriptor.web.ContextEnvironment;
/*     */ import org.apache.tomcat.util.descriptor.web.ContextResource;
/*     */ import org.apache.tomcat.util.descriptor.web.ContextResourceLink;
/*     */ import org.apache.tomcat.util.modeler.BaseModelMBean;
/*     */ import org.apache.tomcat.util.modeler.ManagedBean;
/*     */ import org.apache.tomcat.util.modeler.Registry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NamingResourcesMBean
/*     */   extends BaseModelMBean
/*     */ {
/*  69 */   protected final Registry registry = MBeanUtils.createRegistry();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  75 */   protected final ManagedBean managed = this.registry
/*  76 */     .findManagedBean("NamingResources");
/*     */   
/*     */ 
/*     */ 
/*     */   public NamingResourcesMBean()
/*     */     throws MBeanException, RuntimeOperationsException
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */   public String[] getEnvironments()
/*     */   {
/*  88 */     ContextEnvironment[] envs = ((NamingResourcesImpl)this.resource).findEnvironments();
/*  89 */     ArrayList<String> results = new ArrayList();
/*  90 */     for (int i = 0; i < envs.length; i++) {
/*     */       try
/*     */       {
/*  93 */         ObjectName oname = MBeanUtils.createObjectName(this.managed.getDomain(), envs[i]);
/*  94 */         results.add(oname.toString());
/*     */       } catch (MalformedObjectNameException e) {
/*  96 */         IllegalArgumentException iae = new IllegalArgumentException("Cannot create object name for environment " + envs[i]);
/*     */         
/*  98 */         iae.initCause(e);
/*  99 */         throw iae;
/*     */       }
/*     */     }
/* 102 */     return (String[])results.toArray(new String[results.size()]);
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
/*     */   public String[] getResources()
/*     */   {
/* 115 */     ContextResource[] resources = ((NamingResourcesImpl)this.resource).findResources();
/* 116 */     ArrayList<String> results = new ArrayList();
/* 117 */     for (int i = 0; i < resources.length; i++) {
/*     */       try
/*     */       {
/* 120 */         ObjectName oname = MBeanUtils.createObjectName(this.managed.getDomain(), resources[i]);
/* 121 */         results.add(oname.toString());
/*     */       } catch (MalformedObjectNameException e) {
/* 123 */         IllegalArgumentException iae = new IllegalArgumentException("Cannot create object name for resource " + resources[i]);
/*     */         
/* 125 */         iae.initCause(e);
/* 126 */         throw iae;
/*     */       }
/*     */     }
/* 129 */     return (String[])results.toArray(new String[results.size()]);
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
/*     */   public String[] getResourceLinks()
/*     */   {
/* 142 */     ContextResourceLink[] resourceLinks = ((NamingResourcesImpl)this.resource).findResourceLinks();
/* 143 */     ArrayList<String> results = new ArrayList();
/* 144 */     for (int i = 0; i < resourceLinks.length; i++) {
/*     */       try
/*     */       {
/* 147 */         ObjectName oname = MBeanUtils.createObjectName(this.managed.getDomain(), resourceLinks[i]);
/* 148 */         results.add(oname.toString());
/*     */       } catch (MalformedObjectNameException e) {
/* 150 */         IllegalArgumentException iae = new IllegalArgumentException("Cannot create object name for resource " + resourceLinks[i]);
/*     */         
/* 152 */         iae.initCause(e);
/* 153 */         throw iae;
/*     */       }
/*     */     }
/* 156 */     return (String[])results.toArray(new String[results.size()]);
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
/*     */   public String addEnvironment(String envName, String type, String value)
/*     */     throws MalformedObjectNameException
/*     */   {
/* 175 */     NamingResourcesImpl nresources = (NamingResourcesImpl)this.resource;
/* 176 */     if (nresources == null) {
/* 177 */       return null;
/*     */     }
/* 179 */     ContextEnvironment env = nresources.findEnvironment(envName);
/* 180 */     if (env != null) {
/* 181 */       throw new IllegalArgumentException("Invalid environment name - already exists '" + envName + "'");
/*     */     }
/*     */     
/* 184 */     env = new ContextEnvironment();
/* 185 */     env.setName(envName);
/* 186 */     env.setType(type);
/* 187 */     env.setValue(value);
/* 188 */     nresources.addEnvironment(env);
/*     */     
/*     */ 
/* 191 */     ManagedBean managed = this.registry.findManagedBean("ContextEnvironment");
/*     */     
/* 193 */     ObjectName oname = MBeanUtils.createObjectName(managed.getDomain(), env);
/* 194 */     return oname.toString();
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
/*     */   public String addResource(String resourceName, String type)
/*     */     throws MalformedObjectNameException
/*     */   {
/* 210 */     NamingResourcesImpl nresources = (NamingResourcesImpl)this.resource;
/* 211 */     if (nresources == null) {
/* 212 */       return null;
/*     */     }
/* 214 */     ContextResource resource = nresources.findResource(resourceName);
/* 215 */     if (resource != null) {
/* 216 */       throw new IllegalArgumentException("Invalid resource name - already exists'" + resourceName + "'");
/*     */     }
/*     */     
/* 219 */     resource = new ContextResource();
/* 220 */     resource.setName(resourceName);
/* 221 */     resource.setType(type);
/* 222 */     nresources.addResource(resource);
/*     */     
/*     */ 
/* 225 */     ManagedBean managed = this.registry.findManagedBean("ContextResource");
/*     */     
/* 227 */     ObjectName oname = MBeanUtils.createObjectName(managed.getDomain(), resource);
/* 228 */     return oname.toString();
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
/*     */   public String addResourceLink(String resourceLinkName, String type)
/*     */     throws MalformedObjectNameException
/*     */   {
/* 243 */     NamingResourcesImpl nresources = (NamingResourcesImpl)this.resource;
/* 244 */     if (nresources == null) {
/* 245 */       return null;
/*     */     }
/*     */     
/* 248 */     ContextResourceLink resourceLink = nresources.findResourceLink(resourceLinkName);
/* 249 */     if (resourceLink != null) {
/* 250 */       throw new IllegalArgumentException("Invalid resource link name - already exists'" + resourceLinkName + "'");
/*     */     }
/*     */     
/*     */ 
/* 254 */     resourceLink = new ContextResourceLink();
/* 255 */     resourceLink.setName(resourceLinkName);
/* 256 */     resourceLink.setType(type);
/* 257 */     nresources.addResourceLink(resourceLink);
/*     */     
/*     */ 
/* 260 */     ManagedBean managed = this.registry.findManagedBean("ContextResourceLink");
/*     */     
/* 262 */     ObjectName oname = MBeanUtils.createObjectName(managed.getDomain(), resourceLink);
/* 263 */     return oname.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeEnvironment(String envName)
/*     */   {
/* 274 */     NamingResourcesImpl nresources = (NamingResourcesImpl)this.resource;
/* 275 */     if (nresources == null) {
/* 276 */       return;
/*     */     }
/* 278 */     ContextEnvironment env = nresources.findEnvironment(envName);
/* 279 */     if (env == null) {
/* 280 */       throw new IllegalArgumentException("Invalid environment name '" + envName + "'");
/*     */     }
/*     */     
/* 283 */     nresources.removeEnvironment(envName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeResource(String resourceName)
/*     */   {
/* 295 */     resourceName = ObjectName.unquote(resourceName);
/* 296 */     NamingResourcesImpl nresources = (NamingResourcesImpl)this.resource;
/* 297 */     if (nresources == null) {
/* 298 */       return;
/*     */     }
/* 300 */     ContextResource resource = nresources.findResource(resourceName);
/* 301 */     if (resource == null) {
/* 302 */       throw new IllegalArgumentException("Invalid resource name '" + resourceName + "'");
/*     */     }
/*     */     
/* 305 */     nresources.removeResource(resourceName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeResourceLink(String resourceLinkName)
/*     */   {
/* 317 */     resourceLinkName = ObjectName.unquote(resourceLinkName);
/* 318 */     NamingResourcesImpl nresources = (NamingResourcesImpl)this.resource;
/* 319 */     if (nresources == null) {
/* 320 */       return;
/*     */     }
/*     */     
/* 323 */     ContextResourceLink resourceLink = nresources.findResourceLink(resourceLinkName);
/* 324 */     if (resourceLink == null) {
/* 325 */       throw new IllegalArgumentException("Invalid resource Link name '" + resourceLinkName + "'");
/*     */     }
/*     */     
/* 328 */     nresources.removeResourceLink(resourceLinkName);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\mbeans\NamingResourcesMBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */