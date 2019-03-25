/*     */ package org.apache.naming;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.NamingException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContextBindings
/*     */ {
/*  45 */   private static final Hashtable<Object, Context> objectBindings = new Hashtable();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  51 */   private static final Hashtable<Thread, Context> threadBindings = new Hashtable();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  57 */   private static final Hashtable<Thread, Object> threadObjectBindings = new Hashtable();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  63 */   private static final Hashtable<ClassLoader, Context> clBindings = new Hashtable();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  69 */   private static final Hashtable<ClassLoader, Object> clObjectBindings = new Hashtable();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  75 */   protected static final StringManager sm = StringManager.getManager(ContextBindings.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void bindContext(Object obj, Context context)
/*     */   {
/*  87 */     bindContext(obj, context, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void bindContext(Object obj, Context context, Object token)
/*     */   {
/*  99 */     if (ContextAccessController.checkSecurityToken(obj, token)) {
/* 100 */       objectBindings.put(obj, context);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void unbindContext(Object obj, Object token)
/*     */   {
/* 112 */     if (ContextAccessController.checkSecurityToken(obj, token)) {
/* 113 */       objectBindings.remove(obj);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static Context getContext(Object obj)
/*     */   {
/* 124 */     return (Context)objectBindings.get(obj);
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
/*     */   public static void bindThread(Object obj, Object token)
/*     */     throws NamingException
/*     */   {
/* 138 */     if (ContextAccessController.checkSecurityToken(obj, token)) {
/* 139 */       Context context = (Context)objectBindings.get(obj);
/* 140 */       if (context == null)
/*     */       {
/* 142 */         throw new NamingException(sm.getString("contextBindings.unknownContext", new Object[] { obj }));
/*     */       }
/* 144 */       threadBindings.put(Thread.currentThread(), context);
/* 145 */       threadObjectBindings.put(Thread.currentThread(), obj);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void unbindThread(Object obj, Object token)
/*     */   {
/* 157 */     if (ContextAccessController.checkSecurityToken(obj, token)) {
/* 158 */       threadBindings.remove(Thread.currentThread());
/* 159 */       threadObjectBindings.remove(Thread.currentThread());
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
/*     */   public static Context getThread()
/*     */     throws NamingException
/*     */   {
/* 173 */     Context context = (Context)threadBindings.get(Thread.currentThread());
/* 174 */     if (context == null)
/*     */     {
/* 176 */       throw new NamingException(sm.getString("contextBindings.noContextBoundToThread"));
/*     */     }
/* 178 */     return context;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static String getThreadName()
/*     */     throws NamingException
/*     */   {
/* 187 */     Object obj = threadObjectBindings.get(Thread.currentThread());
/* 188 */     if (obj == null)
/*     */     {
/* 190 */       throw new NamingException(sm.getString("contextBindings.noContextBoundToThread"));
/*     */     }
/* 192 */     return obj.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isThreadBound()
/*     */   {
/* 203 */     return threadBindings.containsKey(Thread.currentThread());
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
/*     */   public static void bindClassLoader(Object obj, Object token, ClassLoader classLoader)
/*     */     throws NamingException
/*     */   {
/* 219 */     if (ContextAccessController.checkSecurityToken(obj, token)) {
/* 220 */       Context context = (Context)objectBindings.get(obj);
/* 221 */       if (context == null)
/*     */       {
/* 223 */         throw new NamingException(sm.getString("contextBindings.unknownContext", new Object[] { obj }));
/*     */       }
/* 225 */       clBindings.put(classLoader, context);
/* 226 */       clObjectBindings.put(classLoader, obj);
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
/*     */   public static void unbindClassLoader(Object obj, Object token, ClassLoader classLoader)
/*     */   {
/* 240 */     if (ContextAccessController.checkSecurityToken(obj, token)) {
/* 241 */       Object o = clObjectBindings.get(classLoader);
/* 242 */       if ((o == null) || (!o.equals(obj))) {
/* 243 */         return;
/*     */       }
/* 245 */       clBindings.remove(classLoader);
/* 246 */       clObjectBindings.remove(classLoader);
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
/*     */   public static Context getClassLoader()
/*     */     throws NamingException
/*     */   {
/* 260 */     ClassLoader cl = Thread.currentThread().getContextClassLoader();
/* 261 */     Context context = null;
/*     */     do {
/* 263 */       context = (Context)clBindings.get(cl);
/* 264 */       if (context != null) {
/* 265 */         return context;
/*     */       }
/* 267 */     } while ((cl = cl.getParent()) != null);
/* 268 */     throw new NamingException(sm.getString("contextBindings.noContextBoundToCL"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static String getClassLoaderName()
/*     */     throws NamingException
/*     */   {
/* 277 */     ClassLoader cl = Thread.currentThread().getContextClassLoader();
/* 278 */     Object obj = null;
/*     */     do {
/* 280 */       obj = clObjectBindings.get(cl);
/* 281 */       if (obj != null) {
/* 282 */         return obj.toString();
/*     */       }
/* 284 */     } while ((cl = cl.getParent()) != null);
/* 285 */     throw new NamingException(sm.getString("contextBindings.noContextBoundToCL"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isClassLoaderBound()
/*     */   {
/* 297 */     ClassLoader cl = Thread.currentThread().getContextClassLoader();
/*     */     do {
/* 299 */       if (clBindings.containsKey(cl)) {
/* 300 */         return true;
/*     */       }
/* 302 */     } while ((cl = cl.getParent()) != null);
/* 303 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\ContextBindings.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */