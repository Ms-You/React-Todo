import axios from 'axios';

const instance = axios.create({
  baseURL: 'http://localhost:8080',
  withCredentials: true, // 쿠키 전송을 위함
});

// 요청 인터셉터
instance.interceptors.request.use(
  config => {
    const accessToken = localStorage.getItem('accessToken');

    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    }

    return config;
  }, 
  error => {
    return Promise.reject(error);
  }
);

// 응답 인터셉터
instance.interceptors.response.use(
  response => {
    return response;
  },
  async error => {
    const originalRequest = error.config;
    
    // 401 에러 처리
    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      
      try {
        // RefreshToken으로 토큰 재발급 API 호출
        const res = await axios.post('/reissue', {}, { withCredentials: true });

        // RefreshToken이 유효하지 않은 경우 로그아웃
        if (res.status === 403 || res.status === 700) {
          localStorage.removeItem('accessToken');
          return Promise.reject(error);
        }

        const newAuthorizationHeader = res.headers['Authorization'];
        const reissuedAccessToken = newAuthorizationHeader ? newAuthorizationHeader.replace('Bearer ', '') : null;

        if (reissuedAccessToken) {
          localStorage.setItem('accessToken', reissuedAccessToken);
          instance.defaults.headers.common.Authorization = `Bearer ${reissuedAccessToken}`;
          originalRequest.headers.Authorization = `Bearer ${reissuedAccessToken}`;

          return instance(originalRequest); // 원래 호출했던 요청 재전송
        }
      } catch (refreshError) {
        localStorage.removeItem('accessToken');
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);

export default instance;
