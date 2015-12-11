/* oryx-hates-java
 * Copyright (C) 2011-2012 Furyhunter <furyhunter600@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.oryxhatesjava.xml;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;

public class Account {
	
	public int credits;
	public int nextSlotPrice;
	public String accountId;
	public boolean verifiedEmail;
	public List<Integer> starredAccounts;
	public List<Integer> ignoredAccounts;
	public List<List<Integer>> chests;
	public String name;
	public boolean nameChosen;
	public int bestCharFame;
	public int totalFame;
	public int currentFame;
	
	public int wizardBestLevel;
	public int wizardBestFame;
	public int archerBestLevel;
	public int archerBestFame;
	public int rogueBestLevel;
	public int rogueBestFame;
	public int warriorBestLevel;
	public int warriorBestFame;
	public int necromancerBestLevel;
	public int necromancerBestFame;
	public int priestBestLevel;
	public int priestBestFame;
	public int knightBestLevel;
	public int knightBestFame;
	public int paladinBestLevel;
	public int paladinBestFame;
	public int assassinBestLevel;
	public int assassinBestFame;
	public int huntressBestLevel;
	public int huntressBestFame;
	public int mysticBestLevel;
	public int mysticBestFame;
	public int tricksterBestLevel;
	public int tricksterBestFame;
	public int sorcererBestLevel;
	public int sorcererBestFame;
	
	public int guildId;
	public String guildName;
	public int guildRank;
	
	public static final int WIZARD = 0x307;
	public static final int ARCHER = 0x30e;
	public static final int ROGUE = 0x300;
	
	
	public Account() {
		
	}
	
	public Account(Element e) {
		parseElement(e);
	}
	
	@SuppressWarnings("unchecked")
	public void parseElement(Element accountRoot) {
		if (!accountRoot.getName().equals("Account")) {
			throw new IllegalArgumentException();
		}
		
		credits = Integer.parseInt(accountRoot.getChildText("Credits"));
		nextSlotPrice = Integer.parseInt(accountRoot.getChildText("NextCharSlotPrice"));
		accountId = accountRoot.getChildText("AccountId");
		if (accountRoot.getChild("VerifiedEmail") != null) {
			verifiedEmail = true;
		} else {
			verifiedEmail = false;
		}
		
		starredAccounts = new Vector<Integer>();
		Scanner scan;
		if (accountRoot.getChild("StarredAccounts") != null) {
			scan = new Scanner(accountRoot.getChildText("StarredAccounts"));
			scan.useDelimiter(",");
			while (scan.hasNextInt()) {
				starredAccounts.add(scan.nextInt());
			}
			scan.close();
		}

		ignoredAccounts = new Vector<Integer>();
		if (accountRoot.getChild("IgnoredAccounts") != null) {
			scan = new Scanner(accountRoot.getChildText("IgnoredAccounts"));
			scan.useDelimiter(",");
			while (scan.hasNextInt()) {
				ignoredAccounts.add(scan.nextInt());
			}
			scan.close();
		}
		
		chests = new Vector<List<Integer>>();
		
		Element vaultEle = accountRoot.getChild("Vault");
		
		Iterator<Element> itr = vaultEle.getChildren().iterator();
		
		while (itr.hasNext()) {
			Element e = itr.next();
			LinkedList<Integer> chest = new LinkedList<Integer>();
			scan = new Scanner(e.getText());
			scan.useDelimiter(",");
			while (scan.hasNextInt()) {
				chest.add(scan.nextInt());
			}
			scan.close();
		}
		
		name = accountRoot.getChildText("Name");
		if (accountRoot.getChild("NameChosen") != null) {
			nameChosen = true;
		} else {
			nameChosen = false;
		}
		
		Element stats = accountRoot.getChild("Stats");
		
		bestCharFame = Integer.parseInt(stats.getChildText("BestCharFame"));
		totalFame = Integer.parseInt(stats.getChildText("TotalFame"));
		currentFame = Integer.parseInt(stats.getChildText("Fame"));
		
		Element guildElement = accountRoot.getChild("Guild");
		
		if (guildElement != null) {
			//TODO: Causes error guildId = Integer.parseInt(guildElement.getAttributeValue("id"));
			guildName = guildElement.getChildText("Name");
			guildRank = Integer.parseInt(guildElement.getChildText("Rank"));
		}
		
		itr = stats.getChildren("ClassStats").iterator();
		
		while (itr.hasNext()) {
			Element e = itr.next();
			int classId = Integer.parseInt(e.getAttributeValue("objectType").substring(2), 16);
			int bestLevel = Integer.parseInt(e.getChildText("BestLevel"));
			int bestFame = Integer.parseInt(e.getChildText("BestFame"));
			switch (classId) {
			case ARCHER:
				archerBestLevel = bestLevel;
				archerBestFame = bestFame;
				break;
			case WIZARD:
				wizardBestLevel = bestLevel;
				wizardBestFame = bestFame;
				break;
			case ROGUE:
				rogueBestLevel = bestLevel;
				rogueBestFame = bestFame;
				break;
			}
		}
		
	}
	
	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", name=" + name + "]";
	}
}
