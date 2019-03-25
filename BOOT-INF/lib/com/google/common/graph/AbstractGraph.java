/*    */ package com.google.common.graph;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import java.util.Set;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Beta
/*    */ public abstract class AbstractGraph<N>
/*    */   extends AbstractBaseGraph<N>
/*    */   implements Graph<N>
/*    */ {
/*    */   public final boolean equals(@Nullable Object obj)
/*    */   {
/* 37 */     if (obj == this) {
/* 38 */       return true;
/*    */     }
/* 40 */     if (!(obj instanceof Graph)) {
/* 41 */       return false;
/*    */     }
/* 43 */     Graph<?> other = (Graph)obj;
/*    */     
/* 45 */     return (isDirected() == other.isDirected()) && 
/* 46 */       (nodes().equals(other.nodes())) && 
/* 47 */       (edges().equals(other.edges()));
/*    */   }
/*    */   
/*    */   public final int hashCode()
/*    */   {
/* 52 */     return edges().hashCode();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 59 */     String propertiesString = String.format("isDirected: %s, allowsSelfLoops: %s", new Object[] { Boolean.valueOf(isDirected()), Boolean.valueOf(allowsSelfLoops()) });
/* 60 */     return String.format("%s, nodes: %s, edges: %s", new Object[] { propertiesString, nodes(), edges() });
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\graph\AbstractGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */