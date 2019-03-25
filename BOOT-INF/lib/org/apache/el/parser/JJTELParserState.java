/*     */ package org.apache.el.parser;
/*     */ 
/*     */ import java.util.List;
/*     */ 
/*     */ public class JJTELParserState
/*     */ {
/*     */   private List<Node> nodes;
/*     */   private List<Integer> marks;
/*     */   private int sp;
/*     */   private int mk;
/*     */   private boolean node_created;
/*     */   
/*     */   public JJTELParserState() {
/*  14 */     this.nodes = new java.util.ArrayList();
/*  15 */     this.marks = new java.util.ArrayList();
/*  16 */     this.sp = 0;
/*  17 */     this.mk = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean nodeCreated()
/*     */   {
/*  24 */     return this.node_created;
/*     */   }
/*     */   
/*     */ 
/*     */   public void reset()
/*     */   {
/*  30 */     this.nodes.clear();
/*  31 */     this.marks.clear();
/*  32 */     this.sp = 0;
/*  33 */     this.mk = 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public Node rootNode()
/*     */   {
/*  39 */     return (Node)this.nodes.get(0);
/*     */   }
/*     */   
/*     */   public void pushNode(Node n)
/*     */   {
/*  44 */     this.nodes.add(n);
/*  45 */     this.sp += 1;
/*     */   }
/*     */   
/*     */ 
/*     */   public Node popNode()
/*     */   {
/*  51 */     if (--this.sp < this.mk) {
/*  52 */       this.mk = ((Integer)this.marks.remove(this.marks.size() - 1)).intValue();
/*     */     }
/*  54 */     return (Node)this.nodes.remove(this.nodes.size() - 1);
/*     */   }
/*     */   
/*     */   public Node peekNode()
/*     */   {
/*  59 */     return (Node)this.nodes.get(this.nodes.size() - 1);
/*     */   }
/*     */   
/*     */ 
/*     */   public int nodeArity()
/*     */   {
/*  65 */     return this.sp - this.mk;
/*     */   }
/*     */   
/*     */   public void clearNodeScope(Node n)
/*     */   {
/*  70 */     while (this.sp > this.mk) {
/*  71 */       popNode();
/*     */     }
/*  73 */     this.mk = ((Integer)this.marks.remove(this.marks.size() - 1)).intValue();
/*     */   }
/*     */   
/*     */   public void openNodeScope(Node n)
/*     */   {
/*  78 */     this.marks.add(Integer.valueOf(this.mk));
/*  79 */     this.mk = this.sp;
/*  80 */     n.jjtOpen();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void closeNodeScope(Node n, int num)
/*     */   {
/*  89 */     this.mk = ((Integer)this.marks.remove(this.marks.size() - 1)).intValue();
/*  90 */     while (num-- > 0) {
/*  91 */       Node c = popNode();
/*  92 */       c.jjtSetParent(n);
/*  93 */       n.jjtAddChild(c, num);
/*     */     }
/*  95 */     n.jjtClose();
/*  96 */     pushNode(n);
/*  97 */     this.node_created = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void closeNodeScope(Node n, boolean condition)
/*     */   {
/* 107 */     if (condition) {
/* 108 */       int a = nodeArity();
/* 109 */       this.mk = ((Integer)this.marks.remove(this.marks.size() - 1)).intValue();
/* 110 */       while (a-- > 0) {
/* 111 */         Node c = popNode();
/* 112 */         c.jjtSetParent(n);
/* 113 */         n.jjtAddChild(c, a);
/*     */       }
/* 115 */       n.jjtClose();
/* 116 */       pushNode(n);
/* 117 */       this.node_created = true;
/*     */     } else {
/* 119 */       this.mk = ((Integer)this.marks.remove(this.marks.size() - 1)).intValue();
/* 120 */       this.node_created = false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\JJTELParserState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */