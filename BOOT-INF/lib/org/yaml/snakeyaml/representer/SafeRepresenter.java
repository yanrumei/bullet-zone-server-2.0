/*     */ package org.yaml.snakeyaml.representer;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import java.util.UUID;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ import org.yaml.snakeyaml.reader.StreamReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SafeRepresenter
/*     */   extends BaseRepresenter
/*     */ {
/*     */   protected Map<Class<? extends Object>, Tag> classTags;
/*  46 */   protected TimeZone timeZone = null;
/*     */   
/*     */   public SafeRepresenter() {
/*  49 */     this.nullRepresenter = new RepresentNull();
/*  50 */     this.representers.put(String.class, new RepresentString());
/*  51 */     this.representers.put(Boolean.class, new RepresentBoolean());
/*  52 */     this.representers.put(Character.class, new RepresentString());
/*  53 */     this.representers.put(UUID.class, new RepresentUuid());
/*  54 */     this.representers.put(byte[].class, new RepresentByteArray());
/*     */     
/*  56 */     Represent primitiveArray = new RepresentPrimitiveArray();
/*  57 */     this.representers.put(short[].class, primitiveArray);
/*  58 */     this.representers.put(int[].class, primitiveArray);
/*  59 */     this.representers.put(long[].class, primitiveArray);
/*  60 */     this.representers.put(float[].class, primitiveArray);
/*  61 */     this.representers.put(double[].class, primitiveArray);
/*  62 */     this.representers.put(char[].class, primitiveArray);
/*  63 */     this.representers.put(boolean[].class, primitiveArray);
/*     */     
/*  65 */     this.multiRepresenters.put(Number.class, new RepresentNumber());
/*  66 */     this.multiRepresenters.put(List.class, new RepresentList());
/*  67 */     this.multiRepresenters.put(Map.class, new RepresentMap());
/*  68 */     this.multiRepresenters.put(Set.class, new RepresentSet());
/*  69 */     this.multiRepresenters.put(Iterator.class, new RepresentIterator());
/*  70 */     this.multiRepresenters.put(new Object[0].getClass(), new RepresentArray());
/*  71 */     this.multiRepresenters.put(Date.class, new RepresentDate());
/*  72 */     this.multiRepresenters.put(Enum.class, new RepresentEnum());
/*  73 */     this.multiRepresenters.put(Calendar.class, new RepresentDate());
/*  74 */     this.classTags = new HashMap();
/*     */   }
/*     */   
/*     */   protected Tag getTag(Class<?> clazz, Tag defaultTag) {
/*  78 */     if (this.classTags.containsKey(clazz)) {
/*  79 */       return (Tag)this.classTags.get(clazz);
/*     */     }
/*  81 */     return defaultTag;
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
/*     */   public Tag addClassTag(Class<? extends Object> clazz, Tag tag)
/*     */   {
/*  96 */     if (tag == null) {
/*  97 */       throw new NullPointerException("Tag must be provided.");
/*     */     }
/*  99 */     return (Tag)this.classTags.put(clazz, tag);
/*     */   }
/*     */   
/*     */   protected class RepresentNull implements Represent { protected RepresentNull() {}
/*     */     
/* 104 */     public Node representData(Object data) { return SafeRepresenter.this.representScalar(Tag.NULL, "null"); }
/*     */   }
/*     */   
/*     */ 
/* 108 */   public static Pattern MULTILINE_PATTERN = Pattern.compile("\n|| | ");
/*     */   
/*     */   protected class RepresentString implements Represent { protected RepresentString() {}
/*     */     
/* 112 */     public Node representData(Object data) { Tag tag = Tag.STR;
/* 113 */       Character style = null;
/* 114 */       String value = data.toString();
/* 115 */       if (StreamReader.NON_PRINTABLE.matcher(value).find()) {
/* 116 */         tag = Tag.BINARY;
/*     */         char[] binary;
/*     */         try {
/* 119 */           binary = Base64Coder.encode(value.getBytes("UTF-8"));
/*     */         } catch (UnsupportedEncodingException e) {
/* 121 */           throw new YAMLException(e);
/*     */         }
/* 123 */         value = String.valueOf(binary);
/* 124 */         style = Character.valueOf('|');
/*     */       }
/*     */       
/*     */ 
/* 128 */       if ((SafeRepresenter.this.defaultScalarStyle == null) && (SafeRepresenter.MULTILINE_PATTERN.matcher(value).find())) {
/* 129 */         style = Character.valueOf('|');
/*     */       }
/* 131 */       return SafeRepresenter.this.representScalar(tag, value, style);
/*     */     } }
/*     */   
/*     */   protected class RepresentBoolean implements Represent { protected RepresentBoolean() {}
/*     */     
/*     */     public Node representData(Object data) { String value;
/*     */       String value;
/* 138 */       if (Boolean.TRUE.equals(data)) {
/* 139 */         value = "true";
/*     */       } else {
/* 141 */         value = "false";
/*     */       }
/* 143 */       return SafeRepresenter.this.representScalar(Tag.BOOL, value);
/*     */     } }
/*     */   
/*     */   protected class RepresentNumber implements Represent { protected RepresentNumber() {}
/*     */     
/*     */     public Node representData(Object data) { String value;
/*     */       Tag tag;
/*     */       String value;
/* 151 */       if (((data instanceof Byte)) || ((data instanceof Short)) || ((data instanceof Integer)) || ((data instanceof Long)) || ((data instanceof BigInteger)))
/*     */       {
/* 153 */         Tag tag = Tag.INT;
/* 154 */         value = data.toString();
/*     */       } else {
/* 156 */         Number number = (Number)data;
/* 157 */         tag = Tag.FLOAT;
/* 158 */         String value; if (number.equals(Double.valueOf(NaN.0D))) {
/* 159 */           value = ".NaN"; } else { String value;
/* 160 */           if (number.equals(Double.valueOf(Double.POSITIVE_INFINITY))) {
/* 161 */             value = ".inf"; } else { String value;
/* 162 */             if (number.equals(Double.valueOf(Double.NEGATIVE_INFINITY))) {
/* 163 */               value = "-.inf";
/*     */             } else
/* 165 */               value = number.toString();
/*     */           }
/*     */         } }
/* 168 */       return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), tag), value);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentList implements Represent {
/*     */     protected RepresentList() {}
/*     */     
/* 175 */     public Node representData(Object data) { return SafeRepresenter.this.representSequence(SafeRepresenter.this.getTag(data.getClass(), Tag.SEQ), (List)data, null); }
/*     */   }
/*     */   
/*     */   protected class RepresentIterator implements Represent {
/*     */     protected RepresentIterator() {}
/*     */     
/*     */     public Node representData(Object data) {
/* 182 */       Iterator<Object> iter = (Iterator)data;
/* 183 */       return SafeRepresenter.this.representSequence(SafeRepresenter.this.getTag(data.getClass(), Tag.SEQ), new SafeRepresenter.IteratorWrapper(iter), null);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class IteratorWrapper implements Iterable<Object>
/*     */   {
/*     */     private Iterator<Object> iter;
/*     */     
/*     */     public IteratorWrapper(Iterator<Object> iter) {
/* 192 */       this.iter = iter;
/*     */     }
/*     */     
/*     */ 
/* 196 */     public Iterator<Object> iterator() { return this.iter; }
/*     */   }
/*     */   
/*     */   protected class RepresentArray implements Represent {
/*     */     protected RepresentArray() {}
/*     */     
/* 202 */     public Node representData(Object data) { Object[] array = (Object[])data;
/* 203 */       List<Object> list = Arrays.asList(array);
/* 204 */       return SafeRepresenter.this.representSequence(Tag.SEQ, list, null);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentPrimitiveArray
/*     */     implements Represent
/*     */   {
/*     */     protected RepresentPrimitiveArray() {}
/*     */     
/*     */     public Node representData(Object data)
/*     */     {
/* 215 */       Class<?> type = data.getClass().getComponentType();
/*     */       
/* 217 */       if (Byte.TYPE == type)
/* 218 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asByteList(data), null);
/* 219 */       if (Short.TYPE == type)
/* 220 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asShortList(data), null);
/* 221 */       if (Integer.TYPE == type)
/* 222 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asIntList(data), null);
/* 223 */       if (Long.TYPE == type)
/* 224 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asLongList(data), null);
/* 225 */       if (Float.TYPE == type)
/* 226 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asFloatList(data), null);
/* 227 */       if (Double.TYPE == type)
/* 228 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asDoubleList(data), null);
/* 229 */       if (Character.TYPE == type)
/* 230 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asCharList(data), null);
/* 231 */       if (Boolean.TYPE == type) {
/* 232 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asBooleanList(data), null);
/*     */       }
/*     */       
/* 235 */       throw new YAMLException("Unexpected primitive '" + type.getCanonicalName() + "'");
/*     */     }
/*     */     
/*     */     private List<Byte> asByteList(Object in) {
/* 239 */       byte[] array = (byte[])in;
/* 240 */       List<Byte> list = new ArrayList(array.length);
/* 241 */       for (int i = 0; i < array.length; i++)
/* 242 */         list.add(Byte.valueOf(array[i]));
/* 243 */       return list;
/*     */     }
/*     */     
/*     */     private List<Short> asShortList(Object in) {
/* 247 */       short[] array = (short[])in;
/* 248 */       List<Short> list = new ArrayList(array.length);
/* 249 */       for (int i = 0; i < array.length; i++)
/* 250 */         list.add(Short.valueOf(array[i]));
/* 251 */       return list;
/*     */     }
/*     */     
/*     */     private List<Integer> asIntList(Object in) {
/* 255 */       int[] array = (int[])in;
/* 256 */       List<Integer> list = new ArrayList(array.length);
/* 257 */       for (int i = 0; i < array.length; i++)
/* 258 */         list.add(Integer.valueOf(array[i]));
/* 259 */       return list;
/*     */     }
/*     */     
/*     */     private List<Long> asLongList(Object in) {
/* 263 */       long[] array = (long[])in;
/* 264 */       List<Long> list = new ArrayList(array.length);
/* 265 */       for (int i = 0; i < array.length; i++)
/* 266 */         list.add(Long.valueOf(array[i]));
/* 267 */       return list;
/*     */     }
/*     */     
/*     */     private List<Float> asFloatList(Object in) {
/* 271 */       float[] array = (float[])in;
/* 272 */       List<Float> list = new ArrayList(array.length);
/* 273 */       for (int i = 0; i < array.length; i++)
/* 274 */         list.add(Float.valueOf(array[i]));
/* 275 */       return list;
/*     */     }
/*     */     
/*     */     private List<Double> asDoubleList(Object in) {
/* 279 */       double[] array = (double[])in;
/* 280 */       List<Double> list = new ArrayList(array.length);
/* 281 */       for (int i = 0; i < array.length; i++)
/* 282 */         list.add(Double.valueOf(array[i]));
/* 283 */       return list;
/*     */     }
/*     */     
/*     */     private List<Character> asCharList(Object in) {
/* 287 */       char[] array = (char[])in;
/* 288 */       List<Character> list = new ArrayList(array.length);
/* 289 */       for (int i = 0; i < array.length; i++)
/* 290 */         list.add(Character.valueOf(array[i]));
/* 291 */       return list;
/*     */     }
/*     */     
/*     */     private List<Boolean> asBooleanList(Object in) {
/* 295 */       boolean[] array = (boolean[])in;
/* 296 */       List<Boolean> list = new ArrayList(array.length);
/* 297 */       for (int i = 0; i < array.length; i++)
/* 298 */         list.add(Boolean.valueOf(array[i]));
/* 299 */       return list;
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentMap implements Represent {
/*     */     protected RepresentMap() {}
/*     */     
/* 306 */     public Node representData(Object data) { return SafeRepresenter.this.representMapping(SafeRepresenter.this.getTag(data.getClass(), Tag.MAP), (Map)data, null); }
/*     */   }
/*     */   
/*     */   protected class RepresentSet implements Represent
/*     */   {
/*     */     protected RepresentSet() {}
/*     */     
/*     */     public Node representData(Object data) {
/* 314 */       Map<Object, Object> value = new LinkedHashMap();
/* 315 */       Set<Object> set = (Set)data;
/* 316 */       for (Object key : set) {
/* 317 */         value.put(key, null);
/*     */       }
/* 319 */       return SafeRepresenter.this.representMapping(SafeRepresenter.this.getTag(data.getClass(), Tag.SET), value, null);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentDate implements Represent { protected RepresentDate() {}
/*     */     
/*     */     public Node representData(Object data) { Calendar calendar;
/*     */       Calendar calendar;
/* 327 */       if ((data instanceof Calendar)) {
/* 328 */         calendar = (Calendar)data;
/*     */       } else {
/* 330 */         calendar = Calendar.getInstance(SafeRepresenter.this.getTimeZone() == null ? TimeZone.getTimeZone("UTC") : SafeRepresenter.this.timeZone);
/*     */         
/* 332 */         calendar.setTime((Date)data);
/*     */       }
/* 334 */       int years = calendar.get(1);
/* 335 */       int months = calendar.get(2) + 1;
/* 336 */       int days = calendar.get(5);
/* 337 */       int hour24 = calendar.get(11);
/* 338 */       int minutes = calendar.get(12);
/* 339 */       int seconds = calendar.get(13);
/* 340 */       int millis = calendar.get(14);
/* 341 */       StringBuilder buffer = new StringBuilder(String.valueOf(years));
/* 342 */       while (buffer.length() < 4)
/*     */       {
/* 344 */         buffer.insert(0, "0");
/*     */       }
/* 346 */       buffer.append("-");
/* 347 */       if (months < 10) {
/* 348 */         buffer.append("0");
/*     */       }
/* 350 */       buffer.append(String.valueOf(months));
/* 351 */       buffer.append("-");
/* 352 */       if (days < 10) {
/* 353 */         buffer.append("0");
/*     */       }
/* 355 */       buffer.append(String.valueOf(days));
/* 356 */       buffer.append("T");
/* 357 */       if (hour24 < 10) {
/* 358 */         buffer.append("0");
/*     */       }
/* 360 */       buffer.append(String.valueOf(hour24));
/* 361 */       buffer.append(":");
/* 362 */       if (minutes < 10) {
/* 363 */         buffer.append("0");
/*     */       }
/* 365 */       buffer.append(String.valueOf(minutes));
/* 366 */       buffer.append(":");
/* 367 */       if (seconds < 10) {
/* 368 */         buffer.append("0");
/*     */       }
/* 370 */       buffer.append(String.valueOf(seconds));
/* 371 */       if (millis > 0) {
/* 372 */         if (millis < 10) {
/* 373 */           buffer.append(".00");
/* 374 */         } else if (millis < 100) {
/* 375 */           buffer.append(".0");
/*     */         } else {
/* 377 */           buffer.append(".");
/*     */         }
/* 379 */         buffer.append(String.valueOf(millis));
/*     */       }
/* 381 */       if (TimeZone.getTimeZone("UTC").equals(calendar.getTimeZone())) {
/* 382 */         buffer.append("Z");
/*     */       }
/*     */       else {
/* 385 */         int gmtOffset = calendar.getTimeZone().getOffset(calendar.get(0), calendar.get(1), calendar.get(2), calendar.get(5), calendar.get(7), calendar.get(14));
/*     */         
/*     */ 
/*     */ 
/* 389 */         int minutesOffset = gmtOffset / 60000;
/* 390 */         int hoursOffset = minutesOffset / 60;
/* 391 */         int partOfHour = minutesOffset % 60;
/* 392 */         buffer.append((hoursOffset > 0 ? "+" : "") + hoursOffset + ":" + (partOfHour < 10 ? "0" + partOfHour : Integer.valueOf(partOfHour)));
/*     */       }
/*     */       
/* 395 */       return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), Tag.TIMESTAMP), buffer.toString(), null);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentEnum implements Represent { protected RepresentEnum() {}
/*     */     
/* 401 */     public Node representData(Object data) { Tag tag = new Tag(data.getClass());
/* 402 */       return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), tag), ((Enum)data).name());
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentByteArray implements Represent { protected RepresentByteArray() {}
/*     */     
/* 408 */     public Node representData(Object data) { char[] binary = Base64Coder.encode((byte[])data);
/* 409 */       return SafeRepresenter.this.representScalar(Tag.BINARY, String.valueOf(binary), Character.valueOf('|'));
/*     */     }
/*     */   }
/*     */   
/*     */   public TimeZone getTimeZone() {
/* 414 */     return this.timeZone;
/*     */   }
/*     */   
/*     */ 
/* 418 */   public void setTimeZone(TimeZone timeZone) { this.timeZone = timeZone; }
/*     */   
/*     */   protected class RepresentUuid implements Represent {
/*     */     protected RepresentUuid() {}
/*     */     
/* 423 */     public Node representData(Object data) { return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), new Tag(UUID.class)), data.toString()); }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\representer\SafeRepresenter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */