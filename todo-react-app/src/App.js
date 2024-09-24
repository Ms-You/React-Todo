// 함수형 컴포넌트
import './App.css';
import Todo from './Todo';
import { Paper, List } from "@material-ui/core";

function App() {
  const items = [
    { id: 0, title: 'Hello world 1', done: true },
    { id: 1, title: 'Hello world 2', done: false },
  ];

  return (
    <div className="App">
      {items.length > 0 && (
        <Paper style={{ margin: 16 }}>
          <List>
            {items.map(item => (
              <Todo key={item.id} item={item} />
            ))}
          </List>
        </Paper>
      )}
    </div>
  );
}

export default App;

// 클래스형 컴포넌트
// import './App.css';
// import Todo from './Todo';
// import React from 'react';

// class App extends React.Component {
//   constructor(props) {
//     super(props);
//     this.state = {
//       item: { id: 0, title: "Hello, world!", done: false },
//     };
//   }

//   render() {
//     return (
//       <div className="App">
//         <Todo item={this.state.item} />
//       </div>
//     )
//   }
// }

// export default App;