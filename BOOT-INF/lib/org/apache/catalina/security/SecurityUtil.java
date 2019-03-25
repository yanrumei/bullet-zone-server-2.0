/*     */ package org.apache.catalina.security;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.Principal;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.security.auth.Subject;
/*     */ import javax.servlet.Filter;
/*     */ import javax.servlet.Servlet;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.UnavailableException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.apache.catalina.Globals;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
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
/*     */ 
/*     */ 
/*     */ public final class SecurityUtil
/*     */ {
/*     */   private static final int INIT = 0;
/*     */   private static final int SERVICE = 1;
/*     */   private static final int DOFILTER = 1;
/*     */   private static final int EVENT = 2;
/*     */   private static final int DOFILTEREVENT = 2;
/*     */   private static final int DESTROY = 3;
/*     */   private static final String INIT_METHOD = "init";
/*     */   private static final String DOFILTER_METHOD = "doFilter";
/*     */   private static final String SERVICE_METHOD = "service";
/*     */   private static final String EVENT_METHOD = "event";
/*     */   private static final String DOFILTEREVENT_METHOD = "doFilterEvent";
/*     */   private static final String DESTROY_METHOD = "destroy";
/*  74 */   private static final Map<Class<?>, Method[]> classCache = new ConcurrentHashMap();
/*     */   
/*  76 */   private static final Log log = LogFactory.getLog(SecurityUtil.class);
/*     */   
/*     */ 
/*  79 */   private static final boolean packageDefinitionEnabled = (System.getProperty("package.definition") != null) || 
/*  80 */     (System.getProperty("package.access") != null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  86 */   private static final StringManager sm = StringManager.getManager("org.apache.catalina.security");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void doAsPrivilege(String methodName, Servlet targetObject)
/*     */     throws Exception
/*     */   {
/* 100 */     doAsPrivilege(methodName, targetObject, null, null, null);
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
/*     */   public static void doAsPrivilege(String methodName, Servlet targetObject, Class<?>[] targetType, Object[] targetArguments)
/*     */     throws Exception
/*     */   {
/* 123 */     doAsPrivilege(methodName, targetObject, targetType, targetArguments, null);
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
/*     */ 
/*     */ 
/*     */   public static void doAsPrivilege(String methodName, Servlet targetObject, Class<?>[] targetParameterTypes, Object[] targetArguments, Principal principal)
/*     */     throws Exception
/*     */   {
/* 153 */     Method method = null;
/* 154 */     Method[] methodsCache = (Method[])classCache.get(Servlet.class);
/* 155 */     if (methodsCache == null) {
/* 156 */       method = createMethodAndCacheIt(null, Servlet.class, methodName, targetParameterTypes);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 161 */       method = findMethod(methodsCache, methodName);
/* 162 */       if (method == null) {
/* 163 */         method = createMethodAndCacheIt(methodsCache, Servlet.class, methodName, targetParameterTypes);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 170 */     execute(method, targetObject, targetArguments, principal);
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
/*     */   public static void doAsPrivilege(String methodName, Filter targetObject)
/*     */     throws Exception
/*     */   {
/* 187 */     doAsPrivilege(methodName, targetObject, null, null);
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
/*     */   public static void doAsPrivilege(String methodName, Filter targetObject, Class<?>[] targetType, Object[] targetArguments)
/*     */     throws Exception
/*     */   {
/* 210 */     doAsPrivilege(methodName, targetObject, targetType, targetArguments, null);
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
/*     */   public static void doAsPrivilege(String methodName, Filter targetObject, Class<?>[] targetParameterTypes, Object[] targetParameterValues, Principal principal)
/*     */     throws Exception
/*     */   {
/* 236 */     Method method = null;
/* 237 */     Method[] methodsCache = (Method[])classCache.get(Filter.class);
/* 238 */     if (methodsCache == null) {
/* 239 */       method = createMethodAndCacheIt(null, Filter.class, methodName, targetParameterTypes);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 244 */       method = findMethod(methodsCache, methodName);
/* 245 */       if (method == null) {
/* 246 */         method = createMethodAndCacheIt(methodsCache, Filter.class, methodName, targetParameterTypes);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 253 */     execute(method, targetObject, targetParameterValues, principal);
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
/*     */   private static void execute(Method method, final Object targetObject, final Object[] targetArguments, Principal principal)
/*     */     throws Exception
/*     */   {
/*     */     try
/*     */     {
/* 277 */       Subject subject = null;
/* 278 */       PrivilegedExceptionAction<Void> pea = new PrivilegedExceptionAction()
/*     */       {
/*     */         public Void run() throws Exception
/*     */         {
/* 282 */           this.val$method.invoke(targetObject, targetArguments);
/* 283 */           return null;
/*     */         }
/*     */       };
/*     */       
/*     */ 
/* 288 */       if ((targetArguments != null) && ((targetArguments[0] instanceof HttpServletRequest)))
/*     */       {
/* 290 */         HttpServletRequest request = (HttpServletRequest)targetArguments[0];
/*     */         
/*     */ 
/* 293 */         boolean hasSubject = false;
/* 294 */         HttpSession session = request.getSession(false);
/* 295 */         if (session != null)
/*     */         {
/* 297 */           subject = (Subject)session.getAttribute("javax.security.auth.subject");
/* 298 */           hasSubject = subject != null;
/*     */         }
/*     */         
/* 301 */         if (subject == null) {
/* 302 */           subject = new Subject();
/*     */           
/* 304 */           if (principal != null) {
/* 305 */             subject.getPrincipals().add(principal);
/*     */           }
/*     */         }
/*     */         
/* 309 */         if ((session != null) && (!hasSubject)) {
/* 310 */           session.setAttribute("javax.security.auth.subject", subject);
/*     */         }
/*     */       }
/*     */       
/* 314 */       Subject.doAsPrivileged(subject, pea, null);
/*     */     } catch (PrivilegedActionException pe) {
/*     */       Throwable e;
/* 317 */       if ((pe.getException() instanceof InvocationTargetException)) {
/* 318 */         Throwable e = pe.getException().getCause();
/* 319 */         ExceptionUtils.handleThrowable(e);
/*     */       } else {
/* 321 */         e = pe;
/*     */       }
/*     */       
/* 324 */       if (log.isDebugEnabled()) {
/* 325 */         log.debug(sm.getString("SecurityUtil.doAsPrivilege"), e);
/*     */       }
/*     */       
/* 328 */       if ((e instanceof UnavailableException))
/* 329 */         throw ((UnavailableException)e);
/* 330 */       if ((e instanceof ServletException))
/* 331 */         throw ((ServletException)e);
/* 332 */       if ((e instanceof IOException))
/* 333 */         throw ((IOException)e);
/* 334 */       if ((e instanceof RuntimeException)) {
/* 335 */         throw ((RuntimeException)e);
/*     */       }
/* 337 */       throw new ServletException(e.getMessage(), e);
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
/*     */   private static Method findMethod(Method[] methodsCache, String methodName)
/*     */   {
/* 350 */     if (methodName.equals("init"))
/* 351 */       return methodsCache[0];
/* 352 */     if (methodName.equals("destroy"))
/* 353 */       return methodsCache[3];
/* 354 */     if (methodName.equals("service"))
/* 355 */       return methodsCache[1];
/* 356 */     if (methodName.equals("doFilter"))
/* 357 */       return methodsCache[1];
/* 358 */     if (methodName.equals("event"))
/* 359 */       return methodsCache[2];
/* 360 */     if (methodName.equals("doFilterEvent")) {
/* 361 */       return methodsCache[2];
/*     */     }
/* 363 */     return null;
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
/*     */   private static Method createMethodAndCacheIt(Method[] methodsCache, Class<?> targetType, String methodName, Class<?>[] parameterTypes)
/*     */     throws Exception
/*     */   {
/* 383 */     if (methodsCache == null) {
/* 384 */       methodsCache = new Method[4];
/*     */     }
/*     */     
/* 387 */     Method method = targetType.getMethod(methodName, parameterTypes);
/*     */     
/* 389 */     if (methodName.equals("init")) {
/* 390 */       methodsCache[0] = method;
/* 391 */     } else if (methodName.equals("destroy")) {
/* 392 */       methodsCache[3] = method;
/* 393 */     } else if (methodName.equals("service")) {
/* 394 */       methodsCache[1] = method;
/* 395 */     } else if (methodName.equals("doFilter")) {
/* 396 */       methodsCache[1] = method;
/* 397 */     } else if (methodName.equals("event")) {
/* 398 */       methodsCache[2] = method;
/* 399 */     } else if (methodName.equals("doFilterEvent")) {
/* 400 */       methodsCache[2] = method;
/*     */     }
/*     */     
/* 403 */     classCache.put(targetType, methodsCache);
/*     */     
/* 405 */     return method;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void remove(Object cachedObject)
/*     */   {
/* 415 */     classCache.remove(cachedObject);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isPackageProtectionEnabled()
/*     */   {
/* 425 */     if ((packageDefinitionEnabled) && (Globals.IS_SECURITY_ENABLED)) {
/* 426 */       return true;
/*     */     }
/* 428 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\security\SecurityUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */