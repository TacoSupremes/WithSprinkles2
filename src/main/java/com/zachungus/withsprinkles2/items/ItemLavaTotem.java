package com.zachungus.withsprinkles2.items;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemLavaTotem extends ItemMod
{
    private static final String IN_LAVA = "IN_LAVA";

    @Override
    public String getItemRegistryName()
    {
        return "lava_totem";
    }

    public ItemLavaTotem()
    {
        super(ItemMod.getDefaultProps().maxStackSize(1).rarity(Rarity.UNCOMMON));
    }

    public boolean isFirstInHotBar(Entity e, ItemStack stack, int slot)
    {
        if(e instanceof PlayerEntity)
        {
            PlayerEntity p =((PlayerEntity)e);

            if(!p.inventory.offHandInventory.isEmpty() && p.inventory.offHandInventory.get(0).isItemEqual(stack))
            {
                return slot == 0;
            }

            for(int i = 0; i< 9; i++)
            {
                if(p.inventory.getStackInSlot(i).isItemEqual(stack))
                {
                    return i == slot;
                }
            }
        }
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, World w, Entity e, int itemSlot, boolean isSelected)
    {
        if(!isFirstInHotBar(e, stack, itemSlot))
                return;

        if(!stack.hasTag())
        {
            stack.setTag(new CompoundNBT());
        }

        if(itemSlot < 9)
        {
            if(e.isInLava())
            {
                if(!stack.getTag().getBoolean(IN_LAVA))
                {
                    stack.getTag().putBoolean(IN_LAVA, true);
                }
                    if(e.getMotion().y < 1)
                    {
                        Vec3d v3 = e.getMotion();

                        if(v3.getY() < 0)
                        {
                            v3 = new Vec3d(0,0, 0);
                        }

                        v3 = new Vec3d(0,v3.y + 0.25D,0);

                        if(v3.y > 1)
                        {
                            v3 = new Vec3d(0,1,0);
                        }

                        e.setMotion(v3);
                    }

                    if(!w.isRemote)
                    {
                        for(int x = -1; x <= 1; x++)
                            for(int z = -1; z <= 1; z++)
                                w.setBlockState(e.getPosition().add(x,0, z), Blocks.OBSIDIAN.getDefaultState());
                    }

                    e.setPosition(e.getPosX(), e.getPosY() + 0.25D, e.getPosZ());
            }
            else if(stack.getTag().getBoolean(IN_LAVA))
            {
                if(e.isBurning())
                {
                    if(e instanceof LivingEntity)
                        ((LivingEntity)e).heal(2);

                    e.extinguish();
                }
                stack.setCount(0);

                if(w.isRemote)
                {
                    w.playSound(((PlayerEntity)e),e.getPosition(), SoundEvents.ENTITY_BLAZE_AMBIENT, SoundCategory.AMBIENT,1, 1);
                }
            }
        }
    }
}
