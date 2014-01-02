/*
 * Copyright (C) 2014 - Gonçalo Baltazar <http://goncalomb.com>
 *
 * This file is part of NBTEditor.
 *
 * NBTEditor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NBTEditor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NBTEditor.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.goncalomb.bukkit.nbteditor.commands;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.bkglib.Lang;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommand;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommandException;
import com.goncalomb.bukkit.bkglib.utils.Utils;
import com.goncalomb.bukkit.nbteditor.ItemStorage;
import com.goncalomb.bukkit.nbteditor.NBTEditor;

public class CommandItemStorage extends BKgCommand {

	public CommandItemStorage() {
		super("itemstorage", "is");
	}
	
	private static void validateName(String name) throws BKgCommandException {
		if (!ItemStorage.isValidName(name)) {
			throw new BKgCommandException(Lang._(NBTEditor.class, "commands.itemstorage.invalid-name"));
		}
	}
	
	private static void checkItemExistance(String name) throws BKgCommandException {
		if (!ItemStorage.existsItem(name)) {
			throw new BKgCommandException(Lang._(NBTEditor.class, "commands.itemstorage.not-found"));
		}
	}
	
	@Command(args = "store", type = CommandType.PLAYER_ONLY, minargs = 1, usage = "<name>")
	public boolean command_store(CommandSender sender, String[] args) throws BKgCommandException {
		ItemStack item = ((Player) sender).getItemInHand();
		if (item == null || item.getType() == Material.AIR) {
			sender.sendMessage(Lang._(NBTEditor.class, "commands.itemstorage.not-holding"));
		} else {
			validateName(args[0]);
			if (ItemStorage.addItem(item, args[0])) {
				sender.sendMessage(Lang._(NBTEditor.class, "commands.itemstorage.stored"));
			} else {
				sender.sendMessage(Lang._(NBTEditor.class, "commands.itemstorage.found"));
			}
		}
		return true;
	}
	
	@Command(args = "get", type = CommandType.PLAYER_ONLY, minargs = 1, usage = "<name>")
	public boolean command_get(CommandSender sender, String[] args) throws BKgCommandException {
		validateName(args[0]);
		checkItemExistance(args[0]);
		CommandUtils.giveItem((Player) sender, ItemStorage.getItem(args[0]));
		sender.sendMessage(Lang._(NBTEditor.class, "commands.itemstorage.got"));
		return true;
	}
	
	@TabComplete(args = "get")
	public List<String> tabcomplete_get(CommandSender sender, String[] args) {
		return Utils.getElementsWithPrefix(ItemStorage.listItems(), args[0]);
	}
	
	@Command(args = "remove", type = CommandType.PLAYER_ONLY, minargs = 1, usage = "<name>")
	public boolean command_remove(CommandSender sender, String[] args) throws BKgCommandException {
		validateName(args[0]);
		checkItemExistance(args[0]);
		ItemStorage.removeItem(args[0]);
		sender.sendMessage(Lang._(NBTEditor.class, "commands.itemstorage.removed"));
		return true;
	}
	
	@TabComplete(args = "remove")
	public List<String> tabcomplete_remove(CommandSender sender, String[] args) {
		return Utils.getElementsWithPrefix(ItemStorage.listItems(), args[0]);
	}
	
	@Command(args = "list", type = CommandType.PLAYER_ONLY)
	public boolean command_list(CommandSender sender, String[] args) throws BKgCommandException {
		sender.sendMessage(Lang._(NBTEditor.class, "storeditems-prefix") + StringUtils.join(ItemStorage.listItems(), ", "));
		return true;
	}
	
}