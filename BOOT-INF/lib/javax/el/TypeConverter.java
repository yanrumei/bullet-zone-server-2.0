/*    */ package javax.el;
/*    */ 
/*    */ import java.beans.FeatureDescriptor;
/*    */ import java.util.Iterator;
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
/*    */ public abstract class TypeConverter
/*    */   extends ELResolver
/*    */ {
/*    */   public Object getValue(ELContext context, Object base, Object property)
/*    */   {
/* 29 */     return null;
/*    */   }
/*    */   
/*    */   public Class<?> getType(ELContext context, Object base, Object property)
/*    */   {
/* 34 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void setValue(ELContext context, Object base, Object property, Object value) {}
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean isReadOnly(ELContext context, Object base, Object property)
/*    */   {
/* 45 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base)
/*    */   {
/* 51 */     return null;
/*    */   }
/*    */   
/*    */   public Class<?> getCommonPropertyType(ELContext context, Object base)
/*    */   {
/* 56 */     return null;
/*    */   }
/*    */   
/*    */   public abstract Object convertToType(ELContext paramELContext, Object paramObject, Class<?> paramClass);
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\TypeConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */