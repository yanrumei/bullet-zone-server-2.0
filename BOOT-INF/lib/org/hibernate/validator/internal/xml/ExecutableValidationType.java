/*    */ package org.hibernate.validator.internal.xml;
/*    */ 
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlAttribute;
/*    */ import javax.xml.bind.annotation.XmlElement;
/*    */ import javax.xml.bind.annotation.XmlType;
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
/*    */ @XmlAccessorType(XmlAccessType.FIELD)
/*    */ @XmlType(name="executable-validationType", namespace="http://jboss.org/xml/ns/javax/validation/configuration", propOrder={"defaultValidatedExecutableTypes"})
/*    */ public class ExecutableValidationType
/*    */ {
/*    */   @XmlElement(name="default-validated-executable-types")
/*    */   protected DefaultValidatedExecutableTypesType defaultValidatedExecutableTypes;
/*    */   @XmlAttribute(name="enabled")
/*    */   protected Boolean enabled;
/*    */   
/*    */   public DefaultValidatedExecutableTypesType getDefaultValidatedExecutableTypes()
/*    */   {
/* 58 */     return this.defaultValidatedExecutableTypes;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setDefaultValidatedExecutableTypes(DefaultValidatedExecutableTypesType value)
/*    */   {
/* 70 */     this.defaultValidatedExecutableTypes = value;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Boolean getEnabled()
/*    */   {
/* 82 */     if (this.enabled == null) {
/* 83 */       return Boolean.valueOf(true);
/*    */     }
/* 85 */     return this.enabled;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setEnabled(Boolean value)
/*    */   {
/* 98 */     this.enabled = value;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\ExecutableValidationType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */