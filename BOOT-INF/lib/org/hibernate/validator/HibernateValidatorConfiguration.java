package org.hibernate.validator;

import javax.validation.Configuration;
import org.hibernate.validator.cfg.ConstraintMapping;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;
import org.hibernate.validator.spi.time.TimeProvider;
import org.hibernate.validator.spi.valuehandling.ValidatedValueUnwrapper;

public abstract interface HibernateValidatorConfiguration
  extends Configuration<HibernateValidatorConfiguration>
{
  public static final String FAIL_FAST = "hibernate.validator.fail_fast";
  public static final String ALLOW_PARAMETER_CONSTRAINT_OVERRIDE = "hibernate.validator.allow_parameter_constraint_override";
  public static final String ALLOW_MULTIPLE_CASCADED_VALIDATION_ON_RESULT = "hibernate.validator.allow_multiple_cascaded_validation_on_result";
  public static final String ALLOW_PARALLEL_METHODS_DEFINE_PARAMETER_CONSTRAINTS = "hibernate.validator.allow_parallel_method_parameter_constraint";
  public static final String VALIDATED_VALUE_HANDLERS = "hibernate.validator.validated_value_handlers";
  @Deprecated
  public static final String CONSTRAINT_MAPPING_CONTRIBUTOR = "hibernate.validator.constraint_mapping_contributor";
  public static final String CONSTRAINT_MAPPING_CONTRIBUTORS = "hibernate.validator.constraint_mapping_contributors";
  public static final String TIME_PROVIDER = "hibernate.validator.time_provider";
  
  public abstract ResourceBundleLocator getDefaultResourceBundleLocator();
  
  public abstract ConstraintMapping createConstraintMapping();
  
  public abstract HibernateValidatorConfiguration addMapping(ConstraintMapping paramConstraintMapping);
  
  public abstract HibernateValidatorConfiguration failFast(boolean paramBoolean);
  
  public abstract HibernateValidatorConfiguration addValidatedValueHandler(ValidatedValueUnwrapper<?> paramValidatedValueUnwrapper);
  
  public abstract HibernateValidatorConfiguration externalClassLoader(ClassLoader paramClassLoader);
  
  public abstract HibernateValidatorConfiguration timeProvider(TimeProvider paramTimeProvider);
  
  public abstract HibernateValidatorConfiguration allowOverridingMethodAlterParameterConstraint(boolean paramBoolean);
  
  public abstract HibernateValidatorConfiguration allowMultipleCascadedValidationOnReturnValues(boolean paramBoolean);
  
  public abstract HibernateValidatorConfiguration allowParallelMethodsDefineParameterConstraints(boolean paramBoolean);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\HibernateValidatorConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */