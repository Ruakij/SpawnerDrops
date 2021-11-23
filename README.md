# SpawnerDrops

Bukkit-Plugin for Spawner Place&Block configs.
Can differenciate between generated and placed blocks by admins or users.
Supports custom messages for all actions.

<br>

## Features

- Who can break spawners from
  - World (Generated/Worldedit)
  - Admin (Placed by amdin)
  - Other players
- When the block actually drops an item
- Who can place spawners
- Messages for each action of success/fail

<br>

## Usage

1. The break-prerequisite is the first stage and will determine if the spawner will break or not break at all.
2. Then the drop-prerequisite is checked and if met will actually drop the spawner as item.

<br>

## Setup

1. Place the plugin in your plugins folder and resatart the server
2. Set necessary and additional permissions if needed

<br>

### Permissions

- **minecraft.nbt.place** has to be set for players to be able to place spawners (does not allow placing in protected zones)
- **spawnerDrops.admin** to allow placing spawners as source "ADMIN"

<br>

## Examples

### 1

```yaml
break:
  break-prerequisite:
    UNKNOWN:
      canBreak: true
      allowed-items:
        - 'diamond_pickaxe'
      silktouch: false
      msg:
        success: ''
        fail: '§7§oNothing happens, it seems you need a diamond-pickaxe'
    # [..]

drop:
  drop-prerequisite:
    UNKNOWN:
      canDrop: true
      allowed-items: null
      silktouch: false
      msg:
        success: '§7§oThe spawner breaks with a loud *Pop* and falls to the ground to be picked up'
        # As canDrop is true and allowed-items is every item, the fail-message is never used
        fail: ''
    # [..]
```

break-prerequisite is a diamond-pickaxe, but an iron-one is used, then the block **will not break**.

drop-prerequisite is anything, including a diamond-pickaxe, then the **block will drop**.


<br>

### 2

```yaml
break:
  break-prerequisite:
    UNKNOWN:
      canBreak: true
      allowed-items: null
      silktouch: false
      msg:
        success: ''
        # As canBreak is true and allowed-items is every item, the fail-message is never used
        fail: ''
    # [..]

drop:
  drop-prerequisite:
    UNKNOWN:
      canDrop: true
      allowed-items:
        - 'diamond_pickaxe'
      silktouch: false
      msg:
        success: '§7§oThe spawner breaks with a loud *Pop* and falls to the ground to be picked up'
        fail: '§7§o*Poof* The spawner breaks into many pieces and is lost'
    # [..]
```

break-prerequisite is anything, the block **will break** with any tool used.

drop-prerequisite is a diamond-pickaxe, but an iron-one is used, then the item **will not drop**, but the **block will be broken** -> **spawner is lost**


