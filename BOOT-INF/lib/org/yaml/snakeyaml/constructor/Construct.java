package org.yaml.snakeyaml.constructor;

import org.yaml.snakeyaml.nodes.Node;

public abstract interface Construct
{
  public abstract Object construct(Node paramNode);
  
  public abstract void construct2ndStep(Node paramNode, Object paramObject);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\constructor\Construct.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */