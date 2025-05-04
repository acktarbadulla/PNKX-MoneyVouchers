package com.acktar.moneyvouchers.api;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import com.acktar.moneyvouchers.VouchersPlugin;

public class VoucherAPI {

    public static void giveVoucher(Player player, double amount, String customLore) {
        VouchersPlugin.getInstance().giveVoucher(player, amount, customLore);
    }

    public static boolean isVoucher(Item item) {
        CompoundTag tag = item.getNamedTag();
        return tag != null && tag.contains("voucher_amount");
    }

    public static double getVoucherAmount(Item item) {
        if (!isVoucher(item)) return 0;
        return item.getNamedTag().getDouble("voucher_amount");
    }
}
