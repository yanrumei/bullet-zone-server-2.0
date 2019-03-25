/*     */ package org.springframework.validation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.EmptyStackException;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractErrors
/*     */   implements Errors, Serializable
/*     */ {
/*  40 */   private String nestedPath = "";
/*     */   
/*  42 */   private final Stack<String> nestedPathStack = new Stack();
/*     */   
/*     */ 
/*     */   public void setNestedPath(String nestedPath)
/*     */   {
/*  47 */     doSetNestedPath(nestedPath);
/*  48 */     this.nestedPathStack.clear();
/*     */   }
/*     */   
/*     */   public String getNestedPath()
/*     */   {
/*  53 */     return this.nestedPath;
/*     */   }
/*     */   
/*     */   public void pushNestedPath(String subPath)
/*     */   {
/*  58 */     this.nestedPathStack.push(getNestedPath());
/*  59 */     doSetNestedPath(getNestedPath() + subPath);
/*     */   }
/*     */   
/*     */   public void popNestedPath() throws IllegalArgumentException
/*     */   {
/*     */     try {
/*  65 */       String formerNestedPath = (String)this.nestedPathStack.pop();
/*  66 */       doSetNestedPath(formerNestedPath);
/*     */     }
/*     */     catch (EmptyStackException ex) {
/*  69 */       throw new IllegalStateException("Cannot pop nested path: no nested path on stack");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doSetNestedPath(String nestedPath)
/*     */   {
/*  78 */     if (nestedPath == null) {
/*  79 */       nestedPath = "";
/*     */     }
/*  81 */     nestedPath = canonicalFieldName(nestedPath);
/*  82 */     if ((nestedPath.length() > 0) && (!nestedPath.endsWith("."))) {
/*  83 */       nestedPath = nestedPath + ".";
/*     */     }
/*  85 */     this.nestedPath = nestedPath;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String fixedField(String field)
/*     */   {
/*  93 */     if (StringUtils.hasLength(field)) {
/*  94 */       return getNestedPath() + canonicalFieldName(field);
/*     */     }
/*     */     
/*  97 */     String path = getNestedPath();
/*  98 */     return path.endsWith(".") ? path
/*  99 */       .substring(0, path.length() - ".".length()) : path;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String canonicalFieldName(String field)
/*     */   {
/* 110 */     return field;
/*     */   }
/*     */   
/*     */ 
/*     */   public void reject(String errorCode)
/*     */   {
/* 116 */     reject(errorCode, null, null);
/*     */   }
/*     */   
/*     */   public void reject(String errorCode, String defaultMessage)
/*     */   {
/* 121 */     reject(errorCode, null, defaultMessage);
/*     */   }
/*     */   
/*     */   public void rejectValue(String field, String errorCode)
/*     */   {
/* 126 */     rejectValue(field, errorCode, null, null);
/*     */   }
/*     */   
/*     */   public void rejectValue(String field, String errorCode, String defaultMessage)
/*     */   {
/* 131 */     rejectValue(field, errorCode, null, defaultMessage);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean hasErrors()
/*     */   {
/* 137 */     return !getAllErrors().isEmpty();
/*     */   }
/*     */   
/*     */   public int getErrorCount()
/*     */   {
/* 142 */     return getAllErrors().size();
/*     */   }
/*     */   
/*     */   public List<ObjectError> getAllErrors()
/*     */   {
/* 147 */     List<ObjectError> result = new LinkedList();
/* 148 */     result.addAll(getGlobalErrors());
/* 149 */     result.addAll(getFieldErrors());
/* 150 */     return Collections.unmodifiableList(result);
/*     */   }
/*     */   
/*     */   public boolean hasGlobalErrors()
/*     */   {
/* 155 */     return getGlobalErrorCount() > 0;
/*     */   }
/*     */   
/*     */   public int getGlobalErrorCount()
/*     */   {
/* 160 */     return getGlobalErrors().size();
/*     */   }
/*     */   
/*     */   public ObjectError getGlobalError()
/*     */   {
/* 165 */     List<ObjectError> globalErrors = getGlobalErrors();
/* 166 */     return !globalErrors.isEmpty() ? (ObjectError)globalErrors.get(0) : null;
/*     */   }
/*     */   
/*     */   public boolean hasFieldErrors()
/*     */   {
/* 171 */     return getFieldErrorCount() > 0;
/*     */   }
/*     */   
/*     */   public int getFieldErrorCount()
/*     */   {
/* 176 */     return getFieldErrors().size();
/*     */   }
/*     */   
/*     */   public FieldError getFieldError()
/*     */   {
/* 181 */     List<FieldError> fieldErrors = getFieldErrors();
/* 182 */     return !fieldErrors.isEmpty() ? (FieldError)fieldErrors.get(0) : null;
/*     */   }
/*     */   
/*     */   public boolean hasFieldErrors(String field)
/*     */   {
/* 187 */     return getFieldErrorCount(field) > 0;
/*     */   }
/*     */   
/*     */   public int getFieldErrorCount(String field)
/*     */   {
/* 192 */     return getFieldErrors(field).size();
/*     */   }
/*     */   
/*     */   public List<FieldError> getFieldErrors(String field)
/*     */   {
/* 197 */     List<FieldError> fieldErrors = getFieldErrors();
/* 198 */     List<FieldError> result = new LinkedList();
/* 199 */     String fixedField = fixedField(field);
/* 200 */     for (FieldError error : fieldErrors) {
/* 201 */       if (isMatchingFieldError(fixedField, error)) {
/* 202 */         result.add(error);
/*     */       }
/*     */     }
/* 205 */     return Collections.unmodifiableList(result);
/*     */   }
/*     */   
/*     */   public FieldError getFieldError(String field)
/*     */   {
/* 210 */     List<FieldError> fieldErrors = getFieldErrors(field);
/* 211 */     return !fieldErrors.isEmpty() ? (FieldError)fieldErrors.get(0) : null;
/*     */   }
/*     */   
/*     */ 
/*     */   public Class<?> getFieldType(String field)
/*     */   {
/* 217 */     Object value = getFieldValue(field);
/* 218 */     return value != null ? value.getClass() : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isMatchingFieldError(String field, FieldError fieldError)
/*     */   {
/* 228 */     if (field.equals(fieldError.getField())) {
/* 229 */       return true;
/*     */     }
/*     */     
/* 232 */     int endIndex = field.length() - 1;
/* 233 */     return (endIndex >= 0) && (field.charAt(endIndex) == '*') && ((endIndex == 0) || 
/* 234 */       (field.regionMatches(0, fieldError.getField(), 0, endIndex)));
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 240 */     StringBuilder sb = new StringBuilder(getClass().getName());
/* 241 */     sb.append(": ").append(getErrorCount()).append(" errors");
/* 242 */     for (ObjectError error : getAllErrors()) {
/* 243 */       sb.append('\n').append(error);
/*     */     }
/* 245 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\validation\AbstractErrors.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */