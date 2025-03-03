function fn() {
  var env = karate.env; // get system property 'karate.env'
  karate.log('karate.env:', env);
  
  if (!env) {
    env = 'dev';
  }
  
  var config = {
    baseUrl: 'http://localhost:8080'
  }
  
  if (env == 'dev') {
    // customize for dev environment
  } else if (env == 'qa') {
    // customize for qa environment
    config.baseUrl = 'http://qa-api.yourcompany.com';
  }
  
  return config;
}