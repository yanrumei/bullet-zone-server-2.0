/*     */ package org.apache.coyote.http2;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public abstract class ConnectionSettingsBase<T extends Throwable>
/*     */ {
/*  28 */   private final Log log = LogFactory.getLog(ConnectionSettingsBase.class);
/*  29 */   private final StringManager sm = StringManager.getManager(ConnectionSettingsBase.class);
/*     */   
/*     */   private final String connectionId;
/*     */   
/*     */   protected static final int MAX_WINDOW_SIZE = Integer.MAX_VALUE;
/*     */   
/*     */   protected static final int MIN_MAX_FRAME_SIZE = 16384;
/*     */   
/*     */   protected static final int MAX_MAX_FRAME_SIZE = 16777215;
/*     */   
/*     */   protected static final long UNLIMITED = 4294967296L;
/*     */   
/*     */   protected static final int MAX_HEADER_TABLE_SIZE = 65536;
/*     */   protected static final int DEFAULT_HEADER_TABLE_SIZE = 4096;
/*     */   protected static final boolean DEFAULT_ENABLE_PUSH = true;
/*     */   protected static final long DEFAULT_MAX_CONCURRENT_STREAMS = 4294967296L;
/*     */   protected static final int DEFAULT_INITIAL_WINDOW_SIZE = 65535;
/*     */   protected static final int DEFAULT_MAX_FRAME_SIZE = 16384;
/*     */   protected static final long DEFAULT_MAX_HEADER_LIST_SIZE = 4294967296L;
/*  48 */   protected Map<Setting, Long> current = new HashMap();
/*  49 */   protected Map<Setting, Long> pending = new HashMap();
/*     */   
/*     */   public ConnectionSettingsBase(String connectionId)
/*     */   {
/*  53 */     this.connectionId = connectionId;
/*     */     
/*  55 */     this.current.put(Setting.HEADER_TABLE_SIZE, Long.valueOf(4096L));
/*  56 */     this.current.put(Setting.ENABLE_PUSH, Long.valueOf(1L));
/*  57 */     this.current.put(Setting.MAX_CONCURRENT_STREAMS, Long.valueOf(4294967296L));
/*  58 */     this.current.put(Setting.INITIAL_WINDOW_SIZE, Long.valueOf(65535L));
/*  59 */     this.current.put(Setting.MAX_FRAME_SIZE, Long.valueOf(16384L));
/*  60 */     this.current.put(Setting.MAX_HEADER_LIST_SIZE, Long.valueOf(4294967296L));
/*     */   }
/*     */   
/*     */   public void set(Setting setting, long value) throws Throwable
/*     */   {
/*  65 */     if (this.log.isDebugEnabled()) {
/*  66 */       this.log.debug(this.sm.getString("connectionSettings.debug", new Object[] { this.connectionId, setting, 
/*  67 */         Long.toString(value) }));
/*     */     }
/*     */     
/*  70 */     switch (setting) {
/*     */     case HEADER_TABLE_SIZE: 
/*  72 */       validateHeaderTableSize(value);
/*  73 */       break;
/*     */     case ENABLE_PUSH: 
/*  75 */       validateEnablePush(value);
/*  76 */       break;
/*     */     case MAX_CONCURRENT_STREAMS: 
/*     */       break;
/*     */     
/*     */     case INITIAL_WINDOW_SIZE: 
/*  81 */       validateInitialWindowSize(value);
/*  82 */       break;
/*     */     case MAX_FRAME_SIZE: 
/*  84 */       validateMaxFrameSize(value);
/*  85 */       break;
/*     */     
/*     */     case MAX_HEADER_LIST_SIZE: 
/*     */       break;
/*     */     
/*     */     case UNKNOWN: 
/*  91 */       this.log.warn(this.sm.getString("connectionSettings.unknown", new Object[] { this.connectionId, setting, 
/*  92 */         Long.toString(value) }));
/*  93 */       return;
/*     */     }
/*     */     
/*  96 */     set(setting, Long.valueOf(value));
/*     */   }
/*     */   
/*     */   synchronized void set(Setting setting, Long value)
/*     */   {
/* 101 */     this.current.put(setting, value);
/*     */   }
/*     */   
/*     */   public int getHeaderTableSize()
/*     */   {
/* 106 */     return getMinInt(Setting.HEADER_TABLE_SIZE);
/*     */   }
/*     */   
/*     */   public boolean getEnablePush()
/*     */   {
/* 111 */     long result = getMin(Setting.ENABLE_PUSH);
/* 112 */     return result != 0L;
/*     */   }
/*     */   
/*     */   public long getMaxConcurrentStreams()
/*     */   {
/* 117 */     return getMax(Setting.MAX_CONCURRENT_STREAMS);
/*     */   }
/*     */   
/*     */   public int getInitialWindowSize()
/*     */   {
/* 122 */     return getMaxInt(Setting.INITIAL_WINDOW_SIZE);
/*     */   }
/*     */   
/*     */   public int getMaxFrameSize()
/*     */   {
/* 127 */     return getMaxInt(Setting.MAX_FRAME_SIZE);
/*     */   }
/*     */   
/*     */   public long getMaxHeaderListSize()
/*     */   {
/* 132 */     return getMax(Setting.MAX_HEADER_LIST_SIZE);
/*     */   }
/*     */   
/*     */   private synchronized long getMin(Setting setting)
/*     */   {
/* 137 */     Long pendingValue = (Long)this.pending.get(setting);
/* 138 */     long currentValue = ((Long)this.current.get(setting)).longValue();
/* 139 */     if (pendingValue == null) {
/* 140 */       return currentValue;
/*     */     }
/* 142 */     return Math.min(pendingValue.longValue(), currentValue);
/*     */   }
/*     */   
/*     */ 
/*     */   private synchronized int getMinInt(Setting setting)
/*     */   {
/* 148 */     long result = getMin(setting);
/* 149 */     if (result > 2147483647L) {
/* 150 */       return Integer.MAX_VALUE;
/*     */     }
/* 152 */     return (int)result;
/*     */   }
/*     */   
/*     */ 
/*     */   private synchronized long getMax(Setting setting)
/*     */   {
/* 158 */     Long pendingValue = (Long)this.pending.get(setting);
/* 159 */     long currentValue = ((Long)this.current.get(setting)).longValue();
/* 160 */     if (pendingValue == null) {
/* 161 */       return currentValue;
/*     */     }
/* 163 */     return Math.max(pendingValue.longValue(), currentValue);
/*     */   }
/*     */   
/*     */ 
/*     */   private synchronized int getMaxInt(Setting setting)
/*     */   {
/* 169 */     long result = getMax(setting);
/* 170 */     if (result > 2147483647L) {
/* 171 */       return Integer.MAX_VALUE;
/*     */     }
/* 173 */     return (int)result;
/*     */   }
/*     */   
/*     */   private void validateHeaderTableSize(long headerTableSize)
/*     */     throws Throwable
/*     */   {
/* 179 */     if (headerTableSize > 65536L) {
/* 180 */       String msg = this.sm.getString("connectionSettings.headerTableSizeLimit", new Object[] { this.connectionId, 
/* 181 */         Long.toString(headerTableSize) });
/* 182 */       throwException(msg, Http2Error.PROTOCOL_ERROR);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void validateEnablePush(long enablePush)
/*     */     throws Throwable
/*     */   {
/* 190 */     if (enablePush > 1L) {
/* 191 */       String msg = this.sm.getString("connectionSettings.enablePushInvalid", new Object[] { this.connectionId, 
/* 192 */         Long.toString(enablePush) });
/* 193 */       throwException(msg, Http2Error.PROTOCOL_ERROR);
/*     */     }
/*     */   }
/*     */   
/*     */   private void validateInitialWindowSize(long initialWindowSize) throws Throwable
/*     */   {
/* 199 */     if (initialWindowSize > 2147483647L) {
/* 200 */       String msg = this.sm.getString("connectionSettings.windowSizeTooBig", new Object[] { this.connectionId, 
/* 201 */         Long.toString(initialWindowSize), Long.toString(2147483647L) });
/* 202 */       throwException(msg, Http2Error.FLOW_CONTROL_ERROR);
/*     */     }
/*     */   }
/*     */   
/*     */   private void validateMaxFrameSize(long maxFrameSize) throws Throwable
/*     */   {
/* 208 */     if ((maxFrameSize < 16384L) || (maxFrameSize > 16777215L)) {
/* 209 */       String msg = this.sm.getString("connectionSettings.maxFrameSizeInvalid", new Object[] { this.connectionId, 
/* 210 */         Long.toString(maxFrameSize), Integer.toString(16384), 
/* 211 */         Integer.toString(16777215) });
/* 212 */       throwException(msg, Http2Error.PROTOCOL_ERROR);
/*     */     }
/*     */   }
/*     */   
/*     */   abstract void throwException(String paramString, Http2Error paramHttp2Error)
/*     */     throws Throwable;
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http2\ConnectionSettingsBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */