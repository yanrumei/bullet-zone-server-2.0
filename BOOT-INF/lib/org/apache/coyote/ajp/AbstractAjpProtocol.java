/*     */ package org.apache.coyote.ajp;
/*     */ 
/*     */ import org.apache.coyote.AbstractProtocol;
/*     */ import org.apache.coyote.AbstractProtocol.ConnectionHandler;
/*     */ import org.apache.coyote.Processor;
/*     */ import org.apache.coyote.UpgradeProtocol;
/*     */ import org.apache.coyote.UpgradeToken;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.tomcat.util.net.AbstractEndpoint;
/*     */ import org.apache.tomcat.util.net.SSLHostConfig;
/*     */ import org.apache.tomcat.util.net.SocketWrapperBase;
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
/*     */ public abstract class AbstractAjpProtocol<S>
/*     */   extends AbstractProtocol<S>
/*     */ {
/*  41 */   protected static final StringManager sm = StringManager.getManager(AbstractAjpProtocol.class);
/*     */   
/*     */   public AbstractAjpProtocol(AbstractEndpoint<S> endpoint)
/*     */   {
/*  45 */     super(endpoint);
/*  46 */     setConnectionTimeout(-1);
/*     */     
/*  48 */     getEndpoint().setUseSendfile(false);
/*  49 */     AbstractProtocol.ConnectionHandler<S> cHandler = new AbstractProtocol.ConnectionHandler(this);
/*  50 */     setHandler(cHandler);
/*  51 */     getEndpoint().setHandler(cHandler);
/*     */   }
/*     */   
/*     */ 
/*     */   protected String getProtocolName()
/*     */   {
/*  57 */     return "Ajp";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractEndpoint<S> getEndpoint()
/*     */   {
/*  68 */     return super.getEndpoint();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected UpgradeProtocol getNegotiatedProtocol(String name)
/*     */   {
/*  79 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected UpgradeProtocol getUpgradeProtocol(String name)
/*     */   {
/*  90 */     return null;
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
/* 107 */   protected boolean ajpFlush = true;
/* 108 */   public boolean getAjpFlush() { return this.ajpFlush; }
/*     */   
/* 110 */   public void setAjpFlush(boolean ajpFlush) { this.ajpFlush = ajpFlush; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 118 */   private boolean tomcatAuthentication = true;
/* 119 */   public boolean getTomcatAuthentication() { return this.tomcatAuthentication; }
/*     */   
/* 121 */   public void setTomcatAuthentication(boolean tomcatAuthentication) { this.tomcatAuthentication = tomcatAuthentication; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 129 */   private boolean tomcatAuthorization = false;
/* 130 */   public boolean getTomcatAuthorization() { return this.tomcatAuthorization; }
/*     */   
/* 132 */   public void setTomcatAuthorization(boolean tomcatAuthorization) { this.tomcatAuthorization = tomcatAuthorization; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 139 */   private String requiredSecret = null;
/*     */   
/* 141 */   public void setRequiredSecret(String requiredSecret) { this.requiredSecret = requiredSecret; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 148 */   private int packetSize = 8192;
/* 149 */   public int getPacketSize() { return this.packetSize; }
/*     */   
/* 151 */   public void setPacketSize(int packetSize) { if (packetSize < 8192) {
/* 152 */       this.packetSize = 8192;
/*     */     } else {
/* 154 */       this.packetSize = packetSize;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addSslHostConfig(SSLHostConfig sslHostConfig)
/*     */   {
/* 163 */     getLog().warn(sm.getString("ajpprotocol.noSSL", new Object[] { sslHostConfig.getHostName() }));
/*     */   }
/*     */   
/*     */ 
/*     */   public SSLHostConfig[] findSslHostConfigs()
/*     */   {
/* 169 */     return new SSLHostConfig[0];
/*     */   }
/*     */   
/*     */ 
/*     */   public void addUpgradeProtocol(UpgradeProtocol upgradeProtocol)
/*     */   {
/* 175 */     getLog().warn(sm.getString("ajpprotocol.noUpgrade", new Object[] { upgradeProtocol.getClass().getName() }));
/*     */   }
/*     */   
/*     */ 
/*     */   public UpgradeProtocol[] findUpgradeProtocols()
/*     */   {
/* 181 */     return new UpgradeProtocol[0];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Processor createProcessor()
/*     */   {
/* 188 */     AjpProcessor processor = new AjpProcessor(getPacketSize(), getEndpoint());
/* 189 */     processor.setAdapter(getAdapter());
/* 190 */     processor.setAjpFlush(getAjpFlush());
/* 191 */     processor.setTomcatAuthentication(getTomcatAuthentication());
/* 192 */     processor.setTomcatAuthorization(getTomcatAuthorization());
/* 193 */     processor.setRequiredSecret(this.requiredSecret);
/* 194 */     processor.setKeepAliveTimeout(getKeepAliveTimeout());
/* 195 */     processor.setClientCertProvider(getClientCertProvider());
/* 196 */     processor.setSendReasonPhrase(getSendReasonPhrase());
/* 197 */     return processor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Processor createUpgradeProcessor(SocketWrapperBase<?> socket, UpgradeToken upgradeToken)
/*     */   {
/* 204 */     throw new IllegalStateException(sm.getString("ajpprotocol.noUpgradeHandler", new Object[] {upgradeToken
/* 205 */       .getHttpUpgradeHandler().getClass().getName() }));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\ajp\AbstractAjpProtocol.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */