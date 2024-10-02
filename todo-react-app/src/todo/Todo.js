import { ListItem, ListItemText, InputBase, Checkbox, ListItemSecondaryAction, IconButton, Collapse, Typography } from "@material-ui/core";
import { DeleteOutlined } from "@material-ui/icons";
import { useState } from "react";

const Todo = ({ item, deleteItem, updateItem }) => {
  const [title, setTitle] = useState(item.title);
  const [readOnly, setReadOnly] = useState(true);
  const [done, setDone] = useState(item.done);
  const [showDescription, setShowDescription] = useState(false);

  const toggleDescription = () => {
    setShowDescription(!showDescription);
    offReadOnlyMode();
  }

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
          // onClick={offReadOnlyMode}
          onClick={toggleDescription}
          onChange={editEventHandler}
          type="text"
          id={item.id.toString()}
          name={item.id.toString()}
          value={title}
          multiline={true}
          fullWidth={true}
          onKeyDown={enterKeyEventHandler}
        />
        <Collapse in={showDescription} timeout="auto" unmountOnExit>
          <Typography variant="body2" style={{ padding: '8px 16px', backgroundColor: '#f0f0f0', borderRadius: '4px' }}>
            {item.description} {/* 설명을 보여주는 부분 */}
          </Typography>
        </Collapse>
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
