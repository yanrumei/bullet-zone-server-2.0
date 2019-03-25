/*    */ package org.springframework.validation.support;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import org.springframework.ui.ExtendedModelMap;
/*    */ import org.springframework.validation.BindingResult;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BindingAwareModelMap
/*    */   extends ExtendedModelMap
/*    */ {
/*    */   public Object put(String key, Object value)
/*    */   {
/* 43 */     removeBindingResultIfNecessary(key, value);
/* 44 */     return super.put(key, value);
/*    */   }
/*    */   
/*    */   public void putAll(Map<? extends String, ?> map)
/*    */   {
/* 49 */     for (Map.Entry<? extends String, ?> entry : map.entrySet()) {
/* 50 */       removeBindingResultIfNecessary(entry.getKey(), entry.getValue());
/*    */     }
/* 52 */     super.putAll(map);
/*    */   }
/*    */   
/*    */   private void removeBindingResultIfNecessary(Object key, Object value) {
/* 56 */     if ((key instanceof String)) {
/* 57 */       String attributeName = (String)key;
/* 58 */       if (!attributeName.startsWith(BindingResult.MODEL_KEY_PREFIX)) {
/* 59 */         String bindingResultKey = BindingResult.MODEL_KEY_PREFIX + attributeName;
/* 60 */         BindingResult bindingResult = (BindingResult)get(bindingResultKey);
/* 61 */         if ((bindingResult != null) && (bindingResult.getTarget() != value)) {
/* 62 */           remove(bindingResultKey);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\validation\support\BindingAwareModelMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */