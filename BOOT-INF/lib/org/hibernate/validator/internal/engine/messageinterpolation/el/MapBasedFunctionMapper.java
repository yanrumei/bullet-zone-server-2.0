/*    */ package org.hibernate.validator.internal.engine.messageinterpolation.el;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import javax.el.FunctionMapper;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MapBasedFunctionMapper
/*    */   extends FunctionMapper
/*    */ {
/*    */   private static final String FUNCTION_NAME_SEPARATOR = ":";
/* 20 */   private Map<String, Method> map = Collections.emptyMap();
/*    */   
/*    */   public Method resolveFunction(String prefix, String localName)
/*    */   {
/* 24 */     return (Method)this.map.get(prefix + ":" + localName);
/*    */   }
/*    */   
/*    */   public void setFunction(String prefix, String localName, Method method) {
/* 28 */     if (this.map.isEmpty()) {
/* 29 */       this.map = new HashMap();
/*    */     }
/* 31 */     this.map.put(prefix + ":" + localName, method);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\messageinterpolation\el\MapBasedFunctionMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */