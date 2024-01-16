package com.quantum_pixel.ecm.config.swagger;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

import jakarta.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceTransformer;
import org.springframework.web.servlet.resource.ResourceTransformerChain;
import org.springframework.web.servlet.resource.TransformedResource;
import org.springframework.web.servlet.resource.WebJarsResourceResolver;

@Configuration
@ConditionalOnProperty(prefix = "swagger-ui", name = "enabled", havingValue = "true")
public class SwaggerUiMvcConfiguration implements WebMvcConfigurer {
  private static final String SWAGGER_UI_BASE_PATH = "/swagger-ui/";
  private static final String SWAGGER_UI_DEFAULT_PATH = "forward:/swagger-ui/index.html";
  private static final String OPENAPI_SECURITY_SCHEME_YAML_DEFAULT = "/openapi/securityScheme.yaml";
  private static final String OPENAPI_SECURITY_SCHEME_YAML_TEMPLATE =
      "forward:/openapi/securityScheme-%s.yaml";
  private static final String SWAGGER_UI_CONFIG_JS_DEFAULT =
      "/openapi/swagger-ui/swagger-ui-config.js";
  private static final String SWAGGER_UI_CONFIG_JS_TEMPLATE =
      "forward:/openapi/swagger-ui/swagger-ui-config-%s.js";

  private final String configurationName;

  public SwaggerUiMvcConfiguration(
      @Value("${swagger-ui.configuration-name}") String configurationName) {
    this.configurationName = configurationName;
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/", "classpath:/openapi")
        .resourceChain(true)
        .addResolver(new WebJarsResourceResolver())
        .addResolver(new PathResourceResolver())
        .addTransformer(new SwaggerUiTransformer());
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController(SWAGGER_UI_BASE_PATH).setViewName(SWAGGER_UI_DEFAULT_PATH);
    registry
        .addViewController(OPENAPI_SECURITY_SCHEME_YAML_DEFAULT)
        .setViewName(format(OPENAPI_SECURITY_SCHEME_YAML_TEMPLATE, configurationName));
    registry
        .addViewController(SWAGGER_UI_CONFIG_JS_DEFAULT)
        .setViewName(format(SWAGGER_UI_CONFIG_JS_TEMPLATE, configurationName));
  }

  /** Injects swagger ui configuration js into Swagger UI */
  private static class SwaggerUiTransformer implements ResourceTransformer {

    public static final int BUFFER_SIZE = 1024;

    @Override
    public Resource transform(
        HttpServletRequest request, Resource resource, ResourceTransformerChain transformerChain)
        throws IOException {
      if (isSwaggerUI(resource)) {
        String customHtml = injectScriptInHtml(resource);
        return new TransformedResource(resource, customHtml.getBytes(UTF_8));
      } else {
        return resource;
      }
    }

    private static boolean isSwaggerUI(Resource resource) throws IOException {
      final AntPathMatcher antPathMatcher = new AntPathMatcher();
      return antPathMatcher.match("**/swagger-ui/**/index.html", resource.getURL().toString());
    }

    private static String injectScriptInHtml(Resource resource) throws IOException {
      return readFullyAsString(resource.getInputStream())
          .replace(
              "</body>",
              "<script src=\".." + SWAGGER_UI_CONFIG_JS_DEFAULT + "\"></script>\n</body>");
    }

    private static String readFullyAsString(InputStream inputStream) throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte[] buffer = new byte[BUFFER_SIZE];
      int length;
      while ((length = inputStream.read(buffer)) != -1) {
        baos.write(buffer, 0, length);
      }
      return baos.toString(UTF_8);
    }
  }
}
