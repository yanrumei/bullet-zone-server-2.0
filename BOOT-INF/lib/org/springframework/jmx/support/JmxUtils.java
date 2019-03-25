/*     */ package org.springframework.jmx.support;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import javax.management.DynamicMBean;
/*     */ import javax.management.JMX;
/*     */ import javax.management.MBeanParameterInfo;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.MBeanServerFactory;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.jmx.MBeanServerNotFoundException;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class JmxUtils
/*     */ {
/*     */   public static final String IDENTITY_OBJECT_NAME_KEY = "identity";
/*     */   private static final String MBEAN_SUFFIX = "MBean";
/*  63 */   private static final Log logger = LogFactory.getLog(JmxUtils.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static MBeanServer locateMBeanServer()
/*     */     throws MBeanServerNotFoundException
/*     */   {
/*  76 */     return locateMBeanServer(null);
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
/*     */   public static MBeanServer locateMBeanServer(String agentId)
/*     */     throws MBeanServerNotFoundException
/*     */   {
/*  92 */     MBeanServer server = null;
/*     */     
/*     */ 
/*  95 */     if (!"".equals(agentId)) {
/*  96 */       List<MBeanServer> servers = MBeanServerFactory.findMBeanServer(agentId);
/*  97 */       if ((servers != null) && (servers.size() > 0))
/*     */       {
/*  99 */         if ((servers.size() > 1) && (logger.isWarnEnabled())) {
/* 100 */           logger.warn("Found more than one MBeanServer instance" + (agentId != null ? " with agent id [" + agentId + "]" : "") + ". Returning first from list.");
/*     */         }
/*     */         
/*     */ 
/* 104 */         server = (MBeanServer)servers.get(0);
/*     */       }
/*     */     }
/*     */     
/* 108 */     if ((server == null) && (!StringUtils.hasLength(agentId))) {
/*     */       try
/*     */       {
/* 111 */         server = ManagementFactory.getPlatformMBeanServer();
/*     */       }
/*     */       catch (SecurityException ex) {
/* 114 */         throw new MBeanServerNotFoundException("No specific MBeanServer found, and not allowed to obtain the Java platform MBeanServer", ex);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 119 */     if (server == null) {
/* 120 */       throw new MBeanServerNotFoundException("Unable to locate an MBeanServer instance" + (agentId != null ? " with agent id [" + agentId + "]" : ""));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 125 */     if (logger.isDebugEnabled()) {
/* 126 */       logger.debug("Found MBeanServer: " + server);
/*     */     }
/* 128 */     return server;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Class<?>[] parameterInfoToTypes(MBeanParameterInfo[] paramInfo)
/*     */     throws ClassNotFoundException
/*     */   {
/* 139 */     return parameterInfoToTypes(paramInfo, ClassUtils.getDefaultClassLoader());
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
/*     */   public static Class<?>[] parameterInfoToTypes(MBeanParameterInfo[] paramInfo, ClassLoader classLoader)
/*     */     throws ClassNotFoundException
/*     */   {
/* 153 */     Class<?>[] types = null;
/* 154 */     if ((paramInfo != null) && (paramInfo.length > 0)) {
/* 155 */       types = new Class[paramInfo.length];
/* 156 */       for (int x = 0; x < paramInfo.length; x++) {
/* 157 */         types[x] = ClassUtils.forName(paramInfo[x].getType(), classLoader);
/*     */       }
/*     */     }
/* 160 */     return types;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] getMethodSignature(Method method)
/*     */   {
/* 171 */     Class<?>[] types = method.getParameterTypes();
/* 172 */     String[] signature = new String[types.length];
/* 173 */     for (int x = 0; x < types.length; x++) {
/* 174 */       signature[x] = types[x].getName();
/*     */     }
/* 176 */     return signature;
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
/*     */   public static String getAttributeName(PropertyDescriptor property, boolean useStrictCasing)
/*     */   {
/* 190 */     if (useStrictCasing) {
/* 191 */       return StringUtils.capitalize(property.getName());
/*     */     }
/*     */     
/* 194 */     return property.getName();
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
/*     */   public static ObjectName appendIdentityToObjectName(ObjectName objectName, Object managedResource)
/*     */     throws MalformedObjectNameException
/*     */   {
/* 215 */     Hashtable<String, String> keyProperties = objectName.getKeyPropertyList();
/* 216 */     keyProperties.put("identity", ObjectUtils.getIdentityHexString(managedResource));
/* 217 */     return ObjectNameManager.getInstance(objectName.getDomain(), keyProperties);
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
/*     */   public static Class<?> getClassToExpose(Object managedBean)
/*     */   {
/* 231 */     return ClassUtils.getUserClass(managedBean);
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
/*     */   public static Class<?> getClassToExpose(Class<?> clazz)
/*     */   {
/* 245 */     return ClassUtils.getUserClass(clazz);
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
/*     */   public static boolean isMBean(Class<?> clazz)
/*     */   {
/* 258 */     return (clazz != null) && (
/* 259 */       (DynamicMBean.class.isAssignableFrom(clazz)) || 
/* 260 */       (getMBeanInterface(clazz) != null) || (getMXBeanInterface(clazz) != null));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Class<?> getMBeanInterface(Class<?> clazz)
/*     */   {
/* 271 */     if ((clazz == null) || (clazz.getSuperclass() == null)) {
/* 272 */       return null;
/*     */     }
/* 274 */     String mbeanInterfaceName = clazz.getName() + "MBean";
/* 275 */     Class<?>[] implementedInterfaces = clazz.getInterfaces();
/* 276 */     for (Class<?> iface : implementedInterfaces) {
/* 277 */       if (iface.getName().equals(mbeanInterfaceName)) {
/* 278 */         return iface;
/*     */       }
/*     */     }
/* 281 */     return getMBeanInterface(clazz.getSuperclass());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Class<?> getMXBeanInterface(Class<?> clazz)
/*     */   {
/* 292 */     if ((clazz == null) || (clazz.getSuperclass() == null)) {
/* 293 */       return null;
/*     */     }
/* 295 */     Class<?>[] implementedInterfaces = clazz.getInterfaces();
/* 296 */     for (Class<?> iface : implementedInterfaces) {
/* 297 */       if (JMX.isMXBeanInterface(iface)) {
/* 298 */         return iface;
/*     */       }
/*     */     }
/* 301 */     return getMXBeanInterface(clazz.getSuperclass());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static boolean isMXBeanSupportAvailable()
/*     */   {
/* 312 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jmx\support\JmxUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */