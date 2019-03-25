/*     */ package org.apache.catalina.mbeans;
/*     */ 
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.RuntimeOperationsException;
/*     */ import javax.management.modelmbean.InvalidTargetObjectTypeException;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.tomcat.util.descriptor.web.ApplicationParameter;
/*     */ import org.apache.tomcat.util.descriptor.web.ErrorPage;
/*     */ import org.apache.tomcat.util.descriptor.web.FilterDef;
/*     */ import org.apache.tomcat.util.descriptor.web.FilterMap;
/*     */ import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContextMBean
/*     */   extends ContainerMBean
/*     */ {
/*     */   public ContextMBean()
/*     */     throws MBeanException, RuntimeOperationsException
/*     */   {}
/*     */   
/*     */   public String[] findApplicationParameters()
/*     */     throws MBeanException
/*     */   {
/*     */     try
/*     */     {
/*  48 */       context = (Context)getManagedResource();
/*     */     } catch (InstanceNotFoundException e) { Context context;
/*  50 */       throw new MBeanException(e);
/*     */     } catch (RuntimeOperationsException e) {
/*  52 */       throw new MBeanException(e);
/*     */     } catch (InvalidTargetObjectTypeException e) {
/*  54 */       throw new MBeanException(e);
/*     */     }
/*     */     Context context;
/*  57 */     ApplicationParameter[] params = context.findApplicationParameters();
/*  58 */     String[] stringParams = new String[params.length];
/*  59 */     for (int counter = 0; counter < params.length; counter++) {
/*  60 */       stringParams[counter] = params[counter].toString();
/*     */     }
/*     */     
/*  63 */     return stringParams;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] findConstraints()
/*     */     throws MBeanException
/*     */   {
/*     */     try
/*     */     {
/*  78 */       context = (Context)getManagedResource();
/*     */     } catch (InstanceNotFoundException e) { Context context;
/*  80 */       throw new MBeanException(e);
/*     */     } catch (RuntimeOperationsException e) {
/*  82 */       throw new MBeanException(e);
/*     */     } catch (InvalidTargetObjectTypeException e) {
/*  84 */       throw new MBeanException(e);
/*     */     }
/*     */     Context context;
/*  87 */     SecurityConstraint[] constraints = context.findConstraints();
/*  88 */     String[] stringConstraints = new String[constraints.length];
/*  89 */     for (int counter = 0; counter < constraints.length; counter++) {
/*  90 */       stringConstraints[counter] = constraints[counter].toString();
/*     */     }
/*     */     
/*  93 */     return stringConstraints;
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
/*     */   public String findErrorPage(int errorCode)
/*     */     throws MBeanException
/*     */   {
/*     */     try
/*     */     {
/* 109 */       context = (Context)getManagedResource();
/*     */     } catch (InstanceNotFoundException e) { Context context;
/* 111 */       throw new MBeanException(e);
/*     */     } catch (RuntimeOperationsException e) {
/* 113 */       throw new MBeanException(e);
/*     */     } catch (InvalidTargetObjectTypeException e) {
/* 115 */       throw new MBeanException(e);
/*     */     }
/*     */     Context context;
/* 118 */     return context.findErrorPage(errorCode).toString();
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
/*     */   public String findErrorPage(String exceptionType)
/*     */     throws MBeanException
/*     */   {
/*     */     try
/*     */     {
/* 134 */       context = (Context)getManagedResource();
/*     */     } catch (InstanceNotFoundException e) { Context context;
/* 136 */       throw new MBeanException(e);
/*     */     } catch (RuntimeOperationsException e) {
/* 138 */       throw new MBeanException(e);
/*     */     } catch (InvalidTargetObjectTypeException e) {
/* 140 */       throw new MBeanException(e);
/*     */     }
/*     */     Context context;
/* 143 */     return context.findErrorPage(exceptionType).toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] findErrorPages()
/*     */     throws MBeanException
/*     */   {
/*     */     try
/*     */     {
/* 157 */       context = (Context)getManagedResource();
/*     */     } catch (InstanceNotFoundException e) { Context context;
/* 159 */       throw new MBeanException(e);
/*     */     } catch (RuntimeOperationsException e) {
/* 161 */       throw new MBeanException(e);
/*     */     } catch (InvalidTargetObjectTypeException e) {
/* 163 */       throw new MBeanException(e);
/*     */     }
/*     */     Context context;
/* 166 */     ErrorPage[] pages = context.findErrorPages();
/* 167 */     String[] stringPages = new String[pages.length];
/* 168 */     for (int counter = 0; counter < pages.length; counter++) {
/* 169 */       stringPages[counter] = pages[counter].toString();
/*     */     }
/*     */     
/* 172 */     return stringPages;
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
/*     */   public String findFilterDef(String name)
/*     */     throws MBeanException
/*     */   {
/*     */     try
/*     */     {
/* 188 */       context = (Context)getManagedResource();
/*     */     } catch (InstanceNotFoundException e) { Context context;
/* 190 */       throw new MBeanException(e);
/*     */     } catch (RuntimeOperationsException e) {
/* 192 */       throw new MBeanException(e);
/*     */     } catch (InvalidTargetObjectTypeException e) {
/* 194 */       throw new MBeanException(e);
/*     */     }
/*     */     Context context;
/* 197 */     FilterDef filterDef = context.findFilterDef(name);
/* 198 */     return filterDef.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] findFilterDefs()
/*     */     throws MBeanException
/*     */   {
/*     */     try
/*     */     {
/* 212 */       context = (Context)getManagedResource();
/*     */     } catch (InstanceNotFoundException e) { Context context;
/* 214 */       throw new MBeanException(e);
/*     */     } catch (RuntimeOperationsException e) {
/* 216 */       throw new MBeanException(e);
/*     */     } catch (InvalidTargetObjectTypeException e) {
/* 218 */       throw new MBeanException(e);
/*     */     }
/*     */     Context context;
/* 221 */     FilterDef[] filterDefs = context.findFilterDefs();
/* 222 */     String[] stringFilters = new String[filterDefs.length];
/* 223 */     for (int counter = 0; counter < filterDefs.length; counter++) {
/* 224 */       stringFilters[counter] = filterDefs[counter].toString();
/*     */     }
/*     */     
/* 227 */     return stringFilters;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] findFilterMaps()
/*     */     throws MBeanException
/*     */   {
/*     */     try
/*     */     {
/* 240 */       context = (Context)getManagedResource();
/*     */     } catch (InstanceNotFoundException e) { Context context;
/* 242 */       throw new MBeanException(e);
/*     */     } catch (RuntimeOperationsException e) {
/* 244 */       throw new MBeanException(e);
/*     */     } catch (InvalidTargetObjectTypeException e) {
/* 246 */       throw new MBeanException(e);
/*     */     }
/*     */     Context context;
/* 249 */     FilterMap[] maps = context.findFilterMaps();
/* 250 */     String[] stringMaps = new String[maps.length];
/* 251 */     for (int counter = 0; counter < maps.length; counter++) {
/* 252 */       stringMaps[counter] = maps[counter].toString();
/*     */     }
/*     */     
/* 255 */     return stringMaps;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\mbeans\ContextMBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */