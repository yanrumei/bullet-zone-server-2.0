/*     */ package org.yaml.snakeyaml.representer;
/*     */ 
/*     */ import java.beans.IntrospectionException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.yaml.snakeyaml.DumperOptions.FlowStyle;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Representer
/*     */   extends SafeRepresenter
/*     */ {
/*  44 */   public Representer() { this.representers.put(null, new RepresentJavaBean()); }
/*     */   
/*     */   protected class RepresentJavaBean implements Represent {
/*     */     protected RepresentJavaBean() {}
/*     */     
/*     */     public Node representData(Object data) {
/*  50 */       try { return Representer.this.representJavaBean(Representer.this.getProperties(data.getClass()), data);
/*     */       } catch (IntrospectionException e) {
/*  52 */         throw new YAMLException(e);
/*     */       }
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
/*     */   protected MappingNode representJavaBean(Set<Property> properties, Object javaBean)
/*     */   {
/*  72 */     List<NodeTuple> value = new ArrayList(properties.size());
/*     */     
/*  74 */     Tag customTag = (Tag)this.classTags.get(javaBean.getClass());
/*  75 */     Tag tag = customTag != null ? customTag : new Tag(javaBean.getClass());
/*     */     
/*  77 */     MappingNode node = new MappingNode(tag, value, null);
/*  78 */     this.representedObjects.put(javaBean, node);
/*  79 */     boolean bestStyle = true;
/*  80 */     for (Property property : properties) {
/*  81 */       Object memberValue = property.get(javaBean);
/*  82 */       Tag customPropertyTag = memberValue == null ? null : (Tag)this.classTags.get(memberValue.getClass());
/*     */       
/*  84 */       NodeTuple tuple = representJavaBeanProperty(javaBean, property, memberValue, customPropertyTag);
/*     */       
/*  86 */       if (tuple != null)
/*     */       {
/*     */ 
/*  89 */         if (((ScalarNode)tuple.getKeyNode()).getStyle() != null) {
/*  90 */           bestStyle = false;
/*     */         }
/*  92 */         Node nodeValue = tuple.getValueNode();
/*  93 */         if ((!(nodeValue instanceof ScalarNode)) || (((ScalarNode)nodeValue).getStyle() != null)) {
/*  94 */           bestStyle = false;
/*     */         }
/*  96 */         value.add(tuple);
/*     */       } }
/*  98 */     if (this.defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
/*  99 */       node.setFlowStyle(this.defaultFlowStyle.getStyleBoolean());
/*     */     } else {
/* 101 */       node.setFlowStyle(Boolean.valueOf(bestStyle));
/*     */     }
/* 103 */     return node;
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
/*     */   protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue, Tag customTag)
/*     */   {
/* 122 */     ScalarNode nodeKey = (ScalarNode)representData(property.getName());
/*     */     
/* 124 */     boolean hasAlias = this.representedObjects.containsKey(propertyValue);
/*     */     
/* 126 */     Node nodeValue = representData(propertyValue);
/*     */     
/* 128 */     if ((propertyValue != null) && (!hasAlias)) {
/* 129 */       NodeId nodeId = nodeValue.getNodeId();
/* 130 */       if (customTag == null) {
/* 131 */         if (nodeId == NodeId.scalar) {
/* 132 */           if ((propertyValue instanceof Enum)) {
/* 133 */             nodeValue.setTag(Tag.STR);
/*     */           }
/*     */         } else {
/* 136 */           if ((nodeId == NodeId.mapping) && 
/* 137 */             (property.getType() == propertyValue.getClass()) && 
/* 138 */             (!(propertyValue instanceof Map)) && 
/* 139 */             (!nodeValue.getTag().equals(Tag.SET))) {
/* 140 */             nodeValue.setTag(Tag.MAP);
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 145 */           checkGlobalTag(property, nodeValue, propertyValue);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 150 */     return new NodeTuple(nodeKey, nodeValue);
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
/*     */   protected void checkGlobalTag(Property property, Node node, Object object)
/*     */   {
/* 167 */     if ((object.getClass().isArray()) && (object.getClass().getComponentType().isPrimitive())) {
/* 168 */       return;
/*     */     }
/*     */     
/* 171 */     Class<?>[] arguments = property.getActualTypeArguments();
/* 172 */     Class<?> keyType; Class<?> valueType; if (arguments != null) { Class<? extends Object> t;
/* 173 */       Iterator<Object> iter; if (node.getNodeId() == NodeId.sequence)
/*     */       {
/* 175 */         t = arguments[0];
/* 176 */         SequenceNode snode = (SequenceNode)node;
/* 177 */         Iterable<Object> memberList = Collections.EMPTY_LIST;
/* 178 */         if (object.getClass().isArray()) {
/* 179 */           memberList = Arrays.asList((Object[])object);
/* 180 */         } else if ((object instanceof Iterable))
/*     */         {
/* 182 */           memberList = (Iterable)object;
/*     */         }
/* 184 */         iter = memberList.iterator();
/* 185 */         if (iter.hasNext())
/* 186 */           for (Node childNode : snode.getValue()) {
/* 187 */             Object member = iter.next();
/* 188 */             if ((member != null) && 
/* 189 */               (t.equals(member.getClass())) && 
/* 190 */               (childNode.getNodeId() == NodeId.mapping))
/* 191 */               childNode.setTag(Tag.MAP);
/*     */           }
/*     */       } else {
/*     */         Class<?> t;
/*     */         Iterator<NodeTuple> iter;
/* 196 */         if ((object instanceof Set)) {
/* 197 */           t = arguments[0];
/* 198 */           MappingNode mnode = (MappingNode)node;
/* 199 */           iter = mnode.getValue().iterator();
/* 200 */           Set<?> set = (Set)object;
/* 201 */           for (Object member : set) {
/* 202 */             NodeTuple tuple = (NodeTuple)iter.next();
/* 203 */             Node keyNode = tuple.getKeyNode();
/* 204 */             if ((t.equals(member.getClass())) && 
/* 205 */               (keyNode.getNodeId() == NodeId.mapping)) {
/* 206 */               keyNode.setTag(Tag.MAP);
/*     */             }
/*     */           }
/*     */         }
/* 210 */         else if ((object instanceof Map)) {
/* 211 */           keyType = arguments[0];
/* 212 */           valueType = arguments[1];
/* 213 */           MappingNode mnode = (MappingNode)node;
/* 214 */           for (NodeTuple tuple : mnode.getValue()) {
/* 215 */             resetTag(keyType, tuple.getKeyNode());
/* 216 */             resetTag(valueType, tuple.getValueNode());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void resetTag(Class<? extends Object> type, Node node)
/*     */   {
/* 226 */     Tag tag = node.getTag();
/* 227 */     if (tag.matches(type)) {
/* 228 */       if (Enum.class.isAssignableFrom(type)) {
/* 229 */         node.setTag(Tag.STR);
/*     */       } else {
/* 231 */         node.setTag(Tag.MAP);
/*     */       }
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
/*     */   protected Set<Property> getProperties(Class<? extends Object> type)
/*     */     throws IntrospectionException
/*     */   {
/* 246 */     return getPropertyUtils().getProperties(type);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\representer\Representer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */