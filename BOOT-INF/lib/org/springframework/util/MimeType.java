/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.BitSet;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MimeType
/*     */   implements Comparable<MimeType>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 4085923477777865903L;
/*     */   protected static final String WILDCARD_TYPE = "*";
/*     */   private static final String PARAM_CHARSET = "charset";
/*     */   private static final BitSet TOKEN;
/*     */   private final String type;
/*     */   private final String subtype;
/*     */   private final Map<String, String> parameters;
/*     */   
/*     */   static
/*     */   {
/*  63 */     BitSet ctl = new BitSet(128);
/*  64 */     for (int i = 0; i <= 31; i++) {
/*  65 */       ctl.set(i);
/*     */     }
/*  67 */     ctl.set(127);
/*     */     
/*  69 */     BitSet separators = new BitSet(128);
/*  70 */     separators.set(40);
/*  71 */     separators.set(41);
/*  72 */     separators.set(60);
/*  73 */     separators.set(62);
/*  74 */     separators.set(64);
/*  75 */     separators.set(44);
/*  76 */     separators.set(59);
/*  77 */     separators.set(58);
/*  78 */     separators.set(92);
/*  79 */     separators.set(34);
/*  80 */     separators.set(47);
/*  81 */     separators.set(91);
/*  82 */     separators.set(93);
/*  83 */     separators.set(63);
/*  84 */     separators.set(61);
/*  85 */     separators.set(123);
/*  86 */     separators.set(125);
/*  87 */     separators.set(32);
/*  88 */     separators.set(9);
/*     */     
/*  90 */     TOKEN = new BitSet(128);
/*  91 */     TOKEN.set(0, 128);
/*  92 */     TOKEN.andNot(ctl);
/*  93 */     TOKEN.andNot(separators);
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
/*     */   public MimeType(String type)
/*     */   {
/* 112 */     this(type, "*");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MimeType(String type, String subtype)
/*     */   {
/* 123 */     this(type, subtype, Collections.emptyMap());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MimeType(String type, String subtype, Charset charset)
/*     */   {
/* 134 */     this(type, subtype, Collections.singletonMap("charset", charset.name()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MimeType(MimeType other, Charset charset)
/*     */   {
/* 146 */     this(other.getType(), other.getSubtype(), addCharsetParameter(charset, other.getParameters()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MimeType(MimeType other, Map<String, String> parameters)
/*     */   {
/* 157 */     this(other.getType(), other.getSubtype(), parameters);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MimeType(String type, String subtype, Map<String, String> parameters)
/*     */   {
/* 168 */     Assert.hasLength(type, "'type' must not be empty");
/* 169 */     Assert.hasLength(subtype, "'subtype' must not be empty");
/* 170 */     checkToken(type);
/* 171 */     checkToken(subtype);
/* 172 */     this.type = type.toLowerCase(Locale.ENGLISH);
/* 173 */     this.subtype = subtype.toLowerCase(Locale.ENGLISH);
/* 174 */     if (!CollectionUtils.isEmpty(parameters)) {
/* 175 */       Map<String, String> map = new LinkedCaseInsensitiveMap(parameters.size(), Locale.ENGLISH);
/* 176 */       for (Map.Entry<String, String> entry : parameters.entrySet()) {
/* 177 */         String attribute = (String)entry.getKey();
/* 178 */         String value = (String)entry.getValue();
/* 179 */         checkParameters(attribute, value);
/* 180 */         map.put(attribute, value);
/*     */       }
/* 182 */       this.parameters = Collections.unmodifiableMap(map);
/*     */     }
/*     */     else {
/* 185 */       this.parameters = Collections.emptyMap();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void checkToken(String token)
/*     */   {
/* 196 */     for (int i = 0; i < token.length(); i++) {
/* 197 */       char ch = token.charAt(i);
/* 198 */       if (!TOKEN.get(ch)) {
/* 199 */         throw new IllegalArgumentException("Invalid token character '" + ch + "' in token \"" + token + "\"");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void checkParameters(String attribute, String value) {
/* 205 */     Assert.hasLength(attribute, "'attribute' must not be empty");
/* 206 */     Assert.hasLength(value, "'value' must not be empty");
/* 207 */     checkToken(attribute);
/* 208 */     if ("charset".equals(attribute)) {
/* 209 */       value = unquote(value);
/* 210 */       Charset.forName(value);
/*     */     }
/* 212 */     else if (!isQuotedString(value)) {
/* 213 */       checkToken(value);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isQuotedString(String s) {
/* 218 */     if (s.length() < 2) {
/* 219 */       return false;
/*     */     }
/*     */     
/* 222 */     return ((s.startsWith("\"")) && (s.endsWith("\""))) || ((s.startsWith("'")) && (s.endsWith("'")));
/*     */   }
/*     */   
/*     */   protected String unquote(String s)
/*     */   {
/* 227 */     if (s == null) {
/* 228 */       return null;
/*     */     }
/* 230 */     return isQuotedString(s) ? s.substring(1, s.length() - 1) : s;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isWildcardType()
/*     */   {
/* 238 */     return "*".equals(getType());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isWildcardSubtype()
/*     */   {
/* 248 */     return ("*".equals(getSubtype())) || (getSubtype().startsWith("*+"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isConcrete()
/*     */   {
/* 257 */     return (!isWildcardType()) && (!isWildcardSubtype());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getType()
/*     */   {
/* 264 */     return this.type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getSubtype()
/*     */   {
/* 271 */     return this.subtype;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Charset getCharset()
/*     */   {
/* 280 */     String charset = getParameter("charset");
/* 281 */     return charset != null ? Charset.forName(unquote(charset)) : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public Charset getCharSet()
/*     */   {
/* 292 */     return getCharset();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getParameter(String name)
/*     */   {
/* 301 */     return (String)this.parameters.get(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, String> getParameters()
/*     */   {
/* 309 */     return this.parameters;
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
/*     */   public boolean includes(MimeType other)
/*     */   {
/* 322 */     if (other == null) {
/* 323 */       return false;
/*     */     }
/* 325 */     if (isWildcardType())
/*     */     {
/* 327 */       return true;
/*     */     }
/* 329 */     if (getType().equals(other.getType())) {
/* 330 */       if (getSubtype().equals(other.getSubtype())) {
/* 331 */         return true;
/*     */       }
/* 333 */       if (isWildcardSubtype())
/*     */       {
/* 335 */         int thisPlusIdx = getSubtype().indexOf('+');
/* 336 */         if (thisPlusIdx == -1) {
/* 337 */           return true;
/*     */         }
/*     */         
/*     */ 
/* 341 */         int otherPlusIdx = other.getSubtype().indexOf('+');
/* 342 */         if (otherPlusIdx != -1) {
/* 343 */           String thisSubtypeNoSuffix = getSubtype().substring(0, thisPlusIdx);
/* 344 */           String thisSubtypeSuffix = getSubtype().substring(thisPlusIdx + 1);
/* 345 */           String otherSubtypeSuffix = other.getSubtype().substring(otherPlusIdx + 1);
/* 346 */           if ((thisSubtypeSuffix.equals(otherSubtypeSuffix)) && ("*".equals(thisSubtypeNoSuffix))) {
/* 347 */             return true;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 353 */     return false;
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
/*     */   public boolean isCompatibleWith(MimeType other)
/*     */   {
/* 366 */     if (other == null) {
/* 367 */       return false;
/*     */     }
/* 369 */     if ((isWildcardType()) || (other.isWildcardType())) {
/* 370 */       return true;
/*     */     }
/* 372 */     if (getType().equals(other.getType())) {
/* 373 */       if (getSubtype().equals(other.getSubtype())) {
/* 374 */         return true;
/*     */       }
/*     */       
/* 377 */       if ((isWildcardSubtype()) || (other.isWildcardSubtype()))
/*     */       {
/* 379 */         int thisPlusIdx = getSubtype().indexOf('+');
/* 380 */         int otherPlusIdx = other.getSubtype().indexOf('+');
/*     */         
/* 382 */         if ((thisPlusIdx == -1) && (otherPlusIdx == -1)) {
/* 383 */           return true;
/*     */         }
/* 385 */         if ((thisPlusIdx != -1) && (otherPlusIdx != -1)) {
/* 386 */           String thisSubtypeNoSuffix = getSubtype().substring(0, thisPlusIdx);
/* 387 */           String otherSubtypeNoSuffix = other.getSubtype().substring(0, otherPlusIdx);
/*     */           
/* 389 */           String thisSubtypeSuffix = getSubtype().substring(thisPlusIdx + 1);
/* 390 */           String otherSubtypeSuffix = other.getSubtype().substring(otherPlusIdx + 1);
/*     */           
/* 392 */           if ((thisSubtypeSuffix.equals(otherSubtypeSuffix)) && (
/* 393 */             ("*".equals(thisSubtypeNoSuffix)) || ("*".equals(otherSubtypeNoSuffix)))) {
/* 394 */             return true;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 399 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 405 */     if (this == other) {
/* 406 */       return true;
/*     */     }
/* 408 */     if (!(other instanceof MimeType)) {
/* 409 */       return false;
/*     */     }
/* 411 */     MimeType otherType = (MimeType)other;
/* 412 */     return (this.type.equalsIgnoreCase(otherType.type)) && 
/* 413 */       (this.subtype.equalsIgnoreCase(otherType.subtype)) && 
/* 414 */       (parametersAreEqual(otherType));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean parametersAreEqual(MimeType other)
/*     */   {
/* 424 */     if (this.parameters.size() != other.parameters.size()) {
/* 425 */       return false;
/*     */     }
/*     */     
/* 428 */     for (String key : this.parameters.keySet()) {
/* 429 */       if (!other.parameters.containsKey(key)) {
/* 430 */         return false;
/*     */       }
/*     */       
/* 433 */       if ("charset".equals(key)) {
/* 434 */         if (!ObjectUtils.nullSafeEquals(getCharset(), other.getCharset())) {
/* 435 */           return false;
/*     */         }
/*     */       }
/* 438 */       else if (!ObjectUtils.nullSafeEquals(this.parameters.get(key), other.parameters.get(key))) {
/* 439 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 443 */     return true;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 448 */     int result = this.type.hashCode();
/* 449 */     result = 31 * result + this.subtype.hashCode();
/* 450 */     result = 31 * result + this.parameters.hashCode();
/* 451 */     return result;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 456 */     StringBuilder builder = new StringBuilder();
/* 457 */     appendTo(builder);
/* 458 */     return builder.toString();
/*     */   }
/*     */   
/*     */   protected void appendTo(StringBuilder builder) {
/* 462 */     builder.append(this.type);
/* 463 */     builder.append('/');
/* 464 */     builder.append(this.subtype);
/* 465 */     appendTo(this.parameters, builder);
/*     */   }
/*     */   
/*     */   private void appendTo(Map<String, String> map, StringBuilder builder) {
/* 469 */     for (Map.Entry<String, String> entry : map.entrySet()) {
/* 470 */       builder.append(';');
/* 471 */       builder.append((String)entry.getKey());
/* 472 */       builder.append('=');
/* 473 */       builder.append((String)entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int compareTo(MimeType other)
/*     */   {
/* 484 */     int comp = getType().compareToIgnoreCase(other.getType());
/* 485 */     if (comp != 0) {
/* 486 */       return comp;
/*     */     }
/* 488 */     comp = getSubtype().compareToIgnoreCase(other.getSubtype());
/* 489 */     if (comp != 0) {
/* 490 */       return comp;
/*     */     }
/* 492 */     comp = getParameters().size() - other.getParameters().size();
/* 493 */     if (comp != 0) {
/* 494 */       return comp;
/*     */     }
/* 496 */     TreeSet<String> thisAttributes = new TreeSet(String.CASE_INSENSITIVE_ORDER);
/* 497 */     thisAttributes.addAll(getParameters().keySet());
/* 498 */     TreeSet<String> otherAttributes = new TreeSet(String.CASE_INSENSITIVE_ORDER);
/* 499 */     otherAttributes.addAll(other.getParameters().keySet());
/* 500 */     Iterator<String> thisAttributesIterator = thisAttributes.iterator();
/* 501 */     Iterator<String> otherAttributesIterator = otherAttributes.iterator();
/* 502 */     while (thisAttributesIterator.hasNext()) {
/* 503 */       String thisAttribute = (String)thisAttributesIterator.next();
/* 504 */       String otherAttribute = (String)otherAttributesIterator.next();
/* 505 */       comp = thisAttribute.compareToIgnoreCase(otherAttribute);
/* 506 */       if (comp != 0) {
/* 507 */         return comp;
/*     */       }
/* 509 */       String thisValue = (String)getParameters().get(thisAttribute);
/* 510 */       String otherValue = (String)other.getParameters().get(otherAttribute);
/* 511 */       if (otherValue == null) {
/* 512 */         otherValue = "";
/*     */       }
/* 514 */       comp = thisValue.compareTo(otherValue);
/* 515 */       if (comp != 0) {
/* 516 */         return comp;
/*     */       }
/*     */     }
/* 519 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static MimeType valueOf(String value)
/*     */   {
/* 530 */     return MimeTypeUtils.parseMimeType(value);
/*     */   }
/*     */   
/*     */   private static Map<String, String> addCharsetParameter(Charset charset, Map<String, String> parameters) {
/* 534 */     Map<String, String> map = new LinkedHashMap(parameters);
/* 535 */     map.put("charset", charset.name());
/* 536 */     return map;
/*     */   }
/*     */   
/*     */   public static class SpecificityComparator<T extends MimeType>
/*     */     implements Comparator<T>
/*     */   {
/*     */     public int compare(T mimeType1, T mimeType2)
/*     */     {
/* 544 */       if ((mimeType1.isWildcardType()) && (!mimeType2.isWildcardType())) {
/* 545 */         return 1;
/*     */       }
/* 547 */       if ((mimeType2.isWildcardType()) && (!mimeType1.isWildcardType())) {
/* 548 */         return -1;
/*     */       }
/* 550 */       if (!mimeType1.getType().equals(mimeType2.getType())) {
/* 551 */         return 0;
/*     */       }
/*     */       
/* 554 */       if ((mimeType1.isWildcardSubtype()) && (!mimeType2.isWildcardSubtype())) {
/* 555 */         return 1;
/*     */       }
/* 557 */       if ((mimeType2.isWildcardSubtype()) && (!mimeType1.isWildcardSubtype())) {
/* 558 */         return -1;
/*     */       }
/* 560 */       if (!mimeType1.getSubtype().equals(mimeType2.getSubtype())) {
/* 561 */         return 0;
/*     */       }
/*     */       
/* 564 */       return compareParameters(mimeType1, mimeType2);
/*     */     }
/*     */     
/*     */ 
/*     */     protected int compareParameters(T mimeType1, T mimeType2)
/*     */     {
/* 570 */       int paramsSize1 = mimeType1.getParameters().size();
/* 571 */       int paramsSize2 = mimeType2.getParameters().size();
/* 572 */       return paramsSize2 == paramsSize1 ? 0 : paramsSize2 < paramsSize1 ? -1 : 1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\MimeType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */