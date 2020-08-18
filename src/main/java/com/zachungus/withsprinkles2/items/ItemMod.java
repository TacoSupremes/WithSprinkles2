package com.zachungus.withsprinkles2.items;

import com.zachungus.withsprinkles2.WithSprinkles2;
import net.minecraft.item.Item;

public abstract class ItemMod extends Item
{
    public ItemMod()
    {
        super(ItemMod.getDefaultProps());
    }

    public ItemMod(Properties props)
    {
        super(props);
    }

    public static Properties getDefaultProps()
    {
        return new Item.Properties().group(WithSprinkles2.TAB);
    }

    public abstract String getItemRegistryName();
}
