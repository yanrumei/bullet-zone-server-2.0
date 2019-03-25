/*    */ package org.springframework.jmx.export.assembler;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import javax.management.modelmbean.ModelMBeanNotificationInfo;
/*    */ import org.springframework.jmx.export.metadata.JmxMetadataUtils;
/*    */ import org.springframework.jmx.export.metadata.ManagedNotification;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ public abstract class AbstractConfigurableMBeanInfoAssembler
/*    */   extends AbstractReflectiveMBeanInfoAssembler
/*    */ {
/*    */   private ModelMBeanNotificationInfo[] notificationInfos;
/* 42 */   private final Map<String, ModelMBeanNotificationInfo[]> notificationInfoMappings = new HashMap();
/*    */   
/*    */ 
/*    */   public void setNotificationInfos(ManagedNotification[] notificationInfos)
/*    */   {
/* 47 */     ModelMBeanNotificationInfo[] infos = new ModelMBeanNotificationInfo[notificationInfos.length];
/* 48 */     for (int i = 0; i < notificationInfos.length; i++) {
/* 49 */       ManagedNotification notificationInfo = notificationInfos[i];
/* 50 */       infos[i] = JmxMetadataUtils.convertToModelMBeanNotificationInfo(notificationInfo);
/*    */     }
/* 52 */     this.notificationInfos = infos;
/*    */   }
/*    */   
/*    */   public void setNotificationInfoMappings(Map<String, Object> notificationInfoMappings) {
/* 56 */     for (Map.Entry<String, Object> entry : notificationInfoMappings.entrySet()) {
/* 57 */       this.notificationInfoMappings.put(entry.getKey(), extractNotificationMetadata(entry.getValue()));
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   protected ModelMBeanNotificationInfo[] getNotificationInfo(Object managedBean, String beanKey)
/*    */   {
/* 64 */     ModelMBeanNotificationInfo[] result = null;
/* 65 */     if (StringUtils.hasText(beanKey)) {
/* 66 */       result = (ModelMBeanNotificationInfo[])this.notificationInfoMappings.get(beanKey);
/*    */     }
/* 68 */     if (result == null) {
/* 69 */       result = this.notificationInfos;
/*    */     }
/* 71 */     return result != null ? result : new ModelMBeanNotificationInfo[0];
/*    */   }
/*    */   
/*    */   private ModelMBeanNotificationInfo[] extractNotificationMetadata(Object mapValue) {
/* 75 */     if ((mapValue instanceof ManagedNotification)) {
/* 76 */       ManagedNotification mn = (ManagedNotification)mapValue;
/* 77 */       return new ModelMBeanNotificationInfo[] { JmxMetadataUtils.convertToModelMBeanNotificationInfo(mn) };
/*    */     }
/* 79 */     if ((mapValue instanceof Collection)) {
/* 80 */       Collection<?> col = (Collection)mapValue;
/* 81 */       List<ModelMBeanNotificationInfo> result = new ArrayList();
/* 82 */       for (Object colValue : col) {
/* 83 */         if (!(colValue instanceof ManagedNotification)) {
/* 84 */           throw new IllegalArgumentException("Property 'notificationInfoMappings' only accepts ManagedNotifications for Map values");
/*    */         }
/*    */         
/* 87 */         ManagedNotification mn = (ManagedNotification)colValue;
/* 88 */         result.add(JmxMetadataUtils.convertToModelMBeanNotificationInfo(mn));
/*    */       }
/* 90 */       return (ModelMBeanNotificationInfo[])result.toArray(new ModelMBeanNotificationInfo[result.size()]);
/*    */     }
/*    */     
/* 93 */     throw new IllegalArgumentException("Property 'notificationInfoMappings' only accepts ManagedNotifications for Map values");
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jmx\export\assembler\AbstractConfigurableMBeanInfoAssembler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */