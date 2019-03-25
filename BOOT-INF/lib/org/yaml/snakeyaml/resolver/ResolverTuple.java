/*    */ package org.yaml.snakeyaml.resolver;
/*    */ 
/*    */ import java.util.regex.Pattern;
/*    */ import org.yaml.snakeyaml.nodes.Tag;
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
/*    */ final class ResolverTuple
/*    */ {
/*    */   private final Tag tag;
/*    */   private final Pattern regexp;
/*    */   
/*    */   public ResolverTuple(Tag tag, Pattern regexp)
/*    */   {
/* 27 */     this.tag = tag;
/* 28 */     this.regexp = regexp;
/*    */   }
/*    */   
/*    */   public Tag getTag() {
/* 32 */     return this.tag;
/*    */   }
/*    */   
/*    */   public Pattern getRegexp() {
/* 36 */     return this.regexp;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 41 */     return "Tuple tag=" + this.tag + " regexp=" + this.regexp;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\resolver\ResolverTuple.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */