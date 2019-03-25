/*    */ package org.hibernate.validator.internal.xml;
/*    */ 
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlAttribute;
/*    */ import javax.xml.bind.annotation.XmlType;
/*    */ import javax.xml.bind.annotation.XmlValue;
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
/*    */ @XmlAccessorType(XmlAccessType.FIELD)
/*    */ @XmlType(name="propertyType", namespace="http://jboss.org/xml/ns/javax/validation/configuration", propOrder={"value"})
/*    */ public class PropertyType
/*    */ {
/*    */   @XmlValue
/*    */   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
/*    */   protected String value;
/*    */   @XmlAttribute(name="name", required=true)
/*    */   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
/*    */   protected String name;
/*    */   
/*    */   public String getValue()
/*    */   {
/* 59 */     return this.value;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setValue(String value)
/*    */   {
/* 71 */     this.value = value;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getName()
/*    */   {
/* 83 */     return this.name;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setName(String value)
/*    */   {
/* 95 */     this.name = value;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\PropertyType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */