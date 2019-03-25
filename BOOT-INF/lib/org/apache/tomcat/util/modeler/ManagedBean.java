/*     */ package org.apache.tomcat.util.modeler;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import javax.management.AttributeNotFoundException;
/*     */ import javax.management.DynamicMBean;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.MBeanInfo;
/*     */ import javax.management.ReflectionException;
/*     */ import javax.management.RuntimeOperationsException;
/*     */ import javax.management.ServiceNotFoundException;
/*     */ import org.apache.tomcat.util.buf.StringUtils;
/*     */ import org.apache.tomcat.util.buf.StringUtils.Function;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ManagedBean
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final String BASE_MBEAN = "org.apache.tomcat.util.modeler.BaseModelMBean";
/*  57 */   static final Class<?>[] NO_ARGS_PARAM_SIG = new Class[0];
/*     */   
/*     */ 
/*  60 */   private final ReadWriteLock mBeanInfoLock = new ReentrantReadWriteLock();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  65 */   private volatile transient MBeanInfo info = null;
/*     */   
/*  67 */   private Map<String, AttributeInfo> attributes = new HashMap();
/*     */   
/*  69 */   private Map<String, OperationInfo> operations = new HashMap();
/*     */   
/*  71 */   protected String className = "org.apache.tomcat.util.modeler.BaseModelMBean";
/*  72 */   protected String description = null;
/*  73 */   protected String domain = null;
/*  74 */   protected String group = null;
/*  75 */   protected String name = null;
/*     */   
/*  77 */   private NotificationInfo[] notifications = new NotificationInfo[0];
/*  78 */   protected String type = null;
/*     */   
/*     */ 
/*     */ 
/*     */   public ManagedBean()
/*     */   {
/*  84 */     AttributeInfo ai = new AttributeInfo();
/*  85 */     ai.setName("modelerType");
/*  86 */     ai.setDescription("Type of the modeled resource. Can be set only once");
/*  87 */     ai.setType("java.lang.String");
/*  88 */     ai.setWriteable(false);
/*  89 */     addAttribute(ai);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AttributeInfo[] getAttributes()
/*     */   {
/*  99 */     AttributeInfo[] result = new AttributeInfo[this.attributes.size()];
/* 100 */     this.attributes.values().toArray(result);
/* 101 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 113 */     return this.className;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void setClassName(String className)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 4	org/apache/tomcat/util/modeler/ManagedBean:mBeanInfoLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   4: invokeinterface 33 1 0
/*     */     //   9: invokeinterface 34 1 0
/*     */     //   14: aload_0
/*     */     //   15: aload_1
/*     */     //   16: putfield 12	org/apache/tomcat/util/modeler/ManagedBean:className	Ljava/lang/String;
/*     */     //   19: aload_0
/*     */     //   20: aconst_null
/*     */     //   21: putfield 5	org/apache/tomcat/util/modeler/ManagedBean:info	Ljavax/management/MBeanInfo;
/*     */     //   24: aload_0
/*     */     //   25: getfield 4	org/apache/tomcat/util/modeler/ManagedBean:mBeanInfoLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   28: invokeinterface 33 1 0
/*     */     //   33: invokeinterface 35 1 0
/*     */     //   38: goto +20 -> 58
/*     */     //   41: astore_2
/*     */     //   42: aload_0
/*     */     //   43: getfield 4	org/apache/tomcat/util/modeler/ManagedBean:mBeanInfoLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   46: invokeinterface 33 1 0
/*     */     //   51: invokeinterface 35 1 0
/*     */     //   56: aload_2
/*     */     //   57: athrow
/*     */     //   58: return
/*     */     // Line number table:
/*     */     //   Java source line #117	-> byte code offset #0
/*     */     //   Java source line #119	-> byte code offset #14
/*     */     //   Java source line #120	-> byte code offset #19
/*     */     //   Java source line #122	-> byte code offset #24
/*     */     //   Java source line #123	-> byte code offset #38
/*     */     //   Java source line #122	-> byte code offset #41
/*     */     //   Java source line #124	-> byte code offset #58
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	59	0	this	ManagedBean
/*     */     //   0	59	1	className	String
/*     */     //   41	16	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   14	24	41	finally
/*     */   }
/*     */   
/*     */   public String getDescription()
/*     */   {
/* 131 */     return this.description;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void setDescription(String description)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 4	org/apache/tomcat/util/modeler/ManagedBean:mBeanInfoLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   4: invokeinterface 33 1 0
/*     */     //   9: invokeinterface 34 1 0
/*     */     //   14: aload_0
/*     */     //   15: aload_1
/*     */     //   16: putfield 13	org/apache/tomcat/util/modeler/ManagedBean:description	Ljava/lang/String;
/*     */     //   19: aload_0
/*     */     //   20: aconst_null
/*     */     //   21: putfield 5	org/apache/tomcat/util/modeler/ManagedBean:info	Ljavax/management/MBeanInfo;
/*     */     //   24: aload_0
/*     */     //   25: getfield 4	org/apache/tomcat/util/modeler/ManagedBean:mBeanInfoLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   28: invokeinterface 33 1 0
/*     */     //   33: invokeinterface 35 1 0
/*     */     //   38: goto +20 -> 58
/*     */     //   41: astore_2
/*     */     //   42: aload_0
/*     */     //   43: getfield 4	org/apache/tomcat/util/modeler/ManagedBean:mBeanInfoLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   46: invokeinterface 33 1 0
/*     */     //   51: invokeinterface 35 1 0
/*     */     //   56: aload_2
/*     */     //   57: athrow
/*     */     //   58: return
/*     */     // Line number table:
/*     */     //   Java source line #135	-> byte code offset #0
/*     */     //   Java source line #137	-> byte code offset #14
/*     */     //   Java source line #138	-> byte code offset #19
/*     */     //   Java source line #140	-> byte code offset #24
/*     */     //   Java source line #141	-> byte code offset #38
/*     */     //   Java source line #140	-> byte code offset #41
/*     */     //   Java source line #142	-> byte code offset #58
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	59	0	this	ManagedBean
/*     */     //   0	59	1	description	String
/*     */     //   41	16	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   14	24	41	finally
/*     */   }
/*     */   
/*     */   public String getDomain()
/*     */   {
/* 150 */     return this.domain;
/*     */   }
/*     */   
/*     */   public void setDomain(String domain) {
/* 154 */     this.domain = domain;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getGroup()
/*     */   {
/* 162 */     return this.group;
/*     */   }
/*     */   
/*     */   public void setGroup(String group) {
/* 166 */     this.group = group;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 175 */     return this.name;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void setName(String name)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 4	org/apache/tomcat/util/modeler/ManagedBean:mBeanInfoLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   4: invokeinterface 33 1 0
/*     */     //   9: invokeinterface 34 1 0
/*     */     //   14: aload_0
/*     */     //   15: aload_1
/*     */     //   16: putfield 16	org/apache/tomcat/util/modeler/ManagedBean:name	Ljava/lang/String;
/*     */     //   19: aload_0
/*     */     //   20: aconst_null
/*     */     //   21: putfield 5	org/apache/tomcat/util/modeler/ManagedBean:info	Ljavax/management/MBeanInfo;
/*     */     //   24: aload_0
/*     */     //   25: getfield 4	org/apache/tomcat/util/modeler/ManagedBean:mBeanInfoLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   28: invokeinterface 33 1 0
/*     */     //   33: invokeinterface 35 1 0
/*     */     //   38: goto +20 -> 58
/*     */     //   41: astore_2
/*     */     //   42: aload_0
/*     */     //   43: getfield 4	org/apache/tomcat/util/modeler/ManagedBean:mBeanInfoLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   46: invokeinterface 33 1 0
/*     */     //   51: invokeinterface 35 1 0
/*     */     //   56: aload_2
/*     */     //   57: athrow
/*     */     //   58: return
/*     */     // Line number table:
/*     */     //   Java source line #179	-> byte code offset #0
/*     */     //   Java source line #181	-> byte code offset #14
/*     */     //   Java source line #182	-> byte code offset #19
/*     */     //   Java source line #184	-> byte code offset #24
/*     */     //   Java source line #185	-> byte code offset #38
/*     */     //   Java source line #184	-> byte code offset #41
/*     */     //   Java source line #186	-> byte code offset #58
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	59	0	this	ManagedBean
/*     */     //   0	59	1	name	String
/*     */     //   41	16	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   14	24	41	finally
/*     */   }
/*     */   
/*     */   public NotificationInfo[] getNotifications()
/*     */   {
/* 193 */     return this.notifications;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public OperationInfo[] getOperations()
/*     */   {
/* 201 */     OperationInfo[] result = new OperationInfo[this.operations.size()];
/* 202 */     this.operations.values().toArray(result);
/* 203 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getType()
/*     */   {
/* 213 */     return this.type;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void setType(String type)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 4	org/apache/tomcat/util/modeler/ManagedBean:mBeanInfoLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   4: invokeinterface 33 1 0
/*     */     //   9: invokeinterface 34 1 0
/*     */     //   14: aload_0
/*     */     //   15: aload_1
/*     */     //   16: putfield 19	org/apache/tomcat/util/modeler/ManagedBean:type	Ljava/lang/String;
/*     */     //   19: aload_0
/*     */     //   20: aconst_null
/*     */     //   21: putfield 5	org/apache/tomcat/util/modeler/ManagedBean:info	Ljavax/management/MBeanInfo;
/*     */     //   24: aload_0
/*     */     //   25: getfield 4	org/apache/tomcat/util/modeler/ManagedBean:mBeanInfoLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   28: invokeinterface 33 1 0
/*     */     //   33: invokeinterface 35 1 0
/*     */     //   38: goto +20 -> 58
/*     */     //   41: astore_2
/*     */     //   42: aload_0
/*     */     //   43: getfield 4	org/apache/tomcat/util/modeler/ManagedBean:mBeanInfoLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   46: invokeinterface 33 1 0
/*     */     //   51: invokeinterface 35 1 0
/*     */     //   56: aload_2
/*     */     //   57: athrow
/*     */     //   58: return
/*     */     // Line number table:
/*     */     //   Java source line #217	-> byte code offset #0
/*     */     //   Java source line #219	-> byte code offset #14
/*     */     //   Java source line #220	-> byte code offset #19
/*     */     //   Java source line #222	-> byte code offset #24
/*     */     //   Java source line #223	-> byte code offset #38
/*     */     //   Java source line #222	-> byte code offset #41
/*     */     //   Java source line #224	-> byte code offset #58
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	59	0	this	ManagedBean
/*     */     //   0	59	1	type	String
/*     */     //   41	16	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   14	24	41	finally
/*     */   }
/*     */   
/*     */   public void addAttribute(AttributeInfo attribute)
/*     */   {
/* 236 */     this.attributes.put(attribute.getName(), attribute);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void addNotification(NotificationInfo notification)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 4	org/apache/tomcat/util/modeler/ManagedBean:mBeanInfoLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   4: invokeinterface 33 1 0
/*     */     //   9: invokeinterface 34 1 0
/*     */     //   14: aload_0
/*     */     //   15: getfield 18	org/apache/tomcat/util/modeler/ManagedBean:notifications	[Lorg/apache/tomcat/util/modeler/NotificationInfo;
/*     */     //   18: arraylength
/*     */     //   19: iconst_1
/*     */     //   20: iadd
/*     */     //   21: anewarray 17	org/apache/tomcat/util/modeler/NotificationInfo
/*     */     //   24: astore_2
/*     */     //   25: aload_0
/*     */     //   26: getfield 18	org/apache/tomcat/util/modeler/ManagedBean:notifications	[Lorg/apache/tomcat/util/modeler/NotificationInfo;
/*     */     //   29: iconst_0
/*     */     //   30: aload_2
/*     */     //   31: iconst_0
/*     */     //   32: aload_0
/*     */     //   33: getfield 18	org/apache/tomcat/util/modeler/ManagedBean:notifications	[Lorg/apache/tomcat/util/modeler/NotificationInfo;
/*     */     //   36: arraylength
/*     */     //   37: invokestatic 39	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
/*     */     //   40: aload_2
/*     */     //   41: aload_0
/*     */     //   42: getfield 18	org/apache/tomcat/util/modeler/ManagedBean:notifications	[Lorg/apache/tomcat/util/modeler/NotificationInfo;
/*     */     //   45: arraylength
/*     */     //   46: aload_1
/*     */     //   47: aastore
/*     */     //   48: aload_0
/*     */     //   49: aload_2
/*     */     //   50: putfield 18	org/apache/tomcat/util/modeler/ManagedBean:notifications	[Lorg/apache/tomcat/util/modeler/NotificationInfo;
/*     */     //   53: aload_0
/*     */     //   54: aconst_null
/*     */     //   55: putfield 5	org/apache/tomcat/util/modeler/ManagedBean:info	Ljavax/management/MBeanInfo;
/*     */     //   58: aload_0
/*     */     //   59: getfield 4	org/apache/tomcat/util/modeler/ManagedBean:mBeanInfoLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   62: invokeinterface 33 1 0
/*     */     //   67: invokeinterface 35 1 0
/*     */     //   72: goto +20 -> 92
/*     */     //   75: astore_3
/*     */     //   76: aload_0
/*     */     //   77: getfield 4	org/apache/tomcat/util/modeler/ManagedBean:mBeanInfoLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   80: invokeinterface 33 1 0
/*     */     //   85: invokeinterface 35 1 0
/*     */     //   90: aload_3
/*     */     //   91: athrow
/*     */     //   92: return
/*     */     // Line number table:
/*     */     //   Java source line #246	-> byte code offset #0
/*     */     //   Java source line #248	-> byte code offset #14
/*     */     //   Java source line #250	-> byte code offset #25
/*     */     //   Java source line #252	-> byte code offset #40
/*     */     //   Java source line #253	-> byte code offset #48
/*     */     //   Java source line #254	-> byte code offset #53
/*     */     //   Java source line #256	-> byte code offset #58
/*     */     //   Java source line #257	-> byte code offset #72
/*     */     //   Java source line #256	-> byte code offset #75
/*     */     //   Java source line #258	-> byte code offset #92
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	93	0	this	ManagedBean
/*     */     //   0	93	1	notification	NotificationInfo
/*     */     //   24	26	2	results	NotificationInfo[]
/*     */     //   75	16	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   14	58	75	finally
/*     */   }
/*     */   
/*     */   public void addOperation(OperationInfo operation)
/*     */   {
/* 267 */     this.operations.put(createOperationKey(operation), operation);
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
/*     */ 
/*     */   public DynamicMBean createMBean(Object instance)
/*     */     throws InstanceNotFoundException, MBeanException, RuntimeOperationsException
/*     */   {
/* 292 */     BaseModelMBean mbean = null;
/*     */     
/*     */ 
/* 295 */     if (getClassName().equals("org.apache.tomcat.util.modeler.BaseModelMBean"))
/*     */     {
/* 297 */       mbean = new BaseModelMBean();
/*     */     } else {
/* 299 */       Class<?> clazz = null;
/* 300 */       Exception ex = null;
/*     */       try {
/* 302 */         clazz = Class.forName(getClassName());
/*     */       }
/*     */       catch (Exception localException1) {}
/*     */       
/* 306 */       if (clazz == null) {
/*     */         try {
/* 308 */           ClassLoader cl = Thread.currentThread().getContextClassLoader();
/* 309 */           if (cl != null)
/* 310 */             clazz = cl.loadClass(getClassName());
/*     */         } catch (Exception e) {
/* 312 */           ex = e;
/*     */         }
/*     */       }
/*     */       
/* 316 */       if (clazz == null)
/*     */       {
/* 318 */         throw new MBeanException(ex, "Cannot load ModelMBean class " + getClassName());
/*     */       }
/*     */       try
/*     */       {
/* 322 */         mbean = (BaseModelMBean)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/*     */       } catch (RuntimeOperationsException e) {
/* 324 */         throw e;
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 328 */         throw new MBeanException(e, "Cannot instantiate ModelMBean of class " + getClassName());
/*     */       }
/*     */     }
/*     */     
/* 332 */     mbean.setManagedBean(this);
/*     */     
/*     */     try
/*     */     {
/* 336 */       if (instance != null)
/* 337 */         mbean.setManagedResource(instance, "ObjectReference");
/*     */     } catch (InstanceNotFoundException e) {
/* 339 */       throw e;
/*     */     }
/*     */     
/* 342 */     return mbean;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   MBeanInfo getMBeanInfo()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 4	org/apache/tomcat/util/modeler/ManagedBean:mBeanInfoLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   4: invokeinterface 67 1 0
/*     */     //   9: invokeinterface 34 1 0
/*     */     //   14: aload_0
/*     */     //   15: getfield 5	org/apache/tomcat/util/modeler/ManagedBean:info	Ljavax/management/MBeanInfo;
/*     */     //   18: ifnull +24 -> 42
/*     */     //   21: aload_0
/*     */     //   22: getfield 5	org/apache/tomcat/util/modeler/ManagedBean:info	Ljavax/management/MBeanInfo;
/*     */     //   25: astore_1
/*     */     //   26: aload_0
/*     */     //   27: getfield 4	org/apache/tomcat/util/modeler/ManagedBean:mBeanInfoLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   30: invokeinterface 67 1 0
/*     */     //   35: invokeinterface 35 1 0
/*     */     //   40: aload_1
/*     */     //   41: areturn
/*     */     //   42: aload_0
/*     */     //   43: getfield 4	org/apache/tomcat/util/modeler/ManagedBean:mBeanInfoLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   46: invokeinterface 67 1 0
/*     */     //   51: invokeinterface 35 1 0
/*     */     //   56: goto +20 -> 76
/*     */     //   59: astore_2
/*     */     //   60: aload_0
/*     */     //   61: getfield 4	org/apache/tomcat/util/modeler/ManagedBean:mBeanInfoLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   64: invokeinterface 67 1 0
/*     */     //   69: invokeinterface 35 1 0
/*     */     //   74: aload_2
/*     */     //   75: athrow
/*     */     //   76: aload_0
/*     */     //   77: getfield 4	org/apache/tomcat/util/modeler/ManagedBean:mBeanInfoLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   80: invokeinterface 33 1 0
/*     */     //   85: invokeinterface 34 1 0
/*     */     //   90: aload_0
/*     */     //   91: getfield 5	org/apache/tomcat/util/modeler/ManagedBean:info	Ljavax/management/MBeanInfo;
/*     */     //   94: ifnonnull +149 -> 243
/*     */     //   97: aload_0
/*     */     //   98: invokevirtual 68	org/apache/tomcat/util/modeler/ManagedBean:getAttributes	()[Lorg/apache/tomcat/util/modeler/AttributeInfo;
/*     */     //   101: astore_1
/*     */     //   102: aload_1
/*     */     //   103: arraylength
/*     */     //   104: anewarray 69	javax/management/MBeanAttributeInfo
/*     */     //   107: astore_2
/*     */     //   108: iconst_0
/*     */     //   109: istore_3
/*     */     //   110: iload_3
/*     */     //   111: aload_1
/*     */     //   112: arraylength
/*     */     //   113: if_icmpge +18 -> 131
/*     */     //   116: aload_2
/*     */     //   117: iload_3
/*     */     //   118: aload_1
/*     */     //   119: iload_3
/*     */     //   120: aaload
/*     */     //   121: invokevirtual 70	org/apache/tomcat/util/modeler/AttributeInfo:createAttributeInfo	()Ljavax/management/MBeanAttributeInfo;
/*     */     //   124: aastore
/*     */     //   125: iinc 3 1
/*     */     //   128: goto -18 -> 110
/*     */     //   131: aload_0
/*     */     //   132: invokevirtual 71	org/apache/tomcat/util/modeler/ManagedBean:getOperations	()[Lorg/apache/tomcat/util/modeler/OperationInfo;
/*     */     //   135: astore_3
/*     */     //   136: aload_3
/*     */     //   137: arraylength
/*     */     //   138: anewarray 72	javax/management/MBeanOperationInfo
/*     */     //   141: astore 4
/*     */     //   143: iconst_0
/*     */     //   144: istore 5
/*     */     //   146: iload 5
/*     */     //   148: aload_3
/*     */     //   149: arraylength
/*     */     //   150: if_icmpge +21 -> 171
/*     */     //   153: aload 4
/*     */     //   155: iload 5
/*     */     //   157: aload_3
/*     */     //   158: iload 5
/*     */     //   160: aaload
/*     */     //   161: invokevirtual 73	org/apache/tomcat/util/modeler/OperationInfo:createOperationInfo	()Ljavax/management/MBeanOperationInfo;
/*     */     //   164: aastore
/*     */     //   165: iinc 5 1
/*     */     //   168: goto -22 -> 146
/*     */     //   171: aload_0
/*     */     //   172: invokevirtual 74	org/apache/tomcat/util/modeler/ManagedBean:getNotifications	()[Lorg/apache/tomcat/util/modeler/NotificationInfo;
/*     */     //   175: astore 5
/*     */     //   177: aload 5
/*     */     //   179: arraylength
/*     */     //   180: anewarray 75	javax/management/MBeanNotificationInfo
/*     */     //   183: astore 6
/*     */     //   185: iconst_0
/*     */     //   186: istore 7
/*     */     //   188: iload 7
/*     */     //   190: aload 5
/*     */     //   192: arraylength
/*     */     //   193: if_icmpge +22 -> 215
/*     */     //   196: aload 6
/*     */     //   198: iload 7
/*     */     //   200: aload 5
/*     */     //   202: iload 7
/*     */     //   204: aaload
/*     */     //   205: invokevirtual 76	org/apache/tomcat/util/modeler/NotificationInfo:createNotificationInfo	()Ljavax/management/MBeanNotificationInfo;
/*     */     //   208: aastore
/*     */     //   209: iinc 7 1
/*     */     //   212: goto -24 -> 188
/*     */     //   215: aload_0
/*     */     //   216: new 77	javax/management/MBeanInfo
/*     */     //   219: dup
/*     */     //   220: aload_0
/*     */     //   221: invokevirtual 41	org/apache/tomcat/util/modeler/ManagedBean:getClassName	()Ljava/lang/String;
/*     */     //   224: aload_0
/*     */     //   225: invokevirtual 78	org/apache/tomcat/util/modeler/ManagedBean:getDescription	()Ljava/lang/String;
/*     */     //   228: aload_2
/*     */     //   229: iconst_0
/*     */     //   230: anewarray 79	javax/management/MBeanConstructorInfo
/*     */     //   233: aload 4
/*     */     //   235: aload 6
/*     */     //   237: invokespecial 80	javax/management/MBeanInfo:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljavax/management/MBeanAttributeInfo;[Ljavax/management/MBeanConstructorInfo;[Ljavax/management/MBeanOperationInfo;[Ljavax/management/MBeanNotificationInfo;)V
/*     */     //   240: putfield 5	org/apache/tomcat/util/modeler/ManagedBean:info	Ljavax/management/MBeanInfo;
/*     */     //   243: aload_0
/*     */     //   244: getfield 5	org/apache/tomcat/util/modeler/ManagedBean:info	Ljavax/management/MBeanInfo;
/*     */     //   247: astore_1
/*     */     //   248: aload_0
/*     */     //   249: getfield 4	org/apache/tomcat/util/modeler/ManagedBean:mBeanInfoLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   252: invokeinterface 33 1 0
/*     */     //   257: invokeinterface 35 1 0
/*     */     //   262: aload_1
/*     */     //   263: areturn
/*     */     //   264: astore 8
/*     */     //   266: aload_0
/*     */     //   267: getfield 4	org/apache/tomcat/util/modeler/ManagedBean:mBeanInfoLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   270: invokeinterface 33 1 0
/*     */     //   275: invokeinterface 35 1 0
/*     */     //   280: aload 8
/*     */     //   282: athrow
/*     */     // Line number table:
/*     */     //   Java source line #354	-> byte code offset #0
/*     */     //   Java source line #356	-> byte code offset #14
/*     */     //   Java source line #357	-> byte code offset #21
/*     */     //   Java source line #360	-> byte code offset #26
/*     */     //   Java source line #357	-> byte code offset #40
/*     */     //   Java source line #360	-> byte code offset #42
/*     */     //   Java source line #361	-> byte code offset #56
/*     */     //   Java source line #360	-> byte code offset #59
/*     */     //   Java source line #363	-> byte code offset #76
/*     */     //   Java source line #365	-> byte code offset #90
/*     */     //   Java source line #367	-> byte code offset #97
/*     */     //   Java source line #368	-> byte code offset #102
/*     */     //   Java source line #370	-> byte code offset #108
/*     */     //   Java source line #371	-> byte code offset #116
/*     */     //   Java source line #370	-> byte code offset #125
/*     */     //   Java source line #373	-> byte code offset #131
/*     */     //   Java source line #374	-> byte code offset #136
/*     */     //   Java source line #376	-> byte code offset #143
/*     */     //   Java source line #377	-> byte code offset #153
/*     */     //   Java source line #376	-> byte code offset #165
/*     */     //   Java source line #380	-> byte code offset #171
/*     */     //   Java source line #381	-> byte code offset #177
/*     */     //   Java source line #383	-> byte code offset #185
/*     */     //   Java source line #384	-> byte code offset #196
/*     */     //   Java source line #383	-> byte code offset #209
/*     */     //   Java source line #388	-> byte code offset #215
/*     */     //   Java source line #389	-> byte code offset #225
/*     */     //   Java source line #396	-> byte code offset #243
/*     */     //   Java source line #398	-> byte code offset #248
/*     */     //   Java source line #396	-> byte code offset #262
/*     */     //   Java source line #398	-> byte code offset #264
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	283	0	this	ManagedBean
/*     */     //   25	16	1	localMBeanInfo	MBeanInfo
/*     */     //   101	162	1	attrs	Object
/*     */     //   59	16	2	localObject1	Object
/*     */     //   107	122	2	attributes	javax.management.MBeanAttributeInfo[]
/*     */     //   109	17	3	i	int
/*     */     //   135	23	3	opers	OperationInfo[]
/*     */     //   141	93	4	operations	javax.management.MBeanOperationInfo[]
/*     */     //   144	22	5	i	int
/*     */     //   175	26	5	notifs	NotificationInfo[]
/*     */     //   183	53	6	notifications	javax.management.MBeanNotificationInfo[]
/*     */     //   186	24	7	i	int
/*     */     //   264	17	8	localObject2	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   14	26	59	finally
/*     */     //   90	248	264	finally
/*     */     //   264	266	264	finally
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 409 */     StringBuilder sb = new StringBuilder("ManagedBean[");
/* 410 */     sb.append("name=");
/* 411 */     sb.append(this.name);
/* 412 */     sb.append(", className=");
/* 413 */     sb.append(this.className);
/* 414 */     sb.append(", description=");
/* 415 */     sb.append(this.description);
/* 416 */     if (this.group != null) {
/* 417 */       sb.append(", group=");
/* 418 */       sb.append(this.group);
/*     */     }
/* 420 */     sb.append(", type=");
/* 421 */     sb.append(this.type);
/* 422 */     sb.append("]");
/* 423 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   Method getGetter(String aname, BaseModelMBean mbean, Object resource)
/*     */     throws AttributeNotFoundException, ReflectionException
/*     */   {
/* 430 */     Method m = null;
/*     */     
/* 432 */     AttributeInfo attrInfo = (AttributeInfo)this.attributes.get(aname);
/*     */     
/* 434 */     if (attrInfo == null) {
/* 435 */       throw new AttributeNotFoundException(" Cannot find attribute " + aname + " for " + resource);
/*     */     }
/* 437 */     String getMethod = attrInfo.getGetMethod();
/* 438 */     if (getMethod == null) {
/* 439 */       throw new AttributeNotFoundException("Cannot find attribute " + aname + " get method name");
/*     */     }
/* 441 */     Object object = null;
/* 442 */     NoSuchMethodException exception = null;
/*     */     try {
/* 444 */       object = mbean;
/* 445 */       m = object.getClass().getMethod(getMethod, NO_ARGS_PARAM_SIG);
/*     */     } catch (NoSuchMethodException e) {
/* 447 */       exception = e;
/*     */     }
/* 449 */     if ((m == null) && (resource != null)) {
/*     */       try {
/* 451 */         object = resource;
/* 452 */         m = object.getClass().getMethod(getMethod, NO_ARGS_PARAM_SIG);
/* 453 */         exception = null;
/*     */       } catch (NoSuchMethodException e) {
/* 455 */         exception = e;
/*     */       }
/*     */     }
/* 458 */     if (exception != null) {
/* 459 */       throw new ReflectionException(exception, "Cannot find getter method " + getMethod);
/*     */     }
/*     */     
/* 462 */     return m;
/*     */   }
/*     */   
/*     */   public Method getSetter(String aname, BaseModelMBean bean, Object resource)
/*     */     throws AttributeNotFoundException, ReflectionException
/*     */   {
/* 468 */     Method m = null;
/*     */     
/* 470 */     AttributeInfo attrInfo = (AttributeInfo)this.attributes.get(aname);
/* 471 */     if (attrInfo == null) {
/* 472 */       throw new AttributeNotFoundException(" Cannot find attribute " + aname);
/*     */     }
/*     */     
/* 475 */     String setMethod = attrInfo.getSetMethod();
/* 476 */     if (setMethod == null) {
/* 477 */       throw new AttributeNotFoundException("Cannot find attribute " + aname + " set method name");
/*     */     }
/* 479 */     String argType = attrInfo.getType();
/*     */     
/*     */ 
/* 482 */     Class<?>[] signature = {BaseModelMBean.getAttributeClass(argType) };
/*     */     
/* 484 */     Object object = null;
/* 485 */     NoSuchMethodException exception = null;
/*     */     try {
/* 487 */       object = bean;
/* 488 */       m = object.getClass().getMethod(setMethod, signature);
/*     */     } catch (NoSuchMethodException e) {
/* 490 */       exception = e;
/*     */     }
/* 492 */     if ((m == null) && (resource != null)) {
/*     */       try {
/* 494 */         object = resource;
/* 495 */         m = object.getClass().getMethod(setMethod, signature);
/* 496 */         exception = null;
/*     */       } catch (NoSuchMethodException e) {
/* 498 */         exception = e;
/*     */       }
/*     */     }
/* 501 */     if (exception != null) {
/* 502 */       throw new ReflectionException(exception, "Cannot find setter method " + setMethod + " " + resource);
/*     */     }
/*     */     
/*     */ 
/* 506 */     return m;
/*     */   }
/*     */   
/*     */   public Method getInvoke(String aname, Object[] params, String[] signature, BaseModelMBean bean, Object resource)
/*     */     throws MBeanException, ReflectionException
/*     */   {
/* 512 */     Method method = null;
/*     */     
/* 514 */     if (params == null)
/* 515 */       params = new Object[0];
/* 516 */     if (signature == null)
/* 517 */       signature = new String[0];
/* 518 */     if (params.length != signature.length) {
/* 519 */       throw new RuntimeOperationsException(new IllegalArgumentException("Inconsistent arguments and signature"), "Inconsistent arguments and signature");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 527 */     OperationInfo opInfo = (OperationInfo)this.operations.get(createOperationKey(aname, signature));
/* 528 */     if (opInfo == null) {
/* 529 */       throw new MBeanException(new ServiceNotFoundException("Cannot find operation " + aname), "Cannot find operation " + aname);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 535 */     Class<?>[] types = new Class[signature.length];
/* 536 */     for (int i = 0; i < signature.length; i++) {
/* 537 */       types[i] = BaseModelMBean.getAttributeClass(signature[i]);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 543 */     Object object = null;
/* 544 */     Exception exception = null;
/*     */     try {
/* 546 */       object = bean;
/* 547 */       method = object.getClass().getMethod(aname, types);
/*     */     } catch (NoSuchMethodException e) {
/* 549 */       exception = e;
/*     */     }
/*     */     try {
/* 552 */       if ((method == null) && (resource != null)) {
/* 553 */         object = resource;
/* 554 */         method = object.getClass().getMethod(aname, types);
/*     */       }
/*     */     } catch (NoSuchMethodException e) {
/* 557 */       exception = e;
/*     */     }
/* 559 */     if (method == null) {
/* 560 */       throw new ReflectionException(exception, "Cannot find method " + aname + " with this signature");
/*     */     }
/*     */     
/*     */ 
/* 564 */     return method;
/*     */   }
/*     */   
/*     */   private String createOperationKey(OperationInfo operation)
/*     */   {
/* 569 */     StringBuilder key = new StringBuilder(operation.getName());
/* 570 */     key.append('(');
/* 571 */     StringUtils.join(operation.getSignature(), ',', new StringUtils.Function() {
/* 572 */       public String apply(ParameterInfo t) { return t.getType(); } }, key);
/* 573 */     key.append(')');
/* 574 */     return key.toString();
/*     */   }
/*     */   
/*     */   private String createOperationKey(String methodName, String[] parameterTypes)
/*     */   {
/* 579 */     StringBuilder key = new StringBuilder(methodName);
/* 580 */     key.append('(');
/* 581 */     StringUtils.join(parameterTypes, ',', key);
/* 582 */     key.append(')');
/*     */     
/* 584 */     return key.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\modeler\ManagedBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */