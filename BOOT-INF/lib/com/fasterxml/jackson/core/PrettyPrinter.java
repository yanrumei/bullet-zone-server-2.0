package com.fasterxml.jackson.core;

import java.io.IOException;

public abstract interface PrettyPrinter
{
  public abstract void writeRootValueSeparator(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException;
  
  public abstract void writeStartObject(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException;
  
  public abstract void writeEndObject(JsonGenerator paramJsonGenerator, int paramInt)
    throws IOException, JsonGenerationException;
  
  public abstract void writeObjectEntrySeparator(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException;
  
  public abstract void writeObjectFieldValueSeparator(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException;
  
  public abstract void writeStartArray(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException;
  
  public abstract void writeEndArray(JsonGenerator paramJsonGenerator, int paramInt)
    throws IOException, JsonGenerationException;
  
  public abstract void writeArrayValueSeparator(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException;
  
  public abstract void beforeArrayValues(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException;
  
  public abstract void beforeObjectEntries(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\PrettyPrinter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */