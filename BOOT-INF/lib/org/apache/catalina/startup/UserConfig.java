/*     */ package org.apache.catalina.startup;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.Host;
/*     */ import org.apache.catalina.LifecycleEvent;
/*     */ import org.apache.catalina.LifecycleListener;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UserConfig
/*     */   implements LifecycleListener
/*     */ {
/*  53 */   private static final Log log = LogFactory.getLog(UserConfig.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  62 */   private String configClass = "org.apache.catalina.startup.ContextConfig";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  68 */   private String contextClass = "org.apache.catalina.core.StandardContext";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  74 */   private String directoryName = "public_html";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  80 */   private String homeBase = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  86 */   private Host host = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  93 */   private static final StringManager sm = StringManager.getManager("org.apache.catalina.startup");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  99 */   private String userClass = "org.apache.catalina.startup.PasswdUserDatabase";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 105 */   Pattern allow = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 110 */   Pattern deny = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getConfigClass()
/*     */   {
/* 120 */     return this.configClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setConfigClass(String configClass)
/*     */   {
/* 132 */     this.configClass = configClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContextClass()
/*     */   {
/* 142 */     return this.contextClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContextClass(String contextClass)
/*     */   {
/* 154 */     this.contextClass = contextClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDirectoryName()
/*     */   {
/* 164 */     return this.directoryName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDirectoryName(String directoryName)
/*     */   {
/* 176 */     this.directoryName = directoryName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getHomeBase()
/*     */   {
/* 186 */     return this.homeBase;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHomeBase(String homeBase)
/*     */   {
/* 198 */     this.homeBase = homeBase;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUserClass()
/*     */   {
/* 208 */     return this.userClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUserClass(String userClass)
/*     */   {
/* 219 */     this.userClass = userClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getAllow()
/*     */   {
/* 227 */     if (this.allow == null) return null;
/* 228 */     return this.allow.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAllow(String allow)
/*     */   {
/* 238 */     if ((allow == null) || (allow.length() == 0)) {
/* 239 */       this.allow = null;
/*     */     } else {
/* 241 */       this.allow = Pattern.compile(allow);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDeny()
/*     */   {
/* 250 */     if (this.deny == null) return null;
/* 251 */     return this.deny.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDeny(String deny)
/*     */   {
/* 261 */     if ((deny == null) || (deny.length() == 0)) {
/* 262 */       this.deny = null;
/*     */     } else {
/* 264 */       this.deny = Pattern.compile(deny);
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
/*     */ 
/*     */   public void lifecycleEvent(LifecycleEvent event)
/*     */   {
/*     */     try
/*     */     {
/* 281 */       this.host = ((Host)event.getLifecycle());
/*     */     } catch (ClassCastException e) {
/* 283 */       log.error(sm.getString("hostConfig.cce", new Object[] { event.getLifecycle() }), e);
/* 284 */       return;
/*     */     }
/*     */     
/*     */ 
/* 288 */     if (event.getType().equals("start")) {
/* 289 */       start();
/* 290 */     } else if (event.getType().equals("stop")) {
/* 291 */       stop();
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
/*     */   private void deploy()
/*     */   {
/* 305 */     if (this.host.getLogger().isDebugEnabled()) {
/* 306 */       this.host.getLogger().debug(sm.getString("userConfig.deploying"));
/*     */     }
/*     */     
/* 309 */     UserDatabase database = null;
/*     */     try {
/* 311 */       Class<?> clazz = Class.forName(this.userClass);
/* 312 */       database = (UserDatabase)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/* 313 */       database.setUserConfig(this);
/*     */     } catch (Exception e) {
/* 315 */       this.host.getLogger().error(sm.getString("userConfig.database"), e);
/* 316 */       return;
/*     */     }
/*     */     
/* 319 */     ExecutorService executor = this.host.getStartStopExecutor();
/* 320 */     List<Future<?>> results = new ArrayList();
/*     */     
/*     */ 
/* 323 */     Enumeration<String> users = database.getUsers();
/* 324 */     String user; while (users.hasMoreElements()) {
/* 325 */       user = (String)users.nextElement();
/* 326 */       if (isDeployAllowed(user)) {
/* 327 */         String home = database.getHome(user);
/* 328 */         results.add(executor.submit(new DeployUserDirectory(this, user, home)));
/*     */       }
/*     */     }
/* 331 */     for (Future<?> result : results) {
/*     */       try {
/* 333 */         result.get();
/*     */       } catch (Exception e) {
/* 335 */         this.host.getLogger().error(sm.getString("userConfig.deploy.threaded.error"), e);
/*     */       }
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
/*     */ 
/*     */   private void deploy(String user, String home)
/*     */   {
/* 351 */     String contextPath = "/~" + user;
/* 352 */     if (this.host.findChild(contextPath) != null)
/* 353 */       return;
/* 354 */     File app = new File(home, this.directoryName);
/* 355 */     if ((!app.exists()) || (!app.isDirectory())) {
/* 356 */       return;
/*     */     }
/* 358 */     this.host.getLogger().info(sm.getString("userConfig.deploy", new Object[] { user }));
/*     */     
/*     */     try
/*     */     {
/* 362 */       Class<?> clazz = Class.forName(this.contextClass);
/* 363 */       Context context = (Context)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/* 364 */       context.setPath(contextPath);
/* 365 */       context.setDocBase(app.toString());
/* 366 */       clazz = Class.forName(this.configClass);
/* 367 */       LifecycleListener listener = (LifecycleListener)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/* 368 */       context.addLifecycleListener(listener);
/* 369 */       this.host.addChild(context);
/*     */     } catch (Exception e) {
/* 371 */       this.host.getLogger().error(sm.getString("userConfig.error", new Object[] { user }), e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void start()
/*     */   {
/* 382 */     if (this.host.getLogger().isDebugEnabled()) {
/* 383 */       this.host.getLogger().debug(sm.getString("userConfig.start"));
/*     */     }
/* 385 */     deploy();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void stop()
/*     */   {
/* 395 */     if (this.host.getLogger().isDebugEnabled()) {
/* 396 */       this.host.getLogger().debug(sm.getString("userConfig.stop"));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isDeployAllowed(String user)
/*     */   {
/* 407 */     if ((this.deny != null) && (this.deny.matcher(user).matches())) {
/* 408 */       return false;
/*     */     }
/* 410 */     if (this.allow != null) {
/* 411 */       if (this.allow.matcher(user).matches()) {
/* 412 */         return true;
/*     */       }
/* 414 */       return false;
/*     */     }
/*     */     
/* 417 */     return true;
/*     */   }
/*     */   
/*     */   private static class DeployUserDirectory implements Runnable
/*     */   {
/*     */     private UserConfig config;
/*     */     private String user;
/*     */     private String home;
/*     */     
/*     */     public DeployUserDirectory(UserConfig config, String user, String home) {
/* 427 */       this.config = config;
/* 428 */       this.user = user;
/* 429 */       this.home = home;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 434 */       this.config.deploy(this.user, this.home);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\UserConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */