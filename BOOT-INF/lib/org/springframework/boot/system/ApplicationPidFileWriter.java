/*     */ package org.springframework.boot.system;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.boot.ApplicationPid;
/*     */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*     */ import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
/*     */ import org.springframework.boot.context.event.ApplicationPreparedEvent;
/*     */ import org.springframework.boot.context.event.ApplicationReadyEvent;
/*     */ import org.springframework.boot.context.event.SpringApplicationEvent;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class ApplicationPidFileWriter
/*     */   implements ApplicationListener<SpringApplicationEvent>, Ordered
/*     */ {
/*  65 */   private static final Log logger = LogFactory.getLog(ApplicationPidFileWriter.class);
/*     */   private static final String DEFAULT_FILE_NAME = "application.pid";
/*     */   private static final List<Property> FILE_PROPERTIES;
/*     */   private static final List<Property> FAIL_ON_WRITE_ERROR_PROPERTIES;
/*     */   
/*     */   static
/*     */   {
/*  72 */     List<Property> properties = new ArrayList();
/*  73 */     properties.add(new SpringProperty("spring.pid.", "file"));
/*  74 */     properties.add(new SpringProperty("spring.", "pidfile"));
/*  75 */     properties.add(new SystemProperty("PIDFILE"));
/*  76 */     FILE_PROPERTIES = Collections.unmodifiableList(properties);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  82 */     List<Property> properties = new ArrayList();
/*  83 */     properties.add(new SpringProperty("spring.pid.", "fail-on-write-error"));
/*  84 */     properties.add(new SystemProperty("PID_FAIL_ON_WRITE_ERROR"));
/*  85 */     FAIL_ON_WRITE_ERROR_PROPERTIES = Collections.unmodifiableList(properties);
/*     */   }
/*     */   
/*  88 */   private static final AtomicBoolean created = new AtomicBoolean(false);
/*     */   
/*  90 */   private int order = -2147483635;
/*     */   
/*     */   private final File file;
/*     */   
/*  94 */   private Class<? extends SpringApplicationEvent> triggerEventType = ApplicationPreparedEvent.class;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ApplicationPidFileWriter()
/*     */   {
/* 101 */     this(new File("application.pid"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ApplicationPidFileWriter(String filename)
/*     */   {
/* 109 */     this(new File(filename));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ApplicationPidFileWriter(File file)
/*     */   {
/* 117 */     Assert.notNull(file, "File must not be null");
/* 118 */     this.file = file;
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
/*     */   public void setTriggerEventType(Class<? extends SpringApplicationEvent> triggerEventType)
/*     */   {
/* 131 */     Assert.notNull(triggerEventType, "Trigger event type must not be null");
/* 132 */     this.triggerEventType = triggerEventType;
/*     */   }
/*     */   
/*     */   public void onApplicationEvent(SpringApplicationEvent event)
/*     */   {
/* 137 */     if ((this.triggerEventType.isInstance(event)) && 
/* 138 */       (created.compareAndSet(false, true))) {
/*     */       try {
/* 140 */         writePidFile(event);
/*     */       }
/*     */       catch (Exception ex) {
/* 143 */         String message = String.format("Cannot create pid file %s", new Object[] { this.file });
/*     */         
/* 145 */         if (failOnWriteError(event)) {
/* 146 */           throw new IllegalStateException(message, ex);
/*     */         }
/* 148 */         logger.warn(message, ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void writePidFile(SpringApplicationEvent event) throws IOException
/*     */   {
/* 155 */     File pidFile = this.file;
/* 156 */     String override = getProperty(event, FILE_PROPERTIES);
/* 157 */     if (override != null) {
/* 158 */       pidFile = new File(override);
/*     */     }
/* 160 */     new ApplicationPid().write(pidFile);
/* 161 */     pidFile.deleteOnExit();
/*     */   }
/*     */   
/*     */   private boolean failOnWriteError(SpringApplicationEvent event) {
/* 165 */     String value = getProperty(event, FAIL_ON_WRITE_ERROR_PROPERTIES);
/* 166 */     return value == null ? false : Boolean.parseBoolean(value);
/*     */   }
/*     */   
/*     */   private String getProperty(SpringApplicationEvent event, List<Property> candidates) {
/* 170 */     for (Property candidate : candidates) {
/* 171 */       String value = candidate.getValue(event);
/* 172 */       if (value != null) {
/* 173 */         return value;
/*     */       }
/*     */     }
/* 176 */     return null;
/*     */   }
/*     */   
/*     */   public void setOrder(int order) {
/* 180 */     this.order = order;
/*     */   }
/*     */   
/*     */   public int getOrder()
/*     */   {
/* 185 */     return this.order;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected static void reset()
/*     */   {
/* 192 */     created.set(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static abstract interface Property
/*     */   {
/*     */     public abstract String getValue(SpringApplicationEvent paramSpringApplicationEvent);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class SpringProperty
/*     */     implements ApplicationPidFileWriter.Property
/*     */   {
/*     */     private final String prefix;
/*     */     
/*     */     private final String key;
/*     */     
/*     */ 
/*     */     SpringProperty(String prefix, String key)
/*     */     {
/* 214 */       this.prefix = prefix;
/* 215 */       this.key = key;
/*     */     }
/*     */     
/*     */     public String getValue(SpringApplicationEvent event)
/*     */     {
/* 220 */       Environment environment = getEnvironment(event);
/* 221 */       if (environment == null) {
/* 222 */         return null;
/*     */       }
/* 224 */       return 
/* 225 */         new RelaxedPropertyResolver(environment, this.prefix).getProperty(this.key);
/*     */     }
/*     */     
/*     */     private Environment getEnvironment(SpringApplicationEvent event) {
/* 229 */       if ((event instanceof ApplicationEnvironmentPreparedEvent)) {
/* 230 */         return ((ApplicationEnvironmentPreparedEvent)event).getEnvironment();
/*     */       }
/* 232 */       if ((event instanceof ApplicationPreparedEvent)) {
/* 233 */         return 
/* 234 */           ((ApplicationPreparedEvent)event).getApplicationContext().getEnvironment();
/*     */       }
/* 236 */       if ((event instanceof ApplicationReadyEvent)) {
/* 237 */         return 
/* 238 */           ((ApplicationReadyEvent)event).getApplicationContext().getEnvironment();
/*     */       }
/* 240 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class SystemProperty
/*     */     implements ApplicationPidFileWriter.Property
/*     */   {
/*     */     private final String[] properties;
/*     */     
/*     */ 
/*     */     SystemProperty(String name)
/*     */     {
/* 253 */       this.properties = new String[] { name.toUpperCase(), name.toLowerCase() };
/*     */     }
/*     */     
/*     */     public String getValue(SpringApplicationEvent event)
/*     */     {
/* 258 */       return SystemProperties.get(this.properties);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\system\ApplicationPidFileWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */