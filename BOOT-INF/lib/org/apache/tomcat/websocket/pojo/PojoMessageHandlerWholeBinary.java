/*     */ package org.apache.tomcat.websocket.pojo;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.websocket.DecodeException;
/*     */ import javax.websocket.Decoder;
/*     */ import javax.websocket.Decoder.Binary;
/*     */ import javax.websocket.Decoder.BinaryStream;
/*     */ import javax.websocket.EndpointConfig;
/*     */ import javax.websocket.Session;
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
/*     */ public class PojoMessageHandlerWholeBinary
/*     */   extends PojoMessageHandlerWholeBase<ByteBuffer>
/*     */ {
/*  42 */   private static final StringManager sm = StringManager.getManager(PojoMessageHandlerWholeBinary.class);
/*     */   
/*  44 */   private final List<Decoder> decoders = new ArrayList();
/*     */   
/*     */ 
/*     */   private final boolean isForInputStream;
/*     */   
/*     */ 
/*     */ 
/*     */   public PojoMessageHandlerWholeBinary(Object pojo, Method method, Session session, EndpointConfig config, List<Class<? extends Decoder>> decoderClazzes, Object[] params, int indexPayload, boolean convert, int indexSession, boolean isForInputStream, long maxMessageSize)
/*     */   {
/*  53 */     super(pojo, method, session, params, indexPayload, convert, indexSession, maxMessageSize);
/*     */     
/*     */ 
/*     */ 
/*  57 */     if ((maxMessageSize > -1L) && (maxMessageSize > session.getMaxBinaryMessageBufferSize())) {
/*  58 */       if (maxMessageSize > 2147483647L) {
/*  59 */         throw new IllegalArgumentException(sm.getString("pojoMessageHandlerWhole.maxBufferSize"));
/*     */       }
/*     */       
/*  62 */       session.setMaxBinaryMessageBufferSize((int)maxMessageSize);
/*     */     }
/*     */     try
/*     */     {
/*  66 */       if (decoderClazzes != null) {
/*  67 */         for (Class<? extends Decoder> decoderClazz : decoderClazzes) {
/*  68 */           if (Decoder.Binary.class.isAssignableFrom(decoderClazz)) {
/*  69 */             Decoder.Binary<?> decoder = (Decoder.Binary)decoderClazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/*  70 */             decoder.init(config);
/*  71 */             this.decoders.add(decoder);
/*  72 */           } else if (Decoder.BinaryStream.class.isAssignableFrom(decoderClazz))
/*     */           {
/*     */ 
/*  75 */             Decoder.BinaryStream<?> decoder = (Decoder.BinaryStream)decoderClazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/*  76 */             decoder.init(config);
/*  77 */             this.decoders.add(decoder);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (ReflectiveOperationException e)
/*     */     {
/*  84 */       throw new IllegalArgumentException(e);
/*     */     }
/*  86 */     this.isForInputStream = isForInputStream;
/*     */   }
/*     */   
/*     */   protected Object decode(ByteBuffer message)
/*     */     throws DecodeException
/*     */   {
/*  92 */     for (Decoder decoder : this.decoders) {
/*  93 */       if ((decoder instanceof Decoder.Binary)) {
/*  94 */         if (((Decoder.Binary)decoder).willDecode(message)) {
/*  95 */           return ((Decoder.Binary)decoder).decode(message);
/*     */         }
/*     */       } else {
/*  98 */         byte[] array = new byte[message.limit() - message.position()];
/*  99 */         message.get(array);
/* 100 */         ByteArrayInputStream bais = new ByteArrayInputStream(array);
/*     */         try {
/* 102 */           return ((Decoder.BinaryStream)decoder).decode(bais);
/*     */         } catch (IOException ioe) {
/* 104 */           throw new DecodeException(message, sm.getString("pojoMessageHandlerWhole.decodeIoFail"), ioe);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 109 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   protected Object convert(ByteBuffer message)
/*     */   {
/* 115 */     byte[] array = new byte[message.remaining()];
/* 116 */     message.get(array);
/* 117 */     if (this.isForInputStream) {
/* 118 */       return new ByteArrayInputStream(array);
/*     */     }
/* 120 */     return array;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void onClose()
/*     */   {
/* 127 */     for (Decoder decoder : this.decoders) {
/* 128 */       decoder.destroy();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\pojo\PojoMessageHandlerWholeBinary.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */