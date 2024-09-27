import { TextField, Paper, Button, Grid } from "@material-ui/core";
import { useState } from "react";

// addItem 함수를 App.js 컴포넌트에서 전달 받음
const AddTodo = ({addItem}) => {
  const [item, setItem] = useState({ title: "" });

  // 새로운 아이템을 입력 받는 함수
  const onInputChange = (e) => {
    const newItem = { ...item, title: e.target.value };
    setItem(newItem);
    console.log(newItem);
  }

  // 아이템을 추가하는 함수
  const onButtonClick = () => {
    addItem(item);
    setItem({ title: "" });
  }

  // Enter로 아이템을 추가하는 함수
  const enterKeyEventHandler = (e) => {
    if (e.key === 'Enter') {
      onButtonClick();
    }
  }

  return (
    <Paper style={{ margin: 16, padding: 16 }}>
      <Grid container>
        <Grid xs={11} md={11} item style={{ paddingRight: 16 }}>
          <TextField 
            placeholder="Add Todo Here" 
            fullWidth
            onChange={onInputChange}
            onKeyDown={enterKeyEventHandler}
            value={item.title}
          />
        </Grid>
        <Grid xs={1} md={1} item>
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
