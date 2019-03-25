/*     */ package org.apache.el.lang;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.math.RoundingMode;
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
/*     */ public abstract class ELArithmetic
/*     */ {
/*     */   public static final class BigDecimalDelegate
/*     */     extends ELArithmetic
/*     */   {
/*     */     protected Number add(Number num0, Number num1)
/*     */     {
/*  37 */       return ((BigDecimal)num0).add((BigDecimal)num1);
/*     */     }
/*     */     
/*     */     protected Number coerce(Number num)
/*     */     {
/*  42 */       if ((num instanceof BigDecimal))
/*  43 */         return num;
/*  44 */       if ((num instanceof BigInteger))
/*  45 */         return new BigDecimal((BigInteger)num);
/*  46 */       return new BigDecimal(num.doubleValue());
/*     */     }
/*     */     
/*     */     protected Number coerce(String str)
/*     */     {
/*  51 */       return new BigDecimal(str);
/*     */     }
/*     */     
/*     */     protected Number divide(Number num0, Number num1)
/*     */     {
/*  56 */       return ((BigDecimal)num0).divide((BigDecimal)num1, RoundingMode.HALF_UP);
/*     */     }
/*     */     
/*     */ 
/*     */     protected Number subtract(Number num0, Number num1)
/*     */     {
/*  62 */       return ((BigDecimal)num0).subtract((BigDecimal)num1);
/*     */     }
/*     */     
/*     */     protected Number mod(Number num0, Number num1)
/*     */     {
/*  67 */       return Double.valueOf(num0.doubleValue() % num1.doubleValue());
/*     */     }
/*     */     
/*     */     protected Number multiply(Number num0, Number num1)
/*     */     {
/*  72 */       return ((BigDecimal)num0).multiply((BigDecimal)num1);
/*     */     }
/*     */     
/*     */     public boolean matches(Object obj0, Object obj1)
/*     */     {
/*  77 */       return ((obj0 instanceof BigDecimal)) || ((obj1 instanceof BigDecimal));
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class BigIntegerDelegate extends ELArithmetic
/*     */   {
/*     */     protected Number add(Number num0, Number num1)
/*     */     {
/*  85 */       return ((BigInteger)num0).add((BigInteger)num1);
/*     */     }
/*     */     
/*     */     protected Number coerce(Number num)
/*     */     {
/*  90 */       if ((num instanceof BigInteger))
/*  91 */         return num;
/*  92 */       return new BigInteger(num.toString());
/*     */     }
/*     */     
/*     */     protected Number coerce(String str)
/*     */     {
/*  97 */       return new BigInteger(str);
/*     */     }
/*     */     
/*     */     protected Number divide(Number num0, Number num1)
/*     */     {
/* 102 */       return new BigDecimal((BigInteger)num0).divide(new BigDecimal((BigInteger)num1), RoundingMode.HALF_UP);
/*     */     }
/*     */     
/*     */     protected Number multiply(Number num0, Number num1)
/*     */     {
/* 107 */       return ((BigInteger)num0).multiply((BigInteger)num1);
/*     */     }
/*     */     
/*     */     protected Number mod(Number num0, Number num1)
/*     */     {
/* 112 */       return ((BigInteger)num0).mod((BigInteger)num1);
/*     */     }
/*     */     
/*     */     protected Number subtract(Number num0, Number num1)
/*     */     {
/* 117 */       return ((BigInteger)num0).subtract((BigInteger)num1);
/*     */     }
/*     */     
/*     */     public boolean matches(Object obj0, Object obj1)
/*     */     {
/* 122 */       return ((obj0 instanceof BigInteger)) || ((obj1 instanceof BigInteger));
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class DoubleDelegate
/*     */     extends ELArithmetic
/*     */   {
/*     */     protected Number add(Number num0, Number num1)
/*     */     {
/* 131 */       if ((num0 instanceof BigDecimal))
/* 132 */         return ((BigDecimal)num0).add(new BigDecimal(num1.doubleValue()));
/* 133 */       if ((num1 instanceof BigDecimal)) {
/* 134 */         return new BigDecimal(num0.doubleValue()).add((BigDecimal)num1);
/*     */       }
/* 136 */       return Double.valueOf(num0.doubleValue() + num1.doubleValue());
/*     */     }
/*     */     
/*     */     protected Number coerce(Number num)
/*     */     {
/* 141 */       if ((num instanceof Double))
/* 142 */         return num;
/* 143 */       if ((num instanceof BigInteger))
/* 144 */         return new BigDecimal((BigInteger)num);
/* 145 */       return Double.valueOf(num.doubleValue());
/*     */     }
/*     */     
/*     */     protected Number coerce(String str)
/*     */     {
/* 150 */       return Double.valueOf(str);
/*     */     }
/*     */     
/*     */     protected Number divide(Number num0, Number num1)
/*     */     {
/* 155 */       return Double.valueOf(num0.doubleValue() / num1.doubleValue());
/*     */     }
/*     */     
/*     */     protected Number mod(Number num0, Number num1)
/*     */     {
/* 160 */       return Double.valueOf(num0.doubleValue() % num1.doubleValue());
/*     */     }
/*     */     
/*     */ 
/*     */     protected Number subtract(Number num0, Number num1)
/*     */     {
/* 166 */       if ((num0 instanceof BigDecimal))
/* 167 */         return ((BigDecimal)num0).subtract(new BigDecimal(num1.doubleValue()));
/* 168 */       if ((num1 instanceof BigDecimal)) {
/* 169 */         return new BigDecimal(num0.doubleValue()).subtract((BigDecimal)num1);
/*     */       }
/* 171 */       return Double.valueOf(num0.doubleValue() - num1.doubleValue());
/*     */     }
/*     */     
/*     */ 
/*     */     protected Number multiply(Number num0, Number num1)
/*     */     {
/* 177 */       if ((num0 instanceof BigDecimal))
/* 178 */         return ((BigDecimal)num0).multiply(new BigDecimal(num1.doubleValue()));
/* 179 */       if ((num1 instanceof BigDecimal)) {
/* 180 */         return new BigDecimal(num0.doubleValue()).multiply((BigDecimal)num1);
/*     */       }
/* 182 */       return Double.valueOf(num0.doubleValue() * num1.doubleValue());
/*     */     }
/*     */     
/*     */     public boolean matches(Object obj0, Object obj1)
/*     */     {
/* 187 */       return ((obj0 instanceof Double)) || ((obj1 instanceof Double)) || ((obj0 instanceof Float)) || ((obj1 instanceof Float)) || (((obj0 instanceof String)) && 
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 192 */         (ELSupport.isStringFloat((String)obj0))) || (((obj1 instanceof String)) && 
/* 193 */         (ELSupport.isStringFloat((String)obj1)));
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class LongDelegate extends ELArithmetic
/*     */   {
/*     */     protected Number add(Number num0, Number num1)
/*     */     {
/* 201 */       return Long.valueOf(num0.longValue() + num1.longValue());
/*     */     }
/*     */     
/*     */     protected Number coerce(Number num)
/*     */     {
/* 206 */       if ((num instanceof Long))
/* 207 */         return num;
/* 208 */       return Long.valueOf(num.longValue());
/*     */     }
/*     */     
/*     */     protected Number coerce(String str)
/*     */     {
/* 213 */       return Long.valueOf(str);
/*     */     }
/*     */     
/*     */     protected Number divide(Number num0, Number num1)
/*     */     {
/* 218 */       return Long.valueOf(num0.longValue() / num1.longValue());
/*     */     }
/*     */     
/*     */     protected Number mod(Number num0, Number num1)
/*     */     {
/* 223 */       return Long.valueOf(num0.longValue() % num1.longValue());
/*     */     }
/*     */     
/*     */     protected Number subtract(Number num0, Number num1)
/*     */     {
/* 228 */       return Long.valueOf(num0.longValue() - num1.longValue());
/*     */     }
/*     */     
/*     */     protected Number multiply(Number num0, Number num1)
/*     */     {
/* 233 */       return Long.valueOf(num0.longValue() * num1.longValue());
/*     */     }
/*     */     
/*     */     public boolean matches(Object obj0, Object obj1)
/*     */     {
/* 238 */       return ((obj0 instanceof Long)) || ((obj1 instanceof Long));
/*     */     }
/*     */   }
/*     */   
/* 242 */   public static final BigDecimalDelegate BIGDECIMAL = new BigDecimalDelegate();
/*     */   
/* 244 */   public static final BigIntegerDelegate BIGINTEGER = new BigIntegerDelegate();
/*     */   
/* 246 */   public static final DoubleDelegate DOUBLE = new DoubleDelegate();
/*     */   
/* 248 */   public static final LongDelegate LONG = new LongDelegate();
/*     */   
/* 250 */   private static final Long ZERO = Long.valueOf(0L);
/*     */   
/*     */   public static final Number add(Object obj0, Object obj1) {
/* 253 */     ELArithmetic delegate = findDelegate(obj0, obj1);
/* 254 */     if (delegate == null) {
/* 255 */       return Long.valueOf(0L);
/*     */     }
/*     */     
/* 258 */     Number num0 = delegate.coerce(obj0);
/* 259 */     Number num1 = delegate.coerce(obj1);
/*     */     
/* 261 */     return delegate.add(num0, num1);
/*     */   }
/*     */   
/*     */   public static final Number mod(Object obj0, Object obj1) {
/* 265 */     if ((obj0 == null) && (obj1 == null)) {
/* 266 */       return Long.valueOf(0L);
/*     */     }
/*     */     ELArithmetic delegate;
/*     */     ELArithmetic delegate;
/* 270 */     if (BIGDECIMAL.matches(obj0, obj1)) {
/* 271 */       delegate = DOUBLE; } else { ELArithmetic delegate;
/* 272 */       if (DOUBLE.matches(obj0, obj1)) {
/* 273 */         delegate = DOUBLE; } else { ELArithmetic delegate;
/* 274 */         if (BIGINTEGER.matches(obj0, obj1)) {
/* 275 */           delegate = BIGINTEGER;
/*     */         } else
/* 277 */           delegate = LONG;
/*     */       } }
/* 279 */     Number num0 = delegate.coerce(obj0);
/* 280 */     Number num1 = delegate.coerce(obj1);
/*     */     
/* 282 */     return delegate.mod(num0, num1);
/*     */   }
/*     */   
/*     */   public static final Number subtract(Object obj0, Object obj1) {
/* 286 */     ELArithmetic delegate = findDelegate(obj0, obj1);
/* 287 */     if (delegate == null) {
/* 288 */       return Long.valueOf(0L);
/*     */     }
/*     */     
/* 291 */     Number num0 = delegate.coerce(obj0);
/* 292 */     Number num1 = delegate.coerce(obj1);
/*     */     
/* 294 */     return delegate.subtract(num0, num1);
/*     */   }
/*     */   
/*     */   public static final Number divide(Object obj0, Object obj1) {
/* 298 */     if ((obj0 == null) && (obj1 == null)) {
/* 299 */       return ZERO;
/*     */     }
/*     */     ELArithmetic delegate;
/*     */     ELArithmetic delegate;
/* 303 */     if (BIGDECIMAL.matches(obj0, obj1)) {
/* 304 */       delegate = BIGDECIMAL; } else { ELArithmetic delegate;
/* 305 */       if (BIGINTEGER.matches(obj0, obj1)) {
/* 306 */         delegate = BIGDECIMAL;
/*     */       } else
/* 308 */         delegate = DOUBLE;
/*     */     }
/* 310 */     Number num0 = delegate.coerce(obj0);
/* 311 */     Number num1 = delegate.coerce(obj1);
/*     */     
/* 313 */     return delegate.divide(num0, num1);
/*     */   }
/*     */   
/*     */   public static final Number multiply(Object obj0, Object obj1) {
/* 317 */     ELArithmetic delegate = findDelegate(obj0, obj1);
/* 318 */     if (delegate == null) {
/* 319 */       return Long.valueOf(0L);
/*     */     }
/*     */     
/* 322 */     Number num0 = delegate.coerce(obj0);
/* 323 */     Number num1 = delegate.coerce(obj1);
/*     */     
/* 325 */     return delegate.multiply(num0, num1);
/*     */   }
/*     */   
/*     */   private static ELArithmetic findDelegate(Object obj0, Object obj1) {
/* 329 */     if ((obj0 == null) && (obj1 == null)) {
/* 330 */       return null;
/*     */     }
/*     */     
/* 333 */     if (BIGDECIMAL.matches(obj0, obj1))
/* 334 */       return BIGDECIMAL;
/* 335 */     if (DOUBLE.matches(obj0, obj1)) {
/* 336 */       if (BIGINTEGER.matches(obj0, obj1)) {
/* 337 */         return BIGDECIMAL;
/*     */       }
/* 339 */       return DOUBLE;
/*     */     }
/* 341 */     if (BIGINTEGER.matches(obj0, obj1)) {
/* 342 */       return BIGINTEGER;
/*     */     }
/* 344 */     return LONG;
/*     */   }
/*     */   
/*     */   public static final boolean isNumber(Object obj)
/*     */   {
/* 349 */     return (obj != null) && (isNumberType(obj.getClass()));
/*     */   }
/*     */   
/*     */   public static final boolean isNumberType(Class<?> type) {
/* 353 */     return (type == Long.TYPE) || (type == Double.TYPE) || (type == Byte.TYPE) || (type == Short.TYPE) || (type == Integer.TYPE) || (type == Float.TYPE) || 
/*     */     
/*     */ 
/* 356 */       (Number.class.isAssignableFrom(type));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract Number add(Number paramNumber1, Number paramNumber2);
/*     */   
/*     */ 
/*     */   protected abstract Number multiply(Number paramNumber1, Number paramNumber2);
/*     */   
/*     */ 
/*     */   protected abstract Number subtract(Number paramNumber1, Number paramNumber2);
/*     */   
/*     */ 
/*     */   protected abstract Number mod(Number paramNumber1, Number paramNumber2);
/*     */   
/*     */ 
/*     */   protected abstract Number coerce(Number paramNumber);
/*     */   
/*     */ 
/*     */   protected final Number coerce(Object obj)
/*     */   {
/* 378 */     if (isNumber(obj)) {
/* 379 */       return coerce((Number)obj);
/*     */     }
/* 381 */     if ((obj == null) || ("".equals(obj))) {
/* 382 */       return coerce(ZERO);
/*     */     }
/* 384 */     if ((obj instanceof String)) {
/* 385 */       return coerce((String)obj);
/*     */     }
/* 387 */     if ((obj instanceof Character)) {
/* 388 */       return coerce(Short.valueOf((short)((Character)obj).charValue()));
/*     */     }
/*     */     
/* 391 */     throw new IllegalArgumentException(MessageFactory.get("error.convert", new Object[] { obj, obj
/* 392 */       .getClass(), "Number" }));
/*     */   }
/*     */   
/*     */   protected abstract Number coerce(String paramString);
/*     */   
/*     */   protected abstract Number divide(Number paramNumber1, Number paramNumber2);
/*     */   
/*     */   protected abstract boolean matches(Object paramObject1, Object paramObject2);
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\lang\ELArithmetic.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */