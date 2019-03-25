/*     */ package org.apache.tomcat.websocket;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import javax.websocket.CloseReason.CloseCode;
/*     */ import javax.websocket.CloseReason.CloseCodes;
/*     */ import javax.websocket.Decoder;
/*     */ import javax.websocket.Decoder.Binary;
/*     */ import javax.websocket.Decoder.BinaryStream;
/*     */ import javax.websocket.Decoder.Text;
/*     */ import javax.websocket.Decoder.TextStream;
/*     */ import javax.websocket.DeploymentException;
/*     */ import javax.websocket.Encoder;
/*     */ import javax.websocket.EndpointConfig;
/*     */ import javax.websocket.Extension;
/*     */ import javax.websocket.MessageHandler;
/*     */ import javax.websocket.MessageHandler.Whole;
/*     */ import javax.websocket.PongMessage;
/*     */ import javax.websocket.Session;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ import org.apache.tomcat.websocket.pojo.PojoMessageHandlerPartialBinary;
/*     */ import org.apache.tomcat.websocket.pojo.PojoMessageHandlerWholeBinary;
/*     */ import org.apache.tomcat.websocket.pojo.PojoMessageHandlerWholeText;
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
/*     */ public class Util
/*     */ {
/*  62 */   private static final StringManager sm = StringManager.getManager(Util.class);
/*  63 */   private static final Queue<SecureRandom> randoms = new ConcurrentLinkedQueue();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static boolean isControl(byte opCode)
/*     */   {
/*  72 */     return (opCode & 0x8) != 0;
/*     */   }
/*     */   
/*     */   static boolean isText(byte opCode)
/*     */   {
/*  77 */     return opCode == 1;
/*     */   }
/*     */   
/*     */   static boolean isContinuation(byte opCode)
/*     */   {
/*  82 */     return opCode == 0;
/*     */   }
/*     */   
/*     */   static CloseReason.CloseCode getCloseCode(int code)
/*     */   {
/*  87 */     if ((code > 2999) && (code < 5000)) {
/*  88 */       return CloseReason.CloseCodes.getCloseCode(code);
/*     */     }
/*  90 */     switch (code) {
/*     */     case 1000: 
/*  92 */       return CloseReason.CloseCodes.NORMAL_CLOSURE;
/*     */     case 1001: 
/*  94 */       return CloseReason.CloseCodes.GOING_AWAY;
/*     */     case 1002: 
/*  96 */       return CloseReason.CloseCodes.PROTOCOL_ERROR;
/*     */     case 1003: 
/*  98 */       return CloseReason.CloseCodes.CANNOT_ACCEPT;
/*     */     
/*     */ 
/*     */     case 1004: 
/* 102 */       return CloseReason.CloseCodes.PROTOCOL_ERROR;
/*     */     
/*     */ 
/*     */     case 1005: 
/* 106 */       return CloseReason.CloseCodes.PROTOCOL_ERROR;
/*     */     
/*     */ 
/*     */     case 1006: 
/* 110 */       return CloseReason.CloseCodes.PROTOCOL_ERROR;
/*     */     case 1007: 
/* 112 */       return CloseReason.CloseCodes.NOT_CONSISTENT;
/*     */     case 1008: 
/* 114 */       return CloseReason.CloseCodes.VIOLATED_POLICY;
/*     */     case 1009: 
/* 116 */       return CloseReason.CloseCodes.TOO_BIG;
/*     */     case 1010: 
/* 118 */       return CloseReason.CloseCodes.NO_EXTENSION;
/*     */     case 1011: 
/* 120 */       return CloseReason.CloseCodes.UNEXPECTED_CONDITION;
/*     */     
/*     */ 
/*     */     case 1012: 
/* 124 */       return CloseReason.CloseCodes.PROTOCOL_ERROR;
/*     */     
/*     */ 
/*     */     case 1013: 
/* 128 */       return CloseReason.CloseCodes.PROTOCOL_ERROR;
/*     */     
/*     */ 
/*     */     case 1015: 
/* 132 */       return CloseReason.CloseCodes.PROTOCOL_ERROR;
/*     */     }
/* 134 */     return CloseReason.CloseCodes.PROTOCOL_ERROR;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static byte[] generateMask()
/*     */   {
/* 146 */     SecureRandom sr = (SecureRandom)randoms.poll();
/*     */     
/*     */ 
/* 149 */     if (sr == null) {
/*     */       try {
/* 151 */         sr = SecureRandom.getInstance("SHA1PRNG");
/*     */       }
/*     */       catch (NoSuchAlgorithmException e) {
/* 154 */         sr = new SecureRandom();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 159 */     byte[] result = new byte[4];
/* 160 */     sr.nextBytes(result);
/*     */     
/*     */ 
/* 163 */     randoms.add(sr);
/*     */     
/* 165 */     return result;
/*     */   }
/*     */   
/*     */   static Class<?> getMessageType(MessageHandler listener)
/*     */   {
/* 170 */     return 
/* 171 */       getGenericType(MessageHandler.class, listener.getClass()).getClazz();
/*     */   }
/*     */   
/*     */   private static Class<?> getDecoderType(Class<? extends Decoder> decoder)
/*     */   {
/* 176 */     return getGenericType(Decoder.class, decoder).getClazz();
/*     */   }
/*     */   
/*     */   static Class<?> getEncoderType(Class<? extends Encoder> encoder)
/*     */   {
/* 181 */     return getGenericType(Encoder.class, encoder).getClazz();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static <T> TypeResult getGenericType(Class<T> type, Class<? extends T> clazz)
/*     */   {
/* 191 */     Type[] interfaces = clazz.getGenericInterfaces();
/* 192 */     for (Type iface : interfaces)
/*     */     {
/* 194 */       if ((iface instanceof ParameterizedType)) {
/* 195 */         ParameterizedType pi = (ParameterizedType)iface;
/*     */         
/* 197 */         if (((pi.getRawType() instanceof Class)) && 
/* 198 */           (type.isAssignableFrom((Class)pi.getRawType()))) {
/* 199 */           return getTypeParameter(clazz, pi
/* 200 */             .getActualTypeArguments()[0]);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 209 */     Object superClazz = clazz.getSuperclass();
/* 210 */     if (superClazz == null)
/*     */     {
/* 212 */       return null;
/*     */     }
/*     */     
/* 215 */     TypeResult superClassTypeResult = getGenericType(type, (Class)superClazz);
/* 216 */     int dimension = superClassTypeResult.getDimension();
/* 217 */     if ((superClassTypeResult.getIndex() == -1) && (dimension == 0))
/*     */     {
/*     */ 
/* 220 */       return superClassTypeResult;
/*     */     }
/*     */     
/* 223 */     if (superClassTypeResult.getIndex() > -1)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 228 */       ParameterizedType superClassType = (ParameterizedType)clazz.getGenericSuperclass();
/* 229 */       TypeResult result = getTypeParameter(clazz, superClassType
/* 230 */         .getActualTypeArguments()[superClassTypeResult
/* 231 */         .getIndex()]);
/* 232 */       result.incrementDimension(superClassTypeResult.getDimension());
/* 233 */       if ((result.getClazz() != null) && (result.getDimension() > 0)) {
/* 234 */         superClassTypeResult = result;
/*     */       } else {
/* 236 */         return result;
/*     */       }
/*     */     }
/*     */     
/* 240 */     if (superClassTypeResult.getDimension() > 0) {
/* 241 */       StringBuilder className = new StringBuilder();
/* 242 */       for (int i = 0; i < dimension; i++) {
/* 243 */         className.append('[');
/*     */       }
/* 245 */       className.append('L');
/* 246 */       className.append(superClassTypeResult.getClazz().getCanonicalName());
/* 247 */       className.append(';');
/*     */       
/*     */       try
/*     */       {
/* 251 */         arrayClazz = Class.forName(className.toString());
/*     */       } catch (ClassNotFoundException e) { Class<?> arrayClazz;
/* 253 */         throw new IllegalArgumentException(e);
/*     */       }
/*     */       Class<?> arrayClazz;
/* 256 */       return new TypeResult(arrayClazz, -1, 0);
/*     */     }
/*     */     
/*     */ 
/* 260 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static TypeResult getTypeParameter(Class<?> clazz, Type argType)
/*     */   {
/* 269 */     if ((argType instanceof Class))
/* 270 */       return new TypeResult((Class)argType, -1, 0);
/* 271 */     if ((argType instanceof ParameterizedType))
/* 272 */       return new TypeResult((Class)((ParameterizedType)argType).getRawType(), -1, 0);
/* 273 */     if ((argType instanceof GenericArrayType)) {
/* 274 */       Type arrayElementType = ((GenericArrayType)argType).getGenericComponentType();
/* 275 */       TypeResult result = getTypeParameter(clazz, arrayElementType);
/* 276 */       result.incrementDimension(1);
/* 277 */       return result;
/*     */     }
/* 279 */     TypeVariable<?>[] tvs = clazz.getTypeParameters();
/* 280 */     for (int i = 0; i < tvs.length; i++) {
/* 281 */       if (tvs[i].equals(argType)) {
/* 282 */         return new TypeResult(null, i, 0);
/*     */       }
/*     */     }
/* 285 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean isPrimitive(Class<?> clazz)
/*     */   {
/* 291 */     if (clazz.isPrimitive())
/* 292 */       return true;
/* 293 */     if ((clazz.equals(Boolean.class)) || 
/* 294 */       (clazz.equals(Byte.class)) || 
/* 295 */       (clazz.equals(Character.class)) || 
/* 296 */       (clazz.equals(Double.class)) || 
/* 297 */       (clazz.equals(Float.class)) || 
/* 298 */       (clazz.equals(Integer.class)) || 
/* 299 */       (clazz.equals(Long.class)) || 
/* 300 */       (clazz.equals(Short.class))) {
/* 301 */       return true;
/*     */     }
/* 303 */     return false;
/*     */   }
/*     */   
/*     */   public static Object coerceToType(Class<?> type, String value)
/*     */   {
/* 308 */     if (type.equals(String.class))
/* 309 */       return value;
/* 310 */     if ((type.equals(Boolean.TYPE)) || (type.equals(Boolean.class)))
/* 311 */       return Boolean.valueOf(value);
/* 312 */     if ((type.equals(Byte.TYPE)) || (type.equals(Byte.class)))
/* 313 */       return Byte.valueOf(value);
/* 314 */     if ((type.equals(Character.TYPE)) || (type.equals(Character.class)))
/* 315 */       return Character.valueOf(value.charAt(0));
/* 316 */     if ((type.equals(Double.TYPE)) || (type.equals(Double.class)))
/* 317 */       return Double.valueOf(value);
/* 318 */     if ((type.equals(Float.TYPE)) || (type.equals(Float.class)))
/* 319 */       return Float.valueOf(value);
/* 320 */     if ((type.equals(Integer.TYPE)) || (type.equals(Integer.class)))
/* 321 */       return Integer.valueOf(value);
/* 322 */     if ((type.equals(Long.TYPE)) || (type.equals(Long.class)))
/* 323 */       return Long.valueOf(value);
/* 324 */     if ((type.equals(Short.TYPE)) || (type.equals(Short.class))) {
/* 325 */       return Short.valueOf(value);
/*     */     }
/* 327 */     throw new IllegalArgumentException(sm.getString("util.invalidType", new Object[] { value, type
/* 328 */       .getName() }));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<DecoderEntry> getDecoders(List<Class<? extends Decoder>> decoderClazzes)
/*     */     throws DeploymentException
/*     */   {
/* 337 */     List<DecoderEntry> result = new ArrayList();
/* 338 */     if (decoderClazzes != null) {
/* 339 */       for (Class<? extends Decoder> decoderClazz : decoderClazzes)
/*     */       {
/*     */ 
/*     */         try
/*     */         {
/*     */ 
/* 345 */           instance = (Decoder)decoderClazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/*     */         } catch (ReflectiveOperationException e) {
/*     */           Decoder instance;
/* 348 */           throw new DeploymentException(sm.getString("pojoMethodMapping.invalidDecoder", new Object[] {decoderClazz
/* 349 */             .getName() }), e);
/*     */         }
/*     */         Decoder instance;
/* 352 */         DecoderEntry entry = new DecoderEntry(getDecoderType(decoderClazz), decoderClazz);
/* 353 */         result.add(entry);
/*     */       }
/*     */     }
/*     */     
/* 357 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static Set<MessageHandlerResult> getMessageHandlers(Class<?> target, MessageHandler listener, EndpointConfig endpointConfig, Session session)
/*     */   {
/* 366 */     Set<MessageHandlerResult> results = new HashSet(2);
/*     */     
/*     */ 
/*     */ 
/* 370 */     if (String.class.isAssignableFrom(target)) {
/* 371 */       MessageHandlerResult result = new MessageHandlerResult(listener, MessageHandlerResultType.TEXT);
/*     */       
/*     */ 
/* 374 */       results.add(result);
/* 375 */     } else if (ByteBuffer.class.isAssignableFrom(target)) {
/* 376 */       MessageHandlerResult result = new MessageHandlerResult(listener, MessageHandlerResultType.BINARY);
/*     */       
/*     */ 
/* 379 */       results.add(result);
/* 380 */     } else if (PongMessage.class.isAssignableFrom(target)) {
/* 381 */       MessageHandlerResult result = new MessageHandlerResult(listener, MessageHandlerResultType.PONG);
/*     */       
/*     */ 
/* 384 */       results.add(result);
/*     */ 
/*     */     }
/* 387 */     else if (byte[].class.isAssignableFrom(target)) {
/* 388 */       boolean whole = MessageHandler.Whole.class.isAssignableFrom(listener.getClass());
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 395 */       MessageHandlerResult result = new MessageHandlerResult((MessageHandler)(whole ? new PojoMessageHandlerWholeBinary(listener, getOnMessageMethod(listener), session, endpointConfig, matchDecoders(target, endpointConfig, true), new Object[1], 0, true, -1, false, -1L) : new PojoMessageHandlerPartialBinary(listener, getOnMessagePartialMethod(listener), session, new Object[2], 0, true, 1, -1, -1L)), MessageHandlerResultType.BINARY);
/*     */       
/*     */ 
/* 398 */       results.add(result);
/* 399 */     } else if (InputStream.class.isAssignableFrom(target))
/*     */     {
/*     */ 
/*     */ 
/* 403 */       MessageHandlerResult result = new MessageHandlerResult(new PojoMessageHandlerWholeBinary(listener, getOnMessageMethod(listener), session, endpointConfig, matchDecoders(target, endpointConfig, true), new Object[1], 0, true, -1, true, -1L), MessageHandlerResultType.BINARY);
/*     */       
/*     */ 
/* 406 */       results.add(result);
/* 407 */     } else if (Reader.class.isAssignableFrom(target))
/*     */     {
/*     */ 
/*     */ 
/* 411 */       MessageHandlerResult result = new MessageHandlerResult(new PojoMessageHandlerWholeText(listener, getOnMessageMethod(listener), session, endpointConfig, matchDecoders(target, endpointConfig, false), new Object[1], 0, true, -1, -1L), MessageHandlerResultType.TEXT);
/*     */       
/*     */ 
/* 414 */       results.add(result);
/*     */     }
/*     */     else
/*     */     {
/* 418 */       DecoderMatch decoderMatch = matchDecoders(target, endpointConfig);
/* 419 */       Method m = getOnMessageMethod(listener);
/* 420 */       if (decoderMatch.getBinaryDecoders().size() > 0)
/*     */       {
/*     */ 
/*     */ 
/* 424 */         MessageHandlerResult result = new MessageHandlerResult(new PojoMessageHandlerWholeBinary(listener, m, session, endpointConfig, decoderMatch.getBinaryDecoders(), new Object[1], 0, false, -1, false, -1L), MessageHandlerResultType.BINARY);
/*     */         
/*     */ 
/* 427 */         results.add(result);
/*     */       }
/* 429 */       if (decoderMatch.getTextDecoders().size() > 0)
/*     */       {
/*     */ 
/*     */ 
/* 433 */         MessageHandlerResult result = new MessageHandlerResult(new PojoMessageHandlerWholeText(listener, m, session, endpointConfig, decoderMatch.getTextDecoders(), new Object[1], 0, false, -1, -1L), MessageHandlerResultType.TEXT);
/*     */         
/*     */ 
/* 436 */         results.add(result);
/*     */       }
/*     */     }
/*     */     
/* 440 */     if (results.size() == 0)
/*     */     {
/* 442 */       throw new IllegalArgumentException(sm.getString("wsSession.unknownHandler", new Object[] { listener, target }));
/*     */     }
/*     */     
/* 445 */     return results;
/*     */   }
/*     */   
/*     */   private static List<Class<? extends Decoder>> matchDecoders(Class<?> target, EndpointConfig endpointConfig, boolean binary)
/*     */   {
/* 450 */     DecoderMatch decoderMatch = matchDecoders(target, endpointConfig);
/* 451 */     if (binary) {
/* 452 */       if (decoderMatch.getBinaryDecoders().size() > 0) {
/* 453 */         return decoderMatch.getBinaryDecoders();
/*     */       }
/* 455 */     } else if (decoderMatch.getTextDecoders().size() > 0) {
/* 456 */       return decoderMatch.getTextDecoders();
/*     */     }
/* 458 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   private static DecoderMatch matchDecoders(Class<?> target, EndpointConfig endpointConfig)
/*     */   {
/*     */     try
/*     */     {
/* 466 */       List<Class<? extends Decoder>> decoders = endpointConfig.getDecoders();
/* 467 */       List<DecoderEntry> decoderEntries = getDecoders(decoders);
/* 468 */       decoderMatch = new DecoderMatch(target, decoderEntries);
/*     */     } catch (DeploymentException e) { DecoderMatch decoderMatch;
/* 470 */       throw new IllegalArgumentException(e); }
/*     */     DecoderMatch decoderMatch;
/* 472 */     return decoderMatch;
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
/*     */   public static void parseExtensionHeader(List<Extension> extensions, String header)
/*     */   {
/* 493 */     String[] unparsedExtensions = header.split(",");
/* 494 */     for (String unparsedExtension : unparsedExtensions)
/*     */     {
/*     */ 
/* 497 */       String[] unparsedParameters = unparsedExtension.split(";");
/* 498 */       WsExtension extension = new WsExtension(unparsedParameters[0].trim());
/*     */       
/* 500 */       for (int i = 1; i < unparsedParameters.length; i++) {
/* 501 */         int equalsPos = unparsedParameters[i].indexOf('=');
/*     */         String value;
/*     */         String name;
/* 504 */         String value; if (equalsPos == -1) {
/* 505 */           String name = unparsedParameters[i].trim();
/* 506 */           value = null;
/*     */         } else {
/* 508 */           name = unparsedParameters[i].substring(0, equalsPos).trim();
/* 509 */           value = unparsedParameters[i].substring(equalsPos + 1).trim();
/* 510 */           int len = value.length();
/* 511 */           if ((len > 1) && 
/* 512 */             (value.charAt(0) == '"') && (value.charAt(len - 1) == '"')) {
/* 513 */             value = value.substring(1, value.length() - 1);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 519 */         if ((containsDelims(name)) || (containsDelims(value))) {
/* 520 */           throw new IllegalArgumentException(sm.getString("util.notToken", new Object[] { name, value }));
/*     */         }
/*     */         
/* 523 */         if ((value != null) && (
/* 524 */           (value.indexOf(',') > -1) || (value.indexOf(';') > -1) || 
/* 525 */           (value.indexOf('"') > -1) || (value.indexOf('=') > -1))) {
/* 526 */           throw new IllegalArgumentException(sm.getString("", new Object[] { value }));
/*     */         }
/* 528 */         extension.addParameter(new WsExtensionParameter(name, value));
/*     */       }
/* 530 */       extensions.add(extension);
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean containsDelims(String input)
/*     */   {
/* 536 */     if ((input == null) || (input.length() == 0)) {
/* 537 */       return false;
/*     */     }
/* 539 */     for (char c : input.toCharArray()) {
/* 540 */       switch (c) {
/*     */       case '"': 
/*     */       case ',': 
/*     */       case ';': 
/*     */       case '=': 
/* 545 */         return true;
/*     */       }
/*     */       
/*     */     }
/*     */     
/*     */ 
/* 551 */     return false;
/*     */   }
/*     */   
/*     */   private static Method getOnMessageMethod(MessageHandler listener) {
/*     */     try {
/* 556 */       return listener.getClass().getMethod("onMessage", new Class[] { Object.class });
/*     */     }
/*     */     catch (NoSuchMethodException|SecurityException e) {
/* 559 */       throw new IllegalArgumentException(sm.getString("util.invalidMessageHandler"), e);
/*     */     }
/*     */   }
/*     */   
/*     */   private static Method getOnMessagePartialMethod(MessageHandler listener) {
/*     */     try {
/* 565 */       return listener.getClass().getMethod("onMessage", new Class[] { Object.class, Boolean.TYPE });
/*     */     }
/*     */     catch (NoSuchMethodException|SecurityException e) {
/* 568 */       throw new IllegalArgumentException(sm.getString("util.invalidMessageHandler"), e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class DecoderMatch
/*     */   {
/* 575 */     private final List<Class<? extends Decoder>> textDecoders = new ArrayList();
/*     */     
/* 577 */     private final List<Class<? extends Decoder>> binaryDecoders = new ArrayList();
/*     */     private final Class<?> target;
/*     */     
/*     */     public DecoderMatch(Class<?> target, List<DecoderEntry> decoderEntries)
/*     */     {
/* 582 */       this.target = target;
/* 583 */       for (DecoderEntry decoderEntry : decoderEntries) {
/* 584 */         if (decoderEntry.getClazz().isAssignableFrom(target)) {
/* 585 */           if (Decoder.Binary.class.isAssignableFrom(decoderEntry
/* 586 */             .getDecoderClazz())) {
/* 587 */             this.binaryDecoders.add(decoderEntry.getDecoderClazz());
/*     */           }
/*     */           else
/*     */           {
/* 591 */             if (Decoder.BinaryStream.class.isAssignableFrom(decoderEntry
/* 592 */               .getDecoderClazz())) {
/* 593 */               this.binaryDecoders.add(decoderEntry.getDecoderClazz());
/*     */               
/*     */ 
/* 596 */               break; }
/* 597 */             if (Decoder.Text.class.isAssignableFrom(decoderEntry
/* 598 */               .getDecoderClazz())) {
/* 599 */               this.textDecoders.add(decoderEntry.getDecoderClazz());
/*     */             }
/*     */             else
/*     */             {
/* 603 */               if (Decoder.TextStream.class.isAssignableFrom(decoderEntry
/* 604 */                 .getDecoderClazz())) {
/* 605 */                 this.textDecoders.add(decoderEntry.getDecoderClazz());
/*     */                 
/*     */ 
/* 608 */                 break;
/*     */               }
/*     */               
/* 611 */               throw new IllegalArgumentException(Util.sm.getString("util.unknownDecoderType"));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public List<Class<? extends Decoder>> getTextDecoders() {
/* 619 */       return this.textDecoders;
/*     */     }
/*     */     
/*     */     public List<Class<? extends Decoder>> getBinaryDecoders()
/*     */     {
/* 624 */       return this.binaryDecoders;
/*     */     }
/*     */     
/*     */     public Class<?> getTarget()
/*     */     {
/* 629 */       return this.target;
/*     */     }
/*     */     
/*     */     public boolean hasMatches()
/*     */     {
/* 634 */       return (this.textDecoders.size() > 0) || (this.binaryDecoders.size() > 0);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TypeResult
/*     */   {
/*     */     private final Class<?> clazz;
/*     */     private final int index;
/*     */     private int dimension;
/*     */     
/*     */     public TypeResult(Class<?> clazz, int index, int dimension) {
/* 645 */       this.clazz = clazz;
/* 646 */       this.index = index;
/* 647 */       this.dimension = dimension;
/*     */     }
/*     */     
/*     */     public Class<?> getClazz() {
/* 651 */       return this.clazz;
/*     */     }
/*     */     
/*     */     public int getIndex() {
/* 655 */       return this.index;
/*     */     }
/*     */     
/*     */     public int getDimension() {
/* 659 */       return this.dimension;
/*     */     }
/*     */     
/*     */     public void incrementDimension(int inc) {
/* 663 */       this.dimension += inc;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\Util.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */