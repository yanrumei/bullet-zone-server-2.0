/*     */ package org.hibernate.validator.internal.xml;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElementRefs;
/*     */ import javax.xml.bind.annotation.XmlMixed;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @XmlAccessorType(XmlAccessType.FIELD)
/*     */ @XmlType(name="elementType", propOrder={"content"})
/*     */ public class ElementType
/*     */ {
/*     */   @XmlElementRefs({@javax.xml.bind.annotation.XmlElementRef(name="annotation", namespace="http://jboss.org/xml/ns/javax/validation/mapping", type=javax.xml.bind.JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="value", namespace="http://jboss.org/xml/ns/javax/validation/mapping", type=javax.xml.bind.JAXBElement.class)})
/*     */   @XmlMixed
/*     */   protected List<Serializable> content;
/*     */   @XmlAttribute(name="name", required=true)
/*     */   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
/*     */   protected String name;
/*     */   
/*     */   public List<Serializable> getContent()
/*     */   {
/*  88 */     if (this.content == null) {
/*  89 */       this.content = new ArrayList();
/*     */     }
/*  91 */     return this.content;
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
/* 103 */     return this.name;
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
/* 115 */     this.name = value;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\ElementType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */