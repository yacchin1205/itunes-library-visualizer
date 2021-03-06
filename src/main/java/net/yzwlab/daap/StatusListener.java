package net.yzwlab.daap;

/**
 * 状態の監視用インタフェースです。
 */
public interface StatusListener {

	/**
	 * 状態の更新を検出します。
	 * 
	 * @param source
	 *            検出元。nullは不可。
	 * @param updated
	 *            更新された状態。nullは不可。
	 */
	public void statusUpdated(LibrarySession source, StatusUpdate updated);

}
