/*    */ package org.springframework.validation;
/*    */ 
/*    */ import java.beans.PropertyEditor;
/*    */ import java.util.Map;
/*    */ import org.springframework.beans.PropertyEditorRegistry;
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
/*    */ public abstract interface BindingResult
/*    */   extends Errors
/*    */ {
/* 50 */   public static final String MODEL_KEY_PREFIX = BindingResult.class.getName() + ".";
/*    */   
/*    */   public abstract Object getTarget();
/*    */   
/*    */   public abstract Map<String, Object> getModel();
/*    */   
/*    */   public abstract Object getRawFieldValue(String paramString);
/*    */   
/*    */   public abstract PropertyEditor findEditor(String paramString, Class<?> paramClass);
/*    */   
/*    */   public abstract PropertyEditorRegistry getPropertyEditorRegistry();
/*    */   
/*    */   public abstract void addError(ObjectError paramObjectError);
/*    */   
/*    */   public abstract String[] resolveMessageCodes(String paramString);
/*    */   
/*    */   public abstract String[] resolveMessageCodes(String paramString1, String paramString2);
/*    */   
/*    */   public abstract void recordSuppressedField(String paramString);
/*    */   
/*    */   public abstract String[] getSuppressedFields();
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\validation\BindingResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */