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
/*     */ import javax.jms.MessageConsumer;
/*     */ import javax.jms.MessageListener;
/*     */ import javax.jms.ObjectMessage;
/*     */ import javax.jms.Queue;
/*     */ import javax.jms.QueueConnection;
/*     */ import javax.jms.QueueConnectionFactory;
/*     */ import javax.jms.QueueSession;
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
/*     */ public class JMSQueueSink
/*     */   implements MessageListener
/*     */ {
/*  48 */   private Logger logger = (Logger)LoggerFactory.getLogger(JMSTopicSink.class);
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/*  51 */     if (args.length < 2) {
/*  52 */       usage("Wrong number of arguments.");
/*     */     }
/*     */     
/*  55 */     String qcfBindingName = args[0];
/*  56 */     String queueBindingName = args[1];
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
/*  67 */     new JMSQueueSink(qcfBindingName, queueBindingName, username, password);
/*     */     
/*  69 */     BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
/*     */     
/*  71 */     System.out.println("Type \"exit\" to quit JMSQueueSink.");
/*     */     String s;
/*  73 */     do { s = stdin.readLine();
/*  74 */     } while (!s.equalsIgnoreCase("exit"));
/*  75 */     System.out.println("Exiting. Kill the application if it does not exit due to daemon threads.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JMSQueueSink(String qcfBindingName, String queueBindingName, String username, String password)
/*     */   {
/*     */     try
/*     */     {
/*  84 */       Properties env = new Properties();
/*  85 */       env.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
/*  86 */       env.put("java.naming.provider.url", "tcp://localhost:61616");
/*  87 */       Context ctx = new InitialContext(env);
/*     */       
/*  89 */       QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory)lookup(ctx, qcfBindingName);
/*  90 */       System.out.println("Queue Cnx Factory found");
/*  91 */       Queue queue = (Queue)ctx.lookup(queueBindingName);
/*  92 */       System.out.println("Queue found: " + queue.getQueueName());
/*     */       
/*  94 */       QueueConnection queueConnection = queueConnectionFactory.createQueueConnection(username, password);
/*  95 */       System.out.println("Queue Connection created");
/*     */       
/*  97 */       QueueSession queueSession = queueConnection.createQueueSession(false, 1);
/*     */       
/*  99 */       MessageConsumer queueConsumer = queueSession.createConsumer(queue);
/*     */       
/* 101 */       queueConsumer.setMessageListener(this);
/*     */       
/* 103 */       queueConnection.start();
/* 104 */       System.out.println("Queue Connection started");
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
/* 138 */     System.err.println("Usage: java " + JMSQueueSink.class.getName() + " QueueConnectionFactoryBindingName QueueBindingName Username Password");
/* 139 */     System.exit(1);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\net\JMSQueueSink.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */