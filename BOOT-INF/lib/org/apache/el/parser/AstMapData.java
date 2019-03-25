/*    */ package org.apache.el.parser;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import javax.el.ELException;
/*    */ import org.apache.el.lang.EvaluationContext;
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
/*    */ public class AstMapData
/*    */   extends SimpleNode
/*    */ {
/*    */   public AstMapData(int id)
/*    */   {
/* 30 */     super(id);
/*    */   }
/*    */   
/*    */   public Object getValue(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 36 */     Map<Object, Object> result = new HashMap();
/*    */     
/* 38 */     if (this.children != null) {
/* 39 */       for (Node child : this.children) {
/* 40 */         AstMapEntry mapEntry = (AstMapEntry)child;
/* 41 */         Object key = mapEntry.children[0].getValue(ctx);
/* 42 */         Object value = mapEntry.children[1].getValue(ctx);
/* 43 */         result.put(key, value);
/*    */       }
/*    */     }
/*    */     
/* 47 */     return result;
/*    */   }
/*    */   
/*    */   public Class<?> getType(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 53 */     return Map.class;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstMapData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */