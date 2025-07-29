package net.minestom.arena.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.Contract;

import java.util.List;

public final class ItemUtils {
    private ItemUtils() {
    }

    @Contract("null -> null; !null -> !null")
    public static Component stripItalics(Component component) {
        if (component == null) return null;

        if (component.decoration(TextDecoration.ITALIC) == TextDecoration.State.NOT_SET) {
            component = component.decoration(TextDecoration.ITALIC, false);
        }

        return component;
    }

    @Contract("null -> null; !null -> !null")
    public static ItemStack stripItalics(ItemStack itemStack) {
        if (itemStack == null) return null;

        Component previousName = itemStack.get(DataComponents.CUSTOM_NAME);
        List<Component> previousLore = itemStack.get(DataComponents.LORE);

        return itemStack
                .with(DataComponents.CUSTOM_NAME, stripItalics(previousName))
                .with(DataComponents.LORE, previousLore.stream().map(ItemUtils::stripItalics).toList());
    }
}
