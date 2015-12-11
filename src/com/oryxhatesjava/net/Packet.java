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

package com.oryxhatesjava.net;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

import com.oryxhatesjava.net.data.Parsable;

/**
 * <p>
 * A generic packet.
 * </p>
 * <p>
 * Started Mar 2, 2011
 * </p>
 * 
 * @author Furyhunter
 */
public class Packet implements Parsable {
    
	public static final int FAILURE = 0; // slotid = 1
	public static final int CREATE_SUCCESS = 86; // slotid = 2
	public static final int CREATE = 90; // slotid = 3
	public static final int PLAYERSHOOT = 16; // slotid = 4
	public static final int MOVE = 14; // slotid = 5
	public static final int PLAYERTEXT = 67; // slotid = 6
	public static final int TEXT = 10; // slotid = 7
	public static final int SHOOT2 = 88; // slotid = 8
	public static final int DAMAGE = 57; // slotid = 9
	public static final int UPDATE = 49; // slotid = 10
	public static final int UPDATEACK = 37; // slotid = 11
	public static final int NOTIFICATION = 99; // slotid = 12
	public static final int NEW_TICK = 47; // slotid = 13
	public static final int INVSWAP = 44; // slotid = 14
	public static final int USEITEM = 92; // slotid = 15
	public static final int SHOW_EFFECT = 89; // slotid = 16
	public static final int HELLO = 83; // slotid = 17
	public static final int GOTO = 50; // slotid = 18
	public static final int INVDROP = 8; // slotid = 19
	public static final int INVRESULT = 45; // slotid = 20
	public static final int RECONNECT = 101; // slotid = 21
	public static final int PING = 38; // slotid = 22
	public static final int PONG = 68; // slotid = 23
	public static final int MAPINFO = 58; // slotid = 24
	public static final int LOAD = 66; // slotid = 25
	public static final int PIC = 28; // slotid = 26
	public static final int SETCONDITION = 46; // slotid = 27
	public static final int TELEPORT = 5; // slotid = 28
	public static final int USEPORTAL = 4; // slotid = 29
	public static final int DEATH = 75; // slotid = 30
	public static final int BUY = 24; // slotid = 31
	public static final int BUYRESULT = 3; // slotid = 32
	public static final int AOE = 53; // slotid = 33
	public static final int GROUNDDAMAGE = 59; // slotid = 34
	public static final int PLAYERHIT = 22; // slotid = 35
	public static final int ENEMYHIT = 97; // slotid = 36
	public static final int AOEACK = 82; // slotid = 37
	public static final int SHOOTACK = 7; // slotid = 38
	public static final int OTHERHIT = 94; // slotid = 39
	public static final int SQUAREHIT = 19; // slotid = 40
	public static final int GOTOACK = 9; // slotid = 41
	public static final int EDITACCOUNTLIST = 60; // slotid = 42
	public static final int ACCOUNTLIST = 79; // slotid = 43
	public static final int QUESTOBJID = 77; // slotid = 44
	public static final int CHOOSENAME = 27; // slotid = 45
	public static final int NAMERESULT = 76; // slotid = 46
	public static final int CREATEGUILD = 69; // slotid = 47
	public static final int CREATEGUILDRESULT = 21; // slotid = 48
	public static final int GUILDREMOVE = 40; // slotid = 49
	public static final int GUILDINVITE = 25; // slotid = 50
	public static final int ALLYSHOOT = 74; // slotid = 51
	public static final int SHOOT = 20; // slotid = 52
	public static final int REQUESTTRADE = 78; // slotid = 53
	public static final int TRADEREQUESTED = 23; // slotid = 54
	public static final int TRADESTART = 11; // slotid = 55
	public static final int CHANGETRADE = 56; // slotid = 56
	public static final int TRADECHANGED = 17; // slotid = 57
	public static final int ACCEPTTRADE = 98; // slotid = 58
	public static final int CANCELTRADE = 15; // slotid = 59
	public static final int TRADEDONE = 36; // slotid = 60
	public static final int TRADEACCEPTED = 26; // slotid = 61
	public static final int CLIENTSTAT = 62; // slotid = 62
	public static final int CHECKCREDITS = 35; // slotid = 63
	public static final int ESCAPE = 33; // slotid = 64
	public static final int FILE = 84; // slotid = 65
	public static final int INVITEDTOGUILD = 1; // slotid = 66
	public static final int JOINGUILD = 48; // slotid = 67
	public static final int CHANGEGUILDRANK = 6; // slotid = 68
	public static final int PLAYSOUND = 55; // slotid = 69
	public static final int GLOBAL_NOTIFICATION = 31; // slotid = 70
	public static final int RESKIN = 61; // slotid = 71
//	public static final int _-12t = 12; // slotid = 72
//	public static final int _-02u = 63; // slotid = 73
//	public static final int _-0kN = 95; // slotid = 74
//	public static final int _-00N = 30; // slotid = 75
//	public static final int _-1US = 80; // slotid = 76
//	public static final int _-0Xg = 91; // slotid = 77
//	public static final int _-0MY = 85; // slotid = 78
//	public static final int _-05N = 18; // slotid = 79
	public static final int ENTER_ARENA = 51; // slotid = 80
//	public static final int _-0wo = 34; // slotid = 81
//	public static final int _-0jA = 39; // slotid = 82
//	public static final int _-0UC = 93; // slotid = 83
//	public static final int _-1KK = 65; // slotid = 84
//	public static final int _-zh = 100; // slotid = 85
//	public static final int _-1uy = 96; // slotid = 86
//	public static final int _-z0 = 41; // slotid = 87
//	public static final int _-21M = 87; // slotid = 88
//	public static final int _-1pU = 52; // slotid = 89
//	public static final int _-1XQ = 64; // slotid = 90
//	public static final int _-1sf = 42; // slotid = 91
//	public static final int _-185 = 13; // slotid = 92
//	public static final int _-Xd = 81; // slotid = 93
	public static final int ENEMYSHOOT = -1;
    
    public int type;
    
    private byte[] data;
    
    protected Packet() {
        
    }
    
    protected Packet(int type, byte[] data) {
        this.type = type;
        this.data = data.clone();
    }
    
    public String toString() {
        return "Unknown type " + type + " " + Arrays.toString(data);
    }
    
    @Override
    public void writeToDataOutput(DataOutput out) throws IOException {
        out.write(data);
    }
    
    @Override
    public void parseFromDataInput(DataInput in) throws IOException {
        in.readFully(data);
    }
    
    public static Packet parse(int type, byte[] data) {
        DataInput in = new ByteArrayDataInput(data);
        switch (type) {
            case FAILURE:
                return new FailurePacket(in);
            case CREATE_SUCCESS:
                return new CreateSuccessPacket(in);
            case CREATE:
                return new CreatePacket(in);
            case PLAYERSHOOT:
                return new PlayerShootPacket(in);
            case MOVE:
                return new MovePacket(in);
            case PLAYERTEXT:
                return new PlayerTextPacket(in);
            case TEXT:
                return new TextPacket(in);
            case SHOOT:
                return new ShootPacket(in);
            case HELLO:
                return new HelloPacket(in);
            case DAMAGE:
            	return new DamagePacket(in);
            case UPDATE:
            	return new UpdatePacket(in);
            case UPDATEACK:
            	return new UpdateAckPacket(in);
            case NOTIFICATION:
            	return new NotificationPacket(in);
            case NEW_TICK:
            	return new NewTickPacket(in);
            case INVSWAP:
            	return new InvSwapPacket(in);
            case USEITEM:
            	return new UseItemPacket(in);
            case SHOW_EFFECT:
            	return new ShowEffectPacket(in);
            case GOTO:
            	return new GotoPacket(in);
            case INVDROP:
            	return new InvDropPacket(in);
            case INVRESULT:
            	return new InvResultPacket(in);
            case RECONNECT:
            	return new ReconnectPacket(in);
            case PING:
            	return new PingPacket(in);
            case PONG:
            	return new PongPacket(in);
            case MAPINFO:
            	return new MapInfoPacket(in);
            case LOAD:
            	return new LoadPacket(in);
            case PIC:
            	return new PicPacket(in);
            case SETCONDITION:
            	return new SetConditionPacket(in);
            case TELEPORT:
            	return new TeleportPacket(in);
            case USEPORTAL:
            	return new UsePortalPacket(in);
            case DEATH:
            	return new DeathPacket(in);
            case BUY:
            	return new BuyPacket(in);
            case BUYRESULT:
            	return new BuyResultPacket(in);
            case AOE:
            	return new AOEPacket(in);
            case GROUNDDAMAGE:
            	return new GroundDamagePacket(in);
            case PLAYERHIT:
            	return new PlayerHitPacket(in);
            case ENEMYHIT:
            	return new EnemyHitPacket(in);
            case AOEACK:
            	return new AOEAckPacket(in);
            case SHOOTACK:
            	return new ShootAckPacket(in);
            case OTHERHIT:
            	return new OtherHitPacket(in);
            case SQUAREHIT:
            	return new SquareHitPacket(in);
            case GOTOACK:
            	return new GotoAckPacket(in);
            case EDITACCOUNTLIST:
            	return new EditAccountListPacket(in);
            case ACCOUNTLIST:
            	return new AccountListPacket(in);
            case QUESTOBJID:
            	return new QuestObjIDPacket(in);
            case CHOOSENAME:
            	return new ChooseNamePacket(in);
            case NAMERESULT:
            	return new NameResultPacket(in);
            case CREATEGUILD:
            	return new CreateGuildPacket(in);
            case CREATEGUILDRESULT:
            	return new CreateGuildResultPacket(in);
            case GUILDREMOVE:
            	return new GuildRemovePacket(in);
            case GUILDINVITE:
            	return new GuildInvitePacket(in);
            case ALLYSHOOT:
            	return new AllyShootPacket(in);
            case ENEMYSHOOT:
            	return new EnemyShootPacket(in);
            case REQUESTTRADE:
            	return new RequestTradePacket(in);
            case TRADEREQUESTED:
            	return new TradeRequestedPacket(in);
            case TRADESTART:
            	return new TradeStartPacket(in);
            case CHANGETRADE:
            	return new ChangeTradePacket(in);
            case TRADECHANGED:
            	return new TradeChangedPacket(in);
            case ACCEPTTRADE:
            	return new AcceptTradePacket(in);
            case CANCELTRADE:
            	return new CancelTradePacket(in);
            case TRADEDONE:
            	return new TradeDonePacket(in);
            case TRADEACCEPTED:
            	return new TradeAcceptedPacket(in);
            case CLIENTSTAT:
            	return new ClientStatPacket(in);
            case CHECKCREDITS:
            	return new CheckCreditsPacket(in);
            case ESCAPE:
            	return new EscapePacket(in);
            case FILE:
            	return new FilePacket(in);
            case INVITEDTOGUILD:
            	return new InvitedToGuildPacket(in);
            case JOINGUILD:
            	return new JoinGuildPacket(in);
            case CHANGEGUILDRANK:
            	return new ChangeGuildRankPacket(in);
            case PLAYSOUND:
            	return new PlaySoundPacket(in);
            default:
                return new Packet(type, data);
        }
    }
}
