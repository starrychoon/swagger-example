import SwaggerUI from 'swagger-ui'
import 'swagger-ui/dist/swagger-ui.css'
import SwaggerUIStandalonePreset from 'swagger-ui-dist/swagger-ui-standalone-preset'
import CustomPlugin from './plugin.jsx'

const ui = SwaggerUI({
  // core
  dom_id: '#swagger-ui',
  urls: [{
    name: 'default',
    url: 'http://localhost:8080/swagger-ui/openapi3.yaml'
  }],
  'urls.primaryName': 'default',
  // plugin system
  // Topbar를 사용하기 위해 swagger-ui-dist의 SwaggerUIStandalonePreset 사용
  layout: 'StandaloneLayout',
  plugins: [
    CustomPlugin
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
  showCommonExtensions: true,
  syntaxHighlight: {
    theme: 'nord'
  },
  requestSnippetsEnabled: true,
  // network
  validatorUrl: null,
})
