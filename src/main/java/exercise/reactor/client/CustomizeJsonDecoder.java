package exercise.reactor.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.CodecException;
import org.springframework.core.codec.DecodingException;
import org.springframework.core.codec.Hints;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.log.LogFormatUtils;
import org.springframework.http.codec.json.Jackson2CodecSupport;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.MimeType;

import java.io.IOException;
import java.util.Map;

public class CustomizeJsonDecoder extends Jackson2JsonDecoder {
    static final String ACTUAL_TYPE_HINT = Jackson2CodecSupport.class.getName() + ".actualType";

    public CustomizeJsonDecoder(ObjectMapper mapper, MimeType... mimeTypes) {
        super(mapper, mimeTypes);
    }

    private void logValue(@Nullable Object value, @Nullable Map<String, Object> hints) {
        if (!Hints.isLoggingSuppressed(hints)) {
            LogFormatUtils.traceDebug(this.logger, (traceOn) -> {
                String formatted = LogFormatUtils.formatValue(value, !traceOn);
                return Hints.getLogPrefix(hints) + "Decoded [" + formatted + "]";
            });
        }
    }

    private ObjectReader getObjectReader(ResolvableType elementType, @Nullable Map<String, Object> hints) {
        Assert.notNull(elementType, "'elementType' must not be null");
        Class<?> contextClass = this.getContextClass(elementType);
        if (contextClass == null && hints != null) {
            contextClass = this.getContextClass((ResolvableType) hints.get(ACTUAL_TYPE_HINT));
        }

        JavaType javaType = this.getJavaType(elementType.getType(), contextClass);
        Class<?> jsonView = hints != null ? (Class) hints.get(Jackson2CodecSupport.JSON_VIEW_HINT) : null;
        return jsonView != null ? this.getObjectMapper().readerWithView(jsonView).forType(javaType) : this.getObjectMapper().readerFor(javaType);
    }

    private CodecException processException(IOException ex) {
        if (ex instanceof InvalidDefinitionException) {
            JavaType type = ((InvalidDefinitionException) ex).getType();
            return new CodecException("Type definition error: " + type, ex);
        } else if (ex instanceof JsonProcessingException) {
            String originalMessage = ((JsonProcessingException) ex).getOriginalMessage();
            return new DecodingException("JSON decoding error: " + originalMessage, ex);
        } else {
            return new DecodingException("I/O error while parsing input stream", ex);
        }
    }

    @Nullable
    private Class<?> getContextClass(@Nullable ResolvableType elementType) {
        MethodParameter param = elementType != null ? this.getParameter(elementType) : null;
        return param != null ? param.getContainingClass() : null;
    }


    public Object decode(DataBuffer dataBuffer, ResolvableType targetType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) throws DecodingException {
        Object var7;
        try {
            ObjectReader objectReader = this.getObjectReader(targetType, hints);
            Object value = objectReader.readValue(dataBuffer.asInputStream());
            this.logValue(value, hints);
            var7 = value;
        } catch (MismatchedInputException decodingException) {
            return null;
        } catch (IOException var11) {
            throw this.processException(var11);
        } finally {
            DataBufferUtils.release(dataBuffer);
        }
        return var7;
    }

}