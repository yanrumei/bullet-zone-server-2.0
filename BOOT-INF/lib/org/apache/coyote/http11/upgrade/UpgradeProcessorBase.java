/*    */ package org.apache.coyote.http11.upgrade;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ import javax.servlet.http.WebConnection;
/*    */ import org.apache.coyote.AbstractProcessorLight;
/*    */ import org.apache.coyote.Request;
/*    */ import org.apache.coyote.UpgradeToken;
/*    */ import org.apache.tomcat.util.net.AbstractEndpoint.Handler.SocketState;
/*    */ import org.apache.tomcat.util.net.SocketWrapperBase;
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
/*    */ public abstract class UpgradeProcessorBase
/*    */   extends AbstractProcessorLight
/*    */   implements WebConnection
/*    */ {
/*    */   protected static final int INFINITE_TIMEOUT = -1;
/*    */   private final UpgradeToken upgradeToken;
/*    */   
/*    */   public UpgradeProcessorBase(UpgradeToken upgradeToken)
/*    */   {
/* 37 */     this.upgradeToken = upgradeToken;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final boolean isUpgrade()
/*    */   {
/* 45 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */   public UpgradeToken getUpgradeToken()
/*    */   {
/* 51 */     return this.upgradeToken;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final void recycle() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final AbstractEndpoint.Handler.SocketState service(SocketWrapperBase<?> socketWrapper)
/*    */     throws IOException
/*    */   {
/* 65 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public final AbstractEndpoint.Handler.SocketState asyncPostProcess()
/*    */   {
/* 71 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public final boolean isAsync()
/*    */   {
/* 77 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   public final Request getRequest()
/*    */   {
/* 83 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public ByteBuffer getLeftoverInput()
/*    */   {
/* 89 */     return null;
/*    */   }
/*    */   
/*    */   public void timeoutAsync(long now) {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http1\\upgrade\UpgradeProcessorBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */