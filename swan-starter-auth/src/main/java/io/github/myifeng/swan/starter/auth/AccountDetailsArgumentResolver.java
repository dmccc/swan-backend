package io.github.myifeng.swan.starter.auth;

import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AccountDetailsArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(@NotNull MethodParameter parameter) {
        return AccountDetails.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer mavContainer, @NotNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        val principal = webRequest.getUserPrincipal();
        if (principal instanceof OAuth2Authentication) {
            return ((OAuth2Authentication) principal).getUserAuthentication().getDetails();
        } else {
            return null;
        }
    }

}

