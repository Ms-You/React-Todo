import Todo from './Todo';
import AddTodo from './AddTodo';
import { Paper, List, Button } from "@material-ui/core";
import DeleteIcon from '@mui/icons-material/Delete';
import CheckIcon from '@mui/icons-material/Check';
<<<<<<< Updated upstream
import { useEffect, useState } from 'react';
<<<<<<< Updated upstream
import instance from '../service/Interceptor';
=======
import { call } from '../service/ApiService';
=======
import { useCallback, useEffect, useState } from 'react';
import instance from '../service/Interceptor';
import { useRecoilValue } from 'recoil';
import { authState } from '../store/atom';
>>>>>>> Stashed changes
>>>>>>> Stashed changes

const TodoContainer = () => {
  const [items, setItems] = useState([]);
  const [selectedItems, setSelectedItems] = useState([]);
  const { isAuthenticated } = useRecoilValue(authState);

  // Todo 목록 조회
  const fetchTodos = useCallback(async () => {
    try {
<<<<<<< Updated upstream
      const todos = await instance.get('/todo');
      setItems(todos.data.result);
=======
<<<<<<< Updated upstream
      const todos = await call('/todo', 'GET');
      setItems(todos.result);
=======
      // 로그인 여부에 따라 API 다르게 호출
      const todos = isAuthenticated ? await instance.get('/auth/todo') : await instance.get('/todo');
      setItems(todos.data.result);
>>>>>>> Stashed changes
>>>>>>> Stashed changes
    } catch (error) {
      console.log('Error fetching todos: ', error);
      window.alert('Todo 목록을 가져오는 데 실패했습니다.');
    }
  }, [isAuthenticated]);

  // 컴포넌트가 마운트될 때 API에서 Todo 목록을 가져옴
  useEffect(() => {
    fetchTodos();
  }, [fetchTodos]);

  // Todo 항목 전체 선택/해제 토글
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

  // Todo 항목 추가
  const addItem = async (newItem) => {
    newItem.done = false;

    try {
<<<<<<< Updated upstream
      const addedItem = await instance.post('/todo', newItem);
      
      setItems([...items, addedItem.data.result]);
=======
<<<<<<< Updated upstream
      const addedItem = await call('/todo', 'POST', newItem);
      
      setItems([...items, addedItem.result]);
=======
      // 로그인 여부에 따라 API 다르게 호출
      const addedItem = isAuthenticated ? await instance.post('/auth/todo', newItem) : await instance.post('/todo', newItem);
      setItems([...items, addedItem.data.result]);
>>>>>>> Stashed changes
>>>>>>> Stashed changes
    } catch (error) {
      console.log('Error adding new todo', error);
    }
  }

  // Todo 항목 삭제
  const deleteItem = async (item) => {
    try {
<<<<<<< Updated upstream
      await instance.delete(`/todo/${item.id}`);
=======
<<<<<<< Updated upstream
      await call(`/todo/${item.id}`, 'DELETE');
=======
      // 로그인 여부에 따라 API 다르게 호출
      isAuthenticated ? await instance.delete(`/auth/todo/${item.id}`) : await instance.delete(`/todo/${item.id}`);
>>>>>>> Stashed changes
>>>>>>> Stashed changes
      const itemsWithoutDeletedItem = items.filter(i => i.id !== item.id);

      setItems(itemsWithoutDeletedItem);
      setSelectedItems(selectedItems.filter(id => id !== item.id));
      console.log("Items after deleted: ", items);
    } catch (error) {
      console.log('Error deleting todo: ', error);
    }
  }

  // 선택한 Todo 항목 삭제
  const deleteSelectedItems = async () => {
    try {
<<<<<<< Updated upstream
      await Promise.all(selectedItems.map(id => instance.delete(`/todo/${id}`)));
=======
<<<<<<< Updated upstream
      await Promise.all(selectedItems.map(id => call(`/todo/${id}`, 'DELETE')));
=======
      await Promise.all(selectedItems.map(id => 
        // 로그인 여부에 따라 API 다르게 호출
        isAuthenticated ? instance.delete(`/auth/todo/${id}`) : instance.delete(`/todo/${id}`)
      ));
>>>>>>> Stashed changes
>>>>>>> Stashed changes
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

  // Todo 항목 수정
  const updateItem = async (updateItem) => {
    try {
<<<<<<< Updated upstream
      const updatedResponse = await instance.put(`/todo/${updateItem.id}`, updateItem);
=======
<<<<<<< Updated upstream
      const updatedResponse = await call(`/todo/${updateItem.id}`, 'PUT', updateItem);
>>>>>>> Stashed changes
      
=======
      // 선택한 Todo 항목 삭제
      const updatedResponse = isAuthenticated ? await instance.put(`/auth/todo/${updateItem.id}`, updateItem) : await instance.put(`/todo/${updateItem.id}`, updateItem);
>>>>>>> Stashed changes
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
