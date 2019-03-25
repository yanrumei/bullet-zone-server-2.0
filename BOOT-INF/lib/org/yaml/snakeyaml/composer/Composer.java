/*     */ package org.yaml.snakeyaml.composer;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.yaml.snakeyaml.events.AliasEvent;
/*     */ import org.yaml.snakeyaml.events.Event;
/*     */ import org.yaml.snakeyaml.events.Event.ID;
/*     */ import org.yaml.snakeyaml.events.ImplicitTuple;
/*     */ import org.yaml.snakeyaml.events.MappingStartEvent;
/*     */ import org.yaml.snakeyaml.events.NodeEvent;
/*     */ import org.yaml.snakeyaml.events.ScalarEvent;
/*     */ import org.yaml.snakeyaml.events.SequenceStartEvent;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
/*     */ import org.yaml.snakeyaml.nodes.NodeTuple;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.SequenceNode;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ import org.yaml.snakeyaml.parser.Parser;
/*     */ import org.yaml.snakeyaml.resolver.Resolver;
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
/*     */ public class Composer
/*     */ {
/*     */   protected final Parser parser;
/*     */   private final Resolver resolver;
/*     */   private final Map<String, Node> anchors;
/*     */   private final Set<Node> recursiveNodes;
/*     */   
/*     */   public Composer(Parser parser, Resolver resolver)
/*     */   {
/*  55 */     this.parser = parser;
/*  56 */     this.resolver = resolver;
/*  57 */     this.anchors = new HashMap();
/*  58 */     this.recursiveNodes = new HashSet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean checkNode()
/*     */   {
/*  68 */     if (this.parser.checkEvent(Event.ID.StreamStart)) {
/*  69 */       this.parser.getEvent();
/*     */     }
/*     */     
/*  72 */     return !this.parser.checkEvent(Event.ID.StreamEnd);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Node getNode()
/*     */   {
/*  83 */     if (!this.parser.checkEvent(Event.ID.StreamEnd)) {
/*  84 */       return composeDocument();
/*     */     }
/*  86 */     return null;
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
/*     */   public Node getSingleNode()
/*     */   {
/* 101 */     this.parser.getEvent();
/*     */     
/* 103 */     Node document = null;
/* 104 */     if (!this.parser.checkEvent(Event.ID.StreamEnd)) {
/* 105 */       document = composeDocument();
/*     */     }
/*     */     
/* 108 */     if (!this.parser.checkEvent(Event.ID.StreamEnd)) {
/* 109 */       Event event = this.parser.getEvent();
/* 110 */       throw new ComposerException("expected a single document in the stream", document.getStartMark(), "but found another document", event.getStartMark());
/*     */     }
/*     */     
/*     */ 
/* 114 */     this.parser.getEvent();
/* 115 */     return document;
/*     */   }
/*     */   
/*     */   private Node composeDocument()
/*     */   {
/* 120 */     this.parser.getEvent();
/*     */     
/* 122 */     Node node = composeNode(null);
/*     */     
/* 124 */     this.parser.getEvent();
/* 125 */     this.anchors.clear();
/* 126 */     this.recursiveNodes.clear();
/* 127 */     return node;
/*     */   }
/*     */   
/*     */   private Node composeNode(Node parent) {
/* 131 */     this.recursiveNodes.add(parent);
/* 132 */     if (this.parser.checkEvent(Event.ID.Alias)) {
/* 133 */       AliasEvent event = (AliasEvent)this.parser.getEvent();
/* 134 */       String anchor = event.getAnchor();
/* 135 */       if (!this.anchors.containsKey(anchor)) {
/* 136 */         throw new ComposerException(null, null, "found undefined alias " + anchor, event.getStartMark());
/*     */       }
/*     */       
/* 139 */       Node result = (Node)this.anchors.get(anchor);
/* 140 */       if (this.recursiveNodes.remove(result)) {
/* 141 */         result.setTwoStepsConstruction(true);
/*     */       }
/* 143 */       return result;
/*     */     }
/* 145 */     NodeEvent event = (NodeEvent)this.parser.peekEvent();
/* 146 */     String anchor = null;
/* 147 */     anchor = event.getAnchor();
/*     */     
/* 149 */     Node node = null;
/* 150 */     if (this.parser.checkEvent(Event.ID.Scalar)) {
/* 151 */       node = composeScalarNode(anchor);
/* 152 */     } else if (this.parser.checkEvent(Event.ID.SequenceStart)) {
/* 153 */       node = composeSequenceNode(anchor);
/*     */     } else {
/* 155 */       node = composeMappingNode(anchor);
/*     */     }
/* 157 */     this.recursiveNodes.remove(parent);
/* 158 */     return node;
/*     */   }
/*     */   
/*     */   protected Node composeScalarNode(String anchor) {
/* 162 */     ScalarEvent ev = (ScalarEvent)this.parser.getEvent();
/* 163 */     String tag = ev.getTag();
/* 164 */     boolean resolved = false;
/*     */     Tag nodeTag;
/* 166 */     if ((tag == null) || (tag.equals("!"))) {
/* 167 */       Tag nodeTag = this.resolver.resolve(NodeId.scalar, ev.getValue(), ev.getImplicit().canOmitTagInPlainScalar());
/*     */       
/* 169 */       resolved = true;
/*     */     } else {
/* 171 */       nodeTag = new Tag(tag);
/*     */     }
/* 173 */     Node node = new ScalarNode(nodeTag, resolved, ev.getValue(), ev.getStartMark(), ev.getEndMark(), ev.getStyle());
/*     */     
/* 175 */     if (anchor != null) {
/* 176 */       this.anchors.put(anchor, node);
/*     */     }
/* 178 */     return node;
/*     */   }
/*     */   
/*     */   protected Node composeSequenceNode(String anchor) {
/* 182 */     SequenceStartEvent startEvent = (SequenceStartEvent)this.parser.getEvent();
/* 183 */     String tag = startEvent.getTag();
/*     */     
/* 185 */     boolean resolved = false;
/* 186 */     Tag nodeTag; if ((tag == null) || (tag.equals("!"))) {
/* 187 */       Tag nodeTag = this.resolver.resolve(NodeId.sequence, null, startEvent.getImplicit());
/* 188 */       resolved = true;
/*     */     } else {
/* 190 */       nodeTag = new Tag(tag);
/*     */     }
/* 192 */     ArrayList<Node> children = new ArrayList();
/* 193 */     SequenceNode node = new SequenceNode(nodeTag, resolved, children, startEvent.getStartMark(), null, startEvent.getFlowStyle());
/*     */     
/* 195 */     if (anchor != null) {
/* 196 */       this.anchors.put(anchor, node);
/*     */     }
/* 198 */     while (!this.parser.checkEvent(Event.ID.SequenceEnd)) {
/* 199 */       children.add(composeNode(node));
/*     */     }
/* 201 */     Event endEvent = this.parser.getEvent();
/* 202 */     node.setEndMark(endEvent.getEndMark());
/* 203 */     return node;
/*     */   }
/*     */   
/*     */   protected Node composeMappingNode(String anchor) {
/* 207 */     MappingStartEvent startEvent = (MappingStartEvent)this.parser.getEvent();
/* 208 */     String tag = startEvent.getTag();
/*     */     
/* 210 */     boolean resolved = false;
/* 211 */     Tag nodeTag; if ((tag == null) || (tag.equals("!"))) {
/* 212 */       Tag nodeTag = this.resolver.resolve(NodeId.mapping, null, startEvent.getImplicit());
/* 213 */       resolved = true;
/*     */     } else {
/* 215 */       nodeTag = new Tag(tag);
/*     */     }
/*     */     
/* 218 */     List<NodeTuple> children = new ArrayList();
/* 219 */     MappingNode node = new MappingNode(nodeTag, resolved, children, startEvent.getStartMark(), null, startEvent.getFlowStyle());
/*     */     
/* 221 */     if (anchor != null) {
/* 222 */       this.anchors.put(anchor, node);
/*     */     }
/* 224 */     while (!this.parser.checkEvent(Event.ID.MappingEnd)) {
/* 225 */       composeMappingChildren(children, node);
/*     */     }
/* 227 */     Event endEvent = this.parser.getEvent();
/* 228 */     node.setEndMark(endEvent.getEndMark());
/* 229 */     return node;
/*     */   }
/*     */   
/*     */   protected void composeMappingChildren(List<NodeTuple> children, MappingNode node) {
/* 233 */     Node itemKey = composeKeyNode(node);
/* 234 */     if (itemKey.getTag().equals(Tag.MERGE)) {
/* 235 */       node.setMerged(true);
/*     */     }
/* 237 */     Node itemValue = composeValueNode(node);
/* 238 */     children.add(new NodeTuple(itemKey, itemValue));
/*     */   }
/*     */   
/*     */   protected Node composeKeyNode(MappingNode node) {
/* 242 */     return composeNode(node);
/*     */   }
/*     */   
/*     */   protected Node composeValueNode(MappingNode node) {
/* 246 */     return composeNode(node);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\composer\Composer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */