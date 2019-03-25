/*     */ package ch.qos.logback.classic.db;
/*     */ 
/*     */ import ch.qos.logback.classic.Level;
/*     */ import ch.qos.logback.classic.db.names.DBNameResolver;
/*     */ import ch.qos.logback.classic.db.names.DefaultDBNameResolver;
/*     */ import ch.qos.logback.classic.spi.CallerData;
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import ch.qos.logback.classic.spi.IThrowableProxy;
/*     */ import ch.qos.logback.classic.spi.LoggerContextVO;
/*     */ import ch.qos.logback.classic.spi.StackTraceElementProxy;
/*     */ import ch.qos.logback.classic.spi.ThrowableProxyUtil;
/*     */ import ch.qos.logback.core.db.DBAppenderBase;
/*     */ import java.lang.reflect.Method;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DBAppender
/*     */   extends DBAppenderBase<ILoggingEvent>
/*     */ {
/*     */   protected String insertPropertiesSQL;
/*     */   protected String insertExceptionSQL;
/*     */   protected String insertSQL;
/*     */   protected static final Method GET_GENERATED_KEYS_METHOD;
/*     */   private DBNameResolver dbNameResolver;
/*     */   static final int TIMESTMP_INDEX = 1;
/*     */   static final int FORMATTED_MESSAGE_INDEX = 2;
/*     */   static final int LOGGER_NAME_INDEX = 3;
/*     */   static final int LEVEL_STRING_INDEX = 4;
/*     */   static final int THREAD_NAME_INDEX = 5;
/*     */   static final int REFERENCE_FLAG_INDEX = 6;
/*     */   static final int ARG0_INDEX = 7;
/*     */   static final int ARG1_INDEX = 8;
/*     */   static final int ARG2_INDEX = 9;
/*     */   static final int ARG3_INDEX = 10;
/*     */   static final int CALLER_FILENAME_INDEX = 11;
/*     */   static final int CALLER_CLASS_INDEX = 12;
/*     */   static final int CALLER_METHOD_INDEX = 13;
/*     */   static final int CALLER_LINE_INDEX = 14;
/*     */   static final int EVENT_ID_INDEX = 15;
/*  67 */   static final StackTraceElement EMPTY_CALLER_DATA = ;
/*     */   
/*     */   static
/*     */   {
/*     */     Method getGeneratedKeysMethod;
/*     */     try
/*     */     {
/*  74 */       getGeneratedKeysMethod = PreparedStatement.class.getMethod("getGeneratedKeys", null);
/*     */     } catch (Exception localException) { Method getGeneratedKeysMethod;
/*  76 */       getGeneratedKeysMethod = null;
/*     */     }
/*  78 */     GET_GENERATED_KEYS_METHOD = getGeneratedKeysMethod;
/*     */   }
/*     */   
/*     */   public void setDbNameResolver(DBNameResolver dbNameResolver) {
/*  82 */     this.dbNameResolver = dbNameResolver;
/*     */   }
/*     */   
/*     */   public void start()
/*     */   {
/*  87 */     if (this.dbNameResolver == null)
/*  88 */       this.dbNameResolver = new DefaultDBNameResolver();
/*  89 */     this.insertExceptionSQL = SQLBuilder.buildInsertExceptionSQL(this.dbNameResolver);
/*  90 */     this.insertPropertiesSQL = SQLBuilder.buildInsertPropertiesSQL(this.dbNameResolver);
/*  91 */     this.insertSQL = SQLBuilder.buildInsertSQL(this.dbNameResolver);
/*  92 */     super.start();
/*     */   }
/*     */   
/*     */   protected void subAppend(ILoggingEvent event, Connection connection, PreparedStatement insertStatement)
/*     */     throws Throwable
/*     */   {
/*  98 */     bindLoggingEventWithInsertStatement(insertStatement, event);
/*  99 */     bindLoggingEventArgumentsWithPreparedStatement(insertStatement, event.getArgumentArray());
/*     */     
/*     */ 
/* 102 */     bindCallerDataWithPreparedStatement(insertStatement, event.getCallerData());
/*     */     
/* 104 */     int updateCount = insertStatement.executeUpdate();
/* 105 */     if (updateCount != 1) {
/* 106 */       addWarn("Failed to insert loggingEvent");
/*     */     }
/*     */   }
/*     */   
/*     */   protected void secondarySubAppend(ILoggingEvent event, Connection connection, long eventId) throws Throwable {
/* 111 */     Map<String, String> mergedMap = mergePropertyMaps(event);
/* 112 */     insertProperties(mergedMap, connection, eventId);
/*     */     
/* 114 */     if (event.getThrowableProxy() != null) {
/* 115 */       insertThrowable(event.getThrowableProxy(), connection, eventId);
/*     */     }
/*     */   }
/*     */   
/*     */   void bindLoggingEventWithInsertStatement(PreparedStatement stmt, ILoggingEvent event) throws SQLException {
/* 120 */     stmt.setLong(1, event.getTimeStamp());
/* 121 */     stmt.setString(2, event.getFormattedMessage());
/* 122 */     stmt.setString(3, event.getLoggerName());
/* 123 */     stmt.setString(4, event.getLevel().toString());
/* 124 */     stmt.setString(5, event.getThreadName());
/* 125 */     stmt.setShort(6, DBHelper.computeReferenceMask(event));
/*     */   }
/*     */   
/*     */   void bindLoggingEventArgumentsWithPreparedStatement(PreparedStatement stmt, Object[] argArray) throws SQLException
/*     */   {
/* 130 */     int arrayLen = argArray != null ? argArray.length : 0;
/*     */     
/* 132 */     for (int i = 0; (i < arrayLen) && (i < 4); i++) {
/* 133 */       stmt.setString(7 + i, asStringTruncatedTo254(argArray[i]));
/*     */     }
/* 135 */     if (arrayLen < 4) {
/* 136 */       for (int i = arrayLen; i < 4; i++) {
/* 137 */         stmt.setString(7 + i, null);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   String asStringTruncatedTo254(Object o) {
/* 143 */     String s = null;
/* 144 */     if (o != null) {
/* 145 */       s = o.toString();
/*     */     }
/*     */     
/* 148 */     if (s == null) {
/* 149 */       return null;
/*     */     }
/* 151 */     if (s.length() <= 254) {
/* 152 */       return s;
/*     */     }
/* 154 */     return s.substring(0, 254);
/*     */   }
/*     */   
/*     */   void bindCallerDataWithPreparedStatement(PreparedStatement stmt, StackTraceElement[] callerDataArray)
/*     */     throws SQLException
/*     */   {
/* 160 */     StackTraceElement caller = extractFirstCaller(callerDataArray);
/*     */     
/* 162 */     stmt.setString(11, caller.getFileName());
/* 163 */     stmt.setString(12, caller.getClassName());
/* 164 */     stmt.setString(13, caller.getMethodName());
/* 165 */     stmt.setString(14, Integer.toString(caller.getLineNumber()));
/*     */   }
/*     */   
/*     */   private StackTraceElement extractFirstCaller(StackTraceElement[] callerDataArray) {
/* 169 */     StackTraceElement caller = EMPTY_CALLER_DATA;
/* 170 */     if (hasAtLeastOneNonNullElement(callerDataArray))
/* 171 */       caller = callerDataArray[0];
/* 172 */     return caller;
/*     */   }
/*     */   
/*     */   private boolean hasAtLeastOneNonNullElement(StackTraceElement[] callerDataArray) {
/* 176 */     return (callerDataArray != null) && (callerDataArray.length > 0) && (callerDataArray[0] != null);
/*     */   }
/*     */   
/*     */   Map<String, String> mergePropertyMaps(ILoggingEvent event) {
/* 180 */     Map<String, String> mergedMap = new HashMap();
/*     */     
/*     */ 
/*     */ 
/* 184 */     Map<String, String> loggerContextMap = event.getLoggerContextVO().getPropertyMap();
/* 185 */     Map<String, String> mdcMap = event.getMDCPropertyMap();
/* 186 */     if (loggerContextMap != null) {
/* 187 */       mergedMap.putAll(loggerContextMap);
/*     */     }
/* 189 */     if (mdcMap != null) {
/* 190 */       mergedMap.putAll(mdcMap);
/*     */     }
/*     */     
/* 193 */     return mergedMap;
/*     */   }
/*     */   
/*     */   protected Method getGeneratedKeysMethod()
/*     */   {
/* 198 */     return GET_GENERATED_KEYS_METHOD;
/*     */   }
/*     */   
/*     */   protected String getInsertSQL()
/*     */   {
/* 203 */     return this.insertSQL;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected void insertProperties(Map<String, String> mergedMap, Connection connection, long eventId)
/*     */     throws SQLException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: invokeinterface 305 1 0
/*     */     //   6: astore 5
/*     */     //   8: aload 5
/*     */     //   10: invokeinterface 309 1 0
/*     */     //   15: ifle +152 -> 167
/*     */     //   18: aconst_null
/*     */     //   19: astore 6
/*     */     //   21: aload_2
/*     */     //   22: aload_0
/*     */     //   23: getfield 102	ch/qos/logback/classic/db/DBAppender:insertPropertiesSQL	Ljava/lang/String;
/*     */     //   26: invokeinterface 314 2 0
/*     */     //   31: astore 6
/*     */     //   33: aload 5
/*     */     //   35: invokeinterface 320 1 0
/*     */     //   40: astore 8
/*     */     //   42: goto +82 -> 124
/*     */     //   45: aload 8
/*     */     //   47: invokeinterface 324 1 0
/*     */     //   52: checkcast 237	java/lang/String
/*     */     //   55: astore 7
/*     */     //   57: aload_1
/*     */     //   58: aload 7
/*     */     //   60: invokeinterface 330 2 0
/*     */     //   65: checkcast 237	java/lang/String
/*     */     //   68: astore 9
/*     */     //   70: aload 6
/*     */     //   72: iconst_1
/*     */     //   73: lload_3
/*     */     //   74: invokeinterface 187 4 0
/*     */     //   79: aload 6
/*     */     //   81: iconst_2
/*     */     //   82: aload 7
/*     */     //   84: invokeinterface 195 3 0
/*     */     //   89: aload 6
/*     */     //   91: iconst_3
/*     */     //   92: aload 9
/*     */     //   94: invokeinterface 195 3 0
/*     */     //   99: aload_0
/*     */     //   100: getfield 334	ch/qos/logback/classic/db/DBAppender:cnxSupportsBatchUpdates	Z
/*     */     //   103: ifeq +13 -> 116
/*     */     //   106: aload 6
/*     */     //   108: invokeinterface 338 1 0
/*     */     //   113: goto +11 -> 124
/*     */     //   116: aload 6
/*     */     //   118: invokeinterface 341 1 0
/*     */     //   123: pop
/*     */     //   124: aload 8
/*     */     //   126: invokeinterface 345 1 0
/*     */     //   131: ifne -86 -> 45
/*     */     //   134: aload_0
/*     */     //   135: getfield 334	ch/qos/logback/classic/db/DBAppender:cnxSupportsBatchUpdates	Z
/*     */     //   138: ifeq +24 -> 162
/*     */     //   141: aload 6
/*     */     //   143: invokeinterface 348 1 0
/*     */     //   148: pop
/*     */     //   149: goto +13 -> 162
/*     */     //   152: astore 10
/*     */     //   154: aload 6
/*     */     //   156: invokestatic 352	ch/qos/logback/core/db/DBHelper:closeStatement	(Ljava/sql/Statement;)V
/*     */     //   159: aload 10
/*     */     //   161: athrow
/*     */     //   162: aload 6
/*     */     //   164: invokestatic 352	ch/qos/logback/core/db/DBHelper:closeStatement	(Ljava/sql/Statement;)V
/*     */     //   167: return
/*     */     // Line number table:
/*     */     //   Java source line #207	-> byte code offset #0
/*     */     //   Java source line #208	-> byte code offset #8
/*     */     //   Java source line #209	-> byte code offset #18
/*     */     //   Java source line #211	-> byte code offset #21
/*     */     //   Java source line #213	-> byte code offset #33
/*     */     //   Java source line #214	-> byte code offset #57
/*     */     //   Java source line #216	-> byte code offset #70
/*     */     //   Java source line #217	-> byte code offset #79
/*     */     //   Java source line #218	-> byte code offset #89
/*     */     //   Java source line #220	-> byte code offset #99
/*     */     //   Java source line #221	-> byte code offset #106
/*     */     //   Java source line #222	-> byte code offset #113
/*     */     //   Java source line #223	-> byte code offset #116
/*     */     //   Java source line #213	-> byte code offset #124
/*     */     //   Java source line #227	-> byte code offset #134
/*     */     //   Java source line #228	-> byte code offset #141
/*     */     //   Java source line #230	-> byte code offset #149
/*     */     //   Java source line #231	-> byte code offset #154
/*     */     //   Java source line #232	-> byte code offset #159
/*     */     //   Java source line #231	-> byte code offset #162
/*     */     //   Java source line #234	-> byte code offset #167
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	168	0	this	DBAppender
/*     */     //   0	168	1	mergedMap	Map<String, String>
/*     */     //   0	168	2	connection	Connection
/*     */     //   0	168	3	eventId	long
/*     */     //   6	28	5	propertiesKeys	java.util.Set<String>
/*     */     //   19	144	6	insertPropertiesStatement	PreparedStatement
/*     */     //   55	28	7	key	String
/*     */     //   40	85	8	localIterator	java.util.Iterator
/*     */     //   68	25	9	value	String
/*     */     //   152	8	10	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   21	152	152	finally
/*     */   }
/*     */   
/*     */   void updateExceptionStatement(PreparedStatement exceptionStatement, String txt, short i, long eventId)
/*     */     throws SQLException
/*     */   {
/* 241 */     exceptionStatement.setLong(1, eventId);
/* 242 */     exceptionStatement.setShort(2, i);
/* 243 */     exceptionStatement.setString(3, txt);
/* 244 */     if (this.cnxSupportsBatchUpdates) {
/* 245 */       exceptionStatement.addBatch();
/*     */     } else {
/* 247 */       exceptionStatement.execute();
/*     */     }
/*     */   }
/*     */   
/*     */   short buildExceptionStatement(IThrowableProxy tp, short baseIndex, PreparedStatement insertExceptionStatement, long eventId) throws SQLException
/*     */   {
/* 253 */     StringBuilder buf = new StringBuilder();
/* 254 */     ThrowableProxyUtil.subjoinFirstLine(buf, tp);
/* 255 */     updateExceptionStatement(insertExceptionStatement, buf.toString(), baseIndex++, eventId);
/*     */     
/* 257 */     int commonFrames = tp.getCommonFrames();
/* 258 */     StackTraceElementProxy[] stepArray = tp.getStackTraceElementProxyArray();
/* 259 */     for (int i = 0; i < stepArray.length - commonFrames; i++) {
/* 260 */       StringBuilder sb = new StringBuilder();
/* 261 */       sb.append('\t');
/* 262 */       ThrowableProxyUtil.subjoinSTEP(sb, stepArray[i]);
/* 263 */       updateExceptionStatement(insertExceptionStatement, sb.toString(), baseIndex++, eventId);
/*     */     }
/*     */     
/* 266 */     if (commonFrames > 0) {
/* 267 */       StringBuilder sb = new StringBuilder();
/* 268 */       sb.append('\t').append("... ").append(commonFrames).append(" common frames omitted");
/* 269 */       updateExceptionStatement(insertExceptionStatement, sb.toString(), baseIndex++, eventId);
/*     */     }
/*     */     
/* 272 */     return baseIndex;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected void insertThrowable(IThrowableProxy tp, Connection connection, long eventId)
/*     */     throws SQLException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore 5
/*     */     //   3: aload_2
/*     */     //   4: aload_0
/*     */     //   5: getfield 97	ch/qos/logback/classic/db/DBAppender:insertExceptionSQL	Ljava/lang/String;
/*     */     //   8: invokeinterface 314 2 0
/*     */     //   13: astore 5
/*     */     //   15: iconst_0
/*     */     //   16: istore 6
/*     */     //   18: goto +22 -> 40
/*     */     //   21: aload_0
/*     */     //   22: aload_1
/*     */     //   23: iload 6
/*     */     //   25: aload 5
/*     */     //   27: lload_3
/*     */     //   28: invokevirtual 421	ch/qos/logback/classic/db/DBAppender:buildExceptionStatement	(Lch/qos/logback/classic/spi/IThrowableProxy;SLjava/sql/PreparedStatement;J)S
/*     */     //   31: istore 6
/*     */     //   33: aload_1
/*     */     //   34: invokeinterface 423 1 0
/*     */     //   39: astore_1
/*     */     //   40: aload_1
/*     */     //   41: ifnonnull -20 -> 21
/*     */     //   44: aload_0
/*     */     //   45: getfield 334	ch/qos/logback/classic/db/DBAppender:cnxSupportsBatchUpdates	Z
/*     */     //   48: ifeq +24 -> 72
/*     */     //   51: aload 5
/*     */     //   53: invokeinterface 348 1 0
/*     */     //   58: pop
/*     */     //   59: goto +13 -> 72
/*     */     //   62: astore 7
/*     */     //   64: aload 5
/*     */     //   66: invokestatic 352	ch/qos/logback/core/db/DBHelper:closeStatement	(Ljava/sql/Statement;)V
/*     */     //   69: aload 7
/*     */     //   71: athrow
/*     */     //   72: aload 5
/*     */     //   74: invokestatic 352	ch/qos/logback/core/db/DBHelper:closeStatement	(Ljava/sql/Statement;)V
/*     */     //   77: return
/*     */     // Line number table:
/*     */     //   Java source line #277	-> byte code offset #0
/*     */     //   Java source line #279	-> byte code offset #3
/*     */     //   Java source line #281	-> byte code offset #15
/*     */     //   Java source line #282	-> byte code offset #18
/*     */     //   Java source line #283	-> byte code offset #21
/*     */     //   Java source line #284	-> byte code offset #33
/*     */     //   Java source line #282	-> byte code offset #40
/*     */     //   Java source line #287	-> byte code offset #44
/*     */     //   Java source line #288	-> byte code offset #51
/*     */     //   Java source line #290	-> byte code offset #59
/*     */     //   Java source line #291	-> byte code offset #64
/*     */     //   Java source line #292	-> byte code offset #69
/*     */     //   Java source line #291	-> byte code offset #72
/*     */     //   Java source line #294	-> byte code offset #77
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	78	0	this	DBAppender
/*     */     //   0	78	1	tp	IThrowableProxy
/*     */     //   0	78	2	connection	Connection
/*     */     //   0	78	3	eventId	long
/*     */     //   1	72	5	exceptionStatement	PreparedStatement
/*     */     //   16	16	6	baseIndex	short
/*     */     //   62	8	7	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   3	62	62	finally
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\db\DBAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */