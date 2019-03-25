/*     */ package org.apache.tomcat.websocket;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.zip.DataFormatException;
/*     */ import java.util.zip.Deflater;
/*     */ import java.util.zip.Inflater;
/*     */ import javax.websocket.Extension;
/*     */ import javax.websocket.Extension.Parameter;
/*     */ import javax.websocket.SendHandler;
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
/*     */ public class PerMessageDeflate
/*     */   implements Transformation
/*     */ {
/*  35 */   private static final StringManager sm = StringManager.getManager(PerMessageDeflate.class);
/*     */   
/*     */   private static final String SERVER_NO_CONTEXT_TAKEOVER = "server_no_context_takeover";
/*     */   
/*     */   private static final String CLIENT_NO_CONTEXT_TAKEOVER = "client_no_context_takeover";
/*     */   private static final String SERVER_MAX_WINDOW_BITS = "server_max_window_bits";
/*     */   private static final String CLIENT_MAX_WINDOW_BITS = "client_max_window_bits";
/*     */   private static final int RSV_BITMASK = 4;
/*  43 */   private static final byte[] EOM_BYTES = { 0, 0, -1, -1 };
/*     */   
/*     */   public static final String NAME = "permessage-deflate";
/*     */   
/*     */   private final boolean serverContextTakeover;
/*     */   private final int serverMaxWindowBits;
/*     */   private final boolean clientContextTakeover;
/*     */   private final int clientMaxWindowBits;
/*     */   private final boolean isServer;
/*  52 */   private final Inflater inflater = new Inflater(true);
/*  53 */   private final ByteBuffer readBuffer = ByteBuffer.allocate(Constants.DEFAULT_BUFFER_SIZE);
/*  54 */   private final Deflater deflater = new Deflater(-1, true);
/*  55 */   private final byte[] EOM_BUFFER = new byte[EOM_BYTES.length + 1];
/*     */   
/*     */   private volatile Transformation next;
/*  58 */   private volatile boolean skipDecompression = false;
/*  59 */   private volatile ByteBuffer writeBuffer = ByteBuffer.allocate(Constants.DEFAULT_BUFFER_SIZE);
/*  60 */   private volatile boolean firstCompressedFrameWritten = false;
/*     */   
/*     */   static PerMessageDeflate negotiate(List<List<Extension.Parameter>> preferences, boolean isServer)
/*     */   {
/*  64 */     for (List<Extension.Parameter> preference : preferences) {
/*  65 */       boolean ok = true;
/*  66 */       boolean serverContextTakeover = true;
/*  67 */       int serverMaxWindowBits = -1;
/*  68 */       boolean clientContextTakeover = true;
/*  69 */       int clientMaxWindowBits = -1;
/*     */       
/*  71 */       for (Extension.Parameter param : preference) {
/*  72 */         if ("server_no_context_takeover".equals(param.getName())) {
/*  73 */           if (serverContextTakeover) {
/*  74 */             serverContextTakeover = false;
/*     */           }
/*     */           else {
/*  77 */             throw new IllegalArgumentException(sm.getString("perMessageDeflate.duplicateParameter", new Object[] { "server_no_context_takeover" }));
/*     */           }
/*     */           
/*     */         }
/*  81 */         else if ("client_no_context_takeover".equals(param.getName())) {
/*  82 */           if (clientContextTakeover) {
/*  83 */             clientContextTakeover = false;
/*     */           }
/*     */           else {
/*  86 */             throw new IllegalArgumentException(sm.getString("perMessageDeflate.duplicateParameter", new Object[] { "client_no_context_takeover" }));
/*     */           }
/*     */           
/*     */         }
/*  90 */         else if ("server_max_window_bits".equals(param.getName())) {
/*  91 */           if (serverMaxWindowBits == -1) {
/*  92 */             serverMaxWindowBits = Integer.parseInt(param.getValue());
/*  93 */             if ((serverMaxWindowBits < 8) || (serverMaxWindowBits > 15)) {
/*  94 */               throw new IllegalArgumentException(sm.getString("perMessageDeflate.invalidWindowSize", new Object[] { "server_max_window_bits", 
/*     */               
/*     */ 
/*  97 */                 Integer.valueOf(serverMaxWindowBits) }));
/*     */             }
/*     */             
/*     */ 
/*     */ 
/* 102 */             if ((isServer) && (serverMaxWindowBits != 15)) {
/* 103 */               ok = false;
/* 104 */               break;
/*     */             }
/*     */             
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/*     */ 
/* 112 */             throw new IllegalArgumentException(sm.getString("perMessageDeflate.duplicateParameter", new Object[] { "server_max_window_bits" }));
/*     */           }
/*     */           
/*     */         }
/* 116 */         else if ("client_max_window_bits".equals(param.getName())) {
/* 117 */           if (clientMaxWindowBits == -1) {
/* 118 */             if (param.getValue() == null)
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/* 123 */               clientMaxWindowBits = 15;
/*     */             } else {
/* 125 */               clientMaxWindowBits = Integer.parseInt(param.getValue());
/* 126 */               if ((clientMaxWindowBits < 8) || (clientMaxWindowBits > 15)) {
/* 127 */                 throw new IllegalArgumentException(sm.getString("perMessageDeflate.invalidWindowSize", new Object[] { "client_max_window_bits", 
/*     */                 
/*     */ 
/* 130 */                   Integer.valueOf(clientMaxWindowBits) }));
/*     */               }
/*     */             }
/*     */             
/*     */ 
/*     */ 
/* 136 */             if ((!isServer) && (clientMaxWindowBits != 15)) {
/* 137 */               ok = false;
/* 138 */               break;
/*     */             }
/*     */             
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/*     */ 
/* 146 */             throw new IllegalArgumentException(sm.getString("perMessageDeflate.duplicateParameter", new Object[] { "client_max_window_bits" }));
/*     */           }
/*     */           
/*     */ 
/*     */         }
/*     */         else {
/* 152 */           throw new IllegalArgumentException(sm.getString("perMessageDeflate.unknownParameter", new Object[] {param
/* 153 */             .getName() }));
/*     */         }
/*     */       }
/* 156 */       if (ok) {
/* 157 */         return new PerMessageDeflate(serverContextTakeover, serverMaxWindowBits, clientContextTakeover, clientMaxWindowBits, isServer);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 162 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   private PerMessageDeflate(boolean serverContextTakeover, int serverMaxWindowBits, boolean clientContextTakeover, int clientMaxWindowBits, boolean isServer)
/*     */   {
/* 168 */     this.serverContextTakeover = serverContextTakeover;
/* 169 */     this.serverMaxWindowBits = serverMaxWindowBits;
/* 170 */     this.clientContextTakeover = clientContextTakeover;
/* 171 */     this.clientMaxWindowBits = clientMaxWindowBits;
/* 172 */     this.isServer = isServer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TransformationResult getMoreData(byte opCode, boolean fin, int rsv, ByteBuffer dest)
/*     */     throws IOException
/*     */   {
/* 181 */     if (Util.isControl(opCode)) {
/* 182 */       return this.next.getMoreData(opCode, fin, rsv, dest);
/*     */     }
/*     */     
/* 185 */     if (!Util.isContinuation(opCode))
/*     */     {
/* 187 */       this.skipDecompression = ((rsv & 0x4) == 0);
/*     */     }
/*     */     
/*     */ 
/* 191 */     if (this.skipDecompression) {
/* 192 */       return this.next.getMoreData(opCode, fin, rsv, dest);
/*     */     }
/*     */     
/*     */ 
/* 196 */     boolean usedEomBytes = false;
/*     */     
/* 198 */     while (dest.remaining() > 0)
/*     */     {
/*     */       try {
/* 201 */         written = this.inflater.inflate(dest
/* 202 */           .array(), dest.arrayOffset() + dest.position(), dest.remaining());
/*     */       } catch (DataFormatException e) { int written;
/* 204 */         throw new IOException(sm.getString("perMessageDeflate.deflateFailed"), e); }
/*     */       int written;
/* 206 */       dest.position(dest.position() + written);
/*     */       
/* 208 */       if ((this.inflater.needsInput()) && (!usedEomBytes)) {
/* 209 */         if (dest.hasRemaining()) {
/* 210 */           this.readBuffer.clear();
/*     */           
/* 212 */           TransformationResult nextResult = this.next.getMoreData(opCode, fin, rsv ^ 0x4, this.readBuffer);
/* 213 */           this.inflater.setInput(this.readBuffer
/* 214 */             .array(), this.readBuffer.arrayOffset(), this.readBuffer.position());
/* 215 */           if (TransformationResult.UNDERFLOW.equals(nextResult))
/* 216 */             return nextResult;
/* 217 */           if ((TransformationResult.END_OF_FRAME.equals(nextResult)) && 
/* 218 */             (this.readBuffer.position() == 0)) {
/* 219 */             if (fin) {
/* 220 */               this.inflater.setInput(EOM_BYTES);
/* 221 */               usedEomBytes = true;
/*     */             } else {
/* 223 */               return TransformationResult.END_OF_FRAME;
/*     */             }
/*     */           }
/*     */         }
/* 227 */       } else if (written == 0) {
/* 228 */         if ((fin) && (((this.isServer) && (!this.clientContextTakeover)) || ((!this.isServer) && (!this.serverContextTakeover))))
/*     */         {
/* 230 */           this.inflater.reset();
/*     */         }
/* 232 */         return TransformationResult.END_OF_FRAME;
/*     */       }
/*     */     }
/*     */     
/* 236 */     return TransformationResult.OVERFLOW;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean validateRsv(int rsv, byte opCode)
/*     */   {
/* 242 */     if (Util.isControl(opCode)) {
/* 243 */       if ((rsv & 0x4) != 0) {
/* 244 */         return false;
/*     */       }
/* 246 */       if (this.next == null) {
/* 247 */         return true;
/*     */       }
/* 249 */       return this.next.validateRsv(rsv, opCode);
/*     */     }
/*     */     
/*     */ 
/* 253 */     int rsvNext = rsv;
/* 254 */     if ((rsv & 0x4) != 0) {
/* 255 */       rsvNext = rsv ^ 0x4;
/*     */     }
/* 257 */     if (this.next == null) {
/* 258 */       return true;
/*     */     }
/* 260 */     return this.next.validateRsv(rsvNext, opCode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Extension getExtensionResponse()
/*     */   {
/* 268 */     Extension result = new WsExtension("permessage-deflate");
/*     */     
/* 270 */     List<Extension.Parameter> params = result.getParameters();
/*     */     
/* 272 */     if (!this.serverContextTakeover) {
/* 273 */       params.add(new WsExtensionParameter("server_no_context_takeover", null));
/*     */     }
/* 275 */     if (this.serverMaxWindowBits != -1) {
/* 276 */       params.add(new WsExtensionParameter("server_max_window_bits", 
/* 277 */         Integer.toString(this.serverMaxWindowBits)));
/*     */     }
/* 279 */     if (!this.clientContextTakeover) {
/* 280 */       params.add(new WsExtensionParameter("client_no_context_takeover", null));
/*     */     }
/* 282 */     if (this.clientMaxWindowBits != -1) {
/* 283 */       params.add(new WsExtensionParameter("client_max_window_bits", 
/* 284 */         Integer.toString(this.clientMaxWindowBits)));
/*     */     }
/*     */     
/* 287 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setNext(Transformation t)
/*     */   {
/* 293 */     if (this.next == null) {
/* 294 */       this.next = t;
/*     */     } else {
/* 296 */       this.next.setNext(t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean validateRsvBits(int i)
/*     */   {
/* 303 */     if ((i & 0x4) != 0) {
/* 304 */       return false;
/*     */     }
/* 306 */     if (this.next == null) {
/* 307 */       return true;
/*     */     }
/* 309 */     return this.next.validateRsvBits(i | 0x4);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<MessagePart> sendMessagePart(List<MessagePart> uncompressedParts)
/*     */   {
/* 316 */     List<MessagePart> allCompressedParts = new ArrayList();
/*     */     
/*     */ 
/* 319 */     boolean emptyMessage = true;
/*     */     
/* 321 */     for (MessagePart uncompressedPart : uncompressedParts) {
/* 322 */       byte opCode = uncompressedPart.getOpCode();
/* 323 */       boolean emptyPart = uncompressedPart.getPayload().limit() == 0;
/* 324 */       emptyMessage = (emptyMessage) && (emptyPart);
/* 325 */       if (Util.isControl(opCode))
/*     */       {
/*     */ 
/* 328 */         allCompressedParts.add(uncompressedPart);
/* 329 */       } else if ((emptyMessage) && (uncompressedPart.isFin()))
/*     */       {
/*     */ 
/* 332 */         allCompressedParts.add(uncompressedPart);
/*     */       } else {
/* 334 */         List<MessagePart> compressedParts = new ArrayList();
/* 335 */         ByteBuffer uncompressedPayload = uncompressedPart.getPayload();
/*     */         
/* 337 */         SendHandler uncompressedIntermediateHandler = uncompressedPart.getIntermediateHandler();
/*     */         
/* 339 */         this.deflater.setInput(uncompressedPayload.array(), uncompressedPayload
/* 340 */           .arrayOffset() + uncompressedPayload.position(), uncompressedPayload
/* 341 */           .remaining());
/*     */         
/* 343 */         int flush = uncompressedPart.isFin() ? 2 : 0;
/* 344 */         boolean deflateRequired = true;
/*     */         
/* 346 */         while (deflateRequired) {
/* 347 */           ByteBuffer compressedPayload = this.writeBuffer;
/*     */           
/* 349 */           int written = this.deflater.deflate(compressedPayload.array(), compressedPayload
/* 350 */             .arrayOffset() + compressedPayload.position(), compressedPayload
/* 351 */             .remaining(), flush);
/* 352 */           compressedPayload.position(compressedPayload.position() + written);
/*     */           
/* 354 */           if ((!uncompressedPart.isFin()) && (compressedPayload.hasRemaining()) && (this.deflater.needsInput())) {
/*     */             break;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 366 */           this.writeBuffer = ByteBuffer.allocate(Constants.DEFAULT_BUFFER_SIZE);
/*     */           
/*     */ 
/* 369 */           compressedPayload.flip();
/*     */           
/* 371 */           boolean fin = uncompressedPart.isFin();
/* 372 */           boolean full = compressedPayload.limit() == compressedPayload.capacity();
/* 373 */           boolean needsInput = this.deflater.needsInput();
/* 374 */           long blockingWriteTimeoutExpiry = uncompressedPart.getBlockingWriteTimeoutExpiry();
/*     */           
/* 376 */           if ((fin) && (!full) && (needsInput))
/*     */           {
/* 378 */             compressedPayload.limit(compressedPayload.limit() - EOM_BYTES.length);
/* 379 */             MessagePart compressedPart = new MessagePart(true, getRsv(uncompressedPart), opCode, compressedPayload, uncompressedIntermediateHandler, uncompressedIntermediateHandler, blockingWriteTimeoutExpiry);
/*     */             
/*     */ 
/* 382 */             deflateRequired = false;
/* 383 */             startNewMessage(); } else { MessagePart compressedPart;
/* 384 */             if ((full) && (!needsInput))
/*     */             {
/*     */ 
/* 387 */               compressedPart = new MessagePart(false, getRsv(uncompressedPart), opCode, compressedPayload, uncompressedIntermediateHandler, uncompressedIntermediateHandler, blockingWriteTimeoutExpiry);
/*     */ 
/*     */             }
/* 390 */             else if ((!fin) && (full) && (needsInput))
/*     */             {
/*     */ 
/* 393 */               MessagePart compressedPart = new MessagePart(false, getRsv(uncompressedPart), opCode, compressedPayload, uncompressedIntermediateHandler, uncompressedIntermediateHandler, blockingWriteTimeoutExpiry);
/*     */               
/*     */ 
/* 396 */               deflateRequired = false; } else { MessagePart compressedPart;
/* 397 */               if ((fin) && (full) && (needsInput))
/*     */               {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 405 */                 int eomBufferWritten = this.deflater.deflate(this.EOM_BUFFER, 0, this.EOM_BUFFER.length, 2);
/* 406 */                 if (eomBufferWritten < this.EOM_BUFFER.length)
/*     */                 {
/* 408 */                   compressedPayload.limit(compressedPayload.limit() - EOM_BYTES.length + eomBufferWritten);
/*     */                   
/* 410 */                   MessagePart compressedPart = new MessagePart(true, getRsv(uncompressedPart), opCode, compressedPayload, uncompressedIntermediateHandler, uncompressedIntermediateHandler, blockingWriteTimeoutExpiry);
/*     */                   
/*     */ 
/* 413 */                   deflateRequired = false;
/* 414 */                   startNewMessage();
/*     */                 }
/*     */                 else
/*     */                 {
/* 418 */                   this.writeBuffer.put(this.EOM_BUFFER, 0, eomBufferWritten);
/*     */                   
/* 420 */                   compressedPart = new MessagePart(false, getRsv(uncompressedPart), opCode, compressedPayload, uncompressedIntermediateHandler, uncompressedIntermediateHandler, blockingWriteTimeoutExpiry);
/*     */                 }
/*     */               }
/*     */               else
/*     */               {
/* 425 */                 throw new IllegalStateException("Should never happen");
/*     */               }
/*     */             }
/*     */           }
/*     */           MessagePart compressedPart;
/* 430 */           compressedParts.add(compressedPart);
/*     */         }
/*     */         
/* 433 */         SendHandler uncompressedEndHandler = uncompressedPart.getEndHandler();
/* 434 */         int size = compressedParts.size();
/* 435 */         if (size > 0) {
/* 436 */           ((MessagePart)compressedParts.get(size - 1)).setEndHandler(uncompressedEndHandler);
/*     */         }
/*     */         
/* 439 */         allCompressedParts.addAll(compressedParts);
/*     */       }
/*     */     }
/*     */     
/* 443 */     if (this.next == null) {
/* 444 */       return allCompressedParts;
/*     */     }
/* 446 */     return this.next.sendMessagePart(allCompressedParts);
/*     */   }
/*     */   
/*     */ 
/*     */   private void startNewMessage()
/*     */   {
/* 452 */     this.firstCompressedFrameWritten = false;
/* 453 */     if (((this.isServer) && (!this.serverContextTakeover)) || ((!this.isServer) && (!this.clientContextTakeover))) {
/* 454 */       this.deflater.reset();
/*     */     }
/*     */   }
/*     */   
/*     */   private int getRsv(MessagePart uncompressedMessagePart)
/*     */   {
/* 460 */     int result = uncompressedMessagePart.getRsv();
/* 461 */     if (!this.firstCompressedFrameWritten) {
/* 462 */       result += 4;
/* 463 */       this.firstCompressedFrameWritten = true;
/*     */     }
/* 465 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void close()
/*     */   {
/* 472 */     this.next.close();
/* 473 */     this.inflater.end();
/* 474 */     this.deflater.end();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\PerMessageDeflate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */