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
/*     */ @XmlAccessorType(XmlAccessType.FIELD)
/*     */ @XmlType(name="constraint-mappingsType", propOrder={"defaultPackage", "bean", "constraintDefinition"})
/*     */ public class ConstraintMappingsType
/*     */ {
/*     */   @XmlElement(name="default-package")
/*     */   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
/*     */   protected String defaultPackage;
/*     */   protected List<BeanType> bean;
/*     */   @XmlElement(name="constraint-definition")
/*     */   protected List<ConstraintDefinitionType> constraintDefinition;
/*     */   @XmlAttribute(name="version", required=true)
/*     */   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
/*     */   protected String version;
/*     */   
/*     */   public String getDefaultPackage()
/*     */   {
/*  70 */     return this.defaultPackage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDefaultPackage(String value)
/*     */   {
/*  82 */     this.defaultPackage = value;
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
/*     */   public List<BeanType> getBean()
/*     */   {
/* 108 */     if (this.bean == null) {
/* 109 */       this.bean = new ArrayList();
/*     */     }
/* 111 */     return this.bean;
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
/*     */   public List<ConstraintDefinitionType> getConstraintDefinition()
/*     */   {
/* 137 */     if (this.constraintDefinition == null) {
/* 138 */       this.constraintDefinition = new ArrayList();
/*     */     }
/* 140 */     return this.constraintDefinition;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getVersion()
/*     */   {
/* 152 */     if (this.version == null) {
/* 153 */       return "1.1";
/*     */     }
/* 155 */     return this.version;
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
/*     */   public void setVersion(String value)
/*     */   {
/* 168 */     this.version = value;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\ConstraintMappingsType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */