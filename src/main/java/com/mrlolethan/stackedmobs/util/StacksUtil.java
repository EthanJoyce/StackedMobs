package com.mrlolethan.stackedmobs.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;

import com.mrlolethan.stackedmobs.P;

public enum StacksUtil {;

	/** The stacked mob custom name format. */

	//Set default name format to include special color code and special unicode X.
	public static String CUSTOM_NAME_FORMAT = "\u00A76Χ%1$d";
	public static int INVALID_STACK = -1;


	/*
	 * Stack/unstack methods
	 */
	public static boolean attemptUnstackOne(LivingEntity livingEntity) {
		ChatColor stackNumberColor = P.getInstance().getConfiguration().stackNumberColor;

		String displayName = livingEntity.getCustomName();
		int stackAmt = parseAmount(displayName);

		// Kill this mob
		livingEntity.setHealth(0);

		if (stackAmt <= 1) {
			// The stack is down to one mob; don't recreate it
			return false;
		}

		// Recreate the stack with one less mob
		stackAmt--;
		String newDisplayName = String.format(stackNumberColor + CUSTOM_NAME_FORMAT, stackAmt);

		LivingEntity dupEntity = (LivingEntity) livingEntity.getWorld().spawnEntity(livingEntity.getLocation(), livingEntity.getType());
		dupEntity.setCustomName(newDisplayName);
		dupEntity.setCustomNameVisible(true);

		return true;
	}

	public static boolean stack(LivingEntity target, LivingEntity stackee) {
		if (target.getType() != stackee.getType()) {
			return false; // The entities must be of the same type.
		}
		ChatColor stackNumberColor = P.getInstance().getConfiguration().stackNumberColor;

		String displayName = target.getCustomName();
		int alreadyStacked = parseAmount(displayName);
		int stackDelta = 1;

		// Check if the stackee is already a stack
		if (isStacked(stackee)) {
			stackDelta = parseAmount(stackee.getCustomName());
		}

		stackee.remove();

		if (alreadyStacked == INVALID_STACK) {
			// The target is NOT a stack
			String newDisplayName = String.format(stackNumberColor + CUSTOM_NAME_FORMAT, 1 + stackDelta);
			target.setCustomName(newDisplayName);
			target.setCustomNameVisible(true);
		} else {
			// The target is already a stack
			String newDisplayName = String.format(stackNumberColor + CUSTOM_NAME_FORMAT, alreadyStacked + stackDelta);
			target.setCustomName(newDisplayName);
		}
		return true;
	}

	/*
	 * "Helper" methods
	 */
	public static int parseAmount(String displayName) {
		if (displayName == null) {
			return INVALID_STACK; // No display name, therefor not a stack.
		}
				
		// Fetch the stack number color
		ChatColor stackNumberColor = P.getInstance().getConfiguration().stackNumberColor;

		String nameColor = ChatColor.getLastColors(displayName);
		if (nameColor.equals(ChatColor.COLOR_CHAR + stackNumberColor.getChar())) {
			return INVALID_STACK; // Not a valid stack.
		}
				
		//Sterilize mob displayname.. 
		//Not possible to set this via nametag. Nametag results would be "&6X99" or something like "\u00A7f\u00A76X99"
		String cleanDisplayName = displayName.replace("\u00A76Χ", "");
		String cleanDisplayName2 = cleanDisplayName.replace("§f", "");

		//Check if sterilized thing is valid by checking if it just a number or if number is unrealistically large.
		if (cleanDisplayName2.matches("[0-9]+") == false){
			//P.log(Level.WARNING, "Match char: " + cleanDisplayName2);
			return INVALID_STACK;
		}else if (cleanDisplayName2.length() > 4 == true){
			//P.log(Level.WARNING, "Match len: " + cleanDisplayName2);
			return INVALID_STACK;
		}else{
			return Integer.parseInt(cleanDisplayName2);
		}
	}

	public static boolean isStacked(LivingEntity entity) {
		return parseAmount(entity.getCustomName()) != INVALID_STACK;
	}

}
