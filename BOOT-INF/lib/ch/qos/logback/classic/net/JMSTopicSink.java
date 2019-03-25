/*     */ package ch.qos.logback.classic.net;
/*     */ 
/*     */ import ch.qos.logback.classic.Logger;
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import ch.qos.logback.classic.util.ContextInitializer;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Properties;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Message;
/*     */ import javax.jms.MessageListener;
/*     */ import javax.jms.ObjectMessage;
/*     */ import javax.jms.Topic;
/*     */ import javax.jms.TopicConnection;
/*     */ import javax.jms.TopicConnectionFactory;
/*     */ import javax.jms.TopicSession;
/*     */ import javax.jms.TopicSubscriber;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NameNotFoundException;
/*     */ import javax.naming.NamingException;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ public class JMSTopicSink
/*     */   implements MessageListener
/*     */ {
/*  48 */   private Logger logger = (Logger)LoggerFactory.getLogger(JMSTopicSink.class);
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/*  51 */     if (args.length < 2) {
/*  52 */       usage("Wrong number of arguments.");
/*     */     }
/*     */     
/*  55 */     String tcfBindingName = args[0];
/*  56 */     String topicBindingName = args[1];
/*  57 */     String username = null;
/*  58 */     String password = null;
/*  59 */     if (args.length == 4) {
/*  60 */       username = args[2];
/*  61 */       password = args[3];
/*     */     }
/*     */     
/*  64 */     LoggerContext loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();
/*  65 */     new ContextInitializer(loggerContext).autoConfig();
/*     */     
/*  67 */     new JMSTopicSink(tcfBindingName, topicBindingName, username, password);
/*     */     
/*  69 */     BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
/*     */     
/*  71 */     System.out.println("Type \"exit\" to quit JMSTopicSink.");
/*     */     String s;
/*  73 */     do { s = stdin.readLine();
/*  74 */     } while (!s.equalsIgnoreCase("exit"));
/*  75 */     System.out.println("Exiting. Kill the application if it does not exit due to daemon threads.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JMSTopicSink(String tcfBindingName, String topicBindingName, String username, String password)
/*     */   {
/*     */     try
/*     */     {
/*  84 */       Properties env = new Properties();
/*  85 */       env.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
/*  86 */       env.put("java.naming.provider.url", "tcp://localhost:61616");
/*  87 */       Context ctx = new InitialContext(env);
/*     */       
/*  89 */       TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory)lookup(ctx, tcfBindingName);
/*  90 */       System.out.println("Topic Cnx Factory found");
/*  91 */       Topic topic = (Topic)ctx.lookup(topicBindingName);
/*  92 */       System.out.println("Topic found: " + topic.getTopicName());
/*     */       
/*  94 */       TopicConnection topicConnection = topicConnectionFactory.createTopicConnection(username, password);
/*  95 */       System.out.println("Topic Connection created");
/*     */       
/*  97 */       TopicSession topicSession = topicConnection.createTopicSession(false, 1);
/*     */       
/*  99 */       TopicSubscriber topicSubscriber = topicSession.createSubscriber(topic);
/*     */       
/* 101 */       topicSubscriber.setMessageListener(this);
/*     */       
/* 103 */       topicConnection.start();
/* 104 */       System.out.println("Topic Connection started");
/*     */     }
/*     */     catch (Exception e) {
/* 107 */       this.logger.error("Could not read JMS message.", e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onMessage(Message message)
/*     */   {
/*     */     try {
/* 114 */       if ((message instanceof ObjectMessage)) {
/* 115 */         ObjectMessage objectMessage = (ObjectMessage)message;
/* 116 */         ILoggingEvent event = (ILoggingEvent)objectMessage.getObject();
/* 117 */         Logger log = (Logger)LoggerFactory.getLogger(event.getLoggerName());
/* 118 */         log.callAppenders(event);
/*     */       } else {
/* 120 */         this.logger.warn("Received message is of type " + message.getJMSType() + ", was expecting ObjectMessage.");
/*     */       }
/*     */     } catch (JMSException jmse) {
/* 123 */       this.logger.error("Exception thrown while processing incoming message.", jmse);
/*     */     }
/*     */   }
/*     */   
/*     */   protected Object lookup(Context ctx, String name) throws NamingException {
/*     */     try {
/* 129 */       return ctx.lookup(name);
/*     */     } catch (NameNotFoundException e) {
/* 131 */       this.logger.error("Could not find name [" + name + "].");
/* 132 */       throw e;
/*     */     }
/*     */   }
/*     */   
/*     */   static void usage(String msg) {
/* 137 */     System.err.println(msg);
/* 138 */     System.err.println("Usage: java " + JMSTopicSink.class.getName() + " TopicConnectionFactoryBindingName TopicBindingName Username Password");
/* 139 */     System.exit(1);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\net\JMSTopicSink.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */