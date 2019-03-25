/*     */ package org.apache.tomcat.util.descriptor.tld;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.List;
/*     */ import javax.servlet.jsp.tagext.TagAttributeInfo;
/*     */ import javax.servlet.jsp.tagext.TagVariableInfo;
/*     */ import org.apache.tomcat.util.digester.Digester;
/*     */ import org.apache.tomcat.util.digester.Rule;
/*     */ import org.apache.tomcat.util.digester.RuleSetBase;
/*     */ import org.xml.sax.Attributes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TldRuleSet
/*     */   extends RuleSetBase
/*     */ {
/*     */   private static final String PREFIX = "taglib";
/*     */   private static final String VALIDATOR_PREFIX = "taglib/validator";
/*     */   private static final String TAG_PREFIX = "taglib/tag";
/*     */   private static final String TAGFILE_PREFIX = "taglib/tag-file";
/*     */   private static final String FUNCTION_PREFIX = "taglib/function";
/*     */   
/*     */   public void addRuleInstances(Digester digester)
/*     */   {
/*  44 */     digester.addCallMethod("taglib/tlibversion", "setTlibVersion", 0);
/*  45 */     digester.addCallMethod("taglib/tlib-version", "setTlibVersion", 0);
/*  46 */     digester.addCallMethod("taglib/jspversion", "setJspVersion", 0);
/*  47 */     digester.addCallMethod("taglib/jsp-version", "setJspVersion", 0);
/*  48 */     digester.addRule("taglib", new Rule()
/*     */     {
/*     */       public void begin(String namespace, String name, Attributes attributes)
/*     */       {
/*  52 */         TaglibXml taglibXml = (TaglibXml)this.digester.peek();
/*  53 */         taglibXml.setJspVersion(attributes.getValue("version"));
/*     */       }
/*  55 */     });
/*  56 */     digester.addCallMethod("taglib/shortname", "setShortName", 0);
/*  57 */     digester.addCallMethod("taglib/short-name", "setShortName", 0);
/*     */     
/*     */ 
/*  60 */     digester.addCallMethod("taglib/uri", "setUri", 0);
/*  61 */     digester.addCallMethod("taglib/info", "setInfo", 0);
/*  62 */     digester.addCallMethod("taglib/description", "setInfo", 0);
/*  63 */     digester.addCallMethod("taglib/listener/listener-class", "addListener", 0);
/*     */     
/*     */ 
/*  66 */     digester.addObjectCreate("taglib/validator", ValidatorXml.class.getName());
/*  67 */     digester.addCallMethod("taglib/validator/validator-class", "setValidatorClass", 0);
/*  68 */     digester.addCallMethod("taglib/validator/init-param", "addInitParam", 2);
/*  69 */     digester.addCallParam("taglib/validator/init-param/param-name", 0);
/*  70 */     digester.addCallParam("taglib/validator/init-param/param-value", 1);
/*  71 */     digester.addSetNext("taglib/validator", "setValidator", ValidatorXml.class.getName());
/*     */     
/*     */ 
/*     */ 
/*  75 */     digester.addObjectCreate("taglib/tag", TagXml.class.getName());
/*  76 */     addDescriptionGroup(digester, "taglib/tag");
/*  77 */     digester.addCallMethod("taglib/tag/name", "setName", 0);
/*  78 */     digester.addCallMethod("taglib/tag/tagclass", "setTagClass", 0);
/*  79 */     digester.addCallMethod("taglib/tag/tag-class", "setTagClass", 0);
/*  80 */     digester.addCallMethod("taglib/tag/teiclass", "setTeiClass", 0);
/*  81 */     digester.addCallMethod("taglib/tag/tei-class", "setTeiClass", 0);
/*  82 */     digester.addCallMethod("taglib/tag/bodycontent", "setBodyContent", 0);
/*  83 */     digester.addCallMethod("taglib/tag/body-content", "setBodyContent", 0);
/*     */     
/*  85 */     digester.addRule("taglib/tag/variable", new ScriptVariableRule(null));
/*  86 */     digester.addCallMethod("taglib/tag/variable/name-given", "setNameGiven", 0);
/*  87 */     digester.addCallMethod("taglib/tag/variable/name-from-attribute", "setNameFromAttribute", 0);
/*     */     
/*  89 */     digester.addCallMethod("taglib/tag/variable/variable-class", "setClassName", 0);
/*  90 */     digester.addRule("taglib/tag/variable/declare", new GenericBooleanRule(Variable.class, "setDeclare", null));
/*     */     
/*  92 */     digester.addCallMethod("taglib/tag/variable/scope", "setScope", 0);
/*     */     
/*  94 */     digester.addRule("taglib/tag/attribute", new TagAttributeRule(null));
/*  95 */     digester.addCallMethod("taglib/tag/attribute/description", "setDescription", 0);
/*  96 */     digester.addCallMethod("taglib/tag/attribute/name", "setName", 0);
/*  97 */     digester.addRule("taglib/tag/attribute/required", new GenericBooleanRule(Attribute.class, "setRequired", null));
/*     */     
/*  99 */     digester.addRule("taglib/tag/attribute/rtexprvalue", new GenericBooleanRule(Attribute.class, "setRequestTime", null));
/*     */     
/* 101 */     digester.addCallMethod("taglib/tag/attribute/type", "setType", 0);
/* 102 */     digester.addCallMethod("taglib/tag/attribute/deferred-value", "setDeferredValue");
/* 103 */     digester.addCallMethod("taglib/tag/attribute/deferred-value/type", "setExpectedTypeName", 0);
/*     */     
/* 105 */     digester.addCallMethod("taglib/tag/attribute/deferred-method", "setDeferredMethod");
/* 106 */     digester.addCallMethod("taglib/tag/attribute/deferred-method/method-signature", "setMethodSignature", 0);
/*     */     
/* 108 */     digester.addRule("taglib/tag/attribute/fragment", new GenericBooleanRule(Attribute.class, "setFragment", null));
/*     */     
/*     */ 
/* 111 */     digester.addRule("taglib/tag/dynamic-attributes", new GenericBooleanRule(TagXml.class, "setDynamicAttributes", null));
/*     */     
/* 113 */     digester.addSetNext("taglib/tag", "addTag", TagXml.class.getName());
/*     */     
/*     */ 
/* 116 */     digester.addObjectCreate("taglib/tag-file", TagFileXml.class.getName());
/* 117 */     addDescriptionGroup(digester, "taglib/tag-file");
/* 118 */     digester.addCallMethod("taglib/tag-file/name", "setName", 0);
/* 119 */     digester.addCallMethod("taglib/tag-file/path", "setPath", 0);
/* 120 */     digester.addSetNext("taglib/tag-file", "addTagFile", TagFileXml.class.getName());
/*     */     
/*     */ 
/* 123 */     digester.addCallMethod("taglib/function", "addFunction", 3);
/* 124 */     digester.addCallParam("taglib/function/name", 0);
/* 125 */     digester.addCallParam("taglib/function/function-class", 1);
/* 126 */     digester.addCallParam("taglib/function/function-signature", 2);
/*     */   }
/*     */   
/*     */   private void addDescriptionGroup(Digester digester, String prefix) {
/* 130 */     digester.addCallMethod(prefix + "/info", "setInfo", 0);
/* 131 */     digester.addCallMethod(prefix + "small-icon", "setSmallIcon", 0);
/* 132 */     digester.addCallMethod(prefix + "large-icon", "setLargeIcon", 0);
/*     */     
/* 134 */     digester.addCallMethod(prefix + "/description", "setInfo", 0);
/* 135 */     digester.addCallMethod(prefix + "/display-name", "setDisplayName", 0);
/* 136 */     digester.addCallMethod(prefix + "/icon/small-icon", "setSmallIcon", 0);
/* 137 */     digester.addCallMethod(prefix + "/icon/large-icon", "setLargeIcon", 0);
/*     */   }
/*     */   
/*     */   private static class TagAttributeRule extends Rule
/*     */   {
/*     */     public void begin(String namespace, String name, Attributes attributes) throws Exception {
/* 143 */       TaglibXml taglibXml = (TaglibXml)this.digester.peek(this.digester.getCount() - 1);
/* 144 */       this.digester.push(new TldRuleSet.Attribute("1.2".equals(taglibXml.getJspVersion()), null));
/*     */     }
/*     */     
/*     */     public void end(String namespace, String name) throws Exception
/*     */     {
/* 149 */       TldRuleSet.Attribute attribute = (TldRuleSet.Attribute)this.digester.pop();
/* 150 */       TagXml tag = (TagXml)this.digester.peek();
/* 151 */       tag.getAttributes().add(attribute.toTagAttributeInfo());
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Attribute {
/*     */     private final boolean allowShortNames;
/*     */     private String name;
/*     */     private boolean required;
/*     */     private String type;
/*     */     private boolean requestTime;
/*     */     private boolean fragment;
/*     */     private String description;
/*     */     private boolean deferredValue;
/*     */     private boolean deferredMethod;
/*     */     private String expectedTypeName;
/*     */     private String methodSignature;
/*     */     
/*     */     private Attribute(boolean allowShortNames) {
/* 169 */       this.allowShortNames = allowShortNames;
/*     */     }
/*     */     
/*     */     public void setName(String name) {
/* 173 */       this.name = name;
/*     */     }
/*     */     
/*     */     public void setRequired(boolean required) {
/* 177 */       this.required = required;
/*     */     }
/*     */     
/*     */     public void setType(String type) {
/* 181 */       if (this.allowShortNames) {
/* 182 */         switch (type) {
/*     */         case "Boolean": 
/* 184 */           this.type = "java.lang.Boolean";
/* 185 */           break;
/*     */         case "Character": 
/* 187 */           this.type = "java.lang.Character";
/* 188 */           break;
/*     */         case "Byte": 
/* 190 */           this.type = "java.lang.Byte";
/* 191 */           break;
/*     */         case "Short": 
/* 193 */           this.type = "java.lang.Short";
/* 194 */           break;
/*     */         case "Integer": 
/* 196 */           this.type = "java.lang.Integer";
/* 197 */           break;
/*     */         case "Long": 
/* 199 */           this.type = "java.lang.Long";
/* 200 */           break;
/*     */         case "Float": 
/* 202 */           this.type = "java.lang.Float";
/* 203 */           break;
/*     */         case "Double": 
/* 205 */           this.type = "java.lang.Double";
/* 206 */           break;
/*     */         case "String": 
/* 208 */           this.type = "java.lang.String";
/* 209 */           break;
/*     */         case "Object": 
/* 211 */           this.type = "java.lang.Object";
/* 212 */           break;
/*     */         default: 
/* 214 */           this.type = type;
/*     */         }
/*     */         
/*     */       } else {
/* 218 */         this.type = type;
/*     */       }
/*     */     }
/*     */     
/*     */     public void setRequestTime(boolean requestTime) {
/* 223 */       this.requestTime = requestTime;
/*     */     }
/*     */     
/*     */     public void setFragment(boolean fragment) {
/* 227 */       this.fragment = fragment;
/*     */     }
/*     */     
/*     */     public void setDescription(String description) {
/* 231 */       this.description = description;
/*     */     }
/*     */     
/*     */     public void setDeferredValue() {
/* 235 */       this.deferredValue = true;
/*     */     }
/*     */     
/*     */     public void setDeferredMethod() {
/* 239 */       this.deferredMethod = true;
/*     */     }
/*     */     
/*     */     public void setExpectedTypeName(String expectedTypeName) {
/* 243 */       this.expectedTypeName = expectedTypeName;
/*     */     }
/*     */     
/*     */     public void setMethodSignature(String methodSignature) {
/* 247 */       this.methodSignature = methodSignature;
/*     */     }
/*     */     
/*     */     public TagAttributeInfo toTagAttributeInfo() {
/* 251 */       if (this.fragment)
/*     */       {
/* 253 */         this.type = "javax.servlet.jsp.tagext.JspFragment";
/* 254 */         this.requestTime = true;
/* 255 */       } else if (this.deferredValue) {
/* 256 */         this.type = "javax.el.ValueExpression";
/* 257 */         if (this.expectedTypeName == null) {
/* 258 */           this.expectedTypeName = "java.lang.Object";
/*     */         }
/* 260 */       } else if (this.deferredMethod) {
/* 261 */         this.type = "javax.el.MethodExpression";
/* 262 */         if (this.methodSignature == null) {
/* 263 */           this.methodSignature = "java.lang.Object method()";
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 269 */       if ((!this.requestTime) && (this.type == null)) {
/* 270 */         this.type = "java.lang.String";
/*     */       }
/*     */       
/* 273 */       return new TagAttributeInfo(this.name, this.required, this.type, this.requestTime, this.fragment, this.description, this.deferredValue, this.deferredMethod, this.expectedTypeName, this.methodSignature);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class ScriptVariableRule
/*     */     extends Rule
/*     */   {
/*     */     public void begin(String namespace, String name, Attributes attributes)
/*     */       throws Exception
/*     */     {
/* 290 */       this.digester.push(new TldRuleSet.Variable());
/*     */     }
/*     */     
/*     */     public void end(String namespace, String name) throws Exception
/*     */     {
/* 295 */       TldRuleSet.Variable variable = (TldRuleSet.Variable)this.digester.pop();
/* 296 */       TagXml tag = (TagXml)this.digester.peek();
/* 297 */       tag.getVariables().add(variable.toTagVariableInfo());
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Variable {
/*     */     private String nameGiven;
/*     */     private String nameFromAttribute;
/* 304 */     private String className = "java.lang.String";
/* 305 */     private boolean declare = true;
/* 306 */     private int scope = 0;
/*     */     
/*     */     public void setNameGiven(String nameGiven) {
/* 309 */       this.nameGiven = nameGiven;
/*     */     }
/*     */     
/*     */     public void setNameFromAttribute(String nameFromAttribute) {
/* 313 */       this.nameFromAttribute = nameFromAttribute;
/*     */     }
/*     */     
/*     */     public void setClassName(String className) {
/* 317 */       this.className = className;
/*     */     }
/*     */     
/*     */     public void setDeclare(boolean declare) {
/* 321 */       this.declare = declare;
/*     */     }
/*     */     
/*     */     public void setScope(String scopeName) {
/* 325 */       switch (scopeName) {
/*     */       case "NESTED": 
/* 327 */         this.scope = 0;
/* 328 */         break;
/*     */       case "AT_BEGIN": 
/* 330 */         this.scope = 1;
/* 331 */         break;
/*     */       case "AT_END": 
/* 333 */         this.scope = 2;
/*     */       }
/*     */     }
/*     */     
/*     */     public TagVariableInfo toTagVariableInfo()
/*     */     {
/* 339 */       return new TagVariableInfo(this.nameGiven, this.nameFromAttribute, this.className, this.declare, this.scope);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class GenericBooleanRule extends Rule {
/*     */     private final Method setter;
/*     */     
/*     */     private GenericBooleanRule(Class<?> type, String setterName) {
/*     */       try {
/* 348 */         this.setter = type.getMethod(setterName, new Class[] { Boolean.TYPE });
/*     */       } catch (NoSuchMethodException e) {
/* 350 */         throw new IllegalArgumentException(e);
/*     */       }
/*     */     }
/*     */     
/*     */     public void body(String namespace, String name, String text) throws Exception
/*     */     {
/* 356 */       if (null != text)
/* 357 */         text = text.trim();
/* 358 */       boolean value = ("true".equalsIgnoreCase(text)) || ("yes".equalsIgnoreCase(text));
/* 359 */       this.setter.invoke(this.digester.peek(), new Object[] { Boolean.valueOf(value) });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\tld\TldRuleSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */