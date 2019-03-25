/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonPointer;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ObjectNode extends ContainerNode<ObjectNode>
/*     */ {
/*     */   protected final Map<String, JsonNode> _children;
/*     */   
/*     */   public ObjectNode(JsonNodeFactory nc)
/*     */   {
/*  24 */     super(nc);
/*  25 */     this._children = new java.util.LinkedHashMap();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ObjectNode(JsonNodeFactory nc, Map<String, JsonNode> kids)
/*     */   {
/*  32 */     super(nc);
/*  33 */     this._children = kids;
/*     */   }
/*     */   
/*     */   protected JsonNode _at(JsonPointer ptr)
/*     */   {
/*  38 */     return get(ptr.getMatchingProperty());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode deepCopy()
/*     */   {
/*  49 */     ObjectNode ret = new ObjectNode(this._nodeFactory);
/*     */     
/*  51 */     for (Map.Entry<String, JsonNode> entry : this._children.entrySet()) {
/*  52 */       ret._children.put(entry.getKey(), ((JsonNode)entry.getValue()).deepCopy());
/*     */     }
/*  54 */     return ret;
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
/*  65 */     return this._children.isEmpty();
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
/*  76 */     return JsonNodeType.OBJECT;
/*     */   }
/*     */   
/*  79 */   public JsonToken asToken() { return JsonToken.START_OBJECT; }
/*     */   
/*     */   public int size()
/*     */   {
/*  83 */     return this._children.size();
/*     */   }
/*     */   
/*     */   public Iterator<JsonNode> elements()
/*     */   {
/*  88 */     return this._children.values().iterator();
/*     */   }
/*     */   
/*     */   public JsonNode get(int index) {
/*  92 */     return null;
/*     */   }
/*     */   
/*     */   public JsonNode get(String fieldName) {
/*  96 */     return (JsonNode)this._children.get(fieldName);
/*     */   }
/*     */   
/*     */   public Iterator<String> fieldNames()
/*     */   {
/* 101 */     return this._children.keySet().iterator();
/*     */   }
/*     */   
/*     */   public JsonNode path(int index)
/*     */   {
/* 106 */     return MissingNode.getInstance();
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonNode path(String fieldName)
/*     */   {
/* 112 */     JsonNode n = (JsonNode)this._children.get(fieldName);
/* 113 */     if (n != null) {
/* 114 */       return n;
/*     */     }
/* 116 */     return MissingNode.getInstance();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator<Map.Entry<String, JsonNode>> fields()
/*     */   {
/* 125 */     return this._children.entrySet().iterator();
/*     */   }
/*     */   
/*     */   public ObjectNode with(String propertyName)
/*     */   {
/* 130 */     JsonNode n = (JsonNode)this._children.get(propertyName);
/* 131 */     if (n != null) {
/* 132 */       if ((n instanceof ObjectNode)) {
/* 133 */         return (ObjectNode)n;
/*     */       }
/* 135 */       throw new UnsupportedOperationException("Property '" + propertyName + "' has value that is not of type ObjectNode (but " + n.getClass().getName() + ")");
/*     */     }
/*     */     
/*     */ 
/* 139 */     ObjectNode result = objectNode();
/* 140 */     this._children.put(propertyName, result);
/* 141 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public ArrayNode withArray(String propertyName)
/*     */   {
/* 147 */     JsonNode n = (JsonNode)this._children.get(propertyName);
/* 148 */     if (n != null) {
/* 149 */       if ((n instanceof ArrayNode)) {
/* 150 */         return (ArrayNode)n;
/*     */       }
/* 152 */       throw new UnsupportedOperationException("Property '" + propertyName + "' has value that is not of type ArrayNode (but " + n.getClass().getName() + ")");
/*     */     }
/*     */     
/*     */ 
/* 156 */     ArrayNode result = arrayNode();
/* 157 */     this._children.put(propertyName, result);
/* 158 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(java.util.Comparator<JsonNode> comparator, JsonNode o)
/*     */   {
/* 164 */     if (!(o instanceof ObjectNode)) {
/* 165 */       return false;
/*     */     }
/* 167 */     ObjectNode other = (ObjectNode)o;
/* 168 */     Map<String, JsonNode> m1 = this._children;
/* 169 */     Map<String, JsonNode> m2 = other._children;
/*     */     
/* 171 */     int len = m1.size();
/* 172 */     if (m2.size() != len) {
/* 173 */       return false;
/*     */     }
/*     */     
/* 176 */     for (Map.Entry<String, JsonNode> entry : m1.entrySet()) {
/* 177 */       JsonNode v2 = (JsonNode)m2.get(entry.getKey());
/* 178 */       if ((v2 == null) || (!((JsonNode)entry.getValue()).equals(comparator, v2))) {
/* 179 */         return false;
/*     */       }
/*     */     }
/* 182 */     return true;
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
/* 194 */     for (Map.Entry<String, JsonNode> entry : this._children.entrySet()) {
/* 195 */       if (fieldName.equals(entry.getKey())) {
/* 196 */         return (JsonNode)entry.getValue();
/*     */       }
/* 198 */       JsonNode value = ((JsonNode)entry.getValue()).findValue(fieldName);
/* 199 */       if (value != null) {
/* 200 */         return value;
/*     */       }
/*     */     }
/* 203 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<JsonNode> findValues(String fieldName, List<JsonNode> foundSoFar)
/*     */   {
/* 209 */     for (Map.Entry<String, JsonNode> entry : this._children.entrySet()) {
/* 210 */       if (fieldName.equals(entry.getKey())) {
/* 211 */         if (foundSoFar == null) {
/* 212 */           foundSoFar = new ArrayList();
/*     */         }
/* 214 */         foundSoFar.add(entry.getValue());
/*     */       } else {
/* 216 */         foundSoFar = ((JsonNode)entry.getValue()).findValues(fieldName, foundSoFar);
/*     */       }
/*     */     }
/* 219 */     return foundSoFar;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<String> findValuesAsText(String fieldName, List<String> foundSoFar)
/*     */   {
/* 225 */     for (Map.Entry<String, JsonNode> entry : this._children.entrySet()) {
/* 226 */       if (fieldName.equals(entry.getKey())) {
/* 227 */         if (foundSoFar == null) {
/* 228 */           foundSoFar = new ArrayList();
/*     */         }
/* 230 */         foundSoFar.add(((JsonNode)entry.getValue()).asText());
/*     */       } else {
/* 232 */         foundSoFar = ((JsonNode)entry.getValue()).findValuesAsText(fieldName, foundSoFar);
/*     */       }
/*     */     }
/*     */     
/* 236 */     return foundSoFar;
/*     */   }
/*     */   
/*     */ 
/*     */   public ObjectNode findParent(String fieldName)
/*     */   {
/* 242 */     for (Map.Entry<String, JsonNode> entry : this._children.entrySet()) {
/* 243 */       if (fieldName.equals(entry.getKey())) {
/* 244 */         return this;
/*     */       }
/* 246 */       JsonNode value = ((JsonNode)entry.getValue()).findParent(fieldName);
/* 247 */       if (value != null) {
/* 248 */         return (ObjectNode)value;
/*     */       }
/*     */     }
/* 251 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<JsonNode> findParents(String fieldName, List<JsonNode> foundSoFar)
/*     */   {
/* 257 */     for (Map.Entry<String, JsonNode> entry : this._children.entrySet()) {
/* 258 */       if (fieldName.equals(entry.getKey())) {
/* 259 */         if (foundSoFar == null) {
/* 260 */           foundSoFar = new ArrayList();
/*     */         }
/* 262 */         foundSoFar.add(this);
/*     */       } else {
/* 264 */         foundSoFar = ((JsonNode)entry.getValue()).findParents(fieldName, foundSoFar);
/*     */       }
/*     */     }
/*     */     
/* 268 */     return foundSoFar;
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
/*     */   public void serialize(JsonGenerator g, SerializerProvider provider)
/*     */     throws java.io.IOException
/*     */   {
/* 286 */     boolean trimEmptyArray = (provider != null) && (!provider.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS));
/*     */     
/* 288 */     g.writeStartObject(this);
/* 289 */     for (Map.Entry<String, JsonNode> en : this._children.entrySet())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 295 */       BaseJsonNode value = (BaseJsonNode)en.getValue();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 300 */       if ((!trimEmptyArray) || (!value.isArray()) || (!value.isEmpty(provider)))
/*     */       {
/*     */ 
/* 303 */         g.writeFieldName((String)en.getKey());
/* 304 */         value.serialize(g, provider);
/*     */       } }
/* 306 */     g.writeEndObject();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void serializeWithType(JsonGenerator g, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws java.io.IOException
/*     */   {
/* 315 */     boolean trimEmptyArray = (provider != null) && (!provider.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS));
/*     */     
/* 317 */     typeSer.writeTypePrefixForObject(this, g);
/* 318 */     for (Map.Entry<String, JsonNode> en : this._children.entrySet()) {
/* 319 */       BaseJsonNode value = (BaseJsonNode)en.getValue();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 324 */       if ((!trimEmptyArray) || (!value.isArray()) || (!value.isEmpty(provider)))
/*     */       {
/*     */ 
/*     */ 
/* 328 */         g.writeFieldName((String)en.getKey());
/* 329 */         value.serialize(g, provider);
/*     */       } }
/* 331 */     typeSer.writeTypeSuffixForObject(this, g);
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
/*     */   public JsonNode set(String fieldName, JsonNode value)
/*     */   {
/* 358 */     if (value == null) {
/* 359 */       value = nullNode();
/*     */     }
/* 361 */     this._children.put(fieldName, value);
/* 362 */     return this;
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
/*     */   public JsonNode setAll(Map<String, ? extends JsonNode> properties)
/*     */   {
/* 377 */     for (Map.Entry<String, ? extends JsonNode> en : properties.entrySet()) {
/* 378 */       JsonNode n = (JsonNode)en.getValue();
/* 379 */       if (n == null) {
/* 380 */         n = nullNode();
/*     */       }
/* 382 */       this._children.put(en.getKey(), n);
/*     */     }
/* 384 */     return this;
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
/*     */   public JsonNode setAll(ObjectNode other)
/*     */   {
/* 399 */     this._children.putAll(other._children);
/* 400 */     return this;
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
/*     */   public JsonNode replace(String fieldName, JsonNode value)
/*     */   {
/* 417 */     if (value == null) {
/* 418 */       value = nullNode();
/*     */     }
/* 420 */     return (JsonNode)this._children.put(fieldName, value);
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
/*     */   public JsonNode without(String fieldName)
/*     */   {
/* 433 */     this._children.remove(fieldName);
/* 434 */     return this;
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
/*     */   public ObjectNode without(Collection<String> fieldNames)
/*     */   {
/* 449 */     this._children.keySet().removeAll(fieldNames);
/* 450 */     return this;
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
/*     */   @Deprecated
/*     */   public JsonNode put(String fieldName, JsonNode value)
/*     */   {
/* 474 */     if (value == null) {
/* 475 */       value = nullNode();
/*     */     }
/* 477 */     return (JsonNode)this._children.put(fieldName, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNode remove(String fieldName)
/*     */   {
/* 488 */     return (JsonNode)this._children.remove(fieldName);
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
/*     */   public ObjectNode remove(Collection<String> fieldNames)
/*     */   {
/* 501 */     this._children.keySet().removeAll(fieldNames);
/* 502 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode removeAll()
/*     */   {
/* 514 */     this._children.clear();
/* 515 */     return this;
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
/*     */   @Deprecated
/*     */   public JsonNode putAll(Map<String, ? extends JsonNode> properties)
/*     */   {
/* 530 */     return setAll(properties);
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
/*     */   @Deprecated
/*     */   public JsonNode putAll(ObjectNode other)
/*     */   {
/* 545 */     return setAll(other);
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
/*     */   public ObjectNode retain(Collection<String> fieldNames)
/*     */   {
/* 558 */     this._children.keySet().retainAll(fieldNames);
/* 559 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode retain(String... fieldNames)
/*     */   {
/* 571 */     return retain(java.util.Arrays.asList(fieldNames));
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
/*     */   public ArrayNode putArray(String fieldName)
/*     */   {
/* 593 */     ArrayNode n = arrayNode();
/* 594 */     _put(fieldName, n);
/* 595 */     return n;
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
/*     */   public ObjectNode putObject(String fieldName)
/*     */   {
/* 611 */     ObjectNode n = objectNode();
/* 612 */     _put(fieldName, n);
/* 613 */     return n;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ObjectNode putPOJO(String fieldName, Object pojo)
/*     */   {
/* 620 */     return _put(fieldName, pojoNode(pojo));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ObjectNode putRawValue(String fieldName, com.fasterxml.jackson.databind.util.RawValue raw)
/*     */   {
/* 627 */     return _put(fieldName, rawValueNode(raw));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode putNull(String fieldName)
/*     */   {
/* 635 */     this._children.put(fieldName, nullNode());
/* 636 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode put(String fieldName, short v)
/*     */   {
/* 645 */     return _put(fieldName, numberNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode put(String fieldName, Short v)
/*     */   {
/* 655 */     return _put(fieldName, v == null ? nullNode() : numberNode(v.shortValue()));
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
/*     */   public ObjectNode put(String fieldName, int v)
/*     */   {
/* 669 */     return _put(fieldName, numberNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode put(String fieldName, Integer v)
/*     */   {
/* 679 */     return _put(fieldName, v == null ? nullNode() : numberNode(v.intValue()));
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
/*     */   public ObjectNode put(String fieldName, long v)
/*     */   {
/* 693 */     return _put(fieldName, numberNode(v));
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
/*     */   public ObjectNode put(String fieldName, Long v)
/*     */   {
/* 709 */     return _put(fieldName, v == null ? nullNode() : numberNode(v.longValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode put(String fieldName, float v)
/*     */   {
/* 719 */     return _put(fieldName, numberNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode put(String fieldName, Float v)
/*     */   {
/* 729 */     return _put(fieldName, v == null ? nullNode() : numberNode(v.floatValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode put(String fieldName, double v)
/*     */   {
/* 739 */     return _put(fieldName, numberNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode put(String fieldName, Double v)
/*     */   {
/* 749 */     return _put(fieldName, v == null ? nullNode() : numberNode(v.doubleValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode put(String fieldName, java.math.BigDecimal v)
/*     */   {
/* 759 */     return _put(fieldName, v == null ? nullNode() : numberNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode put(String fieldName, String v)
/*     */   {
/* 769 */     return _put(fieldName, v == null ? nullNode() : textNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode put(String fieldName, boolean v)
/*     */   {
/* 779 */     return _put(fieldName, booleanNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode put(String fieldName, Boolean v)
/*     */   {
/* 789 */     return _put(fieldName, v == null ? nullNode() : booleanNode(v.booleanValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode put(String fieldName, byte[] v)
/*     */   {
/* 799 */     return _put(fieldName, v == null ? nullNode() : binaryNode(v));
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
/*     */   public boolean equals(Object o)
/*     */   {
/* 812 */     if (o == this) return true;
/* 813 */     if (o == null) return false;
/* 814 */     if ((o instanceof ObjectNode)) {
/* 815 */       return _childrenEqual((ObjectNode)o);
/*     */     }
/* 817 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean _childrenEqual(ObjectNode other)
/*     */   {
/* 825 */     return this._children.equals(other._children);
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 831 */     return this._children.hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 837 */     StringBuilder sb = new StringBuilder(32 + (size() << 4));
/* 838 */     sb.append("{");
/* 839 */     int count = 0;
/* 840 */     for (Map.Entry<String, JsonNode> en : this._children.entrySet()) {
/* 841 */       if (count > 0) {
/* 842 */         sb.append(",");
/*     */       }
/* 844 */       count++;
/* 845 */       TextNode.appendQuoted(sb, (String)en.getKey());
/* 846 */       sb.append(':');
/* 847 */       sb.append(((JsonNode)en.getValue()).toString());
/*     */     }
/* 849 */     sb.append("}");
/* 850 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ObjectNode _put(String fieldName, JsonNode value)
/*     */   {
/* 861 */     this._children.put(fieldName, value);
/* 862 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\node\ObjectNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */