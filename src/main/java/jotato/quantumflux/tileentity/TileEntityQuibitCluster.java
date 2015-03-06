package jotato.quantumflux.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jotato.quantumflux.ConfigMan;
import jotato.quantumflux.core.IWirelessCapable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TileEntityQuibitCluster extends TileEntity implements IWirelessCapable, IEnergyHandler
{
    protected EnergyStorage localEnergyStorage;
    private int transferRate;
    private int capacity;
    public int level;

    public TileEntityQuibitCluster(int transferRate, int capacity, int level)
    {

        this.transferRate = transferRate;
        this.capacity = capacity;
        this.level = level;
        for (int i = 1; i < level; i++)
        {
            this.transferRate *= ConfigMan.quibitCluster_multiplier;
            this.capacity *= ConfigMan.quibitCluster_multiplier;
        }
        // todo: anything other than this ugly hack!

        if (level == 5)
            this.transferRate = Integer.MAX_VALUE;
        localEnergyStorage = new EnergyStorage(this.capacity, this.transferRate);
    }

    protected EnergyStorage getEnergyDevice()
    {
        return localEnergyStorage; // todo: update to use the redstoneflux field
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from)
    {
        return true;
    }

    public int getEnergyTransferRate()
    {
        return this.transferRate;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
    {
        return getEnergyDevice().receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
    {
        return getEnergyDevice().extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored(ForgeDirection from)
    {
        return getEnergyDevice().getEnergyStored();
    }

    public void setEnergyStored(int energy)
    {
        this.getEnergyDevice().setEnergyStored(energy);
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from)
    {
        return getEnergyDevice().getMaxEnergyStored();
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        NBTTagCompound energyTag = new NBTTagCompound();
        this.getEnergyDevice().writeToNBT(energyTag);
        tag.setTag("Energy", energyTag);

    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        NBTTagCompound energyTag = tag.getCompoundTag("Energy");
        this.getEnergyDevice().readFromNBT(energyTag);
    }

    @SideOnly(Side.CLIENT)
    public int getBufferScaled(int scale)
    {
        return getEnergyStored(null) * scale / getMaxEnergyStored(null);
    }

}
