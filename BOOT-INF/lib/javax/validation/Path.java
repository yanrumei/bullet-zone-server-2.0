package javax.validation;

import java.util.List;

public abstract interface Path
  extends Iterable<Node>
{
  public static abstract interface PropertyNode
    extends Path.Node
  {}
  
  public static abstract interface BeanNode
    extends Path.Node
  {}
  
  public static abstract interface CrossParameterNode
    extends Path.Node
  {}
  
  public static abstract interface ParameterNode
    extends Path.Node
  {
    public abstract int getParameterIndex();
  }
  
  public static abstract interface ReturnValueNode
    extends Path.Node
  {}
  
  public static abstract interface ConstructorNode
    extends Path.Node
  {
    public abstract List<Class<?>> getParameterTypes();
  }
  
  public static abstract interface MethodNode
    extends Path.Node
  {
    public abstract List<Class<?>> getParameterTypes();
  }
  
  public static abstract interface Node
  {
    public abstract String getName();
    
    public abstract boolean isInIterable();
    
    public abstract Integer getIndex();
    
    public abstract Object getKey();
    
    public abstract ElementKind getKind();
    
    public abstract <T extends Node> T as(Class<T> paramClass);
  }
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\Path.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */