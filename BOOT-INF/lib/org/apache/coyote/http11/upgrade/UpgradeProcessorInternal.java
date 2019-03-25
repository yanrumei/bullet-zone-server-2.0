/*    */ package org.apache.coyote.http11.upgrade;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.servlet.ServletInputStream;
/*    */ import javax.servlet.ServletOutputStream;
/*    */ import org.apache.coyote.UpgradeToken;
/*    */ import org.apache.juli.logging.Log;
/*    */ import org.apache.juli.logging.LogFactory;
/*    */ import org.apache.tomcat.util.net.AbstractEndpoint.Handler.SocketState;
/*    */ import org.apache.tomcat.util.net.SSLSupport;
/*    */ import org.apache.tomcat.util.net.SocketEvent;
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
/*    */ 
/*    */ public class UpgradeProcessorInternal
/*    */   extends UpgradeProcessorBase
/*    */ {
/* 34 */   private static final Log log = LogFactory.getLog(UpgradeProcessorInternal.class);
/*    */   
/*    */   private final InternalHttpUpgradeHandler internalHttpUpgradeHandler;
/*    */   
/*    */   public UpgradeProcessorInternal(SocketWrapperBase<?> wrapper, UpgradeToken upgradeToken)
/*    */   {
/* 40 */     super(upgradeToken);
/* 41 */     this.internalHttpUpgradeHandler = ((InternalHttpUpgradeHandler)upgradeToken.getHttpUpgradeHandler());
/*    */     
/*    */ 
/*    */ 
/* 45 */     wrapper.setReadTimeout(-1L);
/* 46 */     wrapper.setWriteTimeout(-1L);
/*    */     
/* 48 */     this.internalHttpUpgradeHandler.setSocketWrapper(wrapper);
/*    */   }
/*    */   
/*    */ 
/*    */   public AbstractEndpoint.Handler.SocketState dispatch(SocketEvent status)
/*    */   {
/* 54 */     return this.internalHttpUpgradeHandler.upgradeDispatch(status);
/*    */   }
/*    */   
/*    */ 
/*    */   public final void setSslSupport(SSLSupport sslSupport)
/*    */   {
/* 60 */     this.internalHttpUpgradeHandler.setSslSupport(sslSupport);
/*    */   }
/*    */   
/*    */ 
/*    */   public void pause()
/*    */   {
/* 66 */     this.internalHttpUpgradeHandler.pause();
/*    */   }
/*    */   
/*    */ 
/*    */   protected Log getLog()
/*    */   {
/* 72 */     return log;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void close()
/*    */     throws Exception
/*    */   {
/* 80 */     this.internalHttpUpgradeHandler.destroy();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public ServletInputStream getInputStream()
/*    */     throws IOException
/*    */   {
/* 88 */     return null;
/*    */   }
/*    */   
/*    */   public ServletOutputStream getOutputStream() throws IOException
/*    */   {
/* 93 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http1\\upgrade\UpgradeProcessorInternal.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */