package com.acktar.moneyvouchers;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemPaper;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import net.lldv.llamaeconomy.LlamaEconomy;
import cn.nukkit.item.enchantment.Enchantment;

public class VouchersPlugin extends PluginBase implements Listener {

    private static VouchersPlugin instance;
    private Config config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
    
        if (getServer().getPluginManager().getPlugin("LlamaEconomy") == null ||
        !getServer().getPluginManager().getPlugin("LlamaEconomy").isEnabled()) {
        getLogger().error("LlamaEconomy is required for VouchersPlugin. Disabling...");
        getServer().getPluginManager().disablePlugin(this);
        return;
        }
        
        instance = this;
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getCommandMap().register("makevoucher", new MakeVoucherCommand(this));
        this.getLogger().info("VouchersPlugin enabled.");
    }

    public static VouchersPlugin getInstance() {
        return instance;
    }

    public void giveVoucher(Player player, double amount, String customLore) {
        ItemPaper paper = new ItemPaper();
        paper.setCustomName("§l§e$" + amount + " §eMoney Voucher");
        paper.setLore(customLore, "Right-click to redeem");
        paper.addEnchantment(Enchantment.get(0));
        CompoundTag tag = paper.getNamedTag(); 
        if (tag == null) {
            tag = new CompoundTag();
        }
        tag.putDouble("voucher_amount", amount); 
        paper.setNamedTag(tag);
        player.getInventory().addItem(paper);
    }

@EventHandler
public void onInteract(PlayerInteractEvent event) {
    if (event.getAction() != PlayerInteractEvent.Action.RIGHT_CLICK_AIR &&
        event.getAction() != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
        return;
    }

    Item item = event.getItem();
    Player player = event.getPlayer();

    if (item instanceof ItemPaper && item.hasCompoundTag() && item.getNamedTag().contains("voucher_amount")) {
        double amount = item.getNamedTag().getDouble("voucher_amount");

        LlamaEconomy.getAPI().addMoney(player, amount);

        String msg = this.getConfig().getString("message")
                .replace("{amount}", String.valueOf(amount));
        String title = this.getConfig().getString("title");
        String subtitle = this.getConfig().getString("subtitle")
                .replace("{amount}", String.valueOf(amount));
        String broadcast = this.getConfig().getString("broadcast")
                .replace("{player}", player.getName())
                .replace("{amount}", String.valueOf(amount));

        player.sendMessage(msg);
        player.sendTitle(TextFormat.GOLD + title, TextFormat.GREEN + subtitle, 10, 40, 10);

        player.getLevel().addSound(player, cn.nukkit.level.Sound.RANDOM_LEVELUP);

        player.getInventory().removeItem(item);
        
        player.getServer().broadcastMessage(broadcast);

        event.setCancelled(true);
    }
}

}
