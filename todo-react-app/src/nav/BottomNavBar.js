import * as React from 'react';
import { useNavigate } from 'react-router-dom';
import Box from '@mui/material/Box';
import BottomNavigation from '@mui/material/BottomNavigation';
import BottomNavigationAction from '@mui/material/BottomNavigationAction';
import AppRegistrationIcon from '@mui/icons-material/AppRegistration';
import HomeIcon from '@mui/icons-material/Home';
import LoginIcon from '@mui/icons-material/Login';
import LogoutIcon from '@mui/icons-material/Logout';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import { authState } from '../store/atom';
import instance from '../service/Interceptor';

const BottomNavBar = () => {
  const navigate = useNavigate();
  const [value, setValue] = React.useState(0);
  const { isAuthenticated } = useRecoilValue(authState);
  const setIsAuth = useSetRecoilState(authState);

  const goToHome = () => {
    navigate('/');
  }

  const goToSignUp = () => {
    navigate('/sign-up');
  }

  const goToSignIn = () => {
    navigate('/sign-in');
  }

  // 로그아웃 API 호출
  const handleLogout = async () => {
    // 쿠키에서 refresh Token 삭제
    try {
      const responseData = await instance.post('/auth/logout');
      // 로컬 스토리지에서 accessToken 삭제
      localStorage.removeItem('accessToken');
      
      // 인증 상태 업데이트
      setIsAuth({ isAuthenticated: false });

      window.alert(responseData.data.message);

      // 메인 페이지로 이동
      navigate('/');
    } catch (error) {
      console.log('Error occured when you logout', error);
      window.alert('로그아웃 중 문제가 발생했습니다.');
    }
  }

  return (
    <Box sx={{ width: 500 }}>
      <BottomNavigation
        showLabels
        value={value}
        onChange={(event, newValue) => {
          setValue(newValue);
        }}
      >
        <BottomNavigationAction 
          label="메인" 
          icon={<HomeIcon />} 
          onClick={goToHome}
        />
        {isAuthenticated ? (
          [
            <BottomNavigationAction 
              key='logout'
              label="로그아웃" 
              icon={<LogoutIcon />}
              onClick={handleLogout}
            />
          ]
        ) : (
          [
            <BottomNavigationAction 
              key='sign-up'
              label="회원가입" 
              icon={<AppRegistrationIcon />} 
              onClick={goToSignUp}
            />,
            <BottomNavigationAction 
              key='sign-in'
              label="로그인" 
              icon={<LoginIcon />}
              onClick={goToSignIn} 
            />
          ]
        )}
      </BottomNavigation>
    </Box>
  );
}

export default BottomNavBar;
