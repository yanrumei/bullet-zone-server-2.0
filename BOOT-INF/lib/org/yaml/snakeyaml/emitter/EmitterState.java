package org.yaml.snakeyaml.emitter;

import java.io.IOException;

abstract interface EmitterState
{
  public abstract void expect()
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\emitter\EmitterState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */