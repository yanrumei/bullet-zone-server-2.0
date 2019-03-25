/*     */ package org.springframework.jmx.export;
/*     */ 
/*     */ import javax.management.AttributeList;
/*     */ import javax.management.AttributeNotFoundException;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.ReflectionException;
/*     */ import javax.management.RuntimeOperationsException;
/*     */ import javax.management.modelmbean.InvalidTargetObjectTypeException;
/*     */ import javax.management.modelmbean.ModelMBeanInfo;
/*     */ import javax.management.modelmbean.RequiredModelMBean;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SpringModelMBean
/*     */   extends RequiredModelMBean
/*     */ {
/*  46 */   private ClassLoader managedResourceClassLoader = Thread.currentThread().getContextClassLoader();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringModelMBean()
/*     */     throws MBeanException, RuntimeOperationsException
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringModelMBean(ModelMBeanInfo mbi)
/*     */     throws MBeanException, RuntimeOperationsException
/*     */   {
/*  62 */     super(mbi);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setManagedResource(Object managedResource, String managedResourceType)
/*     */     throws MBeanException, InstanceNotFoundException, InvalidTargetObjectTypeException
/*     */   {
/*  73 */     this.managedResourceClassLoader = managedResource.getClass().getClassLoader();
/*  74 */     super.setManagedResource(managedResource, managedResourceType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object invoke(String opName, Object[] opArgs, String[] sig)
/*     */     throws MBeanException, ReflectionException
/*     */   {
/*  87 */     ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
/*     */     try {
/*  89 */       Thread.currentThread().setContextClassLoader(this.managedResourceClassLoader);
/*  90 */       return super.invoke(opName, opArgs, sig);
/*     */     }
/*     */     finally {
/*  93 */       Thread.currentThread().setContextClassLoader(currentClassLoader);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getAttribute(String attrName)
/*     */     throws AttributeNotFoundException, MBeanException, ReflectionException
/*     */   {
/* 106 */     ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
/*     */     try {
/* 108 */       Thread.currentThread().setContextClassLoader(this.managedResourceClassLoader);
/* 109 */       return super.getAttribute(attrName);
/*     */     }
/*     */     finally {
/* 112 */       Thread.currentThread().setContextClassLoader(currentClassLoader);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AttributeList getAttributes(String[] attrNames)
/*     */   {
/* 123 */     ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
/*     */     try {
/* 125 */       Thread.currentThread().setContextClassLoader(this.managedResourceClassLoader);
/* 126 */       return super.getAttributes(attrNames);
/*     */     }
/*     */     finally {
/* 129 */       Thread.currentThread().setContextClassLoader(currentClassLoader);
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void setAttribute(javax.management.Attribute attribute)
/*     */     throws AttributeNotFoundException, javax.management.InvalidAttributeValueException, MBeanException, ReflectionException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: invokestatic 2	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   3: invokevirtual 3	java/lang/Thread:getContextClassLoader	()Ljava/lang/ClassLoader;
/*     */     //   6: astore_2
/*     */     //   7: invokestatic 2	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   10: aload_0
/*     */     //   11: getfield 4	org/springframework/jmx/export/SpringModelMBean:managedResourceClassLoader	Ljava/lang/ClassLoader;
/*     */     //   14: invokevirtual 9	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/*     */     //   17: aload_0
/*     */     //   18: aload_1
/*     */     //   19: invokespecial 13	javax/management/modelmbean/RequiredModelMBean:setAttribute	(Ljavax/management/Attribute;)V
/*     */     //   22: invokestatic 2	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   25: aload_2
/*     */     //   26: invokevirtual 9	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/*     */     //   29: goto +13 -> 42
/*     */     //   32: astore_3
/*     */     //   33: invokestatic 2	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   36: aload_2
/*     */     //   37: invokevirtual 9	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/*     */     //   40: aload_3
/*     */     //   41: athrow
/*     */     //   42: return
/*     */     // Line number table:
/*     */     //   Java source line #142	-> byte code offset #0
/*     */     //   Java source line #144	-> byte code offset #7
/*     */     //   Java source line #145	-> byte code offset #17
/*     */     //   Java source line #148	-> byte code offset #22
/*     */     //   Java source line #149	-> byte code offset #29
/*     */     //   Java source line #148	-> byte code offset #32
/*     */     //   Java source line #150	-> byte code offset #42
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	43	0	this	SpringModelMBean
/*     */     //   0	43	1	attribute	javax.management.Attribute
/*     */     //   6	31	2	currentClassLoader	ClassLoader
/*     */     //   32	9	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	22	32	finally
/*     */   }
/*     */   
/*     */   public AttributeList setAttributes(AttributeList attributes)
/*     */   {
/* 159 */     ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
/*     */     try {
/* 161 */       Thread.currentThread().setContextClassLoader(this.managedResourceClassLoader);
/* 162 */       return super.setAttributes(attributes);
/*     */     }
/*     */     finally {
/* 165 */       Thread.currentThread().setContextClassLoader(currentClassLoader);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jmx\export\SpringModelMBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */