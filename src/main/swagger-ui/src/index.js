import SwaggerUI from 'swagger-ui'
import 'swagger-ui/dist/swagger-ui.css'
import SwaggerUIStandalonePreset from 'swagger-ui-dist/swagger-ui-standalone-preset'
import 'swagger-ui-dist/index.css'
import CustomLogoPlugin from './plugin.jsx'
import CaseInsensitiveTagFilterPlugin from "./tag-filter";
import PathAndMethodOperationsSorter from "./operations-sorter";
import DateTimeSwaggerPlugin from "./datepicker";

const ui = SwaggerUI({
  // core
  dom_id: '#swagger-ui',
  urls: [
    {
      name: 'default',
      url: 'http://localhost:8080/swagger-ui/openapi3.yaml'
    },
    {
      name: 'petstore',
      url: 'https://raw.githubusercontent.com/swagger-api/swagger-petstore/master/src/main/resources/openapi.yaml'
    },
  ],
  'urls.primaryName': 'default',
  // plugin system
  // Topbar를 사용하기 위해 swagger-ui-dist의 SwaggerUIStandalonePreset 사용
  layout: 'StandaloneLayout',
  plugins: [
    CustomLogoPlugin,
    CaseInsensitiveTagFilterPlugin,
    DateTimeSwaggerPlugin,
  ],
  presets: [
    SwaggerUI.presets.apis,
    SwaggerUIStandalonePreset
  ],
  // display
  deepLinking: true,
  displayRequestDuration: true,
  docExpansion: 'none',
  filter: true,
  operationsSorter: PathAndMethodOperationsSorter,
  showCommonExtensions: true,
  syntaxHighlight: {
    theme: 'nord'
  },
  requestSnippetsEnabled: true,
  // network
  validatorUrl: null,
})
