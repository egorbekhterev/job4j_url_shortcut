package ru.job4j.urlshortcut.resolver;

import com.jayway.jsonpath.JsonPath;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import ru.job4j.urlshortcut.annotation.JsonArg;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class JsonArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String JSON_BODY_ATTRIBUTE = "JSON_REQUEST_BODY";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JsonArg.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {
        String body = getRequestBody(webRequest);
        String jsonPath = Objects.requireNonNull(
                Objects.requireNonNull(parameter.getParameterAnnotation(JsonArg.class)).value());
        Class<?> parameterType = parameter.getParameterType();
        return JsonPath.parse(body).read(jsonPath, parameterType);
    }

    private String getRequestBody(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest =
                Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class));
        String jsonBody = Objects.requireNonNullElse((String) servletRequest.getAttribute(JSON_BODY_ATTRIBUTE), "");
        if (jsonBody.isEmpty()) {
            try (InputStream input = servletRequest.getInputStream()) {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int n;
                while ((n = input.read(buffer)) != -1) {
                    output.write(buffer, 0, n);
                }
                jsonBody = output.toString(StandardCharsets.UTF_8);
                servletRequest.setAttribute(JSON_BODY_ATTRIBUTE, jsonBody);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return jsonBody;
    }
}
