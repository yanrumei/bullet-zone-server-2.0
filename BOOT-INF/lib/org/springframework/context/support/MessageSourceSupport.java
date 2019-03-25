/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MessageSourceSupport
/*     */ {
/*  43 */   private static final MessageFormat INVALID_MESSAGE_FORMAT = new MessageFormat("");
/*     */   
/*     */ 
/*  46 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  48 */   private boolean alwaysUseMessageFormat = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  55 */   private final Map<String, Map<Locale, MessageFormat>> messageFormatsPerMessage = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAlwaysUseMessageFormat(boolean alwaysUseMessageFormat)
/*     */   {
/*  74 */     this.alwaysUseMessageFormat = alwaysUseMessageFormat;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isAlwaysUseMessageFormat()
/*     */   {
/*  82 */     return this.alwaysUseMessageFormat;
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
/*     */   protected String renderDefaultMessage(String defaultMessage, Object[] args, Locale locale)
/*     */   {
/* 101 */     return formatMessage(defaultMessage, args, locale);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected String formatMessage(String msg, Object[] args, Locale locale)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: ifnull +17 -> 18
/*     */     //   4: aload_0
/*     */     //   5: invokevirtual 10	org/springframework/context/support/MessageSourceSupport:isAlwaysUseMessageFormat	()Z
/*     */     //   8: ifne +12 -> 20
/*     */     //   11: aload_2
/*     */     //   12: invokestatic 11	org/springframework/util/ObjectUtils:isEmpty	([Ljava/lang/Object;)Z
/*     */     //   15: ifeq +5 -> 20
/*     */     //   18: aload_1
/*     */     //   19: areturn
/*     */     //   20: aconst_null
/*     */     //   21: astore 4
/*     */     //   23: aload_0
/*     */     //   24: getfield 8	org/springframework/context/support/MessageSourceSupport:messageFormatsPerMessage	Ljava/util/Map;
/*     */     //   27: dup
/*     */     //   28: astore 5
/*     */     //   30: monitorenter
/*     */     //   31: aload_0
/*     */     //   32: getfield 8	org/springframework/context/support/MessageSourceSupport:messageFormatsPerMessage	Ljava/util/Map;
/*     */     //   35: aload_1
/*     */     //   36: invokeinterface 12 2 0
/*     */     //   41: checkcast 13	java/util/Map
/*     */     //   44: astore 6
/*     */     //   46: aload 6
/*     */     //   48: ifnull +19 -> 67
/*     */     //   51: aload 6
/*     */     //   53: aload_3
/*     */     //   54: invokeinterface 12 2 0
/*     */     //   59: checkcast 14	java/text/MessageFormat
/*     */     //   62: astore 4
/*     */     //   64: goto +25 -> 89
/*     */     //   67: new 6	java/util/HashMap
/*     */     //   70: dup
/*     */     //   71: invokespecial 7	java/util/HashMap:<init>	()V
/*     */     //   74: astore 6
/*     */     //   76: aload_0
/*     */     //   77: getfield 8	org/springframework/context/support/MessageSourceSupport:messageFormatsPerMessage	Ljava/util/Map;
/*     */     //   80: aload_1
/*     */     //   81: aload 6
/*     */     //   83: invokeinterface 15 3 0
/*     */     //   88: pop
/*     */     //   89: aload 4
/*     */     //   91: ifnonnull +42 -> 133
/*     */     //   94: aload_0
/*     */     //   95: aload_1
/*     */     //   96: aload_3
/*     */     //   97: invokevirtual 16	org/springframework/context/support/MessageSourceSupport:createMessageFormat	(Ljava/lang/String;Ljava/util/Locale;)Ljava/text/MessageFormat;
/*     */     //   100: astore 4
/*     */     //   102: goto +20 -> 122
/*     */     //   105: astore 7
/*     */     //   107: aload_0
/*     */     //   108: invokevirtual 10	org/springframework/context/support/MessageSourceSupport:isAlwaysUseMessageFormat	()Z
/*     */     //   111: ifeq +6 -> 117
/*     */     //   114: aload 7
/*     */     //   116: athrow
/*     */     //   117: getstatic 18	org/springframework/context/support/MessageSourceSupport:INVALID_MESSAGE_FORMAT	Ljava/text/MessageFormat;
/*     */     //   120: astore 4
/*     */     //   122: aload 6
/*     */     //   124: aload_3
/*     */     //   125: aload 4
/*     */     //   127: invokeinterface 15 3 0
/*     */     //   132: pop
/*     */     //   133: aload 5
/*     */     //   135: monitorexit
/*     */     //   136: goto +11 -> 147
/*     */     //   139: astore 8
/*     */     //   141: aload 5
/*     */     //   143: monitorexit
/*     */     //   144: aload 8
/*     */     //   146: athrow
/*     */     //   147: aload 4
/*     */     //   149: getstatic 18	org/springframework/context/support/MessageSourceSupport:INVALID_MESSAGE_FORMAT	Ljava/text/MessageFormat;
/*     */     //   152: if_acmpne +5 -> 157
/*     */     //   155: aload_1
/*     */     //   156: areturn
/*     */     //   157: aload 4
/*     */     //   159: dup
/*     */     //   160: astore 5
/*     */     //   162: monitorenter
/*     */     //   163: aload 4
/*     */     //   165: aload_0
/*     */     //   166: aload_2
/*     */     //   167: aload_3
/*     */     //   168: invokevirtual 19	org/springframework/context/support/MessageSourceSupport:resolveArguments	([Ljava/lang/Object;Ljava/util/Locale;)[Ljava/lang/Object;
/*     */     //   171: invokevirtual 20	java/text/MessageFormat:format	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   174: aload 5
/*     */     //   176: monitorexit
/*     */     //   177: areturn
/*     */     //   178: astore 9
/*     */     //   180: aload 5
/*     */     //   182: monitorexit
/*     */     //   183: aload 9
/*     */     //   185: athrow
/*     */     // Line number table:
/*     */     //   Java source line #115	-> byte code offset #0
/*     */     //   Java source line #116	-> byte code offset #18
/*     */     //   Java source line #118	-> byte code offset #20
/*     */     //   Java source line #119	-> byte code offset #23
/*     */     //   Java source line #120	-> byte code offset #31
/*     */     //   Java source line #121	-> byte code offset #46
/*     */     //   Java source line #122	-> byte code offset #51
/*     */     //   Java source line #125	-> byte code offset #67
/*     */     //   Java source line #126	-> byte code offset #76
/*     */     //   Java source line #128	-> byte code offset #89
/*     */     //   Java source line #130	-> byte code offset #94
/*     */     //   Java source line #140	-> byte code offset #102
/*     */     //   Java source line #132	-> byte code offset #105
/*     */     //   Java source line #135	-> byte code offset #107
/*     */     //   Java source line #136	-> byte code offset #114
/*     */     //   Java source line #139	-> byte code offset #117
/*     */     //   Java source line #141	-> byte code offset #122
/*     */     //   Java source line #143	-> byte code offset #133
/*     */     //   Java source line #144	-> byte code offset #147
/*     */     //   Java source line #145	-> byte code offset #155
/*     */     //   Java source line #147	-> byte code offset #157
/*     */     //   Java source line #148	-> byte code offset #163
/*     */     //   Java source line #149	-> byte code offset #178
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	186	0	this	MessageSourceSupport
/*     */     //   0	186	1	msg	String
/*     */     //   0	186	2	args	Object[]
/*     */     //   0	186	3	locale	Locale
/*     */     //   21	143	4	messageFormat	MessageFormat
/*     */     //   28	114	5	Ljava/lang/Object;	Object
/*     */     //   160	21	5	Ljava/lang/Object;	Object
/*     */     //   44	79	6	messageFormatsPerLocale	Map<Locale, MessageFormat>
/*     */     //   105	10	7	ex	IllegalArgumentException
/*     */     //   139	6	8	localObject1	Object
/*     */     //   178	6	9	localObject2	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   94	102	105	java/lang/IllegalArgumentException
/*     */     //   31	136	139	finally
/*     */     //   139	144	139	finally
/*     */     //   163	177	178	finally
/*     */     //   178	183	178	finally
/*     */   }
/*     */   
/*     */   protected MessageFormat createMessageFormat(String msg, Locale locale)
/*     */   {
/* 159 */     return new MessageFormat(msg != null ? msg : "", locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object[] resolveArguments(Object[] args, Locale locale)
/*     */   {
/* 171 */     return args;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\support\MessageSourceSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */