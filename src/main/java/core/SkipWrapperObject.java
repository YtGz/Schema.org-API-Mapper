package core;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/*
 * @see: http://stackoverflow.com/questions/11747370/jackson-how-to-process-deserialize-nested-json
 */
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface SkipWrapperObject {
    String value();
}
