package jotato.quantumflux.machine.infuser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cofh.lib.inventory.ComparableItemStack;
import jotato.quantumflux.items.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class InfuserRecipeManager {

	public static Map<List<ComparableItemStack>, InfuserRecipe> recipeMap = new HashMap<List<ComparableItemStack>, InfuserRecipeManager.InfuserRecipe>();
	
	
	public static void addDefaultRecipes(){
		addRecipe("ingotSteel", new ItemStack(Items.ender_pearl,16), new ItemStack(ModItems.crystalizedRedstone));
		addRecipe(new ItemStack(Items.iron_ingot), new ItemStack(Blocks.packed_ice), new ItemStack(ModItems.steelIngot));
	}
	

	public static InfuserRecipe addRecipe(ItemStack first, ItemStack second, ItemStack result) {
		
		InfuserRecipe recipe = null;
		
		if (first == null || second == null || result == null){
			return recipe;
		}
		
		recipe = getRecipe(first, second);

		if(recipe == null){
			recipe = new InfuserRecipe(first, second, result);
			recipeMap.put(Arrays.asList(new ComparableItemStack(first), new ComparableItemStack(second)), recipe);
		}
		
		return recipe;

	}
	
	public static InfuserRecipe addRecipe(String first, String second, ItemStack result)
	{
		  List<ItemStack> firstOreList = OreDictionary.getOres(first);
          List<ItemStack> secondOreList = OreDictionary.getOres(second);

          if (firstOreList.size() > 0 && secondOreList.size() > 0) {
                 return addRecipe(firstOreList.get(0), secondOreList.get(0), result);
          }
          
          return null;
	}
	
	public static InfuserRecipe addRecipe(String first, ItemStack second, ItemStack result)
	{
		  List<ItemStack> firstOreList = OreDictionary.getOres(first);

          if (firstOreList.size() > 0) {
                 return addRecipe(firstOreList.get(0).copy(), second, result);
          }
          
          return null;
	}

	public static InfuserRecipe getRecipe(ItemStack first, ItemStack second) {
		if (first == null || second == null)
			return null;

		ComparableItemStack q1 = new ComparableItemStack(first);
		ComparableItemStack q2 = new ComparableItemStack(second);

		InfuserRecipe recipe = recipeMap.get(Arrays.asList(q1, q2));

		if (recipe == null) {
			recipe = recipeMap.get(Arrays.asList(q2, q1));
		}

		return recipe;
	}

	public static class InfuserRecipe {
		
		private final ItemStack first;
		private final ItemStack second;
		private final ItemStack result;

		public InfuserRecipe(ItemStack first, ItemStack second, ItemStack result) {
			
			first.stackSize=Math.max(first.stackSize, 1);
			second.stackSize=Math.max(second.stackSize, 1);
			result.stackSize=Math.max(result.stackSize, 1);
			
			this.first = first;
			this.second = second;
			this.result = result;
		}
		
		public ItemStack getFirstInput(){
			return first.copy();
		}
		
		public ItemStack getSecondInput(){
			return second.copy();
		}
		
		public ItemStack getResult(){
			return result.copy();
		}
	}

}
