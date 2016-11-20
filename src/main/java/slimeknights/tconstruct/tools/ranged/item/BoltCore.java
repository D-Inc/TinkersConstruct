package slimeknights.tconstruct.tools.ranged.item;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import javax.annotation.Nonnull;

import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.client.CustomTextureCreator;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;
import slimeknights.tconstruct.library.tools.ToolPart;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.Tags;
import slimeknights.tconstruct.tools.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerTools;

public class BoltCore extends ToolPart {

  public static ItemStack GUI_RENDER_ITEMSTACK;

  public BoltCore(int cost) {
    super(cost);
  }

  @Override
  public Material getMaterial(ItemStack stack) {
    NBTTagList materials = TagUtil.getBaseMaterialsTagList(stack);
    return TinkerRegistry.getMaterial(materials.getStringTagAt(0));
  }

  public static Material getHeadMaterial(ItemStack stack) {
    NBTTagList materials = TagUtil.getBaseMaterialsTagList(stack);
    return TinkerRegistry.getMaterial(materials.getStringTagAt(1));
  }

  @Override
  public ItemStack getItemstackWithMaterial(Material material) {
    if(material.hasStats(MaterialTypes.SHAFT)) {
      return getItemstackWithMaterials(material, TinkerMaterials.iron);
    }
    return getItemstackWithMaterials(TinkerMaterials.wood, material);
  }

  public static ItemStack getItemstackWithMaterials(Material shaft, Material head) {
    ItemStack stack = new ItemStack(TinkerTools.boltCore);
    NBTTagList tagList = new NBTTagList();
    tagList.appendTag(new NBTTagString(shaft.getIdentifier()));
    tagList.appendTag(new NBTTagString(head.getIdentifier()));

    NBTTagCompound rootTag = new NBTTagCompound();
    NBTTagCompound baseTag = new NBTTagCompound();

    baseTag.setTag(Tags.BASE_MATERIALS, tagList);
    rootTag.setTag(Tags.BASE_DATA, baseTag);
    stack.setTagCompound(rootTag);

    return stack;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    Material material = getMaterial(stack);
    Material material2 = getHeadMaterial(stack);

    // Material traits/info
    boolean shift = Util.isShiftKeyDown();

    if(!checkMissingMaterialTooltip(stack, tooltip)) {
      tooltip.addAll(getTooltipTraitInfo(material));
      tooltip.addAll(getTooltipTraitInfo(material2));
    }

    // Stats
    if(Config.extraTooltips) {
      if(!shift) {
        // info tooltip for detailed and component info
        tooltip.add("");
        tooltip.add(Util.translate("tooltip.tool.holdShift"));
      }
      else {
        tooltip.addAll(getTooltipStatsInfo(material));
        tooltip.addAll(getTooltipStatsInfo(material2));
      }
    }

    tooltip.addAll(getAddedByInfo(material2));
  }

  @Nonnull
  @Override
  public String getItemStackDisplayName(@Nonnull ItemStack stack) {
    Material material = getMaterial(stack);
    Material material2 = getHeadMaterial(stack);

    String originalItemName = ("" + I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name")).trim();

    return Material.getCombinedItemName(originalItemName, ImmutableList.of(material, material2));
  }

  @Override
  public boolean canBeCrafted() {
    return false;
  }

  @Override
  public boolean canBeCasted() {
    return false;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public ItemStack getOutlineRenderStack() {
    if(GUI_RENDER_ITEMSTACK == null) {
      GUI_RENDER_ITEMSTACK = getItemstackWithMaterials(CustomTextureCreator.guiMaterial, CustomTextureCreator.guiMaterial);
    }
    return GUI_RENDER_ITEMSTACK;
  }
}
