package com.ustmc.players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;

import com.ustmc.utils.Console;
import com.ustmc.utils.Utils;

public enum College {
	AB("Faculty of Arts and Letters", "AB", "college.ab",
			new String[] { "Type A Male - https://i.imgur.com/tNOTXIU.png",
					"Type A Female - https://i.imgur.com/qQ5lNaL.png" }),
	ARCHI("College of Architecture", "ARCHI", "college.archi",
			new String[] { "Type A Male - https://i.imgur.com/c6NA9Ur.png",
					"Type B Male - https://i.imgur.com/EmRH3H9.png", "Type A Female - https://i.imgur.com/m2jlTXX.png",
					"Type B Female - https://i.imgur.com/oes2sBE.png" }),
	CFAD("College of Fine Arts and Design", "CFAD", "college.cfad",
			new String[] { "Type A Male - https://i.imgur.com/YwOMi0r.png",
					"Type A Female - https://i.imgur.com/UedwUFj.png" }),
	COA("College of Accountancy", "COA", "college.coa",
			new String[] { "Type A Male - https://i.imgur.com/bvm9rty.png",
					"Type A Female Skirt - https://i.imgur.com/pmkYdcR.png",
					"Type A Female Pants - https://i.imgur.com/pCyrNyt.png" }),
	COMM("College of Commerce and Business Administration", "COMM", "college.comm",
			new String[] { "Type A Male - https://i.imgur.com/uN3jKro.png",
					"Type A Female Pants - https://i.imgur.com/qQSz2mo.png" }),
	CRS("College of Rehabilitation Sciences", "CRS", "college.crs",
			new String[] { "Type A Male - https://i.imgur.com/HWjrQif.png",
					"Type A Female - https://i.imgur.com/ZRl8yOS.png" }),
	CTHM("College of Tourism and Hospitality Management", "CTHM", "college.cthm",
			new String[] { "Type A Male - https://i.imgur.com/lvPbclu.png",
					"Type A Female - https://i.imgur.com/IWhzT5a.png" }),
	EDUC("College of Education", "EDUC", "college.educ",
			new String[] { "Type A Male - https://i.imgur.com/2JEes5o.png",
					"Type A Female - https://i.imgur.com/HqKVe2h.png" }),
	EHS("Education High School", "EHS", "college.ehs",
			new String[] { "Type A Male - https://i.imgur.com/H8H6Pu4.png",
					"Type A Female - https://i.imgur.com/crwEKOJ.png" }),
	ENGG("Faculty of Engineering", "ENGG", "college.engg",
			new String[] { "Type A Male - https://i.imgur.com/64DXF0e.png",
					"Type A Female - https://i.imgur.com/P8mvddi.png" }),
	GS("Graduate School", "GS", "college.gs", new String[] { "Male Formal 1 - https://i.imgur.com/QeBn1IX.png",
			"Male Formal 2 - https://i.imgur.com/AvlWq3j.png", "Male Formal 3 - https://i.imgur.com/6Ih10VF.png",
			"Male Formal 4 - https://i.imgur.com/oO6XWJb.png", "Female Formal 1 - https://i.imgur.com/EOghLjP.png",
			"Female Formal 2 - https://i.imgur.com/HNVWq8h.png", "Female Formal 3 - https://i.imgur.com/wejLVKY.png",
			"Female Formal 4 - https://i.imgur.com/y0kKM6H.png" }),
	IICS("Institute of Information and Computing Sciences", "IICS", "college.iics",
			new String[] { "Type A Male - https://i.imgur.com/35WaJaV.png",
					"Type A Female - https://i.imgur.com/2OCuPwb.png" }),
	IPEA("Institute of Physical Education and Athletics", "IPEA", "college.ipea",
			new String[] { "Type A Male - https://i.imgur.com/3xEXnwX.png",
					"Type A Female Pants - https://i.imgur.com/B7fLtEN.png",
					"Type A Female Skirt - https://i.imgur.com/PhYYDWF.png" }),
	JHS("Junior High School", "JHS", "college.jhs",
			new String[] { "Type A Male - https://i.imgur.com/CIAFv9Z.png",
					"Type A Female - https://i.imgur.com/WP9qF4n.png" }),
	LAW("Faculty of Canon Law", "LAW", "college.law",
			new String[] { "Long Sleeves - https://i.imgur.com/vox3DJW.png",
					"Short Sleeves - https://i.imgur.com/Aj7VQuM.png", }),
	MED("Faculty of Medicine and Surgery", "MED", "college.med",
			new String[] { "Type A Male - https://i.imgur.com/pTFfiyp.png",
					"Type B Male - https://i.imgur.com/rH4tiNv.png", "Type A Female - https://i.imgur.com/DFMKCH3.png",
					"Type B Female - https://i.imgur.com/lk3dTN4.png" }),
	MUSIC("Conservatory of Music", "MUSIC", "college.music",
			new String[] { "Type A Male - https://i.imgur.com/HbpMshs.png",
					"Type A Female - https://i.imgur.com/slFltzk.png" }),
	NUR("College of Nursing", "NUR", "college.nur",
			new String[] { "Lower Batch Male - https://i.imgur.com/klEvlKI.png",
					"Higher Batch Male - https://i.imgur.com/6UCuWZw.png",
					"Lower Batch Female - https://i.imgur.com/ZVQ25RB.png",
					"Higher Batch Female - https://i.imgur.com/p3YQkZq.png" }),
	PHARMA("Faculty of Pharmacy", "PHARMA", "college.pharma",
			new String[] { "Lower Batch Male - https://i.imgur.com/juaZbTv.png",
					"Higher Batch Male - https://i.imgur.com/UhEeFh1.png",
					"Lower Batch Female - https://i.imgur.com/3fJ2Lf4.png",
					"Higher Batch Female - https://i.imgur.com/XtUqTuk.png" }),
	SCI("College of Science", "SCI", "college.sci",
			new String[] { "Type A Male - https://i.imgur.com/ev6THwL.png",
					"Type A Female - https://i.imgur.com/6beEtPR.png" }),
	SHS("Senior High School", "SHS", "college.shs",
			new String[] { "Type A Male - https://i.imgur.com/96aezuc.png",
					"Type A Female - https://i.imgur.com/VrKyeqF.png" }),
	UST_PROF("UST Professor/Instructor", "PROF", "college.prof", new String[] {
			"Male Formal 1 - https://i.imgur.com/QeBn1IX.png", "Male Formal 2 - https://i.imgur.com/AvlWq3j.png",
			"Male Formal 3 - https://i.imgur.com/6Ih10VF.png", "Male Formal 4 - https://i.imgur.com/oO6XWJb.png",
			"Female Formal 1 - https://i.imgur.com/EOghLjP.png", "Female Formal 2 - https://i.imgur.com/HNVWq8h.png",
			"Female Formal 3 - https://i.imgur.com/wejLVKY.png", "Female Formal 4 - https://i.imgur.com/y0kKM6H.png" }),
	UST_ADMIN("UST Administrator/Staff", "UST_ADMIN", "college.admin", new String[] {
			"Male Formal 1 - https://i.imgur.com/QeBn1IX.png", "Male Formal 2 - https://i.imgur.com/AvlWq3j.png",
			"Male Formal 3 - https://i.imgur.com/6Ih10VF.png", "Male Formal 4 - https://i.imgur.com/oO6XWJb.png",
			"Female Formal 1 - https://i.imgur.com/EOghLjP.png", "Female Formal 2 - https://i.imgur.com/HNVWq8h.png",
			"Female Formal 3 - https://i.imgur.com/wejLVKY.png", "Female Formal 4 - https://i.imgur.com/y0kKM6H.png" }),
	VISITOR("Visitor", "Visitor", "ust.nocollege", new String[] { "Male Formal 1 - https://i.imgur.com/QeBn1IX.png",
			"Male Formal 2 - https://i.imgur.com/AvlWq3j.png", "Male Formal 3 - https://i.imgur.com/6Ih10VF.png",
			"Male Formal 4 - https://i.imgur.com/oO6XWJb.png", "Female Formal 1 - https://i.imgur.com/EOghLjP.png",
			"Female Formal 2 - https://i.imgur.com/HNVWq8h.png", "Female Formal 3 - https://i.imgur.com/wejLVKY.png",
			"Female Formal 4 - https://i.imgur.com/y0kKM6H.png" }),
	DEFAULT("Default", "Default", "ust.nocollege", new String[] { "Male Formal 1 - https://i.imgur.com/QeBn1IX.png",
			"Male Formal 2 - https://i.imgur.com/AvlWq3j.png", "Male Formal 3 - https://i.imgur.com/6Ih10VF.png",
			"Male Formal 4 - https://i.imgur.com/oO6XWJb.png", "Female Formal 1 - https://i.imgur.com/EOghLjP.png",
			"Female Formal 2 - https://i.imgur.com/HNVWq8h.png", "Female Formal 3 - https://i.imgur.com/wejLVKY.png",
			"Female Formal 4 - https://i.imgur.com/y0kKM6H.png" });

	private String name, code, perm;
	private Map<String, String> uniform;
	private static List<String> collegeCodes = new ArrayList<String>();
	private static List<String> collegeNames = new ArrayList<String>();
	private final Inventory uniformInventory;

	public static void initialize() {
		collegeCodes = new ArrayList<String>();
		collegeNames = new ArrayList<String>();
	}

	private College(String name, String code, String perm, String uniform[]) {
		if (College.getCodeList() == null) {
			initialize();
		}
		Console.info("Initializing college " + name);
		this.name = name;
		this.code = code;
		this.perm = perm;
		this.uniform = new HashMap<String, String>();
		// Uniforms
		uniformInventory = Bukkit.getServer().createInventory(null, 9, ChatColor.BLUE + this.name + " uniform");
		int m1 = -1, m2 = 9;
		for (String str : uniform) {
			String unifstr[] = str.split(" - ");
			this.uniform.put(unifstr[0], unifstr[1]);
			ItemStack unif = new ItemStack(Material.LEATHER_CHESTPLATE);
			LeatherArmorMeta meta2 = (LeatherArmorMeta) unif.getItemMeta();
			meta2.setDisplayName(ChatColor.BLUE + unifstr[0]);
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.GOLD + this.getCollegeName() + ChatColor.YELLOW + " uniform");
			lore.add(" ");
			lore.add(ChatColor.GOLD + "Right click" + ChatColor.GREEN + " to change uniform.");
			meta2.setLore(lore);
			meta2.getPersistentDataContainer().set(Utils.unifKey, PersistentDataType.STRING, unifstr[1]);
			if (unifstr[0].contains("Female")) {
				meta2.setColor(Color.fromRGB(255, 51, 153));
				unif.setItemMeta(meta2);
				m2--;
				uniformInventory.setItem(m2, unif);
			} else { // Male
				meta2.setColor(Color.BLUE);
				unif.setItemMeta(meta2);
				m1++;
				uniformInventory.setItem(m1, unif);
			}
		}
		ItemStack remove = new ItemStack(Material.BARRIER);
		ItemMeta removeMeta = remove.getItemMeta();
		removeMeta.setDisplayName(ChatColor.RED + "Remove uniform");
		List<String> lore = new ArrayList<String>();
		lore.add(" ");
		lore.add(ChatColor.GOLD + "Right click" + ChatColor.RED + " to remove your uniform.");
		remove.setItemMeta(removeMeta);
		uniformInventory.setItem(4, remove);
		// End uniforms
		College.getCodeList().add(code);
		College.getCollegeList().add(name);

	}

	public String getCollegeName() {
		return name;
	}

	public String getCollegeCode() {
		return code;
	}

	public String getPermission() {
		return perm;
	}

	public Inventory getInventory() {
		return uniformInventory;
	}

	public static boolean isInventory(InventoryView inv) {
		if (inv.getTitle() == null)
			return false;
		return inv.getTitle().endsWith(" uniform") && inv.getTitle().startsWith(ChatColor.BLUE + "");
	}

	public static College getCollege(String key) {
		for (College college : College.values()) {
			if (college.getCollegeCode().startsWith(key.toUpperCase())) {
				return college;
			}
		}
		return College.DEFAULT;
	}

	public static College getCollegeComplete(String exactName) {
		for (College college : College.values()) {
			if (college.getCollegeName().toLowerCase().startsWith(exactName.toLowerCase())) {
				return college;
			}
		}
		return College.DEFAULT;
	}

	public static List<String> getTabCollegeCodes(String arg) {
		List<String> list = new ArrayList<String>();
		for (College college : College.values()) {
			if (college.getCollegeCode().startsWith(arg.toUpperCase())) {
				list.add(college.getCollegeCode());
			}
		}
		return list;
	}

	public static List<String> getTabCollegeNames(String arg) {
		List<String> list = new ArrayList<String>();
		for (College college : College.values()) {
			if (college.getCollegeCode().toLowerCase().startsWith(arg.toLowerCase())) {
				list.add(college.getCollegeCode());
			}
		}
		return list;
	}

	public static List<String> getCodeList() {
		return collegeCodes;
	}

	public static List<String> getCollegeList() {
		return collegeNames;
	}

	public Map<String, String> getUniform() {
		return uniform;
	}
}
