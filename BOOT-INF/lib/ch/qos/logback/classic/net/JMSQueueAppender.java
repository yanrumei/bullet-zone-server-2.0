/*     */ package ch.qos.logback.classic.net;
/*     */ 
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import ch.qos.logback.core.net.JMSAppenderBase;
/*     */ import ch.qos.logback.core.spi.PreSerializationTransformer;
/*     */ import java.io.Serializable;
/*     */ import javax.jms.ObjectMessage;
/*     */ import javax.jms.Queue;
/*     */ import javax.jms.QueueConnection;
/*     */ import javax.jms.QueueConnectionFactory;
/*     */ import javax.jms.QueueSender;
/*     */ import javax.jms.QueueSession;
/*     */ import javax.naming.Context;
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
/*     */ public class JMSQueueAppender
/*     */   extends JMSAppenderBase<ILoggingEvent>
/*     */ {
/*  44 */   static int SUCCESSIVE_FAILURE_LIMIT = 3;
/*     */   
/*     */   String queueBindingName;
/*     */   
/*     */   String qcfBindingName;
/*     */   QueueConnection queueConnection;
/*     */   QueueSession queueSession;
/*     */   QueueSender queueSender;
/*  52 */   int successiveFailureCount = 0;
/*     */   
/*  54 */   private PreSerializationTransformer<ILoggingEvent> pst = new LoggingEventPreSerializationTransformer();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setQueueConnectionFactoryBindingName(String qcfBindingName)
/*     */   {
/*  62 */     this.qcfBindingName = qcfBindingName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getQueueConnectionFactoryBindingName()
/*     */   {
/*  69 */     return this.qcfBindingName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setQueueBindingName(String queueBindingName)
/*     */   {
/*  77 */     this.queueBindingName = queueBindingName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getQueueBindingName()
/*     */   {
/*  84 */     return this.queueBindingName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void start()
/*     */   {
/*     */     try
/*     */     {
/*  94 */       Context jndi = buildJNDIContext();
/*     */       
/*     */ 
/*  97 */       QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory)lookup(jndi, this.qcfBindingName);
/*     */       
/*  99 */       if (this.userName != null) {
/* 100 */         this.queueConnection = queueConnectionFactory.createQueueConnection(this.userName, this.password);
/*     */       } else {
/* 102 */         this.queueConnection = queueConnectionFactory.createQueueConnection();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 108 */       this.queueSession = this.queueConnection.createQueueSession(false, 1);
/*     */       
/*     */ 
/* 111 */       Queue queue = (Queue)lookup(jndi, this.queueBindingName);
/*     */       
/*     */ 
/* 114 */       this.queueSender = this.queueSession.createSender(queue);
/*     */       
/*     */ 
/* 117 */       this.queueConnection.start();
/*     */       
/* 119 */       jndi.close();
/*     */     } catch (Exception e) {
/* 121 */       addError("Error while activating options for appender named [" + this.name + "].", e);
/*     */     }
/*     */     
/* 124 */     if ((this.queueConnection != null) && (this.queueSession != null) && (this.queueSender != null)) {
/* 125 */       super.start();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void stop()
/*     */   {
/* 135 */     if (!this.started) {
/* 136 */       return;
/*     */     }
/*     */     
/* 139 */     this.started = false;
/*     */     try
/*     */     {
/* 142 */       if (this.queueSession != null) {
/* 143 */         this.queueSession.close();
/*     */       }
/* 145 */       if (this.queueConnection != null) {
/* 146 */         this.queueConnection.close();
/*     */       }
/*     */     } catch (Exception e) {
/* 149 */       addError("Error while closing JMSAppender [" + this.name + "].", e);
/*     */     }
/*     */     
/*     */ 
/* 153 */     this.queueSender = null;
/* 154 */     this.queueSession = null;
/* 155 */     this.queueConnection = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void append(ILoggingEvent event)
/*     */   {
/* 163 */     if (!isStarted()) {
/* 164 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 168 */       ObjectMessage msg = this.queueSession.createObjectMessage();
/* 169 */       Serializable so = this.pst.transform(event);
/* 170 */       msg.setObject(so);
/* 171 */       this.queueSender.send(msg);
/* 172 */       this.successiveFailureCount = 0;
/*     */     } catch (Exception e) {
/* 174 */       this.successiveFailureCount += 1;
/* 175 */       if (this.successiveFailureCount > SUCCESSIVE_FAILURE_LIMIT) {
/* 176 */         stop();
/*     */       }
/* 178 */       addError("Could not send message in JMSQueueAppender [" + this.name + "].", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected QueueConnection getQueueConnection()
/*     */   {
/* 188 */     return this.queueConnection;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected QueueSession getQueueSession()
/*     */   {
/* 196 */     return this.queueSession;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected QueueSender getQueueSender()
/*     */   {
/* 204 */     return this.queueSender;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\net\JMSQueueAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */