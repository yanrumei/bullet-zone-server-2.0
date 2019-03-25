/*     */ package org.apache.catalina.core;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.catalina.LifecycleEvent;
/*     */ import org.apache.catalina.LifecycleListener;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.jni.Library;
/*     */ import org.apache.tomcat.jni.LibraryNotFoundError;
/*     */ import org.apache.tomcat.jni.SSL;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
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
/*     */ public class AprLifecycleListener
/*     */   implements LifecycleListener
/*     */ {
/*  48 */   private static final Log log = LogFactory.getLog(AprLifecycleListener.class);
/*  49 */   private static boolean instanceCreated = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  55 */   private static final List<String> initInfoLogMessages = new ArrayList(3);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  61 */   protected static final StringManager sm = StringManager.getManager("org.apache.catalina.core");
/*     */   
/*     */ 
/*     */   protected static final int TCN_REQUIRED_MAJOR = 1;
/*     */   
/*     */ 
/*     */   protected static final int TCN_REQUIRED_MINOR = 2;
/*     */   
/*     */   protected static final int TCN_REQUIRED_PATCH = 14;
/*     */   
/*     */   protected static final int TCN_RECOMMENDED_MINOR = 2;
/*     */   
/*     */   protected static final int TCN_RECOMMENDED_PV = 14;
/*     */   
/*  75 */   protected static String SSLEngine = "on";
/*  76 */   protected static String FIPSMode = "off";
/*  77 */   protected static String SSLRandomSeed = "builtin";
/*  78 */   protected static boolean sslInitialized = false;
/*  79 */   protected static boolean aprInitialized = false;
/*  80 */   protected static boolean aprAvailable = false;
/*  81 */   protected static boolean useAprConnector = false;
/*  82 */   protected static boolean useOpenSSL = true;
/*  83 */   protected static boolean fipsModeActive = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int FIPS_ON = 1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int FIPS_OFF = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 101 */   protected static final Object lock = new Object();
/*     */   
/*     */   public static boolean isAprAvailable()
/*     */   {
/* 105 */     if (instanceCreated) {
/* 106 */       synchronized (lock) {
/* 107 */         init();
/*     */       }
/*     */     }
/* 110 */     return aprAvailable;
/*     */   }
/*     */   
/*     */   public AprLifecycleListener() {
/* 114 */     instanceCreated = true;
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
/*     */   public void lifecycleEvent(LifecycleEvent event)
/*     */   {
/* 127 */     if ("before_init".equals(event.getType())) {
/* 128 */       synchronized (lock) {
/* 129 */         init();
/* 130 */         for (String msg : initInfoLogMessages) {
/* 131 */           log.info(msg);
/*     */         }
/* 133 */         initInfoLogMessages.clear();
/* 134 */         if (aprAvailable) {
/*     */           try {
/* 136 */             initializeSSL();
/*     */           } catch (Throwable t) {
/* 138 */             t = ExceptionUtils.unwrapInvocationTargetException(t);
/* 139 */             ExceptionUtils.handleThrowable(t);
/* 140 */             log.error(sm.getString("aprListener.sslInit"), t);
/*     */           }
/*     */         }
/*     */         
/* 144 */         if ((null != FIPSMode) && (!"off".equalsIgnoreCase(FIPSMode)) && (!isFIPSModeActive()))
/*     */         {
/* 146 */           Error e = new Error(sm.getString("aprListener.initializeFIPSFailed"));
/*     */           
/* 148 */           log.fatal(e.getMessage(), e);
/* 149 */           throw e;
/*     */         }
/*     */       }
/* 152 */     } else if ("after_destroy".equals(event.getType())) {
/* 153 */       synchronized (lock) {
/* 154 */         if (!aprAvailable) {
/* 155 */           return;
/*     */         }
/*     */         try {
/* 158 */           terminateAPR();
/*     */         } catch (Throwable t) {
/* 160 */           t = ExceptionUtils.unwrapInvocationTargetException(t);
/* 161 */           ExceptionUtils.handleThrowable(t);
/* 162 */           log.info(sm.getString("aprListener.aprDestroy"));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static void terminateAPR()
/*     */     throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
/*     */   {
/* 173 */     String methodName = "terminate";
/*     */     
/* 175 */     Method method = Class.forName("org.apache.tomcat.jni.Library").getMethod(methodName, (Class[])null);
/* 176 */     method.invoke(null, (Object[])null);
/* 177 */     aprAvailable = false;
/* 178 */     aprInitialized = false;
/* 179 */     sslInitialized = false;
/* 180 */     fipsModeActive = false;
/*     */   }
/*     */   
/*     */   private static void init()
/*     */   {
/* 185 */     int major = 0;
/* 186 */     int minor = 0;
/* 187 */     int patch = 0;
/* 188 */     int apver = 0;
/* 189 */     int rqver = 1214;
/* 190 */     int rcver = 1214;
/*     */     
/* 192 */     if (aprInitialized) {
/* 193 */       return;
/*     */     }
/* 195 */     aprInitialized = true;
/*     */     try
/*     */     {
/* 198 */       Library.initialize(null);
/* 199 */       major = Library.TCN_MAJOR_VERSION;
/* 200 */       minor = Library.TCN_MINOR_VERSION;
/* 201 */       patch = Library.TCN_PATCH_VERSION;
/* 202 */       apver = major * 1000 + minor * 100 + patch;
/*     */     }
/*     */     catch (LibraryNotFoundError lnfe) {
/* 205 */       if (log.isDebugEnabled()) {
/* 206 */         log.debug(sm.getString("aprListener.aprInitDebug", new Object[] {lnfe
/* 207 */           .getLibraryNames(), System.getProperty("java.library.path"), lnfe
/* 208 */           .getMessage() }), lnfe);
/*     */       }
/* 210 */       initInfoLogMessages.add(sm.getString("aprListener.aprInit", new Object[] {
/* 211 */         System.getProperty("java.library.path") }));
/* 212 */       return;
/*     */     }
/*     */     catch (Throwable t) {
/* 215 */       t = ExceptionUtils.unwrapInvocationTargetException(t);
/* 216 */       ExceptionUtils.handleThrowable(t);
/* 217 */       log.warn(sm.getString("aprListener.aprInitError", new Object[] { t.getMessage() }), t);
/* 218 */       return;
/*     */     }
/* 220 */     if (apver < rqver) {
/* 221 */       log.error(sm.getString("aprListener.tcnInvalid", new Object[] { major + "." + minor + "." + patch, "1.2.14" }));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       try
/*     */       {
/* 229 */         terminateAPR();
/*     */       } catch (Throwable t) {
/* 231 */         t = ExceptionUtils.unwrapInvocationTargetException(t);
/* 232 */         ExceptionUtils.handleThrowable(t);
/*     */       }
/* 234 */       return;
/*     */     }
/* 236 */     if (apver < rcver) {
/* 237 */       initInfoLogMessages.add(sm.getString("aprListener.tcnVersion", new Object[] { major + "." + minor + "." + patch, "1.2.14" }));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 244 */     initInfoLogMessages.add(sm.getString("aprListener.tcnValid", new Object[] { major + "." + minor + "." + patch, Library.APR_MAJOR_VERSION + "." + Library.APR_MINOR_VERSION + "." + Library.APR_PATCH_VERSION }));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 251 */     initInfoLogMessages.add(sm.getString("aprListener.flags", new Object[] {
/* 252 */       Boolean.valueOf(Library.APR_HAVE_IPV6), 
/* 253 */       Boolean.valueOf(Library.APR_HAS_SENDFILE), 
/* 254 */       Boolean.valueOf(Library.APR_HAS_SO_ACCEPTFILTER), 
/* 255 */       Boolean.valueOf(Library.APR_HAS_RANDOM) }));
/*     */     
/* 257 */     initInfoLogMessages.add(sm.getString("aprListener.config", new Object[] {
/* 258 */       Boolean.valueOf(useAprConnector), 
/* 259 */       Boolean.valueOf(useOpenSSL) }));
/*     */     
/* 261 */     aprAvailable = true;
/*     */   }
/*     */   
/*     */   private static void initializeSSL() throws Exception
/*     */   {
/* 266 */     if ("off".equalsIgnoreCase(SSLEngine)) {
/* 267 */       return;
/*     */     }
/* 269 */     if (sslInitialized)
/*     */     {
/* 271 */       return;
/*     */     }
/*     */     
/* 274 */     sslInitialized = true;
/*     */     
/* 276 */     String methodName = "randSet";
/* 277 */     Class<?>[] paramTypes = new Class[1];
/* 278 */     paramTypes[0] = String.class;
/* 279 */     Object[] paramValues = new Object[1];
/* 280 */     paramValues[0] = SSLRandomSeed;
/* 281 */     Class<?> clazz = Class.forName("org.apache.tomcat.jni.SSL");
/* 282 */     Method method = clazz.getMethod(methodName, paramTypes);
/* 283 */     method.invoke(null, paramValues);
/*     */     
/*     */ 
/* 286 */     methodName = "initialize";
/* 287 */     paramValues[0] = ("on".equalsIgnoreCase(SSLEngine) ? null : SSLEngine);
/* 288 */     method = clazz.getMethod(methodName, paramTypes);
/* 289 */     method.invoke(null, paramValues);
/*     */     
/* 291 */     if ((null != FIPSMode) && (!"off".equalsIgnoreCase(FIPSMode)))
/*     */     {
/* 293 */       fipsModeActive = false;
/*     */       
/*     */ 
/* 296 */       int fipsModeState = SSL.fipsModeGet();
/*     */       
/* 298 */       if (log.isDebugEnabled()) {
/* 299 */         log.debug(sm.getString("aprListener.currentFIPSMode", new Object[] {
/* 300 */           Integer.valueOf(fipsModeState) }));
/*     */       }
/*     */       boolean enterFipsMode;
/* 303 */       if ("on".equalsIgnoreCase(FIPSMode)) { boolean enterFipsMode;
/* 304 */         if (fipsModeState == 1) {
/* 305 */           log.info(sm.getString("aprListener.skipFIPSInitialization"));
/* 306 */           fipsModeActive = true;
/* 307 */           enterFipsMode = false;
/*     */         } else {
/* 309 */           enterFipsMode = true;
/*     */         }
/* 311 */       } else if ("require".equalsIgnoreCase(FIPSMode)) { boolean enterFipsMode;
/* 312 */         if (fipsModeState == 1) {
/* 313 */           fipsModeActive = true;
/* 314 */           enterFipsMode = false;
/*     */         }
/*     */         else {
/* 317 */           throw new IllegalStateException(sm.getString("aprListener.requireNotInFIPSMode"));
/*     */         }
/* 319 */       } else if ("enter".equalsIgnoreCase(FIPSMode)) { boolean enterFipsMode;
/* 320 */         if (fipsModeState == 0) {
/* 321 */           enterFipsMode = true;
/*     */         } else {
/* 323 */           throw new IllegalStateException(sm.getString("aprListener.enterAlreadyInFIPSMode", new Object[] {
/*     */           
/* 325 */             Integer.valueOf(fipsModeState) }));
/*     */         }
/*     */       } else {
/* 328 */         throw new IllegalArgumentException(sm.getString("aprListener.wrongFIPSMode", new Object[] { FIPSMode }));
/*     */       }
/*     */       
/*     */       boolean enterFipsMode;
/* 332 */       if (enterFipsMode) {
/* 333 */         log.info(sm.getString("aprListener.initializingFIPS"));
/*     */         
/* 335 */         fipsModeState = SSL.fipsModeSet(1);
/* 336 */         if (fipsModeState != 1)
/*     */         {
/*     */ 
/* 339 */           String message = sm.getString("aprListener.initializeFIPSFailed");
/* 340 */           log.error(message);
/* 341 */           throw new IllegalStateException(message);
/*     */         }
/*     */         
/* 344 */         fipsModeActive = true;
/* 345 */         log.info(sm.getString("aprListener.initializeFIPSSuccess"));
/*     */       }
/*     */     }
/*     */     
/* 349 */     log.info(sm.getString("aprListener.initializedOpenSSL", new Object[] { SSL.versionString() }));
/*     */   }
/*     */   
/*     */   public String getSSLEngine() {
/* 353 */     return SSLEngine;
/*     */   }
/*     */   
/*     */   public void setSSLEngine(String SSLEngine) {
/* 357 */     if (!SSLEngine.equals(SSLEngine))
/*     */     {
/* 359 */       if (sslInitialized)
/*     */       {
/* 361 */         throw new IllegalStateException(sm.getString("aprListener.tooLateForSSLEngine"));
/*     */       }
/*     */       
/* 364 */       SSLEngine = SSLEngine;
/*     */     }
/*     */   }
/*     */   
/*     */   public String getSSLRandomSeed() {
/* 369 */     return SSLRandomSeed;
/*     */   }
/*     */   
/*     */   public void setSSLRandomSeed(String SSLRandomSeed) {
/* 373 */     if (!SSLRandomSeed.equals(SSLRandomSeed))
/*     */     {
/* 375 */       if (sslInitialized)
/*     */       {
/* 377 */         throw new IllegalStateException(sm.getString("aprListener.tooLateForSSLRandomSeed"));
/*     */       }
/*     */       
/* 380 */       SSLRandomSeed = SSLRandomSeed;
/*     */     }
/*     */   }
/*     */   
/*     */   public String getFIPSMode() {
/* 385 */     return FIPSMode;
/*     */   }
/*     */   
/*     */   public void setFIPSMode(String FIPSMode) {
/* 389 */     if (!FIPSMode.equals(FIPSMode))
/*     */     {
/* 391 */       if (sslInitialized)
/*     */       {
/* 393 */         throw new IllegalStateException(sm.getString("aprListener.tooLateForFIPSMode"));
/*     */       }
/*     */       
/* 396 */       FIPSMode = FIPSMode;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isFIPSModeActive() {
/* 401 */     return fipsModeActive;
/*     */   }
/*     */   
/*     */   public void setUseAprConnector(boolean useAprConnector) {
/* 405 */     if (useAprConnector != useAprConnector) {
/* 406 */       useAprConnector = useAprConnector;
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean getUseAprConnector() {
/* 411 */     return useAprConnector;
/*     */   }
/*     */   
/*     */   public void setUseOpenSSL(boolean useOpenSSL) {
/* 415 */     if (useOpenSSL != useOpenSSL) {
/* 416 */       useOpenSSL = useOpenSSL;
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean getUseOpenSSL() {
/* 421 */     return useOpenSSL;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\AprLifecycleListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */