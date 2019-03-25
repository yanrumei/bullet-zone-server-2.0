/*     */ package org.apache.el.parser;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import javax.el.ELException;
/*     */ import javax.el.MethodInfo;
/*     */ import javax.el.PropertyNotWritableException;
/*     */ import javax.el.ValueReference;
/*     */ import org.apache.el.lang.ELSupport;
/*     */ import org.apache.el.lang.EvaluationContext;
/*     */ import org.apache.el.util.MessageFactory;
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
/*     */ 
/*     */ public abstract class SimpleNode
/*     */   extends ELSupport
/*     */   implements Node
/*     */ {
/*     */   protected Node parent;
/*     */   protected Node[] children;
/*     */   protected final int id;
/*     */   protected String image;
/*     */   
/*     */   public SimpleNode(int i)
/*     */   {
/*  46 */     this.id = i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void jjtOpen() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void jjtClose() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void jjtSetParent(Node n)
/*     */   {
/*  61 */     this.parent = n;
/*     */   }
/*     */   
/*     */   public Node jjtGetParent()
/*     */   {
/*  66 */     return this.parent;
/*     */   }
/*     */   
/*     */   public void jjtAddChild(Node n, int i)
/*     */   {
/*  71 */     if (this.children == null) {
/*  72 */       this.children = new Node[i + 1];
/*  73 */     } else if (i >= this.children.length) {
/*  74 */       Node[] c = new Node[i + 1];
/*  75 */       System.arraycopy(this.children, 0, c, 0, this.children.length);
/*  76 */       this.children = c;
/*     */     }
/*  78 */     this.children[i] = n;
/*     */   }
/*     */   
/*     */   public Node jjtGetChild(int i)
/*     */   {
/*  83 */     return this.children[i];
/*     */   }
/*     */   
/*     */   public int jjtGetNumChildren()
/*     */   {
/*  88 */     return this.children == null ? 0 : this.children.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 100 */     if (this.image != null) {
/* 101 */       return ELParserTreeConstants.jjtNodeName[this.id] + "[" + this.image + "]";
/*     */     }
/*     */     
/* 104 */     return ELParserTreeConstants.jjtNodeName[this.id];
/*     */   }
/*     */   
/*     */   public String getImage()
/*     */   {
/* 109 */     return this.image;
/*     */   }
/*     */   
/*     */   public void setImage(String image) {
/* 113 */     this.image = image;
/*     */   }
/*     */   
/*     */   public Class<?> getType(EvaluationContext ctx)
/*     */     throws ELException
/*     */   {
/* 119 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Object getValue(EvaluationContext ctx)
/*     */     throws ELException
/*     */   {
/* 125 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isReadOnly(EvaluationContext ctx)
/*     */     throws ELException
/*     */   {
/* 131 */     return true;
/*     */   }
/*     */   
/*     */   public void setValue(EvaluationContext ctx, Object value)
/*     */     throws ELException
/*     */   {
/* 137 */     throw new PropertyNotWritableException(MessageFactory.get("error.syntax.set"));
/*     */   }
/*     */   
/*     */   public void accept(NodeVisitor visitor) throws Exception
/*     */   {
/* 142 */     visitor.visit(this);
/* 143 */     if ((this.children != null) && (this.children.length > 0)) {
/* 144 */       for (int i = 0; i < this.children.length; i++) {
/* 145 */         this.children[i].accept(visitor);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Object invoke(EvaluationContext ctx, Class<?>[] paramTypes, Object[] paramValues)
/*     */     throws ELException
/*     */   {
/* 153 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public MethodInfo getMethodInfo(EvaluationContext ctx, Class<?>[] paramTypes)
/*     */     throws ELException
/*     */   {
/* 159 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 165 */     int prime = 31;
/* 166 */     int result = 1;
/* 167 */     result = 31 * result + Arrays.hashCode(this.children);
/* 168 */     result = 31 * result + this.id;
/* 169 */     result = 31 * result + (this.image == null ? 0 : this.image.hashCode());
/* 170 */     return result;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 175 */     if (this == obj) {
/* 176 */       return true;
/*     */     }
/* 178 */     if (!(obj instanceof SimpleNode)) {
/* 179 */       return false;
/*     */     }
/* 181 */     SimpleNode other = (SimpleNode)obj;
/* 182 */     if (!Arrays.equals(this.children, other.children)) {
/* 183 */       return false;
/*     */     }
/* 185 */     if (this.id != other.id) {
/* 186 */       return false;
/*     */     }
/* 188 */     if (this.image == null) {
/* 189 */       if (other.image != null) {
/* 190 */         return false;
/*     */       }
/* 192 */     } else if (!this.image.equals(other.image)) {
/* 193 */       return false;
/*     */     }
/* 195 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ValueReference getValueReference(EvaluationContext ctx)
/*     */   {
/* 203 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isParametersProvided()
/*     */   {
/* 211 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\SimpleNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */