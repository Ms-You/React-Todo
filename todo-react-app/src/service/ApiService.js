import { API_BASE_URL } from '../api-config';

export function call(api, method, request) {
  const options = {
    headers: new Headers({
      "Content-Type": "application/json",
    }),
    url: API_BASE_URL + api,
    method: method,
  };

  if (request) {
    options.body = JSON.stringify(request);
  }

  return fetch(options.url, options)
    .then((response) => {
      return response.json().then((json) => {
        if (!response.ok) {
          return Promise.reject(json);
        }

        return json;
      });
  })
  .catch((error) => {
    console.log("API call error: ", error);
    throw error;
  });
}