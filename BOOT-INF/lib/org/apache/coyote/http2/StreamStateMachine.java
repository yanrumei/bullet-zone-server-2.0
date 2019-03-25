/*     */ package org.apache.coyote.http2;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StreamStateMachine
/*     */ {
/*  38 */   private static final Log log = LogFactory.getLog(StreamStateMachine.class);
/*  39 */   private static final StringManager sm = StringManager.getManager(StreamStateMachine.class);
/*     */   
/*     */   private final Stream stream;
/*     */   private State state;
/*     */   
/*     */   public StreamStateMachine(Stream stream)
/*     */   {
/*  46 */     this.stream = stream;
/*  47 */     stateChange(null, State.IDLE);
/*     */   }
/*     */   
/*     */   public synchronized void sentPushPromise()
/*     */   {
/*  52 */     stateChange(State.IDLE, State.RESERVED_LOCAL);
/*     */   }
/*     */   
/*     */   public synchronized void receivedPushPromise()
/*     */   {
/*  57 */     stateChange(State.IDLE, State.RESERVED_REMOTE);
/*     */   }
/*     */   
/*     */   public synchronized void sentStartOfHeaders()
/*     */   {
/*  62 */     stateChange(State.IDLE, State.OPEN);
/*  63 */     stateChange(State.RESERVED_LOCAL, State.HALF_CLOSED_REMOTE);
/*     */   }
/*     */   
/*     */   public synchronized void receivedStartOfHeaders()
/*     */   {
/*  68 */     stateChange(State.IDLE, State.OPEN);
/*  69 */     stateChange(State.RESERVED_REMOTE, State.HALF_CLOSED_LOCAL);
/*     */   }
/*     */   
/*     */   public synchronized void sentEndOfStream()
/*     */   {
/*  74 */     stateChange(State.OPEN, State.HALF_CLOSED_LOCAL);
/*  75 */     stateChange(State.HALF_CLOSED_REMOTE, State.CLOSED_TX);
/*     */   }
/*     */   
/*     */   public synchronized void receivedEndOfStream()
/*     */   {
/*  80 */     stateChange(State.OPEN, State.HALF_CLOSED_REMOTE);
/*  81 */     stateChange(State.HALF_CLOSED_LOCAL, State.CLOSED_RX);
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
/*     */   public synchronized void sendReset()
/*     */   {
/*  97 */     if (this.state == State.IDLE) {
/*  98 */       throw new IllegalStateException(sm.getString("streamStateMachine.debug.change", new Object[] {this.stream
/*  99 */         .getConnectionId(), this.stream.getIdentifier(), this.state }));
/*     */     }
/* 101 */     if (this.state.canReset()) {
/* 102 */       stateChange(this.state, State.CLOSED_RST_TX);
/*     */     }
/*     */   }
/*     */   
/*     */   final synchronized void receivedReset()
/*     */   {
/* 108 */     stateChange(this.state, State.CLOSED_RST_RX);
/*     */   }
/*     */   
/*     */   private void stateChange(State oldState, State newState)
/*     */   {
/* 113 */     if (this.state == oldState) {
/* 114 */       this.state = newState;
/* 115 */       if (log.isDebugEnabled()) {
/* 116 */         log.debug(sm.getString("streamStateMachine.debug.change", new Object[] { this.stream.getConnectionId(), this.stream
/* 117 */           .getIdentifier(), oldState, newState }));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void checkFrameType(FrameType frameType)
/*     */     throws Http2Exception
/*     */   {
/* 126 */     if (!isFrameTypePermitted(frameType)) {
/* 127 */       if (this.state.connectionErrorForInvalidFrame)
/*     */       {
/*     */ 
/* 130 */         throw new ConnectionException(sm.getString("streamStateMachine.invalidFrame", new Object[] {this.stream.getConnectionId(), this.stream.getIdentifier(), this.state, frameType }), this.state.errorCodeForInvalidFrame);
/*     */       }
/*     */       
/*     */ 
/* 134 */       throw new StreamException(sm.getString("streamStateMachine.invalidFrame", new Object[] {this.stream.getConnectionId(), this.stream.getIdentifier(), this.state, frameType }), this.state.errorCodeForInvalidFrame, this.stream.getIdentifier().intValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized boolean isFrameTypePermitted(FrameType frameType)
/*     */   {
/* 141 */     return this.state.isFrameTypePermitted(frameType);
/*     */   }
/*     */   
/*     */   public synchronized boolean isActive()
/*     */   {
/* 146 */     return this.state.isActive();
/*     */   }
/*     */   
/*     */   public synchronized boolean canRead()
/*     */   {
/* 151 */     return this.state.canRead();
/*     */   }
/*     */   
/*     */   public synchronized boolean canWrite()
/*     */   {
/* 156 */     return this.state.canWrite();
/*     */   }
/*     */   
/*     */   public synchronized boolean isClosedFinal()
/*     */   {
/* 161 */     return this.state == State.CLOSED_FINAL;
/*     */   }
/*     */   
/*     */   public synchronized void closeIfIdle() {
/* 165 */     stateChange(State.IDLE, State.CLOSED_FINAL);
/*     */   }
/*     */   
/*     */   private static enum State
/*     */   {
/* 170 */     IDLE(false, false, false, true, Http2Error.PROTOCOL_ERROR, new FrameType[] { FrameType.HEADERS, FrameType.PRIORITY }), 
/*     */     
/*     */ 
/* 173 */     OPEN(true, true, true, true, Http2Error.PROTOCOL_ERROR, new FrameType[] { FrameType.DATA, FrameType.HEADERS, FrameType.PRIORITY, FrameType.RST, FrameType.PUSH_PROMISE, FrameType.WINDOW_UPDATE }), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 180 */     RESERVED_LOCAL(false, false, true, true, Http2Error.PROTOCOL_ERROR, new FrameType[] { FrameType.PRIORITY, FrameType.RST, FrameType.WINDOW_UPDATE }), 
/*     */     
/*     */ 
/*     */ 
/* 184 */     RESERVED_REMOTE(false, false, true, true, Http2Error.PROTOCOL_ERROR, new FrameType[] { FrameType.HEADERS, FrameType.PRIORITY, FrameType.RST }), 
/*     */     
/*     */ 
/*     */ 
/* 188 */     HALF_CLOSED_LOCAL(true, false, true, true, Http2Error.PROTOCOL_ERROR, new FrameType[] { FrameType.DATA, FrameType.HEADERS, FrameType.PRIORITY, FrameType.RST, FrameType.PUSH_PROMISE, FrameType.WINDOW_UPDATE }), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 195 */     HALF_CLOSED_REMOTE(false, true, true, true, Http2Error.STREAM_CLOSED, new FrameType[] { FrameType.PRIORITY, FrameType.RST, FrameType.WINDOW_UPDATE }), 
/*     */     
/*     */ 
/*     */ 
/* 199 */     CLOSED_RX(false, false, false, true, Http2Error.STREAM_CLOSED, new FrameType[] { FrameType.PRIORITY }), 
/*     */     
/* 201 */     CLOSED_TX(false, false, false, true, Http2Error.STREAM_CLOSED, new FrameType[] { FrameType.PRIORITY, FrameType.RST, FrameType.WINDOW_UPDATE }), 
/*     */     
/*     */ 
/*     */ 
/* 205 */     CLOSED_RST_RX(false, false, false, false, Http2Error.STREAM_CLOSED, new FrameType[] { FrameType.PRIORITY }), 
/*     */     
/* 207 */     CLOSED_RST_TX(false, false, false, false, Http2Error.STREAM_CLOSED, new FrameType[] { FrameType.DATA, FrameType.HEADERS, FrameType.PRIORITY, FrameType.RST, FrameType.PUSH_PROMISE, FrameType.WINDOW_UPDATE }), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 214 */     CLOSED_FINAL(false, false, false, true, Http2Error.PROTOCOL_ERROR, new FrameType[] { FrameType.PRIORITY });
/*     */     
/*     */ 
/*     */     private final boolean canRead;
/*     */     private final boolean canWrite;
/*     */     private final boolean canReset;
/*     */     private final boolean connectionErrorForInvalidFrame;
/*     */     private final Http2Error errorCodeForInvalidFrame;
/* 222 */     private final Set<FrameType> frameTypesPermitted = new HashSet();
/*     */     
/*     */ 
/*     */     private State(boolean canRead, boolean canWrite, boolean canReset, boolean connectionErrorForInvalidFrame, Http2Error errorCode, FrameType... frameTypes)
/*     */     {
/* 227 */       this.canRead = canRead;
/* 228 */       this.canWrite = canWrite;
/* 229 */       this.canReset = canReset;
/* 230 */       this.connectionErrorForInvalidFrame = connectionErrorForInvalidFrame;
/* 231 */       this.errorCodeForInvalidFrame = errorCode;
/* 232 */       for (FrameType frameType : frameTypes) {
/* 233 */         this.frameTypesPermitted.add(frameType);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean isActive() {
/* 238 */       return (this.canWrite) || (this.canRead);
/*     */     }
/*     */     
/*     */     public boolean canRead() {
/* 242 */       return this.canRead;
/*     */     }
/*     */     
/*     */     public boolean canWrite() {
/* 246 */       return this.canWrite;
/*     */     }
/*     */     
/*     */     public boolean canReset() {
/* 250 */       return this.canReset;
/*     */     }
/*     */     
/*     */     public boolean isFrameTypePermitted(FrameType frameType) {
/* 254 */       return this.frameTypesPermitted.contains(frameType);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http2\StreamStateMachine.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */