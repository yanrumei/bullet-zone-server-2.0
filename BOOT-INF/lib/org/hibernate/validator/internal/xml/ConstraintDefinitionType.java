/*    */ package org.hibernate.validator.internal.xml;
/*    */ 
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlAttribute;
/*    */ import javax.xml.bind.annotation.XmlElement;
/*    */ import javax.xml.bind.annotation.XmlType;
/*    */ import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
/*    */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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
/*    */ @XmlType(name="constraint-definitionType", propOrder={"validatedBy"})
/*    */ public class ConstraintDefinitionType
/*    */ {
/*    */   @XmlElement(name="validated-by", required=true)
/*    */   protected ValidatedByType validatedBy;
/*    */   @XmlAttribute(name="annotation", required=true)
/*    */   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
/*    */   protected String annotation;
/*    */   
/*    */   public ValidatedByType getValidatedBy()
/*    */   {
/* 61 */     return this.validatedBy;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setValidatedBy(ValidatedByType value)
/*    */   {
/* 73 */     this.validatedBy = value;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getAnnotation()
/*    */   {
/* 85 */     return this.annotation;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setAnnotation(String value)
/*    */   {
/* 97 */     this.annotation = value;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\ConstraintDefinitionType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */