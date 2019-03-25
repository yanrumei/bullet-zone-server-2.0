/*    */ package ch.qos.logback.core.recovery;
/*    */ 
/*    */ import ch.qos.logback.core.net.SyslogOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.net.SocketException;
/*    */ import java.net.UnknownHostException;
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
/*    */ public class ResilientSyslogOutputStream
/*    */   extends ResilientOutputStreamBase
/*    */ {
/*    */   String syslogHost;
/*    */   int port;
/*    */   
/*    */   public ResilientSyslogOutputStream(String syslogHost, int port)
/*    */     throws UnknownHostException, SocketException
/*    */   {
/* 29 */     this.syslogHost = syslogHost;
/* 30 */     this.port = port;
/* 31 */     this.os = new SyslogOutputStream(syslogHost, port);
/* 32 */     this.presumedClean = true;
/*    */   }
/*    */   
/*    */   String getDescription()
/*    */   {
/* 37 */     return "syslog [" + this.syslogHost + ":" + this.port + "]";
/*    */   }
/*    */   
/*    */   OutputStream openNewOutputStream() throws IOException
/*    */   {
/* 42 */     return new SyslogOutputStream(this.syslogHost, this.port);
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 47 */     return "c.q.l.c.recovery.ResilientSyslogOutputStream@" + System.identityHashCode(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\recovery\ResilientSyslogOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */