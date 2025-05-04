# MoneyVouchers

**MoneyVouchers** is a PowerNukkitX plugin that lets players convert in-game currency into voucher items and redeem them later using [LlamaEconomy](https://github.com/PowerNukkitX-Bundle/LlamaEconomy/releases/tag/1.0.0).

## Features

- Create vouchers via `/makevoucher <amount> <lore>`
- Store money value inside paper items with NBT
- Redeem vouchers by right-clicking
- Voucher item glows with an enchantment
- Fully configurable min/max values and messages
- Redemption broadcasts and sounds

## Commands

### `/makevoucher <amount> <lore>`
- Deducts money from the player
- Adds it to a voucher (paper item)
- Customizable item lore
- Limits set by config
