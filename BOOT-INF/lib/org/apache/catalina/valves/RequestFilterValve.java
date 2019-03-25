/*     */ package org.apache.catalina.valves;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.servlet.ServletException;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.Valve;
/*     */ import org.apache.catalina.connector.Response;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
/*     */ import org.apache.tomcat.util.http.MimeHeaders;
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
/*     */ public abstract class RequestFilterValve
/*     */   extends ValveBase
/*     */ {
/*     */   public RequestFilterValve()
/*     */   {
/*  71 */     super(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  80 */   protected volatile Pattern allow = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  87 */   protected volatile String allowValue = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  96 */   protected volatile boolean allowValid = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 102 */   protected volatile Pattern deny = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 109 */   protected volatile String denyValue = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 118 */   protected volatile boolean denyValid = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 125 */   protected int denyStatus = 403;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 133 */   private boolean invalidAuthenticationWhenDeny = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 140 */   private volatile boolean addConnectorPort = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getAllow()
/*     */   {
/* 151 */     return this.allowValue;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void setAllow(String allow)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: ifnull +10 -> 11
/*     */     //   4: aload_1
/*     */     //   5: invokevirtual 12	java/lang/String:length	()I
/*     */     //   8: ifne +21 -> 29
/*     */     //   11: aload_0
/*     */     //   12: aconst_null
/*     */     //   13: putfield 2	org/apache/catalina/valves/RequestFilterValve:allow	Ljava/util/regex/Pattern;
/*     */     //   16: aload_0
/*     */     //   17: aconst_null
/*     */     //   18: putfield 3	org/apache/catalina/valves/RequestFilterValve:allowValue	Ljava/lang/String;
/*     */     //   21: aload_0
/*     */     //   22: iconst_1
/*     */     //   23: putfield 4	org/apache/catalina/valves/RequestFilterValve:allowValid	Z
/*     */     //   26: goto +36 -> 62
/*     */     //   29: iconst_0
/*     */     //   30: istore_2
/*     */     //   31: aload_0
/*     */     //   32: aload_1
/*     */     //   33: putfield 3	org/apache/catalina/valves/RequestFilterValve:allowValue	Ljava/lang/String;
/*     */     //   36: aload_0
/*     */     //   37: aload_1
/*     */     //   38: invokestatic 13	java/util/regex/Pattern:compile	(Ljava/lang/String;)Ljava/util/regex/Pattern;
/*     */     //   41: putfield 2	org/apache/catalina/valves/RequestFilterValve:allow	Ljava/util/regex/Pattern;
/*     */     //   44: iconst_1
/*     */     //   45: istore_2
/*     */     //   46: aload_0
/*     */     //   47: iload_2
/*     */     //   48: putfield 4	org/apache/catalina/valves/RequestFilterValve:allowValid	Z
/*     */     //   51: goto +11 -> 62
/*     */     //   54: astore_3
/*     */     //   55: aload_0
/*     */     //   56: iload_2
/*     */     //   57: putfield 4	org/apache/catalina/valves/RequestFilterValve:allowValid	Z
/*     */     //   60: aload_3
/*     */     //   61: athrow
/*     */     //   62: return
/*     */     // Line number table:
/*     */     //   Java source line #162	-> byte code offset #0
/*     */     //   Java source line #163	-> byte code offset #11
/*     */     //   Java source line #164	-> byte code offset #16
/*     */     //   Java source line #165	-> byte code offset #21
/*     */     //   Java source line #167	-> byte code offset #29
/*     */     //   Java source line #169	-> byte code offset #31
/*     */     //   Java source line #170	-> byte code offset #36
/*     */     //   Java source line #171	-> byte code offset #44
/*     */     //   Java source line #173	-> byte code offset #46
/*     */     //   Java source line #174	-> byte code offset #51
/*     */     //   Java source line #173	-> byte code offset #54
/*     */     //   Java source line #176	-> byte code offset #62
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	63	0	this	RequestFilterValve
/*     */     //   0	63	1	allow	String
/*     */     //   30	27	2	success	boolean
/*     */     //   54	7	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   31	46	54	finally
/*     */   }
/*     */   
/*     */   public String getDeny()
/*     */   {
/* 185 */     return this.denyValue;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void setDeny(String deny)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: ifnull +10 -> 11
/*     */     //   4: aload_1
/*     */     //   5: invokevirtual 12	java/lang/String:length	()I
/*     */     //   8: ifne +21 -> 29
/*     */     //   11: aload_0
/*     */     //   12: aconst_null
/*     */     //   13: putfield 5	org/apache/catalina/valves/RequestFilterValve:deny	Ljava/util/regex/Pattern;
/*     */     //   16: aload_0
/*     */     //   17: aconst_null
/*     */     //   18: putfield 6	org/apache/catalina/valves/RequestFilterValve:denyValue	Ljava/lang/String;
/*     */     //   21: aload_0
/*     */     //   22: iconst_1
/*     */     //   23: putfield 7	org/apache/catalina/valves/RequestFilterValve:denyValid	Z
/*     */     //   26: goto +36 -> 62
/*     */     //   29: iconst_0
/*     */     //   30: istore_2
/*     */     //   31: aload_0
/*     */     //   32: aload_1
/*     */     //   33: putfield 6	org/apache/catalina/valves/RequestFilterValve:denyValue	Ljava/lang/String;
/*     */     //   36: aload_0
/*     */     //   37: aload_1
/*     */     //   38: invokestatic 13	java/util/regex/Pattern:compile	(Ljava/lang/String;)Ljava/util/regex/Pattern;
/*     */     //   41: putfield 5	org/apache/catalina/valves/RequestFilterValve:deny	Ljava/util/regex/Pattern;
/*     */     //   44: iconst_1
/*     */     //   45: istore_2
/*     */     //   46: aload_0
/*     */     //   47: iload_2
/*     */     //   48: putfield 7	org/apache/catalina/valves/RequestFilterValve:denyValid	Z
/*     */     //   51: goto +11 -> 62
/*     */     //   54: astore_3
/*     */     //   55: aload_0
/*     */     //   56: iload_2
/*     */     //   57: putfield 7	org/apache/catalina/valves/RequestFilterValve:denyValid	Z
/*     */     //   60: aload_3
/*     */     //   61: athrow
/*     */     //   62: return
/*     */     // Line number table:
/*     */     //   Java source line #196	-> byte code offset #0
/*     */     //   Java source line #197	-> byte code offset #11
/*     */     //   Java source line #198	-> byte code offset #16
/*     */     //   Java source line #199	-> byte code offset #21
/*     */     //   Java source line #201	-> byte code offset #29
/*     */     //   Java source line #203	-> byte code offset #31
/*     */     //   Java source line #204	-> byte code offset #36
/*     */     //   Java source line #205	-> byte code offset #44
/*     */     //   Java source line #207	-> byte code offset #46
/*     */     //   Java source line #208	-> byte code offset #51
/*     */     //   Java source line #207	-> byte code offset #54
/*     */     //   Java source line #210	-> byte code offset #62
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	63	0	this	RequestFilterValve
/*     */     //   0	63	1	deny	String
/*     */     //   30	27	2	success	boolean
/*     */     //   54	7	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   31	46	54	finally
/*     */   }
/*     */   
/*     */   public final boolean isAllowValid()
/*     */   {
/* 220 */     return this.allowValid;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isDenyValid()
/*     */   {
/* 231 */     return this.denyValid;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getDenyStatus()
/*     */   {
/* 239 */     return this.denyStatus;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDenyStatus(int denyStatus)
/*     */   {
/* 248 */     this.denyStatus = denyStatus;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getInvalidAuthenticationWhenDeny()
/*     */   {
/* 256 */     return this.invalidAuthenticationWhenDeny;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInvalidAuthenticationWhenDeny(boolean value)
/*     */   {
/* 265 */     this.invalidAuthenticationWhenDeny = value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getAddConnectorPort()
/*     */   {
/* 276 */     return this.addConnectorPort;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAddConnectorPort(boolean addConnectorPort)
/*     */   {
/* 288 */     this.addConnectorPort = addConnectorPort;
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
/*     */   public abstract void invoke(org.apache.catalina.connector.Request paramRequest, Response paramResponse)
/*     */     throws IOException, ServletException;
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
/*     */   protected void initInternal()
/*     */     throws LifecycleException
/*     */   {
/* 315 */     super.initInternal();
/* 316 */     if ((!this.allowValid) || (!this.denyValid))
/*     */     {
/* 318 */       throw new LifecycleException(sm.getString("requestFilterValve.configInvalid"));
/*     */     }
/*     */   }
/*     */   
/*     */   protected synchronized void startInternal()
/*     */     throws LifecycleException
/*     */   {
/* 325 */     if ((!this.allowValid) || (!this.denyValid))
/*     */     {
/* 327 */       throw new LifecycleException(sm.getString("requestFilterValve.configInvalid"));
/*     */     }
/* 329 */     super.startInternal();
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
/*     */   protected void process(String property, org.apache.catalina.connector.Request request, Response response)
/*     */     throws IOException, ServletException
/*     */   {
/* 347 */     if (isAllowed(property)) {
/* 348 */       getNext().invoke(request, response);
/* 349 */       return;
/*     */     }
/*     */     
/* 352 */     if (getLog().isDebugEnabled()) {
/* 353 */       getLog().debug(sm.getString("requestFilterValve.deny", new Object[] {request
/* 354 */         .getRequestURI(), property }));
/*     */     }
/*     */     
/*     */ 
/* 358 */     denyRequest(request, response);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract Log getLog();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void denyRequest(org.apache.catalina.connector.Request request, Response response)
/*     */     throws IOException, ServletException
/*     */   {
/* 378 */     if (this.invalidAuthenticationWhenDeny) {
/* 379 */       Context context = request.getContext();
/* 380 */       if ((context != null) && (context.getPreemptiveAuthentication())) {
/* 381 */         if (request.getCoyoteRequest().getMimeHeaders().getValue("authorization") == null) {
/* 382 */           request.getCoyoteRequest().getMimeHeaders().addValue("authorization").setString("invalid");
/*     */         }
/* 384 */         getNext().invoke(request, response);
/* 385 */         return;
/*     */       }
/*     */     }
/* 388 */     response.sendError(this.denyStatus);
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
/*     */   public boolean isAllowed(String property)
/*     */   {
/* 403 */     Pattern deny = this.deny;
/* 404 */     Pattern allow = this.allow;
/*     */     
/*     */ 
/* 407 */     if ((deny != null) && (deny.matcher(property).matches())) {
/* 408 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 412 */     if ((allow != null) && (allow.matcher(property).matches())) {
/* 413 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 417 */     if ((deny != null) && (allow == null)) {
/* 418 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 422 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\valves\RequestFilterValve.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */