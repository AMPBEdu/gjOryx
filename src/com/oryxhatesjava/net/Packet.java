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
    
    public static final int FAILURE = 0; //FailurePacket
    public static final int CREATE_SUCCESS = 1; //CreateSuccessPacket
    public static final int CREATE = 3; //CreatePacket
    public static final int PLAYERSHOOT = 4; //PlayerShootPacket
    public static final int MOVE = 5; //MovePacket
    public static final int PLAYERTEXT = 6; //PlayerTextPacket
    public static final int TEXT = 7; //TextPacket
    public static final int SHOOT = 8; //ShootPacket
    public static final int DAMAGE = 9; //DamagePacket
    public static final int UPDATE = 10; //UpdatePacket
    public static final int UPDATEACK = 11; //UpdateAckPacket
    public static final int NOTIFICATION = 12; //NotificationPacket
    public static final int NEW_TICK = 13; //NewTickPacket
    public static final int INVSWAP = 14; //InvSwapPacket
    public static final int USEITEM = 15; //UseItemPacket
    public static final int SHOW_EFFECT = 16; //ShowEffectPacket
    public static final int HELLO = 17; //HelloPacket
    public static final int GOTO = 18; //GotoPacket
    public static final int INVDROP = 19; //InvDropPacket
    public static final int INVRESULT = 20; //InvResultPacket
    public static final int RECONNECT = 21; //ReconnectPacket
    public static final int PING = 22; //PingPacket
    public static final int PONG = 23; //PongPacket
    public static final int MAPINFO = 24; //MapInfoPacket
    public static final int LOAD = 25; //LoadPacket
    public static final int PIC = 26; //PicPacket
    public static final int SETCONDITION = 27; //SetConditionPacket
    public static final int TELEPORT = 28; //TeleportPacket
    public static final int USEPORTAL = 30; //UsePortalPacket
    public static final int DEATH = 31; //DeathPacket
    public static final int UNKNOWN_32 = 32; // UNKNOWN5
    public static final int BUY = 33; //BuyPacket
    public static final int BUYRESULT = 34; //BuyResultPacket
    public static final int AOE = 35; //AOEPacket
    public static final int GROUNDDAMAGE = 36; //GroundDamagePacket
    public static final int PLAYERHIT = 37; //PlayerHitPacket
    public static final int ENEMYHIT = 38; //EnemyHitPacket
    public static final int AOEACK = 39; //AOEAckPacket
    public static final int SHOOTACK = 40; //ShootAckPacket
    public static final int OTHERHIT = 41; //OtherHitPacket
    public static final int SQUAREHIT = 42; //SquareHitPacket
    public static final int GOTOACK = 44; //GotoAckPacket
    public static final int EDITACCOUNTLIST = 45; //EditAccountListPacket
    public static final int ACCOUNTLIST = 46; //AccountListPacket
    public static final int QUESTOBJID = 47; //QuestObjIDPacket
    public static final int CHOOSENAME = 48; //ChooseNamePacket
    public static final int NAMERESULT = 49; //NameResultPacket
    public static final int CREATEGUILD = 50; //CreateGuildPacket
    public static final int CREATEGUILDRESULT = 51; //CreateGuildResult
    public static final int GUILDREMOVE = 52; //GuildRemovePacket
    public static final int GUILDINVITE = 53; //GuildInvitePacket
    public static final int ALLYSHOOT = 55; //AllyShootPacket
    public static final int ENEMYSHOOT = 56; //EnemyShootPacket
    public static final int REQUESTTRADE = 57; //RequestTradePacket
    public static final int TRADEREQUESTED = 58; //TradeRequestedPacket
    public static final int TRADESTART = 59; //TradeStartPacket
    public static final int CHANGETRADE = 60; //ChangeTradePacket
    public static final int TRADECHANGED = 61; //TradeChangedPacket
    public static final int ACCEPTTRADE = 62; //AcceptTradePacket
    public static final int CANCELTRADE = 63; //CancelTradePacket
    public static final int TRADEDONE = 64; //TradeDonePacket
    public static final int TRADEACCEPTED = 65; //TradeAcceptedPacket
    public static final int CLIENTSTAT = 66; //ClientStatPacket
    public static final int CHECKCREDITS = 67; //CheckCreditsPacket
    public static final int ESCAPE = 68; //EscapePacket
    public static final int FILE = 69; //FilePacket
    public static final int INVITEDTOGUILD = 74; //InvitedToGuildPacket
    public static final int JOINGUILD = 75; //JoinGuildPacket
    public static final int CHANGEGUILDRANK = 76; //ChangeGuildRankPacket
    public static final int PLAYSOUND = 77; //PlaySoundPacket
    
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
