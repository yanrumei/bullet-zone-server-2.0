/*     */ package org.yaml.snakeyaml.constructor;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.text.NumberFormat;
/*     */ import java.text.ParseException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
/*     */ import org.yaml.snakeyaml.nodes.NodeTuple;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.SequenceNode;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SafeConstructor
/*     */   extends BaseConstructor
/*     */ {
/*  49 */   public static final ConstructUndefined undefinedConstructor = new ConstructUndefined();
/*     */   
/*     */   public SafeConstructor() {
/*  52 */     this.yamlConstructors.put(Tag.NULL, new ConstructYamlNull());
/*  53 */     this.yamlConstructors.put(Tag.BOOL, new ConstructYamlBool());
/*  54 */     this.yamlConstructors.put(Tag.INT, new ConstructYamlInt());
/*  55 */     this.yamlConstructors.put(Tag.FLOAT, new ConstructYamlFloat());
/*  56 */     this.yamlConstructors.put(Tag.BINARY, new ConstructYamlBinary());
/*  57 */     this.yamlConstructors.put(Tag.TIMESTAMP, new ConstructYamlTimestamp());
/*  58 */     this.yamlConstructors.put(Tag.OMAP, new ConstructYamlOmap());
/*  59 */     this.yamlConstructors.put(Tag.PAIRS, new ConstructYamlPairs());
/*  60 */     this.yamlConstructors.put(Tag.SET, new ConstructYamlSet());
/*  61 */     this.yamlConstructors.put(Tag.STR, new ConstructYamlStr());
/*  62 */     this.yamlConstructors.put(Tag.SEQ, new ConstructYamlSeq());
/*  63 */     this.yamlConstructors.put(Tag.MAP, new ConstructYamlMap());
/*  64 */     this.yamlConstructors.put(null, undefinedConstructor);
/*  65 */     this.yamlClassConstructors.put(NodeId.scalar, undefinedConstructor);
/*  66 */     this.yamlClassConstructors.put(NodeId.sequence, undefinedConstructor);
/*  67 */     this.yamlClassConstructors.put(NodeId.mapping, undefinedConstructor);
/*     */   }
/*     */   
/*     */   protected void flattenMapping(MappingNode node)
/*     */   {
/*  72 */     if (node.isMerged()) {
/*  73 */       node.setValue(mergeNode(node, true, new HashMap(), new ArrayList()));
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
/*     */   private List<NodeTuple> mergeNode(MappingNode node, boolean isPreffered, Map<Object, Integer> key2index, List<NodeTuple> values)
/*     */   {
/*  94 */     List<NodeTuple> nodeValue = node.getValue();
/*     */     
/*  96 */     Collections.reverse(nodeValue);
/*  97 */     for (Iterator<NodeTuple> iter = nodeValue.iterator(); iter.hasNext();) {
/*  98 */       NodeTuple nodeTuple = (NodeTuple)iter.next();
/*  99 */       Node keyNode = nodeTuple.getKeyNode();
/* 100 */       Node valueNode = nodeTuple.getValueNode();
/* 101 */       if (keyNode.getTag().equals(Tag.MERGE)) {
/* 102 */         iter.remove();
/* 103 */         switch (valueNode.getNodeId()) {
/*     */         case mapping: 
/* 105 */           MappingNode mn = (MappingNode)valueNode;
/* 106 */           mergeNode(mn, false, key2index, values);
/* 107 */           break;
/*     */         case sequence: 
/* 109 */           SequenceNode sn = (SequenceNode)valueNode;
/* 110 */           List<Node> vals = sn.getValue();
/* 111 */           for (Node subnode : vals) {
/* 112 */             if (!(subnode instanceof MappingNode)) {
/* 113 */               throw new ConstructorException("while constructing a mapping", node.getStartMark(), "expected a mapping for merging, but found " + subnode.getNodeId(), subnode.getStartMark());
/*     */             }
/*     */             
/*     */ 
/*     */ 
/* 118 */             MappingNode mnode = (MappingNode)subnode;
/* 119 */             mergeNode(mnode, false, key2index, values);
/*     */           }
/* 121 */           break;
/*     */         default: 
/* 123 */           throw new ConstructorException("while constructing a mapping", node.getStartMark(), "expected a mapping or list of mappings for merging, but found " + valueNode.getNodeId(), valueNode.getStartMark());
/*     */         
/*     */         }
/*     */         
/*     */       }
/*     */       else
/*     */       {
/* 130 */         Object key = constructObject(keyNode);
/* 131 */         if (!key2index.containsKey(key)) {
/* 132 */           values.add(nodeTuple);
/*     */           
/* 134 */           key2index.put(key, Integer.valueOf(values.size() - 1));
/* 135 */         } else if (isPreffered)
/*     */         {
/*     */ 
/* 138 */           values.set(((Integer)key2index.get(key)).intValue(), nodeTuple);
/*     */         }
/*     */       }
/*     */     }
/* 142 */     return values;
/*     */   }
/*     */   
/*     */   protected void constructMapping2ndStep(MappingNode node, Map<Object, Object> mapping) {
/* 146 */     flattenMapping(node);
/* 147 */     super.constructMapping2ndStep(node, mapping);
/*     */   }
/*     */   
/*     */   protected void constructSet2ndStep(MappingNode node, Set<Object> set)
/*     */   {
/* 152 */     flattenMapping(node);
/* 153 */     super.constructSet2ndStep(node, set);
/*     */   }
/*     */   
/*     */   public class ConstructYamlNull extends AbstractConstruct { public ConstructYamlNull() {}
/*     */     
/* 158 */     public Object construct(Node node) { SafeConstructor.this.constructScalar((ScalarNode)node);
/* 159 */       return null;
/*     */     }
/*     */   }
/*     */   
/* 163 */   private static final Map<String, Boolean> BOOL_VALUES = new HashMap();
/*     */   
/* 165 */   static { BOOL_VALUES.put("yes", Boolean.TRUE);
/* 166 */     BOOL_VALUES.put("no", Boolean.FALSE);
/* 167 */     BOOL_VALUES.put("true", Boolean.TRUE);
/* 168 */     BOOL_VALUES.put("false", Boolean.FALSE);
/* 169 */     BOOL_VALUES.put("on", Boolean.TRUE);
/* 170 */     BOOL_VALUES.put("off", Boolean.FALSE);
/*     */   }
/*     */   
/*     */   public class ConstructYamlBool extends AbstractConstruct { public ConstructYamlBool() {}
/*     */     
/* 175 */     public Object construct(Node node) { String val = (String)SafeConstructor.this.constructScalar((ScalarNode)node);
/* 176 */       return SafeConstructor.BOOL_VALUES.get(val.toLowerCase());
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlInt extends AbstractConstruct { public ConstructYamlInt() {}
/*     */     
/* 182 */     public Object construct(Node node) { String value = SafeConstructor.this.constructScalar((ScalarNode)node).toString().replaceAll("_", "");
/* 183 */       int sign = 1;
/* 184 */       char first = value.charAt(0);
/* 185 */       if (first == '-') {
/* 186 */         sign = -1;
/* 187 */         value = value.substring(1);
/* 188 */       } else if (first == '+') {
/* 189 */         value = value.substring(1);
/*     */       }
/* 191 */       int base = 10;
/* 192 */       if ("0".equals(value))
/* 193 */         return Integer.valueOf(0);
/* 194 */       if (value.startsWith("0b")) {
/* 195 */         value = value.substring(2);
/* 196 */         base = 2;
/* 197 */       } else if (value.startsWith("0x")) {
/* 198 */         value = value.substring(2);
/* 199 */         base = 16;
/* 200 */       } else if (value.startsWith("0")) {
/* 201 */         value = value.substring(1);
/* 202 */         base = 8;
/* 203 */       } else { if (value.indexOf(':') != -1) {
/* 204 */           String[] digits = value.split(":");
/* 205 */           int bes = 1;
/* 206 */           int val = 0;
/* 207 */           int i = 0; for (int j = digits.length; i < j; i++) {
/* 208 */             val = (int)(val + Long.parseLong(digits[(j - i - 1)]) * bes);
/* 209 */             bes *= 60;
/*     */           }
/* 211 */           return SafeConstructor.this.createNumber(sign, String.valueOf(val), 10);
/*     */         }
/* 213 */         return SafeConstructor.this.createNumber(sign, value, 10);
/*     */       }
/* 215 */       return SafeConstructor.this.createNumber(sign, value, base);
/*     */     }
/*     */   }
/*     */   
/*     */   private Number createNumber(int sign, String number, int radix)
/*     */   {
/* 221 */     if (sign < 0)
/* 222 */       number = "-" + number;
/*     */     Number result;
/*     */     try {
/* 225 */       result = Integer.valueOf(number, radix);
/*     */     } catch (NumberFormatException e) {
/*     */       try {
/* 228 */         result = Long.valueOf(number, radix);
/*     */       } catch (NumberFormatException e1) {
/* 230 */         result = new BigInteger(number, radix);
/*     */       }
/*     */     }
/* 233 */     return result;
/*     */   }
/*     */   
/*     */   public class ConstructYamlFloat extends AbstractConstruct { public ConstructYamlFloat() {}
/*     */     
/* 238 */     public Object construct(Node node) { String value = SafeConstructor.this.constructScalar((ScalarNode)node).toString().replaceAll("_", "");
/* 239 */       int sign = 1;
/* 240 */       char first = value.charAt(0);
/* 241 */       if (first == '-') {
/* 242 */         sign = -1;
/* 243 */         value = value.substring(1);
/* 244 */       } else if (first == '+') {
/* 245 */         value = value.substring(1);
/*     */       }
/* 247 */       String valLower = value.toLowerCase();
/* 248 */       if (".inf".equals(valLower))
/* 249 */         return new Double(sign == -1 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
/* 250 */       if (".nan".equals(valLower))
/* 251 */         return new Double(NaN.0D);
/* 252 */       if (value.indexOf(':') != -1) {
/* 253 */         String[] digits = value.split(":");
/* 254 */         int bes = 1;
/* 255 */         double val = 0.0D;
/* 256 */         int i = 0; for (int j = digits.length; i < j; i++) {
/* 257 */           val += Double.parseDouble(digits[(j - i - 1)]) * bes;
/* 258 */           bes *= 60;
/*     */         }
/* 260 */         return new Double(sign * val);
/*     */       }
/* 262 */       Double d = Double.valueOf(value);
/* 263 */       return new Double(d.doubleValue() * sign);
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlBinary extends AbstractConstruct {
/*     */     public ConstructYamlBinary() {}
/*     */     
/* 270 */     public Object construct(Node node) { byte[] decoded = Base64Coder.decode(SafeConstructor.this.constructScalar((ScalarNode)node).toString().toCharArray());
/*     */       
/* 272 */       return decoded;
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlNumber extends AbstractConstruct { public ConstructYamlNumber() {}
/*     */     
/* 278 */     private final NumberFormat nf = NumberFormat.getInstance();
/*     */     
/*     */     public Object construct(Node node) {
/* 281 */       ScalarNode scalar = (ScalarNode)node;
/*     */       try {
/* 283 */         return this.nf.parse(scalar.getValue());
/*     */       } catch (ParseException e) {
/* 285 */         String lowerCaseValue = scalar.getValue().toLowerCase();
/* 286 */         if ((lowerCaseValue.contains("inf")) || (lowerCaseValue.contains("nan")))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 293 */           return (Number)((Construct)SafeConstructor.this.yamlConstructors.get(Tag.FLOAT)).construct(node);
/*     */         }
/* 295 */         throw new IllegalArgumentException("Unable to parse as Number: " + scalar.getValue());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 302 */   private static final Pattern TIMESTAMP_REGEXP = Pattern.compile("^([0-9][0-9][0-9][0-9])-([0-9][0-9]?)-([0-9][0-9]?)(?:(?:[Tt]|[ \t]+)([0-9][0-9]?):([0-9][0-9]):([0-9][0-9])(?:\\.([0-9]*))?(?:[ \t]*(?:Z|([-+][0-9][0-9]?)(?::([0-9][0-9])?)?))?)?$");
/*     */   
/* 304 */   private static final Pattern YMD_REGEXP = Pattern.compile("^([0-9][0-9][0-9][0-9])-([0-9][0-9]?)-([0-9][0-9]?)$");
/*     */   
/*     */   public static class ConstructYamlTimestamp extends AbstractConstruct
/*     */   {
/*     */     private Calendar calendar;
/*     */     
/*     */     public Calendar getCalendar() {
/* 311 */       return this.calendar;
/*     */     }
/*     */     
/*     */     public Object construct(Node node) {
/* 315 */       ScalarNode scalar = (ScalarNode)node;
/* 316 */       String nodeValue = scalar.getValue();
/* 317 */       Matcher match = SafeConstructor.YMD_REGEXP.matcher(nodeValue);
/* 318 */       if (match.matches()) {
/* 319 */         String year_s = match.group(1);
/* 320 */         String month_s = match.group(2);
/* 321 */         String day_s = match.group(3);
/* 322 */         this.calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
/* 323 */         this.calendar.clear();
/* 324 */         this.calendar.set(1, Integer.parseInt(year_s));
/*     */         
/* 326 */         this.calendar.set(2, Integer.parseInt(month_s) - 1);
/* 327 */         this.calendar.set(5, Integer.parseInt(day_s));
/* 328 */         return this.calendar.getTime();
/*     */       }
/* 330 */       match = SafeConstructor.TIMESTAMP_REGEXP.matcher(nodeValue);
/* 331 */       if (!match.matches()) {
/* 332 */         throw new YAMLException("Unexpected timestamp: " + nodeValue);
/*     */       }
/* 334 */       String year_s = match.group(1);
/* 335 */       String month_s = match.group(2);
/* 336 */       String day_s = match.group(3);
/* 337 */       String hour_s = match.group(4);
/* 338 */       String min_s = match.group(5);
/*     */       
/* 340 */       String seconds = match.group(6);
/* 341 */       String millis = match.group(7);
/* 342 */       if (millis != null) {
/* 343 */         seconds = seconds + "." + millis;
/*     */       }
/* 345 */       double fractions = Double.parseDouble(seconds);
/* 346 */       int sec_s = (int)Math.round(Math.floor(fractions));
/* 347 */       int usec = (int)Math.round((fractions - sec_s) * 1000.0D);
/*     */       
/* 349 */       String timezoneh_s = match.group(8);
/* 350 */       String timezonem_s = match.group(9);
/*     */       TimeZone timeZone;
/* 352 */       TimeZone timeZone; if (timezoneh_s != null) {
/* 353 */         String time = timezonem_s != null ? ":" + timezonem_s : "00";
/* 354 */         timeZone = TimeZone.getTimeZone("GMT" + timezoneh_s + time);
/*     */       }
/*     */       else {
/* 357 */         timeZone = TimeZone.getTimeZone("UTC");
/*     */       }
/* 359 */       this.calendar = Calendar.getInstance(timeZone);
/* 360 */       this.calendar.set(1, Integer.parseInt(year_s));
/*     */       
/* 362 */       this.calendar.set(2, Integer.parseInt(month_s) - 1);
/* 363 */       this.calendar.set(5, Integer.parseInt(day_s));
/* 364 */       this.calendar.set(11, Integer.parseInt(hour_s));
/* 365 */       this.calendar.set(12, Integer.parseInt(min_s));
/* 366 */       this.calendar.set(13, sec_s);
/* 367 */       this.calendar.set(14, usec);
/* 368 */       return this.calendar.getTime();
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlOmap extends AbstractConstruct
/*     */   {
/*     */     public ConstructYamlOmap() {}
/*     */     
/*     */     public Object construct(Node node) {
/* 377 */       Map<Object, Object> omap = new LinkedHashMap();
/* 378 */       if (!(node instanceof SequenceNode)) {
/* 379 */         throw new ConstructorException("while constructing an ordered map", node.getStartMark(), "expected a sequence, but found " + node.getNodeId(), node.getStartMark());
/*     */       }
/*     */       
/*     */ 
/* 383 */       SequenceNode snode = (SequenceNode)node;
/* 384 */       for (Node subnode : snode.getValue()) {
/* 385 */         if (!(subnode instanceof MappingNode)) {
/* 386 */           throw new ConstructorException("while constructing an ordered map", node.getStartMark(), "expected a mapping of length 1, but found " + subnode.getNodeId(), subnode.getStartMark());
/*     */         }
/*     */         
/*     */ 
/* 390 */         MappingNode mnode = (MappingNode)subnode;
/* 391 */         if (mnode.getValue().size() != 1) {
/* 392 */           throw new ConstructorException("while constructing an ordered map", node.getStartMark(), "expected a single mapping item, but found " + mnode.getValue().size() + " items", mnode.getStartMark());
/*     */         }
/*     */         
/*     */ 
/* 396 */         Node keyNode = ((NodeTuple)mnode.getValue().get(0)).getKeyNode();
/* 397 */         Node valueNode = ((NodeTuple)mnode.getValue().get(0)).getValueNode();
/* 398 */         Object key = SafeConstructor.this.constructObject(keyNode);
/* 399 */         Object value = SafeConstructor.this.constructObject(valueNode);
/* 400 */         omap.put(key, value);
/*     */       }
/* 402 */       return omap;
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlPairs extends AbstractConstruct
/*     */   {
/*     */     public ConstructYamlPairs() {}
/*     */     
/*     */     public Object construct(Node node) {
/* 411 */       if (!(node instanceof SequenceNode)) {
/* 412 */         throw new ConstructorException("while constructing pairs", node.getStartMark(), "expected a sequence, but found " + node.getNodeId(), node.getStartMark());
/*     */       }
/*     */       
/* 415 */       SequenceNode snode = (SequenceNode)node;
/* 416 */       List<Object[]> pairs = new ArrayList(snode.getValue().size());
/* 417 */       for (Node subnode : snode.getValue()) {
/* 418 */         if (!(subnode instanceof MappingNode)) {
/* 419 */           throw new ConstructorException("while constructingpairs", node.getStartMark(), "expected a mapping of length 1, but found " + subnode.getNodeId(), subnode.getStartMark());
/*     */         }
/*     */         
/*     */ 
/* 423 */         MappingNode mnode = (MappingNode)subnode;
/* 424 */         if (mnode.getValue().size() != 1) {
/* 425 */           throw new ConstructorException("while constructing pairs", node.getStartMark(), "expected a single mapping item, but found " + mnode.getValue().size() + " items", mnode.getStartMark());
/*     */         }
/*     */         
/*     */ 
/* 429 */         Node keyNode = ((NodeTuple)mnode.getValue().get(0)).getKeyNode();
/* 430 */         Node valueNode = ((NodeTuple)mnode.getValue().get(0)).getValueNode();
/* 431 */         Object key = SafeConstructor.this.constructObject(keyNode);
/* 432 */         Object value = SafeConstructor.this.constructObject(valueNode);
/* 433 */         pairs.add(new Object[] { key, value });
/*     */       }
/* 435 */       return pairs;
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlSet implements Construct { public ConstructYamlSet() {}
/*     */     
/* 441 */     public Object construct(Node node) { if (node.isTwoStepsConstruction()) {
/* 442 */         return SafeConstructor.this.createDefaultSet();
/*     */       }
/* 444 */       return SafeConstructor.this.constructSet((MappingNode)node);
/*     */     }
/*     */     
/*     */ 
/*     */     public void construct2ndStep(Node node, Object object)
/*     */     {
/* 450 */       if (node.isTwoStepsConstruction()) {
/* 451 */         SafeConstructor.this.constructSet2ndStep((MappingNode)node, (Set)object);
/*     */       } else
/* 453 */         throw new YAMLException("Unexpected recursive set structure. Node: " + node);
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlStr extends AbstractConstruct {
/*     */     public ConstructYamlStr() {}
/*     */     
/* 460 */     public Object construct(Node node) { return SafeConstructor.this.constructScalar((ScalarNode)node); }
/*     */   }
/*     */   
/*     */   public class ConstructYamlSeq implements Construct {
/*     */     public ConstructYamlSeq() {}
/*     */     
/* 466 */     public Object construct(Node node) { SequenceNode seqNode = (SequenceNode)node;
/* 467 */       if (node.isTwoStepsConstruction()) {
/* 468 */         return SafeConstructor.this.createDefaultList(seqNode.getValue().size());
/*     */       }
/* 470 */       return SafeConstructor.this.constructSequence(seqNode);
/*     */     }
/*     */     
/*     */ 
/*     */     public void construct2ndStep(Node node, Object data)
/*     */     {
/* 476 */       if (node.isTwoStepsConstruction()) {
/* 477 */         SafeConstructor.this.constructSequenceStep2((SequenceNode)node, (List)data);
/*     */       } else
/* 479 */         throw new YAMLException("Unexpected recursive sequence structure. Node: " + node);
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlMap implements Construct {
/*     */     public ConstructYamlMap() {}
/*     */     
/* 486 */     public Object construct(Node node) { if (node.isTwoStepsConstruction()) {
/* 487 */         return SafeConstructor.this.createDefaultMap();
/*     */       }
/* 489 */       return SafeConstructor.this.constructMapping((MappingNode)node);
/*     */     }
/*     */     
/*     */ 
/*     */     public void construct2ndStep(Node node, Object object)
/*     */     {
/* 495 */       if (node.isTwoStepsConstruction()) {
/* 496 */         SafeConstructor.this.constructMapping2ndStep((MappingNode)node, (Map)object);
/*     */       } else {
/* 498 */         throw new YAMLException("Unexpected recursive mapping structure. Node: " + node);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ConstructUndefined extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/* 505 */       throw new ConstructorException(null, null, "could not determine a constructor for the tag " + node.getTag(), node.getStartMark());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\constructor\SafeConstructor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */