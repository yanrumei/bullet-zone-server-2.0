/*    */ package org.springframework.boot.autoconfigure.jdbc.metadata;
/*    */ 
/*    */ import javax.sql.DataSource;
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
/*    */ public abstract class AbstractDataSourcePoolMetadata<T extends DataSource>
/*    */   implements DataSourcePoolMetadata
/*    */ {
/*    */   private final T dataSource;
/*    */   
/*    */   protected AbstractDataSourcePoolMetadata(T dataSource)
/*    */   {
/* 38 */     this.dataSource = dataSource;
/*    */   }
/*    */   
/*    */   public Float getUsage()
/*    */   {
/* 43 */     Integer maxSize = getMax();
/* 44 */     Integer currentSize = getActive();
/* 45 */     if ((maxSize == null) || (currentSize == null)) {
/* 46 */       return null;
/*    */     }
/* 48 */     if (maxSize.intValue() < 0) {
/* 49 */       return Float.valueOf(-1.0F);
/*    */     }
/* 51 */     if (currentSize.intValue() == 0) {
/* 52 */       return Float.valueOf(0.0F);
/*    */     }
/* 54 */     return Float.valueOf(currentSize.intValue() / maxSize.intValue());
/*    */   }
/*    */   
/*    */   protected final T getDataSource() {
/* 58 */     return this.dataSource;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jdbc\metadata\AbstractDataSourcePoolMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */