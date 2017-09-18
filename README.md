## DL

Dream List

### UI

#### Main Page

- List
- Button (add)
- Settings (icon)

#### Add Page

#### Edit Page

#### Remove Confirm + Mark Confirm

#### Stats Page


### Data Schema

#### List

Array of ListItem.

#### ListItem

```
{
  title : String,
  description : String,
  tags : String,
  group : String,
  created : Date,
  updated : Date,
  finished : Date,
  ddl : Date,
  status : Enum("new", "outdated", "done", "removed"),
  progress : float,
  repeated : boolean
}
```

### Features

- [ ] add an new item
- [ ] remove an item from the list
- [ ] edit an item
- [ ] mark an item as DONE
- [ ] show progress on an item
- [ ] group items (and tag them)

Advance:

- [ ] auto recognize (similar to add item to google calendar)
- [ ] sync with calendar
