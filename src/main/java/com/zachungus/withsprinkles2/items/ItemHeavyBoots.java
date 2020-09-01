package com.zachungus.withsprinkles2.items;

import com.zachungus.withsprinkles2.WithSprinkles2;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemHeavyBoots extends ArmorItem implements IModItem
{
    public ItemHeavyBoots()
    {
        super(ArmorMaterial.IRON, EquipmentSlotType.FEET, ItemMod.getDefaultProps());
    }

    @Override
    public String getItemRegistryName()
    {
        return "heavy_boots";
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player)
    {
        if(player.isInWater())
        {
           //WithSprinkles2.LOGGER.warn(player.getMotion().toString());


            if(!player.collidedHorizontally)
                player.setMotion(player.getMotion().add(0,-0.1D,0));
            else
                player.jump();
        }
    }
}
