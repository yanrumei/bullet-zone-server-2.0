/*    */ package org.apache.el.parser;
/*    */ 
/*    */ import java.math.BigDecimal;
/*    */ import java.math.BigInteger;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class AstNegative
/*    */   extends SimpleNode
/*    */ {
/*    */   public AstNegative(int id)
/*    */   {
/* 34 */     super(id);
/*    */   }
/*    */   
/*    */   public Class<?> getType(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 40 */     return Number.class;
/*    */   }
/*    */   
/*    */   public Object getValue(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 46 */     Object obj = this.children[0].getValue(ctx);
/*    */     
/* 48 */     if (obj == null) {
/* 49 */       return Long.valueOf(0L);
/*    */     }
/* 51 */     if ((obj instanceof BigDecimal)) {
/* 52 */       return ((BigDecimal)obj).negate();
/*    */     }
/* 54 */     if ((obj instanceof BigInteger)) {
/* 55 */       return ((BigInteger)obj).negate();
/*    */     }
/* 57 */     if ((obj instanceof String)) {
/* 58 */       if (isStringFloat((String)obj)) {
/* 59 */         return Double.valueOf(-Double.parseDouble((String)obj));
/*    */       }
/* 61 */       return Long.valueOf(-Long.parseLong((String)obj));
/*    */     }
/* 63 */     if ((obj instanceof Long)) {
/* 64 */       return Long.valueOf(-((Long)obj).longValue());
/*    */     }
/* 66 */     if ((obj instanceof Double)) {
/* 67 */       return Double.valueOf(-((Double)obj).doubleValue());
/*    */     }
/* 69 */     if ((obj instanceof Integer)) {
/* 70 */       return Integer.valueOf(-((Integer)obj).intValue());
/*    */     }
/* 72 */     if ((obj instanceof Float)) {
/* 73 */       return Float.valueOf(-((Float)obj).floatValue());
/*    */     }
/* 75 */     if ((obj instanceof Short)) {
/* 76 */       return Short.valueOf((short)-((Short)obj).shortValue());
/*    */     }
/* 78 */     if ((obj instanceof Byte)) {
/* 79 */       return Byte.valueOf((byte)-((Byte)obj).byteValue());
/*    */     }
/* 81 */     Long num = (Long)coerceToNumber(ctx, obj, Long.class);
/* 82 */     return Long.valueOf(-num.longValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstNegative.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */