function fn() {
  console.log('Cargando karate-config.js...');
  
  base64Encode = function(input) {
      return java.util.Base64.getEncoder().encodeToString(new java.lang.String(input).getBytes());
  };


  generateBasicAuthHeader = function(username, password) {
      var credentials = username + ':' + password;
      var encodedCredentials = base64Encode(credentials);
      return 'Basic ' + encodedCredentials;
  };

  var config = {
      baseUrl: 'http://localhost:15672', 
      authHeader: generateBasicAuthHeader('admin', 'admin123') 
  };

  return config;
}
  