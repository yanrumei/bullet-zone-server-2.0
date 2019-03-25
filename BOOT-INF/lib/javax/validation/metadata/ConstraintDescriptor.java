package javax.validation.metadata;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintTarget;
import javax.validation.ConstraintValidator;
import javax.validation.Payload;

public abstract interface ConstraintDescriptor<T extends Annotation>
{
  public abstract T getAnnotation();
  
  public abstract String getMessageTemplate();
  
  public abstract Set<Class<?>> getGroups();
  
  public abstract Set<Class<? extends Payload>> getPayload();
  
  public abstract ConstraintTarget getValidationAppliesTo();
  
  public abstract List<Class<? extends ConstraintValidator<T, ?>>> getConstraintValidatorClasses();
  
  public abstract Map<String, Object> getAttributes();
  
  public abstract Set<ConstraintDescriptor<?>> getComposingConstraints();
  
  public abstract boolean isReportAsSingleViolation();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\metadata\ConstraintDescriptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */