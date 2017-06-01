/**
* Copyright (c) Kyle Fricilone
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.openrs.cache.type;

import net.openrs.cache.Cache;
import net.openrs.cache.type.areas.AreaType;
import net.openrs.cache.type.areas.AreaTypeList;
import net.openrs.cache.type.enums.EnumType;
import net.openrs.cache.type.enums.EnumTypeList;
import net.openrs.cache.type.hitbars.HitBarType;
import net.openrs.cache.type.hitbars.HitBarTypeList;
import net.openrs.cache.type.hitmarks.HitMarkType;
import net.openrs.cache.type.hitmarks.HitMarkTypeList;
import net.openrs.cache.type.identkits.IdentkitType;
import net.openrs.cache.type.identkits.IdentkitTypeList;
import net.openrs.cache.type.invs.InvType;
import net.openrs.cache.type.invs.InvTypeList;
import net.openrs.cache.type.items.ItemType;
import net.openrs.cache.type.items.ItemTypeList;
import net.openrs.cache.type.npcs.NpcType;
import net.openrs.cache.type.npcs.NpcTypeList;
import net.openrs.cache.type.objects.ObjectType;
import net.openrs.cache.type.objects.ObjectTypeList;
import net.openrs.cache.type.overlays.OverlayType;
import net.openrs.cache.type.overlays.OverlayTypeList;
import net.openrs.cache.type.params.ParamTypeList;
import net.openrs.cache.type.params.ParamType;
import net.openrs.cache.type.sequences.SequenceType;
import net.openrs.cache.type.sequences.SequenceTypeList;
import net.openrs.cache.type.spotanims.SpotAnimType;
import net.openrs.cache.type.spotanims.SpotAnimTypeList;
import net.openrs.cache.type.structs.StructType;
import net.openrs.cache.type.structs.StructTypeList;
import net.openrs.cache.type.underlays.UnderlayType;
import net.openrs.cache.type.underlays.UnderlayTypeList;
import net.openrs.cache.type.varbits.VarBitType;
import net.openrs.cache.type.varbits.VarBitTypeList;
import net.openrs.cache.type.varclients.VarClientType;
import net.openrs.cache.type.varclients.VarClientTypeList;
import net.openrs.cache.type.varclientstrings.VarClientStringType;
import net.openrs.cache.type.varclientstrings.VarClientStringTypeList;
import net.openrs.cache.type.varplayers.VarPlayerType;
import net.openrs.cache.type.varplayers.VarPlayerTypeList;

/**
 * @author Kyle Friz
 * 
 * @since May 26, 2015
 */
public class TypeListManager {

	private static final SpotAnimTypeList spot = new SpotAnimTypeList();
	private static final EnumTypeList enm = new EnumTypeList();
	private static final IdentkitTypeList ident = new IdentkitTypeList();
	private static final InvTypeList inv = new InvTypeList();
	private static final ItemTypeList item = new ItemTypeList();
	private static final NpcTypeList npc = new NpcTypeList();
	private static final ObjectTypeList obj = new ObjectTypeList();
	private static final OverlayTypeList over = new OverlayTypeList();
	private static final SequenceTypeList seq = new SequenceTypeList();
	private static final UnderlayTypeList under = new UnderlayTypeList();
	private static final VarBitTypeList varbit = new VarBitTypeList();
	private static final VarClientTypeList varc = new VarClientTypeList();
	private static final VarClientStringTypeList varcstr = new VarClientStringTypeList();
	private static final VarPlayerTypeList varp = new VarPlayerTypeList();
	private static final AreaTypeList area = new AreaTypeList();
	private static final ParamTypeList param = new ParamTypeList();
	private static final StructTypeList struct = new StructTypeList();
	private static final HitMarkTypeList hitmark = new HitMarkTypeList();
	private static final HitBarTypeList hitbar = new HitBarTypeList();
	
	public static void initialize(Cache cache) {
		enm.initialize(cache);
		ident.initialize(cache);
		inv.initialize(cache);
		item.initialize(cache);
		npc.initialize(cache);
		obj.initialize(cache);
		over.initialize(cache);
		seq.initialize(cache);
		spot.initialize(cache);
		under.initialize(cache);
		varbit.initialize(cache);
		varc.initialize(cache);
		varcstr.initialize(cache);
		varp.initialize(cache);
		area.initialize(cache);
		param.initialize(cache);
		struct.initialize(cache);
		hitmark.initialize(cache);
		hitbar.initialize(cache);
	}
	
	public static final int enumSize() {
		return enm.size();
	}
	
	public static final int identSize() {
		return ident.size();
	}
	
	public static final int invSize() {
		return inv.size();
	}
	
	public static final int itemSize() {
		return item.size();
	}
	
	public static final int npcSize() {
		return npc.size();
	}
	
	public static final int objSize() {
		return obj.size();
	}
	
	public static final int overSize() {
		return over.size();
	}
	
	public static final int seqSize() {
		return seq.size();
	}
	
	public static final int spotSize() {
		return spot.size();
	}
	
	public static final int underSize() {
		return under.size();
	}
	
	public static final int varbitSize() {
		return varbit.size();
	}
	
	public static final int varcSize() {
		return varc.size();
	}
	
	public static final int varcstrSize() {
		return varcstr.size();
	}
	
	public static final int varpSize() {
		return varp.size();
	}
	
	public static final int areaSize() {
		return area.size();
	}
	
	public static final int paramSize() {
		return param.size();
	}
	
	public static final int structSize() {
		return struct.size();
	}
	
	public static final int hitmarkSize() {
		return hitmark.size();
	}
	
	public static final int hitbarSize() {
		return hitbar.size();
	}

	public static final EnumType lookupEnum(int id) {
		return enm.list(id);
	}

	public static final IdentkitType lookupIdentkit(int id) {
		return ident.list(id);
	}

	public static final InvType lookupInv(int id) {
		return inv.list(id);
	}

	public static final ItemType lookupItem(int id) {
		return item.list(id);
	}

	public static final NpcType lookupNpc(int id) {
		return npc.list(id);
	}

	public static final ObjectType lookupObject(int id) {
		return obj.list(id);
	}

	public static final OverlayType lookupOver(int id) {
		return over.list(id);
	}

	public static final SequenceType lookupSequence(int id) {
		return seq.list(id);
	}

	public static final SpotAnimType lookupSpotAnim(int id) {
		return spot.list(id);
	}

	public static final UnderlayType lookupUnder(int id) {
		return under.list(id);
	}

	public static final VarBitType lookupVarBit(int id) {
		return varbit.list(id);
	}

	public static final VarClientType lookupVarClient(int id) {
		return varc.list(id);
	}

	public static final VarClientStringType lookupVarClientString(int id) {
		return varcstr.list(id);
	}

	public static final VarPlayerType lookupVarPlayer(int id) {
		return varp.list(id);
	}

	public static final AreaType lookupArea(int id) {
		return area.list(id);
	}
	
	public static final ParamType lookupParam(int id) {
		return param.list(id);
	}
	
	public static final StructType lookupStruct(int id) {
		return struct.list(id);
	}
	
	public static final HitMarkType lookupHitMark(int id) {
		return hitmark.list(id);
	}
	
	public static final HitBarType lookupHitBar(int id) {
		return hitbar.list(id);
	}
	
	public static void print() {
		item.print();
		ident.print();
		npc.print();
		varp.print();
		varbit.print();
		varc.print();
		varcstr.print();
		under.print();
		over.print();
		enm.print();
		obj.print();
		spot.print();
		seq.print();
		inv.print();
		area.print();
		param.print();
		struct.print();
		hitmark.print();
		hitbar.print();
	}

}
