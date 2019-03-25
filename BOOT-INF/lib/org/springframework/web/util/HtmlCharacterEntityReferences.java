/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.util.Map;
/*     */ 
/*     */ class HtmlCharacterEntityReferences
/*     */ {
/*     */   private static final String PROPERTIES_FILE = "HtmlCharacterEntityReferences.properties";
/*     */   static final char REFERENCE_START = '&';
/*     */   static final String DECIMAL_REFERENCE_START = "&#";
/*     */   static final String HEX_REFERENCE_START = "&#x";
/*     */   static final char REFERENCE_END = ';';
/*     */   static final char CHAR_NULL = '￿';
/*     */   private final String[] characterToEntityReferenceMap;
/*     */   private final Map<String, Character> entityReferenceToCharacterMap;
/*     */   
/*     */   /* Error */
/*     */   public HtmlCharacterEntityReferences()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokespecial 1	java/lang/Object:<init>	()V
/*     */     //   4: aload_0
/*     */     //   5: sipush 3000
/*     */     //   8: anewarray 2	java/lang/String
/*     */     //   11: putfield 3	org/springframework/web/util/HtmlCharacterEntityReferences:characterToEntityReferenceMap	[Ljava/lang/String;
/*     */     //   14: aload_0
/*     */     //   15: new 4	java/util/HashMap
/*     */     //   18: dup
/*     */     //   19: sipush 252
/*     */     //   22: invokespecial 5	java/util/HashMap:<init>	(I)V
/*     */     //   25: putfield 6	org/springframework/web/util/HtmlCharacterEntityReferences:entityReferenceToCharacterMap	Ljava/util/Map;
/*     */     //   28: new 7	java/util/Properties
/*     */     //   31: dup
/*     */     //   32: invokespecial 8	java/util/Properties:<init>	()V
/*     */     //   35: astore_1
/*     */     //   36: ldc 9
/*     */     //   38: ldc 10
/*     */     //   40: invokevirtual 11	java/lang/Class:getResourceAsStream	(Ljava/lang/String;)Ljava/io/InputStream;
/*     */     //   43: astore_2
/*     */     //   44: aload_2
/*     */     //   45: ifnonnull +13 -> 58
/*     */     //   48: new 12	java/lang/IllegalStateException
/*     */     //   51: dup
/*     */     //   52: ldc 13
/*     */     //   54: invokespecial 14	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
/*     */     //   57: athrow
/*     */     //   58: aload_1
/*     */     //   59: aload_2
/*     */     //   60: invokevirtual 15	java/util/Properties:load	(Ljava/io/InputStream;)V
/*     */     //   63: aload_2
/*     */     //   64: invokevirtual 16	java/io/InputStream:close	()V
/*     */     //   67: goto +10 -> 77
/*     */     //   70: astore_3
/*     */     //   71: aload_2
/*     */     //   72: invokevirtual 16	java/io/InputStream:close	()V
/*     */     //   75: aload_3
/*     */     //   76: athrow
/*     */     //   77: goto +34 -> 111
/*     */     //   80: astore_3
/*     */     //   81: new 12	java/lang/IllegalStateException
/*     */     //   84: dup
/*     */     //   85: new 18	java/lang/StringBuilder
/*     */     //   88: dup
/*     */     //   89: invokespecial 19	java/lang/StringBuilder:<init>	()V
/*     */     //   92: ldc 20
/*     */     //   94: invokevirtual 21	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   97: aload_3
/*     */     //   98: invokevirtual 22	java/io/IOException:getMessage	()Ljava/lang/String;
/*     */     //   101: invokevirtual 21	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   104: invokevirtual 23	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   107: invokespecial 14	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
/*     */     //   110: athrow
/*     */     //   111: aload_1
/*     */     //   112: invokevirtual 24	java/util/Properties:propertyNames	()Ljava/util/Enumeration;
/*     */     //   115: astore_3
/*     */     //   116: aload_3
/*     */     //   117: invokeinterface 25 1 0
/*     */     //   122: ifeq +155 -> 277
/*     */     //   125: aload_3
/*     */     //   126: invokeinterface 26 1 0
/*     */     //   131: checkcast 2	java/lang/String
/*     */     //   134: astore 4
/*     */     //   136: aload 4
/*     */     //   138: invokestatic 27	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*     */     //   141: istore 5
/*     */     //   143: iload 5
/*     */     //   145: sipush 1000
/*     */     //   148: if_icmplt +19 -> 167
/*     */     //   151: iload 5
/*     */     //   153: sipush 8000
/*     */     //   156: if_icmplt +15 -> 171
/*     */     //   159: iload 5
/*     */     //   161: sipush 10000
/*     */     //   164: if_icmpge +7 -> 171
/*     */     //   167: iconst_1
/*     */     //   168: goto +4 -> 172
/*     */     //   171: iconst_0
/*     */     //   172: new 18	java/lang/StringBuilder
/*     */     //   175: dup
/*     */     //   176: invokespecial 19	java/lang/StringBuilder:<init>	()V
/*     */     //   179: ldc 28
/*     */     //   181: invokevirtual 21	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   184: iload 5
/*     */     //   186: invokevirtual 29	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*     */     //   189: invokevirtual 23	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   192: invokestatic 30	org/springframework/util/Assert:isTrue	(ZLjava/lang/String;)V
/*     */     //   195: iload 5
/*     */     //   197: sipush 1000
/*     */     //   200: if_icmpge +8 -> 208
/*     */     //   203: iload 5
/*     */     //   205: goto +9 -> 214
/*     */     //   208: iload 5
/*     */     //   210: sipush 7000
/*     */     //   213: isub
/*     */     //   214: istore 6
/*     */     //   216: aload_1
/*     */     //   217: aload 4
/*     */     //   219: invokevirtual 31	java/util/Properties:getProperty	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   222: astore 7
/*     */     //   224: aload_0
/*     */     //   225: getfield 3	org/springframework/web/util/HtmlCharacterEntityReferences:characterToEntityReferenceMap	[Ljava/lang/String;
/*     */     //   228: iload 6
/*     */     //   230: new 18	java/lang/StringBuilder
/*     */     //   233: dup
/*     */     //   234: invokespecial 19	java/lang/StringBuilder:<init>	()V
/*     */     //   237: bipush 38
/*     */     //   239: invokevirtual 32	java/lang/StringBuilder:append	(C)Ljava/lang/StringBuilder;
/*     */     //   242: aload 7
/*     */     //   244: invokevirtual 21	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   247: bipush 59
/*     */     //   249: invokevirtual 32	java/lang/StringBuilder:append	(C)Ljava/lang/StringBuilder;
/*     */     //   252: invokevirtual 23	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   255: aastore
/*     */     //   256: aload_0
/*     */     //   257: getfield 6	org/springframework/web/util/HtmlCharacterEntityReferences:entityReferenceToCharacterMap	Ljava/util/Map;
/*     */     //   260: aload 7
/*     */     //   262: iload 5
/*     */     //   264: i2c
/*     */     //   265: invokestatic 33	java/lang/Character:valueOf	(C)Ljava/lang/Character;
/*     */     //   268: invokeinterface 34 3 0
/*     */     //   273: pop
/*     */     //   274: goto -158 -> 116
/*     */     //   277: return
/*     */     // Line number table:
/*     */     //   Java source line #63	-> byte code offset #0
/*     */     //   Java source line #55	-> byte code offset #4
/*     */     //   Java source line #57	-> byte code offset #14
/*     */     //   Java source line #64	-> byte code offset #28
/*     */     //   Java source line #67	-> byte code offset #36
/*     */     //   Java source line #68	-> byte code offset #44
/*     */     //   Java source line #69	-> byte code offset #48
/*     */     //   Java source line #74	-> byte code offset #58
/*     */     //   Java source line #77	-> byte code offset #63
/*     */     //   Java source line #78	-> byte code offset #67
/*     */     //   Java source line #77	-> byte code offset #70
/*     */     //   Java source line #83	-> byte code offset #77
/*     */     //   Java source line #80	-> byte code offset #80
/*     */     //   Java source line #81	-> byte code offset #81
/*     */     //   Java source line #82	-> byte code offset #98
/*     */     //   Java source line #86	-> byte code offset #111
/*     */     //   Java source line #87	-> byte code offset #116
/*     */     //   Java source line #88	-> byte code offset #125
/*     */     //   Java source line #89	-> byte code offset #136
/*     */     //   Java source line #90	-> byte code offset #143
/*     */     //   Java source line #92	-> byte code offset #195
/*     */     //   Java source line #93	-> byte code offset #216
/*     */     //   Java source line #94	-> byte code offset #224
/*     */     //   Java source line #95	-> byte code offset #256
/*     */     //   Java source line #96	-> byte code offset #274
/*     */     //   Java source line #97	-> byte code offset #277
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	278	0	this	HtmlCharacterEntityReferences
/*     */     //   35	182	1	entityReferences	java.util.Properties
/*     */     //   43	29	2	is	java.io.InputStream
/*     */     //   70	6	3	localObject1	Object
/*     */     //   80	18	3	ex	java.io.IOException
/*     */     //   115	11	3	keys	Object
/*     */     //   134	84	4	key	String
/*     */     //   141	122	5	referredChar	int
/*     */     //   214	15	6	index	int
/*     */     //   222	39	7	reference	String
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   58	63	70	finally
/*     */     //   58	77	80	java/io/IOException
/*     */   }
/*     */   
/*     */   public int getSupportedReferenceCount()
/*     */   {
/* 104 */     return this.entityReferenceToCharacterMap.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isMappedToReference(char character)
/*     */   {
/* 111 */     return isMappedToReference(character, "ISO-8859-1");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isMappedToReference(char character, String encoding)
/*     */   {
/* 118 */     return convertToReference(character, encoding) != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String convertToReference(char character)
/*     */   {
/* 125 */     return convertToReference(character, "ISO-8859-1");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String convertToReference(char character, String encoding)
/*     */   {
/* 133 */     if (encoding.startsWith("UTF-")) {
/* 134 */       switch (character) {
/*     */       case '<': 
/* 136 */         return "&lt;";
/*     */       case '>': 
/* 138 */         return "&gt;";
/*     */       case '"': 
/* 140 */         return "&quot;";
/*     */       case '&': 
/* 142 */         return "&amp;";
/*     */       case '\'': 
/* 144 */         return "&#39;";
/*     */       }
/*     */     }
/* 147 */     else if ((character < 'Ϩ') || ((character >= 'ὀ') && (character < '✐'))) {
/* 148 */       int index = character < 'Ϩ' ? character : character - '᭘';
/* 149 */       String entityReference = this.characterToEntityReferenceMap[index];
/* 150 */       if (entityReference != null) {
/* 151 */         return entityReference;
/*     */       }
/*     */     }
/* 154 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public char convertToCharacter(String entityReference)
/*     */   {
/* 161 */     Character referredCharacter = (Character)this.entityReferenceToCharacterMap.get(entityReference);
/* 162 */     if (referredCharacter != null) {
/* 163 */       return referredCharacter.charValue();
/*     */     }
/* 165 */     return 65535;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\we\\util\HtmlCharacterEntityReferences.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */