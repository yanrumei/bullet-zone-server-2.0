/*    */ package org.springframework.remoting.caucho;
/*    */ 
/*    */ import com.caucho.burlap.io.BurlapInput;
/*    */ import com.caucho.burlap.io.BurlapOutput;
/*    */ import com.caucho.burlap.server.BurlapSkeleton;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import org.springframework.beans.factory.InitializingBean;
/*    */ import org.springframework.remoting.support.RemoteExporter;
/*    */ import org.springframework.util.Assert;
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
/*    */ @Deprecated
/*    */ public class BurlapExporter
/*    */   extends RemoteExporter
/*    */   implements InitializingBean
/*    */ {
/*    */   private BurlapSkeleton skeleton;
/*    */   
/*    */   public void afterPropertiesSet()
/*    */   {
/* 55 */     prepare();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void prepare()
/*    */   {
/* 62 */     checkService();
/* 63 */     checkServiceInterface();
/* 64 */     this.skeleton = new BurlapSkeleton(getProxyForService(), getServiceInterface());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void invoke(InputStream inputStream, OutputStream outputStream)
/*    */     throws Throwable
/*    */   {
/* 75 */     Assert.notNull(this.skeleton, "Burlap exporter has not been initialized");
/* 76 */     ClassLoader originalClassLoader = overrideThreadContextClassLoader();
/*    */     try {
/* 78 */       this.skeleton.invoke(new BurlapInput(inputStream), new BurlapOutput(outputStream));
/*    */     }
/*    */     finally {
/*    */       try {
/* 82 */         inputStream.close();
/*    */       }
/*    */       catch (IOException localIOException2) {}
/*    */       
/*    */       try
/*    */       {
/* 88 */         outputStream.close();
/*    */       }
/*    */       catch (IOException localIOException3) {}
/*    */       
/*    */ 
/* 93 */       resetThreadContextClassLoader(originalClassLoader);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\remoting\caucho\BurlapExporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */