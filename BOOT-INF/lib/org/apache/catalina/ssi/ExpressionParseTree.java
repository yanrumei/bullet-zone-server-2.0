/*     */ package org.apache.catalina.ssi;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.regex.PatternSyntaxException;
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
/*     */ public class ExpressionParseTree
/*     */ {
/*  35 */   private final LinkedList<Node> nodeStack = new LinkedList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  40 */   private final LinkedList<OppNode> oppStack = new LinkedList();
/*     */   
/*     */ 
/*     */   private Node root;
/*     */   
/*     */ 
/*     */   private final SSIMediator ssiMediator;
/*     */   
/*     */ 
/*     */   private static final int PRECEDENCE_NOT = 5;
/*     */   
/*     */   private static final int PRECEDENCE_COMPARE = 4;
/*     */   
/*     */   private static final int PRECEDENCE_LOGICAL = 1;
/*     */   
/*     */ 
/*     */   public ExpressionParseTree(String expr, SSIMediator ssiMediator)
/*     */     throws ParseException
/*     */   {
/*  59 */     this.ssiMediator = ssiMediator;
/*  60 */     parseExpression(expr);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean evaluateTree()
/*     */   {
/*  70 */     return this.root.evaluate();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void pushOpp(OppNode node)
/*     */   {
/*  81 */     if (node == null) {
/*  82 */       this.oppStack.add(0, node);
/*  83 */       return;
/*     */     }
/*     */     
/*  86 */     while (this.oppStack.size() != 0) {
/*  87 */       OppNode top = (OppNode)this.oppStack.get(0);
/*     */       
/*     */ 
/*  90 */       if (top == null) {
/*     */         break;
/*     */       }
/*  93 */       if (top.getPrecedence() < node.getPrecedence())
/*     */         break;
/*  95 */       this.oppStack.remove(0);
/*     */       
/*  97 */       top.popValues(this.nodeStack);
/*     */       
/*  99 */       this.nodeStack.add(0, top);
/*     */     }
/*     */     
/* 102 */     this.oppStack.add(0, node);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void resolveGroup()
/*     */   {
/* 111 */     OppNode top = null;
/* 112 */     while ((top = (OppNode)this.oppStack.remove(0)) != null)
/*     */     {
/* 114 */       top.popValues(this.nodeStack);
/*     */       
/* 116 */       this.nodeStack.add(0, top);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void parseExpression(String expr)
/*     */     throws ParseException
/*     */   {
/* 127 */     StringNode currStringNode = null;
/*     */     
/*     */ 
/* 130 */     pushOpp(null);
/* 131 */     ExpressionTokenizer et = new ExpressionTokenizer(expr);
/* 132 */     while (et.hasMoreTokens()) {
/* 133 */       int token = et.nextToken();
/* 134 */       if (token != 0)
/* 135 */         currStringNode = null;
/* 136 */       switch (token) {
/*     */       case 0: 
/* 138 */         if (currStringNode == null) {
/* 139 */           currStringNode = new StringNode(et.getTokenValue());
/* 140 */           this.nodeStack.add(0, currStringNode);
/*     */         }
/*     */         else {
/* 143 */           currStringNode.value.append(" ");
/* 144 */           currStringNode.value.append(et.getTokenValue());
/*     */         }
/* 146 */         break;
/*     */       case 1: 
/* 148 */         pushOpp(new AndNode(null));
/* 149 */         break;
/*     */       case 2: 
/* 151 */         pushOpp(new OrNode(null));
/* 152 */         break;
/*     */       case 3: 
/* 154 */         pushOpp(new NotNode(null));
/* 155 */         break;
/*     */       case 4: 
/* 157 */         pushOpp(new EqualNode(null));
/* 158 */         break;
/*     */       case 5: 
/* 160 */         pushOpp(new NotNode(null));
/*     */         
/*     */ 
/* 163 */         this.oppStack.add(0, new EqualNode(null));
/* 164 */         break;
/*     */       
/*     */       case 6: 
/* 167 */         resolveGroup();
/* 168 */         break;
/*     */       
/*     */       case 7: 
/* 171 */         pushOpp(null);
/* 172 */         break;
/*     */       case 8: 
/* 174 */         pushOpp(new NotNode(null));
/*     */         
/*     */ 
/* 177 */         this.oppStack.add(0, new LessThanNode(null));
/* 178 */         break;
/*     */       case 9: 
/* 180 */         pushOpp(new NotNode(null));
/*     */         
/*     */ 
/* 183 */         this.oppStack.add(0, new GreaterThanNode(null));
/* 184 */         break;
/*     */       case 10: 
/* 186 */         pushOpp(new GreaterThanNode(null));
/* 187 */         break;
/*     */       case 11: 
/* 189 */         pushOpp(new LessThanNode(null));
/*     */       }
/*     */       
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 196 */     resolveGroup();
/* 197 */     if (this.nodeStack.size() == 0) {
/* 198 */       throw new ParseException("No nodes created.", et.getIndex());
/*     */     }
/* 200 */     if (this.nodeStack.size() > 1) {
/* 201 */       throw new ParseException("Extra nodes created.", et.getIndex());
/*     */     }
/* 203 */     if (this.oppStack.size() != 0) {
/* 204 */       throw new ParseException("Unused opp nodes exist.", et.getIndex());
/*     */     }
/* 206 */     this.root = ((Node)this.nodeStack.get(0));
/*     */   }
/*     */   
/*     */ 
/*     */   private abstract class Node
/*     */   {
/*     */     private Node() {}
/*     */     
/*     */ 
/*     */     public abstract boolean evaluate();
/*     */   }
/*     */   
/*     */ 
/*     */   private class StringNode
/*     */     extends ExpressionParseTree.Node
/*     */   {
/*     */     StringBuilder value;
/* 223 */     String resolved = null;
/*     */     
/*     */     public StringNode(String value) {
/* 226 */       super(null);
/* 227 */       this.value = new StringBuilder(value);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getValue()
/*     */     {
/* 237 */       if (this.resolved == null)
/* 238 */         this.resolved = ExpressionParseTree.this.ssiMediator.substituteVariables(this.value.toString());
/* 239 */       return this.resolved;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean evaluate()
/*     */     {
/* 248 */       return getValue().length() != 0;
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 254 */       return this.value.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private abstract class OppNode extends ExpressionParseTree.Node
/*     */   {
/*     */     ExpressionParseTree.Node left;
/*     */     ExpressionParseTree.Node right;
/*     */     
/*     */     private OppNode()
/*     */     {
/* 265 */       super(null);
/*     */     }
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
/*     */     public abstract int getPrecedence();
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
/*     */     public void popValues(List<ExpressionParseTree.Node> values)
/*     */     {
/* 290 */       this.right = ((ExpressionParseTree.Node)values.remove(0));
/* 291 */       this.left = ((ExpressionParseTree.Node)values.remove(0));
/*     */     } }
/*     */   
/* 294 */   private final class NotNode extends ExpressionParseTree.OppNode { private NotNode() { super(null); }
/*     */     
/*     */     public boolean evaluate() {
/* 297 */       return !this.left.evaluate();
/*     */     }
/*     */     
/*     */ 
/*     */     public int getPrecedence()
/*     */     {
/* 303 */       return 5;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void popValues(List<ExpressionParseTree.Node> values)
/*     */     {
/* 312 */       this.left = ((ExpressionParseTree.Node)values.remove(0));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 318 */     public String toString() { return this.left + " NOT"; }
/*     */   }
/*     */   
/* 321 */   private final class AndNode extends ExpressionParseTree.OppNode { private AndNode() { super(null); }
/*     */     
/*     */     public boolean evaluate() {
/* 324 */       if (!this.left.evaluate())
/* 325 */         return false;
/* 326 */       return this.right.evaluate();
/*     */     }
/*     */     
/*     */ 
/*     */     public int getPrecedence()
/*     */     {
/* 332 */       return 1;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 338 */     public String toString() { return this.left + " " + this.right + " AND"; }
/*     */   }
/*     */   
/* 341 */   private final class OrNode extends ExpressionParseTree.OppNode { private OrNode() { super(null); }
/*     */     
/*     */     public boolean evaluate() {
/* 344 */       if (this.left.evaluate())
/* 345 */         return true;
/* 346 */       return this.right.evaluate();
/*     */     }
/*     */     
/*     */ 
/*     */     public int getPrecedence()
/*     */     {
/* 352 */       return 1;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 358 */     public String toString() { return this.left + " " + this.right + " OR"; }
/*     */   }
/*     */   
/* 361 */   private abstract class CompareNode extends ExpressionParseTree.OppNode { private CompareNode() { super(null); }
/*     */     
/* 363 */     protected int compareBranches() { String val1 = ((ExpressionParseTree.StringNode)this.left).getValue();
/* 364 */       String val2 = ((ExpressionParseTree.StringNode)this.right).getValue();
/*     */       
/* 366 */       int val2Len = val2.length();
/* 367 */       if ((val2Len > 1) && (val2.charAt(0) == '/') && 
/* 368 */         (val2.charAt(val2Len - 1) == '/'))
/*     */       {
/* 370 */         String expr = val2.substring(1, val2Len - 1);
/*     */         try {
/* 372 */           Pattern pattern = Pattern.compile(expr);
/*     */           
/*     */ 
/* 375 */           if (pattern.matcher(val1).find()) {
/* 376 */             return 0;
/*     */           }
/* 378 */           return -1;
/*     */         }
/*     */         catch (PatternSyntaxException pse) {
/* 381 */           ExpressionParseTree.this.ssiMediator.log("Invalid expression: " + expr, pse);
/* 382 */           return 0;
/*     */         }
/*     */       }
/* 385 */       return val1.compareTo(val2);
/*     */     } }
/*     */   
/* 388 */   private final class EqualNode extends ExpressionParseTree.CompareNode { private EqualNode() { super(null); }
/*     */     
/*     */     public boolean evaluate() {
/* 391 */       return compareBranches() == 0;
/*     */     }
/*     */     
/*     */ 
/*     */     public int getPrecedence()
/*     */     {
/* 397 */       return 4;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 403 */     public String toString() { return this.left + " " + this.right + " EQ"; }
/*     */   }
/*     */   
/* 406 */   private final class GreaterThanNode extends ExpressionParseTree.CompareNode { private GreaterThanNode() { super(null); }
/*     */     
/*     */     public boolean evaluate() {
/* 409 */       return compareBranches() > 0;
/*     */     }
/*     */     
/*     */ 
/*     */     public int getPrecedence()
/*     */     {
/* 415 */       return 4;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 421 */     public String toString() { return this.left + " " + this.right + " GT"; }
/*     */   }
/*     */   
/* 424 */   private final class LessThanNode extends ExpressionParseTree.CompareNode { private LessThanNode() { super(null); }
/*     */     
/*     */     public boolean evaluate() {
/* 427 */       return compareBranches() < 0;
/*     */     }
/*     */     
/*     */ 
/*     */     public int getPrecedence()
/*     */     {
/* 433 */       return 4;
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 439 */       return this.left + " " + this.right + " LT";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\ssi\ExpressionParseTree.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */