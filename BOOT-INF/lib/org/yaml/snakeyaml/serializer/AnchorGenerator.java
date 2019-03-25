package org.yaml.snakeyaml.serializer;

import org.yaml.snakeyaml.nodes.Node;

public abstract interface AnchorGenerator
{
  public abstract String nextAnchor(Node paramNode);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\serializer\AnchorGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */