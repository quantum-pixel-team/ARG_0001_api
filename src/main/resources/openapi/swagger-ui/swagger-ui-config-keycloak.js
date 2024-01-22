window.onload = function () {
    // Begin Swagger UI call region
    const portPart = window.location.port ? ":" + window.location.port : "";
    const ui = SwaggerUIBundle({
        url: "/openapi/api-v1.0.yaml",
        dom_id: '#swagger-ui',
        deepLinking: true,
        presets: [
            SwaggerUIBundle.presets.apis,
            SwaggerUIStandalonePreset
        ],
        plugins: [
            SwaggerUIBundle.plugins.DownloadUrl
        ],
        layout: "StandaloneLayout",
        oauth2RedirectUrl: window.location.protocol + "//" + window.location.hostname + portPart + "/swagger-ui/oauth2-redirect.html",
        withCredentials: true
    })
    // End Swagger UI call region

    ui.initOAuth({
        clientId: "boilerplate-oidc",
        scopes: "openid profile",
        usePkceWithAuthorizationCodeGrant: true
    });

    window.ui = ui;
}
