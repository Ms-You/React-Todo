import './App.css';
import SignUp from './sign/sign-up/SignUp';
import SignIn from './sign/sign-in/SignIn';
import BottomNavBar from './nav/BottomNavBar';
import { Routes, Route } from 'react-router-dom';
import TodoContainer from './todo/TodoContainer';

const App = () => {
  return (
    <div className="App" style={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      <Routes>
        <Route path='/sign-up' element={<SignUp />} />
        <Route path='/sign-in' element={<SignIn />} />
        <Route path='/' element={
          <>
          <TodoContainer />
          <div style={{ marginTop: 'auto', display: 'flex', justifyContent: 'center', alignItems: 'center', marginBottom: '3rem' }}>
            <BottomNavBar />
          </div>
          </>
        } />
      </Routes>
    </div>
  );
}

export default App;
