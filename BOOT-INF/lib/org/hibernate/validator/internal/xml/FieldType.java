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
/*     */ @XmlAccessorType(XmlAccessType.FIELD)
/*     */ @XmlType(name="fieldType", propOrder={"valid", "convertGroup", "constraint"})
/*     */ public class FieldType
/*     */ {
/*     */   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
/*     */   protected String valid;
/*     */   @XmlElement(name="convert-group")
/*     */   protected List<GroupConversionType> convertGroup;
/*     */   protected List<ConstraintType> constraint;
/*     */   @XmlAttribute(name="name", required=true)
/*     */   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
/*     */   protected String name;
/*     */   @XmlAttribute(name="ignore-annotations")
/*     */   protected Boolean ignoreAnnotations;
/*     */   
/*     */   public String getValid()
/*     */   {
/*  73 */     return this.valid;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValid(String value)
/*     */   {
/*  85 */     this.valid = value;
/*     */   }
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
/*     */   public List<GroupConversionType> getConvertGroup()
/*     */   {
/* 111 */     if (this.convertGroup == null) {
/* 112 */       this.convertGroup = new ArrayList();
/*     */     }
/* 114 */     return this.convertGroup;
/*     */   }
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
/*     */   public List<ConstraintType> getConstraint()
/*     */   {
/* 140 */     if (this.constraint == null) {
/* 141 */       this.constraint = new ArrayList();
/*     */     }
/* 143 */     return this.constraint;
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
/* 155 */     return this.name;
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
/* 167 */     this.name = value;
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
/* 179 */     return this.ignoreAnnotations;
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
/* 191 */     this.ignoreAnnotations = value;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\FieldType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */