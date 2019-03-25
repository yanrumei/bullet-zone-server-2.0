/*     */ package org.apache.coyote.http2;
/*     */ 
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
/*     */ public enum FrameType
/*     */ {
/*  23 */   DATA(0, false, true, null, false), 
/*  24 */   HEADERS(1, false, true, null, true), 
/*  25 */   PRIORITY(2, false, true, equals(5), false), 
/*  26 */   RST(3, false, true, equals(4), false), 
/*  27 */   SETTINGS(4, true, false, dividableBy(6), true), 
/*  28 */   PUSH_PROMISE(5, false, true, greaterOrEquals(4), true), 
/*  29 */   PING(6, true, false, equals(8), false), 
/*  30 */   GOAWAY(7, true, false, greaterOrEquals(8), false), 
/*  31 */   WINDOW_UPDATE(8, true, true, equals(4), true), 
/*  32 */   CONTINUATION(9, false, true, null, true), 
/*  33 */   UNKNOWN(256, true, true, null, false);
/*     */   
/*  35 */   private static final StringManager sm = StringManager.getManager(FrameType.class);
/*     */   
/*     */   private final int id;
/*     */   
/*     */   private final boolean streamZero;
/*     */   private final boolean streamNonZero;
/*     */   private final IntPredicate payloadSizeValidator;
/*     */   private final boolean payloadErrorFatal;
/*     */   
/*     */   private FrameType(int id, boolean streamZero, boolean streamNonZero, IntPredicate payloadSizeValidator, boolean payloadErrorFatal)
/*     */   {
/*  46 */     this.id = id;
/*  47 */     this.streamZero = streamZero;
/*  48 */     this.streamNonZero = streamNonZero;
/*  49 */     this.payloadSizeValidator = payloadSizeValidator;
/*  50 */     this.payloadErrorFatal = payloadErrorFatal;
/*     */   }
/*     */   
/*     */   public byte getIdByte()
/*     */   {
/*  55 */     return (byte)this.id;
/*     */   }
/*     */   
/*     */   public void check(int streamId, int payloadSize)
/*     */     throws Http2Exception
/*     */   {
/*  61 */     if (((streamId == 0) && (!this.streamZero)) || ((streamId != 0) && (!this.streamNonZero))) {
/*  62 */       throw new ConnectionException(sm.getString("frameType.checkStream", new Object[] { this }), Http2Error.PROTOCOL_ERROR);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  67 */     if ((this.payloadSizeValidator != null) && (!this.payloadSizeValidator.test(payloadSize))) {
/*  68 */       if ((this.payloadErrorFatal) || (streamId == 0)) {
/*  69 */         throw new ConnectionException(sm.getString("frameType.checkPayloadSize", new Object[] {
/*  70 */           Integer.toString(payloadSize), this }), Http2Error.FRAME_SIZE_ERROR);
/*     */       }
/*     */       
/*  73 */       throw new StreamException(sm.getString("frameType.checkPayloadSize", new Object[] {
/*  74 */         Integer.toString(payloadSize), this }), Http2Error.FRAME_SIZE_ERROR, streamId);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static FrameType valueOf(int i)
/*     */   {
/*  82 */     switch (i) {
/*     */     case 0: 
/*  84 */       return DATA;
/*     */     case 1: 
/*  86 */       return HEADERS;
/*     */     case 2: 
/*  88 */       return PRIORITY;
/*     */     case 3: 
/*  90 */       return RST;
/*     */     case 4: 
/*  92 */       return SETTINGS;
/*     */     case 5: 
/*  94 */       return PUSH_PROMISE;
/*     */     case 6: 
/*  96 */       return PING;
/*     */     case 7: 
/*  98 */       return GOAWAY;
/*     */     case 8: 
/* 100 */       return WINDOW_UPDATE;
/*     */     case 9: 
/* 102 */       return CONTINUATION;
/*     */     }
/* 104 */     return UNKNOWN;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static IntPredicate greaterOrEquals(int y)
/*     */   {
/* 113 */     new IntPredicate()
/*     */     {
/*     */       public boolean test(int x) {
/* 116 */         return x >= this.val$y;
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   private static IntPredicate equals(int y) {
/* 122 */     new IntPredicate()
/*     */     {
/*     */       public boolean test(int x) {
/* 125 */         return x == this.val$y;
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   private static IntPredicate dividableBy(int y) {
/* 131 */     new IntPredicate()
/*     */     {
/*     */       public boolean test(int x) {
/* 134 */         return x % this.val$y == 0;
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   private static abstract interface IntPredicate
/*     */   {
/*     */     public abstract boolean test(int paramInt);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http2\FrameType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */