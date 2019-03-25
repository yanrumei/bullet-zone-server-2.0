/*     */ package org.apache.naming.factory;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.RefAddr;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.spi.ObjectFactory;
/*     */ import org.apache.naming.ResourceLinkRef;
/*     */ import org.apache.naming.StringManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceLinkFactory
/*     */   implements ObjectFactory
/*     */ {
/*  43 */   private static final StringManager sm = StringManager.getManager(ResourceLinkFactory.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  48 */   private static Context globalContext = null;
/*     */   
/*  50 */   private static Map<ClassLoader, Map<String, String>> globalResourceRegistrations = new ConcurrentHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setGlobalContext(Context newGlobalContext)
/*     */   {
/*  61 */     SecurityManager sm = System.getSecurityManager();
/*  62 */     if (sm != null) {
/*  63 */       sm.checkPermission(new RuntimePermission(ResourceLinkFactory.class
/*  64 */         .getName() + ".setGlobalContext"));
/*     */     }
/*  66 */     globalContext = newGlobalContext;
/*     */   }
/*     */   
/*     */ 
/*     */   public static void registerGlobalResourceAccess(Context globalContext, String localName, String globalName)
/*     */   {
/*  72 */     validateGlobalContext(globalContext);
/*  73 */     ClassLoader cl = Thread.currentThread().getContextClassLoader();
/*  74 */     Map<String, String> registrations = (Map)globalResourceRegistrations.get(cl);
/*  75 */     if (registrations == null)
/*     */     {
/*     */ 
/*  78 */       registrations = new HashMap();
/*  79 */       globalResourceRegistrations.put(cl, registrations);
/*     */     }
/*  81 */     registrations.put(localName, globalName);
/*     */   }
/*     */   
/*     */   public static void deregisterGlobalResourceAccess(Context globalContext, String localName)
/*     */   {
/*  86 */     validateGlobalContext(globalContext);
/*  87 */     ClassLoader cl = Thread.currentThread().getContextClassLoader();
/*  88 */     Map<String, String> registrations = (Map)globalResourceRegistrations.get(cl);
/*  89 */     if (registrations != null) {
/*  90 */       registrations.remove(localName);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void deregisterGlobalResourceAccess(Context globalContext)
/*     */   {
/*  96 */     validateGlobalContext(globalContext);
/*  97 */     ClassLoader cl = Thread.currentThread().getContextClassLoader();
/*  98 */     globalResourceRegistrations.remove(cl);
/*     */   }
/*     */   
/*     */   private static void validateGlobalContext(Context globalContext)
/*     */   {
/* 103 */     if ((globalContext != null) && (globalContext != globalContext))
/*     */     {
/* 105 */       throw new SecurityException("Caller provided invalid global context");
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean validateGlobalResourceAccess(String globalName)
/*     */   {
/* 111 */     ClassLoader cl = Thread.currentThread().getContextClassLoader();
/* 112 */     while (cl != null) {
/* 113 */       Map<String, String> registrations = (Map)globalResourceRegistrations.get(cl);
/* 114 */       if ((registrations != null) && (registrations.containsValue(globalName))) {
/* 115 */         return true;
/*     */       }
/* 117 */       cl = cl.getParent();
/*     */     }
/* 119 */     return false;
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
/*     */   public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment)
/*     */     throws NamingException
/*     */   {
/* 134 */     if (!(obj instanceof ResourceLinkRef)) {
/* 135 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 139 */     Reference ref = (Reference)obj;
/*     */     
/*     */ 
/* 142 */     String globalName = null;
/* 143 */     RefAddr refAddr = ref.get("globalName");
/* 144 */     if (refAddr != null) {
/* 145 */       globalName = refAddr.getContent().toString();
/*     */       
/*     */ 
/* 148 */       if (!validateGlobalResourceAccess(globalName)) {
/* 149 */         return null;
/*     */       }
/* 151 */       Object result = null;
/* 152 */       result = globalContext.lookup(globalName);
/*     */       
/* 154 */       String expectedClassName = ref.getClassName();
/* 155 */       if (expectedClassName == null)
/*     */       {
/* 157 */         throw new IllegalArgumentException(sm.getString("resourceLinkFactory.nullType", new Object[] { name, globalName }));
/*     */       }
/*     */       try {
/* 160 */         Class<?> expectedClazz = Class.forName(expectedClassName, true, 
/* 161 */           Thread.currentThread().getContextClassLoader());
/* 162 */         if (!expectedClazz.isAssignableFrom(result.getClass())) {
/* 163 */           throw new IllegalArgumentException(sm.getString("resourceLinkFactory.wrongType", new Object[] { name, globalName, expectedClassName, result
/* 164 */             .getClass().getName() }));
/*     */         }
/*     */       } catch (ClassNotFoundException e) {
/* 167 */         throw new IllegalArgumentException(sm.getString("resourceLinkFactory.unknownType", new Object[] { name, globalName, expectedClassName }), e);
/*     */       }
/*     */       
/* 170 */       return result;
/*     */     }
/*     */     
/* 173 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\factory\ResourceLinkFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */