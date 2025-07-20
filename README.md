# OnlyTP

A Minecraft Forge mod that provides enhanced teleportation functionality. Easily teleport to your friends to help them.

## Features

- **Simple Commands**: Easy-to-use commands for teleporting players
- **Riding Entity Teleportation**: When teleporting while riding a living entity (horse, pig, etc.), both player and mount are teleported together
- **No Dependencies**: This mod does not require any additional libraries or mods to function

## Usage

Run the following commands in-game:
- `/tlp <player>`: Teleports you to the specified player.

### Riding Entity Teleportation
When you use `/tlp` while riding a living entity (such as a horse, pig, donkey, mule, llama, camel, or strider), both you and your mount will be teleported together to the destination. The riding relationship is maintained after teleportation.

**Supported Living Entities:**
- Horses, Donkeys, Mules
- Pigs, Striders  
- Llamas, Camels
- Any other rideable living entities

**Note:** Non-living vehicles like boats and minecarts are not teleported - only the player will be teleported in these cases.

**Note**: This mod is not recommended for use on PvP servers as it may provide unfair advantages.

## Compatibility

- **Minecraft Version**: 1.20.1
- **Forge Version**: 47.4.0+
- **Side**: Only server is required, client is optional.
  - Install on client for localized messages, otherwise server language will be used.

## License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.
