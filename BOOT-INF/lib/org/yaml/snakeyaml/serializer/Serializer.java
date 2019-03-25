/*     */ package org.yaml.snakeyaml.serializer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.yaml.snakeyaml.DumperOptions;
/*     */ import org.yaml.snakeyaml.DumperOptions.Version;
/*     */ import org.yaml.snakeyaml.emitter.Emitable;
/*     */ import org.yaml.snakeyaml.events.AliasEvent;
/*     */ import org.yaml.snakeyaml.events.DocumentEndEvent;
/*     */ import org.yaml.snakeyaml.events.DocumentStartEvent;
/*     */ import org.yaml.snakeyaml.events.ImplicitTuple;
/*     */ import org.yaml.snakeyaml.events.MappingEndEvent;
/*     */ import org.yaml.snakeyaml.events.MappingStartEvent;
/*     */ import org.yaml.snakeyaml.events.ScalarEvent;
/*     */ import org.yaml.snakeyaml.events.SequenceEndEvent;
/*     */ import org.yaml.snakeyaml.events.SequenceStartEvent;
/*     */ import org.yaml.snakeyaml.events.StreamEndEvent;
/*     */ import org.yaml.snakeyaml.events.StreamStartEvent;
/*     */ import org.yaml.snakeyaml.nodes.AnchorNode;
/*     */ import org.yaml.snakeyaml.nodes.CollectionNode;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
/*     */ import org.yaml.snakeyaml.nodes.NodeTuple;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.SequenceNode;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
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
/*     */ public final class Serializer
/*     */ {
/*     */   private final Emitable emitter;
/*     */   private final Resolver resolver;
/*     */   private boolean explicitStart;
/*     */   private boolean explicitEnd;
/*     */   private DumperOptions.Version useVersion;
/*     */   private Map<String, String> useTags;
/*     */   private Set<Node> serializedNodes;
/*     */   private Map<Node, String> anchors;
/*     */   private AnchorGenerator anchorGenerator;
/*     */   private Boolean closed;
/*     */   private Tag explicitRoot;
/*     */   
/*     */   public Serializer(Emitable emitter, Resolver resolver, DumperOptions opts, Tag rootTag)
/*     */   {
/*  65 */     this.emitter = emitter;
/*  66 */     this.resolver = resolver;
/*  67 */     this.explicitStart = opts.isExplicitStart();
/*  68 */     this.explicitEnd = opts.isExplicitEnd();
/*  69 */     if (opts.getVersion() != null) {
/*  70 */       this.useVersion = opts.getVersion();
/*     */     }
/*  72 */     this.useTags = opts.getTags();
/*  73 */     this.serializedNodes = new HashSet();
/*  74 */     this.anchors = new HashMap();
/*  75 */     this.anchorGenerator = opts.getAnchorGenerator();
/*  76 */     this.closed = null;
/*  77 */     this.explicitRoot = rootTag;
/*     */   }
/*     */   
/*     */   public void open() throws IOException {
/*  81 */     if (this.closed == null) {
/*  82 */       this.emitter.emit(new StreamStartEvent(null, null));
/*  83 */       this.closed = Boolean.FALSE;
/*  84 */     } else { if (Boolean.TRUE.equals(this.closed)) {
/*  85 */         throw new SerializerException("serializer is closed");
/*     */       }
/*  87 */       throw new SerializerException("serializer is already opened");
/*     */     }
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/*  92 */     if (this.closed == null)
/*  93 */       throw new SerializerException("serializer is not opened");
/*  94 */     if (!Boolean.TRUE.equals(this.closed)) {
/*  95 */       this.emitter.emit(new StreamEndEvent(null, null));
/*  96 */       this.closed = Boolean.TRUE;
/*     */     }
/*     */   }
/*     */   
/*     */   public void serialize(Node node) throws IOException {
/* 101 */     if (this.closed == null)
/* 102 */       throw new SerializerException("serializer is not opened");
/* 103 */     if (this.closed.booleanValue()) {
/* 104 */       throw new SerializerException("serializer is closed");
/*     */     }
/* 106 */     this.emitter.emit(new DocumentStartEvent(null, null, this.explicitStart, this.useVersion, this.useTags));
/*     */     
/* 108 */     anchorNode(node);
/* 109 */     if (this.explicitRoot != null) {
/* 110 */       node.setTag(this.explicitRoot);
/*     */     }
/* 112 */     serializeNode(node, null);
/* 113 */     this.emitter.emit(new DocumentEndEvent(null, null, this.explicitEnd));
/* 114 */     this.serializedNodes.clear();
/* 115 */     this.anchors.clear();
/*     */   }
/*     */   
/*     */   private void anchorNode(Node node) {
/* 119 */     if (node.getNodeId() == NodeId.anchor) {
/* 120 */       node = ((AnchorNode)node).getRealNode();
/*     */     }
/* 122 */     if (this.anchors.containsKey(node)) {
/* 123 */       String anchor = (String)this.anchors.get(node);
/* 124 */       if (null == anchor) {
/* 125 */         anchor = this.anchorGenerator.nextAnchor(node);
/* 126 */         this.anchors.put(node, anchor);
/*     */       }
/*     */     } else {
/* 129 */       this.anchors.put(node, null);
/* 130 */       switch (node.getNodeId()) {
/*     */       case sequence: 
/* 132 */         SequenceNode seqNode = (SequenceNode)node;
/* 133 */         List<Node> list = seqNode.getValue();
/* 134 */         for (Node item : list) {
/* 135 */           anchorNode(item);
/*     */         }
/* 137 */         break;
/*     */       case mapping: 
/* 139 */         MappingNode mnode = (MappingNode)node;
/* 140 */         List<NodeTuple> map = mnode.getValue();
/* 141 */         for (NodeTuple object : map) {
/* 142 */           Node key = object.getKeyNode();
/* 143 */           Node value = object.getValueNode();
/* 144 */           anchorNode(key);
/* 145 */           anchorNode(value);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void serializeNode(Node node, Node parent) throws IOException
/*     */   {
/* 153 */     if (node.getNodeId() == NodeId.anchor) {
/* 154 */       node = ((AnchorNode)node).getRealNode();
/*     */     }
/* 156 */     String tAlias = (String)this.anchors.get(node);
/* 157 */     if (this.serializedNodes.contains(node)) {
/* 158 */       this.emitter.emit(new AliasEvent(tAlias, null, null));
/*     */     } else {
/* 160 */       this.serializedNodes.add(node);
/* 161 */       switch (node.getNodeId()) {
/*     */       case scalar: 
/* 163 */         ScalarNode scalarNode = (ScalarNode)node;
/* 164 */         Tag detectedTag = this.resolver.resolve(NodeId.scalar, scalarNode.getValue(), true);
/* 165 */         Tag defaultTag = this.resolver.resolve(NodeId.scalar, scalarNode.getValue(), false);
/* 166 */         ImplicitTuple tuple = new ImplicitTuple(node.getTag().equals(detectedTag), node.getTag().equals(defaultTag));
/*     */         
/* 168 */         ScalarEvent event = new ScalarEvent(tAlias, node.getTag().getValue(), tuple, scalarNode.getValue(), null, null, scalarNode.getStyle());
/*     */         
/* 170 */         this.emitter.emit(event);
/* 171 */         break;
/*     */       case sequence: 
/* 173 */         SequenceNode seqNode = (SequenceNode)node;
/* 174 */         boolean implicitS = node.getTag().equals(this.resolver.resolve(NodeId.sequence, null, true));
/*     */         
/* 176 */         this.emitter.emit(new SequenceStartEvent(tAlias, node.getTag().getValue(), implicitS, null, null, seqNode.getFlowStyle()));
/*     */         
/* 178 */         List<Node> list = seqNode.getValue();
/* 179 */         for (Node item : list) {
/* 180 */           serializeNode(item, node);
/*     */         }
/* 182 */         this.emitter.emit(new SequenceEndEvent(null, null));
/* 183 */         break;
/*     */       default: 
/* 185 */         Tag implicitTag = this.resolver.resolve(NodeId.mapping, null, true);
/* 186 */         boolean implicitM = node.getTag().equals(implicitTag);
/* 187 */         this.emitter.emit(new MappingStartEvent(tAlias, node.getTag().getValue(), implicitM, null, null, ((CollectionNode)node).getFlowStyle()));
/*     */         
/* 189 */         MappingNode mnode = (MappingNode)node;
/* 190 */         List<NodeTuple> map = mnode.getValue();
/* 191 */         for (NodeTuple row : map) {
/* 192 */           Node key = row.getKeyNode();
/* 193 */           Node value = row.getValueNode();
/* 194 */           serializeNode(key, mnode);
/* 195 */           serializeNode(value, mnode);
/*     */         }
/* 197 */         this.emitter.emit(new MappingEndEvent(null, null));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\serializer\Serializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */