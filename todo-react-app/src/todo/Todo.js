import { ListItem, ListItemText, InputBase, Checkbox, ListItemSecondaryAction, IconButton } from "@material-ui/core";
import { DeleteOutlined } from "@material-ui/icons";
import { useState } from "react";

const Todo = ({ item, deleteItem, updateItem }) => {
  const [title, setTitle] = useState(item.title);
  const [readOnly, setReadOnly] = useState(true);
  const [done, setDone] = useState(item.done);

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

  const checkboxEventHandler = async (e) => {
    const updatedItem = { ...item, done: !done };
    setDone(updatedItem.done);
    await updateItem(updatedItem);
  }

  return (
    <ListItem>
      <Checkbox 
        checked={done} 
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
          id={item.id.toString()}
          name={item.id.toString()}
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
