/*    */ package org.hibernate.validator.internal.xml;
/*    */ 
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.InputStream;
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ import org.hibernate.validator.internal.util.logging.Log;
/*    */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*    */ import org.hibernate.validator.internal.util.privilegedactions.GetClassLoader;
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
/*    */ final class ResourceLoaderHelper
/*    */ {
/* 24 */   private static final Log log = ;
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
/*    */   static InputStream getResettableInputStreamForPath(String path, ClassLoader externalClassLoader)
/*    */   {
/* 39 */     String inputPath = path;
/* 40 */     if (inputPath.startsWith("/")) {
/* 41 */       inputPath = inputPath.substring(1);
/*    */     }
/*    */     
/* 44 */     InputStream inputStream = null;
/*    */     
/* 46 */     if (externalClassLoader != null) {
/* 47 */       log.debug("Trying to load " + path + " via user class loader");
/* 48 */       inputStream = externalClassLoader.getResourceAsStream(inputPath);
/*    */     }
/*    */     
/* 51 */     if (inputStream == null) {
/* 52 */       ClassLoader loader = (ClassLoader)run(GetClassLoader.fromContext());
/* 53 */       if (loader != null) {
/* 54 */         log.debug("Trying to load " + path + " via TCCL");
/* 55 */         inputStream = loader.getResourceAsStream(inputPath);
/*    */       }
/*    */     }
/*    */     
/* 59 */     if (inputStream == null) {
/* 60 */       log.debug("Trying to load " + path + " via Hibernate Validator's class loader");
/* 61 */       ClassLoader loader = ResourceLoaderHelper.class.getClassLoader();
/* 62 */       inputStream = loader.getResourceAsStream(inputPath);
/*    */     }
/*    */     
/* 65 */     if (inputStream == null) {
/* 66 */       return null;
/*    */     }
/* 68 */     if (inputStream.markSupported()) {
/* 69 */       return inputStream;
/*    */     }
/*    */     
/* 72 */     return new BufferedInputStream(inputStream);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private static <T> T run(PrivilegedAction<T> action)
/*    */   {
/* 83 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\ResourceLoaderHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */