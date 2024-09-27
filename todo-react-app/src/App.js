import './App.css';
import Todo from './todo/Todo';
import AddTodo from './todo/AddTodo';
import { Paper, List } from "@material-ui/core";
import { useState } from 'react';

const App = () => {
  const [items, setItems] = useState([]);

  const addItem = (newItem) => {
    newItem.id = "ID-" + items.length;
    newItem.done = false;
    setItems([ ...items, newItem ]);
  }

  return (
    <div className="App">
      <AddTodo addItem={addItem} />
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
