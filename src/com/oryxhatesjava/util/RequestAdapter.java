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
