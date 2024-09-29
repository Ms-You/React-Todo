import { ListItem, ListItemText, InputBase, Checkbox, ListItemSecondaryAction, IconButton } from "@material-ui/core";
import { DeleteOutlined } from "@material-ui/icons";

const Todo = ({ item, deleteItem }) => {
  const deleteButtonClick = () => {
    deleteItem(item);
  }

  return (
    <ListItem>
      <Checkbox checked={item.done} disableRipple />
      <ListItemText>
        <InputBase 
          inputProps={{ "aria-label": "naked" }}
          type="text"
          id={item.id}
          name={item.id}
          value={item.title}
          multiline={true}
          fullWidth={true}
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
