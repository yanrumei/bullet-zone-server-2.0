/*     */ package org.apache.el.lang;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.beans.PropertyEditorManager;
/*     */ import java.lang.reflect.Array;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.el.ELContext;
/*     */ import javax.el.ELException;
/*     */ import javax.el.ELResolver;
/*     */ import org.apache.el.util.MessageFactory;
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
/*     */ public class ELSupport
/*     */ {
/*  43 */   private static final Long ZERO = Long.valueOf(0L);
/*     */   
/*     */ 
/*  46 */   private static final boolean IS_SECURITY_ENABLED = System.getSecurityManager() != null;
/*     */   protected static final boolean COERCE_TO_ZERO;
/*     */   
/*     */   static {
/*     */     String coerceToZeroStr;
/*     */     String coerceToZeroStr;
/*  52 */     if (IS_SECURITY_ENABLED) {
/*  53 */       coerceToZeroStr = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public String run()
/*     */         {
/*  57 */           return System.getProperty("org.apache.el.parser.COERCE_TO_ZERO", "false");
/*     */         }
/*     */         
/*     */ 
/*     */       });
/*     */     }
/*     */     else
/*     */     {
/*  65 */       coerceToZeroStr = System.getProperty("org.apache.el.parser.COERCE_TO_ZERO", "false");
/*     */     }
/*     */     
/*  68 */     COERCE_TO_ZERO = Boolean.parseBoolean(coerceToZeroStr);
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
/*     */   public static final int compare(ELContext ctx, Object obj0, Object obj1)
/*     */     throws ELException
/*     */   {
/* 104 */     if ((obj0 == obj1) || (equals(ctx, obj0, obj1))) {
/* 105 */       return 0;
/*     */     }
/* 107 */     if (isBigDecimalOp(obj0, obj1)) {
/* 108 */       BigDecimal bd0 = (BigDecimal)coerceToNumber(ctx, obj0, BigDecimal.class);
/* 109 */       BigDecimal bd1 = (BigDecimal)coerceToNumber(ctx, obj1, BigDecimal.class);
/* 110 */       return bd0.compareTo(bd1);
/*     */     }
/* 112 */     if (isDoubleOp(obj0, obj1)) {
/* 113 */       Double d0 = (Double)coerceToNumber(ctx, obj0, Double.class);
/* 114 */       Double d1 = (Double)coerceToNumber(ctx, obj1, Double.class);
/* 115 */       return d0.compareTo(d1);
/*     */     }
/* 117 */     if (isBigIntegerOp(obj0, obj1)) {
/* 118 */       BigInteger bi0 = (BigInteger)coerceToNumber(ctx, obj0, BigInteger.class);
/* 119 */       BigInteger bi1 = (BigInteger)coerceToNumber(ctx, obj1, BigInteger.class);
/* 120 */       return bi0.compareTo(bi1);
/*     */     }
/* 122 */     if (isLongOp(obj0, obj1)) {
/* 123 */       Long l0 = (Long)coerceToNumber(ctx, obj0, Long.class);
/* 124 */       Long l1 = (Long)coerceToNumber(ctx, obj1, Long.class);
/* 125 */       return l0.compareTo(l1);
/*     */     }
/* 127 */     if (((obj0 instanceof String)) || ((obj1 instanceof String))) {
/* 128 */       return coerceToString(ctx, obj0).compareTo(coerceToString(ctx, obj1));
/*     */     }
/* 130 */     if ((obj0 instanceof Comparable))
/*     */     {
/* 132 */       Comparable<Object> comparable = (Comparable)obj0;
/* 133 */       return obj1 != null ? comparable.compareTo(obj1) : 1;
/*     */     }
/* 135 */     if ((obj1 instanceof Comparable))
/*     */     {
/* 137 */       Comparable<Object> comparable = (Comparable)obj1;
/* 138 */       return obj0 != null ? -comparable.compareTo(obj0) : -1;
/*     */     }
/* 140 */     throw new ELException(MessageFactory.get("error.compare", new Object[] { obj0, obj1 }));
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
/*     */   public static final boolean equals(ELContext ctx, Object obj0, Object obj1)
/*     */     throws ELException
/*     */   {
/* 160 */     if (obj0 == obj1)
/* 161 */       return true;
/* 162 */     if ((obj0 == null) || (obj1 == null))
/* 163 */       return false;
/* 164 */     if (isBigDecimalOp(obj0, obj1)) {
/* 165 */       BigDecimal bd0 = (BigDecimal)coerceToNumber(ctx, obj0, BigDecimal.class);
/* 166 */       BigDecimal bd1 = (BigDecimal)coerceToNumber(ctx, obj1, BigDecimal.class);
/* 167 */       return bd0.equals(bd1); }
/* 168 */     if (isDoubleOp(obj0, obj1)) {
/* 169 */       Double d0 = (Double)coerceToNumber(ctx, obj0, Double.class);
/* 170 */       Double d1 = (Double)coerceToNumber(ctx, obj1, Double.class);
/* 171 */       return d0.equals(d1); }
/* 172 */     if (isBigIntegerOp(obj0, obj1)) {
/* 173 */       BigInteger bi0 = (BigInteger)coerceToNumber(ctx, obj0, BigInteger.class);
/* 174 */       BigInteger bi1 = (BigInteger)coerceToNumber(ctx, obj1, BigInteger.class);
/* 175 */       return bi0.equals(bi1); }
/* 176 */     if (isLongOp(obj0, obj1)) {
/* 177 */       Long l0 = (Long)coerceToNumber(ctx, obj0, Long.class);
/* 178 */       Long l1 = (Long)coerceToNumber(ctx, obj1, Long.class);
/* 179 */       return l0.equals(l1); }
/* 180 */     if (((obj0 instanceof Boolean)) || ((obj1 instanceof Boolean)))
/* 181 */       return coerceToBoolean(ctx, obj0, false).equals(coerceToBoolean(ctx, obj1, false));
/* 182 */     if (obj0.getClass().isEnum())
/* 183 */       return obj0.equals(coerceToEnum(ctx, obj1, obj0.getClass()));
/* 184 */     if (obj1.getClass().isEnum())
/* 185 */       return obj1.equals(coerceToEnum(ctx, obj0, obj1.getClass()));
/* 186 */     if (((obj0 instanceof String)) || ((obj1 instanceof String))) {
/* 187 */       int lexCompare = coerceToString(ctx, obj0).compareTo(coerceToString(ctx, obj1));
/* 188 */       return lexCompare == 0;
/*     */     }
/* 190 */     return obj0.equals(obj1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final Enum<?> coerceToEnum(ELContext ctx, Object obj, Class type)
/*     */   {
/* 201 */     if (ctx != null) {
/* 202 */       boolean originalIsPropertyResolved = ctx.isPropertyResolved();
/*     */       try {
/* 204 */         Object result = ctx.getELResolver().convertToType(ctx, obj, type);
/* 205 */         if (ctx.isPropertyResolved()) {
/* 206 */           return (Enum)result;
/*     */         }
/*     */       } finally {
/* 209 */         ctx.setPropertyResolved(originalIsPropertyResolved);
/*     */       }
/*     */     }
/*     */     
/* 213 */     if ((obj == null) || ("".equals(obj))) {
/* 214 */       return null;
/*     */     }
/* 216 */     if (type.isAssignableFrom(obj.getClass())) {
/* 217 */       return (Enum)obj;
/*     */     }
/*     */     
/* 220 */     if (!(obj instanceof String)) {
/* 221 */       throw new ELException(MessageFactory.get("error.convert", new Object[] { obj, obj
/* 222 */         .getClass(), type }));
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 227 */       result = Enum.valueOf(type, (String)obj);
/*     */     } catch (IllegalArgumentException iae) { Enum<?> result;
/* 229 */       throw new ELException(MessageFactory.get("error.convert", new Object[] { obj, obj
/* 230 */         .getClass(), type })); }
/*     */     Enum<?> result;
/* 232 */     return result;
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
/*     */   public static final Boolean coerceToBoolean(ELContext ctx, Object obj, boolean primitive)
/*     */     throws ELException
/*     */   {
/* 248 */     if (ctx != null) {
/* 249 */       boolean originalIsPropertyResolved = ctx.isPropertyResolved();
/*     */       try {
/* 251 */         Object result = ctx.getELResolver().convertToType(ctx, obj, Boolean.class);
/* 252 */         if (ctx.isPropertyResolved()) {
/* 253 */           return (Boolean)result;
/*     */         }
/*     */       } finally {
/* 256 */         ctx.setPropertyResolved(originalIsPropertyResolved);
/*     */       }
/*     */     }
/*     */     
/* 260 */     if ((!COERCE_TO_ZERO) && (!primitive) && 
/* 261 */       (obj == null)) {
/* 262 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 266 */     if ((obj == null) || ("".equals(obj))) {
/* 267 */       return Boolean.FALSE;
/*     */     }
/* 269 */     if ((obj instanceof Boolean)) {
/* 270 */       return (Boolean)obj;
/*     */     }
/* 272 */     if ((obj instanceof String)) {
/* 273 */       return Boolean.valueOf((String)obj);
/*     */     }
/*     */     
/* 276 */     throw new ELException(MessageFactory.get("error.convert", new Object[] { obj, obj
/* 277 */       .getClass(), Boolean.class }));
/*     */   }
/*     */   
/*     */   private static final Character coerceToCharacter(ELContext ctx, Object obj)
/*     */     throws ELException
/*     */   {
/* 283 */     if (ctx != null) {
/* 284 */       boolean originalIsPropertyResolved = ctx.isPropertyResolved();
/*     */       try {
/* 286 */         Object result = ctx.getELResolver().convertToType(ctx, obj, Character.class);
/* 287 */         if (ctx.isPropertyResolved()) {
/* 288 */           return (Character)result;
/*     */         }
/*     */       } finally {
/* 291 */         ctx.setPropertyResolved(originalIsPropertyResolved);
/*     */       }
/*     */     }
/*     */     
/* 295 */     if ((obj == null) || ("".equals(obj))) {
/* 296 */       return Character.valueOf('\000');
/*     */     }
/* 298 */     if ((obj instanceof String)) {
/* 299 */       return Character.valueOf(((String)obj).charAt(0));
/*     */     }
/* 301 */     if (ELArithmetic.isNumber(obj)) {
/* 302 */       return Character.valueOf((char)((Number)obj).shortValue());
/*     */     }
/* 304 */     Class<?> objType = obj.getClass();
/* 305 */     if ((obj instanceof Character)) {
/* 306 */       return (Character)obj;
/*     */     }
/*     */     
/* 309 */     throw new ELException(MessageFactory.get("error.convert", new Object[] { obj, objType, Character.class }));
/*     */   }
/*     */   
/*     */   protected static final Number coerceToNumber(Number number, Class<?> type)
/*     */     throws ELException
/*     */   {
/* 315 */     if ((Long.TYPE == type) || (Long.class.equals(type))) {
/* 316 */       return Long.valueOf(number.longValue());
/*     */     }
/* 318 */     if ((Double.TYPE == type) || (Double.class.equals(type))) {
/* 319 */       return Double.valueOf(number.doubleValue());
/*     */     }
/* 321 */     if ((Integer.TYPE == type) || (Integer.class.equals(type))) {
/* 322 */       return Integer.valueOf(number.intValue());
/*     */     }
/* 324 */     if (BigInteger.class.equals(type)) {
/* 325 */       if ((number instanceof BigDecimal)) {
/* 326 */         return ((BigDecimal)number).toBigInteger();
/*     */       }
/* 328 */       if ((number instanceof BigInteger)) {
/* 329 */         return number;
/*     */       }
/* 331 */       return BigInteger.valueOf(number.longValue());
/*     */     }
/* 333 */     if (BigDecimal.class.equals(type)) {
/* 334 */       if ((number instanceof BigDecimal)) {
/* 335 */         return number;
/*     */       }
/* 337 */       if ((number instanceof BigInteger)) {
/* 338 */         return new BigDecimal((BigInteger)number);
/*     */       }
/* 340 */       return new BigDecimal(number.doubleValue());
/*     */     }
/* 342 */     if ((Byte.TYPE == type) || (Byte.class.equals(type))) {
/* 343 */       return Byte.valueOf(number.byteValue());
/*     */     }
/* 345 */     if ((Short.TYPE == type) || (Short.class.equals(type))) {
/* 346 */       return Short.valueOf(number.shortValue());
/*     */     }
/* 348 */     if ((Float.TYPE == type) || (Float.class.equals(type))) {
/* 349 */       return Float.valueOf(number.floatValue());
/*     */     }
/* 351 */     if (Number.class.equals(type)) {
/* 352 */       return number;
/*     */     }
/*     */     
/* 355 */     throw new ELException(MessageFactory.get("error.convert", new Object[] { number, number
/* 356 */       .getClass(), type }));
/*     */   }
/*     */   
/*     */   public static final Number coerceToNumber(ELContext ctx, Object obj, Class<?> type)
/*     */     throws ELException
/*     */   {
/* 362 */     if (ctx != null) {
/* 363 */       boolean originalIsPropertyResolved = ctx.isPropertyResolved();
/*     */       try {
/* 365 */         Object result = ctx.getELResolver().convertToType(ctx, obj, type);
/* 366 */         if (ctx.isPropertyResolved()) {
/* 367 */           return (Number)result;
/*     */         }
/*     */       } finally {
/* 370 */         ctx.setPropertyResolved(originalIsPropertyResolved);
/*     */       }
/*     */     }
/*     */     
/* 374 */     if ((!COERCE_TO_ZERO) && 
/* 375 */       (obj == null) && (!type.isPrimitive())) {
/* 376 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 380 */     if ((obj == null) || ("".equals(obj))) {
/* 381 */       return coerceToNumber(ZERO, type);
/*     */     }
/* 383 */     if ((obj instanceof String)) {
/* 384 */       return coerceToNumber((String)obj, type);
/*     */     }
/* 386 */     if (ELArithmetic.isNumber(obj)) {
/* 387 */       return coerceToNumber((Number)obj, type);
/*     */     }
/*     */     
/* 390 */     if ((obj instanceof Character)) {
/* 391 */       return coerceToNumber(Short.valueOf(
/* 392 */         (short)((Character)obj).charValue()), type);
/*     */     }
/*     */     
/* 395 */     throw new ELException(MessageFactory.get("error.convert", new Object[] { obj, obj
/* 396 */       .getClass(), type }));
/*     */   }
/*     */   
/*     */   protected static final Number coerceToNumber(String val, Class<?> type) throws ELException
/*     */   {
/* 401 */     if ((Long.TYPE == type) || (Long.class.equals(type))) {
/*     */       try {
/* 403 */         return Long.valueOf(val);
/*     */       } catch (NumberFormatException nfe) {
/* 405 */         throw new ELException(MessageFactory.get("error.convert", new Object[] { val, String.class, type }));
/*     */       }
/*     */     }
/*     */     
/* 409 */     if ((Integer.TYPE == type) || (Integer.class.equals(type))) {
/*     */       try {
/* 411 */         return Integer.valueOf(val);
/*     */       } catch (NumberFormatException nfe) {
/* 413 */         throw new ELException(MessageFactory.get("error.convert", new Object[] { val, String.class, type }));
/*     */       }
/*     */     }
/*     */     
/* 417 */     if ((Double.TYPE == type) || (Double.class.equals(type))) {
/*     */       try {
/* 419 */         return Double.valueOf(val);
/*     */       } catch (NumberFormatException nfe) {
/* 421 */         throw new ELException(MessageFactory.get("error.convert", new Object[] { val, String.class, type }));
/*     */       }
/*     */     }
/*     */     
/* 425 */     if (BigInteger.class.equals(type)) {
/*     */       try {
/* 427 */         return new BigInteger(val);
/*     */       } catch (NumberFormatException nfe) {
/* 429 */         throw new ELException(MessageFactory.get("error.convert", new Object[] { val, String.class, type }));
/*     */       }
/*     */     }
/*     */     
/* 433 */     if (BigDecimal.class.equals(type)) {
/*     */       try {
/* 435 */         return new BigDecimal(val);
/*     */       } catch (NumberFormatException nfe) {
/* 437 */         throw new ELException(MessageFactory.get("error.convert", new Object[] { val, String.class, type }));
/*     */       }
/*     */     }
/*     */     
/* 441 */     if ((Byte.TYPE == type) || (Byte.class.equals(type))) {
/*     */       try {
/* 443 */         return Byte.valueOf(val);
/*     */       } catch (NumberFormatException nfe) {
/* 445 */         throw new ELException(MessageFactory.get("error.convert", new Object[] { val, String.class, type }));
/*     */       }
/*     */     }
/*     */     
/* 449 */     if ((Short.TYPE == type) || (Short.class.equals(type))) {
/*     */       try {
/* 451 */         return Short.valueOf(val);
/*     */       } catch (NumberFormatException nfe) {
/* 453 */         throw new ELException(MessageFactory.get("error.convert", new Object[] { val, String.class, type }));
/*     */       }
/*     */     }
/*     */     
/* 457 */     if ((Float.TYPE == type) || (Float.class.equals(type))) {
/*     */       try {
/* 459 */         return Float.valueOf(val);
/*     */       } catch (NumberFormatException nfe) {
/* 461 */         throw new ELException(MessageFactory.get("error.convert", new Object[] { val, String.class, type }));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 466 */     throw new ELException(MessageFactory.get("error.convert", new Object[] { val, String.class, type }));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String coerceToString(ELContext ctx, Object obj)
/*     */   {
/* 478 */     if (ctx != null) {
/* 479 */       boolean originalIsPropertyResolved = ctx.isPropertyResolved();
/*     */       try {
/* 481 */         Object result = ctx.getELResolver().convertToType(ctx, obj, String.class);
/* 482 */         if (ctx.isPropertyResolved()) {
/* 483 */           return (String)result;
/*     */         }
/*     */       } finally {
/* 486 */         ctx.setPropertyResolved(originalIsPropertyResolved);
/*     */       }
/*     */     }
/*     */     
/* 490 */     if (obj == null)
/* 491 */       return "";
/* 492 */     if ((obj instanceof String))
/* 493 */       return (String)obj;
/* 494 */     if ((obj instanceof Enum)) {
/* 495 */       return ((Enum)obj).name();
/*     */     }
/* 497 */     return obj.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public static final Object coerceToType(ELContext ctx, Object obj, Class<?> type)
/*     */     throws ELException
/*     */   {
/* 504 */     if (ctx != null) {
/* 505 */       boolean originalIsPropertyResolved = ctx.isPropertyResolved();
/*     */       try {
/* 507 */         Object result = ctx.getELResolver().convertToType(ctx, obj, type);
/* 508 */         if (ctx.isPropertyResolved()) {
/* 509 */           return result;
/*     */         }
/*     */       } finally {
/* 512 */         ctx.setPropertyResolved(originalIsPropertyResolved);
/*     */       }
/*     */     }
/*     */     
/* 516 */     if ((type == null) || (Object.class.equals(type)) || ((obj != null) && 
/* 517 */       (type.isAssignableFrom(obj.getClass())))) {
/* 518 */       return obj;
/*     */     }
/*     */     
/* 521 */     if ((!COERCE_TO_ZERO) && 
/* 522 */       (obj == null) && (!type.isPrimitive()) && 
/* 523 */       (!String.class.isAssignableFrom(type))) {
/* 524 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 528 */     if (String.class.equals(type)) {
/* 529 */       return coerceToString(ctx, obj);
/*     */     }
/* 531 */     if (ELArithmetic.isNumberType(type)) {
/* 532 */       return coerceToNumber(ctx, obj, type);
/*     */     }
/* 534 */     if ((Character.class.equals(type)) || (Character.TYPE == type)) {
/* 535 */       return coerceToCharacter(ctx, obj);
/*     */     }
/* 537 */     if ((Boolean.class.equals(type)) || (Boolean.TYPE == type)) {
/* 538 */       return coerceToBoolean(ctx, obj, Boolean.TYPE == type);
/*     */     }
/* 540 */     if (type.isEnum()) {
/* 541 */       return coerceToEnum(ctx, obj, type);
/*     */     }
/*     */     
/*     */ 
/* 545 */     if (obj == null)
/* 546 */       return null;
/* 547 */     if ((obj instanceof String)) {
/* 548 */       PropertyEditor editor = PropertyEditorManager.findEditor(type);
/* 549 */       if (editor == null) {
/* 550 */         if ("".equals(obj)) {
/* 551 */           return null;
/*     */         }
/* 553 */         throw new ELException(MessageFactory.get("error.convert", new Object[] { obj, obj
/* 554 */           .getClass(), type }));
/*     */       }
/*     */       try {
/* 557 */         editor.setAsText((String)obj);
/* 558 */         return editor.getValue();
/*     */       } catch (RuntimeException e) {
/* 560 */         if ("".equals(obj)) {
/* 561 */           return null;
/*     */         }
/* 563 */         throw new ELException(MessageFactory.get("error.convert", new Object[] { obj, obj
/* 564 */           .getClass(), type }), e);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 571 */     if (((obj instanceof Set)) && (type == Map.class) && 
/* 572 */       (((Set)obj).isEmpty())) {
/* 573 */       return Collections.EMPTY_MAP;
/*     */     }
/*     */     
/*     */ 
/* 577 */     if ((type.isArray()) && (obj.getClass().isArray())) {
/* 578 */       return coerceToArray(ctx, obj, type);
/*     */     }
/*     */     
/* 581 */     throw new ELException(MessageFactory.get("error.convert", new Object[] { obj, obj
/* 582 */       .getClass(), type }));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Object coerceToArray(ELContext ctx, Object obj, Class<?> type)
/*     */   {
/* 591 */     int size = Array.getLength(obj);
/*     */     
/*     */ 
/*     */ 
/* 595 */     Class<?> componentType = type.getComponentType();
/*     */     
/* 597 */     Object result = Array.newInstance(componentType, size);
/*     */     
/* 599 */     for (int i = 0; i < size; i++) {
/* 600 */       Array.set(result, i, coerceToType(ctx, Array.get(obj, i), componentType));
/*     */     }
/*     */     
/* 603 */     return result;
/*     */   }
/*     */   
/*     */   public static final boolean isBigDecimalOp(Object obj0, Object obj1)
/*     */   {
/* 608 */     return ((obj0 instanceof BigDecimal)) || ((obj1 instanceof BigDecimal));
/*     */   }
/*     */   
/*     */   public static final boolean isBigIntegerOp(Object obj0, Object obj1)
/*     */   {
/* 613 */     return ((obj0 instanceof BigInteger)) || ((obj1 instanceof BigInteger));
/*     */   }
/*     */   
/*     */   public static final boolean isDoubleOp(Object obj0, Object obj1) {
/* 617 */     return ((obj0 instanceof Double)) || ((obj1 instanceof Double)) || ((obj0 instanceof Float)) || ((obj1 instanceof Float));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static final boolean isLongOp(Object obj0, Object obj1)
/*     */   {
/* 624 */     return ((obj0 instanceof Long)) || ((obj1 instanceof Long)) || ((obj0 instanceof Integer)) || ((obj1 instanceof Integer)) || ((obj0 instanceof Character)) || ((obj1 instanceof Character)) || ((obj0 instanceof Short)) || ((obj1 instanceof Short)) || ((obj0 instanceof Byte)) || ((obj1 instanceof Byte));
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
/*     */   public static final boolean isStringFloat(String str)
/*     */   {
/* 637 */     int len = str.length();
/* 638 */     if (len > 1) {
/* 639 */       for (int i = 0; i < len; i++) {
/* 640 */         switch (str.charAt(i)) {
/*     */         case 'E': 
/* 642 */           return true;
/*     */         case 'e': 
/* 644 */           return true;
/*     */         case '.': 
/* 646 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 650 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\lang\ELSupport.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */