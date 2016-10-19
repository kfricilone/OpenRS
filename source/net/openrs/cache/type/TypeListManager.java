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
import net.openrs.cache.type.enums.EnumType;
import net.openrs.cache.type.enums.EnumTypeList;
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
import net.openrs.cache.type.sequences.SequenceType;
import net.openrs.cache.type.sequences.SequenceTypeList;
import net.openrs.cache.type.spotanims.SpotAnimType;
import net.openrs.cache.type.spotanims.SpotAnimTypeList;
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
	}

}
