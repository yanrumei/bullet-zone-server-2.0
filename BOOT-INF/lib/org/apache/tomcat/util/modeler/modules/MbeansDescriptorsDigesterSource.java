/*     */ package org.apache.tomcat.util.modeler.modules;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.digester.Digester;
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
/*     */ public class MbeansDescriptorsDigesterSource
/*     */   extends ModelerSource
/*     */ {
/*  38 */   private static final Log log = LogFactory.getLog(MbeansDescriptorsDigesterSource.class);
/*  39 */   private static final Object dLock = new Object();
/*     */   
/*     */   private Registry registry;
/*  42 */   private final List<ObjectName> mbeans = new ArrayList();
/*  43 */   private static Digester digester = null;
/*     */   
/*     */   private static Digester createDigester()
/*     */   {
/*  47 */     Digester digester = new Digester();
/*  48 */     digester.setNamespaceAware(false);
/*  49 */     digester.setValidating(false);
/*     */     
/*  51 */     URL url = Registry.getRegistry(null, null).getClass().getResource("/org/apache/tomcat/util/modeler/mbeans-descriptors.dtd");
/*  52 */     digester
/*  53 */       .register("-//Apache Software Foundation//DTD Model MBeans Configuration File", url
/*  54 */       .toString());
/*     */     
/*     */ 
/*  57 */     digester
/*  58 */       .addObjectCreate("mbeans-descriptors/mbean", "org.apache.tomcat.util.modeler.ManagedBean");
/*     */     
/*  60 */     digester
/*  61 */       .addSetProperties("mbeans-descriptors/mbean");
/*  62 */     digester
/*  63 */       .addSetNext("mbeans-descriptors/mbean", "add", "java.lang.Object");
/*     */     
/*     */ 
/*     */ 
/*  67 */     digester
/*  68 */       .addObjectCreate("mbeans-descriptors/mbean/attribute", "org.apache.tomcat.util.modeler.AttributeInfo");
/*     */     
/*  70 */     digester
/*  71 */       .addSetProperties("mbeans-descriptors/mbean/attribute");
/*  72 */     digester
/*  73 */       .addSetNext("mbeans-descriptors/mbean/attribute", "addAttribute", "org.apache.tomcat.util.modeler.AttributeInfo");
/*     */     
/*     */ 
/*     */ 
/*  77 */     digester
/*  78 */       .addObjectCreate("mbeans-descriptors/mbean/notification", "org.apache.tomcat.util.modeler.NotificationInfo");
/*     */     
/*  80 */     digester
/*  81 */       .addSetProperties("mbeans-descriptors/mbean/notification");
/*  82 */     digester
/*  83 */       .addSetNext("mbeans-descriptors/mbean/notification", "addNotification", "org.apache.tomcat.util.modeler.NotificationInfo");
/*     */     
/*     */ 
/*     */ 
/*  87 */     digester
/*  88 */       .addObjectCreate("mbeans-descriptors/mbean/notification/descriptor/field", "org.apache.tomcat.util.modeler.FieldInfo");
/*     */     
/*  90 */     digester
/*  91 */       .addSetProperties("mbeans-descriptors/mbean/notification/descriptor/field");
/*  92 */     digester
/*  93 */       .addSetNext("mbeans-descriptors/mbean/notification/descriptor/field", "addField", "org.apache.tomcat.util.modeler.FieldInfo");
/*     */     
/*     */ 
/*     */ 
/*  97 */     digester
/*  98 */       .addCallMethod("mbeans-descriptors/mbean/notification/notification-type", "addNotifType", 0);
/*     */     
/*     */ 
/* 101 */     digester
/* 102 */       .addObjectCreate("mbeans-descriptors/mbean/operation", "org.apache.tomcat.util.modeler.OperationInfo");
/*     */     
/* 104 */     digester
/* 105 */       .addSetProperties("mbeans-descriptors/mbean/operation");
/* 106 */     digester
/* 107 */       .addSetNext("mbeans-descriptors/mbean/operation", "addOperation", "org.apache.tomcat.util.modeler.OperationInfo");
/*     */     
/*     */ 
/*     */ 
/* 111 */     digester
/* 112 */       .addObjectCreate("mbeans-descriptors/mbean/operation/descriptor/field", "org.apache.tomcat.util.modeler.FieldInfo");
/*     */     
/* 114 */     digester
/* 115 */       .addSetProperties("mbeans-descriptors/mbean/operation/descriptor/field");
/* 116 */     digester
/* 117 */       .addSetNext("mbeans-descriptors/mbean/operation/descriptor/field", "addField", "org.apache.tomcat.util.modeler.FieldInfo");
/*     */     
/*     */ 
/*     */ 
/* 121 */     digester
/* 122 */       .addObjectCreate("mbeans-descriptors/mbean/operation/parameter", "org.apache.tomcat.util.modeler.ParameterInfo");
/*     */     
/* 124 */     digester
/* 125 */       .addSetProperties("mbeans-descriptors/mbean/operation/parameter");
/* 126 */     digester
/* 127 */       .addSetNext("mbeans-descriptors/mbean/operation/parameter", "addParameter", "org.apache.tomcat.util.modeler.ParameterInfo");
/*     */     
/*     */ 
/*     */ 
/* 131 */     return digester;
/*     */   }
/*     */   
/*     */   public void setRegistry(Registry reg)
/*     */   {
/* 136 */     this.registry = reg;
/*     */   }
/*     */   
/*     */   public void setSource(Object source)
/*     */   {
/* 141 */     this.source = source;
/*     */   }
/*     */   
/*     */   public List<ObjectName> loadDescriptors(Registry registry, String type, Object source)
/*     */     throws Exception
/*     */   {
/* 147 */     setRegistry(registry);
/* 148 */     setSource(source);
/* 149 */     execute();
/* 150 */     return this.mbeans;
/*     */   }
/*     */   
/*     */   public void execute() throws Exception {
/* 154 */     if (this.registry == null) {
/* 155 */       this.registry = Registry.getRegistry(null, null);
/*     */     }
/*     */     
/* 158 */     InputStream stream = (InputStream)this.source;
/*     */     
/* 160 */     ArrayList<ManagedBean> loadedMbeans = new ArrayList();
/* 161 */     synchronized (dLock) {
/* 162 */       if (digester == null) {
/* 163 */         digester = createDigester();
/*     */       }
/*     */       
/*     */ 
/*     */       try
/*     */       {
/* 169 */         digester.push(loadedMbeans);
/* 170 */         digester.parse(stream);
/*     */       } catch (Exception e) {
/* 172 */         log.error("Error digesting Registry data", e);
/* 173 */         throw e;
/*     */       } finally {
/* 175 */         digester.reset();
/*     */       }
/*     */     }
/*     */     
/* 179 */     Iterator<ManagedBean> iter = loadedMbeans.iterator();
/* 180 */     while (iter.hasNext()) {
/* 181 */       this.registry.addManagedBean((ManagedBean)iter.next());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\modeler\modules\MbeansDescriptorsDigesterSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */