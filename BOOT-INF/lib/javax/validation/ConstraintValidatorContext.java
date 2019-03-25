package javax.validation;

public abstract interface ConstraintValidatorContext
{
  public abstract void disableDefaultConstraintViolation();
  
  public abstract String getDefaultConstraintMessageTemplate();
  
  public abstract ConstraintViolationBuilder buildConstraintViolationWithTemplate(String paramString);
  
  public abstract <T> T unwrap(Class<T> paramClass);
  
  public static abstract interface ConstraintViolationBuilder
  {
    /**
     * @deprecated
     */
    public abstract NodeBuilderDefinedContext addNode(String paramString);
    
    public abstract NodeBuilderCustomizableContext addPropertyNode(String paramString);
    
    public abstract LeafNodeBuilderCustomizableContext addBeanNode();
    
    public abstract NodeBuilderDefinedContext addParameterNode(int paramInt);
    
    public abstract ConstraintValidatorContext addConstraintViolation();
    
    public static abstract interface NodeContextBuilder
    {
      public abstract ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderDefinedContext atKey(Object paramObject);
      
      public abstract ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderDefinedContext atIndex(Integer paramInteger);
      
      /**
       * @deprecated
       */
      public abstract ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext addNode(String paramString);
      
      public abstract ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext addPropertyNode(String paramString);
      
      public abstract ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeBuilderCustomizableContext addBeanNode();
      
      public abstract ConstraintValidatorContext addConstraintViolation();
    }
    
    public static abstract interface NodeBuilderCustomizableContext
    {
      public abstract ConstraintValidatorContext.ConstraintViolationBuilder.NodeContextBuilder inIterable();
      
      /**
       * @deprecated
       */
      public abstract NodeBuilderCustomizableContext addNode(String paramString);
      
      public abstract NodeBuilderCustomizableContext addPropertyNode(String paramString);
      
      public abstract ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeBuilderCustomizableContext addBeanNode();
      
      public abstract ConstraintValidatorContext addConstraintViolation();
    }
    
    public static abstract interface NodeBuilderDefinedContext
    {
      /**
       * @deprecated
       */
      public abstract ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext addNode(String paramString);
      
      public abstract ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext addPropertyNode(String paramString);
      
      public abstract ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeBuilderCustomizableContext addBeanNode();
      
      public abstract ConstraintValidatorContext addConstraintViolation();
    }
    
    public static abstract interface LeafNodeContextBuilder
    {
      public abstract ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeBuilderDefinedContext atKey(Object paramObject);
      
      public abstract ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeBuilderDefinedContext atIndex(Integer paramInteger);
      
      public abstract ConstraintValidatorContext addConstraintViolation();
    }
    
    public static abstract interface LeafNodeBuilderCustomizableContext
    {
      public abstract ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeContextBuilder inIterable();
      
      public abstract ConstraintValidatorContext addConstraintViolation();
    }
    
    public static abstract interface LeafNodeBuilderDefinedContext
    {
      public abstract ConstraintValidatorContext addConstraintViolation();
    }
  }
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\ConstraintValidatorContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */