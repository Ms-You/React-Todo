import { TextField, Paper, Button, Grid } from "@material-ui/core";
import { useState } from "react";

// addItem 함수를 App.js 컴포넌트에서 전달 받음
const AddTodo = ({ addItem }) => {
  const [item, setItem] = useState({ title: "", description: "" });

  // 새로운 아이템을 입력 받는 함수
  const onInputChange = (e) => {
    const { name, value } = e.target;
    const newItem = { ...item, [name]: value };
    setItem(newItem);
    console.log(newItem);
  }

  // 아이템을 추가하는 함수
  const onButtonClick = () => {
    addItem(item);
    setItem({ title: "", description: "" });
  }

  // Enter로 아이템을 추가하는 함수
  const enterKeyEventHandler = (e) => {
    if (e.key === 'Enter') {
      onButtonClick();
    }
  }

  return (
    <Paper style={{ margin: 16, padding: 16 }}>
      <Grid container spacing={2}>
        <Grid item xs={12}>
          <TextField 
            placeholder="Add Title here" 
            fullWidth
            name="title"
            onChange={onInputChange}
            value={item.title}
          />
        </Grid>
        <Grid item xs={12}>
          <TextField 
            placeholder="Add Description here"
            fullWidth
            name="description"
            onChange={onInputChange}
            onKeyDown={enterKeyEventHandler}
            value={item.description}
            multiline
          />
        </Grid>
        
        <Grid item xs={12}>
          <Button 
            fullWidth 
            color="secondary" 
            variant="outlined"
            onClick={onButtonClick}
          >
            +
          </Button>
        </Grid>
      </Grid>
    </Paper>
  )
}

export default AddTodo;
