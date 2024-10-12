import Todo from './Todo';
import AddTodo from './AddTodo';
import { Paper, List, Button } from "@material-ui/core";
import DeleteIcon from '@mui/icons-material/Delete';
import CheckIcon from '@mui/icons-material/Check';
import { useEffect, useState } from 'react';
import { call } from '../service/ApiService';

const TodoContainer = () => {
  const [items, setItems] = useState([]);
  const [selectedItems, setSelectedItems] = useState([]);

  // 컴포넌트가 마운트될 때 API에서 Todo 목록을 가져옴
  useEffect(() => {
    fetchTodos();
  }, []);

  const fetchTodos = async () => {
    try {
      const todos = await call('/todo', 'GET');
      
      setItems(todos.data.result);
      
    } catch (error) {
      console.log('Error fetching todos: ', error);
    }
  }

  const toggleAllItems = () => {
    const allDone = items.every(item => item.done);
    const updatedItems = items.map(item => ({ ...item, done: !allDone }));

    setItems(updatedItems);

    if (allDone) {
      setSelectedItems([]);
    } else {
      setSelectedItems(items.map(item => item.id));
    }
  }

  const addItem = async (newItem) => {
    newItem.done = false;

    try {
      const addedItem = await call('/todo', 'POST', newItem);
      
      setItems([...items, addedItem.data.result]);
    } catch (error) {
      console.log('Error adding new todo', error);
    }
  }

  const deleteItem = async (item) => {
    try {
      await call(`/todo/${item.id}`, 'DELETE');
      const itemsWithoutDeletedItem = items.filter(i => i.id !== item.id);

      setItems(itemsWithoutDeletedItem);
      setSelectedItems(selectedItems.filter(id => id !== item.id));
      console.log("Items after deleted: ", items);
    } catch (error) {
      console.log('Error deleting todo: ', error);
    }
  }

  const deleteSelectedItems = async () => {
    try {
      await Promise.all(selectedItems.map(id => call(`/todo/${id}`, 'DELETE')));
      setItems(items.filter(item => !selectedItems.includes(item.id)));
      setSelectedItems([]);
    } catch (error) {
      console.log("Error deleting selected todos: ", error);
    }
  }

  const toggleItemSelection = (itemId) => {
    setSelectedItems(prevSelected => 
      prevSelected.includes(itemId) 
        ? prevSelected.filter(id => id !== itemId) 
        : [...prevSelected, itemId]
    );
  }

  const updateItem = async (updateItem) => {
    try {
      const updatedResponse = await call(`/todo/${updateItem.id}`, 'PUT', updateItem);
      
      const newItems = items.map(item => 
        item.id === updatedResponse.data.result.id ? updatedResponse.data.result : item
      );
  
      setItems(newItems);
    } catch (error) {
      console.log('Error updating todo: ', error);
    }
  }

  return (
    <div style={{ display: 'flex', flexDirection: 'column' }}>
      <AddTodo addItem={addItem} />
      {items.length > 0 && (
        <Paper style={{ margin: 16 }}>
          <List>
            {items.map(item => (
              <Todo 
                key={item.id}
                item={item}
                selectedItems={selectedItems}
                deleteItem={deleteItem} 
                updateItem={updateItem}
                toggleItemSelection={toggleItemSelection}
              />
            ))}
          </List>
        </Paper>
      )}
      <div style={{ display: 'flex', justifyContent: 'center', gap: '16px', margin: '16px 0'}}>
        <Button 
          variant="outlined" 
          startIcon={<CheckIcon />}
          onClick={toggleAllItems}
        >
          전체 선택/해제
        </Button>
        <Button 
          variant="outlined" 
          endIcon={<DeleteIcon />}
          onClick={deleteSelectedItems}
        >
          선택된 항목 삭제
        </Button>
      </div>
    </div>
  );
}

export default TodoContainer;
