/*    */ package org.apache.el.lang;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import javax.el.FunctionMapper;
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
/*    */ public class FunctionMapperFactory
/*    */   extends FunctionMapper
/*    */ {
/* 29 */   protected FunctionMapperImpl memento = null;
/*    */   protected final FunctionMapper target;
/*    */   
/*    */   public FunctionMapperFactory(FunctionMapper mapper) {
/* 33 */     if (mapper == null) {
/* 34 */       throw new NullPointerException("FunctionMapper target cannot be null");
/*    */     }
/* 36 */     this.target = mapper;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Method resolveFunction(String prefix, String localName)
/*    */   {
/* 45 */     if (this.memento == null) {
/* 46 */       this.memento = new FunctionMapperImpl();
/*    */     }
/* 48 */     Method m = this.target.resolveFunction(prefix, localName);
/* 49 */     if (m != null) {
/* 50 */       this.memento.mapFunction(prefix, localName, m);
/*    */     }
/* 52 */     return m;
/*    */   }
/*    */   
/*    */ 
/*    */   public void mapFunction(String prefix, String localName, Method method)
/*    */   {
/* 58 */     if (this.memento == null) {
/* 59 */       this.memento = new FunctionMapperImpl();
/*    */     }
/* 61 */     this.memento.mapFunction(prefix, localName, method);
/*    */   }
/*    */   
/*    */   public FunctionMapper create()
/*    */   {
/* 66 */     return this.memento;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\lang\FunctionMapperFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */