/*     */ package org.yaml.snakeyaml.constructor;
/*     */ 
/*     */ import java.beans.IntrospectionException;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import java.util.UUID;
/*     */ import org.yaml.snakeyaml.TypeDescription;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.introspector.Property;
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
/*     */ public class Constructor
/*     */   extends SafeConstructor
/*     */ {
/*     */   private final Map<Tag, Class<? extends Object>> typeTags;
/*     */   protected final Map<Class<? extends Object>, TypeDescription> typeDefinitions;
/*     */   
/*     */   public Constructor()
/*     */   {
/*  55 */     this(Object.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Constructor(Class<? extends Object> theRoot)
/*     */   {
/*  65 */     this(new TypeDescription(checkRoot(theRoot)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static Class<? extends Object> checkRoot(Class<? extends Object> theRoot)
/*     */   {
/*  72 */     if (theRoot == null) {
/*  73 */       throw new NullPointerException("Root class must be provided.");
/*     */     }
/*  75 */     return theRoot;
/*     */   }
/*     */   
/*     */   public Constructor(TypeDescription theRoot) {
/*  79 */     if (theRoot == null) {
/*  80 */       throw new NullPointerException("Root type must be provided.");
/*     */     }
/*  82 */     this.yamlConstructors.put(null, new ConstructYamlObject());
/*  83 */     if (!Object.class.equals(theRoot.getType())) {
/*  84 */       this.rootTag = new Tag(theRoot.getType());
/*     */     }
/*  86 */     this.typeTags = new HashMap();
/*  87 */     this.typeDefinitions = new HashMap();
/*  88 */     this.yamlClassConstructors.put(NodeId.scalar, new ConstructScalar());
/*  89 */     this.yamlClassConstructors.put(NodeId.mapping, new ConstructMapping());
/*  90 */     this.yamlClassConstructors.put(NodeId.sequence, new ConstructSequence());
/*  91 */     addTypeDescription(theRoot);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Constructor(String theRoot)
/*     */     throws ClassNotFoundException
/*     */   {
/* 104 */     this(Class.forName(check(theRoot)));
/*     */   }
/*     */   
/*     */   private static final String check(String s) {
/* 108 */     if (s == null) {
/* 109 */       throw new NullPointerException("Root type must be provided.");
/*     */     }
/* 111 */     if (s.trim().length() == 0) {
/* 112 */       throw new YAMLException("Root type must be provided.");
/*     */     }
/* 114 */     return s;
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
/*     */   public TypeDescription addTypeDescription(TypeDescription definition)
/*     */   {
/* 128 */     if (definition == null) {
/* 129 */       throw new NullPointerException("TypeDescription is required.");
/*     */     }
/* 131 */     Tag tag = definition.getTag();
/* 132 */     this.typeTags.put(tag, definition.getType());
/* 133 */     return (TypeDescription)this.typeDefinitions.put(definition.getType(), definition);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected class ConstructMapping
/*     */     implements Construct
/*     */   {
/*     */     protected ConstructMapping() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Object construct(Node node)
/*     */     {
/* 152 */       MappingNode mnode = (MappingNode)node;
/* 153 */       if (Properties.class.isAssignableFrom(node.getType())) {
/* 154 */         Properties properties = new Properties();
/* 155 */         if (!node.isTwoStepsConstruction()) {
/* 156 */           Constructor.this.constructMapping2ndStep(mnode, properties);
/*     */         } else {
/* 158 */           throw new YAMLException("Properties must not be recursive.");
/*     */         }
/* 160 */         return properties; }
/* 161 */       if (SortedMap.class.isAssignableFrom(node.getType())) {
/* 162 */         SortedMap<Object, Object> map = new TreeMap();
/* 163 */         if (!node.isTwoStepsConstruction()) {
/* 164 */           Constructor.this.constructMapping2ndStep(mnode, map);
/*     */         }
/* 166 */         return map; }
/* 167 */       if (Map.class.isAssignableFrom(node.getType())) {
/* 168 */         if (node.isTwoStepsConstruction()) {
/* 169 */           return Constructor.this.createDefaultMap();
/*     */         }
/* 171 */         return Constructor.this.constructMapping(mnode);
/*     */       }
/* 173 */       if (SortedSet.class.isAssignableFrom(node.getType())) {
/* 174 */         SortedSet<Object> set = new TreeSet();
/*     */         
/*     */ 
/* 177 */         Constructor.this.constructSet2ndStep(mnode, set);
/*     */         
/* 179 */         return set; }
/* 180 */       if (Collection.class.isAssignableFrom(node.getType())) {
/* 181 */         if (node.isTwoStepsConstruction()) {
/* 182 */           return Constructor.this.createDefaultSet();
/*     */         }
/* 184 */         return Constructor.this.constructSet(mnode);
/*     */       }
/*     */       
/* 187 */       if (node.isTwoStepsConstruction()) {
/* 188 */         return createEmptyJavaBean(mnode);
/*     */       }
/* 190 */       return constructJavaBean2ndStep(mnode, createEmptyJavaBean(mnode));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void construct2ndStep(Node node, Object object)
/*     */     {
/* 197 */       if (Map.class.isAssignableFrom(node.getType())) {
/* 198 */         Constructor.this.constructMapping2ndStep((MappingNode)node, (Map)object);
/* 199 */       } else if (Set.class.isAssignableFrom(node.getType())) {
/* 200 */         Constructor.this.constructSet2ndStep((MappingNode)node, (Set)object);
/*     */       } else {
/* 202 */         constructJavaBean2ndStep((MappingNode)node, object);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected Object createEmptyJavaBean(MappingNode node)
/*     */     {
/*     */       try
/*     */       {
/* 216 */         java.lang.reflect.Constructor<?> c = node.getType().getDeclaredConstructor(new Class[0]);
/* 217 */         c.setAccessible(true);
/* 218 */         return c.newInstance(new Object[0]);
/*     */       } catch (Exception e) {
/* 220 */         throw new YAMLException(e);
/*     */       }
/*     */     }
/*     */     
/*     */     protected Object constructJavaBean2ndStep(MappingNode node, Object object) {
/* 225 */       Constructor.this.flattenMapping(node);
/* 226 */       Class<? extends Object> beanType = node.getType();
/* 227 */       List<NodeTuple> nodeValue = node.getValue();
/* 228 */       for (NodeTuple tuple : nodeValue) {
/*     */         ScalarNode keyNode;
/* 230 */         if ((tuple.getKeyNode() instanceof ScalarNode))
/*     */         {
/* 232 */           keyNode = (ScalarNode)tuple.getKeyNode();
/*     */         } else
/* 234 */           throw new YAMLException("Keys must be scalars but found: " + tuple.getKeyNode());
/*     */         ScalarNode keyNode;
/* 236 */         Node valueNode = tuple.getValueNode();
/*     */         
/* 238 */         keyNode.setType(String.class);
/* 239 */         String key = (String)Constructor.this.constructObject(keyNode);
/*     */         try {
/* 241 */           Property property = getProperty(beanType, key);
/* 242 */           valueNode.setType(property.getType());
/* 243 */           TypeDescription memberDescription = (TypeDescription)Constructor.this.typeDefinitions.get(beanType);
/* 244 */           boolean typeDetected = false;
/* 245 */           if (memberDescription != null) {
/* 246 */             switch (Constructor.1.$SwitchMap$org$yaml$snakeyaml$nodes$NodeId[valueNode.getNodeId().ordinal()]) {
/*     */             case 1: 
/* 248 */               SequenceNode snode = (SequenceNode)valueNode;
/* 249 */               Class<? extends Object> memberType = memberDescription.getListPropertyType(key);
/*     */               
/* 251 */               if (memberType != null) {
/* 252 */                 snode.setListType(memberType);
/* 253 */                 typeDetected = true;
/* 254 */               } else if (property.getType().isArray()) {
/* 255 */                 snode.setListType(property.getType().getComponentType());
/* 256 */                 typeDetected = true;
/*     */               }
/*     */               break;
/*     */             case 2: 
/* 260 */               MappingNode mnode = (MappingNode)valueNode;
/* 261 */               Class<? extends Object> keyType = memberDescription.getMapKeyType(key);
/* 262 */               if (keyType != null) {
/* 263 */                 mnode.setTypes(keyType, memberDescription.getMapValueType(key));
/* 264 */                 typeDetected = true;
/*     */               }
/*     */               break;
/*     */             }
/*     */             
/*     */           }
/* 270 */           if ((!typeDetected) && (valueNode.getNodeId() != NodeId.scalar))
/*     */           {
/* 272 */             Class<?>[] arguments = property.getActualTypeArguments();
/* 273 */             if ((arguments != null) && (arguments.length > 0))
/*     */             {
/*     */ 
/* 276 */               if (valueNode.getNodeId() == NodeId.sequence) {
/* 277 */                 Class<?> t = arguments[0];
/* 278 */                 SequenceNode snode = (SequenceNode)valueNode;
/* 279 */                 snode.setListType(t);
/* 280 */               } else if (valueNode.getTag().equals(Tag.SET)) {
/* 281 */                 Class<?> t = arguments[0];
/* 282 */                 MappingNode mnode = (MappingNode)valueNode;
/* 283 */                 mnode.setOnlyKeyType(t);
/* 284 */                 mnode.setUseClassConstructor(Boolean.valueOf(true));
/* 285 */               } else if (property.getType().isAssignableFrom(Map.class)) {
/* 286 */                 Class<?> ketType = arguments[0];
/* 287 */                 Class<?> valueType = arguments[1];
/* 288 */                 MappingNode mnode = (MappingNode)valueNode;
/* 289 */                 mnode.setTypes(ketType, valueType);
/* 290 */                 mnode.setUseClassConstructor(Boolean.valueOf(true));
/*     */               }
/*     */             }
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 298 */           Object value = Constructor.this.constructObject(valueNode);
/*     */           
/*     */ 
/* 301 */           if (((property.getType() == Float.TYPE) || (property.getType() == Float.class)) && 
/* 302 */             ((value instanceof Double))) {
/* 303 */             value = Float.valueOf(((Double)value).floatValue());
/*     */           }
/*     */           
/*     */ 
/* 307 */           if ((property.getType() == String.class) && (Tag.BINARY.equals(valueNode.getTag())) && ((value instanceof byte[]))) {
/* 308 */             value = new String((byte[])value);
/*     */           }
/*     */           
/* 311 */           property.set(object, value);
/*     */         } catch (Exception e) {
/* 313 */           throw new ConstructorException("Cannot create property=" + key + " for JavaBean=" + object, node.getStartMark(), e.getMessage(), valueNode.getStartMark(), e);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 318 */       return object;
/*     */     }
/*     */     
/*     */     protected Property getProperty(Class<? extends Object> type, String name) throws IntrospectionException
/*     */     {
/* 323 */       return Constructor.this.getPropertyUtils().getProperty(type, name);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected class ConstructYamlObject
/*     */     implements Construct
/*     */   {
/*     */     protected ConstructYamlObject() {}
/*     */     
/*     */ 
/*     */     private Construct getConstructor(Node node)
/*     */     {
/* 336 */       Class<?> cl = Constructor.this.getClassForNode(node);
/* 337 */       node.setType(cl);
/*     */       
/* 339 */       Construct constructor = (Construct)Constructor.this.yamlClassConstructors.get(node.getNodeId());
/* 340 */       return constructor;
/*     */     }
/*     */     
/*     */     public Object construct(Node node) {
/* 344 */       Object result = null;
/*     */       try {
/* 346 */         result = getConstructor(node).construct(node);
/*     */       } catch (ConstructorException e) {
/* 348 */         throw e;
/*     */       } catch (Exception e) {
/* 350 */         throw new ConstructorException(null, null, "Can't construct a java object for " + node.getTag() + "; exception=" + e.getMessage(), node.getStartMark(), e);
/*     */       }
/*     */       
/* 353 */       return result;
/*     */     }
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/*     */       try {
/* 358 */         getConstructor(node).construct2ndStep(node, object);
/*     */       } catch (Exception e) {
/* 360 */         throw new ConstructorException(null, null, "Can't construct a second step for a java object for " + node.getTag() + "; exception=" + e.getMessage(), node.getStartMark(), e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected class ConstructScalar
/*     */     extends AbstractConstruct
/*     */   {
/*     */     protected ConstructScalar() {}
/*     */     
/*     */     public Object construct(Node nnode)
/*     */     {
/* 373 */       ScalarNode node = (ScalarNode)nnode;
/* 374 */       Class<?> type = node.getType();
/*     */       Object result;
/* 376 */       Object result; if ((type.isPrimitive()) || (type == String.class) || (Number.class.isAssignableFrom(type)) || (type == Boolean.class) || (Date.class.isAssignableFrom(type)) || (type == Character.class) || (type == BigInteger.class) || (type == BigDecimal.class) || (Enum.class.isAssignableFrom(type)) || (Tag.BINARY.equals(node.getTag())) || (Calendar.class.isAssignableFrom(type)) || (type == UUID.class))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 382 */         result = constructStandardJavaInstance(type, node);
/*     */       }
/*     */       else {
/* 385 */         java.lang.reflect.Constructor<?>[] javaConstructors = type.getDeclaredConstructors();
/*     */         
/* 387 */         int oneArgCount = 0;
/* 388 */         java.lang.reflect.Constructor<?> javaConstructor = null;
/* 389 */         for (java.lang.reflect.Constructor<?> c : javaConstructors) {
/* 390 */           if (c.getParameterTypes().length == 1) {
/* 391 */             oneArgCount++;
/* 392 */             javaConstructor = c;
/*     */           }
/*     */         }
/*     */         
/* 396 */         if (javaConstructor == null)
/* 397 */           throw new YAMLException("No single argument constructor found for " + type);
/* 398 */         Object argument; Object argument; if (oneArgCount == 1) {
/* 399 */           argument = constructStandardJavaInstance(javaConstructor.getParameterTypes()[0], node);
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/*     */ 
/* 408 */           argument = Constructor.this.constructScalar(node);
/*     */           try {
/* 410 */             javaConstructor = type.getDeclaredConstructor(new Class[] { String.class });
/*     */           } catch (Exception e) {
/* 412 */             throw new YAMLException("Can't construct a java object for scalar " + node.getTag() + "; No String constructor found. Exception=" + e.getMessage(), e);
/*     */           }
/*     */         }
/*     */         
/*     */         try
/*     */         {
/* 418 */           javaConstructor.setAccessible(true);
/* 419 */           result = javaConstructor.newInstance(new Object[] { argument });
/*     */         } catch (Exception e) {
/* 421 */           throw new ConstructorException(null, null, "Can't construct a java object for scalar " + node.getTag() + "; exception=" + e.getMessage(), node.getStartMark(), e);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 426 */       return result;
/*     */     }
/*     */     
/*     */ 
/*     */     private Object constructStandardJavaInstance(Class type, ScalarNode node)
/*     */     {
/*     */       Object result;
/* 433 */       if (type == String.class) {
/* 434 */         Construct stringConstructor = (Construct)Constructor.this.yamlConstructors.get(Tag.STR);
/* 435 */         result = stringConstructor.construct(node); } else { Object result;
/* 436 */         if ((type == Boolean.class) || (type == Boolean.TYPE)) {
/* 437 */           Construct boolConstructor = (Construct)Constructor.this.yamlConstructors.get(Tag.BOOL);
/* 438 */           result = boolConstructor.construct(node); } else { Object result;
/* 439 */           if ((type == Character.class) || (type == Character.TYPE)) {
/* 440 */             Construct charConstructor = (Construct)Constructor.this.yamlConstructors.get(Tag.STR);
/* 441 */             String ch = (String)charConstructor.construct(node);
/* 442 */             Object result; if (ch.length() == 0) {
/* 443 */               result = null;
/* 444 */             } else { if (ch.length() != 1) {
/* 445 */                 throw new YAMLException("Invalid node Character: '" + ch + "'; length: " + ch.length());
/*     */               }
/*     */               
/* 448 */               result = Character.valueOf(ch.charAt(0));
/*     */             }
/* 450 */           } else if (Date.class.isAssignableFrom(type)) {
/* 451 */             Construct dateConstructor = (Construct)Constructor.this.yamlConstructors.get(Tag.TIMESTAMP);
/* 452 */             Date date = (Date)dateConstructor.construct(node);
/* 453 */             Object result; if (type == Date.class) {
/* 454 */               result = date;
/*     */             } else {
/*     */               try {
/* 457 */                 java.lang.reflect.Constructor<?> constr = type.getConstructor(new Class[] { Long.TYPE });
/* 458 */                 result = constr.newInstance(new Object[] { Long.valueOf(date.getTime()) });
/*     */               } catch (RuntimeException e) { Object result;
/* 460 */                 throw e;
/*     */               } catch (Exception e) {
/* 462 */                 throw new YAMLException("Cannot construct: '" + type + "'");
/*     */               }
/*     */             }
/* 465 */           } else if ((type == Float.class) || (type == Double.class) || (type == Float.TYPE) || (type == Double.TYPE) || (type == BigDecimal.class)) {
/*     */             Object result;
/* 467 */             if (type == BigDecimal.class) {
/* 468 */               result = new BigDecimal(node.getValue());
/*     */             } else {
/* 470 */               Construct doubleConstructor = (Construct)Constructor.this.yamlConstructors.get(Tag.FLOAT);
/* 471 */               Object result = doubleConstructor.construct(node);
/* 472 */               if ((type == Float.class) || (type == Float.TYPE)) {
/* 473 */                 result = new Float(((Double)result).doubleValue());
/*     */               }
/*     */             }
/* 476 */           } else if ((type == Byte.class) || (type == Short.class) || (type == Integer.class) || (type == Long.class) || (type == BigInteger.class) || (type == Byte.TYPE) || (type == Short.TYPE) || (type == Integer.TYPE) || (type == Long.TYPE))
/*     */           {
/*     */ 
/* 479 */             Construct intConstructor = (Construct)Constructor.this.yamlConstructors.get(Tag.INT);
/* 480 */             Object result = intConstructor.construct(node);
/* 481 */             if ((type == Byte.class) || (type == Byte.TYPE)) {
/* 482 */               result = Byte.valueOf(result.toString());
/* 483 */             } else if ((type == Short.class) || (type == Short.TYPE)) {
/* 484 */               result = Short.valueOf(result.toString());
/* 485 */             } else if ((type == Integer.class) || (type == Integer.TYPE)) {
/* 486 */               result = Integer.valueOf(Integer.parseInt(result.toString()));
/* 487 */             } else if ((type == Long.class) || (type == Long.TYPE)) {
/* 488 */               result = Long.valueOf(result.toString());
/*     */             }
/*     */             else {
/* 491 */               result = new BigInteger(result.toString());
/*     */             }
/* 493 */           } else if (Enum.class.isAssignableFrom(type)) {
/* 494 */             String enumValueName = node.getValue();
/*     */             try {
/* 496 */               result = Enum.valueOf(type, enumValueName);
/*     */             } catch (Exception ex) { Object result;
/* 498 */               throw new YAMLException("Unable to find enum value '" + enumValueName + "' for enum class: " + type.getName());
/*     */             }
/*     */           } else { Object result;
/* 501 */             if (Calendar.class.isAssignableFrom(type)) {
/* 502 */               SafeConstructor.ConstructYamlTimestamp contr = new SafeConstructor.ConstructYamlTimestamp();
/* 503 */               contr.construct(node);
/* 504 */               result = contr.getCalendar(); } else { Object result;
/* 505 */               if (Number.class.isAssignableFrom(type)) {
/* 506 */                 SafeConstructor.ConstructYamlNumber contr = new SafeConstructor.ConstructYamlNumber(Constructor.this);
/* 507 */                 result = contr.construct(node); } else { Object result;
/* 508 */                 if (UUID.class == type) {
/* 509 */                   result = UUID.fromString(node.getValue());
/*     */                 } else { Object result;
/* 511 */                   if (Constructor.this.yamlConstructors.containsKey(node.getTag())) {
/* 512 */                     result = ((Construct)Constructor.this.yamlConstructors.get(node.getTag())).construct(node);
/*     */                   } else
/* 514 */                     throw new YAMLException("Unsupported class: " + type);
/*     */                 } } } } } }
/*     */       Object result;
/* 517 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */   protected class ConstructSequence
/*     */     implements Construct
/*     */   {
/*     */     protected ConstructSequence() {}
/*     */     
/*     */     public Object construct(Node node)
/*     */     {
/* 528 */       SequenceNode snode = (SequenceNode)node;
/* 529 */       if (Set.class.isAssignableFrom(node.getType())) {
/* 530 */         if (node.isTwoStepsConstruction()) {
/* 531 */           throw new YAMLException("Set cannot be recursive.");
/*     */         }
/* 533 */         return Constructor.this.constructSet(snode);
/*     */       }
/* 535 */       if (Collection.class.isAssignableFrom(node.getType())) {
/* 536 */         if (node.isTwoStepsConstruction()) {
/* 537 */           return Constructor.this.createDefaultList(snode.getValue().size());
/*     */         }
/* 539 */         return Constructor.this.constructSequence(snode);
/*     */       }
/* 541 */       if (node.getType().isArray()) {
/* 542 */         if (node.isTwoStepsConstruction()) {
/* 543 */           return Constructor.this.createArray(node.getType(), snode.getValue().size());
/*     */         }
/* 545 */         return Constructor.this.constructArray(snode);
/*     */       }
/*     */       
/*     */ 
/* 549 */       List<java.lang.reflect.Constructor<?>> possibleConstructors = new ArrayList(snode.getValue().size());
/*     */       
/* 551 */       for (java.lang.reflect.Constructor<?> constructor : node.getType().getDeclaredConstructors())
/*     */       {
/* 553 */         if (snode.getValue().size() == constructor.getParameterTypes().length)
/*     */         {
/* 555 */           possibleConstructors.add(constructor); } }
/*     */       List<Object> argumentList;
/*     */       Class<?>[] parameterTypes;
/* 558 */       if (!possibleConstructors.isEmpty()) {
/* 559 */         if (possibleConstructors.size() == 1) {
/* 560 */           Object[] argumentList = new Object[snode.getValue().size()];
/* 561 */           java.lang.reflect.Constructor<?> c = (java.lang.reflect.Constructor)possibleConstructors.get(0);
/* 562 */           int index = 0;
/* 563 */           for (Node argumentNode : snode.getValue()) {
/* 564 */             Class<?> type = c.getParameterTypes()[index];
/*     */             
/* 566 */             argumentNode.setType(type);
/* 567 */             argumentList[(index++)] = Constructor.this.constructObject(argumentNode);
/*     */           }
/*     */           try
/*     */           {
/* 571 */             c.setAccessible(true);
/* 572 */             return c.newInstance(argumentList);
/*     */           } catch (Exception e) {
/* 574 */             throw new YAMLException(e);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 579 */         argumentList = Constructor.this.constructSequence(snode);
/* 580 */         parameterTypes = new Class[argumentList.size()];
/* 581 */         int index = 0;
/* 582 */         for (Object parameter : argumentList) {
/* 583 */           parameterTypes[index] = parameter.getClass();
/* 584 */           index++;
/*     */         }
/*     */         
/* 587 */         for (java.lang.reflect.Constructor<?> c : possibleConstructors) {
/* 588 */           Class<?>[] argTypes = c.getParameterTypes();
/* 589 */           boolean foundConstructor = true;
/* 590 */           for (int i = 0; i < argTypes.length; i++) {
/* 591 */             if (!wrapIfPrimitive(argTypes[i]).isAssignableFrom(parameterTypes[i])) {
/* 592 */               foundConstructor = false;
/* 593 */               break;
/*     */             }
/*     */           }
/* 596 */           if (foundConstructor) {
/*     */             try {
/* 598 */               c.setAccessible(true);
/* 599 */               return c.newInstance(argumentList.toArray());
/*     */             } catch (Exception e) {
/* 601 */               throw new YAMLException(e);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 606 */       throw new YAMLException("No suitable constructor with " + String.valueOf(snode.getValue().size()) + " arguments found for " + node.getType());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private final Class<? extends Object> wrapIfPrimitive(Class<?> clazz)
/*     */     {
/* 614 */       if (!clazz.isPrimitive()) {
/* 615 */         return clazz;
/*     */       }
/* 617 */       if (clazz == Integer.TYPE) {
/* 618 */         return Integer.class;
/*     */       }
/* 620 */       if (clazz == Float.TYPE) {
/* 621 */         return Float.class;
/*     */       }
/* 623 */       if (clazz == Double.TYPE) {
/* 624 */         return Double.class;
/*     */       }
/* 626 */       if (clazz == Boolean.TYPE) {
/* 627 */         return Boolean.class;
/*     */       }
/* 629 */       if (clazz == Long.TYPE) {
/* 630 */         return Long.class;
/*     */       }
/* 632 */       if (clazz == Character.TYPE) {
/* 633 */         return Character.class;
/*     */       }
/* 635 */       if (clazz == Short.TYPE) {
/* 636 */         return Short.class;
/*     */       }
/* 638 */       if (clazz == Byte.TYPE) {
/* 639 */         return Byte.class;
/*     */       }
/* 641 */       throw new YAMLException("Unexpected primitive " + clazz);
/*     */     }
/*     */     
/*     */     public void construct2ndStep(Node node, Object object)
/*     */     {
/* 646 */       SequenceNode snode = (SequenceNode)node;
/* 647 */       if (List.class.isAssignableFrom(node.getType())) {
/* 648 */         List<Object> list = (List)object;
/* 649 */         Constructor.this.constructSequenceStep2(snode, list);
/* 650 */       } else if (node.getType().isArray()) {
/* 651 */         Constructor.this.constructArrayStep2(snode, object);
/*     */       } else {
/* 653 */         throw new YAMLException("Immutable objects cannot be recursive.");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected Class<?> getClassForNode(Node node) {
/* 659 */     Class<? extends Object> classForTag = (Class)this.typeTags.get(node.getTag());
/* 660 */     if (classForTag == null) {
/* 661 */       String name = node.getTag().getClassName();
/*     */       Class<?> cl;
/*     */       try {
/* 664 */         cl = getClassForName(name);
/*     */       } catch (ClassNotFoundException e) {
/* 666 */         throw new YAMLException("Class not found: " + name);
/*     */       }
/* 668 */       this.typeTags.put(node.getTag(), cl);
/* 669 */       return cl;
/*     */     }
/* 671 */     return classForTag;
/*     */   }
/*     */   
/*     */   protected Class<?> getClassForName(String name) throws ClassNotFoundException
/*     */   {
/*     */     try {
/* 677 */       return Class.forName(name, true, Thread.currentThread().getContextClassLoader());
/*     */     } catch (ClassNotFoundException e) {}
/* 679 */     return Class.forName(name);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\constructor\Constructor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */