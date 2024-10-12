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
      return response.json().then((data) => {
        if (!response.ok) {
          return Promise.reject(data);
        }

        return { data, response };
      });
  })
  .catch((error) => {
    console.log("API call error: ", error);
    throw error;
  });
}