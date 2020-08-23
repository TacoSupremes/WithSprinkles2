package com.zachungus.withsprinkles2.events;

import com.zachungus.withsprinkles2.enchants.ModEnchantments;
import com.zachungus.withsprinkles2.items.ModItems;
import com.zachungus.withsprinkles2.lib.LibMisc;
import com.zachungus.withsprinkles2.recipes.EnchantedBookRecipe;
import com.zachungus.withsprinkles2.recipes.ModRecipes;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LogBlock;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.*;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map;

public class WS2Events
{
    @SubscribeEvent
    public static void onPlayerCrafted(PlayerEvent.ItemCraftedEvent event)
    {

        if(!event.getCrafting().isEmpty() && event.getCrafting().getItem() == Items.ENCHANTED_BOOK)
        {
            IInventory inv = event.getInventory();

            int oldPaper = 0;

            for (int i = 0; i < inv.getSizeInventory(); i++)
            {
                ItemStack invs = inv.getStackInSlot(i);

                if (!invs.isEmpty() && invs.getItem() == ModItems.LOST_PAGE.get())
                    oldPaper++;
            }

            oldPaper--;
            EnchantedBookRecipe.enchant.set(oldPaper, null);
        }
    }




    @SubscribeEvent
    public static void onPlayerBreaking(BlockEvent.BreakEvent event)
    {
        if (!event.getPlayer().getHeldItem(Hand.MAIN_HAND).isEmpty())
        {
            handleFiery(event);

            handleFelling(event);
            handleFracking(event);


            if (!event.getPlayer().isSneaking())
                    handleExchange(event);
        }
    }

    private static void handleFracking(BlockEvent.BreakEvent event)
    {
        ItemStack is = event.getPlayer().getHeldItemMainhand();

        if(is.isEnchanted() && EnchantmentHelper.getEnchantmentLevel(ModEnchantments.FRACKING.get(), is) > 0)
        {
            if(event.getState().getBlock().getRegistryName().getPath().toLowerCase().contains("_ore"))
            {
                int blocksLeft = is.getMaxDamage() - is.getDamage();

                breakConnectedIncludeDiagonal(event.getPlayer().world, event.getPos(), event.getState().getBlock(), is);

             /*
               List<BlockPos> l = getConnectedIncludeDiagonal(new ArrayList<BlockPos>(),event.getPlayer().world, event.getPos(),event.getState().getBlock(), blocksLeft);

               for(BlockPos bp : l)
               {
                   event.getPlayer().getEntityWorld().destroyBlock(bp, true);
                   is.attemptDamageItem(1,event.getWorld().getRandom(), (ServerPlayerEntity) event.getPlayer());
               }

               */
            }

        }

    }

    private static void handleFelling(BlockEvent.BreakEvent event)
    {
       ItemStack is = event.getPlayer().getHeldItemMainhand();


       if(is.isEnchanted() && EnchantmentHelper.getEnchantmentLevel(ModEnchantments.FELLING.get(), is) > 0)
       {
           if(event.getState().getBlock() instanceof LogBlock)
           {
               int blocksLeft = is.getMaxDamage() - is.getDamage();

               breakConnectedIncludeDiagonal(event.getPlayer().world, event.getPos(), event.getState().getBlock(), is);

             /*
               List<BlockPos> l = getConnectedIncludeDiagonal(new ArrayList<BlockPos>(),event.getPlayer().world, event.getPos(),event.getState().getBlock(), blocksLeft);

               for(BlockPos bp : l)
               {
                   event.getPlayer().getEntityWorld().destroyBlock(bp, true);
                   is.attemptDamageItem(1,event.getWorld().getRandom(), (ServerPlayerEntity) event.getPlayer());
               }

               */
           }

       }

    }

    /**does not do diagonal **/
    private static List<BlockPos> getConnected(List<BlockPos> l, World w, BlockPos pos, Block b, int cap)
    {
        l.add(pos);

        if(l.size() == cap)
            return l;

        for(Direction d : Direction.values())
        {
            Block b2 = w.getBlockState(pos.add(d.getDirectionVec())).getBlock();

            if(b == b2 && !l.contains(pos.add(d.getDirectionVec())))
                getConnected(l, w, pos.add(d.getDirectionVec()), b2, cap);

        }

        return l;
    }



    private static void breakConnectedIncludeDiagonal(World w, BlockPos pos, Block b, ItemStack is)
    {
        if(is.isEmpty() || is.getDamage() == is.getMaxDamage())
            return;

        for(int x = -1; x <= 1; x++)
            for(int y = -1; y <= 1; y++)
                for(int z = -1; z <= 1; z++)
                {
                    Vec3i vi = new Vec3i(x, y, z);

                    Block b2 = w.getBlockState(pos.add(vi)).getBlock();

                    if (b == b2)
                    {
                        w.destroyBlock(pos.add(vi),true);
                        is.attemptDamageItem(2, w.rand, null);
                        breakConnectedIncludeDiagonal(w, pos.add(vi), b, is);

                    }
                }
    }

    private static List<BlockPos> getConnectedIncludeDiagonal(List<BlockPos> l, World w, BlockPos pos, Block b, int cap)
    {
        l.add(pos);

        if(l.size() == cap)
            return l;

        for(int x = -1; x <= 1; x++)
            for(int y = -1; y <= 1; y++)
                for(int z = -1; z <= 1; z++)
                {
                    Vec3i vi = new Vec3i(x, y, z);

                    Block b2 = w.getBlockState(pos.add(vi)).getBlock();

                    if (b == b2 && !l.contains(pos.add(vi)))
                        getConnectedIncludeDiagonal(l, w, pos.add(vi), b2, cap);
                }

        return l;
    }

    public static ItemStack getSmeltingResult(World w, ItemStack is)
    {
        Inventory i = new Inventory(3);

        i.setInventorySlotContents(0, is);

        FurnaceRecipe ir = w.getRecipeManager().getRecipe(IRecipeType.SMELTING, i, w).orElse(null);

        if(ir != null)
           return ir.getRecipeOutput();

        return ItemStack.EMPTY;
    }

    private static void handleFiery(BlockEvent.BreakEvent event)
    {
        ItemStack stack = event.getPlayer().getHeldItem(Hand.MAIN_HAND);

        if (stack.isEnchanted() && EnchantmentHelper.getEnchantmentLevel(ModEnchantments.FIERY.get(), stack) > 0 && ForgeHooks.canToolHarvestBlock(event.getWorld(), event.getPos(), stack))
        {
            ItemStack result = getSmeltingResult(event.getPlayer().world, new ItemStack(event.getState().getBlock(),1));

            if (result != ItemStack.EMPTY)
            {
                World w = event.getPlayer().getEntityWorld();
                BlockPos pos = event.getPos();

                if (!w.isRemote)
                {
                    w.destroyBlock(pos, false);
                    w.getBlockState(pos).getBlock();
                    Block.spawnAsEntity(w, pos, new ItemStack(result.getItem(), 1));
                }

                event.getPlayer().getHeldItem(Hand.MAIN_HAND).attemptDamageItem(1, event.getWorld().getRandom(), (ServerPlayerEntity) event.getPlayer());
                event.setCanceled(true);
            }
        }
    }

    private static void handleExchange(BlockEvent.BreakEvent event)
    {

       // event.getPlayer().getAdjustedHorizontalFacing();
        ItemStack stack = event.getPlayer().getHeldItem(Hand.MAIN_HAND);

        if (stack.isEnchanted() && EnchantmentHelper.getEnchantmentLevel(ModEnchantments.EXCHANGE.get(), stack) > 0)
        {
            ItemStack replace = event.getPlayer().getHeldItem(Hand.OFF_HAND);

            if (replace.isEmpty())
                return;

            Block replaceb = Block.getBlockFromItem(replace.getItem());

            if (replaceb == event.getState().getBlock() || replaceb == Blocks.AIR)
                return;

            World w = event.getPlayer().getEntityWorld();

            w.destroyBlock(event.getPos(), true, event.getPlayer());

            Direction d = event.getPlayer().getAdjustedHorizontalFacing().getOpposite();

            replace.getItem().onItemUse(new ItemUseContext(event.getPlayer(), Hand.OFF_HAND, new BlockRayTraceResult(event.getPlayer().getLookVec(), d, event.getPos(), false)));

            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerRightClick(PlayerInteractEvent.RightClickItem event)
    {
        handleMultiple(event);
    }

    private static void handleMultiple(PlayerInteractEvent.RightClickItem event)
    {
        if (!event.getPlayer().isSneaking())
            return;

        if (event.getItemStack() == ItemStack.EMPTY)
            return;

        if (!event.getItemStack().hasTag())
            return;


        if (event.getItemStack().getItem() instanceof ArmorItem || event.getItemStack().getItem() instanceof FishingRodItem)
        {

            event.setCanceled(true);
            event.setCancellationResult(ActionResultType.FAIL);
            return;
        }


        if ((EnchantmentHelper.getEnchantmentLevel(ModEnchantments.MULTIPLE.get(), event.getItemStack()) > 0))
        {

            if (!event.getItemStack().getTag().contains("MULTIPLELVL"))
            {
                event.getItemStack().getTag().putInt("MULTIPLELVL", EnchantmentHelper.getEnchantmentLevel(ModEnchantments.MULTIPLE.get(), event.getItemStack()));
                event.getItemStack().getTag().putInt("MULTIPLEMODE", 1);



                event.getItemStack().getTag().put("ench1", event.getItemStack().getTag().get("Enchantments"));

                if(event.getItemStack().getRepairCost() > 0)
                {
                    event.getItemStack().getTag().putInt("rc1", event.getItemStack().getRepairCost());
                    event.getItemStack().setRepairCost(0);
                }

                if(event.getItemStack().hasDisplayName())
                {
                    event.getItemStack().getTag().putString("name1", event.getItemStack().getDisplayName().getString());
                    event.getItemStack().clearCustomName();
                }

                event.getItemStack().getTag().remove("Enchantments");

            }
            else
            {

                if ((EnchantmentHelper.getEnchantmentLevel(ModEnchantments.MULTIPLE.get(), event.getItemStack()) != event.getItemStack().getTag().getInt("MULTIPLELVL")))
                    event.getItemStack().getTag().putInt("MULTIPLELVL", EnchantmentHelper.getEnchantmentLevel(ModEnchantments.MULTIPLE.get(), event.getItemStack()));

                if (event.getItemStack().getTag().getInt("MULTIPLELVL") > 1)
                {

                    int mode = event.getItemStack().getTag().getInt("MULTIPLEMODE");

                    switch (mode)
                    {


                        case (0) :
                        {
                            event.getItemStack().getTag().put("ench1", event.getItemStack().getEnchantmentTagList());
                            if(event.getItemStack().hasDisplayName())
                            {
                                event.getItemStack().getTag().putString("name1", event.getItemStack().getDisplayName().getString());
                                event.getItemStack().clearCustomName();
                            }

                            if(event.getItemStack().getRepairCost() > 0)
                            {
                                event.getItemStack().getTag().putInt("rc1", event.getItemStack().getRepairCost());
                            }

                            event.getItemStack().setRepairCost(event.getItemStack().getTag().getInt("rc2"));

                            event.getItemStack().getTag().put("Enchantments", event.getItemStack().getTag().getList("ench2", 10));
                            if(!event.getItemStack().getTag().getString("name2").isEmpty())
                                event.getItemStack().setDisplayName(new StringTextComponent(event.getItemStack().getTag().getString("name2")));
                            event.getItemStack().getTag().putInt("MULTIPLEMODE", 1);
                            break;
                        }

                        case (1) :
                        {
                            event.getItemStack().getTag().put("ench2", event.getItemStack().getEnchantmentTagList());

                            if(event.getItemStack().hasDisplayName())
                            {
                                event.getItemStack().getTag().putString("name2", event.getItemStack().getDisplayName().getString());
                                event.getItemStack().clearCustomName();
                            }

                            if(event.getItemStack().getRepairCost() > 0)
                            {
                                event.getItemStack().getTag().putInt("rc2", event.getItemStack().getRepairCost());
                            }

                            event.getItemStack().setRepairCost(event.getItemStack().getTag().getInt("rc3"));

                            event.getItemStack().getTag().put("Enchantments", event.getItemStack().getTag().getList("ench3", 10));
                            if(!event.getItemStack().getTag().getString("name3").isEmpty())
                                event.getItemStack().setDisplayName(new StringTextComponent(event.getItemStack().getTag().getString("name3")));

                            event.getItemStack().getTag().putInt("MULTIPLEMODE", 2);
                            break;
                        }

                        case (2) :
                        {
                            event.getItemStack().getTag().put("ench3", event.getItemStack().getEnchantmentTagList());
                            if(event.getItemStack().hasDisplayName())
                            {
                                event.getItemStack().getTag().putString("name3", event.getItemStack().getDisplayName().getString());
                                event.getItemStack().clearCustomName();
                            }

                            if(event.getItemStack().getRepairCost() > 0)
                            {
                                event.getItemStack().getTag().putInt("rc3", event.getItemStack().getRepairCost());
                            }

                            event.getItemStack().setRepairCost(event.getItemStack().getTag().getInt("rc1"));
                            event.getItemStack().getTag().put("Enchantments", event.getItemStack().getTag().getList("ench1", 10));
                            if(!event.getItemStack().getTag().getString("name1").isEmpty())
                                event.getItemStack().setDisplayName(new StringTextComponent(event.getItemStack().getTag().getString("name1")));

                            event.getItemStack().getTag().putInt("MULTIPLEMODE", 0);
                            break;
                        }

                    }

                }
                else
                {

                    if ((EnchantmentHelper.getEnchantmentLevel(ModEnchantments.MULTIPLE.get(), event.getItemStack()) != event.getItemStack().getTag().getInt("MULTIPLELVL")))
                    {
                        event.getItemStack().getTag().putInt("MULTIPLELVL", EnchantmentHelper.getEnchantmentLevel(ModEnchantments.MULTIPLE.get(), event.getItemStack()));
                        handleMultiple(event);
                        return;

                    }

                    ListNBT ench = event.getItemStack().getTag().getList("ench2", 10);


                    event.getItemStack().getTag().put("ench1", event.getItemStack().getEnchantmentTagList());

                    if(event.getItemStack().hasDisplayName())
                    {
                        event.getItemStack().getTag().putString("name1", event.getItemStack().getDisplayName().getString());
                        event.getItemStack().clearCustomName();
                    }

                    if(event.getItemStack().getRepairCost() > 0)
                    {
                        event.getItemStack().getTag().putInt("rc1", event.getItemStack().getRepairCost());
                        event.getItemStack().setRepairCost(0);
                    }


                    event.getItemStack().setRepairCost(event.getItemStack().getTag().getInt("rc2"));

                    event.getItemStack().getTag().put("Enchantments", ench);



                    if(!event.getItemStack().getTag().getString("name2").isEmpty())
                        event.getItemStack().setDisplayName(new StringTextComponent(event.getItemStack().getTag().getString("name2")));

                    event.getItemStack().getTag().putInt("MULTIPLEMODE", 1);

                }

            }

        }
        else
        {

            if (!event.getItemStack().getTag().contains("MULTIPLELVL"))
                return;

            if (event.getItemStack().getTag().getInt("MULTIPLEMODE") == 0)
            {

                event.getItemStack().getTag().remove("MULTIPLELVL");

                event.getItemStack().getTag().remove("MULTIPLEMODE");

                event.getItemStack().getTag().remove("ench1");

                event.getItemStack().getTag().remove("ench2");

                event.getItemStack().getTag().remove("ench3");

                return;

            }

            if (event.getItemStack().getTag().getInt("MULTIPLELVL") > 1)
            {

                int mode = event.getItemStack().getTag().getInt("MULTIPLEMODE");

                switch (mode)
                {

                    case (0) :
                    {
                        event.getItemStack().getTag().put("ench1", event.getItemStack().getEnchantmentTagList());
                        if(event.getItemStack().hasDisplayName())
                        {
                            event.getItemStack().getTag().putString("name1", event.getItemStack().getDisplayName().getString());
                            event.getItemStack().clearCustomName();
                        }

                        if(event.getItemStack().getRepairCost() > 0)
                        {
                            event.getItemStack().getTag().putInt("rc1", event.getItemStack().getRepairCost());
                        }

                        event.getItemStack().setRepairCost(event.getItemStack().getTag().getInt("rc2"));

                        event.getItemStack().getTag().put("Enchantments", event.getItemStack().getTag().getList("ench2", 10));
                        if(!event.getItemStack().getTag().getString("name2").isEmpty())
                            event.getItemStack().setDisplayName(new StringTextComponent(event.getItemStack().getTag().getString("name2")));
                        event.getItemStack().getTag().putInt("MULTIPLEMODE", 1);
                        break;
                    }

                    case (1) :
                    {
                        event.getItemStack().getTag().put("ench2", event.getItemStack().getEnchantmentTagList());

                        if(event.getItemStack().hasDisplayName())
                        {
                            event.getItemStack().getTag().putString("name2", event.getItemStack().getDisplayName().getString());
                            event.getItemStack().clearCustomName();
                        }

                        if(event.getItemStack().getRepairCost() > 0)
                        {
                            event.getItemStack().getTag().putInt("rc2", event.getItemStack().getRepairCost());
                        }

                        event.getItemStack().setRepairCost(event.getItemStack().getTag().getInt("rc3"));

                        event.getItemStack().getTag().put("Enchantments", event.getItemStack().getTag().getList("ench3", 10));
                        if(!event.getItemStack().getTag().getString("name3").isEmpty())
                            event.getItemStack().setDisplayName(new StringTextComponent(event.getItemStack().getTag().getString("name3")));

                        event.getItemStack().getTag().putInt("MULTIPLEMODE", 2);
                        break;
                    }

                    case (2) :
                    {
                        event.getItemStack().getTag().put("ench3", event.getItemStack().getEnchantmentTagList());
                        if(event.getItemStack().hasDisplayName())
                        {
                            event.getItemStack().getTag().putString("name3", event.getItemStack().getDisplayName().getString());
                            event.getItemStack().clearCustomName();
                        }

                        if(event.getItemStack().getRepairCost() > 0)
                        {
                            event.getItemStack().getTag().putInt("rc3", event.getItemStack().getRepairCost());
                        }

                        event.getItemStack().setRepairCost(event.getItemStack().getTag().getInt("rc1"));
                        event.getItemStack().getTag().put("Enchantments", event.getItemStack().getTag().getList("ench1", 10));
                        if(!event.getItemStack().getTag().getString("name1").isEmpty())
                            event.getItemStack().setDisplayName(new StringTextComponent(event.getItemStack().getTag().getString("name1")));

                        event.getItemStack().getTag().putInt("MULTIPLEMODE", 0);
                        break;
                    }

                }

            }
            else
            {
                ListNBT ench = event.getItemStack().getTag().getList("ench1", 10);

                event.getItemStack().getTag().put("ench2", event.getItemStack().getEnchantmentTagList());

                if(event.getItemStack().hasDisplayName())
                {
                    event.getItemStack().getTag().putString("name2", event.getItemStack().getDisplayName().getString());
                    event.getItemStack().clearCustomName();
                }

                event.getItemStack().getTag().put("Enchantments", ench);

                if(event.getItemStack().getRepairCost() > 0)
                {
                    event.getItemStack().getTag().putInt("rc2", event.getItemStack().getRepairCost());
                }


                event.getItemStack().setRepairCost(event.getItemStack().getTag().getInt("rc1"));


                if(!event.getItemStack().getTag().getString("name1").isEmpty())
                    event.getItemStack().setDisplayName(new StringTextComponent(event.getItemStack().getTag().getString("name1")));



                event.getItemStack().getTag().putInt("MULTIPLEMODE", 0);

            }

        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void renderText(ItemTooltipEvent event)
    {

        if (event.getItemStack().isEmpty())
            return;

        handleMultiple(event);

    }

    @OnlyIn(Dist.CLIENT)
    private static void handleMultiple(ItemTooltipEvent event)
    {

        if (!event.getItemStack().hasTag())
            return;

        if (event.getItemStack().getTag().contains("MULTIPLEMODE"))
        {

            Map<Enchantment, Integer> j = EnchantmentHelper.getEnchantments(event.getItemStack());

            if (event.getItemStack().getTag().getInt("MULTIPLEMODE") != 0 && event.getItemStack().isEnchanted())
            {
                Enchantment[] en = j.keySet().toArray(new Enchantment[j.keySet().size()]);

                int pos = event.getToolTip().indexOf(en[en.length - 1].getDisplayName(j.get(en[en.length - 1])));

                event.getToolTip().add(pos + 1, ModEnchantments.MULTIPLE.get().getDisplayName(event.getItemStack().getTag().getInt("MULTIPLELVL")));
            }
            else if(!event.getItemStack().isEnchanted())
            {
                event.getToolTip().add(1, ModEnchantments.MULTIPLE.get().getDisplayName(event.getItemStack().getTag().getInt("MULTIPLELVL")));
            }

            int pos = event.getToolTip().indexOf(ModEnchantments.MULTIPLE.get().getDisplayName(event.getItemStack().getTag().getInt("MULTIPLELVL")));

            event.getToolTip().add(pos == -1 ? 1 : pos + 1,  new StringTextComponent(I18n.format(LibMisc.ModID + ".mode") + ": " + (event.getItemStack().getTag().getInt("MULTIPLEMODE") + 1)));
        }
    }

}
