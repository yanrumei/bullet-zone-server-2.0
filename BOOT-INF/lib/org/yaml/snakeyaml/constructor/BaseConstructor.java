/*     */ package org.yaml.snakeyaml.constructor;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.yaml.snakeyaml.composer.Composer;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.introspector.PropertyUtils;
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
/*     */ 
/*     */ 
/*     */ public abstract class BaseConstructor
/*     */ {
/*  47 */   protected final Map<NodeId, Construct> yamlClassConstructors = new EnumMap(NodeId.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  56 */   protected final Map<Tag, Construct> yamlConstructors = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  61 */   protected final Map<String, Construct> yamlMultiConstructors = new HashMap();
/*     */   
/*     */   protected Composer composer;
/*     */   private final Map<Node, Object> constructedObjects;
/*     */   private final Set<Node> recursiveObjects;
/*     */   private final ArrayList<RecursiveTuple<Map<Object, Object>, RecursiveTuple<Object, Object>>> maps2fill;
/*     */   private final ArrayList<RecursiveTuple<Set<Object>, Object>> sets2fill;
/*     */   protected Tag rootTag;
/*     */   private PropertyUtils propertyUtils;
/*     */   private boolean explicitPropertyUtils;
/*     */   
/*     */   public BaseConstructor()
/*     */   {
/*  74 */     this.constructedObjects = new HashMap();
/*  75 */     this.recursiveObjects = new HashSet();
/*  76 */     this.maps2fill = new ArrayList();
/*  77 */     this.sets2fill = new ArrayList();
/*  78 */     this.rootTag = null;
/*  79 */     this.explicitPropertyUtils = false;
/*     */   }
/*     */   
/*     */   public void setComposer(Composer composer) {
/*  83 */     this.composer = composer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean checkData()
/*     */   {
/*  93 */     return this.composer.checkNode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getData()
/*     */   {
/* 103 */     this.composer.checkNode();
/* 104 */     Node node = this.composer.getNode();
/* 105 */     if (this.rootTag != null) {
/* 106 */       node.setTag(this.rootTag);
/*     */     }
/* 108 */     return constructDocument(node);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getSingleData(Class<?> type)
/*     */   {
/* 120 */     Node node = this.composer.getSingleNode();
/* 121 */     if (node != null) {
/* 122 */       if (Object.class != type) {
/* 123 */         node.setTag(new Tag(type));
/* 124 */       } else if (this.rootTag != null) {
/* 125 */         node.setTag(this.rootTag);
/*     */       }
/* 127 */       return constructDocument(node);
/*     */     }
/* 129 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Object constructDocument(Node node)
/*     */   {
/* 141 */     Object data = constructObject(node);
/* 142 */     fillRecursive();
/* 143 */     this.constructedObjects.clear();
/* 144 */     this.recursiveObjects.clear();
/* 145 */     return data;
/*     */   }
/*     */   
/*     */   private void fillRecursive() {
/* 149 */     if (!this.maps2fill.isEmpty()) {
/* 150 */       for (RecursiveTuple<Map<Object, Object>, RecursiveTuple<Object, Object>> entry : this.maps2fill) {
/* 151 */         RecursiveTuple<Object, Object> key_value = (RecursiveTuple)entry._2();
/* 152 */         ((Map)entry._1()).put(key_value._1(), key_value._2());
/*     */       }
/* 154 */       this.maps2fill.clear();
/*     */     }
/* 156 */     if (!this.sets2fill.isEmpty()) {
/* 157 */       for (RecursiveTuple<Set<Object>, Object> value : this.sets2fill) {
/* 158 */         ((Set)value._1()).add(value._2());
/*     */       }
/* 160 */       this.sets2fill.clear();
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
/*     */   protected Object constructObject(Node node)
/*     */   {
/* 173 */     if (this.constructedObjects.containsKey(node)) {
/* 174 */       return this.constructedObjects.get(node);
/*     */     }
/* 176 */     if (this.recursiveObjects.contains(node)) {
/* 177 */       throw new ConstructorException(null, null, "found unconstructable recursive node", node.getStartMark());
/*     */     }
/*     */     
/* 180 */     this.recursiveObjects.add(node);
/* 181 */     Construct constructor = getConstructor(node);
/* 182 */     Object data = constructor.construct(node);
/* 183 */     this.constructedObjects.put(node, data);
/* 184 */     this.recursiveObjects.remove(node);
/* 185 */     if (node.isTwoStepsConstruction()) {
/* 186 */       constructor.construct2ndStep(node, data);
/*     */     }
/* 188 */     return data;
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
/*     */   protected Construct getConstructor(Node node)
/*     */   {
/* 201 */     if (node.useClassConstructor()) {
/* 202 */       return (Construct)this.yamlClassConstructors.get(node.getNodeId());
/*     */     }
/* 204 */     Construct constructor = (Construct)this.yamlConstructors.get(node.getTag());
/* 205 */     if (constructor == null) {
/* 206 */       for (String prefix : this.yamlMultiConstructors.keySet()) {
/* 207 */         if (node.getTag().startsWith(prefix)) {
/* 208 */           return (Construct)this.yamlMultiConstructors.get(prefix);
/*     */         }
/*     */       }
/* 211 */       return (Construct)this.yamlConstructors.get(null);
/*     */     }
/* 213 */     return constructor;
/*     */   }
/*     */   
/*     */   protected Object constructScalar(ScalarNode node)
/*     */   {
/* 218 */     return node.getValue();
/*     */   }
/*     */   
/*     */   protected List<Object> createDefaultList(int initSize) {
/* 222 */     return new ArrayList(initSize);
/*     */   }
/*     */   
/*     */   protected Set<Object> createDefaultSet(int initSize) {
/* 226 */     return new LinkedHashSet(initSize);
/*     */   }
/*     */   
/*     */   protected Object createArray(Class<?> type, int size) {
/* 230 */     return Array.newInstance(type.getComponentType(), size);
/*     */   }
/*     */   
/*     */   protected List<? extends Object> constructSequence(SequenceNode node)
/*     */   {
/*     */     List<Object> result;
/* 236 */     if ((List.class.isAssignableFrom(node.getType())) && (!node.getType().isInterface())) {
/*     */       try
/*     */       {
/* 239 */         result = (List)node.getType().newInstance();
/*     */       } catch (Exception e) {
/* 241 */         throw new YAMLException(e);
/*     */       }
/*     */     } else {
/* 244 */       result = createDefaultList(node.getValue().size());
/*     */     }
/* 246 */     constructSequenceStep2(node, result);
/* 247 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   protected Set<? extends Object> constructSet(SequenceNode node)
/*     */   {
/*     */     Set<Object> result;
/* 254 */     if (!node.getType().isInterface()) {
/*     */       try
/*     */       {
/* 257 */         result = (Set)node.getType().newInstance();
/*     */       } catch (Exception e) {
/* 259 */         throw new YAMLException(e);
/*     */       }
/*     */     } else {
/* 262 */       result = createDefaultSet(node.getValue().size());
/*     */     }
/* 264 */     constructSequenceStep2(node, result);
/* 265 */     return result;
/*     */   }
/*     */   
/*     */   protected Object constructArray(SequenceNode node)
/*     */   {
/* 270 */     return constructArrayStep2(node, createArray(node.getType(), node.getValue().size()));
/*     */   }
/*     */   
/*     */   protected void constructSequenceStep2(SequenceNode node, Collection<Object> collection) {
/* 274 */     for (Node child : node.getValue()) {
/* 275 */       collection.add(constructObject(child));
/*     */     }
/*     */   }
/*     */   
/*     */   protected Object constructArrayStep2(SequenceNode node, Object array) {
/* 280 */     Class<?> componentType = node.getType().getComponentType();
/*     */     
/* 282 */     int index = 0;
/* 283 */     for (Node child : node.getValue())
/*     */     {
/* 285 */       if (child.getType() == Object.class) {
/* 286 */         child.setType(componentType);
/*     */       }
/*     */       
/* 289 */       Object value = constructObject(child);
/*     */       
/* 291 */       if (componentType.isPrimitive())
/*     */       {
/* 293 */         if (value == null) {
/* 294 */           throw new NullPointerException("Unable to construct element value for " + child);
/*     */         }
/*     */         
/*     */ 
/* 298 */         if (Byte.TYPE.equals(componentType)) {
/* 299 */           Array.setByte(array, index, ((Number)value).byteValue());
/*     */         }
/* 301 */         else if (Short.TYPE.equals(componentType)) {
/* 302 */           Array.setShort(array, index, ((Number)value).shortValue());
/*     */         }
/* 304 */         else if (Integer.TYPE.equals(componentType)) {
/* 305 */           Array.setInt(array, index, ((Number)value).intValue());
/*     */         }
/* 307 */         else if (Long.TYPE.equals(componentType)) {
/* 308 */           Array.setLong(array, index, ((Number)value).longValue());
/*     */         }
/* 310 */         else if (Float.TYPE.equals(componentType)) {
/* 311 */           Array.setFloat(array, index, ((Number)value).floatValue());
/*     */         }
/* 313 */         else if (Double.TYPE.equals(componentType)) {
/* 314 */           Array.setDouble(array, index, ((Number)value).doubleValue());
/*     */         }
/* 316 */         else if (Character.TYPE.equals(componentType)) {
/* 317 */           Array.setChar(array, index, ((Character)value).charValue());
/*     */         }
/* 319 */         else if (Boolean.TYPE.equals(componentType)) {
/* 320 */           Array.setBoolean(array, index, ((Boolean)value).booleanValue());
/*     */         }
/*     */         else {
/* 323 */           throw new YAMLException("unexpected primitive type");
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 328 */         Array.set(array, index, value);
/*     */       }
/*     */       
/* 331 */       index++;
/*     */     }
/* 333 */     return array;
/*     */   }
/*     */   
/*     */   protected Map<Object, Object> createDefaultMap()
/*     */   {
/* 338 */     return new LinkedHashMap();
/*     */   }
/*     */   
/*     */   protected Set<Object> createDefaultSet()
/*     */   {
/* 343 */     return new LinkedHashSet();
/*     */   }
/*     */   
/*     */   protected Set<Object> constructSet(MappingNode node) {
/* 347 */     Set<Object> set = createDefaultSet();
/* 348 */     constructSet2ndStep(node, set);
/* 349 */     return set;
/*     */   }
/*     */   
/*     */   protected Map<Object, Object> constructMapping(MappingNode node) {
/* 353 */     Map<Object, Object> mapping = createDefaultMap();
/* 354 */     constructMapping2ndStep(node, mapping);
/* 355 */     return mapping;
/*     */   }
/*     */   
/*     */   protected void constructMapping2ndStep(MappingNode node, Map<Object, Object> mapping) {
/* 359 */     List<NodeTuple> nodeValue = node.getValue();
/* 360 */     for (NodeTuple tuple : nodeValue) {
/* 361 */       Node keyNode = tuple.getKeyNode();
/* 362 */       Node valueNode = tuple.getValueNode();
/* 363 */       Object key = constructObject(keyNode);
/* 364 */       if (key != null) {
/*     */         try {
/* 366 */           key.hashCode();
/*     */         } catch (Exception e) {
/* 368 */           throw new ConstructorException("while constructing a mapping", node.getStartMark(), "found unacceptable key " + key, tuple.getKeyNode().getStartMark(), e);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 373 */       Object value = constructObject(valueNode);
/* 374 */       if (keyNode.isTwoStepsConstruction())
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 381 */         this.maps2fill.add(0, new RecursiveTuple(mapping, new RecursiveTuple(key, value)));
/*     */       }
/*     */       else
/*     */       {
/* 385 */         mapping.put(key, value);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void constructSet2ndStep(MappingNode node, Set<Object> set) {
/* 391 */     List<NodeTuple> nodeValue = node.getValue();
/* 392 */     for (NodeTuple tuple : nodeValue) {
/* 393 */       Node keyNode = tuple.getKeyNode();
/* 394 */       Object key = constructObject(keyNode);
/* 395 */       if (key != null) {
/*     */         try {
/* 397 */           key.hashCode();
/*     */         } catch (Exception e) {
/* 399 */           throw new ConstructorException("while constructing a Set", node.getStartMark(), "found unacceptable key " + key, tuple.getKeyNode().getStartMark(), e);
/*     */         }
/*     */       }
/*     */       
/* 403 */       if (keyNode.isTwoStepsConstruction())
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 410 */         this.sets2fill.add(0, new RecursiveTuple(set, key));
/*     */       } else {
/* 412 */         set.add(key);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setPropertyUtils(PropertyUtils propertyUtils) {
/* 418 */     this.propertyUtils = propertyUtils;
/* 419 */     this.explicitPropertyUtils = true;
/*     */   }
/*     */   
/*     */   public final PropertyUtils getPropertyUtils() {
/* 423 */     if (this.propertyUtils == null) {
/* 424 */       this.propertyUtils = new PropertyUtils();
/*     */     }
/* 426 */     return this.propertyUtils;
/*     */   }
/*     */   
/*     */   private static class RecursiveTuple<T, K> {
/*     */     private final T _1;
/*     */     private final K _2;
/*     */     
/*     */     public RecursiveTuple(T _1, K _2) {
/* 434 */       this._1 = _1;
/* 435 */       this._2 = _2;
/*     */     }
/*     */     
/*     */     public K _2() {
/* 439 */       return (K)this._2;
/*     */     }
/*     */     
/*     */     public T _1() {
/* 443 */       return (T)this._1;
/*     */     }
/*     */   }
/*     */   
/*     */   public final boolean isExplicitPropertyUtils() {
/* 448 */     return this.explicitPropertyUtils;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\constructor\BaseConstructor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */