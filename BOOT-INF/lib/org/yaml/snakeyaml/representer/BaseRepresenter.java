/*     */ package org.yaml.snakeyaml.representer;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.yaml.snakeyaml.DumperOptions.FlowStyle;
/*     */ import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
/*     */ import org.yaml.snakeyaml.introspector.PropertyUtils;
/*     */ import org.yaml.snakeyaml.nodes.AnchorNode;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
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
/*     */ public abstract class BaseRepresenter
/*     */ {
/*  40 */   protected final Map<Class<?>, Represent> representers = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */   protected Represent nullRepresenter;
/*     */   
/*     */ 
/*     */ 
/*  48 */   protected final Map<Class<?>, Represent> multiRepresenters = new LinkedHashMap();
/*     */   protected Character defaultScalarStyle;
/*  50 */   protected DumperOptions.FlowStyle defaultFlowStyle = DumperOptions.FlowStyle.AUTO;
/*  51 */   protected final Map<Object, Node> representedObjects = new IdentityHashMap() {
/*     */     private static final long serialVersionUID = -5576159264232131854L;
/*     */     
/*     */     public Node put(Object key, Node value) {
/*  55 */       return (Node)super.put(key, new AnchorNode(value));
/*     */     }
/*     */   };
/*     */   
/*     */   protected Object objectToRepresent;
/*     */   private PropertyUtils propertyUtils;
/*  61 */   private boolean explicitPropertyUtils = false;
/*     */   
/*     */   public Node represent(Object data) {
/*  64 */     Node node = representData(data);
/*  65 */     this.representedObjects.clear();
/*  66 */     this.objectToRepresent = null;
/*  67 */     return node;
/*     */   }
/*     */   
/*     */   protected final Node representData(Object data) {
/*  71 */     this.objectToRepresent = data;
/*     */     
/*  73 */     if (this.representedObjects.containsKey(this.objectToRepresent)) {
/*  74 */       Node node = (Node)this.representedObjects.get(this.objectToRepresent);
/*  75 */       return node;
/*     */     }
/*     */     
/*     */ 
/*  79 */     if (data == null) {
/*  80 */       Node node = this.nullRepresenter.representData(null);
/*  81 */       return node;
/*     */     }
/*     */     
/*     */ 
/*  85 */     Class<?> clazz = data.getClass();
/*  86 */     Node node; Node node; if (this.representers.containsKey(clazz)) {
/*  87 */       Represent representer = (Represent)this.representers.get(clazz);
/*  88 */       node = representer.representData(data);
/*     */     }
/*     */     else {
/*  91 */       for (Class<?> repr : this.multiRepresenters.keySet()) {
/*  92 */         if (repr.isInstance(data)) {
/*  93 */           Represent representer = (Represent)this.multiRepresenters.get(repr);
/*  94 */           Node node = representer.representData(data);
/*  95 */           return node;
/*     */         }
/*     */       }
/*     */       
/*     */       Node node;
/* 100 */       if (this.multiRepresenters.containsKey(null)) {
/* 101 */         Represent representer = (Represent)this.multiRepresenters.get(null);
/* 102 */         node = representer.representData(data);
/*     */       } else {
/* 104 */         Represent representer = (Represent)this.representers.get(null);
/* 105 */         node = representer.representData(data);
/*     */       }
/*     */     }
/* 108 */     return node;
/*     */   }
/*     */   
/*     */   protected Node representScalar(Tag tag, String value, Character style) {
/* 112 */     if (style == null) {
/* 113 */       style = this.defaultScalarStyle;
/*     */     }
/* 115 */     Node node = new ScalarNode(tag, value, null, null, style);
/* 116 */     return node;
/*     */   }
/*     */   
/*     */   protected Node representScalar(Tag tag, String value) {
/* 120 */     return representScalar(tag, value, null);
/*     */   }
/*     */   
/*     */   protected Node representSequence(Tag tag, Iterable<?> sequence, Boolean flowStyle) {
/* 124 */     int size = 10;
/* 125 */     if ((sequence instanceof List)) {
/* 126 */       size = ((List)sequence).size();
/*     */     }
/* 128 */     List<Node> value = new ArrayList(size);
/* 129 */     SequenceNode node = new SequenceNode(tag, value, flowStyle);
/* 130 */     this.representedObjects.put(this.objectToRepresent, node);
/* 131 */     boolean bestStyle = true;
/* 132 */     for (Object item : sequence) {
/* 133 */       Node nodeItem = representData(item);
/* 134 */       if ((!(nodeItem instanceof ScalarNode)) || (((ScalarNode)nodeItem).getStyle() != null)) {
/* 135 */         bestStyle = false;
/*     */       }
/* 137 */       value.add(nodeItem);
/*     */     }
/* 139 */     if (flowStyle == null) {
/* 140 */       if (this.defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
/* 141 */         node.setFlowStyle(this.defaultFlowStyle.getStyleBoolean());
/*     */       } else {
/* 143 */         node.setFlowStyle(Boolean.valueOf(bestStyle));
/*     */       }
/*     */     }
/* 146 */     return node;
/*     */   }
/*     */   
/*     */   protected Node representMapping(Tag tag, Map<?, ?> mapping, Boolean flowStyle) {
/* 150 */     List<NodeTuple> value = new ArrayList(mapping.size());
/* 151 */     MappingNode node = new MappingNode(tag, value, flowStyle);
/* 152 */     this.representedObjects.put(this.objectToRepresent, node);
/* 153 */     boolean bestStyle = true;
/* 154 */     for (Map.Entry<?, ?> entry : mapping.entrySet()) {
/* 155 */       Node nodeKey = representData(entry.getKey());
/* 156 */       Node nodeValue = representData(entry.getValue());
/* 157 */       if ((!(nodeKey instanceof ScalarNode)) || (((ScalarNode)nodeKey).getStyle() != null)) {
/* 158 */         bestStyle = false;
/*     */       }
/* 160 */       if ((!(nodeValue instanceof ScalarNode)) || (((ScalarNode)nodeValue).getStyle() != null)) {
/* 161 */         bestStyle = false;
/*     */       }
/* 163 */       value.add(new NodeTuple(nodeKey, nodeValue));
/*     */     }
/* 165 */     if (flowStyle == null) {
/* 166 */       if (this.defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
/* 167 */         node.setFlowStyle(this.defaultFlowStyle.getStyleBoolean());
/*     */       } else {
/* 169 */         node.setFlowStyle(Boolean.valueOf(bestStyle));
/*     */       }
/*     */     }
/* 172 */     return node;
/*     */   }
/*     */   
/*     */   public void setDefaultScalarStyle(DumperOptions.ScalarStyle defaultStyle) {
/* 176 */     this.defaultScalarStyle = defaultStyle.getChar();
/*     */   }
/*     */   
/*     */   public void setDefaultFlowStyle(DumperOptions.FlowStyle defaultFlowStyle) {
/* 180 */     this.defaultFlowStyle = defaultFlowStyle;
/*     */   }
/*     */   
/*     */   public DumperOptions.FlowStyle getDefaultFlowStyle() {
/* 184 */     return this.defaultFlowStyle;
/*     */   }
/*     */   
/*     */   public void setPropertyUtils(PropertyUtils propertyUtils) {
/* 188 */     this.propertyUtils = propertyUtils;
/* 189 */     this.explicitPropertyUtils = true;
/*     */   }
/*     */   
/*     */   public final PropertyUtils getPropertyUtils() {
/* 193 */     if (this.propertyUtils == null) {
/* 194 */       this.propertyUtils = new PropertyUtils();
/*     */     }
/* 196 */     return this.propertyUtils;
/*     */   }
/*     */   
/*     */   public final boolean isExplicitPropertyUtils() {
/* 200 */     return this.explicitPropertyUtils;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\representer\BaseRepresenter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */