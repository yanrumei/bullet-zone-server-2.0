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
/*     */ @XmlAccessorType(XmlAccessType.FIELD)
/*     */ @XmlType(name="validation-configType", namespace="http://jboss.org/xml/ns/javax/validation/configuration", propOrder={"defaultProvider", "messageInterpolator", "traversableResolver", "constraintValidatorFactory", "parameterNameProvider", "executableValidation", "constraintMapping", "property"})
/*     */ public class ValidationConfigType
/*     */ {
/*     */   @XmlElement(name="default-provider")
/*     */   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
/*     */   protected String defaultProvider;
/*     */   @XmlElement(name="message-interpolator")
/*     */   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
/*     */   protected String messageInterpolator;
/*     */   @XmlElement(name="traversable-resolver")
/*     */   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
/*     */   protected String traversableResolver;
/*     */   @XmlElement(name="constraint-validator-factory")
/*     */   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
/*     */   protected String constraintValidatorFactory;
/*     */   @XmlElement(name="parameter-name-provider")
/*     */   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
/*     */   protected String parameterNameProvider;
/*     */   @XmlElement(name="executable-validation")
/*     */   protected ExecutableValidationType executableValidation;
/*     */   @XmlElement(name="constraint-mapping")
/*     */   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
/*     */   protected List<String> constraintMapping;
/*     */   protected List<PropertyType> property;
/*     */   @XmlAttribute(name="version", required=true)
/*     */   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
/*     */   protected String version;
/*     */   
/*     */   public String getDefaultProvider()
/*     */   {
/*  95 */     return this.defaultProvider;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDefaultProvider(String value)
/*     */   {
/* 107 */     this.defaultProvider = value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMessageInterpolator()
/*     */   {
/* 119 */     return this.messageInterpolator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMessageInterpolator(String value)
/*     */   {
/* 131 */     this.messageInterpolator = value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getTraversableResolver()
/*     */   {
/* 143 */     return this.traversableResolver;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTraversableResolver(String value)
/*     */   {
/* 155 */     this.traversableResolver = value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getConstraintValidatorFactory()
/*     */   {
/* 167 */     return this.constraintValidatorFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setConstraintValidatorFactory(String value)
/*     */   {
/* 179 */     this.constraintValidatorFactory = value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getParameterNameProvider()
/*     */   {
/* 191 */     return this.parameterNameProvider;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setParameterNameProvider(String value)
/*     */   {
/* 203 */     this.parameterNameProvider = value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ExecutableValidationType getExecutableValidation()
/*     */   {
/* 215 */     return this.executableValidation;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExecutableValidation(ExecutableValidationType value)
/*     */   {
/* 227 */     this.executableValidation = value;
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
/*     */   public List<String> getConstraintMapping()
/*     */   {
/* 253 */     if (this.constraintMapping == null) {
/* 254 */       this.constraintMapping = new ArrayList();
/*     */     }
/* 256 */     return this.constraintMapping;
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
/*     */   public List<PropertyType> getProperty()
/*     */   {
/* 282 */     if (this.property == null) {
/* 283 */       this.property = new ArrayList();
/*     */     }
/* 285 */     return this.property;
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
/* 297 */     if (this.version == null) {
/* 298 */       return "1.1";
/*     */     }
/* 300 */     return this.version;
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
/* 313 */     this.version = value;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\ValidationConfigType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */