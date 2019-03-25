/*     */ package ch.qos.logback.classic.net;
/*     */ 
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import ch.qos.logback.core.net.JMSAppenderBase;
/*     */ import ch.qos.logback.core.spi.PreSerializationTransformer;
/*     */ import java.io.Serializable;
/*     */ import javax.jms.ObjectMessage;
/*     */ import javax.jms.Topic;
/*     */ import javax.jms.TopicConnection;
/*     */ import javax.jms.TopicConnectionFactory;
/*     */ import javax.jms.TopicPublisher;
/*     */ import javax.jms.TopicSession;
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
/*     */ public class JMSTopicAppender
/*     */   extends JMSAppenderBase<ILoggingEvent>
/*     */ {
/*  44 */   static int SUCCESSIVE_FAILURE_LIMIT = 3;
/*     */   
/*     */   String topicBindingName;
/*     */   
/*     */   String tcfBindingName;
/*     */   TopicConnection topicConnection;
/*     */   TopicSession topicSession;
/*     */   TopicPublisher topicPublisher;
/*  52 */   int successiveFailureCount = 0;
/*     */   
/*  54 */   private PreSerializationTransformer<ILoggingEvent> pst = new LoggingEventPreSerializationTransformer();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTopicConnectionFactoryBindingName(String tcfBindingName)
/*     */   {
/*  62 */     this.tcfBindingName = tcfBindingName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getTopicConnectionFactoryBindingName()
/*     */   {
/*  69 */     return this.tcfBindingName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTopicBindingName(String topicBindingName)
/*     */   {
/*  77 */     this.topicBindingName = topicBindingName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getTopicBindingName()
/*     */   {
/*  84 */     return this.topicBindingName;
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
/*  97 */       TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory)lookup(jndi, this.tcfBindingName);
/*     */       
/*  99 */       if (this.userName != null) {
/* 100 */         this.topicConnection = topicConnectionFactory.createTopicConnection(this.userName, this.password);
/*     */       } else {
/* 102 */         this.topicConnection = topicConnectionFactory.createTopicConnection();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 108 */       this.topicSession = this.topicConnection.createTopicSession(false, 1);
/*     */       
/*     */ 
/* 111 */       Topic topic = (Topic)lookup(jndi, this.topicBindingName);
/*     */       
/*     */ 
/* 114 */       this.topicPublisher = this.topicSession.createPublisher(topic);
/*     */       
/*     */ 
/* 117 */       this.topicConnection.start();
/*     */       
/* 119 */       jndi.close();
/*     */     } catch (Exception e) {
/* 121 */       addError("Error while activating options for appender named [" + this.name + "].", e);
/*     */     }
/*     */     
/* 124 */     if ((this.topicConnection != null) && (this.topicSession != null) && (this.topicPublisher != null)) {
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
/* 142 */       if (this.topicSession != null) {
/* 143 */         this.topicSession.close();
/*     */       }
/* 145 */       if (this.topicConnection != null) {
/* 146 */         this.topicConnection.close();
/*     */       }
/*     */     } catch (Exception e) {
/* 149 */       addError("Error while closing JMSAppender [" + this.name + "].", e);
/*     */     }
/*     */     
/*     */ 
/* 153 */     this.topicPublisher = null;
/* 154 */     this.topicSession = null;
/* 155 */     this.topicConnection = null;
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
/* 168 */       ObjectMessage msg = this.topicSession.createObjectMessage();
/* 169 */       Serializable so = this.pst.transform(event);
/* 170 */       msg.setObject(so);
/* 171 */       this.topicPublisher.publish(msg);
/* 172 */       this.successiveFailureCount = 0;
/*     */     } catch (Exception e) {
/* 174 */       this.successiveFailureCount += 1;
/* 175 */       if (this.successiveFailureCount > SUCCESSIVE_FAILURE_LIMIT) {
/* 176 */         stop();
/*     */       }
/* 178 */       addError("Could not publish message in JMSTopicAppender [" + this.name + "].", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TopicConnection getTopicConnection()
/*     */   {
/* 187 */     return this.topicConnection;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TopicSession getTopicSession()
/*     */   {
/* 195 */     return this.topicSession;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TopicPublisher getTopicPublisher()
/*     */   {
/* 203 */     return this.topicPublisher;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\net\JMSTopicAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */