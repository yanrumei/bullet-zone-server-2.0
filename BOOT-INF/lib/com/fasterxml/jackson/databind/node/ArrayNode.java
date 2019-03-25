/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonPointer;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializable;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.util.RawValue;
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArrayNode
/*     */   extends ContainerNode<ArrayNode>
/*     */ {
/*     */   private final List<JsonNode> _children;
/*     */   
/*     */   public ArrayNode(JsonNodeFactory nf)
/*     */   {
/*  29 */     super(nf);
/*  30 */     this._children = new ArrayList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ArrayNode(JsonNodeFactory nf, int capacity)
/*     */   {
/*  37 */     super(nf);
/*  38 */     this._children = new ArrayList(capacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ArrayNode(JsonNodeFactory nf, List<JsonNode> children)
/*     */   {
/*  45 */     super(nf);
/*  46 */     this._children = children;
/*     */   }
/*     */   
/*     */   protected JsonNode _at(JsonPointer ptr)
/*     */   {
/*  51 */     return get(ptr.getMatchingIndex());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode deepCopy()
/*     */   {
/*  59 */     ArrayNode ret = new ArrayNode(this._nodeFactory);
/*     */     
/*  61 */     for (JsonNode element : this._children) {
/*  62 */       ret._children.add(element.deepCopy());
/*     */     }
/*  64 */     return ret;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEmpty(SerializerProvider serializers)
/*     */   {
/*  75 */     return this._children.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNodeType getNodeType()
/*     */   {
/*  86 */     return JsonNodeType.ARRAY;
/*     */   }
/*     */   
/*  89 */   public JsonToken asToken() { return JsonToken.START_ARRAY; }
/*     */   
/*     */   public int size()
/*     */   {
/*  93 */     return this._children.size();
/*     */   }
/*     */   
/*     */   public Iterator<JsonNode> elements()
/*     */   {
/*  98 */     return this._children.iterator();
/*     */   }
/*     */   
/*     */   public JsonNode get(int index)
/*     */   {
/* 103 */     if ((index >= 0) && (index < this._children.size())) {
/* 104 */       return (JsonNode)this._children.get(index);
/*     */     }
/* 106 */     return null;
/*     */   }
/*     */   
/*     */   public JsonNode get(String fieldName) {
/* 110 */     return null;
/*     */   }
/*     */   
/* 113 */   public JsonNode path(String fieldName) { return MissingNode.getInstance(); }
/*     */   
/*     */   public JsonNode path(int index)
/*     */   {
/* 117 */     if ((index >= 0) && (index < this._children.size())) {
/* 118 */       return (JsonNode)this._children.get(index);
/*     */     }
/* 120 */     return MissingNode.getInstance();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Comparator<JsonNode> comparator, JsonNode o)
/*     */   {
/* 126 */     if (!(o instanceof ArrayNode)) {
/* 127 */       return false;
/*     */     }
/* 129 */     ArrayNode other = (ArrayNode)o;
/* 130 */     int len = this._children.size();
/* 131 */     if (other.size() != len) {
/* 132 */       return false;
/*     */     }
/* 134 */     List<JsonNode> l1 = this._children;
/* 135 */     List<JsonNode> l2 = other._children;
/* 136 */     for (int i = 0; i < len; i++) {
/* 137 */       if (!((JsonNode)l1.get(i)).equals(comparator, (JsonNode)l2.get(i))) {
/* 138 */         return false;
/*     */       }
/*     */     }
/* 141 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void serialize(JsonGenerator f, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 153 */     List<JsonNode> c = this._children;
/* 154 */     int size = c.size();
/* 155 */     f.writeStartArray(size);
/* 156 */     for (int i = 0; i < size; i++)
/*     */     {
/* 158 */       JsonNode n = (JsonNode)c.get(i);
/* 159 */       if ((n instanceof BaseJsonNode)) {
/* 160 */         ((BaseJsonNode)n).serialize(f, provider);
/*     */       } else {
/* 162 */         n.serialize(f, provider);
/*     */       }
/*     */     }
/* 165 */     f.writeEndArray();
/*     */   }
/*     */   
/*     */ 
/*     */   public void serializeWithType(JsonGenerator jg, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException
/*     */   {
/* 172 */     typeSer.writeTypePrefixForArray(this, jg);
/* 173 */     for (JsonNode n : this._children) {
/* 174 */       ((BaseJsonNode)n).serialize(jg, provider);
/*     */     }
/* 176 */     typeSer.writeTypeSuffixForArray(this, jg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNode findValue(String fieldName)
/*     */   {
/* 188 */     for (JsonNode node : this._children) {
/* 189 */       JsonNode value = node.findValue(fieldName);
/* 190 */       if (value != null) {
/* 191 */         return value;
/*     */       }
/*     */     }
/* 194 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<JsonNode> findValues(String fieldName, List<JsonNode> foundSoFar)
/*     */   {
/* 200 */     for (JsonNode node : this._children) {
/* 201 */       foundSoFar = node.findValues(fieldName, foundSoFar);
/*     */     }
/* 203 */     return foundSoFar;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<String> findValuesAsText(String fieldName, List<String> foundSoFar)
/*     */   {
/* 209 */     for (JsonNode node : this._children) {
/* 210 */       foundSoFar = node.findValuesAsText(fieldName, foundSoFar);
/*     */     }
/* 212 */     return foundSoFar;
/*     */   }
/*     */   
/*     */ 
/*     */   public ObjectNode findParent(String fieldName)
/*     */   {
/* 218 */     for (JsonNode node : this._children) {
/* 219 */       JsonNode parent = node.findParent(fieldName);
/* 220 */       if (parent != null) {
/* 221 */         return (ObjectNode)parent;
/*     */       }
/*     */     }
/* 224 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<JsonNode> findParents(String fieldName, List<JsonNode> foundSoFar)
/*     */   {
/* 230 */     for (JsonNode node : this._children) {
/* 231 */       foundSoFar = node.findParents(fieldName, foundSoFar);
/*     */     }
/* 233 */     return foundSoFar;
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
/*     */   public JsonNode set(int index, JsonNode value)
/*     */   {
/* 255 */     if (value == null) {
/* 256 */       value = nullNode();
/*     */     }
/* 258 */     if ((index < 0) || (index >= this._children.size())) {
/* 259 */       throw new IndexOutOfBoundsException("Illegal index " + index + ", array size " + size());
/*     */     }
/* 261 */     return (JsonNode)this._children.set(index, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(JsonNode value)
/*     */   {
/* 271 */     if (value == null) {
/* 272 */       value = nullNode();
/*     */     }
/* 274 */     _add(value);
/* 275 */     return this;
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
/*     */   public ArrayNode addAll(ArrayNode other)
/*     */   {
/* 288 */     this._children.addAll(other._children);
/* 289 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode addAll(Collection<? extends JsonNode> nodes)
/*     */   {
/* 301 */     this._children.addAll(nodes);
/* 302 */     return this;
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
/*     */   public ArrayNode insert(int index, JsonNode value)
/*     */   {
/* 316 */     if (value == null) {
/* 317 */       value = nullNode();
/*     */     }
/* 319 */     _insert(index, value);
/* 320 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNode remove(int index)
/*     */   {
/* 332 */     if ((index >= 0) && (index < this._children.size())) {
/* 333 */       return (JsonNode)this._children.remove(index);
/*     */     }
/* 335 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode removeAll()
/*     */   {
/* 347 */     this._children.clear();
/* 348 */     return this;
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
/*     */   public ArrayNode addArray()
/*     */   {
/* 365 */     ArrayNode n = arrayNode();
/* 366 */     _add(n);
/* 367 */     return n;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode addObject()
/*     */   {
/* 378 */     ObjectNode n = objectNode();
/* 379 */     _add(n);
/* 380 */     return n;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode addPOJO(Object value)
/*     */   {
/* 391 */     if (value == null) {
/* 392 */       addNull();
/*     */     } else {
/* 394 */       _add(pojoNode(value));
/*     */     }
/* 396 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode addRawValue(RawValue raw)
/*     */   {
/* 405 */     if (raw == null) {
/* 406 */       addNull();
/*     */     } else {
/* 408 */       _add(rawValueNode(raw));
/*     */     }
/* 410 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode addNull()
/*     */   {
/* 420 */     _add(nullNode());
/* 421 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(int v)
/*     */   {
/* 430 */     _add(numberNode(v));
/* 431 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(Integer value)
/*     */   {
/* 441 */     if (value == null) {
/* 442 */       return addNull();
/*     */     }
/* 444 */     return _add(numberNode(value.intValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(long v)
/*     */   {
/* 452 */     return _add(numberNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(Long value)
/*     */   {
/* 461 */     if (value == null) {
/* 462 */       return addNull();
/*     */     }
/* 464 */     return _add(numberNode(value.longValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(float v)
/*     */   {
/* 473 */     return _add(numberNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(Float value)
/*     */   {
/* 483 */     if (value == null) {
/* 484 */       return addNull();
/*     */     }
/* 486 */     return _add(numberNode(value.floatValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(double v)
/*     */   {
/* 495 */     return _add(numberNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(Double value)
/*     */   {
/* 505 */     if (value == null) {
/* 506 */       return addNull();
/*     */     }
/* 508 */     return _add(numberNode(value.doubleValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(BigDecimal v)
/*     */   {
/* 517 */     if (v == null) {
/* 518 */       return addNull();
/*     */     }
/* 520 */     return _add(numberNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(String v)
/*     */   {
/* 529 */     if (v == null) {
/* 530 */       return addNull();
/*     */     }
/* 532 */     return _add(textNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(boolean v)
/*     */   {
/* 541 */     return _add(booleanNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(Boolean value)
/*     */   {
/* 551 */     if (value == null) {
/* 552 */       return addNull();
/*     */     }
/* 554 */     return _add(booleanNode(value.booleanValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(byte[] v)
/*     */   {
/* 564 */     if (v == null) {
/* 565 */       return addNull();
/*     */     }
/* 567 */     return _add(binaryNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insertArray(int index)
/*     */   {
/* 578 */     ArrayNode n = arrayNode();
/* 579 */     _insert(index, n);
/* 580 */     return n;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode insertObject(int index)
/*     */   {
/* 592 */     ObjectNode n = objectNode();
/* 593 */     _insert(index, n);
/* 594 */     return n;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insertPOJO(int index, Object value)
/*     */   {
/* 605 */     if (value == null) {
/* 606 */       return insertNull(index);
/*     */     }
/* 608 */     return _insert(index, pojoNode(value));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insertNull(int index)
/*     */   {
/* 619 */     _insert(index, nullNode());
/* 620 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insert(int index, int v)
/*     */   {
/* 630 */     _insert(index, numberNode(v));
/* 631 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insert(int index, Integer value)
/*     */   {
/* 641 */     if (value == null) {
/* 642 */       insertNull(index);
/*     */     } else {
/* 644 */       _insert(index, numberNode(value.intValue()));
/*     */     }
/* 646 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insert(int index, long v)
/*     */   {
/* 656 */     return _insert(index, numberNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insert(int index, Long value)
/*     */   {
/* 666 */     if (value == null) {
/* 667 */       return insertNull(index);
/*     */     }
/* 669 */     return _insert(index, numberNode(value.longValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insert(int index, float v)
/*     */   {
/* 679 */     return _insert(index, numberNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insert(int index, Float value)
/*     */   {
/* 689 */     if (value == null) {
/* 690 */       return insertNull(index);
/*     */     }
/* 692 */     return _insert(index, numberNode(value.floatValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insert(int index, double v)
/*     */   {
/* 702 */     return _insert(index, numberNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insert(int index, Double value)
/*     */   {
/* 712 */     if (value == null) {
/* 713 */       return insertNull(index);
/*     */     }
/* 715 */     return _insert(index, numberNode(value.doubleValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insert(int index, BigDecimal v)
/*     */   {
/* 725 */     if (v == null) {
/* 726 */       return insertNull(index);
/*     */     }
/* 728 */     return _insert(index, numberNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insert(int index, String v)
/*     */   {
/* 738 */     if (v == null) {
/* 739 */       return insertNull(index);
/*     */     }
/* 741 */     return _insert(index, textNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insert(int index, boolean v)
/*     */   {
/* 751 */     return _insert(index, booleanNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insert(int index, Boolean value)
/*     */   {
/* 761 */     if (value == null) {
/* 762 */       return insertNull(index);
/*     */     }
/* 764 */     return _insert(index, booleanNode(value.booleanValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insert(int index, byte[] v)
/*     */   {
/* 775 */     if (v == null) {
/* 776 */       return insertNull(index);
/*     */     }
/* 778 */     return _insert(index, binaryNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 790 */     if (o == this) return true;
/* 791 */     if (o == null) return false;
/* 792 */     if ((o instanceof ArrayNode)) {
/* 793 */       return this._children.equals(((ArrayNode)o)._children);
/*     */     }
/* 795 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean _childrenEqual(ArrayNode other)
/*     */   {
/* 802 */     return this._children.equals(other._children);
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 807 */     return this._children.hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 813 */     StringBuilder sb = new StringBuilder(16 + (size() << 4));
/* 814 */     sb.append('[');
/* 815 */     int i = 0; for (int len = this._children.size(); i < len; i++) {
/* 816 */       if (i > 0) {
/* 817 */         sb.append(',');
/*     */       }
/* 819 */       sb.append(((JsonNode)this._children.get(i)).toString());
/*     */     }
/* 821 */     sb.append(']');
/* 822 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ArrayNode _add(JsonNode node)
/*     */   {
/* 832 */     this._children.add(node);
/* 833 */     return this;
/*     */   }
/*     */   
/*     */   protected ArrayNode _insert(int index, JsonNode node)
/*     */   {
/* 838 */     if (index < 0) {
/* 839 */       this._children.add(0, node);
/* 840 */     } else if (index >= this._children.size()) {
/* 841 */       this._children.add(node);
/*     */     } else {
/* 843 */       this._children.add(index, node);
/*     */     }
/* 845 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\node\ArrayNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */