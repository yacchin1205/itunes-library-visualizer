package net.yzwlab.daap.impl;

import org.tunesremote.daap.Paired;
import org.tunesremote.daap.PairingServer;

/**
 * AndroidのHandlerの代用インターフェイス。
 */
public interface PairingListener {

	/**
	 * ペアリングされたことを通知します。
	 * 
	 * @param source
	 *            発生元。nullは不可。
	 * @param paired
	 *            ペアリングされた情報。nullは不可。
	 */
	public void paired(PairingServer source, Paired paired);

}
