/*     */ package org.springframework.web.servlet.support;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.BeanWrapper;
/*     */ import org.springframework.beans.PropertyAccessorFactory;
/*     */ import org.springframework.context.NoSuchMessageException;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.validation.BindingResult;
/*     */ import org.springframework.validation.Errors;
/*     */ import org.springframework.validation.ObjectError;
/*     */ import org.springframework.web.util.HtmlUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BindStatus
/*     */ {
/*     */   private final RequestContext requestContext;
/*     */   private final String path;
/*     */   private final boolean htmlEscape;
/*     */   private final String expression;
/*     */   private final Errors errors;
/*     */   private BindingResult bindingResult;
/*     */   private Object value;
/*     */   private Class<?> valueType;
/*     */   private Object actualValue;
/*     */   private PropertyEditor editor;
/*     */   private List<? extends ObjectError> objectErrors;
/*     */   private String[] errorCodes;
/*     */   private String[] errorMessages;
/*     */   
/*     */   public BindStatus(RequestContext requestContext, String path, boolean htmlEscape)
/*     */     throws IllegalStateException
/*     */   {
/*  88 */     this.requestContext = requestContext;
/*  89 */     this.path = path;
/*  90 */     this.htmlEscape = htmlEscape;
/*     */     
/*     */ 
/*     */ 
/*  94 */     int dotPos = path.indexOf('.');
/*  95 */     String beanName; if (dotPos == -1)
/*     */     {
/*  97 */       String beanName = path;
/*  98 */       this.expression = null;
/*     */     }
/*     */     else {
/* 101 */       beanName = path.substring(0, dotPos);
/* 102 */       this.expression = path.substring(dotPos + 1);
/*     */     }
/*     */     
/* 105 */     this.errors = requestContext.getErrors(beanName, false);
/*     */     
/* 107 */     if (this.errors != null)
/*     */     {
/*     */ 
/*     */ 
/* 111 */       if (this.expression != null) {
/* 112 */         if ("*".equals(this.expression)) {
/* 113 */           this.objectErrors = this.errors.getAllErrors();
/*     */         }
/* 115 */         else if (this.expression.endsWith("*")) {
/* 116 */           this.objectErrors = this.errors.getFieldErrors(this.expression);
/*     */         }
/*     */         else {
/* 119 */           this.objectErrors = this.errors.getFieldErrors(this.expression);
/* 120 */           this.value = this.errors.getFieldValue(this.expression);
/* 121 */           this.valueType = this.errors.getFieldType(this.expression);
/* 122 */           if ((this.errors instanceof BindingResult)) {
/* 123 */             this.bindingResult = ((BindingResult)this.errors);
/* 124 */             this.actualValue = this.bindingResult.getRawFieldValue(this.expression);
/* 125 */             this.editor = this.bindingResult.findEditor(this.expression, null);
/*     */           }
/*     */           else {
/* 128 */             this.actualValue = this.value;
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 133 */         this.objectErrors = this.errors.getGlobalErrors();
/*     */       }
/* 135 */       initErrorCodes();
/*     */ 
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/* 142 */       Object target = requestContext.getModelObject(beanName);
/* 143 */       if (target == null) {
/* 144 */         throw new IllegalStateException("Neither BindingResult nor plain target object for bean name '" + beanName + "' available as request attribute");
/*     */       }
/*     */       
/* 147 */       if ((this.expression != null) && (!"*".equals(this.expression)) && (!this.expression.endsWith("*"))) {
/* 148 */         BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(target);
/* 149 */         this.value = bw.getPropertyValue(this.expression);
/* 150 */         this.valueType = bw.getPropertyType(this.expression);
/* 151 */         this.actualValue = this.value;
/*     */       }
/* 153 */       this.errorCodes = new String[0];
/* 154 */       this.errorMessages = new String[0];
/*     */     }
/*     */     
/* 157 */     if ((htmlEscape) && ((this.value instanceof String))) {
/* 158 */       this.value = HtmlUtils.htmlEscape((String)this.value);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void initErrorCodes()
/*     */   {
/* 166 */     this.errorCodes = new String[this.objectErrors.size()];
/* 167 */     for (int i = 0; i < this.objectErrors.size(); i++) {
/* 168 */       ObjectError error = (ObjectError)this.objectErrors.get(i);
/* 169 */       this.errorCodes[i] = error.getCode();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void initErrorMessages()
/*     */     throws NoSuchMessageException
/*     */   {
/* 177 */     if (this.errorMessages == null) {
/* 178 */       this.errorMessages = new String[this.objectErrors.size()];
/* 179 */       for (int i = 0; i < this.objectErrors.size(); i++) {
/* 180 */         ObjectError error = (ObjectError)this.objectErrors.get(i);
/* 181 */         this.errorMessages[i] = this.requestContext.getMessage(error, this.htmlEscape);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPath()
/*     */   {
/* 192 */     return this.path;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getExpression()
/*     */   {
/* 203 */     return this.expression;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getValue()
/*     */   {
/* 213 */     return this.value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getValueType()
/*     */   {
/* 222 */     return this.valueType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getActualValue()
/*     */   {
/* 230 */     return this.actualValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDisplayValue()
/*     */   {
/* 241 */     if ((this.value instanceof String)) {
/* 242 */       return (String)this.value;
/*     */     }
/* 244 */     if (this.value != null) {
/* 245 */       return this.htmlEscape ? HtmlUtils.htmlEscape(this.value.toString()) : this.value.toString();
/*     */     }
/* 247 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isError()
/*     */   {
/* 254 */     return (this.errorCodes != null) && (this.errorCodes.length > 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getErrorCodes()
/*     */   {
/* 262 */     return this.errorCodes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getErrorCode()
/*     */   {
/* 269 */     return this.errorCodes.length > 0 ? this.errorCodes[0] : "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getErrorMessages()
/*     */   {
/* 277 */     initErrorMessages();
/* 278 */     return this.errorMessages;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getErrorMessage()
/*     */   {
/* 285 */     initErrorMessages();
/* 286 */     return this.errorMessages.length > 0 ? this.errorMessages[0] : "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getErrorMessagesAsString(String delimiter)
/*     */   {
/* 296 */     initErrorMessages();
/* 297 */     return StringUtils.arrayToDelimitedString(this.errorMessages, delimiter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Errors getErrors()
/*     */   {
/* 307 */     return this.errors;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertyEditor getEditor()
/*     */   {
/* 316 */     return this.editor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertyEditor findEditor(Class<?> valueClass)
/*     */   {
/* 326 */     return this.bindingResult != null ? this.bindingResult.findEditor(this.expression, valueClass) : null;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 332 */     StringBuilder sb = new StringBuilder("BindStatus: ");
/* 333 */     sb.append("expression=[").append(this.expression).append("]; ");
/* 334 */     sb.append("value=[").append(this.value).append("]");
/* 335 */     if (isError()) {
/* 336 */       sb.append("; errorCodes=").append(Arrays.asList(this.errorCodes));
/*     */     }
/* 338 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\support\BindStatus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */