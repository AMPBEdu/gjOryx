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

package com.oryxhatesjava.util;

import org.jdom.Document;

import com.oryxhatesjava.xml.Account;
import com.oryxhatesjava.xml.CharFame;
import com.oryxhatesjava.xml.Chars;
import com.oryxhatesjava.xml.FameList;
import com.oryxhatesjava.xml.GuildMembers;
import com.oryxhatesjava.xml.RequestListener;

public class RequestAdapter implements RequestListener {

	@Override
	public void xmlReceived(String fullRequest, Document doc) {
		
	}

	@Override
	public void textReceived(String fullRequest, String text) {
		
	}

	@Override
	public void accountReceived(String fullRequest, Account account) {
		
	}

	@Override
	public void charsReceived(String fullRequest, Chars chars) {
		
	}

	@Override
	public void guildMemberListReceived(String fullRequest, GuildMembers members) {
		
	}

	@Override
	public void fameListReceived(String fullRequest, FameList list) {
		
	}

	@Override
	public void charFameReceived(String fullRequest, CharFame fame) {
		
	}

	@Override
	public void booleanReceived(String fullRequest, boolean success) {
		
	}

	@Override
	public void requestFailed(String fullRequest, String reason) {
		
	}

}
