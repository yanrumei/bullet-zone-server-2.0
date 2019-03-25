/*    */ package org.apache.tomcat.util.compat;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import javax.net.ssl.SSLEngine;
/*    */ import javax.net.ssl.SSLParameters;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class Jre8Compat
/*    */   extends JreCompat
/*    */ {
/*    */   private static final int RUNTIME_MAJOR_VERSION = 8;
/*    */   private static final Method setUseCipherSuitesOrderMethod;
/*    */   
/*    */   static
/*    */   {
/* 33 */     Method m1 = null;
/*    */     try
/*    */     {
/* 36 */       Class<?> c1 = Class.forName("javax.net.ssl.SSLParameters");
/*    */       
/* 38 */       m1 = c1.getMethod("setUseCipherSuitesOrder", new Class[] { Boolean.TYPE });
/*    */     }
/*    */     catch (SecurityException localSecurityException) {}catch (NoSuchMethodException localNoSuchMethodException) {}catch (ClassNotFoundException localClassNotFoundException) {}
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 46 */     setUseCipherSuitesOrderMethod = m1;
/*    */   }
/*    */   
/*    */   static boolean isSupported()
/*    */   {
/* 51 */     return setUseCipherSuitesOrderMethod != null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void setUseServerCipherSuitesOrder(SSLEngine engine, boolean useCipherSuitesOrder)
/*    */   {
/* 58 */     SSLParameters sslParameters = engine.getSSLParameters();
/*    */     try {
/* 60 */       setUseCipherSuitesOrderMethod.invoke(sslParameters, new Object[] {
/* 61 */         Boolean.valueOf(useCipherSuitesOrder) });
/* 62 */       engine.setSSLParameters(sslParameters);
/*    */     } catch (IllegalArgumentException e) {
/* 64 */       throw new UnsupportedOperationException(e);
/*    */     } catch (IllegalAccessException e) {
/* 66 */       throw new UnsupportedOperationException(e);
/*    */     } catch (InvocationTargetException e) {
/* 68 */       throw new UnsupportedOperationException(e);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public int jarFileRuntimeMajorVersion()
/*    */   {
/* 75 */     return 8;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\compat\Jre8Compat.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */