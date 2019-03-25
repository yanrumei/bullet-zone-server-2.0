/*     */ package org.apache.tomcat.websocket.pojo;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.websocket.DecodeException;
/*     */ import javax.websocket.Decoder;
/*     */ import javax.websocket.Decoder.Text;
/*     */ import javax.websocket.Decoder.TextStream;
/*     */ import javax.websocket.EndpointConfig;
/*     */ import javax.websocket.Session;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ import org.apache.tomcat.websocket.Util;
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
/*     */ public class PojoMessageHandlerWholeText
/*     */   extends PojoMessageHandlerWholeBase<String>
/*     */ {
/*  43 */   private static final StringManager sm = StringManager.getManager(PojoMessageHandlerWholeText.class);
/*     */   
/*  45 */   private final List<Decoder> decoders = new ArrayList();
/*     */   
/*     */ 
/*     */   private final Class<?> primitiveType;
/*     */   
/*     */ 
/*     */   public PojoMessageHandlerWholeText(Object pojo, Method method, Session session, EndpointConfig config, List<Class<? extends Decoder>> decoderClazzes, Object[] params, int indexPayload, boolean convert, int indexSession, long maxMessageSize)
/*     */   {
/*  53 */     super(pojo, method, session, params, indexPayload, convert, indexSession, maxMessageSize);
/*     */     
/*     */ 
/*     */ 
/*  57 */     if ((maxMessageSize > -1L) && (maxMessageSize > session.getMaxTextMessageBufferSize())) {
/*  58 */       if (maxMessageSize > 2147483647L) {
/*  59 */         throw new IllegalArgumentException(sm.getString("pojoMessageHandlerWhole.maxBufferSize"));
/*     */       }
/*     */       
/*  62 */       session.setMaxTextMessageBufferSize((int)maxMessageSize);
/*     */     }
/*     */     
/*     */ 
/*  66 */     Class<?> type = method.getParameterTypes()[indexPayload];
/*  67 */     if (Util.isPrimitive(type)) {
/*  68 */       this.primitiveType = type;
/*  69 */       return;
/*     */     }
/*  71 */     this.primitiveType = null;
/*     */     
/*     */     try
/*     */     {
/*  75 */       if (decoderClazzes != null) {
/*  76 */         for (Class<? extends Decoder> decoderClazz : decoderClazzes) {
/*  77 */           if (Decoder.Text.class.isAssignableFrom(decoderClazz)) {
/*  78 */             Decoder.Text<?> decoder = (Decoder.Text)decoderClazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/*  79 */             decoder.init(config);
/*  80 */             this.decoders.add(decoder);
/*  81 */           } else if (Decoder.TextStream.class.isAssignableFrom(decoderClazz))
/*     */           {
/*     */ 
/*  84 */             Decoder.TextStream<?> decoder = (Decoder.TextStream)decoderClazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/*  85 */             decoder.init(config);
/*  86 */             this.decoders.add(decoder);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (ReflectiveOperationException e)
/*     */     {
/*  93 */       throw new IllegalArgumentException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected Object decode(String message)
/*     */     throws DecodeException
/*     */   {
/* 101 */     if (this.primitiveType != null) {
/* 102 */       return Util.coerceToType(this.primitiveType, message);
/*     */     }
/*     */     
/* 105 */     for (Decoder decoder : this.decoders) {
/* 106 */       if ((decoder instanceof Decoder.Text)) {
/* 107 */         if (((Decoder.Text)decoder).willDecode(message)) {
/* 108 */           return ((Decoder.Text)decoder).decode(message);
/*     */         }
/*     */       } else {
/* 111 */         StringReader r = new StringReader(message);
/*     */         try {
/* 113 */           return ((Decoder.TextStream)decoder).decode(r);
/*     */         } catch (IOException ioe) {
/* 115 */           throw new DecodeException(message, sm.getString("pojoMessageHandlerWhole.decodeIoFail"), ioe);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 120 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   protected Object convert(String message)
/*     */   {
/* 126 */     return new StringReader(message);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void onClose()
/*     */   {
/* 132 */     for (Decoder decoder : this.decoders) {
/* 133 */       decoder.destroy();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\pojo\PojoMessageHandlerWholeText.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */