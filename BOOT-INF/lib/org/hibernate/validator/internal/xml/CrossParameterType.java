/*    */ package org.hibernate.validator.internal.xml;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlAttribute;
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
/*    */ @XmlType(name="crossParameterType", propOrder={"constraint"})
/*    */ public class CrossParameterType
/*    */ {
/*    */   protected List<ConstraintType> constraint;
/*    */   @XmlAttribute(name="ignore-annotations")
/*    */   protected Boolean ignoreAnnotations;
/*    */   
/*    */   public List<ConstraintType> getConstraint()
/*    */   {
/* 72 */     if (this.constraint == null) {
/* 73 */       this.constraint = new ArrayList();
/*    */     }
/* 75 */     return this.constraint;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Boolean getIgnoreAnnotations()
/*    */   {
/* 87 */     return this.ignoreAnnotations;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setIgnoreAnnotations(Boolean value)
/*    */   {
/* 99 */     this.ignoreAnnotations = value;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\CrossParameterType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */