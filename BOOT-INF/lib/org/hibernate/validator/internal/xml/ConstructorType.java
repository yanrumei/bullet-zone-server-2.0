/*     */ package org.hibernate.validator.internal.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @XmlAccessorType(XmlAccessType.FIELD)
/*     */ @XmlType(name="constructorType", propOrder={"parameter", "crossParameter", "returnValue"})
/*     */ public class ConstructorType
/*     */ {
/*     */   protected List<ParameterType> parameter;
/*     */   @XmlElement(name="cross-parameter")
/*     */   protected CrossParameterType crossParameter;
/*     */   @XmlElement(name="return-value")
/*     */   protected ReturnValueType returnValue;
/*     */   @XmlAttribute(name="ignore-annotations")
/*     */   protected Boolean ignoreAnnotations;
/*     */   
/*     */   public List<ParameterType> getParameter()
/*     */   {
/*  81 */     if (this.parameter == null) {
/*  82 */       this.parameter = new ArrayList();
/*     */     }
/*  84 */     return this.parameter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CrossParameterType getCrossParameter()
/*     */   {
/*  96 */     return this.crossParameter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCrossParameter(CrossParameterType value)
/*     */   {
/* 108 */     this.crossParameter = value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ReturnValueType getReturnValue()
/*     */   {
/* 120 */     return this.returnValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setReturnValue(ReturnValueType value)
/*     */   {
/* 132 */     this.returnValue = value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Boolean getIgnoreAnnotations()
/*     */   {
/* 144 */     return this.ignoreAnnotations;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIgnoreAnnotations(Boolean value)
/*     */   {
/* 156 */     this.ignoreAnnotations = value;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\ConstructorType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */