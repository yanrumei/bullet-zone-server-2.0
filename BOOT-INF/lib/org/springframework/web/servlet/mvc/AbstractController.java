/*     */ package org.springframework.web.servlet.mvc;
/*     */ 
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ import org.springframework.web.servlet.support.WebContentGenerator;
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
/*     */ public abstract class AbstractController
/*     */   extends WebContentGenerator
/*     */   implements Controller
/*     */ {
/*  96 */   private boolean synchronizeOnSession = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractController()
/*     */   {
/* 104 */     this(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractController(boolean restrictDefaultSupportedMethods)
/*     */   {
/* 115 */     super(restrictDefaultSupportedMethods);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setSynchronizeOnSession(boolean synchronizeOnSession)
/*     */   {
/* 139 */     this.synchronizeOnSession = synchronizeOnSession;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final boolean isSynchronizeOnSession()
/*     */   {
/* 146 */     return this.synchronizeOnSession;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/*     */     // Byte code:
/*     */     //   0: getstatic 4	org/springframework/http/HttpMethod:OPTIONS	Lorg/springframework/http/HttpMethod;
/*     */     //   3: aload_1
/*     */     //   4: invokeinterface 5 1 0
/*     */     //   9: invokevirtual 6	org/springframework/http/HttpMethod:matches	(Ljava/lang/String;)Z
/*     */     //   12: ifeq +17 -> 29
/*     */     //   15: aload_2
/*     */     //   16: ldc 7
/*     */     //   18: aload_0
/*     */     //   19: invokevirtual 8	org/springframework/web/servlet/mvc/AbstractController:getAllowHeader	()Ljava/lang/String;
/*     */     //   22: invokeinterface 9 3 0
/*     */     //   27: aconst_null
/*     */     //   28: areturn
/*     */     //   29: aload_0
/*     */     //   30: aload_1
/*     */     //   31: invokevirtual 10	org/springframework/web/servlet/mvc/AbstractController:checkRequest	(Ljavax/servlet/http/HttpServletRequest;)V
/*     */     //   34: aload_0
/*     */     //   35: aload_2
/*     */     //   36: invokevirtual 11	org/springframework/web/servlet/mvc/AbstractController:prepareResponse	(Ljavax/servlet/http/HttpServletResponse;)V
/*     */     //   39: aload_0
/*     */     //   40: getfield 3	org/springframework/web/servlet/mvc/AbstractController:synchronizeOnSession	Z
/*     */     //   43: ifeq +45 -> 88
/*     */     //   46: aload_1
/*     */     //   47: iconst_0
/*     */     //   48: invokeinterface 12 2 0
/*     */     //   53: astore_3
/*     */     //   54: aload_3
/*     */     //   55: ifnull +33 -> 88
/*     */     //   58: aload_3
/*     */     //   59: invokestatic 13	org/springframework/web/util/WebUtils:getSessionMutex	(Ljavax/servlet/http/HttpSession;)Ljava/lang/Object;
/*     */     //   62: astore 4
/*     */     //   64: aload 4
/*     */     //   66: dup
/*     */     //   67: astore 5
/*     */     //   69: monitorenter
/*     */     //   70: aload_0
/*     */     //   71: aload_1
/*     */     //   72: aload_2
/*     */     //   73: invokevirtual 14	org/springframework/web/servlet/mvc/AbstractController:handleRequestInternal	(Ljavax/servlet/http/HttpServletRequest;Ljavax/servlet/http/HttpServletResponse;)Lorg/springframework/web/servlet/ModelAndView;
/*     */     //   76: aload 5
/*     */     //   78: monitorexit
/*     */     //   79: areturn
/*     */     //   80: astore 6
/*     */     //   82: aload 5
/*     */     //   84: monitorexit
/*     */     //   85: aload 6
/*     */     //   87: athrow
/*     */     //   88: aload_0
/*     */     //   89: aload_1
/*     */     //   90: aload_2
/*     */     //   91: invokevirtual 14	org/springframework/web/servlet/mvc/AbstractController:handleRequestInternal	(Ljavax/servlet/http/HttpServletRequest;Ljavax/servlet/http/HttpServletResponse;)Lorg/springframework/web/servlet/ModelAndView;
/*     */     //   94: areturn
/*     */     // Line number table:
/*     */     //   Java source line #154	-> byte code offset #0
/*     */     //   Java source line #155	-> byte code offset #15
/*     */     //   Java source line #156	-> byte code offset #27
/*     */     //   Java source line #160	-> byte code offset #29
/*     */     //   Java source line #161	-> byte code offset #34
/*     */     //   Java source line #164	-> byte code offset #39
/*     */     //   Java source line #165	-> byte code offset #46
/*     */     //   Java source line #166	-> byte code offset #54
/*     */     //   Java source line #167	-> byte code offset #58
/*     */     //   Java source line #168	-> byte code offset #64
/*     */     //   Java source line #169	-> byte code offset #70
/*     */     //   Java source line #170	-> byte code offset #80
/*     */     //   Java source line #174	-> byte code offset #88
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	95	0	this	AbstractController
/*     */     //   0	95	1	request	HttpServletRequest
/*     */     //   0	95	2	response	HttpServletResponse
/*     */     //   53	6	3	session	javax.servlet.http.HttpSession
/*     */     //   62	3	4	mutex	Object
/*     */     //   67	16	5	Ljava/lang/Object;	Object
/*     */     //   80	6	6	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   70	79	80	finally
/*     */     //   80	85	80	finally
/*     */   }
/*     */   
/*     */   protected abstract ModelAndView handleRequestInternal(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
/*     */     throws Exception;
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\AbstractController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */