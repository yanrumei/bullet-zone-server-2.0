/*     */ package org.hibernate.validator.internal.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
/*     */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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
/*     */ 
/*     */ @XmlAccessorType(XmlAccessType.FIELD)
/*     */ @XmlType(name="methodType", propOrder={"parameter", "crossParameter", "returnValue"})
/*     */ public class MethodType
/*     */ {
/*     */   protected List<ParameterType> parameter;
/*     */   @XmlElement(name="cross-parameter")
/*     */   protected CrossParameterType crossParameter;
/*     */   @XmlElement(name="return-value")
/*     */   protected ReturnValueType returnValue;
/*     */   @XmlAttribute(name="name", required=true)
/*     */   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
/*     */   protected String name;
/*     */   @XmlAttribute(name="ignore-annotations")
/*     */   protected Boolean ignoreAnnotations;
/*     */   
/*     */   public List<ParameterType> getParameter()
/*     */   {
/*  87 */     if (this.parameter == null) {
/*  88 */       this.parameter = new ArrayList();
/*     */     }
/*  90 */     return this.parameter;
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
/* 102 */     return this.crossParameter;
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
/* 114 */     this.crossParameter = value;
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
/* 126 */     return this.returnValue;
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
/* 138 */     this.returnValue = value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 150 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setName(String value)
/*     */   {
/* 162 */     this.name = value;
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
/* 174 */     return this.ignoreAnnotations;
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
/* 186 */     this.ignoreAnnotations = value;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\MethodType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */