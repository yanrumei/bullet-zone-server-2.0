/*     */ package org.apache.catalina.core;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.management.ObjectName;
/*     */ import javax.naming.NamingException;
/*     */ import javax.servlet.Filter;
/*     */ import javax.servlet.FilterConfig;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.InstanceManager;
/*     */ import org.apache.tomcat.util.descriptor.web.FilterDef;
/*     */ import org.apache.tomcat.util.modeler.Registry;
/*     */ import org.apache.tomcat.util.modeler.Util;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ApplicationFilterConfig
/*     */   implements FilterConfig, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  63 */   static final StringManager sm = StringManager.getManager("org.apache.catalina.core");
/*     */   
/*  65 */   private static final Log log = LogFactory.getLog(ApplicationFilterConfig.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  70 */   private static final List<String> emptyString = Collections.emptyList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final transient Context context;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   ApplicationFilterConfig(Context context, FilterDef filterDef)
/*     */     throws ClassCastException, ClassNotFoundException, IllegalAccessException, InstantiationException, ServletException, InvocationTargetException, NamingException, IllegalArgumentException, NoSuchMethodException, SecurityException
/*     */   {
/* 104 */     this.context = context;
/* 105 */     this.filterDef = filterDef;
/*     */     
/* 107 */     if (filterDef.getFilter() == null) {
/* 108 */       getFilter();
/*     */     } else {
/* 110 */       this.filter = filterDef.getFilter();
/* 111 */       getInstanceManager().newInstance(this.filter);
/* 112 */       initFilter();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 129 */   private transient Filter filter = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final FilterDef filterDef;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private transient InstanceManager instanceManager;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ObjectName oname;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFilterName()
/*     */   {
/* 155 */     return this.filterDef.getFilterName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getFilterClass()
/*     */   {
/* 162 */     return this.filterDef.getFilterClass();
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
/*     */   public String getInitParameter(String name)
/*     */   {
/* 175 */     Map<String, String> map = this.filterDef.getParameterMap();
/* 176 */     if (map == null) {
/* 177 */       return null;
/*     */     }
/*     */     
/* 180 */     return (String)map.get(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Enumeration<String> getInitParameterNames()
/*     */   {
/* 191 */     Map<String, String> map = this.filterDef.getParameterMap();
/*     */     
/* 193 */     if (map == null) {
/* 194 */       return Collections.enumeration(emptyString);
/*     */     }
/*     */     
/* 197 */     return Collections.enumeration(map.keySet());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServletContext getServletContext()
/*     */   {
/* 207 */     return this.context.getServletContext();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 218 */     StringBuilder sb = new StringBuilder("ApplicationFilterConfig[");
/* 219 */     sb.append("name=");
/* 220 */     sb.append(this.filterDef.getFilterName());
/* 221 */     sb.append(", filterClass=");
/* 222 */     sb.append(this.filterDef.getFilterClass());
/* 223 */     sb.append("]");
/* 224 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Map<String, String> getFilterInitParameterMap()
/*     */   {
/* 231 */     return Collections.unmodifiableMap(this.filterDef.getParameterMap());
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
/*     */ 
/*     */ 
/*     */ 
/*     */   Filter getFilter()
/*     */     throws ClassCastException, ClassNotFoundException, IllegalAccessException, InstantiationException, ServletException, InvocationTargetException, NamingException, IllegalArgumentException, NoSuchMethodException, SecurityException
/*     */   {
/* 259 */     if (this.filter != null) {
/* 260 */       return this.filter;
/*     */     }
/*     */     
/* 263 */     String filterClass = this.filterDef.getFilterClass();
/* 264 */     this.filter = ((Filter)getInstanceManager().newInstance(filterClass));
/*     */     
/* 266 */     initFilter();
/*     */     
/* 268 */     return this.filter;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   private void initFilter()
/*     */     throws ServletException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 3	org/apache/catalina/core/ApplicationFilterConfig:context	Lorg/apache/catalina/Context;
/*     */     //   4: instanceof 30
/*     */     //   7: ifeq +87 -> 94
/*     */     //   10: aload_0
/*     */     //   11: getfield 3	org/apache/catalina/core/ApplicationFilterConfig:context	Lorg/apache/catalina/Context;
/*     */     //   14: invokeinterface 31 1 0
/*     */     //   19: ifeq +75 -> 94
/*     */     //   22: invokestatic 32	org/apache/tomcat/util/log/SystemLogHandler:startCapture	()V
/*     */     //   25: aload_0
/*     */     //   26: getfield 2	org/apache/catalina/core/ApplicationFilterConfig:filter	Ljavax/servlet/Filter;
/*     */     //   29: aload_0
/*     */     //   30: invokeinterface 33 2 0
/*     */     //   35: invokestatic 34	org/apache/tomcat/util/log/SystemLogHandler:stopCapture	()Ljava/lang/String;
/*     */     //   38: astore_1
/*     */     //   39: aload_1
/*     */     //   40: ifnull +20 -> 60
/*     */     //   43: aload_1
/*     */     //   44: invokevirtual 35	java/lang/String:length	()I
/*     */     //   47: ifle +13 -> 60
/*     */     //   50: aload_0
/*     */     //   51: invokevirtual 36	org/apache/catalina/core/ApplicationFilterConfig:getServletContext	()Ljavax/servlet/ServletContext;
/*     */     //   54: aload_1
/*     */     //   55: invokeinterface 37 2 0
/*     */     //   60: goto +31 -> 91
/*     */     //   63: astore_2
/*     */     //   64: invokestatic 34	org/apache/tomcat/util/log/SystemLogHandler:stopCapture	()Ljava/lang/String;
/*     */     //   67: astore_3
/*     */     //   68: aload_3
/*     */     //   69: ifnull +20 -> 89
/*     */     //   72: aload_3
/*     */     //   73: invokevirtual 35	java/lang/String:length	()I
/*     */     //   76: ifle +13 -> 89
/*     */     //   79: aload_0
/*     */     //   80: invokevirtual 36	org/apache/catalina/core/ApplicationFilterConfig:getServletContext	()Ljavax/servlet/ServletContext;
/*     */     //   83: aload_3
/*     */     //   84: invokeinterface 37 2 0
/*     */     //   89: aload_2
/*     */     //   90: athrow
/*     */     //   91: goto +13 -> 104
/*     */     //   94: aload_0
/*     */     //   95: getfield 2	org/apache/catalina/core/ApplicationFilterConfig:filter	Ljavax/servlet/Filter;
/*     */     //   98: aload_0
/*     */     //   99: invokeinterface 33 2 0
/*     */     //   104: aload_0
/*     */     //   105: invokespecial 38	org/apache/catalina/core/ApplicationFilterConfig:registerJMX	()V
/*     */     //   108: return
/*     */     // Line number table:
/*     */     //   Java source line #273	-> byte code offset #0
/*     */     //   Java source line #274	-> byte code offset #14
/*     */     //   Java source line #276	-> byte code offset #22
/*     */     //   Java source line #277	-> byte code offset #25
/*     */     //   Java source line #279	-> byte code offset #35
/*     */     //   Java source line #280	-> byte code offset #39
/*     */     //   Java source line #281	-> byte code offset #50
/*     */     //   Java source line #283	-> byte code offset #60
/*     */     //   Java source line #279	-> byte code offset #63
/*     */     //   Java source line #280	-> byte code offset #68
/*     */     //   Java source line #281	-> byte code offset #79
/*     */     //   Java source line #283	-> byte code offset #89
/*     */     //   Java source line #285	-> byte code offset #94
/*     */     //   Java source line #289	-> byte code offset #104
/*     */     //   Java source line #290	-> byte code offset #108
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	109	0	this	ApplicationFilterConfig
/*     */     //   38	17	1	capturedlog	String
/*     */     //   63	27	2	localObject	Object
/*     */     //   67	17	3	capturedlog	String
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   22	35	63	finally
/*     */   }
/*     */   
/*     */   FilterDef getFilterDef()
/*     */   {
/* 297 */     return this.filterDef;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   void release()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokespecial 39	org/apache/catalina/core/ApplicationFilterConfig:unregisterJMX	()V
/*     */     //   4: aload_0
/*     */     //   5: getfield 2	org/apache/catalina/core/ApplicationFilterConfig:filter	Ljavax/servlet/Filter;
/*     */     //   8: ifnull +196 -> 204
/*     */     //   11: getstatic 40	org/apache/catalina/Globals:IS_SECURITY_ENABLED	Z
/*     */     //   14: ifeq +35 -> 49
/*     */     //   17: ldc 41
/*     */     //   19: aload_0
/*     */     //   20: getfield 2	org/apache/catalina/core/ApplicationFilterConfig:filter	Ljavax/servlet/Filter;
/*     */     //   23: invokestatic 42	org/apache/catalina/security/SecurityUtil:doAsPrivilege	(Ljava/lang/String;Ljavax/servlet/Filter;)V
/*     */     //   26: aload_0
/*     */     //   27: getfield 2	org/apache/catalina/core/ApplicationFilterConfig:filter	Ljavax/servlet/Filter;
/*     */     //   30: invokestatic 43	org/apache/catalina/security/SecurityUtil:remove	(Ljava/lang/Object;)V
/*     */     //   33: goto +13 -> 46
/*     */     //   36: astore_1
/*     */     //   37: aload_0
/*     */     //   38: getfield 2	org/apache/catalina/core/ApplicationFilterConfig:filter	Ljavax/servlet/Filter;
/*     */     //   41: invokestatic 43	org/apache/catalina/security/SecurityUtil:remove	(Ljava/lang/Object;)V
/*     */     //   44: aload_1
/*     */     //   45: athrow
/*     */     //   46: goto +12 -> 58
/*     */     //   49: aload_0
/*     */     //   50: getfield 2	org/apache/catalina/core/ApplicationFilterConfig:filter	Ljavax/servlet/Filter;
/*     */     //   53: invokeinterface 44 1 0
/*     */     //   58: goto +55 -> 113
/*     */     //   61: astore_1
/*     */     //   62: aload_1
/*     */     //   63: invokestatic 46	org/apache/tomcat/util/ExceptionUtils:handleThrowable	(Ljava/lang/Throwable;)V
/*     */     //   66: aload_0
/*     */     //   67: getfield 3	org/apache/catalina/core/ApplicationFilterConfig:context	Lorg/apache/catalina/Context;
/*     */     //   70: invokeinterface 47 1 0
/*     */     //   75: getstatic 48	org/apache/catalina/core/ApplicationFilterConfig:sm	Lorg/apache/tomcat/util/res/StringManager;
/*     */     //   78: ldc 49
/*     */     //   80: iconst_2
/*     */     //   81: anewarray 50	java/lang/Object
/*     */     //   84: dup
/*     */     //   85: iconst_0
/*     */     //   86: aload_0
/*     */     //   87: getfield 4	org/apache/catalina/core/ApplicationFilterConfig:filterDef	Lorg/apache/tomcat/util/descriptor/web/FilterDef;
/*     */     //   90: invokevirtual 10	org/apache/tomcat/util/descriptor/web/FilterDef:getFilterName	()Ljava/lang/String;
/*     */     //   93: aastore
/*     */     //   94: dup
/*     */     //   95: iconst_1
/*     */     //   96: aload_0
/*     */     //   97: getfield 4	org/apache/catalina/core/ApplicationFilterConfig:filterDef	Lorg/apache/tomcat/util/descriptor/web/FilterDef;
/*     */     //   100: invokevirtual 11	org/apache/tomcat/util/descriptor/web/FilterDef:getFilterClass	()Ljava/lang/String;
/*     */     //   103: aastore
/*     */     //   104: invokevirtual 51	org/apache/tomcat/util/res/StringManager:getString	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   107: aload_1
/*     */     //   108: invokeinterface 52 3 0
/*     */     //   113: aload_0
/*     */     //   114: getfield 3	org/apache/catalina/core/ApplicationFilterConfig:context	Lorg/apache/catalina/Context;
/*     */     //   117: invokeinterface 53 1 0
/*     */     //   122: ifne +82 -> 204
/*     */     //   125: aload_0
/*     */     //   126: getfield 3	org/apache/catalina/core/ApplicationFilterConfig:context	Lorg/apache/catalina/Context;
/*     */     //   129: checkcast 30	org/apache/catalina/core/StandardContext
/*     */     //   132: invokevirtual 54	org/apache/catalina/core/StandardContext:getInstanceManager	()Lorg/apache/tomcat/InstanceManager;
/*     */     //   135: aload_0
/*     */     //   136: getfield 2	org/apache/catalina/core/ApplicationFilterConfig:filter	Ljavax/servlet/Filter;
/*     */     //   139: invokeinterface 55 2 0
/*     */     //   144: goto +60 -> 204
/*     */     //   147: astore_1
/*     */     //   148: aload_1
/*     */     //   149: invokestatic 57	org/apache/tomcat/util/ExceptionUtils:unwrapInvocationTargetException	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
/*     */     //   152: astore_2
/*     */     //   153: aload_2
/*     */     //   154: invokestatic 46	org/apache/tomcat/util/ExceptionUtils:handleThrowable	(Ljava/lang/Throwable;)V
/*     */     //   157: aload_0
/*     */     //   158: getfield 3	org/apache/catalina/core/ApplicationFilterConfig:context	Lorg/apache/catalina/Context;
/*     */     //   161: invokeinterface 47 1 0
/*     */     //   166: getstatic 48	org/apache/catalina/core/ApplicationFilterConfig:sm	Lorg/apache/tomcat/util/res/StringManager;
/*     */     //   169: ldc 58
/*     */     //   171: iconst_2
/*     */     //   172: anewarray 50	java/lang/Object
/*     */     //   175: dup
/*     */     //   176: iconst_0
/*     */     //   177: aload_0
/*     */     //   178: getfield 4	org/apache/catalina/core/ApplicationFilterConfig:filterDef	Lorg/apache/tomcat/util/descriptor/web/FilterDef;
/*     */     //   181: invokevirtual 10	org/apache/tomcat/util/descriptor/web/FilterDef:getFilterName	()Ljava/lang/String;
/*     */     //   184: aastore
/*     */     //   185: dup
/*     */     //   186: iconst_1
/*     */     //   187: aload_0
/*     */     //   188: getfield 4	org/apache/catalina/core/ApplicationFilterConfig:filterDef	Lorg/apache/tomcat/util/descriptor/web/FilterDef;
/*     */     //   191: invokevirtual 11	org/apache/tomcat/util/descriptor/web/FilterDef:getFilterClass	()Ljava/lang/String;
/*     */     //   194: aastore
/*     */     //   195: invokevirtual 51	org/apache/tomcat/util/res/StringManager:getString	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   198: aload_2
/*     */     //   199: invokeinterface 52 3 0
/*     */     //   204: aload_0
/*     */     //   205: aconst_null
/*     */     //   206: putfield 2	org/apache/catalina/core/ApplicationFilterConfig:filter	Ljavax/servlet/Filter;
/*     */     //   209: return
/*     */     // Line number table:
/*     */     //   Java source line #307	-> byte code offset #0
/*     */     //   Java source line #309	-> byte code offset #4
/*     */     //   Java source line #311	-> byte code offset #11
/*     */     //   Java source line #313	-> byte code offset #17
/*     */     //   Java source line #315	-> byte code offset #26
/*     */     //   Java source line #316	-> byte code offset #33
/*     */     //   Java source line #315	-> byte code offset #36
/*     */     //   Java source line #318	-> byte code offset #49
/*     */     //   Java source line #326	-> byte code offset #58
/*     */     //   Java source line #320	-> byte code offset #61
/*     */     //   Java source line #321	-> byte code offset #62
/*     */     //   Java source line #322	-> byte code offset #66
/*     */     //   Java source line #324	-> byte code offset #90
/*     */     //   Java source line #325	-> byte code offset #100
/*     */     //   Java source line #322	-> byte code offset #104
/*     */     //   Java source line #327	-> byte code offset #113
/*     */     //   Java source line #329	-> byte code offset #125
/*     */     //   Java source line #337	-> byte code offset #144
/*     */     //   Java source line #330	-> byte code offset #147
/*     */     //   Java source line #331	-> byte code offset #148
/*     */     //   Java source line #332	-> byte code offset #149
/*     */     //   Java source line #333	-> byte code offset #153
/*     */     //   Java source line #334	-> byte code offset #157
/*     */     //   Java source line #336	-> byte code offset #181
/*     */     //   Java source line #335	-> byte code offset #195
/*     */     //   Java source line #334	-> byte code offset #199
/*     */     //   Java source line #340	-> byte code offset #204
/*     */     //   Java source line #342	-> byte code offset #209
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	210	0	this	ApplicationFilterConfig
/*     */     //   36	9	1	localObject	Object
/*     */     //   61	47	1	t	Throwable
/*     */     //   147	2	1	e	Exception
/*     */     //   152	47	2	t	Throwable
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   17	26	36	finally
/*     */     //   11	58	61	java/lang/Throwable
/*     */     //   125	144	147	java/lang/Exception
/*     */   }
/*     */   
/*     */   private InstanceManager getInstanceManager()
/*     */   {
/* 348 */     if (this.instanceManager == null) {
/* 349 */       if ((this.context instanceof StandardContext)) {
/* 350 */         this.instanceManager = ((StandardContext)this.context).getInstanceManager();
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 355 */         this.instanceManager = new DefaultInstanceManager(null, new HashMap(), this.context, getClass().getClassLoader());
/*     */       }
/*     */     }
/* 358 */     return this.instanceManager;
/*     */   }
/*     */   
/*     */   private void registerJMX() {
/* 362 */     String parentName = this.context.getName();
/* 363 */     if (!parentName.startsWith("/")) {
/* 364 */       parentName = "/" + parentName;
/*     */     }
/*     */     
/* 367 */     String hostName = this.context.getParent().getName();
/* 368 */     hostName = hostName == null ? "DEFAULT" : hostName;
/*     */     
/*     */ 
/* 371 */     String domain = this.context.getParent().getParent().getName();
/*     */     
/* 373 */     String webMod = "//" + hostName + parentName;
/* 374 */     String onameStr = null;
/* 375 */     String filterName = this.filterDef.getFilterName();
/* 376 */     if (Util.objectNameValueNeedsQuote(filterName)) {
/* 377 */       filterName = ObjectName.quote(filterName);
/*     */     }
/* 379 */     if ((this.context instanceof StandardContext)) {
/* 380 */       StandardContext standardContext = (StandardContext)this.context;
/*     */       
/*     */ 
/*     */ 
/* 384 */       onameStr = domain + ":j2eeType=Filter,WebModule=" + webMod + ",name=" + filterName + ",J2EEApplication=" + standardContext.getJ2EEApplication() + ",J2EEServer=" + standardContext.getJ2EEServer();
/*     */     } else {
/* 386 */       onameStr = domain + ":j2eeType=Filter,name=" + filterName + ",WebModule=" + webMod;
/*     */     }
/*     */     try
/*     */     {
/* 390 */       this.oname = new ObjectName(onameStr);
/* 391 */       Registry.getRegistry(null, null).registerComponent(this, this.oname, null);
/*     */     }
/*     */     catch (Exception ex) {
/* 394 */       log.info(sm.getString("applicationFilterConfig.jmxRegisterFail", new Object[] {
/* 395 */         getFilterClass(), getFilterName() }), ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private void unregisterJMX()
/*     */   {
/* 401 */     if (this.oname != null) {
/*     */       try {
/* 403 */         Registry.getRegistry(null, null).unregisterComponent(this.oname);
/* 404 */         if (log.isDebugEnabled())
/* 405 */           log.debug(sm.getString("applicationFilterConfig.jmxUnregister", new Object[] {
/*     */           
/* 407 */             getFilterClass(), getFilterName() }));
/*     */       } catch (Exception ex) {
/* 409 */         log.error(sm.getString("applicationFilterConfig.jmxUnregisterFail", new Object[] {
/*     */         
/* 411 */           getFilterClass(), getFilterName() }), ex);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\ApplicationFilterConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */