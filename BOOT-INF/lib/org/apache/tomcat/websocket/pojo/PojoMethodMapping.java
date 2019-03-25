/*     */ package org.apache.tomcat.websocket.pojo;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.websocket.CloseReason;
/*     */ import javax.websocket.DecodeException;
/*     */ import javax.websocket.Decoder;
/*     */ import javax.websocket.DeploymentException;
/*     */ import javax.websocket.EndpointConfig;
/*     */ import javax.websocket.MessageHandler;
/*     */ import javax.websocket.OnClose;
/*     */ import javax.websocket.OnError;
/*     */ import javax.websocket.OnMessage;
/*     */ import javax.websocket.OnOpen;
/*     */ import javax.websocket.PongMessage;
/*     */ import javax.websocket.Session;
/*     */ import javax.websocket.server.PathParam;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ import org.apache.tomcat.websocket.DecoderEntry;
/*     */ import org.apache.tomcat.websocket.Util;
/*     */ import org.apache.tomcat.websocket.Util.DecoderMatch;
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
/*     */ public class PojoMethodMapping
/*     */ {
/*  61 */   private static final StringManager sm = StringManager.getManager(PojoMethodMapping.class);
/*     */   
/*     */   private final Method onOpen;
/*     */   private final Method onClose;
/*     */   private final Method onError;
/*     */   private final PojoPathParam[] onOpenParams;
/*     */   private final PojoPathParam[] onCloseParams;
/*     */   private final PojoPathParam[] onErrorParams;
/*  69 */   private final List<MessageHandlerInfo> onMessage = new ArrayList();
/*     */   
/*     */   private final String wsPath;
/*     */   
/*     */ 
/*     */   public PojoMethodMapping(Class<?> clazzPojo, List<Class<? extends Decoder>> decoderClazzes, String wsPath)
/*     */     throws DeploymentException
/*     */   {
/*  77 */     this.wsPath = wsPath;
/*     */     
/*  79 */     List<DecoderEntry> decoders = Util.getDecoders(decoderClazzes);
/*  80 */     Method open = null;
/*  81 */     Method close = null;
/*  82 */     Method error = null;
/*  83 */     Method[] clazzPojoMethods = null;
/*  84 */     Class<?> currentClazz = clazzPojo;
/*  85 */     while (!currentClazz.equals(Object.class)) {
/*  86 */       Method[] currentClazzMethods = currentClazz.getDeclaredMethods();
/*  87 */       if (currentClazz == clazzPojo) {
/*  88 */         clazzPojoMethods = currentClazzMethods;
/*     */       }
/*  90 */       for (Method method : currentClazzMethods) {
/*  91 */         if (method.getAnnotation(OnOpen.class) != null) {
/*  92 */           checkPublic(method);
/*  93 */           if (open == null) {
/*  94 */             open = method;
/*     */           }
/*  96 */           else if ((currentClazz == clazzPojo) || 
/*  97 */             (!isMethodOverride(open, method)))
/*     */           {
/*  99 */             throw new DeploymentException(sm.getString("pojoMethodMapping.duplicateAnnotation", new Object[] { OnOpen.class, currentClazz }));
/*     */           }
/*     */           
/*     */ 
/*     */         }
/* 104 */         else if (method.getAnnotation(OnClose.class) != null) {
/* 105 */           checkPublic(method);
/* 106 */           if (close == null) {
/* 107 */             close = method;
/*     */           }
/* 109 */           else if ((currentClazz == clazzPojo) || 
/* 110 */             (!isMethodOverride(close, method)))
/*     */           {
/* 112 */             throw new DeploymentException(sm.getString("pojoMethodMapping.duplicateAnnotation", new Object[] { OnClose.class, currentClazz }));
/*     */           }
/*     */           
/*     */ 
/*     */         }
/* 117 */         else if (method.getAnnotation(OnError.class) != null) {
/* 118 */           checkPublic(method);
/* 119 */           if (error == null) {
/* 120 */             error = method;
/*     */           }
/* 122 */           else if ((currentClazz == clazzPojo) || 
/* 123 */             (!isMethodOverride(error, method)))
/*     */           {
/* 125 */             throw new DeploymentException(sm.getString("pojoMethodMapping.duplicateAnnotation", new Object[] { OnError.class, currentClazz }));
/*     */           }
/*     */           
/*     */ 
/*     */         }
/* 130 */         else if (method.getAnnotation(OnMessage.class) != null) {
/* 131 */           checkPublic(method);
/* 132 */           MessageHandlerInfo messageHandler = new MessageHandlerInfo(method, decoders);
/* 133 */           boolean found = false;
/* 134 */           for (MessageHandlerInfo otherMessageHandler : this.onMessage) {
/* 135 */             if (messageHandler.targetsSameWebSocketMessageType(otherMessageHandler)) {
/* 136 */               found = true;
/* 137 */               if ((currentClazz == clazzPojo) || 
/* 138 */                 (!isMethodOverride(messageHandler.m, otherMessageHandler.m)))
/*     */               {
/* 140 */                 throw new DeploymentException(sm.getString("pojoMethodMapping.duplicateAnnotation", new Object[] { OnMessage.class, currentClazz }));
/*     */               }
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 146 */           if (!found) {
/* 147 */             this.onMessage.add(messageHandler);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 153 */       currentClazz = currentClazz.getSuperclass();
/*     */     }
/*     */     
/*     */ 
/* 157 */     if ((open != null) && (open.getDeclaringClass() != clazzPojo) && 
/* 158 */       (isOverridenWithoutAnnotation(clazzPojoMethods, open, OnOpen.class))) {
/* 159 */       open = null;
/*     */     }
/*     */     
/* 162 */     if ((close != null) && (close.getDeclaringClass() != clazzPojo) && 
/* 163 */       (isOverridenWithoutAnnotation(clazzPojoMethods, close, OnClose.class))) {
/* 164 */       close = null;
/*     */     }
/*     */     
/* 167 */     if ((error != null) && (error.getDeclaringClass() != clazzPojo) && 
/* 168 */       (isOverridenWithoutAnnotation(clazzPojoMethods, error, OnError.class))) {
/* 169 */       error = null;
/*     */     }
/*     */     
/* 172 */     List<MessageHandlerInfo> overriddenOnMessage = new ArrayList();
/* 173 */     for (??? = this.onMessage.iterator(); ((Iterator)???).hasNext();) { MessageHandlerInfo messageHandler = (MessageHandlerInfo)((Iterator)???).next();
/* 174 */       if ((messageHandler.m.getDeclaringClass() != clazzPojo) && 
/* 175 */         (isOverridenWithoutAnnotation(clazzPojoMethods, messageHandler.m, OnMessage.class))) {
/* 176 */         overriddenOnMessage.add(messageHandler);
/*     */       }
/*     */     }
/* 179 */     for (??? = overriddenOnMessage.iterator(); ((Iterator)???).hasNext();) { MessageHandlerInfo messageHandler = (MessageHandlerInfo)((Iterator)???).next();
/* 180 */       this.onMessage.remove(messageHandler);
/*     */     }
/* 182 */     this.onOpen = open;
/* 183 */     this.onClose = close;
/* 184 */     this.onError = error;
/* 185 */     this.onOpenParams = getPathParams(this.onOpen, MethodType.ON_OPEN);
/* 186 */     this.onCloseParams = getPathParams(this.onClose, MethodType.ON_CLOSE);
/* 187 */     this.onErrorParams = getPathParams(this.onError, MethodType.ON_ERROR);
/*     */   }
/*     */   
/*     */   private void checkPublic(Method m) throws DeploymentException
/*     */   {
/* 192 */     if (!Modifier.isPublic(m.getModifiers())) {
/* 193 */       throw new DeploymentException(sm.getString("pojoMethodMapping.methodNotPublic", new Object[] {m
/* 194 */         .getName() }));
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isMethodOverride(Method method1, Method method2)
/*     */   {
/* 200 */     return (method1.getName().equals(method2.getName())) && 
/* 201 */       (method1.getReturnType().equals(method2.getReturnType())) && 
/* 202 */       (Arrays.equals(method1.getParameterTypes(), method2.getParameterTypes()));
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean isOverridenWithoutAnnotation(Method[] methods, Method superclazzMethod, Class<? extends Annotation> annotation)
/*     */   {
/* 208 */     for (Method method : methods) {
/* 209 */       if ((isMethodOverride(method, superclazzMethod)) && 
/* 210 */         (method.getAnnotation(annotation) == null)) {
/* 211 */         return true;
/*     */       }
/*     */     }
/* 214 */     return false;
/*     */   }
/*     */   
/*     */   public String getWsPath()
/*     */   {
/* 219 */     return this.wsPath;
/*     */   }
/*     */   
/*     */   public Method getOnOpen()
/*     */   {
/* 224 */     return this.onOpen;
/*     */   }
/*     */   
/*     */   public Object[] getOnOpenArgs(Map<String, String> pathParameters, Session session, EndpointConfig config)
/*     */     throws DecodeException
/*     */   {
/* 230 */     return buildArgs(this.onOpenParams, pathParameters, session, config, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */   public Method getOnClose()
/*     */   {
/* 236 */     return this.onClose;
/*     */   }
/*     */   
/*     */   public Object[] getOnCloseArgs(Map<String, String> pathParameters, Session session, CloseReason closeReason)
/*     */     throws DecodeException
/*     */   {
/* 242 */     return buildArgs(this.onCloseParams, pathParameters, session, null, null, closeReason);
/*     */   }
/*     */   
/*     */ 
/*     */   public Method getOnError()
/*     */   {
/* 248 */     return this.onError;
/*     */   }
/*     */   
/*     */   public Object[] getOnErrorArgs(Map<String, String> pathParameters, Session session, Throwable throwable)
/*     */     throws DecodeException
/*     */   {
/* 254 */     return buildArgs(this.onErrorParams, pathParameters, session, null, throwable, null);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean hasMessageHandlers()
/*     */   {
/* 260 */     return !this.onMessage.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Set<MessageHandler> getMessageHandlers(Object pojo, Map<String, String> pathParameters, Session session, EndpointConfig config)
/*     */   {
/* 267 */     Set<MessageHandler> result = new HashSet();
/* 268 */     for (MessageHandlerInfo messageMethod : this.onMessage) {
/* 269 */       result.addAll(messageMethod.getMessageHandlers(pojo, pathParameters, session, config));
/*     */     }
/*     */     
/* 272 */     return result;
/*     */   }
/*     */   
/*     */   private static PojoPathParam[] getPathParams(Method m, MethodType methodType)
/*     */     throws DeploymentException
/*     */   {
/* 278 */     if (m == null) {
/* 279 */       return new PojoPathParam[0];
/*     */     }
/* 281 */     boolean foundThrowable = false;
/* 282 */     Class<?>[] types = m.getParameterTypes();
/* 283 */     Annotation[][] paramsAnnotations = m.getParameterAnnotations();
/* 284 */     PojoPathParam[] result = new PojoPathParam[types.length];
/* 285 */     for (int i = 0; i < types.length; i++) {
/* 286 */       Class<?> type = types[i];
/* 287 */       if (type.equals(Session.class)) {
/* 288 */         result[i] = new PojoPathParam(type, null);
/* 289 */       } else if ((methodType == MethodType.ON_OPEN) && 
/* 290 */         (type.equals(EndpointConfig.class))) {
/* 291 */         result[i] = new PojoPathParam(type, null);
/* 292 */       } else if ((methodType == MethodType.ON_ERROR) && 
/* 293 */         (type.equals(Throwable.class))) {
/* 294 */         foundThrowable = true;
/* 295 */         result[i] = new PojoPathParam(type, null);
/* 296 */       } else if ((methodType == MethodType.ON_CLOSE) && 
/* 297 */         (type.equals(CloseReason.class))) {
/* 298 */         result[i] = new PojoPathParam(type, null);
/*     */       } else {
/* 300 */         Annotation[] paramAnnotations = paramsAnnotations[i];
/* 301 */         for (Annotation paramAnnotation : paramAnnotations) {
/* 302 */           if (paramAnnotation.annotationType().equals(PathParam.class))
/*     */           {
/*     */ 
/*     */             try
/*     */             {
/* 307 */               Util.coerceToType(type, "0");
/*     */             } catch (IllegalArgumentException iae) {
/* 309 */               throw new DeploymentException(sm.getString("pojoMethodMapping.invalidPathParamType"), iae);
/*     */             }
/*     */             
/*     */ 
/*     */ 
/* 314 */             result[i] = new PojoPathParam(type, ((PathParam)paramAnnotation).value());
/* 315 */             break;
/*     */           }
/*     */         }
/*     */         
/* 319 */         if (result[i] == null) {
/* 320 */           throw new DeploymentException(sm.getString("pojoMethodMapping.paramWithoutAnnotation", new Object[] { type, m
/*     */           
/* 322 */             .getName(), m.getClass().getName() }));
/*     */         }
/*     */       }
/*     */     }
/* 326 */     if ((methodType == MethodType.ON_ERROR) && (!foundThrowable)) {
/* 327 */       throw new DeploymentException(sm.getString("pojoMethodMapping.onErrorNoThrowable", new Object[] {m
/*     */       
/* 329 */         .getName(), m.getDeclaringClass().getName() }));
/*     */     }
/* 331 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static Object[] buildArgs(PojoPathParam[] pathParams, Map<String, String> pathParameters, Session session, EndpointConfig config, Throwable throwable, CloseReason closeReason)
/*     */     throws DecodeException
/*     */   {
/* 339 */     Object[] result = new Object[pathParams.length];
/* 340 */     for (int i = 0; i < pathParams.length; i++) {
/* 341 */       Class<?> type = pathParams[i].getType();
/* 342 */       if (type.equals(Session.class)) {
/* 343 */         result[i] = session;
/* 344 */       } else if (type.equals(EndpointConfig.class)) {
/* 345 */         result[i] = config;
/* 346 */       } else if (type.equals(Throwable.class)) {
/* 347 */         result[i] = throwable;
/* 348 */       } else if (type.equals(CloseReason.class)) {
/* 349 */         result[i] = closeReason;
/*     */       } else {
/* 351 */         String name = pathParams[i].getName();
/* 352 */         String value = (String)pathParameters.get(name);
/*     */         try {
/* 354 */           result[i] = Util.coerceToType(type, value);
/*     */         } catch (Exception e) {
/* 356 */           throw new DecodeException(value, sm.getString("pojoMethodMapping.decodePathParamFail", new Object[] { value, type }), e);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 362 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   private static class MessageHandlerInfo
/*     */   {
/*     */     private final Method m;
/* 369 */     private int indexString = -1;
/* 370 */     private int indexByteArray = -1;
/* 371 */     private int indexByteBuffer = -1;
/* 372 */     private int indexPong = -1;
/* 373 */     private int indexBoolean = -1;
/* 374 */     private int indexSession = -1;
/* 375 */     private int indexInputStream = -1;
/* 376 */     private int indexReader = -1;
/* 377 */     private int indexPrimitive = -1;
/* 378 */     private Class<?> primitiveType = null;
/* 379 */     private Map<Integer, PojoPathParam> indexPathParams = new HashMap();
/* 380 */     private int indexPayload = -1;
/* 381 */     private Util.DecoderMatch decoderMatch = null;
/* 382 */     private long maxMessageSize = -1L;
/*     */     
/*     */     public MessageHandlerInfo(Method m, List<DecoderEntry> decoderEntries) {
/* 385 */       this.m = m;
/*     */       
/* 387 */       Class<?>[] types = m.getParameterTypes();
/* 388 */       Annotation[][] paramsAnnotations = m.getParameterAnnotations();
/*     */       
/* 390 */       for (int i = 0; i < types.length; i++) {
/* 391 */         boolean paramFound = false;
/* 392 */         Annotation[] paramAnnotations = paramsAnnotations[i];
/* 393 */         for (Annotation paramAnnotation : paramAnnotations) {
/* 394 */           if (paramAnnotation.annotationType().equals(PathParam.class))
/*     */           {
/* 396 */             this.indexPathParams.put(
/* 397 */               Integer.valueOf(i), new PojoPathParam(types[i], ((PathParam)paramAnnotation)
/* 398 */               .value()));
/* 399 */             paramFound = true;
/* 400 */             break;
/*     */           }
/*     */         }
/* 403 */         if (!paramFound)
/*     */         {
/*     */ 
/* 406 */           if (String.class.isAssignableFrom(types[i])) {
/* 407 */             if (this.indexString == -1) {
/* 408 */               this.indexString = i;
/*     */             } else {
/* 410 */               throw new IllegalArgumentException(PojoMethodMapping.sm.getString("pojoMethodMapping.duplicateMessageParam", new Object[] {m
/*     */               
/* 412 */                 .getName(), m.getDeclaringClass().getName() }));
/*     */             }
/* 414 */           } else if (Reader.class.isAssignableFrom(types[i])) {
/* 415 */             if (this.indexReader == -1) {
/* 416 */               this.indexReader = i;
/*     */             } else {
/* 418 */               throw new IllegalArgumentException(PojoMethodMapping.sm.getString("pojoMethodMapping.duplicateMessageParam", new Object[] {m
/*     */               
/* 420 */                 .getName(), m.getDeclaringClass().getName() }));
/*     */             }
/* 422 */           } else if (Boolean.TYPE == types[i]) {
/* 423 */             if (this.indexBoolean == -1) {
/* 424 */               this.indexBoolean = i;
/*     */             } else {
/* 426 */               throw new IllegalArgumentException(PojoMethodMapping.sm.getString("pojoMethodMapping.duplicateLastParam", new Object[] {m
/*     */               
/* 428 */                 .getName(), m.getDeclaringClass().getName() }));
/*     */             }
/* 430 */           } else if (ByteBuffer.class.isAssignableFrom(types[i])) {
/* 431 */             if (this.indexByteBuffer == -1) {
/* 432 */               this.indexByteBuffer = i;
/*     */             } else {
/* 434 */               throw new IllegalArgumentException(PojoMethodMapping.sm.getString("pojoMethodMapping.duplicateMessageParam", new Object[] {m
/*     */               
/* 436 */                 .getName(), m.getDeclaringClass().getName() }));
/*     */             }
/* 438 */           } else if (byte[].class == types[i]) {
/* 439 */             if (this.indexByteArray == -1) {
/* 440 */               this.indexByteArray = i;
/*     */             } else {
/* 442 */               throw new IllegalArgumentException(PojoMethodMapping.sm.getString("pojoMethodMapping.duplicateMessageParam", new Object[] {m
/*     */               
/* 444 */                 .getName(), m.getDeclaringClass().getName() }));
/*     */             }
/* 446 */           } else if (InputStream.class.isAssignableFrom(types[i])) {
/* 447 */             if (this.indexInputStream == -1) {
/* 448 */               this.indexInputStream = i;
/*     */             } else {
/* 450 */               throw new IllegalArgumentException(PojoMethodMapping.sm.getString("pojoMethodMapping.duplicateMessageParam", new Object[] {m
/*     */               
/* 452 */                 .getName(), m.getDeclaringClass().getName() }));
/*     */             }
/* 454 */           } else if (Util.isPrimitive(types[i])) {
/* 455 */             if (this.indexPrimitive == -1) {
/* 456 */               this.indexPrimitive = i;
/* 457 */               this.primitiveType = types[i];
/*     */             } else {
/* 459 */               throw new IllegalArgumentException(PojoMethodMapping.sm.getString("pojoMethodMapping.duplicateMessageParam", new Object[] {m
/*     */               
/* 461 */                 .getName(), m.getDeclaringClass().getName() }));
/*     */             }
/* 463 */           } else if (Session.class.isAssignableFrom(types[i])) {
/* 464 */             if (this.indexSession == -1) {
/* 465 */               this.indexSession = i;
/*     */             } else {
/* 467 */               throw new IllegalArgumentException(PojoMethodMapping.sm.getString("pojoMethodMapping.duplicateSessionParam", new Object[] {m
/*     */               
/* 469 */                 .getName(), m.getDeclaringClass().getName() }));
/*     */             }
/* 471 */           } else if (PongMessage.class.isAssignableFrom(types[i])) {
/* 472 */             if (this.indexPong == -1) {
/* 473 */               this.indexPong = i;
/*     */             } else {
/* 475 */               throw new IllegalArgumentException(PojoMethodMapping.sm.getString("pojoMethodMapping.duplicatePongMessageParam", new Object[] {m
/*     */               
/* 477 */                 .getName(), m.getDeclaringClass().getName() }));
/*     */             }
/*     */           } else {
/* 480 */             if ((this.decoderMatch != null) && (this.decoderMatch.hasMatches())) {
/* 481 */               throw new IllegalArgumentException(PojoMethodMapping.sm.getString("pojoMethodMapping.duplicateMessageParam", new Object[] {m
/*     */               
/* 483 */                 .getName(), m.getDeclaringClass().getName() }));
/*     */             }
/* 485 */             this.decoderMatch = new Util.DecoderMatch(types[i], decoderEntries);
/*     */             
/* 487 */             if (this.decoderMatch.hasMatches()) {
/* 488 */               this.indexPayload = i;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 494 */       if (this.indexString != -1) {
/* 495 */         if (this.indexPayload != -1) {
/* 496 */           throw new IllegalArgumentException(PojoMethodMapping.sm.getString("pojoMethodMapping.duplicateMessageParam", new Object[] {m
/*     */           
/* 498 */             .getName(), m.getDeclaringClass().getName() }));
/*     */         }
/* 500 */         this.indexPayload = this.indexString;
/*     */       }
/*     */       
/* 503 */       if (this.indexReader != -1) {
/* 504 */         if (this.indexPayload != -1) {
/* 505 */           throw new IllegalArgumentException(PojoMethodMapping.sm.getString("pojoMethodMapping.duplicateMessageParam", new Object[] {m
/*     */           
/* 507 */             .getName(), m.getDeclaringClass().getName() }));
/*     */         }
/* 509 */         this.indexPayload = this.indexReader;
/*     */       }
/*     */       
/* 512 */       if (this.indexByteArray != -1) {
/* 513 */         if (this.indexPayload != -1) {
/* 514 */           throw new IllegalArgumentException(PojoMethodMapping.sm.getString("pojoMethodMapping.duplicateMessageParam", new Object[] {m
/*     */           
/* 516 */             .getName(), m.getDeclaringClass().getName() }));
/*     */         }
/* 518 */         this.indexPayload = this.indexByteArray;
/*     */       }
/*     */       
/* 521 */       if (this.indexByteBuffer != -1) {
/* 522 */         if (this.indexPayload != -1) {
/* 523 */           throw new IllegalArgumentException(PojoMethodMapping.sm.getString("pojoMethodMapping.duplicateMessageParam", new Object[] {m
/*     */           
/* 525 */             .getName(), m.getDeclaringClass().getName() }));
/*     */         }
/* 527 */         this.indexPayload = this.indexByteBuffer;
/*     */       }
/*     */       
/* 530 */       if (this.indexInputStream != -1) {
/* 531 */         if (this.indexPayload != -1) {
/* 532 */           throw new IllegalArgumentException(PojoMethodMapping.sm.getString("pojoMethodMapping.duplicateMessageParam", new Object[] {m
/*     */           
/* 534 */             .getName(), m.getDeclaringClass().getName() }));
/*     */         }
/* 536 */         this.indexPayload = this.indexInputStream;
/*     */       }
/*     */       
/* 539 */       if (this.indexPrimitive != -1) {
/* 540 */         if (this.indexPayload != -1) {
/* 541 */           throw new IllegalArgumentException(PojoMethodMapping.sm.getString("pojoMethodMapping.duplicateMessageParam", new Object[] {m
/*     */           
/* 543 */             .getName(), m.getDeclaringClass().getName() }));
/*     */         }
/* 545 */         this.indexPayload = this.indexPrimitive;
/*     */       }
/*     */       
/* 548 */       if (this.indexPong != -1) {
/* 549 */         if (this.indexPayload != -1) {
/* 550 */           throw new IllegalArgumentException(PojoMethodMapping.sm.getString("pojoMethodMapping.pongWithPayload", new Object[] {m
/*     */           
/* 552 */             .getName(), m.getDeclaringClass().getName() }));
/*     */         }
/* 554 */         this.indexPayload = this.indexPong;
/*     */       }
/*     */       
/* 557 */       if ((this.indexPayload == -1) && (this.indexPrimitive == -1) && (this.indexBoolean != -1))
/*     */       {
/*     */ 
/* 560 */         this.indexPayload = this.indexBoolean;
/* 561 */         this.indexPrimitive = this.indexBoolean;
/* 562 */         this.primitiveType = Boolean.TYPE;
/* 563 */         this.indexBoolean = -1;
/*     */       }
/* 565 */       if (this.indexPayload == -1) {
/* 566 */         throw new IllegalArgumentException(PojoMethodMapping.sm.getString("pojoMethodMapping.noPayload", new Object[] {m
/*     */         
/* 568 */           .getName(), m.getDeclaringClass().getName() }));
/*     */       }
/* 570 */       if ((this.indexPong != -1) && (this.indexBoolean != -1)) {
/* 571 */         throw new IllegalArgumentException(PojoMethodMapping.sm.getString("pojoMethodMapping.partialPong", new Object[] {m
/*     */         
/* 573 */           .getName(), m.getDeclaringClass().getName() }));
/*     */       }
/* 575 */       if ((this.indexReader != -1) && (this.indexBoolean != -1)) {
/* 576 */         throw new IllegalArgumentException(PojoMethodMapping.sm.getString("pojoMethodMapping.partialReader", new Object[] {m
/*     */         
/* 578 */           .getName(), m.getDeclaringClass().getName() }));
/*     */       }
/* 580 */       if ((this.indexInputStream != -1) && (this.indexBoolean != -1)) {
/* 581 */         throw new IllegalArgumentException(PojoMethodMapping.sm.getString("pojoMethodMapping.partialInputStream", new Object[] {m
/*     */         
/* 583 */           .getName(), m.getDeclaringClass().getName() }));
/*     */       }
/* 585 */       if ((this.decoderMatch != null) && (this.decoderMatch.hasMatches()) && (this.indexBoolean != -1))
/*     */       {
/* 587 */         throw new IllegalArgumentException(PojoMethodMapping.sm.getString("pojoMethodMapping.partialObject", new Object[] {m
/*     */         
/* 589 */           .getName(), m.getDeclaringClass().getName() }));
/*     */       }
/*     */       
/* 592 */       this.maxMessageSize = ((OnMessage)m.getAnnotation(OnMessage.class)).maxMessageSize();
/*     */     }
/*     */     
/*     */     public boolean targetsSameWebSocketMessageType(MessageHandlerInfo otherHandler)
/*     */     {
/* 597 */       if (otherHandler == null) {
/* 598 */         return false;
/*     */       }
/* 600 */       if ((this.indexByteArray >= 0) && (otherHandler.indexByteArray >= 0)) {
/* 601 */         return true;
/*     */       }
/* 603 */       if ((this.indexByteBuffer >= 0) && (otherHandler.indexByteBuffer >= 0)) {
/* 604 */         return true;
/*     */       }
/* 606 */       if ((this.indexInputStream >= 0) && (otherHandler.indexInputStream >= 0)) {
/* 607 */         return true;
/*     */       }
/* 609 */       if ((this.indexPong >= 0) && (otherHandler.indexPong >= 0)) {
/* 610 */         return true;
/*     */       }
/* 612 */       if ((this.indexPrimitive >= 0) && (otherHandler.indexPrimitive >= 0) && (this.primitiveType == otherHandler.primitiveType))
/*     */       {
/* 614 */         return true;
/*     */       }
/* 616 */       if ((this.indexReader >= 0) && (otherHandler.indexReader >= 0)) {
/* 617 */         return true;
/*     */       }
/* 619 */       if ((this.indexString >= 0) && (otherHandler.indexString >= 0)) {
/* 620 */         return true;
/*     */       }
/* 622 */       if ((this.decoderMatch != null) && (otherHandler.decoderMatch != null) && 
/* 623 */         (this.decoderMatch.getTarget().equals(otherHandler.decoderMatch.getTarget()))) {
/* 624 */         return true;
/*     */       }
/* 626 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Set<MessageHandler> getMessageHandlers(Object pojo, Map<String, String> pathParameters, Session session, EndpointConfig config)
/*     */     {
/* 633 */       Object[] params = new Object[this.m.getParameterTypes().length];
/*     */       
/*     */ 
/* 636 */       for (Map.Entry<Integer, PojoPathParam> entry : this.indexPathParams.entrySet()) {
/* 637 */         PojoPathParam pathParam = (PojoPathParam)entry.getValue();
/* 638 */         String valueString = (String)pathParameters.get(pathParam.getName());
/* 639 */         Object value = null;
/*     */         try {
/* 641 */           value = Util.coerceToType(pathParam.getType(), valueString);
/*     */         }
/*     */         catch (Exception e) {
/* 644 */           DecodeException de = new DecodeException(valueString, PojoMethodMapping.sm.getString("pojoMethodMapping.decodePathParamFail", new Object[] { valueString, pathParam
/*     */           
/* 646 */             .getType() }), e);
/* 647 */           params = new Object[] { de };
/*     */         }
/* 649 */         params[((Integer)entry.getKey()).intValue()] = value;
/*     */       }
/*     */       
/* 652 */       Object results = new HashSet(2);
/* 653 */       if (this.indexBoolean == -1)
/*     */       {
/* 655 */         if ((this.indexString != -1) || (this.indexPrimitive != -1)) {
/* 656 */           MessageHandler mh = new PojoMessageHandlerWholeText(pojo, this.m, session, config, null, params, this.indexPayload, false, this.indexSession, this.maxMessageSize);
/*     */           
/*     */ 
/* 659 */           ((Set)results).add(mh);
/* 660 */         } else if (this.indexReader != -1) {
/* 661 */           MessageHandler mh = new PojoMessageHandlerWholeText(pojo, this.m, session, config, null, params, this.indexReader, true, this.indexSession, this.maxMessageSize);
/*     */           
/*     */ 
/* 664 */           ((Set)results).add(mh);
/* 665 */         } else if (this.indexByteArray != -1) {
/* 666 */           MessageHandler mh = new PojoMessageHandlerWholeBinary(pojo, this.m, session, config, null, params, this.indexByteArray, true, this.indexSession, false, this.maxMessageSize);
/*     */           
/*     */ 
/* 669 */           ((Set)results).add(mh);
/* 670 */         } else if (this.indexByteBuffer != -1) {
/* 671 */           MessageHandler mh = new PojoMessageHandlerWholeBinary(pojo, this.m, session, config, null, params, this.indexByteBuffer, false, this.indexSession, false, this.maxMessageSize);
/*     */           
/*     */ 
/* 674 */           ((Set)results).add(mh);
/* 675 */         } else if (this.indexInputStream != -1) {
/* 676 */           MessageHandler mh = new PojoMessageHandlerWholeBinary(pojo, this.m, session, config, null, params, this.indexInputStream, true, this.indexSession, true, this.maxMessageSize);
/*     */           
/*     */ 
/* 679 */           ((Set)results).add(mh);
/* 680 */         } else if ((this.decoderMatch != null) && (this.decoderMatch.hasMatches())) {
/* 681 */           if (this.decoderMatch.getBinaryDecoders().size() > 0)
/*     */           {
/*     */ 
/* 684 */             MessageHandler mh = new PojoMessageHandlerWholeBinary(pojo, this.m, session, config, this.decoderMatch.getBinaryDecoders(), params, this.indexPayload, true, this.indexSession, true, this.maxMessageSize);
/*     */             
/*     */ 
/* 687 */             ((Set)results).add(mh);
/*     */           }
/* 689 */           if (this.decoderMatch.getTextDecoders().size() > 0)
/*     */           {
/*     */ 
/* 692 */             MessageHandler mh = new PojoMessageHandlerWholeText(pojo, this.m, session, config, this.decoderMatch.getTextDecoders(), params, this.indexPayload, true, this.indexSession, this.maxMessageSize);
/*     */             
/* 694 */             ((Set)results).add(mh);
/*     */           }
/*     */         } else {
/* 697 */           MessageHandler mh = new PojoMessageHandlerWholePong(pojo, this.m, session, params, this.indexPong, false, this.indexSession);
/*     */           
/* 699 */           ((Set)results).add(mh);
/*     */         }
/*     */         
/*     */       }
/* 703 */       else if (this.indexString != -1) {
/* 704 */         MessageHandler mh = new PojoMessageHandlerPartialText(pojo, this.m, session, params, this.indexString, false, this.indexBoolean, this.indexSession, this.maxMessageSize);
/*     */         
/*     */ 
/* 707 */         ((Set)results).add(mh);
/* 708 */       } else if (this.indexByteArray != -1) {
/* 709 */         MessageHandler mh = new PojoMessageHandlerPartialBinary(pojo, this.m, session, params, this.indexByteArray, true, this.indexBoolean, this.indexSession, this.maxMessageSize);
/*     */         
/*     */ 
/* 712 */         ((Set)results).add(mh);
/*     */       } else {
/* 714 */         MessageHandler mh = new PojoMessageHandlerPartialBinary(pojo, this.m, session, params, this.indexByteBuffer, false, this.indexBoolean, this.indexSession, this.maxMessageSize);
/*     */         
/*     */ 
/* 717 */         ((Set)results).add(mh);
/*     */       }
/*     */       
/* 720 */       return (Set<MessageHandler>)results;
/*     */     }
/*     */   }
/*     */   
/*     */   private static enum MethodType
/*     */   {
/* 726 */     ON_OPEN, 
/* 727 */     ON_CLOSE, 
/* 728 */     ON_ERROR;
/*     */     
/*     */     private MethodType() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\pojo\PojoMethodMapping.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */