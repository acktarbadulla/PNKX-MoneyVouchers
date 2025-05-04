package com.acktar.moneyvouchers;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import net.lldv.llamaeconomy.LlamaEconomy;

public class MakeVoucherCommand extends Command {

    private final VouchersPlugin plugin;

    public MakeVoucherCommand(VouchersPlugin plugin) {
        super("makevoucher", "Create a money voucher", "/makevoucher <amount> <lore>");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(TextFormat.RED + "Usage: /makevoucher <amount> <lore...>");
            return true;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(plugin.getConfig().getString("invalid_amount"));
            return true;
        }

        double min = plugin.getConfig().getDouble("min_amount", 10);
        double max = plugin.getConfig().getDouble("max_amount", 10000);

        if (amount < min || amount > max) {
            String outOfRange = plugin.getConfig().getString("out_of_range")
                    .replace("{min}", String.valueOf(min))
                    .replace("{max}", String.valueOf(max));
            sender.sendMessage(outOfRange);
            return true;
        }

        Player player = (Player) sender;
        double balance = LlamaEconomy.getAPI().getMoney(player);

        if (amount > balance) {
            sender.sendMessage(plugin.getConfig().getString("not_enough_money"));
            return true;
        }

        String lore = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
        LlamaEconomy.getAPI().reduceMoney(player, amount);
        plugin.giveVoucher(player, amount, lore);

        String success = plugin.getConfig().getString("success")
                .replace("{amount}", String.valueOf(amount));
        sender.sendMessage(success);
        return true;
    }
}
