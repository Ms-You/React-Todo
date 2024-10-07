import * as React from 'react';
import { useNavigate } from 'react-router-dom';
import Box from '@mui/material/Box';
import BottomNavigation from '@mui/material/BottomNavigation';
import BottomNavigationAction from '@mui/material/BottomNavigationAction';
import AppRegistrationIcon from '@mui/icons-material/AppRegistration';
import LoginIcon from '@mui/icons-material/Login';
import LogoutIcon from '@mui/icons-material/Logout';

const BottomNavBar = () => {
  const navigate = useNavigate();
  const [value, setValue] = React.useState(0);

  const goToSignUp = () => {
    navigate('/sign-up');
  }

  const goToSignIn = () => {
    navigate('/sign-in');
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
          label="회원가입" 
          icon={<AppRegistrationIcon />} 
          onClick={goToSignUp}
        />
        <BottomNavigationAction 
          label="로그인" 
          icon={<LoginIcon />}
          onClick={goToSignIn} 
        />
        <BottomNavigationAction 
          label="로그아웃" 
          icon={<LogoutIcon />} 
        />
      </BottomNavigation>
    </Box>
  );
}

export default BottomNavBar;
