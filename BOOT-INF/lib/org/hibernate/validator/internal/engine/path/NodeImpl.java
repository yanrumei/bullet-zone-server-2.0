/*     */ package org.hibernate.validator.internal.engine.path;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.validation.ElementKind;
/*     */ import javax.validation.Path.BeanNode;
/*     */ import javax.validation.Path.ConstructorNode;
/*     */ import javax.validation.Path.CrossParameterNode;
/*     */ import javax.validation.Path.MethodNode;
/*     */ import javax.validation.Path.Node;
/*     */ import javax.validation.Path.ParameterNode;
/*     */ import javax.validation.Path.PropertyNode;
/*     */ import javax.validation.Path.ReturnValueNode;
/*     */ import org.hibernate.validator.internal.util.Contracts;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.path.PropertyNode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NodeImpl
/*     */   implements Path.PropertyNode, Path.MethodNode, Path.ConstructorNode, Path.BeanNode, Path.ParameterNode, Path.ReturnValueNode, Path.CrossParameterNode, PropertyNode, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2075466571633860499L;
/*  36 */   private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
/*     */   
/*  38 */   private static final Log log = LoggerFactory.make();
/*     */   
/*     */   private static final String INDEX_OPEN = "[";
/*     */   
/*     */   private static final String INDEX_CLOSE = "]";
/*     */   
/*     */   private static final String RETURN_VALUE_NODE_NAME = "<return value>";
/*     */   
/*     */   private static final String CROSS_PARAMETER_NODE_NAME = "<cross-parameter>";
/*     */   private static final String COLLECTION_ELEMENT_NODE_NAME = "<collection element>";
/*     */   private final String name;
/*     */   private final NodeImpl parent;
/*     */   private final boolean isIterable;
/*     */   private final Integer index;
/*     */   private final Object key;
/*     */   private final ElementKind kind;
/*     */   private final int hashCode;
/*     */   private final Class<?>[] parameterTypes;
/*     */   private final Integer parameterIndex;
/*     */   private final Object value;
/*     */   private String asString;
/*     */   
/*     */   private NodeImpl(String name, NodeImpl parent, boolean indexable, Integer index, Object key, ElementKind kind, Class<?>[] parameterTypes, Integer parameterIndex, Object value)
/*     */   {
/*  62 */     this.name = name;
/*  63 */     this.parent = parent;
/*  64 */     this.index = index;
/*  65 */     this.key = key;
/*  66 */     this.value = value;
/*  67 */     this.isIterable = indexable;
/*  68 */     this.kind = kind;
/*  69 */     this.parameterTypes = parameterTypes;
/*  70 */     this.parameterIndex = parameterIndex;
/*  71 */     this.hashCode = buildHashCode();
/*     */   }
/*     */   
/*     */   public static NodeImpl createPropertyNode(String name, NodeImpl parent)
/*     */   {
/*  76 */     return new NodeImpl(name, parent, false, null, null, ElementKind.PROPERTY, EMPTY_CLASS_ARRAY, null, null);
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
/*     */   public static NodeImpl createCollectionElementNode(NodeImpl parent)
/*     */   {
/*  90 */     return new NodeImpl("<collection element>", parent, false, null, null, ElementKind.PROPERTY, EMPTY_CLASS_ARRAY, null, null);
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
/*     */   public static NodeImpl createParameterNode(String name, NodeImpl parent, int parameterIndex)
/*     */   {
/* 104 */     return new NodeImpl(name, parent, false, null, null, ElementKind.PARAMETER, EMPTY_CLASS_ARRAY, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 112 */       Integer.valueOf(parameterIndex), null);
/*     */   }
/*     */   
/*     */ 
/*     */   public static NodeImpl createCrossParameterNode(NodeImpl parent)
/*     */   {
/* 118 */     return new NodeImpl("<cross-parameter>", parent, false, null, null, ElementKind.CROSS_PARAMETER, EMPTY_CLASS_ARRAY, null, null);
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
/*     */   public static NodeImpl createMethodNode(String name, NodeImpl parent, Class<?>[] parameterTypes)
/*     */   {
/* 132 */     return new NodeImpl(name, parent, false, null, null, ElementKind.METHOD, parameterTypes, null, null);
/*     */   }
/*     */   
/*     */   public static NodeImpl createConstructorNode(String name, NodeImpl parent, Class<?>[] parameterTypes) {
/* 136 */     return new NodeImpl(name, parent, false, null, null, ElementKind.CONSTRUCTOR, parameterTypes, null, null);
/*     */   }
/*     */   
/*     */   public static NodeImpl createBeanNode(NodeImpl parent) {
/* 140 */     return new NodeImpl(null, parent, false, null, null, ElementKind.BEAN, EMPTY_CLASS_ARRAY, null, null);
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
/*     */   public static NodeImpl createReturnValue(NodeImpl parent)
/*     */   {
/* 154 */     return new NodeImpl("<return value>", parent, false, null, null, ElementKind.RETURN_VALUE, EMPTY_CLASS_ARRAY, null, null);
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
/*     */   public static NodeImpl makeIterable(NodeImpl node)
/*     */   {
/* 168 */     return new NodeImpl(node.name, node.parent, true, null, null, node.kind, node.parameterTypes, node.parameterIndex, node.value);
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
/*     */   public static NodeImpl setIndex(NodeImpl node, Integer index)
/*     */   {
/* 183 */     return new NodeImpl(node.name, node.parent, true, index, null, node.kind, node.parameterTypes, node.parameterIndex, node.value);
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
/*     */   public static NodeImpl setMapKey(NodeImpl node, Object key)
/*     */   {
/* 197 */     return new NodeImpl(node.name, node.parent, true, null, key, node.kind, node.parameterTypes, node.parameterIndex, node.value);
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
/*     */   public static NodeImpl setPropertyValue(NodeImpl node, Object value)
/*     */   {
/* 211 */     return new NodeImpl(node.name, node.parent, node.isIterable, node.index, node.key, node.kind, node.parameterTypes, node.parameterIndex, value);
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
/*     */   public final String getName()
/*     */   {
/* 226 */     return this.name;
/*     */   }
/*     */   
/*     */   public final boolean isInIterable()
/*     */   {
/* 231 */     return (this.parent != null) && (this.parent.isIterable());
/*     */   }
/*     */   
/*     */   public final boolean isIterable() {
/* 235 */     return this.isIterable;
/*     */   }
/*     */   
/*     */   public final Integer getIndex()
/*     */   {
/* 240 */     if (this.parent == null) {
/* 241 */       return null;
/*     */     }
/*     */     
/* 244 */     return this.parent.index;
/*     */   }
/*     */   
/*     */ 
/*     */   public final Object getKey()
/*     */   {
/* 250 */     if (this.parent == null) {
/* 251 */       return null;
/*     */     }
/*     */     
/* 254 */     return this.parent.key;
/*     */   }
/*     */   
/*     */   public final NodeImpl getParent()
/*     */   {
/* 259 */     return this.parent;
/*     */   }
/*     */   
/*     */   public ElementKind getKind()
/*     */   {
/* 264 */     return this.kind;
/*     */   }
/*     */   
/*     */   public <T extends Path.Node> T as(Class<T> nodeType)
/*     */   {
/* 269 */     if (((this.kind == ElementKind.BEAN) && (nodeType == Path.BeanNode.class)) || ((this.kind == ElementKind.CONSTRUCTOR) && (nodeType == Path.ConstructorNode.class)) || ((this.kind == ElementKind.CROSS_PARAMETER) && (nodeType == Path.CrossParameterNode.class)) || ((this.kind == ElementKind.METHOD) && (nodeType == Path.MethodNode.class)) || ((this.kind == ElementKind.PARAMETER) && (nodeType == Path.ParameterNode.class)) || ((this.kind == ElementKind.PROPERTY) && ((nodeType == Path.PropertyNode.class) || (nodeType == PropertyNode.class))) || ((this.kind == ElementKind.RETURN_VALUE) && (nodeType == Path.ReturnValueNode.class)))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 276 */       return (Path.Node)nodeType.cast(this);
/*     */     }
/*     */     
/* 279 */     throw log.getUnableToNarrowNodeTypeException(getClass().getName(), this.kind, nodeType.getName());
/*     */   }
/*     */   
/*     */   public List<Class<?>> getParameterTypes()
/*     */   {
/* 284 */     return Arrays.asList(this.parameterTypes);
/*     */   }
/*     */   
/*     */   public int getParameterIndex()
/*     */   {
/* 289 */     Contracts.assertTrue(this.kind == ElementKind.PARAMETER, "getParameterIndex() may only be invoked for nodes of ElementKind.PARAMETER.");
/*     */     
/*     */ 
/*     */ 
/* 293 */     return this.parameterIndex.intValue();
/*     */   }
/*     */   
/*     */   public Object getValue()
/*     */   {
/* 298 */     return this.value;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 303 */     return asString();
/*     */   }
/*     */   
/*     */   public final String asString() {
/* 307 */     if (this.asString == null) {
/* 308 */       this.asString = buildToString();
/*     */     }
/* 310 */     return this.asString;
/*     */   }
/*     */   
/*     */   private String buildToString() {
/* 314 */     StringBuilder builder = new StringBuilder();
/*     */     
/* 316 */     if (ElementKind.BEAN.equals(getKind()))
/*     */     {
/* 318 */       builder.append("");
/*     */     }
/*     */     else {
/* 321 */       builder.append(getName());
/*     */     }
/*     */     
/* 324 */     if (isIterable()) {
/* 325 */       builder.append("[");
/* 326 */       if (this.index != null) {
/* 327 */         builder.append(this.index);
/*     */       }
/* 329 */       else if (this.key != null) {
/* 330 */         builder.append(this.key);
/*     */       }
/* 332 */       builder.append("]");
/*     */     }
/* 334 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public final int buildHashCode() {
/* 338 */     int prime = 31;
/* 339 */     int result = 1;
/* 340 */     result = 31 * result + (this.index == null ? 0 : this.index.hashCode());
/* 341 */     result = 31 * result + (this.isIterable ? 1231 : 1237);
/* 342 */     result = 31 * result + (this.key == null ? 0 : this.key.hashCode());
/* 343 */     result = 31 * result + (this.kind == null ? 0 : this.kind.hashCode());
/* 344 */     result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
/* 345 */     result = 31 * result + (this.parameterIndex == null ? 0 : this.parameterIndex.hashCode());
/* 346 */     result = 31 * result + (this.parameterTypes == null ? 0 : this.parameterTypes.hashCode());
/* 347 */     result = 31 * result + (this.parent == null ? 0 : this.parent.hashCode());
/* 348 */     return result;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 353 */     return this.hashCode;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 358 */     if (this == obj) {
/* 359 */       return true;
/*     */     }
/* 361 */     if (obj == null) {
/* 362 */       return false;
/*     */     }
/* 364 */     if (getClass() != obj.getClass()) {
/* 365 */       return false;
/*     */     }
/* 367 */     NodeImpl other = (NodeImpl)obj;
/* 368 */     if (this.index == null) {
/* 369 */       if (other.index != null) {
/* 370 */         return false;
/*     */       }
/*     */     }
/* 373 */     else if (!this.index.equals(other.index)) {
/* 374 */       return false;
/*     */     }
/* 376 */     if (this.isIterable != other.isIterable) {
/* 377 */       return false;
/*     */     }
/* 379 */     if (this.key == null) {
/* 380 */       if (other.key != null) {
/* 381 */         return false;
/*     */       }
/*     */     }
/* 384 */     else if (!this.key.equals(other.key)) {
/* 385 */       return false;
/*     */     }
/* 387 */     if (this.kind != other.kind) {
/* 388 */       return false;
/*     */     }
/* 390 */     if (this.name == null) {
/* 391 */       if (other.name != null) {
/* 392 */         return false;
/*     */       }
/*     */     }
/* 395 */     else if (!this.name.equals(other.name)) {
/* 396 */       return false;
/*     */     }
/* 398 */     if (this.parameterIndex == null) {
/* 399 */       if (other.parameterIndex != null) {
/* 400 */         return false;
/*     */       }
/*     */     }
/* 403 */     else if (!this.parameterIndex.equals(other.parameterIndex)) {
/* 404 */       return false;
/*     */     }
/* 406 */     if (this.parameterTypes == null) {
/* 407 */       if (other.parameterTypes != null) {
/* 408 */         return false;
/*     */       }
/*     */     }
/* 411 */     else if (!this.parameterTypes.equals(other.parameterTypes)) {
/* 412 */       return false;
/*     */     }
/* 414 */     if (this.parent == null) {
/* 415 */       if (other.parent != null) {
/* 416 */         return false;
/*     */       }
/*     */     }
/* 419 */     else if (!this.parent.equals(other.parent)) {
/* 420 */       return false;
/*     */     }
/* 422 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\path\NodeImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */