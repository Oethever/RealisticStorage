## What does it do?

All large blocks (such as dirt, cobble, stairs, etc) are not permitted anymore in chests, etc. You gotta find alternative storage solution for these (see below). Only items and small blocks (torches, flowers, etc) are permitted in chests. This is a fork from [OversizedItemInStorageArea](https://www.curseforge.com/minecraft/mc-mods/oversized-item-in-storage-area "OversizedItemInStorageArea"), adapted to work as a standalone mod and with a different size criterion.

Since the distinction between allowed / denied items is somewhat arbitrary, the mod config provides full control on the filtering.

This mod adds a new block: the Pallet (comes in all vanilla Wood variants). All blocks placed on the pallet (up to 8 blocks above it) can be broken instantly without any tool. Time to build some warehouses!

Alternately, you can also use other storage solutions such as [Impractical Storage](https://www.curseforge.com/minecraft/mc-mods/impractical-storage "Impractical Storage").

## How to use it?

**Mod support is possible, but requires manual config!**

By default, only vanilla chests are checked. Anything in the chest is ejected if it can be placed in the world. Exceptions to this simple rule (black list and white list) are defined in the config file, and a decent default config is provided for vanilla Minecraft. Regular expressions (regex) are allowed, in order to make config a bit faster.
