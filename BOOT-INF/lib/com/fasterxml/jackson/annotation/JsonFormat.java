/*     */ package com.fasterxml.jackson.annotation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.annotation.Target;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
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
/*     */ @Target({java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.PARAMETER, java.lang.annotation.ElementType.TYPE})
/*     */ @Retention(RetentionPolicy.RUNTIME)
/*     */ @JacksonAnnotation
/*     */ public @interface JsonFormat
/*     */ {
/*     */   public static final String DEFAULT_LOCALE = "##default";
/*     */   public static final String DEFAULT_TIMEZONE = "##default";
/*     */   
/*     */   String pattern() default "";
/*     */   
/*     */   Shape shape() default Shape.ANY;
/*     */   
/*     */   String locale() default "##default";
/*     */   
/*     */   String timezone() default "##default";
/*     */   
/*     */   Feature[] with() default {};
/*     */   
/*     */   Feature[] without() default {};
/*     */   
/*     */   public static enum Shape
/*     */   {
/* 144 */     ANY, 
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
/* 155 */     NATURAL, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 161 */     SCALAR, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 166 */     ARRAY, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 171 */     OBJECT, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 178 */     NUMBER, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 183 */     NUMBER_FLOAT, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 189 */     NUMBER_INT, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 194 */     STRING, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 200 */     BOOLEAN;
/*     */     
/*     */     private Shape() {}
/*     */     
/* 204 */     public boolean isNumeric() { return (this == NUMBER) || (this == NUMBER_INT) || (this == NUMBER_FLOAT); }
/*     */     
/*     */     public boolean isStructured()
/*     */     {
/* 208 */       return (this == OBJECT) || (this == ARRAY);
/*     */     }
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
/*     */   public static enum Feature
/*     */   {
/* 231 */     ACCEPT_SINGLE_VALUE_AS_ARRAY, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 239 */     ACCEPT_CASE_INSENSITIVE_PROPERTIES, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 245 */     WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 251 */     WRITE_DATES_WITH_ZONE_ID, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 258 */     WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 265 */     WRITE_SORTED_MAP_ENTRIES, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 275 */     ADJUST_DATES_TO_CONTEXT_TIME_ZONE;
/*     */     
/*     */ 
/*     */     private Feature() {}
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Features
/*     */   {
/*     */     private final int _enabled;
/*     */     
/*     */     private final int _disabled;
/*     */     
/* 288 */     private static final Features EMPTY = new Features(0, 0);
/*     */     
/*     */     private Features(int e, int d) {
/* 291 */       this._enabled = e;
/* 292 */       this._disabled = d;
/*     */     }
/*     */     
/*     */     public static Features empty() {
/* 296 */       return EMPTY;
/*     */     }
/*     */     
/*     */     public static Features construct(JsonFormat f) {
/* 300 */       return construct(f.with(), f.without());
/*     */     }
/*     */     
/*     */     public static Features construct(JsonFormat.Feature[] enabled, JsonFormat.Feature[] disabled)
/*     */     {
/* 305 */       int e = 0;
/* 306 */       for (JsonFormat.Feature f : enabled) {
/* 307 */         e |= 1 << f.ordinal();
/*     */       }
/* 309 */       int d = 0;
/* 310 */       for (JsonFormat.Feature f : disabled) {
/* 311 */         d |= 1 << f.ordinal();
/*     */       }
/* 313 */       return new Features(e, d);
/*     */     }
/*     */     
/*     */     public Features withOverrides(Features overrides)
/*     */     {
/* 318 */       if (overrides == null) {
/* 319 */         return this;
/*     */       }
/* 321 */       int overrideD = overrides._disabled;
/* 322 */       int overrideE = overrides._enabled;
/* 323 */       if ((overrideD == 0) && (overrideE == 0)) {
/* 324 */         return this;
/*     */       }
/* 326 */       if ((this._enabled == 0) && (this._disabled == 0)) {
/* 327 */         return overrides;
/*     */       }
/*     */       
/* 330 */       int newE = this._enabled & (overrideD ^ 0xFFFFFFFF) | overrideE;
/* 331 */       int newD = this._disabled & (overrideE ^ 0xFFFFFFFF) | overrideD;
/*     */       
/*     */ 
/* 334 */       if ((newE == this._enabled) && (newD == this._disabled)) {
/* 335 */         return this;
/*     */       }
/*     */       
/* 338 */       return new Features(newE, newD);
/*     */     }
/*     */     
/*     */     public Features with(JsonFormat.Feature... features) {
/* 342 */       int e = this._enabled;
/* 343 */       for (JsonFormat.Feature f : features) {
/* 344 */         e |= 1 << f.ordinal();
/*     */       }
/* 346 */       return e == this._enabled ? this : new Features(e, this._disabled);
/*     */     }
/*     */     
/*     */     public Features without(JsonFormat.Feature... features) {
/* 350 */       int d = this._disabled;
/* 351 */       for (JsonFormat.Feature f : features) {
/* 352 */         d |= 1 << f.ordinal();
/*     */       }
/* 354 */       return d == this._disabled ? this : new Features(this._enabled, d);
/*     */     }
/*     */     
/*     */     public Boolean get(JsonFormat.Feature f) {
/* 358 */       int mask = 1 << f.ordinal();
/* 359 */       if ((this._disabled & mask) != 0) {
/* 360 */         return Boolean.FALSE;
/*     */       }
/* 362 */       if ((this._enabled & mask) != 0) {
/* 363 */         return Boolean.TRUE;
/*     */       }
/* 365 */       return null;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 370 */       return this._disabled + this._enabled;
/*     */     }
/*     */     
/*     */     public boolean equals(Object o)
/*     */     {
/* 375 */       if (o == this) return true;
/* 376 */       if (o == null) return false;
/* 377 */       if (o.getClass() != getClass()) return false;
/* 378 */       Features other = (Features)o;
/* 379 */       return (other._enabled == this._enabled) && (other._disabled == this._disabled);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Value
/*     */     implements JacksonAnnotationValue<JsonFormat>, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/* 393 */     private static final Value EMPTY = new Value();
/*     */     
/*     */     private final String _pattern;
/*     */     
/*     */     private final JsonFormat.Shape _shape;
/*     */     
/*     */     private final Locale _locale;
/*     */     
/*     */     private final String _timezoneStr;
/*     */     
/*     */     private final JsonFormat.Features _features;
/*     */     
/*     */     private transient TimeZone _timezone;
/*     */     
/*     */ 
/*     */     public Value()
/*     */     {
/* 410 */       this("", JsonFormat.Shape.ANY, "", "", JsonFormat.Features.empty());
/*     */     }
/*     */     
/*     */     public Value(JsonFormat ann) {
/* 414 */       this(ann.pattern(), ann.shape(), ann.locale(), ann.timezone(), JsonFormat.Features.construct(ann));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Value(String p, JsonFormat.Shape sh, String localeStr, String tzStr, JsonFormat.Features f)
/*     */     {
/* 423 */       this(p, sh, (localeStr == null) || (localeStr.length() == 0) || ("##default".equals(localeStr)) ? null : new Locale(localeStr), (tzStr == null) || (tzStr.length() == 0) || ("##default".equals(tzStr)) ? null : tzStr, null, f);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Value(String p, JsonFormat.Shape sh, Locale l, TimeZone tz, JsonFormat.Features f)
/*     */     {
/* 436 */       this._pattern = p;
/* 437 */       this._shape = (sh == null ? JsonFormat.Shape.ANY : sh);
/* 438 */       this._locale = l;
/* 439 */       this._timezone = tz;
/* 440 */       this._timezoneStr = null;
/* 441 */       this._features = (f == null ? JsonFormat.Features.empty() : f);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Value(String p, JsonFormat.Shape sh, Locale l, String tzStr, TimeZone tz, JsonFormat.Features f)
/*     */     {
/* 449 */       this._pattern = p;
/* 450 */       this._shape = (sh == null ? JsonFormat.Shape.ANY : sh);
/* 451 */       this._locale = l;
/* 452 */       this._timezone = tz;
/* 453 */       this._timezoneStr = tzStr;
/* 454 */       this._features = (f == null ? JsonFormat.Features.empty() : f);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     @Deprecated
/*     */     public Value(String p, JsonFormat.Shape sh, Locale l, TimeZone tz)
/*     */     {
/* 462 */       this(p, sh, l, tz, JsonFormat.Features.empty());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     @Deprecated
/*     */     public Value(String p, JsonFormat.Shape sh, String localeStr, String tzStr)
/*     */     {
/* 470 */       this(p, sh, localeStr, tzStr, JsonFormat.Features.empty());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     @Deprecated
/*     */     public Value(String p, JsonFormat.Shape sh, Locale l, String tzStr, TimeZone tz)
/*     */     {
/* 478 */       this(p, sh, l, tzStr, tz, JsonFormat.Features.empty());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public static final Value empty()
/*     */     {
/* 485 */       return EMPTY;
/*     */     }
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
/*     */     public static Value merge(Value base, Value overrides)
/*     */     {
/* 501 */       return base == null ? overrides : base.withOverrides(overrides);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public static Value mergeAll(Value... values)
/*     */     {
/* 510 */       Value result = null;
/* 511 */       for (Value curr : values) {
/* 512 */         if (curr != null) {
/* 513 */           result = result == null ? curr : result.withOverrides(curr);
/*     */         }
/*     */       }
/* 516 */       return result;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public static final Value from(JsonFormat ann)
/*     */     {
/* 523 */       if (ann == null) {
/* 524 */         return null;
/*     */       }
/* 526 */       return new Value(ann);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public final Value withOverrides(Value overrides)
/*     */     {
/* 533 */       if ((overrides == null) || (overrides == EMPTY)) {
/* 534 */         return this;
/*     */       }
/* 536 */       if (this == EMPTY) {
/* 537 */         return overrides;
/*     */       }
/* 539 */       String p = overrides._pattern;
/* 540 */       if ((p == null) || (p.isEmpty())) {
/* 541 */         p = this._pattern;
/*     */       }
/* 543 */       JsonFormat.Shape sh = overrides._shape;
/* 544 */       if (sh == JsonFormat.Shape.ANY) {
/* 545 */         sh = this._shape;
/*     */       }
/* 547 */       Locale l = overrides._locale;
/* 548 */       if (l == null) {
/* 549 */         l = this._locale;
/*     */       }
/* 551 */       JsonFormat.Features f = this._features;
/* 552 */       if (f == null) {
/* 553 */         f = overrides._features;
/*     */       } else {
/* 555 */         f = f.withOverrides(overrides._features);
/*     */       }
/*     */       
/*     */ 
/* 559 */       String tzStr = overrides._timezoneStr;
/*     */       TimeZone tz;
/*     */       TimeZone tz;
/* 562 */       if ((tzStr == null) || (tzStr.isEmpty())) {
/* 563 */         tzStr = this._timezoneStr;
/* 564 */         tz = this._timezone;
/*     */       } else {
/* 566 */         tz = overrides._timezone;
/*     */       }
/* 568 */       return new Value(p, sh, l, tzStr, tz, f);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public static Value forPattern(String p)
/*     */     {
/* 575 */       return new Value(p, null, null, null, null, JsonFormat.Features.empty());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public static Value forShape(JsonFormat.Shape sh)
/*     */     {
/* 582 */       return new Value(null, sh, null, null, null, JsonFormat.Features.empty());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Value withPattern(String p)
/*     */     {
/* 589 */       return new Value(p, this._shape, this._locale, this._timezoneStr, this._timezone, this._features);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Value withShape(JsonFormat.Shape s)
/*     */     {
/* 596 */       return new Value(this._pattern, s, this._locale, this._timezoneStr, this._timezone, this._features);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Value withLocale(Locale l)
/*     */     {
/* 603 */       return new Value(this._pattern, this._shape, l, this._timezoneStr, this._timezone, this._features);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Value withTimeZone(TimeZone tz)
/*     */     {
/* 610 */       return new Value(this._pattern, this._shape, this._locale, null, tz, this._features);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Value withFeature(JsonFormat.Feature f)
/*     */     {
/* 617 */       JsonFormat.Features newFeats = this._features.with(new JsonFormat.Feature[] { f });
/* 618 */       return newFeats == this._features ? this : new Value(this._pattern, this._shape, this._locale, this._timezoneStr, this._timezone, newFeats);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Value withoutFeature(JsonFormat.Feature f)
/*     */     {
/* 626 */       JsonFormat.Features newFeats = this._features.without(new JsonFormat.Feature[] { f });
/* 627 */       return newFeats == this._features ? this : new Value(this._pattern, this._shape, this._locale, this._timezoneStr, this._timezone, newFeats);
/*     */     }
/*     */     
/*     */ 
/*     */     public Class<JsonFormat> valueFor()
/*     */     {
/* 633 */       return JsonFormat.class;
/*     */     }
/*     */     
/* 636 */     public String getPattern() { return this._pattern; }
/* 637 */     public JsonFormat.Shape getShape() { return this._shape; }
/* 638 */     public Locale getLocale() { return this._locale; }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String timeZoneAsString()
/*     */     {
/* 648 */       if (this._timezone != null) {
/* 649 */         return this._timezone.getID();
/*     */       }
/* 651 */       return this._timezoneStr;
/*     */     }
/*     */     
/*     */     public TimeZone getTimeZone() {
/* 655 */       TimeZone tz = this._timezone;
/* 656 */       if (tz == null) {
/* 657 */         if (this._timezoneStr == null) {
/* 658 */           return null;
/*     */         }
/* 660 */         tz = TimeZone.getTimeZone(this._timezoneStr);
/* 661 */         this._timezone = tz;
/*     */       }
/* 663 */       return tz;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean hasShape()
/*     */     {
/* 669 */       return this._shape != JsonFormat.Shape.ANY;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean hasPattern()
/*     */     {
/* 675 */       return (this._pattern != null) && (this._pattern.length() > 0);
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean hasLocale()
/*     */     {
/* 681 */       return this._locale != null;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean hasTimeZone()
/*     */     {
/* 687 */       return (this._timezone != null) || ((this._timezoneStr != null) && (!this._timezoneStr.isEmpty()));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Boolean getFeature(JsonFormat.Feature f)
/*     */     {
/* 700 */       return this._features.get(f);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public JsonFormat.Features getFeatures()
/*     */     {
/* 709 */       return this._features;
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 715 */       return String.format("[pattern=%s,shape=%s,locale=%s,timezone=%s]", new Object[] { this._pattern, this._shape, this._locale, this._timezoneStr });
/*     */     }
/*     */     
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 721 */       int hash = this._timezoneStr == null ? 1 : this._timezoneStr.hashCode();
/* 722 */       if (this._pattern != null) {
/* 723 */         hash ^= this._pattern.hashCode();
/*     */       }
/* 725 */       hash += this._shape.hashCode();
/* 726 */       if (this._locale != null) {
/* 727 */         hash ^= this._locale.hashCode();
/*     */       }
/* 729 */       hash += this._features.hashCode();
/* 730 */       return hash;
/*     */     }
/*     */     
/*     */     public boolean equals(Object o)
/*     */     {
/* 735 */       if (o == this) return true;
/* 736 */       if (o == null) return false;
/* 737 */       if (o.getClass() != getClass()) return false;
/* 738 */       Value other = (Value)o;
/*     */       
/* 740 */       if ((this._shape != other._shape) || (!this._features.equals(other._features)))
/*     */       {
/* 742 */         return false;
/*     */       }
/* 744 */       return (_equal(this._timezoneStr, other._timezoneStr)) && (_equal(this._pattern, other._pattern)) && (_equal(this._timezone, other._timezone)) && (_equal(this._locale, other._locale));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private static <T> boolean _equal(T value1, T value2)
/*     */     {
/* 752 */       if (value1 == null)
/* 753 */         return value2 == null;
/* 754 */       if (value2 == null) {
/* 755 */         return false;
/*     */       }
/* 757 */       return value1.equals(value2);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-annotations-2.8.0.jar!\com\fasterxml\jackson\annotation\JsonFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */