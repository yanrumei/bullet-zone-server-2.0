/*    */ package org.springframework.boot.autoconfigure.jdbc.metadata;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DataSourcePoolMetadataProviders
/*    */   implements DataSourcePoolMetadataProvider
/*    */ {
/*    */   private final List<DataSourcePoolMetadataProvider> providers;
/*    */   
/*    */   public DataSourcePoolMetadataProviders(Collection<? extends DataSourcePoolMetadataProvider> providers)
/*    */   {
/* 45 */     this.providers = (providers == null ? Collections.emptyList() : new ArrayList(providers));
/*    */   }
/*    */   
/*    */ 
/*    */   public DataSourcePoolMetadata getDataSourcePoolMetadata(DataSource dataSource)
/*    */   {
/* 51 */     for (DataSourcePoolMetadataProvider provider : this.providers)
/*    */     {
/* 53 */       DataSourcePoolMetadata metadata = provider.getDataSourcePoolMetadata(dataSource);
/* 54 */       if (metadata != null) {
/* 55 */         return metadata;
/*    */       }
/*    */     }
/* 58 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jdbc\metadata\DataSourcePoolMetadataProviders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */