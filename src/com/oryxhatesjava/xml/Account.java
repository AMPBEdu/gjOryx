/*
 * Copyright (C) 2011 Furyhunter <furyhunter600@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of the creator nor the names of its
 *   contributors may be used to endorse or promote products derived from this
 *   software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
	public int accountId;
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
	
	public Account(Document d) {
		parseElement(d.getRootElement());
	}
	
	@SuppressWarnings("unchecked")
	public void parseElement(Element accountRoot) {
		if (!accountRoot.getName().equals("Account")) {
			return;
		}
		
		credits = Integer.parseInt(accountRoot.getChild("Credits").getValue());
		nextSlotPrice = Integer.parseInt(accountRoot.getChild("NextCharSlotPrice").getValue());
		accountId = Integer.parseInt(accountRoot.getChild("AccountId").getValue());
		if (accountRoot.getChild("VerifiedEmail") != null) {
			verifiedEmail = true;
		} else {
			verifiedEmail = false;
		}
		
		starredAccounts = new Vector<Integer>();
		Scanner scan = new Scanner(accountRoot.getChild("StarredAccounts").getValue());
		scan.useDelimiter(",");
		while (scan.hasNextInt()) {
			starredAccounts.add(scan.nextInt());
		}
		scan.close();
		
		ignoredAccounts = new Vector<Integer>();
		scan = new Scanner(accountRoot.getChild("IgnoredAccounts").getValue());
		scan.useDelimiter(",");
		while (scan.hasNextInt()) {
			ignoredAccounts.add(scan.nextInt());
		}
		scan.close();
		
		chests = new Vector<List<Integer>>();
		
		Element vaultEle = accountRoot.getChild("Vault");
		
		Iterator<Element> itr = vaultEle.getChildren().iterator();
		
		while (itr.hasNext()) {
			Element e = itr.next();
			LinkedList<Integer> chest = new LinkedList<Integer>();
			scan = new Scanner(e.getValue());
			scan.useDelimiter(",");
			while (scan.hasNextInt()) {
				chest.add(scan.nextInt());
			}
			scan.close();
		}
		
		name = accountRoot.getChild("Name").getValue();
		if (accountRoot.getChild("NameChosen") != null) {
			nameChosen = true;
		} else {
			nameChosen = false;
		}
		
		bestCharFame = Integer.parseInt(accountRoot.getChild("BestCharFame").getValue());
		totalFame = Integer.parseInt(accountRoot.getChild("TotalFame").getValue());
		currentFame = Integer.parseInt(accountRoot.getChild("Fame").getValue());
		
		Element guildElement = accountRoot.getChild("Guild");
		
		guildId = Integer.parseInt(guildElement.getAttributeValue("id"));
		guildName = guildElement.getChild("Name").getValue();
		guildRank = Integer.parseInt(guildElement.getChild("Rank").getValue());
		
		itr = accountRoot.getChildren("ClassStats").iterator();
		
		while (itr.hasNext()) {
			Element e = itr.next();
			int classId = Integer.parseInt(e.getAttributeValue("objectType").substring(2), 16);
			int bestLevel = Integer.parseInt(e.getChild("BestLevel").getValue());
			int bestFame = Integer.parseInt(e.getChild("BestFame").getValue());
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
		return "Account [accountId=" + accountId + ", guildName=" + guildName + "]";
	}
}
