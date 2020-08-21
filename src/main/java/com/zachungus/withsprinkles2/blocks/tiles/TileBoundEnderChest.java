package com.zachungus.withsprinkles2.blocks.tiles;

import com.zachungus.withsprinkles2.blocks.ModBlocks;
import com.zachungus.withsprinkles2.util.OfflinePlayerUtils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.UUID;

public class TileBoundEnderChest extends TileMod implements IChestLid, ITickableTileEntity, IInventory {


    public TileBoundEnderChest()
    {
        super(ModBlocks.TILE_BOUND_ENDER_CHEST.get());
    }

    public float lidAngle;
    /** The angle of the ender chest lid last tick */
    public float prevLidAngle;
    public int numPlayersUsing;

    private final String UUID_NBT = "UUID_NBT";
    private final String NAME_NBT = "NAME_NBT";

    public UUID uuid = null;

    public String name = null;
    /**
     * A counter that is incremented once each tick. Used to determine when to determine when to sync with the client;
     * this happens every 80 ticks. However, the number of players is not re-counted.
     */
    private int ticksSinceSync;


    public void writeCustomNBT(CompoundNBT cmp)
    {
        if(uuid != null)
        {
            cmp.putString(UUID_NBT, uuid.toString());
            cmp.putString(NAME_NBT, name);
        }
    }

    public void readCustomNBT(CompoundNBT cmp)
    {
        if((cmp.contains(UUID_NBT)))
        {
            this.uuid = UUID.fromString(cmp.getString(UUID_NBT));
            this.name = cmp.getString(NAME_NBT);
        }
    }

    public void tick()
    {
        //lidAngle += 0.2F;

        if (++this.ticksSinceSync % 20 * 4 == 0) {
            this.world.addBlockEvent(this.pos, ModBlocks.BOUND_ENDER_CHEST.get(), 1, this.numPlayersUsing);
        }

        this.prevLidAngle = this.lidAngle;
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        float f = 0.1F;
        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
            double d0 = (double)i + 0.5D;
            double d1 = (double)k + 0.5D;
            this.world.playSound((PlayerEntity)null, d0, (double)j + 0.5D, d1, SoundEvents.BLOCK_ENDER_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
            float f2 = this.lidAngle;
            if (this.numPlayersUsing > 0) {
                this.lidAngle += 0.1F;
            } else {
                this.lidAngle -= 0.1F;
            }

            if (this.lidAngle > 1.0F) {
                this.lidAngle = 1.0F;
            }

            float f1 = 0.5F;
            if (this.lidAngle < 0.5F && f2 >= 0.5F) {
                double d3 = (double)i + 0.5D;
                double d2 = (double)k + 0.5D;
                this.world.playSound((PlayerEntity)null, d3, (double)j + 0.5D, d2, SoundEvents.BLOCK_ENDER_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F) {
                this.lidAngle = 0.0F;
            }
        }

    }

    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            this.numPlayersUsing = type;
            return true;
        } else {
            return super.receiveClientEvent(id, type);
        }
    }

    /**
     * invalidates a tile entity
     */
    public void remove() {
        this.updateContainingBlockInfo();
        super.remove();
    }

    public void openChest() {
        ++this.numPlayersUsing;
        this.world.addBlockEvent(this.pos, ModBlocks.BOUND_ENDER_CHEST.get(), 1, this.numPlayersUsing);

    }

    public void closeChest() {
        --this.numPlayersUsing;
        this.world.addBlockEvent(this.pos, ModBlocks.BOUND_ENDER_CHEST.get(), 1, this.numPlayersUsing);
    }

    public boolean canBeUsed(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return !(player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) > 64.0D);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public float getLidAngle(float partialTicks) {
        return MathHelper.lerp(partialTicks, this.prevLidAngle, this.lidAngle);
    }

    public EnderChestInventory getEnder()
    {
        PlayerEntity player2 = this.getWorld().getPlayerByUuid(uuid);

        return player2 == null ? OfflinePlayerUtils.getOfflineEnderChest(uuid, this.getWorld()) : player2.getInventoryEnderChest();

    }

    @Override
    public int getSizeInventory() {
        return 27;
    }

    @Override
    public boolean isEmpty()
    {
        return getEnder().isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return getEnder().getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return getEnder().decrStackSize(index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return getEnder().removeStackFromSlot(index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        getEnder().setInventorySlotContents(index, stack);
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return true;
    }
    @Override
    public void openInventory(PlayerEntity player)
    {
        openChest();
    }

    @Override
    public void closeInventory(PlayerEntity player)
    {
        closeChest();
    }

    @Override
    public void clear()
    {
        getEnder().clear();
    }
}
