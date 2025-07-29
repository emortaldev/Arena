package net.minestom.arena.game.mob;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.arena.utils.ItemUtils;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.enchant.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntFunction;

record ArenaUpgrade(String name, String description, TextColor color, Material material,
                    @Nullable BiConsumer<Player, Integer> apply, @Nullable Consumer<Player> remove,
                    @NotNull IntFunction<String> effect, int cost, float costMultiplier, int maxLevel) {
    public ItemStack itemStack(int level) {
        ItemStack.Builder builder = ItemStack.builder(material)
                .customName(Component.text(name, color))
                .lore(
                        Component.text(description, NamedTextColor.GRAY),
                        Component.empty(),
                        Component.text("Buy this team upgrade for " + cost(level) + " coins", NamedTextColor.GOLD),
                        Component.text(effect.apply(level), NamedTextColor.YELLOW)
                )

                .hideExtraTooltip();

        if (level >= maxLevel) builder.set(DataComponents.ENCHANTMENTS, new EnchantmentList(Enchantment.PROTECTION, 1));

        return ItemUtils.stripItalics(builder.build());
    }

    public int cost(int level) {
        return (int) (cost * Math.pow(costMultiplier, level));
    }
}
