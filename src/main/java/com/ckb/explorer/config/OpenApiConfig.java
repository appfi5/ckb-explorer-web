package com.ckb.explorer.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CKB Explorer API")
                        .version("1.0")
                        .description("REST API for CKB Explorer, providing access to blockchain data")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
            .components(new Components()
                // 可选：定义全局 content 复用
                .addSchemas("JsonApiSchema", new Schema<>().type("object"))
            );



    }

  @Bean
  public OperationCustomizer jsonApiOperationCustomizer() {
    return (operation, handlerMethod) -> {
      String mediaType = "application/vnd.api+json";

      // 创建通用 media type
      Content content = new Content();
      content.addMediaType(mediaType, new MediaType().schema(new ObjectSchema()));

      // 修改所有响应
      if (operation.getResponses() != null) {
        operation.getResponses().values().forEach(response -> {
          if (response.getContent() != null) {
            response.setContent(content);
          }
        });
      }

      // 修改请求体
      if (operation.getRequestBody() != null && operation.getRequestBody().getContent() != null) {
        operation.getRequestBody().setContent(content);
      }

      return operation;
    };
  }


}