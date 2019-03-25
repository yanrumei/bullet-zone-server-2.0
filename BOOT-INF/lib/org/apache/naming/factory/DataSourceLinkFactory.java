/*     */ package org.apache.naming.factory;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.RefAddr;
/*     */ import javax.naming.Reference;
/*     */ import javax.sql.DataSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DataSourceLinkFactory
/*     */   extends ResourceLinkFactory
/*     */ {
/*     */   public static void setGlobalContext(Context newGlobalContext)
/*     */   {
/*  45 */     ResourceLinkFactory.setGlobalContext(newGlobalContext);
/*     */   }
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
/*  58 */     Object result = super.getObjectInstance(obj, name, nameCtx, environment);
/*     */     
/*  60 */     if (result != null) {
/*  61 */       Reference ref = (Reference)obj;
/*  62 */       RefAddr userAttr = ref.get("username");
/*  63 */       RefAddr passAttr = ref.get("password");
/*  64 */       if ((userAttr.getContent() != null) && (passAttr.getContent() != null)) {
/*  65 */         result = wrapDataSource(result, userAttr.getContent().toString(), passAttr.getContent().toString());
/*     */       }
/*     */     }
/*  68 */     return result;
/*     */   }
/*     */   
/*     */   protected Object wrapDataSource(Object datasource, String username, String password) throws NamingException {
/*     */     try {
/*  73 */       Class<?> proxyClass = Proxy.getProxyClass(datasource.getClass().getClassLoader(), datasource.getClass().getInterfaces());
/*  74 */       Constructor<?> proxyConstructor = proxyClass.getConstructor(new Class[] { InvocationHandler.class });
/*  75 */       DataSourceHandler handler = new DataSourceHandler((DataSource)datasource, username, password);
/*  76 */       return proxyConstructor.newInstance(new Object[] { handler });
/*     */     } catch (Exception x) {
/*  78 */       if ((x instanceof InvocationTargetException)) {
/*  79 */         Throwable cause = x.getCause();
/*  80 */         if ((cause instanceof ThreadDeath)) {
/*  81 */           throw ((ThreadDeath)cause);
/*     */         }
/*  83 */         if ((cause instanceof VirtualMachineError)) {
/*  84 */           throw ((VirtualMachineError)cause);
/*     */         }
/*  86 */         if ((cause instanceof Exception)) {
/*  87 */           x = (Exception)cause;
/*     */         }
/*     */       }
/*  90 */       if ((x instanceof NamingException)) { throw ((NamingException)x);
/*     */       }
/*  92 */       NamingException nx = new NamingException(x.getMessage());
/*  93 */       nx.initCause(x);
/*  94 */       throw nx;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class DataSourceHandler
/*     */     implements InvocationHandler
/*     */   {
/*     */     private final DataSource ds;
/*     */     private final String username;
/*     */     private final String password;
/*     */     private final Method getConnection;
/*     */     
/*     */     public DataSourceHandler(DataSource ds, String username, String password)
/*     */       throws Exception
/*     */     {
/* 110 */       this.ds = ds;
/* 111 */       this.username = username;
/* 112 */       this.password = password;
/* 113 */       this.getConnection = ds.getClass().getMethod("getConnection", new Class[] { String.class, String.class });
/*     */     }
/*     */     
/*     */     public Object invoke(Object proxy, Method method, Object[] args)
/*     */       throws Throwable
/*     */     {
/* 119 */       if (("getConnection".equals(method.getName())) && ((args == null) || (args.length == 0))) {
/* 120 */         args = new String[] { this.username, this.password };
/* 121 */         method = this.getConnection;
/* 122 */       } else if ("unwrap".equals(method.getName())) {
/* 123 */         return unwrap((Class)args[0]);
/*     */       }
/*     */       try {
/* 126 */         return method.invoke(this.ds, args);
/*     */       } catch (Throwable t) {
/* 128 */         if (((t instanceof InvocationTargetException)) && 
/* 129 */           (t.getCause() != null)) {
/* 130 */           throw t.getCause();
/*     */         }
/* 132 */         throw t;
/*     */       }
/*     */     }
/*     */     
/*     */     public Object unwrap(Class<?> iface) throws SQLException
/*     */     {
/* 138 */       if (iface == DataSource.class) {
/* 139 */         return this.ds;
/*     */       }
/* 141 */       throw new SQLException("Not a wrapper of " + iface.getName());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\factory\DataSourceLinkFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */