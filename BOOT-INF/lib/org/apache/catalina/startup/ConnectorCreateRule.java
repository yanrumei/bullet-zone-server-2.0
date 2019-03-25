/*     */ package org.apache.catalina.startup;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import org.apache.catalina.Service;
/*     */ import org.apache.catalina.connector.Connector;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.IntrospectionUtils;
/*     */ import org.apache.tomcat.util.digester.Digester;
/*     */ import org.apache.tomcat.util.digester.Rule;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ import org.xml.sax.Attributes;
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
/*     */ public class ConnectorCreateRule
/*     */   extends Rule
/*     */ {
/*  41 */   private static final Log log = LogFactory.getLog(ConnectorCreateRule.class);
/*  42 */   protected static final StringManager sm = StringManager.getManager(ConnectorCreateRule.class);
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
/*     */   public void begin(String namespace, String name, Attributes attributes)
/*     */     throws Exception
/*     */   {
/*  59 */     Service svc = (Service)this.digester.peek();
/*  60 */     org.apache.catalina.Executor ex = null;
/*  61 */     if (attributes.getValue("executor") != null) {
/*  62 */       ex = svc.getExecutor(attributes.getValue("executor"));
/*     */     }
/*  64 */     Connector con = new Connector(attributes.getValue("protocol"));
/*  65 */     if (ex != null) {
/*  66 */       setExecutor(con, ex);
/*     */     }
/*  68 */     String sslImplementationName = attributes.getValue("sslImplementationName");
/*  69 */     if (sslImplementationName != null) {
/*  70 */       setSSLImplementationName(con, sslImplementationName);
/*     */     }
/*  72 */     this.digester.push(con);
/*     */   }
/*     */   
/*     */   private static void setExecutor(Connector con, org.apache.catalina.Executor ex) throws Exception {
/*  76 */     Method m = IntrospectionUtils.findMethod(con.getProtocolHandler().getClass(), "setExecutor", new Class[] { java.util.concurrent.Executor.class });
/*  77 */     if (m != null) {
/*  78 */       m.invoke(con.getProtocolHandler(), new Object[] { ex });
/*     */     } else {
/*  80 */       log.warn(sm.getString("connector.noSetExecutor", new Object[] { con }));
/*     */     }
/*     */   }
/*     */   
/*     */   private static void setSSLImplementationName(Connector con, String sslImplementationName) throws Exception {
/*  85 */     Method m = IntrospectionUtils.findMethod(con.getProtocolHandler().getClass(), "setSslImplementationName", new Class[] { String.class });
/*  86 */     if (m != null) {
/*  87 */       m.invoke(con.getProtocolHandler(), new Object[] { sslImplementationName });
/*     */     } else {
/*  89 */       log.warn(sm.getString("connector.noSetSSLImplementationName", new Object[] { con }));
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
/*     */   public void end(String namespace, String name)
/*     */     throws Exception
/*     */   {
/* 104 */     this.digester.pop();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\ConnectorCreateRule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */