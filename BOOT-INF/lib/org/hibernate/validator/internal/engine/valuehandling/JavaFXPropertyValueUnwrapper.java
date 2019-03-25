/*    */ package org.hibernate.validator.internal.engine.valuehandling;
/*    */ 
/*    */ import javafx.beans.value.ObservableValue;
/*    */ import org.hibernate.validator.internal.util.IgnoreJava6Requirement;
/*    */ import org.hibernate.validator.internal.util.TypeResolutionHelper;
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
/*    */ @IgnoreJava6Requirement
/*    */ public class JavaFXPropertyValueUnwrapper
/*    */   extends TypeResolverBasedValueUnwrapper<ObservableValue<?>>
/*    */ {
/*    */   public JavaFXPropertyValueUnwrapper(TypeResolutionHelper typeResolutionHelper)
/*    */   {
/* 23 */     super(typeResolutionHelper);
/*    */   }
/*    */   
/*    */   public Object handleValidatedValue(ObservableValue<?> value)
/*    */   {
/* 28 */     if (value != null) {
/* 29 */       return value.getValue();
/*    */     }
/* 31 */     return value;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\valuehandling\JavaFXPropertyValueUnwrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */