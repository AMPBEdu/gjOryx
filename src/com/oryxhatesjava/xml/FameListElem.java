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

import java.util.Scanner;

import org.jdom.Element;

public class FameListElem {

	public boolean mine;
	public int accountId;
	public int charId;
	public String name;
	public int objectType;
	public int tex1;
	public int tex2;
	public int[] equipment;
	public int totalFame;
	
	public FameListElem() {
		
	}
	
	public FameListElem(Element e) {
		parseElement(e);
	}
	
	public void parseElement(Element e) {
		if (!e.getName().equals("FameListElem") && !e.getName().equals("MyFameListElem")) {
			return;
		}
		
		if (e.getName().equals("MyFameListElem")) {
			mine = true;
		}
		
		accountId = Integer.parseInt(e.getAttributeValue("accountId"));
		charId = Integer.parseInt(e.getAttributeValue("charId"));
		
		name = e.getChildText("Name");
		objectType = Integer.parseInt(e.getChildText("ObjectType"));
		tex1 = Integer.parseInt(e.getChildText("Tex1"));
		tex2 = Integer.parseInt(e.getChildText("Tex2"));
		
		equipment = new int[12];
		Scanner scan = new Scanner(e.getChildText("Equipment"));
		scan.useDelimiter(",");
		for (int i = 0; i < 12; i++) {
			equipment[i] = scan.nextInt();
		}
		
		totalFame = Integer.parseInt(e.getChildText("TotalFame"));
	}
}
