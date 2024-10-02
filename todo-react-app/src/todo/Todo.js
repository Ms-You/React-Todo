import { ListItem, ListItemText, InputBase, Checkbox, ListItemSecondaryAction, IconButton, Collapse, Divider } from "@material-ui/core";
import { DeleteOutlined } from "@material-ui/icons";
import { useState } from "react";

const Todo = ({ item, deleteItem, updateItem }) => {
  const [title, setTitle] = useState(item.title);
  const [description, setDescription] = useState(item.description);
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
      const { name, value } = e.target;
      setReadOnly(true);

      if (name === 'description') {
        updateItem({ ...item, description: value });
      } else {
        updateItem({ ...item, title: value });
      }
    }
  }

  const editEventHandler = (e) => {
    const { name, value } = e.target;

    if (name === 'title') {
      setTitle(value);
    } else if (name === 'description') {
      setDescription(value);
    }
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
          onClick={toggleDescription}
          onChange={editEventHandler}
          type="text"
          id={item.id.toString()}
          name="title"
          value={title}
          multiline={false}
          fullWidth={true}
          onKeyDown={enterKeyEventHandler}
          style={{ textDecoration: done ? 'line-through' : 'none' }}
        />
        <Divider style={{ margin: '8px 0' }} />
        {description && (
          <Collapse 
            in={showDescription}
            timeout="auto"
            unmountOnExit
          >
            <InputBase 
              inputProps={{
                "aria-label": "description",
                readOnly: readOnly,
              }}
              onChange={editEventHandler}
              type="text"
              id={`description - ${item.id}`}
              name="description"
              value={description}
              multiline={true}
              fullWidth={true}
              onKeyDown={enterKeyEventHandler}
              style={{ textDecoration: done ? 'line-through' : 'none' }}
            />
          </Collapse>
        )}
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
