import './App.css';
import SignUp from './sign/sign-up/SignUp';
import SignIn from './sign/sign-in/SignIn';
import { Routes, Route } from 'react-router-dom';
import TodoContainer from './todo/TodoContainer';
import Footer from './nav/Footer';

const App = () => {
  return (
    <div className="App" style={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      <Routes>
        <Route path='/' element={<Footer />}>
          <Route index element={<TodoContainer />} />
          <Route path='/sign-up' element={<SignUp />} />
          <Route path='/sign-in' element={<SignIn />} />
        </Route>
      </Routes>
    </div>
  );
}

export default App;
