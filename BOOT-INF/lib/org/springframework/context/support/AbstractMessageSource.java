/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Properties;
/*     */ import org.springframework.context.HierarchicalMessageSource;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.context.MessageSourceResolvable;
/*     */ import org.springframework.context.NoSuchMessageException;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractMessageSource
/*     */   extends MessageSourceSupport
/*     */   implements HierarchicalMessageSource
/*     */ {
/*     */   private MessageSource parentMessageSource;
/*     */   private Properties commonMessages;
/*  70 */   private boolean useCodeAsDefaultMessage = false;
/*     */   
/*     */ 
/*     */   public void setParentMessageSource(MessageSource parent)
/*     */   {
/*  75 */     this.parentMessageSource = parent;
/*     */   }
/*     */   
/*     */   public MessageSource getParentMessageSource()
/*     */   {
/*  80 */     return this.parentMessageSource;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCommonMessages(Properties commonMessages)
/*     */   {
/*  90 */     this.commonMessages = commonMessages;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Properties getCommonMessages()
/*     */   {
/*  97 */     return this.commonMessages;
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
/*     */   public void setUseCodeAsDefaultMessage(boolean useCodeAsDefaultMessage)
/*     */   {
/* 118 */     this.useCodeAsDefaultMessage = useCodeAsDefaultMessage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isUseCodeAsDefaultMessage()
/*     */   {
/* 130 */     return this.useCodeAsDefaultMessage;
/*     */   }
/*     */   
/*     */ 
/*     */   public final String getMessage(String code, Object[] args, String defaultMessage, Locale locale)
/*     */   {
/* 136 */     String msg = getMessageInternal(code, args, locale);
/* 137 */     if (msg != null) {
/* 138 */       return msg;
/*     */     }
/* 140 */     if (defaultMessage == null) {
/* 141 */       String fallback = getDefaultMessage(code);
/* 142 */       if (fallback != null) {
/* 143 */         return fallback;
/*     */       }
/*     */     }
/* 146 */     return renderDefaultMessage(defaultMessage, args, locale);
/*     */   }
/*     */   
/*     */   public final String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException
/*     */   {
/* 151 */     String msg = getMessageInternal(code, args, locale);
/* 152 */     if (msg != null) {
/* 153 */       return msg;
/*     */     }
/* 155 */     String fallback = getDefaultMessage(code);
/* 156 */     if (fallback != null) {
/* 157 */       return fallback;
/*     */     }
/* 159 */     throw new NoSuchMessageException(code, locale);
/*     */   }
/*     */   
/*     */   public final String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException
/*     */   {
/* 164 */     String[] codes = resolvable.getCodes();
/* 165 */     if (codes != null) {
/* 166 */       for (String code : codes) {
/* 167 */         String message = getMessageInternal(code, resolvable.getArguments(), locale);
/* 168 */         if (message != null) {
/* 169 */           return message;
/*     */         }
/*     */       }
/*     */     }
/* 173 */     String defaultMessage = getDefaultMessage(resolvable, locale);
/* 174 */     if (defaultMessage != null) {
/* 175 */       return defaultMessage;
/*     */     }
/* 177 */     throw new NoSuchMessageException(!ObjectUtils.isEmpty(codes) ? codes[(codes.length - 1)] : null, locale);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected String getMessageInternal(String code, Object[] args, Locale locale)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: ifnonnull +5 -> 6
/*     */     //   4: aconst_null
/*     */     //   5: areturn
/*     */     //   6: aload_3
/*     */     //   7: ifnonnull +7 -> 14
/*     */     //   10: invokestatic 14	java/util/Locale:getDefault	()Ljava/util/Locale;
/*     */     //   13: astore_3
/*     */     //   14: aload_2
/*     */     //   15: astore 4
/*     */     //   17: aload_0
/*     */     //   18: invokevirtual 15	org/springframework/context/support/AbstractMessageSource:isAlwaysUseMessageFormat	()Z
/*     */     //   21: ifne +29 -> 50
/*     */     //   24: aload_2
/*     */     //   25: invokestatic 13	org/springframework/util/ObjectUtils:isEmpty	([Ljava/lang/Object;)Z
/*     */     //   28: ifeq +22 -> 50
/*     */     //   31: aload_0
/*     */     //   32: aload_1
/*     */     //   33: aload_3
/*     */     //   34: invokevirtual 16	org/springframework/context/support/AbstractMessageSource:resolveCodeWithoutArguments	(Ljava/lang/String;Ljava/util/Locale;)Ljava/lang/String;
/*     */     //   37: astore 5
/*     */     //   39: aload 5
/*     */     //   41: ifnull +6 -> 47
/*     */     //   44: aload 5
/*     */     //   46: areturn
/*     */     //   47: goto +49 -> 96
/*     */     //   50: aload_0
/*     */     //   51: aload_2
/*     */     //   52: aload_3
/*     */     //   53: invokevirtual 17	org/springframework/context/support/AbstractMessageSource:resolveArguments	([Ljava/lang/Object;Ljava/util/Locale;)[Ljava/lang/Object;
/*     */     //   56: astore 4
/*     */     //   58: aload_0
/*     */     //   59: aload_1
/*     */     //   60: aload_3
/*     */     //   61: invokevirtual 18	org/springframework/context/support/AbstractMessageSource:resolveCode	(Ljava/lang/String;Ljava/util/Locale;)Ljava/text/MessageFormat;
/*     */     //   64: astore 5
/*     */     //   66: aload 5
/*     */     //   68: ifnull +28 -> 96
/*     */     //   71: aload 5
/*     */     //   73: dup
/*     */     //   74: astore 6
/*     */     //   76: monitorenter
/*     */     //   77: aload 5
/*     */     //   79: aload 4
/*     */     //   81: invokevirtual 19	java/text/MessageFormat:format	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   84: aload 6
/*     */     //   86: monitorexit
/*     */     //   87: areturn
/*     */     //   88: astore 7
/*     */     //   90: aload 6
/*     */     //   92: monitorexit
/*     */     //   93: aload 7
/*     */     //   95: athrow
/*     */     //   96: aload_0
/*     */     //   97: invokevirtual 20	org/springframework/context/support/AbstractMessageSource:getCommonMessages	()Ljava/util/Properties;
/*     */     //   100: astore 5
/*     */     //   102: aload 5
/*     */     //   104: ifnull +25 -> 129
/*     */     //   107: aload 5
/*     */     //   109: aload_1
/*     */     //   110: invokevirtual 21	java/util/Properties:getProperty	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   113: astore 6
/*     */     //   115: aload 6
/*     */     //   117: ifnull +12 -> 129
/*     */     //   120: aload_0
/*     */     //   121: aload 6
/*     */     //   123: aload_2
/*     */     //   124: aload_3
/*     */     //   125: invokevirtual 22	org/springframework/context/support/AbstractMessageSource:formatMessage	(Ljava/lang/String;[Ljava/lang/Object;Ljava/util/Locale;)Ljava/lang/String;
/*     */     //   128: areturn
/*     */     //   129: aload_0
/*     */     //   130: aload_1
/*     */     //   131: aload 4
/*     */     //   133: aload_3
/*     */     //   134: invokevirtual 23	org/springframework/context/support/AbstractMessageSource:getMessageFromParent	(Ljava/lang/String;[Ljava/lang/Object;Ljava/util/Locale;)Ljava/lang/String;
/*     */     //   137: areturn
/*     */     // Line number table:
/*     */     //   Java source line #196	-> byte code offset #0
/*     */     //   Java source line #197	-> byte code offset #4
/*     */     //   Java source line #199	-> byte code offset #6
/*     */     //   Java source line #200	-> byte code offset #10
/*     */     //   Java source line #202	-> byte code offset #14
/*     */     //   Java source line #204	-> byte code offset #17
/*     */     //   Java source line #209	-> byte code offset #31
/*     */     //   Java source line #210	-> byte code offset #39
/*     */     //   Java source line #211	-> byte code offset #44
/*     */     //   Java source line #213	-> byte code offset #47
/*     */     //   Java source line #219	-> byte code offset #50
/*     */     //   Java source line #221	-> byte code offset #58
/*     */     //   Java source line #222	-> byte code offset #66
/*     */     //   Java source line #223	-> byte code offset #71
/*     */     //   Java source line #224	-> byte code offset #77
/*     */     //   Java source line #225	-> byte code offset #88
/*     */     //   Java source line #230	-> byte code offset #96
/*     */     //   Java source line #231	-> byte code offset #102
/*     */     //   Java source line #232	-> byte code offset #107
/*     */     //   Java source line #233	-> byte code offset #115
/*     */     //   Java source line #234	-> byte code offset #120
/*     */     //   Java source line #239	-> byte code offset #129
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	138	0	this	AbstractMessageSource
/*     */     //   0	138	1	code	String
/*     */     //   0	138	2	args	Object[]
/*     */     //   0	138	3	locale	Locale
/*     */     //   15	117	4	argsToUse	Object[]
/*     */     //   37	8	5	message	String
/*     */     //   64	14	5	messageFormat	MessageFormat
/*     */     //   100	8	5	commonMessages	Properties
/*     */     //   74	17	6	Ljava/lang/Object;	Object
/*     */     //   113	9	6	commonMessage	String
/*     */     //   88	6	7	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   77	87	88	finally
/*     */     //   88	93	88	finally
/*     */   }
/*     */   
/*     */   protected String getMessageFromParent(String code, Object[] args, Locale locale)
/*     */   {
/* 252 */     MessageSource parent = getParentMessageSource();
/* 253 */     if (parent != null) {
/* 254 */       if ((parent instanceof AbstractMessageSource))
/*     */       {
/*     */ 
/* 257 */         return ((AbstractMessageSource)parent).getMessageInternal(code, args, locale);
/*     */       }
/*     */       
/*     */ 
/* 261 */       return parent.getMessage(code, args, null, locale);
/*     */     }
/*     */     
/*     */ 
/* 265 */     return null;
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
/*     */   protected String getDefaultMessage(MessageSourceResolvable resolvable, Locale locale)
/*     */   {
/* 281 */     String defaultMessage = resolvable.getDefaultMessage();
/* 282 */     String[] codes = resolvable.getCodes();
/* 283 */     if (defaultMessage != null) {
/* 284 */       if ((!ObjectUtils.isEmpty(codes)) && (defaultMessage.equals(codes[0])))
/*     */       {
/* 286 */         return defaultMessage;
/*     */       }
/* 288 */       return renderDefaultMessage(defaultMessage, resolvable.getArguments(), locale);
/*     */     }
/* 290 */     return !ObjectUtils.isEmpty(codes) ? getDefaultMessage(codes[0]) : null;
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
/*     */   protected String getDefaultMessage(String code)
/*     */   {
/* 304 */     if (isUseCodeAsDefaultMessage()) {
/* 305 */       return code;
/*     */     }
/* 307 */     return null;
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
/*     */   protected Object[] resolveArguments(Object[] args, Locale locale)
/*     */   {
/* 321 */     if (args == null) {
/* 322 */       return new Object[0];
/*     */     }
/* 324 */     List<Object> resolvedArgs = new ArrayList(args.length);
/* 325 */     for (Object arg : args) {
/* 326 */       if ((arg instanceof MessageSourceResolvable)) {
/* 327 */         resolvedArgs.add(getMessage((MessageSourceResolvable)arg, locale));
/*     */       }
/*     */       else {
/* 330 */         resolvedArgs.add(arg);
/*     */       }
/*     */     }
/* 333 */     return resolvedArgs.toArray(new Object[resolvedArgs.size()]);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected String resolveCodeWithoutArguments(String code, Locale locale)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: aload_2
/*     */     //   3: invokevirtual 18	org/springframework/context/support/AbstractMessageSource:resolveCode	(Ljava/lang/String;Ljava/util/Locale;)Ljava/text/MessageFormat;
/*     */     //   6: astore_3
/*     */     //   7: aload_3
/*     */     //   8: ifnull +28 -> 36
/*     */     //   11: aload_3
/*     */     //   12: dup
/*     */     //   13: astore 4
/*     */     //   15: monitorenter
/*     */     //   16: aload_3
/*     */     //   17: iconst_0
/*     */     //   18: anewarray 30	java/lang/Object
/*     */     //   21: invokevirtual 19	java/text/MessageFormat:format	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   24: aload 4
/*     */     //   26: monitorexit
/*     */     //   27: areturn
/*     */     //   28: astore 5
/*     */     //   30: aload 4
/*     */     //   32: monitorexit
/*     */     //   33: aload 5
/*     */     //   35: athrow
/*     */     //   36: aconst_null
/*     */     //   37: areturn
/*     */     // Line number table:
/*     */     //   Java source line #354	-> byte code offset #0
/*     */     //   Java source line #355	-> byte code offset #7
/*     */     //   Java source line #356	-> byte code offset #11
/*     */     //   Java source line #357	-> byte code offset #16
/*     */     //   Java source line #358	-> byte code offset #28
/*     */     //   Java source line #360	-> byte code offset #36
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	38	0	this	AbstractMessageSource
/*     */     //   0	38	1	code	String
/*     */     //   0	38	2	locale	Locale
/*     */     //   6	11	3	messageFormat	MessageFormat
/*     */     //   13	18	4	Ljava/lang/Object;	Object
/*     */     //   28	6	5	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   16	27	28	finally
/*     */     //   28	33	28	finally
/*     */   }
/*     */   
/*     */   protected abstract MessageFormat resolveCode(String paramString, Locale paramLocale);
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\support\AbstractMessageSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */