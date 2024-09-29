import { ListItem, ListItemText, InputBase, Checkbox, ListItemSecondaryAction, IconButton } from "@material-ui/core";
import { DeleteOutlined } from "@material-ui/icons";
import { useState } from "react";

const Todo = ({ item, deleteItem, updateItem }) => {
  const [title, setTitle] = useState(item.title);
  const [readOnly, setReadOnly] = useState(true);

  const deleteButtonClick = () => {
    deleteItem(item);
  }

  const offReadOnlyMode = () => {
    setReadOnly(false);
  }

  const enterKeyEventHandler = (e) => {
    if (e.key === 'Enter') {
      setReadOnly(true);
      updateItem({ ...item, title });
      console.log(title);
    }
  }

  const editEventHandler = (e) => {
    setTitle(e.target.value);
    console.log(title);
    
  }

  const checkboxEventHandler = (e) => {
    updateItem({ ...item, done: !item.done })
    console.log(!item.done);
    
  }

  return (
    <ListItem>
      <Checkbox 
        checked={item.done} 
        onChange={checkboxEventHandler} 
      />
      <ListItemText>
        <InputBase 
          inputProps={{ 
            "aria-label": "naked",
            readOnly: readOnly,
          }}
          onClick={offReadOnlyMode}
          onChange={editEventHandler}
          type="text"
          id={item.id}
          name={item.id}
          value={title}
          multiline={true}
          fullWidth={true}
          onKeyDown={enterKeyEventHandler}
        />
      </ListItemText>

      <ListItemSecondaryAction>
        <IconButton 
          aria-label="Delete Todo"
          onClick={deleteButtonClick}
        >
          <DeleteOutlined />
        </IconButton>
      </ListItemSecondaryAction>
    </ListItem>
  );
}

export default Todo;
