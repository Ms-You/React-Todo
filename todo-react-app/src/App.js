import './App.css';
import Todo from './todo/Todo';
import AddTodo from './todo/AddTodo';
import { Paper, List } from "@material-ui/core";
import { useEffect, useState } from 'react';
import { call } from './service/ApiService';

const App = () => {
  const [items, setItems] = useState([]);

  // 컴포넌트가 마운트될 때 API에서 Todo 목록을 가져옴
  useEffect(() => {
    fetchTodos();
  }, []);

  const fetchTodos = async () => {
    try {
      const todos = await call('/todo', 'GET');
      setItems(todos.result);
    } catch (error) {
      console.log('Error fetching todos: ', error);
    }
  }

  const addItem = async (newItem) => {
    newItem.id = "ID-" + items.length;
    newItem.done = false;

    try {
      const addedItem = await call('/todo', 'POST', newItem);
      
      setItems([...items, addedItem.result]);
    } catch (error) {
      console.log('Error adding new todo', error);
    }
  }

  const deleteItem = async (item) => {
    try {
      await call(`/todo/${item.id}`, 'DELETE');
      const itemsWithoutDeletedItem = items.filter(i => i.id !== item.id);

      setItems(itemsWithoutDeletedItem);
      console.log("Items after deleted: ", items);
    } catch (error) {
      console.log('Error deleting todo: ', error);
    }
  }

  const updateItem = async (updateItem) => {
    try {
      const updatedResponse = await call(`/todo/${updateItem.id}`, 'PUT', updateItem);
      
      const newItems = items.map(item => 
        item.id === updatedResponse.result.id ? updatedResponse.result : item
      );
  
      setItems(newItems);
    } catch (error) {
      console.log('Error updating todo: ', error);
    }
  }

  return (
    <div className="App">
      <AddTodo addItem={addItem} />
      {items.length > 0 && (
        <Paper style={{ margin: 16 }}>
          <List>
            {items.map(item => (
              <Todo 
                key={item.id} 
                item={item} 
                deleteItem={deleteItem} 
                updateItem={updateItem}
              />
            ))}
          </List>
        </Paper>
      )}
    </div>
  );
}

export default App;
