/*     */ package org.apache.catalina.util;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.SessionIdGenerator;
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
/*     */ public abstract class SessionIdGeneratorBase
/*     */   extends LifecycleBase
/*     */   implements SessionIdGenerator
/*     */ {
/*  35 */   private static final Log log = LogFactory.getLog(SessionIdGeneratorBase.class);
/*     */   
/*     */ 
/*     */ 
/*  39 */   private static final StringManager sm = StringManager.getManager("org.apache.catalina.util");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  49 */   private final Queue<SecureRandom> randoms = new ConcurrentLinkedQueue();
/*     */   
/*  51 */   private String secureRandomClass = null;
/*     */   
/*  53 */   private String secureRandomAlgorithm = "SHA1PRNG";
/*     */   
/*  55 */   private String secureRandomProvider = null;
/*     */   
/*     */ 
/*     */ 
/*  59 */   private String jvmRoute = "";
/*     */   
/*     */ 
/*     */ 
/*  63 */   private int sessionIdLength = 16;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSecureRandomClass()
/*     */   {
/*  74 */     return this.secureRandomClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSecureRandomClass(String secureRandomClass)
/*     */   {
/*  86 */     this.secureRandomClass = secureRandomClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSecureRandomAlgorithm()
/*     */   {
/*  98 */     return this.secureRandomAlgorithm;
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
/*     */   public void setSecureRandomAlgorithm(String secureRandomAlgorithm)
/*     */   {
/* 115 */     this.secureRandomAlgorithm = secureRandomAlgorithm;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSecureRandomProvider()
/*     */   {
/* 127 */     return this.secureRandomProvider;
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
/*     */   public void setSecureRandomProvider(String secureRandomProvider)
/*     */   {
/* 144 */     this.secureRandomProvider = secureRandomProvider;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getJvmRoute()
/*     */   {
/* 154 */     return this.jvmRoute;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setJvmRoute(String jvmRoute)
/*     */   {
/* 166 */     this.jvmRoute = jvmRoute;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSessionIdLength()
/*     */   {
/* 175 */     return this.sessionIdLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSessionIdLength(int sessionIdLength)
/*     */   {
/* 186 */     this.sessionIdLength = sessionIdLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String generateSessionId()
/*     */   {
/* 195 */     return generateSessionId(this.jvmRoute);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void getRandomBytes(byte[] bytes)
/*     */   {
/* 201 */     SecureRandom random = (SecureRandom)this.randoms.poll();
/* 202 */     if (random == null) {
/* 203 */       random = createSecureRandom();
/*     */     }
/* 205 */     random.nextBytes(bytes);
/* 206 */     this.randoms.add(random);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private SecureRandom createSecureRandom()
/*     */   {
/* 216 */     SecureRandom result = null;
/*     */     
/* 218 */     long t1 = System.currentTimeMillis();
/* 219 */     if (this.secureRandomClass != null) {
/*     */       try
/*     */       {
/* 222 */         Class<?> clazz = Class.forName(this.secureRandomClass);
/* 223 */         result = (SecureRandom)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/*     */       } catch (Exception e) {
/* 225 */         log.error(sm.getString("sessionIdGeneratorBase.random", new Object[] { this.secureRandomClass }), e);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 230 */     boolean error = false;
/* 231 */     if (result == null) {
/*     */       try
/*     */       {
/* 234 */         if ((this.secureRandomProvider != null) && 
/* 235 */           (this.secureRandomProvider.length() > 0)) {
/* 236 */           result = SecureRandom.getInstance(this.secureRandomAlgorithm, this.secureRandomProvider);
/*     */         }
/* 238 */         else if ((this.secureRandomAlgorithm != null) && 
/* 239 */           (this.secureRandomAlgorithm.length() > 0)) {
/* 240 */           result = SecureRandom.getInstance(this.secureRandomAlgorithm);
/*     */         }
/*     */       } catch (NoSuchAlgorithmException e) {
/* 243 */         error = true;
/* 244 */         log.error(sm.getString("sessionIdGeneratorBase.randomAlgorithm", new Object[] { this.secureRandomAlgorithm }), e);
/*     */       }
/*     */       catch (NoSuchProviderException e) {
/* 247 */         error = true;
/* 248 */         log.error(sm.getString("sessionIdGeneratorBase.randomProvider", new Object[] { this.secureRandomProvider }), e);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 253 */     if ((result == null) && (error)) {
/*     */       try
/*     */       {
/* 256 */         result = SecureRandom.getInstance("SHA1PRNG");
/*     */       } catch (NoSuchAlgorithmException e) {
/* 258 */         log.error(sm.getString("sessionIdGeneratorBase.randomAlgorithm", new Object[] { this.secureRandomAlgorithm }), e);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 263 */     if (result == null)
/*     */     {
/* 265 */       result = new SecureRandom();
/*     */     }
/*     */     
/*     */ 
/* 269 */     result.nextInt();
/*     */     
/* 271 */     long t2 = System.currentTimeMillis();
/* 272 */     if (t2 - t1 > 100L) {
/* 273 */       log.warn(sm.getString("sessionIdGeneratorBase.createRandom", new Object[] {result
/* 274 */         .getAlgorithm(), Long.valueOf(t2 - t1) }));
/*     */     }
/* 276 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void initInternal()
/*     */     throws LifecycleException
/*     */   {}
/*     */   
/*     */ 
/*     */   protected void startInternal()
/*     */     throws LifecycleException
/*     */   {
/* 289 */     generateSessionId();
/*     */     
/* 291 */     setState(LifecycleState.STARTING);
/*     */   }
/*     */   
/*     */   protected void stopInternal()
/*     */     throws LifecycleException
/*     */   {
/* 297 */     setState(LifecycleState.STOPPING);
/* 298 */     this.randoms.clear();
/*     */   }
/*     */   
/*     */   protected void destroyInternal()
/*     */     throws LifecycleException
/*     */   {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\util\SessionIdGeneratorBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */