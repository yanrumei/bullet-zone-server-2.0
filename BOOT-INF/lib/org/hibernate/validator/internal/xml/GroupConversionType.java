/*    */ package org.hibernate.validator.internal.xml;
/*    */ 
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlAttribute;
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
/*    */ @XmlAccessorType(XmlAccessType.FIELD)
/*    */ @XmlType(name="groupConversionType")
/*    */ public class GroupConversionType
/*    */ {
/*    */   @XmlAttribute(name="from", required=true)
/*    */   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
/*    */   protected String from;
/*    */   @XmlAttribute(name="to", required=true)
/*    */   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
/*    */   protected String to;
/*    */   
/*    */   public String getFrom()
/*    */   {
/* 57 */     return this.from;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setFrom(String value)
/*    */   {
/* 69 */     this.from = value;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getTo()
/*    */   {
/* 81 */     return this.to;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setTo(String value)
/*    */   {
/* 93 */     this.to = value;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\GroupConversionType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */