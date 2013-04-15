package net.yzwlab.daap;

import java.io.IOException;

/**
 * RemoteとおはなしするためのZeroConfサービス用のインタフェースを定義します。
 */
public interface LibraryService {

	/**
	 * 開始します。
	 * 
	 * @throws IOException
	 *             入出力関係のエラー。
	 */
	public void start() throws IOException;

	/**
	 * 破棄します。
	 */
	public void dispose();

	/**
	 * リスナを追加します。
	 * 
	 * @param l
	 *            リスナ。nullは不可。
	 */
	public void addAccessCodeListener(AccessCodeListener l);

	/**
	 * リスナを削除します。
	 * 
	 * @param l
	 *            リスナ。nullは不可。
	 */
	public void removeAccessCodeListener(AccessCodeListener l);

	/**
	 * ペアリングを開始します。
	 * 
	 * @param libraryName
	 *            ライブラリ名。nullは不可。
	 */
	public PairingStatus startPairing(String libraryName);

	/**
	 * セッションを開きます。
	 * 
	 * @param target
	 *            対象セッション。nullは不可。
	 * @return セッション。
	 * @throws IOException
	 *             入出力関係のエラー。
	 */
	public LibrarySession openSession(AccessCode target) throws IOException;

}
