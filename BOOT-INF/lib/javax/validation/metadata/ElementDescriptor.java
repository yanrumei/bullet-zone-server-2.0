package javax.validation.metadata;

import java.lang.annotation.ElementType;
import java.util.Set;

public abstract interface ElementDescriptor
{
  public abstract boolean hasConstraints();
  
  public abstract Class<?> getElementClass();
  
  public abstract Set<ConstraintDescriptor<?>> getConstraintDescriptors();
  
  public abstract ConstraintFinder findConstraints();
  
  public static abstract interface ConstraintFinder
  {
    public abstract ConstraintFinder unorderedAndMatchingGroups(Class<?>... paramVarArgs);
    
    public abstract ConstraintFinder lookingAt(Scope paramScope);
    
    public abstract ConstraintFinder declaredOn(ElementType... paramVarArgs);
    
    public abstract Set<ConstraintDescriptor<?>> getConstraintDescriptors();
    
    public abstract boolean hasConstraints();
  }
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\metadata\ElementDescriptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */